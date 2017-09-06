package com.zhiren.jt.zdt.rulgl.gusmh;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Gusmh extends BasePage {
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

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGrisT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
//		ResultSet rs = con.getResultSet(sql);
//		try {                                      
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
//		return getQibb();
//		if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getZhiltj();
//		} else {
//			return "无此报表";
//		}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		int jib=this.getDiancTreeJib();
		String titlename="耗煤估算日报";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		
		String diancCondition=
			"and r.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		String diancCondition1=
			"where d.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
			sbsql.append("from  \n");
			sbsql.append("(select d.id,d.xuh,d.mingc,d.fuid,vwfenx.fenx from diancxxb d,vwfenx ").append(diancCondition1).append(") fx, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,sum(fadl) as fadl,sum(gongdl) as gongdl, \n");
			sbsql.append("sum(gongrl) as gongrl from riscsjb where riq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fadl) as fadl,sum(gongdl) as gongdl, \n");
			sbsql.append("sum(gongrl) as gongrl from riscsjb where riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("and riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') group by diancxxb_id) rsj, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
			sbsql.append("from shouhcrbb where riq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fady) as fadym,sum(gongry) as gongrym \n");
			sbsql.append("from shouhcrbb where riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("and riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') group by diancxxb_id) msj, \n");
			sbsql.append("(select rl.diancxxb_id,decode(1,1,'当日') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
			sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
			sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("group by rl.diancxxb_id \n");
			sbsql.append("union select rl.diancxxb_id,decode(1,1,'累计') as fenx,decode(sum(mh.fadhy+mh.gongrhy), \n");
			sbsql.append("0,0,round((sum(mh.fadhy*rl.qnet_ar)+sum(mh.gongrhy*rl.qnet_ar))/sum(mh.fadhy+mh.gongrhy),2)) as farl  \n");
			sbsql.append("from rulmzlb rl,meihyb mh where mh.rulmzlb_id=rl.id and mh.rulrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
			sbsql.append("and mh.rulrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') group by rl.diancxxb_id) mrez, \n");
			sbsql.append("(select diancxxb_id,decode(1,1,'当日') as fenx,sum(fady) as fady,'40.98' as yrl,sum(gongry) as gongry \n");
			sbsql.append("from shouhcrbyb where riq=to_date('"+getEndriqDate()+"','yyyy-mm-dd') group by diancxxb_id \n");
			sbsql.append("union select diancxxb_id,decode(1,1,'累计') as fenx,sum(fady) as fady,'40.98' as yrl,sum(gongry) as gongry \n");
			sbsql.append("from shouhcrbyb where riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  \n");
			sbsql.append("and  riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') group by diancxxb_id) ysj,vwfengs gs,vwdianc vdc \n");
			String  tjsql=sbsql.toString();
			sbsql.setLength(0);
		
		if(jib==3){
			sbsql.append("select fx.mingc as danw, \n");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0) as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),2)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongrl),2)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),2)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.mingc)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}else if(jib==2){
			JDBCcon cn = new JDBCcon();
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			String danw = "";
			String groupby = "";
			String having ="";
			String orderby = "";
			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl.next()){
					danw="decode(grouping(vdc.rlgsmc)+grouping(gs.mingc)+grouping(fx.mingc),2,vdc.rlgsmc,1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw,\n";
					groupby="group by rollup(fx.fenx,vdc.rlgsmc,gs.mingc,fx.mingc) \n";
					having="having not(grouping(vdc.rlgsmc)=1) \n";
					orderby="order by grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n";
				}else{
					danw="decode(grouping(gs.mingc)+grouping(fx.mingc),1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw,\n";
					groupby="group by rollup(fx.fenx,gs.mingc,fx.mingc) \n";
					having="having not(grouping(gs.mingc)=1) \n";
					orderby="order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n";
				}
				rl.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			sbsql.append("select "+danw+"");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0) as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),2)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongrl),2)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),2)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+) and fx.id=vdc.id  \n");
			sbsql.append(groupby);
			sbsql.append(having);
			sbsql.append(orderby);
		}else{
			sbsql.append("select decode(grouping(gs.mingc)+grouping(fx.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;'||fx.mingc) as danw, \n");
			sbsql.append("fx.fenx,round(sum(rsj.fadl),0) as fadl,round(sum(rsj.gongdl),0) as gongdl,round(sum(rsj.gongrl),0) as gongrl, \n");
			sbsql.append("round(sum(msj.fadym),0) as fadym,round(sum(msj.gongrym),0) as gongrym, \n");
			sbsql.append("decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),2)) as meihrz, \n");
			sbsql.append("round(sum(ysj.fady),0) as fadyy,round(sum(ysj.gongry),0) as gongryy, \n");
			sbsql.append("decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2)) as youhrz, \n");
			sbsql.append("round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271) as fadmzbml, \n");
			sbsql.append("round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271) as gongrmzbml, \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271) as fadyzbml, \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271) as gongryzbml, \n");
			sbsql.append("(round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271)) as fadzhbml, \n");
			sbsql.append("(round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),0))/29.271)) as gongrzhbml, \n");
			sbsql.append("decode(sum(rsj.fadl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.fadl),0)) as fadbzmh, \n");
			sbsql.append("decode(sum(rsj.gongrl),0,0,round((round(sum(msj.gongrym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.gongry)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongrl),0)) as gongrbzmh, \n");
			sbsql.append("decode(sum(rsj.gongdl),0,0,round((round(sum(msj.fadym)*decode(sum(msj.fadym+msj.gongrym),0,0,round((sum(msj.fadym*mrez.farl)+sum(msj.gongrym*mrez.farl))/sum(msj.fadym+msj.gongrym),0))/29.271)+ \n");
			sbsql.append("round(sum(ysj.fady)*decode(sum(ysj.fady+ysj.gongry),0,0,round((sum(ysj.fady*ysj.yrl)+sum(ysj.gongry*ysj.yrl))/sum(ysj.fady+ysj.gongry),2))/29.271))/sum(rsj.gongdl),0)) as gongdbzmh \n");
			sbsql.append(tjsql);
			sbsql.append("where fx.fuid=gs.id and fx.id=ysj.diancxxb_id(+) and fx.id=mrez.diancxxb_id(+) and fx.id=rsj.diancxxb_id(+)  \n");
			sbsql.append("and fx.id=msj.diancxxb_id(+) and fx.fenx=ysj.fenx(+) and fx.fenx=msj.fenx(+) and fx.fenx=rsj.fenx(+) \n");
			sbsql.append("and fx.fenx=mrez.fenx(+)  \n");
			sbsql.append("group by rollup(fx.fenx,gs.mingc,fx.mingc) \n");
			sbsql.append("having not(grouping(fx.fenx)=1) \n");
			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh),fx.mingc,fx.fenx \n");
		}
			
		 //System.out.println(sbsql);
		 ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		 Report rt = new Report();
		 String ArrHeader[][]=new String[2][20];
		 ArrHeader[0]=new String[] {"单位","单位","发电量<br>(千瓦时)","供电量<br>(千瓦时)","供热量<br>(吉焦)","耗天然煤量(吨)","耗天然煤量(吨)","耗煤热值<br>(MJ/Kg)","耗天然油量(吨)","耗天然油量(吨)","耗油热值<br>(千卡/千克)","耗用煤折标煤量(吨)","耗用煤折标煤量(吨)","耗用油折标煤量(吨)","耗用油折标煤量(吨)","综合标煤量(吨)","综合标煤量(吨)","标准煤耗","标准煤耗","标准煤耗"};
		 ArrHeader[1]=new String[] {"单位","单位","发电量<br>(千瓦时)","供电量<br>(千瓦时)","供热量<br>(吉焦)","发电","供热","耗煤热值<br>(MJ/Kg)","发电","供热","耗油热值<br>(MJ/Kg)","发电","供热","发电","供热","发电","供热","发电<br>(克/度)","供电<br>(克/度)","供热<br>(千克/吉焦)"};
		 int ArrWidth[]=new int[] {150,45,70,70,70,70,70,70,50,50,70,70,70,50,50,70,70,45,45,45};
		 iFixedRows=1;
		 iCol=10;
	
	 
		// 数据
	//	rt.setBody(new Table(rs,2, 0, 2));
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		//第二列居中
		bt.setColAlign(2,Table.ALIGN_CENTER);
		rt.setTitle(getBiaotmc()+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, riq1, Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		//第三行、第一列居中
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		//页脚 
		  rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 
	//	设置页数
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(15,3,"审核人:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(18,3,"填报人:",Table.ALIGN_LEFT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
				stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
//				stra.setTime(new Date());
				stra.add(Calendar.MONTH,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80); 
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1); 
		tb1.addText(new ToolbarText("-"));
		
		Visit visit = (Visit) getPage().getVisit();
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
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);
		}
		getToolbars();

		blnIsBegin = true;

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
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
//		 分公司下拉框
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}

		public IPropertySelectionModel getFengsModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getFengsModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setDiancxxModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(value);
		}

		public void getFengsModels() {
			String sql;
			sql = "select id ,mingc from diancxxb where jib=2 order by id";
			setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
		}
	
//		得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
			return biaotmc;
			
		}
		
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
