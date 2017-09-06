package com.zhiren.dtrlgs.shoumgl.shoumjs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ResultSetList;
//import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dtrlgs.faygl.fayjs.Dcbalancebean;
import com.zhiren.dtrlgs.pubclass.BalanceLiuc;
 
public class Shoumjswsxg extends BasePage {

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
//		_liucmc="";
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
	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Dcbalancebean)getEditValues().get(0)).getJiesbh().equals("")){
//	        	if(Shoumjsdcz.checkbh(jiesbh,((Dcbalancebean)getEditValues().get(0)).getId(),((Dcbalancebean)getEditValues().get(0)).getYid())){
	        				sql="begin \n";
			        		sql+=" update kuangfjsmkb set bianm = '"+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"',"
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
							       + " buhsdj = "+((Dcbalancebean)getEditValues().get(0)).getBuhsdj()+","
								   + " beiz = '"+((Dcbalancebean)getEditValues().get(0)).getBeiz()+"',meikje = "+((Dcbalancebean)getEditValues().get(0)).getJiakje()+","
							       + " ranlbmjbr='"+((Dcbalancebean)getEditValues().get(0)).getRanlbmjbr()+"',ranlbmjbrq=to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getRanlbmjbrq())+"','yyyy-MM-dd')";
							
			        		sql+= " where id ="+((Dcbalancebean)getEditValues().get(0)).getId()+"; \n";
			        		
			        		sql+=" update kuangfjsyfb set bianm = '"+((Dcbalancebean)getEditValues().get(0)).getJiesbh()+"',	" +
			        			 " gongysmc = '"+((Dcbalancebean)getEditValues().get(0)).getFahdw()+"',	\n faz = '"+((Dcbalancebean)getEditValues().get(0)).getFaz()+"',	\n fahksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " fahjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),	\n meiz ='"+((Dcbalancebean)getEditValues().get(0)).getPinz()+"',	\n daibch ='"+((Dcbalancebean)getEditValues().get(0)).getDaibcc()+"',	\n"
			        			+ " yuanshr = '"+((Dcbalancebean)getEditValues().get(0)).getYuanshr()+"',	\n xianshr = '"+((Dcbalancebean)getEditValues().get(0)).getXianshr()+"',	\n yansksrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd'),	\n"
			        			+ " yansjzrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),	\n yansbh ='"+((Dcbalancebean)getEditValues().get(0)).getYansbh()+"',	\n shoukdw = '"+((Dcbalancebean)getEditValues().get(0)).getShoukdw()+"',	\n"
			        			+ " kaihyh = '"+((Dcbalancebean)getEditValues().get(0)).getKaihyh()+"',	\n zhangh = '"+((Dcbalancebean)getEditValues().get(0)).getZhangh()+"',	\n fapbh = '"+((Dcbalancebean)getEditValues().get(0)).getFapbh()+"',	\n"
			        			+ " fukfs = '"+((Dcbalancebean)getEditValues().get(0)).getFukfs()+"',	\n duifdd = '"+((Dcbalancebean)getEditValues().get(0)).getDuifdd()+"',	\n ches ="+((Dcbalancebean)getEditValues().get(0)).getChes()+",		\n"
			        			+ " jiessl = "+((Dcbalancebean)getEditValues().get(0)).getJiessl()+",	\n guohl = "+((Dcbalancebean)getEditValues().get(0)).getJingz()+",	\n guotyf = "+((Dcbalancebean)getEditValues().get(0)).getTielyf()+",		\n"
			        			+ " guotzf = "+((Dcbalancebean)getEditValues().get(0)).getTielzf()+",	\n kuangqyf="+((Dcbalancebean)getEditValues().get(0)).getKuangqyf()+",	\n kuangqzf="+((Dcbalancebean)getEditValues().get(0)).getKuangqzf()+",	\n"
			        			+ " jiskc = "+((double)Math.round((((Dcbalancebean)getEditValues().get(0)).getTielzf()+((Dcbalancebean)getEditValues().get(0)).getKuangqzf())*100)/100)+",	\n bukyf ="+((Dcbalancebean)getEditValues().get(0)).getBukyqyzf()+",	\n hansyf = "+((Dcbalancebean)getEditValues().get(0)).getYunzfhj()+",	\n"
			        			+ " buhsyf = "+((Dcbalancebean)getEditValues().get(0)).getBuhsyf()+",	\n shuik = "+((Dcbalancebean)getEditValues().get(0)).getYunfsk()+",	\n shuil ="+((Dcbalancebean)getEditValues().get(0)).getYunfsl()+",	\n jiesrq = to_date('"+this.FormatDate(((Dcbalancebean)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),	\n"
			        			+ " beiz = '"+((Dcbalancebean)getEditValues().get(0)).getBeiz()+"'	\n where id = "+((Dcbalancebean)getEditValues().get(0)).getYid()+"; \n";
			        		sql+="end;";
			        		if(con.getUpdate(sql)>-1){
			        			setMsg("结算单更新成功！");
			        		}else{
			        			setMsg("结算单更新失败！");
			        		}
	        }
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
	}
	

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			setMsg("");
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
		
		return "燃 料 采 购 结 算 单";
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
			String myuanshr = "";
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
			Shoumjsdcz jsdcz=new Shoumjsdcz();
