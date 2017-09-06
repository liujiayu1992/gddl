package com.zhiren.jt.zdt.shengcdy.hetwcqkb2;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.SysConstant;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.Radio;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hetwcqktb extends BasePage {
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
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	private String mstrReportName = "";
	private boolean blnIsBegin = false;
	private boolean blnIsBegin1 = false;

	public String getPrintTable() {//电厂
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetcx(1);//
	}
	public String getPrintTable1() {//煤矿
		if (!blnIsBegin1) {
			return "";
		}
		blnIsBegin1 = false;
		return getHetcx(2);
	}
	private String getHetcx(int flag) {//1,电厂2，煤矿
		//电厂显示自己
		//公司分公司显示下一级单位
		//区域燃料公司用shangjgsid关联而不用fuid
		// 到货量分组问题值得注意
        int jib=1,ranlgs=0;
        String diancWhere="";// 电厂数据
        String groupWhere="";//按公司分组、按电厂分组
        String biaotWhere="";//统计表头
        String fenzzd="";
		String sql="select jib,diancxxb.ranlgs\n" +
		"from diancxxb\n" + 
		"where id="+getTreeid();
		JDBCcon con=new JDBCcon();
		ResultSet rs1=con.getResultSet(sql);
		try {
			while(rs1.next()){
				jib=rs1.getInt("jib") ;
				ranlgs=rs1.getInt("ranlgs") ;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return "";
		}
		con.Close();
		if(gettubSelectValue().getId()==1){//到货率
			if(flag==1){
//				电厂条件与分组
				if(jib==2||jib==3){//如果是电厂或区域()或燃料公司
					fenzzd="diancxxb_id ";
					groupWhere=" group by "+fenzzd;
					if(jib==2&&ranlgs==1){//燃料公司
						diancWhere=" and diancxxb.shangjgsid="+getTreeid();
						biaotWhere=" where diancxxb.shangjgsid="+getTreeid();
					}else if(jib==3){//如果是电厂
						biaotWhere=" where diancxxb.id="+getTreeid();
						diancWhere=" and diancxxb.id="+getTreeid();
					}else if(jib==2&&ranlgs!=1){//区域公司
						biaotWhere=" where diancxxb.fuid="+getTreeid();
						diancWhere=" and diancxxb.fuid="+getTreeid();
					}else{
						return "";
					}
				}else if(jib==1){//如果是集团只显示分公司（赞不显示燃料公司）
//					diancWhere=" and diancxxb.fuid="+getTreeid();所有电厂
					biaotWhere=" where ranlgs<>1 and diancxxb.fuid="+getTreeid();
					fenzzd="fuid ";
					groupWhere=" group by  "+fenzzd;
				}else{
					return "";
				}
			    sql=
					"select dianc.mingc,dianc.fenx,decode(jihlb.jihl,null,0,0,0,decode(shisl.daohl,null,0,round(shisl.daohl/(jihlb.jihl*10000)*100,2))) daohl\n" + 
					"from\n" + 
					"(select "+fenzzd+"diancxxb_id,max(fenx)fenx,sum(daohl)daohl from(select fh.diancxxb_id,'同期'fenx,max(fuid)fuid ,"+SysConstant.LaimField+" daohl--实到分组标准，有待改进\n" + 
					"from fahb fh,diancxxb\n" + 
					"where fh.jihkjb_id=1 and fh.diancxxb_id=diancxxb.id and fh.daohrq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and fh.daohrq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)\n" + 
					 diancWhere+
					"group by fh.diancxxb_id,fh.lieid) \n" + groupWhere+
					
					"union\n" + 
					"select "+fenzzd+"diancxxb_id,max(fenx)fenx,sum(daohl)daohl from(select fh.diancxxb_id,'本期'fenx,max(fuid)fuid ,"+SysConstant.LaimField+" daohl--实到分组标准，有待改进\n" + 
					"from fahb fh,diancxxb\n" + 
					"where  fh.jihkjb_id=1 and fh.diancxxb_id=diancxxb.id and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
					 diancWhere+
					"group by fh.diancxxb_id,fh.lieid )"+groupWhere+")shisl,\n" + 
					
					"(\n" + 
					"select "+fenzzd+"diancxxb_id,'同期'fenx,sum(j.hej) jihl\n" + 
					"from niandhtqkb j,diancxxb\n" + 
					"where  j.jihkjb_id=1 and j.diancxxb_id=diancxxb.id  and j.riq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and j.riq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)\n" + 
					 diancWhere+groupWhere + 
					" union\n" + 
					"select "+fenzzd+"diancxxb_id,'本期'fenx,sum(j.hej) jihl\n" + 
					"from niandhtqkb j,diancxxb\n" + 
					"where   j.jihkjb_id=1 and j.diancxxb_id=diancxxb.id and j.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
					 diancWhere+groupWhere+")jihlb,\n" + 
					"(\n" + 
					"select id,mingc,vwfenx_tongqbq.fenx\n" + 
					"from diancxxb,vwfenx_tongqbq  \n" + 
					biaotWhere+
					")dianc\n" + 
					"where dianc.id=jihlb.diancxxb_id(+) and dianc.id=shisl.diancxxb_id(+)and dianc.fenx=jihlb.fenx(+) and dianc.fenx=shisl.fenx(+)";
				
			}else{//2，煤矿
//				电厂条件与分组
				if(jib==2||jib==3){//如果是电厂或区域()或燃料公司
					fenzzd="dqmc ";
					groupWhere=" group by "+fenzzd;
					if(jib==2&&ranlgs==1){//燃料公司
						diancWhere=" and diancxxb.shangjgsid="+getTreeid();
						biaotWhere=" where diancxxb.shangjgsid="+getTreeid();
					}else if(jib==3){//如果是电厂
						biaotWhere=" where diancxxb.id="+getTreeid();
						diancWhere=" and diancxxb.id="+getTreeid();
					}else if(jib==2&&ranlgs!=1){//区域公司
						biaotWhere=" where diancxxb.fuid="+getTreeid();
						diancWhere=" and diancxxb.fuid="+getTreeid();
					}else{
						return "";
					}
				}else if(jib==1){//如果是集团只显示分公司（赞不显示燃料公司）
//					diancWhere=" and diancxxb.fuid="+getTreeid();所有电厂
					biaotWhere=" where ranlgs<>1 and diancxxb.fuid="+getTreeid();
					fenzzd="dqmc ";
					groupWhere=" group by  "+fenzzd;
				}else{
					return "";
				}
			    sql=
					"select biaot.dqmc mingc,biaot.fenx,decode(jihlb.jihl,null,0,0,0,decode(shisl.daohl,null,0,round(shisl.daohl/(jihlb.jihl*10000)*100,2))) daohl\n" + 
					"from\n" + 
					"(select "+fenzzd+"diancxxb_id,max(fenx)fenx,sum(daohl)daohl from(select fh.diancxxb_id,'同期'fenx,max(fuid)fuid ,max(g.dqmc)dqmc,"+SysConstant.LaimField+" daohl--实到分组标准，有待改进\n" + 
					"from fahb fh,diancxxb,vwgongys g\n" + 
					"where fh.gongysb_id=g.id and fh.diancxxb_id=diancxxb.id and fh.daohrq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and fh.daohrq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)\n" + 
					 diancWhere+
					"group by fh.diancxxb_id,fh.lieid) \n" + groupWhere+
					
					"union\n" + 
					"select "+fenzzd+"diancxxb_id,max(fenx)fenx,sum(daohl)daohl from(select fh.diancxxb_id,'本期'fenx,max(fuid)fuid ,max(g.dqmc)dqmc,"+SysConstant.LaimField+" daohl--实到分组标准，有待改进\n" + 
					"from fahb fh,diancxxb,vwgongys g\n" + 
					"where fh.gongysb_id=g.id and fh.diancxxb_id=diancxxb.id and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
					 diancWhere+
					"group by fh.diancxxb_id,fh.lieid )"+groupWhere+")shisl, \n" + 
					
					"(\n" + 
					"select "+fenzzd+"diancxxb_id,'同期'fenx,sum(getHetl_Sjd(ADD_MONTHS(to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd'), -12),ADD_MONTHS(to_date('"+getEndriqDate()+"', 'yyyy-mm-dd'), -12),j.riq,j.hej)) jihl\n" + 
					"from niandhtqkb j,diancxxb,vwgongys g\n" + 
					"where  j.gongysb_id=g.id and j.diancxxb_id=diancxxb.id  and j.riq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and j.riq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12)\n" + 
					 diancWhere+groupWhere + 
					" union\n" + 
					"select "+fenzzd+"diancxxb_id,'本期'fenx,sum(getHetl_Sjd(to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd'),to_date('"+getEndriqDate()+"', 'yyyy-mm-dd'),j.riq,j.hej)) jihl\n" + 
					"from niandhtqkb j,diancxxb,vwgongys g\n" + 
					"where   j.gongysb_id=g.id and j.diancxxb_id=diancxxb.id and j.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
					 diancWhere+groupWhere+")jihlb,\n" +
					 //表头，注意时间段为同期开始到本期结束
					 "(select dqmc,vwfenx_tongqbq.fenx\n" +
					 "from (\n" + 
					 "select vwgongys.dqmc\n" + 
					 "from niandhtqkb,vwgongys,diancxxb\n" + 
					 "where niandhtqkb.jihkjb_id=1 and niandhtqkb.diancxxb_id=diancxxb.id "+diancWhere+" and niandhtqkb.gongysb_id=vwgongys.id  and ((niandhtqkb.riq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and niandhtqkb.riq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12))" + 
					 "or (niandhtqkb.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and niandhtqkb.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')))"+
					 "union\n" + 
					 "select vwgongys.dqmc\n" + 
					 "from fahb fh,vwgongys,diancxxb\n" + 
					 "where fh.jihkjb_id=1 and  fh.diancxxb_id=diancxxb.id "+diancWhere+"  and fh.gongysb_id=vwgongys.id and ((fh.daohrq>=ADD_MONTHS(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'),-12) and fh.daohrq<=ADD_MONTHS(to_date('"+getEndriqDate()+"','yyyy-mm-dd'),-12))" + 
					 "or (fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')))"+
					 "),vwfenx_tongqbq)biaot\n" + 
					 
					 "where biaot.dqmc=shisl.diancxxb_id(+) and biaot.dqmc=jihlb.diancxxb_id(+)\n" + 
					 "and biaot.fenx=shisl.fenx(+) and biaot.fenx=jihlb.fenx(+)";

			};
			JDBCcon cn = new JDBCcon();
			ResultSet rs = cn.getResultSet(sql);
			Chart ct = new Chart();
			ChartData cd = new ChartData();
			CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "daohl");
			cn.Close();
//			ct.intDigits=2;				//	显示小数位数
			ct.barItemMargin=-0.05;
			ct.barLabelsFontbln = true;
			ct.showXvalue = true;
			ct.showYvalue = true;
			ct.showLegend = true;
			ct.xTiltShow=true;
			ct.barfontPlace=ItemLabelAnchor.OUTSIDE1;
			((Visit) getPage().getVisit()).setString1(ct.ChartBar3D(getPage(),dataset, "煤炭到货率", 800, 400));
		}else{//供煤比例
			if(flag==1){
//				电厂条件与分组
				if(jib==2||jib==3){//如果是电厂或区域()或燃料公司
					fenzzd="diancxxb_id ";
					groupWhere=" group by "+fenzzd;
					if(jib==2&&ranlgs==1){//燃料公司
						diancWhere=" and diancxxb.shangjgsid="+getTreeid();
						biaotWhere=" where diancxxb.shangjgsid="+getTreeid();
					}else if(jib==3){//如果是电厂
						biaotWhere=" where diancxxb.id="+getTreeid();
						diancWhere=" and diancxxb.id="+getTreeid();
					}else if(jib==2&&ranlgs!=1){//区域公司
						biaotWhere=" where diancxxb.fuid="+getTreeid();
						diancWhere=" and diancxxb.fuid="+getTreeid();
					}else{
						return "";
					}
				}else if(jib==1){//如果是集团只显示分公司（赞不显示燃料公司）
//					diancWhere=" and diancxxb.fuid="+getTreeid();所有电厂
					biaotWhere=" where ranlgs<>1 and diancxxb.fuid="+getTreeid();
					fenzzd="fuid ";
					groupWhere=" group by  "+fenzzd;
				}else{
					return "";
				}
				sql="select diancxxb.mingc, daohl from (select "+fenzzd+"diancxxb_id,sum(daohl)daohl\n" +
				"from (\n" + 
				"select fh.diancxxb_id,'本期'fenx,max(fuid)fuid ,max(g.dqmc)dqmc,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0))  daohl\n" + 
				"from fahb fh,diancxxb,vwgongys g\n" + 
				"where fh.jihkjb_id=1 and  fh.gongysb_id=g.id and fh.diancxxb_id=diancxxb.id and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
				 diancWhere+
				"group by fh.diancxxb_id,fh.lieid\n" + 
				")\n" + 
				groupWhere+")shuj,diancxxb where shuj.diancxxb_id=diancxxb.id";			
			}else{//2，煤矿
				if(jib==2||jib==3){//如果是电厂或区域()或燃料公司
					fenzzd="dqmc ";
					groupWhere=" group by "+fenzzd;
					if(jib==2&&ranlgs==1){//燃料公司
						diancWhere=" and diancxxb.shangjgsid="+getTreeid();
						biaotWhere=" where diancxxb.shangjgsid="+getTreeid();
					}else if(jib==3){//如果是电厂
						biaotWhere=" where diancxxb.id="+getTreeid();
						diancWhere=" and diancxxb.id="+getTreeid();
					}else if(jib==2&&ranlgs!=1){//区域公司
						biaotWhere=" where diancxxb.fuid="+getTreeid();
						diancWhere=" and diancxxb.fuid="+getTreeid();
					}else{
						return "";
					}
				}else if(jib==1){//如果是集团只显示分公司（赞不显示燃料公司）
//					diancWhere=" and diancxxb.fuid="+getTreeid();所有电厂
					biaotWhere=" where ranlgs<>1 and diancxxb.fuid="+getTreeid();
					fenzzd="dqmc ";
					groupWhere=" group by  "+fenzzd;
				}else{
					return "";
				}
				sql="select "+fenzzd+"mingc,sum(daohl)daohl\n" +
				"from (\n" + 
				"select fh.diancxxb_id,'本期'fenx,max(fuid)fuid ,max(g.dqmc)dqmc,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0))  daohl\n" + 
				"from fahb fh,diancxxb,vwgongys g\n" + 
				"where  fh.jihkjb_id=1 and fh.gongysb_id=g.id and fh.diancxxb_id=diancxxb.id and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
				 diancWhere+
				"group by fh.diancxxb_id,fh.lieid\n" + 
				")\n" + 
				groupWhere+"";	
			};
			JDBCcon cn = new JDBCcon();
			ResultSet rs = cn.getResultSet(sql);
			Chart ct = new Chart();
			ChartData cd = new ChartData();
			DefaultPieDataset dataset = cd.getRsDataPie(rs, "mingc",  "daohl",true);
			cn.Close();
			ct.pieLabGenerator=ct.piedatformat2;//显示百分比
			ct.showLegend=true;
			ct.pieLabFormat=true;
			((Visit) getPage().getVisit()).setString1(ct.ChartPie3D(getPage(),dataset, "供煤比例", 800, 400));
		}
		
		return((Visit) getPage().getVisit()).getString1();
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
	private void getToolbars(){
		
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		需方
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
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
		//日期
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		df.setId("riq1");
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		df1.setId("riq2");
		tb1.addField(df1);
		
		tb1.addText(new ToolbarText("图表类型:"));
		ComboBox comb2=new ComboBox();
		comb2.setId("tub");
		comb2.setWidth(100);
		comb2.setTransform("tubSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		tb1.addField(comb2);
		ToolbarButton tbb = new ToolbarButton(null,"刷新","function(){document.forms[0].submit()}");
		ComboBox comb1=new ComboBox();
		comb2.setId("tub");
		comb2.setWidth(100);
		comb2.setTransform("tubSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		tb1.addItem(tbb);
		tb1.addFill();
		Radio rd1 = new Radio("kouj");
		rd1.setBoxLabel("电厂");
		rd1.setId("rd1");
		tb1.addField(rd1);
		Radio rd2 = new Radio("kouj");
		rd2.setBoxLabel("煤矿");
		rd2.setId("rd2");
		rd2.setChecked(false);
//		rd2.setListeners("onclick:function(own,newValue,oldValue){"+
//                "if(document.all.item('mag1').display.value=='none'){document.all.item('mag1').display.value=='';document.all.item('mag2').display.value=='none'}" +
//                "else {document.all.item('mag2').display.value=='';document.all.item('mag1').display.value=='none'}}");
	
		tb1.addField(rd2);
		
		if(getZhuangt()==1){//电厂
			rd2.setListeners("");
			rd2.setChecked(false);
			rd1.setChecked(true);
			rd1.setListeners("check :function(cbox ,value){"+
	                "if(value){document.all.item('Zhuangt').value=1;document.all.item('mag1').style.display='';document.all.item('mag2').style.display='none'}" +
	                "else {document.all.item('Zhuangt').value=0;document.all.item('mag2').style.display='';document.all.item('mag1').style.display='none'}}");
		
		}else{
			rd1.setListeners("");
			rd1.setChecked(false);
			rd2.setChecked(true);
			rd2.setListeners("check :function(cbox ,value){"+
	                "if(value){document.all.item('Zhuangt').value=0;document.all.item('mag2').style.display='';document.all.item('mag1').style.display='none'}" +
	                "else {document.all.item('Zhuangt').value=1;document.all.item('mag1').style.display='';document.all.item('mag2').style.display='none'}}");
		
		}
//		tb1.addText(new ToolbarText("<a  href=\"#\" onclick=window.open(\\\""+MainGlobal.getHomeContext(this)+"/app?service=page/Hetwcqkb2&riq1='+document.getElementById('qiandrq1').value+'&riq2='+document.getElementById('qiandrq2').value+'\"'+',\"_self\")>报表查询</a>"));
		tb1.addText(new ToolbarText("<a  href=# onclick=openLink()>报表查询</a>"));
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
		
			visit.setInt1(1);
			visit.setString1("");
			if(cycle.getRequestContext().getParameter("riq1")==null){//如果不是连接返回
				this.setBeginriqDate(""+DateUtil.getYear(visit.getMorkssj())+"-01-01");
				this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
			}else{
				visit.setString4(cycle.getRequestContext().getParameter("riq1"));
				visit.setString5(cycle.getRequestContext().getParameter("riq2"));
			}
		}
		getToolbars();
		blnIsBegin = true;
		blnIsBegin1 = true;
	}

    
 
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(_value);
	}
	    
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
				stra.set(DateUtil.getYear(new Date()), 0, 1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	 //图表
    public IDropDownBean gettubSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)gettubSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void settubSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void settubSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel gettubSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            gettubSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void gettubSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"到货率"));
        list.add(new IDropDownBean(2,"供煤比例"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
	 //ext
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
		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
		
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}