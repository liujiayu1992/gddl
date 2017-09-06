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

public class Chedxypj_cx extends BasePage implements PageValidateListener {
	
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
	
		//if(this.getBaoblxValue().getValue().equals("按矿统计")){
			return getKuangbTj();
		//}
		
	}
	

	private String getKuangbTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String riqi=this.getRiqi();
		String riqi2=this.getRiqi2();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and kk.diancxxb_id in ("+this.getTreeid_dc()+")";
		}
		
		
		
		String tongjfs="";
		if(this.getTreeid_ysdw().equals("0")){
			tongjfs="";
		}else{
			tongjfs=" and kk.yunsdwb_id in ("+this.getTreeid_ysdw()+") \n";
		}
		
		String riqitiaoj="";
		String liangfzTiaoj="";
		if(this.getRiqiValue().getValue().equals("到货日期")){
			riqitiaoj="    where f.daohrq>=date'"+riqi+"'\n" + 
			          "    and f.daohrq<=date'"+riqi2+"'\n" ;
			liangfzTiaoj="and f.daohrq>=to_Date('"+riqi+"','yyyy-mm-dd') and f.daohrq<=to_Date('"+riqi2+"','yyyy-mm-dd')";
		}else{
			riqitiaoj="    where f.fahrq>=date'"+riqi+"'\n" + 
	                 "    and f.fahrq<=date'"+riqi2+"'\n" ;
			
			liangfzTiaoj="and f.fahrq>=to_Date('"+riqi+"','yyyy-mm-dd') and f.fahrq<=to_Date('"+riqi2+"','yyyy-mm-dd')";
		}
		
		
		
		
		StringBuffer buffer = new StringBuffer();
		
		
		if(this.getTongjfsValue().getValue().equals("不分厂别统计")){
			buffer.append(		"select decode(ys.mingc,null,'总计',ys.mingc) as ysdw,\n" +
					"decode(grouping(ys.mingc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as mkdw,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(yf.yunf*f.jingz)/sum(f.jingz),0))  as yunf,\n" + 
					"sum(f.biaoz) as kuangfl,sum(f.jingz) as rucl,\n" + 
					"--量分值\n"+
					"round(sum(f.jingz)/\n" +
					"(select sum(jingz) from fahb f,kuidkkb_wh kui\n" + 
					" where kui.fahb_id=f.id and f.yunsfsb_id<>1 "+liangfzTiaoj+")\n" + 
					"*25,2) as liangfz,\n"+
					"round((sum(f.biaoz)-sum(f.jingz))/sum(f.biaoz)*100,2) as yunsl,\n" + 
					"sum(kk.yunsyxcb) as yunsyxcb,\n" + 
					"round(25-round(sum(kk.yunsyxcb)/sum(f.jingz)*10000*2,2),2) as kuidfz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.chukrz*f.jingz)/sum(f.jingz),0)) as chukrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as daocrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.chukrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rezc,\n" + 
					"--亏卡影响成本\n"+
					"round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					" (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					" decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					" decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(jingz)/10000,2) as dunkkyxcb,"+
					"--亏卡分值\n"+
					"round(70-" +
					"round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					"    (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(f.jingz)/10000,2)"+
					"/sum(f.jingz)*10000*2,2) as kuikfz,\n" + 
					
					"--量分值+亏吨分值+亏卡分值=总分值\n" + 
					"round(sum(f.jingz)/\n" +
					"(select sum(jingz) from fahb f,kuidkkb_wh kui\n" + 
					" where kui.fahb_id=f.id and f.yunsfsb_id<>1 "+liangfzTiaoj+")\n" + 
					"*25,2)+\n"+
					"round(25-round(sum(kk.yunsyxcb)/sum(f.jingz)*10000*2,2),2)+\n" +
					"round(70-" +
					" round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					"    (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(f.jingz)/10000,2)"+
					"/sum(f.jingz)*10000*2,2)   as zongfz\n" + 
					
					"from kuidkkb_wh kk,meikxxb mk,yunsdwb ys,diancxxb dc ,fahb f ,kuidkkb_wh_yunf yf\n" + 
					"  "+riqitiaoj+""+
					" and kk.meikxxb_id=mk.id\n" + 
					" and kk.diancxxb_id=dc.id\n" + 
					" and kk.fahb_id=f.id\n" + 
					" and kk.fahb_id=yf.fahb_id(+)\n" + 
					" and kk.yunsdwb_id=ys.id and ys.id<>1\n" + 
					"  "+tongjfs+"  "+
					""+diancTiaoj+""+
					"group by rollup (ys.mingc,mk.mingc)\n" + 
					" order by grouping(ys.mingc),ys.mingc,grouping(mk.mingc),mk.mingc");
		}else{
			buffer.append(


					"select decode(dc.mingc,null,'总计',dc.mingc) as dcmc,\n" +
					"decode(grouping(dc.mingc)+grouping(ys.mingc),2,'',1,'合计',ys.mingc) as ysdw,\n" + 
					"decode(grouping(ys.mingc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as mkdw,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(yf.yunf*f.jingz)/sum(f.jingz),0))  as yunf,\n" + 
					"sum(f.biaoz) as kuangfl,sum(f.jingz) as rucl,\n" + 
					"--量分值\n"+
					"round(sum(f.jingz)/\n" +
					"(select sum(jingz) from fahb f,kuidkkb_wh kui\n" + 
					" where kui.fahb_id=f.id and f.yunsfsb_id<>1 "+liangfzTiaoj+")\n" + 
					"*25,2) as liangfz,\n"+
					"round((sum(f.biaoz)-sum(f.jingz))/sum(f.biaoz)*100,2) as yunsl,\n" + 
					"sum(kk.yunsyxcb) as yunsyxcb,\n" + 
					"round(25-round(sum(kk.yunsyxcb)/sum(f.jingz)*10000*2,2),2) as kuidfz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.chukrz*f.jingz)/sum(f.jingz),0)) as chukrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as daocrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.chukrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"decode(sum(f.jingz),0,0,round(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rezc,\n" + 
					
					"--亏卡影响成本\n"+
					"round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					" (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					" decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					" decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(jingz)/10000,2) as dunkkyxcb,"+
					"--亏卡分值\n"+
					"round(70-" +
					"round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					"    (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(f.jingz)/10000,2)"+
					"/sum(f.jingz)*10000*2,2) as kuikfz,\n" + 
					
					"--量分值+亏吨分值+亏卡分值=总分值\n" + 
					"round(sum(f.jingz)/\n" +
					"(select sum(jingz) from fahb f,kuidkkb_wh kui\n" + 
					" where kui.fahb_id=f.id and f.yunsfsb_id<>1 "+liangfzTiaoj+")\n" + 
					"*25,2)+\n"+
					"round(25-round(sum(kk.yunsyxcb)/sum(f.jingz)*10000*2,2),2)+\n" +
					"round(70-" +
					" round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" +
					"    (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"  decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"  decode(sum(jiesrz),0,0,round_new(\n" + 
					"     (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"    decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"      sum(f.jingz)/10000,2)"+
					"/sum(f.jingz)*10000*2,2)   as zongfz\n" + 
					"from kuidkkb_wh kk,meikxxb mk,yunsdwb ys,diancxxb dc ,fahb f ,kuidkkb_wh_yunf yf\n" + 
					"  "+riqitiaoj+""+
					" and kk.meikxxb_id=mk.id\n" + 
					" and kk.diancxxb_id=dc.id\n" + 
					" and kk.fahb_id=f.id\n" + 
					" and kk.fahb_id=yf.fahb_id(+)\n" + 
					" and kk.yunsdwb_id=ys.id and ys.id<>1\n" + 
					"  "+tongjfs+"  "+
					""+diancTiaoj+""+
					"group by rollup (dc.mingc,ys.mingc,mk.mingc)\n" + 
					" order by grouping(dc.mingc),min(dc.id),grouping(ys.mingc),ys.mingc,grouping(mk.mingc),mk.mingc");
			
		}
		

		
		
		

		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String ArrHeader[][];
		int ArrWidth[];
		if(this.getTongjfsValue().getValue().equals("不分厂别统计")){
			ArrHeader = new String[1][18];
			ArrHeader[0] = new String[] { "运输单位","煤矿","合同<br>热值", "煤价","运价", "矿发<br>数量", "到厂<br>数量", "量分值","实际<br>运损率", 
					"运损影<br>响成本", "亏吨<br>分值", "出矿<br>热值","到厂<br>热值","结算<br>热值","出矿到厂热值差","亏卡<br>影响成本","亏卡<br>分值","总分值"};
			
			 ArrWidth = new int[] {100, 100, 60,60, 60, 60, 60, 60, 60, 60,60 , 60, 60,60, 60, 60,60,60};
		}else{
			 ArrHeader= new String[1][19];
			 ArrHeader[0] = new String[] {"厂别", "运输单位","煤矿","合同<br>热值", "煤价","运价", "矿发<br>数量", "到厂<br>数量", "量分值","实际<br>运损率", 
					"运损影<br>响成本", "亏吨<br>分值", "出矿<br>热值","到厂<br>热值","结算<br>热值","出矿到厂热值差","亏卡<br>影响成本","亏卡<br>分值","总分值"};
			 ArrWidth = new int[] {80,100, 100, 60,60, 60, 60, 60, 60, 60, 60,60 , 60, 60,60, 60, 60,60,60};
		}
		
		rt.setTitle("直购煤承运车队信誉评价表", ArrWidth);
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
		rt.setDefaultTitle(14, 5, this.getRiqiValue().getValue()+":"+riqi+ "至"+riqi2, Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		/*if(rt.body.getRows()>1){
			//清空最后一行第8列到最后的数据
			int hangs=rt.body.getRows();
			for (int a=8;a<=rt.body.getCols();a++){
				rt.body.setCellValue(hangs, a, "");
			}
			
		}
		*/
		
		
		
		//----------------
		
		
		rt.body.ShowZero=true;
		
		
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
			
			setTreeid_ysdw(null);
			getYunsdwmcModels();
			
		
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			
			this.setRiqiValue(null);
			this.getRiqiModels();
			//getSelectData();
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
		
		ComboBox riqtj = new ComboBox();
		riqtj.setTransform("RiqiDropDown");
		riqtj.setListeners("select:function(){document.Form0.submit();}");
		riqtj.setId("Riqi");
		riqtj.setWidth(80);
		tb1.addField(riqtj);
		tb1.addText(new ToolbarText("-"));
		
		
		
		//tb1.addText(new ToolbarText("发货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.setId("riqi");
		df.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.setId("riqi2");
		df2.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI2').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
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
		
		
		
		
		String yunsdwtj=" where id<>1 and id in (select distinct yunsdwb_id from kuidkkb_wh wh where wh.fahrq>=date'"+this.getRiqi()+"' and wh.fahrq<=date'"+this.getRiqi2()+"')";
		ExtTreeUtil etu2 = new ExtTreeUtil("yunsdwTree",
				ExtTreeUtil.treeWindowCheck_yunsdw_datong, diancxxb_id, "0",yunsdwtj,true);

				setTree2(etu2);
				TextField tf2 = new TextField();
				tf2.setId("yunsdwTree_text");
				tf2.setWidth(100);
				String[] str2=getTreeid_ysdw().split(",");
				if(str2[0].equals("0")){
					tf2.setValue("全部");
				}else{
					tf2.setValue(((IDropDownModel) getYunsdwmcModel()).getBeanValue(Long.parseLong(str2[0])));
				}
				
				ToolbarButton toolb = new ToolbarButton(null, null,
						"function(){yunsdwTree_window.show();}");
				toolb.setIcon("ext/resources/images/list-items.gif");
				toolb.setCls("x-btn-icon");
				toolb.setMinWidth(20);
				
				tb1.addText(new ToolbarText("车队:"));
				tb1.addField(tf2);
				tb1.addItem(toolb);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		tb1.addText(new ToolbarText("统计:"));
		ComboBox tj = new ComboBox();
		tj.setTransform("TongjfsDropDown");
		tj.setListeners("select:function(){document.Form0.submit();}");
		tj.setId("Tongjfs");
		tj.setWidth(100);
		tb1.addField(tj);
		tb1.addText(new ToolbarText("-"));
		
		
		
		

		//ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){}");
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
			fangs.add(new IDropDownBean(1,"不分厂别统计"));
			fangs.add(new IDropDownBean(2,"分厂别统计"));
			_TongjfsModel = new IDropDownModel(fangs);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _TongjfsModel;
	}
	
	



	private String Markmk = "true"; // 
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
	}
	
	
	
	
	


//	日期方式
		public boolean _Riqichange = false;
		private IDropDownBean _RiqiValue;

		public IDropDownBean getRiqiValue() {
			if(_RiqiValue==null){
				_RiqiValue=(IDropDownBean)getRiqiModels().getOption(0);
			}
			return _RiqiValue;
		}

		public void setRiqiValue(IDropDownBean Value) {
			long id = -2;
			if (_RiqiValue != null) {
				id = _RiqiValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_Riqichange = true;
				} else {
					_Riqichange = false;
				}
			}
			_RiqiValue = Value;
		}

		private IPropertySelectionModel _RiqiModel;

		public void setRiqiModel(IPropertySelectionModel value) {
			_RiqiModel = value;
		}

		public IPropertySelectionModel getRiqiModel() {
			if (_RiqiModel == null) {
				getRiqiModels();
			}
			return _RiqiModel;
		}

		public IPropertySelectionModel getRiqiModels() {
			
			try{
				List fangs = new ArrayList();
				//fahdwList.add(new IDropDownBean(0,"按厂统计"));
				fangs.add(new IDropDownBean(1,"发货日期"));
				fangs.add(new IDropDownBean(2,"到货日期"));
				_RiqiModel = new IDropDownModel(fangs);

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//con.Close();
			}
			return _RiqiModel;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		//以下是新增的车队复选框----开始
		
		public ExtTreeUtil getTree2() {
			return ((Visit) this.getPage().getVisit()).getExtTree2();
		}

		public void setTree2(ExtTreeUtil etu) {
			((Visit) this.getPage().getVisit()).setExtTree2(etu);
		}
		
		public String getTreeHtml2() {
			if (getDCTree() == null){
				return "";
			}else {
				return getTree2().getWindowTreeHtml(this);
			}
		}

		public String getTreeScript2() {
			if (getDCTree() == null) {
				return "";
			}else {
				return getTree2().getWindowTreeScript();
			}
		}
		
		public String getTreeid_ysdw() {
			String treeid = ((Visit) getPage().getVisit()).getString4();
			if (treeid == null || treeid.equals("")) {
				((Visit) getPage().getVisit()).setString4("0");
			}
			return ((Visit) getPage().getVisit()).getString4();
		}

		public void setTreeid_ysdw(String treeid) {
			if(((Visit) getPage().getVisit()).getString4()!=null && !((Visit) getPage().getVisit()).getString4().equals(treeid)){
			}
			((Visit) getPage().getVisit()).setString4(treeid);
		}
		
		
		public IPropertySelectionModel getYunsdwmcModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getYunsdwmcModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setYunsdwmcModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(_value);
		}

		public void getYunsdwmcModels() {
			String sql2 = "select id,mingc from yunsdwb";
			setYunsdwmcModel(new IDropDownModel(sql2));
		}
		
		
		//以上是新增的车队复选框----结束
		
}