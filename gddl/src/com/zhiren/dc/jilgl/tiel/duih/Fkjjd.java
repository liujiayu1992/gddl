package com.zhiren.dc.jilgl.tiel.duih;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Table;

public class Fkjjd extends BasePage {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
//	日期是否变化
	private boolean riqchange = false;
//	绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq != null){
			if(!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
	
//	衡单下拉框
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getHengdModel().getOptionCount()>0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	public void setHengdModels() {
//		Visit visit = (Visit)this.getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
//		sb.append("select zhi from xitxxb where mingc='过衡单打印时间范围' and leib='数量' and diancxxb_id="+visit.getDiancxxb_id());
//		ResultSetList rsl = con.getResultSetList(sb.toString());
//		String tians = "30";
//		if(rsl.next()) {
//			tians = rsl.getString("zhi");
//		}
//		sb.delete(0, sb.length());
		sb.append("select id,to_char(guohsj,'hh24:mi:ss') guohsj from guohb where to_char(guohsj,'yyyy-mm-dd') = '").append(getRiq()).append("'");
		setHengdModel(new IDropDownModel(sb.toString(),"全部"));
		
		
	}
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
        return ;
    }

//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(visit.isFencb()) {
			tb1.addText(new ToolbarText("厂别:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("检斤时间:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	/*
	 * 设置表眉
	 */
	private void setTableHeader(Paragraph ph, String date, String unit) {
		//ph.addText("<pre>");
		ph.addText("<table align=center width='685'　border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr height=50><td colspan=3></td></tr>");
		ph.addText("<tr><td align='center' colspan=3><font style='font-size:20pt'>");
		ph.addText("<B>轨道衡称重记录</B></font></td></tr>");
		ph.addText("<tr height=10><td colspan=3></td></tr>");
		ph.addText("<tr><td align='left'><font size=2 >制表单位:"
						+ ((Visit)this.getPage().getVisit()).getDiancqc()
						+ "</font></td><td align='center'><font size=2 >"
						+ date
						+"</font></td><td align='right'><font size=2 >");
		ph.addText(unit);
		ph.addText("</font></td></tr></table>");
	}
	
	/*
	 * 设置表尾
	 */
	private void setTableFooter(Paragraph ph,String jianjy) {
		ph.addText("<table valign=top align=center width='685'　border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr><td align='left' width=40%><font size=2>打印日期："
						+ DateUtil.FormatDate(new Date()));
		ph.addText("</font></td><td align='left' width=30%><font size=2>");
		ph.addText("复核：</font></td><td align='left' width=30%><font size=2>");
		ph.addText("检斤员："+jianjy+"</font></td></tr></table>");
	}
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		String gongys = "";
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " and g.id ="+getTreeid();
				}else {
					gongys = " and m.id ="+getTreeid();
				}
			}
		}
		con.Close();
		StringBuffer sb = new StringBuffer("");
		Document _Document = new Document();
		long hengdid = -1;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		
		String ghsj="";
		if(getHengdValue().getValue().equals("全部")) {
			
			ghsj="and c.zhongcsj like to_date('"+getRiq()+"','yyyy-mm-dd ')";
		}else{
			ghsj="and c.guohb_id ="+hengdid;
		}
		//报表时间
		String time = (hengdid > -2) ? getRiq()+" "+getHengdValue().getValue()
				: "1990-01-01 23:00:00";
		//设置检斤员
		sb.append("select distinct zhongcjjy from chepb where guohb_id = ")
			.append(hengdid);
		ResultSetList rsl = con.getResultSetList(sb.toString()); 
		String lury = ""; 
		if (rsl.next()) {
			lury = rsl.getString(0); 
		}
		// 设置明细行SQL
		sb.setLength(0);
		sb.append("select c.xuh,g.mingc fahdw, m.mingc meikdw, c.cheph,\n");
		sb.append("p.mingc pinz, c.biaoz, c.maoz, c.piz, c.maoz-c.piz-c.zongkd jingz, c.zongkd, \n");
		sb.append("c.yuns, c.yingd, c.yingd-c.yingk kuid, to_char(f.fahrq,'yyyy-mm-dd') fahrq, c.ches\n");
		sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p\n");
		sb.append("where f.id = c.fahb_id and f.gongysb_id = g.id\n");
		sb.append("and f.meikxxb_id = m.id and f.pinzb_id = p.id\n");
		sb.append(ghsj).append(" \n");
		sb.append("and f.yunsfsb_id=").append(SysConstant.YUNSFS_HUOY);
		sb.append(gongys);
		sb.append(" order by f.meikxxb_id,c.xuh");
		ResultSet rs = con.getResultSet(sb,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		int EndPageRow = 0;
		try {
			if (rs.next()) {
				Table tb = new Table(rs, 1, 0, 2);
				String[][] ArrHeader = new String[1][15];
				ArrHeader[0] = new String[] { Locale.xuh_chepb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.cheph_chepb, Locale.pinzb_id_fahb,
						Locale.biaoz_chepb, Locale.maoz_chepb, Locale.piz_chepb, Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_chepb, Locale.kuid_chepb,
						Locale.fahrq_fahb, Locale.ches_chepb };
				int[] ArrWidth = new int[15];
				ArrWidth = new int[] { 32, 90, 140, 45, 40, 35, 40, 32, 40, 40, 32, 32, 32, 65, 30 };
				tb.setWidth(ArrWidth);
				tb.setHeaderData(ArrHeader);
				tb.setPageRows(40);
				tb.merge(1,2,tb.getRows(),3,false);
				tb.setColFormat(7,"0.000");
				tb.setColFormat(9,"0.000");
				tb.setColFormat(10,"0.000");
				tb.setColFormat(11,"0.000");
				tb.setColFormat(12,"0.000");
				tb.setColAlign(1, Table.ALIGN_CENTER);

				tb.setColAlign(5, Table.ALIGN_CENTER);
//				数字右对齐
				tb.setColAlign(6, Table.ALIGN_RIGHT);
				tb.setColAlign(7, Table.ALIGN_RIGHT);
				tb.setColAlign(8, Table.ALIGN_RIGHT);
				tb.setColAlign(9, Table.ALIGN_RIGHT);
				tb.setColAlign(10, Table.ALIGN_RIGHT);
				tb.setColAlign(11, Table.ALIGN_RIGHT);
				tb.setColAlign(12, Table.ALIGN_RIGHT);
				tb.setColAlign(13, Table.ALIGN_RIGHT);
				tb.setColAlign(15, Table.ALIGN_RIGHT);
				//设置标题字体
				tb.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_ALIGN,
						Table.ALIGN_CENTER);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_VALIGN,
						Table.VALIGN_TOP);
				tb.setRowHeight(21);
				EndPageRow = (tb.getRows()-1)%40+3;
				setAllPages(tb.getPages());
				for(int iPage=1; iPage <= tb.getPages(); iPage++){
					String display = "";
					String feny = "";
					if(iPage != 1){
						display = "style='display:none'";
						feny = "<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span>";
					}
					Paragraph ph = new Paragraph();
					ph.addText(feny+"<span id='reportpage"+iPage+"' "+display+" >");
					
					setTableHeader(ph,"称重时间："+time,"单位：吨、车、千米/时");
					ph.addText("<center>"+tb.getHtml(iPage));
					if(iPage == tb.getPages()){
						if(EndPageRow != 0 && 41-EndPageRow>0){
							Table total = getTotalTable(true,41-EndPageRow);
							if(total!=null){
								ph.addText("<table><tr height=10><td></td></tr></table>");
								total.setPageRows(41-EndPageRow);
								ph.addText(total.getHtml(1));
							}
						}else{
							EndPageRow = 0;
						}
					}
					ph.addText("</center>");
					setTableFooter(ph,lury);
					ph.addText("</span>");
					_Document.addParagraph(ph);
				}
				
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Table total = null;
		if(EndPageRow == 0){
			total = getTotalTable(false,0);
		}else{
			total = getTotalTable(false,41-EndPageRow);
		}
		if(total != null){
			total.setPageRows(40);
			for(int iPage=1; iPage <= total.getPages(); iPage++){
				Paragraph ph = new Paragraph();
				ph.addText("<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span><span id='reportpage"+(getAllPages()+iPage)+"' style='display:none'>");
				setTableHeader(ph,"称重时间："+time,"单位：吨、车、千米/时");
				ph.addText("<center>"+total.getHtml(iPage)+"</center>");
				setTableFooter(ph,lury);
				ph.addText("</span>");
				_Document.addParagraph(ph);
			}
		}
		con.Close();
		_Document.setTableWidth(0);
		if(_Document.paragraphs.size() > 0) {
			setCurrentPage(1);
			setAllPages(_Document.paragraphs.size());
		}
		return _Document.getHtml();// ph;
	}
	
	private Table getTotalTable(boolean less,int rownum){
		JDBCcon con = new JDBCcon();
		String gongys = "";
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			ResultSetList rsl = con.getResultSetList("select lx from vwgongysmk where id="+getTreeid());
			if(rsl == null) {
				return null;
			}
			if(rsl.next()) {
				if(rsl.getInt("lx")==1) {
					gongys = " and g.id ="+getTreeid();
				}else {
					gongys = " and m.id ="+getTreeid();
				}
			}
		}
		con.Close();

		//long hengdid = getHengdValue().getId();;
		String ghsj="";
		if(getHengdValue().getValue().equals("全部")) {
			
			ghsj=" where chepb.zhongcsj like to_date('"+getRiq()+"','yyyy-mm-dd ')";;
		}else{
			ghsj="where guohb_id ="+getHengdValue().getId();
		}
		
		Table tb1 = null;
		String strrow ="";
		if(less){
			strrow = " where r < "+rownum;
		}else{
			strrow = " where r >="+rownum;
		}
		ResultSet hz;
		StringBuffer sb = new StringBuffer();
		sb.append("select bianm,fahdw,meikdw,pinz,ches,biaoz,jingz,zongkd,yuns,yingd,kuid,yingk,faz from \n");
		sb.append("(select rownum r, s.* from (select decode(y.bianm,null,'合计',y.bianm) bianm, g.mingc fahdw, m.mingc meikdw, p.mingc pinz, \n");
		sb.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz, sum(zongkd) zongkd, \n");
		sb.append("sum(f.yuns) yuns, sum(f.yingd) yingd, sum(f.yingd-f.yingk) kuid, \n");
		sb.append("sum(f.yingk) yingk, c.mingc faz \n");
		sb.append("from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, caiyb y, \n");
		sb.append("(select distinct fahb_id from chepb    ");
		sb.append(ghsj).append(") cp \n");
		sb.append("where f.id = cp.fahb_id and f.gongysb_id = g.id and f.pinzb_id = p.id \n");
		sb.append("and f.meikxxb_id = m.id and f.zhilb_id = y.zhilb_id and f.faz_id = c.id \n");
		sb.append("and f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
		sb.append(gongys);
		sb.append("group by rollup(y.bianm,g.mingc,m.mingc,p.mingc,c.mingc) \n");
		sb.append("having (GROUPING(c.mingc) = 0 OR GROUPING(y.bianm) =1 )) s) \n");
		sb.append(strrow);
		
		hz = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		try {
			if(hz.next()){
				tb1 = new Table(hz, 1, 0, 1);
				String ArrHeaderHZ[][] = new String[1][13];
				ArrHeaderHZ[0] = new String[] { Locale.caiybm_caiyb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb, Locale.ches_fahb,
						Locale.biaoz_fahb, Locale.jingz_fahb, Locale.zongkd_fahb, Locale.yuns_fahb, Locale.yingd_fahb, Locale.kuid_fahb, Locale.yingk_fahb, Locale.faz_id_fahb };
				int[] ArrWidthHz = new int[13];
				ArrWidthHz = new int[] { 55, 95, 140, 40, 40, 40, 50, 40, 40, 40, 40, 50,
						55 };
				tb1.setWidth(ArrWidthHz);
				tb1.setHeaderData(ArrHeaderHZ);
				tb1.setColFormat(7,"0.000");
				tb1.setColFormat(9,"0.000");
				tb1.setColFormat(10,"0.000");
				tb1.setColFormat(11,"0.000");
				tb1.setColAlign(1, Table.ALIGN_CENTER);
				tb1.setColAlign(2, Table.ALIGN_CENTER);
				tb1.setColAlign(3, Table.ALIGN_CENTER);
				tb1.setColAlign(4, Table.ALIGN_CENTER);
				tb1.setColAlign(5, Table.ALIGN_RIGHT);
				tb1.setColAlign(6, Table.ALIGN_RIGHT);
				tb1.setColAlign(7, Table.ALIGN_RIGHT);
				tb1.setColAlign(8, Table.ALIGN_RIGHT);
				tb1.setColAlign(9, Table.ALIGN_RIGHT);
				tb1.setColAlign(10, Table.ALIGN_RIGHT);
				tb1.setColAlign(11, Table.ALIGN_RIGHT);
				tb1.setColAlign(11, Table.ALIGN_RIGHT);
				tb1.setColAlign(13, Table.ALIGN_CENTER);
//				设置标题字体
				tb1.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb1.setRowHeight(21);
				tb1.ShowZero = false;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return tb1;
	}
//	工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
			getSelectData();
		}
		if(riqchange){
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
			setTbmsg(null);
		}
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
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
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
