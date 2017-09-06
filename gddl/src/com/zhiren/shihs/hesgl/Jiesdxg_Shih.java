package com.zhiren.shihs.hesgl;

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

import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
 
public class Jiesdxg_Shih extends BasePage {

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
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh().equals("")){
	        	if(Jiesdcz.checkbh("shihjsb","shihjsyfb",((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh(),((Dcbalancebean_Shih)getEditValues().get(0)).getId(),((Dcbalancebean_Shih)getEditValues().get(0)).getYid())){
	        				
			        		sql=" update shihjsb set bianm = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh()+"',"
							       + " shihgysb_id = "+getProperId(getIFahdwModels(),((Dcbalancebean_Shih)getEditValues().get(0)).getFahdw())+","
							       + " gongysmc = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFahdw()+"',"
							       + " faz = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFaz()+"',"
							       + " fahksrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
							       + " fahjzrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"
							       + " meiz = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getPinz()+"'," 
							       + " yuanshr ='"+((Dcbalancebean_Shih)getEditValues().get(0)).getYuanshr()+"',xianshr = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getXianshr()+"',"
							       + " yansksrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd')," 
							       + " yansjzrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),"
							       + " shoukdw ='"+((Dcbalancebean_Shih)getEditValues().get(0)).getShoukdw()+"',kaihyh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getKaihyh()+"',"
							       + " zhangh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getZhangh()+"',"
							       + " fapbh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFapbh()+"',fukfs = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFukfs()+"',"
							       + " duifdd = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getDuifdd()+"',ches ="+((Dcbalancebean_Shih)getEditValues().get(0)).getChes()+","
							       + " jiessl = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiessl()+",guohl = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJingz()+","
							       + " hansdj = "+((Dcbalancebean_Shih)getEditValues().get(0)).getShulzjbz()+",bukk = "+((Dcbalancebean_Shih)getEditValues().get(0)).getBukyqjk()+","
							       + " hansje = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiasje()+",buhsje = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiakhj()+","
							       + " shuik = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiaksk()+",shuil = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiaksl()+","
							       + " buhsdj = "+((Dcbalancebean_Shih)getEditValues().get(0)).getBuhsdj()+","
								   + " beiz = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getBeiz()+"',meikje = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiakje()
							       + " where id ="+((Dcbalancebean_Shih)getEditValues().get(0)).getId()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		sql=" update shihjsyfb set bianm = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh()+"',	\n shihgysb_id = "+getProperId(getIFahdwModels(),((Dcbalancebean_Shih)getEditValues().get(0)).getFahdw())+",	\n"
			        			+ " gongysmc = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFahdw()+"',	\n faz = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFaz()+"',	\n fahksrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " fahjzrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),	\n meiz ='"+((Dcbalancebean_Shih)getEditValues().get(0)).getPinz()+"',	\n daibch ='"+((Dcbalancebean_Shih)getEditValues().get(0)).getDaibcc()+"',	\n"
			        			+ " yuanshr = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getYuanshr()+"',	\n xianshr = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getXianshr()+"',	\n yansksrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " yansjzrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),	\n yansbh ='"+((Dcbalancebean_Shih)getEditValues().get(0)).getYansbh()+"',	\n shoukdw = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getShoukdw()+"',	\n"
			        			+ " kaihyh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getKaihyh()+"',	\n zhangh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getZhangh()+"',	\n fapbh = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFapbh()+"',	\n"
			        			+ " fukfs = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getFukfs()+"',	\n duifdd = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getDuifdd()+"',	\n ches ="+((Dcbalancebean_Shih)getEditValues().get(0)).getChes()+",		\n"
			        			+ " jiessl = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJiessl()+",	\n guohl = "+((Dcbalancebean_Shih)getEditValues().get(0)).getJingz()+",	\n guotyf = "+((Dcbalancebean_Shih)getEditValues().get(0)).getTielyf()+",		\n"
			        			+ " jiskc = "+((double)Math.round((((Dcbalancebean_Shih)getEditValues().get(0)).getTielzf()+((Dcbalancebean_Shih)getEditValues().get(0)).getKuangqzf())*100)/100)+",	\n bukyf ="+((Dcbalancebean_Shih)getEditValues().get(0)).getBukyqyzf()+",	\n hansyf = "+((Dcbalancebean_Shih)getEditValues().get(0)).getYunzfhj()+",	\n"
			        			+ " buhsyf = "+((Dcbalancebean_Shih)getEditValues().get(0)).getBuhsyf()+",	\n shuik = "+((Dcbalancebean_Shih)getEditValues().get(0)).getYunfsk()+",	\n shuil ="+((Dcbalancebean_Shih)getEditValues().get(0)).getYunfsl()+",	\n jiesrq = to_date('"+this.FormatDate(((Dcbalancebean_Shih)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),	\n"
			        			+ " beiz = '"+((Dcbalancebean_Shih)getEditValues().get(0)).getBeiz()+"'	\n where id = "+((Dcbalancebean_Shih)getEditValues().get(0)).getYid()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		setMsg("结算单更新成功！");
		        		
			        		Chengbcl.CountCb_js(((Visit) getPage().getVisit()).getDiancxxb_id()+"",((Dcbalancebean_Shih)getEditValues().get(0)).getId(),
			        				((Dcbalancebean_Shih)getEditValues().get(0)).getYid());
	        		
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
	}
	
	
	private void Delete(){
	    JDBCcon con=new JDBCcon();
	    try{
	    	StringBuffer strbf=new StringBuffer();
	    	if(getEditValues()!= null && !getEditValues().isEmpty()){

	    		strbf.append("begin		\n");
	    		if(((Dcbalancebean_Shih)getEditValues().get(0)).getId()>0){
	    			
	    			strbf.append(" delete from shihjsb where id="+((Dcbalancebean_Shih)getEditValues().get(0)).getId()+";	\n");
	    			
	    			strbf.append(" delete from shihjszbsjb where jiesdid="+((Dcbalancebean_Shih)getEditValues().get(0)).getId()+";	\n");
	    			
	    			strbf.append(" update shihcpb set shihjsb_id=0 where shihjsb_id="+((Dcbalancebean_Shih)getEditValues().get(0)).getId()+";	\n");
	    		}
	    		
	    		if(((Dcbalancebean_Shih)getEditValues().get(0)).getYid()>0){
	    			
	    			strbf.append("delete from shihjsyfb where id="+((Dcbalancebean_Shih)getEditValues().get(0)).getYid()+";	\n");
	    			
//	    			strbf.append("update shihcpb set yunfjsb_id=0 where yunfjsb_id="+((Dcbalancebean_Shih)getEditValues().get(0)).getYid()+";	\n");
	    			
	    			strbf.append("delete from jieszfb where jiesyfb_id="+((Dcbalancebean_Shih)getEditValues().get(0)).getYid()+";	\n");
	    		}
	    		
	    		strbf.append("end;	\n");
	    		
	    		if(con.getUpdate(strbf.toString())>=0){
	    			
	    			setMsg("编号："+((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh()+"结算单已删除！");
	    		}else{
	    			
	    			setMsg("编号："+((Dcbalancebean_Shih)getEditValues().get(0)).getJiesbh()+"结算单删除失败！");
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
			_editvalues.add(new Dcbalancebean_Shih());
			setEditValues(_editvalues);
	    }
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Dcbalancebean_Shih _EditValue;
	public Dcbalancebean_Shih getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Dcbalancebean_Shih EditValue) {
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

			long id = 0;
			long yid = 0;
			long diancxxb_id=0;
			long gongysb_id=0;
			String tianzdw ="";
			String jiesbh = "";
			String fahdw = "";
			String faz = "";
			String shoukdw = "";
			Date fahksrq = new Date();
			Date fahjzrq = new Date();
			Date yansksrq = new Date();
			Date yansjzrq = new Date();
			String fahrq="";
			String kaihyh = "";
			String pinz = "";
			String yuanshr = tianzdw;
			String zhangh = "";
			String hetsl = "";// 合同数量
			double gongfsl = 0;
			long ches = 0;
			String xianshr = yuanshr;
			String fapbh = "";
			String daibcc = "";
			String yansbh = "";
			String duifdd = "";
			String fukfs = "";
			double shulzjbz = 0;
			double yanssl = 0;
			double yingksl = 0;
			double shulzjje = 0;
			
			String CaO_ht = "";		// 合同热量
			double CaO_kf = 0;		// 供方热量
			double CaO_cf = 0;		// 厂方热量
			double CaO_js = 0;		// 厂方结算
			double CaO_yk = 0;		// 厂方盈亏
			double CaO_zdj = 0;		// 折单价
			double CaO_zje = 0;		// 这金额
			
			String MgO_ht="";			//合同硫分
			double MgO_kf=0;			//供方热量
			double MgO_cf=0;			//厂方热量
			double MgO_js=0;			//结算热量
			double MgO_yk = 0;			// 厂方盈亏
			double MgO_zdj = 0;		// 折单价
			double MgO_zje = 0;		// 这金额
			
			String Xid_ht="";			//合同硫分
			double Xid_kf=0;			//供方热量
			double Xid_cf=0;			//厂方热量
			double Xid_js=0;			//结算热量
			double Xid_yk = 0;			// 厂方盈亏
			double Xid_zdj = 0;			// 折单价
			double Xid_zje = 0;			// 这金额
			
			long hetb_id=0; 
			double koud_js=0;
			double jiessl = 0;
			double yunfjsl = 0;
			double buhsdj = 0;
			double jiakje = 0;
			double bukyqjk = 0;
			double jiakhj = 0;
			double jiaksl = 0.13;
			double jiaksk = 0;
			double jiasje = 0;
			double tielyf = 0;
			double tielzf = 0;
			double kuangqyf=0;
			double kuangqzf=0;
			double kuangqsk=0;
			double kuangqjk=0;
			double bukyqyzf = 0;
			double jiskc = 0;
			double buhsyf = 0;
			double yunfsl = 0.07;
			double yunfsk = 0;
			double yunzfhj = 0;
			double hej = 0;
			String daxhj = "";
			String meikhjdx = "";
			String yunzfhjdx = "";
			String beiz = "";
			String ranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date ranlbmjbrq = new Date();
			double kuidjf = 0;
			double jingz = 0;
			Date jiesrq = new Date();
			String changcwjbr = "";
			Date changcwjbrq = null;
			Date ruzrq = null;
			String jieszxjbr = "";
			Date jieszxjbrq = null;
			String gongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date gongsrlbjbrq = new Date();
			double hetjg = 0;
			long jieslx = 3;
			double yuns = 0;
			String jiesslblxsw="0";
			String yunsfs = "";
			String strJieszb="";
			boolean blnHasMeik=false;
			Money mn=new Money();
			Jiesdcz jsdcz=new Jiesdcz();
			double jiesslcy=0;
			long yunsfsb_id=0;
			double yingd=0;
			double kuid=0;
			double yunju=0;		//运距
			
//			进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
//			结算时要判断有无这个id，如果有就一定要用这个id
			long Meikjsb_id=0;
			long Yunfjsb_id=0;
			long Jihkjb_id=0;
			
			String sql="select * from shihjsb  where bianm='"+getJiesbhValue().getValue()+"'";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				id = rs.getLong("id");
				tianzdw =rs.getString("xianshr");
				jiesbh = rs.getString("bianm");
				fahdw = rs.getString("gongysmc");
				faz =rs.getString("faz");
				shoukdw = rs.getString("shoukdw");
				fahksrq = rs.getDate("fahksrq");
				fahjzrq = rs.getDate("fahjzrq");
				yansksrq = rs.getDate("yansksrq");
				yansjzrq =rs.getDate("yansjzrq");
				kaihyh =rs.getString("kaihyh");
				pinz = rs.getString("meiz");
				yuanshr = rs.getString("yuanshr");
				zhangh = rs.getString("zhangh");
				ches = rs.getInt("ches");
				xianshr =rs.getString("xianshr");
				daibcc = rs.getString("daibch");
//				yansbh =rs.getString("yansbh");
				duifdd = rs.getString("duifdd");
				fukfs = rs.getString("fukfs");
				jiessl = rs.getDouble("jiessl");
				buhsdj = rs.getDouble("buhsdj");
				jiakje = rs.getDouble("meikje");
				bukyqjk = rs.getDouble("bukk");
				jiakhj = rs.getDouble("buhsje");
				jiaksl = rs.getDouble("shuil");
				jiaksk = rs.getDouble("shuik");
				jiasje = rs.getDouble("hansje");
				jieslx = rs.getInt("jieslx");
				fapbh = rs.getString("fapbh");
//				jiesslcy=rs.getDouble("jiesslcy");
				diancxxb_id=rs.getLong("diancxxb_id");
				gongysb_id=rs.getLong("shihgysb_id");
//				koud_js=rs.getDouble("koud");
//				myunsfsb_id=rs.getLong("yunsfsb_id");
//				yingd=rs.getDouble("yingd");
//				kuid=rs.getDouble("kuid");
//				yunju=rs.getDouble("yunj");
				hetb_id=rs.getLong("HETB_ID");
				
				sql="select shihjszbsjb.*,zhibb.bianm from shihjszbsjb,shihjsb,zhibb "
					 + " where shihjszbsjb.jiesdid=shihjsb.id and zhibb.id=shihjszbsjb.zhibb_id and zhibb.leib=2"
					 + " and shihjsb.bianm='"+getJiesbhValue().getValue()+"' and shihjszbsjb.zhuangt=1";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("bianm").equals(Locale.jiessl_zhibb)){
						
						hetsl = rs2.getString("hetbz");
						gongfsl =rs2.getDouble("gongf") ;
						shulzjbz =rs2.getDouble("zhejbz");
						yanssl = rs2.getDouble("changf");
						yingksl = rs2.getDouble("yingk");
						shulzjje=rs2.getDouble("zhejje");
						
					}else if(rs2.getString("bianm").equals(Locale.CaO_zhibb)){
						
						CaO_ht = rs2.getString("hetbz");// 合同热量
						CaO_kf = rs2.getDouble("gongf");// 供方热量
						CaO_cf = rs2.getDouble("changf");
						CaO_js = rs2.getDouble("jies");// 结算热量
						CaO_yk = rs2.getDouble("yingk");
						CaO_zdj = rs2.getDouble("zhejbz");
						CaO_zje = rs2.getDouble("zhejje");
						
						strJieszb+=jsdcz.SetJieszb("CaO(%)","CaO_ht","CaO_kf","CaO_cf","CaO_js","CaO_yk","CaO_zdj","CaO_zje",
								CaO_ht,CaO_kf,CaO_cf,
								MainGlobal.Mjkg_to_kcalkg(CaO_js,0),CaO_yk,CaO_zdj,CaO_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.MgO_zhibb)){
						
						MgO_ht = rs2.getString("hetbz");	// 合同硫分
						MgO_kf = rs2.getDouble("gongf");
						MgO_cf =rs2.getDouble("changf");
						MgO_js = rs2.getDouble("jies");	// 结算硫分
						MgO_yk = rs2.getDouble("yingk");
						MgO_zdj = rs2.getDouble("zhejbz");
						MgO_zje = rs2.getDouble("zhejje");
						
						strJieszb+=jsdcz.SetJieszb("MgO(%)","MgO_ht","MgO_kf","MgO_cf","MgO_js","MgO_yk","MgO_zdj","MgO_zje",
								MgO_ht,MgO_kf,MgO_cf,MgO_js,MgO_yk,MgO_zdj,MgO_zje);
						
					}else if(rs2.getString("bianm").equals(Locale.Xid_zhibb)){
						
						Xid_ht = rs2.getString("hetbz");	// 合同挥发分
						Xid_kf = rs2.getDouble("gongf");
						Xid_cf = rs2.getDouble("changf");
						Xid_js = rs2.getDouble("jies");		// 结算挥发分
						Xid_yk = rs2.getDouble("yingk");
						Xid_zdj = rs2.getDouble("zhejbz");
						Xid_zje = rs2.getDouble("zhejje");
						
						strJieszb+=jsdcz.SetJieszb("Xid(%)","Xid_ht","Xid_kf","Xid_cf","Xid_js","Xid_yk","Xid_zdj","Xid_zje",
								Xid_ht,Xid_kf,Xid_cf,Xid_js,Xid_yk,Xid_zdj,Xid_zje);
						
					}
					
					this.setJieszb(strJieszb);
				}
				
				rs2.close();
				beiz = rs.getString("beiz");
//				待定
//				ranlbmjbr = rs.getString("ranlbmjbr");
//				ranlbmjbrq =rs.getDate("ranlbmjbrq");

				jingz = rs.getDouble("guohl");
				jiesrq =rs.getDate("jiesrq");
				ruzrq =rs.getDate("ruzrq");
				
				jiesslblxsw=Jiesdcz.getJiessz_item(diancxxb_id, gongysb_id, hetb_id, Locale.jiesslblxsw_jies, jiesslblxsw);
				if(jiesslblxsw.equals("0")){
					
					yuns=Math.round((jiessl-jingz));
				}else{
					
					String strblxs="";
					for(int i=0;i<Integer.parseInt(jiesslblxsw);i++){
						
						if(i==0){
							
							strblxs="10";
						}else{
							
							strblxs+="0";
						}
					}
					yuns=Math.round((jiessl-jingz)*Double.parseDouble(strblxs))/Double.parseDouble(strblxs);
				}
				
			}

			
				if(((Visit) getPage().getVisit()).getDouble2()>0
					||((Visit) getPage().getVisit()).getDouble3()>0){
						
					
					kuangqyf=((Visit) getPage().getVisit()).getDouble2();
					kuangqzf=((Visit) getPage().getVisit()).getDouble3();
					kuangqsk=((Visit) getPage().getVisit()).getDouble4();
					kuangqjk=((Visit) getPage().getVisit()).getDouble5();
					yunzfhj=Math.round((tielyf+tielzf+kuangqyf+kuangqzf+bukyqyzf)*100)/100;
					yunfsk=(double)Math.round(((double)Math.round((tielyf+bukyqyzf)*yunfsl*100)/100+((Visit) getPage().getVisit()).getDouble4())*100)/100;
					buhsyf=(double)Math.round(((double)Math.round((yunzfhj-yunfsk)*100)/100+((Visit) getPage().getVisit()).getDouble5())*100)/100;
				}
			
			meikhjdx=getDXMoney(jiasje);
			yunzfhjdx=getDXMoney(yunzfhj);
			hej=jiasje+yunzfhj;
			daxhj=getDXMoney(hej);
			
			_editvalues.add(new Dcbalancebean_Shih(id, yid, tianzdw, jiesbh,
					fahdw, faz, yunsfsb_id, shoukdw, fahksrq, fahjzrq, yansksrq,
					yansjzrq, kaihyh, pinz, yuanshr, zhangh, hetsl,
					gongfsl, ches, xianshr, fapbh, daibcc, yansbh,
					duifdd, fukfs, shulzjbz, yanssl, yingksl,  yingd, kuid, shulzjje,
					jiessl, jiesslcy,yunfjsl,buhsdj, jiakje,
					bukyqjk, jiakhj, jiaksl, jiaksk, jiasje, tielyf,tielzf,
					kuangqyf,kuangqzf, kuangqsk, kuangqjk,bukyqyzf, jiskc, buhsyf, yunfsl, yunfsk,
					yunzfhj, hej, meikhjdx, yunzfhjdx, daxhj, beiz,
					ranlbmjbr, ranlbmjbrq, kuidjf, jingz, jiesrq,
					fahrq, changcwjbr, changcwjbrq, ruzrq,
					jieszxjbr, jieszxjbrq, gongsrlbjbr, gongsrlbjbrq,
					hetjg, jieslx,yuns,koud_js,
					yunsfs,hetb_id,yunju,Meikjsb_id,Yunfjsb_id,Jihkjb_id));
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Dcbalancebean_Shih());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	
	
	//****************************************************************************
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
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
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
//		删除
		ToolbarButton deletebt=new ToolbarButton(null,"删除","function(){ document.Form0.DeleteButton.click(); }");
		deletebt.setId("deletebt");
		tb1.addItem(deletebt);
		tb1.addText(new ToolbarText("-"));

		
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			visit.setboolean2(false);//分公司
			visit.setboolean3(false);//电厂
			visit.setString2("");	 //结算指标页面显示
			((Visit) getPage().getVisit()).setString11("");	//用于Kuangqzf返回时，跳转到正确的界面(DCBalance,Jiesdxg)
			
			if(visit.getRenyjb()<3){
				
				visit.setboolean3(true);
			}	
			
			getIShoukdwModels();
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
			
			visit.setboolean1(false);//共用
			visit.setboolean5(false);//合同编号显示
//			visit.setboolean6(false);//流程名称显示
			
			getSelectData();
		}
		getToolbars();
		
		if(visit.getboolean1()){
			visit.setboolean1(false);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
		}
	}
//	public String _liucmc;
//	
//	public void setLiucmc(String _value) {
//		_liucmc = _value;
//	}
//	
//	public String getLiucmc() {
//		if (_liucmc == null) {
//			_liucmc = "";
//		}
//		return _liucmc;
//	}

	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
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
                 	+ " select distinct quanc as shoukdw from shoukdw	\n"
                 	+ " order by quanc)";	
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
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
    			
    			sql=" select bianm from "
    				+ " (select distinct bianm from shihjsb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct bianm from shihjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by bianm";
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
    		
    		if(((Visit)getPage().getVisit()).isDCUser()){
        		
        		sql=" select id,gongysmc from "
    				+ " (select distinct shihgysb_id as id,gongysmc from shihjsb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct shihgysb_id as id,gongysmc from shihjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).isGSUser()){
        		
        		sql=" select id,gongysmc from "
    				+ " (select distinct shihgysb_id as id,gongysmc from shihjsb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct shihgysb_id as id,gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).isJTUser()){
        		
        		sql="select id,gongysmc from "
    				+ " (select distinct shihgysb_id as id,gongysmc from shihjsb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct shihgysb_id as id,gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
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
}
