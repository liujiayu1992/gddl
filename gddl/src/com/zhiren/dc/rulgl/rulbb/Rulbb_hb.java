package com.zhiren.dc.rulgl.rulbb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;

public class Rulbb_hb extends BasePage implements PageValidateListener{
	private static final String RULBB = "Rulbb";//入炉报表
	private static final String RULTZ = "Rultz";//入炉台帐
//		界面用户提示
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
	
//		绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//		绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
//		页面变化记录
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
//		
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
	
	public IPropertySelectionModel Diancjb;
	public void setDiancjb() {
		String sql = "select id,jib from diancxxb";
		Diancjb =new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getToolbar();
		}
	}

	public String getPrintTable() {
		Visit visit = (Visit)this.getPage().getVisit();
		if (RULBB.equals(visit.getString1())) {
			return getRulbb();
		} else if (RULTZ.equals(visit.getString1())) {
			return getRultz();
		}else {
			return "无此报表";
		}
	}

	private String getRulbb(){
		JDBCcon con = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "";
		int iFixedRows = 0;// 固定行号
		String titledate = "";// 标题日期
		titledate = getBRiq() + "至" + getERiq() + " 入炉煤质量报表";
		String strSqlwhere = "";
		String strSqlhaving = "";
		if("3".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
			strSqlwhere = " and d.id = " + getTreeid();
			strSqlhaving = " having (grouping(d.mingc) = 0 and grouping(i.xuh)+grouping(i.mingc)<>1)";
		}else 
		if("2".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
			strSqlwhere = " and fgs.id = " + getTreeid();
			strSqlhaving = " having not (grouping(fgs.mingc)=1 and grouping(i.xuh)+grouping(i.mingc)<>1)";
		}else 
		if("1".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
			strSqlhaving = " having not (grouping(z.rulrq)=0 and grouping(i.xuh)+grouping(i.mingc)<>1)";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping( fgs.mingc)+grouping(d.mingc),2,'大唐集团',1,fgs.mingc,d.mingc) as dianc, \n")
		.append("decode(z.rulrq,null,'合计',to_char(z.rulrq,'yyyy-mm-dd')) rlrq, \n")
		.append("decode(grouping(i.xuh)+grouping(i.mingc), 2, '合计',1,null,i.mingc) rulbz, \n")
		.append("round(decode(avg(z.mt),0,0,avg(z.mt)),1) mt, \n")
		.append("round(decode(avg(z.mad),0,0,avg(z.mad)),2) mad, \n")
		.append("round(decode(avg(z.aad),0,0,avg(z.aad)),2) aad,  \n")
		.append("round(decode(avg(z.ad),0,0,avg(z.ad)),2) ad, \n")
		.append("round(decode(avg(z.aar),0,0,avg(z.aar)),2) aar, \n")
		.append("round(decode(avg(z.vad),0,0,avg(z.vad)),2) vad, \n")
		.append("round(decode(avg(z.vdaf),0,0,avg(z.vdaf)),2) vdaf, \n")
		.append("round(decode(avg(z.qbad),0,0,avg(z.qbad)),2)*1000 qbad, \n")
		.append("round(decode(avg(z.qnet_ar),0,0,avg(z.qnet_ar)),2)*1000 qnet_ar, \n")
		.append("round(round(decode(avg(z.qnet_ar),0,0,avg(z.qnet_ar)),2)*1000/4.1816,0) qnet_ar1, \n")
		.append("round(decode(avg(z.stad),0,0,avg(z.stad)),2) stad, \n")
		.append("round(decode(avg(z.std),0,0,avg(z.std)),2) std, \n")
		.append("ROUND_NEW((100 - round(decode(avg(z.mt),0,0,avg(z.mt)),1)) \n")
		.append("*round(decode(avg(z.stad),0,0,avg(z.stad)),2) / (100 - round(decode(avg(z.mad),0,0,avg(z.mad)),2)), 2) star, \n")
		.append("round(decode(avg(z.hdaf),0,0,avg(z.hdaf)),2) hdaf, \n")
		.append("round(decode(avg(z.had),0,0,avg(z.had)),2) had  \n")
		.append("from rulmzlzb z,diancxxb d ,vwfengs fgs,item i  \n")
		.append("where z.diancxxb_id = d.id and z.rulbzb_id=i.id and d.fuid = fgs.id(+) and z.qnet_ar <> 0  \n")
		.append("and z.rulrq >=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and z.rulrq <=").append(DateUtil.FormatOracleDate(getERiq()))
		
		/*
		 * 修改人：ww
		 * 时间：2009-06-19
		 * 修改内容：
		 * 		修改上sql语句  d.fuid=fgs.id 为 d.fuid=fgs.id(+)
		 * 		确保没有上级公司时也能显示数据
		 */
/**
 * huochaoyuan2009-02-17修改上边sql语句，原来为"<",导致不能完全查询数据，修改为“<=”
 * 
**/
		.append(strSqlwhere).append(" \n")
		.append(" group by rollup(fgs.mingc, d.mingc,z.rulrq,i.xuh,i.mingc) \n")
		.append(strSqlhaving);

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[][]{{"单位","入炉日期","入炉班组", "全水分(%)MT","空气干燥基水分(%)Mad","空气干燥基灰分(%)Aad","干燥基灰分(%)Ad","收到基灰分(%)Aar","空气干燥基挥发分(%)Vad","干燥无灰基挥发分(%)Vdaf","弹筒发热量(J/g)Qb,ad","收到基低位发热量(J/g)Qnet,ar","发热量(Kcal/kg)","全硫分(%)St,ad","干燥基硫(%)St,d","收到基硫(%)St,ar","干燥无灰基氢(%)Hdaf","空干基氢(%)Had"}};
		ArrWidth = new int[] {100,70,65,65,65,65,65,65,65,65,65,65,65,65,65,65};
		iFixedRows = 1;
		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows));
