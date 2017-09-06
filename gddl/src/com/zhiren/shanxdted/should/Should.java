package com.zhiren.shanxdted.should;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Should extends BasePage implements PageValidateListener {

	public boolean getRaw() {
		return true;
	}

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

	public String getPrintTable() {
		return getHuaybgd();
	}

	//	查询zhillsb的数据
	private StringBuffer getBaseSQl(){
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select j.diancxxb_id, j.id,j.hetj,j.bianm,nvl(j.meiz,'') meiz,nvl(j.gongysmc,'') gongysmc,to_char(j.fahksrq,'yyyy-MM-dd') fahksrq,to_char(j.fahjzrq,'yyyy-MM-dd') fahjzrq,\n");
		bf.append(" nvl(j.xianshr,'') xianshr,j.ches,j.jiessl,j.hansdj hansdjmk,j.buhsdj buhsdjmk,j.hansmk,j.buhsmk,\n");
//		bf.append(" jy.hansdj hansdjyf,jy.buhsdj buhsdjyf,jy.hansyf,jy.buhsyf\n");
		bf.append(" jy.hansdj hansdjyf,(jy.hansdj*(1-nvl(jy.shuil,0))) buhsdjyf,jy.hansyf,jy.buhsyf,ht.hetbh,decode(j.yunsfsb_id,1,'火车','汽车') as yunsfs,to_char(j.jiesrq,'yyyy-mm-dd') as jiesrq, j.chongdjsb_id\n");
		bf.append(" from jiesb j,jiesyfb jy,hetb ht\n");
		bf.append(" where j.id=jy.diancjsmkb_id(+)   and j.hetb_id=ht.id(+)\n");
		bf.append(" and j.bianm='"+this.getJiesdbm()+"'\n");
		
		bf.append(" union \n");
		

		bf.append(" select j.diancxxb_id,j.id,j.hetj,jy.bianm,nvl(j.meiz,'') meiz,nvl(j.gongysmc,'') gongysmc,to_char(j.fahksrq,'yyyy-MM-dd') fahksrq,to_char(j.fahjzrq,'yyyy-MM-dd') fahjzrq,\n");
		bf.append(" nvl(j.xianshr,'') xianshr,j.ches,j.jiessl,j.hansdj hansdjmk,j.buhsdj buhsdjmk,j.hansmk,j.buhsmk,\n");
//		bf.append(" jy.hansdj hansdjyf,jy.buhsdj buhsdjyf,jy.hansyf,jy.buhsyf\n");
		bf.append(" jy.hansdj hansdjyf,(jy.hansdj*(1-nvl(jy.shuil,0))) buhsdjyf,jy.hansyf,jy.buhsyf,ht.hetbh,decode(jy.yunsfsb_id,1,'火车','汽车') as yunsfs,to_char(jy.jiesrq,'yyyy-mm-dd') as jiesrq, j.chongdjsb_id\n");
		bf.append(" from jiesb j,jiesyfb jy,hetb ht\n");
		bf.append(" where j.id(+)=jy.diancjsmkb_id   and jy.hetb_id=ht.id(+)\n");
		bf.append(" and jy.bianm='"+this.getJiesdbm()+"'\n");
		
		
		return bf;
		
	}
	

	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		boolean should_dat="是".equals(MainGlobal.getXitxx_item("结算", "大同电厂收料单", visit.getDiancxxb_id(), "否"));
		
		if(should_dat){
			//大同电厂收料单
			return getshould_dat(con);
		}else{
			return getShould(con);
		}

	}
	
	public String getshould_dat(JDBCcon con){
		Report rt = new Report();
		

		if(this.getJiesdbm()==null || this.getJiesdbm().equals("")){
			return "";
		}
		StringBuffer sql = this.getBaseSQl();
		
		ResultSetList rs = con.getResultSetList(sql.toString());
		String jiesrq="";
		String[][] ArrHeader = new String[5][6];
		long diancxxb_id=0;
		String xianshr="";
		String meiz="";
		String gongysmc="";
		String fahksrq="";
		String fahjzrq="";
		String hetbh="";
		String jiessl="";
		String yunsfs="";
		double hansdjmk=0;
		long chongdjsd=0;
		double hansmk=0;
		String shifmyk_num="";
		
		try {
			if (rs.next()) {
				diancxxb_id=rs.getLong("diancxxb_id");
				meiz=rs.getString("meiz");
				gongysmc=rs.getString("gongysmc");
			    fahksrq= rs.getString("fahksrq");
			    fahjzrq=rs.getString("fahjzrq");
				xianshr=rs.getString("xianshr");
			    hetbh=rs.getString("hetbh");
				jiessl=rs.getString("jiessl");
			    yunsfs=rs.getString("yunsfs");
			    jiesrq=rs.getString("jiesrq");
				 hansdjmk=rs.getDouble("hansdjmk");
				 chongdjsd=rs.getLong("chongdjsb_id");
				 hansmk=rs.getDouble("hansmk");
				if(chongdjsd!=0){//是冲抵结算单,冲抵结算单的单价要用总煤款除以结算数量,进行反推
					hansdjmk=CustomMaths.div(hansmk,Double.parseDouble(jiessl),2);
				}
				
				 DecimalFormat df = new DecimalFormat("0.00"); 
				 shifmyk_num = df.format(hansmk); //煤运款
				
			} else{
				return null;
			}
				
		} catch (Exception e) {
			System.out.println(e);
		}
		
		String meizsql="select pz.mingc\n" +
			"from hetslb sl,hetb h ,jiesb js ,pinzb pz\n" + 
			"where js.hetb_id=h.id\n" + 
			"and h.id=sl.hetb_id\n" + 
			"and sl.pinzb_id=pz.id\n" + 
			"and js.bianm='"+this.getJiesdbm()+"'";
		rs = con.getResultSetList(meizsql);
		if(rs.next()){
			//煤种取合同表中的品种字段
			meiz=rs.getString("mingc");
		}

		
		ArrHeader[0] = new String[] { "收煤单位",xianshr , "结算单据号","结算单据号",this.getJiesdbm(),this.getJiesdbm() };
		ArrHeader[1] = new String[] { "品名","供煤期","单位","结算数量(吨)","单价(元/吨)","金额(元)"};
		ArrHeader[2] = new String[] { meiz,fahksrq+"-"+fahjzrq,"吨",jiessl,hansdjmk+"",shifmyk_num+""};
		ArrHeader[3] = new String[] { "供货单位",gongysmc ,"合同编号","合同编号", hetbh,hetbh};
		ArrHeader[4] = new String[] { "记事", "","运输方式", "运输方式", yunsfs, yunsfs };
		int[] ArrWidth = new int[] { 100, 350, 60, 130, 110, 100 };
		if(diancxxb_id==301){
			rt.setTitle("<br><br><br>国电电力大同第二发电厂<br><br>燃煤收料单<br>", ArrWidth);
		}else{
			rt.setTitle("<br><br><br>国电电力大同发电有限责任公司<br><br>燃煤收料单<br>", ArrWidth);
		}
		
		rt.setBody(new Table(5,6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		
	
		rt.setDefaultTitle(1, 2, "结算日期:"+jiesrq, Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		
		rt.body.merge(1, 3, 1, 4);
		rt.body.merge(1, 5, 1, 6);
		rt.body.merge(4, 3, 4, 4);
		rt.body.merge(4, 5, 4, 6);
		
		rt.body.merge(5, 3, 5, 4);
		rt.body.merge(5, 5, 5, 6);
		
		rt.body.setCellFontBold(1, 1, true);
		rt.body.setCellFontBold(1, 3, true);
		rt.body.setCellFontBold(2, 1, true);
		rt.body.setCellFontBold(2, 2, true);
		rt.body.setCellFontBold(2, 3, true);
		rt.body.setCellFontBold(2, 4, true);
		rt.body.setCellFontBold(2, 5, true);
		rt.body.setCellFontBold(2, 6, true);
		rt.body.setCellFontBold(4, 1, true);
		rt.body.setCellFontBold(4, 3, true);
		rt.body.setCellFontBold(5, 1, true);
		rt.body.setCellFontBold(5, 3, true);
		
 
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "财务稽核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "负责人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "验收：" , Table.ALIGN_LEFT);

		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		
		
		rt.body.setFontSize(12);
		
		for(int a=1;a<=rt.body.getCols();a++){
			rt.body.setColAlign(a, Table.ALIGN_CENTER);
		}
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(60);

		return rt.getAllPagesHtml();
	}
	
	public String getShould(JDBCcon con){
		Report rt = new Report();
		

		if(this.getJiesdbm()==null || this.getJiesdbm().equals("")){
			return "";
		}
		StringBuffer sql = this.getBaseSQl();
		
		ResultSetList rs = con.getResultSetList(sql.toString());

		String[][] ArrHeader = new String[9][6];
		try {
			if (rs.next()) {
				
				String meiz=rs.getString("meiz");
				String gongysmc=rs.getString("gongysmc");
				String fahksrq=DateUtil.Formatdate("yyyy年MM月dd日起", DateUtil.getDate(rs.getString("fahksrq")));
				String fahjzrq=DateUtil.Formatdate("yyyy年MM月dd日止", DateUtil.getDate(rs.getString("fahjzrq")));
				String xianshr=rs.getString("xianshr");
				String ches=rs.getString("ches");
				String jiessl=rs.getString("jiessl");
				double hansdjmk=rs.getDouble("hansdjmk");
				double buhsdjmk=rs.getDouble("buhsdjmk");
				double hansmk=rs.getDouble("hansmk");
				double buhsmk=rs.getDouble("buhsmk");
				double hansdjyf=rs.getDouble("hansdjyf");
				double buhsdjyf=rs.getDouble("buhsdjyf");
				double hansyf=rs.getDouble("hansyf");
				double buhsyf=rs.getDouble("buhsyf");
				
				
			
			
				
				ArrHeader[0] = new String[] { "货物名称",
						"" + meiz + "", "供货单位",
						"" + gongysmc + "", gongysmc,
						"" + gongysmc+ "" };

				ArrHeader[1] = new String[] { "供货时间",
						"" + fahksrq+"<br>"+fahjzrq+ "", "结算单位",
						"" + xianshr + "", xianshr,
						"" +xianshr + "" };
				
				ArrHeader[2] = new String[] { "数量",
						"车数", ches,
						ches,
						ches,
						ches };
				
			
				ArrHeader[3] = new String[] { "数量", "吨数",
							jiessl,jiessl, jiessl,
							jiessl };
					
			
				ArrHeader[4] = new String[] { "项目", "项目",
						"结算单价", "结算单价", "结算金额", "结算金额" };
				
				
				ArrHeader[5] = new String[] { "项目", "项目",
						"含税价", "不含税价", "含税价", "不含税价" };
				
				
				ArrHeader[6] = new String[] { "煤价", "煤价",
						(hansdjmk==0?"":(hansdjmk+"")), (buhsdjmk==0?"":(buhsdjmk+"")), 
						(hansmk==0?"":(hansmk+"")), (buhsmk==0?"":(buhsmk+"")) };
				
				
				ArrHeader[7] = new String[] { "运杂费", "运杂费",
						(hansdjyf==0?"":(hansdjyf+"")), (buhsdjyf==0?"":(buhsdjyf+"")), 
						(hansyf==0?"":(hansyf+"")), (buhsyf==0?"":(buhsyf+"")) };
				
				
				
				ArrHeader[8] =new String[] { "结算费用", "结算费用",
						(hansdjmk+hansdjyf==0?"":(hansdjmk+hansdjyf+"")), (buhsdjmk+buhsdjyf==0?"":(buhsdjmk+buhsdjyf+"")), 
						(hansmk+hansyf==0?"":(hansmk+hansyf+"")), (buhsmk+buhsyf==0?"":(buhsmk+buhsyf+"")) };
				
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		int[] ArrWidth = new int[] { 140, 160, 170, 170, 170, 170 };

		
		
		 String ArrTitle[][]=new String[7][6];
		 ArrTitle[0]=new String[] {"","","", "","",""};
		 ArrTitle[1]=new String[] {"","","", "","","",};
		 ArrTitle[2]=new String[] {"国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司"};
		 ArrTitle[3]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
		 ArrTitle[4]=new String[] {"大 同 第 二 发 电 厂 燃 料 收 料 单","大 同 第 二 发 电 厂 燃 料 收 料 单","大 同 第 二 发 电 厂 燃 料 收 料 单","大 同 第 二 发 电 厂 燃 料 收 料 单","大 同 第 二 发 电 厂 燃 料 收 料 单","大 同 第 二 发 电 厂 燃 料 收 料 单"};
		 ArrTitle[5]=new String[] {"","","","","","","","","","","","","","","","","","",""};
		 ArrTitle[6]=new String[] {"","",DateUtil.Formatdate("yyyy年MM月dd日", new Date()),DateUtil.Formatdate("yyyy年MM月dd日", new Date()),"编号:"+this.getJiesdbm(),"编号:"+this.getJiesdbm()};
		 
		 
		rt.setTitle(new Table(ArrTitle,0,0,0));
		rt.title.setWidth(ArrWidth);
		
		 rt.title.merge(2, 1, 2, 6);
		 rt.title.merge(3, 1, 3, 6);
		 rt.title.merge(4, 1, 4, 6);
		 rt.title.merge(5, 1, 5, 6);
		 rt.title.merge(6, 1, 6, 6);
		 
		 rt.title.merge(7,3, 7, 4);
		 rt.title.merge(7,5, 7, 6);
		 
		 rt.title.setCellAlign(7, 3, Table.ALIGN_CENTER);
		 rt.title.setCellAlign(7, 4, Table.ALIGN_CENTER);
		 rt.title.setCellAlign(7, 5, Table.ALIGN_RIGHT);
		 rt.title.setCellAlign(7, 6, Table.ALIGN_RIGHT);
		 
		 
		 rt.title.setBorder(0,0,0,0);
		 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
		 rt.title.setRowCells(7,Table.PER_BORDER_BOTTOM,0);
		 
		 
		 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
		 rt.title.setRowCells(7,Table.PER_BORDER_RIGHT,0);
		 
		 
		 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
//		 rt.title.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_LEFT);
		 
		 
		 
		// 字体
		 rt.title.setCells(3, 1, 3, 6, Table.PER_FONTNAME, "黑体");
		 rt.title.setCells(3, 1, 3, 6, Table.PER_FONTSIZE, 13);
		 rt.title.setCells(4, 1, 4, 6, Table.PER_FONTNAME, "Arial Unicode MS");
		 rt.title.setCells(4, 1, 4, 6, Table.PER_FONTSIZE, 11);
		 rt.title.setCells(5, 1, 5, 6, Table.PER_FONTNAME, "隶书");
		 rt.title.setCells(5, 1, 5, 6, Table.PER_FONTSIZE, 20);
//		 字体				 
		 
//		 图片
		 rt.title.setCellImage(6, 1, rt.title.getWidth()*2/3+60, 10, "imgs/report/GDHX.gif");
		 
		 
		 
		String str = DateUtil.FormatDate(new Date());
		
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "燃料部盖章:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "验收人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "经办人：" , Table.ALIGN_LEFT);

		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(9, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setColFormat(new String[]{"0.00","0.00","0.00","0.00","0.00","0.00"});
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		
		for (int i = 1; i < 9; i++) {
			for (int j = 0; j < 6; j++) {
			
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
	

		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(1, 1, 9, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.merge(1, 4, 1, 6);
		rt.body.merge(2, 4, 2, 6);
		
		rt.body.merge(3, 1, 4, 1);
		rt.body.merge(3, 3, 3, 6);
		
		rt.body.merge(4, 3, 4, 6);
		
		rt.body.merge(5, 1, 9, 2);
		
		rt.body.merge(5, 3, 5, 4);
		rt.body.merge(5, 5, 5, 6);
		
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(45);

		return rt.getAllPagesHtml();
	}

	
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		
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

	
    private String jiesdbm="";
    
    
	

	public String getJiesdbm() {
		return jiesdbm;
	}

	public void setJiesdbm(String jiesdbm) {
		this.jiesdbm = jiesdbm;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
//			visit.setActivePageName(getPageName().toString());
		}
		
		this.setJiesdbm("");
		
		jiesdbm=cycle.getRequestContext().getRequest().getParameter("lx");
		
//		System.out.println(jiesdbm);
//		this.setJiesdbm("GD-DT-DTYQ-0907-001");
		
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}




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
