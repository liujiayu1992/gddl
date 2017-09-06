package com.zhiren.shanxdted.diqmkext;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-11-04
 * 内容:复合煤矿  简直台帐
 */
public class Fuhmkjztz extends BasePage implements PageValidateListener {

	
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
	private boolean briqboo=false;
	private String briq;

	public String getBRiq() {
		if (briq==null && "".equals(briq)){
			briq = DateUtil.FormatDate(new Date());
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
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		String briq ="";
		briq = " and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq());
	
		String eriq="";
		eriq="  and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
		
//		运输方式
		String yunsfsStr="  ";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr="  and f.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select  m.id ,m.mingc,1 jib from meikxxb m,fahb f,diancxxb d where f.diancxxb_id=d.id and m.id=f.meikxxb_id "+briq+eriq+yunsfsStr+" and (d.id="+this.getTreeid_dc()+" or d.fuid="+this.getTreeid_dc()+")");
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
    	
    	String sql="select id,mingc from diqmkgroup  ";
    	
    	sql+="  union select 0 id, '全部' mingc from dual ";
    	
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
		String sql="select id,mingc from diancxxb where fuid = " + id+" or id="+id+"  order by id asc";
		ResultSetList rsl=con.getResultSetList(sql);
		
		return rsl;
	}
    
//	获取相关的SQL

	public StringBuffer getBaseSql(String dcid,int dcrow) {
    	Visit visit = (Visit) this.getPage().getVisit();

	
		String meik="";
		String fahgl="";
		String diancid = " and d.id="+dcid ;

		
		if(dcrow>1 && dcid.equals(this.getTreeid_dc())){
			diancid = " and d.fgsid="+dcid ;
		}
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and zh.diqmkgroup_id="+this.getTreeid();
			meik="  and m.id in ( select meikxxb_id from diqmkzh where diqmkgroup_id="+this.getTreeid()+" ) ";
		}
//		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
//			if(hasDianc(getTreeid_dc())){
//				diancid = " and d.fgsid = " + getTreeid_dc();
//			} else {
//				diancid = " and d.id ="+ getTreeid_dc();
//			}
//			
//		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and cp.yunsdwb_id="+this.getMeikid();
		}
		
		String briq ="";
		briq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		String eriq="";
		eriq=""+DateUtil.FormatOracleDate(getERiq());
		
		
//		运输方式
		String yunsfsStr="";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr=" and y.id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		StringBuffer sb = new StringBuffer();
		
		

		
		sb.append(" select \n");
		sb.append("  decode(rownum || m.mingc || fh.ysdw, null, decode(y.mingc,'铁路','火车','公路','汽车'), rownum) xuh,\n");
//		sb.append(" decode(y.mingc,'铁路','火车','公路','汽车') ysfs,\n");
		sb.append(" decode(fh.id,null,decode(rownum || m.mingc || fh.ysdw,null, decode(y.mingc, '铁路', '火车', '公路', '汽车'),''),to_char(fh.daohrq,'yyyy-MM-dd')) ysfs,\n");
		sb.append("  decode(rownum || m.mingc || fh.ysdw, null, decode(y.mingc,'铁路','火车','公路','汽车'), m.mingc) mk,\n");
		sb.append("  decode(rownum || m.mingc || fh.ysdw, null, decode(y.mingc,'铁路','火车','公路','汽车'), fh.ysdw) ysdw,\n");
		
		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		sb.append(" sum(round_new(fh.ches,0)) ches,\n");
	
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'' as qnet_ar,\n");
		
//		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
//		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816, 0)) as qbar,\n");
//		
		sb.append(" round_new(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))* 1000 / 4.1816,0)||'' as qbar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")) as mt,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'' as mad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as ad,\n");
		
		
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vdaf,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as stad,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.std * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as qgrd\n");
		
		
		sb.append("  from\n");
		
		sb.append(" ( select * from ( select f.* , \n");
		sb.append(" (select (select ys.mingc from yunsdwb ys where ys.id=c.yunsdwb_id )\n");
		sb.append("  from chepb c where c.fahb_id=f.id and rownum=1) ysdw\n");
		sb.append("  from (SELECT f.* FROM fahb f, diqmkzh zh WHERE ");
		sb.append("f.daohrq BETWEEN  zh.kaissj AND zh.jiessj ");
		sb.append("AND f.meikxxb_id=zh.meikxxb_id " + fahgl + ") f"); 
		sb.append(		" where f.daohrq>="+briq+"\n");
		sb.append(" and  f.daohrq<="+eriq +"\n");
		sb.append(" )    )fh ,zhilb z,meikxxb m,yunsfsb y,vwdianc d \n");
		sb.append(" where fh.diancxxb_id=d.id "+diancid+"and fh.zhilb_id=z.id(+) and fh.meikxxb_id=m.id "+meik+" and fh.yunsfsb_id=y.id "+yunsfsStr+" "+this.getRZTJ()+"\n");
		
		sb.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fh.id"+yunsdw+" )=1\n");
		
		sb.append(" group by grouping sets('1',(fh.id,fh.daohrq,y.mingc,m.mingc,fh.ysdw,rownum),(y.mingc,m.mingc,fh.ysdw),(y.mingc))\n");
		sb.append(" having grouping(fh.id)+grouping(y.mingc)+grouping(m.mingc)+grouping(fh.ysdw)!=4\n");
		sb.append(" order by y.mingc desc,m.mingc asc,fh.ysdw asc,grouping(fh.id) asc,fh.daohrq asc\n");
		
		return sb;
    }
	
	
	private StringBuffer getTJByYunsfsSql(String dcid,int dcrow){
    	Visit visit = (Visit) this.getPage().getVisit();

	
		String meik="";
		String fahgl="";
		
		
		String diancid = " and d.id="+dcid ;
		
		if(dcrow>1 && dcid.equals(this.getTreeid_dc())){
			diancid = " and d.fgsid="+dcid ;
		}
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and zh.diqmkgroup_id="+this.getTreeid();
			meik="  and m.id in ( select meikxxb_id from diqmkzh where diqmkgroup_id="+this.getTreeid()+" ) ";
		}
