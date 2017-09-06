package com.zhiren.shanxdted.jinmrbb;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Column;
import com.zhiren.report.Report;
import com.zhiren.report.Row;
import com.zhiren.report.Table;

public class Jinmrbbeq extends BasePage implements PageValidateListener {

	
	private static final String BAOBPZB_GUANJZ = "Jinmrbb_GJZ_ED";// baobpzb中对应的关键字
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	绑定日期
	private boolean briqboo=false;
	private String briq;

	public String getBRiq() {
		
		if(this.briq==null || this.briq.equals("")){
			return DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq!=null && !this.briq.equals(briq)){
			briqboo=true;
		}
		this.briq = briq;
	}
	

//	页面变化记录
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

//	获取煤矿
	
	private StringBuffer getMKSql(){
		StringBuffer bf=new StringBuffer();
		String briq ="";
		briq = " and f.daohrq="+DateUtil.FormatOracleDate(getBRiq());
	
//		运输方式
		String yunsfsStr="  ";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr="  and f.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select  m.id ,m.mingc,1 jib from meikxxb m,fahb f,diancxxb d where f.diancxxb_id=d.id and m.id=f.meikxxb_id "+briq+yunsfsStr+" and (d.id in ("+this.getTreeid_dc()+") or d.fuid in ("+this.getTreeid_dc()+"))");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {

		String briq ="";
		briq = " and f.daohrq="+DateUtil.FormatOracleDate(getBRiq());

    	String sql="";
    	
    	sql+="  select 0 id, '全部' mingc from dual union select m.id ,m.mingc from meikxxb m,fahb f,diancxxb d" +
    			" where m.id=f.meikxxb_id"+briq+" " +
    			"and f.diancxxb_id=d.id and ( d.id in("+this.getTreeid_dc()+") or d.fuid in ("+this.getTreeid_dc()+"))";
    	setGongysDropDownModel(new IDropDownModel(sql)) ;
        return ;
    }
    
//  获得选择的树节点的对应的电厂名称   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private ResultSetList hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		String sql="select id, mingc from diancxxb where fuid = " + id +" union  select id,mingc from diancxxb where id="+id+" order by id asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		con.Close();
		return rsl;
	}
    
	//煤矿地区 
	public IDropDownBean getMeikdqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean10()==null) {
			if(getMeikdqModel().getOptionCount()>0) {
				setMeikdqValue((IDropDownBean)getMeikdqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean10();
	}
	
	public void setMeikdqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean10(value);
	}
	
	public IPropertySelectionModel getMeikdqModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getMeikdqModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setMeikdqModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(_value);
	}

	public void getMeikdqModels() {
		
		String sql = 
			"SELECT ID,mingc FROM meikdqb ORDER BY xuh";
		
		setMeikdqModel(new IDropDownModel(sql, "全部"));
		return;
	}
//	获取相关的SQL

	public StringBuffer getBaseSql(String dcid,int dcrw) {
    	Visit visit = (Visit) this.getPage().getVisit();

		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and fc.meikxxb_id="+this.getTreeid();
			meik="  and m.id = "+this.getTreeid();
		}
		
		diancid = " and d.id in ("+ dcid+")";
		if(dcrw>1 && dcid.equals("300")){
			diancid = " and d.fgsid ="+ dcid;
		}
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and cp.yunsdwb_id="+this.getMeikid();
		}
		
		String mkdqWhere="";
		String mkdq = "";
		if (!"".equals(getMeikdqValue().getValue()) && getMeikdqValue().getValue()!=null) {
			mkdq = getMeikdqValue().getValue();
		} else {
			mkdq = ((IDropDownBean)getMeikdqModel().getOption(0)).getValue();
		}
		if ("全部".equals(mkdq)) {
			mkdqWhere = "";
		} else {
			mkdqWhere = " AND mkdq.mingc='" + mkdq + "'";
		}
		String riq ="";
		riq = "  fc.daohrq="+DateUtil.FormatOracleDate(getBRiq());
	
