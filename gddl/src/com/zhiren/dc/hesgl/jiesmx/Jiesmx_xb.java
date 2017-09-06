package com.zhiren.dc.hesgl.jiesmx;

import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Jiesmx_xb extends BasePage implements PageValidateListener {
	private String yunj = "";
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
		doc.addParagraph(getBiaow(con));
		con.Close();
		return doc.getHtml();
	}

	public Paragraph getBiaot(JDBCcon con){
		Visit v = (Visit)getPage().getVisit();
		Paragraph bt = new Paragraph();
		Table tb = new Table(4,8);
		tb.setCellValue(1, 2, "合同价格：");
//		tb.setCellValue(2, 2, "运杂费用：");
		tb.setCellValue(3, 2, "合同编号：");
		tb.setCellValue(4, 2, "结算说明：");
		
		tb.setCellValue(1, 4, "结算日期：");
		tb.setCellValue(2, 2, "供应商名称：");
		
		tb.setCellValue(1, 7, "结算人：");
		String flag = MainGlobal.getXitxx_item("结算", "录入人是否显示", v.getDiancxxb_id()+"", "否");
		if(flag.equals("是")){
			tb.setCellValue(3, 7, "录入人：");
		}
		
		//合并单元格
		tb.merge(1, 5, 1, 6);
		tb.merge(2, 3, 2, 8);
		tb.merge(4, 3, 4, 8);
		
		tb.setCellValue(4, 3, "在本月中发现车内有煤矸石和其他等杂物，因此从本月结算中扣除。");
		
		String sql = 
			"select max(jg.jij) jig,round(j.yunfhsdj,0) yunj,h.hetbh,j.gongysmc,j.ranlbmjbr from jiesb j,hetb h,hetjgb jg\n" +
			"where j.hetb_id=h.id\n" + 
			"  and jg.hetb_id=h.id\n" + 
			"  and j.id=" + getJiesID() + "\n" +
			"group by j.yunfhsdj,h.hetbh,j.gongysmc,j.ranlbmjbr";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			tb.setCellValue(1, 3, rs.getString("jig")+"元/吨");
			tb.setCellValue(1, 3, rs.getString("jig")+"元/吨");
//			tb.setCellValue(2, 3, rs.getString("yunj")+"元/吨");
			tb.setCellValue(3, 3, rs.getString("hetbh"));
			tb.setCellValue(2, 3, rs.getString("gongysmc"));
			tb.setCellValue(1, 8, rs.getString("ranlbmjbr"));
		}
		
		String sql1 = 
			"select to_char(qisrq,'yyyy-mm-dd') qisrq,to_char(guoqrq,'yyyy-mm-dd') guoqrq from hetb h,jiesb j where j.hetb_id=h.id and j.id=" + getJiesID();


		ResultSetList rs1 = con.getResultSetList(sql1);
		if(rs1.next()){
			tb.setCellValue(1, 5, rs1.getString("qisrq")+"至"+rs1.getString("guoqrq"));
		}
		
		int ArrWidth[] = new int[] {82, 82, 82, 82, 82, 82, 82, 86};
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setBody(tb);
		
		rt.createTitle(5, ArrWidth);
		rt.title.setWidth(ArrWidth);
		rt.title.merge(4, 1, 4, 8);

		rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//国电的标志（到现场要一个换上就行了）
		rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
		
		rt.title.setCellValue(2, 1, "国电电力发展股份有限公司", ArrWidth.length);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 12);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(2, Table.PER_FONTNAME, "黑体");
