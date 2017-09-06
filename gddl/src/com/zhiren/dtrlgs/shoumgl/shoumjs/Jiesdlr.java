package com.zhiren.dtrlgs.shoumgl.shoumjs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
//import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dtrlgs.faygl.fayjs.Dcbalancebean;
import com.zhiren.dtrlgs.faygl.fayjs.JiesdlrBean;
import com.zhiren.dtrlgs.faygl.fayjs.Fayjsdcz;
 
public class Jiesdlr extends BasePage {

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
	

	private void Save() {
		Visit visit = (Visit) getPage().getVisit();
	    JDBCcon con = new JDBCcon();
//	    con.setAutoCommit(false);
//	    con.rollBack();
//	    con.commit();
	    String jiesbh=((JiesdlrBean)getEditValues().get(0)).getJiesbh();
	    long newid=Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
	    
	    String sql = "begin \n";
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((JiesdlrBean)getEditValues().get(0)).getJiesbh().equals("")){
	        	if(Fayjsdcz.checkbh(jiesbh,((JiesdlrBean)getEditValues().get(0)).getId(),((JiesdlrBean)getEditValues().get(0)).getYid())){
	        		String sqlmk="insert into diancjsmkb (\n" +
	        			"id,diancxxb_id,bianm,gongysb_id,gongysmc,faz,fahksrq,fahjzrq,meiz,daibch,\n" + 
	        			"yuanshr,xianshr,yansksrq,yansjzrq,yansbh,shoukdw,kaihyh,zhangh,fapbh,fukfs,\n" + 
	        			"ches,jiessl,guohl,hansdj,bukmk,hansmk,buhsmk,meikje,shuik,shuil,buhsdj,jieslx,\n" + 
	        			"jiesrq,hetb_id,ranlbmjbr,ranlbmjbrq,jiesrl,jieslf,beiz,YEWLXB_ID,gongsqrzt\n" + 
	        			")values(" +
	        			newid+"," +
	        			getProperId(getXianshrModels(),((JiesdlrBean)getEditValues().get(0)).getXianshr())+","+
	        			//getXianshrValue().getStrId()+","+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getJiesbh()+"',"+
	        			getProperId(getGonghModels(),((JiesdlrBean)getEditValues().get(0)).getFahdw())+","+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getFahdw()+"','"+((JiesdlrBean)getEditValues().get(0)).getFaz()+"',"+
	        			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"+
	        			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getPinz()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getDaibcc()+"',"+
	        			"'"+getYuanshrValue().getValue()+"',"+
	        			"'"+getXianshrValue().getValue()+"',"+
	        			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd')," +
	        			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getYansbh()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getShoukdw()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getKaihyh()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getZhangh()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getFapbh()+"',"+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getFukfs()+"',"+
	        			((JiesdlrBean)getEditValues().get(0)).getChes()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiessl()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJingz()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getShulzjbz()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getBukyqjk()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiasje()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiakhj()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiakje()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiaksk()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getJiaksl()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getBuhsdj()+","+
	        			getJieslxValue().getStrId()+","+
//	        			"to_date('"+getRiq()+"','yyyy-mm-dd'),"+
	        			"to_date('"+getRiq()+"','yyyy-mm-dd'),"+
	        			getHetValue().getStrId()+","+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getRanlbmjbr()+"',"+
	        			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getRanlbmjbrq())+"','yyyy-MM-dd'),"+
	        			((JiesdlrBean)getEditValues().get(0)).getQnetar_js()+","+
	        			((JiesdlrBean)getEditValues().get(0)).getStd_js()+","+
	        			"'"+((JiesdlrBean)getEditValues().get(0)).getBeiz()+"',"
	        			+getJiesbhValue().getStrId()+",1); \n";
	        	
	       String sqlyf = " insert into diancjsyfb  (id,diancxxb_id, bianm, gongysb_id, gongysmc,"
				+ "	faz, fahksrq, fahjzrq, meiz, daibch, "
				+ " yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, "
				+ " fukfs, ches, gongfsl, yanssl, jiessl, yingk, guohl,  "
				+ " guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, "
				+ " shuil, buhsdj, jieslx, jiesrq,  hetb_id, beiz, "
				+ " diancjsmkb_id, RANLBMJBR, RANLBMJBRQ, kuidjfyf,kuidjfzf,yewlxb_id)"
				+ " values("+newid+","+
				getProperId(getXianshrModels(),((JiesdlrBean)getEditValues().get(0)).getXianshr())+","+
//				getXianshrValue().getStrId()+","+ 
				"'"+((JiesdlrBean)getEditValues().get(0)).getJiesbh()+"',"+ 
    			getProperId(getGonghModels(),((JiesdlrBean)getEditValues().get(0)).getFahdw())+","+  
    			"'"+((JiesdlrBean)getEditValues().get(0)).getFahdw()+"',"+   
    			"'"+((JiesdlrBean)getEditValues().get(0)).getFaz()+"',"+
    			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"+
    			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"+
    			"'"+((JiesdlrBean)getEditValues().get(0)).getPinz()+"',"+      
    			"'"+((JiesdlrBean)getEditValues().get(0)).getDaibcc()+"',"+
    			"'"+getYuanshrValue().getValue()+"',"+
    			"'"+getXianshrValue().getValue()+"',"+
    			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd')," +
    			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),"+
    			"'"+((JiesdlrBean)getEditValues().get(0)).getYansbh()+"',"+    
    			"'"+((JiesdlrBean)getEditValues().get(0)).getShoukdw()+"',"+
    			"'"+((JiesdlrBean)getEditValues().get(0)).getKaihyh()+"',"+   
    			"'"+((JiesdlrBean)getEditValues().get(0)).getZhangh()+"',"+
    			"'"+((JiesdlrBean)getEditValues().get(0)).getFapbh()+"',"+  
    			"'"+((JiesdlrBean)getEditValues().get(0)).getFukfs()+"',"+
    			((JiesdlrBean)getEditValues().get(0)).getChes()+","+       			
    			((JiesdlrBean)getEditValues().get(0)).getGongfsl()+","+
    			((JiesdlrBean)getEditValues().get(0)).getYanssl()+","+       			
    			((JiesdlrBean)getEditValues().get(0)).getJiessl()+","+
    			((JiesdlrBean)getEditValues().get(0)).getYingksl()+","+        			
    			((JiesdlrBean)getEditValues().get(0)).getJingz()+","+
    			((JiesdlrBean)getEditValues().get(0)).getTielyf()+","+      			
    			((JiesdlrBean)getEditValues().get(0)).getTielzf()+","+
    			((JiesdlrBean)getEditValues().get(0)).getKuangqyf()+","+      			
    			((JiesdlrBean)getEditValues().get(0)).getKuangqzf()+","+
    			(double) Math.round(
    					(
    					((JiesdlrBean) getEditValues().get(0)).getTielzf() + 
    					((JiesdlrBean) getEditValues().get(0)).getKuangqzf()) * 100)/ 100	+ ","+
    			((JiesdlrBean)getEditValues().get(0)).getShulzjbz()+","+  
    			((JiesdlrBean)getEditValues().get(0)).getBukyqjk()+","+
    			((JiesdlrBean)getEditValues().get(0)).getYunzfhj()+","+    
    			((JiesdlrBean)getEditValues().get(0)).getBuhsyf()+","+
    			((JiesdlrBean)getEditValues().get(0)).getYunfsk()+","+     
    			((JiesdlrBean)getEditValues().get(0)).getYunfsl()+","+
    			((JiesdlrBean)getEditValues().get(0)).getBuhsdj()+","+    
    			getJieslxValue().getStrId()+","+
    			"to_date('"+getRiq()+"','yyyy-mm-dd'),"+       
//    			"to_date('"+getRiq()+"','yyyy-mm-dd'),"+
    			getHetValue().getStrId()+","+    		
    			"'"+((JiesdlrBean)getEditValues().get(0)).getBeiz()+"',"+
    			newid+","+    		
    			"'"+((JiesdlrBean)getEditValues().get(0)).getRanlbmjbr()+"',"+
    			"to_date('"+this.FormatDate(((JiesdlrBean)getEditValues().get(0)).getRanlbmjbrq())+"','yyyy-MM-dd'),"+
    			((JiesdlrBean)getEditValues().get(0)).getKuidjfyf()+","+    
    			((JiesdlrBean)getEditValues().get(0)).getKuidjfzf()+","+
    			getJiesbhValue().getStrId()+"); \n";
    		
