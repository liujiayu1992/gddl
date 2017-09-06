package com.zhiren.shanxdted.huayyb;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-07-07
 * 内容:质量化验月报
 */
public class Huayyb extends BasePage implements PageValidateListener {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

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

	public static final String REPORT_NAME_RUCMZJYYB = "Rucmzjyyb";// 入厂煤质检验报表

	public static final String REPORT_NAME_RUCMZJYYB_A = "Rucmzjyyb_A";// 入厂煤质检验报表

	public static final String REPORT_NAME_RUCMZJYYB_B = "Rucmzjyyb_B";// 入厂煤质检验报表

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
//		 if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB)) {
		return getRucmzjyyb();
//		 } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_A)) {
//		 return getRucmzjyyb_A();
//		 } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_B)) {
//		 return getRucmzjyyb_B();
//		 } else {
//		 return "无此报表";
//		 }
	}
	
	
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
    

	private String getRucmzjyyb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
		}
		String yunSql = "";
		long yunsfsId = getYunsfsValue().getId();
		if(yunsfsId == 0){     //判断是否为0，为0时，运输方式为全部，SQL语句中则没有对运输方式的判断
			yunSql = " \n";
		}else{                //如果不为0，运输方式为yunsfsID的值，SQL中加入条件判断语句
			yunSql = "and f.yunsfsb_id = "+yunsfsId +" \n";
		}
	
		
//		运输单位
		String ysdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			ysdw=" where yunsdwb_id="+this.getMeikid();
		}
		
		//煤矿
		String meik="";
		String fahgl="";
		if(this.getTreeid()!=null && !this.getTreeid().equals("") && !this.getTreeid().equals("0")){
			meik=" where id="+this.getTreeid();
			fahgl=" and fh.meikxxb_id="+this.getTreeid();
		}
		
		
		
		StringBuffer buffer = new StringBuffer();

		buffer
				.append("select fhdw,\n"
						+ "       mkdw,\n"
						+"		  ysdw,\n"
						+ "       pz,\n"
						+ "       fz,\n"
						+ "       jingz,\n"
						+ "       ches,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       ad,\n"
						+ "       aar,\n"
						+ "       vad,\n"
						+ "       vdaf,\n"
						+ "       qbad*1000 as qbad,\n"
						+ "       farl*1000 as farl,\n"
//						+ "       qbar,\n"
						+"		  round_new(farl * 1000 / 4.1816,0) qbar,\n"		  
						
						+ "       sdaf,stad,\n"
						+ "       std,\n"
						+ "       star,\n"
						+ "       hdaf,\n"
						+ "       had,\n"
						
						+ "       fcad,\n"
						+ "       qgrd*1000 as qgrd\n"
						+ "  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
						+ "                      1,\n"
						+ "                      '合计',\n"
						+ "                      m.mingc) mkdw,\n"
						+"				  decode(grouping(g.mingc) + grouping(m.mingc)+grouping(f.ysdw),1,'小计',f.ysdw) ysdw,\n"
						+ "               p.mingc pz,\n"
						+ "               c.mingc fz,\n"
						+ "               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(f.yuns) yuns,\n"
						+ "               sum(f.yingk) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n"
						+ "                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n"
//						+ "                                           * 1000 / 4.1816,\n"
						+ "															,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum((round_new(z.stad*(100-z.mt)/(100-z.mad),2)) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had,\n"
						
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as qgrd\n"
						//+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
						//+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
						+ "          from ( select fh.*,(select mingc from yunsdwb where id in( select  yunsdwb_id from chepb where fahb_id=fh.id and rownum=1)"+fahgl+") ysdw from fahb fh where fh.id in(select distinct fahb_id from chepb "+ysdw+")) f, gongysb g, (select * from meikxxb "+meik+") m, pinzb p, chezxxb c, zhilb z\n"
						+ "         where f.gongysb_id = g.id\n"
						
						+s
						
						
						+ "           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.daohrq >= to_date('"
						+ getRiq()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f.daohrq <= to_date('"
						+ getAfter()
						+ "', 'yyyy-mm-dd')\n"
						+ yunSql
						+ "\n"
						+ "         group by rollup(g.mingc, m.mingc,f.ysdw,p.mingc, c.mingc)\n"
						+ "         order by decode(grouping(g.mingc),1,0,999),max(g.xuh),g.mingc," +
								"		decode(grouping(m.mingc),1,0,999),max(m.xuh),m.mingc,grouping(f.ysdw) desc,f.ysdw," +
								"	decode(grouping(p.mingc),1,0,999),p.mingc,decode(grouping(c.mingc),1,0,999))");
//		--------------------------- 新加的方法 ------------------------------------------------------
//		System.out.println(buffer.toString());
		ResultSetList rstmp = con.getResultSetList(buffer.toString());
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Rucmzjyyb_ED' order by xuh");
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
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='Rucmzjyyb'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
//        	-----------------------------  新加的方法 -------------------------------------------------------
        	ArrHeader = new String[1][24];

    		ArrHeader[0] = new String[] { "发货单位", "煤矿单位","运输单位", "品种", "发站",
    				"检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
    				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
    				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
    				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
    				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
    				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
    				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
    				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "空气<br>干燥<br>基硫<br>(%)<br>Stad","干燥<br>基全<br>硫(%)<br>St,d","收到<br>基全<br>硫(%)<br>St,ar",
    				"干燥<br>无灰<br>基氢<br>(%)<br>Hdaf","空气<br>干燥<br>基氢<br>(%)<br>Had",
    				
    				"固定<br>碳<br>(%)<br>Fcad",
    				"干基<br>高位<br>热<br>(J/g)<br>Qgrd"};
//    		int[] 
    		ArrWidth = new int[24];

    		ArrWidth = new int[] { 90, 180,100, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
    				40, 40, 40, 40, 40, 40, 40,40 ,40, 40, 40,40};
    		rt.setTitle("煤  质  检  验  月  报", ArrWidth);
    		
    	    strFormat = new String[] { "", "","", "", "", "0.00", "0", "0.00",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
    				"0.00", "0.00", "0.00","0.00","0.00" ,"0.00","0.00","" };
     

        }
        
		
		//System.out.println(buffer);
        
