package com.zhiren.dc.huaygl.zhiltz;
/*
 * 作者:tzf
 * 时间:2009-10-16
 * 修改内容:增加方法getZhiltz_pz（）  得到对品种小计的报表
 */
/*
 * 作者:夏峥
 * 时间:2009-05-12
 * 修改内容：修改查询语句qgrd*1000并设置无小数位，修正干基高位热的单位显示
 */
/*
 * 作者:tzf
 * 时间:2009-05-11
 * 内容:针对多采多化，实现不同数据源(zhilb  zhillsb)的数据展示
 */
/*
 * 2009-04-17
 * 王磊
 * 运输方式增加全部选项
 */
/*
 * 作者:tzf
 * 时间:2009-4-16
 * 修改内容：增加 电厂树形 ，并能以总厂查询 所有数据
 */
/*
* 作者：ww
* 时间：2009-12-26
* 描述：1、添加选择报表统计时间是按到货日期还是按发货日期，默认为到货日期
* 	   配置参数：
* 		insert into xitxxb values(
			getnewid(257),
			1,
			257,                 --diancxxbID
			'计量报表统计日期',
			'fahrq',             --fahrq,daohrq
			'',
			'数量',
			1,
			'使用'
		)
*/
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：王磊
 * 时间：2009-10-21
 * 描述：修改供应商煤矿关系后报表查询进行改变getZhiltz_pz
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：赵胜男
 * 时间：2012-06-04
 * 描述：修正报表显示时界面排序异常的问题，
 *      采用系统信息表配置方式控制是否显示煤矿地区下拉框
 * 		 
 */
/*
 * 作者：赵胜男
 * 时间：2012-09-07
 * 描述：修正报表显示时界面显示格式 
 */
/*
 * 作者：夏峥
 * 时间：2013-04-27
 * 描述：调整煤矿地区下拉框显示顺序，采用系统信息表配置方式控制是否显示供应商树 
 */
public class Zhiltz extends BasePage {
	
	private static final String REPORTNAME_HUAYBGD_ZHILB="Zhiltz_zhilb";//当没有设置资源或者 资源不正确也以此默认取数
	private static final String REPORTNAME_HUAYBGD_ZHILLSB="Zhiltz_zhillsb"; 
	
	private static final String REPORTNAME_HUAYTZ="Huaytz";//当没有设置资源或者 资源不正确也以此默认取数
	private static final String REPORTNAME_HUAYTZ_PINZ="Huaytz_pz"; 
	
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
	
	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;
		
		if(this.mstrReportName==null || this.mstrReportName.equals("")){
			return getZhiltz();
		}else if(this.mstrReportName.equals("zhilpz")){
			return this.getZhiltz_pz();
		}
		
		return "";

	}
	
	// 设置制表人默认当前用户
	private String getZhibr() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "否";
		String sql = "select zhi from xitxxb where mingc = '月报管理制表人是否默认当前用户' and diancxxb_id = " + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				zhi = rs.getString("zhi");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("是")) {
			zhibr = visit.getRenymc();
		}
		return zhibr;
	}
	
	// 判断电厂Tree中所选电厂时候还有子电厂
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}
	
    private String getTongjRq(JDBCcon con) {
    	Visit visit = (Visit) getPage().getVisit();
    	if (con == null){
    		con = new JDBCcon();
    	}
		String tongjrq=" select * from xitxxb where mingc='计量报表统计日期'  and zhuangt=1 and leib='数量' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(tongjrq);
		
		String strTongjrq="daohrq";
		
		if(rsl.next()){
			strTongjrq=rsl.getString("zhi");
		}
		
		rsl.close();
		con.Close();
		return strTongjrq;
    }

	// 全部编码
	/*
	 * 将检质数量由jingz改为发货表laimsl字段 并对弹筒热值、发热量（MJ）、发热量（Kcal）进行修约 修改时间：2008-12-04
	 * 修改人：王磊
	 */
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String strTongjRq = getTongjRq(con);
		String pinz = "";
		String s = "";
		
		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// 增加厂别处理条件;
		}
		
		if (getPinzValue().getId() != -1) {
			pinz = " and f.pinzb_id=" + getPinzValue().getId();
		}
		
		String YunsfsSql = "";
		if (getYunsfsValue().getId() != -1) {
			YunsfsSql = "           and f.yunsfsb_id = " + getYunsfsValue().getId() + "\n";
		}
		
		String MeikdqSql = "";
