package com.zhiren.haiym.baob;

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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ruckfhy extends BasePage  implements PageValidateListener{

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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRucmzjyyb();
	}
	
	private boolean _QueryClick = false;
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}
	
	private int[] ArrWidth=null;
	private String[][] ArrHeader=null;
	private String type=new String();
	private int intFixedCols=0;
	
	//判断统计方式
	private void tongjfs(){
		if(getTongjfsValue().getId()==1){
			type="anmk";
		}else if(getTongjfsValue().getId()==2){
			type="anmz";
		}else if(getTongjfsValue().getId()==3){
			type="angys";
		}
	}
	
	private String getRucmzjyyb() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
//		String s="";
//		if(!this.hasDianc(this.getTreeid_dc())){
//			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
//		}
		tongjfs();
		String sql = new String();
		if("anmk".equals(type)){
			sql=
				"select\n" +
				"       decode(grouping(g.mingc), 1, '合计', g.mingc) as gmingc,\n" + 
				"       decode(grouping(m.mingc)+grouping(g.mingc), 1, '地区合计', m.mingc) as mmingc,\n" + 
				"		decode(grouping(m.mingc)+grouping(l.mingc), 1, '单位小计', l.mingc) as lmingc,\n" + 
				"       to_char(f.daohrq,'yyyy-mm-dd') librq,\n"+
						getZhilsql() +
				"		zl.caiyml,\n" +
				"       f.biaoz\n" + 
				"from fahb f,zhilb z,zhillsb zl,gongysb g,meikxxb m,luncxxb l,diancxxb d\n" + 
				"where f.gongysb_id = g.id(+)\n" + 
				"  and f.meikxxb_id = m.id(+)\n" + 
				"  and f.luncxxb_id = l.id(+)\n" + 
				"  and f.zhilb_id = z.id(+)\n" + 
				"  and z.id = zl.zhilb_id\n" + 
				"  and f.diancxxb_id = d.id\n" + 
				"  and (f.diancxxb_id=" + getTreeid_dc() + "\n"+
				"   or d.fuid = " + getTreeid_dc() + ")\n"+ 
				"  and f.daohrq >= to_date('" + getRiq() + "', 'yyyy-mm-dd')\n" + 
				"  and f.daohrq <= to_date('" + getAfter() + "', 'yyyy-mm-dd')\n" + 
				"group by rollup(g.mingc, m.mingc,(l.mingc, f.daohrq,zl.caiyml,f.biaoz))\n" + 
				"order by g.mingc,m.mingc,l.mingc,f.daohrq\n" ;
		}else if("anmz".equals(type)){
			sql=
				"select\n" +
				"		decode(grouping(p.mingc), 1, '合计', p.mingc) as pmingc,\n" +
				"       decode(grouping(p.mingc)+grouping(l.mingc), 1, '单位小计', l.mingc) as lmingc,\n" + 
				"       to_char(f.daohrq,'yyyy-mm-dd') librq,"+
						getZhilsql() +
				"		zl.caiyml,\n" +
				"       f.biaoz\n" + 
				"from fahb f,zhilb z,zhillsb zl,meikxxb m,luncxxb l,pinzb p,diancxxb d\n" + 
				"where f.meikxxb_id = m.id(+)\n" + 
				"  and f.luncxxb_id = l.id(+)\n" + 
				"  and f.zhilb_id = z.id(+)\n" + 
				"  and z.id = zl.zhilb_id\n" + 
				"  and f.pinzb_id = p.id(+)\n" +
				"  and f.diancxxb_id = d.id\n" + 
				"  and (f.diancxxb_id=" + getTreeid_dc() + "\n"+
				"   or d.fuid = " + getTreeid_dc() + ")\n"+
				"  and f.daohrq >= to_date('" + getRiq() + "', 'yyyy-mm-dd')\n" + 
				"  and f.daohrq <= to_date('" + getAfter() + "', 'yyyy-mm-dd')\n" + 
				"group by rollup(p.mingc, (l.mingc,f.daohrq,zl.caiyml,f.biaoz))\n" + 
				"order by p.mingc,l.mingc,f.daohrq" ;
		}else if("angys".equals(type)){
			sql=
				"select\n" +
				"gmingc,\n" + 
				"lmingc,\n" +
				"getHtmlAlert('http://localhost:8086/zgdt','Huaybgd_bianh','lx','zhilb'||','||huaybh,huaybm),\n" +
				"huaysj,mt,aar,var,fcar,qnet_ar,star,mad,\n" + 
				"aad,vad,fcad,qgrad,stad,ad,vd,fcd,qgrd,std,vdaf,qbad,had,\n" + 
				"caiyml,\n" + 
				"huayy\n" + 
				"from (\n" + 
				"select\n" + 
				"	huaybh,\n" + 
				"   huaybm,\n" +
				"	decode(gmingc,null,'合计',gmingc) gmingc,\n" + 
				"	lmingc,huaysj,mt,aar,var,fcar,qnet_ar,star,mad,\n" + 
				"	aad,vad,fcad,qgrad,stad,ad,vd,fcd,qgrd,std,vdaf,qbad,had,\n" + 
				"	caiyml,\n" + 
				"	huayy\n" +
				"from\n" + 
				"(select\n" + 
				"        z.id,\n" + 
				"        0 flag,\n" + 
				"        grouping(g.mingc) gysfz,\n" + 
				"        grouping(z.id) zlfz,\n" + 
				"        decode(z.huaybh,null,'-1',z.huaybh) huaybh,\n" + 
				"		 zm.bianm huaybm,\n" +
				"        g.mingc gmingc,\n" + 
				"        decode(grouping(z.id)+grouping(g.mingc),0,l.mingc||'\'||f.chec||'平均样',1,'小计') lmingc,\n" + 
				"        to_char(z.huaysj,'yyyy-mm-dd') huaysj,\n" + 
						 getZhilsql() +			
				"        sum(zl.caiyml) caiyml ,\n" + 
				"        z.huayy\n" + 
				"from zhilb z,fahb f,gongysb g,luncxxb l,zhillsb zl,diancxxb d,zhuanmb zm,zhuanmlb zml\n" + 
				"where z.id=f.zhilb_id\n" + 
				"  and f.gongysb_id=g.id(+)\n" + 
				"  and f.luncxxb_id=l.id(+)\n" + 
				"  and zl.zhilb_id=z.id\n" + 
				"  and zl.id=zm.zhillsb_id\n" +
				"  and zm.zhuanmlb_id=zml.id\n" + 
				"  and zml.jib=3\n" + 
				"  and f.diancxxb_id=d.id\n" + 
				"  and (f.diancxxb_id=" + getTreeid_dc() + "\n" + 
				"   or d.fuid = " + getTreeid_dc() + ")\n" + 
				"  and f.daohrq>=to_date('" + getRiq() + "','yyyy-mm-dd')\n" + 
				"  and f.daohrq<=to_date('" + getAfter() + "','yyyy-mm-dd')\n" + 
				"group by rollup(g.mingc,(z.id,l.mingc,z.huaybh,z.huaysj,f.chec,z.huayy,zl.caiyml,zm.bianm))\n" + 
				"\n" + 
				"union ALL\n" + 
				"select  z.zhilb_id id,\n" + 
				"        1 flag,\n" + 
				"        0 as gysfz,\n" + 
				"        0 as zhilfz,\n" + 
				"        zl.huaybh,\n" + 
				"		 zm.bianm huaybm,\n"+
				"        g.mingc gmingc,\n" + 
				"        l.mingc||'/'||f.chec lmingc,\n" + 
				"        to_char(z.huaysj,'yyyy-mm-dd') huaysj,\n" + 
				"        -------ar\n" + 
				"        round_new(z.mt,1) mt,\n" + 
				"        round_new(z.aar,2) aar,\n" + 
				"        round_new(z.var,2) var,\n" + 
				"        round_new(100-z.mt-z.aar-z.var,2) fcar,\n" + 
				"        round_new(z.qnet_ar,2) qnet_ar,\n" + 
				"        round_new(z.stad*(100-z.mt)/(100-z.mad),2) star,\n" + 
				"        --------ad\n" + 
				"        round_new(z.mad,2) mad,\n" + 
				"        round_new(z.aad,2) aad,\n" + 
				"        round_new(z.vad,2) vad,\n" + 
				"        round_new(z.fcad,2) fcad,\n" + 
				"        round_new(z.qgrad,2) qgrad,\n" + 
				"        round_new(z.stad,2) stad,\n" + 
				"        ---------d\n" + 
				"        round_new(z.ad,2) ad,\n" + 
				"        round_new(z.vad*100/(100-z.mad),2) vd,\n" + 
				"        round_new(100-z.ad-(z.vad*100/(100-z.mad)),2) fcd,\n" + 
				"        round_new(z.qgrd,2) qgrd,\n" + 
				"        round_new(z.std,2) std,\n" + 
				"        ---------\n" + 
				"        round_new(z.vdaf,2) vdaf,\n" + 
				"        round_new(z.qbad,2) qbad,\n" + 
				"        round_new(z.had,2) had,\n" + 
				"        z.caiyml,\n" + 
				"        z.huayy\n" + 
				"from zhillsb z,zhilb zl,fahb f,luncxxb l,gongysb g,diancxxb d,zhuanmb zm,zhuanmlb zml\n" + 
				"where f.zhilb_id=zl.id\n" + 
				"  and z.zhilb_id=zl.id\n" + 
				"  and f.luncxxb_id=l.id(+)\n" + 
				"  and f.gongysb_id=g.id(+)\n" + 
				"  and f.diancxxb_id=d.id\n" + 
				"  and z.id=zm.zhillsb_id\n" +
				"  and zm.zhuanmlb_id=zml.id\n" + 
				"  and zml.jib=3\n" +
				"  and (f.diancxxb_id=" + getTreeid_dc() + "\n" + 
				"   or d.fuid = " + getTreeid_dc() + ")\n" + 
				"  and f.daohrq>=to_date('" + getRiq() + "','yyyy-mm-dd')\n" + 
				"  and f.daohrq<=to_date('" + getAfter() + "','yyyy-mm-dd'))\n" + 
				"order by gysfz,gmingc,zlfz,id,flag desc,huaysj\n" + 
				"\n)";
		}
		ResultSetList rstmp = con.getResultSetList(sql);
		setArrWidth_strFormat_ArrHeader(type);
		rt.setTitle(v.getDiancqc() +"<br>厂方化验", ArrWidth);
