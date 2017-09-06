package com.zhiren.dc.huopfytj;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
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
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/* 作者：尹佳明
 * 日期：2010-02-01
 * 描述：
 * 		1、增加货票类型下拉框，通过下拉框选择"单票"或"联票"进行费用查询。
 * 		2、重写费用查询的SQL语句。
 */

public class Huopfytj extends BasePage {
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

	private String RT_DR16 = "Yufkcx";

	private String RT_DR03 = "Yufktjb";

	private boolean blnIsBegin = false;

	private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {

		return getHuopfytj();
	}
	
//	类别下拉框_开始
	public IDropDownBean getLeibValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getLeibModel().getOptionCount() > 0) {
				setLeibValue((IDropDownBean) getLeibModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setLeibValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(LeibValue);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setLeibModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setLeibModels() {
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(1, "单票"));
		list.add(new IDropDownBean(2, "联票"));
		setLeibModel(new IDropDownModel(list));
	}
//	类别下拉框_结束

	private String getHuopfytj() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		// sbsql.append("select z.bianm as 结算指标,j.changf as 厂方,j.gongf as
		// 矿方,j.jies as 结算 from jieszbsjb j,zhibb z\n" );
		// sbsql.append("where j.zhibb_id=z.id and z.leib=1 and
		// j.yansbhb_id="+visit.getString1()+" order by z.bianm");
		StringBuffer strGongys = new StringBuffer();
		if(this.getGongysValue().getId()>-1){
			strGongys.append(" and g.mingc='").append(getGongysValue().getValue()).append("'");
		}
		
		if(this.getMeikdwValue().getId()>-1){
			strGongys.append(" and m.id=").append(getMeikdwValue().getId());
		}

		Report rt = new Report();
		ResultSet rs = null;
		String sql = "";
		
		if (getLeibValue().getValue().equals("单票")) { // 单票查询
			
			sql = 
				"select /*grouping(j.fahdw) f, grouping(j.meikdwmc) mk, grouping(j.ches1) c1, grouping(j.biaoz1) b1, grouping(j.fahb_id) fid, grouping(j.mingc) m,*/\n" +
				"       decode(grouping(j.fahdw), 1, '总计', j.fahdw) fahdw,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 1, '合计', 2, '总计', j.meikdwmc) meikdwmc,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 1, '合计', 2, '总计', nvl2(j.mingc, max(to_char(j.daohrq, 'yyyy-mm-dd')), '小计')) daohrq,\n" + 
				"       nvl2(j.mingc, max(j.chec), decode(grouping(j.fahdw)+grouping(j.meikdwmc), 0, '小计', 1, '合计', 2, '总计')) chec,\n" + 
				"       decode(grouping(j.mingc), 0, j.ches1, sum(j.ches2)) ches,\n" + 
				"       decode(grouping(j.mingc), 0, j.biaoz1, sum(j.biaoz2)) biaoz,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 1, '', 2, '', nvl2(j.mingc, j.mingc, '')) mingc,\n" + 
				"       decode(grouping(j.mingc), 0, sum(j.zhi), 1, sum(j.zhi2)) zhi from\n" + 
				"  (select --grouping(x.fahdw) f, grouping(x.meikdwmc) mk, grouping(x.fahb_id) fid, grouping(x.mingc) m,\n" + 
				"         x.fahdw,\n" + 
				"         x.meikdwmc,\n" + 
				"         max(x.daohrq) daohrq,\n" + 
				"         max(x.chec) chec,\n" + 
				"         sum(x.ches) ches,\n" + 
				"         sum(x.biaoz) biaoz,\n" + 
				"         sum(x.ches)/count(distinct x.mingc) ches1,\n" + 
				"         sum(x.biaoz)/count(distinct x.mingc) biaoz1,\n" + 
				"         decode(grouping(x.mingc), 1, sum(x.ches)/count(distinct x.mingc), 0) ches2,\n" + 
				"         decode(grouping(x.mingc), 1, sum(x.biaoz)/count(distinct x.mingc), 0) biaoz2,\n" + 
				"         x.fahb_id,\n" + 
				"         x.mingc,\n" + 
				"         sum(x.zhi) zhi,\n" + 
				"         decode(grouping(x.mingc), 1, sum(x.zhi), 0) zhi2 from\n" + 
				"    (select max(d.fahdw) fahdw,\n" + 
				"           max(d.meikdwmc) meikdwmc,\n" + 
				"           max(d.daohrq) daohrq,\n" + 
				"           max(d.chec) chec,\n" + 
				"           max(d.ches) ches,\n" + 
				"           max(d.biaoz) biaoz,\n" + 
				"           d.fahb_id,\n" + 
				"           d.chepb_id,\n" + 
				"           fymc.mingc,\n" + 
				"           sum(fy.zhi) zhi\n" + 
				"      from (select fc.fahb_id,\n" + 
				"                   max(fc.fahdw) as fahdw,\n" + 
				"                   max(fc.meikdwmc) as meikdwmc,\n" + 
				"                   max(fc.daohrq) as daohrq,\n" + 
				"                   max(fc.chec) as chec,\n" + 
				"                   sum(fc.biaoz) as biaoz,\n" + 
				"                   sum(fc.ches) as ches,\n" + 
				"                   p.chepb_id,\n" + 
				"                   p.yunfdjb_id\n" + 
				"              from (select g.mingc as fahdw,\n" + 
				"                           m.mingc as meikdwmc,\n" + 
				"                           c.fahb_id,\n" + 
				"                           c.id chepb_id,\n" + 
				"                           c.biaoz,\n" + 
				"                           1 as ches,\n" + 
				"                           f.chec,\n" + 
				"                           f.daohrq\n" + 
				"                      from fahb f, chepb c, gongysb g, meikxxb m\n" + 
				"                     where f.id = c.fahb_id\n" + 
				"                       and f.gongysb_id = g.id\n" + 
				"                       and f.meikxxb_id = m.id\n" + 
				"                       and f.daohrq >= to_date('"+ getRiqi() +"', 'yyyy-mm-dd')\n" + 
				"                       and f.daohrq < to_date('"+ getRiq2() +"', 'yyyy-mm-dd') + 1\n" +  strGongys + "\n) fc,\n" +
				"                   danjcpb p,\n" + 
				"                   yunfdjb yfdj\n" + 
				"             where fc.chepb_id = p.chepb_id\n" + 
				"               and p.yunfdjb_id = yfdj.id\n" + 
				"               and yfdj.ches = 1\n" + 
				"             group by fc.fahb_id, p.chepb_id, p.yunfdjb_id\n" + 
				"             order by fc.fahb_id, p.chepb_id, p.yunfdjb_id) d,\n" + 
				"           feiyb fy,\n" + 
				"           feiyxmb fyxm,\n" + 
				"           feiymcb fymc,\n" + 
				"           yunfdjb yfdj\n" + 
				"     where d.yunfdjb_id = yfdj.id\n" + 
				"       and yfdj.feiyb_id = fy.feiyb_id\n" + 
				"       and fy.feiyxmb_id = fyxm.id\n" + 
				"       and fyxm.feiymcb_id = fymc.id\n" + 
				"     group by d.fahb_id, d.chepb_id, fymc.mingc\n" + 
				"     order by fymc.mingc, d.chepb_id) x\n" + 
				"  group by rollup(x.fahdw, x.meikdwmc, x.fahb_id, x.mingc)\n" + 
				"  having grouping(x.fahb_id) = 0\n" + 
				"  order by x.fahdw, x.meikdwmc, x.fahb_id, x.mingc) j\n" + 
				"group by rollup (j.fahdw, j.meikdwmc, j.ches1, j.biaoz1, j.fahb_id, j.mingc)\n" + 
				"having grouping(j.mingc) = 0 or grouping(j.meikdwmc) = 1\n" + 
				"order by j.fahdw, j.meikdwmc, max(j.daohrq), max(j.chec), j.ches1, j.fahb_id, j.mingc, max(j.zhi)";

			rs = cn.getResultSet(sql);

			String ArrHeader[][] = new String[1][8];
			ArrHeader[0] = new String[] { "发货单位", "煤矿单位", "到货日期", "车次", "车数", "票重", "费用", "金额(元)"};

			int ArrWidth[] = new int[] { 100, 100, 100, 80, 80, 80, 100, 100 };

			rt.setTitle("货 票("+getLeibValue().getValue()+") 费 用 核 对", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 2));
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = false;
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_RIGHT);
			rt.body.setColAlign(6, Table.ALIGN_RIGHT);
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setPageRows(20);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			for(int i = 2; i< rt.body.getRows(); i++){
				rt.body.merge(i, 2, i, 4);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 4);
			rt.setDefaultTitle(1, 2, "制表单位：" + ((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 4, getRiqi() + " 至 " + getRiq2(), Table.ALIGN_CENTER);
			rt.setDefaultTitle(7, 2, "单位：吨、车", Table.ALIGN_RIGHT);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
			rt.setDefautlFooter(4, 2, "审核：", Table.ALIGN_CENTER);
			rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);
			
		} else { // 联票查询
			
			sql = 
				"select /*grouping(j.fahdw) f, grouping(j.meikdwmc) mk, grouping(j.yd_ches) ydcs, grouping(j.yd_biaoz) ydbz, grouping(j.yunfdjb_id) ydid,\n" +
				"       grouping(j.mingc) mc, grouping(j.fahb_id) fhid,*/\n" + 
				"       decode(grouping(j.fahdw), 0, j.fahdw, 1, '总计') fahdw,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 0, j.meikdwmc, 1, '合计', 2, '总计') meikdwmc,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 0, nvl2(j.mingc, max(to_char(j.daohrq, 'yyyy-mm-dd')), '小计'), 1, '合计', 2, '总计') daohrq,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 0, nvl2(j.mingc, max(j.chec), '小计'), 1, '合计', 2, '总计') chec,\n" + 
				"       decode(grouping(j.fahdw)+grouping(j.meikdwmc), 0, nvl2(j.mingc, max(j.danjbh), '小计'), 1, '合计', 2, '总计') danjbh,\n" + 
				"       decode(grouping(j.meikdwmc), 0, j.yd_ches, 1, sum(j.yd_ches1)) yd_ches,\n" + 
				"       decode(grouping(j.meikdwmc), 0, j.yd_biaoz, 1, sum(j.yd_biaoz1)) yd_biaoz,\n" + 
				"       decode(grouping(j.meikdwmc), 0, nvl2(j.mingc, j.mingc, ''), 1, '')  mingc,\n" + 
				"       decode(grouping(j.meikdwmc), 0, max(j.zhi), 1, sum(j.zhi1)) zhi,\n" + 
				"       decode(grouping(j.meikdwmc), 0, sum(j.ches), 1, sum(j.ches1)) ches,\n" + 
				"       decode(grouping(j.meikdwmc), 0, sum(j.biaoz), 1, sum(j.biaoz1)) biaoz from\n" + 
				"  (select /*grouping(x.fahdw) f, grouping(x.meikdwmc) mk, grouping(x.yunfdjb_id) yfdid,\n" + 
				"         grouping(x.mingc) mc, grouping(x.fahb_id) fhid,*/\n" + 
				"         x.fahdw,\n" + 
				"         x.meikdwmc,\n" + 
				"         max(x.daohrq) daohrq,\n" + 
				"         max(x.chec) chec,\n" + 
				"         max(x.danjbh) danjbh,\n" + 
				"         max(x.yd_ches) yd_ches,\n" + 
				"         max(x.yd_biaoz) yd_biaoz,\n" + 
				"         decode(grouping(x.yunfdjb_id)+grouping(x.mingc),0,0,1,max(x.yd_ches)) yd_ches1,\n" + 
				"         decode(grouping(x.yunfdjb_id)+grouping(x.mingc),0,0,1,max(x.yd_biaoz)) yd_biaoz1,\n" + 
				"         x.yunfdjb_id,\n" + 
				"         x.mingc,\n" + 
				"         decode(grouping(x.fahb_id),0,max(x.zhi),1,max(x.zongje)) zhi,\n" + 
				"         decode(grouping(x.yunfdjb_id)+grouping(x.mingc),0,0,1,max(x.zongje)) zhi1,\n" + 
				"         x.fahb_id,\n" + 
				"         count(distinct x.chepb_id) ches,\n" + 
				"         decode(grouping(x.fahb_id),0,sum(x.biaoz),1,max(x.yd_biaoz)) biaoz,\n" + 
				"         decode(grouping(x.yunfdjb_id)+grouping(x.mingc),0,0,1,count(distinct x.chepb_id)) ches1,\n" + 
				"         decode(grouping(x.yunfdjb_id)+grouping(x.mingc),0,0,1,max(x.yd_biaoz)) biaoz1 from\n" + 
				"    (select max(fc.fahdw) as fahdw,\n" + 
				"           max(fc.meikdwmc) as meikdwmc,\n" + 
				"           max(fc.daohrq) as daohrq,\n" + 
				"           max(fc.chec) as chec,\n" + 
				"           max(nvl(yfdj.danjbh, to_char(yfdj.caozsj, 'yyyy-mm-dd'))) danjbh,\n" + 
				"           max(yfdj.ches) yd_ches,\n" + 
				"           max(yfdj.biaoz) yd_biaoz,\n" + 
				"           max(yfdj.zongje) zongje,\n" + 
				"           djcp.yunfdjb_id,\n" + 
				"           fymc.mingc,\n" + 
				"           sum(fy.zhi) zhi,\n" + 
				"           fc.fahb_id,\n" + 
				"           max(fc.ches) as ches,\n" + 
				"           max(fc.biaoz) as biaoz,\n" + 
				"           fc.chepb_id\n" + 
				"      from (select g.mingc as fahdw,\n" + 
				"                   m.mingc as meikdwmc,\n" + 
				"                   c.fahb_id,\n" + 
				"                   c.id chepb_id,\n" + 
				"                   c.biaoz,\n" + 
				"                   1 as ches,\n" + 
				"                   f.chec,\n" + 
				"                   f.daohrq\n" + 
				"              from fahb f, chepb c, gongysb g, meikxxb m\n" + 
				"             where f.id = c.fahb_id\n" + 
				"               and f.gongysb_id = g.id\n" + 
				"               and f.meikxxb_id = m.id\n" + 
				"               and f.daohrq >= to_date('"+ getRiqi() +"', 'yyyy-mm-dd')\n" + 
				"               and f.daohrq < to_date('"+ getRiq2() +"', 'yyyy-mm-dd') + 1\n "+ strGongys + "\n) fc,\n" +
				"           danjcpb djcp,\n" + 
				"           yunfdjb yfdj,\n" + 
				"           feiyb fy,\n" + 
				"           feiyxmb fyxm,\n" + 
				"           feiymcb fymc\n" + 
				"     where fc.chepb_id = djcp.chepb_id\n" + 
				"       and djcp.yunfdjb_id = yfdj.id\n" + 
				"       and yfdj.ches > 1\n" + 
				"       and yfdj.feiyb_id = fy.feiyb_id\n" + 
				"       and fy.feiyxmb_id = fyxm.id\n" + 
				"       and fyxm.feiymcb_id = fymc.id\n" + 
				"     group by djcp.yunfdjb_id, fymc.mingc, fc.chepb_id, fc.fahb_id\n" + 
				"     order by djcp.yunfdjb_id, fymc.mingc, fc.chepb_id, fc.fahb_id) x\n" + 
				"  group by rollup (x.fahdw, x.meikdwmc, x.yunfdjb_id, x.mingc, x.fahb_id)\n" + 
				"  having grouping(x.yunfdjb_id)+grouping(x.mingc) = 1 or grouping(x.fahb_id) = 0\n" + 
				"  order by x.fahdw, x.meikdwmc, x.yunfdjb_id, x.mingc, x.fahb_id) j\n" + 
				"group by rollup (j.fahdw, j.meikdwmc, j.yd_ches, j.yd_biaoz, j.yunfdjb_id, j.mingc, j.fahb_id)\n" + 
				"having grouping(j.fahb_id) = 0 or grouping(j.meikdwmc) = 1\n" + 
				"order by j.fahdw, j.meikdwmc, j.yd_ches, j.yd_biaoz, j.yunfdjb_id, j.mingc, j.fahb_id";

			rs = cn.getResultSet(sql);
			
			String ArrHeader[][] = new String[1][11];
			ArrHeader[0] = new String[] { "发货单位", "煤矿单位", "到货日期", "车次", "单据编号", "车数", "票重", "费用", "金额(元)", "车数", "票重"};

			int ArrWidth[] = new int[] {100, 100, 80, 80, 100, 60, 80, 100, 80, 60, 80};

			rt.setTitle("货 票("+getLeibValue().getValue()+") 费 用 核 对", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 2));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(20);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = false;
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);
			rt.body.setPageRows(20);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			for(int i = 2; i< rt.body.getRows() ; i++){
				rt.body.merge(i, 2, i, 5);
			}
			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 5);
			rt.setDefaultTitle(1, 2, "制表单位：" + ((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 4, getRiqi() + " 至 " + getRiq2(), Table.ALIGN_CENTER);
			rt.setDefaultTitle(10, 2, "单位：吨、车", Table.ALIGN_RIGHT);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "审核：", Table.ALIGN_CENTER);
			rt.setDefautlFooter(8, 2, "制表：", Table.ALIGN_RIGHT);
			
		}
		
