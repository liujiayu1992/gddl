package com.zhiren.dc.hesgl.report;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;


public class Qicmyfjshzb extends BasePage implements PageValidateListener {

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;
	
	private int paperStyle = Report.PAPER_A4;
	
	private String Change = "1";	//���㷽����ѡ���ֵ

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	private String riq;
	public String getRiq()
	{
		return riq;
	}
	public void setRiq(String riq){
		this.riq = riq; 
	}
	// ***************������Ϣ��******************//
	// ҳ���ж�����
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

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// �õ���λȫ��
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		String sql=	"SELECT quanc\n" +
					"\tFROM DIANCXXB\n" + 
					" WHERE ID IN (SELECT JIESDW_ID FROM JIESFAB WHERE ID IN ("+this.getChange()+"))";
	    StringBuffer danw=new StringBuffer("");
	    String title = "";
	    ResultSetList rs1=cn.getResultSetList(sql);
	    while(rs1.next()){
	    	
	    	danw.append(rs1.getString("QUANC")).append(",");
	    }
	    
	    if(danw.length()>0){
	    	
	    	danw.deleteCharAt(danw.length()-1);
	    }
	       
	    sql = 
				"SELECT TO_CHAR(min(FA.DAOHJZSJ), 'yyyy') || '��' || TO_CHAR(max(FA.DAOHJZSJ), 'MM') || '��' AS TITLE\n" +
				"\tFROM JIESFAB FA\n" + 
				" WHERE FA.ID in ("+this.getChange()+")";

	    rs1 = cn.getResultSetList(sql);
		if(rs1.next()){
			
			title = rs1.getString("TITLE");
		}
	    
