package com.zhiren.dc.chephhd;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Radio;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.jt.jiesgl.changfhs.Balancebean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.SysConstant;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;
/*作者:王总兵
 * 日期:2009-7-14 
 * 修改录入矿方化验值的弹出框,把名称不规范的都规范化,例如MT改为Mt
 * 
 * 
 * 
 */
public class Danchhd extends BasePage implements PageValidateListener {
	
	private String msg="";
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		super.initialize();
		msg = "";
	}
	
	public double getQnet_ar_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble1();
	}
	
	public void setQnet_ar_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble1(value);
	}
	
	public double getAar_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble2();
	}
	
	public void setAar_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble2(value);
	}
	
	public double getAd_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble3();
	}
	
	public void setAd_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble3(value);
	}
	
	public double getVdaf_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble4();
	}
	
	public void setVdaf_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble4(value);
	}
	
	public double getMt_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble5();
	}
	
	public void setMt_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble5(value);
	}
	
	public double getStad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble6();
	}
	
	public void setStad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble6(value);
	}
	
	public double getAad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble7();
	}
	
	public void setAad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble7(value);
	}
	
	public double getMad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble8();
	}
	
	public void setMad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble8(value);
	}
	
	public double getQbad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble9();
	}
	
	public void setQbad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble9(value);
	}
	
	public double getHad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble10();
	}
	
	public void setHad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble10(value);
	}
	
	public double getVad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble11();
	}
	
	public void setVad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble11(value);
	}
	
	public double getFcad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble12();
	}
	
	public void setFcad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble12(value);
	}
	
	public double getStd_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble13();
	}
	
	public void setStd_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble13(value);
	}
	
	public double getQgrad_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble14();
	}
	
	public void setQgrad_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble14(value);
	}
	
	public double getHdaf_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble15();
	}
	
	public void setHdaf_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble15(value);
	}
	
	public double getQgrad_daf_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble16();
	}
	
	public void setQgrad_daf_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble16(value);
	}
	
	public double getSdaf_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble17();
	}
	
	public void setSdaf_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble17(value);
	}
	
	public double getT1_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble18();
	}
	
	public void setT1_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble18(value);
	}
	
	public double getT2_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble19();
	}
	
	public void setT2_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble19(value);
	}
	
	public double getT3_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble20();
	}
	
	public void setT3_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble20(value);
	}
	
	public double getT4_value(){
		
		return ((Visit) this.getPage().getVisit()).getDouble21();
	}
	
	public void setT4_value(double value){
		
		((Visit) this.getPage().getVisit()).setDouble21(value);
	}
	
	public String getLury(){
		
		return ((Visit) this.getPage().getVisit()).getString5();
	}
	
	public void setLury(String value){
		
		((Visit) this.getPage().getVisit()).setString5(value);
	}

	// 页面变化记录
	public String getChange() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setChange(String change) {
		((Visit) this.getPage().getVisit()).setString1(change);
	}
	
	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setYansbh(String change) {
		((Visit) this.getPage().getVisit()).setString4(change);
	}
	
