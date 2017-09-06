package com.zhiren.dc.gdxw.jianj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 作者:wzb
 * 日期:2010-6-10 9:21:24
 * 修改内容:修改采样编号生成的方法,厂内由500吨一制样改为每天都制样
 * 
 */


/*
 * 作者:tzf
 * 时间:2009-12-28
 * 修改内容:更改弹出框提示信息的内容
 */
/*
 * 作者:tzf
 * 时间:2009-12-27
 * 修改内容:增加锁定行 和颜色标记操作
 */
/*
 * 作者：王磊
 * 时间：2009-09-23
 * 描述:选择分样桶的时候排除(3天内)制样的采样桶号
 */
/*
 * 作者：王磊
 * 时间：2009-09-22 
 * 描述：增加煤管员编辑框 alter table chepbtmp add meigy varchar2(30) null
 */

/*
 * 作者：王总兵
 * 时间：2009-09-26
 * 描述：gdxw_cy增加生成时间字段 alter table gdxw_cy add shengcrq date null
 */
/*
 * 作者：王总兵
 * 时间：2009-10-16
 * 描述：gdxw_cy增加车型字段,0是大车,1是小车,大小车的判断标准是净重是否大于45吨
 *  alter table gdxw_cy add chex number(1) null;
 */
/**
 * @author Rock
 * @since 2009.09.14
 * @version 1.0
 * @discription 国电宣威汽车衡过衡
 */
public class gdxw_qcjj_hp extends BasePage implements PageValidateListener {
	private double mk_kdl_sdz=0;//煤矿扣吨率设定值
	private double mk_kdl_sjz=0;//煤矿扣吨率实际值
	private final static String Customkey = "gdxw_qcjj_hp";
	private  boolean IsUpdateChelPiz=false;//是否更新了车辆信息表的皮重信息
	// 界面用户提示
	private String msg = "";
	private double chelxxb_cheph_piz=0;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

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
//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
//	拒收
	private boolean _JusChick = false;

	public void JusButton(IRequestCycle cycle) {
		_JusChick = true;
	}




	
	//煤矿区分大小车进行采样,以净重40吨为界限,净重大于40吨为大车,小于40吨为小车,只判断大小车,不判断每500吨做为一个样.
	//也就是说一个矿今天来了不管多少吨,只判断车型,不判断煤量多少.
	private int countCaiy_new(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='汽车500吨采样是否区分大小车' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//是否区分大小车采样
		boolean Bool_dac=false;  //是否是大车
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500吨分样是否区分大小车
			if(Dx_zhi.equals("是")){
				Bool_Qufdxc=true;
				String chex="";
				//如果maoz-piz-koud>=Dx_jingz ,是大车,否则是小车,(Dx_jingz是45吨),大车chex=0,小车chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 1 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')  "+chex+""; 
			}else{
//				判断采样表中有没有未结束的采样信息(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 1 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') ";
			}
		}
		
		
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
//			如果采样表中有记录说明该车还可用此编号直接更新相关信息即可
			id = rs.getString("id");
			double old_maoz = rs.getDouble("maoz");
			double old_piz = rs.getDouble("piz");
			double old_koud = rs.getDouble("koud");
			double new_maoz = CustomMaths.add(old_maoz, maoz);
			double new_piz = CustomMaths.add(old_piz, piz);
			double new_koud = CustomMaths.add(old_koud, koud);
			double new_jingz = CustomMaths.sub(new_maoz, new_piz);
			double new_jingz_koud=CustomMaths.sub(new_jingz, new_koud);
			long zhilb_id=rs.getLong("zhilb_id");
			sql = "update gdxw_cy set maoz = " + new_maoz +
			", piz = " + new_piz + 
			", koud = " + new_koud + 
			", jingz = " + new_jingz_koud +
			",zhiyrq=sysdate"+
			" where id = " + rs.getString("id");

			flag = con.getUpdate(sql);
		
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新gdxw_cy表失败!");
				setMsg(this.getClass().getName() + ":更新gdxw_cy表失败!");
				return flag;
			}
			
//			更新chepbtmp中的zhilb_id
			sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
			flag=con.getUpdate(sql1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新更新chepbtmp中的zhilb_id表失败!");
				setMsg(this.getClass().getName() + ":更新更新chepbtmp中的zhilb_id失败!");
				return flag;
			}
			
		}else{
//			如果为新增采样
			id = MainGlobal.getNewID(diancxxb_id);
//			新增采样表、质量表
			long zhilb_id = Jilcz.getZhilbid(con, null, new Date(), visit.getDiancxxb_id());
//			生成采样、制样、化验编号
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id());
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n构造采样失败!");
				setMsg(this.getClass().getName() + ":构造采样失败!");
				return flag;
			}
			
			
