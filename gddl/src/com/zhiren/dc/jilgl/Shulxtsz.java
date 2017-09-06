package com.zhiren.dc.jilgl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.Radio;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
/**
 * 
 * @author Rock
 *
 */
/*
 * 2009-05-18
 * 刘雨
 * 新增检斤单设置、补录信息设置、汽车检斤设置、数据导入设置
 */
/*
 * 2009-05-15
 * 王磊
 * 数据修改默认多行替换的初始化值不正确
 */
/*
 * 2009-02-23
 * 王磊
 * 数量系统设置中增加火车、汽车默认运损率的设置
 */
/*
 * 作者：尹佳明
 * 时间：2009-05-25
 * 描述：增加是否自动生成采样编号、扣水扣杂是否显示、收耗存日报来煤量休约、按质量分列四个参数
 */
public abstract class Shulxtsz extends BasePage {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() { 
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
		}
	}
	
	private String getInsertSql(int xuh,String diancxxb_id,String mingc,String zhi,String danw,String leib,int zhuangt,String beiz) {
		String sql = "insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz) values(getnewid("
			+ diancxxb_id + ")," + xuh + "," + diancxxb_id + ",'" + mingc + "','" + zhi + "','" + danw +"','" 
			+ leib + "'," + zhuangt + ",'" + beiz +"')";
		return sql;
	}
	
	private String getXitxxSql(String zd,String mingc, String danw, String diancxxb_id) {
    	String sdanw = "";
    	if(danw != null) {
    		sdanw = " and danw='"+danw+"'";
    	}
    	String sql = "select "+zd+" from xitxxb where leib='数量' and mingc='"+mingc
    	+"' and diancxxb_id=" + diancxxb_id + sdanw;
    	return sql;
    }
	
	private String UpdateXitxxSql(String zd,String zhi,String mingc, String danw, String diancxxb_id) {
		String sdanw = "";
    	if(danw != null) {
    		sdanw = " and danw='"+danw+"'";
    	}
		String sql = "update xitxxb set "+zd+"='"+zhi+"' where leib='数量' and mingc='"+mingc
		+"' and diancxxb_id=" + diancxxb_id + sdanw;
		return sql;
	}
//  取得初始化数据
    public String getInitData() {
    	Visit visit = (Visit)this.getPage().getVisit();
    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	
//    	到货确认设置
    	boolean guohdh = true;
    	boolean shenhdh = false;
    	ResultSetList rsl = con.getResultSetList(getXitxxSql("zhi","到货确认设置",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("过衡".equals(rsl.getString("zhi"))) {
    			guohdh = true;
    		}else
    			if("审核".equals(rsl.getString("zhi"))) {
        			guohdh = false;
        			shenhdh = true;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"到货确认设置","过衡","","数量",1,""));
    	}
    	Radio rd1g = new Radio("daoh");
    	rd1g.setBoxLabel("过衡即到货");
    	rd1g.setId("rd1g");
    	rd1g.setChecked(guohdh);
    	initData = initData + rd1g.getScript() + "\n";
    	
    	Radio rd1s = new Radio("daoh");
    	rd1s.setBoxLabel("审核即到货");
    	rd1s.setId("rd1s");
    	rd1s.setChecked(shenhdh);
    	initData = initData + rd1s.getScript() + "\n";
    	
//    	收车日期设置
    	boolean sriq = true;
    	boolean eriq = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","收车日期设置",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("结束日期".equals(rsl.getString("zhi"))) {
    			sriq = false;
    			eriq = true;
    		}else
    			if("开始日期".equals(rsl.getString("zhi"))) {
    				sriq = true;
    		    	eriq = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"收车日期设置","开始日期","","数量",1,""));
    	}
    	
    	Radio rdscrqs = new Radio("scrq");
    	rdscrqs.setBoxLabel("开始日期");
    	rdscrqs.setId("rdscrqs");
    	rdscrqs.setChecked(sriq);
    	initData = initData + rdscrqs.getScript() + "\n";
    	
    	Radio rdscrqe = new Radio("scrq");
    	rdscrqe.setBoxLabel("结束日期");
    	rdscrqe.setId("rdscrqe");
    	rdscrqe.setChecked(eriq);
    	initData = initData + rdscrqe.getScript() + "\n";
    	
