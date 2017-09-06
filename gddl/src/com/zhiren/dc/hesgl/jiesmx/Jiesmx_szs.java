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
import com.zhiren.report.Cell;
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：夏峥
 * 时间：2013-03-02
 * 使用范围：国电电力石嘴山
 * 描述：按电厂要求调整结算单显示内容和样式
 */
/*
 * 作者：夏峥
 * 时间：2013-09-22
 * 使用范围：国电电力石嘴山
 * 描述：按电厂要求调整结算单显示内容和样式
 */
/*
 * 作者：夏峥
 * 时间：2014-02-18
 * 使用范围：国电电力石嘴山
 * 描述：调整结算单显示内容和样式
 */
public class Jiesmx_szs extends BasePage implements PageValidateListener {

	private boolean isBegin;

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
		setJiesbhValue(null);
		String strGongsID = "";

		strGongsID=" and (dc.id= " +this.getTreeid()+" or dc.fuid="+this.getTreeid()+")";
		String beginsj=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		Date lastday=DateUtil.getLastDayOfMonth(DateUtil.getDate(beginsj));
		String endsj=DateUtil.Formatdate("yyyy-MM-dd", lastday);
		
		String sql = "select j.id,j.bianm from jiesb j,vwdianc dc "+
			 		" where j.diancxxb_id=dc.id " +strGongsID
			 		+ " and jiesrq >=date'"+beginsj+"' and jiesrq <=date'"+endsj+"'"
			 	    + " order by j.id desc ";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"请选择"));
	}
	
	public long getJiesID() {
		int id = -1;
		if(((Visit)getPage().getVisit()).getLong1()!=0){
			return ((Visit)getPage().getVisit()).getLong1();
		}
		if (getJiesbhValue() == null) {
			return id;
		}
		return getJiesbhValue().getId();
	}
	
	public void setJiesID(long jiesb_id){
		((Visit)getPage().getVisit()).setLong1(jiesb_id);
	}
	
	public String getTitle() {
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
				sb = "国电宁夏石嘴山发电有限责任公司地方煤结算单";
			}else{
				sb = "国电宁夏石嘴山发电有限责任公司统配煤结算单";
			}
		}else if(diancid==509){
			if(jihkj.equals("市场采购")){
				sb = "国电石嘴山第一发电有限责任公司地方煤结算单";
			}else{
				sb = "国电石嘴山第一发电有限责任公司统配煤结算单";
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

	public String getRiq(JDBCcon con) {
		String sDate = "";
		String sql = "select riq from pandb where id=" + getJiesID();
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
		try{
			doc.addParagraph(getBiaot(con));
			doc.addParagraph(getMingx(con));
			doc.addParagraph(getJiesqk(con));
			doc.addParagraph(getHuay(con));
			doc.addParagraph(getBiaow(con));
		}catch(Exception e){
			doc = new Document();
			doc.addParagraph(new Paragraph());
		}
		
		con.Close();
		return doc.getHtml();
	}

	public Paragraph getBiaot(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(5,8);
		//合并单元格
		tb.merge(1, 2, 1, 3);
		tb.merge(2, 4, 2, 8);
		tb.merge(3, 7, 3, 8);
		tb.merge(4, 3, 4, 4);
		tb.merge(4, 5, 4, 8);
		tb.merge(5, 2, 5, 8);
		
		tb.setCellValue(1, 1, "结算单号：");
		tb.setCellValue(1, 4, "流水号：");
		tb.setCellValue(3, 1, "结算人：");
		tb.setCellValue(4, 1, "合同煤款：");
		tb.setCellValue(5, 1, "结算说明：");
		tb.setCellValue(2, 1, "结算日期：");
		tb.setCellValue(3, 3, "结算标志：");
		
		tb.setCellValue(2, 3, "供应商：");
		tb.setCellValue(3, 5, "合同编号：");
		
		int zhib_shul = 21;//加权平均的指标
		int kouj = 0;
//		double koud_js = 0;
		ResultSetList rsl = 
			con.getResultSetList("select zhejbz from danpcjsmxb " +
								 " where jiesdid="+getJiesID()+" and zhibb_id=" + zhib_shul);
		if(rsl.next()){
			kouj = rsl.getInt("zhejbz");
		}
//		rsl = con.getResultSetList("select koud_js from danpcjsmxb where jiesdid="+getJiesID()+" group by koud_js");
//		if(rsl.next()){
//			koud_js = rsl.getDouble("koud_js");
//		}
		
		String sql = 
			"select j.bianm,j.ranlbmjbr,hs.hetl*j.hetj as hetjg,j.jiesrq,decode(j.ruzrq,null,'否','是') as jiesbz,\n" +
			"       j.hetj+j.yunfhsdj jij,j.shoukdw,h.hetbh,j.hetj as htmj,\n" + 
			"       '结算期:'||to_char(j.fahksrq,'yyyy-mm-dd')||'至'||to_char(j.fahjzrq,'yyyy-mm-dd')||'； 矸石杂物扣'|| (select distinct koud_js from danpcjsmxb where jiesdid="+getJiesID()+") ||'吨；'||decode(substr(j.beiz,1,1),' ','',j.beiz) as beiz\n" + 
			"  from jiesb j,hetslb hs,hetb h\n" + 
			" where j.id="+getJiesID()+"\n" + 
			"   and j.hetb_id=hs.hetb_id\n" + 
			"   and j.hetb_id=h.id";

		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			tb.setCellValue(1, 2, rs.getString("bianm").split(":")[0]);
			try{
				tb.setCellValue(1, 5, rs.getString("bianm").split(":")[1]);
			}catch(Exception e){}
			tb.setCellValue(3, 2, rs.getString("ranlbmjbr"));
			tb.setCellValue(4, 2, rs.getString("hetjg"));
			tb.setCellValue(5, 2, rs.getString("beiz"));
			tb.setCellValue(2, 2, DateUtil.FormatDate(rs.getDate("jiesrq")));
			tb.setCellValue(3, 4, rs.getString("jiesbz"));
			tb.setCellValue(4, 3, "到厂合同价：" + rs.getString("jij") + "（元/吨）");
			tb.setCellValue(2, 4, rs.getString("shoukdw"));
			tb.setCellValue(3, 6, rs.getString("hetbh"));
			tb.setCellValue(4, 5, "合同煤价：" + (rs.getInt("htmj")+(kouj)) + "（元/吨）");
		}
		int ArrWidth[] = new int[] { 60, 120, 60, 120, 60, 120, 60, 70  };
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setBody(tb);
		
		rt.createTitle(3, ArrWidth);
		rt.title.setWidth(ArrWidth);
		rt.title.merge(3, 1, 3, 8);

		rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//国电的标志（到现场要一个换上就行了）
		rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
		
		rt.title.setCellValue(2, 1, getTitle(), ArrWidth.length);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 16);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(2, Table.PER_FONTNAME, "楷体");
		rt.title.setRowCells(2, Table.PER_FONTBOLD, true);
		
		rt.title.setCellImage(3, 1, rt.title.getWidth()/2+150, 6, "imgs/report/GDHX.gif");
		rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setBorderNone();
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_LEFT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_TOP, 0);
		bt.addText(rt.getAllPagesHtml());
		return bt;
	}
	
	public Paragraph getMingx(JDBCcon con){
		Paragraph bt = new Paragraph();
		
		String sql = "";
		String sq = 
			"select distinct z.id,z.mingc,j.gongysmc,z.bianm\n" +
			"     from hetzkkb zkk,jiesb j,zhibb z\n" + 
			"   where j.hetb_id=zkk.hetb_id\n" + 
			"      and zkk.zhibb_id=z.id\n" + 
			"      and j.id=" + getJiesID() + "\n" + 
			"      and mingc not in('数量')";
		ResultSetList rsl = con.getResultSetList(sq);
		ResultSetList rs = new ResultSetList();
		StringBuffer sql_tmp = new StringBuffer();

		while(rsl.next()){
			if(rsl.getString("bianm").equals("Qnetar")){
				sql_tmp.append("             (" +
				"select '"+rsl.getString("bianm")+"' name,f.id fid,f.chec chec1,round_new(qnet_ar/4.1816*1000,2) huayz ");
			}else{
				sql_tmp.append("             (" +
				"select '"+rsl.getString("bianm")+"' name,f.id fid,f.chec chec1,"+rsl.getString("bianm")+" huayz ");
			}
			sql_tmp.append(
					"  from zhillsb z,fahb f " +
					" where f.jiesb_id=" + getJiesID() + " " +
					"   and z.zhilb_id=f.zhilb_id" +
					")\n union all\n");
		}
		String sql1 = "";
		try{
			sql1 = sql_tmp.substring(1, sql_tmp.length()-10);
		}catch(Exception e){
			
		}
		
		sql = 
			"select gongysmc,title.huayy,substr(title.bianm,4,length(title.bianm)) bianm,title.jingz,title.koujfs,decode(body.hetbz||'','',title.hetbz||'',body.hetbz||'') hetbz,mingc," +
			"		--decode(title.id,2,round(jies,0),jies),\n" +
			"		title.huayz,\n" +
			"		zhejbz,body.koud,body.kuad,round(body.zhekz,2)\n"+//"title.koud,round(ches,0) " +
			"from\n" +
			"(select g.mingc as meikmc,\n" + 
			"       z.huayy,\n" + 
			"       zm.bianm,\n" + 
			"       f.maoz-f.piz jingz,\n" + 
			"		f.chec," +
//			"       decode(dp.zhekfs, 1, '扣吨', '扣价') as koujfs,\n" + 
			"       dp.hetbz,\n" + 
			"       dp.zhibb_id,\n" + 
			"       dp.jies,\n" + 
			"       decode(dp.zhekfs, 1, 0, dp.zhejbz) as zhejbz,\n" + 
			"       --decode(dp.zhekfs, 1, dp.yanssl - dp.jiessl, 0) as koud\n" + 
			"		round(decode(dp.zhekfs, 1, decode(zb.id,6,dp.yingk/100*(f.sanfsl),dp.yingk/10/dp.hetbz*(f.sanfsl)), 0),2) as koud\n" +
			"       ,hz.jis as kuad\n" + 
			"       ,decode(dp.yingk,0,0,abs(dp.zhejbz/dp.yingk)) as zhekz\n" +  
			"  from danpcjsmxb dp,\n" + 
			"       jiesb js,\n" + 
			"       fahb f,\n" + 
			"       zhilb z,\n" + 
			"       gongysb g,\n" + 
			"       zhillsb zls,\n" + 
			"       zhuanmb zm,\n" + 
			"		zhibb zb,\n" +
			"       (select distinct zhibb_id, round(jis) as jis from hetzkkb h,jiesb j where j.hetb_id=h.hetb_id and j.id=" + getJiesID() + ") hz\n" + 
			" where dp.jiesdid = js.id\n" + 
			"   and instr(dbms_lob.substr(dp.lie_id), to_char(f.lie_id)) > 0\n" + 
			"   and f.zhilb_id = z.id\n" + 
			"   and f.gongysb_id = g.id\n" + 
			"   and zls.zhilb_id=z.id\n" + 
			"   and zm.zhillsb_id=zls.id\n" + 
			"	and dp.zhibb_id=zb.id\n" +
			"   and zm.zhuanmlb_id=100663\n" +//(select id from zhuanmlb zl where zl.mingc = '化验编码')\n" + 
			"   and js.id = " + getJiesID() + "\n" + 
			"   and dp.zhibb_id not in(21)\n" + 
			"   and hz.zhibb_id = dp.zhibb_id\n" + 
			"	and f.jiesb_id=js.id\n" +
			" ) body,\n" + 
			"\n" + 
			"(\n" + 
			"select * from (\n" + 
			"select bm.fid,zb.bianm zbbianm,zb.id,zb.mingc,zb.hetbz,zb.koujfs,bm.bianm,zb.gongysmc,bm.jingz,bm.ches,bm.koud,bm.huayy,bm.chec from\n" + 
			"  (select distinct z.id,z.mingc,j.gongysmc,z.bianm,z.xuh,decode(hzl.tiaojb_id, 3,hzl.shangx,4,hzl.shangx,1,hzl.xiax,2,hzl.xiax,5,hzl.xiax || '-' || hzl.shangx) hetbz,decode(zkk.koujdw,24,'扣价','扣吨') koujfs\n" + 
			"     from hetzkkb zkk,jiesb j,zhibb z,hetzlb hzl\n" + 
			"   where j.hetb_id=zkk.hetb_id\n" + 
			"      and zkk.zhibb_id=z.id\n" + 
			"      and j.id=" + getJiesID() + "\n" + 
			"	   and j.hetb_id=hzl.hetb_id\n" +
			"      and z.id=hzl.zhibb_id" +
			"      and mingc not in('数量') order by z.xuh) zb\n" + 
			"  ,(select z.id, zm.bianm,f.id fid,f.sanfsl jingz,f.ches,f.koud,zls.huayy,f.chec\n" + 
			"    from zhuanmb zm, zhillsb zls, zhilb z,jiesb j,fahb f\n" + 
			"   where zm.zhillsb_id = zls.id\n" + 
			"     and f.jiesb_id=j.id\n" + 
			"     and f.zhilb_id=z.id\n" + 
			"     and zls.zhilb_id = z.id\n" + 
			"     and j.id=" + getJiesID() + "\n" + 
			"     and zm.zhuanmlb_id =\n" + 
			"         (select id from zhuanmlb zl where zl.mingc = '化验编码')) bm) all_\n" + 
			"   ,(select * from (\n" + 
			sql1 +
			"			)\n" + 
			"      ) all_zhi\n" + 
			"where all_zhi.name=all_.zbbianm and all_zhi.fid=all_.fid and all_zhi.chec1=all_.chec\n" + 
			"         ) title\n" + 
			"where title.id=body.zhibb_id(+)\n" + 
			"  and title.bianm=body.bianm(+)\n" + 
			"  and title.chec=body.chec(+)\n" +
			"  order by title.chec,substr(title.bianm,5,length(title.bianm)),title.id";

		rs = con.getResultSetList(sql);
		
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "矿点", "化验员", "样本编号", "样本容量", "折扣方式",
				"标准值", "化验项目", "化验值", "扣价", "扣吨", "跨度", "折扣值" };
		int ArrWidth[] = new int[] { 90, 50, 60, 50, 50, 60, 100, 50, 40, 40, 40, 40 };
		Report rt = new Report();
		
