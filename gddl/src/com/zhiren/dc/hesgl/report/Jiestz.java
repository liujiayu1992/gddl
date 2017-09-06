package com.zhiren.dc.hesgl.report;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：张立东
 * 时间：2009-10-30
 * 描述：结算台帐中增加按入账日期的查询复选框
 */

/*
 * 作者：  王伟
 * 时间：  2009-10-31
 * 描述：  在硫折价中添加了Star的折价，即硫折价=Stad的折价+Star的折价
 */
/*
 * 作者：玉沙沙
 * 时间：2012-3-9
 * 描述：大开热电要求增加查询条件"电厂树、煤矿单位"
 */
/*
 * 作者：赵胜男
 * 时间：2013-01-15
 * 描述：大开热电要求增加单位列，并按单位列进行汇总.
 */
/*
 * 作者：李华廷
 * 时间：2013-06-03
 * 描述：修改sql，jiesyfb和diancxxb关联去掉.
 */
/*
 * 作者：夏峥
 * 时间：2014-02-12
 * 描述：修改除数为0的BUG
 */
/*
 * 作者：夏峥
 * 时间：2014-04-1
 * 描述:修改结算标煤单价的计算BUG
 */
public class Jiestz extends BasePage implements PageValidateListener {

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

	private String check = "false";

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			riq = DateUtil.FormatDate(stra.getTime());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getEndriqDateSelect() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setEndriqDateSelect(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

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

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _GongysChick = false;
	
	public void submit(IRequestCycle cycle) {
		if (_GongysChick) {
			_GongysChick= false;
			getMeikxxModels();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			
		}
		getSelectData();
	}
	//判断是否为大开热电
	boolean isDakrd=false;
	public boolean isDakrd(){
		JDBCcon con = new JDBCcon();
		String sql="select zhi from xitxxb where mingc='国电电力大连开发区热电厂'";
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			isDakrd=true;
		}
		rsl.close();
		con.Close();
		return isDakrd;
	}

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		isDakrd();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			// _BeginriqValue = DateUtil.AddDate(new Date(), -1,
			// DateUtil.AddType_intDay);
			visit.setList1(null);
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			setDiancmcModels();
			setDiancjb();
			if(isDakrd){
			setMeikxxValue(null);
			setMeikxxModel(null);
			}
			this.getSelectData();
		}
		isBegin = true;
		
		getToolBars();

	}

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;

		return getSelectData();

	}

	private boolean isBegin = false;

	/**
	 * 发电集团电煤信息日报表
	 * 
	 * @author xzy
	 */
	private String getSelectData() {
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		// String riq=OraDate(_BeginriqValue);//当前日期
		String riq_sql = "";
		if (this.getCheck().equals("true")) {
			riq_sql = " and j.ruzrq >= to_date('" + getBeginriqDateSelect()
					+ "','yyyy-MM-dd') \n" + " and j.ruzrq <= to_date('"
					+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		} else {
			riq_sql = " and j.jiesrq >= to_date('" + getBeginriqDateSelect()
					+ "','yyyy-MM-dd') \n" + " and j.jiesrq <= to_date('"
					+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		}

		String gys_sql = "";
		if (getGongysValue().getValue() == "全部") {
			gys_sql = " ";
		} else {
			gys_sql = "and g.id =" + getGongysValue().getId() + " \n";
		}
		
		String yunfxx = // 运费信息
		  "       sum(nvl(y.guotyf,0) + nvl(y.kuangqyf,0)) as yunf,\n"
		+ "       sum(nvl(y.guotzf,0) + nvl(y.kuangqzf,0)) as zaf,\n"
		+ "       sum(nvl(y.buhsyf,0)) as buhsyf,\n"
		+ "       sum(nvl(y.shuik,0)) as yunfsk,\n"
		+ "       sum(nvl(y.hansyf,0)) as hansyf,\n"
		+ "       sum(nvl(j.hansmk,0) + nvl(y.hansyf,0)) as zongje,\n";
		
		if (MainGlobal.getXitxx_item("数量", "是否重新关联煤款结算单对应的运费信息", 
			String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//			由于国电青铝电厂的煤款和运费是分开结算的，并且可能一张煤款结算单对应多个运输单位，那么还会有多个运费结算单，
//			这种情况比较特殊，所以需要在此重新关联出运费信息
			yunfxx = 
				"sum(getYunfxx4Jiesbid(j.id, 'guotyf') + getYunfxx4Jiesbid(j.id, 'kuangqyf')) as yunf,\n" +
				"sum(getYunfxx4Jiesbid(j.id, 'guotzf') + getYunfxx4Jiesbid(j.id, 'kuangqzf')) as zaf,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'buhsyf')) as buhsyf,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'shuik')) as yunfsk,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'hansyf')) as hansyf,\n" + 
				"sum(nvl(j.hansmk, 0) + getYunfxx4Jiesbid(j.id, 'hansyf')) as zongje,";
		}

		StringBuffer strSQL = new StringBuffer();
		String sql="";
		if(isDakrd){
			 sql = "select\n" +
			 		"danw,\n"
					+ "GONGYS,\n"
					+ "JIESRQ,\n"
					+ "RUZRQ,\n"
					+ "BIANM,\n"
					+ "PIAOZ,\n"
					+ "JINGZ,\n"
					+ "JIESSL,\n"
					+ "YANSRL,\n"
					+ "JIESRL,\n"
					+ "HANSDJ,\n"
					+ "BUHSMK,\n"
					+ "SHUIK,\n"
					+ "HANSMK,\n"
					+ "YUNF,\n"
					+ "ZAF,\n"
					+ "BUHSYF,\n"
					+ "YUNFSK,\n"
					+ "HANSYF,\n"
					+ "ZONGJE,\n"
     				+ "BIAOMDJ,\n"
					+ "BUHSBMDJ,\n"
					+ "RELZJE,\n"
					+ "LIUFZJE,\n"
					+ "SHUIFZJE,\n"
					+ "HUIFZJE,\n"
					+ "HUIFFZJE\n"
					+ " from (select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danw,\n"
					+"decode(grouping(g.mingc)+grouping(dc.mingc),1,'小计',g.mingc) as gongys,\n"
					+ "       decode(grouping(g.mingc)+grouping(j.jiesrq),1,'小计',to_char(j.jiesrq,'yyyy-MM-dd')) as jiesrq,\n"
					+ "       to_char(j.ruzrq,'yyyy-MM-dd') as ruzrq,\n"
					+ "       j.bianm,\n"
					+ "       decode(grouping(g.mingc), null, '', sum(getjiesdzb('jiesb',j.id,'结算数量','gongf'))) as piaoz,\n"
					+ "       decode(grouping(g.mingc),null,'',sum(j.guohl)) as jingz,\n"
					+ "       decode(grouping(g.mingc),null,'',sum(j.jiessl)) as jiessl,\n"
					+ "       round_new(sum(decode(j.jiessl,0,0,getjiesdzb('jiesb',j.id,'Qnetar','changf')*j.jiessl))/sum(decode(j.jiessl,0,1,j.jiessl)),0) as yansrl,\n"
					+ " \t\tround(sum(decode(j.jiessl,0,0,j.jiessl*getjiesdzb('jiesb', j.id, 'Qnetar', 'jies')))/sum(decode(j.jiessl,0,1,j.jiessl)),0) as jiesrl,\n"
					+ "\t       round(sum(decode(j.jiessl,0,0,(j.hansmk/j.jiessl)*j.jiessl))/sum(decode(j.jiessl,0,1,j.jiessl)),2) as hansdj,\n"
					+ "       sum(nvl(j.buhsmk,0)) as buhsmk,\n"
					+ "       sum(nvl(j.shuik,0)) as shuik,\n"
					+ "       sum(nvl(j.hansmk,0)) as hansmk,\n"
					+ yunfxx
					+ "\t\tround_new(sum(decode(nvl(j.jiessl,0) * nvl(j.jiesrl,0),\n"
					+ "                      0,\n"
					+ "                      0,\n"
					+ "                      round_new(round_new((nvl(j.hansmk,0)+nvl(y.hansyf,0))/ j.jiessl,2)* 7000/getjiesdzb('jiesb', j.id, 'Qnetar', 'jies'),2))*j.jiessl)/\n"
					+ "                      sum(decode(j.jiessl,0,1,j.jiessl)),2)\n"
					+ "                       as biaomdj,\n"
					+ "\n"
					+ "       round_new(sum(decode(nvl(j.jiessl,0) * nvl(j.jiesrl,0),\n"
					+ "                      0,\n"
					+ "                      0,\n"
					+ "                      round_new(round_new(((nvl(j.hansmk,0)-nvl(j.shuik,0))+(nvl(y.hansyf,0)-nvl(y.shuik,0)))/ j.jiessl,2)* 7000/getjiesdzb('jiesb', j.id, 'Qnetar', 'jies'),2))*j.jiessl)/\n"
					+ "                      sum(decode(j.jiessl,0,1,j.jiessl)),2)\n"
					+ "                       as buhsbmdj,       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Qnetar','zhejje'),0)) as relzje,\n"
					+ "       --sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Std','zhejje'),0)) as liufzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Std','zhejje'),0) + nvl(getjiesdzb('jiesb',nvl(j.id,0),'Star','zhejje'),0)+ nvl(getjiesdzb('jiesb',nvl(j.id,0),'Stad','zhejje'),0)) as liufzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Mt','zhejje'),0)) as shuifzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Ad','zhejje'),0) +nvl(getjiesdzb('jiesb',nvl(j.id,0),'Aar','zhejje'),0)+nvl(getjiesdzb('jiesb',nvl(j.id,0),'Aad','zhejje'),0)) as huifzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Vdaf','zhejje'),0)) as huiffzje,dc.mingc\n"
					+ "\n"
					+ " from jiesb j,jiesyfb y,gongysb g,diancxxb dc \n"
					+ " where y.diancjsmkb_id(+) = j.id\n"
					+ "   and j.gongysb_id = g.id\n"
					+ "   and j.diancxxb_id = y.diancxxb_id(+)\n" 
					+" AND j.diancxxb_id=dc.id\n"
					+" --AND y.diancxxb_id=dc.id\n"
					+ "	  and j.diancxxb_id IN (select id\n"
					+ "	   from(\n"
					 + "	  select id from diancxxb\n"
					+ "	   start with fuid="+getTreeid()+"\n"
					+ "	   connect by fuid=prior id\n"
					+ "	   )\n"
					+ "	   union\n"
					+ "	   select id\n"
					+ "	   from diancxxb\n"				
					+ "	   where id="+getTreeid()+")\n " 
					+ riq_sql
					+ "\n"
					+ gys_sql
					+ "\n"
					+ " group by rollup((dc.mingc),g.mingc,j.jiesrq,j.ruzrq,j.bianm)\n"
					+ " having not (grouping(j.bianm)=1 and grouping(j.jiesrq)=0)\n"
					+ " order by grouping(dc.mingc) desc,dc.mingc ,grouping(g.mingc) desc,g.mingc,grouping(j.jiesrq) desc,j.jiesrq)";

			strSQL.append(sql);
			String ArrHeader[][] = new String[1][27];
			ArrHeader[0] = new String[] {"单位", "供应商", "结算日期", "入账日期", "结算编号",
					"票重<br>(吨)", "净重<br>(吨)", "结算量<br>(吨)", "验收热量<br>(kcal/kg)",
					"结算热量<br>(kcal/kg)", "单价<br>（含税）", "价款金额<br>(元)",
					"价款税款<br>(元)", "价税合计<br>(元)", "运费<br>(元)", "杂费<br>(元)",
					"不含税运费<br>(元)", "运费税款<br>(元)", "运杂费合计<br>(元)", "总金额<br>(元)",
					"标煤单价(含税)", "标煤单价(不含税)", "热量折价金额<br>(元)", "硫分折价金额<br>(元)",
					"水分折价金额<br>(元)", "灰分折价金额<br>(元)", "挥发份折价金额<br>(元)" };

			 int ArrWidth[] = new int[] {60,180, 90, 90, 170, 65, 65, 65, 60, 60, 60,
					85, 85, 85, 85, 85, 85, 85, 85, 80, 58, 58, 80, 80, 75, 75, 85};
			 ResultSet rs = cn.getResultSet(strSQL.toString());

				// 数据
				Table tb = new Table(rs, 1, 0, 2);
				rt.setBody(tb);

				rt.setTitle("结算统计台帐", ArrWidth);
				// rt.setDefaultTitle(1, 2, "单位：美元/吨", Table.ALIGN_LEFT);
				rt.body.setRowHeight(1, 40);
				rt.body.setWidth(ArrWidth);
				rt.body.setPageRows(18);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.setColAlign(1, Table.ALIGN_CENTER);
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				rt.body.mergeFixedRowCol();


				_CurrentPage = 1;
				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
		}else{
			 sql = "select\n"
					+ "GONGYS,\n"
					+ "JIESRQ,\n"
					+ "RUZRQ,\n"
					+ "BIANM,\n"
					+ "PIAOZ,\n"
					+ "JINGZ,\n"
					+ "JIESSL,\n"
					+ "YANSRL,\n"
					+ "JIESRL,\n"
					+ "HANSDJ,\n"
					+ "BUHSMK,\n"
					+ "SHUIK,\n"
					+ "HANSMK,\n"
					+ "YUNF,\n"
					+ "ZAF,\n"
					+ "BUHSYF,\n"
					+ "YUNFSK,\n"
					+ "HANSYF,\n"
					+ "ZONGJE,\n"
					+ "decode(jiesrl,0,0,round_new(DECODE(JIESSL,0,0,ROUND((YUNF+HANSMK)/JIESSL,2))*29.271/round_new(jiesrl*0.0041816,2),2)) BIAOMDJ,\n"
					+ "decode(jiesrl,0,0,round_new(round_new(DECODE(JIESSL,0,0,round((BUHSMK+BUHSYF)/JIESSL,2))*29.271/round_new(jiesrl*0.0041816,2),2),2)) BUHSBMDJ,\n"
//					+ "BIAOMDJ,\n"
//					+ "BUHSBMDJ,\n"
					+ "RELZJE,\n"
					+ "LIUFZJE,\n"
					+ "SHUIFZJE,\n"
					+ "HUIFZJE,\n"
					+ "HUIFFZJE\n"
					+ " from (select decode(grouping(g.mingc),1,'总计',g.mingc) as gongys,\n"
					+ "       decode(grouping(g.mingc)+grouping(j.jiesrq),1,'小计',to_char(j.jiesrq,'yyyy-MM-dd')) as jiesrq,\n"
					+ "       to_char(j.ruzrq,'yyyy-MM-dd') as ruzrq,\n"
					+ "       j.bianm,\n"
					+ "       decode(grouping(g.mingc), null, '', sum(getjiesdzb('jiesb',j.id,'结算数量','gongf'))) as piaoz,\n"
					+ "       decode(grouping(g.mingc),null,'',sum(j.guohl)) as jingz,\n"
					+ "       decode(grouping(g.mingc),null,'',sum(j.jiessl)) as jiessl,\n"
					+ "       round_new(sum(decode(j.jiessl,0,0,getjiesdzb('jiesb',j.id,'Qnetar','changf')*j.jiessl))/sum(decode(j.jiessl,0,1,j.jiessl)),0) as yansrl,\n"
					+ " \t\tround(sum(decode(j.jiessl,0,0,j.jiessl*getjiesdzb('jiesb', j.id, 'Qnetar', 'jies')))/sum(decode(j.jiessl,0,1,j.jiessl)),0) as jiesrl,\n"
					+ "\t       round(sum(decode(j.jiessl,0,0,(j.hansmk/j.jiessl)*j.jiessl))/sum(decode(j.jiessl,0,1,j.jiessl)),2) as hansdj,\n"
					+ "       sum(nvl(j.buhsmk,0)) as buhsmk,\n"
					+ "       sum(nvl(j.shuik,0)) as shuik,\n"
					+ "       sum(nvl(j.hansmk,0)) as hansmk,\n"
					+ yunfxx
					+ "\t\tround_new(sum(decode(nvl(j.jiessl,0) * nvl(j.jiesrl,0),\n"
					+ "                      0,\n"
					+ "                      0,\n"
					+ "                      round_new(round_new((nvl(j.hansmk,0)+nvl(y.hansyf,0))/ j.jiessl,2)* 7000/getjiesdzb('jiesb', j.id, 'Qnetar', 'jies'),2))*j.jiessl)/\n"
					+ "                      sum(decode(j.jiessl,0,1,j.jiessl)),2)\n"
					+ "                       as biaomdj,\n"
					+ "\n"
					+ "       round_new(sum(decode(nvl(j.jiessl,0) * nvl(j.jiesrl,0),\n"
					+ "                      0,\n"
					+ "                      0,\n"
					+ "                      round_new(round_new(((nvl(j.hansmk,0)-nvl(j.shuik,0))+(nvl(y.hansyf,0)-nvl(y.shuik,0)))/ j.jiessl,2)* 7000/getjiesdzb('jiesb', j.id, 'Qnetar', 'jies'),2))*j.jiessl)/\n"
					+ "                      sum(decode(j.jiessl,0,1,j.jiessl)),2)\n"
					+ "                       as buhsbmdj,       "
					+ "		  sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Qnetar','zhejje'),0)) as relzje,\n"
					+ "       --sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Std','zhejje'),0)) as liufzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Std','zhejje'),0) + nvl(getjiesdzb('jiesb',nvl(j.id,0),'Star','zhejje'),0)+ nvl(getjiesdzb('jiesb',nvl(j.id,0),'Stad','zhejje'),0)) as liufzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Mt','zhejje'),0)) as shuifzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Ad','zhejje'),0) +nvl(getjiesdzb('jiesb',nvl(j.id,0),'Aar','zhejje'),0)+nvl(getjiesdzb('jiesb',nvl(j.id,0),'Aad','zhejje'),0)) as huifzje,\n"
					+ "       sum(nvl(getjiesdzb('jiesb',nvl(j.id,0),'Vdaf','zhejje'),0)) as huiffzje\n"
					+ "\n"
					+ " from jiesb j,jiesyfb y,gongysb g\n"
					+ " where y.diancjsmkb_id(+) = j.id\n"
					+ "   and j.gongysb_id = g.id\n"
					+ riq_sql
					+ "\n"
					+ gys_sql
					+ "\n"
					+ " group by rollup(g.mingc,j.jiesrq,j.ruzrq,j.bianm)\n"
					+ " having not (grouping(j.bianm)=1 and grouping(j.jiesrq)=0)\n"
					+ " order by grouping(g.mingc) desc,g.mingc,grouping(j.jiesrq) desc,j.jiesrq)";

			strSQL.append(sql);
			String ArrHeader[][] = new String[1][26];
			ArrHeader[0] = new String[] { "供应商", "结算日期", "入账日期", "结算编号",
					"票重<br>(吨)", "净重<br>(吨)", "结算量<br>(吨)", "验收热量<br>(kcal/kg)",
					"结算热量<br>(kcal/kg)", "单价<br>（含税）", "价款金额<br>(元)",
					"价款税款<br>(元)", "价税合计<br>(元)", "运费<br>(元)", "杂费<br>(元)",
					"不含税运费<br>(元)", "运费税款<br>(元)", "运杂费合计<br>(元)", "总金额<br>(元)",
					"标煤单价(含税)", "标煤单价(不含税)", "热量折价金额<br>(元)", "硫分折价金额<br>(元)",
					"水分折价金额<br>(元)", "灰分折价金额<br>(元)", "挥发份折价金额<br>(元)" };

			 int ArrWidth[] = new int[] {180, 90, 90, 170, 65, 65, 65, 60, 60, 60,
					85, 85, 85, 85, 85, 85, 85, 85, 80, 58, 58, 80, 80, 75, 75, 85 };
			 
			 ResultSet rs = cn.getResultSet(strSQL.toString());

				// 数据
				Table tb = new Table(rs, 1, 0, 1);
				rt.setBody(tb);

				rt.setTitle("结算统计台帐", ArrWidth);
				// rt.setDefaultTitle(1, 2, "单位：美元/吨", Table.ALIGN_LEFT);
				rt.body.setRowHeight(1, 40);
				rt.body.setWidth(ArrWidth);
				rt.body.setPageRows(18);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.setColAlign(1, Table.ALIGN_CENTER);
				rt.body.setColAlign(2, Table.ALIGN_CENTER);
				rt.body.mergeFixedRowCol();

				_CurrentPage = 1;
				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
		}
		

		
		cn.Close();
		return rt.getAllPagesHtml();
		//		
	}

	// ******************************************************************************

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	// ***************************报表初始设置***************************//
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
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
		
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginriqDateSelect());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndriqDateSelect());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("供应商:"));
		ComboBox gys = new ComboBox();
		gys.setTransform("GongysDropDown");
		gys.setEditable(true);
		gys.setWidth(200);
		gys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gys);
		
        if(isDakrd){
		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MEIKXXSelect");
		meik.setEditable(true);
		meik.setWidth(150);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);
		}

		Checkbox chk = new Checkbox();
		chk.setId("CHECKED");
		if (this.getCheck().equals("true")) {
			chk.setChecked(true);
		} else {
			chk.setChecked(false);
		}
		chk
				.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true'}else{document.all.CHECKED.value='false'}}");
		tb1.addField(chk);
		tb1.addText(new ToolbarText("入账日期"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	// 供应商下拉框
	public boolean _Gongyschange = false;

	public IDropDownBean getGongysValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModels() {
//		JDBCcon con = new JDBCcon();
//		List List = new ArrayList();
		_GongysChick=true;
		String sql = "select g.id,g.mingc from gongysb g,jiesb j \n"
				+ " where j.gongysb_id = g.id \n"
				+ " 	and j.jiesrq >= to_date('" + getBeginriqDateSelect()
				+ "','yyyy-MM-dd') \n" + " 	and j.jiesrq <= to_date('"
				+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
	
//  获得选择的树节点的对应的供应商名称   
//    private String[] getGys(String id){ 
//    	String[] gys={"全部","-1"};
//    	if(id==null || "".equals(id)){
//    		return gys;
//    	}
//		JDBCcon con=new JDBCcon();
//		String sql="select mingc,lx from vwgongysmk where id = " + id;
//		ResultSetList rsl=con.getResultSetList(sql);
//		if(rsl.next()){
//			gys[0]=rsl.getString("mingc");
//			gys[1]=rsl.getString("lx");
//		}
//		rsl.close();
//		con.Close();
//		return gys;
//	}
//  取得供应商参数SQL
//    private String getGysParam(){
////		供应商煤矿条件
//		String gyssql = "";
//		if("1".equals(getGys(getTreeid())[1])){
//			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
//		}else if("0".equals(getGys(getTreeid())[1])){
//			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
//		}
//		return gyssql;
//    }
	//煤矿
//private boolean flag = false;
	
	private IDropDownBean MeikxxValue;

	public IDropDownBean getMeikxxValue() {
		if (MeikxxValue == null) {
			MeikxxValue = (IDropDownBean) MeikxxModel.getOption(0);
		}
		return MeikxxValue;
	}

	public void setMeikxxValue(IDropDownBean Value) {
		if (!(MeikxxValue == Value)) {
			MeikxxValue = Value;
//			flag = true;
		}
	}

	private IPropertySelectionModel MeikxxModel;

	public void setMeikxxModel(IPropertySelectionModel value) {
		MeikxxModel = value;
	}

	public IPropertySelectionModel getMeikxxModel() {
		if (MeikxxModel == null) {
			getMeikxxModels();
		}
		return MeikxxModel;
	}

	public IPropertySelectionModel getMeikxxModels() {
		String sql = "";
		if (getGongysValue().getValue() == "全部") {
			 sql = "select id,mingc from meikxxb where zhuangt=1 order by mingc";
		} else {
			 sql=
				 "select id, mingc\n" +
				 "  from meikxxb\n" + 
				 " where id in (select meikxxb_id from gongysmkglb gm where gm.gongysb_id ="+getGongysValue().getId()+") order by mingc";
		}
		
		MeikxxModel = new IDropDownModel(sql, "全部");
		return MeikxxModel;
	}
	//
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
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
	
	public IPropertySelectionModel Diancjb;
	public void setDiancjb() {
		String sql = "select id,jib from diancxxb";
		Diancjb =new IDropDownModel(sql);
	}

    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

}