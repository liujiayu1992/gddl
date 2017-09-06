package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ranlysd extends BasePage {
	/*private static final int RPTTYPE_TZ_ALL = 1;
	private static final int RPTTYPE_TZ_HUOY = 2;
	private static final int RPTTYPE_TZ_QIY = 3;
	*/
	/**
	 * 作者:wzb
	 * 时间:2009-6-23 
	 * 内容:合山电厂燃料验收单
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb中对应的关键字
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

    
//  获得选择的树节点的对应的电厂名称   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
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
    


	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String gongys = "";
		String diancid = "" ;
		
		//2009-5-8 22:47:09 暂时没有处理一厂多制
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
			
		}
		con.Close();
		
		
		
		//结算编码
		long jiesbm=this.getChephValue().getId();
		
		
		 
		
		
		
		
		StringBuffer sb = new StringBuffer();
		String sql1 = "";
	
		sql1 = 
			"select decode(grouping(a.meik),1,'合计',a.meik) as meik,decode(grouping(a.meik),1,'合计',a.meik) as meik,decode(grouping(a.meik),1,'合计',a.meik) as meik," +
			"max(a.faz) as faz,max(a.jij) as jij,sum(a.kous) as kous,\n" +
			"round(sum(a.stad*a.jiessl)/sum(a.jiessl),2) as stad,round(sum(a.mt*a.jiessl)/sum(a.jiessl),2) as mt,\n" + 
			"round(sum(a.mad*a.jiessl)/sum(a.jiessl),2) as mad,round(sum(a.vdaf*a.jiessl)/sum(a.jiessl),2) as vdaf,\n" + 
			"round(sum(a.aad*a.jiessl)/sum(a.jiessl),2) as aad,round(sum(a.qnetar*a.jiessl)/sum(a.jiessl),2) as qnetar,sum(a.ches) as ches,\n" + 
			"sum(a.piaoz) as piaoz,sum(a.jiessl) as jiessl,round(sum(a.danj*a.jiessl)/sum(a.jiessl),2) as danj,\n" + 
			"sum(a.jine) as jine\n" + 
			"from\n" + 
			"      (select pc.xuh,max(m.mingc) as meik,(select mingc from chezxxb where id=pc.faz_id)  as faz,max(js.hetj) as jij,\n" + 
			"      max(pc.kous) as kous,max(pc.stad) as stad,max(pc.mt) as mt,max(pc.mad) as mad,\n" + 
			"      max(pc.vdaf) as vdaf, max(pc.aad) as aad, max(pc.qnetar) as qnetar,\n" + 
			"      max(pc.ches) as ches, max(pc.gongfsl) as piaoz,max(pc.jiessl) as jiessl,\n" + 
			"      max(pc.jiesdj) as danj,max(pc.jiashj) as jine\n" + 
			"      from danpcjsmxb pc ,jiesb js,meikxxb m\n" + 
			"      where pc.jiesdid=js.id and js.meikxxb_id=m.id\n" + 
			"      and js.id="+jiesbm+" and pc.leib=1 group by  (pc.xuh,pc.faz_id))  a\n" + 
			"group by rollup (a.xuh,a.meik)\n" + 
			"having not (grouping(a.meik)+grouping(a.xuh)=1 )\n" + 
			"order by a.xuh";


		sb.append(sql1);
		
		
		
		return sb;
	}
	
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"燃料验收单";
		
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("结算日期:"));
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
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("合同单位:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		
//		电厂Tree
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
		
		//结算编码
		tb1.addText(new ToolbarText("结算编号:"));
		ComboBox CB = new ComboBox();
		CB.setTransform("CHEPH");
		CB.setWidth(150);
		CB.setListeners("select:function(){document.Form0.submit();}");
		CB.setEditable(true);
		tb1.addField(CB);
		tb1.addText(new ToolbarText("-"));
		
		

		
		//如果是一厂多制就显示电厂树,如果不是就不显示电厂
		if(hasDianc(String.valueOf(visit.getDiancxxb_id()))){
			tb1.addText(new ToolbarText("电厂:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
//		结算类型
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.Form0.submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		if (getJieslbValue().getValue().equals("验收单")){
			return getYansd();
		}else if(getJieslbValue().getValue().equals("拒付单")){
			return getJufd();
		}else if(getJieslbValue().getValue().equals("开票单")){
			return getKaipd();
		}else if(getJieslbValue().getValue().equals("过衡单")){
			return getGuohd();
		}else{
			return "无此报表";
		}
	}
	
	
	public String getGuohd(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String faz="";
		String kuangb="";
		String hetbh="";
		String famsj="";
		int p=0;

			sbsql.append(
					"select min(c.mingc) as faz,min(m.mingc) as kuangb,\n" +
					"min(h.hetbh) as heth,to_char(min(f.fahrq),'yyyy-MM-dd') as famsj\n" + 
					"from fahb f,meikxxb m ,chezxxb c,hetb h,jiesb j\n" + 
					"where f.meikxxb_id=m.id(+)\n" + 
					"and f.faz_id=c.id(+)\n" + 
					"and f.hetb_id=h.id(+)\n" + 
					"and f.jiesb_id="+this.getChephValue().getId()+""
			);
			rs=con.getResultSetList(sbsql.toString());
			if(rs.next()){
				
				faz=rs.getString("faz");
				kuangb=rs.getString("kuangb");
				hetbh=rs.getString("heth");
				famsj=rs.getString("famsj");
			}
		

			
			sbsql.setLength(0);
			sbsql.append(
					"select decode(c.zhongcsj,null,'合计',to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss')) as zhongcsj,\n" +
					"decode(c.cheph,null,count(c.cheph),c.cheph) as cheph,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz) as jingz,\n" + 
					"sum(c.biaoz) as biaoz,sum(c.koud+c.kous+c.kouz+c.koum) as kous,sum(c.maoz-c.piz-c.koud-c.kous-c.kouz-c.koum) as jisml\n" + 
					" from fahb f,chepb c\n" + 
					"where c.fahb_id=f.id\n" + 
					"and f.jiesb_id="+this.getChephValue().getId()+"\n" + 
					"group by rollup (c.zhongcsj,c.cheph)\n" + 
					"having not (grouping(c.cheph)=1 and grouping(c.zhongcsj)=0)\n" + 
					"order by c.zhongcsj,min(c.xuh)"

			);
			rs=con.getResultSetList(sbsql.toString());
			
			Report rt = new Report(); //报表定义
			String ArrHeader[][]=new String[1][8];
//			1120
			ArrHeader[0]=new String[] {"来煤时间","车号","毛重","皮重","净重","票重","扣吨扣水","结算煤量"};
			int ArrWidth[]=new int[] {125,70,70,70,70,70,70,70};
			// 数据
			rt.setTitle("来煤过衡单",ArrWidth);
			rt.setDefaultTitleLeft("发站:"+faz+"<br>供煤单位:"+kuangb+"",2);
			rt.setDefaultTitleRight("合同编号:"+hetbh+"<br>发煤时间:"+famsj, 2);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setWidth(ArrWidth);
//			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);// 表头数据

			rt.body.ShowZero = true;
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
//			构造表
			talbe.append(rt.getAllPagesHtml(p));
			p++;
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
	
	}
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		结算编码
		long jiesbm1=this.getChephValue().getId();
		
		String huiz="select h.hetbh,h.gongfdwmc,j.jiesrq, j.ches ,j.jiessl,j.hansdj,j.hansmk,0 as shenf,yf.guotyf,yf.kuangqyf ,yf.kuidjfyf as kuidyf,yf.kuidjfzf as juf,\n" +
			"(j.hansmk+nvl(guotyf,0)+nvl(kuangqyf,0)-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as yingfje\n" + 
			"from jiesb j,jiesyfb yf,hetb h\n" + 
			" where  j.bianm=yf.bianm(+) and j.hetb_id=h.id(+)\n" + 
			" and j.id="+jiesbm1+"";
		String ches="";
		String jiessl="";
		String danj="";
		String meik="";
		String shenf="";
		String yunf="";
		String yunzf="";
		String kuidyf="";
		String juf="";
		String zongje="";
		String hetdw="";
		String heth="";
		String jiesrq="";
		ResultSetList HZ = con.getResultSetList(huiz);
		while(HZ.next()){
			hetdw=HZ.getString("gongfdwmc");
			heth=HZ.getString("hetbh");
			jiesrq=HZ.getDateString("jiesrq");
			ches=String.valueOf(HZ.getLong("ches"));
			jiessl=String.valueOf(HZ.getDouble("jiessl"));
			danj=String.valueOf(HZ.getDouble("hansdj"));
			meik=String.valueOf(HZ.getDouble("hansmk"));
			shenf=String.valueOf(HZ.getDouble("shenf"));
			yunf=String.valueOf(HZ.getDouble("guotyf"));
			yunzf=String.valueOf(HZ.getDouble("kuangqyf"));
			kuidyf=String.valueOf(HZ.getDouble("kuidyf"));
			juf=String.valueOf(HZ.getDouble("juf"));
			zongje=String.valueOf(HZ.getDouble("yingfje"));
			
		}
		
		ResultSetList rs = null;
		//String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;



        	rs = rstmp;
        	
        	String ArrHeader[][]=new String[4][17];
        	ArrHeader[0]=new String[] {"车数","结算煤量","单价","煤款","什费","什费","运费","运费","运费","其它运杂费","其它运杂费","扣超/亏吨运费","扣超/亏吨运费","拒付金额","拒付金额","应付总金额","应付总金额"};
        	ArrHeader[1]=new String[] {ches,   jiessl,danj,meik,shenf,shenf,yunf,yunf,yunf,yunzf,yunzf,kuidyf,kuidyf,juf,juf,zongje,zongje};
        	ArrHeader[2]=new String[] {"供煤单位","供煤单位","供煤单位","发站","基价","扣水","煤质","煤质","煤质","煤质","煤质","煤质","车数","票重","验收数","单价","金额"};
        	ArrHeader[3]=new String[] {"供煤单位","供煤单位","供煤单位","发站","基价","扣水","Stad","Mt","Mad","Vdaf","Aad","Qnetar","车数","票重","验收数","单价","金额"};
			
        	
    		 ArrWidth = new int[] {50, 65, 50, 65, 45, 45, 40, 40, 40 ,40, 40, 45, 40, 45, 50, 55, 65};
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		
    		//strFormat = new String[] { "", "", "", "", "0.00", "0.00", "0.00","0.00", "0.00"};
    		rt.setTitle(getRptTitle(), ArrWidth);
    		
		

//
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "合同单位：" + hetdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, "结算日期:"+jiesrq,Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 4, "合同号:"+heth,Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 4, "编号:"+this.getChephValue().getValue(), Table.ALIGN_RIGHT);

//
		rt.setBody(new Table(rs, 4, 0, 3));
//		补够16行
		int iLastRow=rt.body.getRows();
		int iNewlastRow=16;
		if  (iLastRow<16){
			rt.body.AddTableRow(16-iLastRow);
			//合计移到最后
			for (int i=1 ;i<=rt.body.getCols(); i++){
				rt.body.setCellValue(iNewlastRow-1, i, rt.body.getCellValue(iLastRow,i));
				rt.body.setCellValue(iLastRow, i, "");
			}
			rt.body.setCellValue(iNewlastRow, 1, "备注");
			rt.body.setCellValue(iNewlastRow-1, 4, "");//去掉合计中的发站
			rt.body.merge(16, 2, 16, 17);
			
		}
		//设定列对齐
		for(int a=1;a<=rt.body.getCols();a++){
			rt.body.setColAlign(a, Table.ALIGN_CENTER);
		}
	
		
		
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		//rt.body.mergeFixedCols();
		//rt.body.mergeFixedRow();
		//设定表头合并
		rt.body.merge(1, 5, 1, 6);
		rt.body.merge(1, 7, 1, 9);
		rt.body.merge(1, 10, 1, 11);
		rt.body.merge(1, 12, 1, 13);
		rt.body.merge(1, 14, 1, 15);
		rt.body.merge(1, 16, 1, 17);
		
		rt.body.merge(2, 5, 2, 6);
		rt.body.merge(2, 7, 2, 9);
		rt.body.merge(2, 10, 2, 11);
		rt.body.merge(2, 12, 2, 13);
		rt.body.merge(2, 14, 2, 15);
		rt.body.merge(2, 16, 2, 17);
		
		rt.body.merge(3, 1, 4, 3);
		rt.body.merge(3, 4, 4, 4);
		rt.body.merge(3, 5, 4, 5);
		rt.body.merge(3, 6, 4, 6);
		rt.body.merge(3, 7, 3, 12);
		rt.body.merge(3, 13, 4, 13);
		rt.body.merge(3, 14, 4, 14);
		rt.body.merge(3, 15, 4, 15);
		rt.body.merge(3, 16, 4, 16);
		rt.body.merge(3, 17, 4, 17);
		
		//设定前三列从第5行开始合并
		for (int i=5;i<=rt.body.getRows()-1;i++){
			rt.body.merge(i, 1, i, 3);
			
		}
		
		
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(2, 3, "主管：",Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "复核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "结算员：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;

		rt.body.ShowZero = true;
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		
     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	public String getJufd(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		long jiesbm=this.getChephValue().getId();
		String sqlJufd ="select j.jiesrq,j.bianm,g.quanc,j.ches,j.jiessl,(j.hansmk+nvl(yf.guotyf,0))  as tuosje,\n" +
			" (yf.kuidjfyf+yf.kuidjfzf) as jufje,(j.hansmk+nvl(yf.guotyf,0)-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as chengfje,j.beiz\n" + 
			" from jiesb j,gongysb g ,jiesyfb yf\n" + 
			" where j.gongysb_id=g.id(+)\n" + 
			" and j.bianm=yf.bianm(+)\n" + 
			" and j.id="+jiesbm;

		ResultSetList rs_Jufd = con.getResultSetList(sqlJufd);
		String jiesrq = "";
		String bianm = "";
		String gongys = "";
		String ches = "";
		String jiessl = "";
		String tuosje = "";
		String jufje="";
		String chengfje="";
		String beiz="";

		while(rs_Jufd.next()){
			 jiesrq = rs_Jufd.getDateString("jiesrq");
			 bianm = rs_Jufd.getString("bianm");
			 gongys = rs_Jufd.getString("quanc");
			 ches = String.valueOf(rs_Jufd.getLong("ches"));
			 jiessl = String.valueOf(rs_Jufd.getDouble("jiessl"));
			 tuosje = String.valueOf(rs_Jufd.getDouble("tuosje"));
			 jufje=String.valueOf(rs_Jufd.getDouble("jufje"));
			 chengfje=String.valueOf(rs_Jufd.getDouble("chengfje"));
			 beiz=rs_Jufd.getString("beiz");
		}
		
		
		
		

		String[][] CAIY=new String[][]{
			{"合同单位",gongys,gongys,gongys},
			{"车数",ches,"煤量",jiessl},
			{"托收金额",tuosje,"拒付金额",jufje},
			{"","","承付金额",chengfje},
			{"拒付理由","拒付理由","拒付理由","拒付理由"},
			{beiz,beiz,beiz,beiz}};
		rs_Jufd.close();
		
		String[][] ArrHeader = new String[6][4];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] { 150,150,150,150 };
		
		Table bt=new Table(6,4);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][4];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		rt.setTitle(v.getDiancqc()+"拒付理由书", ArrWidth);
		rt.setDefaultTitle(1,2,"结算日期:"+jiesrq,Table.ALIGN_LEFT);
		rt.setDefaultTitle(3,2,"编号:"+bianm,Table.ALIGN_RIGHT);
		
		//合并
		rt.body.merge(1, 2, 1, 4);
		rt.body.merge(4, 1, 4, 2);
		rt.body.merge(5, 1, 5, 4);
		rt.body.merge(6, 1, 6, 4);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setRowHeight(6, 60);
		
		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(3, 2, "盖章：", Table.ALIGN_LEFT);

		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		rt.body.ShowZero = true;
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public String getKaipd(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		long jiesbm_kp=this.getChephValue().getId();
		
		/*因为在结算单页面,填入拒付运费和拒付杂费,页面中不发生js计算,没有影响煤款,税款,价税合计等值.所以在给电厂
		 出开票单时需要刨除拒付运费和拒付杂费,重新计算税款,不含税煤矿,不含税单价,价税合计等*/
		String sql_Kaip="select j.jiesrq,j.bianm,g.quanc,'' as yingsmc,\n" +
			"j.jiessl,\n" + 
			"round_new(((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))- round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))/j.jiessl,6) buhsdj,\n" + 
			"((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))- round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2))  as buhsmk,\n" + 
			"j.shuil,\n" + 
			"round_new((j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0))/1.17*0.17,2) as shuik,\n" + 
			"(nvl(yf.kuangqyf,0)+nvl(yf.kuangqzf,0)) as huopzf,\n" + 
			"nvl(yf.guotyf,0) as guotyf,\n" + 
			"(j.hansmk-nvl(yf.kuidjfyf,0)-nvl(yf.kuidjfzf,0)) as jiashj\n" + 
			" from jiesb j,gongysb g ,jiesyfb yf\n" + 
			" where j.gongysb_id=g.id(+)\n" + 
			" and j.bianm=yf.bianm(+)\n" + 
			" and j.id="+jiesbm_kp;


		ResultSetList rs_Kp = con.getResultSetList(sql_Kaip);
		String jiesrq = "";
		String bianm = "";
		String gongys = "";
		String yingsmc = "";
		String jiessl = "";
		String buhsdj = "";
		String buhsmk="";
		String shuil="";
		String shuik="";
		String huopzf="";
		String yunf="";
		String jiashj="";

		while(rs_Kp.next()){
			 jiesrq = rs_Kp.getDateString("jiesrq");
			 bianm = rs_Kp.getString("bianm");
			 gongys = rs_Kp.getString("quanc");
			 yingsmc = rs_Kp.getString("yingsmc");
			 jiessl = String.valueOf(rs_Kp.getDouble("jiessl"));
			 buhsdj = String.valueOf(rs_Kp.getDouble("buhsdj"));
			 buhsmk=String.valueOf(rs_Kp.getDouble("buhsmk"));
			 shuil=String.valueOf(rs_Kp.getDouble("shuil"));
			 shuik=String.valueOf(rs_Kp.getDouble("shuik"));
			 huopzf=String.valueOf(rs_Kp.getDouble("huopzf"));
			 yunf=String.valueOf(rs_Kp.getDouble("guotyf"));
			 jiashj=String.valueOf(rs_Kp.getDouble("jiashj"));
		}
		
		
		
		

		String[][] KAIPD=new String[][]{
			{"结算单号","结算单号","合同单位","合同单位","合同单位","合同单位","结算日期"},
			{bianm,bianm,gongys,gongys,gongys,gongys,jiesrq},
			{"应税名称","单位","数量","单价","金额","税率","税额"},
			{yingsmc,"吨",jiessl,buhsdj,buhsmk,shuil,shuik},
			{"货票杂费",huopzf,huopzf,"运费",yunf,"价税合计",jiashj}};
		rs_Kp.close();
		
		String[][] ArrHeader = new String[5][7];
		int i=0;
		for(int j=0;j<KAIPD.length;j++){
			ArrHeader[i++]=KAIPD[j];
		}
		int[] ArrWidth = new int[] { 80,80,80,80,90,80,90 };
		
		Table bt=new Table(5,7);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][7];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		rt.setTitle(v.getDiancqc()+"开票通知单", ArrWidth);