//		if (getMeikdqValue().getId() != -1) {
//			MeikdqSql = "           and m.meikdq_id = " + getMeikdqValue().getId() + "\n";
//		}
//
		String source_table = "";
		String source_con = "";
		if (this.getDataSource().equals(REPORTNAME_HUAYBGD_ZHILLSB)) {
			source_table = "zhillsb z";
			source_con = "z.zhilb_id";
		} else {
			source_table = "zhilb z";
			source_con = "z.id";
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer
				.append("select fhdw,\n"
						+ "       mkdw,\n"
						+ "       daohrq,\n"
						+ "       pz,\n"
						+ "       fz,\n"
						+ "       jingz,\n"
						+ "       ches,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       ad,\n"
						+ "       aar,\n"
						+ "       vad,\n"
						+ "       vdaf,\n"
						+ "       qbad*1000,\n"
						+ "       farl*1000,\n"
						+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
						+
						"       sdaf,stad,\n"
						+ "       std,star,\n"
						+ "       hdaf,had,\n"
						+
						"       fcad,\n"
						+ "       qgrd*1000\n"
						+ "  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
						+ "                      1,\n"
						+ "                      '合计',\n"
						+ "                      m.mingc) mkdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
						+ "                      grouping(f.daohrq),\n"
						+ "                      1,\n"
						+ "                      '小计',\n"
						+ "                      to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
						+ "               p.mingc pz,\n"
						+ "               c.mingc fz,\n"
						+ "               sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) jingz,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(f.yuns) yuns,\n"
						+ "               sum(f.yingk) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"
						+ v.getMtdec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getMtdec()
						+ ")) as mt,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as qbad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) /\n"
						+ "                                          sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ "))\n"
						+ "                                           * 1000 / 4.1816,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as farl,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as had,\n"
						+
						"               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrad*100/(100-z.mad)  * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as qgrd\n"
//						+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
//						+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, "+source_table+"\n"
						+ "         where f.gongysb_id = g.id(+)\n"
						+
						""
						+ s
						+ pinz +
						"           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.zhilb_id = "+source_con+"\n"
						+ "           and f." + strTongjRq + " >= to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f." + strTongjRq + " <= to_date('"
						+ getRiq2()
						+ "', 'yyyy-mm-dd')\n"
						+ YunsfsSql + MeikdqSql + getGysParam()
						+ "         group by rollup(g.mingc, m.mingc, f.daohrq, p.mingc, c.mingc)\n"
						+ "        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n"
						+ "        ORDER BY GROUPING(g.mingc) DESC,g.mingc ,grouping( m.mingc) DESC, m.mingc,grouping(f.daohrq) DESC ,f.daohrq)");
		ResultSetList paix = con.getResultSetList("select zhi from xitxxb where mingc='质检台帐日期正序排列' and leib='化验' and zhuangt=1");
		if(paix.next()){
			if(paix.getString("zhi").equals("是")){
				buffer.append(" daohrq)");
			}else{
				buffer.append(" daohrq desc)");
			}
		}
		
		ResultSetList rstmp = con.getResultSetList(buffer.toString());
		ResultSetList rs = null;
		String[][] ArrHeader = new String[1][25];
		String[] strFormat=null;
		int[] ArrWidth=null;
		int aw=0;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+(REPORTNAME_HUAYTZ)+"' order by xuh");
		ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+(REPORTNAME_HUAYTZ)+"'");
        	String Htitle="煤  质  检  验  台  帐" ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//取得报表纸张类型
    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	ArrHeader[0] = new String[] { "发货单位", "煤矿单位", "到货日期", "品种", "发站",
    				"检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
    				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
    				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
    				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
    				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
    				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
    				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
    				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf",
    				"空气<br>干燥<br>基硫<br>(%)<br>St,ad", "干燥<br>基全<br>硫(%)<br>St,d",
    				"收到<br>基全<br>硫(%)<br>St,ar", "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf",
    				"空气<br>干燥<br>基氢<br>(%)<br>Had",

    				"固定<br>碳<br>(%)<br>Fcad", "干基<br>高位<br>热<br>(J/g)<br>Qgrd" };
        	ArrWidth = new int[22];


        	ArrWidth = new int[] { 85, 100, 90, 50, 50, 40, 50, 40, 40, 40, 40, 40,
    				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };
        	strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };
    		aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//取得报表纸张类型
    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    		rt.setTitle("煤  质  检  验  台  帐", ArrWidth);
        }