//		运输方式
		String yunsfsStr=" and fc.yunsfsb_id in (1,2) ";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr="  and fc.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select\n");
		sb.append(" decode(grouping(fh.zhilb_id)+grouping(fh.yunsfsb_id),1,decode(fh.yunsfsb_id,1,'火车',2,'汽车',''),mkdq.mingc) mkdq,\n");
		sb.append(" decode(grouping(fh.zhilb_id)+grouping(fh.yunsfsb_id),1,decode(fh.yunsfsb_id,1,'火车',2,'汽车',''),m.mingc) mk,\n");
		sb.append(" decode(grouping(fh.zhilb_id)+grouping(fh.yunsfsb_id),1,decode(fh.yunsfsb_id,1,'火车',2,'汽车',''),fh.ysdw) ysdw,\n");
		sb.append(" sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		sb.append(" sum(round_new(fh.ches,0)) ches,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'','0') as qnet_ar,\n");
		sb.append(" round_new(nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar* 1000 / 4.1816,0) * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+")),0),0) as qbar,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")),0) as mt,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'',0) as mad,\n");
		sb.append(" nvl(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as aar,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as ad,\n");
		sb.append(" nvl(  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.aad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as aad,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,round_new(sum(z.vad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as vad,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as vdaf,\n");
		sb.append(" nvl( decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as stad,\n");
		sb.append(" nvl(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.qgrd * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")),0) as qgrd\n");
		sb.append("  from\n");
		sb.append(" ( select *  from (\n");
		sb.append(" select  fc.id,fc.diancxxb_id,fc.laimsl,fc.jingz,fc.ches,fc.zhilb_id,fc.yunsfsb_id,fc.meikxxb_id,\n");
		sb.append(" (select mingc from yunsdwb where id in (select yunsdwb_id from chepb where fahb_id=fc.id and rownum=1)) ysdw \n");
		sb.append(" from fahb fc where "+riq+yunsfsStr+"\n");
		sb.append(fahgl+"\n");
		sb.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fc.id"+yunsdw+" )=1\n");
		sb.append(" ) ) fh, zhilb z , meikxxb m,meikdqb mkdq,vwdianc d\n");
		sb.append(" where fh.zhilb_id=z.id(+)  and fh.meikxxb_id=m.id AND m.meikdq2_id = mkdq.id and fh.diancxxb_id=d.id "+mkdqWhere+meik+diancid+"\n");
		sb.append(" group by grouping sets('1',(fh.zhilb_id,mkdq.mingc,m.mingc,fh.ysdw,fh.yunsfsb_id),(fh.yunsfsb_id))\n");
		sb.append(" having grouping(fh.zhilb_id)+grouping(fh.yunsfsb_id)!=2\n");
		sb.append(" order by  fh.yunsfsb_id asc,m.mingc,fh.ysdw\n");
		return sb;
    }
	
	private StringBuffer getTJByYunsfsSql(String dcid,int dcrw){
		Visit visit = (Visit) this.getPage().getVisit();
		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and f2.meikxxb_id="+this.getTreeid();
			meik="  and m.id="+this.getTreeid();
		}
	     diancid = " and d.id in ("+ dcid + ")";
	     if(dcrw>1 && dcid.equals(this.getTreeid_dc())){
				diancid = " and d.fgsid ="+ dcid;
			}
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and c.yunsdwb_id="+this.getMeikid();
		}
		String riq ="";
		riq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		String nianc=DateUtil.FormatOracleDate( DateUtil.getFirstDayOfYear(this.getBRiq()));
		String yuec=DateUtil.FormatOracleDate(DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))));
		
