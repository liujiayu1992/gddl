package com.zhiren.jt.jihgl.nianxqjhh;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class XiancjzBill extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	 年份_开始
	public boolean nianfchanged;
	
	private IDropDownBean _NianfValue;
	
	private static IPropertySelectionModel _NianfModel;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
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
//	 年份_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		Report rt = new Report();
		StringBuffer sbsql = new StringBuffer();
		
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (yx.zhuangt=1 or yx.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and yx.zhuangt=2";
			}
		}
		
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		String jizzt= "and yx.jizzt=0";
		
		String child_dcid = "select id from diancxxb where fuid = " + getTreeid();
		
//		在一厂多制情况下，通过电厂树id判断页面上查询的是总电厂，还是子电厂
		if (Shujpd(con, child_dcid) != 0) { // 页面上查询的是总电厂
			
//			判断总电厂是否有值
			String zhongc = 
				"select (sum(yx.fadl) + sum(yx.gongdbzmh) + sum(yx.zonghcydl) +\n" +
				"       sum(yx.fadbhm) + sum(yx.gongrl) + sum(yx.gongrbzmh) +\n" + 
				"       sum(yx.dianhzryl) + sum(yx.youfrl) + sum(yx.rulrz) +\n" + 
				"       sum(yx.qity) + sum(yx.yuns) + sum(yx.qickc) +\n" + 
				"       sum(yx.qimkc) + sum(yx.ZHUANGJRL)) as total_value\n" + 
				"  from nianxqjhh yx\n" + 
				" where yx.diancxxb_id = "+ getTreeid() +"\n" + 
				"   and yx.nianf = "+ nextyear +"\n" + 
				"   and (yx.shujzt = "+ lastyear +" or yx.shujzt = "+ intyear +" or yx.shujzt = "+ nextyear +")";

			if (hasValue(con, zhongc, "total_value")) { // 总电厂有数据
				sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
				sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'供电标准煤耗',3,'综合厂用电率',4,'发电标准煤耗',5,'供热量',6,'供热标准煤耗', \n");
				sbsql.append("7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'点火助燃油量',11,'油发热量',  \n");
				sbsql.append("12,'需用油折标煤量',13,'入炉热值',14,'发电需原煤量',15,'供热需用标煤量',16,'需用原煤量',17,'其他用',18,'运损',19,'期初库存',\n");
				sbsql.append("20,'期未库存',21,'总需求量',22,'装机容量',23,'装机容量描述',24,'投产日期',25,'设计煤种',26,'备注',mingc),mingc) as mingc,zz.danw,\n");
				//sbsql.append("to_char(sum(nvl(lastyear,0))) as lastyear,to_char(sum(nvl(intyear,0))) as intyear,to_char(sum(nvl(nextyear,0))) as nextyear, \n");
				sbsql.append("to_char(max(nvl(lastyear,0))) as lastyear,to_char(max(nvl(intyear,0))) as intyear,to_char(max(nvl(nextyear,0))) as nextyear \n");
//				sbsql.append("decode("+visit.getRenyjb()+",3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
				sbsql.append("from (  \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'万千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 1 as lanc,'发电量' as mingc,'万千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"  ) z1, \n");
				sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 2 as lanc,'供电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 4 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 5 as lanc,'供热量' as mingc,'吉焦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'千克/吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 6 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 7 as lanc,'发电需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 8 as lanc,'供热需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 9 as lanc,'需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 10 as lanc,'点火助燃油量' as mingc,'吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 11 as lanc,'油发热量' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 12 as lanc,'需用油折标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 13 as lanc,'入炉热值' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 14 as lanc,'发电需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 15 as lanc,'供热需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 16 as lanc,'需用原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 17 as lanc,'其他用' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 18 as lanc,'运损' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 19 as lanc,'期初库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 20 as lanc,'期未库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 21 as lanc,'总需求量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
				sbsql.append("(select 22 as lanc,'装机容量' as mingc,'万千瓦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append(") zz \n");
				sbsql.append("group by (lanc,mingc,zz.danw,zz.zhuangt) \n");
				sbsql.append("order by lanc asc \n");
			} else { // 总电厂没数据，取子电厂的合计数据
				sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
				sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'供电标准煤耗',3,'综合厂用电率',4,'发电标准煤耗',5,'供热量',6,'供热标准煤耗', \n");
				sbsql.append("7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'点火助燃油量',11,'油发热量',  \n");
				sbsql.append("12,'需用油折标煤量',13,'入炉热值',14,'发电需原煤量',15,'供热需用标煤量',16,'需用原煤量',17,'其他用',18,'运损',19,'期初库存',\n");
				sbsql.append("20,'期未库存',21,'总需求量',22,'装机容量',23,'装机容量描述',24,'投产日期',25,'设计煤种',26,'备注',mingc),mingc) as mingc,zz.danw,\n");
				//sbsql.append("to_char(sum(nvl(lastyear,0))) as lastyear,to_char(sum(nvl(intyear,0))) as intyear,to_char(sum(nvl(nextyear,0))) as nextyear, \n");
				sbsql.append("to_char(max(nvl(lastyear,0))) as lastyear,to_char(max(nvl(intyear,0))) as intyear,to_char(max(nvl(nextyear,0))) as nextyear \n");
//				sbsql.append("decode("+visit.getRenyjb()+",3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
				sbsql.append("from (  \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'万千瓦时' as danw from \n");
				sbsql.append("(select 1 as lanc,'发电量' as mingc,'万千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 1 as lanc,decode(to_char(sum(yx.fadl),'FM99999990.00'),0,'0',to_char(sum(yx.fadl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"  ) z1, \n");
				sbsql.append("(select 1 as lanc,decode(to_char(sum(yx.fadl),'FM99999990.00'),0,'0',to_char(sum(yx.fadl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 1 as lanc,decode(to_char(sum(yx.fadl),'FM99999990.00'),0,'0',to_char(sum(yx.fadl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw from \n");
				sbsql.append("(select 2 as lanc,'供电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 2 as lanc,decode(to_char(sum(yx.gongdbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongdbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 2 as lanc,decode(to_char(sum(yx.gongdbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongdbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 2 as lanc,decode(to_char(sum(yx.gongdbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongdbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw from \n");
				sbsql.append("(select 3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 3 as lanc,decode(to_char(sum(yx.zonghcydl),'FM99999990.00'),0,'0',to_char(sum(yx.zonghcydl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 3 as lanc,decode(to_char(sum(yx.zonghcydl),'FM99999990.00'),0,'0',to_char(sum(yx.zonghcydl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 3 as lanc,decode(to_char(sum(yx.zonghcydl),'FM99999990.00'),0,'0',to_char(sum(yx.zonghcydl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw from \n");
				sbsql.append("(select 4 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 4 as lanc,decode(to_char(sum(yx.fadbhm),'FM99999990.00'),0,'0',to_char(sum(yx.fadbhm),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 4 as lanc,decode(to_char(sum(yx.fadbhm),'FM99999990.00'),0,'0',to_char(sum(yx.fadbhm),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 4 as lanc,decode(to_char(sum(yx.fadbhm),'FM99999990.00'),0,'0',to_char(sum(yx.fadbhm),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'吉焦' as danw from \n");
				sbsql.append("(select 5 as lanc,'供热量' as mingc,'吉焦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 5 as lanc,decode(to_char(sum(yx.gongrl),'FM99999990.00'),0,'0',to_char(sum(yx.gongrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 5 as lanc,decode(to_char(sum(yx.gongrl),'FM99999990.00'),0,'0',to_char(sum(yx.gongrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 5 as lanc,decode(to_char(sum(yx.gongrl),'FM99999990.00'),0,'0',to_char(sum(yx.gongrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'千克/吉焦' as danw from \n");
				sbsql.append("(select 6 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 6 as lanc,decode(to_char(sum(yx.gongrbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongrbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 6 as lanc,decode(to_char(sum(yx.gongrbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongrbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 6 as lanc,decode(to_char(sum(yx.gongrbzmh),'FM99999990.00'),0,'0',to_char(sum(yx.gongrbzmh),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 7 as lanc,'发电需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 7 as lanc,decode(to_char(sum(yx.fadxybml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 7 as lanc,decode(to_char(sum(yx.fadxybml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 7 as lanc,decode(to_char(sum(yx.fadxybml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 8 as lanc,'供热需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 8 as lanc,decode(to_char(sum(yx.gongrxybml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 8 as lanc,decode(to_char(sum(yx.gongrxybml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 8 as lanc,decode(to_char(sum(yx.gongrxybml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 9 as lanc,'需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 9 as lanc,decode(to_char(sum(yx.xuybml),'FM99999990.00'),0,'0',to_char(sum(yx.xuybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 9 as lanc,decode(to_char(sum(yx.xuybml),'FM99999990.00'),0,'0',to_char(sum(yx.xuybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 9 as lanc,decode(to_char(sum(yx.xuybml),'FM99999990.00'),0,'0',to_char(sum(yx.xuybml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 10 as lanc,'点火助燃油量' as mingc,'吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 10 as lanc,decode(to_char(sum(yx.dianhzryl),'FM99999990.00'),0,'0',to_char(sum(yx.dianhzryl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 10 as lanc,decode(to_char(sum(yx.dianhzryl),'FM99999990.00'),0,'0',to_char(sum(yx.dianhzryl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 10 as lanc,decode(to_char(sum(yx.dianhzryl),'FM99999990.00'),0,'0',to_char(sum(yx.dianhzryl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw from \n");
				sbsql.append("(select 11 as lanc,'油发热量' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 11 as lanc,decode(to_char(sum(yx.youfrl),'FM99999990.00'),0,'0',to_char(sum(yx.youfrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 11 as lanc,decode(to_char(sum(yx.youfrl),'FM99999990.00'),0,'0',to_char(sum(yx.youfrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 11 as lanc,decode(to_char(sum(yx.youfrl),'FM99999990.00'),0,'0',to_char(sum(yx.youfrl),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw from \n");
				sbsql.append("(select 12 as lanc,'需用油折标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 12 as lanc,decode(to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00'),0,'0',to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 12 as lanc,decode(to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00'),0,'0',to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 12 as lanc,decode(to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00'),0,'0',to_char(sum((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271)),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw from \n");
				sbsql.append("(select 13 as lanc,'入炉热值' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 13 as lanc,decode(to_char(sum(yx.rulrz),'FM99999990.00'),0,'0',to_char(sum(yx.rulrz),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 13 as lanc,decode(to_char(sum(yx.rulrz),'FM99999990.00'),0,'0',to_char(sum(yx.rulrz),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 13 as lanc,decode(to_char(sum(yx.rulrz),'FM99999990.00'),0,'0',to_char(sum(yx.rulrz),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 14 as lanc,'发电需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 14 as lanc,decode(to_char(sum(yx.fadxyml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 14 as lanc,decode(to_char(sum(yx.fadxyml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 14 as lanc,decode(to_char(sum(yx.fadxyml),'FM99999990.00'),0,'0',to_char(sum(yx.fadxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 15 as lanc,'供热需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 15 as lanc,decode(to_char(sum(yx.gongrxyml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 15 as lanc,decode(to_char(sum(yx.gongrxyml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 15 as lanc,decode(to_char(sum(yx.gongrxyml),'FM99999990.00'),0,'0',to_char(sum(yx.gongrxyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 16 as lanc,'需用原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 16 as lanc,decode(to_char(sum(yx.xuyyml),'FM99999990.00'),0,'0',to_char(sum(yx.xuyyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 16 as lanc,decode(to_char(sum(yx.xuyyml),'FM99999990.00'),0,'0',to_char(sum(yx.xuyyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 16 as lanc,decode(to_char(sum(yx.xuyyml),'FM99999990.00'),0,'0',to_char(sum(yx.xuyyml),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 17 as lanc,'其他用' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 17 as lanc,decode(to_char(sum(yx.qity),'FM99999990.00'),0,'0',to_char(sum(yx.qity),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 17 as lanc,decode(to_char(sum(yx.qity),'FM99999990.00'),0,'0',to_char(sum(yx.qity),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 17 as lanc,decode(to_char(sum(yx.qity),'FM99999990.00'),0,'0',to_char(sum(yx.qity),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 18 as lanc,'运损' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 18 as lanc,decode(to_char(sum(yx.yuns),'FM99999990.00'),0,'0',to_char(sum(yx.yuns),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 18 as lanc,decode(to_char(sum(yx.yuns),'FM99999990.00'),0,'0',to_char(sum(yx.yuns),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 18 as lanc,decode(to_char(sum(yx.yuns),'FM99999990.00'),0,'0',to_char(sum(yx.yuns),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 19 as lanc,'期初库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 19 as lanc,decode(to_char(sum(yx.qickc),'FM99999990.00'),0,'0',to_char(sum(yx.qickc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 19 as lanc,decode(to_char(sum(yx.qickc),'FM99999990.00'),0,'0',to_char(sum(yx.qickc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 19 as lanc,decode(to_char(sum(yx.qickc),'FM99999990.00'),0,'0',to_char(sum(yx.qickc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 20 as lanc,'期未库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 20 as lanc,decode(to_char(sum(yx.qimkc),'FM99999990.00'),0,'0',to_char(sum(yx.qimkc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 20 as lanc,decode(to_char(sum(yx.qimkc),'FM99999990.00'),0,'0',to_char(sum(yx.qimkc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 20 as lanc,decode(to_char(sum(yx.qimkc),'FM99999990.00'),0,'0',to_char(sum(yx.qimkc),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 21 as lanc,'总需求量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 21 as lanc,decode(to_char(sum(yx.zongxql),'FM99999990.00'),0,'0',to_char(sum(yx.zongxql),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 21 as lanc,decode(to_char(sum(yx.zongxql),'FM99999990.00'),0,'0',to_char(sum(yx.zongxql),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 21 as lanc,decode(to_char(sum(yx.zongxql),'FM99999990.00'),0,'0',to_char(sum(yx.zongxql),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append("union \n");
				sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw from \n");
				sbsql.append("(select 22 as lanc,'装机容量' as mingc,'万千瓦' dun,0 as zhuangt from dual) t,\n");
				sbsql.append("(select 22 as lanc,decode(to_char(sum(yx.ZHUANGJRL),'FM99999990.00'),0,'0',to_char(sum(yx.ZHUANGJRL),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
				sbsql.append("(select 22 as lanc,decode(to_char(sum(yx.ZHUANGJRL),'FM99999990.00'),0,'0',to_char(sum(yx.ZHUANGJRL),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
				sbsql.append("(select 22 as lanc,decode(to_char(sum(yx.ZHUANGJRL),'FM99999990.00'),0,'0',to_char(sum(yx.ZHUANGJRL),'FM99999990.00')) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id in ("+child_dcid+") and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
				sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
				sbsql.append(") zz \n");
				sbsql.append("group by (lanc,mingc,zz.danw) \n");
				sbsql.append("order by lanc asc \n");
			}
		} else { // 页面上查询的是子电厂
			sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
			sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'供电标准煤耗',3,'综合厂用电率',4,'发电标准煤耗',5,'供热量',6,'供热标准煤耗', \n");
			sbsql.append("7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'点火助燃油量',11,'油发热量',  \n");
			sbsql.append("12,'需用油折标煤量',13,'入炉热值',14,'发电需原煤量',15,'供热需用标煤量',16,'需用原煤量',17,'其他用',18,'运损',19,'期初库存',\n");
			sbsql.append("20,'期未库存',21,'总需求量',22,'装机容量',23,'装机容量描述',24,'投产日期',25,'设计煤种',26,'备注',mingc),mingc) as mingc,zz.danw,\n");
			//sbsql.append("to_char(sum(nvl(lastyear,0))) as lastyear,to_char(sum(nvl(intyear,0))) as intyear,to_char(sum(nvl(nextyear,0))) as nextyear, \n");
			sbsql.append("to_char(max(nvl(lastyear,0))) as lastyear,to_char(max(nvl(intyear,0))) as intyear,to_char(max(nvl(nextyear,0))) as nextyear \n");
//			sbsql.append("decode("+visit.getRenyjb()+",3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
			sbsql.append("from (  \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'万千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 1 as lanc,'发电量' as mingc,'万千瓦时' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"  ) z1, \n");
			sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 2 as lanc,'供电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 4 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 5 as lanc,'供热量' as mingc,'吉焦' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'千克/吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 6 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 7 as lanc,'发电需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 8 as lanc,'供热需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 9 as lanc,'需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 10 as lanc,'点火助燃油量' as mingc,'吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 11 as lanc,'油发热量' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 12 as lanc,'需用油折标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 13 as lanc,'入炉热值' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 14 as lanc,'发电需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 15 as lanc,'供热需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 16 as lanc,'需用原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 17 as lanc,'其他用' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 18 as lanc,'运损' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 19 as lanc,'期初库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 20 as lanc,'期未库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 21 as lanc,'总需求量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
			sbsql.append("(select 22 as lanc,'装机容量' as mingc,'万千瓦' dun,0 as zhuangt from dual) t,\n");
			sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
			sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
			sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append(") zz \n");
			sbsql.append("group by (lanc,mingc,zz.danw,zz.zhuangt) \n");
			sbsql.append("order by lanc asc \n");
		}
		
		String[][] ArrHeader = new String[1][6];
		ArrHeader[0] = new String[]{"栏次", "项目", "单位", lastyear + "年完成", intyear + "年已完成", nextyear + "年预计"};
		
		int[] ArrWidth = new int[] {40, 200, 70, 90, 90, 90};
		
		ResultSetList rslData = con.getResultSetList(sbsql.toString());
		rt.setTitle("年需用计划查询", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.setDefaultTitle(1, 3, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, "查询年份："+getNianfValue()+" 年", Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "制表：", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		
		tbr.addText(new ToolbarText("年份："));
		ComboBox cbx = new ComboBox();
		cbx.setTransform("NIANF");
		cbx.setWidth(60);
		tbr.addField(cbx);
		tbr.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
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
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	/**
	 * 判断数据在数据库中是否存在，存在返回行数，不存在返回0
	 * @param con
	 * @param sql
	 * @return
	 */
	public int Shujpd(JDBCcon con,String sql){
		return JDBCcon.getRow(con.getResultSet(sql));
	}
	
	/**
	 * 判断总电厂是否有数据，有数据返回ture，没数据返回false
	 * @param con
	 * @param sql
	 * @param colName 字段名
	 * @return
	 */
	public boolean hasValue(JDBCcon con, String sql, String colName) {
		
		boolean youz = false;
		
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString(colName).equals("0")) {
				youz = false;
			} else {
				youz = true;
			}
		}
		return youz;
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			this.setTreeid(null);
			setNianfValue(null);
		}
		getSelectData();
	}
}