//		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
//			if(hasDianc(getTreeid_dc())){
//				diancid = " and d.fgsid = " + getTreeid_dc();
//			} else {
//				diancid = " and d.id ="+ getTreeid_dc();
//			}
//			
//		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and cp.yunsdwb_id="+this.getMeikid();
		}
		
		String briq ="";
		briq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		String eriq="";
		eriq=""+DateUtil.FormatOracleDate(getERiq());
		
//		运输方式
		String yunsfsStr="";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr=" and y.id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		
		
		StringBuffer sb = new StringBuffer();
		
		

		
		sb.append(" select \n");
		sb.append("   decode(grouping(m.mingc)+grouping(fh.ysdw),2, decode(y.mingc,'铁路','火车','公路','汽车'),'xuh') xuh,\n");
//		sb.append("  decode(y.mingc,'铁路','火车','公路','汽车') ysfs,\n");
		sb.append("  decode(grouping(m.mingc)+grouping(fh.ysdw),2, decode(y.mingc,'铁路','火车','公路','汽车'),m.mingc) mk,\n");
		sb.append("  decode(grouping(m.mingc)+grouping(fh.ysdw),2, decode(y.mingc,'铁路','火车','公路','汽车'),fh.ysdw) ysdw,\n");
	
		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		
		sb.append(" sum(round_new(fh.ches,0)) ches,\n");
	
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'' as qnet_ar,\n");
		
//		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
//		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816, 0)) as qbar,\n");
		
		sb.append(" round_new(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))*1000/4.1816,0)||'' as qbar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")) as mt,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'' as mad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as ad,\n");
		
		
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vdaf,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as stad,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.std * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as qgrd\n");
		
		
		sb.append("  from\n");
		
		sb.append(" ( select * from ( select f.* , \n");
		sb.append(" (select (select ys.mingc from yunsdwb ys where ys.id=c.yunsdwb_id )\n");
		sb.append("  from chepb c where c.fahb_id=f.id and rownum=1) ysdw\n");
		sb.append("  from (SELECT f.* FROM fahb f, diqmkzh zh WHERE ");
		sb.append("f.daohrq BETWEEN  zh.kaissj AND zh.jiessj ");
		sb.append("AND f.meikxxb_id=zh.meikxxb_id " + fahgl + ") f"); 
		sb.append(		" where f.daohrq>="+briq+"\n");
		sb.append(" and  f.daohrq<="+eriq +"\n");
		sb.append(" )    )fh ,zhilb z,meikxxb m,yunsfsb y,vwdianc d \n");
		sb.append(" where fh.diancxxb_id=d.id "+diancid+"and fh.zhilb_id=z.id(+) and fh.meikxxb_id=m.id "+meik+" and fh.yunsfsb_id=y.id "+yunsfsStr+" "+this.getRZTJ()+"\n");
		
		sb.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fh.id"+yunsdw+" )=1\n");
		
		sb.append(" group by grouping sets('1',(y.mingc,m.mingc,fh.ysdw),(y.mingc))\n");
		sb.append("  having grouping(y.mingc)!=1\n");
		sb.append("  order by y.mingc desc,m.mingc asc,fh.ysdw asc\n");
		
		
		
		return sb;
		
		
    }
	
	private String getRZTJ(){
		
		this.setQnetarS_value(this.getQnetarS());
		this.setQnetarX_value(this.getQnetarX());
		
		
		String s="";
		if(this.getQnetarS()!=null && !this.getQnetarS().equals("") && !this.getQnetarS().equals("0")){
			s+=" and z.qnet_ar<="+this.getQnetarS_value();
		}
		if(this.getQnetarX()!=null && !this.getQnetarX().equals("") && !this.getQnetarX().equals("0")){
			s+=" and z.qnet_ar>="+this.getQnetarX_value();
		}
		if(this.getMtS()!=null && !this.getMtS().equals("") && !this.getMtS().equals("0")){
			s+=" and z.mt<="+this.getMtS();
		}
		if(this.getMtX()!=null && !this.getMtX().equals("") && !this.getMtX().equals("0")){
			s+=" and z.mt>="+this.getMtX();
		}
		if(this.getAarS()!=null && !this.getAarS().equals("") && !this.getAarS().equals("0")){
			s+=" and z.aar<="+this.getAarS();
		}
		if(this.getAarX()!=null && !this.getAarX().equals("") && !this.getAarX().equals("0")){
			s+=" and z.aar>="+this.getAarX();
		}
		if(this.getAdS()!=null && !this.getAdS().equals("") && !this.getAdS().equals("0")){
			s+=" and z.ad<="+this.getAdS();
		}
		if(this.getAdX()!=null && !this.getAdX().equals("") && !this.getAdX().equals("0")){
			s+=" and z.ad>="+this.getAdX();
		}
		if(this.getVdafS()!=null && !this.getVdafS().equals("") && !this.getVdafS().equals("0")){
			s+=" and z.vdaf<="+this.getVdafS();
		}
		if(this.getVdafX()!=null && !this.getVdafX().equals("") && !this.getVdafX().equals("0")){
			s+=" and z.vdaf>="+this.getVdafX();
		}
		if(this.getMadS()!=null && !this.getMadS().equals("") && !this.getMadS().equals("0")){
			s+=" and z.mad<="+this.getMadS();
		}
		if(this.getMadX()!=null && !this.getMadX().equals("") && !this.getMadX().equals("0")){
			s+=" and z.mad>="+this.getMadX();
		}
		if(this.getStdS()!=null && !this.getStdS().equals("") && !this.getStdS().equals("0")){
			s+=" and z.std<="+this.getStdS();
		}
		if(this.getStdX()!=null && !this.getStdX().equals("") && !this.getStdX().equals("0")){
			s+=" and z.std>="+this.getStdX();
		}
		if(this.getStadS()!=null && !this.getStadS().equals("") && !this.getStadS().equals("0")){
			s+=" and z.stad<="+this.getStadS();
		}
		if(this.getStadX()!=null && !this.getStadX().equals("") && !this.getStadX().equals("0")){
			s+=" and z.stad>="+this.getStadX();
		}
		
		return s;
	}
	
	private StringBuffer getTJSql(String dcid,int dcrow){
    	Visit visit = (Visit) this.getPage().getVisit();

	
		String meik="";
		String fahgl="";
		
		String diancid = " and d.id="+dcid ;
		if(dcrow>1 && dcid.equals(this.getTreeid_dc())){
			diancid="  and d.fgsid ="+dcid;
		}
		
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			fahgl=" and dz.diqmkgroup_id="+this.getTreeid();
			meik="  and m.id in ( select meikxxb_id from diqmkzh where diqmkgroup_id="+this.getTreeid()+" ) ";
		}
