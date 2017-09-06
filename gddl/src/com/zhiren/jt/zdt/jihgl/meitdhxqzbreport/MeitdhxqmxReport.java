////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

package com.zhiren.jt.zdt.jihgl.meitdhxqzbreport;
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

public class MeitdhxqmxReport extends BasePage {
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
		//String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				//leix=pa[0];//类型
				diancid="" +pa[1];//电厂ID
			}else{
				diancid="-1";
			}
		}else{
			return "";
		}
		
		String jizzt="";//机组状态
		String title="";//表头名称
		if (visit.getString4()!=null){
			if (visit.getString4().equals("")){
				jizzt="";
			}else{
				if (visit.getString4().equals("0")){
					jizzt=" and yx.jizzt=0 ";
					title="现役机组";
				}else if (visit.getString4().equals("1")){
					jizzt=" and yx.jizzt=1 ";
					title="新增机组";
				}
			}
		}
		
		long nianf=Integer.parseInt(visit.getString3());
		return getMingx(diancid,nianf,jizzt,title);
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
	
	private String getMingx(String diancid,long nianf,String jizzt,String title){//电厂ID，年份，审核状态
		
		long intyear;
		long lastyear;
		long nextyear;
		if (nianf == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = nianf-1;
			lastyear=intyear-1;
			nextyear=nianf;
		}
		
		Visit visit = (Visit) getPage().getVisit();
		String jizzt1=jizzt;
		
		if (visit.getString4()=="1"){
			jizzt="  and yx.jizzt=2  ";//新增机组是z1,z2不取数
		}
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String date=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(datEnd))+"至"+DateUtil.FormatDate(datEnd);
		
//		String riq1=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename=title+"年度耗用需求总表";
		/*int iFixedRows=0;//固定行号
		int iCol=0;//列数*/		
		//报表内容
		titlename=titlename+"";
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
		sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'供电标准煤耗',3,'综合厂用电率',4,'发电标准煤耗',5,'供热量',6,'供热标准煤耗', \n");
		sbsql.append("7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'点火助燃油量',11,'油发热量',  \n");
		sbsql.append("12,'需用煤折标煤量',13,'入炉热值',14,'需用原煤量',15,'其他用',16,'运损',17,'期初库存',18,'期未库存',19,'总需求量',mingc),mingc) as mingc,zz.danw,  \n");
		sbsql.append("sum(lastyear) as lastyear,sum(year) as year,sum(nextyear) as nextyear \n");
		//sbsql.append("decode(3,3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
		sbsql.append("from (  \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万千瓦时' as danw from \n");
		sbsql.append("(select 1 as lanc,'发电量' as mingc,'万千瓦时' dun from dual) t,\n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"  ) z1, \n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 1 as lanc,sum(yx.fadl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万千瓦时' as danw from \n");
		sbsql.append("(select 2 as lanc,'供电标准煤耗' as mingc,'克/千瓦时' dun from dual) t,\n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 2 as lanc,sum(yx.gongdbzmh) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw from \n");
		sbsql.append("(select 3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun from dual) t,\n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 3 as lanc,round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw from \n");
		sbsql.append("(select 4 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun from dual) t,\n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 4 as lanc,round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万吉焦' as danw from \n");
		sbsql.append("(select 5 as lanc,'供热量' as mingc,'万吉焦' dun from dual) t,\n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 5 as lanc,sum(yx.gongrl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'千克/吉焦' as danw from \n");
		sbsql.append("(select 6 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun from dual) t,\n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 6 as lanc,round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 7 as lanc,'发电需用标煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 7 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 8 as lanc,'供热需用标煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 8 as lanc,round_new(sum(yx.gongrxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 9 as lanc,'需用标煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 9 as lanc,round_new(sum(yx.fadxybml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		/*sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 10 as lanc,'发电需原煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,sum(yx.fadxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 11 as lanc,'供热需原煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,sum(yx.gongrxyml) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");*/
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 10 as lanc,'点火助燃油量' as mingc,'吨' dun from dual) t,\n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,sum(yx.dianhzryl) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 11 as lanc,'油发热量' as mingc,'Mj/kg' dun from dual) t,\n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*(yx.dianhzryl))/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 12 as lanc,'需用煤折标煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 12 as lanc,round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw from \n");
		sbsql.append("(select 13 as lanc,'入炉热值' as mingc,'Mj/kg' dun from dual) t,\n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 13 as lanc,round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 14 as lanc,'需用原煤量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 14 as lanc,round_new(sum(xuyyml)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
//		新增：其他用，运损，期初库存，期未库存
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 15 as lanc,'其他用' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 15 as lanc,round_new(sum(yx.qity)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 16 as lanc,'运损' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 16 as lanc,round_new(sum(yx.yuns)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 17 as lanc,'期初库存' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 17 as lanc,round_new(sum(yx.qickc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 18 as lanc,'期未库存' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 18 as lanc,round_new(sum(yx.qimkc)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,t.dun as danw from \n");
		sbsql.append("(select 19 as lanc,'总需求量' as mingc,'万吨' dun from dual) t,\n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z1, \n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt+"   ) z2, \n");
		sbsql.append("(select 19 as lanc,round_new(sum(yx.zongxql)/10000,2) as zhi from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+diancid+" and yx.nianf="+nextyear+" "+jizzt1+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		
		sbsql.append(") zz \n");
		sbsql.append("group by (lanc,mingc,zz.danw) \n");
		sbsql.append("order by lanc asc \n");
		
		ArrHeader=new String[1][6];
		ArrHeader[0]=new String[] {"项目","项目","单位",lastyear+"年完成",intyear+"年已完成",nextyear+"年预计"};

		ArrWidth=new int[] {35,150,70,70,80,70};

		System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		// 数据
		rt.setBody(new Table(rs,1, 0, 2));

		rt.setTitle(titlename, ArrWidth);
//			rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4,date, Table.ALIGN_CENTER);
//			rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(19);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 
		
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(8,1,"单位:车、吨",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(6,3,"制表:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(12,2,"审核:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		
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