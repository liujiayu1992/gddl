package com.zhiren.dc.chengbgl.baob;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Chengbrb extends BasePage {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id()+" order by id");
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
//	获取相关的SQL
	/*
	 * 修改来煤数量统计为fahb.laimsl 修约来煤量与热值
	 * 修改时间：2008-12-05
	 * 修改人：王磊
	 */
	public StringBuffer getBaseSql() {
		Visit v = (Visit) this.getPage().getVisit();
		long diancxxbid=0;
		if(v.isFencb()){
			diancxxbid = getChangbValue().getId();
		}else{
			diancxxbid = v.getDiancxxb_id();
		}
		String CurrentDate = DateUtil.FormatOracleDate(getRiq());
		String FirstDateOfMonth = DateUtil.FormatOracleDate(DateUtil.getFDOfMonth(getRiq()));
		StringBuffer sb = new StringBuffer();
		sb.append("select zjb.*,")
		.append("decode(qnet_ar,0,0,round_new((meij+yunj+jiaohqzf+zaf+daozzf+qitfy)*7000/qnet_ar,2)) as bmdj,\n")
		.append("decode(qnet_ar,0,0,round_new((meij+yunj+jiaohqzf+zaf+daozzf+qitfy-meijs-yunjs)*7000/qnet_ar,2)) as buhsbmdj	\n")
		.append("from ")
		.append("(select decode(grouping(j.mingc),1,'总计',j.mingc) jihkj,\n")
		.append("decode(grouping(g.mingc)+grouping(j.mingc),1,'合计',g.mingc) gongysmc,\n")
		.append("decode(grouping(p.mingc)+grouping(g.mingc)+grouping(j.mingc),1,'小计',p.mingc) pinz,\n")
		.append("y.mingc, nvl(c.fenx,'') fenx, sum(jingz) jingz,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*meij)/sum(jingz)),2) meij,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*meijs)/sum(jingz)),2) meijs,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*yunj)/sum(jingz)),2) yunj,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*yunjs)/sum(jingz)),2) yunjs,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*jiaohqzf)/sum(jingz)),2) jiaohqzf,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*zaf)/sum(jingz)),2) zaf,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*daozzf)/sum(jingz)),2) daozzf,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*qitfy)/sum(jingz)),2) qitfy,\n")
		.append("round_new(decode(sum(jingz),0,0,sum(jingz*Qnet_ar)/sum(jingz)),"+v.getFarldec()+") Qnet_ar\n")
		.append("from \n")
		.append("(select a.gongysb_id, a.jihkjb_id,a.pinzb_id,a.yunsfsb_id,'本日' fenx, nvl(b.jingz,0) jingz,\n")
		.append("nvl(b.meij,0) meij,nvl(b.meijs,0) meijs,nvl(b.yunj,0) yunj,\n")
		.append("nvl(b.yunjs,0) yunjs,nvl(b.jiaohqzf,0) jiaohqzf, nvl(b.zaf,0) zaf,\n")
		.append("nvl(b.daozzf,0) daozzf,nvl(b.qitfy,0) qitfy,nvl(b.Qnet_ar,0) Qnet_ar from \n")
		.append("(select distinct f.gongysb_id, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id\n")
		.append("        from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfMonth)
		.append(" and f.daohrq < ").append(CurrentDate).append("+1 and f.diancxxb_id=").append(diancxxbid).append(") a,\n")
		.append("(select f.gongysb_id,f.jihkjb_id,f.pinzb_id,f.yunsfsb_id,sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq =").append(CurrentDate)
		.append(" and f.diancxxb_id=").append(diancxxbid).append("\n")
		.append("      group by f.gongysb_id,f.jihkjb_id,f.pinzb_id,f.yunsfsb_id) b\n")
		.append("where a.gongysb_id = b.gongysb_id(+) and a.jihkjb_id = b.jihkjb_id(+)\n")
		.append("and a.pinzb_id = b.pinzb_id(+) and a.yunsfsb_id = b.yunsfsb_id(+)\n")
		.append("union\n")
		.append("select f.gongysb_id,f.jihkjb_id,f.pinzb_id,f.yunsfsb_id,'累计' fenx,sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfMonth)
		.append(" and f.daohrq <").append(CurrentDate).append("+1 and f.diancxxb_id=").append(diancxxbid).append("\n")
		.append("      group by f.gongysb_id,f.jihkjb_id,f.pinzb_id,f.yunsfsb_id) c,\n")
		.append("gongysb g, jihkjb j, pinzb p, yunsfsb y\n")
		.append("where c.gongysb_id = g.id and c.jihkjb_id = j.id\n")
		.append("and c.pinzb_id = p.id and c.yunsfsb_id = y.id\n")
		.append("group by rollup (c.fenx,j.mingc,g.mingc,p.mingc,y.mingc)\n")
		.append("having not  (grouping(fenx) = 1 or grouping(p.mingc)+grouping(y.mingc) = 1)\n")
		.append("order by grouping(j.mingc) desc,max(j.xuh),j.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc,\n")
		.append("grouping(p.mingc) desc ,max(p.xuh),p.mingc,grouping(y.mingc) desc ,y.mingc,c.fenx) zjb \n");
		/*sb.append("select decode(grouping(j.mingc),1,'总计',j.mingc) jihkj,\n")
		.append("decode(grouping(g.mingc)+grouping(j.mingc),1,'合计',g.mingc) gongysmc,\n")
		.append("decode(grouping(m.mingc)+grouping(g.mingc)+grouping(j.mingc),1,'小计',m.mingc) meikdwmc,\n")
		.append("nvl(c.fenx,'') fenx, sum(jingz) jingz,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*meij)/sum(jingz)) meij,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*meijs)/sum(jingz)) meijs,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*yunj)/sum(jingz)) yunj,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*yunjs)/sum(jingz)) yunjs,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*jiaohqzf)/sum(jingz)) jiaohqzf,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*zaf)/sum(jingz)) zaf,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*daozzf)/sum(jingz)) daozzf,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*qitfy)/sum(jingz)) qitfy,\n")
		.append("decode(sum(jingz),0,0,sum(jingz*Qnet_ar)/sum(jingz)) Qnet_ar\n")
		.append("from \n")
		.append("(select a.gongysb_id, a.meikxxb_id, a.jihkjb_id,'本日' fenx, nvl(b.jingz,0) jingz,\n")
		.append("nvl(b.meij,0) meij,nvl(b.meijs,0) meijs,nvl(b.yunj,0) yunj,\n")
		.append("nvl(b.yunjs,0) yunjs,nvl(b.jiaohqzf,0) jiaohqzf, nvl(b.zaf,0) zaf,\n")
		.append("nvl(b.daozzf,0) daozzf,nvl(b.qitfy,0) qitfy,nvl(b.Qnet_ar,0) Qnet_ar from \n")
		.append("(select distinct f.gongysb_id,f.meikxxb_id,f.jihkjb_id\n")
		.append("        from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfMonth).append(" \n")
		.append("and f.daohrq < ").append(CurrentDate).append("+1\n")
		.append("and f.diancxxb_id=").append(visit.getDiancxxb_id()).append(") a,\n")
		.append("(select f.gongysb_id,f.meikxxb_id,f.jihkjb_id,sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq =").append(CurrentDate).append("\n")
		.append("and f.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
		.append("      group by f.gongysb_id,f.meikxxb_id,f.jihkjb_id) b\n")
		.append("where a.gongysb_id = b.gongysb_id(+) and a.meikxxb_id = b.meikxxb_id(+)\n")
		.append("and a.jihkjb_id = b.jihkjb_id(+)\n")
		.append("union\n")
		.append("select f.gongysb_id,f.meikxxb_id,f.jihkjb_id,'累计' fenx,sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meij)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meij,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.meijs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) meijs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunj)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunj,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.yunjs)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) yunjs,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.jiaohqzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) jiaohqzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.zaf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) zaf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.daozzf)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) daozzf,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.qitfy)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) qitfy,\n")
		.append("decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),0,0,sum(round_new(f.laimsl,"+v.getShuldec()+")*r.Qnet_ar)/sum(round_new(f.laimsl,"+v.getShuldec()+"))) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfMonth).append("\n")
		.append("and f.daohrq <").append(CurrentDate).append("+1\n")
		.append("and f.diancxxb_id =").append(visit.getDiancxxb_id()).append("\n")
		.append("      group by f.gongysb_id,f.meikxxb_id,f.jihkjb_id) c,gongysb g,meikxxb m, jihkjb j\n")
		.append("where c.gongysb_id = g.id and c.meikxxb_id = m.id and c.jihkjb_id = j.id\n")
		.append("group by rollup (c.fenx,j.mingc,g.mingc,m.mingc)\n")
		.append("having not  grouping(fenx) = 1\n")
		.append("order by grouping(j.mingc) desc,max(j.xuh),j.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc,\n")
		.append("grouping(m.mingc) desc,max(m.xuh),m.mingc,c.fenx\n");*/
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(visit.isFencb()) {
			tb1.addText(new ToolbarText("厂别:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	/*
	 * 将表头信息Locale.jingz_fahb改为Locale.laimsl_fahb
	 * 修改时间：2008-12-05
	 * 修改人：王磊
	 */
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		
		ResultSet rs = con.getResultSet(getBaseSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		Report rt = new Report();

		String[][] ArrHeader = new String[][] {{ Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, 
			Locale.pinzb_id_fahb, Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.laimsl_fahb, Locale.meij_chengbrb, 
			Locale.meijs_chengbrb, Locale.yunj_chengbrb, Locale.yunjs_chengbrb, Locale.jiaohqzf_chengbrb, 
			Locale.zaf_chengbrb, Locale.daozzf_chengbrb, Locale.qitfy_chengbrb, Locale.Qnet_ar_chengbrb,
			Locale.biaomdj_chengbrb, Locale.buhsbmdj_chengbrb
		}};

		int[] ArrWidth = new int[] { 80, 100, 60, 60, 50, 70, 50, 50, 50, 50, 80, 50, 70, 50, 50, 65, 65 };

		rt.setTitle("成本日报", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 6, "报表日期：" + getRiq(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(15, 3, "单位：吨、车", Table.ALIGN_RIGHT);

		String[] arrFormat = new String[] { "", "", "", "", "", "", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "0.00","0.00" };

		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 6, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(15, 3, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
//	工具栏使用的方法
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}
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
//	页面登陆验证
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
}