//	矿方化验编号
	public String getKuangfhybh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setKuangfhybh(String value) {
		((Visit) this.getPage().getVisit()).setString2(value);
	}
	
	public String getKuangfhyxx() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	
	public void setKuangfhyxx(String value) {
		((Visit) this.getPage().getVisit()).setString3(value);
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		
		_DeleteChick = true;
	}
	
	private boolean _DeleteKzChick = false;

	public void DeleteKzButton(IRequestCycle cycle) {
		
		_DeleteKzChick = true;
	}
	
	
	private boolean _UpdataChick = false;

	public void UpdataButton(IRequestCycle cycle) {
		
		_UpdataChick = true;
	}
	
	private boolean _ReturChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		
		_ReturChick = true;
	}
	
	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		
		_QuedChick = true;
	}
	
	private boolean _ShenhChick = false;

	public void ShenhButton(IRequestCycle cycle) {
		
		_ShenhChick = true;
	}

	public void submit(IRequestCycle cycle) {
		
		if (_DeleteChick) {
			_DeleteChick = false;
//			删除已核对为结算的车皮
			Deleteyhd();		
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		if(_QuedChick){
			_QuedChick=false;
//			矿方质量录入
			SaveKuangfzl();
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		if (_ReturChick) {
			_ReturChick = false;
//			返回车号核对选择发货界面
			cycle.activate("Chephhd");
		}

		if (_UpdataChick) {
			_UpdataChick = false;
//			保存的修改的单车记录
			UpdateChep();
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		if (_DeleteKzChick) {
			_DeleteKzChick = false;
//			删除矿方质量记录
			Deletekz();
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		if(_ShenhChick){
			_ShenhChick=false;
			Chehhd();
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		intie();
	}
	
	private void intie() {
		// TODO 自动生成方法存根
		this.setKuangfhybh("");
		this.setQnet_ar_value(0);
		this.setAar_value(0);
		this.setAd_value(0);
		this.setVdaf_value(0);
		this.setMt_value(0);
		this.setStad_value(0);
		this.setAad_value(0);
		this.setMad_value(0);
		this.setQbad_value(0);
		this.setHad_value(0);
		this.setVad_value(0);
		this.setFcad_value(0);
		this.setStd_value(0);
		this.setQgrad_value(0);
		this.setHdaf_value(0);
		this.setQgrad_daf_value(0);
		this.setSdaf_value(0);
		this.setT1_value(0);
		this.setT2_value(0);
		this.setT3_value(0);
		this.setT4_value(0);
		this.setLury("");
	}
	
	private void Deletekz() {
		// TODO 自动生成方法存根
//		删除车皮上的矿方质量
		Visit visit = (Visit) this.getPage().getVisit();
		this.DeleteKuangfzl(getChange(), visit);
		
	}
	private void DeleteKuangfzl(String strchange, Visit visit) {
		// TODO 自动生成方法存根
//		解除kuangfzlzb与chepb之间的关系
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid_Yihd();
		String tableName2="chepb";
		StringBuffer sbChepb_id=new StringBuffer("");
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while(mdrsl.next()){

			sbChepb_id.append(mdrsl.getString("ID")).append(",");
		}
		
		sql.append("update ").append(tableName2).append(" set kuangfzlzb_id=0 ").append("where ");
		sql.append(" id in (").append(sbChepb_id.deleteCharAt(sbChepb_id.length()-1)).append(");		\n");
			
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
		Jiesdcz jc=new Jiesdcz();
		jc.ReCountFahbKfzl(sbChepb_id.deleteCharAt(sbChepb_id.length()-1).toString(), visit.getDiancxxb_id());
	}
	
	private void SaveKuangfzl() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.SaveKz(getChange(), visit);
	}
	
	private void Chehhd() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.SaveHd(getChange(), visit);
	}
	
	public void SaveHd(String strchange, Visit visit){
		
		JDBCcon con = new JDBCcon();
		String tableName="yansbhb";
		String strYansbhb_Id="";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql_chepbid=new StringBuffer();

		ResultSetList mdrsl = this.getExtGrid_Weihd().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			
			sql_chepbid.append(mdrsl.getString("ID")).append(",");
		}
		sql_chepbid.deleteCharAt(sql_chepbid.length()-1);
		
			StringBuffer sql2 = new StringBuffer();
//			visit.getString4();		验收编号
//			插入yansbhb记录
			try {
				strYansbhb_Id=String.valueOf(MainGlobal.getTableId(tableName, "bianm", this.getYansbh()));
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			
			if(strYansbhb_Id.equals("0")){
				
				strYansbhb_Id=MainGlobal.getNewID(visit.getDiancxxb_id());
				
				sql.append("insert into ").append(tableName).append("(id, bianm, liucgzb_id, liucztbid");
				
				sql2.append(strYansbhb_Id).append(",'").append(getYansbh()).append("',").append("1,1");
				
				sql.append(") values(").append(sql2).append(");\n");
				
				sql.append(" update chepb set yansbhb_id=").append(strYansbhb_Id).append(" where id in (").append(sql_chepbid.toString()).append(");	\n");
				
			}else{
				
//					更新chepb中的yansbhb_id
				sql.append(" update chepb set yansbhb_id=").append(strYansbhb_Id).append(" where id in (").append(sql_chepbid.toString()).append(");	\n");
			}
			
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("核对成功！");
		}else{
			
			this.setMsg("核对失败！");
		}
		con.Close();
	}
	
	public void SaveKz(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		String tableName="kuangfzlzb";
		String strKuangfzlzb_Id="";
		String strKzzb_id="";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql2 = new StringBuffer("");
		StringBuffer sql_chepbid=new StringBuffer();

		ResultSetList mdrsl = this.getExtGrid_Weihd().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			
			if(strKzzb_id.equals("")){
				
				strKzzb_id=mdrsl.getString("KZZB_ID");
			}
//			记录所选车皮id
			sql_chepbid.append(mdrsl.getString("ID")).append(",");
		}	
		sql_chepbid.deleteCharAt(sql_chepbid.length()-1);
		
//			当该车皮kuangfzlzb_id=0则，向kuangfzlzb中插入一条记录
			if(strKzzb_id.equals("0")){
				
				sql.append("insert into ").append(tableName).append("(id, bianm, qnet_ar, aar, ad, vdaf, mt, stad, aad, mad, qbad, had, vad, fcad, std, qgrad, hdaf, qgrad_daf, sdaf, var, t1, t2, t3, t4, leib, lury");
				
				strKuangfzlzb_Id=MainGlobal.getNewID(visit.getDiancxxb_id());
				
					sql2.append(strKuangfzlzb_Id).append(",'").append(this.getKuangfhybh());
//					Qnetar,aar,ad
					sql2.append("',").append(visit.getDouble1()).append(",").append(visit.getDouble2()).append(",").append(visit.getDouble3());
//					vdaf,mt,stad
					sql2.append(",").append(visit.getDouble4()).append(",").append(visit.getDouble5()).append(",").append(visit.getDouble6());
//					Aad,Mad,Qbad
					sql2.append(",").append(visit.getDouble7()).append(",").append(visit.getDouble8()).append(",").append(visit.getDouble9());
//					had, vad, fcad
					sql2.append(",").append(visit.getDouble10()).append(",").append(visit.getDouble11()).append(",").append(visit.getDouble12());
//					std, qgrad, hdaf
					sql2.append(",").append(visit.getDouble13()).append(",").append(visit.getDouble14()).append(",").append(visit.getDouble15());
//					qgrad_daf, sdaf, var
					sql2.append(",").append(visit.getDouble16()).append(",").append(visit.getDouble17()).append(",").append("0");
//					t1, t2, t3, t4
					sql2.append(",").append(visit.getDouble18()).append(",").append(visit.getDouble19()).append(",").append(visit.getDouble20()).append(",").append(visit.getDouble21());
//					leib, lury
					sql2.append(",").append("0").append(",'").append(this.getLury()).append("'");
				
				sql.append(") values(").append(sql2).append(");		\n");
				
				sql.append(" update chepb set kuangfzlzb_id=").append(strKuangfzlzb_Id).append(" where id in (").append(sql_chepbid.toString()).append(");	\n");
			}else{
				
				sql.append(" update ").append(tableName).append(" set bianm='").append(this.getKuangfhybh()).append("'");
//				Qnetar,aar,ad
				sql.append(",").append(" qnet_ar=").append(this.getQnet_ar_value()).append(",").append(" aar=").append(this.getAar_value()).append(",").append(" ad=").append(this.getAd_value()).append("");
//				vdaf,mt,stad
				sql.append(",").append(" vdaf=").append(this.getVdaf_value()).append(",").append(" mt=").append(this.getMt_value()).append(",").append(" stad=").append(this.getStad_value()).append("");
//				Aad,Mad,Qbad
				sql.append(",").append(" aad=").append(this.getAad_value()).append(",").append(" mad=").append(this.getMad_value()).append(",").append(" qbad=").append(this.getQbad_value()).append("");
//				had, vad, fcad
				sql.append(",").append(" had=").append(this.getHad_value()).append(",").append(" vad=").append(this.getVad_value()).append(",").append(" fcad=").append(this.getFcad_value()).append("");
//				std, qgrad, hdaf
				sql.append(",").append(" std=").append(this.getStd_value()).append(",").append(" qgrad=").append(this.getQgrad_value()).append(",").append(" hdaf=").append(this.getHdaf_value()).append("");
//				qgrad_daf, sdaf, var
				sql.append(",").append(" qgrad_daf=").append(this.getQgrad_daf_value()).append(",").append(" sdaf=").append(this.getSdaf_value()).append(",").append(" var=").append("0").append("");
//				t1, t2, t3, t4
				sql.append(",").append(" t1=").append(this.getT1_value()).append(",").append(" t2=").append(this.getT2_value()).append(",").append(" t3=").append(this.getT3_value()).append(",").append(" t4=").append(this.getT4_value()).append("");
//				leib, lury
				sql.append(",").append(" leib=").append("0").append(",").append(" lury='").append(this.getLury()).append("'");
				
				sql.append(" where id=").append(strKzzb_id).append(";	\n");
			
//				更新chepb的kuangfzlzb_id
//				sql2.append(" update chepb set kuangfzlzb_id=").append(strKzzb_id).append(" where id in (").append(sql_chepbid.toString()).append(");	\n");
			}
				
//			sql.append(sql2);
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("矿方数据保存成功！");
		}else{
			
			this.setMsg("矿方数据保存失败！");
		}
		con.Close();
		Jiesdcz jc=new Jiesdcz();
		jc.ReCountFahbKfzl(sql_chepbid.toString(), visit.getDiancxxb_id());
	}
	
	private void Deleteyhd() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.DeleteYihdcp(getChange(), visit);
	}
	
	public void DeleteYihdcp(String strchange,Visit visit) {
		
//		将已核对车皮变为未核对车皮
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid_Yihd();
		String tableName="chepb";
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sbChepb_id = new StringBuffer("");
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while(mdrsl.next()) {
			
			sbChepb_id.append(mdrsl.getString("ID")).append(",");
		}	
		
		sql.append("update ").append(tableName).append(" set ").append("yansbhb_id=0 ");
		sql.append("where id in (").append(sbChepb_id.deleteCharAt(sbChepb_id.length()-1)).append(");	\n");
		
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	private void UpdateChep() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save(getChange(), visit);
		if(checkbh()&&!this.getYansbh().equals("")){
			this.Saveysbh(getChange(), visit);
		}
	}
	
	public void Save(String strchange,Visit visit) {
		
//		保存修改的车皮
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=getExtGrid_Weihd();
		String tableName="chepb";
		Jiesdcz jdcz=new Jiesdcz();
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = egu.getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while(mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			
			sql.append("update ").append(tableName).append(" set ");
			for(int i=1;i<mdrsl.getColumnCount();i++) {
				
				if(mdrsl.getColumnNames()[i].equals("CHEPH")||mdrsl.getColumnNames()[i].equals("BIAOZ")){
					
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					sql.append(egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
				}else if(mdrsl.getColumnNames()[i].equals("CHEB")){
					
					sql.append("CHEBB_ID").append(" = ");
					sql.append(MainGlobal.getProperId(getIChebModel(),egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)).replace('\'', ' ').trim())).append(",");
				}
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			
//			将修改前车皮的信息记入"Chepsjtzb"表中
			jdcz.UpdateChepsjtzb(visit.getDiancxxb_id(),Long.parseLong(mdrsl.getString("ID")),visit.getRenymc());
			
//			重算chepb盈亏、运损
			Jilcz.CountChepbYuns(con, mdrsl.getString("ID"), SysConstant.HEDBZ_YSH);
//			重算fahb盈亏、运损
			try {
				Jilcz.updateFahb(con, MainGlobal.getTableCol("chepb", "fahb_id", "id", mdrsl.getString("ID")));
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		sql.append("end;");
		
		jdcz.ReCountFahbKfzl(mdrsl.getString("ID"), visit.getDiancxxb_id());
		
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("车皮修改成功！");
		}else{
			
			this.setMsg("车皮修改不成功！");
		}
		con.Close();
	}
	
	private boolean checkbh() {
        // TODO 自动生成方法存根
	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        sql=" select bianm from yansbhb  where bianm="+getYansbh();
	        ResultSet rs=con.getResultSet(sql);
	        if(rs.next()){
	            return false;
	        }
	        
	        rs.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	        con.Close();
	    }
        return true;
    }
	
	
	public void Saveysbh(String strchange,Visit visit) {
		String tableName="yansbhb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1(). getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl =visit.getExtGrid1(). getModifyResultSet(strchange);
		
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			StringBuffer sql4 = new StringBuffer();
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			//if(checkbh()) {
				sql.append("insert into ").append(tableName).append("(id,bianm");
				sql3.append(",LIUCGZB_ID");
				sql4.append(",'0'");
				sql3.append(",LIUCZTBID");
				sql4.append(",'0'");

				for(int i=1;i<mdrsl.getColumnCount();i++) {
					
					if(!mdrsl.getColumnNames()[i].equals("YANSBH")){
						continue;
					}

					sql2.append(",").append(getYansbh());
				}
				sql.append(sql3).append(sql5).append(") values(").append(sql2).append(sql4).append(sql6).append(");\n");
			
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	public String getYansbhb_id() {
		String yansbhb_id = "";
		JDBCcon cn = new JDBCcon();
		String sql= "select id from yansbhb where bianm="
				+ getYansbh();
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				yansbhb_id = rs.getString("id");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return yansbhb_id;

	}
	
	
//未核对
	public void getWeihdSelectData() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		
		sql=" select nvl(cp.id, 0) as id,f.id as fahb_id,cp.kuangfzlzb_id as kzzb_id,					\n"
			+ " decode(gys.mingc, '', '合计', gys.mingc) as fahdw,m.mingc as meikdwmc,cz.mingc as faz,	\n"
			+ " daoz.mingc as daoz,to_char(f.fahrq, 'yyyy-MM-dd') as fahrq,to_char(f.daohrq, 'yyyy-MM-dd') as daohrq,	\n"
			+ " cp.cheph as cheph,decode(gys.mingc, null, 0, sum(cp.biaoz)) as biaoz,	\n"
			+ " decode(cp.chebb_id, 1, '路车', 2, '自备车', 3, '汽车', 4, '船') as cheb 	\n" 
	        + " from fahb f,gongysb gys,meikxxb m,chezxxb cz,zhilb z,chepb cp,chezxxb daoz 		\n" 
	                 
	        + " where f.id = cp.fahb_id and f.gongysb_id = gys.id and f.meikxxb_id = m.id  	\n"
	        + "      and cp.hedbz = 3 and f.faz_id = cz.id and daoz.id = f.daoz_id 			\n"
	        + "      and f.zhilb_id = z.id(+) 												\n" 
	        + "      and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and cp.yansbhb_id=0  	\n"
	               
	        + " group by cp.id,f.id,cp.kuangfzlzb_id,gys.mingc,m.mingc,cz.mingc,daoz.mingc,f.fahrq,f.daohrq,cp.cheph,	\n"
	        + "       cp.chebb_id	\n"  
	        + " 	order by gys.mingc, m.mingc, cz.mingc, cp.cheph ";
		
		ResultSetList rsl=con.getResultSetList(sql);//未核对
		ExtGridUtil egu = new ExtGridUtil("gridDiv_weihd", rsl);//设定记录集对应的表单
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("kzzb_id").setHidden(true);
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("kzzb_id").setEditor(null);
		
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("daoz").setHeader("到站");
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("cheb").setHeader("车别");
		
		egu.getColumn("faz").setWidth(75);
		egu.getColumn("daoz").setWidth(75);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("cheb").setWidth(70);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
//		录入员赋值
		this.setLury((((Visit) getPage().getVisit()).getRenymc()));
		
//		验收编号初始化_Begin
		
		egu.addTbarText("-");
		egu.addTbarText("车皮号");
		TextField tfcph=new TextField();
		tfcph.setId("T_CHEPH");
		tfcph.setWidth(80);
		egu.addToolbarItem(tfcph.getScript());
		
		egu.addTbarText("票重");
		TextField tfbz=new TextField();
		tfbz.setId("T_BIAOZ");
		tfbz.setWidth(80);
		egu.addToolbarItem(tfbz.getScript());
		
		egu.addTbarText("车别");
		ComboBox ChebSelect = new ComboBox();
		ChebSelect.setId("Cheb");
		ChebSelect.setWidth(80);
		ChebSelect.setLazyRender(true);
		ChebSelect.setTransform("ChebSelect");
		egu.addToolbarItem(ChebSelect.getScript());
		
		
//		TextField tyansbh=new TextField();
//		tyansbh.setId("YANSBH");
//		tyansbh.setValue(getYansbh());
//		tyansbh.setReadOnly(true);
//		tyansbh.setWidth(80);
//		egu.addToolbarItem(tyansbh.getScript());
        
//		车号搜索
		egu.addTbarText("-");
		egu.addTbarText("车号搜索");
		TextField tfsearchcph=new TextField();
		tfsearchcph.setId("T_SEARCH_CHEPH");
		tfsearchcph.setWidth(90);
		tfsearchcph.setListeners("specialkey:Searchcheph");
		egu.addToolbarItem(tfsearchcph.getScript());
		
		if(((Visit) getPage().getVisit()).getString4().equals("")){
			
			this.setYansbh(MainGlobal.getYansbh());
		}
		
//		验收编号
		egu.addTbarText("-");
		egu.addTbarText("验收编号:");
		egu.addTbarText(getYansbh());
		
		egu.addTbarText("-");
		egu.addToolbarButton("确定修改",GridButton.ButtonType_SubmitSel,"UpdataButton");
		
		egu.addTbarText("-");
		egu.addToolbarButton("确认核对",GridButton.ButtonType_SubmitSel, "ShenhButton");
			
		egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("返回","function(){ document.getElementById('ReturnButton').click();" +
				"}").getScript()+"}");
		
