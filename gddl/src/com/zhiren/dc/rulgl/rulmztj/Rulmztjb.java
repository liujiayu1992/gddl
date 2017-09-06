package com.zhiren.dc.rulgl.rulmztj;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rulmztjb extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRulmzdata();
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
	
	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
	
    //记录机组分组表ID
	public long getJizfzb_id() {
		return getJizfzValue().getId();
	}
	
	public String getSql1(int xuh, String mingc,String biaoz, String name, double b, double l) {
		
		
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc,'"+biaoz+"' as biaoz, formatxiaosws(data1,decode('"+name+"','Mt',1,2)) as data1, formatxiaosws(data2,decode('"+name+"','Mt',1,2)) as data2, formatxiaosws(data3,decode('"+name+"','Mt',1,2)) as data3, data4\n" +
			"  from (select round_new(avg(data1),decode('"+name+"','Mt',1,2)) as data1, round_new(max(data2),decode('"+name+"','Mt',1,2)) as data2, round_new(min(data3),decode('"+name+"','Mt',1,2)) as data3\n" + 
			"          from (select avg("+name+") as data1, max("+name+") as data2, min("+name+") as data3\n" + 
			"                  from rulmzlb\n" + 
			"                 where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                   and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                   and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"                 group by jizfzb_id, rulrq)),\n" + 
			"       (select round_new(decode((select count("+name+")\n" + 
			"                        from rulmzlb\n" + 
			"                       where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                         and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                         and jizfzb_id = "+getJizfzb_id()+"),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      ((select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where "+name+" >= "+b+" - "+l+"\n" + 
			"                           and "+name+" <= "+b+" + "+l+"\n" + 
			"                           and to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+")*100 /\n" + 
			"                      (select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+"))),1) as data4\n" + 
			"          from dual)";
		return sql;
	}
	
	public String getSql2(int xuh, String mingc,String biaoz, String name) {
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc,'"+biaoz+"' as biaoz,  formatxiaosws(round_new(avg(data),2),2) as data1, ''||0 as data2, ''||0 as data3, 0\n" + 
			"  from (select avg("+name+") as data\n" + 
			"          from rulmzlb\n" + 
			"         where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"           and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"         group by jizfzb_id, rulrq)\n";
		return sql;
	}
	
	public String getSql3(int xuh, String mingc, String biaoz, String name, double b) {
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc, '"+biaoz+"' as biaoz,formatxiaosws(data1,2) as data1, formatxiaosws(data2,2) as data2, formatxiaosws(data3,2) as data3, data4\n" +
			"  from (select round_new(avg(data1),2) as data1, round_new(max(data2),2) as data2, round_new(min(data3),2) as data3\n" + 
			"          from (select avg("+name+") as data1, max("+name+") as data2, min("+name+") as data3\n" + 
			"                  from rulmzlb\n" + 
			"                 where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                   and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                   and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"                 group by jizfzb_id, rulrq)),\n" + 
			"       (select round_new(decode((select count("+name+")\n" + 
			"                        from rulmzlb\n" + 
			"                       where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                         and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                         and jizfzb_id = "+getJizfzb_id()+"),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      ((select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where "+name+" < "+b+"\n" + 
			"                           and to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+")*100 /\n" + 
			"                      (select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+"))),1) as data4\n" + 
			"          from dual)";
		return sql;
	}
	
	public String getSql4(int xuh, String mingc,String biaoz, String name) {
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc,'"+biaoz+"' as biaoz,  ''||round_new(avg(data),2)*1000 as data1, ''||0 as data2, ''||0 as data3, 0\n" + 
			"  from (select avg("+name+") as data\n" + 
			"          from rulmzlb\n" + 
			"         where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"           and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"         group by jizfzb_id, rulrq)\n";
		return sql;
	}
	
	public String getSql5(int xuh, String mingc, String biaoz,String name, double b, double l) {
		
		
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc, '"+biaoz+"' as biaoz,''||data1*1000 as data1, ''||data2*1000 as data2, ''||data3*1000 as data3, data4\n" +
			"  from (select round_new(avg(data1),2) as data1, round_new(max(data2),2) as data2, round_new(min(data3),2) as data3\n" + 
			"          from (select avg("+name+") as data1, max("+name+") as data2, min("+name+") as data3\n" + 
			"                  from rulmzlb\n" + 
			"                 where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                   and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                   and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"                 group by jizfzb_id, rulrq)),\n" + 
			"       (select round_new(decode((select count("+name+")\n" + 
			"                        from rulmzlb\n" + 
			"                       where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                         and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                         and jizfzb_id = "+getJizfzb_id()+"),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      ((select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where "+name+" >= "+b+" - "+l+"\n" + 
			"                           and "+name+" <= "+b+" + "+l+"\n" + 
			"                           and to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+")*100 /\n" + 
			"                      (select count("+name+")\n" + 
			"                          from rulmzlb\n" + 
			"                         where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                           and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                           and jizfzb_id = "+getJizfzb_id()+"))),1) as data4\n" + 
			"          from dual)";
		return sql;
	}
	
	public String getSql6(int xuh, String mingc,String biaoz, String name) {
		
		
		String sql = 
			"select "+xuh+" as xuh, '"+mingc+"' as mingc,'"+biaoz+"' as biaoz,  ''||round_new(data1*1000/4.1816,0) as data1, ''||round_new(data2*1000/4.1816,0) as data2, ''||round_new(data3*1000/4.1816,0) as data3, 0\n" +
			"  from (select avg(data1) as data1, max(data2) as data2, min(data3) as data3\n" + 
			"          from (select avg("+name+") as data1, max("+name+") as data2, min("+name+") as data3\n" + 
			"                  from rulmzlb\n" + 
			"                 where to_char(rulrq, 'yyyy') = '"+getNianf()+"'\n" + 
			"                   and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			"                   and jizfzb_id = "+getJizfzb_id()+"\n" + 
			"                 group by jizfzb_id, rulrq))\n";
		return sql;
	}
	
	private String getRulmzdata() {
		JDBCcon con = new JDBCcon();
		String cnDate = getNianf() + "年" + getYuef() + "月份";

		String ArrHeader[][] = new String[1][5];

		String sql = "select count(Mt) as shul from rulmzlb where to_char(rulrq, 'yyyy') = '"+getNianf()+"' and to_char(rulrq, 'mm') = '"+getYuef()+"'\n" + 
			 	"and jizfzb_id = " + getJizfzb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String shul = "";
		if (rsl.next()) {
			shul = rsl.getString("shul");
		}
		ArrHeader[0] = new String[] { "名称","标准值", shul+"份平均值", "最大值", "最小值", "合格率<br>(%)"};

		int ArrWidth[] = new int[] { 150,100, 100, 100, 100, 100};
		String bz_Mar="";
		String bz_Aar="";
		String bz_Vdaf="";
		String bz_Star="";
		String bz_qnert="";
		String bz_null="";
		String bz_daka="";
		double hgl_Mt1=0.0;
		double hgl_Mt2=0.0;
		double hgl_Aar1=0.0;
		double hgl_Aar2=0.0;
		double hgl_Vdaf1=0.0;
		double hgl_Vdaf2=0.0;
		double hgl_Star=0.0;
		double  hgl_qnetar1=0.0;
		double  hgl_qnetar2=0.0;
		if(this.getJizfzValue().getValue().equals("C系统")||getJizfzValue().getValue().equals("B2系统")){
			//B2系统和C系统标准值
			bz_Mar="8.9";
			bz_Aar="19.09";
			bz_Vdaf="7.14";
			bz_Star="0.45";
			bz_qnert="24210";
			bz_daka="5790";
			hgl_Mt1=8.9;
			hgl_Mt2=9.42;
			
			hgl_Aar1=19.09;
			hgl_Aar2=9.64;
			
			hgl_Vdaf1=7.14;
			hgl_Vdaf2=2.04;
			
			hgl_Star=0.45;
			
			hgl_qnetar1=24.210;
			hgl_qnetar2=3.174;
		}else{
			//A系统和B1系统的标准值
			bz_Mar="5.67±3";
			bz_Aar="18.86±4.76";
			bz_Vdaf="7.02±1";
			bz_Star="0.34";
			bz_qnert="25539±1674";
			bz_daka="6107±400";
			//合格率范围
			hgl_Mt1=5.67;
			hgl_Mt2=6;
			
			hgl_Aar1=18.86;
			hgl_Aar2=9.52;
			
			hgl_Vdaf1=7.02;
			hgl_Vdaf2=2.0;
			
			hgl_Star=0.34;
			
			hgl_qnetar1=25.539;
			hgl_qnetar2=3.348;
			
		}
		sql = "select 15 as xuh, '备注' as mingc,'0' as biaoz, ''||0 as data1, ''||0 as data2, ''||0 as data3, 0 from dual";
		StringBuffer buffer = new StringBuffer("select mingc, biaoz,data1, data2, data3, data4 from (");		
	    buffer.append(getSql1(1, "全水分Mar (%)",bz_Mar, "Mt", hgl_Mt1, hgl_Mt2)).append(" union ").append(getSql2(2, "一般分析煤样水Mad(%)","", "Mad"))
	    	.append(" union ").append(getSql2(3, "一般分析煤样灰Aad(%)","", "Aad")).append(" union ").append(getSql1(4, "收到基灰分Aar(%)",bz_Aar, "Aar", hgl_Aar1, hgl_Aar2))
	    	.append(" union ").append(getSql2(5, "一般分析煤样挥Vad(%)","", "Vad")).append(" union ").append(getSql1(6, "干燥无灰基挥发Vdaf(%)",bz_Vdaf, "Vdaf", hgl_Vdaf1, hgl_Vdaf2))
	    	.append(" union ").append(getSql2(7, "固定碳Fc,ad(%)","", "Fcad")).append(" union ").append(getSql2(8, "一般分析煤样全St,ad(%)","", "Stad"))
	    	.append(" union ").append(getSql3(9, "收到基全硫St,ar(%)", bz_Star,"Star", hgl_Star)).append(" union ").append(getSql2(10, "一般分析煤样氢Had(%)","", "Had"))
	    	.append(" union ").append(getSql4(11, "弹筒热值Qb,ad(J/g)","", "Qbad")).append(" union ").append(getSql4(12, "干燥基高位热Qgr,d(J/g)","", "Qgrd"))
	    	.append(" union ").append(getSql5(13, "收到基低位Qnet,ar(J/g)<br><br>约合（卡/克）",bz_qnert, "Qnet_ar", hgl_qnetar1, hgl_qnetar2)).append(" union ")
	    	.append(getSql6(14, "收到基低位Qnet,ar(J/g)<br><br>约合（卡/克）",bz_daka, "Qnet_ar")).append(" union ").append(sql).append(") order by xuh");
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		//System.out.println(buffer.toString());
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 1, 0, 1));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		sql = "select mingc from jizfzb where id = " + getJizfzb_id();
		rsl = con.getResultSetList(sql);
		String mingc = "";
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		Visit visit = (Visit) getPage().getVisit();
		rt.setTitle(visit.getDiancmc()+cnDate + mingc + "入炉煤质统计表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		
		String time = DateUtil.FormatDate(new Date());
		time = time.substring(0,4) + "年" + time.substring(5, 7) + "月" + time.substring(8, 10) + "日";
		rt.setDefaultTitle(5,2,time,Table.ALIGN_CENTER);
		
		rt.body.setPageRows(21);
		
		rt.body.mergeCell(14, 1, 15, 1);
		rt.body.mergeRow(16);
		//设定列对齐
		for (int i = 1; i < 7; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		//设定行高
		for (int i = 1; i < 16; i++) {
			rt.body.setRowHeight(i, 45);
		}
		//rt.body.ShowZero=true;
		
		//如果合格率是0,就显示成0.0,而不是隐藏
		if(rt.body.getCellValue(2, 6).equals("0")){
			rt.body.setCellValue(2, 6, " "+"0.0");
		}
		if(rt.body.getCellValue(5, 6).equals("0")){
			rt.body.setCellValue(5, 6, " "+"0.0");
		}
		if(rt.body.getCellValue(7, 6).equals("0")){
			rt.body.setCellValue(7, 6, " "+"0.0");
		}
		if(rt.body.getCellValue(10, 6).equals("0")){
			rt.body.setCellValue(10, 6, " "+"0.0");
		}
		if(rt.body.getCellValue(14, 6).equals("0")){
			rt.body.setCellValue(14, 6, " "+"0.0");
		}
		
		
		
		rt.body.setRowHeight(16, 130);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "批准:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 1, "报表：梁建芬", Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		 // 年份ComBox
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setId("NIANF");// 和自动刷新绑定
		nianf.setLazyRender(true);
		nianf.setEditable(false);
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("机组分组:"));
		ComboBox jizfz = new ComboBox();
		jizfz.setTransform("JIZFZB_ID");
		jizfz.setWidth(80);
		jizfz.setListeners("select:function(){document.Form0.submit();}");
		jizfz.setLazyRender(true);
		tb1.addField(jizfz);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);

			visit.setDefaultTree(null);
		}		
		getToolbars();
		blnIsBegin = true;
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

    //机组分组下拉框
	public IDropDownBean getJizfzValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJizfzModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJizfzModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJizfzValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJizfzModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJizfzModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIJizfzModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIJizfzModels() {		
		String sql = "select id, mingc from jizfzb  order by xuh";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean3() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}
}