//		rt.setTitle("", ArrWidth);
//		rt.setDefaultTitle(1, 3, "验收情况：",Table.ALIGN_LEFT);
		String ArrTitle[][]=new String[1][12];
		ArrTitle[0]=new String[] {"验收情况：","","","","","","","","","","",""};
		rt.setTitle(new Table(ArrTitle,0,0,0));
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_BOTTOM, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_LEFT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_RIGHT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_TOP, 0);
		
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		int rows = rt.body.getRows();
		rt.body.merge(1, 4 , rows, 4);
		
		rt.body.ShowZero=true;
		rt.setPageRows(25);
		bt.addText(rt.getAllPagesHtml());
		return bt;
	}
	
	public Paragraph getJiesqk(JDBCcon con){
		Paragraph bt = new Paragraph();
		int zhib_shul = 21;//加权平均的指标
		int kouj = 0;
		ResultSetList rsl = 
			con.getResultSetList("select zhejbz from danpcjsmxb " +
								 " where jiesdid="+getJiesID()+" and zhibb_id=" + zhib_shul);
		if(rsl.next()){
			kouj = rsl.getInt("zhejbz");
		}
		String sql = 
			"select bianm,yanssl,round(kouj,2) kouj,koud,jiessl,round(kouk,2),round(hej,2) from(\n" +
			"select xuh,decode(xuh,null,'合计',max(bianm)) bianm,sum(yanssl) yanssl,sum(hej)/sum(jiessl) kouj,sum(koud) koud,sum(jiessl) jiessl,sum(kouk) kouk,sum(hej) hej from(\n" + 
			"select rownum xuh,substr(zm.bianm,4,length(zm.bianm)) bianm,f.sanfsl yanssl,sl.kouj+("+kouj+") kouj,sl.koud,sl.jiessl,sl.kouj*sl.jiessl kouk,sl.jiessl*(sl.jiesdj+("+kouj+")) hej\n" + 
			"from jiesb j,fahb f ,zhillsb zls,zhuanmb zm,\n" + 
			"     (select lie_id,yanssl,sum(kouj) kouj,sum(koud) koud,jiessl,jiesdj from\n" + 
			"        (select to_char(d.lie_id) lie_id,yanssl,decode(zhekfs,0,sum(zhejbz),0) kouj,decode(zhekfs,1,sum(f.sanfsl-d.jiessl),0) koud ,d.jiessl,d.jiesdj\n" + 
			"        from danpcjsmxb d,jiesb j,fahb f\n" + 
			"         where d.jiesdid=j.id\n" + 
			"			and to_char(d.lie_id)=to_char(f.lie_id)\n" +
			"           and j.id="+getJiesID()+"\n" + 
			"           and zhibb_id not in(21)\n" + 
			"        group by yanssl,zhekfs,d.jiessl,d.jiesdj,to_char(d.lie_id))\n" + 
			"        group by yanssl,jiessl,jiesdj,lie_id\n" + 
			"     ) sl\n" + 
			"where f.jiesb_id=j.id\n" + 
			"  and f.zhilb_id=zls.zhilb_id\n" + 
			"  and zm.zhillsb_id=zls.id\n" + 
			"  and f.lie_id=sl.lie_id\n" + 
			"  and j.id="+getJiesID()+"\n" + 
			"  and zm.zhuanmlb_id=(select id from zhuanmlb where mingc ='化验编码')\n" + 
			"  )\n" + 
			"group by rollup(xuh)) order by bianm";
		ResultSetList rs = con.getResultSetList(sql);
		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] { "样本编号", "样本容量", "结算单价", "扣吨", "结算数量","扣款", "结算金额"};
		int ArrWidth[] = new int[] { 100, 100,75,75,100,100,120 };
		Report rt = new Report();
		
