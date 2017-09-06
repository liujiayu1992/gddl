package com.zhiren.dc.hesgl.report;

import java.util.ArrayList;
import java.util.Date;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：子结算单查询
 */

public class Zijsdcx extends BasePage implements PageValidateListener {
	
	boolean meikjsbh; // 标识是否在刷新页面时，重构煤款结算编号下拉框里的内容，true为是，false为否
	
	public boolean isMeikjsbh() {
		return meikjsbh;
	}

	public void setMeikjsbh(boolean meikjsbh) {
		this.meikjsbh = meikjsbh;
	}
	
	boolean yunfjsbh; // 标识是否在刷新页面时，重构运费结算编号下拉框里的内容，true为是，false为否
	
	public boolean isYunfjsbh() {
		return yunfjsbh;
	}

	public void setYunfjsbh(boolean yunfjsbh) {
		this.yunfjsbh = yunfjsbh;
	}
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束
	
//	结算类型下拉框_开始
	public IDropDownBean getJieslxValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getJieslxModel().getOptionCount() > 0) {
				setJieslxValue((IDropDownBean) getJieslxModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setJieslxValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getJieslxModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setJieslxModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getJieslxModels() {
		ArrayList list = new ArrayList();
//		list.add(new IDropDownBean(1, "两票结算"));
		list.add(new IDropDownBean(2, "煤款结算"));
		list.add(new IDropDownBean(3, "运费结算"));
		setJieslxModel(new IDropDownModel(list));
	}
//	结算类型下拉框_结束
	
//	运输单位下拉框_开始
	public IDropDownBean getYunsdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean5() == null) {
			if (getYunsdwModel().getOptionCount() > 0) {
				setYunsdwValue((IDropDownBean) getYunsdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean5();
	}

	public void setYunsdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean5(LeibValue);
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			getYunsdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getYunsdwModels() {
		String sql = "select rownum as id, shoukdw from (select distinct yfzb.shoukdw from jiesyfzb yfzb order by yfzb.shoukdw)";
		setYunsdwModel(new IDropDownModel(sql, "全部"));
	}
//	运输单位下拉框_结束
	
//	收款单位下拉框_开始
	public IDropDownBean getShoukdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean6() == null) {
			if (getShoukdwModel().getOptionCount() > 0) {
				setShoukdwValue((IDropDownBean) getShoukdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean6();
	}

	public void setShoukdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean6(LeibValue);
	}

	public IPropertySelectionModel getShoukdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			getShoukdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setShoukdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(value);
	}

	public void getShoukdwModels() {
		String sql = "select rownum as id, shoukdw from (select distinct jszb.shoukdw from jieszb jszb order by jszb.shoukdw)";
		setShoukdwModel(new IDropDownModel(sql, "全部"));
	}
//	收款单位下拉框_结束
	
//	煤款结算编号下拉框_开始
	public IDropDownBean getMeikjsbhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean7() == null) {
			if (getMeikjsbhModel().getOptionCount() > 0) {
				setMeikjsbhValue((IDropDownBean) getMeikjsbhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean7();
	}

	public void setMeikjsbhValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean7(LeibValue);
	}

	public IPropertySelectionModel getMeikjsbhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel7() == null) {
			getMeikjsbhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel7();
	}

	public void setMeikjsbhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel7(value);
	}

	public void getMeikjsbhModels() {
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			gongysOrMeik = " and (jszb.gongysb_id = "+ getTreeid() +" or jszb.meikxxb_id = "+ getTreeid() +")";
		}
		
		String shoukdw = "";
		if (!getShoukdwValue().getValue().equals("全部")) {
			shoukdw = " and jszb.shoukdw = '"+ getShoukdwValue().getValue() +"'";
		}
		
		String sql = 
			"select rownum num, bianm\n" +
			"  from (select distinct jszb.bianm\n" + 
			"          from jieszb jszb\n" + 
			"         where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
			"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + shoukdw + ") order by bianm";
		setMeikjsbhModel(new IDropDownModel(sql, "全部"));
	}