//		最初的SQL查询语句
//		sbsql.append("\t\tselect case when grouping(fahdw)+grouping(meikdwmc) = 2  then '总计' else fahdw end 发货单位,\n"
//						+ "\t\t \t   case when grouping(meikdwmc)=1 then '' else meikdwmc end 煤矿单位,\n"
//						+ "\t\t       case when grouping(fm.mingc)<>1 then to_char(max(daohrq),'yyyy-mm-dd') else '' end 到货日期,\n"
//						+ "\t      case when grouping(fm.mingc)<>1 then max(chec) else  decode(grouping(fahb_id)+grouping(fahdw),1,'合计','')  end  车次,\n"
//						+ "\t      sum(distinct d.ches) as 车数,sum(distinct d.biaoz) as 票重,case when grouping(fm.mingc)=1 and grouping(fahb_id)<>1 then '小计' else fm.mingc end 费用,\n"
//						+ "\t     sum(fe.zhi) as 金额\n"
//						+ "\t from\n"
//						+ " (select  fahb_id,max(fahdw) as fahdw, max(meikdwmc) as meikdwmc,max(daohrq) as daohrq,max(chec) as chec,sum(biaoz) as biaoz,\n " 
//						+ "	sum(ches) as ches,p.yunfdjb_id													\n"
//						+ "\t   from																		\n"
//						+ "   ( select g.mingc as fahdw, m.mingc as meikdwmc,c.fahb_id,f.biaoz,c.id,f.ches,chec,daohrq	\n"
//						+ "\t\t               from fahb f,chepb c,gongysb g,meikxxb m						\n"
//						+ "\t               where f.id=c.fahb_id											\n"
//						+ "\t\t                      and f.gongysb_id=g.id and f.meikxxb_id=m.id 			\n"
//						+ "\t						 and f.daohrq>=to_date('"
//						+ getRiqi()
//						+ "','yyyy-mm-dd') and f.daohrq<=to_date('"
//						+ getRiq2()
//						+ "','yyyy-mm-dd') "+strGongys+" ) fc,\n"
//						+ "                        danjcpb p												\n"
//						+ "\t         where fc.id=p.chepb_id												\n"
//						+ "\n"
//						+ "\n"
//						+ "\t    group by fahb_id,  p.yunfdjb_id) d,										\n"
//						+ "    	feiyb fe,feiyxmb x,feiymcb fm,yunfdjb y									\n"
//						+ "\t		where fe.feiyxmb_id=x.id												\n"
//						+ "     and x.feiymcb_id=fm.id													\n"
//						+ "     and d.yunfdjb_id=y.id														\n"
//						+ "     and fe.feiyb_id=y.feiyb_id												\n"
//						+ "\n"
//						+ "\t group by rollup(fahdw,meikdwmc,fahb_id,fm.mingc)\n"
//						+ " having not (grouping(fahb_id)=1 and grouping(meikdwmc)=0) \n"
//						+ " order by grouping(fahdw),fahdw,grouping(meikdwmc),meikdwmc,	\n"
//						+ " grouping(fm.mingc),fm.mingc"
//						);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;

			getSelectData();
		}

	}

	// 入帐

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");

			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			// 日期
			setRiqi(null);
			setRiq2(null);
			// 供应商
			this.setGongysValue(null); // DropDownBean10
			this.setGongysModel(null); // ProSelectionModel10
			this.getGongysModels();