//		rt.setTitle("", ArrWidth);
//		rt.setDefaultTitle(1, 3, "样本结算情况：",Table.ALIGN_LEFT);
		String ArrTitle[][]=new String[1][7];
		ArrTitle[0]=new String[] {"样本结算情况：","","","","","",""};
		rt.setTitle(new Table(ArrTitle,0,0,0));
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_BOTTOM, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_LEFT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_RIGHT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_TOP, 0);
		
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		bt.addText(rt.getAllPagesHtml());
		return bt;// ph;
	}
	
	public Paragraph getHuay(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(14,2);
		tb.setCellValue(1, 1, "");
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
		tb.setCellValue(14, 1, "13、收到基低位发热量Qnet，ar(Kcal/kg)");
		
		String sql = "select decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.mad)/sum(f.sanfsl),2)) as mad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.aad)/sum(f.sanfsl),2)) as aad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.fcad)/sum(f.sanfsl),2)) as fcad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.ad)/sum(f.sanfsl),2)) as ad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.vdaf)/sum(f.sanfsl),2)) as vdaf, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.aar)/sum(f.sanfsl),2)) as aar, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.qbad)/sum(f.sanfsl),2)) as qbad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.qgrad)/sum(f.sanfsl),2)) as qgrad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.vad)/sum(f.sanfsl),2)) as vad, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.std)/sum(f.sanfsl),2)) as std, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.mt)/sum(f.sanfsl),2)) as mt, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2)) as qnet_ar, \n"
			+ "decode(sum(f.sanfsl),0,0,round_new(round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2)/4.1816*1000,2)) as qnet_ark \n"
			+ "from zhilb z,fahb f where f.jiesb_id="+getJiesID()+"and f.zhilb_id=z.id \n";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			tb.setCellValue(1, 2, "");
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
			tb.setCellValue(14, 2, rs.getString("qnet_ark"));
		}
		
		String ArrHeader[][] = new String[1][2];
		
		ArrHeader[0] = new String[] { "化验项目", "加权平均值"};
		int ArrWidth[] = new int[] { 550,120};
		tb.setWidth(ArrWidth);
		Report rt = new Report();