//			double mjiesslcy=0;
//			long myunsfsb_id=0;
//			double myingd=0;
//			double mkuid=0;
//			double myunju=0;		//运距
//			double mfengsjj=0;		//分公司加价
//			double mjiajqdj=0;		//加价前单价
//			int mjijlx=0;			//基价类型
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

			String sql="select quanc from diancxxb where id=199";
			ResultSet rs0=con.getResultSet(sql);
			while(rs0.next()){
				mtianzdw =rs0.getString("quanc");
			}
			
			sql="select * from kuangfjsmkb where bianm='"+getJiesbhValue().getValue()+"' ";//and fuid=0";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				mid = rs.getLong("id");
//				mtianzdw =rs.getString("xianshr");
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
//				mjiesslcy=rs.getDouble("jiesslcy");
				mdiancxxb_id=rs.getLong("diancxxb_id");
				mgongysb_id=rs.getLong("gongysb_id");
				mkoud_js=0.00;
				mshulzjbz=rs.getDouble("hansdj");//含税单价从煤款表中取
//				myunsfsb_id=rs.getLong("yunsfsb_id");
//				myingd=rs.getDouble("yingd");//
//				mkuid=rs.getDouble("kuid");//
//				myunju=rs.getDouble("yunj");//
				mhetb_id=rs.getLong("HETB_ID");
				blnHasMeik=true;
				mfapbh=rs.getString("fapbh");
//				mfengsjj=rs.getDouble("fengsjj");//
//				mjiajqdj=rs.getDouble("jiajqdj");//
//				mjijlx=rs.getInt("jijlx");//
//				mkuidjfyf_je=rs.getDouble("kuidjfyf");	//亏吨拒付运费金额 //
//				mkuidjfzf_je=rs.getDouble("kuidjfzf");	//亏顿拒付杂费金额 //
				mchaokdl=Math.abs(rs.getDouble("chaokdl"));		//超亏吨量 //
				mchaokdlx=Shoumjsdcz.nvlStr(rs.getString("chaokdlx"));		//超亏吨类型 //
				
				sql="select jieszbsjb.*,zhibb.bianm from jieszbsjb,kuangfjsmkb,zhibb "
					 + " where jieszbsjb.jiesdid="+mid+" and zhibb.id=jieszbsjb.zhibb_id and zhibb.leib=1"
					 + " and kuangfjsmkb.bianm='"+getJiesbhValue().getValue()+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("bianm").equals(Locale.jiessl_zhibb)){
						
						mhetsl = rs2.getString("hetbz");
						mgongfsl =rs2.getDouble("gongf") ;
						mshulzjbz =rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje=rs2.getDouble("zhejje");
						
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
				mranlbmjbr = rs.getString("ranlbmjbr");//燃料部门经办人
				mranlbmjbrq =rs.getDate("ranlbmjbrq");//燃料部门经办日期
//				mkuidjf = 0;
				mjingz = rs.getDouble("guohl");
				mjiesrq =rs.getDate("jiesrq");
				mruzrq =rs.getDate("ruzrq");
				
				mjiesslblxsw=Shoumjsdcz.getJiessz_item(mdiancxxb_id, mgongysb_id, mhetb_id, Locale.jiesslblxsw_jies, mjiesslblxsw);
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
				sql="select * from kuangfjsyfb where bianm='"+getJiesbhValue().getValue()+"'";
				rs=con.getResultSet(sql);
				if(rs.next()){
					myid=rs.getLong("id");
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
				
				sql=" select * from kuangfjsyfb where bianm='"+getJiesbhValue().getValue()+"'";
				
					rs=con.getResultSet(sql);
					if(rs.next()){
						myid=rs.getLong("id");
//						mtianzdw =rs.getString("xianshr");
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
//						myunsfsb_id=rs.getLong("yunsfsb_id");
//						myingd=rs.getDouble("yingd");
//						mkuid=rs.getDouble("kuid");
//						myunju=rs.getDouble("yunj");
						mjingz=rs.getDouble("guohl");
//						mjiesslcy=rs.getDouble("jiesslcy");
						mshulzjbz=rs.getDouble("hansdj");
						mkuidjfyf_je=rs.getDouble("kuidjfyf");
						mkuidjfzf_je=rs.getDouble("kuidjfzf");
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
					mfahdw, mmeikdw,mfaz, 
//					myunsfsb_id,
					mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, 
//					mkuid,
					mshulzjje,
					mjiessl, 
//					mjiesslcy,
					myunfjsl,mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,mtielzf,
					mkuangqyf,mkuangqzf, mkuangqsk, mkuangqjk,mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx,myuns,mkoud_js,
					myunsfs, mdiancjsbs,mhetb_id,
//					myunju,
					mMeikjsb_id,
					mYunfjsb_id,mJihkjb_id,
//					mfengsjj,
//					mjiajqdj,
//					mjijlx,
					mMjtokcalxsclfs,
					mkuidjfyf_je,
					mkuidjfzf_je,
					mchaokdl,mchaokdlx
					));//超吨亏吨标识符
			
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
//		结算编号
		tb1.addText(new ToolbarText("结算单编号:"));
		ComboBox JiesbhDropDown = new ComboBox();
		JiesbhDropDown.setEditable(true);
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
			visit.setString2("");	 //结算指标页面显示
			((Visit) getPage().getVisit()).setString3("");	//合计大写行显示内容（为实现动态设置“超扣吨”显示用）
//			setShoukdwValue(null);
//			setIShoukdwModel(null);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			setShoukdwValue(null);
			setIShoukdwModel(null);
			setZhanghValue(null);
			setZhanghModel(null);
			setKaihyhValue(null);
			setKaihyhModel(null);
		}
		getSelectData();
		getToolbars();
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
    