//    	对号设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","显示录入时间",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean xssj = false; 
    	if(rsl.next()) {
    		xssj = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"显示录入时间","否","","数量",1,""));
    	}
    	Checkbox cb2x = new Checkbox();
    	cb2x.setBoxLabel("显示录入时间");
    	cb2x.setId("cb2x");
    	cb2x.setChecked(xssj);
    	initData = initData + cb2x.getScript() + "\n";
    	
//    	火车采样设置
    	//1、供应商
    	rsl = con.getResultSetList(getXitxxSql("beiz","供应商","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3gyssy = true;
    	if(rsl.next()) {
    		cb3gyssy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"供应商","long,gongysb_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3gys = new Checkbox();
    	cb3gys.setBoxLabel("供应商");
    	cb3gys.setId("cb3gys");
    	cb3gys.setChecked(cb3gyssy);
    	initData = initData + cb3gys.getScript() + "\n";
    	
//    	2、煤矿
    	rsl = con.getResultSetList(getXitxxSql("beiz","煤矿","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3mksy = true;
    	if(rsl.next()) {
    		cb3mksy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(2,String.valueOf(visit.getDiancxxb_id()),"煤矿","long,meikxxb_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3mk = new Checkbox();
    	cb3mk.setBoxLabel("煤矿");
    	cb3mk.setId("cb3mk");
    	cb3mk.setChecked(cb3mksy);
    	initData = initData + cb3mk.getScript() + "\n";
    	
//    	3、品种
    	rsl = con.getResultSetList(getXitxxSql("beiz","品种","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3pzsy = true;
    	if(rsl.next()) {
    		cb3pzsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(3,String.valueOf(visit.getDiancxxb_id()),"品种","long,pinzb_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3pz = new Checkbox();
    	cb3pz.setBoxLabel("品种");
    	cb3pz.setId("cb3pz");
    	cb3pz.setChecked(cb3pzsy);
    	initData = initData + cb3pz.getScript() + "\n";
    	
//    	4、发站
    	rsl = con.getResultSetList(getXitxxSql("beiz","发站","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3fzsy = true;
    	if(rsl.next()) {
    		cb3fzsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(4,String.valueOf(visit.getDiancxxb_id()),"发站","long,faz_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3fz = new Checkbox();
    	cb3fz.setBoxLabel("发站");
    	cb3fz.setId("cb3fz");
    	cb3fz.setChecked(cb3fzsy);
    	initData = initData + cb3fz.getScript() + "\n";
    	
//    	5、口径
    	rsl = con.getResultSetList(getXitxxSql("beiz","口径","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3kjsy = true;
    	if(rsl.next()) {
    		cb3kjsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(5,String.valueOf(visit.getDiancxxb_id()),"口径","long,jihkjb_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3kj = new Checkbox();
    	cb3kj.setBoxLabel("口径");
    	cb3kj.setId("cb3kj");
    	cb3kj.setChecked(cb3kjsy);
    	initData = initData + cb3kj.getScript() + "\n";
  
//    	6、发货日期
    	rsl = con.getResultSetList(getXitxxSql("beiz","发货日期","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3fhrqsy = true;
    	if(rsl.next()) {
    		cb3fhrqsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(6,String.valueOf(visit.getDiancxxb_id()),"发货日期","date,fahrq","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3fhrq = new Checkbox();
    	cb3fhrq.setBoxLabel("发货日期");
    	cb3fhrq.setId("cb3fhrq");
    	cb3fhrq.setChecked(cb3fhrqsy);
    	initData = initData + cb3fhrq.getScript() + "\n";
    	
//    	7、采样日期
    	rsl = con.getResultSetList(getXitxxSql("beiz","采样日期","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3cyrqsy = true;
    	if(rsl.next()) {
    		cb3cyrqsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(7,String.valueOf(visit.getDiancxxb_id()),"采样日期","date,caiyrq","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3cyrq = new Checkbox();
    	cb3cyrq.setBoxLabel("采样日期");
    	cb3cyrq.setId("cb3cyrq");
    	cb3cyrq.setChecked(cb3cyrqsy);
    	initData = initData + cb3cyrq.getScript() + "\n";
    	
//    	8、车次
    	rsl = con.getResultSetList(getXitxxSql("beiz","车次","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3ccsy = true;
    	if(rsl.next()) {
    		cb3ccsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(8,String.valueOf(visit.getDiancxxb_id()),"车次","string,chec","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3cc = new Checkbox();
    	cb3cc.setBoxLabel("车次");
    	cb3cc.setId("cb3cc");
    	cb3cc.setChecked(cb3ccsy);
    	initData = initData + cb3cc.getScript() + "\n";
    	
//    	9、厂别
    	rsl = con.getResultSetList(getXitxxSql("beiz","厂别","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb3cbsy = true;
    	if(rsl.next()) {
    		cb3cbsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(9,String.valueOf(visit.getDiancxxb_id()),"厂别","long,diancxxb_id","火运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb3cb = new Checkbox();
    	cb3cb.setBoxLabel("厂别");
    	cb3cb.setId("cb3cb");
    	cb3cb.setChecked(cb3cbsy);
    	initData = initData + cb3cb.getScript() + "\n";
    	
//    	汽车采样设置
    	//1、供应商
    	rsl = con.getResultSetList(getXitxxSql("beiz","供应商","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4gyssy = true;
    	if(rsl.next()) {
    		cb4gyssy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"供应商","long,gongysb_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4gys = new Checkbox();
    	cb4gys.setBoxLabel("供应商");
    	cb4gys.setId("cb4gys");
    	cb4gys.setChecked(cb4gyssy);
    	initData = initData + cb4gys.getScript() + "\n";
    	
//    	2、煤矿
    	rsl = con.getResultSetList(getXitxxSql("beiz","煤矿","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4mksy = true;
    	if(rsl.next()) {
    		cb4mksy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(2,String.valueOf(visit.getDiancxxb_id()),"煤矿","long,meikxxb_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4mk = new Checkbox();
    	cb4mk.setBoxLabel("煤矿");
    	cb4mk.setId("cb4mk");
    	cb4mk.setChecked(cb4mksy);
    	initData = initData + cb4mk.getScript() + "\n";
    	
//    	3、品种
    	rsl = con.getResultSetList(getXitxxSql("beiz","品种","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4pzsy = true;
    	if(rsl.next()) {
    		cb4pzsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(3,String.valueOf(visit.getDiancxxb_id()),"品种","long,pinzb_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4pz = new Checkbox();
    	cb4pz.setBoxLabel("品种");
    	cb4pz.setId("cb4pz");
    	cb4pz.setChecked(cb4pzsy);
    	initData = initData + cb4pz.getScript() + "\n";
    	
//    	4、发站
    	rsl = con.getResultSetList(getXitxxSql("beiz","发站","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4fzsy = true;
    	if(rsl.next()) {
    		cb4fzsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(4,String.valueOf(visit.getDiancxxb_id()),"发站","long,faz_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4fz = new Checkbox();
    	cb4fz.setBoxLabel("发站");
    	cb4fz.setId("cb4fz");
    	cb4fz.setChecked(cb4fzsy);
    	initData = initData + cb4fz.getScript() + "\n";
    	
//    	5、口径
    	rsl = con.getResultSetList(getXitxxSql("beiz","口径","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4kjsy = true;
    	if(rsl.next()) {
    		cb4kjsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(5,String.valueOf(visit.getDiancxxb_id()),"口径","long,jihkjb_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4kj = new Checkbox();
    	cb4kj.setBoxLabel("口径");
    	cb4kj.setId("cb4kj");
    	cb4kj.setChecked(cb4kjsy);
    	initData = initData + cb4kj.getScript() + "\n";
  
//    	6、发货日期
    	rsl = con.getResultSetList(getXitxxSql("beiz","发货日期","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4fhrqsy = true;
    	if(rsl.next()) {
    		cb4fhrqsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(6,String.valueOf(visit.getDiancxxb_id()),"发货日期","date,fahrq","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4fhrq = new Checkbox();
    	cb4fhrq.setBoxLabel("发货日期");
    	cb4fhrq.setId("cb4fhrq");
    	cb4fhrq.setChecked(cb4fhrqsy);
    	initData = initData + cb4fhrq.getScript() + "\n";
    	
//    	7、采样日期
    	rsl = con.getResultSetList(getXitxxSql("beiz","采样日期","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4cyrqsy = true;
    	if(rsl.next()) {
    		cb4cyrqsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(7,String.valueOf(visit.getDiancxxb_id()),"采样日期","date,caiyrq","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4cyrq = new Checkbox();
    	cb4cyrq.setBoxLabel("采样日期");
    	cb4cyrq.setId("cb4cyrq");
    	cb4cyrq.setChecked(cb4cyrqsy);
    	initData = initData + cb4cyrq.getScript() + "\n";
    	
//    	8、车次
    	rsl = con.getResultSetList(getXitxxSql("beiz","车次","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4ccsy = true;
    	if(rsl.next()) {
    		cb4ccsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(8,String.valueOf(visit.getDiancxxb_id()),"车次","string,chec","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4cc = new Checkbox();
    	cb4cc.setBoxLabel("车次");
    	cb4cc.setId("cb4cc");
    	cb4cc.setChecked(cb4ccsy);
    	initData = initData + cb4cc.getScript() + "\n";
    	
//    	9、厂别
    	rsl = con.getResultSetList(getXitxxSql("beiz","厂别","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
    	boolean cb4cbsy = true;
    	if(rsl.next()) {
    		cb4cbsy = "使用".equals(rsl.getString("beiz"));
    	}else {
    		con.getInsert(getInsertSql(9,String.valueOf(visit.getDiancxxb_id()),"厂别","long,diancxxb_id","汽运采样编号","数量",1,"使用"));
    	}
    	Checkbox cb4cb = new Checkbox();
    	cb4cb.setBoxLabel("厂别");
    	cb4cb.setId("cb4cb");
    	cb4cb.setChecked(cb4cbsy);
    	initData = initData + cb4cb.getScript() + "\n";
    	
//    	运损计算方式
    	boolean dc = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","运损计算方法",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("单车".equals(rsl.getString("zhi"))) {
    			dc = true;
    		}else
    			dc = false;
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"运损计算方法","单车","","数量",1,"使用"));
    	}
    	Radio rd5fp = new Radio("yuns");
    	rd5fp.setBoxLabel("分批计算运损");
    	rd5fp.setId("rd5fp");
    	rd5fp.setChecked(!dc);
    	initData = initData + rd5fp.getScript() + "\n";
    	
    	Radio rd5dc = new Radio("yuns");
    	rd5dc.setBoxLabel("单车计算运损");
    	rd5dc.setId("rd5dc");
    	rd5dc.setChecked(dc);
    	initData = initData + rd5dc.getScript() + "\n";    	
//    	日计划生成采样 
    	boolean jh = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","日计划生成采样","汽运",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("是".equals(rsl.getString("zhi"))) {
    			jh = true;
    		}else
    			jh = false;
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"日计划生成采样","是","汽运","数量",1,"使用"));
    	}
    	Radio rd6jh = new Radio("cyscd");
    	rd6jh.setBoxLabel("计划录入");
    	rd6jh.setId("rd6jh");
    	rd6jh.setChecked(jh);
    	initData = initData + rd6jh.getScript() + "\n";
    	
    	Radio rd6dr = new Radio("cyscd");
    	rd6dr.setBoxLabel("数据导入");
    	rd6dr.setId("rd6dr");
    	rd6dr.setChecked(!jh);
    	initData = initData + rd6dr.getScript() + "\n";    	
    	
//    	火车补录数据是否自动生成采样编号
    	boolean huocblbh = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","补录自动生成编号","火车",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("是".equals(rsl.getString("zhi"))) {
    			huocblbh = true;
    		}else
    			if("否".equals(rsl.getString("zhi"))) {
    				huocblbh = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"补录自动生成编号","否","火车","数量",1,""));
    	}
    	Radio rd7ybh = new Radio("hcblbh");
    	rd7ybh.setBoxLabel("自动生成编号");
    	rd7ybh.setId("rdybh");
    	rd7ybh.setChecked(huocblbh);
    	initData = initData + rd7ybh.getScript() + "\n";
    	
    	Radio rd7nbh = new Radio("hcblbh");
    	rd7nbh.setBoxLabel("手动录入编号");
    	rd7nbh.setId("rdnbh");
    	rd7nbh.setChecked(!huocblbh);
    	initData = initData + rd7nbh.getScript() + "\n";
    	
//    	汽车补录数据是否自动生成采样编号
    	boolean qicblbh = true;
    	rsl = con.getResultSetList(getXitxxSql("zhi","补录自动生成编号","汽车",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()) {
    		if("是".equals(rsl.getString("zhi"))) {
    			qicblbh = true;
    		}else
    			if("否".equals(rsl.getString("zhi"))) {
    				qicblbh = false;
        		}
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"补录自动生成编号","是","汽车","数量",1,""));
    	}
    	Radio rd8qybh = new Radio("qcblbh");
    	rd8qybh.setBoxLabel("自动生成编号");
    	rd8qybh.setId("rdqybh");
    	rd8qybh.setChecked(qicblbh);
    	initData = initData + rd8qybh.getScript() + "\n";
    	
    	Radio rd8qnbh = new Radio("qcblbh");
    	rd8qnbh.setBoxLabel("手动录入编号");
    	rd8qnbh.setId("rdqnbh");
    	rd8qnbh.setChecked(!qicblbh);
    	initData = initData + rd8qnbh.getScript() + "\n";
    	
//    	火车衡数据录入车次是否是自动下拉选项
    	boolean huocczdjs = false;
    	rsl = con.getResultSetList(getXitxxSql("zhi","火车车次自动计算",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		if("是".equals(rsl.getString("zhi"))){
    			huocczdjs = true;
    		}else{
    			huocczdjs = false;
    		}
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"火车车次自动计算","否","","数量",1,""));
    	}
    	rsl.close();
    	Radio rd9hycczd = new Radio("hycc");
    	rd9hycczd.setBoxLabel("车次下拉选择");
    	rd9hycczd.setId("hycczd");
    	rd9hycczd.setChecked(huocczdjs);
    	initData = initData + rd9hycczd.getScript() + "\n";
    	
    	Radio rd9hyccsd = new Radio("hycc");
    	rd9hyccsd.setBoxLabel("车次手动填写");
    	rd9hyccsd.setId("hyccsd");
    	rd9hyccsd.setChecked(!huocczdjs);
    	initData = initData + rd9hyccsd.getScript() + "\n";
//    	采样按照数量分组字段
    	String danw = "maoz-piz";
    	rsl = con.getResultSetList(getXitxxSql("danw","采样按数量分组",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		danw = rsl.getString("danw");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"采样按数量分组","0","jingz","数量",1,"使用"));
    	}
    	TextField tx = new TextField();
    	tx.setFieldLabel("采样分组字段");
    	tx.setId("caiyfzzd");
    	tx.setValue(danw);
    	tx.setWidth(90);
    	initData = initData + tx.getScript() + "\n";
//    	采样按照数量分组数值
    	double fzz = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","采样按数量分组",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		fzz = rsl.getDouble("zhi");
    	}
    	NumberField nx = new NumberField();
    	nx.setFieldLabel("采样分组值");
    	nx.setId("caiyfzz");
    	nx.setValue(String.valueOf(fzz));
    	nx.setWidth(90);
    	initData = initData + nx.getScript() + "\n";
//    	系统默认运损率
//    	火车
    	double tlysl = 0.012;
    	rsl = con.getResultSetList(getXitxxSql("zhi","默认运损率","火车",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		tlysl = rsl.getDouble("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"默认运损率","0.012","火车","数量",1,"使用"));
    	}
    	NumberField hcysl = new NumberField();
    	hcysl.setFieldLabel("火车默认运损率");
    	hcysl.setId("hcmrysl");
    	hcysl.setDecimalPrecision(3);
    	hcysl.setValue(String.valueOf(tlysl));
    	hcysl.setWidth(90);
    	initData = initData + hcysl.getScript() + "\n";
//    	汽车
    	double glysl = 0.01;
    	rsl = con.getResultSetList(getXitxxSql("zhi","默认运损率","汽车",String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		glysl = rsl.getDouble("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"默认运损率","0.01","汽车","数量",1,"使用"));
    	}
    	NumberField qcysl = new NumberField();
    	qcysl.setFieldLabel("汽车默认运损率");
    	qcysl.setId("qcmrysl");
    	qcysl.setDecimalPrecision(3);
    	qcysl.setValue(String.valueOf(glysl));
    	qcysl.setWidth(90);
    	initData = initData + qcysl.getScript() + "\n";
//   	审核设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","审核不可修改数据",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6chsy = false;
    	if(rsl.next()) {
    		cb6chsy = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"审核不可修改数据","否","","数量",1,"使用"));
    	}
    	Checkbox cb6ch = new Checkbox();
    	cb6ch.setBoxLabel("审核不可修改数据");
    	cb6ch.setId("cb6ch");
    	cb6ch.setChecked(cb6chsy);
    	initData = initData + cb6ch.getScript() + "\n";
//    	火车毛重修改设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","火车毛重可修改",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean hcmzkg = false;
    	if(rsl.next()) {
    		hcmzkg = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"火车毛重可修改","否","","数量",1,"使用"));
    	}
    	Checkbox cbhcmzkg = new Checkbox();
    	cbhcmzkg.setBoxLabel("火车毛重可修改");
    	cbhcmzkg.setId("hcmzkg");
    	cbhcmzkg.setChecked(hcmzkg);
    	initData = initData + cbhcmzkg.getScript() + "\n";
//    	汽车毛重修改设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","汽车毛重可修改",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean qcmzkg = false;
    	if(rsl.next()) {
    		qcmzkg = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"汽车毛重可修改","否","","数量",1,"使用"));
    	}
    	Checkbox cbqcmzkg = new Checkbox();
    	cbqcmzkg.setBoxLabel("汽车毛重可修改");
    	cbqcmzkg.setId("qcmzkg");
    	cbqcmzkg.setChecked(qcmzkg);
    	initData = initData + cbqcmzkg.getScript() + "\n";
//    	火车皮重修改设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","火车不可修改皮重",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6hcsy = false;
    	if(rsl.next()) {
    		cb6hcsy = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"火车不可修改皮重","否","","数量",1,"使用"));
    	}
    	Checkbox cb6hc = new Checkbox();
    	cb6hc.setBoxLabel("火车不可修改皮重");
    	cb6hc.setId("cb6hc");
    	cb6hc.setChecked(cb6hcsy);
    	initData = initData + cb6hc.getScript() + "\n";
//    	汽车不可修改皮重
    	rsl = con.getResultSetList(getXitxxSql("zhi","汽车可修改皮重",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cb6qcsy = false;
    	if(rsl.next()) {
    		cb6qcsy = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"汽车可修改皮重","否","","数量",1,"使用"));
    	}
    	Checkbox cb6qc = new Checkbox();
    	cb6qc.setBoxLabel("汽车可修改皮重");
    	cb6qc.setId("cb6qc");
    	cb6qc.setChecked(cb6qcsy);
    	initData = initData + cb6qc.getScript() + "\n";
//    	车次为采样编码
    	rsl = con.getResultSetList(getXitxxSql("zhi","车次为采样编号",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean caiysz1sy = false;
    	if(rsl.next()) {
    		caiysz1sy = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"车次为采样编号","否","","数量",1,"使用"));
    	}
    	Checkbox caiysz1 = new Checkbox();
    	caiysz1.setBoxLabel("车次为采样编号");
    	caiysz1.setId("caiysz1");
    	caiysz1.setChecked(caiysz1sy);
    	initData = initData + caiysz1.getScript() + "\n";
//    	进厂批次号为采样编码
    	rsl = con.getResultSetList(getXitxxSql("zhi","进厂批次号为采样编号",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean jincphcy = false;
    	if(rsl.next()) {
    		jincphcy = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"进厂批次号为采样编号","否","","数量",1,"使用"));
    	}
    	Checkbox caiysz2 = new Checkbox();
    	caiysz2.setBoxLabel("进厂批次号为采样编号");
    	caiysz2.setId("caiysz2");
    	caiysz2.setChecked(jincphcy);
    	initData = initData + caiysz2.getScript() + "\n";
    	
//    	数据修改默认多行替换
    	rsl = con.getResultSetList(getXitxxSql("zhi","数据修改默认多行替换",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean th = false; 
    	if(rsl.next()) {
    		th = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"数据修改默认多行替换","否","","数量",1,"使用"));
    	}
    	Checkbox cb3x = new Checkbox();
    	cb3x.setBoxLabel("数据修改默认多行替换");
    	cb3x.setId("cb3x");
    	cb3x.setChecked(th);
    	initData = initData + cb3x.getScript() + "\n";
    	
//    	汽车检斤单模式
    	String mos = "";
    	rsl = con.getResultSetList(getXitxxSql("zhi","汽车衡检斤单模式",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mos = rsl.getString("zhi");
    	}else{
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"汽车衡检斤单模式","PRINT_GUIGUAN",null,"数量",1,"使用"));
    	}
    	TextField t1x = new TextField();
    	t1x.setFieldLabel("汽车衡检斤单模式");
    	t1x.setId("qicjjdms");
    	t1x.setValue(mos);
    	t1x.setWidth(120);
    	initData = initData + t1x.getScript() + "\n";
    	
//    	补录毛重自动计算
    	rsl = con.getResultSetList(getXitxxSql("zhi","补录毛重自动计算",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean mzjs = false; 
    	if(rsl.next()) {
    		mzjs = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"补录毛重自动计算","否","铁路","数量",1,"使用"));
    	}
    	Checkbox cb4x = new Checkbox();
    	cb4x.setBoxLabel("补录毛重自动计算");
    	cb4x.setId("cb4x");
    	cb4x.setChecked(mzjs);
    	initData = initData + cb4x.getScript() + "\n";
    	
//    	补录默认皮重
    	double mfpz = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","补录默认皮重",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mfpz = rsl.getDouble("zhi");
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"补录默认皮重","0","铁路","数量",1,"使用"));
    	}
    	NumberField nx1 = new NumberField();
    	nx1.setFieldLabel("补录默认皮重");
    	nx1.setId("mfpz");
    	nx1.setValue(String.valueOf(mfpz));
    	nx1.setWidth(90);
    	initData = initData + nx1.getScript() + "\n";
    	
//    	轻车检斤是否计算成本
    	rsl = con.getResultSetList(getXitxxSql("zhi","轻车检斤是否计算成本",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean jscb = false; 
    	if(rsl.next()) {
    		jscb = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"轻车检斤是否计算成本","否","","数量",1,"使用"));
    	}
    	Checkbox cb5x = new Checkbox();
    	cb5x.setBoxLabel("轻车检斤是否计算成本");
    	cb5x.setId("cb5x");
    	cb5x.setChecked(jscb);
    	initData = initData + cb5x.getScript() + "\n";
    	
//    	数据导入默认日期
    	double mrrq = 0.0;
    	rsl = con.getResultSetList(getXitxxSql("zhi","数据导入默认日期",null,String.valueOf(visit.getDiancxxb_id())));
    	if(rsl.next()){
    		mrrq = rsl.getDouble("zhi");
    	}else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"数据导入默认日期","0","","数量",1,"使用"));
    	}
    	NumberField nx2 = new NumberField();
    	nx2.setFieldLabel("数据导入默认日期");
    	nx2.setId("mrrq");
    	nx2.setValue(String.valueOf(mrrq));
    	nx2.setWidth(90);
    	initData = initData + nx2.getScript() + "\n";
    	
//    	是否自动生成采样编号
    	rsl = con.getResultSetList(getXitxxSql("zhi","是否自动生成采样",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean cybh = true;
    	if(rsl.next()) {
    		cybh = "是".equals(rsl.getString("zhi"));
    	}else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "是否自动生成采样", "是", "", "数量", 1, "使用"));
    	}
    	Checkbox cbcybh = new Checkbox();
    	cbcybh.setBoxLabel("自动生成采样");
    	cbcybh.setId("cbcybh");
    	cbcybh.setChecked(cybh);
    	initData = initData + cbcybh.getScript() + "\n";
    	
//    	是否显示扣水扣杂
    	rsl = con.getResultSetList(getXitxxSql("zhi","扣水扣杂是否显示",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean kskz = false;
    	if(rsl.next()) {
    		kskz = "是".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "扣水扣杂是否显示", "否", "", "数量", 1, "使用"));
    	}
    	Checkbox cbkskz = new Checkbox();
    	cbkskz.setBoxLabel("显示扣水扣杂");
    	cbkskz.setId("cbkskz");
    	cbkskz.setChecked(kskz);
    	initData = initData + cbkskz.getScript() + "\n";
    	
//    	收耗存日报休约设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","收耗存日报来煤量休约",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean shcxy = false;
    	if(rsl.next()) {
    		shcxy = "是".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1, String.valueOf(visit.getDiancxxb_id()), "收耗存日报来煤量休约", "否", "", "数量", 1, ""));
    	}
    	Checkbox cbshcxy = new Checkbox();
    	cbshcxy.setBoxLabel("休约收耗存日报来煤量");
    	cbshcxy.setId("cbshcxy");
    	cbshcxy.setChecked(shcxy);
    	initData = initData + cbshcxy.getScript() + "\n";
    	
//    	按质量分列设置
    	rsl = con.getResultSetList(getXitxxSql("zhi","按质量分列",null,String.valueOf(visit.getDiancxxb_id())));
    	boolean azlfl = false;
    	if(rsl.next()) {
    		azlfl = "是".equals(rsl.getString("zhi"));
    	} else {
    		con.getInsert(getInsertSql(1,String.valueOf(visit.getDiancxxb_id()),"按质量分列","否",null, "数量", 1, "使用"));
    	}
    	Checkbox cbazlfl = new Checkbox();
    	cbazlfl.setBoxLabel("按质量分列");
    	cbazlfl.setId("cbazlfl");
    	cbazlfl.setChecked(azlfl);
    	initData = initData + cbazlfl.getScript() + "\n";
    	
    	return initData;
    }
    
    
//	按钮事件处理
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    }
//	保存分组的改动
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		String change[] = getChange().split(";");
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	for(int i=0 ; i<change.length ; i++) {
    		String ch[] = change[i].split(",");
    		String mingc = ch[0];
    		String zhi = ch[1];
    		if("rd1g".equals(mingc)) {
    			String strzhi = "过衡";
    			if("true".equals(zhi)) {
    				strzhi = "过衡";
    			}else {
    				strzhi = "审核";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"到货确认设置",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
    		}else if("rdscrqs".equals(mingc)) {
    			String strzhi = "开始日期";
    			if("true".equals(zhi)) {
    				strzhi = "开始日期";
    			}else {
    				strzhi = "结束日期";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"收车日期设置",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
    		}else if("cb2x".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"显示录入时间",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
    		}else if("cb3gys".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"供应商","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3mk".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"煤矿","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3pz".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"品种","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3fz".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"发站","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3kj".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"口径","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3fhrq".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"发货日期","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cyrq".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"采样日期","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cc".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"车次","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3cb".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"厂别","火运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4gys".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"供应商","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4mk".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"煤矿","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4pz".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"品种","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4fz".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"发站","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4kj".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"口径","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4fhrq".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"发货日期","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cyrq".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"采样日期","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cc".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"车次","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4cb".equals(mingc)) {
				String strzhi = "使用";
				if("true".equals(zhi)) {
					strzhi = "使用";
				}else {
					strzhi = "不使用";
				}
				con.getUpdate(UpdateXitxxSql("beiz",strzhi,"厂别","汽运采样编号",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rd5dc".equals(mingc)) {
				String strzhi = "单车";
				if("true".equals(zhi)) {
					strzhi = "单车";
				}else {
					strzhi = "分批";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"运损计算方法",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rd6jh".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"日计划生成采样","汽运",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiyfzzd".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("danw",zhi,"采样按数量分组",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiyfzz".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"采样按数量分组",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rdybh".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"补录自动生成编号","火车",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("rdqybh".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"补录自动生成编号","汽车",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("hycczd".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"火车车次自动计算",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6ch".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"审核不可修改数据",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6hc".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"火车不可修改皮重",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("hcmzkg".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"火车毛重可修改",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("qcmzkg".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"汽车毛重可修改",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("cb6qc".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"汽车可修改皮重",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
			else if("caiysz1".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"车次为采样编号",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("caiysz2".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"进厂批次号为采样编号",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("hcmrysl".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"默认运损率","火车",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("qcmrysl".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"默认运损率","汽车",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb3x".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"数据修改默认多行替换",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("qicjjdms".equals(mingc)){
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"汽车衡检斤单模式",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb4x".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"补录毛重自动计算","铁路",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("mfpz".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"补录默认皮重","铁路",String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cb5x".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				}else {
					strzhi = "否";
				}
				//String zd,String zhi,String mingc, String danw, String diancxxb_id
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"轻车检斤是否计算成本",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("mrrq".equals(mingc)){
				con.getUpdate(UpdateXitxxSql("zhi",zhi,"数据导入默认日期",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbcybh".equals(mingc)) {
    			String strzhi = "是";
    			if("true".equals(zhi)) {
    				strzhi = "是";
    			}else {
    				strzhi = "否";
    			}
    			con.getUpdate(UpdateXitxxSql("zhi",strzhi,"是否自动生成采样",null,String.valueOf(visit.getDiancxxb_id())));
    			continue;
			}else if("cbkskz".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				} else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"扣水扣杂是否显示",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbshcxy".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				} else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"收耗存日报来煤量休约",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}else if("cbazlfl".equals(mingc)) {
				String strzhi = "是";
				if("true".equals(zhi)) {
					strzhi = "是";
				} else {
					strzhi = "否";
				}
				con.getUpdate(UpdateXitxxSql("zhi",strzhi,"按质量分列",null,String.valueOf(visit.getDiancxxb_id())));
				continue;
			}
    	}
    	con.Close();
	}
}