//		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 26);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "到货日期:" + getRiq() + "至" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rstmp, 2, 0, intFixedCols));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
		rt.body.setPageRows(25);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		rt.setDefautlFooter(23, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(1, 2, "制表:", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
		rt.body.setRowHeight(21);
//		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤质检验月报","Rucmzjyyb");
		return rt.getAllPagesHtml();
	}

	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();
		//电厂
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
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("统计方式:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("TONGJFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "查询",
		"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		setToolbar(tb1);

	}
	
	private void setArrWidth_strFormat_ArrHeader(String value){
		if("anmk".equals(value)){
			intFixedCols=4;
			ArrHeader = new String[2][26];
			ArrHeader[0] = new String[] { "煤矿地区", "煤矿单位", "货船", "离泊时间",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"干燥基(d)","干燥基(d)",
    				"干燥基(d)","干燥基(d)","干燥基(d)",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","运<br>单<br>量<br>(吨)"};
    		ArrHeader[1] = new String[] { "煤矿地区", "煤矿单位", "货船", "离泊时间",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","高位<br>发热量<br>(MJ/Kg)<br>Qgr","全硫<br>(%)<br>St",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","运<br>单<br>量<br>(吨)"};
			ArrWidth = new int[26];
			ArrWidth = new int[] { 100, 90, 70, 70, 
									40, 40, 40, 40, 40, 40, 
									40, 40,	40, 40, 40, 40, 
									40, 40, 40, 40 ,40, 
									40, 40, 40, 40 ,50,};
		}else if("anmz".equals(value)){
			intFixedCols=3;
			ArrHeader = new String[2][25];
			ArrHeader[0] = new String[] { "品种", "货船", "离泊时间",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"干燥基(d)","干燥基(d)",
    				"干燥基(d)","干燥基(d)","干燥基(d)",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","运<br>单<br>量<br>(吨)"};
    		ArrHeader[1] = new String[] { "品种", "货船", "离泊时间",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","高位<br>发热量<br>(MJ/Kg)<br>Qgr","全硫<br>(%)<br>St",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","运<br>单<br>量<br>(吨)"};
			ArrWidth = new int[25];
			ArrWidth = new int[] {100, 70, 70, 
									40, 40, 40, 40, 40, 40, 
									40, 40,	40, 40, 40, 40, 
									40, 40, 40, 40 ,40, 
									40, 40, 40, 40 ,50,};
		}else if("angys".equals(value)){
			intFixedCols=3;
			ArrHeader = new String[2][26];
			ArrHeader[0] = new String[] { "供货单位", "货船", "编号", "报告时间",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"收到基(ar)","收到基(ar)","收到基(ar)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"空气干燥基(ad)","空气干燥基(ad)","空气干燥基(ad)",
    				"干燥基(d)","干燥基(d)",
    				"干燥基(d)","干燥基(d)","干燥基(d)",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","化验员"};
    		ArrHeader[1] = new String[] { "供货单位", "货船", "编号", "报告时间",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"水份<br>(%)<br>M","灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","低位<br>发热量<br>(MJ/Kg)<br>Qnet","全硫<br>(%)<br>St",
    				"灰份<br>(%)<br>A","挥发<br>分(%)<br>V",
    				"固定<br>碳(%)<br>FC","高位<br>发热量<br>(MJ/Kg)<br>Qgr","全硫<br>(%)<br>St",
    				
    				"干燥<br>无灰基<br>挥发分<br>Vdaf","弹筒<br>热值<br>Qbad",
    				"氢<br>Had","采样<br>煤量<br>(Kg)","化验员"};
			ArrWidth = new int[26];
			ArrWidth = new int[] { 100, 70, 90, 70, 
									40, 40, 40, 40, 40, 40, 
									40, 40,	40, 40, 40, 40, 
									40, 40, 40, 40 ,40, 
									40, 40, 40, 40 ,100,};
//			strFormat = new String[25];
//			strFormat = new String[] { "", "", "", 
//									"0.0" , "0.00", "0.00", "0.00", "0.00", "0.00", 
//									"0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
//									"0.00", "0.00", "0.00", "0.00", "0.00", 
//									"0.00", "0.00", "0.00", "0"   , "" };
		}
	}
	
//	质量sql
	private String getZhilsql(){
		String zhil=
			"---------------收到基(ar)\n" +
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.mt,1) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 1)) as mt,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.aar,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as aar,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.var,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as var,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(100-z.mt-z.aar-z.var,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as fcar,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.qnet_ar,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as qnet_ar,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.star,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as star,\n" + 
			"        ---------------------------空气干燥基(ad)\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.mad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as mad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.aad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as aad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.vad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as vad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.fcad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as fcad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.qgrad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as qgrad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.stad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as stad,\n" + 
			"        ---------------------------干燥基(d)\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.ad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as ad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.vad*100/(100-z.mad),2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as vd,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(100-z.ad-(z.vad*100/(100-z.mad)),2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as fcd,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.qgrd,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as qgrd,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.std,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as std,\n" + 
			"        ---------------------------\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.vdaf,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as vdaf,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.qbad,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as qbad,\n" + 
			"        decode(sum(round_new(f.laimsl,0)),0,0,\n" + 
			"              round_new(sum(round_new(z.had,2) * round_new(f.laimsl,0)) / sum(round_new(f.laimsl,0)), 2)) as had,\n";
		return zhil;
	}
	
//  判断电厂Tree中所选电厂时候还有子电厂   
//    private boolean hasDianc(String id){ 
//		JDBCcon con=new JDBCcon();
//		boolean mingc= false;
//		String sql="select mingc from diancxxb where fuid = " + id;
//		ResultSetList rsl=con.getResultSetList(sql);
//		if(rsl.next()){
//			mingc=true;
//		}
//		rsl.close();
//		return mingc;
//	}
	
//	 统计方式下拉框
	private IDropDownBean TongjfsValue;
	public IDropDownBean getTongjfsValue() {
		if (TongjfsValue == null) {
			TongjfsValue = (IDropDownBean) TongjfsModel.getOption(0);
		}
		return TongjfsValue;
	}
	public void setTongjfsValue(IDropDownBean Value) {
		if (!(TongjfsValue == Value)) {
			TongjfsValue = Value;
		}
	}
	private IPropertySelectionModel TongjfsModel;
	public void setTongjfsModel(IPropertySelectionModel value) {
		TongjfsModel = value;
	}
	public IPropertySelectionModel getTongjfsModel() {
		if (TongjfsModel == null) {
			getTongjfsModels();
		}
		return TongjfsModel;
	}
	public IPropertySelectionModel getTongjfsModels() {
		String sql = "select 1 id,'按煤矿' mingc from dual " +
					"union select 2 id,'按煤种' mingc from dual " +
					"union select 3 id,'按供货商' mingc from dual";
		TongjfsModel = new IDropDownModel(sql);
		return TongjfsModel;
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
				setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
				setTongjfsValue(null);
				setTongjfsModel(null);
		}
		blnIsBegin = true;
		getSelectData();
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