//			查找未使用的存样位置(最近两天未使用过的)
			
			/*sql = "select * from cunywzb where id in\n" +
				"(select id from cunywzb where zhuangt = 1\n" + 
				"minus\n" + 
				"select cunywzb_id from caiyb where zhilb_id in\n" + 
				"(select zhilb_id from gdxw_cy where zhuangt = 1 \n" +
				"and (zhiyrq <= sysdate and zhiyrq>= sysdate -1))\n" + 
				") and rownum = 1";*/
			
			sql = "select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where zhuangt = 1 \n" +
			"and shengcrq>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')))\n" + 
			"and rownum = 1";
			
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				更新采样表中存样位置
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//              更新chepbtmp中的zhilb_id
				 sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n存样位置已满,请添加存样位置!");
				setMsg(this.getClass().getName() + ":存样位置已满,请添加存样位置!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//是否区分大小车采样
			if(Bool_Qufdxc){
				//如果是大车,chex=0,否认如果是小车chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,1,sysdate)";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,sysdate)";
			}
			
			
			
			
			
			
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n更新gdxw_cy信息失败!");
				setMsg(this.getClass().getName() + ":更新gdxw_cy信息失败!");
				return flag;
			}
		}
		rs.close();
		return flag;
	}

	private boolean _SavePizChick = false;

	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}

	//判断皮重是否超过系统所设定的范围,只判断下限,不判断上限,例如chelxxb中皮重是20吨,只判断低于20吨的下线是否超过3%,实际重量高于20不判断
	private boolean QSuodzt(String cheph,double piz) {
		boolean issuod = false;
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		long xitwcl=0;
		String sql = "";
		sql="select x.zhi from xitxxb x where x.mingc='汽车回皮皮重误差范围' and zhuangt=1 ";
		 rs = con.getResultSetList(sql); 
		if(rs.next()){
			xitwcl=rs.getLong("zhi");
		}
		sql="select decode(c.piz,0,0,100-Round("+piz+"/c.piz,4)*100) as wucha,c.piz from chelxxb c where c.cheph='"+cheph+"'";
		
		 rs = con.getResultSetList(sql); 
		if (rs.next()) {
				double zhi=Double.parseDouble(rs.getString("wucha"));
				
				if(zhi>xitwcl){
					chelxxb_cheph_piz=rs.getDouble("piz");
					issuod=true;
				}
		}

		rs.close();
		con.Close();

		return issuod;
	}
	//向锁车状态表插入异常过衡数据
	private void InsertSuocztb(String chepbtmpb_id,String cheph,double maoz,double piz){
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		Visit visit = (Visit) this.getPage().getVisit();
		long chelxxb_id=0;
		int flag=0;
		String chelxxbSql="select id from chelxxb where cheph='"+cheph+"'";
		 rs = con.getResultSetList(chelxxbSql); 
		 if(rs.next()){
			  chelxxb_id=rs.getLong("id");
		 }
		 
		 //判断是否重复插入suocztb表
		 String Ischongf="select * from suocztb s where s.chepbtmp_id="+chepbtmpb_id+"";
		 rs = con.getResultSetList(Ischongf); 
		 if(!rs.next()){
			 //向suocztb表插入数据
			 String newid = MainGlobal.getNewID(visit.getDiancxxb_id());
			 String InserSC="insert into suocztb (id,chelxxb_id,suocsj,suocry,suocyy,zt,chepbtmp_id,maoz,piz) values (" +
			 		""+newid+","+chelxxb_id+",sysdate,'"+visit.getRenymc()+"','皮重超过误差范围',1,"+chepbtmpb_id+","+maoz+","+piz+"" +
			 		") ";
			 flag = con.getInsert(InserSC);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
					"\n更新suocztb信息失败!");
					setMsg(this.getClass().getName() + ":更新suocztb信息失败!");
					
				}
		 }
		 
		
		con.Close();	
	}
	
	
	//判断管理员是否已经解锁
	private boolean ISJieSuo(String chepbtmp_id ){
		boolean isjies=true;
		ResultSetList rs=null;
		JDBCcon con = new JDBCcon();
		long zt=0;
		String sql="select s.zt from suocztb s where s.chepbtmp_id="+chepbtmp_id+"";
		 rs = con.getResultSetList(sql); 
		if(rs.next()){
			zt=rs.getLong("zt");
			if(zt==2){
				isjies=false;
			}
		}
		con.Close();
		return isjies;
		
	}
	
	private boolean IsChongfgh(String id) {
		boolean ischongfgh = true;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select  c.qingcsj  from chepbtmp c where c.id=");
		sql.append(id).append("");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("qingcsj")==null) {
					ischongfgh = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ischongfgh;
	}
	
	private void SavePiz() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Qincjjlr.SavePiz 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
		int flag = 0;
		String cheph ="";
		String meikdwmc="";
		double maoz = 0.0;
		double piz =  0.0;
		double biaoz = 0.0;
		double koud =  0.0;
		double jingz =  0.0;
		if (rsl.next()) {
			String id=rsl.getString("id");
			 cheph = rsl.getString("cheph");
			double piz_1=Double.parseDouble(rsl.getString("piz"));
			double maoz_1=Double.parseDouble(rsl.getString("maoz"));
			
			//判断是否重复过衡,当这个车在其它衡器上已经过衡,在这个衡器上因为没刷新,操作员又选中该车进行过衡操作
			if(IsChongfgh(rsl.getString("id"))){
				setMsg("选择车号错误,该车号不存在,请刷新!");
				return;
			}
			
			
			//皮重是否超过误差范围
			if (QSuodzt(cheph,piz_1)) {
				
				if(ISJieSuo(id)){//判断管理员是否已经对suocztb解锁
					
//					向suocztb插入数据
					InsertSuocztb(id,cheph,maoz_1,piz_1);
					setMsg("该车当前皮重:"+piz_1+"与系统皮重:"+chelxxb_cheph_piz+"相差超过3%,禁止回皮,请通知管理员!");
					return;
				}
				
			}
			
			
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("dcmc").combo)
						.getBeanId(rsl.getString("dcmc"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			meikdwmc = rsl.getString("meikdwmc");
			
			maoz = rsl.getDouble("maoz");
			piz = rsl.getDouble("piz");
			biaoz = 0.0;
			koud = rsl.getDouble("koud");
			jingz =CustomMaths.sub(CustomMaths.sub(maoz,piz),koud);
			biaoz = maoz - piz - koud;
			String sql = "update chepbtmp set piz =" + piz + ", biaoz = " + biaoz +
			", koud = " + koud + ",  qingchh = '" +
			rsl.getString("qingchh") + "', qingcsj = sysdate, qingcjjy = '" +
			rsl.getString("qingcjjy") + "', meicb_id = " + 
			idm.getBeanId(rsl.getString("meicb_id")) + ",meigy = '" + 
			rsl.getString("meigy") + "',pinz='原煤',faz='汽',daoz='宣威煤场',jihkj='市场采购',fahrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd')," +
			" daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'), chebb_id=3,xiecfs='自卸' where id = " + id;
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("更新车皮表TMP失败!");
				setMsg("更新车皮表TMP失败!");
				return;
			}
			flag = countCaiy(con, diancxxb_id, meikdwmc, maoz, piz, koud,id);
			//flag = countCaiy_new(con, diancxxb_id, meikdwmc, maoz, piz, koud,id);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				return;
			}
			// 更新车辆皮重信息
			SaveChelxx(con,diancxxb_id,cheph,piz);
			
			
			
		}
		con.commit();
