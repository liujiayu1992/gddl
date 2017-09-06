package com.zhiren.jt.het.hetcx2;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：王磊
 * 时间：2009-07-23 16：19
 * 描述：增加参数查看分公司合同
 */
public class Hetcx extends BasePage {
private String check="false";
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public static final String Htlx_FGS = "fgs";
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
	public String getLeix() {
		return ((Visit)this.getPage().getVisit()).getString10();
	}
	public void setLeix(String leix) {
		((Visit)this.getPage().getVisit()).setString10(leix);
	}
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	private String mstrReportName = "";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetcx();
		//} else {
		//	return "无此报表";getTreeScript
		//}
	}
	private String getHetcx() {
		String shenhWhere="";
		if(getweizSelectValue().getId()==-1){//全部
			shenhWhere="";
		}else if(getweizSelectValue().getId()==0){//未审核
			shenhWhere=" and hetb.liucztb_id=0 ";
		}else{//已审核
			shenhWhere=" and hetb.liucztb_id=1 ";
		}
		JDBCcon con = new JDBCcon();
		String sql="";
		//修改有效日期，增加签订日期和有效日期的选择，所以条件也会发生改变，条件是签订日期查询，另一个是有效日期查询，所以日期要去掉一对去除
		//    有效日期的checkbox，要修改页面和java类
		if("有效日期".equals(getLeiSelectValue().getValue())){//按有效期查询
			sql = "select max(vwdianc.fgsid)id_gongs,\n" +
			"decode(grouping(vwdianc.fgsmc),1,100,decode(grouping(vwdianc.mingc),1,max(vwdianc.fgsid),max(vwdianc.id)))id_dianc,\n" + 
			"vwdianc.fgsmc,\n" + 
			"decode(grouping(vwdianc.fgsmc),1,'合计',decode(grouping(vwdianc.mingc),1,vwdianc.fgsmc,vwdianc.mingc))danwmc,\n" + 
			"decode(grouping(jihkjb.mingc),1,decode(grouping(vwdianc.mingc),0,'小计'),jihkjb.mingc)jihkj,\n" + 
			"count(distinct hetb.id)tiaos,sum(nvl(hetslb.hetl,0))/10000 shul\n" + 
			"from hetb,vwdianc,jihkjb,hetslb\n" + 
			"where hetb.jihkjb_id=jihkjb.id\n" + 
			"and hetslb.hetb_id(+)=hetb.id\n" + 
			"and hetb.diancxxb_id=vwdianc.id \n" + 
			"and hetb.diancxxb_id in(\n" + 
			"select id\n" + 
			" from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by  fuid= prior id\n" + 
			")\n" +
			shenhWhere+
			"and ((to_char(hetb.qisrq,'yyyy-mm-dd')>='"+getBeginriqDate1()+"'  and to_char(hetb.qisrq,'yyyy-mm-dd')<='"+getEndriqDate1()+"')\n" + 
			"or (to_char(hetb.guoqrq,'yyyy-mm-dd')<='"+getEndriqDate1()+"'  and to_char(hetb.guoqrq,'yyyy-mm-dd')>='"+getBeginriqDate1()+"'))\n" + 
			
			"group by rollup( vwdianc.fgsmc,vwdianc.mingc,jihkjb.mingc)\n" + 
			"order by grouping(vwdianc.fgsmc)desc,max(vwdianc.fgsxh),\n" + 
			"         grouping(vwdianc.mingc)desc,max(vwdianc.xuh),\n" + 
			"         grouping(jihkjb.mingc)desc,jihkjb.mingc";
		}else{
			sql = "select max(vwdianc.fgsid)id_gongs,\n" +
			"decode(grouping(vwdianc.fgsmc),1,100,decode(grouping(vwdianc.mingc),1,max(vwdianc.fgsid),max(vwdianc.id)))id_dianc,\n" + 
			"vwdianc.fgsmc,\n" + 
			"decode(grouping(vwdianc.fgsmc),1,'合计',decode(grouping(vwdianc.mingc),1,vwdianc.fgsmc,vwdianc.mingc))danwmc,\n" + 
			"decode(grouping(jihkjb.mingc),1,decode(grouping(vwdianc.mingc),0,'小计'),jihkjb.mingc)jihkj,\n" + 
			"count(distinct hetb.id)tiaos,sum(nvl(hetslb.hetl,0))/10000 shul\n" + 
			"from hetb,vwdianc,jihkjb,hetslb\n" + 
			"where hetb.jihkjb_id=jihkjb.id\n" + 
			"and hetslb.hetb_id(+)=hetb.id\n" + 
			"and hetb.diancxxb_id=vwdianc.id \n" + 
			"and hetb.diancxxb_id in(\n" + 
			"select id\n" + 
			" from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by  fuid= prior id\n" + 
			")\n" +
			shenhWhere+
			"and to_char(hetb.qiandrq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n" + 
			"and to_char(hetb.qiandrq,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n" + 
			"group by rollup( vwdianc.fgsmc,vwdianc.mingc,jihkjb.mingc)\n" + 
			"order by grouping(vwdianc.fgsmc)desc,max(vwdianc.fgsxh),\n" + 
			"         grouping(vwdianc.mingc)desc,max(vwdianc.xuh),\n" + 
			"         grouping(jihkjb.mingc)desc,jihkjb.mingc";
		}
		long leib=getLeibSelectValue().getId();
//		System.out.println(leib);
		StringBuffer bu = new StringBuffer("");
		if(leib!=(-1)){
//			System.out.println(leib);
			bu.append(" and hetb.leib="+leib);
		}
		StringBuffer buffer = new StringBuffer("");
		List list=new ArrayList();
		if(getLeix().equals(Htlx_FGS)){
			if("有效日期".equals(getLeiSelectValue().getValue())){//按有效期查询
				sql = "select max(d.id) id_gongs,\n" +
				"       max(d.id) id_dianc,\n" + 
				"       d.mingc danwmc,\n" + 
				"       d.mingc danwmc,\n" + 
				"       decode(d.mingc,'','总计',decode(jihkjb.mingc,'','小计',jihkjb.mingc)) jihkj,\n" + 
				"       count(distinct hetb.id) tiaos,\n" + 
				"       sum(nvl(hetslb.hetl, 0)) / 10000 shul\n" + 
				"  from hetb, diancxxb d, jihkjb, hetslb\n" + 
				" where hetb.jihkjb_id = jihkjb.id\n" + 
				"   and hetslb.hetb_id(+) = hetb.id\n" + 
				shenhWhere+
				" and hetb.diancxxb_id = d.id \n" +
				" and (hetb.diancxxb_id = " + getTreeid() +
				"or d.fuid=" + getTreeid() +")"
                +bu.toString()+
				" and (" +
//				"(to_char(hetb.qisrq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'  and to_char(hetb.qisrq,'yyyy-mm-dd')<='"+getEndriqDate()+"')\n" + 
				"  (to_char(hetb.guoqrq,'yyyy-mm-dd')<='"+getEndriqDate()+"'  and to_char(hetb.guoqrq,'yyyy-mm-dd')>='"+getBeginriqDate()+"')" +
						")\n" + 
				" group by rollup(d.mingc, jihkjb.mingc)\n" + 
				" order by grouping(d.mingc) desc,\n" + 
				"          max(d.xuh),\n" + 
				"          grouping(jihkjb.mingc) desc,\n" + 
				"          jihkjb.mingc";
			}else{
				sql = "select max(d.id) id_gongs,\n" +
				"       max(d.id) id_dianc,\n" + 
				"       d.mingc danwmc,\n" + 
				"       d.mingc danwmc,\n" + 
				"      decode(d.mingc,'','总计',decode(jihkjb.mingc,'','小计',jihkjb.mingc)) jihkj,\n" + 
				"       count(distinct hetb.id) tiaos,\n" + 
				"       sum(nvl(hetslb.hetl, 0)) / 10000 shul\n" + 
				"  from hetb, diancxxb d, jihkjb, hetslb\n" + 
				" where hetb.jihkjb_id = jihkjb.id\n" + 
				"   and hetslb.hetb_id(+) = hetb.id\n" + 
				shenhWhere+
				"and hetb.diancxxb_id = d.id \n" +
				"and (hetb.diancxxb_id = " + getTreeid() +
				"or d.fuid=" + getTreeid() +")"  +bu.toString()+
				"and to_char(hetb.qiandrq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n" + 
				"and to_char(hetb.qiandrq,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n" + 
				" group by rollup(d.mingc, jihkjb.mingc)\n" + 
				" order by grouping(d.mingc) desc,\n" + 
				"          max(d.xuh),\n" + 
				"          grouping(jihkjb.mingc) desc,\n" + 
				"          jihkjb.mingc";
			}
			

		}
		buffer.append(sql);
//System.out.println(sql);
//System.out.println(getLeibSelectValue()+"@#");
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try{
			while(rs.next()){
				list.add(new Hetcxbean(
					rs.getString("id_dianc"),
					rs.getString("danwmc"),
					rs.getString("jihkj"),
					rs.getString("tiaos"),
					rs.getString("shul")
				));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][4];
		ArrWidth = new int[] { 250, 100, 150,150};
		ArrHeader[0] = new String[] { "名称", "计划口径", "合同数目","合同量(万吨)"};
		rt.setBody(new Table(list.size()+1, 4));
		rt.body.setHeaderData(ArrHeader);
		if("有效日期".equals(getLeiSelectValue().getValue())){//按有效期查询
			for(int i=2;i<=list.size()+1;i++){
				Hetcxbean bean=(Hetcxbean)list.get(i-2);
				//rt.body.setCellValue(i, 1, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+bean.getId()+">"+bean.getHetbh()+"</a>");
				rt.body.setCellValue(i, 1,bean.getDanw());
				rt.body.setCellValue(i, 2, bean.getJihkjmc());
				if(bean.getJihkjmc().equals("小计")||bean.getJihkjmc().equals("总计")){
					rt.body.setCellValue(i, 3, ""
							+bean.getHetsm()+"");
				}else{
				rt.body.setCellValue(i, 3, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Hetcx2_1&diancxxb_id="+bean.getId()
				+"&jihkjmc="+bean.getJihkjmc()
				+"&riq3="+getBeginriqDate()
				+"&riq4="+getEndriqDate()
				+"&liucztb_id="+getweizSelectValue().getId()
				+"&leib="+leib
				+"&youx= true"
				+">"+bean.getHetsm()+"</a>");
				}
				rt.body.setCellValue(i, 4, bean.getHetl());
				
				//http://localhost:8086/zgdt/app?
					//service=page/Hetcx2_1&diancxxb_id=102&jihkjmc=市场采购&riq3=2009-05-01&riq4=2010-05-31&liucztb_id=-1&leib=-1&youx=false
			}
		}else{
			for(int i=2;i<=list.size()+1;i++){
				Hetcxbean bean=(Hetcxbean)list.get(i-2);
				//rt.body.setCellValue(i, 1, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+bean.getId()+">"+bean.getHetbh()+"</a>");
				rt.body.setCellValue(i, 1,bean.getDanw());
				rt.body.setCellValue(i, 2, bean.getJihkjmc());
				if(bean.getJihkjmc().equals("小计")||bean.getJihkjmc().equals("总计")){
					rt.body.setCellValue(i, 3, ""
							+bean.getHetsm()+"");
				}else{
				rt.body.setCellValue(i, 3, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Hetcx2_1&diancxxb_id="+bean.getId()
				+"&jihkjmc="+bean.getJihkjmc()
				+"&riq1="+getBeginriqDate()
				+"&riq2="+getEndriqDate()
				+"&liucztb_id="+getweizSelectValue().getId()
				+">"+bean.getHetsm()+"</a>");
				}
				rt.body.setCellValue(i, 4, bean.getHetl());
			}
		}
		
		rt.setTitle("合   同   查   询", ArrWidth);
//		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.setDefaultTitle(1, 3, "检验日期:" +"",Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		//rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		rt.body.mergeCol(1);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	
	}
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		需方
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		//签订日期
//		tb1.addText(new ToolbarText("签订日期:"));
		ComboBox cbp = new ComboBox();
		cbp.setTransform("LeiSelect");
		cbp.setWidth(100);
		cbp.setId("LeiSelect");
		cbp.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(cbp);
		
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
//		有效日期
//		Checkbox cb=new Checkbox();
////		 cb.setText("导入进入流程");
//		cb.setId("Iscb");
////		cb.setBoxLabel("进入流程");
//		if(this.getCheck().equals("true")){
//		cb.setChecked(true);
//		}
//		cb.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true'}else{document.all.CHECKED.value='false'}}");
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("有效日期:"));
//		DateField df2 = new DateField();
//		df2.setValue(this.getBeginriqDate1());
//		df2.Binding("qiandrq3","");// 与html页中的id绑定,并自动刷新
//		df2.setWidth(80);
//		tb1.addField(df2);
//		
//		DateField df3 = new DateField();
//		df3.setValue(this.getEndriqDate1());
//		df3.Binding("qiandrq4","");// 与html页中的id绑定,并自动刷新
//		df3.setWidth(80);
//		tb1.addField(df3);
		
		tb1.addText(new ToolbarText("状态:"));
		ComboBox comb2=new ComboBox();
		comb2.setId("zhuangt");
		comb2.setWidth(100);
		comb2.setTransform("weizSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		tb1.addField(comb2);
//		
		tb1.addText(new ToolbarText("合同类别:"));
		ComboBox comb3=new ComboBox();
		comb3.setId("leibSelect");
		comb3.setWidth(100);
		comb3.setTransform("leibSelect");
		comb3.setLazyRender(true);//动态绑定weizSelect
		tb1.addField(comb3);
		
		ToolbarButton tbb = new ToolbarButton(null,"刷新","function(){document.forms[0].submit()}");
		tb1.addItem(tbb);
		
		setToolbar(tb1);
	}
	
    //类型
    public IDropDownBean getLeiSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getLeiSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }
    public void setLeiSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
