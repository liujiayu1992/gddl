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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
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
/**
 * 
 * @author huochaoyuan
 *陕西区域专用化验台帐，分矿，分运输方式，分运输单位，分化验状态查询，含单天明细数据；
 *适用于一厂一制电厂，取数方式为：到货日期查询，所有化验数据先规约再计算最后再规约，其中单位（Kcal/Kg）的热值有计算完成的(Mj/kg)单位热值换算所得；
 *资源名配置：Zhiltz_sx&lx=zhiltz
 */

public class Zhiltz_sx extends BasePage {
	private static final String RptKey_Zhilt_zx_ZHILTZ="Zhilt_zx_zhiltz";
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
	private String ZHILTZ="zhiltz";//质量台帐

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
if (mstrReportName.equals(ZHILTZ)) {
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
        if(!getLeixSelectValue().getValue().equals("全部")){
        	yunsdwsql="and c.yunsdwb_id="+getLeixSelectValue().getId()+"\n";
        }
        if(!getDiancmcValue().getValue().equals("全部")){
        	if(getDiancmcValue().getValue().equals("完成")){
        		xiecfsql="and z.id is not null\n";
        	}else xiecfsql="and z.id is null\n";
        }
        if(!getGongysDropDownValue().getValue().equals("全部")){
        	meicsql="and f.yunsfsb_id="+getGongysDropDownValue().getStrId()+"\n";
        }
//   //     System.out.println(getCheph());
//        if(getCheph()!=null&&!getCheph().equals("")){
//        	chephsql="and c.cheph like '%"+_cheph+"%'\n";
//        }
   //     System.out.println(getTreeid());
        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
        	gmsql="and (f.gongysb_id="+getTreeid()+" or f.meikxxb_id="+getTreeid()+" )\n";
        }
/**
 * huochaoyuan 2009-12-28
 * 改进取数sql，添加判断，如果化验值为空，其数量不参与化验值加权计算
 */
// 增加报表配置
			String sql="select fhdw,\n" +
			"       mkdw,\n" + 
			"       ys,\n" + 
			"       daohrq,\n"+
			"       jingz,\n" + 
			"       mt,\n" + 
			"       mad,\n" + 
			"       aad,\n" + 
			"       aar,\n" + 
			"       vad,\n" + 
			"       vdaf,\n" + 
			"       stad,\n" + 
			"       star,\n" + 	
			"       qbad*1000,\n" + 
			"       farl*1000,\n" + 
			"       round_new(farl*1000/4.1816,0) as qbar,\n" + 							
			"       fcad,\n" +
			"		ad,\n"+
			"		had\n"+
			"  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n" + 
			"               decode(grouping(g.mingc) + grouping(m.mingc),\n" + 
			"                      1,\n" + 
			"                      '合计',\n" + 
			"                      m.mingc) mkdw,\n" + 
			"               decode(grouping(g.mingc) + grouping(m.mingc) +\n" + 
			"                      grouping(ys.mingc),\n" + 
			"                      1,\n" + 
			"                      '小计',\n" + 
			"                      ys.mingc) ys,\n" + 
			"               to_char(f.daohrq,'yyyy-mm-dd') as daohrq,\n" + 
			"               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n" + 
			"               sum(f.biaoz) biaoz,\n" + 
			"               sum(f.yuns) yuns,\n" + 
			"               sum(f.yingk) yingk,\n" + 
			"               sum(f.zongkd) zongkd,\n" + 
			"               sum(f.ches) ches,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.mt,null,0,f.laimsl),"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.mad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as mad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.aad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as aad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.ad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as ad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.aar,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as aar,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.vad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as vad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.vdaf,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as vdaf,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.qbad,null,0,f.laimsl),"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n" + 
			"                                          sum(round_new(decode(z.qnet_ar,null,0,f.laimsl),"+v.getShuldec()+"))\n" + 
			"                                           * 1000 / 4.1816,\n" + 
			"                                0)) as qbar,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.qnet_ar,null,0,f.laimsl),"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.sdaf,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as sdaf,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.stad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as stad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.std,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as std,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(decode(z.stad,null,0,f.laimsl),"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.hdaf,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as hdaf,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.had,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as had,\n" + 
			
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.fcad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.fcad,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as fcad,\n" + 
			"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(decode(z.qgrd,null,0,f.laimsl),"+v.getShuldec()+")), 2)) as qgrd,\n" + 
			"               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n" + 
			"               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx,\n" + 
			"               decode(grouping(ys.mingc),1,0,1) yx\n"+
			"          from fahb f, gongysb g, meikxxb m, zhilb z,\n" + 
			"(select distinct ys.mingc as mingc,f.id as id from chepb c,yunsdwb ys,fahb f\n" +
			"          where f.daohrq >= to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd')\n" + 
			"           and f.daohrq <= to_date('"+getEndriqDate()+"', 'yyyy-mm-dd')\n" + 
			"           and c.fahb_id=f.id\n" + yunsdwsql+
			"           and c.yunsdwb_id=ys.id(+)\n" + 
			"           ) ys\n"+
			"         where f.gongysb_id = g.id\n" + 
			"           and f.meikxxb_id = m.id\n" + 
			"           and f.id = ys.id\n" + 
			"           and f.zhilb_id = z.id(+)\n" + gmsql+meicsql+xiecfsql+
			"           and f.daohrq >= to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd')\n" + 
			"           and f.daohrq <= to_date('"+getEndriqDate()+"', 'yyyy-mm-dd')\n" + 
			"         group by rollup(g.mingc, m.mingc, ys.mingc,f.daohrq)\n" + 
			"        having grouping(f.daohrq) = 0 or grouping(ys.mingc) = 1\n" + 
			"         order by dx, fhdw, mx, mkdw,yx,ys, daohrq desc)";
			ResultSetList rstmp = con.getResultSetList(sql);
		
			ResultSetList rs = null;
			String[][] ArrHeader=null;
			String[] strFormat=null;
			int[] ArrWidth=null;
			int aw=0;
			String [] Zidm=null;
			StringBuffer sb=new StringBuffer();
			sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+(RptKey_Zhilt_zx_ZHILTZ+v.getInt1())+"' order by xuh");
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
	        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+(RptKey_Zhilt_zx_ZHILTZ+v.getInt1())+"'");
	        	String Htitle="煤质检验台账" ;
	        	while(rsl.next()){
	        		Htitle=rsl.getString("biaot");
	        	}
	        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString2());//取得报表纸张类型
	    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
	        	rt.setTitle(Htitle, ArrWidth);
	        	rsl.close();
	        }else{
	        	rs = rstmp;
	        	ArrHeader = new String[][] {{Locale.meijy_fahdw,Locale.meijy_meikdw, Locale.meijy_yunsdw, Locale.meijy_daohrq, 
	        		 Locale.meijy_jianzsl,Locale.meijy_quansf,Locale.meijy_kongqgzjsf, Locale.meijy_kongqgzjhf, Locale.meijy_shoudjhf, Locale.meijy_kongqgzjhff,
	    			Locale.meijy_ganzwhjhff, Locale.meijy_kongqgzjl, Locale.meijy_shoudjql, Locale.meijy_dantfrl,Locale.meijy_shoudjdwfrl,Locale.meijy_shoudjdwrz,Locale.meijy_gudt,Locale.meijy_ganzjhf,Locale.meijy_kongqgzq} };
	    
	        	ArrWidth = new int[] {85,80, 80, 80, 80, 65, 60, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45};
	    
	    		aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString2());//取得报表纸张类型
	    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
	    		rt.setTitle("煤质检验台账", ArrWidth);
	        }
			
		    rt.title.setRowCells(2, Table.PER_FONTSIZE, 17);
		    rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
	        rt.title.fontSize=10;
			rt.title.setRowHeight(2, 40);
			rt.setBody(new Table(rs, 1, 0, 3));
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setPageRows(25);
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
	       
			
			
			rt.setDefaultTitle(1, 5, "卸煤日期:"
				+ getBeginriqDate() + "至"
				+getEndriqDate(),
			Table.ALIGN_LEFT);
	    rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 3, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sql,rt,"煤质检验台账",""+RptKey_Zhilt_zx_ZHILTZ+v.getInt1());
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
		df.Binding("BeginTime","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(80);
//		tb1.addField(cb1);
//		tb1.addText(new ToolbarText("-"));

		long diancid=visit.getDiancxxb_id();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"diancTree",""+diancid,null,null,null);
		visit.setDefaultTree(dt);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setValue("请选择煤矿");
		tf.setWidth(70);
		//dt.getTree().getSelectedNodeid()
		//tf.setValue(dt.getTree().getSelectedNodeid());
		//tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
				
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("发货单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
//		tb1.addText(new ToolbarText("供应商:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输单位:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		cb2.setWidth(80);
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
//		
		tb1.addText(new ToolbarText("化验状态:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("DanwSelect");
		cb3.setEditable(true);
		cb3.setWidth(80);
		tb1.addField(cb3);
//		
		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("车号:"));
//		TextField tf1 = new TextField();
//		tf1.setId("cheh");
//		tf1.setListeners("change:function(own,newValue,oldValue) {document.getElementById('CHEPH').value = newValue;}");
//		tf1.setWidth(60);
//		tf1.setValue(getCheph());
//		tb1.addField(tf1);
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
			//设置pagewidth
			visit.setString2(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString2(pagewith);
			}
			else
				visit.setString2("770");
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
			visit.setString4(null);
			getBeginriqDate();
			visit.setString5(null);
			getEndriqDate();
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
    	String sql="select id,mingc from yunsfsb order by id";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    } 

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
    	String sql="select id,mingc from yunsdwb";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部")) ;
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
    	List list=new ArrayList();
        list.add(new IDropDownBean(1,"全部"));
        list.add(new IDropDownBean(2,"完成"));
        list.add(new IDropDownBean(3,"未完成"));
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list)) ;
      return ;
//    	String sql="select id,mingc from xiecfsb";
//     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
//        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"全部")) ;
//        return ;
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