//		System.out.println(buffer.toString());

		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiqi() + "至" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(Report.PAPER_COLROWS);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 22; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(22, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤  质  检  验  台  帐",""+REPORTNAME_HUAYTZ);
		return rt.getAllPagesHtml();

	}
	
	//新增质量台帐 增加品种小计功能
	private String getZhiltz_pz(){

		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		String s = "";
		String pinz = "";

		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// 增加
																		// 厂别处理条件;
		}
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		String YunsfsSql = "";
		if (getYunsfsValue().getId() != -1) {
			YunsfsSql = "           and f.yunsfsb_id = "
					+ getYunsfsValue().getId() + "\n";
		}
		
		String source_table="";
		String source_con="";
		if(this.getDataSource().equals(REPORTNAME_HUAYBGD_ZHILLSB)){
			
			source_table="zhillsb z";
			source_con="z.zhilb_id";
		}else{
			source_table="zhilb z";
			source_con="z.id";
		}

		StringBuffer buffer = new StringBuffer();

		buffer
				.append("select fhdw,\n"
						+ "       mkdw,\n"
						+ "       pz,\n"
						+ "       daohrq,\n"
						+ "       fz,\n"
						+ "       jingz,\n"
						+ "       ches,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       ad,\n"
						+ "       aar,\n"
						+ "       vad,\n"
						+ "       vdaf,\n"
						+ "       qbad*1000,\n"
						+ "       farl*1000,\n"
						+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
						+

						"       sdaf,stad,\n"
						+ "       std,star,\n"
						+ "       hdaf,had,\n"
						+

						"       fcad,\n"
						+ "       qgrd*1000\n"
						+ "  from (select decode(grouping(g.mingc), 1, '总计', g.mingc) as fhdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
						+ "                      1,\n"
						+ "                      '合计',\n"
						+ "                      m.mingc) mkdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
						+ "                       grouping(p.mingc),\n"
						+ "                      1,\n"
						+ "                      '小计',\n"
						+ "                      p.mingc ) pz,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) + grouping(p.mingc)+ \n"
						+"                  grouping(f.daohrq), 1, '小计',  to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
						+ "               c.mingc fz,\n"
						+ "               sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) jingz,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(f.yuns) yuns,\n"
						+ "               sum(f.yingk) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"
						+ v.getMtdec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getMtdec()
						+ ")) as mt,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as qbad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) /\n"
						+ "                                          sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ "))\n"
						+ "                                           * 1000 / 4.1816,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as farl,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as had,\n"
						+

						"               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrd * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as qgrd,\n"
						+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
						+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, "+source_table+"\n"
						+ "         where m.meikdq_id = g.id\n"
						+
						""
						+ s
						+ pinz +
						"           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.zhilb_id = "+source_con+"\n"
						+ "           and f.daohrq >= to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f.daohrq <= to_date('"
						+ getRiq2()
						+ "', 'yyyy-mm-dd')\n"
						+ YunsfsSql + getGysParam()
						+ "         group by rollup(g.mingc, m.mingc,p.mingc, f.daohrq, c.mingc)\n"
						+ "        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n"
						+ "          order by dx, fhdw, mx, mkdw,grouping(p.mingc) desc,pz, daohrq desc)");
		
		ResultSetList rstmp = con.getResultSetList(buffer.toString());
		ResultSetList rs = null;
		String[][] ArrHeader = new String[1][25];
		String[] strFormat=null;
		int[] ArrWidth=null;
		int aw=0;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+(REPORTNAME_HUAYTZ_PINZ)+"' order by xuh");
		ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+(REPORTNAME_HUAYTZ_PINZ)+"'");
        	String Htitle="煤  质  检  验  台  帐" ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//取得报表纸张类型
    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	ArrHeader[0] = new String[] { "发货单位", "煤矿单位",  "品种","到货日期", "发站",
    				"检质数<br>量(吨)", "车数", "全水<br>分<br>(%)Mt",
    				"空气<br>干燥<br>基水<br>分<br>(%)Mad",
    				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "干燥<br>基<br>灰分<br>(%)Ad",
    				"收到<br>基<br>灰分<br>(%)Aar", "空气干<br>燥基挥<br>发分<br>(%)Vad",
    				"干燥无<br>灰基挥<br>发分<br>(%)Vdaf", "弹筒发<br>热量<br>(J/g)<br>Qb,ad",
    				"收到基<br>低位发<br>热量(J/g)<br>Qnet,ar",
    				"收到<br>基低<br>位热<br>值(Kcal<br>/Kg)",
    				"干燥<br>无灰<br>基硫<br>(%)<br>Sdaf",
    				"空气<br>干燥<br>基硫<br>(%)<br>St,ad", "干燥<br>基全<br>硫(%)<br>St,d",
    				"收到<br>基全<br>硫(%)<br>St,ar", "干燥<br>无灰<br>基氢<br>(%)<br>Hdaf",
    				"空气<br>干燥<br>基氢<br>(%)<br>Had",

    				"固定<br>碳<br>(%)<br>Fcad", "干基<br>高位<br>热<br>(J/g)<br>Qgrd" };
    
    		ArrWidth = new int[22];

    		ArrWidth = new int[] { 85, 100,50, 90,  50, 40, 50, 40, 40, 40, 40, 40,
    				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };
    		strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
    				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };
    		aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//取得报表纸张类型
    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    		rt.setTitle("煤  质  检  验  台  帐", ArrWidth);
        }
        
