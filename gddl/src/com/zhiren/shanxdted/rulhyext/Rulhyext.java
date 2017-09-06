package com.zhiren.shanxdted.rulhyext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.rulgl.meihyb.Meihybext;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-10-27
 * 修改内容:增加Vdaf指标值
 */
/*
 * 作者:tzf
 * 时间:2009-10-26
 * 修改内容:更改有关xitxx中和氢值 硫值相关的diancxxb_id，以电厂树选择，并且更改qnet_ar显示3位
 */
/*
 * 作者:tzf
 * 时间:2009-10-19
 * 修改内容:增加一行平均值
 */
/*
 * 作者:tzf
 * 时间:2009-10-19
 * 修改内容:把qnet_ar小数位调整成保留2位
 */
/*
 * 作者:tzf
 * 时间:2009-09-19
 * 内容:入炉煤质化验
 */
public class Rulhyext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
//		Meihybext.UpdateRulzlID(getRiqi(), visit.getDiancxxb_id());
	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "rulmzlb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			String rulmzlbid = delrsl.getString("id");
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Rulhy,
					tableName,rulmzlbid+"");
			sql.append("delete ").append(tableName).append(" where id = ")
			.append(rulmzlbid).append(";");
			
		}
		

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		long rulmzlbid = 0;
		List list_mzbid=new ArrayList();
		List list_zhi=new ArrayList();
		
		String strRulRq = "";
		String diancxxb_id = "";
		
		while (mdrsl.next()) {
			rulmzlbid = Long.parseLong(mdrsl.getString("ID"));
			if (rulmzlbid == 0) {
				rulmzlbid = Long.parseLong(MainGlobal.getNewID(visit
						.getDiancxxb_id()));
			} else {
				StringBuffer sql1 = new StringBuffer("begin \n");
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Rulhy,
						tableName,rulmzlbid+"");
				sql1.append("delete ").append(tableName).append(" where id = ")
						.append(rulmzlbid).append(";");
				sql1.append("end;");
				con.getDelete(sql1.toString());
			}
			
			strRulRq = mdrsl.getString("rulrq");
			diancxxb_id = mdrsl.getString("diancxxb_id");
			
			double[] zhi = new double[7];
			
			zhi[0] = mdrsl.getDouble("mt");
			zhi[1] = mdrsl.getDouble("mad");
			zhi[2] = mdrsl.getDouble("aad");
			zhi[3] = mdrsl.getDouble("vad");
//			zhi[4] = mdrsl.getDouble(strl);
			zhi[4] = mdrsl.getDouble(visit.getString11());
			zhi[5] = mdrsl.getDouble("qbad");