//		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
//			if(hasDianc(getTreeid_dc())){
//				diancid = " and d.fgsid = " + getTreeid_dc();
//			} else {
//				diancid = " and d.id ="+ getTreeid_dc();
//			}
//			
//		}
		
		String yunsdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			yunsdw=" and cp.yunsdwb_id="+this.getMeikid();
		}
		
		String briq ="";
		briq = ""+DateUtil.FormatOracleDate(getBRiq());
	
		String eriq="";
		eriq=""+DateUtil.FormatOracleDate(getERiq());
		
		
//		运输方式
		String yunsfsStr="";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr=" and y.id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		StringBuffer sb = new StringBuffer();
		
		

		
		sb.append(" select \n");
		sb.append("  nvl('加权平均值','')  xuh,\n");
		
		if(this.getGesValue().getStrId().equals("2")){
			sb.append("  nvl('加权平均值','') ysfs,\n");
		}
		
		sb.append("  nvl('加权平均值','') mk,\n");
		sb.append("  nvl('加权平均值','')　ysdw,\n");
		
		sb.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		sb.append(" sum(round_new(fh.ches,0)) ches,\n");
	
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'MJ/kg' as qnet_ar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0,  round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))||'MJ/kg' as qbar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")) as mt,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'' as mad,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aar,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as ad,\n");
		
		
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vdaf,\n");
		
		sb.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as stad,\n");
		
		sb.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.std * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as qgrd\n");
		
		
		sb.append("  from\n");
		
		sb.append(" ( select * from ( select f.* , \n");
		sb.append(" (select (select ys.mingc from yunsdwb ys where ys.id=c.yunsdwb_id )\n");
		sb.append("  from chepb c where c.fahb_id=f.id and rownum=1) ysdw\n");
		sb.append("  from fahb f,diqmkzh dz where f.daohrq>="+briq+"\n");
		sb.append(" and  f.daohrq<="+eriq+" "+" and f.meikxxb_id=dz.meikxxb_id and f.daohrq>=dz.kaissj and f.daohrq<=dz.jiessj "+fahgl+"\n");
		sb.append(" )    )fh ,zhilb z,meikxxb m,yunsfsb y,vwdianc d \n");
		sb.append(" where fh.diancxxb_id=d.id "+diancid+"and fh.zhilb_id=z.id(+) and fh.meikxxb_id=m.id "+meik+" and fh.yunsfsb_id=y.id "+yunsfsStr+" "+this.getRZTJ()+"\n");
		
		sb.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fh.id"+yunsdw+" )=1\n");
		
		sb.append("  group by grouping sets('1',(y.mingc,m.mingc,fh.ysdw))\n");
		sb.append(" having grouping(y.mingc)=1\n");
	
		
		StringBuffer sb1=new StringBuffer();
		sb1.append(" select \n");
		sb1.append("  nvl('加权平均值','')  xuh,\n");
		
		if(this.getGesValue().getStrId().equals("2")){
			sb1.append("  nvl('加权平均值','') ysfs,\n");
		}
		sb1.append("  nvl('加权平均值','') mk,\n");
		sb1.append("  nvl('加权平均值','')　ysdw,\n");
		
		sb1.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		sb1.append(" sum(round_new(fh.ches,0)) ches,\n");
	
		
//		sb1.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
//		sb1.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816, 0))||'Cal/g' as qnet_ar,\n");
		
		sb1.append("  round_new(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
		sb1.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))* 1000 / 4.1816,0)||'Cal/g' as qnet_ar,\n");
		
		
