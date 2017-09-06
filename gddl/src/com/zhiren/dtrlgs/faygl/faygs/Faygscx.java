package com.zhiren.dtrlgs.faygl.faygs;

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
import com.zhiren.dtrlgs.pubclass.FmisInterface;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Faygscx extends BasePage implements PageValidateListener {

	public boolean getRaw() {
		return true;
	}

//	private String userName = "";
//
//	public void setUserName(String value) {
//		userName = ((Visit) getPage().getVisit()).getRenymc();
//	}
//
//	public String getUserName() {
//		return userName;
//	}

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
		_msg =MainGlobal.getExtMessageBox( _value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	private boolean _FmisClick = false;

	public void FmisButton(IRequestCycle cycle) {
		_FmisClick = true;
	}
	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}


	public String getPrintTable() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
	/*	String sSql="select\n"+
        "gmingc,\n"+
      " dmingc,\n"+
     "  fahdw,\n"+
     "  chec,\n"+
      " mingc,\n"+
     "  jingz,\n"+
    "   rez,\n"+
    "   heth,\n"+
     "  hetjg,\n"+
     "  hetbz,\n"+
     "  hetzk,\n"+
     "  meij,\n"+
     "  meis,\n"+
      " riq,\n"+
    "   yunf,\n"+
     "  zaf,\n"+
     "  fazzf,\n"+
    "   yunfs,\n"+
    "   round_new((meij + yunf + zaf) * jingz, 2) as buhsmk,\n"+
    "  round_new(round_new((meij + yunf + zaf) * jingz, 2) * 0.17, 2) as shuik,\n"+
    "   round_new(round_new((meij + yunf + zaf) * jingz, 2) * 1.17, 2) as hansmk\n"+
 " from (select decode(grouping(t.fahd), 1, '总计', t.fahd) as gmingc,\n"+
       "        decode(grouping(t.gongys) + grouping(t.fahd),\n"+
        "              1,\n"+
        "              '合计',\n"+
         "             t.gongys) as dmingc,\n"+
          "     decode(grouping(t.gongys) + grouping(t.fahd) +\n"+
          "            grouping(t.fahdw),\n"+
           "           1,\n"+
          "            '合计',\n"+
          "            t.fahdw) as fahdw,\n"+
         "      t.chec,\n"+
         "      t.mingc,\n"+
         "      sum(t.jingz) as jingz,\n"+
         "      decode(sum(t.jingz),\n"+
         "             0,\n"+
         "             0,\n"+
         "             round_new(sum(t.rez*round_new(t.jingz,0)) / sum(t.jingz), 0)) as rez,\n"+
         "      t.heth,\n"+
         "      decode(sum(t.jingz),\n"+
        "              0,\n"+
        "              0,\n"+
        "              round_new(sum(t.hetjg*round_new(t.jingz,0)) / sum(t.jingz), 2)) as hetjg,\n"+
         "      t.hetbz,\n"+
         "      decode(sum(t.jingz),\n"+
         "             0,\n"+
         "             0,\n"+
         "             round_new(sum(t.hetzk*round_new(t.jingz,0)) / sum(t.jingz), 2)) as hetzk,\n"+
         "      decode(sum(t.jingz),\n"+
         "             0,\n"+
         "             0,\n"+
        "              round_new(sum(t.meij*round_new(t.jingz,0)) / sum(t.jingz), 7)) as meij,\n"+
        "       decode(sum(t.jingz),\n"+
         "             0,\n"+
         "             0,\n"+
        "              round_new(sum(t.meis*round_new(t.jingz,0)) / sum(t.jingz), 2)) as meis,\n"+
        "       t.riq,\n"+
        "       decode(sum(t.jingz),\n"+
        "              0,\n"+
        "              0,\n"+
       "               round_new(sum(t.yunf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as yunf,\n"+
       "        decode(sum(t.jingz),\n"+
       "               0,\n"+
       "               0,\n"+
       "               round_new(sum(t.zaf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as zaf,\n"+
       "        decode(sum(t.jingz),\n"+
       "               0,\n"+
       "               0,\n"+
       "               round_new(sum(t.fazzf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as fazzf,\n"+
       "        decode(sum(t.jingz),\n"+
        "              0,\n"+
        "              0,\n"+
        "              round_new(sum(t.yunfs*round_new(t.jingz,0)) / sum(t.jingz), 2)) as yunfs\n"+
        
       "   from (select d.mingc as fahd,\n"+
         "              g.mingc as gongys,\n"+
         "              s.mingc as fahdw,\n"+
         "              f2.chec as chec,\n"+
         "              lc.mingc,\n"+
         "              f2.meil jingz,\n"+
          "             f.rez as rez,\n"+
        "               f.heth as heth,\n"+
         "              f.hetjg as hetjg,\n"+
         "              f.hetbz as hetbz,\n"+
        "               f.hetzk as hetzk,\n"+
          "             f.meij as meij,\n"+
         "              f.meis as meis,\n"+
          "             to_char(f.riq, 'yyyy-mm-dd') as riq,\n"+
          "             f.yunf as yunf,\n"+
          "             f.zaf as zaf,\n"+
          "             f.fazzf as fazzf,\n"+
          "             f.yunfs as yunfs\n"+
         "         from faygslsb f,\n"+
         "              fayslb f2,\n"+
         "              (select max(id) as id\n"+
         "                 from faygslsb\n"+
//         "                where leix <> 4\n"+
         "                group by (fayslb_id)) f3,\n"+
         "              diancxxb dc,\n"+
          "             (select id, mingc from diancxxb where cangkb_id <> 1) d,\n"+
          "             (select id, mingc from gongysb) g,\n"+
          "             (select id, mingc from diancxxb where cangkb_id = 1) s,\n"+
          "             luncxxb lc\n"+
          "       where f3.id = f.id\n"+
          "         and f2.diancxxb_id = dc.id\n"+
          "         and lc.id(+) = f2.luncxxb_id\n"+
          "         and f2.diancxxb_id = d.id\n"+
          "         and g.id = f2.gongysb_id\n"+
          "         and s.id = f2.shr_diancxxb_id\n"+
          "         and f2.id = f.fayslb_id\n"+
          "         and f.leix <> 4\n"+
          "         and f2.FAHRQ < to_date('"+getRiq2()+"', 'yyyy-mm-dd') + 1\n"+
          str +
          "      \n order by f.id desc) t\n"+
        " group by rollup(t.fahd,\n"+
         "                t.gongys,\n"+
          "               (t.fahdw, t.chec, t.mingc, t.heth, t.hetbz, riq))\n"+
        " order by t.fahd desc, grouping(t.fahd) desc, grouping(t.gongys) desc, grouping(t.fahdw) desc)\n";*/
		
		/* sSql ="select d.mingc as fahd,g.mingc as gongys,s.mingc as fahdw,f2.chec as chec, lc.mingc,f2.meil,f.rez as rez, f.heth as heth,\n" +
			"f.hetjg as  hetjg,f.hetbz as hetbz, f.hetzk as hetzk, f.meij as meij, f.meis as meis,\n" + 
			"to_char(f.riq,'yyyy-mm-dd') as riq, f.yunf as yunf, f.zaf as zaf,f.fazzf as fazzf, f.yunfs as yunfs\n" + 
			"from faygslsb f, fayslb f2,(select max(id) as id from faygslsb where leix<>4 group by (fayslb_id)) f3, diancxxb dc, \n" + 
			" (select id,mingc from diancxxb where cangkb_id<>1) d, (select id,mingc from gongysb) g," +
			" (select id,mingc from diancxxb where cangkb_id=1) s,luncxxb lc \n"+
			" where f3.id=f.id and f2.diancxxb_id=dc.id  and lc.id(+)=f2.luncxxb_id\n" + 
			" and f2.diancxxb_id=d.id and g.id=f2.gongysb_id and s.id=f2.shr_diancxxb_id \n"+
			"and f2.id=f.fayslb_id \n"+
			"and f.leix<>4 \n"+
//			"and f2.FAHRQ>=to_date('"+getRiqi()+"','yyyy-mm-dd') \n"+
			"and f2.FAHRQ<to_date('"+getRiq2()+"','yyyy-mm-dd')+1 \n"+
			str + " order by f.id desc";*/
		


	String sSql=	"  select\n"+
		"gmingc,\n"+
		" dmingc,\n"+
		"  fahdw,\n"+
		"  chec,\n"+
		" mingc,\n"+
		" duow,\n"+
		"  jingz,\n"+
		"   rez,\n"+
		"   heth,\n"+
		"  hetjg,\n"+
		"  hetbz,\n"+
		"  hetzk,\n"+
		"  meij,\n"+
		"  meis,\n"+
		" riq,\n"+
		"   yunf,\n"+
		"  zaf,\n"+
		"  fazzf,\n"+
		"   yunfs,\n"+
		"   round_new((meij + yunf + zaf) * jingz, 2) as buhsmk,\n"+
		 " round_new(round_new((meij + yunf + zaf) * jingz, 2) * 0.17, 2) as shuik,\n"+
		 "  round_new(round_new((meij + yunf + zaf) * jingz, 2) * 1.17, 2) as hansmk\n"+
		" from (select decode(grouping(t.fahd), 1, '总计', t.fahd) as gmingc,\n"+
		"        decode(grouping(t.gongys) + grouping(t.fahd),\n"+
		"             1,\n"+
		"              '合计',\n"+
		"             t.gongys) as dmingc,\n"+
		 "    decode(grouping(t.gongys) + grouping(t.fahd) +\n"+
		"            grouping(t.fahdw),\n"+
		"           1,\n"+
		"            '合计',\n"+
		"            t.fahdw) as fahdw,\n"+
		"      t.chec,\n"+
		"      t.mingc,\n"+
		"      sum(t.jingz) as jingz,\n"+
		"      decode(sum(t.jingz),\n"+
		"             0,\n"+
		"             0,\n"+
		"             round_new(sum(t.rez*round_new(t.jingz,0)) / sum(t.jingz), 0)) as rez,\n"+
		"      t.heth,\n"+
		"      decode(sum(t.jingz),\n"+
		 "             0,\n"+
		"              0,\n"+
		"              round_new(sum(t.hetjg*round_new(t.jingz,0)) / sum(t.jingz), 2)) as hetjg,\n"+
		"      t.hetbz,\n"+
		"      decode(sum(t.jingz),\n"+
		"             0,\n"+
		"             0,\n"+
		"             round_new(sum(t.hetzk*round_new(t.jingz,0)) / sum(t.jingz), 2)) as hetzk,\n"+
		"      decode(sum(t.jingz),\n"+
		"             0,\n"+
		"             0,\n"+
		"              round_new(sum(t.meij*round_new(t.jingz,0)) / sum(t.jingz), 7)) as meij,\n"+
		"       decode(sum(t.jingz),\n"+
		"             0,\n"+
		"             0,\n"+
		"              round_new(sum(t.meis*round_new(t.jingz,0)) / sum(t.jingz), 2)) as meis,\n"+
		"       t.riq,\n"+
		"       decode(sum(t.jingz),\n"+
		"              0,\n"+
		"              0,\n"+
		"               round_new(sum(t.yunf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as yunf,\n"+
		"        decode(sum(t.jingz),\n"+
		"               0,\n"+
		"               0,\n"+
		"               round_new(sum(t.zaf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as zaf,\n"+
		"        decode(sum(t.jingz),\n"+
		"               0,\n"+
		"               0,\n"+
		"               round_new(sum(t.fazzf*round_new(t.jingz,0)) / sum(t.jingz), 2)) as fazzf,\n"+
		"        decode(sum(t.jingz),\n"+
		"              0,\n"+
		"              0,\n"+
		"              round_new(sum(t.yunfs*round_new(t.jingz,0)) / sum(t.jingz), 2)) as yunfs,\n"+
		"              t.duow\n"+
		"   from (\n"+
		   
		   
		"   select    f.fhdmingc as fahd,\n"+
		"              f.gymingc as gongys,\n"+
		"              f.famingc as fahdw,\n"+
		"              f.chec as chec,\n"+
		"              f.mingc,\n"+
		"              f.meil jingz,\n"+
		"             f.rez as rez,\n"+
		"               f.heth as heth,\n"+
		 "             f.hetjg as hetjg,\n"+
		"              f.hetbz as hetbz,\n"+
		"               f.hetzk as hetzk,\n"+
		"             f.meij as meij,\n"+
		"              f.meis as meis,\n"+
		 "            to_char(f.riq, 'yyyy-mm-dd') as riq,\n"+
		"             f.yunf as yunf,\n"+
		"             f.zaf as zaf,\n"+
		"             f.fazzf as fazzf,\n"+
		"             f.yunfs as yunfs,\n"+
		"             substr(dw.mingc,0,1)||'-'||mei.mingc duow\n"+
		"               from (\n"+
		"    select f2.id,f.yunfs,f.fazzf,f.zaf,f.yunf,f.riq,f.meis,f.meij,f.hetzk,f.hetbz,f.hetjg,f.heth,f.rez, substr(f2.id,4)||f2.chec as chec, s.quanc as shr, 'F'||substr(g.bianm,0,6) as bianm, f2.yewlxb_id,\n"+
		"                  nvl(tmp.fahb_id,0) as fahb_id,f2.neibxs, f2.xiaosjsb_id, \n"+
		"                  case when nvl(tmp.fayslb_id,0)>0 and nvl(tmp.zhilb_id,0)>0 then 1 else 0 end as daohzt \n"+
		"                  ,f2.meil,lc.mingc,s.mingc famingc,g.mingc gymingc,d.mingc fhdmingc\n"+
		"            from faygslsb f, fayslb f2, fahbtmp tmp,  diancxxb dc, luncxxb lc,\n"+
		"                 (select max(id) as id from faygslsb group by (fayslb_id)) f3,\n"+
		"                 (select id, quanc,mingc,fuid from diancxxb where cangkb_id <> 1) d,\n"+
		"                 (select id, quanc, bianm,mingc from gongysb) g,\n"+
		"                 (select id, quanc,mingc from diancxxb where cangkb_id = 1) s\n"+
		"           where f3.id = f.id and tmp.fayslb_id(+)=f2.id and f2.diancxxb_id = dc.id \n"+
		"              and lc.id(+) = f2.luncxxb_id and f2.diancxxb_id = d.id and g.id = f2.gongysb_id and s.id = f2.keh_diancxxb_id\n"+
		"              and f2.id = f.fayslb_id and f2.fahrq<to_date('"+getRiq2()+"','yyyy-mm-dd')\n"+
		"				      and (nvl(tmp.fayslb_id,0)=0 or (nvl(tmp.fayslb_id,0)>0 and f.fmisjksj is null))\n"+
		str+
		"	          ) f,\n"+
		"         ( select fahb.id as fahbid,nvl(meij,0)+nvl(yunf,0) as chengb from shoumgslsb gs,fahb  where gs.id in\n"+
		"          (select max(id) as smid from shoumgslsb group by (fahbid)) and fahbid=fahb.id and fahb.yewlxb_id=3 ) cb,\n"+
		"         diancjsmkb dj,duowgsb dw, meicb mei, qumxxb qu, duowkcb  kc, yewlxb lx\n"+
		"   where dj.id(+) = f.xiaosjsb_id and qu.zhuangcb_id = f.id and qu.meicb_id = mei.id\n"+
		"     and mei.duowgsb_id = dw.id(+) and qu.id=kc.duiqm_id(+) and f.yewlxb_id=lx.id and f.fahb_id=cb.fahbid(+)\n"+
		" ) t\n"+
		" group by rollup(t.fahd,\n"+
		"                t.gongys,\n"+
		"               (t.fahdw, t.chec, t.mingc, t.heth, t.hetbz, riq,t.duow))\n"+
		" order by t.fahd desc, grouping(t.fahd) desc, grouping(t.gongys) desc, grouping(t.fahdw) desc)\n";
		
		ResultSetList rs = con.getResultSetList(sSql);
		String[][] ArrHeader = new String[1][21];;
		String[] strFormat = null;
		int[] ArrWidth = null;
			ArrHeader[0] = new String[] {"发货地点","供应商","收货单位","车次/航次","船名","垛位","煤量","估收热值<br>(大卡)","合同号",	"合同价格","合同标准","合同增扣",
					"煤价<br>（不含税）","煤价税","日期","运费","杂费<br>(计税扣除)","发站费","运费税","不含税煤款","税款","含税煤款"};
			ArrWidth = new int[] {60,80,70,60,50,80, 50,50, 100,60, 90,60,70, 50, 70, 50, 40, 40, 40,70,70,70};
			rt.setTitle("发运估收查询", ArrWidth);
		
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(1, 5, "制表单位:"
				+ v.getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 5, "到货日期:"
//				+ getRiqi() + "至"
				+getRiq2(),
				Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		strFormat = new String[] {"","","","","","", "0", "","0.0000","","0.00000","0.00","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};

		rt.setBody(new Table(rs, 1, 0, 9));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.setColFormat(strFormat);
		rt.body.ShowZero=true;
		for (int i = 1; i <= 18; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "制表人:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
//		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
//				Local.RptTitle_zhuangchytjcx, "" + BAOBPZB_GUANJZ);
		return rt.getAllPagesHtml();
		}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setTreeid(null);
			visit.setString1("");
		}
		getSelectData();

	}

	// 绑定日期
	/*boolean riqichange = false;

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

	}*/

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
			getPrintTable();
			_RefurbishClick = false;
		}
	
         if(_FmisClick){
        	 _FmisClick=false;
        	 this.fmis();
        	 getPrintTable();
         }
	}


	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();

	
		tb1.addText(new ToolbarText("发货日期:"));
