package com.zhiren.jt.jihgl.nianxqjhh;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 电厂级用户使用
 * @author xzy
 *
 */
public class XiancjzReport extends BasePage {
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
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getZhiltj();
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq="";//FormatDate(DateUtil.getDate(getBeginriqDate()));
//		int jib=this.getDiancTreeJib();
//		String strGongsID = "";
//		String danwmc="";//汇总名称
//		if(jib==1){//选集团时刷新出所有的电厂
//			strGongsID=" ";
//			
//		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
//			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+")";
//			
//		}else if (jib==3){//选电厂只刷新出该电厂
//			strGongsID=" and dc.id= " +this.getTreeid();
//			 
//		}else if (jib==-1){
//			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
//		}
		
		
		//年，月
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		// 审核状态
		String shenhzt="";
		if(getShenhDropDownValue()!=null){
			shenhzt=" and yx.zhuangt="+getShenhDropDownValue().getId();
		}
		 String ArrHeader[][]=null;
		 int ArrWidth[]=null;
		 
		 sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
			sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'发电标准煤耗',3,'综合厂用电率',4,'供热量',5,'供热标准煤耗', \n");
			sbsql.append("6,'入炉热值',7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'发电需原煤量',11,'供热需原煤量',  \n");
			sbsql.append("12,'需用原煤量',13,'点火助燃油量',14,'计划发电',15,'供热标煤量',16,'标煤耗',mingc),mingc) as mingc,zz.danw,  \n");
			sbsql.append("sum(lastyear) as lastyear,sum(year) as year,sum(nextyear) as nextyear \n");
			//sbsql.append("decode(3,3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
			sbsql.append("from (  \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万千瓦时' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,1 as lanc,'发电量' as mingc,'万千瓦时' dun from dual) t,\n");
			sbsql.append("(select 1 as lanc,yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 1 as lanc,yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 1 as lanc,yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,2 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 2 as lanc,yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 2 as lanc,yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 2 as lanc,yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 3 as lanc,yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 3 as lanc,yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 3 as lanc,yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万吉焦' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,4 as lanc,'供热量' as mingc,'万吉焦' dun from dual) t,\n");
			sbsql.append("(select 4 as lanc,yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 4 as lanc,yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 4 as lanc,yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'千克/吉焦' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,5 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun from dual) t,\n");
			sbsql.append("(select 5 as lanc,yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 5 as lanc,yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 5 as lanc,yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,6 as lanc,'入炉热值' as mingc,'Mj/kg' dun from dual) t,\n");
			sbsql.append("(select 6 as lanc,yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 6 as lanc,yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 6 as lanc,yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,7 as lanc,'发电需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 7 as lanc,yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 7 as lanc,yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 7 as lanc,yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,8 as lanc,'供热需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 8 as lanc,yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 8 as lanc,yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 8 as lanc,yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,9 as lanc,'需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 9 as lanc,yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 9 as lanc,yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 9 as lanc,yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,10 as lanc,'发电需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 10 as lanc,yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 10 as lanc,yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 10 as lanc,yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,11 as lanc,'供热需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 11 as lanc,yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 11 as lanc,yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 11 as lanc,yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,12 as lanc,'需用原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 12 as lanc,yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 12 as lanc,yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 12 as lanc,yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from \n");
			sbsql.append("(select '现存机组' as jizlx,13 as lanc,'点火助燃油量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 13 as lanc,yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z1, \n");
			sbsql.append("(select 13 as lanc,yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z2, \n");
			sbsql.append("(select 13 as lanc,yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+this.getTreeid()+" and yx.nianf="+intyear+" and yx.jizzt=0  ) z3 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
			sbsql.append(") zz \n");
			sbsql.append("group by (jizlx,lanc,mingc,zz.danw,zz.zhuangt) \n");
			sbsql.append("order by jizlx desc,lanc asc \n");
		
		 ArrHeader=new String[1][6];
		 ArrHeader[0]=new String[] {"项目","项目","单位",lastyear+"年完成",intyear+"年已完成",nextyear+"年预计"};

