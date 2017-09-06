package com.zhiren.dc.rulgl.rulbb;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
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
/*
 * 作者：张立东
 * 时间：2009-10-30
 * 描述：增加入炉台帐分机组，资源：Rulbb&lx=Rultz_Jz
 */
/*
 * 修改日期：2010-01-25
 * 人员：liht
 * 内容：增加“固定碳”一列，资源：Rulbb&lx=Rulbb，Rulbb&lx=Rultz
 */
/*
* 修改人：ww
* 时  间：2010-09-02 
* 内  容：去掉在查询中重复使用getJiaqsl()，而生成大量的JDBCcon对象产生连接失败错误;
*/
/*
 * 作者：夏峥
 * 时间：2011-09-02
 * 描述：修正getRultz()方法，在对质量信息进行加权时使用Qnet_ar的值作为加权的先决条件。
 */
/*
 * 作者：夏峥
 * 时间：2012-03-02
 * 描述：修正getRultz()方法，新增收到基挥发份字段。
 */
/*
 * 作者：夏峥
 * 时间：2012-10-24
 * 描述：修正Rulbb的查询方式，当选择总厂时显示总计行
 */
/*
 * 作者：夏峥
 * 时间：2013-07-09
 * 描述：修正Rulbb和Rultz中qnetar的小数保留位数为3位。
 */
/*
 * 作者：夏峥
 * 时间：2013-07-12
 * 描述：修正Rultz中除数为0的BUG。
 */

/*
 * 作者：陈环红
 * 时间：2017-01-17
 * 描述：修正Rultz中除数为0的BUG。
 */
public class Rulbb extends BasePage implements PageValidateListener{
	private static final String RULBB = "Rulbb";//入炉报表
	private static final String RULTZ = "Rultz";//入炉台帐
	private static final String RULTZ_JZ = "Rultz_Jz";//分机组入炉台帐
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
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		if ("".equals(briq) || briq==null) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			briq = DateUtil.FormatDate(stra.getTime());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
	public  String getJiaqsl(){
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String jiaqsl="h.fadhy+h.gongrhy+h.feiscy+h.qity";
		String sql="select zhi from xitxxb where mingc='入炉化验加权数量使用值' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			jiaqsl = rsl.getString("zhi");
		}
		rsl.close();
		return jiaqsl;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		if ("".equals(eriq) || eriq==null) {
			eriq = DateUtil.FormatDate(new Date());
		}
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
		}else if (RULTZ_JZ.equals(visit.getString1())) {
			return getRuljztz();
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
//		String strSqlhaving = "";
		if("3".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
			strSqlwhere = " and (d.id = " + getTreeid()+" or d.fuid=" + getTreeid()+")";
//			strSqlhaving = " having (grouping(d.mingc)=0)";
		}else 
		if("2".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
			strSqlwhere = " and fgs.id = " + getTreeid();
//			strSqlhaving = " having not (grouping(fgs.mingc)=1)";
		}else 
		if("1".equals(((IDropDownModel)Diancjb).getBeanValue(Long.parseLong(getTreeid())))) {
//			strSqlhaving = " having not (grouping(z.rulrq)=0)";
		}
		String strJiaqsl = getJiaqsl();
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping( fgs.mingc)+grouping(d.mingc),2,'总计',1,fgs.mingc,d.mingc) as dianc,\n");
		sb.append("decode(grouping(z.rulrq)+GROUPING(d.mingc),1,'合计',to_char(z.rulrq,'yyyy-mm-dd')) rlrq,sum("+strJiaqsl+") meil, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.mt)/sum("+strJiaqsl+")),0),1),'90.9') mt, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.mad)/sum("+strJiaqsl+")),0),2),'90.99') mad, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.aad)/sum("+strJiaqsl+")),0),2),'90.99')  aad, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.ad)/sum("+strJiaqsl+")),0),2),'90.99')  ad, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.aar)/sum("+strJiaqsl+")),0),2),'90.99')  aar, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.vad)/sum("+strJiaqsl+")),0),2),'90.99')  vad, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.vdaf)/sum("+strJiaqsl+")),0),2),'90.99')  vdaf, \n")
		.append("round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.qbad)/sum("+strJiaqsl+")),0),2)*1000 qbad, \n")
		.append("round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.qnet_ar)/sum("+strJiaqsl+")),0),3)*1000 qnet_ar, \n")
		.append("round_new(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.qnet_ar)/sum("+strJiaqsl+")),0),3)*1000/4.1816,0) qnet_ar1, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.stad)/sum("+strJiaqsl+")),0),2),'90.99')  stad, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.std)/sum("+strJiaqsl+")),0),2),'90.99')  std, \n")
		.append("to_char(ROUND_NEW((100 - round(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.mt)/sum("+strJiaqsl+")),0),1))\n")
		.append("*round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.stad)/sum("+strJiaqsl+")),0),2) / (100 - round(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.mad)/sum("+strJiaqsl+")),0),2)), 2),'90.99')  star, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.hdaf)/sum("+strJiaqsl+")),0),2),'90.99')  hdaf, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.had)/sum("+strJiaqsl+")),0),2),'90.99')  had, \n")
		.append("to_char(round_new(nvl(decode(sum("+strJiaqsl+"),0,0,sum(("+strJiaqsl+")*z.fcad)/sum("+strJiaqsl+")),0),2),'90.99')  fcad \n")
		.append("from rulmzlb z,meihyb h,diancxxb d ,vwfengs fgs \n")
		.append("where h.rulmzlb_id = z.id and (z.diancxxb_id = d.id or d.fuid=z.diancxxb_id) and d.fuid=fgs.id(+) and z.qnet_ar<>0\n")
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
		.append(" group by rollup((fgs.mingc,d.mingc),z.rulrq) \n")
		.append("ORDER BY GROUPING(fgs.mingc)DESC, GROUPING(d.mingc)DESC, d.mingc,grouping(z.rulrq)desc,z.rulrq\n");
