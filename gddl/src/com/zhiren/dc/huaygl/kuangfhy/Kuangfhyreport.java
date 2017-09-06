package com.zhiren.dc.huaygl.kuangfhy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Kuangfhyreport extends BasePage {
	/*
	 * private String FormatDate(Date _date) { if (_date == null) { return
	 * MainGlobal.Formatdate("yyyy-MM-dd", new Date()); } return
	 * MainGlobal.Formatdate("yyyy-MM-dd", _date); }
	 * 
	 * private static Date _riqValue = new Date();
	 * 
	 * public Date getRiq() { return _riqValue; }
	 * 
	 * public void setRiq(Date _date) { _riqValue = _date; }
	 * 
	 * private static Date _riqEndValue = new Date();
	 * 
	 * public Date getEndRiq() { return _riqEndValue; }
	 * 
	 * public void setEndRiq(Date _date) { _riqEndValue = _date; }
	 */

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
	private String[] param=new String[2];
	private String[] mstrReportName = new String[2];

	public String getDiancQuanc() {
		return getDiancQuanc(getDiancId());
	}

	public long getDiancId() {
		/*
		 * if (isGongsUser()){ return getDiancmcValue().getId(); }else{ return
		 * ((Visit)getPage().getVisit()).getDiancxxbId(); }
		 */
		return getDiancId();
	}

	// 得到单位全称
	public String getDiancQuanc(long diancxxbID) {
		String _DiancQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ diancxxbID);
			while (rs.next()) {
				_DiancQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		return _DiancQuanc;
	}

	public String getPrintTable() {
		if (mstrReportName.equals(param)) {
			return getRezc();
		} else {
			return "无此报表";
		}
	}

	private String getRezc() {
		JDBCcon cn = new JDBCcon();
		
		
		String Riqi = this.getRiqi();
		
		
		StringBuffer sbsql = new StringBuffer();
		// 工具栏的年份和月份下拉框
		/*long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}*/

		// 供应商条件
		// 项目名称条件
		/*sbsql.append("select mk.mingc,mz.mingc, yf.mingc,ys.riq,ym.mingc,ys.zhi \n");
		sbsql.append("from yuansfxb ys,meikxxb mk,meizb mz,yunsfsb yf,yuansxmb ym \n");
		sbsql.append("where ys.meikxxb_id = \n");
		sbsql.append(param[1]);
		sbsql.append(" and ys.meizb_id = mz.id \n");
		sbsql.append(" and ys.yunsfsb_id = yf.id \n");
		sbsql.append(" and ys.yuansxmb_id = \n");
		sbsql.append(param[2]);
		sbsql.append(" and to_char(ys.riq,'yyyy')= \n");
		sbsql.append(param[0]);
		sbsql.append(" order by ys.riq \n");*/

	
		sbsql.append("select distinct k.id,g.mingc as gongysb_id,m.mingc as meikxxb_id, k.qnet_ar as qnet_ar,k.aar as aar,k.ad as ad,k.vdaf as vdaf,  k.mt as mt,k.stad as stad,k.aad as aad,k.mad as mad,k.qbad as qbad,k.had,k.vad,k.fcad,k.std,k.qbrad,k.hdaf,k.qgrad_daf,k.sdaf,k.var as var,k.t1 as t1,k.t2 as t2,k.t3 as t3,k.t4 as t4,m.leib as leib,k.lury as lury \n");
		sbsql.append("from kuangfzlb k,gongysb g,meikxxb m,zhilb z,fahb f \n");
		sbsql.append("where k.gongysb_id=g.id and g.id = f.gongysb_id and k.meikxxb_id=m.id and m.id =\n");
		sbsql.append(param[1]);
		sbsql.append( " and f.fahrq=to_date( '"+ Riqi +"','yyyy-mm-dd')\n");
		//sbsql.append(param[0]);
		sbsql.append(" and g.id = \n");
		sbsql.append(param[0]);
		sbsql.append("	order by f.fahrq  \n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		 String ArrHeader[][]=new String[1][26];
		 ArrHeader[0]=new String[] {"供应商","煤矿名称","QNET_AR(Mj/kg)","AAR(%)","AAR(%)","AD(%)","VDAF(%)","MT(%)","STAD(%)","AAD(%)","MAD(%)","QBAD(Mj/kg)","HAD(%)","VAD(%)","FCAD(%)","STD(%)","QBRAD(Mj/kg)","HDAF(%)","QGRAD_DAF(Mj/kg)","SDAF(%)","VAR(%)","T1(℃)","T2(℃)","T3(℃)","T4(℃)","类别","录入员"};
		// 列宽
		 int ArrWidth[]=new int[] {80,80,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,30,};

		// 设置页标题
		rt.setTitle("矿方化验表", ArrWidth);
		// rt.setDefaultTitle(1,4,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		// rt.setValue("tianbrq",strDate +"年"+strEndDate+"月");

		// rt.setDefaultTitle(3,14,strNianf+"年"+strYuef+"月",Table.ALIGN_CENTER);
		// rt.setDefaultTitle(11,2,"调然16-1表",Table.ALIGN_RIGHT);

		// 数据
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		// rt.body.mergeFixedCol(1);
		rt.body.ShowZero = false;

		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 22, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(19,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		return rt.getAllPagesHtml();
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			param = cycle.getRequestContext().getParameters("lx");
			if (param != null) {
				mstrReportName = param;
			}
		}

	}
	///////
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(new Date());
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		
		
	}

	// 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
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
//	 项目名称
	public boolean _Yuansxmmcchange = false;

	private IDropDownBean _YuansxmmcValue;

	public IDropDownBean getYuansxmmcValue() {
		if (_YuansxmmcValue == null) {
			_YuansxmmcValue = (IDropDownBean) getIYuansxmmcModels()
					.getOption(0);
		}
		return _YuansxmmcValue;
	}

	public void setYuansxmmcValue(IDropDownBean Value) {
		long id = -2;
		if (_YuansxmmcValue != null) {
			id = _YuansxmmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Yuansxmmcchange = true;
			} else {
				_Yuansxmmcchange = false;
			}
		}
		_YuansxmmcValue = Value;
	}

	private IPropertySelectionModel _IYuansxmmcModel;

	public void setIYuansxmmcModel(IPropertySelectionModel value) {
		_IYuansxmmcModel = value;
	}

	public IPropertySelectionModel getIYuansxmmcModel() {
		if (_IYuansxmmcModel == null) {
			getIYuansxmmcModels();
		}
		return _IYuansxmmcModel;
	}

	public IPropertySelectionModel getIYuansxmmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc  from yuansxmb where zhuangt=0 order by mingc";
			_IYuansxmmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IYuansxmmcModel;
	}

	// 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from meikxxb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
}