//	煤款结算编号下拉框_结束
	
	
//	运费结算编号下拉框_开始
	public IDropDownBean getYunfjsbhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean8() == null) {
			if (getYunfjsbhModel().getOptionCount() > 0) {
				setYunfjsbhValue((IDropDownBean) getYunfjsbhModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean8();
	}

	public void setYunfjsbhValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean8(LeibValue);
	}

	public IPropertySelectionModel getYunfjsbhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel8() == null) {
			getYunfjsbhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel8();
	}

	public void setYunfjsbhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel8(value);
	}

	public void getYunfjsbhModels() {
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			gongysOrMeik = " and (yfzb.gongysb_id = "+ getTreeid() +" or yfzb.meikxxb_id = "+ getTreeid() +")";
		}
		
		String yunsdw = "";
		if (!getYunsdwValue().getValue().equals("全部")) {
			yunsdw = " and yfzb.shoukdw = '"+ getYunsdwValue().getValue() +"'";
		}
		
		String sql = 
			"select rownum num, bianm\n" +
			"  from (select distinct yfzb.bianm\n" + 
			"          from jiesyfzb yfzb\n" + 
			"         where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
			"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + yunsdw + ") order by bianm";
		setYunfjsbhModel(new IDropDownModel(sql, "全部"));
	}
//	运费结算编号下拉框_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
//	取消煤款结算单拆分，或取消运费结算单拆分
	private boolean _QuxcfClick = false;
	
	public void QuxcfButton(IRequestCycle cycle) {
		_QuxcfClick = true;
	}
	
//	取消两票结算单拆分
	private boolean _QuxlpcfClick = false;
	
	public void QuxlpcfButton(IRequestCycle cycle) {
		_QuxlpcfClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_QuxlpcfClick) {
			_QuxlpcfClick = false;
			quxlpcf();
		}
		if (_QuxcfClick) {
			_QuxcfClick = false;
			quxcf();
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		String sql = "";
		String allPagesHtml = "";
		
		String gongysOrMeik = "";
		if (!getTreeid().equals("0")) {
			if (getJieslxValue().getValue().equals("运费结算")) {
				gongysOrMeik = "and (yfzb.gongysb_id = "+ getTreeid() +" or yfzb.meikxxb_id = "+ getTreeid() +")";
			} else if (getJieslxValue().getValue().equals("煤款结算")) {
				gongysOrMeik = "and (jszb.gongysb_id = "+ getTreeid() +" or jszb.meikxxb_id = "+ getTreeid() +")";
			}
		}
		
		String shoukdw = "";
		if (!getShoukdwValue().getValue().equals("全部")) {
			shoukdw = "and jszb.shoukdw = '"+ getShoukdwValue().getValue() +"'";
		}
		
		String yunsdw = "";
		if (!getYunsdwValue().getValue().equals("全部")) {
			yunsdw = "and yfzb.shoukdw = '"+ getYunsdwValue().getValue() +"'";
		}
		
		if (getJieslxValue().getValue().equals("运费结算")) {
			
			String yunfjsbh = "";
			if (!getYunfjsbhValue().getValue().equals("全部")) {
				yunfjsbh = " and yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"'";
			}
			
			sql = 
				"select /*grouping(ycgj.gysmc) gm, grouping(ycgj.mkmc) km, grouping(yf.zhongchh) hh,*/\n" +
				" decode(grouping(ycgj.gysmc), 1, '总计', ycgj.gysmc) gysmc,\n" + 
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc),\n" + 
				"        1,\n" + 
				"        '合计',\n" + 
				"        2,\n" + 
				"        '总计',\n" + 
				"        ycgj.mkmc) mkmc,\n" + 
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(yf.bianm), 1, '小计', 2, '合计', 3, '总计', yf.bianm) bianm,\n" +
				" decode(grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(yf.zhongchh),\n" + 
				"        1,\n" + 
				"        '小计',\n" + 
				"        2,\n" + 
				"        '合计',\n" + 
				"        3,\n" + 
				"        '总计',\n" + 
				"        yf.zhongchh) zhongchh,\n" + 
				" sum(yf.ches) ches,\n" + 
				" sum(yf.jiessl) jiessl,\n" + 
				" max(yf.yunj) yunju,\n" + 
				" max(yf.hansdj) as yunjia,\n" + 
				" sum(round_new(yf.jiessl * yf.hansdj, 2)) as yunfei,\n" + 
				" sum(yf.ches) as xiecs,\n" + 
				" sum(yf.xiecf) xiecf,\n" + 
				" sum(round_new(yf.jiessl * yf.hansdj + decode(yf.xiecf, null, 1, yf.xiecf), 2)) as hej,\n" + 
				" sum(ycgj.jiessl) as ycgj_jiessl,\n" + 
				" sum(round_new(ycgj.jiessl * yf.hansdj, 2)) as ycgj_yunf,\n" + 
				" sum(ycgj.xiecf) as ycgj_xiecf,\n" + 
				" sum(round_new(ycgj.jiessl * yf.hansdj + ycgj.xiecf, 2)) as ycgj_jine,\n" + 
				" sum(dtyc.jiessl) jiessl,\n" + 
				" sum(round_new(dtyc.jiessl * yf.hansdj, 2)) as dtgj_yunf,\n" + 
				" sum(dtyc.xiecf) as dtyc_xiecf,\n" + 
				" sum(round_new(dtyc.jiessl * yf.hansdj + dtyc.xiecf, 2)) as dtyc_jine\n" + 
				"  from (select gys.mingc gysmc,\n" + 
				"               mk.mingc mkmc,\n" + 
				"               yfzb.jiessl,\n" + 
				"               yfzb.xiecf,\n" + 
				"               yfzb.jiesrq,\n" + 
				"               yfzb.jieslx,\n" + 
				"               yfzb.bianm,\n" + 
				"               yfzb.zhongchh,\n" + 
				"               yfzb.danw,\n" + 
				"               yfzb.chaifbl,\n" + 
				"               yfzb.gongysb_id,\n" + 
				"               yfzb.meikxxb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') "+ gongysOrMeik +"\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + yunsdw + yunfjsbh + "\n" +
				"           and yfzb.bianm = yf.bianm\n" + 
				"           and yfzb.danw = '阳城国际') ycgj,\n" + 
				"\n" + 
				"       (select gys.mingc gycmc,\n" + 
				"               mk.mingc mkmc,\n" + 
				"               yfzb.jiessl,\n" + 
				"               yfzb.xiecf,\n" + 
				"               yfzb.jiesrq,\n" + 
				"               yfzb.jieslx,\n" + 
				"               yfzb.bianm,\n" + 
				"               yfzb.zhongchh,\n" + 
				"               yfzb.danw,\n" + 
				"               yfzb.chaifbl,\n" + 
				"               yfzb.gongysb_id,\n" + 
				"               yfzb.meikxxb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') "+ gongysOrMeik +"\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + yunsdw + yunfjsbh +"\n" +
				"           and yfzb.danw = '大唐阳城') dtyc,\n" + 
				"       jiesyfb yf\n" + 
				" where ycgj.bianm = dtyc.bianm\n" + 
				"   and ycgj.bianm = yf.bianm\n" + 
				"   and dtyc.bianm = yf.bianm\n" + 
				" group by rollup(ycgj.gysmc, ycgj.mkmc, yf.bianm, yf.zhongchh)\n" +
				" having not grouping(yf.bianm) + grouping(yf.zhongchh) = 1";
			