//		运输方式
		String yunsfsStr=" where fs.id in (1,2)  ";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr="  where fs.id="+this.getYunsfsValue().getStrId()+" ";
		}
		StringBuffer sb=new StringBuffer();
		
		sb.append(" select  '' AS mkdq,\n");
		sb.append(" nvl( decode(fs.id,1,'火车',2,'汽车')||'自年初累计','') mk,\n");
		sb.append(" (select  sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+nianc+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ysdw,\n");
		sb.append(" nvl( decode(fs.id,1,'火车',2,'汽车')||'自月初累计','') laimsl,\n");
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ches,\n");
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  qnet_ar,\n");
		sb.append(" nvl( decode(fs.id,1,'当日火车累计',2,'当日汽车累计','') ,'') qbar,\n");
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mt,\n");
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mad,\n");
		sb.append(" nvl(decode(fs.id,1,'当日火车车数累计',2,'当日汽车车数累计',''),'') aar,\n");
		sb.append(" nvl(decode(fs.id,1,'当日火车车数累计',2,'当日汽车车数累计',''),'') ad,\n");
		sb.append(" (select sum(round_new(f2.ches,0)) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  aad,\n");
		sb.append(" nvl( decode(fs.id,1,'当日火车加权热值',2,'当日汽车加权热值',''),'') vad,\n");
		sb.append(" nvl( decode(fs.id,1,'当日火车加权热值',2,'当日汽车加权热值',''),'') vad,\n");
		sb.append(" nvl( decode(fs.id,1,'当日火车加权热值',2,'当日汽车加权热值',''),'') vad,\n");
		sb.append(" round_new( (select decode(sum(round_new(f2.laimsl,"+visit.getShuldec()+")), 0, 0,\n");
		sb.append(" round_new(sum(round_new(z.qnet_ar* 1000 / 4.1816,0) * round_new(f2.laimsl,"+visit.getShuldec()+")) / sum(round_new(f2.laimsl,"+visit.getShuldec()+")),  "+visit.getFarldec()+")) as qbar\n");
		sb.append("  from fahb f2,vwdianc d,zhilb z where \n");
		sb.append(" f2.daohrq="+riq+" \n");
		sb.append(" and f2.zhilb_id=z.id(+)  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1  "+fahgl+diancid+"),0) qgrd \n");
		sb.append(" from yunsfsb fs "+yunsfsStr+" \n");
		return sb;
	}
	
	private StringBuffer getTJSql(String dcid,int dcrw){
		Visit visit = (Visit) this.getPage().getVisit();
		
		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and f2.meikxxb_id="+this.getTreeid();
			meik="  and m.id="+this.getTreeid();
		}
		
	     diancid = " and d.id in ("+ dcid + ")";
	
	     if(dcrw>1 && dcid.equals(this.getTreeid_dc())){
				diancid = " and d.fgsid ="+ dcid;
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and c.yunsdwb_id="+this.getMeikid();
		}
		
		String riq ="";
		riq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		String nianc=DateUtil.FormatOracleDate( DateUtil.getFirstDayOfYear(this.getBRiq()));
		String yuec=DateUtil.FormatOracleDate(DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))));
		
//		运输方式
		String yunsfsStr=" and f2.yunsfsb_id in (1,2) ";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr="  and f2.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		StringBuffer sb=new StringBuffer();
		
		sb.append(" select  '' as mkdq,\n");
		sb.append(" nvl( '自年初累计','') mk,\n");
		sb.append(" (select  sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+nianc+" \n");
		sb.append(" and f2.daohrq<="+riq+yunsfsStr+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ysdw,\n");
		sb.append(" nvl( '自月初累计','') laimsl,\n");
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+yunsfsStr+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ches,\n");
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+yunsfsStr+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  qnet_ar,\n");
		sb.append(" nvl('当日累计','') qbar,\n");
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq="+riq+yunsfsStr+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mt,\n");
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq="+riq+yunsfsStr+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mad,\n");
		sb.append(" nvl('当日车数累计','') aar,\n");
		sb.append("  nvl('当日车数累计','') ad,\n");
		sb.append(" (select sum(round_new(f2.ches,0)) from fahb f2,vwdianc d where f2.daohrq="+riq+yunsfsStr+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  aad,\n");
		sb.append(" nvl('当日加权热值','') vad,\n");
		sb.append(" nvl('当日加权热值','') vad,\n");
		sb.append(" nvl('当日加权热值','') vad,\n");
		sb.append(" round_new((select decode(sum(round_new(f2.laimsl,"+visit.getShuldec()+")), 0, 0,\n");
		sb.append(" round_new(sum(round_new(z.qnet_ar* 1000 / 4.1816,0) * round_new(f2.laimsl,"+visit.getShuldec()+")) / sum(round_new(f2.laimsl,"+visit.getShuldec()+")),  "+visit.getFarldec()+")) as qbar\n");
		sb.append("  from fahb f2,vwdianc d,zhilb z where \n");
		sb.append(" f2.daohrq="+riq+yunsfsStr+" \n");
		sb.append(" and f2.zhilb_id=z.id(+)     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1  "+fahgl+diancid+"),0) qgrd \n");
		sb.append(" from dual\n");
		return sb;
	}