//		rt.setTitle("", ArrWidth);
//		rt.setDefaultTitle(1, 2, "样本化验加权平均统计：",Table.ALIGN_LEFT);
		String ArrTitle[][]=new String[1][2];
		ArrTitle[0]=new String[] {"样本结算情况：",""};
		rt.setTitle(new Table(ArrTitle,0,0,0));
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_BOTTOM, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_LEFT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_RIGHT, 0);
		rt.title.setCells(1, 1, rt.title.getRows(), rt.title.getCols(),
				Table.PER_BORDER_TOP, 0);
		
		rt.setBody(tb);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		bt.addText(rt.getAllPagesHtml());
		return bt;
	}

	public Paragraph getBiaow(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(5,8);
		tb.merge(1, 1, 1, 2);
		tb.merge(1, 3, 1, 4);
		tb.merge(1, 5, 1, 6);
		tb.merge(1, 7, 1, 8);
		tb.merge(2, 1, 2, 2);
		tb.merge(2, 3, 2, 4);
		tb.merge(2, 5, 2, 6);
		tb.merge(2, 7, 2, 8);
		tb.merge(4, 1, 4, 2);
		tb.merge(4, 3, 4, 4);
		tb.merge(4, 5, 4, 6);
		tb.merge(4, 7, 4, 8);
		tb.merge(5, 1, 5, 2);
		tb.merge(5, 3, 5, 4);
		tb.merge(5, 5, 5, 6);
		tb.merge(5, 7, 5, 8);
		
		String sql =
			"select jiajqdj,jiessl,hansmk,\n" +
			"       yunfhsdj yunj,yunfjsl,yunfhsdj*yunfjsl yunf,\n" + 
			"       (select distinct koud_js from danpcjsmxb where jiesdid="+getJiesID()+") koud_js,\n" + 
			"       kouk_js\n" + 
			"from jiesb where id="+getJiesID()+"";
		ResultSetList rsl = con.getResultSetList(sql);
		
		String meikdj = "0";
		String meikjsl = "0";
		String meikje = "0";
		String yunj = "0";
//		String yunsjsl = "0";
		String yunf = "0";
		
		String koud_js = "0";
		String kouk_js = "0";
		if(rsl.next()){
			meikdj = rsl.getString("jiajqdj");  		//煤款加权单价
			meikjsl = rsl.getString("jiessl");      //煤款结算量
			meikje = rsl.getString("hansmk");  		//煤款金额
			yunj = rsl.getString("yunj");  			//运输单价
//			yunsjsl = rsl.getString("yunfjsl"); 	//运输结算量
			yunf = rsl.getString("yunf");      	//运输金额
			koud_js = rsl.getString("koud_js");	    //结算时扣吨
			kouk_js = rsl.getString("kouk_js");	    //结算时扣款
			
		}
		
		tb.setCellValue(1, 1, "矸石扣吨：" + koud_js+"吨");
		tb.setCellValue(1, 3, "实际结算煤量：" + meikjsl+"吨");
		tb.setCellValue(1, 5, "结算煤价：" + meikdj+"元/吨");
		tb.setCellValue(1, 7, "结算煤款：" + meikje+"元");
		
		tb.setCellValue(2, 1, "扣款：" + kouk_js+"元");
		tb.setCellValue(2, 3, "运费：" + yunj+"元/吨");
		tb.setCellValue(2, 5, "运费：" + yunf+"元");
		
		tb.setCellValue(4, 1, "审批人员：");
		tb.setCellValue(4, 3, "审核人员：");
		tb.setCellValue(4, 5, "初审人员：");
		tb.setCellValue(5, 1, "复核人员：");
		tb.setCellValue(5, 3, "结算人员：");
		tb.setCellValue(5, 5, "化验中心：");
		
		int ArrWidth[] = new int[] { 80, 80, 80, 80, 80, 80, 80, 80 };
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setBody(tb);
		
		Cell c = new Cell();
		c.setBorderNone();
		rt.title = new Table(1, ArrWidth.length, c);
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		
		rt.body.setBorderNone();
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_LEFT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),Table.PER_BORDER_TOP, 0);
		rt.body.setRowHeight(5, 40);
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
		
		tb1.addText(new ToolbarText("结算年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算编号："));
		ComboBox combJiesbh = new ComboBox();
		combJiesbh.setTransform("JiesbhDropDown");
		combJiesbh.setWidth(200);
		combJiesbh.setListeners("select:function(own,rec,index){Ext.getDom('JiesbhDropDown').selectedIndex=index}");
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
		visit.setLong1(0);
		String jiesb_id = cycle.getRequestContext().getParameter("jiesb_id");
		if(jiesb_id!=null){
			setJiesID(Long.parseLong(jiesb_id));
			isBegin = true;
		}else{
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
			
			if(getJiesbhValue().getStrId().equals("-1")){
				isBegin = false;
				getJiesbhModels();
			}else{
				isBegin = true;
			}
			getToolBars();
		}
		
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
	
	
	
//	 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
}