//			zhi[6] = mdrsl.getDouble(strq);
			zhi[6] = mdrsl.getDouble(visit.getString12());
			
			list_mzbid.add(rulmzlbid+"");
			list_zhi.add(zhi);
			
			sql
					.append("insert into ")
					.append(tableName)
					.append(
							" (id,diancxxb_id,fenxrq,lursj,rulrq,RULBZB_ID,JIZFZB_ID,huayy,LURY) values (");
			sql.append(rulmzlbid
					+ ","
					+ mdrsl.getString("diancxxb_id")
					+ ",to_date('"
					+ mdrsl.getString("fenxrq")
					+ "','yyyy-mm-dd'),to_date(to_char(sysdate,'yyyy-mm-dd')"
					+ ",'yyyy-mm-dd'),to_date('"
					+ mdrsl.getString("rulrq")
					+ "','yyyy-mm-dd'),"
					+ (getExtGrid().getColumn("rulbzb_id").combo)
							.getBeanId(mdrsl.getString("rulbzb_id"))
					+ ","
					+ (getExtGrid().getColumn("jizfzb_id").combo)
							.getBeanId(mdrsl.getString("jizfzb_id")) + ",'"
					+ mdrsl.getString("huayy") + "','"+visit.getRenymc()+"');");
			
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		
		if (!"".equals(strRulRq)) {
			ResultSetList rs = null;
			int rows = 0;	
			int irow = 0;
			double haoml = 0;
			double shengyhml=0;
			double zonghml = 0;
			StringBuffer sb = new StringBuffer();
			rs = con.getResultSetList("SELECT * FROM meihyb_zb WHERE diancxxb_id=" + diancxxb_id + " AND rulrq=to_date('" + strRulRq + "','yyyy-mm-dd')");
			if (rs.next()) {
				zonghml = rs.getDouble("FADHY") + rs.getDouble("GONGRHY") + rs.getDouble("QITY") + rs.getDouble("FEISCY");
				ResultSetList rsHy = con.getResultSetList("SELECT * FROM rulmzlb WHERE rulrq=to_date('" + strRulRq + "','yyyy-mm-dd') AND diancxxb_id=" + diancxxb_id);				
				rows = rsHy.getRows();
				
				if (rows>0) {
					haoml = Math.round(zonghml/rows*100)/100;
					shengyhml = zonghml - haoml*(rows-1);
				}
				
				sb.append("begin \n");
				sb.append(" delete from ").append(" meihyb ").append(
				"WHERE diancxxb_id=").append(rs.getString("diancxxb_id"))
				.append(" AND  rulrq=to_date('" + strRulRq + "','yyyy-mm-dd')") 
				
				.append(";	\n");
				while (rsHy.next()) {
					irow++;
					
					sb.append("insert into meihyb \n");
					sb
							.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
					sb.append("values (getnewid(").append(diancxxb_id).append(
							"),to_date('");
					sb.append(strRulRq).append("','yyyy-mm-dd'),")
							.append(diancxxb_id).append(",");
					sb.append(rsHy.getString("id")).append(",").append(rsHy.getString("RULBZB_ID")).append(",")
							.append(rsHy.getString("jizfzb_id"));
					sb.append(",").append(rows==irow?shengyhml:haoml).append(",")
							.append("0");
					sb.append(",").append("0").append(",")
							.append("0");
					sb.append(",'").append(rs.getString("BEIZ")).append("','")
							.append(rs.getString("LURY"));
					sb.append("',to_date('").append(strRulRq).append(
							"','yyyy-mm-dd'),'").append(rs.getString("zhiyr")).append("',");
					sb.append(rs.getString("SHENHZT")).append("); \n");
					
					sb.append("UPDATE rulmzlb SET meil=" + (rows==irow?shengyhml:haoml) + " WHERE ID=" + rsHy.getString("id") + "; \n");
				}	
				rsHy.close();
				sb.append("end;");
				con.getInsert(sb.toString());
			} 
			rs.close();
			
		}
		for(int i=0;i<list_mzbid.size();i++){
			rulmzlbid=Long.valueOf((String)list_mzbid.get(i)).longValue();
			double[] zhi=(double[])list_zhi.get(i);
			Compute.ComputeRULUValue(con, rulmzlbid, visit.getDiancxxb_id(), zhi,3);
		}
		

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick=false;
			getSelectData();
		}
	}

	public void getSelectData() {
		
		 String strl = "Stad";//提供默认值

		 String strq = "Had";//提供默认值
		
		 boolean xiansztq = true;

		 boolean xiansztl = true;

		 String chr1 = "";

		 String chr2 = "";
		
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}
		ResultSetList rsl = new ResultSetList();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '是否显示入炉化验氢' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '是否显示入炉化验硫' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '入炉化验硫'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
//				+ visit.getDiancxxb_id();
				+this.getTreeid();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strl = rsl.getString("zhi");
		}
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '入炉化验氢'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
//				+ visit.getDiancxxb_id();
				+this.getTreeid();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strq = rsl.getString("zhi");
		}
		if (xiansztq) {
			chr1 = strq + ",";
		}
		if (xiansztl) {
			chr2 = strl + ",";
		}
		double dblH = 0.0D;
		double dblS = 0.0D;
		
		visit.setString11(strl);
		visit.setString12(strq);
		
		sql = "select nvl(decode(sum(f.jingz),\n"
				+ "                  0,\n"
				+ "                  0,\n"
				+ "                  round(sum(f.jingz * z.had) / sum(f.jingz), 4)),\n"
				+ "           0) as had,\n"
				+ "       nvl(decode(sum(f.jingz),\n"
				+ "                  0,\n"
				+ "                  0,\n"
				+ "                  round(sum(f.jingz * z.hdaf) / sum(f.jingz), 4)),\n"
				+ "           0) as hdaf,\n"
				+ "       nvl(decode(sum(f.jingz),\n"
				+ "                  0,\n"
				+ "                  0,\n"
				+ "                  round(sum(f.jingz * z.std) / sum(f.jingz), 4)),\n"
				+ "           0) as std,\n"
				+ "       nvl(decode(sum(f.jingz),\n"
				+ "                  0,\n"
				+ "                  0,\n"
				+ "                  round(sum(f.jingz * z.stad) / sum(f.jingz), 4)),\n"
				+ "           0) as stad,\n"
				+ "       nvl(decode(sum(f.jingz),\n"
				+ "                  0,\n"
				+ "                  0,\n"
				+ "                  round(sum(f.jingz * z.sdaf) / sum(f.jingz), 4)),\n"
				+ "           0) as sdaf\n"
				+ "  from fahb f, zhilb z, caiyb c\n"
				+ " where f.zhilb_id = z.id\n"
				+ "   and c.zhilb_id = f.zhilb_id\n"
				+ "   and c.zhilb_id = z.id\n"
				+ "   and f.diancxxb_id = "
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "   and to_char(z.huaysj, 'yyyy-mm') =\n"
				+ "       to_char(add_months(to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'), -1),\n"
				+ "               'yyyy-mm')";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			dblH = rs.getDouble(strq);
			dblS = rs.getDouble(strl);
		}
		
		//电厂Tree刷新条件
		String diancxxb_id= " and d.id = " +this.getTreeid();