//		rt.title.setRowCells(2, Table.PER_FONTBOLD, true);
		
		rt.title.setCellValue(3, 1, "燃料（煤炭）结算单", ArrWidth.length);
		rt.title.setCellAlign(3, 1, Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTNAME, "隶书");
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 18);
		
		
		rt.title.setCellImage(4, 1, rt.title.getWidth()/3+30, 6, "imgs/report/GDHX.gif");
		rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.title.setCellValue(5, 1, "结算单位：" + v.getDiancqc(), ArrWidth.length);
		
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
		//50+24*4+24*4
		return bt;
	}
	
	public Paragraph getMingx(JDBCcon con){
		Paragraph bt = new Paragraph();
		String sq = 
			"select distinct z.id,z.mingc,j.gongysmc,z.bianm\n" +
			"     from hetzkkb zkk,jiesb j,zhibb z\n" + 
			"   where j.hetb_id=zkk.hetb_id\n" + 
			"      and zkk.zhibb_id=z.id\n" + 
			"      and j.id=" + getJiesID() + "\n" + 
			"      and mingc not in('数量')";
		ResultSetList rsl = con.getResultSetList(sq);
		StringBuffer sql_tmp = new StringBuffer();
		while(rsl.next()){
			if(rsl.getString("bianm").equals("Qnetar")){
				sql_tmp.append("             (" +
				"select '"+rsl.getString("bianm")+"' name,f.id fid,f.chec chec1,round(qnet_ar/4.1816*1000) huayz ");
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
		String sql1 = sql_tmp.substring(1, sql_tmp.length()-10);
		
		String sql = 
			"select substr(title.bianm,4,length(title.bianm)) bianm,gongysmc,title.huayy,title.jingz,koujfs,hetbz,mingc," +
			"		--decode(title.id,2,round(jies,0),jies),\n" +
			"		title.huayz,\n" +
			"		zhejbz,body.koud,title.koud,round(ches,0) from\n" +
			"(select mk.mingc as meikmc,\n" + 
			"       z.huayy,\n" + 
			"       zm.bianm,\n" + 
			"       f.maoz-f.piz jingz,\n" + 
			"		f.chec," +
			"       decode(dp.zhekfs, 1, '扣吨', '扣价') as koujfs,\n" + 
			"       dp.hetbz,\n" + 
			"       dp.zhibb_id,\n" + 
			"       dp.jies,\n" + 
			"       decode(dp.zhekfs, 1, 0, round(dp.zhejbz,2)) as zhejbz,\n" + 
			"       --decode(dp.zhekfs, 1, dp.yanssl - dp.jiessl, 0) as koud\n" + 
			"		abs(round(decode(dp.zhekfs, 1, decode(zb.id,6,dp.yingk/100*(f.sanfsl),dp.yingk/10/dp.hetbz*(f.sanfsl)), 0),2))*-1 as koud\n" +
			"  from danpcjsmxb dp,\n" + 
			"       jiesb js,\n" + 
			"       fahb f,\n" + 
			"       zhilb z,\n" + 
			"       meikxxb mk,\n" + 
			"       zhillsb zls,\n" + 
			"       zhuanmb zm,\n" + 
			"		zhibb zb\n" +
			" where dp.jiesdid = js.id\n" + 
			"   and instr(dbms_lob.substr(dp.lie_id), to_char(f.lie_id)) > 0\n" + 
			"   and f.zhilb_id = z.id\n" + 
			"   and f.meikxxb_id = mk.id\n" + 
			"   and zls.zhilb_id=z.id\n" + 
			"   and zm.zhillsb_id=zls.id\n" + 
			"	and dp.zhibb_id=zb.id\n" +
			"   and zm.zhuanmlb_id=(select id from zhuanmlb zl where zl.mingc = '化验编码')\n" + 
			"   and js.id = " + getJiesID() + "\n" + 
			"   and dp.zhibb_id not in(21)\n" + 
			" order by huaybh, dp.zhibb_id) body,\n" + 
			"\n" + 
			"(\n" + 
			"select * from (\n" + 
			"select bm.fid,zb.bianm zbbianm,zb.id,zb.mingc,bm.bianm,zb.gongysmc,bm.jingz,bm.ches,bm.koud,bm.huayy,bm.chec from\n" + 
			"  (select distinct z.id,z.mingc,j.gongysmc,z.bianm,z.xuh\n" + 
			"     from hetzkkb zkk,jiesb j,zhibb z\n" + 
			"   where j.hetb_id=zkk.hetb_id\n" + 
			"      and zkk.zhibb_id=z.id\n" + 
			"      and j.id=" + getJiesID() + "\n" + 
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
			"  order by substr(title.bianm,5,length(title.bianm)), title.chec,title.id";
//		substr(title.bianm,5,length(title.bianm)),
		ResultSetList rs = con.getResultSetList(sql);
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "样本编号", "矿点", "化验员", "样本容量", "折扣方式",
				"标准值", "化验项目", "化验值", "扣价", "扣吨", "扣矸石量", "车数" };
		int ArrWidth[] = new int[] { 60, 70, 50, 50, 50, 80, 100, 40, 40, 40, 40, 40 };
		Report rt = new Report();
		
		String ArrTitle[][]=new String[2][12];
		ArrTitle[0]=new String[] {"","","","","","","","","","本币名称：人民币","本币名称：人民币","本币名称：人民币"};
		ArrTitle[1]=new String[] {"验收情况：","验收情况：","","","","","","","","数量单位：吨","数量单位：吨","数量单位：吨"};
		rt.setTitle(new Table(ArrTitle,0,0,0));
		rt.title.setWidth(ArrWidth);
		rt.title.merge(1, 10, 1, 12);
		rt.title.merge(2, 1, 2, 2);
		rt.title.merge(2, 10, 2, 12);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 8);
		rt.title.setCells(2, 10, 2, 12, Table.PER_FONTSIZE, 8);
		
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
		
		int rows = rt.body.getRows();
		for(int i=1; i <= (rows/4-1)+1;i++){
			rt.body.merge(i*4-2, 4 , i*4+1, 4);
			rt.body.merge(i*4-2, 11, i*4+1, 11);
			rt.body.merge(i*4-2, 12, i*4+1, 12);
		}
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
		bt.addText(rt.getAllPagesHtml());
		//(rows+2)*24
		return bt;// ph;
	}
	
	public Paragraph getJiesqk(JDBCcon con){
		Paragraph bt = new Paragraph();
		String sq = "select distinct jg.yunj from hetb h,hetjgb jg where h.id=(select hetb_id from jiesb where id=" + getJiesID() + ") and jg.hetb_id=h.id";
		ResultSetList rs = con.getResultSetList(sq);
		yunj = "0";
		if(rs.next()){
			yunj = rs.getString("yunj");
		}
		String sql = 
			"select bianm,yanssl,kouj,koud,jiang,kou,jiessl,round(kouk,2),round(hej,2) from(\n" +
			"select xuh,decode(xuh,null,'合计',max(bianm)) bianm,sum(yanssl) yanssl,round(sum(kouj),2) kouj,round(sum(koud),2) koud,round(sum(dayuzerodisp(jiesdj_shul-hetj)),2) jiang,round(sum(xiaoyuzerodisp(jiesdj_shul-hetj)),2) kou,sum(jiessl) jiessl,round(sum(kouk),2) kouk,round(sum(hej)+sum("+yunj+"*yanssl),2) hej\n" + 
			"from(\n" + 
			"select rownum xuh,substr(zm.bianm,4,length(zm.bianm)) bianm,f.sanfsl yanssl,sl.kouj,f.sanfsl-sl.jiessl koud,sl.jiessl,sl.kouj*sl.jiessl kouk,round(sl.jiessl*sl.jiesdj,2) hej,sl.jiesdj-sl.kouj jiesdj_shul,sl.hetj\n" + 
			"from jiesb j,fahb f ,zhillsb zls,zhuanmb zm,\n" + 
			"     (select lie_id,yanssl,sum(kouj) kouj,jiessl,jiesdj,hetj from\n" + 
			"        (select to_char(d.lie_id) lie_id,yanssl,decode(zhekfs,0,sum(zhejbz),0) kouj,d.jiessl,d.jiesdj,d.hetj\n" + 
			"        from danpcjsmxb d,jiesb j\n" + 
			"         where d.jiesdid=j.id\n" + 
			"           and j.id="+getJiesID()+"\n" + 
			"           and zhibb_id not in(21)\n" + 
			"        group by yanssl,zhekfs,d.jiessl,d.jiesdj,to_char(d.lie_id),d.hetj)\n" + 
			"        group by yanssl,jiessl,jiesdj,lie_id,hetj\n" + 
			"     ) sl\n" + 
			"where f.jiesb_id=j.id\n" + 
			"  and f.zhilb_id=zls.zhilb_id\n" + 
			"  and zm.zhillsb_id=zls.id\n" + 
			"  and f.lie_id=sl.lie_id\n" + 
			"  and j.id="+getJiesID()+"\n" + 
			"  and zm.zhuanmlb_id=(select id from zhuanmlb where mingc ='化验编码')\n" + 
			"  )\n" + 
			"group by rollup(xuh)\n" + 
			")\n" + 
			"order by substr(bianm,2,length(bianm)),jiang desc\n";


		rs = con.getResultSetList(sql);
		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] { "样本编号", "样本容量", "扣价", "扣吨", "奖励价", "缺货扣价", "结算数量","扣款", "结算金额"};
		int ArrWidth[] = new int[] { 67, 67, 67, 67, 67, 67, 67, 95, 95 };
		Report rt = new Report();
		