	    //��jiesfab ��ȡ�õ糧��Ϣ��id
		String daohjzsj="";
		String DcSQL=
			"select daohjzsj from jiesfab where id in("+this.getChange()+")";
		rs1=cn.getResultSetList(DcSQL);
		while(rs1.next()){
			daohjzsj = rs1.getDateString("daohjzsj");
		}
		String b[]=DateUtil.FormatDate(new Date()).split("-");
		if(daohjzsj!=""){
	    b=daohjzsj.split("-");
		}
		sql=
		" (\n" + 
		"select decode(grouping(a.mingc) + grouping(a.shoukdw),\n" + 
		"              1,\n" + 
		"              'С�ϼ�',\n" + 
		"              2,\n" + 
		"              '�˷Ѻϼ�',\n" + 
		"              a.shoukdw) as chengydw,\n" + 
		"       decode(grouping(a.mingc),1,to_char(count(a.mingc)),a.mingc) as mingc,\n" + 
		"       a.hetbh,\n" + 
		"       to_number(substr(a.yunj,INSTR(a.yunj,':')+1)) as yunj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as yunjl,\n" + 
		"       a.hansdj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as shijyjl,\n" + 
		"       round_new(sum(a.hansdj)/count(a.mingc),2) as shijyj,\n" + 
		"       sum(a.ches) as ches,sum(a.jiessl) as jiessl,sum(a.buhsyf) as buhsyf, sum(a.shuik) as shuik,sum(a.hansyf) as hansyf\n" + 
		"       from\n" + 
		"(\n" + 
		"select jy.shoukdw, m.mingc,h.hetbh,jy.yunj,jy.hansdj,jy.ches,jy.jiessl,jy.buhsyf, jy.shuik, jy.hansyf, jy.diancjsmkb_id\n" + 
		"       from jiesyfb jy,meikxxb m,hetys h\n" + 
		"       where m.id = jy.meikxxb_id\n" + 
		"             and h.id = jy.hetb_id\n" + 
		"             and jy.jiesfab_id in("+this.getChange()+")\n" + 
		" union all\n" + 
		" select y.mingc,b.meikxxb_id,b.miaos,'' yunj, b.danj hansdj,0 ches,0 jiessl,b.yunf,b.shuij,b.yunf+b.shuij,0 daincjsmikb_id\n" + 
		"       from bujb b,yunsdwb y\n" + 
		"       where b.yunsdwb_id = y.id\n" + 
		"             and b.bujlx = '�˷Ѳ���'\n" + 
		"             and b.jiesfab_id in("+this.getChange()+")\n" + 
		") a\n" + 
		"group by rollup(a.shoukdw, a.mingc, a.hetbh, a.yunj, a.hansdj, a.diancjsmkb_id)\n" + 
		"HAVING NOT(GROUPING(a.diancjsmkb_id)=1 AND GROUPING(a.mingc)=0)\n" + 
		")\n" + 
		"union all\n" + 
		"select decode(b.chengydw,'�˷Ѻϼ�','�˷��ۼ�') as chengydw,b.mingc,b.hetbh,b.yunj,b.yunjl,b.hansdj,\n" + 
		"b.shijyjl,b.shijyj,b.ches,b.jiessl,b.buhsyf,b.shuik,b.hansyf from\n" + 
		"(\n" + 
		"select decode(grouping(a.mingc) + grouping(a.shoukdw),\n" + 
		"              1,\n" + 
		"              'С�ϼ�',\n" + 
		"              2,\n" + 
		"              '�˷Ѻϼ�',\n" + 
		"              a.shoukdw) as chengydw,\n" + 
		"       decode(grouping(a.mingc),1,to_char(count(a.mingc)),a.mingc) as mingc,\n" + 
		"       a.hetbh,\n" + 
		"       to_number(substr(a.yunj,INSTR(a.yunj,':')+1)) as yunj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as yunjl,\n" + 
		"       a.hansdj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as shijyjl,\n" + 
		"       round_new(sum(a.hansdj)/count(a.mingc),2) as shijyj,\n" + 
		"       sum(a.ches) as ches, sum(a.jiessl) as jiessl,sum(a.buhsyf) as buhsyf,sum(a.shuik) as shuik,sum(a.hansyf) as hansyf\n" + 
		"       from\n" + 
		"(\n" + 
		"select jy.shoukdw, m.mingc,h.hetbh,jy.yunj,jy.hansdj,jy.ches,jy.jiessl,jy.buhsyf,jy.shuik,jy.hansyf,jy.diancjsmkb_id\n" + 
		"       from jiesyfb jy,meikxxb m,hetys h,jiesfab jb\n" + 
		"       where m.id = jy.meikxxb_id\n" + 
		"             and h.id = jy.hetb_id\n" + 
		"             and jy.jiesfab_id = jb.id\n" + 
		"             and jb.daohjzsj>=to_date('"+b[0]+"-01-01','yyyy-MM-dd')\n" + 
		"             and jb.daohjzsj<=to_date('"+daohjzsj+"','yyyy-MM-dd')\n" + 
		"             --and jy.jiesfab_id in(2541053163)\n" + 
		" union all\n" + 
		"select y.mingc,b.meikxxb_id,b.miaos,'' yunj,b.danj hansdj,0 ches,0 jiessl,b.yunf,b.shuij,b.yunf+b.shuij,0 daincjsmikb_id\n" + 
		"       from bujb b,yunsdwb y\n" + 
		"       where b.yunsdwb_id = y.id\n" + 
		"             and b.bujlx = '�˷Ѳ���'\n" + 
		"             and b.jiesfab_id in("+this.getChange()+")\n" + 
		") a\n" + 
		"group by rollup(a.shoukdw, a.mingc, a.hetbh, a.yunj, a.hansdj, a.diancjsmkb_id)\n" + 
		" HAVING NOT(GROUPING(a.diancjsmkb_id)=1 AND GROUPING(a.mingc)=0)\n" + 
		") b  where b.chengydw='�˷Ѻϼ�'\n" + 
		"union all\n" + 
		"select decode(grouping(y.mingc),1,'���ۺϼ�',y.mingc) as chengydw,\n" + 
		"        b.meikxxb_id as mingc,b.miaos as hetbh,0 as yunj,0.00 as yunjl,0 as hansdj,0 as shijyjl,0 as shijyj,0 as ches,0 as jiessl,sum(b.yunf) as yunf,\n" + 
		"        sum(b.shuij) as shuij,\n" + 
		"        sum(nvl(b.yunf,0)+nvl(b.shuij,0)) as zongj\n" + 
		"        from bujb b,yunsdwb y where b.yunsdwb_id=y.id\n" + 
		"        and b.jiesfab_id in("+this.getChange()+")\n" + 
		"        and b.bujlx = 'ú�����̲���'\n" + 
		"        group by rollup (y.mingc,b.meikxxb_id,b.miaos)\n" + 
		"        having not(grouping(y.mingc)+grouping(b.miaos)=1)\n" + 
		"union all\n" + 
		"select * from\n" + 
		"(\n" + 
		"select decode(grouping(y.mingc),1,'�����ۼ�',y.mingc) as chengydw,\n" + 
		"        b.meikxxb_id as mingc,b.miaos as hetbh,0 as yunj,0.00 as yunjl,0 as hansdj,0 as shijyjl,0 as shijyj,0 as ches,0 as jiessl,sum(b.yunf) as yunf,\n" + 
		"        sum(b.shuij) as shuij,\n" + 
		"        sum(nvl(b.yunf,0)+nvl(b.shuij,0)) as zongj\n" + 
		"        from bujb b,yunsdwb y,jiesfab j\n" + 
		"        where b.yunsdwb_id=y.id\n" + 
		"        and b.jiesfab_id=j.id\n" + 
		"        and j.daohjzsj>=to_date('"+b[0]+"-01-01','yyyy-MM-dd')\n" + 
		"        and j.daohjzsj<=to_date('"+daohjzsj+"','yyyy-MM-dd')\n" + 
		"        --and b.jiesfab_id in(2541053163)\n" + 
		"        group by rollup (y.mingc,b.meikxxb_id,b.miaos)\n" + 
		"        having not(grouping(y.mingc)+grouping(b.miaos)=1)\n" + 
		" ) a where a.chengydw='�����ۼ�'\n" + 
		" union all\n" + 
		"select  '�ܺϼ�','','',\n" + 
		"         nvl(zhj.yunj,0)+nvl(ab.yunj,0),\n" + 
		"         nvl(zhj.yunjl,0)+nvl(ab.yunjl,0),\n" + 
		"         nvl(zhj.hansdj,0)+nvl(ab.hansdj,0),\n" + 
		"         nvl(zhj.shijyjl,0)+nvl(ab.shijyjl,0),\n" + 
		"         nvl(zhj.shijyj,0)+nvl(ab.shijyj,0),\n" + 
		"         nvl(zhj.ches,0)+nvl(ab.ches,0),\n" + 
		"         nvl(zhj.jiessl,0)+nvl(ab.jiessl,0),\n" + 
		"         nvl(zhj.yunf,0)+nvl(ab.buhsyf,0),\n" + 
		"         nvl(zhj.shuij,0)+nvl(ab.shuik,0),\n" + 
		"         nvl(zhj.zongj,0)+nvl(ab.hansyf,0)\n" + 
		"         from\n" + 
		"(\n" + 
		"select decode(grouping(y.mingc),1,'���ۺϼ�',y.mingc) as chengydw,\n" + 
		"        b.meikxxb_id as mingc, b.miaos as hetbh,0 as yunj,0.00 as yunjl, 0 as hansdj,0 as shijyjl, 0 as shijyj, 0 as ches, 0 as jiessl,\n" + 
		"        sum(b.yunf) as yunf,\n" + 
		"        sum(b.shuij) as shuij,\n" + 
		"        sum(nvl(b.yunf,0)+nvl(b.shuij,0)) as zongj\n" + 
		"        from bujb b,yunsdwb y\n" + 
		"        where b.yunsdwb_id=y.id\n" + 
		"        and b.jiesfab_id in("+this.getChange()+")\n" + 
		"      --  and b.bujlx = 'ú�����̲���'\n" + 
		"        group by rollup (y.mingc,b.meikxxb_id,b.miaos)\n" + 
		"        having not(grouping(y.mingc)+grouping(b.miaos)=1)\n" + 
		" ) zhj,\n" + 
		"   (\n" + 
		"select decode(grouping(a.mingc) + grouping(a.shoukdw),\n" + 
		"              1,\n" + 
		"              'С�ϼ�',\n" + 
		"              2,\n" + 
		"              '�˷Ѻϼ�',\n" + 
		"              a.shoukdw) as chengydw,\n" + 
		"       decode(grouping(a.mingc),1,to_char(count(a.mingc)),a.mingc) as mingc,\n" + 
		"       a.hetbh,\n" + 
		"       to_number(substr(a.yunj,INSTR(a.yunj,':')+1)) as yunj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as yunjl,\n" + 
		"       a.hansdj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as shijyjl,\n" + 
		"       round_new(sum(a.hansdj)/count(a.mingc),2) as shijyj,\n" + 
		"       sum(a.ches) as ches,\n" + 
		"       sum(a.jiessl) as jiessl,\n" + 
		"       sum(a.buhsyf) as buhsyf,\n" + 
		"       sum(a.shuik) as shuik,\n" + 
		"       sum(a.hansyf) as hansyf\n" + 
		"       from\n" + 
		"(\n" + 
		"     select jy.shoukdw, m.mingc, h.hetbh,jy.yunj,jy.hansdj,jy.ches,jy.jiessl,jy.buhsyf,jy.shuik,jy.hansyf,jy.diancjsmkb_id\n" + 
		"       from jiesyfb jy,meikxxb m,hetys h\n" + 
		"       where m.id = jy.meikxxb_id\n" + 
		"             and h.id = jy.hetb_id\n" + 
		"             and jy.jiesfab_id in("+this.getChange()+")\n" + 
		" union all\n" + 
		"select y.mingc, b.meikxxb_id, b.miaos,'' yunj, b.danj hansdj, 0 ches, 0 jiessl, b.yunf,b.shuij, b.yunf+b.shuij,0 daincjsmikb_id\n" + 
		"       from bujb b,yunsdwb y\n" + 
		"       where b.yunsdwb_id = y.id\n" + 
		"             and b.bujlx = '�˷Ѳ���'\n" + 
		"             and b.jiesfab_id in("+this.getChange()+")\n" + 
		") a\n" + 
		"group by rollup(a.shoukdw, a.mingc, a.hetbh, a.yunj, a.hansdj, a.diancjsmkb_id)\n" + 
		" HAVING NOT(GROUPING(a.diancjsmkb_id)=1 AND GROUPING(a.mingc)=0)\n" + 
		"   ) ab  where zhj.chengydw='���ۺϼ�' and ab.chengydw='�˷Ѻϼ�'\n" + 
		"\n" + 
		"  union all\n" + 
		"select  '���ۼ�','','',\n" + 
		"         nvl(zhj.yunj,0)+nvl(ab.yunj,0),\n" + 
		"         nvl(zhj.yunjl,0)+nvl(ab.yunjl,0),\n" + 
		"         nvl(zhj.hansdj,0)+nvl(ab.hansdj,0),\n" + 
		"         nvl(zhj.shijyjl,0)+nvl(ab.shijyjl,0),\n" + 
		"         nvl(zhj.shijyj,0)+nvl(ab.shijyj,0),\n" + 
		"         nvl(zhj.ches,0)+nvl(ab.ches,0),\n" + 
		"         nvl(zhj.jiessl,0)+nvl(ab.jiessl,0),\n" + 
		"         nvl(zhj.yunf,0)+nvl(ab.buhsyf,0),\n" + 
		"         nvl(zhj.shuij,0)+nvl(ab.shuik,0),\n" + 
		"         nvl(zhj.zongj,0)+nvl(ab.hansyf,0)\n" + 
		"         from\n" + 
		"(\n" + 
		"select decode(grouping(y.mingc),1,'�����ۼ�',y.mingc) as chengydw,\n" + 
		"        b.meikxxb_id as mingc, b.miaos as hetbh, 0 as yunj,0.00 as yunjl, 0 as hansdj, 0 as shijyjl,0 as shijyj,0 as ches,0 as jiessl,\n" + 
		"        sum(b.yunf) as yunf,\n" + 
		"        sum(b.shuij) as shuij,\n" + 
		"        sum(nvl(b.yunf,0)+nvl(b.shuij,0)) as zongj\n" + 
		"        from bujb b,yunsdwb y,jiesfab j\n" + 
		"        where b.yunsdwb_id=y.id\n" + 
		"        and b.jiesfab_id=j.id\n" + 
		"        and j.daohjzsj>=to_date('"+b[0]+"-01-01','yyyy-MM-dd')\n" + 
		"        and j.daohjzsj<=to_date('"+daohjzsj+"','yyyy-MM-dd')\n" + 
		"        --and b.jiesfab_id in(2541053163)\n" + 
		"        group by rollup (y.mingc,b.meikxxb_id,b.miaos)\n" + 
		"        having not(grouping(y.mingc)+grouping(b.miaos)=1)\n" + 
		" ) zhj,\n" + 
		"    (\n" + 
		"select decode(b.chengydw,'�˷Ѻϼ�','�˷��ۼ�') as chengydw,b.mingc,b.hetbh,b.yunj,b.yunjl,b.hansdj,\n" + 
		"b.shijyjl,b.shijyj,b.ches,b.jiessl,b.buhsyf,b.shuik,b.hansyf from\n" + 
		"(\n" + 
		"select decode(grouping(a.mingc) + grouping(a.shoukdw),\n" + 
		"              1,\n" + 
		"              'С�ϼ�',\n" + 
		"              2,\n" + 
		"              '�˷Ѻϼ�',\n" + 
		"              a.shoukdw) as chengydw,\n" + 
		"       decode(grouping(a.mingc),1,to_char(count(a.mingc)),a.mingc) as mingc,\n" + 
		"       a.hetbh,\n" + 
		"       to_number(substr(a.yunj,INSTR(a.yunj,':')+1)) as yunj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as yunjl,\n" + 
		"       a.hansdj,\n" + 
		"       decode(to_number(substr(a.yunj,INSTR(a.yunj,':')+1)), 0, 0, a.hansdj / to_number(substr(a.yunj,INSTR(a.yunj,':')+1))) as shijyjl,\n" + 
		"       round_new(sum(a.hansdj)/count(a.mingc),2) as shijyj,\n" + 
		"       sum(a.ches) as ches,\n" + 
		"       sum(a.jiessl) as jiessl,\n" + 
		"       sum(a.buhsyf) as buhsyf,\n" + 
		"       sum(a.shuik) as shuik,\n" + 
		"       sum(a.hansyf) as hansyf\n" + 
		"       from\n" + 
		"(\n" + 
		"select jy.shoukdw,m.mingc,h.hetbh,jy.yunj,jy.hansdj,jy.ches,jy.jiessl,jy.buhsyf,jy.shuik,jy.hansyf,jy.diancjsmkb_id\n" + 
		"       from jiesyfb jy,meikxxb m,hetys h,jiesfab jb\n" + 
		"       where m.id = jy.meikxxb_id\n" + 
		"             and h.id = jy.hetb_id\n" + 
		"             and jy.jiesfab_id = jb.id\n" + 
		"             and jb.daohjzsj>=to_date('"+b[0]+"-01-01','yyyy-MM-dd')\n" + 
		"             and jb.daohjzsj<=to_date('"+daohjzsj+"','yyyy-MM-dd')\n" + 
		"             --and jy.jiesfab_id in(2541053163)\n" + 
		" union all\n" + 
		" select y.mingc,b.meikxxb_id, b.miaos,'' yunj,b.danj hansdj,0 ches,0 jiessl,b.yunf, b.shuij, b.yunf+b.shuij, 0 daincjsmikb_id\n" + 
		"       from bujb b,yunsdwb y\n" + 
		"       where b.yunsdwb_id = y.id\n" + 
		"             and b.bujlx = '�˷Ѳ���'\n" + 
		"             and b.jiesfab_id in("+this.getChange()+")\n" + 
		") a\n" + 
		"group by rollup(a.shoukdw, a.mingc, a.hetbh, a.yunj, a.hansdj, a.diancjsmkb_id)\n" + 
		" HAVING NOT(GROUPING(a.diancjsmkb_id)=1 AND GROUPING(a.mingc)=0)\n" + 
		") b  where b.chengydw='�˷Ѻϼ�'\n" + 
		"   ) ab  where zhj.chengydw='�����ۼ�' and ab.chengydw='�˷��ۼ�'\n" + 
		"";
		ResultSetList rs = cn.getResultSetList(sql);
		Report rt = new Report();
		String ArrHeader[][] = new String[3][13];
		ArrHeader[0]=new String[] {"���˵�λ","ú������","��ֵͬ","��ֵͬ","��ֵͬ","��ֵͬ",
				"ʵ���˼���","ʵ���˼�","����","������","�����˷�","�����˷�","�����˷�"};
		ArrHeader[1]=new String[] {"���˵�λ","ú������","��ͬ���","�˾�","�˼���","�˼�",
				"ʵ���˼���","ʵ���˼�","����","������","�˷�","˰��","С��"};
		ArrHeader[2]=new String[] {"���˵�λ","ú������","��ͬ���","����","Ԫ/��.����","Ԫ/��","Ԫ/��KM",
				"Ԫ/��","��","��","Ԫ","Ԫ","Ԫ"};
		int ArrWidth[] = new int[]{90,90,100,50,50,50,50,50,50,80,80,80,80};
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		rt.setTitle("����ú�˷ѽ�����ܱ�", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ��"+danw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 2, "����£�"+title, Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 3, 0, 3));
		
        //	��������
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 3, 2);
		rt.body.merge(2, 3, 3, 3);
		rt.body.merge(1, 8, 2, 8);
		rt.body.merge(1, 9, 2, 9);
		rt.body.merge(1, 10, 2, 10);
		rt.body.merge(1, 11, 2, 11);
		rt.body.merge(1, 12, 2, 12);
		rt.body.merge(1, 13, 2, 13);
		rt.body.merge(1, 7, 2, 7);
		rt.body.merge(1, 11, 1, 13);
		rt.body.merge(1, 3, 1, 7);
		int m=0;
		int n=0;
		for(int i=1;i<rt.body.getRows();i++){
			 if(rt.body.getCellValue(i, 3).equals("")){
				rt.body.merge(i, 3, i, 7);
				rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
			 }
		}
		for(int i=1;i<=rt.body.getRows();i++){

			if(rt.body.getCellValue(i, 1).equals("�˷��ۼ�")){
				m=i;
				rt.body.setCellValue(i, 2, "�˷��ۼ�");
				rt.body.setCellValue(i, 3, "�˷��ۼ�");
				rt.body.setCellValue(i, 4, "�˷��ۼ�");
				rt.body.setCellValue(i, 5, "�˷��ۼ�");
				rt.body.setCellValue(i, 6, "�˷��ۼ�");
				rt.body.setCellValue(i, 7, "�˷��ۼ�");
				rt.body.merge(i, 1, i, 7);
				for(int j=1;j<=7;j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				}
			}
			if(rt.body.getCellValue(i, 1).equals("�˷Ѻϼ�")){
				rt.body.setCellValue(i, 2, "�˷Ѻϼ�");
				rt.body.setCellValue(i, 3, "�˷Ѻϼ�");
				rt.body.setCellValue(i, 4, "�˷Ѻϼ�");
				rt.body.setCellValue(i, 5, "�˷Ѻϼ�");
				rt.body.setCellValue(i, 6, "�˷Ѻϼ�");
				rt.body.setCellValue(i, 7, "�˷Ѻϼ�");
				rt.body.merge(i, 1, i, 7);
				for(int j=1;j<=7;j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				}
			}		
			if(rt.body.getCellValue(i, 1).equals("���ۺϼ�")){
				n=i;
				rt.body.setCellValue(i, 2, "���ۺϼ�");
				rt.body.setCellValue(i, 3, "���ۺϼ�");
				rt.body.setCellValue(i, 4, "���ۺϼ�");
				rt.body.setCellValue(i, 5, "���ۺϼ�");
				rt.body.setCellValue(i, 6, "���ۺϼ�");
				rt.body.setCellValue(i, 7, "���ۺϼ�");
				rt.body.merge(i, 1, i, 7);
				for(int j=1;j<=7;j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				}
			}
			if(rt.body.getCellValue(i, 1).equals("�����ۼ�")){
				rt.body.setCellValue(i, 2, "�����ۼ�");
				rt.body.setCellValue(i, 3, "�����ۼ�");
				rt.body.setCellValue(i, 4, "�����ۼ�");
				rt.body.setCellValue(i, 5, "�����ۼ�");
				rt.body.setCellValue(i, 6, "�����ۼ�");
				rt.body.setCellValue(i, 7, "�����ۼ�");
				rt.body.merge(i, 1, i,7 );
				for(int j=1;j<=7;j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				}
			}
			if(rt.body.getCellValue(i, 1).equals("�ܺϼ�")){
			   rt.body.setCellValue(i, 2, "�ܺϼ�");
			   rt.body.setCellValue(i, 3, "�ܺϼ�");
			   rt.body.setCellValue(i, 4, "�ܺϼ�");
			   rt.body.setCellValue(i, 5, "�ܺϼ�");
			   rt.body.setCellValue(i, 6, "�ܺϼ�");
			   rt.body.setCellValue(i, 7, "�ܺϼ�");
			   rt.body.merge(i, 1, i,7);
			for(int j=1;j<=7;j++){
				rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
			   }
		   }
		   if(rt.body.getCellValue(i, 1).equals("���ۼ�")){
			  rt.body.setCellValue(i, 2, "���ۼ�");
		      rt.body.setCellValue(i, 3, "���ۼ�");
			  rt.body.setCellValue(i, 4, "���ۼ�");
		      rt.body.setCellValue(i, 5, "���ۼ�");
		      rt.body.setCellValue(i, 6, "���ۼ�");
			  rt.body.setCellValue(i, 7, "���ۼ�");
			  rt.body.merge(i, 1, i,7);
			for(int j=1;j<=7;j++){
					rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				}
			}
		}
		for(int i=m+1;i<=n-1;i++){
			rt.body.setCellValue(i, 4, rt.body.getCellValue(i, 3));
			rt.body.setCellValue(i, 5, rt.body.getCellValue(i, 3));
			rt.body.setCellValue(i, 6, rt.body.getCellValue(i, 3));
			rt.body.setCellValue(i, 7, rt.body.getCellValue(i, 3));
			rt.body.merge(i, 3, i, 7);
			for(int j=3;j<=7;j++){
				rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
			}
		}
		rt.body.setPageRows(40);
		rt.getPages();
		rt.createDefautlFooter(ArrWidth);	
		rt.setDefautlFooter(1, 2, "��˾�쵼:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "����:" , Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "ȼ�ϲ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 1, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 1, "�Ʊ�:", Table.ALIGN_RIGHT);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private void ArrWidth(int i) {
		// TODO �Զ����ɷ������

	}

	
	

