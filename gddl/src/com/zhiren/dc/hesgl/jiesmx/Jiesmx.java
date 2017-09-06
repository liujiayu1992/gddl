//2008-10-12 chh 
//修改内容 :加入电厂树，公司的可以选择电厂查看报告

package com.zhiren.dc.hesgl.jiesmx;

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
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesmx extends BasePage implements PageValidateListener {

	private boolean isBegin;

	private List listMeicMC = null;

	private List listMeicMiD = null;

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 报表初始设置
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

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	结算编号下拉框
	public IDropDownBean getJiesbhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJiesbhModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJiesbhModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJiesbhValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJiesbhModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJiesbhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getJiesbhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getJiesbhModels() {
		String strGongsID = "";

		strGongsID=" and (dc.id= " +this.getTreeid()+" or dc.fuid="+this.getTreeid()+")";
		
		String sql = "select j.id,j.bianm from jiesb j,vwdianc dc "+
			 		" where j.diancxxb_id=dc.id " +strGongsID
			 	  + " order by j.id desc ";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	public long getJiesID() {
		int id = -1;
		if (getJiesbhValue() == null) {
			return id;
		}
		return getJiesbhValue().getId();
	}
	
	public String getTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sb="";
		int diancid = 0;
		String jihkj = "";
		String sql = "select distinct f.diancxxb_id,j.mingc from fahb f,jihkjb j where f.jihkjb_id=j.id and f.jiesb_id="+getJiesID();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			diancid = rs.getInt("diancxxb_id");
			jihkj = rs.getString("mingc");
		}
		
		if (diancid==510){
			if(jihkj.equals("市场采购")){
				sb = "国电宁夏石嘴山发电有限公司地方煤结算单";
			}else{
				sb = "国电宁夏石嘴山发电有限公司统配煤结算单";
			}
		}else if(diancid==509){
			if(jihkj.equals("市场采购")){
				sb = "国电石嘴山第一发电有限公司地方煤结算单";
			}else{
				sb = "国电石嘴山第一发电有限公司统配煤结算单";
			}
		}
		return sb;
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}


	public long getJiesbhID() {
		int id = -1;
		if (getJiesbhValue() == null) {
			return id;
		}
		return getJiesbhValue().getId();
	}

	public String getRiq(JDBCcon con) {
		String sDate = "";
		String sql = "select riq from pandb where id=" + getJiesbhID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			sDate = DateUtil.Formatdate("yyyy 年 MM 月 dd 日", rsl.getDate("riq"));
		}
		if (sDate.equals("")) {
			sDate = DateUtil.Formatdate("yyyy 年 MM 月 dd 日", new Date());
		}
		return sDate;

	}

	// submit
	public void submit(IRequestCycle cycle) {

	}

	// 报表主体
	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		JDBCcon con = new JDBCcon();
		Document doc = new Document();
		doc.addParagraph(getBiaot(con));
		doc.addParagraph(getMingx(con));
		doc.addParagraph(getJiesqk(con));
		doc.addParagraph(getHuay(con));
		con.Close();
		return doc.getHtml();
	}

	public Paragraph getBiaot(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(4,8);
		tb.setCellValue(1, 1, "结算单号：");
		tb.setCellValue(2, 1, "结算人：");
		tb.setCellValue(3, 1, "合同煤款：");
		tb.setCellValue(4, 1, "结算说明：");
		tb.setCellValue(1, 3, "结算日期：");
		tb.setCellValue(2, 3, "结算标志：");
		tb.setCellValue(3, 3, "到厂合同价：");
		tb.setCellValue(1, 5, "供应商：");
		tb.setCellValue(2, 5, "合同编号：");
		tb.setCellValue(2, 7, "合同煤价：");
		//合并单元格
		tb.merge(1, 6, 1, 8);
		tb.merge(3, 4, 3, 8);
		tb.merge(4, 2, 4, 8);
		String sql = "select distinct j.bianm,j.ranlbmjbr,(hj.jij-hj.yunj)*hs.hetl as hetjg,j.jiesrq,decode(j.ruzrq,null,'否','是') as jiesbz, \n"
					+ "hj.jij,g.quanc,ht.hetbh,(hj.jij-hj.yunj) as htmj, \n"
					+ "'结算期:'||to_char(j.fahksrq,'yyyy-mm-dd')||'至'||to_char(j.fahjzrq,'yyyy-mm-dd')||'。'||j.beiz as beiz \n"
					+ "from jiesb j,hetb ht,hetjgb hj,hetslb hs,gongysb g \n"
					+ "where j.hetb_id=ht.id and hj.hetb_id=ht.id and hs.hetb_id=ht.id and j.id="+getJiesID()+" \n"
					+ "and g.id=j.gongysb_id ";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			tb.setCellValue(1, 2, rs.getString("bianm"));
			tb.setCellValue(2, 2, rs.getString("ranlbmjbr"));
			tb.setCellValue(3, 2, rs.getString("hetjg"));
			tb.setCellValue(4, 2, rs.getString("beiz"));
			tb.setCellValue(1, 4, DateUtil.FormatDate(rs.getDate("jiesrq")));
			tb.setCellValue(2, 4, rs.getString("jiesbz"));
			tb.setCellValue(3, 4, rs.getString("jij"));
			tb.setCellValue(1, 6, rs.getString("quanc"));
			tb.setCellValue(2, 6, rs.getString("hetbh"));
			tb.setCellValue(2, 8, rs.getString("htmj"));
		}
		int ArrWidth[] = new int[] { 60, 120, 60, 120, 60, 120, 60, 80  };
		int TitleWidth[] = new int[] {60, 120, 60, 120, 60, 120, 60, 80 };
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setBody(tb);
		rt.setTitle(getTitle(),TitleWidth);
		rt.body.setBorderNone();
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_LEFT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
				Table.PER_BORDER_TOP, 0);
		bt.addText(rt.getAllPagesHtml());
		return bt;
	}
	
	public Paragraph getMingx(JDBCcon con){
		Paragraph bt = new Paragraph();
		String sql = "select mk.mingc as meikmc,z.huayy,z.huaybh,f.jingz,decode(dp.zhekfs,1,'扣吨','扣价') as koujfs, \n"
					+"dp.hetbz,zb.mingc,dp.jies,decode(dp.zhekfs,1,0,dp.zhejbz) as zhejbz,decode(dp.zhekfs,1,dp.yanssl-dp.jiessl,0) as koud,hz.jis as kuad,0 as zhekz  \n"
					+"from danpcjsmxb dp,jiesb js,fahb f,zhilb z,zhibb zb,meikxxb mk, \n"
					+"(select distinct hetb_id,zhibb_id,round(jis) as jis from hetzkkb) hz \n"
					+"where dp.jiesdid=js.id and instr(dbms_lob.substr(dp.lie_id),to_char(f.lie_id))>0 and f.zhilb_id=z.id \n"
					+"and f.hetb_id = hz.hetb_id and hz.zhibb_id = dp.zhibb_id \n"
					+"and dp.zhibb_id=zb.id and f.meikxxb_id=mk.id and js.id="+getJiesID()+" \n"
					+"order by huaybh,zb.id \n";

		ResultSetList rs = con.getResultSetList(sql);
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "矿点", "化验员", "样本编号", "样本容量", "折扣方式",
				"标准值", "化验项目", "化验值", "扣价", "扣吨", "跨度", "折扣值" };
		int ArrWidth[] = new int[] { 100, 50, 60, 50, 50, 80, 100, 40,40,40,40,40 };
		Report rt = new Report();
		
		rt.setTitle("", ArrWidth);
		rt.setDefaultTitle(1, 3, "验收情况：",Table.ALIGN_LEFT);
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
		bt.addText(rt.getAllPagesHtml());
		return bt;// ph;
	}
	
	public Paragraph getJiesqk(JDBCcon con){
		Paragraph bt = new Paragraph();
		String sql = "select chec,jingz,kouj,koud,jiessl,kouk,jiakhj from (\n" +
					"select 1 as xuh,a.chec,a.jingz as jingz,''||a.kouj as kouj,b.koud,a.jiessl,a.kouk,''||a.jiakhj as jiakhj from\n" + 
					"(select f.chec as chec,max(d.yanssl) jingz,sum(d.zhejbz) as kouj,\n" + 
					"max(d.jiessl) jiessl,sum(zhejje) as kouk,max(d.jiakhj) as jiakhj\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh>0 and d.zhekfs = 0\n" + 
					"group by f.chec) a,\n" + 
					"(select f.chec,sum(d.yanssl-d.jiessl) koud\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh>0 and d.zhekfs = 1\n" + 
					"group by f.chec) b\n" + 
					"where b.chec=a.chec\n" + 
					"union\n" + 
					"select 2 as xuh,'数量扣价' chec,d.yanssl as jingz,'' as kouj,0 koud,0 jiessl,0 kouk ,''||d.zhejje jiakhj\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh=0\n" + 
					"union\n" + 
					"select 3 as xuh,ab.chec,ab.jingz,'平均价：'||ab.kouj as kouj,ab.koud,ab.jiessl,ab.kouk,'结算总额：￥'||(ab.jiakhj+c.zhejje) as jiakhj from\n" + 
					"(select '合计' chec,sum(a.jingz) jingz,round(sum(a.kouj*a.jingz)/sum(a.jingz),2) kouj,sum(b.koud) koud,sum(a.jiessl) jiessl,\n" + 
					"sum(a.kouk) kouk,sum(a.jiakhj) jiakhj from\n" + 
					"(select f.chec as chec,max(d.yanssl) jingz,sum(d.zhejbz) as kouj,\n" + 
					"max(d.jiessl) jiessl,sum(zhejje) as kouk,max(d.jiakhj) as jiakhj\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh>0 and d.zhekfs = 0\n" + 
					"group by f.chec) a,\n" + 
					"(select f.chec,sum(d.yanssl-d.jiessl) koud\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh>0 and d.zhekfs = 1\n" + 
					"group by f.chec) b\n" + 
					"where b.chec=a.chec) ab,\n" + 
					"(select distinct d.jiesdid,d.zhejje\n" + 
					"from danpcjsmxb d,fahb f where instr(dbms_lob.substr(d.lie_id),to_char(f.lie_id))>0\n" + 
					"and d.jiesdid="+getJiesID()+" and xuh=0) c)";


		ResultSetList rs = con.getResultSetList(sql);
		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] { "样本编号", "样本容量", "扣价", "扣吨", "结算数量","扣款", "结算金额"};
		int ArrWidth[] = new int[] { 100, 100,80,80,100,100,120 };
		Report rt = new Report();
		
		rt.setTitle("", ArrWidth);
		rt.setDefaultTitle(1, 3, "样本结算情况：",Table.ALIGN_LEFT);
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		bt.addText(rt.getAllPagesHtml());
		return bt;// ph;
	}
	
	public Paragraph getHuay(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(13,2);
		tb.setCellValue(1, 1, "01、空干基水分Mad(%)");
		tb.setCellValue(2, 1, "01、空干基水分Mad(%)");
		tb.setCellValue(3, 1, "02、空干基灰份Aad(%)");
		tb.setCellValue(4, 1, "03、空干基固定碳FCad(%)");
		tb.setCellValue(5, 1, "04、干燥基灰份Ad(%)");
		tb.setCellValue(6, 1, "05、干燥无灰基挥发份Vdaf(%)");
		tb.setCellValue(7, 1, "06、收到基灰份Aar(%)");
		tb.setCellValue(8, 1, "07、空干基弹筒发热量Qb，ad(MJ/kg)");
		tb.setCellValue(9, 1, "08、干燥基高位发热量Qgr，d(MJ/kg)");
		tb.setCellValue(10, 1, "09、空干基挥发份Vad(%)");
		tb.setCellValue(11, 1, "10、干燥基全硫St，d(%)");
		tb.setCellValue(12, 1, "11、全水分Mt(%)");
		tb.setCellValue(13, 1, "12、收到基低位发热量Qnet，ar(MJ/kg)");
		
		String sql = "select decode(sum(f.jingz),0,0,round(sum(f.jingz*z.mad)/sum(f.jingz),2)) as mad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.aad)/sum(f.jingz),2)) as aad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.fcad)/sum(f.jingz),2)) as fcad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.ad)/sum(f.jingz),2)) as ad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.vdaf)/sum(f.jingz),2)) as vdaf, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.aar)/sum(f.jingz),2)) as aar, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qbad)/sum(f.jingz),2)) as qbad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qgrad)/sum(f.jingz),2)) as qgrad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.vad)/sum(f.jingz),2)) as vad, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.std)/sum(f.jingz),2)) as std, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.mt)/sum(f.jingz),2)) as mt, \n"
			+ "decode(sum(f.jingz),0,0,round(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)) as qnet_ar \n"
			+ "from zhilb z,fahb f where f.jiesb_id="+getJiesID()+"and f.zhilb_id=z.id \n";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			tb.setCellValue(1, 2, rs.getString("mad"));
			tb.setCellValue(2, 2, rs.getString("mad"));
			tb.setCellValue(3, 2, rs.getString("aad"));
			tb.setCellValue(4, 2, rs.getString("fcad"));
			tb.setCellValue(5, 2, rs.getString("ad"));
			tb.setCellValue(6, 2, rs.getString("vdaf"));
			tb.setCellValue(7, 2, rs.getString("aar"));
			tb.setCellValue(8, 2, rs.getString("qbad"));
			tb.setCellValue(9, 2, rs.getString("qgrad"));
			tb.setCellValue(10, 2, rs.getString("vad"));
			tb.setCellValue(11, 2,rs.getString("std"));
			tb.setCellValue(12, 2,rs.getString("mt"));
			tb.setCellValue(13, 2, rs.getString("qnet_ar"));
		}
		
		String ArrHeader[][] = new String[1][2];
		
		ArrHeader[0] = new String[] { "化验项目", "加权平均值"};
		int ArrWidth[] = new int[] { 500,180};
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setTitle("", ArrWidth);
		rt.setDefaultTitle(1, 2, "样本化验加权平均统计：",Table.ALIGN_LEFT);
		rt.setBody(tb);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		bt.addText(rt.getAllPagesHtml());
		return bt;
	}


	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("结算编号："));
		ComboBox combJiesbh = new ComboBox();
		combJiesbh.setWidth(150);
		combJiesbh.setTransform("JiesbhDropDown");
		combJiesbh.setLazyRender(true);
		tb1.addField(combJiesbh);
		tb1.addText(new ToolbarText("-"));
		
		
		ToolbarButton button = new ToolbarButton(null, "刷新",
				"function() {document.forms[0].submit();}");
		button.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(button);
		setToolbar(tb1);
	}

	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setJiesbhModel(null);
			setJiesbhValue(null);
			this.setTreeid(null);
			this.getTree();
		}
		
		if (blnDiancChange){
			blnDiancChange=false;
			setJiesbhModel(null);
			setJiesbhValue(null);
		}
		isBegin = true;
		getToolBars();
	}

	// 页面判定方法
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
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}
	
	private boolean blnDiancChange=false;
	private String treeid;

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		String strDiancID=((Visit) getPage().getVisit()).getString2();
		if (treeid==null){
			blnDiancChange=true;
		} else if(!treeid.equals(strDiancID)){
			blnDiancChange=true;
		}
		
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}