//		实现车号修改、票重修改、车别修改
		egu.addOtherScript("T_CHEPH.on('change',function(){	\n" 
				+ "	\tvar i=-1;\n"
				+ "	\ti=document.getElementById('EditTableRow').value;	\n"
				+ "	\tif(i>-1&&T_CHEPH.getValue()!=''){	\n"
				+ " \t	gridDiv_weihd_ds.getAt(i).set('CHEPH',T_CHEPH.getValue());}})	\n"
				
				+ "	\tT_BIAOZ.on('change',function(){	\n"
				+ "	\tvar i=-1;	\n"
				+ "	\ti=document.getElementById('EditTableRow').value;	\n"
				+ "	\tif(i>-1&&T_BIAOZ.getValue()!=''){	\n"
				+ "		gridDiv_weihd_ds.getAt(i).set('BIAOZ',T_BIAOZ.getValue());}})	\n"
				
				+ "	\tCheb.on('select',function(){	\n"
				+ "	\t	var i=-1;	\n"
				+ "	\t	i=document.getElementById('EditTableRow').value;	\n"
				+ "	\t	if(i>-1){	\n"
				+ " \t		gridDiv_weihd_ds.getAt(i).set('CHEB',Cheb.getRawValue());}})	\n"
		);
		
//		计算合计项，合计在最下方，自动选择相同货票
		egu.addOtherScript("gridDiv_weihd_grid.on('rowclick',function(own,row,e){\n \tvar rec = gridDiv_weihd_ds.getAt(row); \n"
								+ " \tvar biaoz=0,length=0,jine=0,lic=0,dcbiaoz=0; \n"
								+ " \tvar fahdw='',meikdw='',faz='',daoz='',cheb='',cheph='',strtmp=''; \n"
								+ " \tfahdw=rec.get('FAHDW');\n"
								+ " \tmeikdw=rec.get('MEIKDWMC');\n"
								+ " \tfaz=rec.get('FAZ');\n"
								+ "	\tdaoz=rec.get('DAOZ');\n"
								+ "	\tcheb=rec.get('CHEB');\n"
								+ "	\tcheph=rec.get('CHEPH');\n"
								+ "	\tdcbiaoz=rec.get('BIAOZ');\n"
								+ "	\tT_SEARCH_CHEPH.setValue(cheph);\n"
								+ "	\tdocument.getElementById('EditTableRow').value=row;	\n"
								+ "	\tT_CHEPH.setValue(cheph);T_BIAOZ.setValue(dcbiaoz);Cheb.setRawValue(cheb); \n"
								+ " \tgridDiv_weihd_sm.selectRow(row,true);	\n"
								+ " \trec = gridDiv_weihd_grid.getSelectionModel().getSelections();\n"
								+ "	\tlength=rec.length;\n"
								+ "	\tfor(var i=0;i<rec.length;i++){ \n"
								+ "	\t	if(0!=rec[i].get('ID')){	\n"
								+ "	\t		biaoz+=eval(rec[i].get('BIAOZ')); \n"
								+ "	\t	}else{\n"
								+ "	\t		length--;	}}	\n"
								+ " \t	Hej_ches.setValue(length);	\n"
								+ " \t	Hej_biaoz.setValue(biaoz);	\n"
								+ "	\t	});");
		