//	获取表表标题
	public String getRptTitle(String diancid) {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb="";
		String dcmc="";
		
		try{
			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id in ("+diancid+")");
		}catch(Exception e){
			e.printStackTrace();
		}
//		sb=visit.getDiancqc()+"进煤日报表";
		sb = dcmc + "进煤日报表";
		return sb;
	}
	
	//------------运输单位----------
	private String meikid = "";
		public String getMeikid() {
			if (meikid.equals("")) {

				meikid = "0";
			}
			return meikid;
		}
		public void setMeikid(String meikid) {
			if(meikid!=null) {
				if(!meikid.equals(this.meikid)) {
					((TextField)getToolbar().getItem("meikTree_text")).setValue
					(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
					this.getTree().getTree().setSelectedNodeid(meikid);
				}
			}
			this.meikid = meikid;
		}
		
		//获得运输单位 树形结构sql
		private StringBuffer getDTsql(){
			
			String briq ="";
			briq = "  and f.daohrq="+DateUtil.FormatOracleDate(getBRiq());
		
//			运输方式
			String yunsfsStr="  ";
			if(!this.getYunsfsValue().getStrId().equals("0")){
				yunsfsStr="  and f.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
			}
			
			Visit visit=(Visit)this.getPage().getVisit();
			StringBuffer bf=new StringBuffer();
			
			bf.append(" select distinct * from ( \n");
			bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
			bf.append(" union\n");
			bf.append(" select y.id,y.mingc,1 jib  from yunsdwb y where y.id in ( select c.yunsdwb_id from chepb c where c.fahb_id in (select f.id from fahb f,diancxxb d where f.diancxxb_id=d.id "+briq+yunsfsStr+" and (d.id in ("+this.getTreeid_dc()+") or d.fuid in ("+this.getTreeid_dc()+"))   ))");
			bf.append(" ) \n");
			bf.append(" order by id,mingc\n");
			
			return bf;
		}
		
		DefaultTree mktr;
		
		public DefaultTree getTree() {
			return mktr;
		}
		public void setTree(DefaultTree etu) {
			mktr=etu;
		}

		public String getTreeScriptMK() {
			return this.getTree().getScript();
		}

		public IPropertySelectionModel getMeikModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
				getMeikModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel3();
		}

		public void setMeikModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel3(_value);
		}

		public void getMeikModels() {
			String briq ="";
			briq = "  f.daohrq="+DateUtil.FormatOracleDate(this.getBRiq());
			
			String sql = "select 0 id,'全部' mingc from dual union select y.id,y.mingc  from yunsdwb y where y.id in ( select c.yunsdwb_id from chepb c where c.fahb_id in (select f.id from fahb f,diancxxb d where  f.diancxxb_id=d.id and "+briq+" and (d.id in ("+this.getTreeid_dc()+") or d.fuid in ("+this.getTreeid_dc()+")) ))";
			setMeikModel(new IDropDownModel(sql));
		}
		
		//-------------------------------------------------
		
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
		
////		电厂Tree
//		DefaultTree dt1 = null;
//		
//		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
//		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb中组的名称
//		ResultSetList rsl=con.getResultSetList(toaijsql);
//		
//		if (!rsl.next()) {
//			dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
//					"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid_dc());
//		} else {
//			rsl=con.getResultSetList("select * from diancxxb d where d.id  in (select fuid from diancxxb)");
//			if (rsl.next()) {
//				dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
//						"diancTree", "" + rsl.getString("id"), "forms[0]", null, getTreeid_dc());
//			}
//		}
//		
//		setTree_dc(dt1);
//		TextField tf1 = new TextField();
//		tf1.setId("diancTree_text");
//		tf1.setWidth(100);
//		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
//				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
//						: getTreeid_dc())));
//
//		ToolbarButton tb3 = new ToolbarButton(null, null,
//				"function(){diancTree_window.show();}");
//		tb3.setIcon("ext/resources/images/list-items.gif");
//		tb3.setCls("x-btn-icon");
//		tb3.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("电厂:"));
//		tb1.addField(tf1);
//		tb1.addItem(tb3);
//		tb1.addText(new ToolbarText("-"));
		
