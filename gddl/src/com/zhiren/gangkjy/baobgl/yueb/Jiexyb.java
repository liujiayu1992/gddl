package com.zhiren.gangkjy.baobgl.yueb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者:童忠付
 * 时间:2009-3-31
 * 内容:报表管理 接卸月报
 */
public class Jiexyb extends BasePage implements PageValidateListener {

	private static final String BAOBPZB_GUANJZ = "BB_YB_JX";// baobpzb中对应的关键字   报表管理 月报 接卸月报
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

	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_FindChick) {
			_FindChick = false;
	    }
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private String TAIZ = "Zhiltz";

	private String YUEB = "Zhilyb";

	

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		
			return getZhiltz();
		}
	

	// 全部编码
	/*
	 * 将检质数量由jingz改为发货表laimsl字段 并对弹筒热值、发热量（MJ）、发热量（Kcal）进行修约
	 * 修改时间：2008-12-04
	 * 修改人：王磊
	 */
	
	private StringBuffer getBaseSql(){
		String strDianc = "";
		if(this.getDiancTreeJib()==1){//集团
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		StringBuffer buffer = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String date1=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String date2=getNianfValue().getValue()+"-01-01";
		buffer.append(
		"select decode(grouping(tb.fahr),1,'总计',tb.fahr) as fahr,\n" +
		"       decode(grouping(tb.pinz)+grouping(tb.fahr),1,'合计',tb.pinz) as pinz,\n" + 
		"       tb.xiangmu as xiangmu,\n" + 
		"       sum(s.ches) as ches,\n" + 
		"       sum(s.jingz) as jingz,\n" + 
		"       sum(s.biaoz) as biaoz,\n" + 
		"       sum(s.yingk) as yingk,\n" + 
		"       sum(s.yuns) as yuns,\n" + 
		"       decode(sum(s.jingz), 0, 0, round_new(sum(s.mt) / sum(s.jingz), 1)) as mt,\n" + 
		"       decode(sum(s.jingz),0,0, round_new(sum(s.mad) / sum(s.jingz), 2)) as mad,\n" + 
		"       decode(sum(s.jingz),0,0, round_new(sum(s.aad) / sum(s.jingz), 2)) as aad,\n" + 
		"       decode(sum(s.jingz), 0,0,round_new(sum(s.vdaf) / sum(s.jingz), 2)) as vdaf,\n" + 
		"       decode(sum(s.jingz), 0, 0,round_new(sum(s.stad) / sum(s.jingz), 2)) as stad,\n" + 
		"       decode(sum(round_new(s.jingz,0)),0, 0,round_new(sum(s.had) / sum(s.jingz), 2)) as had,\n" + 
		"       decode(sum(round_new(s.jingz,0)), 0,0,round_new(sum(s.qgrd) / sum(s.jingz), 2)) as qgrd\n" + 
		"       from (\n" + 
		"select dw.fahr as fahr,\n" + 
		" dw.pinz as pinz,\n" + 
		"       decode(1,1,'本月','本月') as xiangmu,\n" + 
		"       sum(round_new(f.ches,"+v.getShuldec()+")) ches,\n" + 
		"       sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n" + 
		"       sum(round_new(f.biaoz,"+v.getShuldec()+")) biaoz,\n" + 
		"       sum(round_new(f.yingk,"+v.getShuldec()+")) yingk,\n" + 
		"       sum(round_new(f.yuns,"+v.getShuldec()+")) yuns,\n" + 
		"       sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) as mt,\n" + 
		"       sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+"))  as mad,\n" + 
		"       sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) as aad,\n" + 
		"       sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+"))  as vdaf,\n" + 
		"       sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) as stad,\n" + 
		"       sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) as had,\n" + 
		"       sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) as qgrd\n" + 
		"from duowkcb dw,duimxxb dm,fahb f,zhillsb z,caiyb ca,caiylbb cb,diancxxb dc\t" +
		"where z.caiyb_id=ca.id and ca.caiylbb_id=cb.id and cb.bianm='JC'\n" + 
		"and  dw.duiqm_id=dm.id and dm.fahb_id=f.id and f.zhilb_id=z.zhilb_id\n" + 
		"and dw.diancxxb_id=dc.id"+ strDianc+"\n" + 
		"and dw.leix=1 and dm.ruksj> = to_date('"+date1+"','yyyy-MM-dd') and dm.ruksj<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
		"group by  (dw.fahr,dw.pinz)\n" + 
		"union\n" + 
		"select dw.fahr as fahr,\n" + 
		" dw.pinz as pinz,\n" + 
		"       decode(1,1,'累计','累计') as xiangmu,\n" + 
		"       sum(round_new(f.ches,"+v.getShuldec()+")) ches,\n" + 
		"       sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n" + 
		"       sum(round_new(f.biaoz,"+v.getShuldec()+")) biaoz,\n" + 
		"       sum(round_new(f.yingk,"+v.getShuldec()+")) yingk,\n" + 
		"       sum(round_new(f.yuns,"+v.getShuldec()+")) yuns,\n" + 
		"       sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) as mt,\n" + 
		"       sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+"))  as mad,\n" + 
		"       sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) as aad,\n" + 
		"       sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+"))  as vdaf,\n" + 
		"       sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) as stad,\n" + 
		"       sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) as had,\n" + 
		"       sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) as qgrd\n" + 
		"from duowkcb dw,duimxxb dm,fahb f,zhillsb z,caiyb ca,caiylbb cb, diancxxb dc\t\t\t\t" +
		"where z.caiyb_id=ca.id and ca.caiylbb_id=cb.id and cb.bianm='JC'\n" + 
		" and dw.duiqm_id=dm.id and dm.fahb_id=f.id and f.zhilb_id=z.zhilb_id        " +
		" and dw.diancxxb_id= dc.id  and dc.id=501\n" + 
		"and dw.leix=1 and dm.ruksj> = to_date('"+date2+"','yyyy-MM-dd') and dm.ruksj<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
		"group by (dw.fahr,dw.pinz)\n" + 
		"\t) s,\n" + 
		"  (select * from (select dw.fahr as fahr, dw.pinz as pinz\n" + 
		"from duowkcb dw,duimxxb dm,fahb f,zhillsb z,caiyb ca,caiylbb cb, diancxxb dc\n" +
		"where z.caiyb_id=ca.id and ca.caiylbb_id=cb.id and cb.bianm='JC'\n" + 
		" and dw.duiqm_id=dm.id and dm.fahb_id=f.id and f.zhilb_id=z.zhilb_id         " +
		"and dw.diancxxb_id=dc.id"+ strDianc+"\n" + 
		"and dw.leix=1 and dm.ruksj> = to_date('"+date2+"','yyyy-MM-dd') and dm.ruksj<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
		"group by(dw.fahr,dw.pinz)\n" + 
		"order by grouping(dw.fahr) desc, fahr, grouping(dw.pinz) desc, pinz\n" + ") bt1 ," +
		" (select decode(1,1,'本月','本月') as xiangmu from dual union select decode(1,1,'累计','累计') as xiangmu from dual) bt2) tb\n" + 
		" where s.fahr(+)=tb.fahr\n" + 
		" and s.pinz(+)=tb.pinz\n" + 
		" and s.xiangmu(+)=tb.xiangmu\n" + 
		" group by rollup (tb.xiangmu,tb.fahr,tb.pinz)\n" + 
		" having not grouping(tb.xiangmu)=1\n" + 
		" order by grouping(tb.fahr) desc, fahr, grouping(tb.pinz) desc, pinz, xiangmu");
				
		return buffer;
	}
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer=this.getBaseSql();

		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		System.out.println(buffer);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { { Local.fahr,
					Local.pinz, Local.xiangmu,
					Local.ches,Local.jingz, Local.biaoz,
					Local.yingk, Local.yuns, Local.quancf_zhilb,
					Local.kongqgzjsf_zhilb,Local.kongqgzjhf_zhilb,
					Local.ganzwhjhff_zhilb,Local.kongqgzjl_zhilb,
					Local.kongqgzjq,"干基<br>高位<br>热<br>(MJ/Kg)<br>Qgrd"
					} };

			ArrWidth = new int[] { 85, 100,90, 50, 50, 50,
					 50, 50, 50, 50 ,50,50,50,50,50};

			rt.setTitle(Local.RptTitle_Jiexyb, ArrWidth);
		}
		

		ArrWidth = new int[] { 85, 100,90, 50, 50, 50,
				 50, 50, 50, 50 ,50,50,50,50,50};
		rt.setTitle(Local.RptTitle_Jiexyb, ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		

		strFormat = new String[] { "", "", "",
				 "", "0.0", "0.0", "0.0", "0.0"
				, "0.0", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
//		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 15; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefaultTitle(1, 12, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		
//		rt.setDefaultTitle(7, 5, "制表时间:"+
//				DateUtil.FormatDate(new Date()),
//				Table.ALIGN_LEFT);
		rt.setDefaultTitle(	13,3,"制表时间:"+getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_RIGHT);
		
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 6, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "制表:", Table.ALIGN_LEFT);
		
	//	rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
				Local.RptTitle_Jiexyb, "" + BAOBPZB_GUANJZ);
		return rt.getAllPagesHtml();

	}
	
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	 年份下拉框
	private static IPropertySelectionModel _NianfModel;
		
		public IPropertySelectionModel getNianfModel() {
			if (_NianfModel == null) {
				getNianfModels();
			}
			return _NianfModel;
		}

		private IDropDownBean _NianfValue;

		public IDropDownBean getNianfValue() {
			if (_NianfValue == null) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					Object obj = getNianfModel().getOption(i);
					if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_NianfValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _NianfValue;
		}

		public boolean nianfchanged;

		public void setNianfValue(IDropDownBean Value) {
			if (_NianfValue != Value) {
				nianfchanged = true;
			}
			_NianfValue = Value;
		}

		public IPropertySelectionModel getNianfModels() {
			List listNianf = new ArrayList();
			int i;
			for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
				listNianf.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_NianfModel = new IDropDownModel(listNianf);
			return _NianfModel;
		}

		public void setNianfModel(IPropertySelectionModel _value) {
			_NianfModel = _value;
		}

		// 月份
		public boolean Changeyuef = false;

		private static IPropertySelectionModel _YuefModel;

		public IPropertySelectionModel getYuefModel() {
			if (_YuefModel == null) {
				getYuefModels();
			}
			return _YuefModel;
		}

		private IDropDownBean _YuefValue;


		public IDropDownBean getYuefValue(){
			if (_YuefValue == null) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					Object obj = getYuefModel().getOption(i);
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue!= null) {
				id = getYuefValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					Changeyuef = true;
				} else {
					Changeyuef = false;
				}
			}
			_YuefValue = Value;
			
		}

		public IPropertySelectionModel getYuefModels() {
			List listYuef = new ArrayList();
			// listYuef.add(new IDropDownBean(-1,"请选择"));
			for (int i = 1; i < 13; i++) {
				if(i<10){
					listYuef.add(new IDropDownBean(i, "0"+String.valueOf(i)));
				}else{
					listYuef.add(new IDropDownBean(i, String.valueOf(i)));
				}
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}
	
//	添加电厂树
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 运输方式下拉框
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
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
		String sql="select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}

	



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			setTreeid(null);
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel1(null);
				visit.setDropDownBean1(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setTreeid(null);
				getSelectData();
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}
		blnIsBegin = true;
		getToolBars();
		getSelectData();

	}
	
	public void getSelectData() {

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

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}
	



}
