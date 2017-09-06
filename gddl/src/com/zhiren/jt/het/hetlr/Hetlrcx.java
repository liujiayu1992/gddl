package com.zhiren.jt.het.hetlr;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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
import org.apache.tapestry.contrib.palette.SortMode;

public class Hetlrcx extends BasePage implements PageValidateListener{
//			界面用户提示
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
	
//			页面变化记录
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
		if(getYewlxValue().getId()==1 || getYewlxValue().getId()==2){
			return getHetlrcxfcfk();
		}else if(getYewlxValue().getId()==3||getYewlxValue().getId()==4){
			return getHetlrcxfc();
		}else{
			return "业务类型选择错误";
		}

	}
	public String riqWhere(){
		String riqwhere="";
		long riqDrop=getRiqValue().getId();
		if(riqDrop==1){
			riqwhere=" h.qiandrq";
		}else if(riqDrop==2){
			riqwhere=" h.gonghksrq";
		}else if(riqDrop==3){
			riqwhere=" h.gonghjsrq";
		}
		return riqwhere;
	}
	
	private String getHetlrcxfc(){
		JDBCcon con = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "";
		int iFixedRows = 0;// 固定行号
		String titledate = "";// 标题日期
		titledate = " 合同录入查询 ";
		StringBuffer sb = new StringBuffer();
		StringBuffer gyswhere=new StringBuffer();
//		StringBuffer dcwhere=new StringBuffer();
		if(getGongysValue().getId()!=-1){
			gyswhere.append(" and h.gongysb_id=").append(getGongysValue().getId()).append(" \n");
		}else{
			gyswhere.append(" \n");
		}
		String where="";
		if(this.getDiancTreeJib()==3){
			where =" and  d.id="+this.getTreeid();
		}else if(this.getDiancTreeJib()==2){
			where =" and (d.id="+this.getTreeid()+" or d.fuid="+this.getTreeid()+" )\n";
		}
		if(this.getYunsfsValue().getId()!=-1){
			where+= " and h.yunsfsb_id="+this.getYunsfsValue().getId()+" \n ";
		}
		if(getYewlxValue().getId()==3){
			sb.append("select  --grouping(g.mingc) g,grouping(d.mingc) d, grouping(h.hetbh) h,\n" +
					"decode(grouping(d.mingc),1,'总计',d.mingc ) danw ,\n" + 
					"decode(grouping(h.hetbh)+grouping(d.mingc),1,'合计',h.hetbh) htbh,\n" + 
					"to_char(h.gonghksrq,'yyyy-mm-dd') gonghksrq,\n" + 
					"to_char(h.gonghjsrq,'yyyy-mm-dd') gonghjsrq,\n" + 
					"to_char(h.qiandrq,'yyyy-mm-dd') qiandrq,\n" + 
					"round(sum(h.hetsl),2) hetsl,\n" + 
					"round(decode(sum(decode(nvl(h.hetjg,0),0,0,h.hetsl)),0,0,sum(nvl(h.hetjg,0)*h.hetsl)/sum(decode(nvl(h.hetjg,0),0,0,h.hetsl))),2) hetjg,\n" + 
					"decode(h.shifhyf,1,'是',0,'否') shifhyf,\n" + 
					"h.yunf,\n" + 
					"round(decode(sum(decode(nvl(h.rezbz,0),0,0,h.hetsl)),0,0,sum(nvl(h.rezbz,0)*h.hetsl)/sum(decode(nvl(h.rezbz,0),0,0,h.hetsl))),0) rezbz,\n" + 
					"round(decode(sum(decode(nvl(h.liubz,0),0,0,h.hetsl)),0,0,sum(nvl(h.liubz,0)*h.hetsl)/sum(decode(nvl(h.liubz,0),0,0,h.hetsl))),2) liubz,\n" + 
					"h.rezjfbz,\n" + 
					"h.liujfbz,\n" + 
					"decode(h.jilfs,0,'厂方',1,'矿方',2,'协商') as jilfs,\n" + 
					"decode(h.zhilfs,0,'厂方',1,'矿方',2,'协商') zhilfs,\n" + 
					"y.mingc yunsfs\n" + 
					"from hetxxb_dtgj h,diancxxb d,gongysb g , yunsfsb y\n" + 
					"where h.diancxxb_id=d.id and g.id=h.gongysb_id  and y.id=h.yunsfsb_id\n" + 
					where+"\n" + 
					" and "+riqWhere()+">=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
					" and "+riqWhere()+"<to_date('"+getRiq2()+"','yyyy-mm-dd')+1\n" + 
					gyswhere+
					"group by rollup(d.mingc,(hetbh,qiandrq,gonghksrq,gonghjsrq,hetsl,hetjg,shifhyf,yunf,rezbz,liubz,rezjfbz,liujfbz,h.jilfs,zhilfs,y.mingc) )\n" + 
					"order by grouping(d.mingc) desc, d.mingc,grouping(h.hetbh)desc,hetbh,qiandrq");
		}else{
			sb.append("select  --grouping(g.mingc) g,grouping(d.mingc) d, grouping(h.hetbh) h,\n" +
					"decode(grouping(g.mingc),1,'总计',g.mingc ) danw ,\n" + 
					"decode(grouping(h.hetbh)+grouping(g.mingc),1,'合计',h.hetbh) htbh,\n" + 
					"to_char(h.gonghksrq,'yyyy-mm-dd') gonghksrq,\n" + 
					"to_char(h.gonghjsrq,'yyyy-mm-dd') gonghjsrq,\n" + 
					"to_char(h.qiandrq,'yyyy-mm-dd') qiandrq,\n" + 
					"round(sum(h.hetsl),2) hetsl,\n" + 
					"round(decode(sum(decode(nvl(h.hetjg,0),0,0,h.hetsl)),0,0,sum(nvl(h.hetjg,0)*h.hetsl)/sum(decode(nvl(h.hetjg,0),0,0,h.hetsl))),2) hetjg,\n" + 
					"decode(h.shifhyf,1,'是',0,'否') shifhyf,\n" + 
					"h.yunf,\n" + 
					"round(decode(sum(decode(nvl(h.rezbz,0),0,0,h.hetsl)),0,0,sum(nvl(h.rezbz,0)*h.hetsl)/sum(decode(nvl(h.rezbz,0),0,0,h.hetsl))),0) rezbz,\n" + 
					"round(decode(sum(decode(nvl(h.liubz,0),0,0,h.hetsl)),0,0,sum(nvl(h.liubz,0)*h.hetsl)/sum(decode(nvl(h.liubz,0),0,0,h.hetsl))),2) liubz,\n" + 
					"h.rezjfbz,\n" + 
					"h.liujfbz,\n" + 
					"decode(h.jilfs,0,'厂方',1,'矿方',2,'协商') as jilfs,\n" + 
					"decode(h.zhilfs,0,'厂方',1,'矿方',2,'协商') zhilfs,\n" + 
					"y.mingc yunsfs\n" + 
					"from hetxxb_dtgj h,diancxxb d,gongysb g , yunsfsb y\n" + 
					"where h.diancxxb_id=d.id and g.id=h.gongysb_id  and y.id=h.yunsfsb_id\n" + 
					where+"\n" + 
					" and "+riqWhere()+">=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
					" and "+riqWhere()+"<to_date('"+getRiq2()+"','yyyy-mm-dd')+1\n" + 
					gyswhere+
					"group by rollup(g.mingc,(hetbh,qiandrq,gonghksrq,gonghjsrq,hetsl,hetjg,shifhyf,yunf,rezbz,liubz,rezjfbz,liujfbz,h.jilfs,zhilfs,y.mingc) )\n" + 
					"order by grouping(g.mingc) desc, g.mingc,grouping(h.hetbh)desc,hetbh,qiandrq");
		}

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader=  new String[2][16];
		
		if(getYewlxValue().getId()==3){
			ArrHeader[0] = new String[]{"单位","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","质量标准","质量标准","奖罚标准","奖罚标准","计量方式","质量方式","运输方式"};
			ArrHeader[1] = new String[]{"单位","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","Qnet,ar","St,d","Qnet,ar","St,d","计量方式","质量方式","运输方式"};
		}else{
			ArrHeader[0] = new String[]{"供应商","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","质量标准","质量标准","奖罚标准","奖罚标准","计量方式","质量方式","运输方式"};
			ArrHeader[1] = new String[]{"供应商","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","Qnet,ar","St,d","Qnet,ar","St,d","计量方式","质量方式","运输方式"};
		}
		
		ArrWidth = new int[] {100,100,80,80,80,65,50,40,65,40,40,150,80,65,65,65};
		String ArrFormat[]=new String[]{"","","","","","","0.00","0.00","","0.00","0.0","","","","",""};
		iFixedRows = 2;
		// 数据
		rt.setBody(new Table(rs, 2, 0, iFixedRows));
//				设置表头
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19); 
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 4, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 5, "单位：万吨、元/吨、千卡/千克、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColFormat(ArrFormat);
		rt.body.setColAlign(3,Table.ALIGN_CENTER);
		rt.body.setColAlign(9,Table.ALIGN_CENTER);
		rt.body.setColAlign(13,Table.ALIGN_LEFT);
		rt.body.setColAlign(14,Table.ALIGN_LEFT);
		rt.body.setColAlign(15,Table.ALIGN_CENTER);
		rt.body.setColAlign(16,Table.ALIGN_CENTER);
		rt.body.setColAlign(17,Table.ALIGN_CENTER);
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

	private String getHetlrcxfcfk(){
		JDBCcon con = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "";
		int iFixedRows = 0;// 固定行号
		String titledate = "";// 标题日期
		titledate = " 合同录入查询 ";
		StringBuffer sb = new StringBuffer();
		StringBuffer gyswhere=new StringBuffer();
//		StringBuffer dcwhere=new StringBuffer();
		if(getGongysValue().getId()!=-1){
			gyswhere.append(" and h.gongysb_id=").append(getGongysValue().getId()).append(" \n");
		}else{
			gyswhere.append(" \n");
		}
		String where="";
		if(this.getDiancTreeJib()==3){
			where =" and  d.id="+this.getTreeid();
		}else if(this.getDiancTreeJib()==2){
			where =" and (d.id="+this.getTreeid()+" or d.fuid="+this.getTreeid()+" )\n";
		}
		if(this.getYunsfsValue().getId()!=-1){
			where+= " and h.yunsfsb_id="+this.getYunsfsValue().getId()+" \n ";
		}
		
		if(getYewlxValue().getId()==1){
			sb.append("select --grouping(d.mingc) d, grouping(g.mingc) g, grouping(h.hetbh) h,\n" +
					"decode(grouping(d.mingc),1,'总计',d.mingc) danw,\n" + 
					"decode(grouping(d.mingc)+grouping(g.mingc),1,'合计',g.mingc ) gys ,\n" + 
					"decode(grouping(h.hetbh)+grouping(g.mingc),1,'小计',h.hetbh) htbh,\n" + 
					"to_char(h.gonghksrq,'yyyy-mm-dd') gonghksrq,\n" + 
					"to_char(h.gonghjsrq,'yyyy-mm-dd') gonghjsrq,\n" + 
					"to_char(h.qiandrq,'yyyy-mm-dd') qiandrq,\n" + 
					"round(sum(h.hetsl),2) hetsl,\n" + 
					"round(decode(sum(decode(nvl(h.hetjg,0),0,0,h.hetsl)),0,0,sum(nvl(h.hetjg,0)*h.hetsl)/sum(decode(nvl(h.hetjg,0),0,0,h.hetsl))),2) hetjg,\n" + 
					"decode(h.shifhyf,1,'是',0,'否') shifhyf,\n" + 
					"h.yunf,\n" + 
					"round(decode(sum(decode(nvl(h.rezbz,0),0,0,h.hetsl)),0,0,sum(nvl(h.rezbz,0)*h.hetsl)/sum(decode(nvl(h.rezbz,0),0,0,h.hetsl))),0) rezbz,\n" + 
					"round(decode(sum(decode(nvl(h.liubz,0),0,0,h.hetsl)),0,0,sum(nvl(h.liubz,0)*h.hetsl)/sum(decode(nvl(h.liubz,0),0,0,h.hetsl))),2) liubz,\n" + 
					"h.rezjfbz,\n" + 
					"h.liujfbz,\n" + 
					"decode(h.jilfs,0,'厂方',1,'矿方',2,'协商') as jilfs,\n" + 
					"decode(h.zhilfs,0,'厂方',1,'矿方',2,'协商') zhilfs,\n" + 
					"y.mingc yunsfs\n" + 
					"from hetxxb_dtgj h,diancxxb d,gongysb g , yunsfsb y\n" + 
					"where h.diancxxb_id=d.id and g.id=h.gongysb_id  and y.id=h.yunsfsb_id\n" + 
					where+"\n" + 
					" and "+riqWhere()+">=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
					" and "+riqWhere()+"<to_date('"+getRiq2()+"','yyyy-mm-dd')+1\n" + 
					gyswhere+
					"group by rollup(d.mingc,g.mingc,(hetbh,qiandrq,gonghksrq,gonghjsrq,hetsl,hetjg,shifhyf,yunf,rezbz,liubz,rezjfbz,liujfbz,h.jilfs,zhilfs,y.mingc) )\n" + 
					" order by grouping(d.mingc) desc, d.mingc,grouping(g.mingc) desc,g.mingc,grouping(h.hetbh)desc,hetbh,qiandrq");
		}else{
			sb.append("select  --grouping(g.mingc) g,grouping(d.mingc) d, grouping(h.hetbh) h,\n" +
					"decode(grouping(g.mingc),1,'总计',g.mingc) gys,\n" + 
					"decode(grouping(d.mingc)+grouping(g.mingc),1,'合计',d.mingc ) danw ,\n" + 
					"decode(grouping(h.hetbh)+grouping(d.mingc),1,'小计',h.hetbh) htbh,\n" + 
					"to_char(h.gonghksrq,'yyyy-mm-dd') gonghksrq,\n" + 
					"to_char(h.gonghjsrq,'yyyy-mm-dd') gonghjsrq,\n" + 
					"to_char(h.qiandrq,'yyyy-mm-dd') qiandrq,\n" + 
					"round(sum(h.hetsl),2) hetsl,\n" + 
					"round(decode(sum(decode(nvl(h.hetjg,0),0,0,h.hetsl)),0,0,sum(nvl(h.hetjg,0)*h.hetsl)/sum(decode(nvl(h.hetjg,0),0,0,h.hetsl))),2) hetjg,\n" + 
					"decode(h.shifhyf,1,'是',0,'否') shifhyf,\n" + 
					"h.yunf,\n" + 
					"round(decode(sum(decode(nvl(h.rezbz,0),0,0,h.hetsl)),0,0,sum(nvl(h.rezbz,0)*h.hetsl)/sum(decode(nvl(h.rezbz,0),0,0,h.hetsl))),0) rezbz,\n" + 
					"round(decode(sum(decode(nvl(h.liubz,0),0,0,h.hetsl)),0,0,sum(nvl(h.liubz,0)*h.hetsl)/sum(decode(nvl(h.liubz,0),0,0,h.hetsl))),2) liubz,\n" + 
					"h.rezjfbz,\n" + 
					"h.liujfbz,\n" + 
					"decode(h.jilfs,0,'厂方',1,'矿方',2,'协商') as jilfs,\n" + 
					"decode(h.zhilfs,0,'厂方',1,'矿方',2,'协商') zhilfs,\n" + 
					"y.mingc yunsfs\n" + 
					"from hetxxb_dtgj h,diancxxb d,gongysb g , yunsfsb y\n" + 
					"where h.diancxxb_id=d.id and g.id=h.gongysb_id  and y.id=h.yunsfsb_id\n" + 
					where+"\n" + 
					" and "+riqWhere()+">=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
					" and "+riqWhere()+"<to_date('"+getRiq2()+"','yyyy-mm-dd')+1\n" + 
					gyswhere+
					"group by rollup(g.mingc,d.mingc,(hetbh,qiandrq,gonghksrq,gonghjsrq,hetsl,hetjg,shifhyf,yunf,rezbz,liubz,rezjfbz,liujfbz,h.jilfs,zhilfs,y.mingc) )\n" + 
					"order by grouping(g.mingc) desc,g.mingc,grouping(d.mingc) desc, d.mingc,grouping(h.hetbh)desc,hetbh,qiandrq");
		}

		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader=  new String[2][17];
		
		if(getYewlxValue().getId()==1){
			ArrHeader[0] = new String[]{"单位","供应商","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","质量标准","质量标准","奖罚标准","奖罚标准","计量方式","质量方式","运输方式"};
			ArrHeader[1] = new String[]{"单位","供应商","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","Qnet,ar","St,d","Qnet,ar","St,d","计量方式","质量方式","运输方式"};
		}else{
			ArrHeader[0] = new String[]{"供应商","单位","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","质量标准","质量标准","奖罚标准","奖罚标准","计量方式","质量方式","运输方式"};
			ArrHeader[1] = new String[]{"供应商","单位","合同编号", "供货开始日期","供货结束日期","签订日期","煤量<br>(万吨)","价格","是否含运费","运费","Qnet,ar","St,d","Qnet,ar","St,d","计量方式","质量方式","运输方式"};
		}
		
		ArrWidth = new int[] {100,100,100,80,80,80,65,50,40,65,40,40,150,80,65,65,65};
		String ArrFormat[]=new String[]{"","","","","","","0.00","0.00","","0.00","0.00","0.00","","","","",""};
		iFixedRows = 2;
		// 数据
		rt.setBody(new Table(rs, 2, 0, iFixedRows));
//				设置表头
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19); 
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 4, "报表日期：" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 6, "单位：万吨、元/吨、千卡/千克、%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColFormat(ArrFormat);
		rt.body.setColAlign(3,Table.ALIGN_CENTER);
		rt.body.setColAlign(9,Table.ALIGN_CENTER);
		rt.body.setColAlign(13,Table.ALIGN_LEFT);
		rt.body.setColAlign(14,Table.ALIGN_LEFT);
		rt.body.setColAlign(15,Table.ALIGN_CENTER);
		rt.body.setColAlign(16,Table.ALIGN_CENTER);
		rt.body.setColAlign(17,Table.ALIGN_CENTER);
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

	public SortMode getSort() {
		return SortMode.USER;
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	
	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期类型"));
		ComboBox Riq = new ComboBox();
		Riq.setTransform("RiqDropDown");
		Riq.setEditable(false);
		Riq.setLazyRender(true);// 动态绑定
		Riq.setWidth(100);
		Riq.setReadOnly(true);
		Riq.setId("RiqDrop");
		Riq.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(Riq);
		tb1.addText(new ToolbarText("-"));
		//设置
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		//设置电厂树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"Form0",null,getTreeid());
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
		//设置供应商
		//////////////////////////////////////////
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox hetxxb_gys = new ComboBox();
		hetxxb_gys.setTransform("GongysDropDown");
		hetxxb_gys.setEditable(true);
		hetxxb_gys.setWidth(100);
		hetxxb_gys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(hetxxb_gys);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("运输方式"));
		ComboBox yunsfs = new ComboBox();
		yunsfs.setTransform("YunsfsDropDown");
		yunsfs.setEditable(false);
		yunsfs.setLazyRender(true);// 动态绑定
		yunsfs.setWidth(70);
		yunsfs.setReadOnly(true);
		yunsfs.setId("YunsfsDrop");
		yunsfs.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yunsfs);
		tb1.addText(new ToolbarText("类型"));
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("YEWLX");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(80);
		comb1.setReadOnly(true);
		comb1.setId("Yewlxmc");
		comb1.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(comb1);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		setToolbar(tb1);
	}
	///////////////////////////////////////////////////////////////////////