//		con.Close();
//		setMsg("保存成功！车号:"+cheph+",煤矿:"+meikdwmc+",毛重:"+maoz+",皮重:"+piz+",净重:"+jingz+",扣吨:"+koud+"");
		if(IsUpdateChelPiz){
			IsUpdateChelPiz=false;
			setMsg("皮重保存成功<br>"+cheph+"为第一次来煤，皮重："+piz+"已经成功保存至车辆信息中!");
		}else{
			setMsg("皮重保存成功");
		}
		//判断当天该煤矿的扣吨率是否超过系统设定值
		boolean meikkdlsd=IsKoudl(con,visit.getDiancxxb_id(),meikdwmc,koud);
		if(meikkdlsd){
			//扣吨率超过系统设定值,锁定该煤矿,禁止其再过重衡
			Insert_Meikkdlsdb(con,visit.getDiancxxb_id(),meikdwmc);
		}
		
		con.Close();
		
	}

	public void Insert_Meikkdlsdb(JDBCcon con,long diancxxb_id,String meikdwmc){
			
				//锁定该矿的名称,禁止其过重衡,已经过完重衡的,允许其回皮, 锁定数据存在meikkdlsdb表中
				String TodayIsHaveSuod="select * from meikkdlsdb m where m.meikdwmc='"+meikdwmc+"' and zhuangt=1 and m.suodrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd')";
				ResultSetList rsl=con.getResultSetList(TodayIsHaveSuod);
				if(rsl.next()){
					// 如果当天已经锁定了,后续回皮车辆不再进行锁定.
				}else{
					  String suodsql="insert into meikkdlsdb (id,meikxxb_id,meikdwmc,suodsj,suodr,suodrq,zhuangt) values (" +
		    			""+MainGlobal.getNewID(diancxxb_id)+"," +
		    			"(select max(id) from meikxxb where mingc='"+meikdwmc+"')," +
		    			"'"+meikdwmc+"'," +
		    			"sysdate," +
		    			"'"+((Visit) getPage().getVisit()).getRenymc()+"'," +
		    			"to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'),1" +
		    			") ";
				  con.getUpdate(suodsql);
				  this.setMsg("煤矿:----"+meikdwmc+"----,今日累计扣吨率为"+mk_kdl_sjz+",已经超过系统设定值"+mk_kdl_sdz+";<br>-----------系统已禁止该煤矿继续过重衡!------------");
				}
				
				
			
	
	}
	
	
	//判断煤矿扣吨率是否超标,煤矿扣吨率设置在meikkdlsz表中
	public boolean IsKoudl(JDBCcon con,long diancxxb_id,String meikdwmc,double koud){
		
		boolean fanhuizhi=false;
		String koudlSql="select koudl as koudl from meikkdlsz s where s.meikmc='全部'";
		ResultSetList rsl=con.getResultSetList(koudlSql);
		if(rsl.next()){// 如果有"全部",就按照全部的设定值来判断扣吨率
			double quanbu_koudl=rsl.getDouble("koudl");
		    String mk_koudl="select koudl as koudl2 from meikkdlsz s where s.meikmc='"+meikdwmc+"' order by s.koudl desc ";
		    rsl=con.getResultSetList(mk_koudl);
		    if(rsl.next()){//如果既有全部,又有煤矿名称,则按照煤矿设定的扣吨率来计算扣吨率.
		    	quanbu_koudl=rsl.getDouble("koudl2");
		    }
		    mk_kdl_sdz=quanbu_koudl;//给全局变量赋值,为弹出窗口取值做准备
		    //sql判断当天该煤矿的扣吨率现在是多少
		    String pandkoudl="select decode(sum(c.maoz-c.piz-c.koud),0,0,nvl(round(sum(c.koud)*100 / sum(c.maoz - c.piz - c.koud),2), 0)) as koudl3" +
		    		        " from chepbtmp c where c.daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd') and c.meikdwmc='"+meikdwmc+"'";
		    rsl=con.getResultSetList(pandkoudl);
		    double mk_today_koud=0;
		    if(rsl.next()){
		    	mk_today_koud=rsl.getDouble("koudl3");
		    }
		    mk_kdl_sjz=mk_today_koud;//全局变量,扣吨率实际值,为弹出窗口取值做准备
		    String suodsql="";
		    if(mk_today_koud>quanbu_koudl){//判断煤矿当前的扣吨率与系统设定的扣吨率的大小
		    	fanhuizhi=true;
		    }
		}
		
		return fanhuizhi;
	}

	

	//如果车辆信息表该车皮重为0，更新该车的皮重信息
	public void SaveChelxx(JDBCcon con,long diancxxb_id,String cheph,double piz){
		String ChelSql="select piz from chelxxb where cheph='"+cheph+"'";
		ResultSetList rsl=con.getResultSetList(ChelSql);
		if(rsl.next()){
			double oldpiz=rsl.getDouble("piz");
			if(oldpiz==0){
				IsUpdateChelPiz=true;//记录是否更新车辆信息表的皮重字段
				String UpdateChelxxib="update chelxxb set piz="+piz+" where cheph='"+cheph+"'";
				con.getUpdate(UpdateChelxxib);
			}
		}
	}

	

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_JusChick) {
			_JusChick = false;
			Jus();
			init();
		}
	}

	private void Jus() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改哦！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
	
		int flag = 0;
		String sql="update chepbtmp set isjus=1 ,jussj=sysdate,jusry='"+visit.getRenymc()+"' where id="+getChange()+"";
		flag = con.getUpdate(sql);
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新车皮表TMP失败!");
			setMsg(this.getClass().getName() + ":更新车皮表TMP失败!");
			return;
		}
			
		
		con.commit();
		con.Close();
		setMsg("拒收成功");
	}


	public void setChepid(String fahids) {
		((Visit) this.getPage().getVisit()).setString1(fahids);
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sqldc = "and d.id = " + visit.getDiancxxb_id();
		String dcmcdef = visit.getDiancmc();
		String dcsql = "select id,mingc from diancxxb where fuid="
			+ visit.getDiancxxb_id();
		ResultSetList rsl;
//		如果是一厂多制
		if(visit.isFencb()){
			sqldc = "and d.fuid = " + visit.getDiancxxb_id();
//			取得默认电厂名称
			rsl = con.getResultSetList(dcsql);
			if(rsl.next()){
				dcmcdef = rsl.getString("mingc");
			}
			rsl.close();
		}
		
		
/*  皮重grid初始化  */
		
		String sql = "select c.id, c.cheph,c.meikdwmc,  c.maoz, c.piz,\n" +
			" cl.piz as lspz,c.koud, '' meicb_id, meigy,c.zhongcjjy , " +
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj," +
			"nvl('"+visit.getRenymc()+"','') qingcjjy," +
			" c.qingchh  from chepbtmp c, chelxxb cl\n" + 
			"where c.zhongcsj is not null " +
			"and c.qingcsj is null  " +
			"  and c.cheph=cl.cheph(+)"+
			"and c.isjus=0 order by zhongcsj";
		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		设置页面可编辑
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid进行分页
		egu1.addPaging(100);
//		设置列名
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("lspz").setHeader("历史皮重");
		egu1.getColumn("lspz").setHidden(true);
		egu1.getColumn("koud").setHeader(Locale.koud_chepb);
		egu1.getColumn("meicb_id").setHeader("存放位置");
		egu1.getColumn("lursj").setCenterHeader("采样时间");
		egu1.getColumn("zhongcsj").setCenterHeader("重车时间");
		egu1.getColumn("meigy").setHeader("验煤员");
		egu1.getColumn("qingcjjy").setHeader("空衡员");
		egu1.getColumn("qingchh").setHeader("衡号");
		egu1.getColumn("zhongcjjy").setHeader("重衡员");
	
		
//		设置列宽
		egu1.getColumn("meikdwmc").setWidth(150);
		egu1.getColumn("cheph").setWidth(110);
		egu1.getColumn("piz").setWidth(70);
		egu1.getColumn("koud").setWidth(70);
		egu1.getColumn("meigy").setWidth(80);
		egu1.getColumn("zhongcjjy").setWidth(80);
		egu1.getColumn("qingcjjy").setWidth(80);
		egu1.getColumn("qingchh").setWidth(70);
		egu1.getColumn("meicb_id").setWidth(135);
		egu1.getColumn("maoz").setWidth(70);
		egu1.getColumn("lursj").setWidth(200);
		egu1.getColumn("zhongcsj").setWidth(200);
//		设置上下限
		sql = "select * from shuzhlfwb where leib ='数量' and mingc = '汽车衡皮重' and diancxxb_id = "
			+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
//		设置列是否可编辑
		egu1.getColumn("meikdwmc").setEditor(null);
		egu1.getColumn("lursj").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		egu1.getColumn("cheph").setEditor(null);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("lspz").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("qingchh").setEditor(null);
		egu1.getColumn("qingcjjy").setHidden(true);
//		设置煤场下拉框
		ComboBox cmc= new ComboBox();
		egu1.getColumn("meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(mcSql));
//      验煤员对应验煤章下拉框
		ComboBox ymz= new ComboBox();
		egu1.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='验煤员' order by r.zhiw";
		egu1.getColumn("meigy").setComboEditor(egu1.gridId,new IDropDownModel(ymy));
		

		// 输入车号可以查到模糊对应的信息。-----------------------------------------------------------
		egu1.addTbarText("输入车号：");
		TextField theKey = new TextField();
		theKey.setId("theKey");
		theKey.setWidth(110);
		theKey.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13) {chaxun();}}\n");
		egu1.addToolbarItem(theKey.getScript());
		// 这是ext中的第二个egu，其中带有gridDiv字样的变量都比第一个多Piz字样，gridDiv----gridDivPiz.
		GridButton chazhao = new GridButton("(模糊)查找",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu1.addTbarBtn(chazhao);
		egu1.addTbarText("-");

		String otherscript = "var sta=''; function chaxun(){\n"
				+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"
				+ "       var len=gridDivPiz_data.length;\n"
				+ "       var count;\n"
				+ "       if(len%"
				+ egu1.getPagSize()
				+ "!=0){\n"
				+ "        count=parseInt(len/"
				+ egu1.getPagSize()
				+ ")+1;\n"
				+ "        }else{\n"
				+ "          count=len/"
				+ egu1.getPagSize()
				+ ";\n"
				+ "        }\n"
				+ "        for(var i=0;i<count;i++){\n"
				+ "           gridDivPiz_ds.load({params:{start:i*"
				+ egu1.getPagSize()
				+ ", limit:"
				+ egu1.getPagSize()
				+ "}});\n"
				+ "           var rec=gridDivPiz_ds.getRange();\n "
				+ "           for(var j=0;j<rec.length;j++){\n "
				+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
				+ "                 var nw=[rec[j]]\n"
				+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
				+ "                      gridDivPiz_sm.selectRecords(nw);\n"
				+ "                      sta+=rec[j].get('ID').toString()+';';\n"
				+ "                       return;\n" + "                  }\n"
				+ "                \n" + "               }\n"
				+ "           }\n" + "        }\n" + "        if(sta==''){\n"
				+ "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"
				+ "        }else{\n" + "           sta='';\n"
				+ "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"
				+ "         }\n" + "}\n";

		egu1.addOtherScript(otherscript);
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);
		egu1.addTbarText("-");
		
		GridButton gbs_pz = new GridButton("保存皮重",GridButton.ButtonType_Save,
				"gridDivPiz", egu1.getGridColumns(), "SavePizButton");
		egu1.addTbarBtn(gbs_pz);
		egu1.addOtherScript("function  gridDivPiz_save(rec){if(confirm('是否保存 车号: '+rec.get('CHEPH') + '  煤矿: ' + rec.get('MEIKDWMC'))){return \"\";}else{return \"return\";}};");
		egu1.addTbarText("-");
		
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDivPiz_grid.on('beforeedit',function(e){ "
				+"if(e.row!=row_maoz_index){ e.cancel=true; } \n"
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		

		egu1.addTbarText("->");// 设置分隔符
		
		
		egu1.addTbarText("历史皮重：");
		TextField Lispiz = new TextField();
		Lispiz.setId("ls");
		Lispiz.setWidth(80);
		egu1.addToolbarItem(Lispiz.getScript());
		egu1.addTbarText("-");// 设置分隔符
		
		
//		拒收
		egu1.addTbarText("-");// 设置分隔符
		String sPwHandler = "function(){"
			+"if(gridDivPiz_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('提示信息','请选中车号进行拒收');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDivPiz_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('提示信息','拒收车号:&nbsp;&nbsp;&nbsp;'+ grid_rcd.get('CHEPH')+'&nbsp;&nbsp;&nbsp;煤矿:&nbsp;&nbsp;&nbsp;'+grid_rcd.get('MEIKDWMC')+'&nbsp;&nbsp;&nbsp;确认吗?&nbsp;&nbsp;&nbsp;',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('ID');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('JusButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		egu1.addTbarBtn(new GridButton("拒收",sPwHandler));
		egu1.addTbarText("-");// 设置分隔符
		
		
		egu1.addOtherScript(" gridDivPiz_grid.on('cellclick',function(grid,rowIndex,columnIndex,e){ \n " +
				" if(columnIndex==2){ \n" +
				" row_maoz_index=rowIndex;\n"+
				" theKey.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('CHEPH'));\n"+
				" ls.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('LSPZ'));\n"+
				" sta='';\n"+
				" gridDivPiz_grid.getView().refresh();\n"+
				" gridDivPiz_grid.getView().getRow(rowIndex).style.backgroundColor=\"red\";} \n"+
				" else { \n" +
//				" gridDiv_grid.getView().focusRow(row_maoz_index); \n" +
				" } \n"+
				" }); \n");
		
		egu1.addOtherScript(" gridDivPiz_grid.addListener('afteredit',function(e){\n " +
				" gridDivPiz_grid.getView().getRow(e.row).style.backgroundColor=\"red\"; \n" +
				"} ); \n");
		
		setPizGrid(egu1);
		
		
		
		
		
/*  皮重保存完成grid初始化  */
		
//		设置车头号默认值
		 sql = "select rownum as xuh,a.* from (\n"+
			"select c.id, d.mingc dcmc,c.cheph, c.meikdwmc, c.maoz  ,c.piz ,(c.maoz-c.piz-c.koud) as jingz," +
			"c.koud, c.meigy,c.zhongcjjy,c.qingcjjy,mc.mingc as meic, "+
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi') as qingcsj,\n" + 
			"to_char(c.lursj,'yyyy-mm-dd hh24:mi') as lursj \n"+
			"from chepbtmp c, diancxxb d, yunsfsb y, meicb mc\n" + 
			"where c.diancxxb_id = d.id \n" + 
			"and c.yunsfs = y.mingc\n" + 
			"and c.meicb_id=mc.id\n" + 
			"and to_char(c.qingcsj,'yyyy-mm-dd')='"+this.getRiq()+"'\n" + 
			"and y.id = " + SysConstant.YUNSFS_QIY + "\n" +
			sqldc + "\n" +
			"order by c.qingcsj  )a order by xuh desc";
		rsl = con.getResultSetList(sql);
//		如果没有取到默认数据
		if(rsl == null){
			
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,Customkey);
//		设置GRID不可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置该grid不进行分页
		egu.addPaging(0);
//		设置列名
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("dcmc").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("jingz").setHeader(Locale.jingz_chepb);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("meic").setHeader("存放位置");
		egu.getColumn("meigy").setHeader("验煤员");
		egu.getColumn("lursj").setHeader("采样时间");
		egu.getColumn("zhongcsj").setHeader("过重时间");
		egu.getColumn("qingcsj").setHeader("过空时间");
		egu.getColumn("zhongcjjy").setHeader("重衡员");
		egu.getColumn("qingcjjy").setHeader("空衡员");
//		设置列宽
		egu.getColumn("xuh").setWidth(55);
		egu.getColumn("dcmc").setWidth(70);
		egu.getColumn("meikdwmc").setWidth(160);
		egu.getColumn("cheph").setWidth(110);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("koud").setWidth(70);
		egu.getColumn("meic").setWidth(130);
		egu.getColumn("meigy").setWidth(80);
		egu.getColumn("lursj").setWidth(200);
		egu.getColumn("zhongcsj").setWidth(200);
		egu.getColumn("qingcsj").setWidth(200);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setWidth(80);
//		设置列是否可编辑
		egu.getColumn("cheph").setEditor(null);


//		设置分厂别电厂下拉框
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("dcmc").setEditor(dc);
			dc.setEditable(true);
			egu.getColumn("dcmc").setComboEditor(egu.gridId,
					new IDropDownModel(dcsql));
		} else {
			egu.getColumn("dcmc").setHidden(true);
			egu.getColumn("dcmc").setEditor(null);
		}
		int caiy=0;
		int zhongc=0;
		int kongc=0;
		double jingz=0.0;
		int jus=0;
		//采样车数
		sql="select count(*) as caiy from chepbtmp c\n" +
			"where c.lursj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.lursj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			caiy=rsl.getInt("caiy");
		}
		//过重磅车数
		sql="select count(*) as zhongc from chepbtmp c\n" +
			"where c.zhongcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.zhongcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.maoz>0\n" + 
			"and c.zhongcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			zhongc=rsl.getInt("zhongc");
		}
		//回皮车数,净重
		sql="select count(*) as huip,sum(c.maoz-c.piz-c.koud) as jingz from chepbtmp c\n" +
			"where c.qingcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.qingcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.piz>0\n" + 
			"and c.qingcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			kongc=rsl.getInt("huip");
			jingz=rsl.getDouble("jingz");
		}