//			获取合同编号
			String hetbh_sql = 
				"select ysht.hetbh\n" +
				"  from hetys ysht\n" + 
				" where ysht.id in\n" + 
				"       (select distinct yfzb.hetb_id\n" + 
				"          from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				"         where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
				"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"           and yfzb.gongysb_id = gys.id\n" + 
				"           and yfzb.meikxxb_id = mk.id\n" + 
				"           and yfzb.bianm = yf.bianm)";
			
			ResultSetList hetbh_rsl = con.getResultSetList(hetbh_sql);
			String hetbh = "";
			while (hetbh_rsl.next()) {
				hetbh += hetbh_rsl.getString("hetbh") + ",&nbsp;";
			}
			
//			获取拆分比率
			String chaifbl_sql = 
				"select yfzb.danw, max(yfzb.chaifbl) chaifbl\n" +
				"  from jiesyfzb yfzb, gongysb gys, meikxxb mk, jiesyfb yf\n" + 
				" where to_char(yfzb.jiesrq, 'yyyy-mm') =\n" + 
				"       to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"   and yfzb.gongysb_id = gys.id\n" + 
				"   and yfzb.meikxxb_id = mk.id\n" + 
				"   and yfzb.bianm = yf.bianm\n" + 
				"group by yfzb.danw\n" + 
				"order by yfzb.danw";
			
			ResultSetList chaifbl_rsl = con.getResultSetList(chaifbl_sql);
			String chaifbl_dtyc = "";
			String chaifbl_ycgj = "";
			while (chaifbl_rsl.next()) {
				if (chaifbl_rsl.getString("danw").equals("大唐阳城")) {
					chaifbl_dtyc = chaifbl_rsl.getString("chaifbl") + "%";
				} else {
					chaifbl_ycgj = chaifbl_rsl.getString("chaifbl") + "%";
				}
			}
			
			ArrHeader = new String[2][20];
			ArrHeader[0] = new String[]{"供煤单位","矿名","结算编号","卸煤<br>地点","车数","承运煤量<br>(吨)","运距<br>(Km)","运价<br>(元/吨)","运费<br>(元)","卸车费","卸车费","合计",
				"阳城国际分摊情况"+chaifbl_ycgj,"阳城国际分摊情况"+chaifbl_ycgj,"阳城国际分摊情况"+chaifbl_ycgj,"阳城国际分摊情况"+chaifbl_ycgj, 
				"大唐阳城分摊情况"+chaifbl_dtyc, "大唐阳城分摊情况"+chaifbl_dtyc, "大唐阳城分摊情况"+chaifbl_dtyc, "大唐阳城分摊情况"+chaifbl_dtyc};
			ArrHeader[1] = new String[]{"供煤单位","矿名","结算编号","卸煤<br>地点","车数","承运煤量<br>(吨)","运距<br>(Km)","运价<br>(元/吨)","运费<br>(元)","自卸<br>车数","金额<br>(元)","合计",
				"煤量<br>(吨)","运费<br>(元)","卸车费<br>(元)","金额<br>(元)", "煤量<br>(吨)", "运费<br>(元)", "卸车费<br>(元)", "金额<br>(元)"};
			ArrWidth = new int[] {95, 75, 80, 40, 30, 50, 35, 50, 65, 35, 40, 65, 45, 60, 45, 65, 45, 60, 45, 65};
			
			ResultSetList rslData =  con.getResultSetList(sql);
			rt.setBody(new Table(rslData, 2, 0, 4));
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(23);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			for(int i = 2; i < rt.body.getRows(); i ++){
				rt.body.merge(i, 2, i, 4);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 4);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			
			if (!yunsdw.equals("")) {
				yunsdw = "与" + getYunsdwValue().getValue();
			}
			rt.setTitle("阳城电厂"+ yunsdw +"公路运输煤量(以实收数)、运费核对表", ArrWidth);
			rt.setDefaultTitle(1, 2, "查询时间："+ getNianfValue().getValue() +"年"+ getYuefValue().getValue() +"月", Table.ALIGN_CENTER);
			if (!hetbh.equals("")) {
				rt.setDefaultTitle(4, 12, "合同编号：" + hetbh.substring(0, hetbh.lastIndexOf(",")), Table.ALIGN_CENTER);
			}
			rt.setDefaultTitle(16, 4, "运输单位：" + getYunsdwValue().getValue(), Table.ALIGN_CENTER);
			
			hetbh_rsl.close();
			chaifbl_rsl.close();
			rslData.close();
			
			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			if(rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			allPagesHtml = rt.getAllPagesHtml();
			
		} else if (getJieslxValue().getValue().equals("煤款结算")) {
			
			String meikjsbh = "";
			if (!getMeikjsbhValue().getValue().equals("全部")) {
				meikjsbh = " and jszb.bianm = '"+ getMeikjsbhValue().getValue() +"'";
			}
			
			sql = 
				"select /*grouping(ycgj.ysfs) ysfs, grouping(ycgj.gysmc) gmc, grouping(ycgj.mkmc) mkmc, grouping(js.bianm) bm, grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'日') riq,*/\n" +
				"       decode(grouping(ycgj.ysfs), 1, '电力燃料总计', ycgj.ysfs) ysfs,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc), 1, ycgj.ysfs||'总计', 2, '电力燃料总计', ycgj.gysmc) gysmc,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc), 1, '合计', 2, ycgj.ysfs||'总计', 3, '电力燃料总计', ycgj.mkmc) mkmc,\n" + 
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(js.bianm), 1, '小计', 2, '合计', 3, ycgj.ysfs||'总计', 4, '电力燃料总计', js.bianm) bianm,\n" +
				"       decode(grouping(ycgj.ysfs) + grouping(ycgj.gysmc) + grouping(ycgj.mkmc) + grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'日'), 1, '小计', 2, '合计', 3, ycgj.ysfs||'总计', 4, '电力燃料总计', to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'日') as gongmsj,\n" + 
				"       max(js.yunj) yunju,\n" + 
				"       sum(js.ches) ches,\n" + 
				"       sum(getjiesdzb('jiesb', js.id, '结算数量', 'changf')) as yanssl,\n" + 
				"       sum(js.koud) koud,\n" + 
				"       sum(js.jiessl) jiessl,\n" + 
				"       round_new(sum(getjiesdzb('jiesb', js.id, '结算数量', 'changf') * js.jieslf) / sum(getjiesdzb('jiesb', js.id, '结算数量', 'changf')), 2) as jieslf,\n" + 
				"       round_new(sum(getjiesdzb('jiesb', js.id, '结算数量', 'changf') * js.jiesrl) / sum(getjiesdzb('jiesb', js.id, '结算数量', 'changf')), 0) as  jiesrl,\n" + 
				"       round_new(sum(js.jiessl * js.hetj) / decode(sum(js.jiessl), 0, 1, sum(js.jiessl)), 2) as hetj,\n" + 
				"       '' as yirzjkdj, --以热值奖(+)扣(-)单价(元/吨)\n" + 
				"       round_new(sum(js.jiessl * js.hansdj) / decode(sum(js.jiessl), 0, 1, sum(js.jiessl)), 2) as hansdj,\n" + 
				"       sum(js.hansmk) hansmk,\n" + 
				"       sum(ycgj.jiessl) ycgj_jiessl,\n" + 
				"       sum(round_new(ycgj.jiessl * js.hansdj, 2)) as ycgj_jine,\n" + 
				"       sum(dtyc.jiessl) dtyc_jiessl,\n" + 
				"       sum(round_new(dtyc.jiessl * js.hansdj, 2)) as dtyc_jine\n" + 
				"  from (select ysfs.mingc ysfs, gys.mingc gysmc, mk.mingc mkmc, jszb.*\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, yunsfsb ysfs\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + "\n" +
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.yunsfsb_id = ysfs.id\n" + shoukdw + meikjsbh + "\n" +
				"           and jszb.danw = '阳城国际') ycgj,\n" + 
				"\n" + 
				"       (select ysfs.mingc ysfs, gys.mingc gysmc, mk.mingc mkmc, jszb.*\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, yunsfsb ysfs\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + gongysOrMeik + "\n" +
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.yunsfsb_id = ysfs.id\n" + shoukdw + meikjsbh + "\n" +
				"           and jszb.danw = '大唐阳城') dtyc,\n" + 
				"       jiesb js\n" + 
				" where ycgj.bianm = dtyc.bianm\n" + 
				"   and ycgj.bianm = js.bianm\n" + 
				"   and dtyc.bianm = js.bianm\n" + 
				"group by rollup (ycgj.ysfs, ycgj.gysmc, ycgj.mkmc, js.bianm, to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'日')\n" +
				"having not grouping(js.bianm) + grouping(to_char(js.yansksrq, 'dd')||'-'||to_char(js.yansjzrq, 'dd')||'日') = 1";
			
