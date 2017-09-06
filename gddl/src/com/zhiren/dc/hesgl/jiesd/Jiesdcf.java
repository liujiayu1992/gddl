package com.zhiren.dc.hesgl.jiesd;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbcl;
 
public class Jiesdcf extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private String _msg;

	protected void initialize() {
		super.initialize();
		_msg = "";
		_liucmc="";
	}

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	// 起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// 结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	保存页面上输入的拆分比率
	private String Bil;

	public String getBil() {
		return Bil;
	}

	public void setBil(String Bil) {
		this.Bil = Bil;
	}
	
//	设置是否重算结算单价_begin
	public String getShifcsdj() {
		
		if(((Visit) getPage().getVisit()).getString5().equals("")){
			
			((Visit) getPage().getVisit()).setString5(MainGlobal.getXitxx_item("结算", Locale.bukyqjksfcsdj_xitxx, 
					getTreeid(), "否"));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setShifcsdj(String _value) {
		((Visit) getPage().getVisit()).setString5(_value);
	}
	
//	煤款含税单价保留小数位
	public int getMeikhsdjblxsw(){
		
		return ((Visit) getPage().getVisit()).getInt2();
	}
	
	public void setMeikhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt2(value);
	}
	
//	运费含税单价保留小数位
	public int getYunfhsdjblxsw(){
		
		return ((Visit) getPage().getVisit()).getInt3();
	}
	
	public void setYunfhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt3(value);
	}
//	设置是否重算结算单价_end
	public boolean isHetbhDp() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setHetbhDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}
	
	public boolean isLiucmcDp() {
		return ((Visit) getPage().getVisit()).getboolean6();
	}

	public void setLiucmcDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean6(editable);
	}
	
//	int1 状态 判断是修改合同号还是提交进入流程	1、修改合同，2、提交进入流程
	public void setZhuangt(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}	
	
	private static Date _JiesrqsmallValue = new Date();
	private boolean _Jiesrqsmallchange = false;
	public Date getJiesrqsmall() {
		return _JiesrqsmallValue;
	}

	public void setJiesrqsmall(Date _value) {
		if (FormatDate(_JiesrqsmallValue).equals(FormatDate(_value))) {
			_Jiesrqsmallchange = false;
		} else {

			_JiesrqsmallValue = _value;
			_Jiesrqsmallchange = true;
		}

	}
	
	private static Date _JiesrqbigValue = new Date();
	private boolean _Jiesrqbigchange = false;
	public Date getJiesrqbig() {
		return _JiesrqbigValue;
	}

	public void setJiesrqbig(Date _value) {
		if (FormatDate(_JiesrqbigValue).equals(FormatDate(_value))) {
			_Jiesrqbigchange = false;
			
		} else {

			_JiesrqbigValue = _value;
			_Jiesrqbigchange = true;
		}

	}

	private void Save() {

	    JDBCcon con = new JDBCcon();
//	    con.setAutoCommit(false);
//	    con.rollBack();
//	    con.commit();
	    String sql = "";
	    
	    String talbe_mk = "jiesb";
	    String talbe_yf = "jiesyfb";
	    
	    if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString4())){
//	    	分公司采购结算修改
	    	talbe_mk = "kuangfjsmkb";
	    	talbe_yf = "kuangfjsyfb";
	    }
	    
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Dcbalancebean)getEditValues().get(0)).getJiesbh().equals("")){
	        	if(Jiesdcz.checkbh(talbe_mk,talbe_yf,((Dcbalancebean)getEditValues().get(0)).getJiesbh(),((Dcbalancebean)getEditValues().get(0)).getId(),((Dcbalancebean)getEditValues().get(0)).getYid())){
	        				
			        		sql=" update "+talbe_mk+" set bianm = '"+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"',"
							       + " gongysb_id = "+getProperId(getIFahdwModels(),((Dcbalancebean)getEditValues().get(0)).getFahdw())+","
							       + " gongysmc = '"+((Dcbalancebean)getEditValues().get(0)).getFahdw()+"',"
							       + " faz = '"+((Dcbalancebean)getEditValues().get(0)).getFaz()+"',"
							       + " fahksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
							       + " fahjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"
							       + " meiz = '"+((Dcbalancebean)getEditValues().get(0)).getPinz()+"',daibch ='"+((Dcbalancebean)getEditValues().get(0)).getDaibcc()+"'," 
							       + " yuanshr ='"+((Dcbalancebean)getEditValues().get(0)).getYuanshr()+"',xianshr = '"+((Dcbalancebean)getEditValues().get(0)).getXianshr()+"',"
							       + " yansksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd')," 
							       + " yansjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),"
							       + " shoukdw ='"+((Dcbalancebean)getEditValues().get(0)).getShoukdw()+"',kaihyh = '"+((Dcbalancebean)getEditValues().get(0)).getKaihyh()+"',"
							       + " yansbh = '"+((Dcbalancebean)getEditValues().get(0)).getYansbh()+"',zhangh = '"+((Dcbalancebean)getEditValues().get(0)).getZhangh()+"',"
							       + " fapbh = '"+((Dcbalancebean)getEditValues().get(0)).getFapbh()+"',fukfs = '"+((Dcbalancebean)getEditValues().get(0)).getFukfs()+"',"
							       + " duifdd = '"+((Dcbalancebean)getEditValues().get(0)).getDuifdd()+"',ches ="+((Dcbalancebean)getEditValues().get(0)).getChes()+","
							       + " jiessl = "+((Dcbalancebean)getEditValues().get(0)).getJiessl()+",guohl = "+((Dcbalancebean)getEditValues().get(0)).getJingz()+","
							       + " hansdj = "+((Dcbalancebean)getEditValues().get(0)).getShulzjbz()+",bukmk = "+((Dcbalancebean)getEditValues().get(0)).getBukyqjk()+","
							       + " hansmk = "+((Dcbalancebean)getEditValues().get(0)).getJiasje()+",buhsmk = "+((Dcbalancebean)getEditValues().get(0)).getJiakhj()+","
							       + " shuik = "+((Dcbalancebean)getEditValues().get(0)).getJiaksk()+",shuil = "+((Dcbalancebean)getEditValues().get(0)).getJiaksl()+","
							       + " buhsdj = "+((Dcbalancebean)getEditValues().get(0)).getBuhsdj()+",hansyf = "+(((Dcbalancebean)getEditValues().get(0)).getYunzfhj()==0?
							    		   ((Dcbalancebean)getEditValues().get(0)).getYunzfhj_mk():((Dcbalancebean)getEditValues().get(0)).getYunzfhj())+","
							       + " buhsyf="+(((Dcbalancebean)getEditValues().get(0)).getBuhsyf()==0?((Dcbalancebean)getEditValues().get(0)).getBuhsyf_mk():((Dcbalancebean)getEditValues().get(0)).getBuhsyf())+","	   
								   + " beiz = '"+((Dcbalancebean)getEditValues().get(0)).getBeiz()+"',meikje = "+((Dcbalancebean)getEditValues().get(0)).getJiakje()+","
							       + " ranlbmjbr='"+((Dcbalancebean)getEditValues().get(0)).getRanlbmjbr()+"',ranlbmjbrq=to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getRanlbmjbrq())+"','yyyy-MM-dd')";
							
							if(((Dcbalancebean)getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){
//								如果结算类型为煤款结算
								sql+=",kuidjfyf="+((Dcbalancebean)getEditValues().get(0)).getKuidjfyf()+",kuidjfzf="+((Dcbalancebean)getEditValues().get(0)).getKuidjfzf()+"";
							}
			        		
			        		sql+= " where id ="+((Dcbalancebean)getEditValues().get(0)).getId()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		sql=" update "+talbe_yf+" set bianm = '"+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"',	\n gongysb_id = "+getProperId(getIFahdwModels(),((Dcbalancebean)getEditValues().get(0)).getFahdw())+",	\n"
			        			+ " gongysmc = '"+((Dcbalancebean)getEditValues().get(0)).getFahdw()+"',	\n faz = '"+((Dcbalancebean)getEditValues().get(0)).getFaz()+"',	\n fahksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " fahjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),	\n meiz ='"+((Dcbalancebean)getEditValues().get(0)).getPinz()+"',	\n daibch ='"+((Dcbalancebean)getEditValues().get(0)).getDaibcc()+"',	\n"
			        			+ " yuanshr = '"+((Dcbalancebean)getEditValues().get(0)).getYuanshr()+"',	\n xianshr = '"+((Dcbalancebean)getEditValues().get(0)).getXianshr()+"',	\n yansksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " yansjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),	\n yansbh ='"+((Dcbalancebean)getEditValues().get(0)).getYansbh()+"',	\n shoukdw = '"+((Dcbalancebean)getEditValues().get(0)).getShoukdw()+"',	\n"
			        			+ " kaihyh = '"+((Dcbalancebean)getEditValues().get(0)).getKaihyh()+"',	\n zhangh = '"+((Dcbalancebean)getEditValues().get(0)).getZhangh()+"',	\n fapbh = '"+((Dcbalancebean)getEditValues().get(0)).getFapbh()+"', \n hansdj="+((Dcbalancebean)getEditValues().get(0)).getYunfhsdj()+",	\n"
			        			+ " fukfs = '"+((Dcbalancebean)getEditValues().get(0)).getFukfs()+"',	\n duifdd = '"+((Dcbalancebean)getEditValues().get(0)).getDuifdd()+"',	\n ches ="+((Dcbalancebean)getEditValues().get(0)).getChes()+",		\n"
			        			+ " jiessl = "+((Dcbalancebean)getEditValues().get(0)).getJiessl()+",	\n guohl = "+((Dcbalancebean)getEditValues().get(0)).getJingz()+",	\n guotyf = "+((Dcbalancebean)getEditValues().get(0)).getTielyf()+",		\n"
			        			+ " guotzf = "+((Dcbalancebean)getEditValues().get(0)).getTielzf()+",	\n kuangqyf="+((Dcbalancebean)getEditValues().get(0)).getKuangqyf()+",	\n kuangqzf="+((Dcbalancebean)getEditValues().get(0)).getKuangqzf()+",	\n"
			        			+ " jiskc = "+((double)Math.round((((Dcbalancebean)getEditValues().get(0)).getTielzf()+((Dcbalancebean)getEditValues().get(0)).getKuangqzf())*100)/100)+",	\n bukyf ="+((Dcbalancebean)getEditValues().get(0)).getBukyqyzf()+",	\n hansyf = "+((Dcbalancebean)getEditValues().get(0)).getYunzfhj()+",	\n"
			        			+ " buhsyf = "+((Dcbalancebean)getEditValues().get(0)).getBuhsyf()+",	\n shuik = "+((Dcbalancebean)getEditValues().get(0)).getYunfsk()+",	\n shuil ="+((Dcbalancebean)getEditValues().get(0)).getYunfsl()+",	\n jiesrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),	\n"
			        			+ " beiz = '"+((Dcbalancebean)getEditValues().get(0)).getBeiz()+"'	\n where id = "+((Dcbalancebean)getEditValues().get(0)).getYid()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		setMsg("结算单更新成功！");
		        		
			        		Chengbcl.CountCb_js(((Visit) getPage().getVisit()).getString4(),
			        				((Dcbalancebean)getEditValues().get(0)).getId(),
			        				((Dcbalancebean)getEditValues().get(0)).getYid());
	        		
	        	}else{
	        		
	        		setMsg("该结算编号已被另一张结算单使用，请核对！");
	        	}
	        }
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
//	    getSelectData();
	}
	
	private void Retruns() {

		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}
	