//		DateField df = new DateField();
//		df.setReadOnly(true);
//		df.setValue(this.getRiqi());
//		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
//		df.setId("riqi");
//		tb1.addField(df);
//		tb1.addText(new ToolbarText("-"));
//
//		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
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
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		String scr=
			"function(){ Ext.MessageBox.confirm('警告', '确定传送fmis接口吗？', function(btn) { if(btn=='yes'){document.getElementById('FmisButton').click();\n" +
			"Ext.MessageBox.show(\n" + 
			"    {msg:'正在处理数据,请稍后...',\n" + 
			"     progressText:'处理中...',\n" + 
			"     width:300,\n" + 
			"     wait:true,\n" + 
			"     waitConfig: {interval:200},\n" + 
			"     icon:Ext.MessageBox.INFO\n" + 
			"     }\n" + 
			");}})" +
//			"Ext.MessageBox.alert('提示信息','成功传入FMIS接口');"+
			"}";
		ToolbarButton tbb2 = new ToolbarButton(null,"传FMIS接口",scr);
		
		tb1.addItem(tbb2);
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
	
	// Page方法
	protected void initialize() {
		_pageLink = "";
		setMsg("");
	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}

//	接口
	private void fmis() {
		FmisInterface fi = new FmisInterface();
		
		String msg = fi.ZgFmis(DateUtil.getDate(getRiq2()), FmisInterface.XIAOS_DATA);
		
		setMsg(msg);
	}

}
