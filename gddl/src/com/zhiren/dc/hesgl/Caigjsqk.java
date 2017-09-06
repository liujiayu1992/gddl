package com.zhiren.dc.hesgl;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;


public class Caigjsqk extends BasePage {
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




	public String getPrintTable() {
		setMsg(null);
        	return  getCaigqkcx();
	}

	private String getCaigqkcx() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();

		StringBuffer sbsql = new StringBuffer();
        sbsql.append(
        		"select fx.mingc,fx.fenx,sj.jiessl,sj.jiesrl,sj.buhsmk,sj.buhsdj,sj.yf," +
        		"sj.zf,round((sj.buhsdj+sj.yf+sj.zf)*29.271/sj.jiesrl/4.1816*1000,2) bmdj,sj.rcsl,sj.rczl,sj.guohl,sj.yk,sj.yuns from\n" +
        		"(select g.mingc,'累计' fenx,\n" + 
        		"       round_new(sum(j.jiessl), 2) jiessl,\n" + 
        		"       decode(round_new(sum(j.jiessl), 2),0,0, round_new(sum(Mjkg_to_kcalkg(j.jiesrl,2)*j.jiessl*1000)/round_new(sum(j.jiessl*1000), 2), 2) )jiesrl,\n" + 
        		"       round_new(sum(j.buhsmk*j.jiessl)/sum(j.jiessl)/10000, 2) buhsmk,\n" + 
        		"       round_new(sum(j.buhsdj*j.jiessl)/sum(j.jiessl), 2) buhsdj,\n" + 
        		"       round_new(sum(jy.guotyf + jy.kuangqyf)/sum(j.jiessl), 2) yf,\n" + 
        		"       round_new(sum(jy.guotzf + jy.kuangqzf)/sum(j.jiessl), 2) zf,\n" + 
        		"       round_new(sum(j.guohl), 2) guohl,\n" + 
        		//"       round_new(sum((j.buhsdj+jy.guotyf + jy.kuangqyf+jy.guotzf + jy.kuangqzf)*29.271/jiesrl),2)bmdj,\n" +
 
        		"       round_new(sum(j.yingd - j.kuid), 2) yk,\n" + 
        		"       round_new(sum(j.yuns), 2) yuns,\n" + 
        		"       round_new(sum(zbs.changf), 2) rcsl,\n" + 
        		"       decode(round_new(sum(zbs.changf), 2),0,0 ,round_new(sum(zbl.changf*zbs.changf*1000)/round_new(sum(zbs.changf)*1000, 2), 2)) rczl\n" + 
        		"  from jiesb j, jiesyfb jy, meikxxb m, gongysb g,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=1 )zbs,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=2 )zbl\n" + 
        		" where j.bianm = jy.bianm(+)\n" + 
        		"   and m.id = j.meikxxb_id\n" + 
        		"   and m.meikdq_id = g.id\n" + 
        		"   and zbs.jiesdid=j.id\n" + 
        		"   and zbl.jiesdid=j.id\n" + 
        		"   and j.diancxxb_id="+getTreeid_dc()+
        		"   and j.ruzrq <= to_date('"+getRiqi()+"', 'yyyy-mm-dd') and j.ruzrq>= (SELECT to_date(to_char(to_date('"+getRiqi()+"','yyyy-mm-dd'),'yyyy')||'-01-01','yyyy-mm-dd') FROM dual)\n" + 
        		"   group by rollup(g.mingc)\n" + 
        		"having not grouping(g.mingc)=1\n" + 
        		"union\n" + 
        		"select g.mingc,'月' fenx,\n" + 
        		"       round_new(sum(j.jiessl), 2) jiessl,\n" + 
        		"       decode(round_new(sum(j.jiessl), 2),0,0, round_new(sum(Mjkg_to_kcalkg(j.jiesrl,2)*j.jiessl*1000)/round_new(sum(j.jiessl*1000), 2), 2) )jiesrl,\n" + 
        		"       round_new(sum(j.buhsmk*j.jiessl)/sum(j.jiessl)/10000, 2) buhsmk,\n" + 
        		"       round_new(sum(j.buhsdj*j.jiessl)/sum(j.jiessl), 2) buhsdj,\n" + 
        		"       round_new(sum(jy.guotyf + jy.kuangqyf)/sum(j.jiessl), 2) yf,\n" + 
        		"       round_new(sum(jy.guotzf + jy.kuangqzf)/sum(j.jiessl), 2) zf,\n" + 
        		"       round_new(sum(j.guohl), 2) guohl,\n" + 
        		//"       round_new(sum((j.buhsdj+jy.guotyf + jy.kuangqyf+jy.guotzf + jy.kuangqzf)*29.271/jiesrl),2)bmdj,\n" +
        		"       round_new(sum(j.yingd - j.kuid), 2) yk,\n" + 
        		"       round_new(sum(j.yuns), 2) yuns,\n" + 
        		"       round_new(sum(zbs.changf), 2) rcsl,\n" + 
        		"       decode(round_new(sum(zbs.changf), 2),0,0 ,round_new(sum(zbl.changf*zbs.changf*1000)/round_new(sum(zbs.changf)*1000, 2), 2)) rczl\n" + 
        		"  from jiesb j, jiesyfb jy, meikxxb m, gongysb g,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=1 )zbs,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=2 )zbl\n" + 
        		" where j.bianm = jy.bianm(+)\n" + 
        		"   and m.id = j.meikxxb_id\n" + 
        		"   and m.meikdq_id = g.id\n" + 
        		"   and zbs.jiesdid=j.id\n" + 
        		"   and zbl.jiesdid=j.id\n" + 
        		"   and j.diancxxb_id="+getTreeid_dc()+
        		"   and j.ruzrq <= to_date('"+getRiqi()+"', 'yyyy-mm-dd') and j.ruzrq >=(SELECT to_date(to_char(to_date('"+getRiqi()+"','yyyy-mm-dd'),'yyyy-mm')||'-01','yyyy-mm-dd') FROM dual)\n" + 
        		"   group by rollup(g.mingc)\n" + 
        		"having not grouping(g.mingc)=1\n" + 
        		"union\n" + 
        		"\n" + 
        		"select g.mingc,'周' fenx,\n" + 
        		"       round_new(sum(j.jiessl), 2) jiessl,\n" + 
        		"       decode(round_new(sum(j.jiessl), 2),0,0, round_new(sum(Mjkg_to_kcalkg(j.jiesrl,2)*j.jiessl*1000)/round_new(sum(j.jiessl*1000), 2), 2) )jiesrl,\n" + 
        		"       round_new(sum(j.buhsmk*j.jiessl)/sum(j.jiessl)/10000, 2) buhsmk,\n" + 
        		"       round_new(sum(j.buhsdj*j.jiessl)/sum(j.jiessl), 2) buhsdj,\n" + 
        		"       round_new(sum(jy.guotyf + jy.kuangqyf)/sum(j.jiessl), 2) yf,\n" + 
        		"       round_new(sum(jy.guotzf + jy.kuangqzf)/sum(j.jiessl), 2) zf,\n" + 
        		"       round_new(sum(j.guohl), 2) guohl,\n" + 
        		//"       round_new(sum((j.buhsdj+jy.gubotyf + jy.kuangqyf+jy.guotzf + jy.kuangqzf)*29.271/jiesrl),2)bmdj,\n" +
        		"       round_new(sum(j.yingd - j.kuid), 2) yk,\n" + 
        		"       round_new(sum(j.yuns), 2) yuns,\n" + 
        		"       round_new(sum(zbs.changf), 2) rcsl,\n" + 
        		"       decode(round_new(sum(zbs.changf), 2),0,0 ,round_new(sum(zbl.changf*zbs.changf*1000)/round_new(sum(zbs.changf)*1000, 2), 2)) rczl\n" + 
        		"  from jiesb j, jiesyfb jy, meikxxb m, gongysb g,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=1 )zbs,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=2 )zbl\n" + 
        		" where j.bianm = jy.bianm(+)\n" + 
        		"   and m.id = j.meikxxb_id\n" + 
        		"   and m.meikdq_id = g.id\n" + 
        		"   and zbs.jiesdid=j.id\n" + 
        		"   and zbl.jiesdid=j.id\n" + 
        		"   and j.diancxxb_id="+getTreeid_dc()+
        		"   and j.ruzrq <= to_date('"+getRiqi()+"', 'yyyy-mm-dd') and j.ruzrq>=getDT_WeeekStart(to_date('"+getRiqi()+"', 'yyyy-mm-dd'))\n" + 
        		"\n" + 
        		"   group by rollup(g.mingc)\n" + 
        		"having not grouping(g.mingc)=1\n" + 
        		"union\n" + 
        		"select g.mingc,'日' fenx,\n" + 
        		"       round_new(sum(j.jiessl), 2) jiessl,\n" + 
        		"       decode(round_new(sum(j.jiessl), 2),0,0, round_new(sum(Mjkg_to_kcalkg(j.jiesrl,2)*j.jiessl*1000)/round_new(sum(j.jiessl*1000), 2), 2) )jiesrl,\n" + 
        		"       round_new(sum(j.buhsmk*j.jiessl)/sum(j.jiessl)/10000, 2) buhsmk,\n" + 
        		"       round_new(sum(j.buhsdj*j.jiessl)/sum(j.jiessl), 2) buhsdj,\n" + 
        		"       round_new(sum(jy.guotyf + jy.kuangqyf)/sum(j.jiessl), 2) yf,\n" + 
        		"       round_new(sum(jy.guotzf + jy.kuangqzf)/sum(j.jiessl), 2) zf,\n" + 
        		"       round_new(sum(j.guohl), 2) guohl,\n" + 
        		//"       round_new(sum((j.buhsdj+jy.guotyf + jy.kuangqyf+jy.guotzf + jy.kuangqzf)*29.271/jiesrl),2)bmdj,\n" +
        		"       round_new(sum(j.yingd - j.kuid), 2) yk,\n" + 
        		"       round_new(sum(j.yuns), 2) yuns,\n" + 
        		"       round_new(sum(zbs.changf), 2) rcsl,\n" + 
        		"       decode(round_new(sum(zbs.changf), 2),0,0 ,round_new(sum(zbl.changf*zbs.changf*1000)/round_new(sum(zbs.changf)*1000, 2), 2)) rczl\n" + 
        		"  from jiesb j, jiesyfb jy, meikxxb m, gongysb g,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=1 )zbs,\n" + 
        		"(select jieszbsjb.jiesdid,changf from jieszbsjb where zhibb_id=2 )zbl\n" + 
        		" where j.bianm = jy.bianm(+)\n" + 
        		"   and m.id = j.meikxxb_id\n" + 
        		"   and m.meikdq_id = g.id\n" + 
        		"   and zbs.jiesdid=j.id\n" + 
        		"   and zbl.jiesdid=j.id\n" + 
        		"   and j.diancxxb_id="+getTreeid_dc()+
        		"   and j.ruzrq = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
        		"   group by rollup(g.mingc)\n" + 
        		"having not grouping(g.mingc)=1)sj,\n" + 
        		"(select distinct '日' fenx,g.mingc from jiesb j,gongysb g,meikxxb m\n" +
					"where j.meikxxb_id=m.id and m.meikdq_id=g.id\n" + 
					"union\n" + 
					"select distinct '周' fenx,g.mingc from jiesb j,gongysb g,meikxxb m\n" + 
					"where j.meikxxb_id=m.id and m.meikdq_id=g.id\n" + 
					"union\n" + 
					"select distinct '月' fenx,g.mingc from jiesb j,gongysb g,meikxxb m\n" + 
					"where j.meikxxb_id=m.id and m.meikdq_id=g.id\n" + 
					"  union\n" + 
					"select distinct '累计' fenx,g.mingc from jiesb j,gongysb g,meikxxb m\n" + 
					"where j.meikxxb_id=m.id and m.meikdq_id=g.id"+")fx\n" + 
        		"where sj.fenx(+)=fx.fenx\n" + 
        		"and sj.mingc(+)=fx.mingc \n" + 
        		"order by fx.mingc,decode(fx.fenx,'日',1,'周',2,'月',3,4 )\n" );

