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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
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
/*
 * 作者:tzf
 * 时间:2009-07-13
 * 内容:进煤日报表
 */
public class Jinmrbb extends BasePage implements PageValidateListener{

	
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
		// TODO 自动生成方法存根
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
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
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
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
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
    	String sql="";
    	
    	sql+="  select 0 id, '全部' mingc from dual union select id ,mingc from meikxxb";
//        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
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
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
//	获取相关的SQL

	public StringBuffer getBaseSql() {
    	Visit visit = (Visit) this.getPage().getVisit();

	
		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and fc.meikxxb_id="+this.getTreeid();
			meik="  and m.id="+this.getTreeid();
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and d.id ="+ getTreeid_dc();
			}
			
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and cp.yunsdwb_id="+this.getMeikid();
		}
		
		String riq ="";
		riq = "  fc.daohrq="+DateUtil.FormatOracleDate(getBRiq());
	
		
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select\n");
		sb.append(" decode(grouping(fh.id)+grouping(fh.yunsfsb_id),1,decode(fh.yunsfsb_id,1,'火车',2,'汽车',''),m.mingc) mk,\n");
		sb.append(" decode(grouping(fh.id)+grouping(fh.yunsfsb_id),1,decode(fh.yunsfsb_id,1,'火车',2,'汽车',''),fh.ysdw) ysdw,\n");
		sb.append(" sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		sb.append(" sum(round_new(fh.ches,0)) ches,\n");
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'' as qnet_ar,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816, 0)) as qbar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")) as mt,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'' as mad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as ad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.aad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,round_new(sum(z.vad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vdaf,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as stad,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.qgrd * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as qgrd\n");
		
		sb.append("  from\n");
		
		sb.append(" ( select *  from (\n");
		sb.append(" select  fc.id,fc.diancxxb_id,fc.laimsl,fc.jingz,fc.ches,fc.zhilb_id,fc.yunsfsb_id,fc.meikxxb_id,\n");
		sb.append(" (select mingc from yunsdwb where id in (select yunsdwb_id from chepb where fahb_id=fc.id and rownum=1)) ysdw \n");
		
		sb.append(" from fahb fc where "+riq+"\n");
		sb.append(fahgl+"\n");
		sb.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fc.id"+yunsdw+" )=1\n");
		sb.append(" ) ) fh, zhilb z , meikxxb m,vwdianc d\n");
		
		sb.append(" where fh.zhilb_id=z.id  and fh.meikxxb_id=m.id and fh.diancxxb_id=d.id "+meik+diancid+"\n");
		sb.append(" group by grouping sets('1',(fh.id,m.mingc,fh.ysdw,fh.yunsfsb_id),(fh.yunsfsb_id))\n");
		sb.append(" having grouping(fh.id)+grouping(fh.yunsfsb_id)!=2\n");
		sb.append(" order by  fh.yunsfsb_id asc,grouping(fh.id) asc\n");
		return sb;
    }
	
	
	private StringBuffer getTJByYunsfsSql(){
		Visit visit = (Visit) this.getPage().getVisit();

		
		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and f2.meikxxb_id="+this.getTreeid();
			meik="  and m.id="+this.getTreeid();
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and d.id ="+ getTreeid_dc();
			}
			
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and c.yunsdwb_id="+this.getMeikid();
		}
		
		String riq ="";
		riq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		
		String nianc=DateUtil.FormatOracleDate( DateUtil.getFirstDayOfYear(this.getBRiq()));
		String yuec=DateUtil.FormatOracleDate(DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))));
		
		StringBuffer sb=new StringBuffer();
		
		sb.append(" select  \n");
		sb.append("  decode(fs.id,1,'火车',2,'汽车')||'自年初累计' mk,\n");
		
		sb.append(" (select  sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+nianc+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ysdw,\n");
		
		sb.append(" decode(fs.id,1,'火车',2,'汽车')||'自月初累计' laimsl,\n");
		
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ches,\n");
		
		
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+" and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  qnet_ar,\n");
		
		sb.append(" nvl('当日累计','') qbar,\n");
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mt,\n");
		
		sb.append(" (select sum(round_new(f2.laimsl,2)) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mad,\n");
		
		sb.append(" nvl('当日车数累计','') aar,\n");
		sb.append("  nvl('当日车数累计','') ad,\n");
		sb.append(" (select sum(round_new(f2.ches,0)) from fahb f2,vwdianc d where f2.daohrq>="+riq+" \n");
		sb.append("  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  aad,\n");
		
		
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		
		sb.append(" (select decode(sum(round_new(f2.laimsl,"+visit.getShuldec()+")), 0, 0,\n");
		sb.append(" round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(f2.laimsl,"+visit.getShuldec()+")) / sum(round_new(f2.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816,  0)) as qbar\n");
		
		sb.append("  from fahb f2,vwdianc d,zhilb z where \n");
		sb.append(" f2.daohrq="+riq+" \n");
	
		sb.append(" and f2.zhilb_id=z.id  and f2.yunsfsb_id=fs.id and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1  "+fahgl+diancid+") qgrd \n");
		sb.append(" from yunsfsb fs where fs.id in (1,2)\n");
		return sb;
	}
	
	private StringBuffer getTJSql(){
		Visit visit = (Visit) this.getPage().getVisit();

		
		String meik="";
		String fahgl="";
		String diancid = "" ;
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and f2.meikxxb_id="+this.getTreeid();
			meik="  and m.id="+this.getTreeid();
		}
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and d.id ="+ getTreeid_dc();
			}
			
		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and c.yunsdwb_id="+this.getMeikid();
		}
		
		String riq ="";
		riq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		
		String nianc=DateUtil.FormatOracleDate( DateUtil.getFirstDayOfYear(this.getBRiq()));
		String yuec=DateUtil.FormatOracleDate(DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))));
		
		StringBuffer sb=new StringBuffer();
		
		sb.append(" select  \n");
		sb.append("  nvl('自年初累计','') mk,\n");
		
		sb.append(" (select  sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+nianc+" \n");
		sb.append(" and f2.daohrq<="+riq+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ysdw,\n");
		
		sb.append("  nvl('自月初累计','') laimsl,\n");
		
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  ches,\n");
		
		
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq>="+yuec+" \n");
		sb.append(" and f2.daohrq<="+riq+"    and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  qnet_ar,\n");
		
		sb.append(" nvl('当日累计','') qbar,\n");
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mt,\n");
		
		sb.append(" (select sum(round_new(f2.laimsl,"+visit.getShuldec()+")) from fahb f2,vwdianc d where f2.daohrq="+riq+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  mad,\n");
		
		sb.append(" nvl('当日车数累计','') aar,\n");
		sb.append("  nvl('当日车数累计','') ad,\n");
		sb.append(" (select sum(round_new(f2.ches,0)) from fahb f2,vwdianc d where f2.daohrq>="+riq+" \n");
		sb.append("     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1 "+fahgl+diancid+")  aad,\n");
		
		
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		sb.append(" nvl('当日汽车加权热值','') vad,\n");
		
		sb.append(" (select decode(sum(round_new(f2.laimsl,"+visit.getShuldec()+")), 0, 0,\n");
		sb.append(" round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(f2.laimsl,"+visit.getShuldec()+")) / sum(round_new(f2.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816,  0)) as qbar\n");
		
		sb.append("  from fahb f2,vwdianc d,zhilb z where \n");
		sb.append(" f2.daohrq="+riq+" \n");
	
		sb.append(" and f2.zhilb_id=z.id     and f2.diancxxb_id=d.id and ( select count(distinct yunsdwb_id) from chepb c where c.fahb_id=f2.id "+yunsdw+")=1  "+fahgl+diancid+") qgrd \n");
		sb.append(" from dual\n");
		return sb;
	}
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb="";
		sb=visit.getDiancqc()+"进煤日报表";
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
			
			Visit visit=(Visit)this.getPage().getVisit();
			StringBuffer bf=new StringBuffer();
			
			bf.append(" select distinct * from ( \n");
			bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
			bf.append(" union\n");
			bf.append(" select id ,mingc,1 jib from yunsdwb");
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
			String sql = "select 0 id,'全部' mingc from dual union select id,mingc  from yunsdwb";
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
		
		
		//煤矿
		
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "gongysTree", visit.getDiancxxb_id()+"", "forms[0]", this.getTreeid(), this.getTreeid());
		visit.setDefaultTree(gystree);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("煤矿:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		
		//运输单位--------------------
		
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
		
		tb1.addText(new ToolbarText("-"));
	//-------------------------------------------------------	
		
		
//		电厂Tree
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
		
	

		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql="";

		sql=this.getBaseSql().toString();

//		System.out.println(sql);
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
	
        	rs = rstmp;
        	 ArrHeader = new String[][] {{"煤矿","运输单位","数量验收(吨)","车数(车)","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果",
        		 "化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果","化验质量验收结果",
        		 "化验质量验收结果"},{"煤矿","运输单位","当日","当日","Qnet","大卡","Mt","Mad","Aar","Ad","Aad","Vad","Vdaf","Stad","Qgrd"}};
    
    		 ArrWidth = new int[] {120,  120, 100,  70, 50, 70, 50, 50, 50, 50 , 50, 50, 50, 50, 50};
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
//    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		String dcmc="";
    		
    		try{
    			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id="+this.getTreeid_dc());
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		rt.setDefaultTitle(1,3, "单位：" +dcmc ,
    				Table.ALIGN_LEFT);
    		rt.setDefaultTitle(5, 3, getBRiq() ,
    				Table.ALIGN_CENTER);
    		
    		strFormat = new String[] { "", "", "", "", "", "", "","","","","",
    				"", "", "" };
//    		rt.setTitle(getRptTitle(), ArrWidth);
        
		

		rt.setBody(new Table(rs, 2, 0, 3));
		
//		System.out.println(this.getTJByYunsfsSql().toString()+"\n"+"**********");
//		System.out.println(this.getTJSql().toString());
		
		ResultSetList rstj=con.getResultSetList(this.getTJByYunsfsSql().toString());
		rt.body.AddTableData(rstj);
		ResultSetList rsl=con.getResultSetList(this.getTJSql().toString());
		rt.body.AddTableData(rsl);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
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
		rt.body.setPageRows(40);
		rt.body.merge(1, 1,2 ,15);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();
		
		rs.beforefirst();
		int i=2;
		while(rs.next()){
			
			if(rs.getString("MK").equals("火车") || rs.getString("MK").equals("汽车")){
				
				//rt.body.rows[i+1].className="th";
				
				for(int j=1;j<ArrWidth.length+1;j++){
					rt.body.getCell(i+1, j).foreColor="blue";
					rt.body.getCell(i+1, j).fontBold=true;
				}
//				System.out.println(i);
				rt.body.getCell(i+1, 1).used=true;
				rt.body.getCell(i+1, 2).used=true;
				
				rt.body.merge(i+1, 1,i+1, 2);
			}
			
			
			i++;
		}

		
		rstj.beforefirst();
		while(rstj.next()){
			
			for(int j=1;j<ArrWidth.length+1;j++){
				rt.body.getCell(i+1, j).foreColor="blue";
				rt.body.getCell(i+1, j).fontBold=true;
			}
			
			
			rt.body.merge(i+1, 1,i+1 , 15);
			
			
			for(int j=1;j<16;j++){
				rt.body.getCell(i+1, j).setBorder(0, 0, 0, 0);
			}
			i++;
		}
		
		rsl.beforefirst();
		while(rsl.next()){
			
			for(int j=1;j<ArrWidth.length+1;j++){
				rt.body.getCell(i+1, j).foreColor="blue";
				rt.body.getCell(i+1, j).fontBold=true;
			}
			
			rt.body.merge(i+1, 1,i+1 , 15);
			
			for(int j=1;j<16;j++){
				rt.body.getCell(i+1, j).setBorder(0, 0, 0, 0);
			}
			i++;
		}
		
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 2, "煤检主管：" ,
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "采样：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "制样：", Table.ALIGN_LEFT);
		
		rt.setDefautlFooter(10, 2, "司磅：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "化验主管:", Table.ALIGN_LEFT);
		
		rt.setDefautlFooter(15, 2, "化验：", Table.ALIGN_LEFT);
		
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
	//	RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSqlStr().toString(),rt,getRptTitle(),""+BAOBPZB_GUANJZ+v.getInt1());
     	return rt.getAllPagesHtml();// ph;
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
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
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
		return getToolbar().getRenderScript();
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
			
			getGongysDropDownModels();
			this.getMeikModels();
//			this.setGongysDropDownModel(null);
//			this.setMeikModel(null);
			this.setGesModel(null);
			
		
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
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
			
			
			getSelectData();
			
			
		}
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
