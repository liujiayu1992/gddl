package com.zhiren.shanxdted.baob.kuidkkb;

import java.sql.ResultSet;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：赵胜男
 * 日期：2013-04-10
 * 描述：“矿发热值”和“出矿热值”的加权数量变更为矿发量进行加权
 */

public class Kuidkkb_cx extends BasePage implements PageValidateListener {
	
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

	// ***************设置消息框******************//
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
	
	public String getPrintTable(){
	
		if(this.getBaoblxValue().getValue().equals("按矿统计")){
			return getKuangbTj();
		}else if(this.getBaoblxValue().getValue().equals("按车队统计")) {//车队统计
			return getChedTj();
		}else if(this.getBaoblxValue().getValue().equals("按地区统计")) {//
			return getDiquTj();
		}else{
			return getShujhd();
		}
		
	}
	
	
	
	private String getShujhd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and kk.diancxxb_id in ("+this.getTreeid_dc()+")  ";
		}
		
		
		
		
	
		StringBuffer buffer = new StringBuffer();
		
		//数据核对始终用到货日期作为条件,不用发货日期,因为核对数据时没必要用发货日期
		buffer.append(
				"select fh.daohrq ,nvl(kk.jingz,0),fh.jingz,nvl(kk.jingz,0)-fh.jingz,nvl(kk.kuangfl,0),fh.kuangfl,\n" +
				"nvl(kk.kuangfl,0)-fh.kuangfl, nvl(kk.rez,0),fh.rez,nvl(kk.rez,0)-fh.rez,\n" + 
				"nvl(kk.kuangfrz,0),fh.kuangfrz,nvl(kk.kuangfrz,0)-fh.kuangfrz as kuangfrz_cha,\n" + 
				"nvl(kk.chukrz,0),fh.chukrz,nvl(kk.chukrz,0)-fh.chukrz as chukrz_cha\n" + 
				"from\n" + 
				"(select decode(kk.daohrq,null,'总计',to_char(kk.daohrq,'yyyy-mm-dd'))  as daohrq,sum(f.jingz) as jingz,\n" + 
				"sum(f.biaoz) as kuangfl,round(sum(f.jingz*kk.rucrz)/sum(f.jingz),0) as rez,\n" + 
				"round(sum(f.biaoz*kk.kuangfrz)/sum(f.biaoz),0) as kuangfrz,\n" + 
				"round(sum(f.biaoz*kk.chukrz)/sum(f.biaoz),0) as chukrz\n" + 
				"from kuidkkb_wh kk,fahb f\n" + 
				"where kk.daohrq>=First_day(date'"+this.getRiqi2()+"')\n" + 
				"and kk.daohrq<=date'"+this.getRiqi2()+"'\n" + 
				"and kk.fahb_id=f.id\n" + 
				""+diancTiaoj+""+
				"group by rollup  (kk.daohrq)) kk,\n" + 
				"(select decode(daohrq,null,'总计',to_char(daohrq,'yyyy-mm-dd')) as daohrq,sum(jingz) as jingz,\n" + 
				"sum(biaoz) as kuangfl,round(sum(zl.qnet_ar*1000/4.1816*kk.jingz)/sum(jingz),0) as rez,\n" + 
				"round(sum(kf.qnet_ar*1000/4.1816*kk.biaoz)/sum(biaoz),0) as kuangfrz,\n" + 
				"round(sum(kf.qnet_ar_ck*1000/4.1816*kk.biaoz)/sum(biaoz),0) as chukrz\n" + 
				"from fahb kk ,zhilb zl,kuangfzlb kf\n" + 
				"where kk.daohrq>=First_day(date'"+this.getRiqi2()+"')\n" + 
				"and kk.daohrq<=date'"+this.getRiqi2()+"'\n" + 
				"and kk.zhilb_id=zl.id\n" + 
				"and kk.kuangfzlb_id=kf.id(+)\n" + 
				""+diancTiaoj+""+
				"group by  rollup (kk.daohrq)) fh\n" + 
				"where fh.daohrq= kk.daohrq(+)\n" + 
				"order by fh.daohrq");
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[1][16];
		
		ArrHeader[0] = new String[] { "到货日期","亏吨亏卡<br>净重","实际来煤<br>净重", "亏吨亏卡<br>净重与实际<br>来煤净重差值",
				"亏吨亏卡<br>矿发量", "实际来煤<br>矿发量", "亏吨亏卡<br>矿发量与<br>实际来煤<br>矿发量差值","亏吨亏卡<br>热值", "实际热值",
				"亏吨亏卡<br>热值与实际<br>热值差","亏吨亏卡<br>矿发热值","实际<br>矿发热值","亏吨亏卡<br>矿发热值<br>与实际矿<br>方热值差",
				"亏吨亏卡<br>出矿热值","实际<br>出矿热值","亏吨亏卡<br>出矿热值<br>与实际<br>出矿热值差"};
		
		int ArrWidth[] = new int[] {100, 100, 70,70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70};

		rt.setTitle(this.getRiqi()+"亏 吨 亏 卡 日 报(数据核对)", ArrWidth);
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="国电大同发电";
		}else if(this.getTreeid_dc().equals("301")){
			baot="国电大同一期";
		}else if(this.getTreeid_dc().equals("302")){
			baot="国电大同二期";
		}else if(this.getTreeid_dc().equals("303")){
			baot="国电大同三期";
		}else if(this.getTreeid_dc().equals("304")){
			baot="洗煤场";
		}
		rt.setDefaultTitle(1, 3, "单位：" + baot, Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		rt.body.ShowZero=true;
		
		for (int i=2;i<=rt.body.getRows();i++){
			if(!rt.body.getCellValue(i, 4).equals("0")){
				rt.body.getCell(i, 4).backColor="yellow";
			}
			if(!rt.body.getCellValue(i, 7).equals("0")){
				rt.body.getCell(i, 7).backColor="yellow";
			}
			if(!rt.body.getCellValue(i, 10).equals("0")){
				rt.body.getCell(i, 10).backColor="yellow";
			}
			if(!rt.body.getCellValue(i, 13).equals("0")){
				rt.body.getCell(i, 13).backColor="yellow";
			}
			if(!rt.body.getCellValue(i, 16).equals("0")){
				rt.body.getCell(i, 16).backColor="yellow";
			}
		}
		
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private String getChedTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and kk.diancxxb_id in ("+this.getTreeid_dc()+")";
		}
		
		String riqitiaoj="";
		String tongjfstj=this.getTongjfsValue().getValue();
		String tongjfs="";
		if(tongjfstj.equals("到货日期")){
			riqitiaoj="    where f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjfs="       to_char(min(f.daohrq),'yyyy-mm-dd')||'至'||to_char(max(f.daohrq),'yyyy-mm-dd') as riq,\n" ;
			
		}else{
			riqitiaoj="    where f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjfs="       to_char(min(f.fahrq),'yyyy-mm-dd')||'至'||to_char(max(f.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}
	
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select decode(ys.mingc,null,'总计',ys.mingc) as ysdw,\n" +
				"       decode(grouping(mk.mingc)+grouping(ys.mingc),2,'',1,'小计',mk.mingc) as meikmc,\n" + 
				"      "+tongjfs+"" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2)) as hetmj,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2)) as yunf,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(hetrz)\n"+
				"       decode(sum(hetrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)),2))  as hetbmdj,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(kuangfrz)\n"+
				"       decode(sum(kuangfrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)),2))  as kuangfbmdj,\n" + 
				"       --因为sum(f.f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(rucrz)\n"+
				"       decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2))  as rucbmdj,\n" + 
				"       sum(f.biaoz) as biaoz,sum(jingz) as jingz,\n" + 
				"       round_new((sum(f.biaoz)-sum(jingz))/sum(f.biaoz),3) as bangc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as kuangfrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0)) as chukrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rucrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0))-\n" + 
				"          decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as hetkfrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as kuangfrcrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as chukrcrzc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) -\n" + 
				"             decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as rucjsrzc,\n" + 
				"       sum(f.biaoz)-sum(f.jingz) as kuidsl,\n" + 
				"       sum(kk.kuidje) as kuidje,\n" + 
				"       --吨亏卡影响  公式=(入厂标煤单价-结算标煤单价)*入厂热值/7000\n" + 
				"       round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2) as dunkkyx,\n" + 
				"        --吨亏卡影响成本  公式=吨亏卡影响*入厂数量/10000\n" + 
				"       round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as dunkkyxcb,\n" + 
				"        --亏吨亏卡金额合计\n" + 
				"        sum(kk.kuidje) +\n" + 
				"         round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as kuidkkhj\n" + 
				"    from kuidkkb_wh kk,fahb f ,meikxxb mk,yunsdwb ys ,kuidkkb_wh_yunf yf\n" + 
				"    "+riqitiaoj+""+
				"    and kk.fahb_id=f.id\n" + 
				"    and kk.meikxxb_id=mk.id\n" + 
				"    and kk.yunsdwb_id=ys.id\n" + 
				"    and kk.fahb_id=yf.fahb_id(+)\n" + 
				"    "+diancTiaoj+"\n"+
				"    and f.yunsfsb_id<>1--车队统计不统计铁路\n" + 
				"    group by rollup(ys.mingc,mk.mingc)\n" + 
				"    order by grouping(ys.mingc) ,ys.mingc,grouping(mk.mingc),mk.mingc");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][24];
		
		ArrHeader[0] = new String[] { "运输单位","煤矿",tongjfstj, "价格情况", "价格情况", "价格情况", "价格情况","价格情况", "价格情况", "入厂煤数量热值情况", "入厂煤数量热值情况","入厂煤数量热值情况", "入厂煤数量热值情况",
				"入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","亏吨","亏吨",
				"亏卡","亏卡","亏吨亏卡合计"};
		ArrHeader[1] = new String[] { "运输单位","煤矿",tongjfstj, "合同煤价", "煤价", "运费", "合同标煤单价","矿发标煤单价","入厂标煤单价","矿发量","入厂量","磅差(%)","合同热值",
				"矿发热值","出矿热值", "入厂热值", "结算热值", "合同与矿发热值差" ,"矿发与入厂热值差","出矿与入厂热值差","入厂与结算热值差","数量","金额(万元)","吨亏卡影响","金额(万元)","亏吨亏卡合计"};

		int ArrWidth[] = new int[] {100, 100, 150,55, 55, 55, 55, 55, 55, 55,55 , 55,55,55, 55, 55,55, 55,55 , 55, 55, 55, 55,55 , 55, 55};

		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"运 输 亏 吨 亏 卡 日 报", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"运 输 亏 吨 亏 卡 日 报", ArrWidth);
		}
		
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="国电大同发电(1-10)";
		}else if(this.getTreeid_dc().equals("301")){
			baot="国电大同一期(1-6)";
		}else if(this.getTreeid_dc().equals("302")){
			baot="国电大同二期(7-8)";
		}else if(this.getTreeid_dc().equals("303")){
			baot="国电大同三期(9-10)";
		}else if(this.getTreeid_dc().equals("302,303")){
			baot="国电大同发电公司(7-10)";
		}else if(this.getTreeid_dc().equals("301,302,303")){
			baot="国电大同发电(1-10)";
		}
		rt.setDefaultTitle(1, 3, "单位：" + baot, Table.ALIGN_LEFT);
		//rt.setDefaultTitle(19, 4, tongjfstj+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		
		rt.body.ShowZero=true;
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 6).equals("0")){
				rt.body.setCellValue(ab, 6, "");
			}
		}
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	
	private String getKuangbTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String riqi=this.getRiqi();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and kk.diancxxb_id in ("+this.getTreeid_dc()+")";
		}
		
		String riqitiaoj="";
		String tongjsf=this.getTongjfsValue().getValue();
		String tongjxsfs="";
		if(tongjsf.equals("到货日期")){
			riqitiaoj="    where f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjxsfs="       to_char(min(f.daohrq),'yyyy-mm-dd')||'至'||to_char(max(f.daohrq),'yyyy-mm-dd') as riq,\n" ;
		}else{
			riqitiaoj="    where f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjxsfs="       to_char(min(f.fahrq),'yyyy-mm-dd')||'至'||to_char(max(f.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}
		

		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select decode(mk.mingc,null,'总计',mk.mingc) as meikdw,\n" +
				"       decode(grouping(mk.mingc)+grouping(ys.mingc),2,'',1,'小计',ys.mingc) as yunsdw,\n" + 
				"       "+tongjxsfs+"\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2)) as hetmj,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2)) as yunf,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(hetrz)\n"+
				"       decode(sum(hetrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)),2))  as hetbmdj,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(kuangfrz)\n"+
				"       decode(sum(kuangfrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)),2))  as kuangfbmdj,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(rucrz)\n"+
				"       decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2))  as rucbmdj,\n" + 
				"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
				"       round_new((sum(f.biaoz)-sum(jingz))/sum(f.biaoz),3) as bangc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as kuangfrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0)) as chukrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rucrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0))-\n" + 
				"          decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as hetkfrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as kuangfrcrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as chukrcrzc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) -\n" + 
				"             decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as rucjsrzc,\n" + 
				"       sum(f.biaoz)-sum(f.jingz) as kuidsl,\n" + 
				"       sum(kk.kuidje) as kuidje,\n"+
				"       --吨亏卡影响  公式=(入厂标煤单价-结算标煤单价)*入厂热值/7000\n" + 
				"       round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2) as dunkkyx,\n" + 
				"        --吨亏卡影响成本  公式=吨亏卡影响*入厂数量/10000\n" + 
				"       round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as dunkkyxcb,\n" + 
				"        --亏吨亏卡金额合计\n" + 
				"        sum(kk.kuidje) +\n" + 
				"         round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as kuidkkhj\n" + 
				"    from kuidkkb_wh kk,fahb f ,meikxxb mk,yunsdwb ys ,kuidkkb_wh_yunf yf\n" + 
				"    "+riqitiaoj+""+
				"    "+diancTiaoj+"\n"+
				"    and kk.fahb_id=f.id\n" + 
				"    and kk.meikxxb_id=mk.id\n" + 
				"    and kk.yunsdwb_id=ys.id\n" + 
				"    and kk.fahb_id=yf.fahb_id(+)\n" + 
				"    group by rollup(mk.mingc,ys.mingc)\n" + 
				"    order by grouping(mk.mingc),mk.mingc,grouping(ys.mingc) ,ys.mingc"
);
		
		
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][26];
		
		ArrHeader[0] = new String[] {"煤矿", "运输单位",tongjsf, "价格情况", "价格情况", "价格情况", "价格情况","价格情况", "价格情况", "入厂煤数量热值情况", "入厂煤数量热值情况", "入厂煤数量热值情况", "入厂煤数量热值情况",
				"入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","亏吨","亏吨",
				"亏卡","亏卡","亏吨亏卡合计"};
		ArrHeader[1] = new String[] {"煤矿", "运输单位",tongjsf, "合同煤价", "煤价", "运费", "合同标煤单价","矿发标煤单价","入厂标煤单价","矿发量","入厂量","磅差(%)","合同热值",
				"矿发热值","出矿热值", "入厂热值", "结算热值", "合同与矿发热值差" ,"矿发与入厂热值差","出矿与入厂热值差","入厂与结算热值差","数量","金额(万元)","吨亏卡影响","金额(万元)","亏吨亏卡合计"};

		int ArrWidth[] = new int[] {100, 100, 150,55, 55, 55, 55, 55, 55, 55,55 ,55,55, 55, 55,55, 55, 55,55 , 55, 55, 55, 55,55 , 55, 55};
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"亏 吨 亏 卡 日 报", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"亏 吨 亏 卡 日 报", ArrWidth);
		}
		
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="国电大同发电(1-10)";
		}else if(this.getTreeid_dc().equals("301")){
			baot="国电大同一期(1-6)";
		}else if(this.getTreeid_dc().equals("302")){
			baot="国电大同二期(7-8)";
		}else if(this.getTreeid_dc().equals("303")){
			baot="国电大同三期(9-10)";
		}else if(this.getTreeid_dc().equals("302,303")){
			baot="国电大同发电公司(7-10)";
		}else if(this.getTreeid_dc().equals("301,302,303")){
			baot="国电大同发电(1-10)";
		}
		rt.setDefaultTitle(1, 3, "单位：" + baot, Table.ALIGN_LEFT);
		//rt.setDefaultTitle(19, 4, tongjsf+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 6).equals("0")){
				rt.body.setCellValue(ab, 6, "");
			}
		}
		
		
		//----------------
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiquTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String riqi=this.getRiqi();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and kk.diancxxb_id in ("+this.getTreeid_dc()+")";
		}
		
		String riqitiaoj="";
		String tongjxsfs="";
		String tongjfstj=this.getTongjfsValue().getValue();
		if(tongjfstj.equals("到货日期")){
			riqitiaoj="    where f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjxsfs="       to_char(min(f.daohrq),'yyyy-mm-dd')||'至'||to_char(max(f.daohrq),'yyyy-mm-dd') as riq,\n" ;
		}else{
			riqitiaoj="    where f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
			tongjxsfs="       to_char(min(f.fahrq),'yyyy-mm-dd')||'至'||to_char(max(f.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select decode(g.mingc,null,'总计',g.mingc) as meikdq,\n" +
				"       decode(grouping(g.mingc)+grouping(mk.mingc),2,'',1,'合计',mk.mingc) as meikdw,\n" + 
				"       decode(grouping(g.mingc)+grouping(mk.mingc)+grouping(ys.mingc),3,'',2,'',1,'小计',ys.mingc) as yunsdw,\n" + 
				"      "+tongjxsfs+"" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2)) as hetmj,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2)) as yunf,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(hetrz)\n"+
				"       decode(sum(hetrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)),2))  as hetbmdj,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(kuangfrz)\n"+
				"       decode(sum(kuangfrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)),2))  as kuangfbmdj,\n" + 
				"       --因为sum(f.jingz)在发货表不可能是0,所以就不判断被除数sum(f.jingz),改成判断sum(rucrz)\n"+
				"       decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2))  as rucbmdj,\n" + 
				"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
				"       round_new((sum(f.biaoz)-sum(f.jingz))/sum(f.biaoz),3) as bangc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as kuangfrz,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0)) as chukrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rucrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0))-\n" + 
				"          decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0)) as hetkfrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.kuangfrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as kuangfrcrzc,\n" + 
				"       decode(sum(f.biaoz),0,0,round_new(sum(kk.chukrz*f.biaoz)/sum(f.biaoz),0))-\n" + 
				"           decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as chukcrzc,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) -\n" + 
				"             decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as rucjsrzc,\n" + 
				"       sum(f.biaoz)-sum(f.jingz) as kuidsl,\n" + 
				"       sum(kk.kuidje) as kuidje,\n" + 
				"       --吨亏卡影响  公式=(入厂标煤单价-结算标煤单价)*入厂热值/7000\n" + 
				"       round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2) as dunkkyx,\n" + 
				"        --吨亏卡影响成本  公式=吨亏卡影响*入厂数量/10000\n" + 
				"       round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as dunkkyxcb,\n" + 
				"        --亏吨亏卡金额合计\n" + 
				"        sum(kk.kuidje) +\n" + 
				"         round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
				"       decode(sum(jiesrz),0,0,round_new(\n" + 
				"        (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
				"         decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
				"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
				"        sum(jingz)/10000,2) as kuidkkhj\n" + 
				"    from kuidkkb_wh kk,fahb f ,meikxxb mk,yunsdwb ys ,kuidkkb_wh_yunf yf,gongysb g\n" + 
				"   "+riqitiaoj+""+
				"    and kk.fahb_id=f.id\n" + 
				"    and kk.meikxxb_id=mk.id\n" + 
				"    and kk.yunsdwb_id=ys.id\n" + 
				"    and kk.fahb_id=yf.fahb_id(+)\n" + 
				"    and mk.meikdq2_id=g.id\n" + 
				"     "+diancTiaoj+"\n "+
				"    group by rollup(g.mingc,mk.mingc,ys.mingc)\n" + 
				"    order by grouping(g.mingc),g.mingc,grouping(mk.mingc),mk.mingc,grouping(ys.mingc) ,ys.mingc");
		
		
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][27];
		
		ArrHeader[0] = new String[] {"煤矿地区","煤矿", "运输单位",tongjfstj, "价格情况", "价格情况", "价格情况", "价格情况","价格情况", "价格情况", "入厂煤数量热值情况", "入厂煤数量热值情况", "入厂煤数量热值情况", "入厂煤数量热值情况",
				"入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","亏吨","亏吨",
				"亏卡","亏卡","亏吨亏卡合计"};
		ArrHeader[1] = new String[] {"煤矿地区","煤矿", "运输单位",tongjfstj, "合同煤价", "煤价", "运费", "合同标煤单价","矿发标煤单价","入厂标煤单价","矿发量","入厂量","磅差(%)","合同热值",
				"矿发热值","出矿热值", "入厂热值", "结算热值", "合同与矿发热值差" ,"矿发与入厂热值差","出矿与入厂热值差","入厂与结算热值差","数量","金额(万元)","吨亏卡影响","金额(万元)","亏吨亏卡合计"};

		int ArrWidth[] = new int[] {80,100, 100, 150,55, 55, 55, 55, 55, 55, 55,55 , 55,55,55, 55,55, 55, 55,55 , 55, 55, 55, 55,55 , 55, 55};
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"亏 吨 亏 卡 日 报", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"亏 吨 亏 卡 日 报", ArrWidth);
		}
		
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="国电大同发电(1-10)";
		}else if(this.getTreeid_dc().equals("301")){
			baot="国电大同一期(1-6)";
		}else if(this.getTreeid_dc().equals("302")){
			baot="国电大同二期(7-8)";
		}else if(this.getTreeid_dc().equals("303")){
			baot="国电大同三期(9-10)";
		}else if(this.getTreeid_dc().equals("302,303")){
			baot="国电大同发电公司(7-10)";
		}else if(this.getTreeid_dc().equals("301,302,303")){
			baot="国电大同发电(1-10)";
		}
		rt.setDefaultTitle(1, 3, "单位：" + baot, Table.ALIGN_LEFT);
		//rt.setDefaultTitle(19, 4, tongjfstj+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 7).equals("0")){
				rt.body.setCellValue(ab, 7, "");
			}
		}
		
		
		//----------------
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}


	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiqi(null);
			this.setRiqi2(null);
			
			setTreeid_dc(null);
			getDiancmcModels();
			
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			getSelectData();
		}
		getSelectData();

	}
	
	// 绑定日期
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
		}

	}

	
	