//		if(getDiancTreeJib()==1){
//			diancxxb_id = "";
//		}else if(getDiancTreeJib()==2){
//			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
//		}else if(getDiancTreeJib()==3){
//			if(visit.isFencb()){
//				diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
//			}else{
//				diancxxb_id = "and d.id = " + this.getTreeid() + "\n";
//			}
//		}

		ResultSetList rs_shunx = con.getResultSetList("select zhi from xitxxb x where x.mingc='入厂入炉化验值录入显示顺序' and x.zhuangt=1");
		String xianssx="";
		if(rs_shunx.next()){
			xianssx=rs_shunx.getString("ZHI");

		}else{
			xianssx=""+chr1+""+chr2+" r.mt,r.mad,r.aad,r.vad,r.vdaf,r.qbad,round_new(r.qnet_ar,3) qnet_ar,\n";
			
		}
		
		
		String chaxun ="  select  id,diancxxb_id,rulrq,fenxrq, rulbzb_id,jizfzb_id, "+chr1+chr2+" mt,mad,aad,vad,vdaf,qbad,qnet_ar,huayy,beiz from ( \n"+
		
		"select r.id,r.diancxxb_id,r.rulrq,r.fenxrq,rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ xianssx
				+ "       r.huayy,r.beiz ,j.xuh jzxh,rb.xuh bzxh \n"
				+ "  from rulmzlb r, diancxxb d, rulbzb rb, jizfzb j\n"
				+ " where r.diancxxb_id = d.id(+)\n"
				+ "   and r.rulbzb_id = rb.id(+)\n"
				+ "   and r.jizfzb_id = j.id(+)\n"
				+ "   and r.rulrq = to_date('"
				+ riqTiaoj
				+ "','yyyy-mm-dd')\n"