//			设置表头
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 5, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 4, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 3, "单位：吨、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//			rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 5, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 4, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		// 设置页数
		 if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		con.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}

	private String getRultz() {
		JDBCcon con = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "";
		int iFixedRows = 0;// 固定行号
		String titledate = "";// 标题日期
		titledate = getBRiq() + "至" + getERiq() + " 入炉煤质量台帐";
		StringBuffer sb = new StringBuffer();
//			sb.append("select to_char(z.rulrq,'yyyy-mm-dd') rulrq, b.mingc banz, j.mingc jiz, to_char(z.fenxrq,'yyyy-mm-dd') fenxrq, h.fadhy+h.gongrhy meil, \n")
//			.append("round(z.mt,1) mt, round(z.qnet_ar,2) qnet_ar, round(z.mad,2) mad, round(z.aad,2) aad, \n")
//			.append("round(z.ad,2) ad, round(z.vad,2) vad, round(z.vdaf,2) vdaf, round(z.qbad,2) qbad, \n")
//			.append("round(z.qgrad,2) qgrad, round(z.stad,2) stad, round(z.hdaf,2) hdaf  \n")
//			.append("from rulmzlb z, rulbzb b, jizfzb j, meihyb h \n")
//			.append("where z.rulbzb_id = b.id and z.jizfzb_id = j.id and h.rulmzlb_id = z.id \n")
//			.append("and z.rulrq >=").append(DateUtil.FormatOracleDate(getBRiq()))
//			.append(" and z.rulrq <").append(DateUtil.FormatOracleDate(getERiq()))
//			.append(" \n")
//			.append("and z.diancxxb_id=").append(getTreeid());
		
		sb.append("select decode(z.rulrq,null,'合计',to_char(z.rulrq,'yyyy-mm-dd')) rlrq,b.mingc banz, j.mingc jiz,to_char(z.fenxrq,'yyyy-mm-dd') fenxrq,sum(h.fadhy+h.gongrhy+h.feiscy+h.qity) meil, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mt)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),1) mt, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) mad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.aad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) aad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.ad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.aar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) aar, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.vad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) vad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.vdaf)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) vdaf, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qbad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000 qbad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qnet_ar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000 qnet_ar, \n")
		.append("round(round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qnet_ar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000/4.1816,0) qnet_ar1, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.stad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) stad, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.std)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) std, \n")
		.append("ROUND_NEW((100 - round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mt)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),1))\n")
		.append("*round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.stad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) / (100 - round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)), 2) star, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.hdaf)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) hdaf, \n")
		.append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.had)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) had \n")
		.append("from rulmzlb z,meihyb h,diancxxb d , (select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ') b, jizfzb j \n")
		.append("where h.rulmzlb_id = z.id and z.diancxxb_id = d.id  \n")
		.append(" and z.rulbzb_id = b.id and z.jizfzb_id = j.id \n")
		.append("and z.rulrq >=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and z.rulrq <=").append(DateUtil.FormatOracleDate(getERiq()))