//		sb1.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
//		sb1.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+"))* 1000 / 4.1816, 0))||'Cal/g' as qbar,\n");
//		

		sb1.append("  round_new(decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(round_new(z.qnet_ar,"+visit.getFarldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) /\n");
		sb1.append("  sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getFarldec()+"))* 1000 / 4.1816,0)||'Cal/g' as qbar,\n");
		
		
		sb1.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(z.mt,"+visit.getMtdec()+") * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getMtdec()+")) as mt,\n");
		
		sb1.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0,  round_new(sum(z.mad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+"))||'' as mad,\n");
		
		sb1.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.aar * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as aar,\n");
		
		sb1.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.ad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as ad,\n");
		
		
		
		sb1.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.vdaf * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as vdaf,\n");
		
		sb1.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")),0,0, round_new(sum(z.stad * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as stad,\n");
		
		sb1.append(" decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(z.std * round_new(fh.laimsl,"+visit.getShuldec()+")) / sum(round_new(fh.laimsl,"+visit.getShuldec()+")), "+visit.getShuldec()+")) as qgrd\n");
		
		
		sb1.append("  from\n");
		
		sb1.append(" ( select * from ( select f.* , \n");
		sb1.append(" (select (select ys.mingc from yunsdwb ys where ys.id=c.yunsdwb_id )\n");
		sb1.append("  from chepb c where c.fahb_id=f.id and rownum=1) ysdw\n");
		sb1.append("  from fahb f,diqmkzh dz where f.daohrq>="+briq+"\n");
		sb1.append(" and  f.daohrq<="+eriq+" "+" and f.meikxxb_id=dz.meikxxb_id and f.daohrq>=dz.kaissj and f.daohrq<=dz.jiessj "+fahgl+"\n");
		sb1.append(" )    )fh ,zhilb z,meikxxb m,yunsfsb y,vwdianc d \n");
		sb1.append(" where fh.diancxxb_id=d.id "+diancid+"and fh.zhilb_id=z.id(+) and fh.meikxxb_id=m.id "+meik+" and fh.yunsfsb_id=y.id "+yunsfsStr+" "+this.getRZTJ()+"\n");
		
		sb1.append(" and (select count(distinct cp.yunsdwb_id) from chepb  cp where cp.fahb_id=fh.id"+yunsdw+" )=1\n");
		
		sb1.append("  group by grouping sets('1',(y.mingc,m.mingc,fh.ysdw))\n");
		sb1.append(" having grouping(y.mingc)=1\n");
	
		
		sb.append(" union \n");
		sb.append(sb1.toString());
		
		
		return sb;
    }
	
	
	
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb="";
		sb="入厂煤质量验收台帐";
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
			briq = "  and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq());
		
			String eriq="";
			eriq="  and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
			
			
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
			bf.append(" select y.id,y.mingc,1 jib  from yunsdwb y where y.id in ( select c.yunsdwb_id from chepb c where c.fahb_id in (select f.id from fahb f,diancxxb d where f.diancxxb_id=d.id "+briq+eriq+yunsfsStr+" and (d.id="+this.getTreeid_dc()+" or d.fuid="+this.getTreeid_dc()+")   ))");
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
			briq = "  f.daohrq>="+DateUtil.FormatOracleDate(getBRiq());
		
			String eriq="";
			eriq="  and f.daohrq<="+DateUtil.FormatOracleDate(getERiq());
			
			
			String sql = "select 0 id,'全部' mingc from dual union select y.id,y.mingc  from yunsdwb y where y.id in ( select c.yunsdwb_id from chepb c where c.fahb_id in (select f.id from fahb f where "+briq+eriq+"  ))";
			setMeikModel(new IDropDownModel(sql));
		}
		
		//-------------------------------------------------
//		绑定日期
		private boolean eriqboo=false;
		private String eriq;

		public String getERiq() {
			if (eriq==null && "".equals(eriq)){
				eriq = DateUtil.FormatDate(new Date());
			}
			return eriq;
		}

		public void setERiq(String eriq) {
			if(this.eriq!=null && !this.eriq.equals(eriq)){
				eriqboo=true;
			}
			this.eriq = eriq;
		}
		
		
		
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
//		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
	
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		
		tb1.addText(new ToolbarText("-"));
		
		
		
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
		
		
		
		//煤矿
		
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getGYSSql().toString(), "gongysTree", visit.getDiancxxb_id()+"", "forms[0]", this.getTreeid(), this.getTreeid());
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
		
		

		
		
		tb1.addText(new ToolbarText("格式:"));
		ComboBox ges = new ComboBox();
		ges.setTransform("GesSelect");
		ges.setWidth(60);
//		ges.setListeners("select:function(){document.forms[0].submit();}");
		tb1.addField(ges);
		
		tb1.addText(new ToolbarText("-"));

//		
//		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
//		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.addItem(rbtn);
		
		
		ToolbarButton tiaoj = new ToolbarButton(null,"条件","function(){if(win){ win.show(this);}}");
		//tiaoj.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(tiaoj);
		
		
		
//		tb1.addFill();
//		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		
		
		 Toolbar t2 = new Toolbar("tbdiv");
		 t2.addText(new ToolbarText("运输方式:"));
		 ComboBox meik = new ComboBox();
		 meik.setTransform("YUNSFSSelect");
		 meik.setWidth(80);
		 t2.addField(meik);	
				
			
		 t2.addText(new ToolbarText("-"));
			
		 ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		 rbtn.setIcon(SysConstant.Btn_Icon_Search);
		 t2.addItem(rbtn);
			
		 t2.setWidth("bodyWidth");
		 t2.addFill();
			
			
		setToolbar(tb1);
		setToolbar2(t2);
	}