//		rt.setTitle("", ArrWidth);
		Cell c = new Cell();
		c.setBorderNone();
		rt.title = new Table(1, ArrWidth.length, c);
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		rt.setDefaultTitle(1, 3, "结算情况：",Table.ALIGN_LEFT);
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		for(int i = 1; i<=rt.body.getRows();i++){
			rt.body.setRowCells(i, Table.PER_ALIGN, Table.ALIGN_CENTER);
		}
		bt.addText(rt.getAllPagesHtml());
		//(1+rows)*24
		return bt;// ph;
	}
	
	public Paragraph getBiaow(JDBCcon con){
		Paragraph bt = new Paragraph();
		Table tb = new Table(5,10);
		tb.merge(1, 7, 1, 8);
		tb.merge(3, 3, 3, 4);
//		tb.setCellValue(1, 1, "运费：");
		tb.setCellValue(1, 3, "扣款：");
		tb.setCellValue(1, 7, "最终结算金额：",Table.ALIGN_RIGHT);
		
		tb.setCellValue(3, 1, "总经理：");
		tb.setCellValue(3, 3, "燃料分管领导：");
		tb.setCellValue(3, 5, "账务审核：");
		tb.setCellValue(3, 7, "计划审核：");
		
		tb.setCellValue(5, 1, "燃供部：");
		tb.setCellValue(5, 3, "质价审核：");
		tb.setCellValue(5, 5, "数量审核：");
		tb.setCellValue(5, 7, "结算人：");
		tb.setCellValue(5, 9, "录入人：");
		
		tb.setCells(1 ,1 ,1 ,1, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		tb.setCells(1 ,3 ,1 ,3, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		tb.setCells(3 ,5 ,3 ,5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		tb.setCells(3 ,7 ,3 ,7, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		
//		String sql = 
//			"select\n" +
//			"       (select max(hjg.yunj) yunj from jiesb j, hetjgb hjg\n" + 
//			"         where j.hetb_id = hjg.hetb_id and j.id = " + getJiesID() + ")\n" + 
//			"           *\n" + 
//			"       (select sum(maoz) - sum(piz) from jiesb j, fahb f\n" + 
//			"         where j.id = f.jiesb_id and j.id = " + getJiesID() + ") yunj\n" + 
//			"from dual";
//
//		ResultSetList rs = con.getResultSetList(sql);
//		double yunj = 0;
//		if(rs.next()){
//			yunj = CustomMaths.round(rs.getDouble("yunj"),2);
//			tb.setCellValue(1, 2, String.valueOf(yunj));
//		}
		
		String sql1 = 
			"select round(sum(sl.kouj*sl.jiessl),2) kouk,sum(round(sl.jiessl*sl.jiesdj,2))+sum(f.maoz-f.piz)*"+yunj+" hej\n" +
			"from jiesb j,fahb f ,zhillsb zls,zhuanmb zm,\n" + 
			"     (select lie_id,yanssl,sum(kouj) kouj,sum(koud) koud,jiessl,jiesdj from\n" + 
			"        (select to_char(d.lie_id) lie_id,yanssl,decode(zhekfs,0,sum(zhejbz),0) kouj,decode(zhekfs,1,sum(zhejbz),0) koud ,d.jiessl,d.jiesdj\n" + 
			"        from danpcjsmxb d,jiesb j\n" + 
			"         where d.jiesdid=j.id\n" + 
			"           and j.id=" + getJiesID() + "\n" + 
			"           and zhibb_id not in(21)\n" + 
			"        group by yanssl,zhekfs,d.jiessl,d.jiesdj,to_char(d.lie_id))\n" + 
			"        group by yanssl,jiessl,jiesdj,lie_id\n" + 
			"     ) sl\n" + 
			"where f.jiesb_id=j.id\n" + 
			"  and f.zhilb_id=zls.zhilb_id\n" + 
			"  and zm.zhillsb_id=zls.id\n" + 
			"  and f.lie_id=sl.lie_id\n" + 
			"  and j.id=" + getJiesID() + "\n" + 
			"  and zm.zhuanmlb_id=(select id from zhuanmlb where mingc ='化验编码')";
		ResultSetList rs1 = con.getResultSetList(sql1);
		if(rs1.next()){
			tb.setCellValue(1, 4, rs1.getString("kouk"));
			tb.setCellValue(1, 9, CustomMaths.Round_new(rs1.getDouble("hej"),2)+"");
		}
		
		int ArrWidth[] = new int[] { 70, 70, 70, 70, 70, 70, 70, 70, 70, 20  };
		tb.setWidth(ArrWidth);
		Report rt = new Report();
		rt.setBody(tb);
		
		Cell c = new Cell();
		c.setBorderNone();
		rt.title = new Table(1, ArrWidth.length, c);
		rt.title.setWidth(ArrWidth);
		rt.title.setBorderNone();
		
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
//	private String treeid;

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