//		rt.setDefaultTitle(1,2,"结算日期:"+getBRiq(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(3,2,"编号:000009-09",Table.ALIGN_RIGHT);
		
		//合并
		rt.body.merge(1, 1, 1, 2);
		rt.body.merge(1, 3, 1, 6);
		rt.body.merge(2, 1, 2, 2);
		rt.body.merge(2, 3, 2, 6);
		rt.body.merge(5, 2, 5, 3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setRowHeight(30);
		

		
		
		rt.createFooter(1, ArrWidth);
		//rt.setDefautlFooter(3, 2, "盖章：", Table.ALIGN_LEFT);

		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		rt.body.ShowZero = true;
		con.Close();
		return rt.getAllPagesHtml();
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
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
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
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			//visit.setInt1(Integer.parseInt(reportType));
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			setChangbValue(null);
			setChangbModel(null);
//			visit.setDefaultTree(null);
//			setDiancmcModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
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
			this.getChephModels();
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
	
	
//	 车号
	public IDropDownBean getChephValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getChephModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setChephValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setChephModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getChephModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getChephModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getChephModels() {
		
		String jiesrq ="";
		Date addRiq=DateUtil.AddDate(DateUtil.getDate(this.getERiq()), 1, DateUtil.AddType_intDay);
		jiesrq = " and  j.jiesrq>="+DateUtil.FormatOracleDate(getBRiq())
		+" and  j.jiesrq<"+DateUtil.FormatOracleDate(addRiq);
		
		String gongys_tj="";
		
		long gongys=this.getGongysValue().getId();
		if(gongys!=-1){
			gongys_tj=" and j.gongysb_id="+gongys;
		}
		String sql = 
			"select j.id,j.bianm\n" +
			"from jiesb j ,gongysb g \n" + 
			"where j.gongysb_id=g.id\n" + 
			""+jiesrq+""+
			""+gongys_tj+""+
			" order by j.bianm";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql,"请输入"));
		return;
	}
	
	
	
//	 结算类别(验收单,拒付单,开票单)
	public IDropDownBean getJieslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJieslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslbValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJieslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJieslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJieslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJieslbModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "验收单"));
		list.add(new IDropDownBean(2, "拒付单"));
		list.add(new IDropDownBean(3, "开票单"));
		list.add(new IDropDownBean(4, "过衡单"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
	
	
	
//	 供应商下拉框
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		
		String sql_gongys = "select g.id,g.mingc from gongysb g order by g.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,""));
		return;
	}
	
}