//	 绑定日期
	private String riqi2;
	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi2 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
		}

	}


	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			//getPrintTable();
			_RefurbishChick = false;
		}

	}

    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
	
		ComboBox tj = new ComboBox();
		tj.setTransform("TongjfsDropDown");
		tj.setListeners("select:function(){document.Form0.submit();}");
		tj.setId("Tongjfs");
		tj.setWidth(80);
		tb1.addField(tj);
		tb1.addText(new ToolbarText("-"));
		
		
		//tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.Binding("riqi2", "");// 与html页中的id绑定,并自动刷新
		df2.setId("riqi2");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		
		tb1.addText(new ToolbarText("-"));
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
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
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("类型:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(100);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		
	
		
		

		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);

	}
	
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
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getDCTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getDCTree().getWindowTreeHtml(this);
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
	
	

	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
			List fahdwList = new ArrayList();
			//fahdwList.add(new IDropDownBean(0,"按厂统计"));
			fahdwList.add(new IDropDownBean(1,"按矿统计"));
			fahdwList.add(new IDropDownBean(2,"按车队统计"));
			fahdwList.add(new IDropDownBean(3,"按地区统计"));
			fahdwList.add(new IDropDownBean(4,"数据核对"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
	
	
	
	

///统计方式
	public boolean _Tongjfschange = false;
	private IDropDownBean _TongjfsValue;

	public IDropDownBean getTongjfsValue() {
		if(_TongjfsValue==null){
			_TongjfsValue=(IDropDownBean)getTongjfsModels().getOption(0);
		}
		return _TongjfsValue;
	}

	public void setTongjfsValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjfsValue != null) {
			id = _TongjfsValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tongjfschange = true;
			} else {
				_Tongjfschange = false;
			}
		}
		_TongjfsValue = Value;
	}

	private IPropertySelectionModel _TongjfsModel;

	public void setTongjfsModel(IPropertySelectionModel value) {
		_TongjfsModel = value;
	}

	public IPropertySelectionModel getTongjfsModel() {
		if (_TongjfsModel == null) {
			getTongjfsModels();
		}
		return _TongjfsModel;
	}

	public IPropertySelectionModel getTongjfsModels() {
		
		try{
			List fangs = new ArrayList();
			//fahdwList.add(new IDropDownBean(0,"按厂统计"));
			fangs.add(new IDropDownBean(1,"到货日期"));
			fangs.add(new IDropDownBean(2,"发货日期"));
			_TongjfsModel = new IDropDownModel(fangs);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _TongjfsModel;
	}
}