//	"拆分"按钮
	private boolean _ChaifClick = false;
	
	public void ChaifButton(IRequestCycle cycle) {
		_ChaifClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getIJiesbhModels();
			getSelectData();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns();
			getIJiesbhModels();
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			getIJiesbhModels();
			getSelectData();
		}
		if(_QuedChick){
			_QuedChick=false;
			Submit();
			getIJiesbhModels();
			getSelectData();
		}
		if (_ChaifClick) {
			_ChaifClick = false;
			chaif();
			getIJiesbhModels();
			getSelectData();
		}
	}
	
	/**
	 * 拆分结算单，并保存到数据库中
	 */
	public void chaif() {
		
		JDBCcon con = new JDBCcon();
		
		double chaifbl = Double.parseDouble(getBil()) / 100; // 保存页面输入的拆分比率
		Dcbalancebean blsBean = (Dcbalancebean) getEditValues().get(0); // 获得结算单信息
		double jieslx = blsBean.getJieslx();
		String jiesbh = blsBean.getJiesbh();
		
		StringBuffer sb_sql = new StringBuffer("begin\n");
		String chaif_sql = "";
		
		if (jieslx == Locale.meikjs_feiylbb_id ) { // 煤款结算单拆分
			
			chaif_sql = saveTojsmkzb(con, jiesbh, chaifbl);
			if (chaif_sql.equals("")) {
				setMsg("请选择一个结算编号！");
			} else {
				if (con.getInsert(sb_sql.append(chaif_sql).append("end;").toString()) > 0) {
					setMsg("拆分成功！");
				} else {
					setMsg("拆分失败！");
				}
			}
			
		} else if (jieslx == Locale.guotyf_feiylbb_id || jieslx == Locale.dityf_feiylbb_id || jieslx == Locale.kuangqyf_feiylbb_id) { // 运费单结算拆分
			
			chaif_sql = saveTojsyfzb(con, jiesbh, chaifbl);
			if (chaif_sql.equals("")) {
				setMsg("请选择一个结算编号！");
			} else {
				if (con.getInsert(sb_sql.append(chaif_sql).append("end;").toString()) > 0) {
					setMsg("拆分成功！");
				} else {
					setMsg("拆分失败！");
				}
			}
			
		} else if (jieslx == Locale.liangpjs_feiylbb_id) { // 两票结算单拆分
			
			chaif_sql = saveTojsmkzb(con, jiesbh, chaifbl) + saveTojsyfzb(con, jiesbh, chaifbl);
			if ((chaif_sql).equals("")) {
				setMsg("请选择一个结算编号！");
			} else {
				if (con.getInsert(sb_sql.append(chaif_sql).append("end;").toString()) > 0) {
					setMsg("拆分成功！");
				} else {
					setMsg("拆分失败！");
				}
			}
			
		}
		
		con.Close();
	}
	
	/**
	 * 返回拆分煤款结算单的SQL语句
	 * @param con
	 * @param jiesbh  结算编号
	 * @param chaifbl 拆分比率
	 */
	public String saveTojsmkzb(JDBCcon con, String jiesbh, double chaifbl) {
		
		String newid_dt = MainGlobal.getNewID(con, getTreeid());
		String newid_gj = MainGlobal.getNewID(con, getTreeid());
		
		String sql = "select * from jiesb mk where mk.bianm = '"+ jiesbh +"'";
		ResultSetList rsl = con.getResultSetList(sql);
		
		String sql_dt = "";
		String sql_gj = "";
		while (rsl.next()) {
			
//			拆分给大唐阳城
			sql_dt = 
				"insert into jieszb(id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, " +
				"	daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, " +
				"	koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid," +
				"	ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, " +
				"	fengsjj, jiajqdj, jijlx, diancjsmkb_id, yufkje, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, yunfhsdj, hansyf, buhsyf, yunfjsl, danw, chaifbl) " +
				"values("+ newid_dt +", "
				+ getTreeid() +", '"
				+ jiesbh +"', "
				+ rsl.getString("gongysb_id") +", '"
				+ rsl.getString("gongysmc") +"', "
				+ rsl.getString("yunsfsb_id") +", '"
				+ rsl.getString("yunj") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("yingd") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuid") * chaifbl, 2) +", '"
				+ rsl.getString("faz") +"', "
				+ "to_date('" + rsl.getDateString("fahksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("fahjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("meiz") +"', '"
				+ rsl.getString("daibch") +"', '"
				+ rsl.getString("yuanshr") +"', '"
				+ rsl.getString("xianshr") +"', "
				+ "to_date('" + rsl.getDateString("yansksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("yansjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("yansbh") +"', '"
				+ rsl.getString("shoukdw") +"', '"
				+ rsl.getString("kaihyh") +"', '"
				+ rsl.getString("zhangh") +"', '"
				+ rsl.getString("fapbh") +"', '"
				+ rsl.getString("fukfs") +"', '"
				+ rsl.getString("duifdd") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("ches") * chaifbl, 0) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiessl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guohl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yuns") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("koud") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiesslcy") * chaifbl, 2) +", "
				+ rsl.getDouble("hansdj") +", "
				+ CustomMaths.Round_new(rsl.getDouble("bukmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("hansmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("buhsmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("meikje") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("shuik") * chaifbl, 2) +", "
				+ rsl.getDouble("shuil") +", "
				+ rsl.getDouble("buhsdj") +", "
				+ rsl.getString("jieslx") +", "
				+ "to_date('" + rsl.getDateString("jiesrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getString("ruzrq") +"', 'yyyy-mm-dd'), "
				+ rsl.getString("hetb_id") +", "
				+ rsl.getString("liucztb_id") +", "
				+ rsl.getString("liucgzid") +", '"
				+ rsl.getString("ranlbmjbr") +"', "
				+ "to_date('" + rsl.getDateString("ranlbmjbrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("beiz") +"', "
				+ rsl.getString("jiesfrl") +", "
				+ rsl.getString("jihkjb_id") +", "
				+ rsl.getString("meikxxb_id") +", "
				+ rsl.getString("hetj") +", '"
				+ rsl.getString("meikdwmc") +"', '"
				+ rsl.getString("zhiljq") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("qiyf") * chaifbl, 3) +", "
				+ rsl.getString("jiesrl") +", "
				+ rsl.getString("jieslf") +", "
				+ rsl.getString("jiesrcrl") +", "
				+ rsl.getString("liucgzbid") +", '"
				+ rsl.getString("ruzry") +"', "
				+ rsl.getString("fuid") +", "
				+ CustomMaths.Round_new(rsl.getDouble("fengsjj") * chaifbl, 5) +", "
				+ rsl.getString("jiajqdj") +", "
				+ rsl.getString("jijlx") +", "
				+ rsl.getLong("diancjsmkb_id") +", "
				+ CustomMaths.Round_new(rsl.getDouble("yufkje") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfyr") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfzf") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("chaokdl") * chaifbl, 3) +", '"
				+ rsl.getString("chaokdlx") +"', "
				+ rsl.getString("yunfhsdj") +", "
				+ rsl.getString("hansyf") +", "
				+ rsl.getString("buhsyf") +", "
				+ CustomMaths.Round_new(rsl.getDouble("yunfjsl") * chaifbl, 5) +", '大唐阳城', "
				 + getBil() +");\n";
			
//			拆分给阳城国际
			sql_gj = 
				"insert into jieszb(id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, " +
				"	daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, " +
				"	koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid," +
				"	ranlbmjbr, ranlbmjbrq, beiz, jiesfrl, jihkjb_id, meikxxb_id, hetj, meikdwmc, zhiljq, qiyf, jiesrl, jieslf, jiesrcrl, liucgzbid, ruzry, fuid, " +
				"	fengsjj, jiajqdj, jijlx, diancjsmkb_id, yufkje, kuidjfyf, kuidjfzf, chaokdl, chaokdlx, yunfhsdj, hansyf, buhsyf, yunfjsl, danw, chaifbl) " +
				"values("+ newid_gj +", "
				+ getTreeid() +", '"
				+ jiesbh +"', "
				+ rsl.getString("gongysb_id") +", '"
				+ rsl.getString("gongysmc") +"', "
				+ rsl.getString("yunsfsb_id") +", '"
				+ rsl.getString("yunj") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("yingd") - rsl.getDouble("yingd") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuid") - rsl.getDouble("kuid") * chaifbl, 2) +", '"
				+ rsl.getString("faz") +"', "
				+ "to_date('" + rsl.getDateString("fahksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("fahjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("meiz") +"', '"
				+ rsl.getString("daibch") +"', '"
				+ rsl.getString("yuanshr") +"', '"
				+ rsl.getString("xianshr") +"', "
				+ "to_date('" + rsl.getDateString("yansksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("yansjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("yansbh") +"', '"
				+ rsl.getString("shoukdw") +"', '"
				+ rsl.getString("kaihyh") +"', '"
				+ rsl.getString("zhangh") +"', '"
				+ rsl.getString("fapbh") +"', '"
				+ rsl.getString("fukfs") +"', '"
				+ rsl.getString("duifdd") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("ches") - rsl.getDouble("ches") * chaifbl, 0) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiessl") - rsl.getDouble("jiessl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guohl") - rsl.getDouble("guohl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yuns") - rsl.getDouble("yuns") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("koud") - rsl.getDouble("koud") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiesslcy") - rsl.getDouble("jiesslcy") * chaifbl, 2) +", "
				+ rsl.getDouble("hansdj") +", "
				+ CustomMaths.Round_new(rsl.getDouble("bukmk") - rsl.getDouble("bukmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("hansmk") - rsl.getDouble("hansmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("buhsmk") - rsl.getDouble("buhsmk") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("meikje") - rsl.getDouble("meikje") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("shuik") - rsl.getDouble("shuik") * chaifbl, 2) +", "
				+ rsl.getDouble("shuil") +", "
				+ rsl.getDouble("buhsdj") +", "
				+ rsl.getString("jieslx") +", "
				+ "to_date('" + rsl.getDateString("jiesrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getString("ruzrq") +"', 'yyyy-mm-dd'), "
				+ rsl.getString("hetb_id") +", "
				+ rsl.getString("liucztb_id") +", "
				+ rsl.getString("liucgzid") +", '"
				+ rsl.getString("ranlbmjbr") +"', "
				+ "to_date('" + rsl.getDateString("ranlbmjbrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("beiz") +"', "
				+ rsl.getString("jiesfrl") +", "
				+ rsl.getString("jihkjb_id") +", "
				+ rsl.getString("meikxxb_id") +", "
				+ rsl.getString("hetj") +", '"
				+ rsl.getString("meikdwmc") +"', '"
				+ rsl.getString("zhiljq") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("qiyf") - rsl.getDouble("qiyf") * chaifbl, 3) +", "
				+ rsl.getString("jiesrl") +", "
				+ rsl.getString("jieslf") +", "
				+ rsl.getString("jiesrcrl") +", "
				+ rsl.getString("liucgzbid") +", '"
				+ rsl.getString("ruzry") +"', "
				+ rsl.getString("fuid") +", "
				+ CustomMaths.Round_new(rsl.getDouble("fengsjj") - rsl.getDouble("fengsjj") * chaifbl, 5) +", "
				+ rsl.getString("jiajqdj") +", "
				+ rsl.getString("jijlx") +", "
				+ rsl.getLong("diancjsmkb_id") +", "
				+ CustomMaths.Round_new(rsl.getDouble("yufkje") - rsl.getDouble("yufkje") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfyr") - rsl.getDouble("kuidjfyr") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfzf") - rsl.getDouble("kuidjfzf") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("chaokdl") - rsl.getDouble("chaokdl") * chaifbl, 3) +", '"
				+ rsl.getString("chaokdlx") +"', "
				+ rsl.getString("yunfhsdj") +", "
				+ rsl.getString("hansyf") +", "
				+ rsl.getString("buhsyf") +", "
				+ CustomMaths.Round_new(rsl.getDouble("yunfjsl") - rsl.getDouble("yunfjsl") * chaifbl, 5) +", '阳城国际', "
				 + (100 - Double.parseDouble(getBil())) +");\n";
			
		}
		
		return sql_dt + sql_gj;
	}
	
	/**
	 * 返回拆分运费结算单的SQL语句
	 * @param con
	 * @param jiesbh  结算编号
	 * @param chaifbl 拆分比率
	 */
	public String saveTojsyfzb(JDBCcon con, String jiesbh, double chaifbl) {
		
		String newid_dt = MainGlobal.getNewID(con, getTreeid());
		String newid_gj = MainGlobal.getNewID(con, getTreeid());
		
		String sql = "select * from jiesyfb yf where yf.bianm = '"+ jiesbh +"'";
		ResultSetList rsl = con.getResultSetList(sql);
		
		String sql_dt = "";
		String sql_gj = "";
		while (rsl.next()) {
			
//			拆分给大唐阳城
			sql_dt = 
				"insert into jiesyfzb(id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq,\n" +
				"	yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf,\n" + 
				"	kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id,\n" + 
				"	liucgzid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, gongfsl, yanssl, yingk, diancxxb_id, bianm,\n" + 
				"	gongysb_id, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id, chaokdl, chaokdlx, danw, chaifbl, zhongchh, xiecf) " +
				"values("+ newid_dt +", '"
				+ rsl.getString("gongysmc") +"', "
				+ rsl.getString("yunsfsb_id") +", '"
				+ rsl.getString("yunj") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("yingd") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuid") * chaifbl, 2) +", '"
				+ rsl.getString("faz") +"', "
				+ "to_date('"+ rsl.getDateString("fahksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('"+ rsl.getDateString("fahjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("meiz") +"', '"
				+ rsl.getString("daibch") +"', '"
				+ rsl.getString("yuanshr") +"', '"
				+ rsl.getString("xianshr") +"', "
				+ "to_date('" + rsl.getDateString("yansksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("yansjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("yansbh") +"', '"
				+ rsl.getString("shoukdw") +"', '"
				+ rsl.getString("kaihyh") +"', '"
				+ rsl.getString("zhangh") +"', '"
				+ rsl.getString("fapbh") +"', '"
				+ rsl.getString("fukfs") +"', '"
				+ rsl.getString("duifdd") +"', "
				+ CustomMaths.Round_new(rsl.getLong("ches") * chaifbl, 0) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiessl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guohl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yuns") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("koud") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiesslcy") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guotyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guotzf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuangqyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuangqzf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiskc") * chaifbl, 2) +", "
				+ rsl.getString("hansdj") +", "
				+ CustomMaths.Round_new(rsl.getDouble("bukyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("hansyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("buhsyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("shuik") * chaifbl, 2) +", "
				+ rsl.getString("shuil") +", "
				+ rsl.getString("buhsdj") +", "
				+ rsl.getString("jieslx") +", "
				+ "to_date('" + rsl.getDateString("jiesrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("ruzrq") +"', 'yyyy-mm-dd'), "
				+ rsl.getString("hetb_id") +", "
				+ rsl.getInt("liucztb_id") +", "
				+ rsl.getInt("liucgzid") +", "
				+ rsl.getString("diancjsmkb_id") +", '"
				+ rsl.getString("ranlbmjbr") +"', "
				+ "to_date('" + rsl.getDateString("ranlbmjbrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("beiz") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("gongfsl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yanssl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yingk") * chaifbl, 2) +", "
				+ rsl.getString("diancxxb_id") +", '"
				+ rsl.getString("bianm") +"', '"
				+ rsl.getString("gongysb_id") +"', '"
				+ rsl.getString("meikxxb_id") +"', "
				+ rsl.getDouble("dityf") + ", '"
				+ rsl.getString("meikdwmc") +"', '"
				+ rsl.getString("ruzry") +"', "
				+ rsl.getLong("fuid") +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfyf") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfzf") * chaifbl, 3) +", '"
				+ rsl.getString("diancjsyfb_id") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("chaokdl") * chaifbl, 5) +", '"
				+ rsl.getDateString("chaokdlx") + "', "
				+ "'大唐阳城', "+ getBil() +", '"+ rsl.getString("zhongchh") +"', " 
				+ CustomMaths.Round_new(rsl.getDouble("xiecf") * chaifbl, 2) + ");\n";
			
			
//			拆分给阳城国际
			sql_gj = 
				"insert into jiesyfzb(id, gongysmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq,\n" +
				"	yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, guotyf, guotzf,\n" + 
				"	kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id,\n" + 
				"	liucgzid, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, beiz, gongfsl, yanssl, yingk, diancxxb_id, bianm,\n" + 
				"	gongysb_id, meikxxb_id, dityf, meikdwmc, ruzry, fuid, kuidjfyf, kuidjfzf, diancjsyfb_id, chaokdl, chaokdlx, danw, chaifbl, zhongchh, xiecf) " +
				"values("+ newid_gj +", '"
				+ rsl.getString("gongysmc") +"', "
				+ rsl.getString("yunsfsb_id") +", '"
				+ rsl.getString("yunj") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("yingd") - rsl.getDouble("yingd") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuid") - rsl.getDouble("kuid") * chaifbl, 2) +", '"
				+ rsl.getString("faz") +"', "
				+ "to_date('"+ rsl.getDateString("fahksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('"+ rsl.getDateString("fahjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("meiz") +"', '"
				+ rsl.getString("daibch") +"', '"
				+ rsl.getString("yuanshr") +"', '"
				+ rsl.getString("xianshr") +"', "
				+ "to_date('" + rsl.getDateString("yansksrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("yansjzrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("yansbh") +"', '"
				+ rsl.getString("shoukdw") +"', '"
				+ rsl.getString("kaihyh") +"', '"
				+ rsl.getString("zhangh") +"', '"
				+ rsl.getString("fapbh") +"', '"
				+ rsl.getString("fukfs") +"', '"
				+ rsl.getString("duifdd") +"', "
				+ CustomMaths.Round_new(rsl.getLong("ches") - rsl.getLong("ches") * chaifbl, 0) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiessl") - rsl.getDouble("jiessl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guohl") - rsl.getDouble("guohl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yuns") - rsl.getDouble("yuns") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("koud") - rsl.getDouble("koud") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiesslcy") - rsl.getDouble("jiesslcy") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guotyf") - rsl.getDouble("guotyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("guotzf") - rsl.getDouble("guotzf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuangqyf") - rsl.getDouble("kuangqyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuangqzf") - rsl.getDouble("kuangqzf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("jiskc") - rsl.getDouble("jiskc") * chaifbl, 2) +", "
				+ rsl.getString("hansdj") +", "
				+ CustomMaths.Round_new(rsl.getDouble("bukyf") - rsl.getDouble("bukyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("hansyf") - rsl.getDouble("hansyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("buhsyf") - rsl.getDouble("buhsyf") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("shuik") - rsl.getDouble("shuik") * chaifbl, 2) +", "
				+ rsl.getString("shuil") +", "
				+ rsl.getString("buhsdj") +", "
				+ rsl.getString("jieslx") +", "
				+ "to_date('" + rsl.getDateString("jiesrq") +"', 'yyyy-mm-dd'), "
				+ "to_date('" + rsl.getDateString("ruzrq") +"', 'yyyy-mm-dd'), "
				+ rsl.getString("hetb_id") +", "
				+ rsl.getInt("liucztb_id") +", "
				+ rsl.getInt("liucgzid") +", "
				+ rsl.getString("diancjsmkb_id") +", '"
				+ rsl.getString("ranlbmjbr") +"', "
				+ "to_date('" + rsl.getDateString("ranlbmjbrq") +"', 'yyyy-mm-dd'), '"
				+ rsl.getString("beiz") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("gongfsl") - rsl.getDouble("gongfsl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yanssl") - rsl.getDouble("yanssl") * chaifbl, 2) +", "
				+ CustomMaths.Round_new(rsl.getDouble("yingk") - rsl.getDouble("yingk") * chaifbl, 2) +", "
				+ rsl.getString("diancxxb_id") +", '"
				+ rsl.getString("bianm") +"', '"
				+ rsl.getString("gongysb_id") +"', '"
				+ rsl.getString("meikxxb_id") +"', "
				+ rsl.getDouble("dityf") + ", '"
				+ rsl.getString("meikdwmc") +"', '"
				+ rsl.getString("ruzry") +"', "
				+ rsl.getLong("fuid") +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfyf") - rsl.getDouble("kuidjfyf") * chaifbl, 3) +", "
				+ CustomMaths.Round_new(rsl.getDouble("kuidjfzf") - rsl.getDouble("kuidjfzf") * chaifbl, 3) +", '"
				+ rsl.getString("diancjsyfb_id") +"', "
				+ CustomMaths.Round_new(rsl.getDouble("chaokdl") - rsl.getDouble("chaokdl") * chaifbl, 5) +", '"
				+ rsl.getDateString("chaokdlx") + "', "
				+ "'阳城国际', "+ (100 - Double.parseDouble(getBil())) +", '"+ rsl.getString("zhongchh") +"', " 
				+ CustomMaths.Round_new(rsl.getDouble("xiecf") - rsl.getDouble("xiecf") * chaifbl, 2) + ");\n";
			
		}
		
		return sql_dt + sql_gj;
	}
		
	private void Submit(){
		long Liuc_id=MainGlobal.getProperId(getILiucmcModel(), getLiucmc());
		if(Liuc_id>-1){
			
			String table_mk = "jiesb";
			String table_yf = "jiesyfb";
			
			if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString4())){
				
				table_mk = "kuangfjsmkb";
				table_yf = "kuangfjsyfb";
			}
//			
			if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.liangpjs_feiylbb_id){//两票结算
				
				Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl(table_mk,((Dcbalancebean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
				
				Liuc.tij(table_yf, ((Dcbalancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				
				Jiesdcz.Zijsdlccl(table_yf,((Dcbalancebean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues().get(0)).getId());
//				Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues().get(0)).getYid());
			
			}else if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){//煤款结算
				
				Liuc.tij(table_mk, ((Dcbalancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl(table_mk,((Dcbalancebean) getEditValues().get(0)).getId(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsmkb(((Dcbalancebean) getEditValues().get(0)).getId());
			}else {//运费结算
				
				Liuc.tij(table_yf, ((Dcbalancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				Jiesdcz.Zijsdlccl(table_yf,((Dcbalancebean) getEditValues().get(0)).getYid(),((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id, "TJ");
//				Jiesdcz.SubmitGsDiancjsyfb(((Dcbalancebean) getEditValues().get(0)).getYid());
			}
		}
	}
	
	private void Delete(){
	    JDBCcon con=new JDBCcon();
	    try{
	    	StringBuffer strbf=new StringBuffer();
	    	if(getEditValues()!= null && !getEditValues().isEmpty()){
	    		
	    		String table_mk = "jiesb";
	    		String table_yf = "jiesyfb";
	    		
	    		if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString4())){
//	    			分公司采购结算
	    			table_mk = "kuangfjsmkb";
	    			table_yf = "kuangfjsyfb";
	    		}

	    		strbf.append("begin		\n");
	    		if(((Dcbalancebean)getEditValues().get(0)).getId()>0){
	    			
	    			strbf.append(" delete from ").append(table_mk).append(" where id="+((Dcbalancebean)getEditValues().get(0)).getId()+";	\n");
	    			
	    			strbf.append(" delete from jieszbsjb where jiesdid="+((Dcbalancebean)getEditValues().get(0)).getId()+";	\n");
	    			
	    			strbf.append(" update ").append(table_mk).append(" set fuid = 0 where fuid="+((Dcbalancebean)getEditValues().get(0)).getId()+";	\n");
	    			
	    			strbf.append(" update fahb set jiesb_id=0 where jiesb_id="+((Dcbalancebean)getEditValues().get(0)).getId()+";	\n");
	    		}
	    		
	    		if(((Dcbalancebean)getEditValues().get(0)).getYid()>0){
	    			
	    			strbf.append("delete from ").append(table_yf).append(" where id="+((Dcbalancebean)getEditValues().get(0)).getYid()+";	\n");
	    			
	    			strbf.append("update danjcpb set yunfjsb_id=0 where yunfjsb_id="+((Dcbalancebean)getEditValues().get(0)).getYid()+";	\n");
	    			
	    			strbf.append("update ").append(table_yf).append(" set fuid = 0 where fuid="+((Dcbalancebean)getEditValues().get(0)).getYid()+";	\n");
	    			
	    			strbf.append("delete from jieszfb where jiesyfb_id="+((Dcbalancebean)getEditValues().get(0)).getYid()+";	\n");
	    		}
	    		
	    		strbf.append("end;	\n");
	    		
	    		if(con.getUpdate(strbf.toString())>=0){
	    			
	    			setMsg("编号："+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"结算单已删除！");
	    		}else{
	    			
	    			setMsg("编号："+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"结算单删除失败！");
	    		}
				
	    	}else{
	    		
	    		setMsg("请选择要删除的结算单！");
	    	}
	    }catch(Exception e){
	    	
	    	e.printStackTrace();
	    }finally{
	    	
	    	con.Close();
	    	getIJiesbhModels();
			getEditValues().clear();
			this.setJieszb("");
			List _editvalues = new ArrayList();
			_editvalues.add(new Dcbalancebean());
			setEditValues(_editvalues);
	    }
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Dcbalancebean _EditValue;
	public Dcbalancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getTitle(){
		
		return Locale.jiesd_title;
	}
	
	public String getJiesslcyText(){
		
		return Locale.jiesslcy_title;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		
		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			long mdiancxxb_id=0;
			long mgongysb_id=0;
			String mtianzdw ="";
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq="";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			double myunfhsdj = 0;		//运费含税单价
			
			String mShulzb_ht = "";		// 合同数量指标
			double mShulzb_yk = 0;		// 数量指标盈亏
			double mShulzb_zdj = 0;		// 数量指标折单价
			double mShulzb_zje = 0;		// 数量指标折金额
			 
			String mQnetar_ht = "";		// 合同热量
			double mQnetar_kf = 0;		// 供方热量
			double mQnetar_cf = 0;		// 厂方热量
			double mQnetar_js = 0;		// 厂方结算
			double mQnetar_yk = 0;		// 厂方盈亏
			double mQnetar_zdj = 0;		// 折单价
			double mQnetar_zje = 0;		// 这金额
			
			String mStd_ht="";			//合同硫分
			double mStd_kf=0;			//供方热量
			double mStd_cf=0;			//厂方热量
			double mStd_js=0;			//结算热量
			double mStd_yk = 0;			// 厂方盈亏
			double mStd_zdj = 0;		// 折单价
			double mStd_zje = 0;		// 这金额
			
			String mAd_ht="";			//合同硫分
			double mAd_kf=0;			//供方热量
			double mAd_cf=0;			//厂方热量
			double mAd_js=0;			//结算热量
			double mAd_yk = 0;			// 厂方盈亏
			double mAd_zdj = 0;			// 折单价
			double mAd_zje = 0;			// 这金额
			
			String mVdaf_ht="";			//合同硫分
			double mVdaf_kf=0;			//供方热量
			double mVdaf_cf=0;			//厂方热量
			double mVdaf_js=0;			//结算热量
			double mVdaf_yk = 0;		// 厂方盈亏
			double mVdaf_zdj = 0;		// 折单价
			double mVdaf_zje = 0;		// 这金额
			
			String mMt_ht="";			//合同硫分
			double mMt_kf=0;			//供方热量
			double mMt_cf=0;			//厂方热量
			double mMt_js=0;			//结算热量
			double mMt_yk = 0;			// 厂方盈亏
			double mMt_zdj = 0;			// 折单价
			double mMt_zje = 0;			// 这金额
			
			String mQgrad_ht="";		//合同硫分
			double mQgrad_kf=0;			//供方热量
			double mQgrad_cf=0;			//厂方热量
			double mQgrad_js=0;			//结算热量
			double mQgrad_yk = 0;		// 厂方盈亏
			double mQgrad_zdj = 0;		// 折单价
			double mQgrad_zje = 0;		// 这金额
			
			String mQbad_ht="";			//合同硫分
			double mQbad_kf=0;			//供方热量
			double mQbad_cf=0;			//厂方热量
			double mQbad_js=0;			//结算热量
			double mQbad_yk = 0;		// 厂方盈亏
			double mQbad_zdj = 0;		// 折单价
			double mQbad_zje = 0;		// 这金额
			
			String mHad_ht="";			//合同硫分
			double mHad_kf=0;			//供方热量
			double mHad_cf=0;			//厂方热量
			double mHad_js=0;			//结算热量
			double mHad_yk = 0;			// 厂方盈亏
			double mHad_zdj = 0;		// 折单价
			double mHad_zje = 0;		// 这金额
			
			String mStad_ht="";			//合同硫分
			double mStad_kf=0;			//供方热量
			double mStad_cf=0;			//厂方热量
			double mStad_js=0;			//结算热量
			double mStad_yk = 0;		// 厂方盈亏
			double mStad_zdj = 0;		// 折单价
			double mStad_zje = 0;		// 这金额
			
			String mStar_ht="";			//合同硫分
			double mStar_kf=0;			//供方热量
			double mStar_cf=0;			//厂方热量
			double mStar_js=0;			//结算热量
			double mStar_yk = 0;		// 厂方盈亏
			double mStar_zdj = 0;		// 折单价
			double mStar_zje = 0;		// 这金额
			
			String mMad_ht="";			//合同硫分
			double mMad_kf=0;			//供方热量
			double mMad_cf=0;			//厂方热量
			double mMad_js=0;			//结算热量
			double mMad_yk = 0;			// 厂方盈亏
			double mMad_zdj = 0;		// 折单价
			double mMad_zje = 0;		// 这金额
			
			String mAar_ht="";			//合同硫分
			double mAar_kf=0;			//供方热量
			double mAar_cf=0;			//厂方热量
			double mAar_js=0;			//结算热量
			double mAar_yk = 0;			// 厂方盈亏
			double mAar_zdj = 0;		// 折单价
			double mAar_zje = 0;		// 这金额
			
			String mAad_ht="";			//合同硫分
			double mAad_kf=0;			//供方热量
			double mAad_cf=0;			//厂方热量
			double mAad_js=0;			//结算热量
			double mAad_yk = 0;			// 厂方盈亏
			double mAad_zdj = 0;		// 折单价
			double mAad_zje = 0;		// 这金额
			
			String mVad_ht="";			//合同硫分
			double mVad_kf=0;			//供方热量
			double mVad_cf=0;			//厂方热量
			double mVad_js=0;			//结算热量
			double mVad_yk = 0;			// 厂方盈亏
			double mVad_zdj = 0;		// 折单价
			double mVad_zje = 0;		// 这金额
			
			String mT2_ht="";			//合同硫分
			double mT2_kf=0;			//供方热量
			double mT2_cf=0;			//厂方热量
			double mT2_js=0;			//结算热量
			double mT2_yk = 0;			// 厂方盈亏
			double mT2_zdj = 0;			// 折单价
			double mT2_zje = 0;			// 这金额
			
			String mYunju_ht="";		//合同运距
			double mYunju_kf=0;			//供方热量
			double mYunju_cf=0;			//厂方热量
			double mYunju_js=0;			//结算热量
			double mYunju_yk = 0;		// 厂方盈亏
			double mYunju_zdj = 0;		// 折单价
			double mYunju_zje = 0;		// 这金额
			
			long mhetb_id=0; 
			double mkoud_js=0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf=0;
			double mkuangqzf=0;
			double mkuangqsk=0;
			double mkuangqjk=0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mmeikhjdx = "";
			String myunzfhjdx = "";
			String mbeiz = "";
			String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = 3;
			double myuns = 0;
			String mjiesslblxsw="0";
			String myunsfs = "";
			String mdiancjsbs = "";
			String mstrJieszb="";
			boolean blnHasMeik=false;
			Money mn=new Money();
			Jiesdcz jsdcz=new Jiesdcz();
			double mjiesslcy=0;
			long myunsfsb_id=0;
			double myingd=0;
			double mkuid=0;
			String myunju="";		//运距
			double mfengsjj=0;		//分公司加价
			double mjiajqdj=0;		//加价前单价
			int mjijlx=0;			//基价类型
			String mMjtokcalxsclfs="";	//兆焦转大卡小数处理方式
			double mkuidjfyf_je=0;	//亏吨拒付运费金额
			double mkuidjfzf_je=0;	//亏顿拒付杂费金额
			double mchaokdl=0;   	//超亏吨量
			String mchaokdlx="";	//超亏吨类型
			long chongdjsb_id=0;//冲抵结算表id
			
//			进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
//			结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id=0;
			long mYunfjsb_id=0;
			long mJihkjb_id=0;
			
//			煤款结算表中的运费关键信息
			double mYunfjsdj_mk = 0;	//运费结算单价(jiesb)
			double mYunzfhj_mk = 0;		//运杂费合计（jiesb）
			double mBuhsyf_mk = 0;		//不含税运费（jiesb）
			double mYunfjsl_mk = 0;		//运费结算数量(jiesb)	
//			煤款结算表中的运费关键信息_End
			
			String table_mk = "jiesb";
			String table_yf = "jiesyfb";
			
			if(Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString4())){
//				如果是分公司结算
				table_mk = "kuangfjsmkb";
				table_yf = "kuangfjsyfb";
			}
			
			String sql="select * from "+table_mk+" where bianm='"+getJiesbhValue().getValue()+"' and fuid=0";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				mid = rs.getLong("id");
				mtianzdw =rs.getString("xianshr");
				mjiesbh = rs.getString("bianm");
				mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
				mfahdw = rs.getString("gongysmc");
				mfaz =rs.getString("faz");
				mshoukdw = rs.getString("shoukdw");
				mfahksrq = rs.getDate("fahksrq");
				mfahjzrq = rs.getDate("fahjzrq");
				myansksrq = rs.getDate("yansksrq");
				myansjzrq =rs.getDate("yansjzrq");
				mkaihyh =rs.getString("kaihyh");
				mpinz = rs.getString("meiz");
				myuanshr = rs.getString("yuanshr");
				mzhangh = rs.getString("zhangh");
				mches = rs.getInt("ches");
				mxianshr =rs.getString("xianshr");
				mdaibcc = rs.getString("daibch");
				myansbh =rs.getString("yansbh");
				mduifdd = rs.getString("duifdd");
				mfukfs = rs.getString("fukfs");
				mjiessl = rs.getDouble("jiessl");
				mbuhsdj = rs.getDouble("buhsdj");
				mjiakje = rs.getDouble("meikje");
				mbukyqjk = rs.getDouble("bukmk");
				mjiakhj = rs.getDouble("buhsmk");
				mjiaksl = rs.getDouble("shuil");
				mjiaksk = rs.getDouble("shuik");
				mjiasje = rs.getDouble("hansmk");
				mjieslx = rs.getInt("jieslx");
				mjiesslcy=rs.getDouble("jiesslcy");
				mdiancxxb_id=rs.getLong("diancxxb_id");
				mgongysb_id=rs.getLong("gongysb_id");
				mkoud_js=rs.getDouble("koud");
				myunsfsb_id=rs.getLong("yunsfsb_id");
				myingd=rs.getDouble("yingd");
				mkuid=rs.getDouble("kuid");
				myunju=rs.getString("yunj");
				mhetb_id=rs.getLong("HETB_ID");
				blnHasMeik=true;
				mfapbh=rs.getString("fapbh");
				mfengsjj=rs.getDouble("fengsjj");
				mjiajqdj=rs.getDouble("jiajqdj");
				mjijlx=rs.getInt("jijlx");
				mkuidjfyf_je=rs.getDouble("kuidjfyf");	//亏吨拒付运费金额
				mkuidjfzf_je=rs.getDouble("kuidjfzf");	//亏顿拒付杂费金额
				mchaokdl=Math.abs(rs.getDouble("chaokdl"));		//超亏吨量
				mchaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));		//超亏吨类型
				mYunfjsdj_mk=rs.getDouble("Yunfhsdj");
				mYunzfhj_mk=rs.getDouble("hansyf");
				mBuhsyf_mk=rs.getDouble("buhsyf");
				mYunfjsl_mk=rs.getDouble("yunfjsl");
				chongdjsb_id=rs.getLong("chongdjsb_id");
				
//				得到煤款单价小数位，和运费单价小数位
				String str_meikhsdjblxsw="";
				str_meikhsdjblxsw=Jiesdcz.getJiessz_item(rs.getLong("diancxxb_id"),rs.getLong("gongysb_id")
							,rs.getLong("hetb_id"),Locale.meikhsdjblxsw_jies,String.valueOf(getMeikhsdjblxsw()));
				
				if(str_meikhsdjblxsw!=null&&!str_meikhsdjblxsw.equals("")&&str_meikhsdjblxsw.matches("\\d+")){
					
					this.setMeikhsdjblxsw(Integer.parseInt(str_meikhsdjblxsw));
				}
				
				sql="select jieszbsjb.*,zhibb.bianm from jieszbsjb,"+table_mk+" js,zhibb "
					 + " where jieszbsjb.jiesdid=js.id and zhibb.id=jieszbsjb.zhibb_id "
					 + " and js.bianm='"+getJiesbhValue().getValue()
					 + "' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("bianm").equals(Locale.jiessl_zhibb)){
						
						mhetsl = rs2.getString("hetbz");
						mgongfsl =rs2.getDouble("gongf") ;
						mshulzjbz =rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje=rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.Shul_zhibb)){
						
						mShulzb_ht = rs2.getString("hetbz");
						mShulzb_yk = rs2.getDouble("yingk");
						mShulzb_zdj = rs2.getDouble("zhejbz");
						mShulzb_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("数量(吨)","Shulzb_ht","Shulzb_kf","Shulzb_cf","Shulzb_js","Shulzb_yk","Shulzb_zdj","Shulzb_zje",
								mShulzb_ht,mgongfsl,myanssl,mjiessl,mShulzb_yk,mShulzb_zdj,mShulzb_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qnetar_zhibb)){
						
						mQnetar_ht = rs2.getString("hetbz");// 合同热量
						mQnetar_kf = rs2.getDouble("gongf");// 供方热量
						mQnetar_cf = rs2.getDouble("changf");
						mQnetar_js = rs2.getDouble("jies");// 结算热量
						mQnetar_yk = rs2.getDouble("yingk");
						mQnetar_zdj = rs2.getDouble("zhejbz");
						mQnetar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qnetar(kcal/kg)","Qnetar_ht","Qnetar_kf","Qnetar_cf","Qnetar_js","Qnetar_yk","Qnetar_zdj","Qnetar_zje",
								mQnetar_ht,MainGlobal.Mjkg_to_kcalkg(mQnetar_kf,0),MainGlobal.Mjkg_to_kcalkg(mQnetar_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQnetar_js,0),mQnetar_yk,mQnetar_zdj,mQnetar_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Std_zhibb)){
						
						mStd_ht = rs2.getString("hetbz");	// 合同硫分
						mStd_kf = rs2.getDouble("gongf");
						mStd_cf =rs2.getDouble("changf");
						mStd_js = rs2.getDouble("jies");	// 结算硫分
						mStd_yk = rs2.getDouble("yingk");
						mStd_zdj = rs2.getDouble("zhejbz");
						mStd_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Std(%)","Std_ht","Std_kf","Std_cf","Std_js","Std_yk","Std_zdj","Std_zje",
								mStd_ht,mStd_kf,mStd_cf,mStd_js,mStd_yk,mStd_zdj,mStd_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Star_zhibb)){
						
						mStar_ht = rs2.getString("hetbz");	// 合同挥发分
						mStar_kf = rs2.getDouble("gongf");
						mStar_cf = rs2.getDouble("changf");
						mStar_js = rs2.getDouble("jies");		// 结算挥发分
						mStar_yk = rs2.getDouble("yingk");
						mStar_zdj = rs2.getDouble("zhejbz");
						mStar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Star(%)","Star_ht","Star_kf","Star_cf","Star_js","Star_yk","Star_zdj","Star_zje",
								mStar_ht,mStar_kf,mStar_cf,mStar_js,mStar_yk,mStar_zdj,mStar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Ad_zhibb)){
						
						mAd_ht = rs2.getString("hetbz");	// 合同挥发分
						mAd_kf = rs2.getDouble("gongf");
						mAd_cf = rs2.getDouble("changf");
						mAd_js = rs2.getDouble("jies");		// 结算挥发分
						mAd_yk = rs2.getDouble("yingk");
						mAd_zdj = rs2.getDouble("zhejbz");
						mAd_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Ad(%)","Ad_ht","Ad_kf","Ad_cf","Ad_js","Ad_yk","Ad_zdj","Ad_zje",
								mAd_ht,mAd_kf,mAd_cf,mAd_js,mAd_yk,mAd_zdj,mAd_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Vdaf_zhibb)){
						
						mVdaf_ht = rs2.getString("hetbz");	// 合同挥发分
						mVdaf_kf = rs2.getDouble("gongf");
						mVdaf_cf = rs2.getDouble("changf");
						mVdaf_js = rs2.getDouble("jies");		// 结算挥发分
						mVdaf_yk = rs2.getDouble("yingk");
						mVdaf_zdj = rs2.getDouble("zhejbz");
						mVdaf_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Vdaf(%)","Vdaf_ht","Vdaf_kf","Vdaf_cf","Vdaf_js","Vdaf_yk","Vdaf_zdj","Vdaf_zje",
								mVdaf_ht,mVdaf_kf,mVdaf_cf,mVdaf_js,mVdaf_yk,mVdaf_zdj,mVdaf_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Mt_zhibb)){
						
						mMt_ht = rs2.getString("hetbz");	// 合同挥发分
						mMt_kf = rs2.getDouble("gongf");
						mMt_cf = rs2.getDouble("changf");
						mMt_js = rs2.getDouble("jies");		// 结算挥发分
						mMt_yk = rs2.getDouble("yingk");
						mMt_zdj = rs2.getDouble("zhejbz");
						mMt_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Mt(%)","Mt_ht","Mt_kf","Mt_cf","Mt_js","Mt_yk","Mt_zdj","Mt_zje",
								mMt_ht,mMt_kf,mMt_cf,mMt_js,mMt_yk,mMt_zdj,mMt_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qgrad_zhibb)){
						
						mQgrad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQgrad_kf = rs2.getDouble("gongf");
						mQgrad_cf = rs2.getDouble("changf");
						mQgrad_js = rs2.getDouble("jies");		// 结算挥发分
						mQgrad_yk = rs2.getDouble("yingk");
						mQgrad_zdj = rs2.getDouble("zhejbz");
						mQgrad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qgrad(kcal/kg)","Qgrad_ht","Qgrad_kf","Qgrad_cf","Qgrad_js","Qgrad_yk","Qgrad_zdj","Qgrad_zje",
								mQgrad_ht,MainGlobal.Mjkg_to_kcalkg(mQgrad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQgrad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQgrad_js,0),mQgrad_yk,mQgrad_zdj,mQgrad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Qbad_zhibb)){
						
						mQbad_ht = rs2.getString("hetbz");		// 合同挥发分
						mQbad_kf = rs2.getDouble("gongf");
						mQbad_cf = rs2.getDouble("changf");
						mQbad_js = rs2.getDouble("jies");		// 结算挥发分
						mQbad_yk = rs2.getDouble("yingk");
						mQbad_zdj = rs2.getDouble("zhejbz");
						mQbad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Qbad(kcal/kg)","Qbad_ht","Qbad_kf","Qbad_cf","Qbad_js","Qbad_yk","Qbad_zdj","Qbad_zje",
								mQbad_ht,MainGlobal.Mjkg_to_kcalkg(mQbad_kf,0),MainGlobal.Mjkg_to_kcalkg(mQbad_cf,0),
								MainGlobal.Mjkg_to_kcalkg(mQbad_js,0),mQbad_yk,mQbad_zdj,mQbad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Had_zhibb)){
						
						mHad_ht = rs2.getString("hetbz");		// 合同挥发分
						mHad_kf = rs2.getDouble("gongf");
						mHad_cf = rs2.getDouble("changf");
						mHad_js = rs2.getDouble("jies");		// 结算挥发分
						mHad_yk = rs2.getDouble("yingk");
						mHad_zdj = rs2.getDouble("zhejbz");
						mHad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Had(%)","Had_ht","Had_kf","Had_cf","Had_js","Had_yk","Had_zdj","Had_zje",
								mHad_ht,mHad_kf,mHad_cf,mHad_js,mHad_yk,mHad_zdj,mHad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Stad_zhibb)){
						
						mStad_ht = rs2.getString("hetbz");		// 合同挥发分
						mStad_kf = rs2.getDouble("gongf");
						mStad_cf = rs2.getDouble("changf");
						mStad_js = rs2.getDouble("jies");		// 结算挥发分
						mStad_yk = rs2.getDouble("yingk");
						mStad_zdj = rs2.getDouble("zhejbz");
						mStad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Stad(%)","Stad_ht","Stad_kf","Stad_cf","Stad_js","Stad_yk","Stad_zdj","Stad_zje",
								mStad_ht,mStad_kf,mStad_cf,mStad_js,mStad_yk,mStad_zdj,mStad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Mad_zhibb)){
						
						mMad_ht = rs2.getString("hetbz");	// 合同挥发分
						mMad_kf = rs2.getDouble("gongf");
						mMad_cf = rs2.getDouble("changf");
						mMad_js = rs2.getDouble("jies");		// 结算挥发分
						mMad_yk = rs2.getDouble("yingk");
						mMad_zdj = rs2.getDouble("zhejbz");
						mMad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Mad(%)","Mad_ht","Mad_kf","Mad_cf","Mad_js","Mad_yk","Mad_zdj","Mad_zje",
								mMad_ht,mMad_kf,mMad_cf,mMad_js,mMad_yk,mMad_zdj,mMad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aar_zhibb)){
						
						mAar_ht = rs2.getString("hetbz");	// 合同挥发分
						mAar_kf = rs2.getDouble("gongf");
						mAar_cf = rs2.getDouble("changf");
						mAar_js = rs2.getDouble("jies");		// 结算挥发分
						mAar_yk = rs2.getDouble("yingk");
						mAar_zdj = rs2.getDouble("zhejbz");
						mAar_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Aar(%)","Aar_ht","Aar_kf","Aar_cf","Aar_js","Aar_yk","Aar_zdj","Aar_zje",
								mAar_ht,mAar_kf,mAar_cf,mAar_js,mAar_yk,mAar_zdj,mAar_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Aad_zhibb)){
						
						mAad_ht = rs2.getString("hetbz");	// 合同挥发分
						mAad_kf = rs2.getDouble("gongf");
						mAad_cf = rs2.getDouble("changf");
						mAad_js = rs2.getDouble("jies");		// 结算挥发分
						mAad_yk = rs2.getDouble("yingk");
						mAad_zdj = rs2.getDouble("zhejbz");
						mAad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Aad(%)","Aad_ht","Aad_kf","Aad_cf","Aad_js","Aad_yk","Aad_zdj","Aad_zje",
								mAad_ht,mAad_kf,mAad_cf,mAad_js,mAad_yk,mAad_zdj,mAad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Vad_zhibb)){
						
						mVad_ht = rs2.getString("hetbz");	// 合同挥发分
						mVad_kf = rs2.getDouble("gongf");
						mVad_cf = rs2.getDouble("changf");
						mVad_js = rs2.getDouble("jies");		// 结算挥发分
						mVad_yk = rs2.getDouble("yingk");
						mVad_zdj = rs2.getDouble("zhejbz");
						mVad_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("Vad(%)","Vad_ht","Vad_kf","Vad_cf","Vad_js","Vad_yk","Vad_zdj","Vad_zje",
								mVad_ht,mVad_kf,mVad_cf,mVad_js,mVad_yk,mVad_zdj,mVad_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.T2_zhibb)){
						
						mT2_ht = rs2.getString("hetbz");	// 合同挥发分
						mT2_kf = rs2.getDouble("gongf");
						mT2_cf = rs2.getDouble("changf");
						mT2_js = rs2.getDouble("jies");		// 结算挥发分
						mT2_yk = rs2.getDouble("yingk");
						mT2_zdj = rs2.getDouble("zhejbz");
						mT2_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("T2(%)","T2_ht","T2_kf","T2_cf","T2_js","T2_yk","T2_zdj","T2_zje",
								mT2_ht,mT2_kf,mT2_cf,mT2_js,mT2_yk,mT2_zdj,mT2_zje);
					
					}else if(rs2.getString("bianm").equals(Locale.Yunju_zhibb)){
						
						mYunju_ht = rs2.getString("hetbz");	// 合同挥发分
						mYunju_kf = rs2.getDouble("gongf");
						mYunju_cf = rs2.getDouble("changf");
						mYunju_js = rs2.getDouble("jies");		// 结算挥发分
						mYunju_yk = rs2.getDouble("yingk");
						mYunju_zdj = rs2.getDouble("zhejbz");
						mYunju_zje = rs2.getDouble("zhejje");
						
						mstrJieszb+=jsdcz.SetJieszb("运距(Km)","Yunju_ht","Yunju_kf","Yunju_cf","Yunju_js","Yunju_yk","Yunju_zdj","Yunju_zje",
								mYunju_ht,mYunju_kf,mYunju_cf,mYunju_js,mYunju_yk,mYunju_zdj,mYunju_zje);
					
					}
					
					this.setJieszb(mstrJieszb);
				}
				
				rs2.close();
				mbeiz = rs.getString("beiz");
//				double mdanjc = 0;
//				待定
				mranlbmjbr = rs.getString("ranlbmjbr");
				mranlbmjbrq =rs.getDate("ranlbmjbrq");
//				mkuidjf = 0;
				mjingz = rs.getDouble("guohl");
				mjiesrq =rs.getDate("jiesrq");
				mruzrq =rs.getDate("ruzrq");
				
				mjiesslblxsw=Jiesdcz.getJiessz_item(mdiancxxb_id, mgongysb_id, mhetb_id, Locale.jiesslblxsw_jies, mjiesslblxsw);
				if(mjiesslblxsw.equals("0")){
					
					myuns=Math.round((mjiessl-mjingz));
				}else{
					
					String strblxs="";
					for(int i=0;i<Integer.parseInt(mjiesslblxsw);i++){
						
						if(i==0){
							
							strblxs="10";
						}else{
							
							strblxs+="0";
						}
					}
					myuns=Math.round((mjiessl-mjingz)*Double.parseDouble(strblxs))/Double.parseDouble(strblxs);
				}
				
			}
//			煤款
;
//			 1, 两票结算;
//			 2, 煤款结算
//			 3, 国铁运费
//			 4, 地铁运费
			if(blnHasMeik&&mjieslx==1){
//				两票		运费
				sql="select * from "+table_yf+" where bianm='"+getJiesbhValue().getValue()+"' and fuid=0";
				rs=con.getResultSet(sql);
				if(rs.next()){
					myid=rs.getLong("id");
					myunfhsdj=rs.getDouble("hansdj");
					mtielyf=rs.getDouble("guotyf");
					mtielzf=rs.getDouble("guotzf");
					mbukyqyzf = rs.getDouble("bukyf");
					mjiskc=rs.getDouble("jiskc");
					mbuhsyf=rs.getDouble("buhsyf");
					myunfsl=rs.getDouble("shuil");
					myunfsk=rs.getDouble("shuik");
					myunzfhj=rs.getDouble("hansyf");
					myunfjsl=rs.getDouble("jiessl");
					mkuangqyf=rs.getDouble("kuangqyf");
					mkuangqzf=rs.getDouble("kuangqzf");
					mkuidjfyf_je=rs.getDouble("kuidjfyf");
					mkuidjfzf_je=rs.getDouble("kuidjfzf");
				}
				
			}else if(mjieslx!=2){
				
				sql=" select * from "+table_yf+" where bianm='"+getJiesbhValue().getValue()+"' and fuid=0";
				
					rs=con.getResultSet(sql);
					if(rs.next()){
						myid=rs.getLong("id");
						myunfhsdj=rs.getDouble("hansdj");
						mtianzdw =rs.getString("xianshr");
						mjiesbh = rs.getString("bianm");
						mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
						mdiancxxb_id=rs.getLong("diancxxb_id");
						mfahdw = rs.getString("gongysmc");
						mfaz =rs.getString("faz");
						mshoukdw = rs.getString("shoukdw");
						mfahksrq = rs.getDate("fahksrq");
						mfahjzrq = rs.getDate("fahjzrq");
						myansksrq = rs.getDate("yansksrq");
						myansjzrq =rs.getDate("yansjzrq");
						mkaihyh =rs.getString("kaihyh");
						mpinz = rs.getString("meiz");
						myuanshr = rs.getString("yuanshr");
						mgongfsl=rs.getDouble("gongfsl");
						myanssl=rs.getDouble("yanssl");
						myingksl=rs.getDouble("yingk");
						mzhangh = rs.getString("zhangh");
						mches = rs.getInt("ches");
						mxianshr =rs.getString("xianshr");
						mfapbh = rs.getString("fapbh");
						mdaibcc = rs.getString("daibch");
						myansbh =rs.getString("yansbh");
						mduifdd = rs.getString("duifdd");
						mfukfs = rs.getString("fukfs");
						mjiessl = rs.getDouble("jiessl");
						mtielyf=rs.getDouble("guotyf");
						mtielzf=rs.getDouble("guotzf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc=rs.getDouble("jiskc");
						mbuhsyf=rs.getDouble("buhsyf");
						myunfsl=rs.getDouble("shuil");
						myunfsk=rs.getDouble("shuik");
						myunzfhj=rs.getDouble("hansyf");
						mkuangqyf=rs.getDouble("kuangqyf");
						mkuangqzf=rs.getDouble("kuangqzf");
						myunsfsb_id=rs.getLong("yunsfsb_id");
						myingd=rs.getDouble("yingd");
						mkuid=rs.getDouble("kuid");
						myunju=rs.getString("yunj");
						mjingz=rs.getDouble("guohl");
						mjiesslcy=rs.getDouble("jiesslcy");
						mshulzjbz=rs.getDouble("hansdj");
						mkuidjfyf_je=rs.getDouble("kuidjfyf");
						mkuidjfzf_je=rs.getDouble("kuidjfzf");
						if (rs.getString("xiecf") != null) {
							mbeiz="卸车费：" + rs.getString("xiecf");
						}
						
//						得到运费单价小数位
						String str_yunfhsdjblxsw="";
						str_yunfhsdjblxsw=Jiesdcz.getJiessz_item(rs.getLong("diancxxb_id"),rs.getLong("gongysb_id")
									,rs.getLong("hetb_id"),Locale.yunfhsdjblxsw_jies,String.valueOf(getYunfhsdjblxsw()));
						
						if(str_yunfhsdjblxsw!=null&&!str_yunfhsdjblxsw.equals("")&&str_yunfhsdjblxsw.matches("\\d+")){
							
							this.setYunfhsdjblxsw(Integer.parseInt(str_yunfhsdjblxsw));
						}
					}
			}
			
				if(((Visit) getPage().getVisit()).getDouble2()>0
					||((Visit) getPage().getVisit()).getDouble3()>0){
						
					
					mkuangqyf=((Visit) getPage().getVisit()).getDouble2();
					mkuangqzf=((Visit) getPage().getVisit()).getDouble3();
					mkuangqsk=((Visit) getPage().getVisit()).getDouble4();
					mkuangqjk=((Visit) getPage().getVisit()).getDouble5();
					myunzfhj=Math.round((mtielyf+mtielzf+mkuangqyf+mkuangqzf+mbukyqyzf)*100)/100;
					myunfsk=(double)Math.round(((double)Math.round((mtielyf+mbukyqyzf)*myunfsl*100)/100+((Visit) getPage().getVisit()).getDouble4())*100)/100;
					mbuhsyf=(double)Math.round(((double)Math.round((myunzfhj-myunfsk)*100)/100+((Visit) getPage().getVisit()).getDouble5())*100)/100;
				}
			
			mmeikhjdx=getDXMoney(mjiasje);
			myunzfhjdx=getDXMoney(myunzfhj);
			mhej=mjiasje+myunzfhj;
			mdaxhj=getDXMoney(mhej);
			
//			设置超/亏吨的显示	
			if(!mchaokdlx.equals("")){
//				说明存在超亏吨
				this.setHejdxh(jsdcz.SetHejdxh(mchaokdlx,mchaokdl,mhej,mdaxhj));
			}else{
				
				this.setHejdxh(jsdcz.SetHejdxh("",0,mhej,mdaxhj));
			}
			
			_editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw,mfaz, myunsfsb_id, mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, Jiesdcz.nvlStr(mfapbh), mdaibcc, Jiesdcz.nvlStr(myansbh),
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl,  myingd, mkuid, mshulzjje,
					mjiessl, mjiesslcy,myunfjsl,mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,myunju,mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,mfengsjj,mjiajqdj,mjijlx,
					mMjtokcalxsclfs,mkuidjfyf_je,mkuidjfzf_je,mchaokdl,
					mchaokdlx,myunfhsdj,mYunfjsdj_mk,mYunzfhj_mk,mBuhsyf_mk,
					mYunfjsl_mk,chongdjsb_id,0,0));
//			超吨亏吨标识符
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
//		厂别下拉框
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"Form0",null,getTreeid());
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
		
		tb1.addText(new ToolbarText("结算日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.setId("briq");
		dfb.Binding("BRiq", "forms[0]");
//		dfb.setListeners("change:function(){document.forms[0].submit();}");
		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText("至:"));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.setId("eriq");
		dfe.Binding("ERiq", "forms[0]");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
//		结算编号
		tb1.addText(new ToolbarText("结算单编号:"));
		ComboBox JiesbhDropDown = new ComboBox();
		JiesbhDropDown.setId("JiesbhDrop");
		JiesbhDropDown.setWidth(200);
		JiesbhDropDown.setLazyRender(true);
		JiesbhDropDown.setTransform("JIESBHDropDown");
		tb1.addField(JiesbhDropDown);
		tb1.addText(new ToolbarText("-"));
		
//		刷新
		ToolbarButton shuaxbt=new ToolbarButton(null,"刷新","function(){ document.Form0.RetrunsButton.click();}");
		shuaxbt.setId("Shuaxbt");
		tb1.addItem(shuaxbt);
		tb1.addText(new ToolbarText("-"));
		
//		保存
//		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click(); }");
//		savebt.setId("savebt");
//		tb1.addItem(savebt);
		
		String handler = 
			"function(){\n" +
			"    if(!win) {\n" + 
			"        var bilForm = new Ext.form.FormPanel({\n" + 
			"            frame: true,\n" + 
			"            layout: 'table',\n" + 
			"            bodyStyle: 'padding:20px 0px 0px 35px',\n" + 
			"            items:[\n" + 
			"                {html:'<font size=2>大唐阳城拆分比率:&nbsp;</font>'},\n" + 
			"                bilf,\n" +
			"				 {html:'<font size=2>&nbsp;%</font>'}" +
			"            ]\n" + 
			"        });\n" + 
			"\n" + 
			"        win = new Ext.Window({\n" + 
			"            el:'hello-win',\n" + 
			"            layout:'fit',\n" + 
			"            width:280,\n" + 
			"            height:150,\n" + 
			"            closeAction:'hide',\n" + 
			"            plain: true,\n" + 
			"            title:'提示',\n" + 
			"            modal:true,\n" + 
			"            items: [bilForm],\n" + 
			"            buttons: [\n" + 
			"                {\n" + 
			"                    text:'确定',\n" + 
			"                    handler:function(){\n" + 
			"						var patrn=/^[0-9]{1,2}\\.[0-9]{1,2}$|^[0-9]{1,2}$|^100$|^100.0$/;\n" +
			"						if (patrn.exec(bilf.getValue())) {\n" + 
			"    						document.getElementById('Bil').value=bilf.getValue();\n" + 
			"    						document.getElementById('ChaifButton').click();\n" + 
			"    						bilf.reset();\n" + 
			"    						win.hide();\n" + 
			"						} else {\n" + 
			"    						Ext.MessageBox.alert('提示信息','请输入小于等于100的整数或小数,且小数保留两位！');\n" + 
			"    						return;\n" + 
			"						}" +
			"                    }\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    text: '取消',\n" + 
			"                        handler: function(){\n" + 
			"                        win.hide();\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        });\n" + 
			"    }\n" + 
			"	 win.show(this);" +
			"}";
		ToolbarButton chaifbtn = new ToolbarButton("chaif_Button", "拆分", handler);
		tb1.addItem(chaifbtn);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("-"));
