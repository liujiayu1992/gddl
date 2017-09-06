package com.zhiren.dc.rulgl.rulmzlb;

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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：刘雨
 * 时间：2010-04-09
 * 描述：新增功能：1.用方向键控制光标移动
 * 		适用范围：大唐阳城
 */
/*作者:王总兵
 *时间:2010-4-8 20:26:20
 *描述:1.低位发热量增加小数位数参数控制
 *     2.增加分析日期比入炉日期大1天的参数设置
 * select zhi from xitxxb where leib = '入炉' and mingc = '入炉化验热值小数位数' and zhuangt=1
 * select zhi from xitxxb x where x.mingc='入炉化验分析日期是否比入炉日期大1天' and leib='入炉' and x.zhuangt=1
 */
/*
 * 作者：王磊
 * 时间：2009-11-16
 * 描述：修改增加马头导入后数据提交为全部提交。
 */
/*
 * 作者:tzf
 * 时间:2009-11-10
 * 修改内容:增加多行替换功能   导入时   化验员默认为登录人员名称
 */
/*
 * 作者:tzf
 * 时间:2009-10-29
 * 修改内容:增加导入按钮对应的提交方法  原来化验值不能重新计算，只需更改其审核状态
 */
/*
 * 作者:tzf
 * 时间:2009-10-27
 * 修改内容:将导入计算弹筒值和低位热值做调整，以 A B C D计算机组弹筒值均值，以每项求出低位热值后求均值为最终的低位热值。
 */
/*
 * 作者:tzf
 * 时间:2009-10-26
 * 修改内容:导入按钮中因为数据库字符编码的不一样，现调整yx_mhysj表中用xh去做判断。
 */
/*
 * 作者:tzf
 * 时间:2009-10-21
 * 修改内容:增加导入按钮及导入处理方法
 */
/*
 * 作者：王磊
 * 时间：2009-07-28 18：07
 * 描述：增加入炉弹筒热值项宽度为80
 */
/*
 * 王磊
 * 2009-05-13
 * 电厂树的TreeId未初始化的BUG
 */
/**
 * @author ly
 * 修改时间：2009-04-27
 * 修改内容：增加电厂Tree
 */
/*
 * 作者：王磊
 * 时间：2009-07-08
 * 描述：修改电厂Tree刷新及保存没有考虑一厂多制的问题。
 * 修改保存时化验员和录入员保存互换的问题
 */
/*
 * 作者：张森涛
 * 时间：2009-07-10
 * 描述：修改保存时化验员和录入员保存互换的问题
 */

/*
 * 作者：赵胜男
 * 时间：2013-05-16
 * 描述：加参数修正入炉化验值录入不自动计算重新计算
 */