//  开户银行
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
    	Visit visit = (Visit) this.getPage().getVisit();
    	String sql = "";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
//		业务类型限制
		String yewlx_condition="";
    	String strDiancID="";
    	String xiasjs_condition="";
    	
		if(visit.isJTUser()){
			strDiancID = "";
			xiasjs_condition= "          and f.jiesb_id(+)=k.id\n";
		}else if(visit.isGSUser()){
			strDiancID = " and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
			xiasjs_condition= "          and f.jiesb_id(+)=k.id\n";
		}else{
			strDiancID = " and dc.id="+visit.getDiancxxb_id();
			xiasjs_condition= "          and f.jiesb_id=k.id\n";
		}

    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));

    		sql="    select bianm\n"+
    	    "  from (select distinct k.bianm\n"+
    	      "        from kuangfjsmkb k,(select f.jiesb_id from diancxxb dc,fahb f where f.diancxxb_id = dc.id "+strDiancID+") f\n"+
    	     "        where k.liucztb_id ="+BalanceLiuc.getLastStatus("收煤结算")+"\n"+
    	     xiasjs_condition+
    	     yewlx_condition+"\n" +
    	          		")\n"+
    	  "  union (\n"+
    	    "         select distinct k1.bianm\n"+
    	    "         from kuangfjsyfb k1 \n"+
    	     "        where \n"+
    	      "              liucztb_id = "+BalanceLiuc.getLastStatus("收煤结算")+"\n"+
    	      "             and k1.kuangfjsb_id in(\n"+
    	     "      select distinct k.id\n"+
    	     "      from kuangfjsmkb k,(select f.jiesb_id from diancxxb dc,fahb f where f.diancxxb_id = dc.id "+strDiancID+") f \n"+
    	      "     where k.liucztb_id ="+BalanceLiuc.getLastStatus("收煤结算")+"\n"+
    	      		xiasjs_condition+
    	       		yewlx_condition+ ")\n"+   
    	      "     )\n"+
    	    " order by bianm\n";
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
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).isGSUser()){
        		
        		sql=" select id,gongysmc from "
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsmkb,diancxxb where kuangfjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsyfb,diancxxb where kuangfjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).isJTUser()){
        		
        		sql="select id,gongysmc from "
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysb_id as id,gongysmc from kuangfjsyfb where liucztb_id=0) order by gongysmc";
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

	private String getProperValue(IPropertySelectionModel _selectModel,int value) {
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