//			获取合同编号
			String hetbh_sql = 
				"select ht.hetbh\n" +
				"  from hetb ht\n" + 
				" where ht.id in\n" + 
				"       (select jszb.hetb_id\n" + 
				"          from jieszb jszb, gongysb gys, meikxxb mk, jiesb js\n" + 
				"         where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
				"               to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"           and jszb.gongysb_id = gys.id\n" + 
				"           and jszb.meikxxb_id = mk.id\n" + 
				"           and jszb.bianm = js.bianm)";
			
			ResultSetList hetbh_rsl = con.getResultSetList(hetbh_sql);
			String hetbh = "";
			while (hetbh_rsl.next()) {
				hetbh += hetbh_rsl.getString("hetbh") + ",&nbsp;";
			}
			
//			获取拆分比率
			String chaifbl_sql = 
				"select jszb.danw, max(jszb.chaifbl) chaifbl\n" +
				"  from jieszb jszb, gongysb gys, meikxxb mk, jiesb js\n" + 
				" where to_char(jszb.jiesrq, 'yyyy-mm') =\n" + 
				"       to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm')\n" + 
				"   and jszb.gongysb_id = gys.id\n" + 
				"   and jszb.meikxxb_id = mk.id\n" + 
				"   and jszb.bianm = js.bianm\n" + 
				"group by jszb.danw\n" + 
				"order by jszb.danw";

			ResultSetList chaifbl_rsl = con.getResultSetList(chaifbl_sql);
			String chaifbl_dtyc = "";
			String chaifbl_ycgj = "";
			while (chaifbl_rsl.next()) {
				if (chaifbl_rsl.getString("danw").equals("大唐阳城")) {
					chaifbl_dtyc = chaifbl_rsl.getString("chaifbl") + "%";
				} else {
					chaifbl_ycgj = chaifbl_rsl.getString("chaifbl") + "%";
				}
			}
			
			ArrHeader = new String[2][20];
			ArrHeader[0] = new String[]{"运输<br>方式","供煤单位","矿点","结算编号","供煤时间","运距<br>(Km)","车数","进煤量(吨)","扣量<br>(吨)","结算量<br>(吨)","St,ad<br>(%)","Qnet,ar(卡/克)","合同基价<br>(元/吨)","以热值<br>奖扣单价<br>(元/吨)","结算价<br>(元/吨)","金额(元)","阳城国际分摊"+chaifbl_ycgj,"阳城国际分摊"+chaifbl_ycgj,"大唐阳城分摊"+chaifbl_dtyc,"大唐阳城分摊"+chaifbl_dtyc};
			ArrHeader[1] = new String[]{"运输<br>方式","供煤单位","矿点","结算编号","供煤时间","运距<br>(Km)","车数","进煤量(吨)","扣量<br>(吨)","结算量<br>(吨)","St,ad<br>(%)","Qnet,ar(卡/克)","合同基价<br>(元/吨)","以热值<br>奖扣单价<br>(元/吨)","结算价<br>(元/吨)","金额(元)","煤量(吨)","金额(元)","煤量(吨)","金额(元)"};
			ArrWidth = new int[] {35, 95, 75, 80, 55, 32, 32, 55, 45, 55, 38, 38, 40, 35, 40, 70, 65, 70, 65, 70};
			
			ResultSetList rslData = con.getResultSetList(sql);
			rt.setBody(new Table(rslData, 2, 0, 5));
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			
			for(int i = 2; i < rt.body.getRows(); i ++){
				rt.body.merge(i, 2, i, 5);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 5);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			
			if (!shoukdw.equals("")) {
				shoukdw = "与" + getShoukdwValue().getValue();
			}
			rt.setTitle("阳城电厂"+ shoukdw +"煤款结算核对表", ArrWidth);
			rt.setDefaultTitle(1, 2, "查询时间："+ getNianfValue().getValue() +"年"+ getYuefValue().getValue() +"月", Table.ALIGN_CENTER);
			if (!hetbh.equals("")) {
				rt.setDefaultTitle(3, 17, "合同编号：" + hetbh.substring(0, hetbh.lastIndexOf(",")), Table.ALIGN_RIGHT);
			}
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 6, "确定双方："+((Visit)this.getPage().getVisit()).getDiancqc()+"燃料部<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+getShoukdwValue().getValue(), Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 4, "统计员：", Table.ALIGN_LEFT);
			rt.setDefautlFooter(11, 2, "审核员：", Table.ALIGN_CENTER);
			rt.setDefautlFooter(16, 4, "核对日期：", Table.ALIGN_LEFT);
			
			hetbh_rsl.close();
			chaifbl_rsl.close();
			rslData.close();

			_CurrentPage = 1;
			_AllPages = 1;
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			if(rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			allPagesHtml = rt.getAllPagesHtml();
		}

		con.Close();
		return allPagesHtml;
	}
	
	/**
	 * 取消煤款结算单拆分，或取消运费结算单拆分
	 */
	public void quxcf() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		if (getJieslxValue().getValue().equals("煤款结算")) {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"';\n");
		} else {
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"';");
		}
		sbsql.append("end;");
		
		if (con.getUpdate(sbsql.toString()) == 1) {
			setMsg("取消拆分成功！");
			this.setMeikjsbh(true); // 标识是否在刷新页面时，重构煤款结算编号下拉框里的内容，true为是，false为否
			this.setYunfjsbh(true); // 标识是否在刷新页面时，重构运费结算编号下拉框里的内容，true为是，false为否
		} else {
			setMsg("取消拆分失败！");
		}
		con.Close();
	}
	
	/**
	 * 取消两票结算单拆分
	 */
	public void quxlpcf() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		if (getJieslxValue().getValue().equals("煤款结算")) {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"';\n");
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getMeikjsbhValue().getValue() +"';");
		} else {
			sbsql.append("delete from jieszb jszb where jszb.bianm = '"+ getYunfjsbhValue().getValue() +"';\n");
			sbsql.append("delete from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"';");
		}
		sbsql.append("end;");
		
		if (con.getUpdate(sbsql.toString()) == 1) {
			setMsg("取消拆分成功！");
			this.setMeikjsbh(true); // 标识是否在刷新页面时，重构煤款结算编号下拉框里的内容，true为是，false为否
			this.setYunfjsbh(true); // 标识是否在刷新页面时，重构运费结算编号下拉框里的内容，true为是，false为否
		} else {
			setMsg("取消拆分失败！");
		}
		con.Close();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		
		tbr.addText(new ToolbarText("年份："));
		ComboBox cbx_nf = new ComboBox();
		cbx_nf.setTransform("Nianf");
		cbx_nf.setWidth(55);
		cbx_nf.setListWidth(55);
		cbx_nf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		tbr.addField(cbx_nf);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("月份："));
		ComboBox cbx_yf = new ComboBox();
		cbx_yf.setTransform("Yuef");
		cbx_yf.setWidth(45);
		cbx_yf.setListWidth(45);
		cbx_yf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		tbr.addField(cbx_yf);
		tbr.addText(new ToolbarText("-"));
		
		String condition = "";
		if (getJieslxValue().getValue().equals("两票结算")) {
			condition = "jieszb jszb, jiesyfzb yfzb & " +
				"and gys.id = jszb.gongysb_id and gys.id = yfzb.gongysb_id " +
				"and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') " +
				"and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') && " +
				"and gys.id = jszb.gongysb_id and m.id = jszb.meikxxb_id and gys.id = yfzb.gongysb_id and m.id = yfzb.meikxxb_id " +
				"and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') " +
				"and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		} else if (getJieslxValue().getValue().equals("煤款结算")) {
			condition = "jieszb jszb & " +
				"and gys.id = jszb.gongysb_id and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') &&" +
				"and gys.id = jszb.gongysb_id and m.id = jszb.meikxxb_id and to_char(jszb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		} else if (getJieslxValue().getValue().equals("运费结算")) {
			condition = "jiesyfzb yfzb & " +
				"and gys.id = yfzb.gongysb_id and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') &&" +
				"and gys.id = yfzb.gongysb_id and m.id = yfzb.meikxxb_id and to_char(yfzb.jiesrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"-01', 'yyyy-mm-dd'), 'yyyy-mm') ";
		}
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree", ExtTreeUtil.treeWindowCheck_gongys_jieszb, 
			((Visit) this.getPage().getVisit()).getDiancxxb_id(), getTreeid(), condition, false);
		setTree(etu);
		
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		
		ToolbarButton btn = new ToolbarButton(null, null,"function(){gongysTree_window.show();}");
		btn.setIcon("ext/resources/images/list-items.gif");
		btn.setCls("x-btn-icon");
		btn.setMinWidth(20);
		tbr.addText(new ToolbarText("供货单位："));
		tbr.addField(tf);
		tbr.addItem(btn);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("结算类型："));
		ComboBox jieslx = new ComboBox();
		jieslx.setWidth(80);
		jieslx.setTransform("Jieslx");
		jieslx.setId("jieslx");
		jieslx.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		jieslx.setLazyRender(true);
		jieslx.setEditable(false);
		tbr.addField(jieslx);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("收款单位："));
		ComboBox combx = new ComboBox();
		combx.setWidth(90);
		combx.setListWidth(250);
		if (getJieslxValue().getValue().equals("煤款结算")) {
			combx.setTransform("Shoukdw");
			combx.setId("shoukdw");
		} else {
			combx.setTransform("Yunsdw");
			combx.setId("yunsdw");
		}
		combx.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		combx.setLazyRender(true);
		combx.setEditable(false);
		tbr.addField(combx);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("结算编号："));
		ComboBox jiesbh = new ComboBox();
		jiesbh.setWidth(90);
		jiesbh.setListWidth(150);
		if (getJieslxValue().getValue().equals("煤款结算")) {
			jiesbh.setTransform("Meikjsbh");
		} else {
			jiesbh.setTransform("Yunfjsbh");
		}
		jiesbh.setId("jsbh");
		jiesbh.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		jiesbh.setLazyRender(true);
		jiesbh.setEditable(false);
		tbr.addField(jiesbh);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton chaifbtn = new ToolbarButton("chaif_Button", "取消拆分", getButtonHandler("test"));
		chaifbtn.setIcon(SysConstant.Btn_Icon_Cancel);
		tbr.addItem(chaifbtn);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * 返回"取消拆分"按钮的handler，如果要取消拆分的结算单为两票结算单时执行quxlpcf()方法，
	 * 如果为煤款结算单或运费结算单，那么执行quxcf()方法。
	 * @param buttonName
	 * @return
	 */
	public String getButtonHandler(String buttonName) {
		
		JDBCcon con = new JDBCcon();
		String str = "    	 document.getElementById('QuxcfButton').click();";
		
		if (getJieslxValue().getValue().equals("煤款结算")) {
			ResultSetList jieslx_rsl = con.getResultSetList("select distinct jszb.jieslx from jieszb jszb where jszb.bianm = '"+ getMeikjsbhValue().getValue() +"'");
			if (jieslx_rsl.next()) {
				if (jieslx_rsl.getString("jieslx").equals("1")) {
					str = 					
					"    Ext.MessageBox.confirm('提示信息','该结算单为两票结算，是否也取消拆分运费结算单？',\n" + 
					"        function(btn){\n" + 
					"            if(btn == 'yes'){\n" + 
					"                document.getElementById('QuxlpcfButton').click();\n" + 
					"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
					"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
					"            };\n" + 
					"        }\n" + 
					"    );";
				}
			}
			jieslx_rsl.close();
		} else {
			ResultSetList jieslx_rsl = con.getResultSetList("select distinct yfzb.jieslx from jiesyfzb yfzb where yfzb.bianm = '"+ getYunfjsbhValue().getValue() +"'");
			if (jieslx_rsl.next()) {
				if (jieslx_rsl.getString("jieslx").equals("1")) {
					str = 					
					"    Ext.MessageBox.confirm('提示信息','该结算单为两票结算，是否也取消拆分煤款结算单？',\n" + 
					"        function(btn){\n" + 
					"            if(btn == 'yes'){\n" + 
					"                document.getElementById('QuxlpcfButton').click();\n" + 
					"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
					"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
					"            };\n" + 
					"        }\n" + 
					"    );";
				}
			}
			jieslx_rsl.close();
		}
		
		String handler = 
			"function(){\n" +
			"    if(jsbh.getRawValue()=='全部'){\n" + 
			"        Ext.MessageBox.alert('提示信息','请选一个结算编号！');\n" + 
			"    }else{\n" + 
					str + 
			"    }\n" + 
			"}";
		
		con.Close();
		return handler;
	}
	
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
	