//		.append(strSqlhaving);

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[][]{{"单位","入炉日期","煤量<br>(吨)", "全水分<br>(%)<br>MT","空气干燥基水分<br>(%)<br>Mad","空气干燥基灰分<br>(%)<br>Aad","干燥基灰分<br>(%)<br>Ad","收到基灰分<br>(%)<br>Aar","空气干燥基挥发分<br>(%)<br>Vad","干燥无灰基挥发分<br>(%)<br>Vdaf","弹筒发热量<br>(J/g)<br>Qb,ad","收到基低位发热量<br>(J/g)<br>Qnet,ar","收到基低位发热量<br>(Kcal/kg)<br>Qnet,ar","全硫分<br>(%)<br>St,ad","干燥基硫<br>(%)<br>St,d","收到基硫<br>(%)<br>St,ar","干燥无灰基氢<br>(%)<br>Hdaf","空干基氢<br>(%)<br>Had","固定碳<br>(%)<br>Fcad"}};
		ArrWidth = new int[] {100,70,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65};
       /* String[] strFormat = new String[] { "","", "0.00", "0.0",
                "0.00", "0.00", "0.00","0.00",
                "0.00",  "0.00", "",  "",
                "","0.00" , "0.00",  "0.00",
                "0.00",  "0.00", "0.00"
        };
        rt.body.setColFormat(strFormat);*/
		iFixedRows = 1;
		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows));
