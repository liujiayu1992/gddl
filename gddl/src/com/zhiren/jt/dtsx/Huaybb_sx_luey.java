package com.zhiren.jt.dtsx;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 *略阳电厂专用报表,含来煤质量和入炉煤质
 *（各厂来煤热值情况）
 *资源名配置：Huaybb_sx_luey&lx=zhiltz
 */
public class Huaybb_sx_luey extends BasePage {
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
	private String mstrReportName = "";
	private String QICHCX_ALL="zhiltz";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
if (mstrReportName.equals(QICHCX_ALL)) {
		return getZhiltz();
	}else {
		return "无此报表";
	}
		
	}


	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		String yunsdwsql="";
		String meicsql="";
		String xiecfsql="";
		String chephsql="";
		String gmsql="";
//        if(!getLeixSelectValue().getValue().equals("全部")){
//        	yunsdwsql="and c.yunsdwb_id="+getLeixSelectValue().getId()+"\n";
//        }
////        if(!getDiancmcValue().getValue().equals("全部")){
////        	xiecfsql="and c.xiecfsb_id="+getDiancmcValue().getId()+"\n";
////        }
//        if(!getGongysDropDownValue().getValue().equals("全部")){
//        	meicsql="and f.yunsfsb_id="+getGongysDropDownValue().getStrId()+"\n";
//        }
////   //     System.out.println(getCheph());
////        if(getCheph()!=null&&!getCheph().equals("")){
////        	chephsql="and c.cheph like '%"+_cheph+"%'\n";
////        }
//   //     System.out.println(getTreeid());
//        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
//        	gmsql="and (f.gongysb_id="+getTreeid()+" or f.meikxxb_id="+getTreeid()+" )\n";
//        }

		buffer.append(
				"select fhdw,\n" +
				"       mkdw,\n" + 
				"       jingz,\n" + 
				"       mt,\n" + 
				"       mad,\n" + 
				"       aad,\n" + 
				"       ad,\n" + 
				"       aar,\n" + 
				"       vad,\n" + 
				"       var,\n" + 
				"       vdaf,\n" + 
				"       stad,\n" + 
				"       farl * 1000\n" + 
				"  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n" + 
				"               decode(grouping(g.mingc) + grouping(m.mingc),\n" + 
				"                      1,\n" + 
				"                      '合计',\n" + 
				"                      m.mingc) mkdw,\n" + 
				"               sum(round_new(f.laimsl, 2)) jingz,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.mt, 1) *\n" + 
				"                                    round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                1)) as mt,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.mad * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as mad,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aad * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as aad,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.ad * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as ad,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aar * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as aar,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vad * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as vad,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vdaf * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as vdaf,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.var * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as var,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qnet_ar, 2) *\n" + 
				"                                    round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as farl,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.stad * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as stad,\n" + 
				"               decode(sum(round_new(f.laimsl, 2)),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.qgrd * round_new(f.laimsl, 2)) /\n" + 
				"                                sum(round_new(f.laimsl, 2)),\n" + 
				"                                2)) as qgrd,\n" + 
				"               grouping(g.mingc) dx,\n" + 
				"               grouping(m.mingc) mx\n" + 
				"          from fahb f, gongysb g, meikxxb m, zhilb z\n" + 
				"         where f.gongysb_id = g.id\n" + 
				"           and f.meikxxb_id = m.id\n" + 
				"           and f.zhilb_id = z.id\n" + 
				"           and f.daohrq >= to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd')\n" + 
				"           and f.daohrq <= to_date('"+getEndriqDate()+"', 'yyyy-mm-dd')\n" + 
				"         group by rollup(g.mingc, m.mingc)\n" + 
				"        having not grouping(g.mingc) = 0 or not grouping(m.mingc) = 1\n" + 
				"         order by dx desc, g.mingc, mx desc, m.mingc)\n" + 
				"union\n" + 
				"select '炉前煤' as fhdw,\n" + 
				"       '' as mkdw,\n" + 
				"       (sum(m.fadhy + m.gongrhy)) jingz,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(round_new(r.mt, 1) * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        1)) as mt,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.mad * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as mad,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.aad * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as aad,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.ad * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as ad,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.aar * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as aar,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.vad * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as vad,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.var * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as var,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.vdaf * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as vdaf,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(r.stad * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) as stad,\n" + 
				"       decode(sum(m.fadhy + m.gongrhy),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(round_new(r.qnet_ar, 2) * (m.fadhy + m.gongrhy)) /\n" + 
				"                        sum(m.fadhy + m.gongrhy),\n" + 
				"                        2)) * 1000 as farl\n" + 
				"\n" + 
				"  from rulmzlb r, meihyb m\n" + 
				" where m.rulmzlb_id = r.id\n" + 
				"           and m.rulrq >= to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd')\n" + 
				"           and m.rulrq <= to_date('"+getEndriqDate()+"', 'yyyy-mm-dd')\n");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		//System.out.println(buffer);

		String[][] ArrHeader = new String[1][13];

		ArrHeader[0] = new String[] { "发货单位", "煤矿单位",
				"检质数<br>量(吨)","全水<br>分<br>(%)Mt",
				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
				"空气<br>干燥<br>基灰<br>分<br>(%)Aad",
				"干<br>燥基灰<br>分<br>(%)Ad",
				"收到<br>基<br>灰分<br>(%)Aar", 
				"空气干<br>燥基挥<br>发分<br>(%)Vad",
				"收到<br>基挥<br>发分<br>(%)Var",
				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", 	
				"空气<br>干燥<br>基硫<br>(%)<br>St,ad",
				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar"
				};
		int[] ArrWidth = new int[13];

		ArrWidth = new int[] { 85, 100,  50, 50, 40, 40, 40, 40, 40, 40, 40,
				40, 40};

		rt.setTitle("燃煤验收厂化验汇总表"+"<p>"+v.getDiancmc(), ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 13);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到货日期:"
				+ getBeginriqDate() + "至"
				+getEndriqDate(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00","0.00",""};

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 17; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 3, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

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
		//日期
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","Form0");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("EndTime","Form0");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
//		
//		tb1.addText(new ToolbarText("录入时间:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(160);
//		tb1.addField(cb1);
//		
//		tb1.addText(new ToolbarText("-"));
////		tb1.addText(new ToolbarText("统计口径:"));
////		ComboBox cb1 = new ComboBox();
////		cb1.setTransform("LeixSelect");
////		cb1.setWidth(80);
////		tb1.addField(cb1);
////		tb1.addText(new ToolbarText("-"));
//
		long diancid=visit.getDiancxxb_id();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"diancTree",""+diancid,null,null,null);
		visit.setDefaultTree(dt);