//	    		((Visit) getPage().getVisit()).setboolean4(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setboolean4(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean6(Value);
//    	}
    }
    public void setLeiSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getLeiSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getLeiSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
    //			visit.setDropDownBean4(null);
	// visit.setProSelectionModel4(null);
    public void getLeiSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"签订日期"));
        list.add(new IDropDownBean(2,"有效日期"));
//        list.add(new IDropDownBean(3,"分厂"));
//        list.add(new IDropDownBean(4,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list)) ;
        return ;
    }
    
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		visit.setboolean2(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		String lx = cycle.getRequestContext().getParameter("htlx");
		if(lx != null) {
			setLeix(lx);
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDefaultTree(null);
			visit.setInt1(2);
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setString5("");
			visit.setString6("");
			visit.setString7("");
			
			setTreeid(null);
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj())); 
			this.setBeginriqDate1(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate1(DateUtil.FormatDate(visit.getMorjssj())); 
			if(lx == null && !"Hetcx2_1".equals(visit.getActivePageName().toString())) {
				setLeix("");
			}
		}
		
		getToolbars();
		blnIsBegin = true;
	}
//	位置
    public IDropDownBean getLeibSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getLeibSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
 
    public void setLeibSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean5()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean5().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
    	}
    }
    public void setLeibSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getLeibSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getLeibSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }

    public void getLeibSelectModels() {
        List list=new ArrayList();
//        合同类别（0电厂与煤矿(电厂与第三方)，1电厂与分公司(第三方与分公司)，2分公司采购合同）

        list.add(new IDropDownBean(-1,"全部"));
        list.add(new IDropDownBean(0,"电厂与第三方"));
        list.add(new IDropDownBean(1,"第三方与分公司"));
        list.add(new IDropDownBean(2,"分公司采购合同"));
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list)) ;
        return ;
    }

//	位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(-1,"全部"));
        list.add(new IDropDownBean(0,"未审核"));
        list.add(new IDropDownBean(1,"已审核"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
 
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(_value);
	}
	    
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
				stra.set(DateUtil.getYear(new Date()), 0, 1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	//有效日期
	public String getBeginriqDate1(){
		if(((Visit) getPage().getVisit()).getString6()==null||((Visit) getPage().getVisit()).getString6()==""){
				Calendar stra=Calendar.getInstance();
				stra.set(DateUtil.getYear(new Date()), 0, 1);
				((Visit) getPage().getVisit()).setString6(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString6();
	}
	public void setBeginriqDate1(String value){
		((Visit) getPage().getVisit()).setString6(value);
	}
	public String getEndriqDate1(){
		if(((Visit) getPage().getVisit()).getString7()==null||((Visit) getPage().getVisit()).getString7()==""){
			((Visit) getPage().getVisit()).setString7(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString7();
	}
	public void setEndriqDate1(String value){
		((Visit) getPage().getVisit()).setString7(value);
	}

	 //ext
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
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
		
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