//	绑定运输方式
	private IDropDownBean _Yunsfs;
	public IDropDownBean getYunsfsValue() {
		if (_Yunsfs == null) {
			_Yunsfs = (IDropDownBean) getYunsfsModel().getOption(0);
		}
		return _Yunsfs;
	}
	public void setYunsfsValue(IDropDownBean Value) {
		_Yunsfs = Value;
	}

	private IPropertySelectionModel _YunsfsModel;
	public void setYunsfsModel(IPropertySelectionModel value) {
		_YunsfsModel = value;
	}
	public IPropertySelectionModel getYunsfsModel() {
		if (_YunsfsModel == null) {
			getYunsfsModels();
		}
		return _YunsfsModel;
	}

	public void getYunsfsModels() {
		String yunsfsSql = "select id,mingc from yunsfsb order by id";
		_YunsfsModel = new IDropDownModel(yunsfsSql,"全部");

	}
	private IDropDownBean _Riq;
	public IDropDownBean getRiqValue() {
		if (_Riq == null) {
			_Riq = (IDropDownBean) getRiqModel().getOption(0);
		}
		return _Riq;
	}
	public void setRiqValue(IDropDownBean Value) {
		_Riq = Value;
	}

	private IPropertySelectionModel _RiqModel;
	public void setRiqModel(IPropertySelectionModel value) {
		_RiqModel = value;
	}
	public IPropertySelectionModel getRiqModel() {
		if (_RiqModel == null) {
			getRiqModels();
		}
		return _RiqModel;
	}

	public void getRiqModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "签订日期"));
		list.add(new IDropDownBean(2, "供货开始日期"));
		list.add(new IDropDownBean(3, "供货结束日期"));
		_RiqModel = new IDropDownModel(list);

	}
	