//		
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setValue("请选择煤矿");
//		tf.setWidth(70);
//		//dt.getTree().getSelectedNodeid()
//		//tf.setValue(dt.getTree().getSelectedNodeid());
//		//tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
//				
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("发货单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		
////		tb1.addText(new ToolbarText("供应商:"));
////		ComboBox cb2 = new ComboBox();
////		cb2.setTransform("GongysDropDown");
////		cb2.setEditable(true);
////		tb1.addField(cb2);
//		
//		tb1.addText(new ToolbarText("-"));
//		
////		tb1.addText(new ToolbarText("运输单位:"));
////		ComboBox cb1 = new ComboBox();
////		cb1.setTransform("LeixSelect");
////		cb1.setWidth(80);
////		tb1.addField(cb1);
////		
////		tb1.addText(new ToolbarText("-"));
////		
////		tb1.addText(new ToolbarText("煤场:"));
////		ComboBox cb2 = new ComboBox();
////		cb2.setTransform("GongysDropDown");
////		cb2.setEditable(true);
////		cb2.setWidth(80);
////		tb1.addField(cb2);
////		
////		tb1.addText(new ToolbarText("-"));
//		
//		tb1.addText(new ToolbarText("接卸方式:"));
//		ComboBox cb3 = new ComboBox();
//		cb3.setTransform("DanwSelect");
//		cb3.setEditable(true);
//		cb3.setWidth(80);
//		tb1.addField(cb3);
//		
////		tb1.addText(new ToolbarText("-"));
////		tb1.addText(new ToolbarText("车号:"));
////		TextField tf1 = new TextField();
////		tf1.setId("cheh");
////		tf1.setListeners("change:function(own,newValue,oldValue) {document.getElementById('CHEPH').value = newValue;}");
////		tf1.setWidth(60);
////		tf1.setValue(getCheph());
////		tb1.addField(tf1);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
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
			visit.setDefaultTree(null);
			setTreeid(null);
			setCheph(null);
			setBeginriqDate(DateUtil.FormatDate(new Date()));
			setEndriqDate(DateUtil.FormatDate(new Date()));
		}
		//setTreeid(null);
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}
//		if (riqchange) {
//			riqchange = false;
//			setLeixSelectValue(null);
//			getLeixSelectModels();
//			setTreeid(null);
//			
//		}
		getToolbars();
		blnIsBegin = true;

	}

    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from meicb order by xuh";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    } 
    private boolean riqchange = false;
	// 日期
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		if (((Visit) getPage().getVisit()).getString4()!= null) {
			if (!((Visit) getPage().getVisit()).getString4().equals(value))
				riqchange = true;
		}
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		if (((Visit) getPage().getVisit()).getString5()!= null) {
			if (!((Visit) getPage().getVisit()).getString5().equals(value))
				riqchange = true;
		}
		((Visit) getPage().getVisit()).setString5(value);
	}
   
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);

    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
    	String sql="select rownum,a.shij from(\n"+
    		"select to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss')as shij from chepb c,fahb f\n" +
    		"where to_char(c.lursj,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n" + 
    		"and to_char(c.lursj,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n" + 
    		"and c.fahb_id=f.id\n"+
    		"and f.yunsfsb_id=1\n"+
    		"group by c.lursj\n" + 
    		"order by c.lursj)a";
;
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"请选择")) ;
        return ;
    }

    private String treeid;
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String a) {
		treeid=a;
	}
	
	private String _cheph;
	
	public String getCheph(){
		return _cheph;
	}
	
	public void setCheph(String a){
		_cheph=a;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
    public IDropDownBean getDiancmcValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getDiancmcModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setDiancmcValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);

    }
    public void setDiancmcModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getDiancmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getDiancmcModels() {
    	String sql="select id,mingc from xiecfsb";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"全部")) ;
        return ;
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