//	获取供应商
	
	private StringBuffer getGYSSql(){
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from diqmkgroup");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
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
	
	public String getFormPanelStr(){
		String s="";
		
		s+=" var form=new Ext.form.FormPanel({\n" +
				"labelAlign:'left',buttonAlign:'right', frame:true,labelWidth:50,monitorValid:true,\n" +
				" items:[" +
				
				"{\n" +
				" layout:'column',border:false, \n" +
				
				" items:[\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Qnet,ar(卡)',autoHeight:true,defaultType:'numberfield',\n" +
				"items:[\n" +
				" qnetarS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getQnetarS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }   }),\n" +
				" qnetarX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getQnetarX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"},\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Mt',autoHeight:true,defaultType:'numberfield',style:'margin-left:20px',\n" +
				"items:[\n" +
				" mtS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getMtS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" mtX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getMtX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"}\n" +
				
				"]\n" +
				"},\n" +
				
				
				
				
				"{\n" +
				" layout:'column',border:false, \n" +
				
				" items:[\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Mad',autoHeight:true,defaultType:'numberfield',\n" +
				"items:[\n" +
				" madS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getMadS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" madX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getMadX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"},\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Aar',autoHeight:true,defaultType:'numberfield',style:'margin-left:20px',\n" +
				"items:[\n" +
				" aarS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getAarS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" aarX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getAarX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"}\n" +
				
				"]\n" +
				"},\n" +
				
				
				
				"{\n" +
				" layout:'column',border:false, \n" +
				
				" items:[\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Ad',autoHeight:true,defaultType:'numberfield',\n" +
				"items:[\n" +
				" adS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getAdS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" adX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getAdX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"},\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Vdaf',autoHeight:true,defaultType:'numberfield',style:'margin-left:20px',\n" +
				"items:[\n" +
				" vdafS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getVdafS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" vdafX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getVdafX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"}\n" +
				
				"]\n" +
				"},\n" +
				
				
				
				"{\n" +
				" layout:'column',border:false, \n" +
				
				" items:[\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Stad',autoHeight:true,defaultType:'numberfield',\n" +
				"items:[\n" +
				" stadS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getStadS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" stadX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getStadX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"},\n" +
				
				"{\n" +
				"layout: 'form',border:false,columnWidth:.5,xtype:'fieldset',title:'Std',autoHeight:true,defaultType:'numberfield',style:'margin-left:20px',\n" +
				"items:[\n" +
				" stdS=new Ext.form.NumberField({fieldLabel:'上限',value:'"+this.getStdS()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }}),\n" +
				" stdX=new Ext.form.NumberField({fieldLabel:'下限',value:'"+this.getStdX()+"',emptyText:'0',listeners:{change :function(field,newValue,oldValue){ if(newValue==null || newValue==''){ newValue='0'; field.value='0';}   }  }})\n" +
				"]\n" +
				"}\n" +
				
				"]\n" +
				"}\n" +
				
				
				"" +
				"" +
				"" +
				"" +
				"]\n" +
				
				
				
//				"buttons:[{\n" +
//				"text:'确认',\n" +
//				"handler:function(){}\n" +
//				"},\n" +
//				"{" +
//				"text:'取消',\n" +
//				"handler:function(){}\n" +
//				"}\n" +
//				"]\n" +

				"" +
				"" +
				"});\n";
		
		
		String s2= " win = new Ext.Window({\n"
		+ " el:'hello-win',\n"
		+ "layout:'fit',\n"
		+ "width:500,\n"
		+ "height:500,\n"
		+ "closeAction:'hide',\n"
		+ "plain: true,\n"
		+ "title:'条件(%)',\n"
		+ "modal:true,"
		+ "items: [form],\n"
        				
		+ "buttons: [{\n"
		+ "   text:'确定',\n"
		+ "   handler:function(){  \n"                  		
		+ "  	win.hide();\n"
		
		+" document.all.AarS.value=aarS.value;\n" +
				"document.all.AarX.value=aarX.value;\n" +
				"document.all.AdS.value=adS.value;\n" +
				"document.all.AdX.value=adX.value;\n" +
				"document.all.QnetarS.value=qnetarS.value;\n" +
				"document.all.QnetarX.value=qnetarX.value;\n" +
				"document.all.MadS.value=madS.value;\n" +
				"document.all.MadX.value=madX.value;\n" +
				"document.all.MtS.value=mtS.value;\n" +
				"document.all.MtX.value=mtX.value;\n" +
				"document.all.StadS.value=stadS.value;\n" +
				"document.all.StadX.value=stadX.value;\n" +
				"document.all.StdS.value=stdS.value;\n" +
				"document.all.StdX.value=stdX.value;\n" +
				"document.all.VdafS.value=vdafS.value;\n" +
				"document.all.VdafX.value=vdafX.value;\n" +