		ResultSet rs = cn.getResultSet(sbsql.toString());
		 String ArrHeader[][]=new String[2][14];
		 ArrHeader[0]=new String[] {"矿别地区","时间","结算数量","结算热值","结算价款（不含税）","结算单价（不含税）","结算运费","结算杂费","结算标煤单价（不含税）","入厂数量","入厂热值","净过衡数量","盈亏数量","运损数量"};
		 ArrHeader[1]=new String[] {"矿别地区","时间","吨","大卡/千克","万元","元/吨","元/吨","元/吨","元/吨","吨","大卡/千克","吨","吨","吨"};

		 int ArrWidth[]=new int[] {100,60,80,80,80,80,80,80,80,80,80,80,80,80};


		rt.setTitle("燃料采购及结算情况(分矿)统计表", ArrWidth);
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
     	rt.body.mergeFixedCol(2);

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
//		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(40);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

		rt.setDefautlFooter(2, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
	}


//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())){
			visit.setActivePageName(getPageName().toString());
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			getGongysDropDownModels();


			setRiqi(null);

			visit.setboolean3(false);
			
	

		}
		
		getSelectData();
	
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}



//	获取供应商
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
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    
	public void getSelectData() {
		
		Visit visit=(Visit)getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("入账日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));


//		电厂
		DefaultTree dt2 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt2);
		TextField tf2 = new TextField();
		tf2.setId("diancTree_text");
		tf2.setWidth(100);
		tf2.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));
		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf2);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

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
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid="0";
		}
		return treeid;
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
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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
}