//			煤矿单位
			this.setMeikdwValue(null); // DropDownBean1
			this.setMeikdwModel(null); // ProSelectionModel1
			this.getMeikdwModels();
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
				
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		blnIsBegin = true;
		// 如果时间改变更新供应商名称
		if (this.riqichange || this.riq2change) {

			this.getGongysModels();
			this.getMeikdwModels();
			this.riqichange = false;
			this.riq2change = false;
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

	// 供货单位

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {

		String sql = " select g.id,g.mingc from gongysb g,fahb f\n"
				+ " where f.gongysb_id=g.id and g.leix=1 and f.daohrq>=to_date('"
				+ this.getRiqi() + "','yyyy-MM-dd')\n"
				+ " and f.daohrq<=to_date('" + this.getRiq2()
				+ "','yyyy-MM-dd') order by g.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
//	供货单位_end
	
//	煤矿单位
	public IDropDownBean getMeikdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getMeikdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikdwValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != value) {

			((Visit) getPage().getVisit()).setDropDownBean1(value);
		}
	}

	public void setMeikdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getMeikdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getMeikdwModels() {
		String sql ="";
		
		if(this.getGongysValue().getId()==-1){
//			全部
			sql = " select m.id,m.mingc from meikxxb m,fahb f\n"
				+ " where f.meikxxb_id=m.id and f.daohrq>=to_date('"
				+ this.getRiqi() + "','yyyy-MM-dd')\n"
				+ " and f.daohrq<=to_date('" + this.getRiq2()
				+ "','yyyy-MM-dd') order by m.mingc";
		}else{
			
			sql = " select m.id,m.mingc from meikxxb m,fahb f\n"
				+ " where f.meikxxb_id=m.id and f.gongysb_id="+this.getGongysValue().getId() 
				+ " and f.daohrq>=to_date('"
				+ this.getRiqi() + "','yyyy-MM-dd')\n"
				+ " and f.daohrq<=to_date('" + this.getRiq2()
				+ "','yyyy-MM-dd') order by m.mingc";
		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	煤矿单位_end

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("GongysDropDown");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("煤矿单位:"));
		ComboBox meikdw = new ComboBox();
		meikdw.setTransform("MeikdwDropDown");
		meikdw.setEditable(true);
		meikdw.setWidth(100);
		meikdw.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meikdw);
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("票据类型："));
		ComboBox Leibcomb = new ComboBox();
		Leibcomb.setWidth(100);
		Leibcomb.setTransform("Leib");
		Leibcomb.setId("Leib");
		Leibcomb.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		Leibcomb.setLazyRender(true);
		Leibcomb.setEditable(true);
		tb1.addField(Leibcomb);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		// if (getRuzztValue().getId() == 0) {
		// ToolbarButton tb2 = new ToolbarButton(null, "入帐",
		// "function(){ document.Form0.SaveButton.click();}");
		// tb2.setId("savebt");
		// tb1.addItem(tb2);
		// }

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