public class Rulmzlbext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
//	页面初始化(每次刷新都执行)
	protected void initialize() {
		super.initialize();
		setMsg("");
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
		if (MainGlobal.getXitxx_item("入炉", "入炉化验值录入是否重新计算","0", "是").equals("是")) {
			Save1(getChange(), visit);
		}else{
			TrueSave();
		}
		
//		Meihybext.UpdateRulzlID(getRiqi(), Integer.parseInt(diancxxb_id));//visit.getDiancxxb_id()，为一厂多制而改，
	}
	
	//真正的保存按钮,数据可以先保存,最后再提交
	public void TrueSave() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
	
		while (mdrsl.next()) {
			sql.append(
					"update rulmzlb\n" +
					"   set fenxrq = to_date('"+mdrsl.getString("FENXRQ")+"','yyyy-MM-dd'),\n" + 
					"       rulbzb_id = "+getExtGrid().getValueSql(getExtGrid().getColumn("RULBZB_ID"), mdrsl.getString("RULBZB_ID"))+",\n" + 
					"       mt = "+mdrsl.getDouble("MT")+",\n" + 
					"       had = "+mdrsl.getDouble("had")+",\n" + 
					"       mad = "+mdrsl.getDouble("MAD")+",\n" + 
					"       aad = "+mdrsl.getDouble("AAD")+",\n" + 
					"       vad = "+mdrsl.getDouble("VAD")+",\n" + 
					"       std = "+mdrsl.getDouble("Std")+",\n" + 
					"       stad = "+mdrsl.getDouble("Stad")+",\n" + 
					"       qbad = "+mdrsl.getDouble("QBAD")+",\n" + 
					"       huayy = '"+mdrsl.getString("HUAYY")+"',\n" + 
					"       beiz = '"+mdrsl.getString("BEIZ")+"',\n" + 
					"       lury = '"+visit.getRenymc()+"',\n" + 
					"       lursj = sysdate\n" + 
					" where id = "+mdrsl.getString("ID")+";\n"
			);
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		
		mdrsl.close();
		con.Close();
	}
	
	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		double meil = 0;
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql2 = new StringBuffer("update rulmzlb set shenhzt=0 where id in (");
		String Sql_xiaosw = "select zhi from xitxxb where leib = '入炉' and mingc = '入炉化验热值小数位数' and zhuangt=1";
		ResultSetList rsl = con.getResultSetList(Sql_xiaosw);
		int Qnetar_weis=3;
		if(rsl.next()){
			Qnetar_weis=rsl.getInt("zhi");
		}
		rsl.close();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		double[] zhi = new double[7];
		long[] rulmzlb_id = new long[mdrsl.getRows()]; 
		int i=0;
		while (mdrsl.next()) {
//			rulmzlbid = Long.parseLong(mdrsl.getString("ID"));
//			diancxxb_id = mdrsl.getString("diancxxb_id");
//			if (rulmzlbid == 0) {
//				rulmzlbid = Long.parseLong(MainGlobal.getNewID(visit
//						.getDiancxxb_id()));
//			} else {
//				StringBuffer sql1 = new StringBuffer("begin \n");
//				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
//						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Rulhy,
//						tableName,rulmzlbid+"");
//				sql1.append("delete ").append(tableName).append(" where id = ")
//						.append(rulmzlbid).append(";");
//				sql1.append("end;");
//				con.getDelete(sql1.toString());
//			}
			zhi[0] = mdrsl.getDouble("mt");
			zhi[1] = mdrsl.getDouble("mad");
			zhi[2] = mdrsl.getDouble("aad");
			zhi[3] = mdrsl.getDouble("vad");
//			zhi[4] = mdrsl.getDouble(strl);
//			硫
			zhi[4] = mdrsl.getDouble(visit.getString11())==0?visit.getDouble1():mdrsl.getDouble(visit.getString11());
			zhi[5] = mdrsl.getDouble("qbad");
//			zhi[6] = mdrsl.getDouble(strq);
//			氢
			zhi[6] = mdrsl.getDouble(visit.getString12())==0?visit.getDouble2():mdrsl.getDouble(visit.getString12());
			
			String sqlmeil = "select fadhy+gongrhy+qity as meil from meihyb where rulmzlb_id="+mdrsl.getString("ID");
			ResultSetList rsmeil = con.getResultSetList(sqlmeil);
			if(rsmeil.next()){
				meil = rsmeil.getDouble("meil");
			}
			sql.append(
					"update rulmzlb\n" +
					"   set fenxrq = to_date('"+mdrsl.getString("FENXRQ")+"','yyyy-MM-dd'),\n" + 
					"       rulbzb_id = "+getExtGrid().getValueSql(getExtGrid().getColumn("RULBZB_ID"), mdrsl.getString("RULBZB_ID"))+",\n" + 
					"       mt = "+mdrsl.getString("MT")+",\n" + 
					"       mad = "+mdrsl.getString("MAD")+",\n" + 
					"       aad = "+mdrsl.getString("AAD")+",\n" + 
					"       vad = "+mdrsl.getString("VAD")+",\n" + 
					"       qbad = "+mdrsl.getString("QBAD")+",\n" + 
					"		meil = "+meil+",\n" +
					"       huayy = '"+mdrsl.getString("HUAYY")+"',\n" + 
					"       beiz = '"+mdrsl.getString("BEIZ")+"',\n" + 
					"       lury = '"+visit.getRenymc()+"',\n" + 
					"       lursj = sysdate\n" + 
					" where id = "+mdrsl.getString("ID")+";\n"
			);
			rulmzlb_id[i]=Long.parseLong(mdrsl.getString("ID"));
			i++;
//			sql
//					.append("insert into ")
//					.append(tableName)
//					.append(
//							" (id,diancxxb_id,fenxrq,lursj,rulrq,RULBZB_ID,JIZFZB_ID,huayy,LURY) values (");
//			sql.append(rulmzlbid
//					+ ","
//					+ diancxxb_id
//					+ ",to_date('"
//					+ mdrsl.getString("fenxrq")
//					+ "','yyyy-mm-dd'),to_date(to_char(sysdate,'yyyy-mm-dd')"
//					+ ",'yyyy-mm-dd'),to_date('"
//					+ mdrsl.getString("rulrq")
//					+ "','yyyy-mm-dd'),"
//					+ (getExtGrid().getColumn("rulbzb_id").combo)
//							.getBeanId(mdrsl.getString("rulbzb_id"))
//					+ ","
//					+ (getExtGrid().getColumn("jizfzb_id").combo)
//							.getBeanId(mdrsl.getString("jizfzb_id")) + ",'"
//					+ mdrsl.getString("huayy") + "','"+visit.getRenymc()+"');\n");
		}
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			for(i=0;i<rulmzlb_id.length;i++){
				
				Compute.ComputeRULUValue(con, rulmzlb_id[i], visit.getDiancxxb_id(), zhi,Qnetar_weis);
				sql2.append(rulmzlb_id[i]).append(",");
			}
			
			sql2.deleteCharAt(sql2.length()-1);
			sql2.append(")");
			
			
			String strsql=" select * from xitxxb where mingc='入炉化验导入提交按钮显示' and leib='入炉' and zhuangt=1 and zhi='是' and diancxxb_id="+visit.getDiancxxb_id();
			mdrsl=con.getResultSetList(strsql);
	 		if(mdrsl.next()){//马头专用
	 			
	 			if(con.getUpdate(sql2.toString())>=0){
					
					this.setMsg("保存成功！");
				}else{
					
					this.setMsg("保存失败！");
				}
	 		}else{
//	 			不是马头电厂直接提示保存成功
	 			this.setMsg("提交成功！");
	 		}
		}else{
			
			this.setMsg("提交失败！");
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	
	private boolean _TrueSaveChick = false;

	public void TrueSaveButton(IRequestCycle cycle) {
		_TrueSaveChick = true;
	}
	
	private boolean _DaorChick;
	
	public void DaorButton(IRequestCycle cycle){
		_DaorChick=true;
	}

    private boolean _RefreshChick;

    public void RefreshButton(IRequestCycle cycle){
        _RefreshChick=true;
    }

	private boolean _DaorTjChick=false;
	public void DaorTjButton(IRequestCycle cycle){
		_DaorTjChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_DaorChick){
			_DaorChick=false;
			Daor();
			getSelectData();
		}
		if(_DaorTjChick){
			_DaorTjChick=false;
			DaorTj();
		}
		if(_TrueSaveChick){
			_TrueSaveChick=false;
			TrueSave();
			getSelectData();
		}
        if(_RefreshChick){
            _RefreshChick=false;
            getSelectData();
        }
	}
	
	private void DaorTj(){

		String tableName = "rulmzlb";
		Visit visit=(Visit)this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		long rulmzlbid = 0;
		while (mdrsl.next()) {
			rulmzlbid = Long.parseLong(mdrsl.getString("ID"));
			sql.append(" update "+tableName+" set RULBZB_ID="+(getExtGrid().getColumn("rulbzb_id").combo)
					.getBeanId(mdrsl.getString("rulbzb_id")) +",huayy='"+mdrsl.getString("huayy")+"' where id="+rulmzlbid+";\n");
			sql.append(" update "+tableName+" set shenhzt = 1 where id="+rulmzlbid+";\n");
			
		}
		
		sql.append("end;");
		con.getUpdate(sql.toString());
		mdrsl.close();
		con.Close();
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		rsl.close();
		return s.substring(0,s.lastIndexOf(","));
	}
	
	private void Daor(){
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String dcidStr=getFenc(con);
		
		String insertSql="";
		
		String sql=" select * from xitxxb where mingc='入炉煤化验导入数据表' and leib='入炉'  and zhuangt=1";
		ResultSetList rsl=con.getResultSetList(sql);
		
		String tableName="yx_mhysj";
		if(rsl.next()){
			tableName=rsl.getString("zhi");
		}
		
		if(tableName!=null ){//针对 马头
			
			sql=" select r.id, r.JIZFZB_ID,REGEXP_SUBSTR(j.mingc,'[0-9]+') jzmc from rulmzlb r,JIZFZB j where r.JIZFZB_ID=j.id and  r.rulrq="+DateUtil.FormatOracleDate(this.getRiqi())+" and  r.diancxxb_id in ("+dcidStr+") ";
			rsl=con.getResultSetList(sql);
			boolean flag=false;
			List list_mzbid=new ArrayList();
			List list_zhi=new ArrayList();
			List list_qbads=new ArrayList();
			while(rsl.next()){//说明本时间化验数据有值，和煤耗用对应的关系不变，即id不变，更改化验值和机组值等信息
				flag=true;
				
				String id=rsl.getString("id");
				String jzmc=rsl.getString("jzmc");
				
				sql=" select y.xh, y.CT_ZBBH,y.A"+jzmc+" aValue,y.B"+jzmc+" bValue,y.C"+jzmc+" cValue,y.D"+jzmc+" dValue,y.E"+jzmc+" jzvalue from "+tableName+"  y where y.DT_RQ="+DateUtil.FormatOracleDate(this.getRiqi());
				
				String qbad="";//弹筒热值
				String mt="";//全水分
				String mad="";//空干基水份
				String aad="";//空干基灰份
				String vad="";//空干基挥发份
				String stad="";//空干基硫
				String had="";//氢元素
				ResultSetList rsl2=con.getResultSetList(sql);
				while(rsl2.next()){//取得每个机组的化验值信息
					
					if(rsl2.getString("xh").equals("-1")){
//						qbad=rsl2.getString("jzvalue");
						int cou=0;
						if(rsl2.getDouble("aValue")!=0) cou++;
						if(rsl2.getDouble("bValue")!=0) cou++;
						if(rsl2.getDouble("cValue")!=0) cou++;
						if(rsl2.getDouble("dValue")!=0) cou++;
						
						if(cou==0) qbad="0";
						else 
						qbad=(rsl2.getDouble("aValue")+rsl2.getDouble("bValue")+rsl2.getDouble("cValue")+rsl2.getDouble("dValue"))*1.0/1000/cou+"";
					
						List list_qbad=new ArrayList();
						list_qbad.add(rsl2.getDouble("aValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("bValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("cValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("dValue")*1.0/1000+"");
						list_qbads.add(list_qbad);
					}
					if(rsl2.getString("xh").equals("1")){
						mt=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("2")){
						mad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("3")){
						aad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("5")){
						vad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("6")){
						stad=rsl2.getString("jzvalue");
					}
				
				}
				
				sql=" select * from xitxxb where mingc='入炉煤化验导入氢值表' and leib='入炉'  and zhuangt=1";
				String qingTableName="yx_mhy_h";
				rsl2=con.getResultSetList(sql);
				if(rsl2.next()){
					qingTableName=rsl2.getString("zhi");
				}
				
				sql=" select * from "+qingTableName+" where ct_no='10004'";
				rsl2=con.getResultSetList(sql);
				if(rsl2.next()){
					had=rsl2.getString("CT_MEMO");
				}
				
				if(qbad==null || qbad.equals(""))   qbad="0";
				
				if(mt==null   || mt.equals("")) 	 mt="0";
				if(mad==null  || mad.equals(""))    mad="0";
				if(aad==null  || aad.equals(""))    aad="0";
				if(vad==null  || vad.equals(""))    vad="0";
				if(stad==null || stad.equals(""))   stad="0";
				if(had==null  || had.equals(""))    had="0";
				
				double[] zhi = new double[7];
				zhi[0] =Double.parseDouble(mt);
				zhi[1] = Double.parseDouble(mad);
				zhi[2] = Double.parseDouble(aad);
				zhi[3] = Double.parseDouble(vad);
				zhi[4] = Double.parseDouble(stad);
				zhi[5] = Double.parseDouble(qbad);
				zhi[6] = Double.parseDouble(had);
				list_zhi.add(zhi);
				
				insertSql+=" update rulmzlb set qbad="+qbad+",mt="+mt+",mad="+mad+",aad="+aad+",vad="+vad+",stad="+stad+",had="+had+",LURY='"+visit.getRenymc()+"' where id="+id+"; \n";
				list_mzbid.add(id);
				
			}
			if(flag){//有对应关系
				insertSql=" begin \n"+insertSql+" end;";
//				System.out.println(insertSql);
				if(con.getUpdate(insertSql)>=0){//更新成功,计算其余化验值
					for(int i=0;i<list_mzbid.size();i++){
						long rulmzlbid=Long.valueOf((String)list_mzbid.get(i)).longValue();
						double[] zhi=(double[])list_zhi.get(i);
						this.ComputeRULUValue(con, rulmzlbid, visit.getDiancxxb_id(), zhi,(List)list_qbads.get(i));
					}
				}
				
			}else{//重新插入,与煤耗用没有任何关联关系
				
			}
		}
		con.Close();
	}
	
	//导入计算化验值的方法
	private  void ComputeRULUValue(JDBCcon con, long rulmzlbid,
			long diancxxb_id, double[] zhi,List list_qbad) {
		StringBuffer SQL = new StringBuffer("");
		double dblHad = 0;
		double dblVad = 0;
		double dblStad = 0;
		double dblAad = 0;
		double dblMt = 0;
		double dblMad = 0;
		double dblA = 0;
		double dblAd = 0;
		double dblAar = 0;
		double dblQgrad = 0;
		double dblQbad = 0;
		double dblQnetar = 0;
		double dblFcad = 0;
		double dblVdaf = 0;
		double dblStd = 0;
		double dblSdaf = 0;
		double dblHdaf = 0;
		double dblQgrdaf = 0;
		double dblHar = 0;
		double dblQgrd = 0;
		try {
			dblMt = zhi[0];
			dblMad = zhi[1];
			dblAad = zhi[2];
			dblVad = zhi[3];
			dblQbad = zhi[5];
			String SQLCtr = "";
			String SQLStr = "select * from xitxxb where leib = '入炉' and mingc = '入炉氢取值'";
			ResultSetList rsl = con.getResultSetList(SQLStr);
			if (rsl.next()) {
				SQLCtr = "select zhi\n" + "  from rulysfxb\n"
						+ " where diancxxb_id = " + diancxxb_id + "\n"
						+ "   and zhuangt = 1\n"
						+ "   and rulysfxxm_id = (select id\n"
						+ "                         from xitxxb\n"
						+ "                        where leib = '入炉'\n"
						+ "                          and mingc like '入炉化验氢'\n"
						+ "                          and zhuangt = 1\n"
						+ "                          and diancxxb_id = "
						+ diancxxb_id + " ) order by riq ";
				ResultSetList rsel = con.getResultSetList(SQLCtr);
				while (rsel.next()) {
					zhi[6] = rsel.getDouble("zhi");
				}
				rsel.close();
			}
			rsl.close();
			SQLStr = "select * from xitxxb where leib = '入炉' and mingc = '入炉硫取值'";
			rsl = con.getResultSetList(SQLStr);
			if (rsl.next()) {
				SQLCtr = "select zhi\n" + "  from rulysfxb\n"
						+ " where diancxxb_id = " + diancxxb_id + "\n"
						+ "   and zhuangt = 1\n"
						+ "   and rulysfxxm_id = (select id\n"
						+ "                         from xitxxb\n"
						+ "                        where leib = '入炉'\n"
						+ "                          and mingc like '入炉化验硫'\n"
						+ "                          and zhuangt = 1\n"
						+ "                          and diancxxb_id = "
						+ diancxxb_id + " ) order by riq ";
				ResultSetList rsel = con.getResultSetList(SQLCtr);
				while (rsel.next()) {
					zhi[4] = rsel.getDouble("zhi");
				}
				rsel.close();
			}
			rsl.close();
			String sql = "select zhi\n" + "  from xitxxb\n"
					+ " where mingc = '入炉化验硫'\n" + "   and zhuangt = 1\n"
					+ "   and diancxxb_id = " + diancxxb_id;
			ResultSetList rs = con.getResultSetList(sql);
			String S = "";
			while (rs.next()) {
				S = rs.getString("zhi");
			}
			sql = "select zhi\n" + "  from xitxxb\n"
					+ " where mingc = '入炉化验氢'\n" + "   and zhuangt = 1\n"
					+ "   and diancxxb_id = " + diancxxb_id;
			rs = con.getResultSetList(sql);
			String H = "";
			while (rs.next()) {
				H = rs.getString("zhi");
			}
			if (S.equals("Std")) {
				dblStd = zhi[4];
				dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
						2);
				dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
						2);
			} else if (S.equals("Stad")) {
				dblStad = zhi[4];
				if (dblMad == 100) {
					dblStd = 0;
				} else {
					dblStd = CustomMaths.Round_new(dblStad * 100
							/ (100 - dblMad), 2);
				}
				dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
						2);
			} else if (S.equals("Sdaf")) {
				dblSdaf = zhi[4];
				if (dblMad == 100) {
					dblStd = 0;
				} else {
					dblStd = CustomMaths.Round_new((100 - dblAd) / 100
							* dblSdaf, 2);
				}
				dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
						2);
			}
			if (H.equals("Had")) {
				dblHad = zhi[6];
				if ((100 - dblMad - dblAad) == 0) {
					dblHdaf = 0;
				} else {
					dblHdaf = CustomMaths.Round_new(dblHad * 100
							/ (100 - dblMad - dblAad), 2);
				}
			} else if (H.equals("Hdaf")) {
				dblHdaf = zhi[6];
				dblHad = CustomMaths.Round_new(((100 - dblMad - dblAad)
						* dblHdaf / 100), 2);
			}
			if (dblMad == 100) {
				dblAd = 0;
			} else {
				dblAd = CustomMaths.Round_new(dblAad * 100 / (100 - dblMad), 2);
			}
			if (dblQbad < 16.72) {
				dblA = 0.001;
			} else {
				if (dblQbad >= 16.72 && dblQbad <= 25.1) {
					dblA = 0.0012;
				} else {
					dblA = 0.0016;
				}
			}
			dblQgrad = CustomMaths.Round_new(
					(dblQbad - (0.0941 * dblStad + dblA * dblQbad)), 3);
			if (dblMad == 100) {
				dblQnetar = -23;
				dblAd = 0;
				dblAar = 0;
			} else {
				
				int cou=0;
				for(int i=0;i<list_qbad.size();i++){//求平均低位热值
					
					if(Double.parseDouble(list_qbad.get(i).toString())==0) continue;
					
					cou++;
					double qbad_i=Double.parseDouble(list_qbad.get(i).toString());
					double qgrad_i=CustomMaths.Round_new(
							(qbad_i - (0.0941 * dblStad + dblA * qbad_i)), 3);
					dblQnetar += CustomMaths.Round_new(((qgrad_i - 0.206 * dblHad)
							* (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), 3);
				}
				if(cou==0) dblQnetar=0;
				else dblQnetar=CustomMaths.Round_new(dblQnetar*1.0/cou, 3);
//				dblQnetar = CustomMaths.Round_new(((dblQgrad - 0.206 * dblHad)
//						* (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), 3);
				dblAd = CustomMaths.Round_new((dblAad * 100 / (100 - dblMad)),
						2);
				dblAar = CustomMaths.Round_new(
						(dblAad * (100 - dblMt) / (100 - dblMad)), 2);
				dblFcad = CustomMaths.Round_new(
						(100 - dblAad - dblVad - dblMad), 2);
			}
			if ((100 - dblMad - dblAad) == 0) {
				dblVdaf = 0;
			} else {
				dblVdaf = CustomMaths.Round_new(
						(dblVad * 100 / (100 - dblMad - dblAad)), 2);
			}
			dblQgrdaf = CustomMaths.Round_new(
					(100 / (100 - dblMad - dblAad) * dblQgrad), 3);
			dblQgrd = CustomMaths.Round_new(dblQgrad * 100 / (100 - dblMad), 3);
			dblHar = CustomMaths.Round_new(dblHad * (100 - dblMt)
					/ (100 - dblMad), 2);
			SQL = new StringBuffer("");
			SQL.append("update rulmzlb\n" + "              set qnet_ar = "
					+ dblQnetar + ",\n" + "                  aar = " + dblAar
					+ ",\n" + "                  ad = " + dblAd + ",\n"
					+ "                  vdaf = " + dblVdaf + ",\n"
					+ "                  mt = " + dblMt + ",\n"
					+ "                  stad = " + dblStad + ",\n"
					+ "                  aad = " + dblAad + ",\n"
					+ "                  mad = " + dblMad + ",\n"
					+ "                  qbad = " + dblQbad + ",\n"
					+ "                  had = " + dblHad + ",\n"
					+ "                  vad = " + dblVad + ",\n"
					+ "                  fcad = " + dblFcad + ",\n"
					+ "                  std = " + dblStd + ",\n"
					+ "                  har = " + dblHar + ",\n"
					+ "                  qgrd = " + dblQgrd + ",\n"
					+ "                  qgrad = " + dblQgrad + ",\n"
					+ "                  hdaf = " + dblHdaf + ",\n"
					+ "                  qgrad_daf = " + dblQgrdaf + ",\n"
					+ "                  sdaf = " + dblSdaf + ",\n"
					+ "                  shenhzt = 0 \n" + " where id = "
					+ rulmzlbid);
			con.getUpdate(SQL.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	
/*
 * 作者:tzf
 * 时间:2009-4-15
 * 修改内容:给strl，strq提供默认值，以免系统设置和多用户访问时，有可能造成的问题
 */
	

	public void getSelectData() {
		
		 String strl = "Stad";//提供默认值

		 String strq = "Had";//提供默认值
		
		 boolean xiansztq = true;

		 boolean xiansztl = true;

		 String chr1 = "";

		 String chr2 = "";
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		是否为阳城发电,如果是阳城电厂启用小键盘上、下、左、右
		boolean isYc = false;
		isYc = "是".equals(MainGlobal.getXitxx_item("化验", "是否为阳城发电",
				visit.getDiancxxb_id() + "", "否"));
		
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
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strl = rsl.getString("zhi");
		}
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '入炉化验氢'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strq = rsl.getString("zhi");
		}
		if (xiansztq) {
			chr1 = "to_char("+strq + ",90.99) "+strq+",";
		}
		if (xiansztl) {
			chr2 = "to_char("+strl + ",90.99) "+strl+",";
		}
		
		rsl.close();
		
		double dblH = 0.0D;	//系统日期的前一个月的入厂化验氢
		double dblS = 0.0D;	//系统日期的前一个月的入厂化验硫
		
		visit.setString11(strl);
		visit.setString12(strq);
		
//		sql = "select nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.had) / sum(f.jingz), 4)),\n"
//				+ "           0) as had,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.hdaf) / sum(f.jingz), 4)),\n"
//				+ "           0) as hdaf,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.std) / sum(f.jingz), 4)),\n"
//				+ "           0) as std,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.stad) / sum(f.jingz), 4)),\n"
//				+ "           0) as stad,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.sdaf) / sum(f.jingz), 4)),\n"
//				+ "           0) as sdaf\n"
//				+ "  from fahb f, zhilb z, caiyb c\n"
//				+ " where f.zhilb_id = z.id\n"
//				+ "   and c.zhilb_id = f.zhilb_id\n"
//				+ "   and c.zhilb_id = z.id\n"
//				+ "   and f.diancxxb_id = "
//				+ visit.getDiancxxb_id()
//				+ "\n"
//				+ "   and to_char(z.huaysj, 'yyyy-mm') =\n"
//				+ "       to_char(add_months(to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'), -1),\n"
//				+ "               'yyyy-mm')";
//		氢、硫取入炉元素分析值
		sql="select y.zhi as zhib, r.zhi\n" +
			"  from rulysfxb r,\n" + 
			"       (select id, zhi\n" + 
			"          from xitxxb\n" + 
			"         where leib = '入炉'\n" + 
			"           and (mingc = '入炉化验氢' or mingc = '入炉化验硫')\n" + 
			"           and zhuangt = 1) y\n" + 
			" where r.diancxxb_id = "+visit.getDiancxxb_id()+"\n" + 
			"   and to_char(r.riq, 'yyyy') = '"+riqTiaoj.substring(0,4)+"'\n" + 
			"   and r.rulysfxxm_id = y.id order by r.id";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			
			if(rs.getString("zhib").equals(strq)){
				
				dblH = rs.getDouble("zhi");
			}
			
			if(rs.getString("zhib").equals(strl)){
				
				dblS = rs.getDouble("zhi");
			}
		}
		rs.close();
		
		visit.setDouble1(dblS);		//硫
		visit.setDouble2(dblH);		//氢
		
		//电厂Tree刷新条件
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
			}else{
				diancxxb_id = "and d.id = " + this.getTreeid() + "\n";
			}
		}
		/*
		 * 2009-5-29
		 * 王总兵
		 * 修改入炉化验中化验热值的前后排列顺序
		 * 
		 */
		ResultSetList rs_shunx = con.getResultSetList("select zhi from xitxxb x where x.mingc='入厂入炉化验值录入显示顺序' and x.zhuangt=1");
		String xianssx="";
		if(rs_shunx.next()){
			xianssx=rs_shunx.getString("ZHI");

		}else{
			xianssx=
					        " to_char(r.mt,90.99) mt," +
							"to_char(r.mad,90.99) mad,"+
							"to_char(r.aad,90.99) aad," +
							"to_char(r.vad,90.99) vad," +
									chr2+
									chr1+
							"to_char(r.qbad,90.99) qbad,\n";

		}
		
		
		String chaxun = "select r.id,r.diancxxb_id,r.rulrq,r.fenxrq ,rb.mingc as rulbzb_id,j.mingc as " +
				"jizfzb_id,m.fadhy+m.gongrhy+m.qity+m.feiscy meil ,\n"
				+ xianssx
				//yuss 2012-3-22  默认显示登录人的名字
				+"'"+visit.getRenymc()+"' as huayy,r.beiz\n" 
				//+ "       r.huayy,r.beiz\n"
				+ "  from rulmzlb r, diancxxb d, rulbzb rb, jizfzb j,meihyb m\n"
				+ " where r.diancxxb_id = d.id(+)\n"
				+ "   and r.rulbzb_id = rb.id(+)\n"
				+ "   and r.jizfzb_id = j.id(+) and r.id=m.rulmzlb_id\n"
				+ "   and r.rulrq = to_date('"
				+ riqTiaoj
				+ "','yyyy-mm-dd')\n"
				+ "   and (r.shenhzt = 0 or r.shenhzt = 2)\n"