	private boolean _SbClick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbClick = true;
	}

	private boolean _SqxgClick = false;

	public void SqxgButton(IRequestCycle cycle) {
		_SqxgClick = true;
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		
		if (_QueryClick) {
			_QueryClick = false;
		    getPrintTable();		
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		//setTreeid(visit.getDiancxxb_id() + "");

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setActivePageName(getPageName().toString());
			init();
		}
		getSelectData();
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
			getIJiesfaModels();
		}
	}

	private void init() {
		
		setJiesfaValue(null);	//2
		setIJiesfaModel(null);	//2
		getIJiesfaModels();	//2
		//visit.setDefaultTree(null);
		((Visit) getPage().getVisit()).setboolean1(false);
		((Visit) getPage().getVisit()).setString3("");	//�糧��
		setDiancmcModel(null);
		paperStyle();
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("riq", "Form0");
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���㷽��:"));
		LovComboBox jiesfa = new LovComboBox();
		jiesfa.setId("jiesfa");
//		ComboBox jiesfa = new ComboBox();
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setWidth(160);
		jiesfa.setForceSelection(false);
		jiesfa.setListeners("select:function(e){document.getElementById('CHANGE').value = e.getValue();}");
		tb1.addField(jiesfa);
		tb1.addText(new ToolbarText("-"));
	
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
			
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

//	���㷽������
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, "1"));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean2().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
				"SELECT ID, BIANM\n" +
				"\tFROM JIESFAB\n" + 
				" WHERE SHIFJS = 1\n" + 
				"\t AND JIESLX = "+Locale.guotyf_feiylbb_id+"\n" + 
				"\t and DAOHJZSJ = to_date('"+this.getRiq()+"','yyyy-MM-dd')\n"+
//				"\t AND JIESDW_ID = "+this.getTreeid()+"\n" + 
				" ORDER BY BIANM desc";
			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	���㷽������_end

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).equals(treeid)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}
	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='ֽ������' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
}