package com.zhiren.shanxdted.rultz;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
/*
 * 作者：赵胜男
 * 日期：2013-04-11
 * 描述：“矿发热值”和“出矿热值”的加权数量变更为矿发量进行加权
 */

/*作者:王总兵
 *日期:2013-5-8 
 *描述:在条件中增加发货日期/到货日期下拉框
 */
public class Kuangcrzc extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}





	public String getPrintTable() {
		setMsg(null);
		if (this.getBaoblxValue().getValue().equals("分厂别汇总显示")) {

			return getChaxun_Huiz();
		} else if(this.getBaoblxValue().getValue().equals("分地区汇总显示")) {

			return getChaxun_Huiz_diq();
		} else{
			return getChaxun_mingx();
		}
	}

	private String Markbh = "true"; // 标记编号下拉框是否被选择
	
	public String getMarkbh() {
		return Markbh;
	}

	public void setMarkbh(String markbh) {
		Markbh = markbh;
	}
	
	private String getChaxun_Huiz_diq() {
		JDBCcon con = new JDBCcon();
		
		
		String dcid = "";
		if("300".equals(getTreeid_dc())){
			dcid = " 300,301,302,303,304";
		}else{
			dcid = ""+getTreeid_dc()+"";
		}
		
		StringBuffer SQL = new StringBuffer();
	
		String riqitiaoj="";
		if(this.getTongjfsValue().getValue().equals("到货日期")){
			riqitiaoj="    and f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
		}else{
			riqitiaoj="    and f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
		}
		

		
        String meiktiaoj="";
        if(this.getMeikxxValue().getId()!=-1){
        	meiktiaoj="  and f.meikxxb_id ="+this.getMeikxxValue().getId();
        }
       
        String yunsdwtiaoj="";
        if(this.getYunsdwValue().getId()!=-1){
        	yunsdwtiaoj=" and yunsdwb.id="+this.getYunsdwValue().getId();
        }
       
        SQL.delete(0, SQL.length());
        SQL.append(

        		"select decode(g.mingc,null,'总计',g.mingc) as meikdq,\n" +
        		" decode(grouping(g.mingc)+grouping(m.mingc),2,'',1,'合计',m.mingc) as mkmc,\n" + 
        		" decode(grouping(t.yunsdw)+grouping(m.mingc),2,'',1,'小计',t.yunsdw) as yunsdw,\n" + 
        		" to_char(min(f.daohrq),'yyyy-mm-dd')||'至'||to_char(max(f.daohrq),'yyyy-mm-dd') as daohrq,\n" + 
        		" sum(f.ches) as ches,sum(biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0)) as rez_ht,\n" + 
        		"  decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar)/sum(f.biaoz)*1000/4.1816,0)) as rez_kf,\n" + 
        		"   decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar_ck)/sum(f.biaoz)*1000/4.1816,0)) as rez_ck,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as rez_cf,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))  as hetkfrzc,\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0)) as kuangfckrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as chukcfrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n"+
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as kuangfdcrzc\n" + 
        		"from fahb f,meikxxb m,zhilb z,kuangfzlb k,diancxxb dc,gongysb g,\n" + 
        		"(\n" + 
        		"select yunsdwb.mingc as yunsdw,f.id,\n" + 
        		"round_new(sum(c.maoz-c.piz-c.koud),1) as jz,\n" + 
        		"count(c.id) as cs\n" + 
        		"from fahb f,chepb c,yunsdwb\n" + 
        		"where f.id = c.fahb_id\n" + 
        		"and c.yunsdwb_id = yunsdwb.id(+)\n" + 
        		""+riqitiaoj+"" + 
        		" "+yunsdwtiaoj+"\n"+
        		"group by f.id,yunsdwb.mingc\n" + 
        		") t\n" + 
        		"where f.meikxxb_id = m.id\n" + 
        		"and f.zhilb_id = z.id(+)\n" + 
        		"and f.id = t.ID\n" + 
        		"and f.diancxxb_id=dc.id\n" + 
        		"and f.kuangfzlb_id = k.id(+)\n" + 
        		"and m.meikdq2_id=g.id(+)\n" + 
        		"AND f.diancxxb_id IN ( "+dcid+")\n" + 
        		""+riqitiaoj+"" + 
        		""+meiktiaoj+"\n"+
        		"group BY rollup(g.mingc,m.mingc,t.yunsdw)\n" + 
        		"order by grouping(g.mingc),g.mingc,grouping(m.mingc),m.mingc,grouping(t.yunsdw),t.yunsdw");
        


		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		String[][] ArrHeader = null;
		ArrHeader=new String[][] {{"地区","煤矿名称","车队","到货日期","车数","票重","净重","合同<br>热值","矿发<br>热值","出矿<br>热值","厂方<br>热值",
			"合同<br>与矿<br>发热<br>值差","矿发<br>与出<br>矿热<br>值差","出矿<br>与到<br>厂热<br>值差","矿发<br>与到<br>厂热<br>值差"}};
		int ArrWidth[]=new int[] {90,150,100,165,50,60,60,50,50,50,50,50,50,50,50};
		rt.setTitle("合同、矿发、出矿、到厂热值差统计（汇总）", ArrWidth);
		

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
		
		
		rt.setDefaultTitle(1, 5, "单位：" + baot, Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 4, this.getTongjfsValue().getValue()+":"+ this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero=true;
		for(int i=0;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		

//		设定小计行的背景色和字体
		for (int i=2;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 3);
			if((xiaoj.equals("")||xiaoj.equals("合计"))){
				for (int j=0;j<rt.body.getCols()+1;j++){
					rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
//		rt.setDefautlFooter(1, 3, "审核：", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private String getChaxun_Huiz() {
		JDBCcon con = new JDBCcon();
		
		
		String dcid = "";
		if("300".equals(getTreeid_dc())){
			dcid = " 300,301,302,303,304";
		}else{
			dcid = ""+getTreeid_dc()+"";
		}
		
		StringBuffer SQL = new StringBuffer();
		
		String riqitiaoj="";
		if(this.getTongjfsValue().getValue().equals("到货日期")){
			riqitiaoj="    and f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
		}else{
			riqitiaoj="    and f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
		}

		
        String meiktiaoj="";
        if(this.getMeikxxValue().getId()!=-1){
        	meiktiaoj="  and f.meikxxb_id ="+this.getMeikxxValue().getId();
        }
       
        String yunsdwtiaoj="";
        if(this.getYunsdwValue().getId()!=-1){
        	yunsdwtiaoj=" and yunsdwb.id="+this.getYunsdwValue().getId();
        }
       
        SQL.delete(0, SQL.length());
        SQL.append(

        		"select decode(dc.mingc,null,'总计',dc.mingc) as dcmc,\n" +
        		" decode(grouping(g.mingc)+grouping(dc.mingc),2,'',1,'厂别合计',g.mingc) as meikdq,\n" + 
        		" decode(grouping(g.mingc)+grouping(m.mingc),2,'',1,'区域合计',m.mingc) as mkmc,\n" + 
        		" decode(grouping(t.yunsdw)+grouping(m.mingc),2,'',1,'小计',t.yunsdw) as yunsdw,\n" + 
        		" to_char(min(f.daohrq),'yyyy-mm-dd')||'至'||to_char(max(f.daohrq),'yyyy-mm-dd') as daohrq,\n" + 
        		" sum(f.ches) as ches,sum(biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0)) as rez_ht,\n" + 
        		"  decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar)/sum(f.biaoz)*1000/4.1816,0)) as rez_kf,\n" + 
        		"   decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar_ck)/sum(f.biaoz)*1000/4.1816,0)) as rez_ck,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as rez_cf,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))  as hetkfrzc,\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0)) as kuangfckrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as chukcfrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n"+
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as kuangfdcrzc\n" + 
        		"from fahb f,meikxxb m,zhilb z,kuangfzlb k,diancxxb dc,gongysb g,\n" + 
        		"(\n" + 
        		"select yunsdwb.mingc as yunsdw,f.id,\n" + 
        		"round_new(sum(c.maoz-c.piz-c.koud),1) as jz,\n" + 
        		"count(c.id) as cs\n" + 
        		"from fahb f,chepb c,yunsdwb\n" + 
        		"where f.id = c.fahb_id\n" + 
        		"and c.yunsdwb_id = yunsdwb.id(+)\n" + 
        		""+riqitiaoj+""+
        		" "+yunsdwtiaoj+"\n"+
        		"group by f.id,yunsdwb.mingc\n" + 
        		") t\n" + 
        		"where f.meikxxb_id = m.id\n" + 
        		"and f.zhilb_id = z.id(+)\n" + 
        		"and f.id = t.ID\n" + 
        		"and f.diancxxb_id=dc.id\n" + 
        		"and f.kuangfzlb_id = k.id(+)\n" + 
        		"and m.meikdq2_id=g.id(+)\n" + 
        		"AND f.diancxxb_id IN ( "+dcid+")\n" + 
        		""+riqitiaoj+""+
        		""+meiktiaoj+"\n"+
        		"group BY rollup(dc.mingc,g.mingc,m.mingc,t.yunsdw)\n" + 
        		"order by grouping(dc.mingc),min(dc.id),grouping(g.mingc),g.mingc,grouping(m.mingc),m.mingc,grouping(t.yunsdw),t.yunsdw");
        


		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		String[][] ArrHeader = null;
		ArrHeader=new String[][] {{"厂别","地区","煤矿名称","车队","到货日期","车数","票重","净重","合同<br>热值","矿发<br>热值","出矿<br>热值","厂方<br>热值",
			"合同<br>与矿<br>发热<br>值差","矿发<br>与出<br>矿热<br>值差","出矿<br>与到<br>厂热<br>值差","矿发<br>与到<br>厂热<br>值差"}};
		int ArrWidth[]=new int[] {90,90,150,100,165,50,60,60,50,50,50,50,50,50,50,50};
		rt.setTitle("合同、矿发、出矿、到厂热值差统计（汇总）", ArrWidth);
		
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
		
		
		rt.setDefaultTitle(1, 5, "单位：" + baot, Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 4, this.getTongjfsValue().getValue()+":"+ this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero=true;
		for(int i=0;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		

//		设定小计行的背景色和字体
		for (int i=2;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 3);
			if((xiaoj.equals("")||xiaoj.equals("区域合计"))){
				for (int j=0;j<rt.body.getCols()+1;j++){
					rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
//		rt.setDefautlFooter(1, 3, "审核：", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private String getChaxun_mingx() {
		JDBCcon con = new JDBCcon();
		
		
		String dcid = "";
		if("300".equals(getTreeid_dc())){
			dcid = " 300,301,302,303,304";
		}else{
			dcid = ""+getTreeid_dc()+"";
		}
		
		StringBuffer SQL = new StringBuffer();
			
		String riqitiaoj="";
		if(this.getTongjfsValue().getValue().equals("到货日期")){
			riqitiaoj="    and f.daohrq>=date'"+this.getRiqi()+"'\n" + 
			          "    and f.daohrq<=date'"+this.getRiqi2()+"'\n" ;
		}else{
			riqitiaoj="    and f.fahrq>=date'"+this.getRiqi()+"'\n" + 
	                 "    and f.fahrq<=date'"+this.getRiqi2()+"'\n" ;
		}

		
        String meiktiaoj="";
        if(this.getMeikxxValue().getId()!=-1){
        	meiktiaoj="  and f.meikxxb_id ="+this.getMeikxxValue().getId();
        }
       
        String yunsdwtiaoj="";
        if(this.getYunsdwValue().getId()!=-1){
        	yunsdwtiaoj=" and yunsdwb.id="+this.getYunsdwValue().getId();
        }
        SQL.delete(0, SQL.length());
        SQL.append(

        		" select decode(dc.mingc,null,'总计',dc.mingc) as dcmc,\n" +
        		" decode(grouping(dc.mingc)+grouping(m.mingc),1,'合计',m.mingc) as mkmc,\n" + 
        		" decode(grouping(m.mingc)+grouping(t.yunsdw),1,'矿小计',t.yunsdw) as yunsdw,\n" + 
        		" decode(grouping(f.daohrq)+grouping(t.yunsdw),2,'',1,'小计',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n" +
        		"sum(f.ches) as ches,sum(biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0)) as rez_ht,\n" + 
        		"  decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar)/sum(f.biaoz)*1000/4.1816,0)) as rez_kf,\n" + 
        		"   decode(sum(f.biaoz),0,0,round_new(sum(f.biaoz*k.qnet_ar_ck)/sum(f.biaoz)*1000/4.1816,0)) as rez_ck,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as rez_cf,\n" + 
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ht)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))  as hetkfrzc,\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"  decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0)) as kuangfckrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar_ck)/sum(f.jingz)*1000/4.1816,0))-\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as chukcfrzc,\n" + 
        		"   decode(sum(f.jingz),0,0,round_new(sum(f.jingz*k.qnet_ar)/sum(f.jingz)*1000/4.1816,0))-\n"+
        		" decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)*1000/4.1816,0)) as kuangfdcrzc\n" + 
        		"from fahb f,meikxxb m,zhilb z,kuangfzlb k,diancxxb dc,\n" + 
        		"(\n" + 
        		"select yunsdwb.mingc as yunsdw,f.id,\n" + 
        		"round_new(sum(c.maoz-c.piz-c.koud),1) as jz,\n" + 
        		"count(c.id) as cs\n" + 
        		"from fahb f,chepb c,yunsdwb\n" + 
        		"where f.id = c.fahb_id\n" + 
        		"and c.yunsdwb_id = yunsdwb.id(+)\n" + 
        		""+riqitiaoj+""+
        		" "+yunsdwtiaoj+"\n"+
        		"group by f.id,yunsdwb.mingc\n" + 
        		") t\n" + 
        		"where f.meikxxb_id = m.id\n" + 
        		"and f.zhilb_id = z.id(+)\n" + 
        		"and f.id = t.ID\n" + 
        		"and f.diancxxb_id=dc.id\n" + 
        		"and f.kuangfzlb_id = k.id(+)\n" + 
        		"AND f.diancxxb_id IN ( "+dcid+")\n" + 
        		""+riqitiaoj+""+
        		""+meiktiaoj+"\n"+
        		"group BY rollup(dc.mingc,m.mingc,t.yunsdw,f.daohrq)\n" + 
        		"order by grouping(dc.mingc),min(dc.id),grouping(m.mingc),m.mingc,grouping(t.yunsdw),t.yunsdw,grouping(f.daohrq),f.daohrq"
        	);
        
        
        


		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		String[][] ArrHeader = null;
		ArrHeader=new String[][] {{"厂别","煤矿名称","车队","到货日期","车数","票重","净重","合同<br>热值","矿发<br>热值","出矿<br>热值","厂方<br>热值",
			"合同<br>与矿<br>发热<br>值差","矿发<br>与出<br>矿热<br>值差","出矿<br>与到<br>厂热<br>值差","矿发<br>与到<br>厂热<br>值差"}};
		int ArrWidth[]=new int[] {100,150,150,100,60,60,60,50,50,50,50,50,50,50,50};
		rt.setTitle("合同、矿发、出矿、到厂热值差统计（明细）", ArrWidth);
		
		

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
		
		
		rt.setDefaultTitle(1, 5, "单位：" + baot, Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 4, this.getTongjfsValue().getValue()+":"+ this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_RIGHT);
		
		
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero=true;
		for(int i=0;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		

		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();


//		设定小计行的背景色和字体
		for (int i=2;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 3);
			if((xiaoj.equals("")||xiaoj.equals("矿小计"))){
				for (int j=0;j<rt.body.getCols()+1;j++){
					rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
//		rt.setDefautlFooter(1, 3, "审核：", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save1Chick = false;

	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			
			getSelectData();
		}
		if (_Save1Chick) {
			_Save1Chick = false;
			
			getSelectData();
		}
	}

	
	
	

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
//			visit.setString1("");
//			visit.setString3("");
			setRiqi(null);
			setRiqi2(null);
			setTreeid_dc(null);
			getDiancmcModels();
			
			this.setMeikxxValue(null);
			this.getMeikxxModels();
			
			this.setYunsdwValue(null);
			this.getYunsdwModels();
			
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			
			
		}
		if (getMarkmk().equals("true")) { // 判断如果getMarkmk()返回"true"，那么重新初始化煤矿单位下拉框和编号下拉框
			this.getMeikxxModels();
			this.getYunsdwModels();
		}
		if (getMarkbh().equals("true")) { // 判断如果getMarkbh()返回"true"，那么重新初始化编号下拉框
			this.getYunsdwModels();
		}

		getSelectData();

	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	
	
	

	// 绑定日期
	boolean riqichange2 = false;

	private String riqi2;

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {

		if (this.riqi2 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;
		}

	}

	

	
	private String Markmk = "true"; // 标记煤矿单位下拉框是否被选择
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
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
		df.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI').value = newValue.dateFormat('Y-m-d'); " +
		"}");
		//df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("至"));
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI2').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		
		
		long diancxxb_id = 300;
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
		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("Meikxx");
		cb.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false';document.getElementById('Mark_bh').value = 'true'; document.Form0.submit();}");
		cb.setId("Meikxx");
		cb.setWidth(140);
		cb.setListWidth(200);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("车队:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("Yunsdw");
		cb2.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false';document.getElementById('Mark_bh').value = 'false'; document.Form0.submit();}");
		cb2.setId("Yunsdw");
		cb2.setWidth(140);
		cb2.setListWidth(200);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		
		
		tb1.addText(new ToolbarText("显示:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("BaoblxDropDown");
		cb3.setListeners("select:function(){document.Form0.submit();}");
		cb3.setId("Baoblx");
		cb3.setWidth(120);
		tb1.addField(cb3);
		tb1.addText(new ToolbarText("-"));
		

		

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);


		setToolbar(tb1);

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

	public String getcontext() {
		return "";
		
	}

	public SortMode getSort() {
		return SortMode.USER;
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
	
	

	public boolean _Meikxxchange = false;
	private IDropDownBean _MeikxxValue;

	public IDropDownBean getMeikxxValue() {
		if(_MeikxxValue==null){
			_MeikxxValue=(IDropDownBean)getMeikxxModels().getOption(0);
		}
		return _MeikxxValue;
	}

	public void setMeikxxValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikxxValue != null) {
			id = _MeikxxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Meikxxchange = true;
			} else {
				_Meikxxchange = false;
			}
		}
		_MeikxxValue = Value;
	}

	private IPropertySelectionModel _MeikxxModel;

	public void setMeikxxModel(IPropertySelectionModel value) {
		_MeikxxModel = value;
	}

	public IPropertySelectionModel getMeikxxModel() {
		if (_MeikxxModel == null) {
			getMeikxxModels();
		}
		return _MeikxxModel;
	}

	public IPropertySelectionModel getMeikxxModels() {
		//JDBCcon con = new JDBCcon();
		try{
			String meiksql=
				"select id,mingc from meikxxb mk where mk.id in (\n" +
				"select distinct f.meikxxb_id\n" + 
				"from fahb f where f.daohrq>=date'"+this.getRiqi()+"' and f.daohrq<=date'"+this.getRiqi2()+"')";
			_MeikxxModel = new IDropDownModel(meiksql,"全部");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _MeikxxModel;
	}
	
	
	
//运输单位
	public boolean _Yunsdwchange = false;
	private IDropDownBean _YunsdwValue;

	public IDropDownBean getYunsdwValue() {
		if(_YunsdwValue==null){
			_YunsdwValue=(IDropDownBean)getYunsdwModels().getOption(0);
		}
		return _YunsdwValue;
	}

	public void setYunsdwValue(IDropDownBean Value) {
		long id = -2;
		if (_YunsdwValue != null) {
			id = _YunsdwValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Yunsdwchange = true;
			} else {
				_Yunsdwchange = false;
			}
		}
		_YunsdwValue = Value;
	}

	private IPropertySelectionModel _YunsdwModel;

	public void setYunsdwModel(IPropertySelectionModel value) {
		_YunsdwModel = value;
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (_YunsdwModel == null) {
			getYunsdwModels();
		}
		return _YunsdwModel;
	}

	public IPropertySelectionModel getYunsdwModels() {
		 	String meiktiaoj="";
	        if(this.getMeikxxValue().getId()!=-1){
	        	meiktiaoj="  and f.meikxxb_id ="+this.getMeikxxValue().getId();
	        }
		try{
			String yunsdwsql=
				
				"select id,mingc from yunsdwb ys where ys.id in (\n" + 
				"select distinct c.yunsdwb_id from fahb f,chepb c\n" + 
				"where c.fahb_id=f.id\n" + 
				"and f.daohrq>= to_date('"+this.getRiqi()+"','yyyy-mm-dd')\n" + 
				"and f.daohrq<= to_date('"+this.getRiqi2()+"','yyyy-mm-dd')\n" +
				""+meiktiaoj+")" ;

			_YunsdwModel = new IDropDownModel(yunsdwsql,"全部");

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _YunsdwModel;
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
			fahdwList.add(new IDropDownBean(1,"分厂别汇总显示"));
			fahdwList.add(new IDropDownBean(2,"分地区汇总显示"));
			fahdwList.add(new IDropDownBean(3,"分厂别明细显示"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	

	

//	/统计方式
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