//								T_SEARCH_CHEPH 搜索车皮
								egu.addOtherScript( "function Searchcheph(){							\n"
										+ "	if(T_SEARCH_CHEPH.getValue().trim()!=''){					\n"
										+"    	var cheph='';\n"  
										+"	 	var cheb='';\n" 
										+"	 	var biaoz='';\n" 	
										+ " \t	var i=-1;					\n"		
										+ " \t	i=gridDiv_weihd_ds.findBy(function(rec){				\n"
										+ " \t		if(rec.get('CHEPH')==T_SEARCH_CHEPH.getValue()){	\n"
										+ " 			cheph=rec.get('CHEPH');							\n" 
										+ "				cheb=rec.get('CHEB');							\n" 
										+ "				biaoz=rec.get('BIAOZ');							\n" 
										+ " \t			return true;									\n"					
										+ "	\t		}													\n"	
										+ " \t });														\n"			
										+ " \t	if(i>=0){												\n"
										+ " \t		gridDiv_weihd_sm.selectRow(i,true);					\n"
										+ "	\t		gridDiv_weihd_grid.getView().focusRow(i);//指定行    \n"
										+ "			T_BIAOZ.setValue(biaoz);							\n"
										+ "			T_CHEPH.setValue(cheph);							\n"
										+ "			Cheb.setRawValue(cheb);								\n"
										+ " \t	}else{													\n"
										+ " \t		Ext.MessageBox.alert('提示信息','没有对应的车号！');	\n"
										+ " \t	}														\n"
										+ " \t  T_SEARCH_CHEPH.focus(true,true);						\n"
										+ "	\t }"
										+ " };"
										
//										全选计算
										+ " gridDiv_weihd_grid.on('click',function(){		\n"
										+ " 	reCountToolbarNum(this);	\n"
										+ " });"
										
//						 				Tab切换后重算Toolbar合计
						 				+ " function reCountToolbarNum(obj){	\n "
						 				+ " \tvar rec;	\n"
						 				+ " \tvar length=0,biaoz=0,jine=0;	\n"
						 				+ " \trec = obj.getSelectionModel().getSelections();	\n"		
						 				+ " \tlength=rec.length;	\n"
						 				+ " \tfor(var i=0;i<rec.length;i++){	\n" 
						 				+ " 	\tif(0!=rec[i].get('ID')){	\n"	
										+ " 		\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n" 
										+ " 		\tjine+=eval(rec[i].get('JINE'));	\n"	
										+ " 	\t}else{								\n"
										+ " 		\tlength--;							\n"	
										+ " 	\t}										\n"
										+ " \t}	\n"	
										+ " \tHej_ches.setValue(length);				\n"	
										+ " \tHej_biaoz.setValue(biaoz);				\n"
										+ " \t} \n"
								);	
	
		
		
		egu.addOtherScript("function gridDiv_weihd_save(rec){" +
				"	if(rec.get('FAHDW')=='合计'){" +
				"	return 'continue';" +
				"}}");
		
		//绑定combobox完成
		setExtGrid_Weihd(egu);
		con.Close();
	}
	
	public void getYihdSelectData() {

		String sql = "";
		int rsl_rows=0;		//记录记录集行数
		JDBCcon con = new JDBCcon();
		
		sql=" select nvl(cp.id, 0) as id,f.id as fahb_id,cp.kuangfzlzb_id as kzzb_id,nvl(kzlzb.bianm,'') as kuangfhybh,			\n"
			+ " decode(gys.mingc, '', '合计', gys.mingc) as fahdw,m.mingc as meikdwmc,cz.mingc as faz,							\n"
			+ " daoz.mingc as daoz,to_char(f.fahrq, 'yyyy-MM-dd') as fahrq,to_char(f.daohrq, 'yyyy-MM-dd') as daohrq,			\n"
			+ " cp.cheph as cheph,decode(gys.mingc, null, 0, sum(cp.biaoz)) as biaoz,	\n"
			+ " decode(cp.chebb_id, 1, '路车', 2, '自备车', 3, '汽车', 4, '船') as cheb 	\n" 
	        + " from fahb f,gongysb gys,meikxxb m,chezxxb cz,zhilb z,chepb cp,chezxxb daoz,kuangfzlzb kzlzb		 				\n" 
	                 
	        + " where f.id = cp.fahb_id and f.gongysb_id = gys.id and f.meikxxb_id = m.id  	\n"
	        + "      and cp.hedbz = 3 and f.faz_id = cz.id and daoz.id = f.daoz_id 			\n"
	        + "      and f.zhilb_id = z.id(+) and cp.kuangfzlzb_id=kzlzb.id(+)													\n" 
	        + "      and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+") and cp.yansbhb_id>0	\n"
	               
	        + " group by cp.id,f.id,cp.kuangfzlzb_id,kzlzb.bianm,gys.mingc,m.mingc,cz.mingc,daoz.mingc,f.fahrq,f.daohrq,cp.cheph,			\n"
	        + "       cp.chebb_id	\n"  
	        + " 	order by gys.mingc, m.mingc, cz.mingc, kzlzb.bianm, cp.cheph ";
		
		ResultSetList rsl=con.getResultSetList(sql);//未核对
		rsl_rows=rsl.getRows();
		ExtGridUtil egu = new ExtGridUtil("gridDiv_yihd", rsl);//设定记录集对应的表单
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("kzzb_id").setHidden(true);
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("kzzb_id").setEditor(null);
		
		egu.getColumn("kuangfhybh").setHeader(Locale.kuangfhybh_title);
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("daoz").setHeader("到站");
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("cheb").setHeader("车别");
		
		egu.getColumn("kuangfhybh").setWidth(100);
		egu.getColumn("faz").setWidth(75);
		egu.getColumn("daoz").setWidth(75);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("cheb").setWidth(70);
		egu.addPaging(0);
		
		