//	绑定业务类型
	private IDropDownBean _Yewlx;
	public IDropDownBean getYewlxValue() {
		if (_Yewlx == null) {
			_Yewlx = (IDropDownBean) getYewlxModel().getOption(0);
		}
		return _Yewlx;
	}
	public void setYewlxValue(IDropDownBean Value) {
		_Yewlx = Value;
	}

	private IPropertySelectionModel _YewlxModel;
	public void setYewlxModel(IPropertySelectionModel value) {
		_YewlxModel = value;
	}
	public IPropertySelectionModel getYewlxModel() {
		if (_YewlxModel == null) {
			getYewlxModels();
		}
		return _YewlxModel;
	}

	public void getYewlxModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂分矿"));
		list.add(new IDropDownBean(2, "分矿分厂"));
		list.add(new IDropDownBean(3, "分厂"));
		list.add(new IDropDownBean(4, "分矿"));
		_YewlxModel = new IDropDownModel(list);

	}
	// 供应商
	public boolean _Gongyschange = false;

	public IDropDownBean getGongysValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setGongysValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getGongysModels() {
		String where="";
		if(this.getDiancTreeJib()==3){
			where =" and  dc.id="+this.getTreeid();
		}else if(this.getDiancTreeJib()==2){
			where =" and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" )\n";
		}
		
		String sql="select gys.id, gys.mingc from\n" +
			"(select g.id, g.mingc\n" + 
			"  from diancxxb d, diancgysglb gl, gongysb g\n" + 
			" where d.id = gl.diancxxb_id\n" + 
			"   and gl.gongysb_id = g.id\n" + 
			" group by (g.id, g.mingc)) gys,\n" + 
			" (select distinct ht.gongysb_id from hetxxb_dtgj ht, diancxxb dc where dc.id=ht.diancxxb_id "+where+" )het\n" + 
			" where het.gongysb_id=gys.id";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
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
				gongysChange=true; 
			}
		}
		this.treeid = treeid;
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
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
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
	
	private boolean gongysChange ;
//			 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setYewlxValue(null);
			setYewlxModel(null);
			setRiqValue(null);
			setRiqModel(null);
			setYunsfsValue(null);
			setYunsfsModel(null);
			setDiancmcModels();
			setDiancjb();
			setGongysValue(null);
			setGongysModel(null);
			gongysChange=false;
		}
		if(gongysChange){
			setGongysValue(null);
			setGongysModel(null);
			gongysChange=false;
		}
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