//		拒收车数
		sql="select count(*) as jus from chepbtmp c\n" +
		"where c.jussj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" +
		" and c.jussj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
		" and c.isjus=1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			jus=rsl.getInt("jus");
		}
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		egu.addTbarText("-");
		GridButton refurbish1 = new GridButton("刷新",
		"function (){document.getElementById('RefurbishButton').click();}");
		refurbish1.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish1);
		egu.addTbarText("-");
		egu.addTbarText("&nbsp;&nbsp;" +
				""+this.getRiq()+":&nbsp;采样:"+caiy+"车," +
				"&nbsp;过重:"+zhongc+" 车,&nbsp;" +
				"过空:"+kongc+" 车,&nbsp;拒收:"+jus+"车,&nbsp;今日来煤:"+jingz+"吨");
		
		egu.addOtherScript(" if(gridDiv_ds.getCount()>=1){ gridDiv_grid.getView().getRow(0).style.backgroundColor=\"red\"; }\n");
		setExtGrid(egu);
		
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridScriptPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public String getGridPizHtml() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
	}

	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}

	public String getTreeScript() {
		if(getDefaultTree()==null){
			return "";
		}
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	private void setJianjdmodel() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='汽车衡检斤单模式' and zhuangt = 1 and diancxxb_id ="
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if (rsl.next()) {
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}
	//每500吨制成一个样生成采样编号的模式,同时也判断车型,以净重40吨为基准,大于40吨是大车,小于40吨是小车.
	private int countCaiy(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='汽车500吨采样是否区分大小车' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//是否区分大小车采样
		boolean Bool_dac=false;  //是否是大车
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500吨分样是否区分大小车
			if(Dx_zhi.equals("是")){
				Bool_Qufdxc=true;
				String chex="";
				//如果maoz-piz-koud>=Dx_jingz ,是大车,否则是小车,(Dx_jingz是45吨),大车chex=0,小车chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')  "+chex+""; 
			}else{
//				判断采样表中有没有未结束的采样信息(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')";
			}
		}
		
		long meikxxb_id=0;
		String GetMeikxxb_id_Sql="select nvl(max(id),0) as id from meikxxb where mingc='"+meikdwmc+"'";
		ResultSetList rs_meikxxb_id = con.getResultSetList(GetMeikxxb_id_Sql);
		if(rs_meikxxb_id.next()){
			meikxxb_id=rs_meikxxb_id.getLong("id");
		}
		rs_meikxxb_id.close();
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
//			如果采样表中有记录说明该车还可用此编号直接更新相关信息即可
			id = rs.getString("id");
			double old_maoz = rs.getDouble("maoz");
			double old_piz = rs.getDouble("piz");
			double old_koud = rs.getDouble("koud");
			double new_maoz = CustomMaths.add(old_maoz, maoz);
			double new_piz = CustomMaths.add(old_piz, piz);
			double new_koud = CustomMaths.add(old_koud, koud);
			double new_jingz = CustomMaths.sub(new_maoz, new_piz);
			double new_jingz_koud=CustomMaths.sub(new_jingz, new_koud);
			long zhilb_id=rs.getLong("zhilb_id");
			sql = "update gdxw_cy set maoz = " + new_maoz +
			", piz = " + new_piz + 
			", koud = " + new_koud + 
			", jingz = " + new_jingz_koud +
			" where id = " + rs.getString("id");

			flag = con.getUpdate(sql);
		
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新gdxw_cy表失败!");
				setMsg(this.getClass().getName() + ":更新gdxw_cy表失败!");
				return flag;
			}
			