    		String sqlzl="";
    		
    		sqlzl+= " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
				+ " values ("+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newid+"," +
				"(select id from zhibb where leib=1 and bianm='结算数量'),"+
				((JiesdlrBean)getEditValues().get(0)).getHetsl()+","+
				((JiesdlrBean)getEditValues().get(0)).getGongfsl()+","+
				((JiesdlrBean)getEditValues().get(0)).getYanssl()+","+
				((JiesdlrBean)getEditValues().get(0)).getJiessl()+","+
				((JiesdlrBean)getEditValues().get(0)).getYingksl()+","+
				((JiesdlrBean)getEditValues().get(0)).getShulzjbz()+","+
				((JiesdlrBean)getEditValues().get(0)).getShulzjje()+",1);\n";
    		
    		sqlzl+= " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
				+ " values ("+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newid+","+
				"(select id from zhibb where leib=1 and bianm='Qnetar')," +
				((JiesdlrBean)getEditValues().get(0)).getQnetar_ht()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_kf()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_cf()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_js()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_yk()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_zdj()+","+
				((JiesdlrBean)getEditValues().get(0)).getQnetar_zje()+",1);\n";
    		
    		sqlzl += " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt) "
				+ " values ("+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))+","+newid+","+
				"(select id from zhibb where leib=1 and bianm='Std')," +
				((JiesdlrBean)getEditValues().get(0)).getStd_ht()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_kf()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_cf()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_js()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_yk()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_zdj()+","+
				((JiesdlrBean)getEditValues().get(0)).getStd_zje()+",1);\n";
				
    		if(getJieslxValue().getValue().equals("两票结算")){sql+=sqlmk+sqlyf+sqlzl;}  		
    		else if(getJieslxValue().getValue().equals("煤款结算")){sql+=sqlmk+sqlzl;}  		
    		else{sql+=sqlyf+sqlzl;}  
    		sql+="end;";

    			if(con.getUpdate(sql)>=0){
    				setMsg("结算单更新成功！");
    			}else{
    				setMsg("结算单更新失败！");
    			}

        	}else{
	        		setMsg("该结算编号已被另一张结算单使用，请重新保存！");
	        		_RefreshCheck=false;
	        	}
