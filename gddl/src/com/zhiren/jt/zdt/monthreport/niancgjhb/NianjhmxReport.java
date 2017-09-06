////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

package com.zhiren.jt.zdt.monthreport.niancgjhb;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class NianjhmxReport extends BasePage {
	private String leix="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {  
		
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
		
		Visit visit = (Visit) getPage().getVisit();
		
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		
		int intLen=0;
		String lx=getLeix();
		intLen=lx.indexOf(",");
		String diancid="";
		String mxlx="";
		//String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==3){
				//leix=pa[0];//类型
				diancid=""+pa[1];//电厂ID
				mxlx=""+pa[2];//报表明细类型
			}else{
				diancid="-1";
			}
		}else{
			return "";
		}
		
		String riq=visit.getString3();
		return getMingx(diancid,riq,mxlx);
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}

//	private String OraDate(Date _date){
//		if (_date == null) {
//			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
//		}
//		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
//	}
	
	private String getMingx(String diancid,String riq,String mxlx){//电厂ID，年份，审核状态
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		
		long intyear;
		long lastyear;
		long nextyear;
		if (riq == null) {
			intyear = DateUtil.getYear(new Date());
			lastyear=intyear-1;
			nextyear=intyear+1;
		} else {
			intyear = Integer.parseInt(riq);
			lastyear=intyear-1;
			nextyear=intyear+1;
		}
	
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		StringBuffer sbsql = new StringBuffer();
		
		String tablename="";
		if (mxlx.equals("0")){
			tablename="年耗用量计划";
			
			sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
			sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'发电标准煤耗',3,'综合厂用电率',4,'供热量',5,'供热标准煤耗', \n");
			sbsql.append("6,'入炉热值',7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'发电需原煤量',11,'供热需原煤量',  \n");
			sbsql.append("12,'需用原煤量',13,'点火助燃油量',14,'计划发电',15,'供热标煤量',16,'标煤耗',mingc),mingc) as mingc,zz.danw,  \n");
			sbsql.append("sum(lastyear) as lastyear \n");
			//sbsql.append("decode(3,3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
			sbsql.append("from (  \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'万千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,1 as lanc,'发电量' as mingc,'万千瓦时' dun from dual) t,\n");
			sbsql.append("(select 1 as lanc,sum(fadl) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'克/千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,2 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 2 as lanc,round_new(decode(sum(decode(fadbhm,0,0,fadl)),0,0,sum(fadbhm*fadl)/sum(decode(fadbhm,0,0,fadl))),2) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'克/千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(zonghcydl,0,0,fadl)),0,0,sum(zonghcydl*fadl)/sum(decode(zonghcydl,0,0,fadl))),2)as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'万吉焦' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,4 as lanc,'供热量' as mingc,'万吉焦' dun from dual) t,\n");
			sbsql.append("(select 4 as lanc,sum(gongrl) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'千克/吉焦' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,5 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun from dual) t,\n");
			sbsql.append("(select 5 as lanc,round_new(decode(sum(decode(gongrbzmh,0,0,gongrl)),0,0,sum(gongrbzmh*gongrl)/sum(decode(gongrbzmh,0,0,gongrl))),2) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'Mj/kg' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,6 as lanc,'入炉热值' as mingc,'Mj/kg' dun from dual) t,\n");
			sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(rulrz,0,0,xuyyml)),0,0,sum(rulrz*(xuyyml))/sum(decode(rulrz,0,0,xuyyml))),2) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,7 as lanc,'发电需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 71 as lanc,sum(fadxybml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,8 as lanc,'供热需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 8 as lanc,sum(gongrxybml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,9 as lanc,'需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 9 as lanc,sum(xuybml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,10 as lanc,'发电需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 10 as lanc,sum(fadxyml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,11 as lanc,'供热需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 11 as lanc,sum(gongrxyml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,12 as lanc,'需用原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 12 as lanc,sum(xuyyml) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as intyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,13 as lanc,'点火助燃油量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 13 as lanc,sum(dianhzryl) as zhi from nianxqjhh where shujzt="+intyear+" and diancxxb_id="+diancid+" and nianf="+intyear+") z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append(") zz \n");
			sbsql.append("group by (jizlx,lanc,mingc,zz.danw) \n");
			sbsql.append("order by jizlx desc,lanc asc \n");
			
			 ArrHeader=new String[1][4];
			 ArrHeader[0]=new String[] {"项目","项目","单位","值"};

			 ArrWidth=new int[] {35,150,70,70};
		}else if (mxlx.equals("1")){//年耗用量计划
			tablename="现役机组年耗用量计划";
			
			sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
			sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'发电标准煤耗',3,'综合厂用电率',4,'供热量',5,'供热标准煤耗', \n");
			sbsql.append("6,'入炉热值',7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'发电需原煤量',11,'供热需原煤量',  \n");
			sbsql.append("12,'需用原煤量',13,'点火助燃油量',14,'计划发电',15,'供热标煤量',16,'标煤耗',mingc),mingc) as mingc,zz.danw,  \n");
			sbsql.append("sum(lastyear) as lastyear \n");
			//sbsql.append("decode(3,3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
			sbsql.append("from (  \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'万千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,1 as lanc,'发电量' as mingc,'万千瓦时' dun from dual) t,\n");
			sbsql.append("(select 1 as lanc,fadl as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'克/千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,2 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 2 as lanc,fadbhm as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'克/千瓦时' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun from dual) t,\n");
			sbsql.append("(select 3 as lanc,zonghcydl as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'万吉焦' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,4 as lanc,'供热量' as mingc,'万吉焦' dun from dual) t,\n");
			sbsql.append("(select 4 as lanc,gongrl as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'千克/吉焦' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,5 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun from dual) t,\n");
			sbsql.append("(select 5 as lanc,gongrbzmh as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'Mj/kg' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,6 as lanc,'入炉热值' as mingc,'Mj/kg' dun from dual) t,\n");
			sbsql.append("(select 6 as lanc,rulrz as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,7 as lanc,'发电需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 71 as lanc,fadxybml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,8 as lanc,'供热需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 8 as lanc,gongrxybml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,9 as lanc,'需用标煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 9 as lanc,xuybml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,10 as lanc,'发电需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 10 as lanc,fadxyml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,11 as lanc,'供热需原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 11 as lanc,gongrxyml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,12 as lanc,'需用原煤量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 12 as lanc,xuyyml as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append("union \n");
			sbsql.append("select t.jizlx as jizlx,t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,'吨' as danw from \n");
			sbsql.append("(select '现存机组' as jizlx,13 as lanc,'点火助燃油量' as mingc,'吨' dun from dual) t,\n");
			sbsql.append("(select 13 as lanc,dianhzryl as zhi from nianxqjhh where shujzt="+lastyear+" and diancxxb_id="+diancid+" and nianf="+intyear+" and jizzt=0  ) z1 \n");
			sbsql.append(" where t.lanc=z1.lanc(+) ");
			sbsql.append(") zz \n");
			sbsql.append("group by (jizlx,lanc,mingc,zz.danw) \n");
			sbsql.append("order by jizlx desc,lanc asc \n");
			
			ArrHeader=new String[1][4];
			 ArrHeader[0]=new String[] {"项目","项目","单位","值"};

			 ArrWidth=new int[] {35,150,70,70};

		}else{//新增机组电煤需求表
			tablename="新增机组电煤需求表";
			
			sbsql.append("select decode(grouping(vdc.mingc),1,'总计',vdc.mingc) as danwmc,\n");
			sbsql.append("sf.quanc,\n");
			sbsql.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
			sbsql.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
//			sbsql.append("from nianxqjhh n,\n");
			sbsql.append("from (select * from nianxqjhh where nianf = "+intyear+" and diancxxb_id = "+diancid+" and shujzt='"+intyear+"' and jizzt=1) n,\n ");
			sbsql.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
			sbsql.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
			sbsql.append("from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
			sbsql.append("and to_char(y.riq,'yyyy')=").append(intyear);
			sbsql.append("group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
			sbsql.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
			sbsql.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
			sbsql.append("and n.nianf=").append(intyear);
			sbsql.append(" and dc.id=").append(diancid).append("\n");
			sbsql.append("group by rollup (vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
			sbsql.append("having not (grouping(sf.quanc) || grouping(n.beiz)) =1 \n");
			sbsql.append("order by grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
			sbsql.append("grouping(sf.quanc) desc,sf.quanc \n");
			
			ArrHeader=new String[1][11];
			ArrHeader[0]=new String[] {"电厂名称","所属省份","装机容量<br>（万千瓦）","投产日期","计划电量<br>（亿千瓦时）","设计煤种","主要需求供煤单位及数量<br>(万吨)","需求煤量<br>（万吨）","到站/中转港","运输方式","备注"};

			ArrWidth=new int[] {150,70,65,120,80,58,150,58,58,58,60};

		}
		System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		//报表内容
		tablename=tablename+"";
		
		// 数据
		Table bt=new Table(rs,1,0,1);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		//
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle( tablename, ArrWidth);//getBiaotmc()+
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(4, 4,""+riq,Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 4, "审核人：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 4, "填报人：", Table.ALIGN_RIGHT);
	// 设置页数
	_CurrentPage = 1;
	_AllPages = rt.body.getPages();
	if (_AllPages == 0) {
		_CurrentPage = 0;
	}
	cn.Close();
	return rt.getAllPagesHtml();
		
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}*/

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
	}

}