//		String[][] 
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 24);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiq() + "至" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 24; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤质检验月报","Rucmzjyyb");

		return rt.getAllPagesHtml();

	}

	// 部门A入厂煤质检验报表
	private String getRucmzjyyb_A() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
		}
		
		
		// String Radix = getRadixValue().getValue();
		// int x = (int) getCumStyleValue().getId();
		// String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
		// + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
		// + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
		// switch (x) {
		// case 1:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
		// break;
		// case 2:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
		// + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
		// + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
		// break;
		// case 3:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
		// break;
		// case 4:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
		// break;
		// }

		StringBuffer buffer = new StringBuffer();

		buffer.append("SELECT MEIKDQMC,\n");
		buffer.append("       MEIKDWMC,\n");
		buffer.append("       FAZ,\n");
		buffer.append("       PINZ,\n");
		buffer.append("       CHES,\n");
		buffer.append("       JINGZ,\n");
		buffer.append("       MT,\n");
		buffer.append("       MAD,\n");
		buffer.append("       AAD,\n");
		buffer.append("       AD,\n");
		buffer.append("       AAR,\n");
		buffer.append("       VAD,\n");
		buffer.append("       VDAF,\n");
		buffer.append("       QBAD,\n");
		buffer.append("       QBAR,\n");
		buffer.append("       FARL,\n");
		buffer.append("       SDAF,\n");
		buffer.append("       STD,\n");
		buffer.append("       HDAF\n");
		buffer.append("  FROM (SELECT A,\n");
		buffer.append("               B,\n");
		buffer.append("               C,\n");
		buffer.append("               D,\n");
		buffer.append("               MEIKDQMC,\n");
		buffer.append("               MEIKDWMC,\n");
		buffer.append("               FAZ,\n");
		buffer.append("               DECODE(MEIKDWMC,\n");
		buffer.append("                      NULL,\n");
		buffer
				.append("                      DECODE(MEIKDQMC, NULL, '总计', '合计'),\n");
		buffer.append("                      NVL(PINZ, '小计')) AS PINZ,\n");
		buffer.append("               JINGZ,\n");
		buffer.append("               CHES,\n");
		buffer.append("               MT,\n");
		buffer.append("               MAD,\n");
		buffer.append("               AAD,\n");
		buffer.append("               AD,\n");
		buffer.append("               AAR,\n");
		buffer.append("               VAD,\n");
		buffer.append("               VDAF,\n");
		buffer.append("               QBAD * 1000 AS QBAD,\n");
		buffer.append("               QBAR * 1000 AS QBAR,\n");
		buffer
				.append("               ROUND_NEW(QBAR * 1000 / 4.1816, 0) AS FARL,\n");
		buffer.append("               SDAF,\n");
		buffer.append("               STD,\n");
		buffer.append("               HDAF\n");
		buffer.append("          FROM (SELECT GROUPING(F.MEIKDQMC) AS A,\n");
		buffer.append("                       GROUPING(F.MEIKDWMC) AS B,\n");
		buffer.append("                       GROUPING(F.JIANC) AS C,\n");
		buffer.append("                       GROUPING(F.PINZ) AS D,\n");
		buffer.append("                       MAX(F.DQXUH) AS DQXH,\n");
		buffer.append("                       MAX(F.KBXUH) AS KBXH,\n");
		buffer.append("                       F.MEIKDQMC,\n");
		buffer.append("                       F.MEIKDWMC,\n");
		buffer.append("                       F.JIANC AS FAZ,\n");
		buffer.append("                       F.PINZ,\n");
		buffer.append("                       SUM(S.JINGZ) AS JINGZ,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.QUANSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.QUANSF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.QUANSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 1) AS MT,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJSF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS MAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.SHOUDJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AAR,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJHFF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS VAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZWHJHFF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS VDAF,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.DANTRL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.DANTRL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.DANTRL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS QBAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.FARL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.FARL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.FARL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS QBAR,\n");
		buffer
				.append("                       ROUND_NEW(ROUND_NEW(SUM(Z.GANZJL * S.JINGZ) /\n");
		buffer
				.append("                                           SUM(S.JINGZ),\n");
		buffer
				.append("                                           2) * 100 /\n");
		buffer
				.append("                                 (100 - ROUND_NEW(SUM(Z.GANZJHF * S.JINGZ) /\n");
		buffer
				.append("                                                  SUM(S.JINGZ),\n");
		buffer
				.append("                                                  2)),\n");
		buffer.append("                                 2) AS SDAF,\n");
		buffer.append("\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZJL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS STD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.HDAF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.HDAF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.HDAF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS HDAF,\n");
		buffer.append("                       SUM(S.CHES) AS CHES\n");
		buffer.append("                  FROM (SELECT F.ID,\n");
		buffer.append("                               F.HEDBZ,\n");
		buffer.append("                               DQ.XUH AS DQXUH,\n");
		buffer.append("                               KB.XUH AS KBXUH,\n");
		buffer.append("                               DQ.MEIKDQMC,\n");
		buffer.append("                               KB.MEIKDWMC,\n");
		buffer.append("                               CZ.JIANC,\n");
		buffer.append("                               PZ.PINZ\n");
		buffer.append("                          FROM FAHB    F,\n");
		buffer.append("                               MEIKDQB DQ,\n");
		buffer.append("                               MEIKXXB KB,\n");
		buffer.append("                               CHEZXXB CZ,\n");
		buffer.append("                               RANLPZB PZ\n");
		buffer.append("                         WHERE F.MEIKXXB_ID = KB.ID\n");
		
		buffer.append(s);
		
		
		buffer.append("                           AND DQ.ID = KB.MEIKDQB_ID\n");
		buffer.append("                           AND F.FAZ_ID = CZ.ID\n");
		buffer.append("                           AND F.RANLPZB_ID = PZ.ID\n");
		buffer.append("                           AND KB.TONGJBZ = 1) F,\n");
		buffer.append("                       VWZHILBTMP Z,\n");
		buffer.append("                       (SELECT C.FAHB_ID,\n");
		buffer
				.append("                               Z.ZHILB_ID AS ZHILBID,\n");
		// buffer.append(" " + cumStyle
		// + " AS JINGZ,\n");
		buffer.append("                               COUNT(C.ID) AS CHES\n");
		buffer
				.append("                          FROM CHEPB C, FAHB F, VWZHILBTMP Z\n");
		buffer.append("                         WHERE C.FAHB_ID = F.ID(+)\n");
		
		
		buffer.append(s);
		
		
		// 实现报表的兼容性
		buffer
				.append("                           AND F.ZHILB_ID = Z.ZHILB_ID\n");
		buffer.append("                           AND Z.BUM = 'A'\n");
		buffer.append("                           AND C.GUOHSJ >=\n");
		// buffer.append(" TO_DATE('"
		// + DateUtil.FormatDate(getRiq()) + "' || ' ' ||\n");
		buffer
				.append("                                       NVL((SELECT XITXXB.ZHI\n");
		buffer
				.append("                                             FROM XITXXB\n");
		buffer
				.append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
		buffer.append("                                           '0'),\n");
		buffer
				.append("                                       'YYYY-MM-DD HH24')\n");
		buffer.append("                           AND C.GUOHSJ <\n");
		// buffer.append(" TO_DATE('"
		// + DateUtil.FormatDate(custom.addDays(getAfter(), 1))
		// + "' || ' ' ||\n");
		buffer
				.append("                                       NVL((SELECT XITXXB.ZHI\n");
		buffer
				.append("                                             FROM XITXXB\n");
		buffer
				.append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
		buffer.append("                                           '0'),\n");
		buffer
				.append("                                       'YYYY-MM-DD HH24')\n");
		buffer
				.append("                         GROUP BY C.FAHB_ID, Z.ZHILB_ID) S\n");
		buffer.append("                 WHERE F.ID = S.FAHB_ID\n");
		
		buffer.append(s);
		
		
		buffer.append("                   AND Z.ZHILB_ID = S.ZHILBID\n");
		buffer.append("                   AND Z.SHANGJSHZT = 1\n");
		buffer.append("                   AND Z.BUM = 'A'\n");
		buffer.append("                   AND F.HEDBZ = 1\n");
		buffer
				.append("                 GROUP BY ROLLUP(F.MEIKDQMC, F.MEIKDWMC, F.JIANC, F.PINZ)\n");
		buffer
				.append("                HAVING GROUPING(F.JIANC) + GROUPING(F.PINZ) != 1) T)\n");
		buffer.append(" ORDER BY A DESC,\n");
		buffer.append("          MEIKDQMC DESC,\n");
		buffer.append("          B DESC,\n");
		buffer.append("          MEIKDWMC DESC,\n");
		buffer.append("          C DESC,\n");
		buffer.append("          FAZ,\n");
		buffer.append("          D DESC,\n");
		buffer.append("          PINZ DESC");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][19];

		ArrHeader[0] = new String[] { "煤矿地区", "煤矿单位", "发站", "品种", "车数",
				"检质数<br>量(吨)", "全水<br>分<br>(%)Mt",
				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "干燥<br>基全<br>硫(%)<br>St,d",
				"干燥<br>无灰<br>基氢<br>(%)<br>Hdaf" };
		int[] ArrWidth = new int[19];

		ArrWidth = new int[] { 90, 180, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("煤  质  检  验  月  报", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到厂日期:"
				+ DateUtil.FormatDate(new Date(getRiq())) + "至"
				+ DateUtil.FormatDate(new Date(getAfter())),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 19; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+  DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
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

	// 部门B入厂煤质检验报表
	private String getRucmzjyyb_B() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
		}
		
		
		// String Radix = getRadixValue().getValue();
		// int x = (int) getCumStyleValue().getId();
		// String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
		// + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
		// + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
		// switch (x) {
		// case 1:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
		// break;
		// case 2:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
		// + ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
		// + ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
		// break;
		// case 3:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
		// break;
		// case 4:
		// cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
		// + ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
		// break;
		// }
		StringBuffer buffer = new StringBuffer();

		buffer.append("SELECT MEIKDQMC,\n");
		buffer.append("       MEIKDWMC,\n");
		buffer.append("       FAZ,\n");
		buffer.append("       PINZ,\n");
		buffer.append("       CHES,\n");
		buffer.append("       JINGZ,\n");
		buffer.append("       MT,\n");
		buffer.append("       MAD,\n");
		buffer.append("       AAD,\n");
		buffer.append("       AD,\n");
		buffer.append("       AAR,\n");
		buffer.append("       VAD,\n");
		buffer.append("       VDAF,\n");
		buffer.append("       QBAD,\n");
		buffer.append("       QBAR,\n");
		buffer.append("       FARL,\n");
		buffer.append("       SDAF,\n");
		buffer.append("       STD,\n");
		buffer.append("       HDAF\n");
		buffer.append("  FROM (SELECT A,\n");
		buffer.append("               B,\n");
		buffer.append("               C,\n");
		buffer.append("               D,\n");
		buffer.append("               MEIKDQMC,\n");
		buffer.append("               MEIKDWMC,\n");
		buffer.append("               FAZ,\n");
		buffer.append("               DECODE(MEIKDWMC,\n");
		buffer.append("                      NULL,\n");
		buffer
				.append("                      DECODE(MEIKDQMC, NULL, '总计', '合计'),\n");
		buffer.append("                      NVL(PINZ, '小计')) AS PINZ,\n");
		buffer.append("               JINGZ,\n");
		buffer.append("               CHES,\n");
		buffer.append("               MT,\n");
		buffer.append("               MAD,\n");
		buffer.append("               AAD,\n");
		buffer.append("               AD,\n");
		buffer.append("               AAR,\n");
		buffer.append("               VAD,\n");
		buffer.append("               VDAF,\n");
		buffer.append("               QBAD * 1000 AS QBAD,\n");
		buffer.append("               QBAR * 1000 AS QBAR,\n");
		buffer
				.append("               ROUND_NEW(QBAR * 1000 / 4.1816, 0) AS FARL,\n");
		buffer.append("               SDAF,\n");
		buffer.append("               STD,\n");
		buffer.append("               HDAF\n");
		buffer.append("          FROM (SELECT GROUPING(F.MEIKDQMC) AS A,\n");
		buffer.append("                       GROUPING(F.MEIKDWMC) AS B,\n");
		buffer.append("                       GROUPING(F.JIANC) AS C,\n");
		buffer.append("                       GROUPING(F.PINZ) AS D,\n");
		buffer.append("                       MAX(F.DQXUH) AS DQXH,\n");
		buffer.append("                       MAX(F.KBXUH) AS KBXH,\n");
		buffer.append("                       F.MEIKDQMC,\n");
		buffer.append("                       F.MEIKDWMC,\n");
		buffer.append("                       F.JIANC AS FAZ,\n");
		buffer.append("                       F.PINZ,\n");
		buffer.append("                       SUM(S.JINGZ) AS JINGZ,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.QUANSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.QUANSF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.QUANSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 1) AS MT,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJSF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJSF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS MAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.GANZJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.SHOUDJHF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.SHOUDJHF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS AAR,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.KONGQGZJHFF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS VAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZWHJHFF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.GANZWHJHFF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS VDAF,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.DANTRL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.DANTRL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.DANTRL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS QBAD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.FARL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.FARL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.FARL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS QBAR,\n");
		buffer
				.append("                       ROUND_NEW(ROUND_NEW(SUM(Z.GANZJL * S.JINGZ) /\n");
		buffer
				.append("                                           SUM(S.JINGZ),\n");
		buffer
				.append("                                           2) * 100 /\n");
		buffer
				.append("                                 (100 - ROUND_NEW(SUM(Z.GANZJHF * S.JINGZ) /\n");
		buffer
				.append("                                                  SUM(S.JINGZ),\n");
		buffer
				.append("                                                  2)),\n");
		buffer.append("                                 2) AS SDAF,\n");
		buffer.append("\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.GANZJL, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.KONGQGZJL, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS STD,\n");
		buffer
				.append("                       ROUND_NEW(DECODE(SUM(DECODE(NVL(Z.HDAF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ)),\n");
		buffer.append("                                        0,\n");
		buffer.append("                                        0,\n");
		buffer
				.append("                                        SUM(NVL(Z.HDAF, 0) * S.JINGZ) /\n");
		buffer
				.append("                                        SUM(DECODE(NVL(Z.HDAF, 0),\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   0,\n");
		buffer
				.append("                                                   S.JINGZ))),\n");
		buffer.append("                                 2) AS HDAF,\n");
		buffer.append("                       SUM(S.CHES) AS CHES\n");
		buffer.append("                  FROM (SELECT F.ID,\n");
		buffer.append("                               F.HEDBZ,\n");
		buffer.append("                               DQ.XUH AS DQXUH,\n");
		buffer.append("                               KB.XUH AS KBXUH,\n");
		buffer.append("                               DQ.MEIKDQMC,\n");
		buffer.append("                               KB.MEIKDWMC,\n");
		buffer.append("                               CZ.JIANC,\n");
		buffer.append("                               PZ.PINZ\n");
		buffer.append("                          FROM FAHB    F,\n");
		buffer.append("                               MEIKDQB DQ,\n");
		buffer.append("                               MEIKXXB KB,\n");
		buffer.append("                               CHEZXXB CZ,\n");
		buffer.append("                               RANLPZB PZ\n");
		buffer.append("                         WHERE F.MEIKXXB_ID = KB.ID\n");
		
		buffer.append(s);
		
		
		buffer.append("                           AND DQ.ID = KB.MEIKDQB_ID\n");
		buffer.append("                           AND F.FAZ_ID = CZ.ID\n");
		buffer.append("                           AND F.RANLPZB_ID = PZ.ID\n");
		buffer.append("                           AND KB.TONGJBZ = 1) F,\n");
		buffer.append("                       VWZHILBTMP Z,\n");
		buffer.append("                       (SELECT C.FAHB_ID,\n");
		buffer
				.append("                               Z.ZHILB_ID AS ZHILBID,\n");
		// buffer.append(" " + cumStyle
		// + " AS JINGZ,\n");
		buffer.append("                               COUNT(C.ID) AS CHES\n");
		buffer
				.append("                          FROM CHEPB C, FAHB F, VWZHILBTMP Z\n");
		buffer.append("                         WHERE C.FAHB_ID = F.ID(+)\n");
		
		buffer.append(s);
		
		
		// 实现报表的兼容性
		buffer
				.append("                           AND F.ZHILB_ID = Z.ZHILB_ID\n");
		buffer.append("                           AND Z.BUM = 'A'\n");
		buffer.append("                           AND C.GUOHSJ >=\n");
		// buffer.append(" TO_DATE('"
		// + DateUtil.FormatDate(getRiq()) + "' || ' ' ||\n");
		buffer
				.append("                                       NVL((SELECT XITXXB.ZHI\n");
		buffer
				.append("                                             FROM XITXXB\n");
		buffer
				.append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
		buffer.append("                                           '0'),\n");
		buffer
				.append("                                       'YYYY-MM-DD HH24')\n");
		buffer.append("                           AND C.GUOHSJ <\n");
		// buffer.append(" TO_DATE('"
		// + DateUtil.FormatDate(custom.addDays(getAfter(), 1))
		// + "' || ' ' ||\n");
		buffer
				.append("                                       NVL((SELECT XITXXB.ZHI\n");
		buffer
				.append("                                             FROM XITXXB\n");
		buffer
				.append("                                            WHERE XITXXB.DUIXM = '日报时间'),\n");
		buffer.append("                                           '0'),\n");
		buffer
				.append("                                       'YYYY-MM-DD HH24')\n");
		buffer
				.append("                         GROUP BY C.FAHB_ID, Z.ZHILB_ID) S\n");
		buffer.append("                 WHERE F.ID = S.FAHB_ID\n");
		
		buffer.append(s);
		
		
		buffer.append("                   AND Z.ZHILB_ID = S.ZHILBID\n");
		buffer.append("                   AND Z.SHANGJSHZT = 1\n");
		buffer.append("                   AND Z.BUM = 'B'\n");
		buffer.append("                   AND F.HEDBZ = 1\n");
		buffer
				.append("                 GROUP BY ROLLUP(F.MEIKDQMC, F.MEIKDWMC, F.JIANC, F.PINZ)\n");
		buffer
				.append("                HAVING GROUPING(F.JIANC) + GROUPING(F.PINZ) != 1) T)\n");
		buffer.append(" ORDER BY A DESC,\n");
		buffer.append("          MEIKDQMC DESC,\n");
		buffer.append("          B DESC,\n");
		buffer.append("          MEIKDWMC DESC,\n");
		buffer.append("          C DESC,\n");
		buffer.append("          FAZ,\n");
		buffer.append("          D DESC,\n");
		buffer.append("          PINZ DESC");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][19];

		ArrHeader[0] = new String[] { "煤矿地区", "煤矿单位", "发站", "品种", "车数",
				"检质数<br>量(吨)", "全水<br>分<br>(%)Mt",
				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf", "干燥<br>基全<br>硫(%)<br>St,d",
				"干燥<br>无灰<br>基氢<br>(%)<br>Hdaf" };
		int[] ArrWidth = new int[19];

		ArrWidth = new int[] { 90, 180, 50, 50, 40, 50, 40, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("煤  质  检  验  月  报", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到厂日期:"
				+  DateUtil.FormatDate(new Date(getRiq())) + "至"
				+ DateUtil.FormatDate(new Date(getAfter())),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 19; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
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

		if(this.getYunsfsValue().getStrId().equals(SysConstant.YUNSFS_HUOY+"")){
			this.setMeikid("0");
		}
		
		if (_QueryClick) {
			_QueryClick = false;
		}
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
	
	//------------运输单位----------

	private String meikid = "";
		public String getMeikid() {
			if (meikid==null || meikid.equals("")) {

				meikid = "0";
			}
			return meikid;
		}
		public void setMeikid(String meikid) {
			if(meikid!=null) {
				if(!meikid.equals(this.meikid)) {
					((TextField)getToolbar().getItem("meikTree_text")).setValue
					(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
					this.getTree().getTree().setSelectedNodeid(meikid);
				}
			}
			this.meikid = meikid;
		}
	
		
	
//	获得运输单位 树形结构sql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from yunsdwb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	
	
	DefaultTree mktr;
	
	public DefaultTree getTree() {
		return mktr;
	}
	public void setTree(DefaultTree etu) {
		mktr=etu;
	}

	public String getTreeScriptMK() {
		return this.getTree().getScript();
	}
	
	


	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getMeikModels() {
		String sql = "select 0 id,'全部' mingc from dual union select id,mingc  from yunsdwb";
		setMeikModel(new IDropDownModel(sql));
	}
	
	//-------------------------------------------------
	
	
//	获取煤矿
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
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
	
	private StringBuffer getMKSql(){
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
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
    	String sql="";
    	
    	sql+="  select 0 id, '全部' mingc from dual union select id ,mingc from meikxxb";
//        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
    	setGongysDropDownModel(new IDropDownModel(sql)) ;
        return ;
    }
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();
		
		

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		
//煤矿
		
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "gongysTree", visit.getDiancxxb_id()+"", "forms[0]", this.getTreeid(), this.getTreeid());
		visit.setDefaultTree(gystree);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("煤矿:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		
		//运输单位--------------------
		
		DefaultTree mktree=new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit.getDiancxxb_id()+"", "forms[0]", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);
		
		TextField tfmk=new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel)this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null || "".equals(this.getMeikid()) ? "-1"
						: this.getMeikid())));
		
		

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);
		
		tb1.addText(new ToolbarText("运输单位"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);
		
		tb1.addText(new ToolbarText("-"));
	//-------------------------------------------------------	
		
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

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
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

	// 运输方式下拉框
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
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
		String sql = "select 0 id,'全部' mingc from dual union select id,mingc from yunsfsb";//连接一条假设行，如果ID为0时，运输方式为全部.
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			this.setRiq(null);
			this.setAfter(null);
			
			this.setMeikid(null);
			this.setTreeid(null);
			
			this.setYunsfsValue(null);
			this.setYunsfsModel(null);
			
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel1(null);
				visit.setDropDownBean1(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		blnIsBegin = true;
		// mstrReportName="diaor04bb";
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