//	        setMsg("结算单编号不能为空！");
	 }
	con.Close();
	}
	


	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	
	private boolean _RefreshCheck=true;
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			((JiesdlrBean)getEditValues().get(0)).setJiesbh(Jiesbh());
			if(_RefreshCheck){
				getSelectData();
			}
			_RefreshCheck=true;
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			setMsg("");
			getSelectData();
		}
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private JiesdlrBean _EditValue;
	public JiesdlrBean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(JiesdlrBean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getTitle(){
		
		return "燃 料 销 售 结 算 单";
	}
	
	public String getJiesslcyText(){
		
		return Locale.jiesslcy_title;
	}

//	自动生成编码
private String Jiesbh(){
		JDBCcon con=new JDBCcon();
		String strJsbh="";
		try{
	        String sYear ="";
	        String sMonth="";
	        java.util.Date datCur = new java.util.Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01");
	        String dat = formatter.format(datCur);
	        sYear = dat.substring(0, 4);
	        sMonth = dat.substring(5, 7);
	        String sDate = sYear + sMonth;
	        int intXh=1;
	        int intBh=0;
	        String sql2="select max(bianm) as jiesbh from (select bianm from diancjsmkb where bianm like 'CDT-JS-DTRL-XS-"+sDate+"-%')";
	        ResultSet rsSl=con.getResultSet(sql2);
	        if (rsSl.next()){
	            strJsbh=rsSl.getString("jiesbh");               
	        }
	        if(strJsbh==null){
	            strJsbh="CDT-JS-DTRL-XS-"+sDate+"-0000";
	        }
	        intBh=Integer.parseInt(strJsbh.trim().substring(strJsbh.trim().length()-4,strJsbh.trim().length()));
	        intBh=intBh+1;
	        if(intBh<10000 && intBh>=1000){
	            strJsbh="CDT-JS-DTRL-XS-"+sDate+"-"+String.valueOf(intBh);
	        }else if (intBh<1000 && intBh>=100){
	            strJsbh="CDT-JS-DTRL-XS-"+sDate+"-0"+String.valueOf(intBh);
	        }else if(intBh>=10 && intBh<100){
	            strJsbh="CDT-JS-DTRL-XS-"+sDate+"-00"+String.valueOf(intBh);
	        }else{
	            strJsbh="CDT-JS-DTRL-XS-"+sDate+"-000"+String.valueOf(intBh);
	        }
	        rsSl.close();
	        
	        return strJsbh;
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return strJsbh;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
//		Visit visit = (Visit) getPage().getVisit();
//		if(getEditValues()!=null){
//			
//			getEditValues().clear();
//		}

			
			long mid = 0;
			long myid = 0;
//			long mdiancxxb_id=visit.getDiancxxb_id();
//			long mgongysb_id=-1;
			String mtianzdw =getProperValue(getTianzdwModel(),199);
			String mjiesbh = Jiesbh();
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
			String myuanshr = "";
			String mzhangh = "";
			String mhetsl = "";// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = "";
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			
			String mQnetar_ht = "0";		// 合同热量
			double mQnetar_kf = 0;		// 供方热量
			double mQnetar_cf = 0;		// 厂方热量
			double mQnetar_js = 0;		// 厂方结算
			double mQnetar_yk = 0;		// 厂方盈亏
			double mQnetar_zdj = 0;		// 折单价
			double mQnetar_zje = 0;		// 这金额
			
			String mStd_ht= "0.0";			//合同硫分
			double mStd_kf=0;			//供方热量
			double mStd_cf=0;			//厂方热量
			double mStd_js=0;			//结算热量
			double mStd_yk = 0;			// 厂方盈亏
			double mStd_zdj = 0;		// 折单价
			double mStd_zje = 0;		// 这金额
			long mhetb_id=0; 
			double mkoud_js=0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.17;
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
			String myunsfs = "";
			String mdiancjsbs = "";
			String mstrJieszb="";
			Money mn=new Money();
			Shoumjsdcz jsdcz=new Shoumjsdcz();
			String mMjtokcalxsclfs="";	//兆焦转大卡小数处理方式
			double mkuidjfyf_je=0;	//亏吨拒付运费金额
			double mkuidjfzf_je=0;	//亏顿拒付杂费金额
			double mchaokdl=0;   	//超亏吨量
			String mchaokdlx="";	//超亏吨类型
			
//			进行单批次结算时，要将每一个批次的结算情况保存起来，存入danpcjsmxb中，此时就产生了id
//			结算时要判断有无这个id，如果有就一定要用这个id
			long mMeikjsb_id=0;
			long mYunfjsb_id=0;
			long mJihkjb_id=0;
			mkoud_js=0.00;
			mmeikhjdx=getDXMoney(mjiasje);
			myunzfhjdx=getDXMoney(myunzfhj);
			mhej=mjiasje+myunzfhj;
			mdaxhj=getDXMoney(mhej);
			
//			设置超/亏吨的显示	
				this.setHejdxh(jsdcz.SetHejdxh("",0,mhej,mdaxhj));
			
			_editvalues.add(new JiesdlrBean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mmeikdw,mfaz, 
					mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, 
					mshulzjje,
					mjiessl, 
					myunfjsl,mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,
					mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,
					mMjtokcalxsclfs,
					mkuidjfyf_je,
					mkuidjfzf_je,
					mchaokdl,mchaokdlx,
					mQnetar_ht ,	// 合同热量
					mQnetar_kf,		// 供方热量
					mQnetar_cf ,	// 厂方热量
					mQnetar_js ,		// 厂方结算
					mQnetar_yk ,	// 厂方盈亏
					mQnetar_zdj,		// 折单价
					mQnetar_zje,	// 折价金额
					mStd_ht,		//合同硫分
					mStd_kf,			//供方热量
					mStd_cf,			//厂方热量
					mStd_js,			//结算热量
					mStd_yk ,			// 厂方盈亏
					mStd_zdj,		// 折单价
					mStd_zje 		// 折价金额
					));//超吨亏吨标识符

		if (_editvalues == null) {
			_editvalues.add(new JiesdlrBean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		结算编号
		tb1.addText(new ToolbarText("结算时间:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("BRiq","Form0");// 与html页中的id绑定,并自动刷新
		df.setId("Jiessj");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("业务类型:"));
		ComboBox JiesbhDropDown = new ComboBox();
		JiesbhDropDown.setEditable(true);
		JiesbhDropDown.setId("JiesbhDrop");
		JiesbhDropDown.setWidth(100);
		JiesbhDropDown.setLazyRender(true);
		JiesbhDropDown.setTransform("JIESBHDropDown");
		tb1.addField(JiesbhDropDown);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算类型:"));
		ComboBox JieslxDropDown = new ComboBox();
		JieslxDropDown.setEditable(true);
		JieslxDropDown.setId("JieslxDrop");
		JieslxDropDown.setWidth(100);
		JieslxDropDown.setLazyRender(true);
		JieslxDropDown.setTransform("JIESLXDropDown");
		tb1.addField(JieslxDropDown);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("合同编号:"));
		ComboBox HETONGDropDown = new ComboBox();
		HETONGDropDown.setEditable(true);
		HETONGDropDown.setId("HetongDrop");
		HETONGDropDown.setWidth(160);
		HETONGDropDown.setLazyRender(true);
		HETONGDropDown.setTransform("HETONGDropDown");
		tb1.addField(HETONGDropDown);
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

		setToolbar(tb1);
	}
	
	private boolean rqchange = false;//判断日期是否改变
//	绑定日期
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}else{if (!this.riq.equals(riq)){rqchange = true;}	}
		return riq;
	}
	
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
//			visit.setString2("");	 //结算指标页面显示
			visit.setString3("");	//合计大写行显示内容（为实现动态设置“超扣吨”显示用）
			setXianshrValue(null);
			setXianshrModel(null);
			setYuanshrValue(null);
			setYuanshrModel(null);
			setTianzdwValue(null);
			setTianzdwModel(null);
			setPinzValue(null);
			setPinzModel(null);
			setFazValue(null);
			setFazModel(null);
			setGonghValue(null);
			setGonghModel(null);
			setHetValue(null);
			setHetModel(null);
			setJieslxValue(null);
			setJieslxModel(null);
			setRiq(null);
			setShoukdwValue(null);
			setIShoukdwModel(null);
			setZhanghValue(null);
			setZhanghModel(null);
			setKaihyhValue(null);
			setKaihyhModel(null);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getSelectData();
		}
		if(rqchange){			
			setHetValue(null);
			setHetModel(null);
			}
		rqchange = false;
		getToolbars();
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
	