//		设置表头
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
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
		rt.body.setPageRows(Report.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		rt.body.setColFormat(ArrFormat);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
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
/*
 * 2009-11-06 
 * 修改人：liangll 
 * 修改内容：修改下边sql，增加jiz字段的汇总数据
 */
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
		String strJiaqsl = getJiaqsl();

		sb.append("select decode(grouping(z.rulrq),1,'合计',to_char(z.rulrq,'yyyy-mm-dd')) rlrq,\n");
		sb.append("b.mingc banz,decode(grouping(z.rulrq)+grouping(b.mingc)+grouping(j.mingc),2,'合计',2,'',3,'',j.mingc) as jiz ,\n"); 
		sb.append("to_char(z.fenxrq,'yyyy-mm-dd') fenxrq,\n"); 
		sb.append("sum(h.fadhy+h.gongrhy+h.feiscy+h.qity) meil,\n"); 
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(mt,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.mt)/sum(decode(nvl(mt,0),0,0,"+strJiaqsl+"))),0),1),'90.9') mt,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(mad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.mad)/sum(decode(nvl(mad,0),0,0,"+strJiaqsl+"))),0),2),'90.99') mad,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(aad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.aad)/sum(decode(nvl(aad,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  aad,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(ad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.ad)/sum(decode(nvl(ad,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  ad,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(aar,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.aar)/sum(decode(nvl(aar,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  aar,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(vad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.vad)/sum(decode(nvl(vad,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  vad,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(vdaf,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.vdaf)/sum(decode(nvl(vdaf,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  vdaf,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(var,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.var)/sum(decode(nvl(var,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  var,\n");
		sb.append("round_new(nvl(decode(sum(decode(nvl(qbad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.qbad)/sum(decode(nvl(qbad,0),0,0,"+strJiaqsl+"))),0),2)*1000 qbad,\n");
		sb.append("round_new(nvl(decode(sum(decode(nvl(qnet_ar,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.qnet_ar)/sum(decode(nvl(qnet_ar,0),0,0,"+strJiaqsl+"))),0),3)*1000 qnet_ar,\n");
		sb.append("round_new(round_new(nvl(decode(sum(decode(nvl(qnet_ar,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.qnet_ar)/sum(decode(nvl(qnet_ar,0),0,0,"+strJiaqsl+"))),0),3)*1000/4.1816,0) qnet_ar1,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(stad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.stad)/sum(decode(nvl(stad,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  stad,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(std,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.std)/sum(decode(nvl(std,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  std,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(round_new((100-Mt)*stad/(100-mad),2),0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*round_new((100-Mt)*stad/(100-mad),2))/sum(decode(nvl(round_new((100-Mt)*stad/(100-mad),2),0),0,0,"+strJiaqsl+"))),0),2),'90.99')  std,\n");
		
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(hdaf,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.hdaf)/sum(decode(nvl(hdaf,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  hdaf,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(had,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.had)/sum(decode(nvl(had,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  had,\n");
		sb.append("to_char(round_new(nvl(decode(sum(decode(nvl(fcad,0),0,0,"+strJiaqsl+")),0,0,sum(("+strJiaqsl+")*z.fcad)/sum(decode(nvl(fcad,0),0,0,"+strJiaqsl+"))),0),2),'90.99')  fcad\n");
		sb.append(	"from rulmzlb z,meihyb h,diancxxb d , rulbzb b, jizfzb j\n"); 
		sb.append("where h.rulmzlb_id = z.id and z.diancxxb_id = d.id\n"); 
		sb.append(" and z.rulbzb_id = b.id and z.jizfzb_id = j.id\n"); 
		sb.append(" and z.rulrq >="+DateUtil.FormatOracleDate(getBRiq())+" and z.rulrq <="+DateUtil.FormatOracleDate(getERiq())+" \n");
		sb.append(" and (z.diancxxb_id="+getTreeid()+" or d.fuid="+getTreeid()+")\n"); 
		sb.append(" group by rollup(z.rulrq,b.mingc,j.mingc,z.fenxrq)\n"); 
		sb.append(" having not(grouping(b.mingc)+grouping(z.fenxrq)=1)\n");
		sb.append(" order by grouping(z.rulrq)desc,z.rulrq,grouping(b.mingc)desc,b.mingc");

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[][]{{"入炉日期", "班组", "机组", "分析日期", "煤量<br>(吨)", "全水分<br>(%)<br>Mt","空气干燥基水分<br>(%)<br>Mad","空气干燥基灰分<br>(%)<br>Aad","干燥基灰分<br>(%)<br>Ad","收到基灰分<br>(%)<br>Aar","空气干燥基挥发分<br>(%)<br>Vad","干燥无灰基挥发分<br>(%)<br>Vdaf","收到基挥发分<br>(%)<br>Var","弹筒发热量<br>(J/g)<br>Qb,ad","收到基低位发热量<br>(J/g)<br>Qnet,ar","收到基低位发热量<br>(Kcal/kg)<br>Qnet,ar","全硫分<br>(%)<br>St,ad","干燥基硫<br>(%)<br>St,d","收到基硫<br>(%)<br>St,ar","干燥无灰基氢<br>(%)<br>Hdaf","空干基氢<br>(%)<br>Had","固定碳<br>(%)<br>Fcad"}};
		ArrWidth = new int[] {70, 70, 70, 70, 65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65};
		iFixedRows = 1;
		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows+2));
		for (int i = 1; i <= 20; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
//		设置表头
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.body.ShowZero=false;
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 6, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 5, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(19, 2, "单位：吨、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		rt.body.setColFormat(ArrFormat);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 6, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 5, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(19, 2, "制表：", Table.ALIGN_RIGHT);
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
	private String getRuljztz() {
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
	
		sb.append("select decode(z.jiz,null,'合计',z.jiz) as jiz,\n")
		.append(" decode(z.riq,null,decode(z.jiz,null,'合计','小计'),to_char(z.riq,'yyyy-mm-dd')) as riq,\n")
		.append(" sum(meil) as rulml,decode(sum(meil),0,0,round_new(sum(qnet_ar*(meil))/sum(meil),2))qnet_ar,\n")
		.append(" round_new(decode(sum(meil),0,0,round_new(sum(qnet_ar*(meil))/sum(meil),2))/0.0041816,0) qnet_ar_K,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(qbad*(meil))/sum(meil),2))qbad,decode(sum(meil),0,0,round_new(sum(aad*(meil))/sum(meil),2))aad,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(ad*(meil))/sum(meil),2))ad,decode(sum(meil),0,0,round_new(sum(aar*(meil))/sum(meil),2)) aar,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(mt*(meil))/sum(meil),1))mt,decode(sum(meil),0,0,round_new(sum(mad*(meil))/sum(meil),2)) mad,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(stad*(meil))/sum(meil),2))stad,decode(sum(meil),0,0,round_new(sum(std*(meil))/sum(meil),2)) std,decode(sum(meil),0,0,round_new(sum(star*(meil))/sum(meil),2)) star,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(vdaf*(meil))/sum(meil),2))vdaf,decode(sum(meil),0,0,round_new(sum(vad*(meil))/sum(meil),2))vad,\n")
		.append(" decode(sum(meil),0,0,round_new(sum(fcad*(meil))/sum(meil),2))fcad,decode(sum(meil),0,0,round_new(sum(fhz*(meil))/sum(meil),2))fhz\n")
		.append(" from (select r.rulrq,j.mingc as jiz,sum(fadhy+gongrhy+qity) as meil,decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(qnet_ar*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2))qnet_ar,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(qbad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) qbad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(aad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) aad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(ad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) ad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(aar*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) aar,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(mt*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),1))mt,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(mad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) mad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(stad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) stad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(std*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) std,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(round_new(stad*(100-mt)/(100-mad),2)*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) star,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(vdaf*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2)) vdaf,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(vad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2))vad,\n")
		.append(" decode(sum(fadhy+gongrhy+qity),0,0,round_new(sum(fcad*(fadhy+gongrhy+qity))/sum(fadhy+gongrhy+qity),2))fcad\n")
		.append(" from  zgdt.rulmzlb r,zgdt.meihyb m,jizfzb j where r.shenhzt=3 and m.rulmzlb_id=r.id and r.jizfzb_id=j.id\n")
		.append(" group by r.rulrq,j.mingc)b,\n")
		.append(" (select riq,jiz,sum(fhz)fhz,sum(dzz)dzz,sum(szmz)szmz\n")
		.append(" from(\n")
		.append(" select r.riq,j.jizbh as jiz,round(sum(zhi)/count(*),3) fhz,0 dzz,0 szmz\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='FH' and r.jizb_id=j.id \n")
		.append(" group by  r.riq,j.jizbh union\n")
		.append(" select r.riq,j.jizbh as jiz,0 fhz,round_new(sum(zhi)/count(*),3) dzz,0 szmz\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='DZ' and r.jizb_id=j.id \n")
		.append(" group by  r.riq,j.jizbh union\n")
		.append(" select r.riq,j.jizbh as jiz,0 fhz,0 dzz,round_new(sum(zhi)/count(*),3) szmz\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='SZM' and r.jizb_id=j.id \n")
		.append(" group by  r.riq,j.jizbh\n")
		.append(" ) group by riq,jiz\n")
		.append(" )d,(select distinct riq,jiz,diancxxb_id from ( select r.rulrq as riq,j.mingc as jiz,r.diancxxb_id from zgdt.rulmzlb r,zgdt.meihyb m,jizfzb j\n")
		.append(" where r.shenhzt=3 and m.rulmzlb_id=r.id and r.jizfzb_id=j.id\n")
		.append(" union select riq,jiz,diancxxb_id from(\n")
		.append(" select r.riq,jizbh as jiz,r.diancxxb_id\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='FH' and r.jizb_id=j.id\n")
		.append(" group by  r.riq,j.jizbh,r.diancxxb_id\n")
		.append(" union select r.riq,jizbh as jiz,r.diancxxb_id\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='DZ' and r.jizb_id=j.id\n")
		.append(" group by  r.riq,j.jizbh,r.diancxxb_id\n")
		.append(" union select r.riq,jizbh as jiz,r.diancxxb_id\n")
		.append(" from zgdt.rulqtzbb r,zgdt.item i,jizb j\n")
		.append(" where r.item_id=i.id and i.bianm='SZM' and r.jizb_id=j.id\n")
		.append(" group by  r.riq,j.jizbh,r.diancxxb_id\n")
		.append(" )group by riq,jiz,diancxxb_id)) z  where  z.riq=b.rulrq(+) and z.riq=d.riq(+) \n")
		.append(" and z.jiz=b.jiz(+) and z.jiz=d.jiz(+)\n")
		.append("and z.riq >=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and z.riq <=").append(DateUtil.FormatOracleDate(getERiq()))
		.append(" and z.diancxxb_id=").append(getTreeid())
		.append(" group by rollup(z.jiz,z.riq)");

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[][]{{"机组","入炉日期",  "煤量(吨)", "收到基低位发热量(J/g)Qnet,ar","发热量(Kcal/kg)","弹筒发热量(J/g)Qb,ad","空气干燥基灰分(%)Aad","干燥基灰分(%)Ad","收到基灰分(%)Aar","全水分(%)Mt","空气干燥基水分(%)Mad","全硫分(%)St,ad","干燥基硫(%)St,d","收到基硫(%)St,ar","干燥无灰基挥发分(%)Vdaf","空气干燥基挥发分(%)Vad","固定碳(%)Fcad","飞灰可燃物(%)"}};
		ArrWidth = new int[] {50, 70, 70, 65,65,65,65,65,65,65,65,65,65,65,65,65,65,65};
		iFixedRows = 1;
		// 数据
		rt.setBody(new Table(rs, 1, 0, iFixedRows));
//		设置表头
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 18);
		rt.setDefaultTitle(1, 6, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 5, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 5, "单位：吨、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		rt.body.setColFormat(ArrFormat);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 6, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 5, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 5, "制表：", Table.ALIGN_LEFT);
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
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		/*if(){//从report类中调用方法pagerStyle并赋值给本类里定义的局部变量
		 * 方法里的说明：如果页面传过来的是null那么就赋予A4的值，否则就按照系统信息表
			
		}*/
//		paperStyle();//初始化关于报表的A4格式值（zhi）
		if(reportType != null) {
//			//setBRiq(DateUtil.FormatDate(new Date()));
//			//setERiq(DateUtil.FormatDate(new Date()));
			visit.setString1(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			setBRiq(DateUtil.FormatDate(stra.getTime()));
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
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
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
		
//		if(reportType != null) {
//			//setBRiq(DateUtil.FormatDate(new Date()));
//			//setERiq(DateUtil.FormatDate(new Date()));
//			visit.setString1(reportType);
//		}else
//		{
//			visit.setString1(RULBB);
//		}
		
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