//				+ "   and d.id="
				+ diancxxb_id + " order by r.fenxrq,r.rulrq,rb.xuh,j.xuh";
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
        egu.getColumn("meil").setHeader("煤量(吨)");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("qbad").setHeader("Qb,ad(MJ/kg)");
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("beiz").setHeader("备注");
		if (xiansztq) {
			egu.getColumn(strq).setHeader(strq + "(%)");
			egu.getColumn(strq).setWidth(60);
			egu.getColumn(strq).setDefaultValue(String.valueOf(dblH));
			if(isYc){
				egu.getColumn(strq).editor.setListeners(getStr(0));
			}
		}
		if (xiansztl) {
			egu.getColumn(strl).setHeader(strl + "(%)");
			egu.getColumn(strl).setWidth(60);
			egu.getColumn(strl).setDefaultValue(String.valueOf(dblS));
			if(isYc){
				egu.getColumn(strl).editor.setListeners(getStr(0));
			}
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
		egu.getColumn("huayy").setWidth(60);
		
		if(isYc){
			egu.getColumn("mt").editor.setListeners(getStr(1));
			egu.getColumn("mad").editor.setListeners(getStr(0));
			egu.getColumn("aad").editor.setListeners(getStr(0));
			egu.getColumn("vad").editor.setListeners(getStr(0));
			egu.getColumn("qbad").editor.setListeners(getStr(0));
			egu.getColumn("huayy").editor.setListeners(getStr(0));
			egu.getColumn("beiz").editor.setListeners(getStr(2));
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		// *****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
		egu.getColumn("fenxrq")
				.setDefaultValue(DateUtil.FormatDate(new Date()));

		// 设置下拉框入炉班组
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		// 一厂多制选总厂时应该刷出所有分厂
		String rulbzb_idSql = "select r.id, r.mingc from rulbzb r, diancxxb dc where r.diancxxb_id = dc.id and (dc.id = "
				+ getTreeid() + " or dc.fuid = " + getTreeid() + ") order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		// 设置下拉框入炉机组
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and (d.id="
				+ getTreeid() + " or d.fuid="+ getTreeid() + ")";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));

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

		egu.addTbarText("-");

		
		//增加真正的保存按钮,数据可以先保存后提交
		sql=" select * from xitxxb where mingc='入炉煤化验显示保存按钮' and leib='入炉' and zhuangt=1 and zhi='是' ";
		ResultSetList  rs1=con.getResultSetList(sql);
		if(rs1.next()){
		
			egu.addToolbarButton(GridButton.ButtonType_Save, "TrueSaveButton");
		}
		
		
    	sql=" select * from xitxxb where mingc='入炉化验导入提交按钮显示' and leib='入炉' and zhuangt=1 and zhi='是' and diancxxb_id="+visit.getDiancxxb_id();
 	   rs1=con.getResultSetList(sql);
 		if(rs1.next()){//马头热电用
 			egu.addToolbarButton("提交",GridButton.ButtonType_SaveAll, "DaorTjButton",null, SysConstant.Btn_Icon_Show);
 			egu.addToolbarButton("保存",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
 		}else{
 			egu.addToolbarButton("提交",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
 		}

        egu.addToolbarButton("刷新",GridButton.ButtonType_Refresh, "RefreshButton",null, SysConstant.Btn_Icon_Search);
//		添加导入按钮
		sql=" select * from xitxxb where mingc='入炉煤化验添加导入按钮' and leib='入炉' and zhuangt=1 and zhi='是' ";
		rs1=con.getResultSetList(sql);
		if(rs1.next()){
			GridButton daor=new GridButton("导入","function(){ Ext.MessageBox.confirm('提示信息','确定要覆盖掉该时间的数据吗?',function(btn){if(btn=='yes'){document.all.DaorButton.click();} });}");
			egu.addTbarBtn(daor);
		}
		
//		是否添加多行替换功能
		
		sql=" select * from xitxxb where mingc='入炉化验多行替换显示' and zhi='是' and leib='入炉' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		rs1=con.getResultSetList(sql);
		if(rs1.next()){
			Checkbox cbselectlike=new Checkbox();
			
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
			
			egu.addTbarText("多行替换");
			
			egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
					
					" if(SelectLike.checked) { \n" +
					
					"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
					"var rec=gridDiv_ds.getAt(i);\n" +
					" rec.set(e.field+'',e.value);\n" +
					"}\n" +
					
					"}\n" +
					
					"" +
					"  }); ");
		}
		
		if(isYc){
			egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;col=icol;});");//得到行、列
		}

		ResultSetList rsl3 = con.getResultSetList("select * from xitxxb where mingc='青铝质量录入是否提示' and zhi='是' and leib='化验' and zhuangt=1 ");
		if(rsl3.next()){
			StringBuffer sb = new StringBuffer();
			sb.append("gridDiv_grid.on('afteredit',function(e){\n");
			sb.append("if(e.field=='QBAD'){if(e.record.get('QBAD')>21||e.record.get('QBAD')<14.5){Ext.MessageBox.alert('提示信息','请确认弹筒热值是否正确！');}}\n");
			sb.append("if(e.field=='MAD'){if(e.record.get('MAD')>7.5||e.record.get('MAD')<0.6){Ext.MessageBox.alert('提示信息','请确认水份是否正确！');}}\n");
			sb.append("if(e.field=='MT'){if(e.record.get('MT')>18||e.record.get('MT')<3.5){Ext.MessageBox.alert('提示信息','请确认全水份是否正确！');}}\n");
			sb.append("if(e.field=='AAD'){if(e.record.get('AAD')>43||e.record.get('AAD')<15){Ext.MessageBox.alert('提示信息','请确认灰份是否正确！');}}\n");
			sb.append("if(e.field=='VAD'){if(e.record.get('VAD')>25||e.record.get('VAD')<16){Ext.MessageBox.alert('提示信息','请确认挥发份是否正确！');}}\n");
			sb.append("if(e.field=='STD'){if(e.record.get('STD')>3||e.record.get('STD')<0.5){Ext.MessageBox.alert('提示信息','请确认硫分是否正确！');}}\n");
			sb.append("if(e.field=='HAD'){if(e.record.get('HAD')>3||e.record.get('HAD')<2){Ext.MessageBox.alert('提示信息','请确认氢值是否正确！');}}\n");
			sb.append("});");
			egu.addOtherScript(sb.toString());
		}
		setExtGrid(egu);
		rs1.close();
		con.Close();
	}
	
	//方向键控制光标监听JS
	public String getStr(int col){
		String Str = "";
////		 硫是否可编辑
//		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
//				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" +
				"			if(row>0){\n" +
				"				if(e.getKey()==e.UP){\n" +
				"					gridDiv_grid.startEditing(row-1, col);\n" +
				"					row = row-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(row+1 < gridDiv_grid.getStore().getCount()){\n" +
				"				if(e.getKey()==e.DOWN){\n" +
				"					gridDiv_grid.startEditing(row+1, col);\n" +
				"					row = row+1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.LEFT){\n" +
				"				if("+col+"!=1){\n" +
				"					gridDiv_grid.startEditing(row, col-1);\n" +
				"					col = col-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.RIGHT){\n" +
				"				if("+col+"!=2){\n" +
				"					gridDiv_grid.startEditing(row, col+1);\n" +
				"					col = col+1;\n" +
				"				}\n" +
				"			}\n" +
				"	 	 }\n";	
		return Str;
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
			visit.setDouble1(0);
			visit.setDouble2(0);
			setRiqi(null);
		}

		getSelectData();

	}

	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='入炉' and mingc ='入炉化验录入默认日期' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
			
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