//		根据矿方化验子表id对应生成页面数组
		StringBuffer  sbKuangfhybh_id=new StringBuffer("");
		String strKuangfhybh_id="";
		for(int i=0;i<rsl_rows;i++){
			
			if(!egu.getDataValue(i, 2).equals("")){
				
				if(!strKuangfhybh_id.equals(egu.getDataValue(i, 2))){
					
					strKuangfhybh_id=egu.getDataValue(i, 2);
					sbKuangfhybh_id.append(egu.getDataValue(i, 2)).append(",");
				}
			}
		}
		
		if(!sbKuangfhybh_id.toString().equals("")){
			
			getKuangfzlArray(sbKuangfhybh_id.deleteCharAt(sbKuangfhybh_id.length()-1).toString());
		}
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(4, new GridColumn(GridColumn.ColType_Check));
		
		
		egu.addToolbarButton("取消核对",GridButton.ButtonType_SubmitSel, "DeleteButton");
		
		if(MainGlobal.getXitxx_item("结算", Locale.kuangfzlxhdch_jies, String.valueOf(((Visit) getPage().getVisit()).getLong1()), "否").equals("是")){
		
				egu.addTbarText("-");
				StringBuffer sb=new StringBuffer();
				sb.append("	var rec;															\n");
				sb.append("	if(checkKuangfhybh()){												\n");
				sb.append("		return;															\n");
				sb.append("	}																	\n");
				sb.append("																		\n");
				sb.append("	function checkKuangfhybh(){											\n");
				sb.append("		var i;															\n");
				sb.append("		var kzzb_id='';													\n");
				sb.append("		rec=gridDiv_yihd_grid.getSelectionModel().getSelections();		\n");
				sb.append("																		\n");
				sb.append("		if(rec==undefined){												\n");
				sb.append("			Ext.MessageBox.alert('提示信息','请选择一个车号!');			\n");
				sb.append("			return true;												\n");
				sb.append("		}																\n");
				sb.append("																		\n");
				sb.append("		for(i = 0; i< rec.length; i++){									\n");
				sb.append("																		\n");
				sb.append("			if(kzzb_id==''){											\n");
				sb.append("																		\n");
				sb.append("				kzzb_id=rec[i].get('KZZB_ID');							\n");
				sb.append("			}else{														\n");
				sb.append("																		\n");
				sb.append("				if(kzzb_id!=rec[i].get('KZZB_ID')){						\n");
				sb.append("																		\n");
				sb.append("					return true;										\n");
				sb.append("				}														\n");
				sb.append("			}															\n");
				sb.append("		}																\n");
				sb.append("		for(var i=0;i<Kuangfhyxx.length;i++){							\n");
				sb.append("																		\n");
				sb.append("			if(rec[rec.length-1].get('KUANGFHYBH')==Kuangfhyxx[i][0]){	\n");
				sb.append("																		\n");
				sb.append("					document.getElementById('KUANGFHYBH_VALUE').value=Kuangfhyxx[i][0];	\n");
				sb.append("					document.getElementById('QNET_AR_VALUE').value=Kuangfhyxx[i][1];	\n");
				sb.append("					document.getElementById('AAR_VALUE').value=Kuangfhyxx[i][2];		\n");
				sb.append("					document.getElementById('AD_VALUE').value=Kuangfhyxx[i][3];			\n");
				sb.append("					document.getElementById('VDAF_VALUE').value=Kuangfhyxx[i][4];		\n");
				sb.append("					document.getElementById('MT_VALUE').value=Kuangfhyxx[i][5];			\n");
				sb.append("					document.getElementById('STAD_VALUE').value=Kuangfhyxx[i][6];		\n");
				sb.append("					document.getElementById('AAD_VALUE').value=Kuangfhyxx[i][7];		\n");
				sb.append("					document.getElementById('MAD_VALUE').value=Kuangfhyxx[i][8];		\n");
				sb.append("					document.getElementById('QBAD_VALUE').value=Kuangfhyxx[i][9];		\n");
				sb.append("					document.getElementById('HAD_VALUE').value=Kuangfhyxx[i][10];		\n");
				sb.append("					document.getElementById('VAD_VALUE').value=Kuangfhyxx[i][11];		\n");
				sb.append("					document.getElementById('FCAD_VALUE').value=Kuangfhyxx[i][12];		\n");
				sb.append("					document.getElementById('STD_VALUE').value=Kuangfhyxx[i][13];		\n");
				sb.append("					document.getElementById('QGRAD_VALUE').value=Kuangfhyxx[i][14];		\n");
				sb.append("					document.getElementById('HDAF_VALUE').value=Kuangfhyxx[i][15];		\n");
				sb.append("					document.getElementById('QGRAD_DAF_VALUE').value=Kuangfhyxx[i][16];	\n");
				sb.append("					document.getElementById('SDAF_VALUE').value=Kuangfhyxx[i][17];		\n");
				sb.append("					document.getElementById('T1_VALUE').value=Kuangfhyxx[i][18];		\n");
				sb.append("					document.getElementById('T2_VALUE').value=Kuangfhyxx[i][19];		\n");
				sb.append("					document.getElementById('T3_VALUE').value=Kuangfhyxx[i][20];		\n");
				sb.append("					document.getElementById('T4_VALUE').value=Kuangfhyxx[i][21];		\n");
				sb.append("					document.getElementById('LURY_VALUE').value=Kuangfhyxx[i][22];		\n");
				sb.append("			}															\n");
				sb.append("		}																\n");
				sb.append("																		\n");
				sb.append("		return false;													\n");
				sb.append("	}																	\n");
				sb.append("																		\n");
				sb.append("	function CheckDelete(btn){											\n");
				sb.append("																		\n");
				sb.append("		if(btn=='yes'){													\n");
				sb.append("			document.getElementById('DeleteKzButton').click();			\n");
				sb.append("		}																\n");
				sb.append("	}																	\n");
				sb.append("																		\n");
				sb.append("	function fdChange(own,nv,ov){										\n");
				sb.append("		if(own.validate()){												\n");
				sb.append("			document.getElementById(own.name).value=nv;					\n");
				sb.append("		}else{															\n");
				sb.append("			alert('数值超出合理范围，请核对!');							\n");
				sb.append("			Ext.get(own.id).focus();									\n");
				sb.append("		}																\n");
				sb.append("	}																	\n");
				
				sb.append("	function gotoNext(own,e){											\n");
				sb.append("		switch(own.name){												\n");
				sb.append("																		\n");
				sb.append("			case 'KUANGFHYBH_VALUE':									\n");
				sb.append("				Ext.get('QNET_AR').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'QNET_AR_VALUE':										\n");
				sb.append("				Ext.get('AAR').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'AAR_VALUE':											\n");
				sb.append("				Ext.get('AD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'AD_VALUE':											\n");
				sb.append("				Ext.get('VDAF').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'VDAF_VALUE':											\n");
				sb.append("				Ext.get('MT').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'MT_VALUE':											\n");
				sb.append("				Ext.get('STAD').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'STAD_VALUE':											\n");
				sb.append("				Ext.get('AAD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'AAD_VALUE':											\n");
				sb.append("				Ext.get('MAD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'MAD_VALUE':											\n");
				sb.append("				Ext.get('QBAD').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'QBAD_VALUE':											\n");
				sb.append("				Ext.get('HAD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'HAD_VALUE':											\n");
				sb.append("				Ext.get('VAD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'VAD_VALUE':											\n");
				sb.append("				Ext.get('FCAD').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'FCAD_VALUE':											\n");
				sb.append("				Ext.get('STD').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'STD_VALUE':											\n");
				sb.append("				Ext.get('QGRAD').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'QGRAD_VALUE':											\n");
				sb.append("				Ext.get('HDAF').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'HDAF_VALUE':											\n");
				sb.append("				Ext.get('QGRAD_DAF').focus();							\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'QGRAD_DAF_VALUE':										\n");
				sb.append("				Ext.get('SDAF').focus();								\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'SDAF_VALUE':											\n");
				sb.append("				Ext.get('T1').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'T1_VALUE':											\n");
				sb.append("				Ext.get('T2').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'T2_VALUE':											\n");
				sb.append("				Ext.get('T3').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'T3_VALUE':											\n");
				sb.append("				Ext.get('T4').focus();									\n");
				sb.append("				break;													\n");
				sb.append("																		\n");
				sb.append("			case 'T4_VALUE':											\n");
				sb.append("				Ext.get('LURY').focus();								\n");
				sb.append("				break;													\n");
				sb.append("		}																\n");
				sb.append("		if(!own.validate()){											\n");
				sb.append("																		\n");
				sb.append("			alert('数值超出合理范围，请核对!');							\n");
				sb.append("			Ext.get(own.id).focus();									\n");
				sb.append("		}																\n");
				sb.append("	}																	\n");
				
				sb.append("	rec=gridDiv_yihd_grid.getSelectionModel().getSelected();			\n");
				sb.append("	if(!win&&rec!=undefined){											\n");
				sb.append("		var form = new Ext.form.FormPanel({								\n");
				sb.append("			baseCls: 'x-plain',											\n");
				sb.append("			labelAlign:'center',										\n");
				sb.append("	     	autoScroll:true,											\n");
				sb.append("	     	autoWidth:true,												\n");
				sb.append("	     	items: [{													\n");
				sb.append("					    xtype:'textfield',								\n");
				sb.append("					    name:'KUANGFHYBH_VALUE',						\n");
				sb.append("					    id:'KUANGFHYBH',								\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxLength:20,									\n");
				sb.append("						value:Ext.getDom('KUANGFHYBH_VALUE').value,		\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("					    fieldLabel:'矿方化验编号',						\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("	     		   	},{													\n");
				sb.append("					    xtype:'numberfield',							\n");
				sb.append("						name:'QNET_AR_VALUE',							\n");
				sb.append("						id:'QNET_AR',									\n");
				sb.append("						labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("						fieldLabel:'Qnet_ar(Mj/kg)',					\n");
				sb.append("						value:Ext.getDom('QNET_AR_VALUE').value,		\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("				   	},{													\n");
				sb.append("					    xtype:'numberfield',							\n");
				sb.append("					    name:'AAR_VALUE',								\n");
				sb.append("					    id:'AAR',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Aar(%)',							\n");
				sb.append("						value:Ext.getDom('AAR_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("				   	},{													\n");
				sb.append("					    xtype:'numberfield',							\n");
				sb.append("					    name:'AD_VALUE',								\n");
				sb.append("					    id:'AD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Ad(%)',								\n");
				sb.append("						value:Ext.getDom('AD_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{					  								\n");
				sb.append("					    xtype:'numberfield',							\n");
				sb.append("					    name:'VDAF_VALUE',								\n");
				sb.append("					    id:'VDAF',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("						fieldLabel:'Vdaf(%)',							\n");
				sb.append("						value:Ext.getDom('VDAF_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("					    xtype:'numberfield',							\n");
				sb.append("					    name:'MT_VALUE',								\n");
				sb.append("					    id:'MT',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:4,									\n");
				sb.append("						decimalPrecision:1,								\n");
				sb.append("					    fieldLabel:'Mt(%)',								\n");
				sb.append("						value:Ext.getDom('MT_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'STAD_VALUE',								\n");
				sb.append("					    id:'STAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Stad(%)',							\n");
				sb.append("						value:Ext.getDom('STAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'AAD_VALUE',								\n");
				sb.append("					    id:'AAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Aad(%)',							\n");
				sb.append("						value:Ext.getDom('AAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'MAD_VALUE',								\n");
				sb.append("					    id:'MAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Mad(%)',							\n");
				sb.append("						value:Ext.getDom('MAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'QBAD_VALUE',								\n");
				sb.append("					    id:'QBAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Qbad(Mj/kg)',						\n");
				sb.append("						value:Ext.getDom('QBAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'HAD_VALUE',								\n");
				sb.append("					    id:'HAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Had(%)',							\n");
				sb.append("						value:Ext.getDom('HAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'VAD_VALUE',								\n");
				sb.append("					    id:'VAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Vad(%)',							\n");
				sb.append("						value:Ext.getDom('VAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'FCAD_VALUE',								\n");
				sb.append("					    id:'FCAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Fcad(%)',							\n");
				sb.append("						value:Ext.getDom('FCAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'STD_VALUE',								\n");
				sb.append("					    id:'STD',										\n");	
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Std(%)',							\n");
				sb.append("						value:Ext.getDom('STD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'QGRAD_VALUE',								\n");
				sb.append("					    id:'QGRAD',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Qgrad(Mj/kg)',						\n");
				sb.append("						value:Ext.getDom('QGRAD_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'HDAF_VALUE',								\n");
				sb.append("					    id:'HDAF',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Hdaf(%)',							\n");
				sb.append("						value:Ext.getDom('HDAF_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'QGRAD_DAF_VALUE',							\n");
				sb.append("					    id:'QGRAD_DAF',									\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Qgrad_daf(Mj/kg)',					\n");
				sb.append("						value:Ext.getDom('QGRAD_DAF_VALUE').value,		\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'SDAF_VALUE',								\n");
				sb.append("					    id:'SDAF',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'Sdaf(%)',							\n");
				sb.append("						value:Ext.getDom('SDAF_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'T1_VALUE',								\n");
				sb.append("					    id:'T1',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'T1(℃)',								\n");
				sb.append("						value:Ext.getDom('T1_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'T2_VALUE',								\n");
				sb.append("					    id:'T2',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'T2(℃)',								\n");
				sb.append("						value:Ext.getDom('T2_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'T3_VALUE',								\n");
				sb.append("					    id:'T3',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'T3(℃)',								\n");
				sb.append("						value:Ext.getDom('T3_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'numberfield',							\n");
				sb.append("					    name:'T4_VALUE',								\n");
				sb.append("					    id:'T4',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("						maxValue:99.99,									\n");
				sb.append("						minValue:0,										\n");
				sb.append("						maxLength:5,									\n");
				sb.append("						decimalPrecision:2,								\n");
				sb.append("					    fieldLabel:'T4(℃)',								\n");
				sb.append("						value:Ext.getDom('T4_VALUE').value,				\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange,							\n");
				sb.append("							specialkey:gotoNext							\n");
				sb.append("						}												\n");
				sb.append("					},{													\n");
				sb.append("						xtype:'textfield',								\n");
				sb.append("					    name:'LURY_VALUE',								\n");
				sb.append("					    id:'LURY',										\n");
				sb.append("					    labelStyle:'width:120',							\n");
				sb.append("					    disabled:true,									\n");
				sb.append("						maxLength:20,									\n");
				sb.append("						value:Ext.getDom('LURY_VALUE').value,			\n");
				sb.append("						selectOnFocus:true,								\n");
				sb.append("					    fieldLabel:'录入员',								\n");
				sb.append("						listeners :{									\n");
				sb.append("							change:fdChange								\n");
				sb.append("						}												\n");
				sb.append("					}]													\n");
				sb.append("				});														\n");
				sb.append("				win = new Ext.Window({									\n");
				sb.append("					el:'hello-win',										\n");
				sb.append("					layout:'fit',										\n");
				sb.append("					width:340,											\n");
				sb.append("					height:520,											\n");
				sb.append("					modal:true,											\n");
				sb.append("					closeAction:'hide',									\n");
				sb.append("					plain: true,										\n");
				sb.append("					title:'矿方质量',									\n");
				sb.append("					items: [form],										\n");
				sb.append("						buttons: [{										\n");
				sb.append("							text:'确定',									\n");
				sb.append("							handler:function(){							\n");
				sb.append("								win.hide();								\n");
				sb.append("								var Mrcd = gridDiv_yihd_grid.getSelectionModel().getSelections();	\n");
				sb.append("								for(i = 0; i< Mrcd.length; i++){		\n");
				sb.append("									if(typeof(gridDiv_yihd_save)=='function'){ 		\n");
				sb.append("										var revalue = gridDiv_yihd_save(Mrcd[i]); 		\n");
				sb.append("										if(revalue=='return'){ 			\n");
				sb.append("											return; 					\n");
				sb.append("										}else if(revalue=='continue'){	\n");
				sb.append("											continue;					\n");
				sb.append("										}								\n");
				sb.append("									}									\n");
				sb.append("									if(Mrcd[i].get('MEIKDWMC') == ''){Ext.MessageBox.alert('提示信息','字段 煤矿单位 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('FAZ') == ''){Ext.MessageBox.alert('提示信息','字段 发站 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('DAOZ') == ''){Ext.MessageBox.alert('提示信息','字段 到站 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('CHEPH') == ''){Ext.MessageBox.alert('提示信息','字段 车皮号 不能为空');return;	\n");
				sb.append("									}gridDiv_yihd_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<FAHB_ID update=\"true\">' 	\n");
				sb.append("									+ Mrcd[i].get('FAHB_ID')+ '</FAHB_ID>'+ '<KZZB_ID update=\"true\">' + Mrcd[i].get('KZZB_ID')+ '</KZZB_ID>'+ '<KUANGFHYBH update=\"true\">' + Mrcd[i].get('KUANGFHYBH')+ '</KUANGFHYBH>'+ '<FAHDW update=\"true\">'  	\n");
				sb.append("									+ Mrcd[i].get('FAHDW').replace('<','&lt;').replace('>','&gt;')+ '</FAHDW>'+ '<MEIKDWMC update=\"true\">' + Mrcd[i].get('MEIKDWMC').replace('<','&lt;').replace('>','&gt;')+ '</MEIKDWMC>'+ '<FAZ update=\"true\">'   	\n");
				sb.append("									+ Mrcd[i].get('FAZ').replace('<','&lt;').replace('>','&gt;')+ '</FAZ>'+ '<DAOZ update=\"true\">' + Mrcd[i].get('DAOZ').replace('<','&lt;').replace('>','&gt;')+ '</DAOZ>'+ '<FAHRQ update=\"true\">'    	\n");
				sb.append("									+ Mrcd[i].get('FAHRQ').replace('<','&lt;').replace('>','&gt;')+ '</FAHRQ>'+ '<DAOHRQ update=\"true\">' + Mrcd[i].get('DAOHRQ').replace('<','&lt;').replace('>','&gt;')+ '</DAOHRQ>'+ '<CHEPH update=\"true\">'     	\n");
				sb.append("									+ Mrcd[i].get('CHEPH').replace('<','&lt;').replace('>','&gt;')+ '</CHEPH>'+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'+ '<CHEB update=\"true\">' + Mrcd[i].get('CHEB').replace('<','&lt;').replace('>','&gt;')+ '</CHEB>' + '</result>' ;      	\n");
				sb.append("								}      									\n");
				sb.append("								if(gridDiv_yihd_history==''){ 			\n");
				sb.append("									Ext.MessageBox.alert('提示信息','没有选择数据信息');	\n");
				sb.append("								}else{									\n");
				sb.append("									var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_yihd_history+'</result>';		\n");
				sb.append("									document.getElementById('QuedButton').click();		\n");
				sb.append("								}										\n");
				sb.append("							}											\n");
				sb.append("						},{												\n");
				sb.append("							text:'删除',									\n");
				sb.append("							handler:function(){							\n");
				sb.append("								win.hide();								\n");
				sb.append("								var Mrcd = gridDiv_yihd_grid.getSelectionModel().getSelections();	\n");
				sb.append("								for(i = 0; i< Mrcd.length; i++){		\n");
				sb.append("									if(typeof(gridDiv_yihd_save)=='function'){ 		\n");
				sb.append("										var revalue = gridDiv_yihd_save(Mrcd[i]); 		\n");
				sb.append("										if(revalue=='return'){ 			\n");
				sb.append("											return; 					\n");
				sb.append("										}else if(revalue=='continue'){	\n");
				sb.append("											continue;					\n");
				sb.append("										}								\n");
				sb.append("									}									\n");
				sb.append("									if(Mrcd[i].get('MEIKDWMC') == ''){Ext.MessageBox.alert('提示信息','字段 煤矿单位 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('FAZ') == ''){Ext.MessageBox.alert('提示信息','字段 发站 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('DAOZ') == ''){Ext.MessageBox.alert('提示信息','字段 到站 不能为空');return;	\n");
				sb.append("									}if(Mrcd[i].get('CHEPH') == ''){Ext.MessageBox.alert('提示信息','字段 车皮号 不能为空');return;	\n");
				sb.append("									}gridDiv_yihd_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<FAHB_ID update=\"true\">' 	\n");
				sb.append("									+ Mrcd[i].get('FAHB_ID')+ '</FAHB_ID>'+ '<KZZB_ID update=\"true\">' + Mrcd[i].get('KZZB_ID')+ '</KZZB_ID>'+ '<KUANGFHYBH update=\"true\">' + Mrcd[i].get('KUANGFHYBH')+ '</KUANGFHYBH>'+ '<FAHDW update=\"true\">'  	\n");
				sb.append("									+ Mrcd[i].get('FAHDW').replace('<','&lt;').replace('>','&gt;')+ '</FAHDW>'+ '<MEIKDWMC update=\"true\">' + Mrcd[i].get('MEIKDWMC').replace('<','&lt;').replace('>','&gt;')+ '</MEIKDWMC>'+ '<FAZ update=\"true\">'   	\n");
				sb.append("									+ Mrcd[i].get('FAZ').replace('<','&lt;').replace('>','&gt;')+ '</FAZ>'+ '<DAOZ update=\"true\">' + Mrcd[i].get('DAOZ').replace('<','&lt;').replace('>','&gt;')+ '</DAOZ>'+ '<FAHRQ update=\"true\">'    	\n");
				sb.append("									+ Mrcd[i].get('FAHRQ').replace('<','&lt;').replace('>','&gt;')+ '</FAHRQ>'+ '<DAOHRQ update=\"true\">' + Mrcd[i].get('DAOHRQ').replace('<','&lt;').replace('>','&gt;')+ '</DAOHRQ>'+ '<CHEPH update=\"true\">'     	\n");
				sb.append("									+ Mrcd[i].get('CHEPH').replace('<','&lt;').replace('>','&gt;')+ '</CHEPH>'+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'+ '<CHEB update=\"true\">' + Mrcd[i].get('CHEB').replace('<','&lt;').replace('>','&gt;')+ '</CHEB>' + '</result>' ;      	\n");
				sb.append("								}      									\n");
				sb.append("								if(gridDiv_yihd_history==''){ 			\n");
				sb.append("									Ext.MessageBox.alert('提示信息','没有选择数据信息');	\n");
				sb.append("								}else{									\n");
				sb.append("									var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_yihd_history+'</result>';		\n");
				sb.append("									Ext.MessageBox.confirm('提示', '确认要删除该车的矿方质量?', CheckDelete);		\n");
				sb.append("								}										\n");
				sb.append("							}											\n");
				sb.append("						},{												\n");
				sb.append("							text: '取消',								\n");
				sb.append("							handler: function(){						\n");
				sb.append("								win.hide();								\n");
				sb.append("							}											\n");
				sb.append("						}]												\n");
				sb.append("				});														\n");
				sb.append("			}															\n");
				sb.append("			if(rec==undefined){											\n");
				sb.append("				Ext.MessageBox.alert('提示信息','请选择车皮号！');		\n");
				sb.append("			}else{														\n");
				sb.append("				win.show(this);											\n");
				sb.append("			}															\n");
				
				egu.addToolbarItem("{"+new GridButton("录入矿方质量","function(){"+sb.toString()+"}").getScript()+"}");
		}
		

		egu.addOtherScript("gridDiv_yihd_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
//		用于TabPanl切换时再画Grid，所以首先不显示这个Grid。
		egu.setDefaultRender(false);
		
		egu.addOtherScript("gridDiv_yihd_grid.on('rowclick',function(own,row,e){	\n"
						+ " var rec = gridDiv_yihd_ds.getAt(row);	\n"
						+ " var biaoz=0,length=0,jine=0,dcbiaoz=0;	\n" 
						+ " var fahdw='',meikdw='',faz='',daoz='',cheb='',cheph='',strtmp='';	\n" 
						+ " \tfahdw=rec.get('FAHDW');	\n"
						+ " \tmeikdw=rec.get('MEIKDWMC');	\n"
						+ " \tfaz=rec.get('FAZ');	\n"
						+ " \tdaoz=rec.get('DAOZ');	\n"
						+ " \tcheb=rec.get('CHEB');	\n"
						+ " \tcheph=rec.get('CHEPH');	\n"
						+ " \tdcbiaoz=rec.get('BIAOZ');	\n"
						+ " \tdocument.getElementById('EditTableRow').value=row;	\n"	

						+ " rec = gridDiv_yihd_grid.getSelectionModel().getSelections();	\n"
						+ " length=rec.length;	\n"
						+ " for(var i=0;i<rec.length;i++){	\n" 
						+ " \tif(0!=rec[i].get('ID')){	\n"	
						+ " \t\tbiaoz+=eval(rec[i].get('BIAOZ'));	\n" 
						+ " }else{	\n"
						+ " length--;	\n"	
						+ " }	\n"
						+ " }	\n"	
						+ " 	Hej_ches.setValue(length);	\n"
						+ " 	Hej_biaoz.setValue(biaoz);	\n"
						+ " });"
						
						+ "	gridDiv_yihd_grid.on('click',function(){	\n"
						+ " 	reCountToolbarNum(this);		\n"
						+ "	});");

		
		egu.addOtherScript("function gridDiv_yihd_save(rec){" +
				"	if(rec.get('FAHDW')=='合计'){" +
				"	return 'continue';" +
				"}}");
		
		setExtGrid_Yihd(egu);
		con.Close();
	}
	
	public void getKuangfzlArray(String sbKuangfhybh) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		StringBuffer KuangfhyxxArrayScript=new StringBuffer();
		String sql="",Strtmp="";
		int i=0;
		
		sql="select * from kuangfzlzb where id in ("+sbKuangfhybh+")";
		ResultSetList rsl=con.getResultSetList(sql);
		
		for(i=0;i<rsl.getRows();i++){
			
			if(i==0){
				
				Strtmp="new Array()";                   
			}else{
        	   
				Strtmp+=",new Array()";
			}
		}
		i=0;
		
		KuangfhyxxArrayScript.append("var Kuangfhyxx=new Array("+Strtmp+");	\n");
		
		while(rsl.next()){
			
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][0]='"+rsl.getString("bianm")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][1]='"+rsl.getString("qnet_ar")+"'	\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][2]='"+rsl.getString("aar")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][3]='"+rsl.getString("ad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][4]='"+rsl.getString("vdaf")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][5]='"+rsl.getString("mt")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][6]='"+rsl.getString("stad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][7]='"+rsl.getString("aad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][8]='"+rsl.getString("mad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][9]='"+rsl.getString("qbad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][10]='"+rsl.getString("had")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][11]='"+rsl.getString("vad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][12]='"+rsl.getString("fcad")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][13]='"+rsl.getString("std")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][14]='"+rsl.getString("qgrad")+"'	\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][15]='"+rsl.getString("hdaf")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][16]='"+rsl.getString("qgrad_daf")+"' \n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][17]='"+rsl.getString("sdaf")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][18]='"+rsl.getString("t1")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][19]='"+rsl.getString("t2")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][20]='"+rsl.getString("t3")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][21]='"+rsl.getString("t4")+"'		\n");
			KuangfhyxxArrayScript.append("Kuangfhyxx["+i+"][22]='"+((Visit) getPage().getVisit()).getRenymc()+"'	\n");
			
			i++;
		}
		
		this.setKuangfhyxx(KuangfhyxxArrayScript.toString());
		con.Close();
	}
	public void getFenzSelectData(){
		
		String sql = "";
		JDBCcon con = new JDBCcon();
		
		sql=" select cp.biaoz,"
			+ " decode(cp.chebb_id,1,'路车',2,'自备车',3,'汽车',4,'船') as cheb,count(cp.id) as ches "
			+ " from chepb cp,fahb f where cp.fahb_id=f.id and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")"
			+ " and f.liucztb_id=1 and cp.hedbz=3 group by cp.biaoz,cp.chebb_id order by cp.chebb_id,cp.biaoz";
		
		ResultSetList rsl=con.getResultSetList(sql);//未核对
		ExtGridUtil egu = new ExtGridUtil("gridDiv_fenz", rsl);//设定记录集对应的表单
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("cheb").setHeader("车别");

		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("cheb").setWidth(80);
		egu.addPaging(10);
		setExtGrid_Fenz(egu);
		con.Close();
	}

	//未核对
	public ExtGridUtil getExtGrid_Weihd() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid_Weihd(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript_Weihd() {
		return getExtGrid_Weihd().getGridScript();
	}

	public String getGridHtml_Weihd() {
		return getExtGrid_Weihd().getHtml();
	}
	//
	
	//已核对
	public ExtGridUtil getExtGrid_Yihd() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid_Yihd(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript_Yihd() {
		return getExtGrid_Yihd().getGridScript();
	}

	public String getGridHtml_Yihd() {
		return getExtGrid_Yihd().getHtml();
	}
	//
	
	//分组
	public ExtGridUtil getExtGrid_Fenz() {
		return ((Visit) this.getPage().getVisit()).getExtGrid3();
	}

	public void setExtGrid_Fenz(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid3(extgrid);
	}

	public String getGridScript_Fenz() {
		return getExtGrid_Fenz().getGridScript();
	}

	public String getGridHtml_Fenz() {
		return getExtGrid_Fenz().getHtml();
	}
	//
	
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
	
	//车别
	public IDropDownBean getChebValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIChebModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChebValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public IPropertySelectionModel getIChebModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getIChebModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setIChebModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	
	public void setEditTableRow(int value){
		
		((Visit) this.getPage().getVisit()).setInt1(value);
	}

	public void getIChebModels() {

		/*List list = new ArrayList();
		list.add(new IDropDownBean(1, "路车"));
		list.add(new IDropDownBean(2, "自备车"));*/
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel("select id,mingc from chebb"));
	}
	//
	
	//矿方质量子表编码
	public IDropDownBean getKuangfzlzbbmValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIKuangfzlzbbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setKuangfzlzbbmValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public IPropertySelectionModel getIKuangfzlzbbmModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getIKuangfzlzbbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setIKuangfzlzbbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getIKuangfzlzbbmModels() {

		String sql="";
		if(this.getYansbh()==null||this.getYansbh().equals("")){
			
			
		}else{
			
			sql="select kzzb.id,kzzb.bianm		\n"
				+ " from fahb f,kuangfzlb kz,yansbhb ys,kuangfzlzb kzzb		\n" 
				+ " where f.kuangfzlb_id=kz.id and f.yansbhb_id=ys.id and ys.bianm='"+((Visit) getPage().getVisit()).getString4()+"'		\n"
				+ " and f.lie_id in ("+((Visit) getPage().getVisit()).getString6()+")		\n"
				+ " and kz.id=kzzb.kuangfzlb_id 	\n"
				+ " order by kzzb.bianm";
		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(-1);		//EditTableRow
			visit.setInt2(0);		//车别
			visit.setInt3(0);		//初始化标准费用表记录数
			visit.setExtGrid1(null);//未核对
			visit.setExtGrid2(null);//已核对
			visit.setExtGrid3(null);//分组
			visit.setString1("");	//change
			visit.setString2("");	//矿方化验编号
			visit.setString3("");	//矿别化验指标
//			visit.setString4("");	//验收编号
			
			visit.setDouble1(0);	//Qnetar
			visit.setDouble2(0);	//Aar
			visit.setDouble3(0);	//Ad
			visit.setDouble4(0);	//Vdaf
			visit.setDouble5(0);	//Mt
			visit.setDouble6(0);	//Stad
			visit.setDouble7(0);	//Aad
			visit.setDouble8(0);	//Mad
			visit.setDouble9(0);	//Qbad
			visit.setDouble10(0);	//Had
			visit.setDouble11(0);	//Vad
			visit.setDouble12(0);	//fcad
			visit.setDouble13(0);	//std
			visit.setDouble14(0);	//qgrad
			visit.setDouble15(0);	//hdaf
			visit.setDouble16(0);	//qgrad_daf
			visit.setDouble17(0);	//sdaf
			visit.setDouble18(0);	//t1
			visit.setDouble19(0);	//t2
			visit.setDouble20(0);	//t3
			visit.setDouble21(0);	//t4
			visit.setString5("");	//录入员
			
			setIChebModel(null);	//1
			setChebValue(null);		//1
			getIChebModels();
			
//			setIKuangfzlzbbmModel(null);	//2
//			setKuangfzlzbbmValue(null);		//2
//			getIKuangfzlzbbmModels();
			
			getWeihdSelectData();
			getYihdSelectData();
			getFenzSelectData();
		}
		setEditTableRow(-1);
	}
	
}