//  合同类型 DropDownBean12
//  合同类型 ProSelectionModel12
	public IDropDownBean getHetValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean12() == null) {
			((Visit) getPage().getVisit()).setDropDownBean12((IDropDownBean) getHetModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean12();
	}

	public void setHetValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean12() != value) {
			((Visit) getPage().getVisit()).setDropDownBean12(value);
		}
	}

	public void setHetModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel12(value);
	}

	public IPropertySelectionModel getHetModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
			getHetModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}

	public IPropertySelectionModel getHetModels() {
		String sql ="  select distinct h.id,h.hetbh\n"
			+ "from hetb h,hetslb hs "
			+ "where h.id = hs.hetb_id(+) "
			+ "and not(to_date('"+this.getRiq()+"','yyyy-MM-dd') >guoqrq or to_date('"+this.getRiq()+"','yyyy-MM-dd')<qisrq)  \n "
			+"and  liucztb_id=1 and h.leib<>2"
			+ " order by hetbh ";
		((Visit) getPage().getVisit()).setProSelectionModel12(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}
	
//  结算类型 DropDownBean11
//  结算类型 ProSelectionModel11
	public IDropDownBean getJieslxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit()).setDropDownBean11((IDropDownBean) getJieslxModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setJieslxValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean11() != value) {
			((Visit) getPage().getVisit()).setDropDownBean11(value);
		}
	}

	public void setJieslxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getJieslxModels() {
		String sql ="select id, mingc from feiylbb where (leib<2 or leib=3) order by id";
		((Visit) getPage().getVisit()).setProSelectionModel11(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}
	
//	业务类型 DropDownBean10
//  业务类型 ProSelectionModel10
   
   public void setIJiesbhModel(IPropertySelectionModel value){
       ((Visit)getPage().getVisit()).setProSelectionModel10(value);
   }
   
   public IPropertySelectionModel getIJiesbhModel(){
   	if(((Visit)getPage().getVisit()).getProSelectionModel10()==null){
   		getIJiesbhModels();
       }
       return ((Visit)getPage().getVisit()).getProSelectionModel10();
   }
   
   public IPropertySelectionModel getIJiesbhModels(){
	((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel("select id, mingc from yewlxb where mingc<>'采购' order by mingc"));
	return ((Visit)getPage().getVisit()).getProSelectionModel10();
   }
   
   public IDropDownBean getJiesbhValue() {
   	if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
   		((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIJiesbhModel().getOption(0));
   	}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
   public void setJiesbhValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean10()!=value){
			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}
//   end	
	
//  收款单位 DropDownBean9
//  收款单位 ProSelectionModel9
    public IDropDownBean getShoukdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean9()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean9((IDropDownBean)getIShoukdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean9();
    }
    
    public void setShoukdwValue(IDropDownBean value)
    {
    		((Visit)getPage().getVisit()).setDropDownBean9(value);
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
    	String sql="select id, quanc from gongysb order by quanc";
		((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    
//    开户银行
	private IDropDownBean KaihyhValue;

	public IDropDownBean getKaihyhValue() {
		if (KaihyhValue == null) {
			KaihyhValue = (IDropDownBean) getKaihyhModel().getOption(0);
		}
		return KaihyhValue;
	}

	public void setKaihyhValue(IDropDownBean Value) {
			KaihyhValue = Value;
	}

	private IPropertySelectionModel KaihyhModel;

	public void setKaihyhModel(IPropertySelectionModel value) {
		KaihyhModel = value;
	}

	public IPropertySelectionModel getKaihyhModel() {
		if (KaihyhModel == null) {
			getKaihyhModels();
		}
		return KaihyhModel;
	}

	public IPropertySelectionModel getKaihyhModels() {
		String sql="select id, kaihyh from gongysb order by quanc";
		
		KaihyhModel = new IDropDownModel(sql);
		return KaihyhModel;
	}
//    银行账号
	private IDropDownBean ZhanghValue;

	public IDropDownBean getZhanghValue() {
		if (ZhanghValue == null) {
			ZhanghValue = (IDropDownBean) getZhanghModel().getOption(0);
		}
		return ZhanghValue;
	}

	public void setZhanghValue(IDropDownBean Value) {
			ZhanghValue = Value;
	}

	private IPropertySelectionModel ZhanghModel;

	public void setZhanghModel(IPropertySelectionModel value) {
		ZhanghModel = value;
	}

	public IPropertySelectionModel getZhanghModel() {
		if (ZhanghModel == null) {
			getZhanghModels();
		}
		return ZhanghModel;
	}

	public IPropertySelectionModel getZhanghModels() {
		String sql="select id, Zhangh from gongysb order by quanc";
		
		ZhanghModel = new IDropDownModel(sql);
		return ZhanghModel;
	}
	
//   填制单位 DropDownBean6
//   填制单位 ProSelectionModel6
    
    public void setTianzdwModel(IPropertySelectionModel value){
        ((Visit)getPage().getVisit()).setProSelectionModel6(value);
    }
    
    public IPropertySelectionModel getTianzdwModel(){
    	if(((Visit)getPage().getVisit()).getProSelectionModel6()==null){
    		getTianzdwModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IPropertySelectionModel getTianzdwModels(){
//    	Visit visit = (Visit) this.getPage().getVisit();
    	((Visit)getPage().getVisit()).setProSelectionModel6(new IDropDownModel("select dc.id,dc.quanc from diancxxb dc order by dc.quanc "));
		return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IDropDownBean getTianzdwValue() {
    	
    	if(((Visit)getPage().getVisit()).getDropDownBean6()==null){
    		((Visit)getPage().getVisit()).setDropDownBean6((IDropDownBean)getTianzdwModel().getOption(0));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean6();
	}
	
    public void setTianzdwValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean6()!=value){
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}
//    end
    
//  现收货人 DropDownBean7
//  现收货人 ProSelectionModel7
   
   public void setXianshrModel(IPropertySelectionModel value){
       ((Visit)getPage().getVisit()).setProSelectionModel7(value);
   }
   
   public IPropertySelectionModel getXianshrModel(){
   	if(((Visit)getPage().getVisit()).getProSelectionModel7()==null){
   		getXianshrModels();
       }
       return ((Visit)getPage().getVisit()).getProSelectionModel7();
   }
   
   public IPropertySelectionModel getXianshrModels(){
//   	Visit visit = (Visit) this.getPage().getVisit();
   	((Visit)getPage().getVisit()).setProSelectionModel7(new IDropDownModel("select id,quanc from diancxxb where (cangkb_id=1 and jib=3) or jib=2 order by quanc"));
		return ((Visit)getPage().getVisit()).getProSelectionModel7();
   }
   
   public IDropDownBean getXianshrValue() {
   	
   	if(((Visit)getPage().getVisit()).getDropDownBean7()==null){
   		((Visit)getPage().getVisit()).setDropDownBean7((IDropDownBean)getXianshrModel().getOption(0));
   	}
		return ((Visit)getPage().getVisit()).getDropDownBean7();
	}
	
   public void setXianshrValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean7()!=value){
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}
//   end
	
//	原收货人 DropDownBean8
//  原收货人 ProSelectionModel8
	public IDropDownBean getYuanshrValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getYuanshrModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setYuanshrValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setYuanshrModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getYuanshrModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getYuanshrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getYuanshrModels() {
		String sql="select id,quanc from diancxxb where (cangkb_id=1 and jib=3) or jib=2 order by quanc";
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//  end
//	供货人 DropDownBean5
//  供货人 ProSelectionModel5
	public IDropDownBean getGonghValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getGonghModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGonghValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean5()!=value){
			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}
	}

	public void setGonghModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGonghModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getGonghModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getGonghModels() {
		String sql="select dc.id,dc.quanc from gongysb dc order by dc.quanc";
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
//  end
//	发站 DropDownBean4
//  发站 ProSelectionModel4
	public IDropDownBean getFazValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getFazModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFazValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean4()!=value){
			((Visit) getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setFazModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getFazModels() {
		String sql="select dc.id,dc.mingc from chezxxb dc order by dc.mingc";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
//  end
//	品种 DropDownBean3
//  品种 ProSelectionModel3
	public IDropDownBean getPinzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getPinzModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setPinzValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean3()!=value){
			((Visit) getPage().getVisit()).setDropDownBean3(value);
		}
	}

	public void setPinzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getPinzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getPinzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getPinzModels() {
		String sql="select dc.id,dc.mingc from pinzb dc order by dc.mingc";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
//  end
	
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

    //********************************************************************************//
    
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,	int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) { //gongysb_id
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