//				+ "   and (r.shenhzt = 0 or r.shenhzt = 2)\n"
				+ diancxxb_id
				
				+"  union  \n"
				
				+"  select * from ( \n"
				+"  select -1 id,"+this.getTreeid()+" diancxxb_id,to_date('"+riqTiaoj+"','yyyy-mm-dd') rulrq,null  fenxrq,nvl('均值','') as rulbzb_id,'' as jizfzb_id,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum("+strq+"*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) "+strq+",\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum("+strl+"*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) "+strl+",\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.mt*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) mt,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.mad*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) mad,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.aad*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) aad,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.vad*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) vad,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.vdaf*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) vdaf,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum( r.qbad*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),2) ) qbad,\n"
				+"  decode(sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),0,0,round_new(sum(r.qnet_ar*(m.fadhy+m.gongrhy+m.qity+m.feiscy))/sum(m.fadhy+m.gongrhy+m.qity+m.feiscy),3) ) qnet_ar,\n"
				+"  null huayy,'' beiz,null jzxh,null bzxh   from rulmzlb r, diancxxb d,meihyb m \n"
				+"   where r.diancxxb_id = d.id(+) and m.rulmzlb_id=r.id  and r.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') and d.id = "+getTreeid()+"   ) \n"
				+"  where  (select count(*) from rulmzlb rm ,meihyb my where rm.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd')    and rm.diancxxb_id="+getTreeid()+" and rm.id=my.rulmzlb_id \n"
				+"    )=(select count(*) from rulmzlb rz where rz.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') and rz.diancxxb_id = "+getTreeid()+" ) \n"
				+"      and (select count(*) from rulmzlb rz where rz.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') and rz.diancxxb_id = "+getTreeid()+" )!=0 \n"
				
				+" union \n"
				
				+"  select * from ( select -1 id,"+getTreeid()+" diancxxb_id,to_date('"+riqTiaoj+"','yyyy-mm-dd') rulrq,null fenxrq,nvl('均值','') as rulbzb_id,'' as jizfzb_id,\n"
				+"    round_new(avg("+strq+"),2) "+strq+",round_new(avg("+strl+"),2) "+strl+",round_new(avg(r.mt),2) mt,\n"
				+"   round_new(avg(r.mad),2) mad,round_new(avg(r.aad),2) aad,\n"
				+"    round_new(avg(r.vad),2) vad,round_new(avg(r.vdaf),2) vdaf,round_new(avg(r.qbad),2) qbad,round_new(avg(r.qnet_ar),3) qnet_ar,\n"
				+"     '' huayy,'' beiz,null jzxh,null bzxh from rulmzlb r, diancxxb d \n"
				+"  where r.diancxxb_id = d.id(+)  and r.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd')  and d.id = "+getTreeid()+"   ) \n"
				+"  where  (select count(*) from rulmzlb rm ,meihyb my where rm.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') \n"
				+"   and rm.diancxxb_id="+getTreeid()+" and rm.id=my.rulmzlb_id    )!=(select count(*) from rulmzlb rz where rz.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') \n"
				+"  and rz.diancxxb_id = "+getTreeid()+" )  " +
						" and (select count(*) from rulmzlb rz where rz.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd') and rz.diancxxb_id = "+getTreeid()+" )!=0 \n" +
						" ) \n order by rulrq,fenxrq,bzxh,jzxh ";
		
		
		
		rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rulmzlb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("rulrq").setHeader("入炉日期");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("jizfzb_id").setHeader("入炉机组");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("qbad").setHeader("Qb,ad(MJ/kg)");
		
		egu.getColumn("qnet_ar").setHeader("Qnet_ar(%)");
		egu.getColumn("qnet_ar").setEditor(null);
		
		
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("beiz").setHeader("备注");
		if (xiansztq) {
			egu.getColumn(strq).setHeader(strq + "(%)");
			egu.getColumn(strq).setWidth(60);
			egu.getColumn(strq).setDefaultValue(""+dblH);
		}
		if (xiansztl) {
			egu.getColumn(strl).setHeader(strl + "(%)");
			egu.getColumn(strl).setWidth(60);
			egu.getColumn(strl).setDefaultValue(""+dblS);
		}
		egu.getColumn("rulrq").setWidth(85);
		egu.getColumn("fenxrq").setWidth(85);
		egu.getColumn("diancxxb_id").setWidth(85);
		egu.getColumn("rulbzb_id").setWidth(85);
		egu.getColumn("jizfzb_id").setWidth(85);

		egu.getColumn("mt").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("huayy").setWidth(60);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		// *****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + this.getTreeid());
		egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
		egu.getColumn("fenxrq")
				.setDefaultValue(DateUtil.FormatDate(new Date()));

		// 设置下拉框入炉班组
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r where r.diancxxb_id="
				+ this.getTreeid() + " order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		// 设置下拉框入炉机组
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ this.getTreeid() + "";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		
		
		//化验员
		
		ComboBox huayy=new ComboBox();
		egu.getColumn("huayy").setEditor(huayy);
		egu.getColumn("huayy").setComboEditor(egu.gridId, new IDropDownModel(" select id,quanc from renyxxb where zhiw='入炉化验员' "));
		egu.getColumn("huayy").setReturnId(false);
		egu.getColumn("huayy").editor.allowBlank=true;
		
		

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

//		egu.addTbarText("-");

//		 ************************************************************
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButtton");
		GridButton gbt = new GridButton("添加",getInsertScript(egu,true));
		gbt.icon=SysConstant.Btn_Icon_Insert;
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
//		egu.addToolbarButton("提交",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
//		egu.addToolbarButton("提交",GridButton.ButtonType_Save, "SaveButton",null, SysConstant.Btn_Icon_Show);
		
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){ if(e.record.get('ID')=='-1'){e.cancel=true;} });");
		setExtGrid(egu);
		con.Close();
	}
	

	private String getInsertScript(ExtGridUtil egu,boolean t){
		
		String parentId=egu.gridId;
	    String 	handler = "function() {\n" +
		" var line="+parentId+"_ds.getCount();\n";
		if(t) handler+=" if(gridDiv_ds.getCount()>0 && gridDiv_ds.getAt(gridDiv_ds.getCount()-1).get('ID')=='-1'){line=gridDiv_ds.getCount()-1;} \n  ";
		handler+=getRecordScript(0,egu) +parentId+"_ds.insert("
		+"line"+",plant);} ";

		
		return handler;
	}
	
     private String getRecordScript(int type,ExtGridUtil egu) {
    	String parentId=egu.gridId;
        List columns=egu.getGridColumns(); 
		StringBuffer record = new StringBuffer();
		record.append("var plant = new ").append(parentId).append("_plant({");
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(type == 0) {
					record.append(gc.dataIndex).append(": '").append(gc.defaultvalue).append("',");
				}else 
				if(type ==1 ){
					record.append(gc.dataIndex).append(": rec.get('").append(gc.dataIndex).append("'),");
				}
			}
		}
		record.deleteCharAt(record.length()-1);
		record.append("});\n");
		return record.toString();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.setString11("");
			visit.setString12("");
		}

		getSelectData();

	}

	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
//			riqi = DateUtil.FormatDate(new Date());
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	
//	 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

	private String treeid;

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
		((Visit) getPage().getVisit()).setString3(treeid);
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
}