//		电厂Tree
		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb中组的名称
		ResultSetList rsl=con.getResultSetList(toaijsql);
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		if(rsl.next()){
			diancxxb_id = 300;
		}
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
		ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid_dc(),null,true);
		setDCTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid_dc().split(",");
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		ToolbarButton toolb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		toolb2.setIcon("ext/resources/images/list-items.gif");
		toolb2.setCls("x-btn-icon");
		toolb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(toolb2);
		
		//煤矿
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "gongysTree", visit.getDiancxxb_id()+"", "forms[0]", this.getTreeid(), this.getTreeid());
		visit.setDefaultTree(gystree);
		TextField tf1 = new TextField();
		tf1.setId("gongysTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("煤矿:"));
		tb1.addField(tf1);
		tb1.addItem(tb2);
		
		DefaultTree mktree=new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit.getDiancxxb_id()+"", "forms[0]", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);
		
		TextField tfmk=new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel)this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null || "".equals(this.getMeikid()) ? "-1"
						: this.getMeikid())));

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);
		
		tb1.addText(new ToolbarText("运输单位"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);
		
		//第二工具栏
		Toolbar toolbar2 = new Toolbar("tbdiv");
		//煤矿地区
		toolbar2.addText(new ToolbarText("煤矿地区:"));
		ComboBox meikdq = new ComboBox();
		meikdq.setTransform("MeikdqSelect");
		meikdq.setEditable(true);
		meikdq.setWidth(100);
		toolbar2.addField(meikdq);	
		
//		运输方式
		toolbar2.addText(new ToolbarText("运输方式:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(80);
		toolbar2.addField(meik);	
		
		toolbar2.addText(new ToolbarText("-"));	
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		toolbar2.setWidth("bodyWidth");
		toolbar2.addItem(rbtn);
		
		tb1.addFill();
		setToolbar(tb1);
		setToolbar2(toolbar2);
	}
	
//	 运输方式下拉框
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) getYunsfsModel().getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select 0 id,'全部' mingc from dual union select id,mingc from yunsfsb";//连接一条假设行，如果ID为0时，运输方式为全部.
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}
	
	
	private String getPrintStr(String diancid,int dcrw){

		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql="";
		sql=this.getBaseSql(diancid,dcrw).toString();

		//System.out.println(sql);
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
	
        	rs = rstmp;
        	ArrHeader = new String[][] {
        		 {"煤矿地区", "煤矿","运输单位","数量验收(吨)","车数(车)","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果",
        		 "化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果"},
        		 {"煤矿地区", "煤矿","运输单位","当日","当日","Qnet","大卡","Mt","Mad","Aar","Ad","Aad","Vad","Vdaf","Stad","Qgrd"}};
    
    		ArrWidth = new int[] {70 ,150,  110, 60,  50, 55, 60, 55, 55, 55, 60 , 55, 55, 55, 55, 55};
    
    		rt.setTitle(getRptTitle(diancid), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		String dcmc="";
    		
    		try{
    			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id in ("+diancid+")");
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		rt.setDefaultTitle(1,3, "单位：" +dcmc , Table.ALIGN_LEFT);
    		rt.setDefaultTitle(6, 3, getBRiq() , Table.ALIGN_CENTER);
    		
    		strFormat = new String[] {"", "", "", "", "", "0.00", "", "0.00","0.00","0.00","0.00","0.00",
    				"0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 2, 0, 3));
		
		rt.body.setFontSize(10);
		
		ResultSetList rstj=con.getResultSetList(this.getTJByYunsfsSql(diancid,dcrw).toString());
		rt.body.AddTableData(rstj);
		ResultSetList rsl=con.getResultSetList(this.getTJSql(diancid,dcrw).toString());
		rt.body.AddTableData(rsl);
	
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		
		if(dcrw<=1){
			rt.body.setPageRows(22);
		}
		rt.body.merge(1, 1,2 ,16);
		rt.body.mergeCol(1);
		rs.beforefirst();
		int i=2;
		while(rs.next()){
			if(rs.getString("MK").equals("火车") || rs.getString("MK").equals("汽车")){
				
				for(int j=1;j<ArrWidth.length+1;j++){
//					rt.body.getCell(i+1, j).foreColor="blue";
//					rt.body.getCell(i+1, j).fontBold=true;
				}
				rt.body.getCell(i+1, 1).used=true;
				rt.body.getCell(i+1, 2).used=true;
				
				rt.body.merge(i+1, 1,i+1, 2);
			}
			i++;
		}
		
		rstj.beforefirst();
		while(rstj.next()){
			
			for(int j=1;j<ArrWidth.length+1;j++){
//				rt.body.getCell(i+1, j).foreColor="blue";
//				rt.body.getCell(i+1, j).fontBold=true;
			}
			
			rt.body.merge(i+1, 1,i+1 , 16);
			for(int j=1;j<17;j++){
				rt.body.getCell(i+1, j).setBorder(0, 0, 0, 0);
			}
			i++;
		}
		
		rsl.beforefirst();
		while(rsl.next()){
			
			for(int j=1;j<ArrWidth.length+1;j++){
//				rt.body.getCell(i+1, j).foreColor="blue";
//				rt.body.getCell(i+1, j).fontBold=true;
			}
			
			rt.body.merge(i+1, 1,i+1 , 16);
			for(int j=1;j<17;j++){
				rt.body.getCell(i+1, j).setBorder(0, 0, 0, 0);
			}
			i++;
		}
		
//		for(int j=1;j<rt.body.getRows()+1;j++){
//			for(int k=1;k<3;k++){
//				String strValue="<div nowrap  style=\"width:"+ArrWidth[k-1]+";overflow:hidden; \">"+rt.body.getCell(j, k).value+"</div>";
//				rt.body.setCellValue(j, k, strValue);
//			}
//		}
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 2, "煤检主管：" ,Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "采样：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "制样：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "司磅：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "化验主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "化验：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(25);

		for(int k=0;k<rt.body.rows.length;k++){
			Row row=rt.body.rows[k];
			
			if(row !=null && k%39==0){
				row.className="\"row_page_end\"";
			}
		}

		return rt.getAllPagesHtml();
	}
	
	public String getPrintTable(){
		
		String diancid="";
		String s="";
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
//			
//			ResultSetList rsl = this.hasDianc(this.getTreeid_dc());
//			  
//			int count=rsl.getRows();
//			int c=0;
//			while(rsl.next()){
//				
//				if(c!=0){
//					s+="<div style=\"PAGE-BREAK-AFTER: always\"></div> ";
//				}
//				diancid=rsl.getString("id");
//				
//				s+=this.getPrintStr(diancid,count);
//				
//				c++;
//			}
		
			String[] arrDc = this.getTreeid_dc().split(","); 
			if (arrDc[0].equals("300")) {
				ResultSetList rsl = this.hasDianc(arrDc[0]);
				  
				int count=rsl.getRows();
				int c=0;
				while(rsl.next()){
					
					if(c!=0){
						s+="<div style=\"PAGE-BREAK-AFTER: always\"></div> ";
					}
					diancid=rsl.getString("id");
					
					s+=this.getPrintStr(diancid,count);
					
					c++;
				}
			}
			else {
				s=this.getPrintStr(this.getTreeid_dc(), 1);
			}
		}
		return s;
	}
	
//	格式下拉框
	public IDropDownBean getGesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGesModel().getOptionCount()>0) {
				setGesValue((IDropDownBean)getGesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setGesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getGesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setGesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setGesModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		
		List list=new ArrayList();
		list.add(new IDropDownBean("1","简报"));
		list.add(new IDropDownBean("2","明细"));
		setGesModel(new IDropDownModel(list));
	}
	
	//---------------------------
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		String sql = "select id,mingc from diancxxb";
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
	private boolean  dcidboo=false;
	
	public void setTreeid_dc(String treeid) {
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
			dcidboo=true;
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript1() {
		return getDCTree().getWindowTreeScript();
	}
	public String getTreeHtml1() {
		return getDCTree().getWindowTreeHtml(this);
	}
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
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
		return getToolbar().getRenderScript() + getOtherScript("diancTree");
	}
	//增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	//第二个工具条
	Toolbar tbar2;
	public Toolbar getToolbar2() {
		return tbar2;
	}
	public void setToolbar2(Toolbar tb2) {
		tbar2=tb2;
	}
	
	public String getToolbarScript2() {
		if(tbar2==null){
			return "";
		}
		return getToolbar2().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			
			this.meikid="";
			this.treeid="";
			this.setTreeid_dc(null);
			getGongysDropDownModels();
			this.getMeikModels();
			this.setGesModel(null);
		
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			this.setYunsfsModel(null);
			this.setYunsfsValue(null);
			
		}
		
		if(briqboo || dcidboo){
			briqboo=false;
			
			dcidboo=false;
			this.meikid="";
			this.treeid="";
			
			getGongysDropDownModels();
			this.getMeikModels();
		}
		getSelectData();
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		Visit visit=(Visit)this.getPage().getVisit();
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		getSelectData();
	}
//	页面登陆验证
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