/**
 * huochaoyuan2009-02-17修改上边sql语句，原来为"<",导致不能完全查询数据，修改为“<=”
 * 
**/		
		.append(" and (d.id = ").append(getTreeid()).append(" or d.fuid = ").append(getTreeid()).append(") \n")
		.append(" group by rollup(z.rulrq,b.mingc,j.mingc,z.fenxrq) \n")
		.append(" having not(grouping(z.fenxrq)=1 and grouping(z.rulrq)<>1)");

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[][]{{"入炉日期", "班组", "机组", "分析日期", "煤量(吨)", "全水分(%)Mt","空气干燥基水分(%)Mad","空气干燥基灰分(%)Aad","干燥基灰分(%)Ad","收到基灰分(%)Aar","空气干燥基挥发分(%)Vad","干燥无灰基挥发分(%)Vdaf","弹筒发热量(J/g)Qb,ad","收到基低位发热量(J/g)Qnet,ar","发热量(Kcal/kg)","全硫分(%)St,ad","干燥基硫(%)St,d","收到基硫(%)St,ar","干燥无灰基氢(%)Hdaf","空干基氢(%)Had"}};
		ArrWidth = new int[] {70, 70, 70, 70, 65,65,65,65,65,65,65,65,65,65,65,65,65,65};
		iFixedRows = 1;
		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows));
//			设置表头
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 6, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 5, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 4, "单位：吨、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//			rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 6, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 5, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 4, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		// 设置页数
		 if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		con.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
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
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		setToolbar(tb1);
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
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
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
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//		 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
//				//setBRiq(DateUtil.FormatDate(new Date()));
//				//setERiq(DateUtil.FormatDate(new Date()));
			visit.setString1(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setDiancmcModels();
			setDiancjb();
			if(reportType != null) {
				//setBRiq(DateUtil.FormatDate(new Date()));
				//setERiq(DateUtil.FormatDate(new Date()));
				visit.setString1(reportType);
			}else
			{
				visit.setString1(RULBB);
			}
			getToolBars();
/**
 * huochaoyuan2009-02-17修改上边语句，修改时间条件框中的时间总是刷新的问题
 */			
		}
/*
 * 修改人：ww
 * 时间：2009-06-19
 * 修改内容：
 * 			注释以下内容，防止“入炉检质台帐”中刷新页面时跳入“入炉报表”页面
 * 			初始化时当"lx"参数为"null"时默认的为"Rulbb&lx=Rulbb"
 */	
		
//			if(reportType != null) {
//				//setBRiq(DateUtil.FormatDate(new Date()));
//				//setERiq(DateUtil.FormatDate(new Date()));
//				visit.setString1(reportType);
//			}else
//			{
//				visit.setString1(RULBB);
//			}
		
		getToolBars();
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
}