//			更新chepbtmp中的zhilb_id
			sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
			flag=con.getUpdate(sql1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"更新更新chepbtmp中的zhilb_id表失败!");
				setMsg(this.getClass().getName() + ":更新更新chepbtmp中的zhilb_id失败!");
				return flag;
			}
			
		}else{
//			如果为新增采样
			id = MainGlobal.getNewID(diancxxb_id);
//			新增采样表、质量表
			long zhilb_id = Jilcz.getZhilbid(con, null, new Date(), visit.getDiancxxb_id());
//			生成采样、制样、化验编号
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id(),meikxxb_id,2,2,1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n构造采样失败!");
				setMsg(this.getClass().getName() + ":构造采样失败!");
				return flag;
			}
			
			
//			查找未使用的存样位置
			
			sql = /*"select * from cunywzb where id in\n" +
				"(select id from cunywzb where zhuangt = 1\n" + 
				"minus\n" + 
				"select cunywzb_id from caiyb where zhilb_id in\n" + 
				"(select zhilb_id from gdxw_cy where zhuangt = 0 \n" +
				"or (zhiyrq <= sysdate and zhiyrq>= sysdate -3))\n" + 
				") and rownum = 1";
			*/
			
			"select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where  \n" +
			" shengcrq>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')))\n" + 
			"and rownum = 1";
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				更新采样表中存样位置
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//              更新chepbtmp中的zhilb_id
				 sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n存样位置已满,请添加存样位置!");
				setMsg(this.getClass().getName() + ":存样位置已满,请添加存样位置!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//是否区分大小车采样
			if(Bool_Qufdxc){
				//如果是大车,chex=0,否认如果是小车chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,1,sysdate)";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
			}
			
			
			
			
			
			
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n更新gdxw_cy信息失败!");
				setMsg(this.getClass().getName() + ":更新gdxw_cy信息失败!");
				return flag;
			}
		}
		rs.close();
		sql = "select zhi,danw from xitxxb where  mingc ='采样按数量分组' and zhuangt=1 and leib= '数量'";
		 rs = con.getResultSetList(sql);
		double Fenzl = 0;
		String Fenzd = "";
		if(rs.next()){
			Fenzl =rs.getDouble("ZHI");
			Fenzd = rs.getString("DANW");
		}
		//rscy.close();
		sql = "select " + Fenzd + " from gdxw_cy where id = " + id;
		rs = con.getResultSetList(sql);
		if(rs.next()){
			if(rs.getDouble(0) > Fenzl){
				sql = "update gdxw_cy set zhuangt = 1, zhiyrq = sysdate where id = " + id;
				con.getUpdate(sql);
			}
		}
		rs.close();
		return flag;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1(null);
			init();
		}
	}

	private void init() {
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}