//		删除
//		ToolbarButton deletebt=new ToolbarButton(null,"删除","function(){ document.Form0.DeleteButton.click(); }");
//		deletebt.setId("deletebt");
//		tb1.addItem(deletebt);
//		tb1.addText(new ToolbarText("-"));
//		删除
//		ToolbarButton kuangqzfbt=new ToolbarButton(null,"矿区杂费修改","function(){ document.Form0.KuangqzfButton.click(); }");
//		kuangqzfbt.setId("kuangqzfbt");
//		tb1.addItem(kuangqzfbt);
//		tb1.addText(new ToolbarText("-"));
//		提交进入流程
//		ToolbarButton submitbt=new ToolbarButton(null,"提交进入流程","function(){  \n"
//				+ " if(!win){	\n" 
//				+ "	\tvar form = new Ext.form.FormPanel({	\n" 
//				+ " \tbaseCls: 'x-plain',	\n" 		
//				+ " \tlabelAlign:'right',	\n" 
//				+ " \tdefaultType: 'textfield',	\n"
//				+ " \titems: [{		\n"
//				+ " \txtype:'fieldset',	\n"
//				+ " \ttitle:'请选择流程名称',	\n"
//				+ " \tautoHeight:false,	\n"
//				+ " \theight:220,	\n"
//				+ " \titems:[	\n"
//        		+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
//				+ " \twidth:150,	\n"
//				+ " \tid:'lcmccb',	\n"
//				+ " \tselectOnFocus:true,	\n"
//				+ "	\ttransform:'LiucmcDropDown',	\n"						
//				+ " \tlazyRender:true,	\n"	
//				+ " \tfieldLabel:'流程名称',		\n" 
//				+ " \ttriggerAction:'all',	\n"
//				+ " \ttypeAhead:true,	\n"	
//				+ " \tforceSelection:true,	\n"
//				+ " \teditable:false	\n"					
//				+ " \t})	\n"
//				+ " \t]		\n"
//				+ " \t}]	\n"		
//				+ " \t});	\n"
//				+ " \twin = new Ext.Window({	\n"
//				+ " \tel:'hello-win',	\n"
//				+ " \tlayout:'fit',	\n"
//				+ " \twidth:500,	\n"	
//				+ " \theight:300,	\n"
//				+ " \tcloseAction:'hide',	\n"
//				+ " \tplain: true,	\n"
//				+ " \ttitle:'流程',	\n"
//				+ " \titems: [form],	\n"
//				+ " \tbuttons: [{	\n"
//				+ " \ttext:'确定',	\n"
//				+ " \thandler:function(){	\n"  
//				+ " \twin.hide();	\n"
//				+ " \tif(lcmccb.getRawValue()=='请选择'){		\n" 
//				+ "	\t	alert('请选择流程名称！');		\n"
//				+ " \t}else{" 
//				+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
//				+ " \t\t document.all.item('QuedButton').click();	\n"
//				+ " \t}	\n"
//				+ " \t}	\n"   
//				+ " \t},{	\n"
//				+ " \ttext: '取消',	\n"
//				+ " \thandler: function(){	\n"
//				+ " \twin.hide();	\n"
//				+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"	
//				+ " \t}		\n"
//				+ " \t}]	\n"
//				+ " \t});}	\n" 
//				+ " \twin.show(this);	\n"
//
//				+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
//				+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
//				+ " \t}	\n"	
//				+ " \t}");
//		submitbt.setId("submitbt");
//		tb1.addItem(submitbt);
		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);								//状态 判断是修改合同号还是提交进入流程
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			visit.setboolean2(false);//分公司
			visit.setboolean3(false);//电厂
			visit.setString2("");	 //结算指标页面显示
			((Visit) getPage().getVisit()).setString11("");	//用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)
			
			((Visit) getPage().getVisit()).setString2("");	//指标显示
			((Visit) getPage().getVisit()).setString3("");	//合计大写行显示内容（为实现动态设置“超扣吨”显示用）
			((Visit) getPage().getVisit()).setString4("");	//结算单位（用来保存页面设置的结算单位，可能是 电厂id 或 分公司id），
															//在结算编号下拉框和getselectdate()、save()、Submit()、Delete() 
															//方法中进行判断时使用
			
			((Visit) getPage().getVisit()).setString5("");	//用于处理 填写增扣款后是否重算结算单价
			((Visit) getPage().getVisit()).setInt2(2);		//煤款含税单价保留小数位
			((Visit) getPage().getVisit()).setInt3(2);		//运费含税单价保留小数位
			
			if(visit.getRenyjb()<3){
				
				visit.setboolean3(true);
			}	

			setLiucmcValue(null);
			setILiucmcModel(null);
			
			setJiesbhValue(null);
			setIJiesbhModel(null);
			
			setDiancmcModel(null);
			setDiancmcValue(null);
			
			visit.setboolean1(false);//共用(目前是电厂数)
			visit.setboolean5(false);//合同编号显示
			visit.setboolean6(false);//流程名称显示
			
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			if(cycle.getRequestContext().getParameters("jsdwid")!=null){
				
				((Visit) this.getPage().getVisit()).setString4(cycle.getRequestContext().getParameters("jsdwid")[0].trim());
			}
			
			getILiucmcModels();
			getIShoukdwModels();
			getIJiesbhModels();
			getSelectData();
			
		}
		getToolbars();
		getIJiesbhModels();
		if(visit.getboolean1()){
			visit.setboolean1(false);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
		}
	}
	public String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}

	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setHejdxh(String value) {

		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getHejdxh() {

		return ((Visit) getPage().getVisit()).getString3();
	}
	
	//工具条_begin
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	//工具条_end
	
	public boolean isGongsdp() {
		return ((Visit) getPage().getVisit()).getboolean2();
	}

	public void setGongsdp(boolean gongsdp) {
		((Visit) getPage().getVisit()).setboolean2(gongsdp);
	}
	
	public boolean isDiancdp() {
		return ((Visit) getPage().getVisit()).getboolean3();
	}

	public void setDiancdp(boolean diancdp) {
		((Visit) getPage().getVisit()).setboolean3(diancdp);
	}
	
	
    public IDropDownBean getShoukdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9((IDropDownBean)getIShoukdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean9();
    }
    
    public void setShoukdwValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()!=value){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9(value);
        }
    }
    
    public void setIShoukdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel9(value);
	}
    
    public IPropertySelectionModel getIShoukdwModel(){
            
    	if(((Visit)getPage().getVisit()).getProSelectionModel9()==null){
        
    		getIShoukdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel9();
    }
    
    public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select shoukdw from (	\n"
                 	+ " select distinct gongfdwmc as shoukdw from hetb h	\n"
                 	+ " union	\n"
                 	+ " select distinct quanc as shoukdw from shoukdw	\n"
                 	+ " union	\n"
					+ " select gongfdwmc as shoukdw from hetys \n"
                 	+ " ) order by shoukdw";	
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("shoukdw")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    
//   结算编号 DropDownBean6
//   结算编号 ProSelectionModel6
    
    public void setIJiesbhModel(IPropertySelectionModel value){
        
        ((Visit)getPage().getVisit()).setProSelectionModel6(value);
    }
    
    public IPropertySelectionModel getIJiesbhModel(){
    	if(((Visit)getPage().getVisit()).getProSelectionModel6()==null){
    		getIJiesbhModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IPropertySelectionModel getIJiesbhModels(){
    	
    	String sql = "";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
    	String talbe_mk = "jiesb";
    	String table_yf = "jiesyfb";
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).isDCUser()){
    			
    			if(Jiesdcz.isFengsj(((Visit)getPage().getVisit()).getString4())){
    				
    				talbe_mk = "kuangfjsmkb";
    				table_yf = "kuangfjsyfb";
    			}
    			
    			sql = 
    				"select mk.bianm\n" +
    				"  from "+ talbe_mk +" mk\n" + 
    				" where mk.diancxxb_id = "+ getTreeid() +"\n" + 
    				"   and mk.liucztb_id = 0\n" + 
    				"	and mk.jiesrq >= to_date('"+ getBRiq() +"', 'yyyy-mm-dd')\n" +
    				"	and mk.jiesrq <= to_date('"+ getERiq() +"', 'yyyy-mm-dd')" +
    				"   and not exists\n" + 
    				" (select zb.bianm from jieszb zb where mk.bianm = zb.bianm)\n" + 
    				"union\n" + 
    				"select yf.bianm\n" + 
    				"  from "+ table_yf +" yf\n" + 
    				" where yf.diancxxb_id = "+ getTreeid() +"\n" + 
    				"   and yf.liucztb_id = 0\n" + 
    				"	and yf.jiesrq >= to_date('"+ getBRiq() +"', 'yyyy-mm-dd')\n" +
    				"	and yf.jiesrq <= to_date('"+ getERiq() +"', 'yyyy-mm-dd')" +
    				"   and not exists\n" + 
    				" (select zb.bianm from jiesyfzb zb where yf.bianm = zb.bianm)\n" + 
    				"order by bianm";

    		}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){
    			
    			List.add(new IDropDownBean(i++, rs.getString("bianm")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	
    	((Visit)getPage().getVisit()).setProSelectionModel6(new IDropDownModel(List));
		return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IDropDownBean getJiesbhValue() {
    	
    	if(((Visit)getPage().getVisit()).getDropDownBean6()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean6((IDropDownBean)getIJiesbhModel().getOption(0));
    	}
    	
		return ((Visit)getPage().getVisit()).getDropDownBean6();
	}
	
    public void setJiesbhValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean6()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}
//    end
    
	
//	流程名称 DropDownBean8
//  流程名称 ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   流程名称 end
	
//***************************************************************************//
	public boolean getRaw() {
		return true;
	}

//	格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
	public IPropertySelectionModel getIFahdwModels(){
    	
    	String sql="";
    	String table_mk = "jiesb";
    	String table_yf = "jiesyfb";
    	
    	if(Jiesdcz.isFengsj(((Visit)getPage().getVisit()).getString4())){
//    		分公司结算
    		table_mk = "kuangfjsmkb";
    		table_yf = "kuangfjsyfb";
    	}
    		
    		if(((Visit)getPage().getVisit()).isDCUser()){
        		
        		sql=" select id,gongysmc from "
    				+ " (select distinct gongysb_id as id,gongysmc from "+table_mk+" where diancxxb_id="+this.getTreeid()+" and fuid=0 and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from "+table_yf+" where diancxxb_id="+this.getTreeid()+" and fuid=0 and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).isGSUser()){
        		
        		sql=" select id,gongysmc from "
    				+ " (select distinct gongysb_id as id,gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+this.getTreeid()+" and fuid=0 and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+this.getTreeid()+" and fuid=0 and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).isJTUser()){
        		
        		sql="select id,gongysmc from "
    				+ " (select distinct gongysb_id as id,gongysmc from diancjsmkb where fuid=0 and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from diancjsyfb where fuid=0 and liucztb_id=0) order by gongysmc";
        	}
 
    	((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));
        return ((Visit)getPage().getVisit()).getProSelectionModel2();
    }
	

    //********************************************************************************//
    
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
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
				((Visit) this.getPage().getVisit()).setboolean1(true);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
    
//		电厂名称
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
		String sql = "(select id,mingc from diancxxb where id="
			+((Visit) getPage().getVisit()).getDiancxxb_id()
			+") union (select id,mingc from diancxxb where fuid="
			+((Visit) getPage().getVisit()).getDiancxxb_id()+") order by mingc";
		
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
}