		 ArrWidth=new int[] {35,150,70,70,80,70};

		 System.out.println(sbsql);
		 ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		 Report rt = new Report();

		Table bt=new Table(rs,1,0,2);
		rt.setBody(bt);
		bt.setColAlign(1, Table.ALIGN_CENTER);
		
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle( "年度耗用需求总表", ArrWidth);//getBiaotmc()+
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(4, 6,riq,Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核人：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2, "填报人：", Table.ALIGN_RIGHT);
	// 设置页数
	_CurrentPage = 1;
	_AllPages = rt.body.getPages();
	if (_AllPages == 0) {
		_CurrentPage = 0;
	}
	con.Close();
	return rt.getAllPagesHtml();

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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("月份:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("YUEF");
//		cb2.setWidth(60);
//		tb1.addField(cb2);
//		tb1.addText(new ToolbarText("-"));
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
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
		
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("审核状态:"));
//		ComboBox tblx = new ComboBox();
//		tblx.setTransform("ShenhDropDown");
//		tblx.setWidth(80);
//		tb1.addField(tblx);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
		}
		getToolbars();

		blnIsBegin = true;

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
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
//		 分公司下拉框
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}

		public IPropertySelectionModel getFengsModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getFengsModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setDiancxxModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(value);
		}

		public void getFengsModels() {
			String sql;
			sql = "select id ,mingc from diancxxb where jib=2 order by id";
			setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
		}
	
//		得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
			return biaotmc;
			
		}
		
//		 年份
		private static IPropertySelectionModel _NianfModel;
		
		public IPropertySelectionModel getNianfModel() {
			if (_NianfModel == null) {
				getNianfModels();
			}
			return _NianfModel;
		}

		private IDropDownBean _NianfValue;

		public IDropDownBean getNianfValue() {
			if (_NianfValue == null||_NianfValue.equals("")) {
				for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
					Object obj = _NianfModel.getOption(i);
					if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_NianfValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _NianfValue;
		}

		public boolean nianfchanged;

		public void setNianfValue(IDropDownBean Value) {
			if (_NianfValue != Value) {
				nianfchanged = true;
			}
			_NianfValue = Value;
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

		// 月份
		public boolean Changeyuef = false;

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
				for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
					Object obj = _YuefModel.getOption(i);
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue!= null) {
				id = getYuefValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					Changeyuef = true;
				} else {
					Changeyuef = false;
				}
			}
			_YuefValue = Value;
			
		}

		public IPropertySelectionModel getYuefModels() {
			List listYuef = new ArrayList();
			// listYuef.add(new IDropDownBean(-1,"请选择"));
			for (int i = 1; i < 13; i++) {
				listYuef.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}
				
		public Toolbar getToolbar() {
			return ((Visit)this.getPage().getVisit()).getToolbar();
		}
		public void setToolbar(Toolbar tb1) {
			((Visit)this.getPage().getVisit()).setToolbar(tb1);
		}
		public String getToolbarScript() {
			return getToolbar().getRenderScript();
		}
		
		
//		类型
		public IDropDownBean getShenhDropDownValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getShenhDropDownModel().getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShenhDropDownValue(IDropDownBean Value) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}

		public void setShenhDropDownModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShenhDropDownModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShenhDropDownModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public void getShenhDropDownModels() {
			List list = new ArrayList();
			Visit visit = (Visit) getPage().getVisit();
			if (visit.getRenyjb()==3) {//电厂
				list.add(new IDropDownBean(0, "未提交"));
				list.add(new IDropDownBean(1, "已提交"));
			}else if(visit.getRenyjb()==2){//分公司
				list.add(new IDropDownBean(1, "未审核"));
				list.add(new IDropDownBean(2, "已审核"));
			}else{//集团
				list.add(new IDropDownBean(2, "未审核"));
				list.add(new IDropDownBean(3, "已审核"));
			}
			((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
			return;
		}
}