//				"document.getElementById('RefurbishButton').click();\n" 
				
					
		
		 "  	}   \n"                
		+ "},{\n"
		+ "   text: '取消',\n"
		+ "   handler: function(){\n"
		+ "       win.hide();\n"
		+ "   }\n"
		+ "}]\n"
       
		+ " });win.show();win.hide();";
    
		return s+s2;
	}
	
	

	private String getPrintMx(String diancid,int dcrow){//明细

		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql=this.getBaseSql(diancid,dcrow).toString();
	
		

//		System.out.println(sql);
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
	
        	rs = rstmp;
        	 ArrHeader = new String[][] {{"序号","到货日期","煤矿","运输单位","来煤量","车数","Qnet,ar<br>(MJ/KJ 大卡)","Qnet,ar<br>(MJ/KJ 大卡)",
        		 "Mt<br>(%)","Mad<br>(%)","Aar<br>(%)","Ad<br>(%)","Vdaf<br>(%)","Stad<br>(%)","Std<br>(%)"},};
    
    		 ArrWidth = new int[] {40,  90, 140,  120, 70, 50, 60, 60, 60, 60, 60 , 60, 60, 60, 60};
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=12;
    		rt.title.setRowHeight(2, 50);
//    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		String dcmc="";
    		
    		try{
    			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id="+diancid);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		rt.setDefaultTitle(1,4, "单位:" +dcmc ,
    				Table.ALIGN_LEFT);
    		rt.setDefaultTitle(9, rt.title.getCols()-8,  DateUtil.Formatdate("yyyy年MM月dd日", DateUtil.getDate(this.getBRiq()))+"至"+ DateUtil.Formatdate("yyyy年MM月dd日", DateUtil.getDate(this.getERiq())),
    				Table.ALIGN_RIGHT);
    		
    		strFormat = new String[] { "", "", "", "", "", "", "0.00","","0.00","0.00","0.00",
    				"0.00", "0.00", "0.00","0.00" };
//    		rt.setTitle(getRptTitle(), ArrWidth);
        
		

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setFontSize(12);

		ResultSetList rsl=con.getResultSetList(this.getTJSql(diancid,dcrow).toString());
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
		
		if(dcrow<=1){
			rt.body.setPageRows(22);
		}
		
//		rt.body.setPageRows(40);
//		rt.body.setPageRows(-1);
		rt.body.merge(1, 1,1 ,14);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();
		
		rs.beforefirst();
		int i=1;
		int count=1;//序号设置
		while(rs.next()){
			
			if(rs.getString("XUH")!=null && !rs.getString("XUH").equals("") &&  !rs.getString("XUH").equals("火车") && !rs.getString("XUH").equals("汽车")){
				
				rt.body.setCellValue(i+1, 1, count+"");
				count++;
			}
			if(rs.getString("XUH").equals("火车") || rs.getString("XUH").equals("汽车")){
				
				
				for(int j=1;j<ArrWidth.length+1;j++){
//					rt.body.getCell(i+1, j).foreColor="blue";
					rt.body.getCell(i+1, j).fontBold=true;
					
				}
//				System.out.println(i);
				rt.body.getCell(i+1, 1).used=true;
				rt.body.getCell(i+1, 2).used=true;
				rt.body.getCell(i+1, 3).used=true;
				rt.body.getCell(i+1, 4).used=true;
				
				rt.body.merge(i+1, 1,i+1, 4);
			}
			
			
			i++;
		}


		int constFlag=i;
		rsl.beforefirst();
		while(rsl.next()){
			
			for(int j=1;j<ArrWidth.length+1;j++){
//				rt.body.getCell(i+1, j).foreColor="blue";
				rt.body.getCell(i+1, j).fontBold=true;
			}
			
			
			i++;
		}
		
		
		rs.beforefirst();
		if(rs.next()){
			rt.body.merge(constFlag+1, 1,constFlag+2 , 15);
//			rt.body.merge(constFlag+1, 1,constFlag+2 , 5);
		}
		
		if (rt.body.getRows()>2){
			rt.body.setCellAlign(rt.body.getRows()-1, 7, Table.ALIGN_CENTER);
			rt.body.setCellAlign(rt.body.getRows(), 7, Table.ALIGN_CENTER);
		}
		
		for(int j=1;j<rt.body.getRows()-1;j++){
			for(int k=1;k<5;k++){
				String strValue="<div nowrap  style=\"width:"+ArrWidth[k-1]+";overflow:hidden; \">"+rt.body.getCell(j, k).value+"</div>";
				rt.body.setCellValue(j, k, strValue);
			}
		}
		
		
		
	
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 2, "煤检主管：" ,
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "采样：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "制样：", Table.ALIGN_LEFT);
		
		rt.setDefautlFooter(10, 2, "司磅：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "化验:", Table.ALIGN_LEFT);
		
//		rt.setDefautlFooter(15, 2, "化验：", Table.ALIGN_LEFT);
		
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=12;
//		rt.footer.setRowHeight(1, 1);
//		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(25);
		
		
//		int pages= (rt.body.getRows()-rt.body.getFixedRows()+rt.footer.getRows()-rt.footer.getFixedRows())*21%1120;
//		
//		if(pages!=0){
//			
//			if(pages%2==0){
//				pages/=2;
//			}else{
//				pages/=2+1;
//			}
//		}
//		sql="";
//		
//		
//		for(int m=0;m<pages;m++){
//			sql=" select '','','','','','','','','','','','','','','' from dual \n";
//			
//			if(m!=pages-1){
//				sql=" union \n";
//			}
//		}
//		
//		int foot_rows=rt.footer.getRows()-rt.footer.getFixedRows();
//		if(pages>0){
//			rt.footer.AddTableData(con.getResultSetList(sql));
//			
//			
//			for(int k=1;k<=1+foot_rows;k++){
//				rt.footer.merge(k, 1,k, 15);
//				
//				for(int j=1;j<15;j++){
//					rt.footer.getCell(k, j).setBorderNone();
//				}
//			}
//			
//		}
//		rt.body.setUseDefaultCss(true);
//		for(int k=0;k<rt.body.rows.length;k++){
//			Row row=rt.body.rows[k];
//			
//			if(row!=null && k%39==0){
//				row.className="\"row_page_end\"";
//			}
//			
//		}
		
		
		con.Close();
     	return rt.getAllPagesHtml();// ph;
	
	}
	
	private String getPrintJb(String diancid,int dcrow){
		JDBCcon con = new JDBCcon();
	
	Visit v = (Visit) getPage().getVisit();
	Report rt = new Report();
	
	
	String sql=this.getTJByYunsfsSql(diancid,dcrow).toString();

	

//	System.out.println(sql);
	ResultSetList rstmp = con.getResultSetList(sql);
	ResultSetList rs = null;
	String[][] ArrHeader=null;
	String[] strFormat=null;
	int[] ArrWidth=null;

    	rs = rstmp;
    	 ArrHeader = new String[][] {{"序号","煤矿","运输单位","来煤量","车数","Qnet,ar<br>(MJ/KJ 大卡)","Qnet,ar<br>(MJ/KJ 大卡)",
    		 "Mt<br>(%)","Mad<br>(%)","Aar<br>(%)","Ad<br>(%)","Vdaf<br>(%)","Stad<br>(%)","Std<br>(%)"},};

		 ArrWidth = new int[] {40, 140,  120, 70, 50, 60, 60, 60, 60, 60 , 60, 60, 60, 60};

		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize=12;
		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		String dcmc="";
		
		try{
			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id="+diancid);
		}catch(Exception e){
			e.printStackTrace();
		}
		rt.setDefaultTitle(1,4, "单位:" +dcmc ,
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, rt.title.getCols()-7,  DateUtil.Formatdate("yyyy年MM月dd日", DateUtil.getDate(this.getBRiq()))+"至"+ DateUtil.Formatdate("yyyy年MM月dd日", DateUtil.getDate(this.getERiq())),
				Table.ALIGN_RIGHT);
		
		strFormat = new String[] { "", "", "", "", "", "0.00", "","0.00","0.00","0.00","0.00",
				"0.00", "0.00", "0.00" };
//		rt.setTitle(getRptTitle(), ArrWidth);
    
	

	rt.setBody(new Table(rs, 1, 0, 3));
	rt.body.setFontSize(12);

	ResultSetList rsl=con.getResultSetList(this.getTJSql(diancid,dcrow).toString());
	rt.body.AddTableData(rsl);
	

	
	
	rt.body.setColAlign(1, Table.ALIGN_CENTER);
	rt.body.setColAlign(2, Table.ALIGN_CENTER);
	rt.body.setColAlign(3, Table.ALIGN_CENTER);
	rt.body.setColAlign(4, Table.ALIGN_CENTER);
	rt.body.setColAlign(5, Table.ALIGN_CENTER);
	rt.body.setColAlign(6, Table.ALIGN_RIGHT);
	rt.body.setColAlign(7, Table.ALIGN_RIGHT);
	
	rt.body.setColAlign(8, Table.ALIGN_RIGHT);
	rt.body.setColAlign(9, Table.ALIGN_RIGHT);
	rt.body.setColAlign(10, Table.ALIGN_RIGHT);
	rt.body.setColAlign(11, Table.ALIGN_RIGHT);
	rt.body.setColAlign(12, Table.ALIGN_RIGHT);
	
	rt.body.setColAlign(13, Table.ALIGN_RIGHT);
	rt.body.setColAlign(14, Table.ALIGN_RIGHT);
	
	
	
	rt.body.setWidth(ArrWidth);
	rt.body.setHeaderData(ArrHeader);
	rt.body.setColFormat(strFormat);
	
	if(dcrow<=1){
		rt.body.setPageRows(22);
	}
//	rt.body.setPageRows(40);
//	rt.body.setPageRows(-1);
	rt.body.merge(1, 1,1 ,14);
//	rt.body.mergeFixedCols();
//	rt.body.mergeFixedRow();
	
	rs.beforefirst();
	int i=1;
	int count=1;//序号设置
	while(rs.next()){
		
		if(rs.getString("XUH")!=null && !rs.getString("XUH").equals("") &&  !rs.getString("XUH").equals("火车") && !rs.getString("XUH").equals("汽车")){
			
			rt.body.setCellValue(i+1, 1, count+"");
			count++;
		}
		if(rs.getString("XUH").equals("火车") || rs.getString("XUH").equals("汽车")){
			
			
			for(int j=1;j<ArrWidth.length+1;j++){
//				rt.body.getCell(i+1, j).foreColor="blue";
				rt.body.getCell(i+1, j).fontBold=true;
				
			}
//			System.out.println(i);
			rt.body.getCell(i+1, 1).used=true;
			rt.body.getCell(i+1, 2).used=true;
			rt.body.getCell(i+1, 3).used=true;
			rt.body.getCell(i+1, 4).used=true;
			
			rt.body.merge(i+1, 1,i+1, 4);
		}
		
		
		i++;
	}


	int constFlag=i;
	rsl.beforefirst();
	while(rsl.next()){
		
		for(int j=1;j<ArrWidth.length+1;j++){
//			rt.body.getCell(i+1, j).foreColor="blue";
			rt.body.getCell(i+1, j).fontBold=true;
		}
		
		
		i++;
	}
	
	
	rs.beforefirst();
	if(rs.next()){
		rt.body.merge(constFlag+1, 1,constFlag+2 , 14);
//		rt.body.merge(constFlag+1, 1,constFlag+2 , 5);
	}
	
	if (rt.body.getRows()>2) {
	rt.body.setCellAlign(rt.body.getRows()-1, 6, Table.ALIGN_CENTER);
	rt.body.setCellAlign(rt.body.getRows(), 6, Table.ALIGN_CENTER);
	}
	
	for(int j=1;j<rt.body.getRows()-1;j++){
		for(int k=1;k<4;k++){
			String strValue="<div nowrap  style=\"width:"+ArrWidth[k-1]+";overflow:hidden; \">"+rt.body.getCell(j, k).value+"</div>";
			rt.body.setCellValue(j, k, strValue);
		}
	}
	
	
	rt.createFooter(1,ArrWidth);
	rt.setDefautlFooter(1, 2, "煤检主管：" ,
			Table.ALIGN_LEFT);
	rt.setDefautlFooter(4, 2, "采样：", Table.ALIGN_LEFT);
	rt.setDefautlFooter(7, 2, "制样：", Table.ALIGN_LEFT);
	
	rt.setDefautlFooter(10, 2, "司磅：", Table.ALIGN_LEFT);
	rt.setDefautlFooter(13, 2, "化验:", Table.ALIGN_LEFT);
	
//	rt.setDefautlFooter(15, 2, "化验：", Table.ALIGN_LEFT);
	
	rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
	rt.footer.fontSize=12;
//	rt.footer.setRowHeight(1, 1);
//	con.Close();
	if(rt.body.getPages()>0) {
		setCurrentPage(1);
		setAllPages(rt.body.getPages());
	}
	rt.body.setRowHeight(25);
	
//	int pages= (rt.body.getRows()-rt.body.getFixedRows()+rt.footer.getRows()-rt.footer.getFixedRows())*21%1120;
//	
//	if(pages!=0){
//		
//		if(pages%2==0){
//			pages/=2;
//		}else{
//			pages/=2+1;
//		}
//	}
//	
//	sql="";
//	
//	
//	for(int m=0;m<pages;m++){
//		sql=" select '','','','','','','','','','','','','','','' from dual \n";
//		
//		if(m!=pages-1){
//			sql=" union \n";
//		}
//	}
//	int foot_rows=rt.footer.getRows()-rt.footer.getFixedRows();
//	if(pages>0){
//		rt.footer.AddTableData(con.getResultSetList(sql));
//		
//		
//		for(int k=1;k<=1+foot_rows;k++){
//			rt.footer.merge(k, 1,k, 14);
//			
//			for(int j=1;j<14;j++){
//				rt.footer.getCell(k, j).setBorderNone();
//			}
//		}
//		
//	}
	
//	rt.body.setUseDefaultCss(true);
//	for(int k=0;k<rt.body.rows.length;k++){
//		Row row=rt.body.rows[k];
//		
//		if(row !=null && k%39==0){
//			row.className="\"row_page_end\"";
//		}
//		
//	}
	
	con.Close();
 	return rt.getAllPagesHtml();// ph;
 	}
	
	
	
	public String getPrintTable(){

		String s="";
		JDBCcon con=new  JDBCcon();
		ResultSetList  rsd=null;
		if(this.getGesValue().getStrId().equals("1")){//简报
		
			 rsd=this.hasDianc(this.getTreeid_dc());
			
			 int count=rsd.getRows();
			 rsd.beforefirst();
			 int c=0;
			while(rsd.next()){
				
				c++;
				String diancid=rsd.getString("id");
				
				s+=this.getPrintJb(diancid,count);
				
				
//				if(c!=count){
//					s+="<div style=\"PAGE-BREAK-AFTER: always\"></div> ";
//				}
//			
				
			}
//			s=this.getPrintJb();
		}else{
			 rsd=this.hasDianc(this.getTreeid_dc());
			 int count=rsd.getRows();
			 rsd.beforefirst();
			 int c=0;
				while(rsd.next()){
					c++;
					String diancid=rsd.getString("id");
					
					s+=this.getPrintMx(diancid,count);
//					if(c!=count){
//						s+="<div style=\"PAGE-BREAK-AFTER: always\"></div> ";
//					}
//					
					
				}
		}
		rsd.close();
		con.Close();
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

	private boolean  dcidboo=false;
	public void setTreeid_dc(String treeid) {
		
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
			dcidboo=true;
		}
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
			setERiq(DateUtil.FormatDate(new Date()));
			this.meikid="";
			this.treeid="";
			
			getGongysDropDownModels();
			this.getMeikModels();
//			this.setGongysDropDownModel(null);
//			this.setMeikModel(null);
			this.setGesModel(null);
			
		
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			this.setYunsfsModel(null);
			this.setYunsfsValue(null);
			
			
			this.setAarS("");
			this.setAarX("");
			
			this.setAdS("");
			this.setAdX("");
			
			this.setQnetarS("");
			this.setQnetarX("");
			
			this.setMadS("");
			this.setMadX("");
			
			this.setMtS("");
			this.setMtX("");
			
			this.setStadS("");
			this.setStadX("");
			
			this.setStdS("");
			this.setStdX("");
			
			this.setVdafS("");
			this.setVdafX("");
			
			this.setQnetarS_value("");
			this.setQnetarX_value("");
			
			
			
		
		}
		
//		if(briqboo || eriqboo || dcidboo){
//			briqboo=false;
//			eriqboo=false;
//			dcidboo=false;
//			this.meikid="";
//			this.treeid="";
//			
//			getGongysDropDownModels();
//			this.getMeikModels();
//			
//
//
//		}
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
		

//		getSelectData();
		
		
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
	
	
	private String QnetarS_value;
	
	public String getQnetarS_value(){
		return QnetarS_value;
	}
	
	public void setQnetarS_value(String value){
		QnetarS_value=value;
		if(value!=null && !value.equals("")){
			QnetarS_value=Float.valueOf(value).floatValue()*4.1816/1000+"";
		}
	}
	
	private String QnetarX_value;
	
	public String getQnetarX_value(){
		return QnetarX_value;
	}
	public void setQnetarX_value(String value){
		
		if(value!=null && !value.equals("")){
			QnetarX_value=Float.valueOf(value).floatValue()*4.1816/1000+"";
		}
	}
	
	private String _QnetarS;
	public String getQnetarS(){
		return _QnetarS;
	}
	
	public void setQnetarS(String value){
		_QnetarS=value;
		
	}
	
	private String _QnetarX;
	public String getQnetarX(){
		return _QnetarX;
	}
	
	public void setQnetarX(String value){
		_QnetarX=value;
	}
	
	private String _MtS;
	public String getMtS(){
		
		return _MtS;
	}
	
	public void setMtS(String value){
		_MtS=value;
	}
	
	private String _MtX;
	public String getMtX(){
		
		return _MtX;
	}
	
	public void setMtX(String value){
		_MtX=value;
	}
	
	private String _MadS;
	public String getMadS(){
		return _MadS;
	}
	
	public void setMadS(String value){
		_MadS=value;
	}
	
	private String _MadX;
	public String getMadX(){
		return _MadX;
	}
	
	public void setMadX(String value){
		_MadX=value;
	}
	
	private String _AarS;
	public String getAarS(){
		return _AarS;
	}
	
	public void setAarS(String value){
		_AarS=value;
	}
	
	private String _AarX;
	public String getAarX(){
		return _AarX;
	}
	
	public void setAarX(String value){
		_AarX=value;
	}
	
	private String _AdS;
	public String getAdS(){
		return _AdS;
	}
	
	public void setAdS(String value){
		_AdS=value;
	}
	
	private String _AdX;
	public String getAdX(){
		return _AdX;
	}
	
	public void setAdX(String value){
		_AdX=value;
	}
	
	private String _VdafS;
	public String getVdafS(){
		return _VdafS;
	}
	
	public void setVdafS(String value){
		_VdafS=value;
	}
	
	private String _VdafX;
	public String getVdafX(){
		return _VdafX;
	}
	
	public void setVdafX(String value){
		_VdafX=value;
	}
	
	private String _StadS;
	public String getStadS(){
		return _StadS;
	}
	
	public void setStadS(String value){
		_StadS=value;
	}
	
	private String _StadX;
	public String getStadX(){
		return _StadX;
	}
	
	public void setStadX(String value){
		_StadX=value;
	}
	
	private String StdS="";
	public String getStdS(){
	
		return StdS;
	}
	
	public void setStdS(String value){
		StdS=value;
	}
	
	private String StdX="";
	public String getStdX(){
		return StdX;
	}
	
	public void setStdX(String value){
		StdX=value;
	}
	
}