//	供应商树_开始
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2() == null || ((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
			this.setMeikjsbh(true); // 标识是否在刷新页面时，重构煤款结算编号下拉框里的内容，true为是，false为否
			this.setYunfjsbh(true); // 标识是否在刷新页面时，重构运费结算编号下拉框里的内容，true为是，false为否
		}
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
//	供应商树_结束

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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // 结算类型下拉框
			visit.setDropDownBean4(null);
			visit.setProSelectionModel5(null); // 运输单位下拉框
			visit.setDropDownBean5(null);
			visit.setProSelectionModel6(null); // 收款单位下拉框
			visit.setDropDownBean6(null);
			visit.setProSelectionModel7(null); // 煤款结算编号下拉框
			visit.setDropDownBean7(null);
			visit.setProSelectionModel8(null); // 运费结算编号下拉框
			visit.setDropDownBean8(null);
		}
		if (this.isMeikjsbh()) {
			visit.setProSelectionModel7(null);
			visit.setDropDownBean7(null);
			getMeikjsbhModels();
			this.setMeikjsbh(false); // 标识是否在刷新页面时，重构煤款结算编号下拉框里的内容，true为是，false为否
		}
		if (this.isYunfjsbh()) {
			visit.setProSelectionModel8(null);
			visit.setDropDownBean8(null);
			getYunfjsbhModels();
			this.setYunfjsbh(false); // 标识是否在刷新页面时，重构运费结算编号下拉框里的内容，true为是，false为否
		}
		getSelectData();
	}

}