//		System.out.println(buffer.toString());
//		 rs = con.getResultSet(buffer,
//				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

//		String[][] ArrHeader = new String[1][25];

		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "卸煤日期:" + getRiqi() + "至" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		 

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(Report.PAPER_COLROWS);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 22; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "制表:"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),buffer.toString(),rt,"煤  质  检  验  台  帐",""+REPORTNAME_HUAYTZ_PINZ);
		return rt.getAllPagesHtml();
	}

	// 运输方式下拉框
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
		String sql = "select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql, "全部");
		return YunsfsModel;
	}
	
	// 品种下拉框
	public IDropDownBean getPinzValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getPinzModel().getOptionCount() > 0) {
				setPinzValue((IDropDownBean) getPinzModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setPinzValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getPinzModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setPinzModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setPinzModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setPinzModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,mingc from pinzb order by id");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "全部"));
		setPinzModel(new IDropDownModel(list, sb));
	}
	
	// 煤矿地区下拉框
	public IDropDownBean getMeikdqValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getMeikdqModel().getOptionCount() > 0) {
				setMeikdqValue((IDropDownBean) getMeikdqModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setMeikdqValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getMeikdqModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setMeikdqModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setMeikdqModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setMeikdqModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id, mingc from gongysb where leix = 0 order by mingc");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "全部"));
		setMeikdqModel(new IDropDownModel(list, sb));
	}
	
	String pagewith = null;
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		pagewith = cycle.getRequestContext().getParameter("pw");//判断是否有特殊设置
		
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			setTreeid(null);
			setPinzValue(null);
			setPinzModel(null);
			setDiancmcModel(null);
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
				this.setRiq2(null);

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		
		if (cycle.getRequestContext().getParameter("ds") != null) {
			if (!cycle.getRequestContext().getParameter("ds").equals(
					this.getDataSource())) {// 需要清空的变量

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
				this.setRiq2(null);
			}

			this.setDataSource(cycle.getRequestContext().getParameter("ds"));
		} else {
			if (this.getDataSource().equals("")) {
				this.setDataSource("");
			}
		}
		
		blnIsBegin = true;
		getSelectData();
	}
	
	private void setDataSource(String source){
		Visit visit = (Visit) getPage().getVisit();

		if (source == null) {
			visit.setString14(REPORTNAME_HUAYBGD_ZHILB);
			return;
		}
		if (source.equals(REPORTNAME_HUAYBGD_ZHILLSB)) {
			visit.setString14(REPORTNAME_HUAYBGD_ZHILLSB);
		} else {
			visit.setString14(REPORTNAME_HUAYBGD_ZHILB);
		}
	}
	
	private String getDataSource(){
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString14();
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			riqi = DateUtil.FormatDate(stra.getTime());
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

	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			Refurbish();
			_RefurbishClick = false;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getZhiltz();
	}

	// -------------------------电厂Tree-----------------------------------------------------------------
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
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="
				+ visit.getDiancxxb_id() + " \n";
		sql += " union \n";
		sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
				+ visit.getDiancxxb_id() + " \n";
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

	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	// -------------------------电厂Tree END----------
	
	// 供应商煤矿发站Tree
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = "0";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("gongysTree_text"))
						.setValue(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	// 获得选择的供应商树节点的对应的名称
	private String[] getGys(String id) {
		String[] gys = {"全部", "-1"};
		if (id == null || "".equals(id)) {
			return gys;
		}
		JDBCcon con = new JDBCcon();
		String sql = "select mingc, jib from vwgongysmkcz where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			gys[0] = rsl.getString("mingc");
			gys[1] = rsl.getString("jib");
		}
		rsl.close();
		con.Close();
		return gys;
	}
	
	// 取得供应商参数SQL
	private String getGysParam() {
		// 供应商煤矿条件
		String gyssql = "";
		
		DefaultTree dt = ((Visit) this.getPage().getVisit()).getDefaultTree();
		int jib = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getLevel();
		String gongysb_id = "";
		long meikxxb_id = 0;
		long faz_id = 0;
		if (jib == 2) {
			gyssql += "and f.gongysb_id = " + getTreeid() + "\n";
		} else if (jib == 3) {
			gongysb_id = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getId();
			gyssql += "and f.gongysb_id = " + gongysb_id + "\n";
			meikxxb_id = Long.parseLong(getTreeid()) - Long.parseLong(gongysb_id);
			gyssql += "and f.meikxxb_id = " + meikxxb_id + "\n";
		} else if (jib == 4) {
			gongysb_id = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getParentNode().getId();
			gyssql += "and f.gongysb_id = " + gongysb_id + "\n";
			String meikNodeid = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getId();
			meikxxb_id = Long.parseLong(meikNodeid) - Long.parseLong(gongysb_id);
			gyssql += "and f.meikxxb_id = " + meikxxb_id + "\n";
			faz_id = Long.parseLong(getTreeid()) - Long.parseLong(meikNodeid);
			gyssql += "and f.faz_id = " + faz_id + "\n";
		}
				
		return gyssql;
	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();

		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));
		
		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		// 供应商树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz, "gongysTree"
				, "" + visit.getDiancxxb_id(), "forms[0]", getTreeid(), getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		if(MainGlobal.getXitxx_item("化验", "化验台账是否显示供应商树", "0", "否").equals("是")){
			tb1.addText(new ToolbarText("供货单位:"));
			tb1.addField(tf);
			tb1.addItem(tb2);
			tb1.addText(new ToolbarText("-"));
		}
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("品种:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		
		if(MainGlobal.getXitxx_item("化验", "化验台账是否显示煤矿地区下拉框", "0", "否").equals("是")){
			tb1.addText(new ToolbarText("煤矿地区:"));
			ComboBox meikdq = new ComboBox();
			meikdq.setTransform("MeikdqSelect");
			meikdq.setWidth(80);
			meikdq.setListeners("select:function(own,rec,index){Ext.getDom('MeikdqSelect').selectedIndex=index}");
			tb1.addField(meikdq);
			tb1.addText(new ToolbarText("-"));
		}
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
}