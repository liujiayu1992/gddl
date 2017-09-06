package com.zhiren.jt.jiesgl.kuangfhs;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Money;
import com.zhiren.common.MainGlobal;
import com.zhiren.jt.jiesgl.changfhs.Balancebean;
import com.zhiren.jt.jiesgl.report.kuangfhs.Kuangfjsd;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;

public class Kuangfjsxg extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	//条件下拉框开始
	
	private static Date _JiesrqsmallValue = new Date();
	private boolean _Jiesrqsmallchange = false;
	public Date getJiesrqsmall() {
		if(_JiesrqsmallValue==null){
			_JiesrqsmallValue=new Date();
		}
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
	
//	 关联矿方结算的编号
	public IDropDownBean getKuangfjsmkbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getIKuangfjsmkbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setKuangfjsmkbhValue(IDropDownBean value) {

		if(((Visit) getPage().getVisit()).getDropDownBean10()!=value){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}

	public void setIKuangfjsmkbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIKuangfjsmkbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getIKuangfjsmkbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIKuangfjsmkbhModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			sql = "select id,bianm from kuangfjsmkb where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " liucztb_id=0 order by bianm";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			sql = "select id,bianm from kuangfjsmkb where diancxxb_id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") and liucztb_id=0 order by bianm";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			sql = "select id,bianm from kuangfjsmkb where liucztb_id=0 order by bianm";
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	//
	
	// 流程名称
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getILiucmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setLiucmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean11() != value) {

			((Visit) getPage().getVisit()).setDropDownBean11(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {

			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getILiucmcModels() {

		String sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}
	
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private void Save(IRequestCycle cycle) {
		
		JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Kuangfjsxgbean)getEditValues().get(0)).getJiesbh().equals("")){
	        				
	        		sql=" update kuangfjsmkb set gongysb_id = "+this.getProperId(getIFahdwModel(), ((Kuangfjsxgbean)getEditValues().get(0)).getFahdw())+","
		        		+ " gongysmc = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFahdw()+"', faz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFaz()+"',"
		        		+ " fahksrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
		        		+ " fahjzrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"
		        		+ " meiz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getPinz()+"',daibch = '"+((Kuangfjsxgbean)getEditValues().get(0)).getDaibcc()+"',"
		        		+ " yuanshr = '"+((Kuangfjsxgbean)getEditValues().get(0)).getYuanshr()+"', xianshr = '"+((Kuangfjsxgbean)getEditValues().get(0)).getXianshr()+"',"
		        		+ " yansksrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getKaisysrq())+"','yyyy-MM-dd'),"
		        		+ " yansjzrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getJiezysrq())+"','yyyy-MM-dd'),"
		        		+ " yansbh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getYansbh()+"',shoukdw = '"+((Kuangfjsxgbean)getEditValues().get(0)).getShoukdw()+"',"
		        		+ " kaihyh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getKaihyh()+"',zhangh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getZhangh()+"',"
		        		+ " fapbh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFapbh()+"',fukfs = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFukfs()+"',"
		        		+ " duifdd = '"+((Kuangfjsxgbean)getEditValues().get(0)).getDuifdd()+"', ches ="+((Kuangfjsxgbean)getEditValues().get(0)).getChes()+","
		        		+ " hansdj = "+((Kuangfjsxgbean)getEditValues().get(0)).getShulzjbz()+", bukmk = "+((Kuangfjsxgbean)getEditValues().get(0)).getBukyqjk()+","
		        		+ " hansmk = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiasje()+", buhsmk = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiakhj()+","
		        		+ " meikje = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiakje()+", shuik = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiaksk()+","
		        		+ " shuil = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiaksl()+", buhsdj = "+((Kuangfjsxgbean)getEditValues().get(0)).getBuhsdj()+",";
	        		if(getHetbhValue().getId()>-1){
	        			
	        			sql=sql+" hetb_id = "+getHetbhValue().getId()+",";
	        		}
	        		
	        		sql=sql+" beiz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getBeiz()+"' where id = "+((Kuangfjsxgbean)getEditValues().get(0)).getId()+"";
	        		
	        		con.getUpdate(sql);
	        		
	        		sql=" update kuangfjsyfb set gongysb_id = "+this.getProperId(getIFahdwModel(), ((Kuangfjsxgbean)getEditValues().get(0)).getFahdw())+","
	        			+ " gongysmc = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFahdw()+"',faz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFaz()+"',"
	        			+ " fahksrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
	        			+ " fahjzrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),"
	        			+ " meiz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getPinz()+"',daibch =  '"+((Kuangfjsxgbean)getEditValues().get(0)).getDaibcc()+"',"
	        			+ " yuanshr = '"+((Kuangfjsxgbean)getEditValues().get(0)).getYuanshr()+"', xianshr ='"+((Kuangfjsxgbean)getEditValues().get(0)).getXianshr()+"',"
	        			+ " yansksrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getKaisysrq())+"','yyyy-MM-dd'),"
	        			+ " yansjzrq = to_date('"+this.FormatDate(((Kuangfjsxgbean)getEditValues().get(0)).getJiezysrq())+"','yyyy-MM-dd'),"
	        			+ " yansbh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getYansbh()+"', shoukdw = '"+((Kuangfjsxgbean)getEditValues().get(0)).getShoukdw()+"',"
	        			+ " kaihyh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getKaihyh()+"', zhangh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getZhangh()+"',"
	        			+ " fapbh = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFapbh()+"',fukfs = '"+((Kuangfjsxgbean)getEditValues().get(0)).getFukfs()+"',"
	        			+ " duifdd = '"+((Kuangfjsxgbean)getEditValues().get(0)).getDuifdd()+"', ches ="+((Kuangfjsxgbean)getEditValues().get(0)).getChes()+","
	        			+ " guotyf = "+((Kuangfjsxgbean)getEditValues().get(0)).getTielyf()+",dityf = "+((Kuangfjsxgbean)getEditValues().get(0)).getQitzf()+","
	        			+ " jiskc = "+((Kuangfjsxgbean)getEditValues().get(0)).getJiskc()+", hansdj = "+((Kuangfjsxgbean)getEditValues().get(0)).getShulzjbz()+","
	        			+ " bukyf = "+((Kuangfjsxgbean)getEditValues().get(0)).getBukyqyzf()+",hansyf = "+((Kuangfjsxgbean)getEditValues().get(0)).getYunzfhj()+","
	        			+ " buhsyf = "+((Kuangfjsxgbean)getEditValues().get(0)).getBuhsyf()+",shuik = "+((Kuangfjsxgbean)getEditValues().get(0)).getYunfsk()+","
	        			+ " shuil = "+((Kuangfjsxgbean)getEditValues().get(0)).getYunfsl()+",";
	        	
	        		if(getHetbhValue().getId()>-1){
	        			
	        			sql=sql+ " hetb_id = "+getHetbhValue().getId()+",";
	        		}	
	        		
	        		sql=sql+" beiz = '"+((Kuangfjsxgbean)getEditValues().get(0)).getBeiz()+"' where id = "+((Kuangfjsxgbean)getEditValues().get(0)).getYid()+"";
	        		
	        		con.getUpdate(sql);
	        		
	        		setMsg("结算单更新成功！");
		        		
	        }
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
	}
	
	private void Delete(IRequestCycle cycle){
	    JDBCcon con=new JDBCcon();
	    try{
	    	String sql="";
	    	long kuangfjsbId=0;
	    	int zhuangt=0;
	    	if(getEditValues()!= null && !getEditValues().isEmpty()){
	    		//不单独删除运费
	    		if(((Kuangfjsxgbean) getEditValues().get(0)).getJieslx()<2){
	    			
	    			sql="select d.id as zongbid,case when d.hansmk=dm.hansmk and d.yunzfhj=dy.hansyf then 1 else 0 end zhuangt"
	    				+ " from kuangfjsb d,kuangfjsmkb dm,kuangfjsyfb dy "
	    				+ " where d.id=dm.kuangfjsb_id and d.id=dy.kuangfjsb_id"
	    				+ " and dm.id="+((Kuangfjsxgbean) getEditValues().get(0)).getId()+"";
	    			
	    			ResultSet rs=con.getResultSet(sql);
	    			if(rs.next()){
	    				
	    				kuangfjsbId=rs.getLong("zongbid");
	    				zhuangt=rs.getInt("zhuangt");
//	    				zhuangt=1删除总表
//	    				zhuangt=0更新总表
	    				if(zhuangt==1){
	    					
	    					sql="delete from kuangfjsmkb where id="+((Kuangfjsxgbean) getEditValues().get(0)).getId();
	    					con.getDelete(sql);
	    					
	    					sql="delete from kuangfjsyfb where id="+((Kuangfjsxgbean) getEditValues().get(0)).getYid();
	    					con.getDelete(sql);
	    					
	    					sql="delete from kuangfjsb where id="+kuangfjsbId;
	    					con.getDelete(sql);
	    					
	    				}else{
	    					
	    					sql="update kuangfjsb set hansmk=hansmk-"+((Kuangfjsxgbean) getEditValues().get(0)).getJiasje()+","
	    						+ " buhsmk=buhsmk-"+((Kuangfjsxgbean) getEditValues().get(0)).getJiakhj()
	    						+",meisk=meisk-"+((Kuangfjsxgbean) getEditValues().get(0)).getJiaksk()
	    						+",guotyf=guotyf-"+((Kuangfjsxgbean) getEditValues().get(0)).getTielyf()
	    						+",dityf=dityf-"+((Kuangfjsxgbean) getEditValues().get(0)).getQitzf()
	    						+",jiskc=jiskc-"+((Kuangfjsxgbean) getEditValues().get(0)).getJiskc()
	    						+",yunfsk=yunfsk-"+((Kuangfjsxgbean) getEditValues().get(0)).getYunfsk()
	    						+",yunzfhj=yunzfhj-"+((Kuangfjsxgbean) getEditValues().get(0)).getYunzfhj()
	    						+",hanshj=hanshj-"+((Kuangfjsxgbean) getEditValues().get(0)).getHej()
	    						+" where id="+kuangfjsbId;
	    					
	    					con.getUpdate(sql);
	    				}
	    			}
//	    			单独删除运费
	    		}else{
	    			
	    			sql="delete from kuangfjsyfb where id="+((Kuangfjsxgbean) getEditValues().get(0)).getYid();
					con.getDelete(sql);
					
					sql="update kuangfjsb set guotyf=guotyf-"+((Kuangfjsxgbean) getEditValues().get(0)).getTielyf()
						+",dityf=dityf-"+((Kuangfjsxgbean) getEditValues().get(0)).getQitzf()
						+",jiskc=jiskc-"+((Kuangfjsxgbean) getEditValues().get(0)).getJiskc()
						+",yunfsk=yunfsk-"+((Kuangfjsxgbean) getEditValues().get(0)).getYunfsk()
						+",yunzfhj=yunzfhj-"+((Kuangfjsxgbean) getEditValues().get(0)).getYunzfhj()
						+",hanshj=hanshj-"+((Kuangfjsxgbean) getEditValues().get(0)).getHej()
						+" where id="+kuangfjsbId;
				
					con.getUpdate(sql);
	    		}
	   		 
				setMsg("编号："+((Kuangfjsxgbean)getEditValues().get(0)).getJiesbh()+"结算单已删除！");
				
	    	}else{
	    		
	    		setMsg("请选择要删除的结算单！");
	    	}
	    }catch(Exception e){
	    	
	    	e.printStackTrace();
	    }finally{
	    	
	    	con.Close();
	    }
	    getIKuangfjsmkbhModels();
		getEditValues().clear();
	}
	
	private void Retruns() {
		//为 "返回" 按钮添加处理程序
		//List _list =((Visit) getPage().getVisit()).getEditValues();
		//((KuangfjsxgBean) _list.get(i)).getXXX();
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		
		_QuedChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			Save(cycle);
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete(cycle);
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			Retruns();
		}
		if(_QuedChick){
			_QuedChick=false;
			Tij();
		}
	}
	
	private void Tij(){
		
		if(getSelectZhuangt()==1){
//			修改合同	
				if(getHetbhValue().getId()>-1){
					 
					Fuz();
				}
			}else if(getSelectZhuangt()==2){
//				进入流程
				
			}
	}
	
	private Kuangfjsxgbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Kuangfjsxgbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Kuangfjsxgbean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}

	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		if(!(getEditValues()==null||getEditValues().isEmpty())){
			getEditValues().clear();
		}
		JDBCcon con=new JDBCcon();
		try {
			
			long mid = 0;
			long myid = 0;
			String mkuangfjsbh = "";
			String mmeikjsdbh = "";
			String mtianzdw = "";

			String mjiesbh = "";
			String mfahdw = "";
			String mfaz = "";
			String mshoukdw = "";
			String mkaihyh = "";
			String mzhangh = "";

			String mhetsl = "";
			double mgongfsl = 0;
			double myanssl = 0;
			double mshulzjbz = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			//
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date mkaisysrq = new Date();
			Date mjiezysrq = new Date();
			String mpinz = "";
			String myuanshr = "";

			long mches = 0;
			String mxianshr = "";
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";

			String mhetrl = "";
			double mgongfrl = 0;
			double myansrl = 0;
			double mjiesrl = 0;
			double myingkrl = 0;
			double mrelzjbz = 0;
			double mrelzjje = 0;

			String mhethff = "";
			double mgongfhff = 0;
			double myanshff = 0;
			double mjieshff = 0;
			double myingkhff = 0;
			double mhuiffzjbz = 0;
			double mhuiffzjje = 0;

			String mhethf = "";
			double mgongfhf = 0;
			double myanshf = 0;
			double mjieshf = 0;
			double myingkhf = 0;
			double mhuifzjbz = 0;
			double mhuifzjje = 0;

			String mhetsf = "";
			double mgongfsf = 0;
			double myanssf = 0;
			double mjiessf = 0;
			double myingksf = 0;
			double mshuifzjbz = 0;
			double mshuifzjje = 0;

			String mhetlf = "";
			double mliubz = 0;
			double myanslf = 0;
			double mjieslf = 0;
			double mliuyk = 0;
			double mliuzjbz = 0;
			double mliuzjje = 0;

			double mjiessl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mbeiz = "";
			double mdanjc = 0;
			String mranlbmjbr = "";
			Date mranlbmjbrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = new Date();
			int mjieslx = 0;
			double mjingz = 0;

			double mtielyf = 0;
			double mzaf = 0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mkuidjf = 0;
			Date mjiesrq = new Date();
			String mfahrq = "";
			double mhuirdzjbz = 0;
			double mhuirdzjje = 0;
			double mhuirdbz = 0;
			double mgongfhrd = 0;
			double myanshrd = 0;
			double mqitzf = 0;

			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit())
					.getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			double mrelsx = 0;
			double mrelxx = 0;
			double mliusx = 0;
			double mliuxx = 0;
			double myuns = 0;
			String myunsfs = "";
			boolean blnHasMeik = false;
			
			String sql="select * from kuangfjsmkb where bianm='"+getKuangfjsmkbhValue().getValue()+"'";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){

				mid = rs.getLong("id");
				myid = 0;
				mmeikjsdbh = rs.getString("bianm");
				mtianzdw = getProperValue(getIDiancmcModel(), rs
						.getInt("diancxxb_id"));

				mjiesbh = rs.getString("bianm");
				mfahdw = rs.getString("gongysmc");
				mfaz = rs.getString("faz");
				mshoukdw = rs.getString("shoukdw");
				mkaihyh = rs.getString("kaihyh");
				mzhangh = rs.getString("zhangh");
				mfahksrq = rs.getDate("fahksrq");
				mfahjzrq = rs.getDate("fahjzrq");
				mkaisysrq = rs.getDate("yansksrq");
				mjiezysrq = rs.getDate("yansjzrq");
				mpinz = rs.getString("meiz");
				myuanshr = rs.getString("yuanshr");
				mches = rs.getInt("ches");
				mxianshr = rs.getString("xianshr");
				mdaibcc = rs.getString("daibch");
				myansbh = rs.getString("yansbh");
				mduifdd = rs.getString("duifdd");
				mfukfs = rs.getString("fukfs");
				mjiessl = rs.getDouble("jiessl");
				mbuhsdj = rs.getDouble("hansdj");
				mjiakje = rs.getDouble("meikje");
				mbukyqjk = rs.getDouble("bukmk");
				mfapbh=rs.getString("fapbh");
				mjiakhj = rs.getDouble("buhsmk");
				mjiaksl = rs.getDouble("shuil");
				mjiaksk = rs.getDouble("shuik");
				mjiasje = rs.getDouble("hansmk");
				mhej = rs.getDouble("hansmk");
				mdaxhj = getDXMoney(mhej);
				mbeiz = rs.getString("beiz");
				mjieslx = rs.getInt("jieslx");
				mjingz = rs.getDouble("guohl");
				mdanjc=rs.getDouble("danjc");
				//
				sql = "select jieszbsjb.*,zhibb.mingc from jieszbsjb,kuangfjsmkb,zhibb "
						+ " where jieszbsjb.jiesdid=kuangfjsmkb.id and zhibb.id=jieszbsjb.zhibb_id"
						+ " and kuangfjsmkb.bianm='"
						+ getKuangfjsmkbhValue().getValue()
						+ "' and jieszbsjb.zhuangt=1 ";

				ResultSet rec = con.getResultSet(sql);

				while (rec.next()) {

					if (rec.getString("mingc").equals("数量")) {

						mhetsl = rec.getString("hetbz");
						mgongfsl = rec.getDouble("gongf");
						myanssl = rec.getDouble("changf");
						myingksl = rec.getDouble("yingk");
						mshulzjbz = rec.getDouble("zhejbz");
						mshulzjje = rec.getDouble("zhejje");

					} else if (rec.getString("mingc").equals("收到基低位热值")) {

						mhetrl = rec.getString("hetbz");
						mgongfrl = rec.getDouble("gongf");
						myansrl = rec.getDouble("changf");
						mjiesrl = rec.getDouble("jies");
						myingkrl = rec.getDouble("yingk");
						mrelzjbz = rec.getDouble("zhejbz");
						mrelzjje = rec.getDouble("zhejje");

					} else if (rec.getString("mingc").equals("干燥无灰基挥发分")) {

						mhethff = rec.getString("hetbz");
						mgongfhff = rec.getDouble("gongf");
						myanshff = rec.getDouble("changf");
						mjieshff = rec.getDouble("jies");
						myingkhff = rec.getDouble("yingk");
						mhuiffzjbz = rec.getDouble("zhejbz");
						mhuiffzjje = rec.getDouble("zhejje");

					} else if (rec.getString("mingc").equals("干燥基灰分")) {

						mhethf = rec.getString("hetbz");
						mgongfhf = rec.getDouble("gongf");
						myanshf = rec.getDouble("changf");
						mjieshf = rec.getDouble("jies");
						myingkhf = rec.getDouble("yingk");
						mhuifzjbz = rec.getDouble("zhejbz");
						mhuifzjje = rec.getDouble("zhejje");

					} else if (rec.getString("mingc").equals("全水分")) {

						mhetsf = rec.getString("hetbz");
						mgongfsf = rec.getDouble("gongf");
						myanssf = rec.getDouble("changf");
						mjiessf = rec.getDouble("jies");
						myingksf = rec.getDouble("yingk");
						mshuifzjbz = rec.getDouble("zhejbz");
						mshuifzjje = rec.getDouble("zhejje");

					} else if (rec.getString("mingc").equals("干燥基全硫")) {

						mhetlf = rec.getString("hetbz");
						mliubz = rec.getDouble("gongf");
						myanslf = rec.getDouble("changf");
						mjieslf = rec.getDouble("jies");
						mliuyk = rec.getDouble("yingk");
						mliuzjbz = rec.getDouble("zhejbz");
						mliuzjje = rec.getDouble("zhejje");
					}
				}
					blnHasMeik = true;
					rec.close();
				}
				if (blnHasMeik && mjieslx == 1) {

					sql = "select * from diancjsyfb where bianm='"
							+ getKuangfjsmkbhValue().getValue() + "'";

					rs = con.getResultSet(sql);
					if (rs.next()) {

						myid = rs.getLong("id");
						mtielyf = rs.getDouble("guotyf");
						mqitzf = rs.getDouble("dityf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc = rs.getDouble("jiskc");
						mbuhsyf = rs.getDouble("buhsyf");
						myunfsl = rs.getDouble("shuil");
						myunfsk = rs.getDouble("shuik");
						myunzfhj = rs.getDouble("hansyf");
					}

				} else if (mjieslx != 0) {

					sql = "select * from kuangfjsyfb where bianm='"
							+ getKuangfjsmkbhValue().getValue() + "'";

					rs = con.getResultSet(sql);
					if (rs.next()) {

						myid = rs.getLong("id");
						mdanjc=rs.getDouble("danjc");
						mtianzdw = rs.getString("xianshr");
						mjiesbh = rs.getString("bianm");
						mfahdw = rs.getString("gongysmc");
						mfaz = rs.getString("faz");
						mshoukdw = rs.getString("shoukdw");
						mfahksrq = rs.getDate("fahksrq");
						mfahjzrq = rs.getDate("fahjzrq");
						mkaisysrq = rs.getDate("yansksrq");
						mjiezysrq = rs.getDate("yansjzrq");
						mkaihyh = rs.getString("kaihyh");
						mpinz = rs.getString("meiz");
						myuanshr = rs.getString("yuanshr");
						mzhangh = rs.getString("zhangh");
						mches = rs.getInt("ches");
						mxianshr = rs.getString("xianshr");
						mfapbh = rs.getString("fapbh");
						mdaibcc = rs.getString("daibch");
						myansbh = rs.getString("yansbh");
						mduifdd = rs.getString("duifdd");
						mfukfs = rs.getString("fukfs");
						mjiessl = rs.getDouble("jiessl");
						mtielyf = rs.getDouble("guotyf");
						mqitzf = rs.getDouble("dityf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc = rs.getDouble("jiskc");
						mbuhsyf = rs.getDouble("buhsyf");
						myunfsl = rs.getDouble("shuil");
						myunfsk = rs.getDouble("shuik");
						myunzfhj = rs.getDouble("hansyf");
					}
				}
					_editvalues.add(new Kuangfjsxgbean(mid,myid,
							mtianzdw, mjiesbh, mfahdw, mfaz, mshoukdw, mfahksrq,
							mfahjzrq, mkaisysrq, mjiezysrq, mkaihyh, mpinz,
							myuanshr, mzhangh, mhetsl, mgongfsl, mches, mxianshr,
							mfapbh, mdaibcc, myansbh, mduifdd, mfukfs, mshulzjbz,
							myanssl, myingksl, mshulzjje, mhetrl, mgongfrl,
							myansrl, mjiesrl, myingkrl, mrelzjbz, mrelzjje,
							mhethff, mgongfhff, myanshff, mjieshff, myingkhff,
							mhuiffzjbz, mhuiffzjje, mhethf, mgongfhf, myanshf,
							mjieshf, myingkhf, mhuifzjbz, mhuifzjje, mhetsf,
							mgongfsf, myanssf, mjiessf, myingksf, mshuifzjbz,
							mshuifzjje, mhetlf, myanslf, mjieslf, mliuyk, mliubz,
							mliuzjbz, mliuzjje, mjiessl, mbuhsdj, mjiakje,
							mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,
							mzaf, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
							myunzfhj, mhej, mdaxhj, mbeiz, mdanjc, mranlbmjbr,
							mranlbmjbrq, mkuidjf, mjingz, mjiesrq, mfahrq,
							mhuirdzjbz, mhuirdzjje, mhuirdbz, mgongfhrd, myanshrd,
							mqitzf, mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr,
							mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq, mhetjg,
							mjieslx, mrelsx, mrelxx, mliusx, mliuxx, myuns,
							myunsfs, mkuangfjsbh, mmeikjsdbh));
				
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
			if (_editvalues == null) {
				_editvalues.add(new Kuangfjsxgbean());
			}
			setEditTableRow(-1);
			setEditValues(_editvalues);
			return getEditValues();
	}	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			visit.setInt1(1);//是第一次显示
			visit.setInt2(0);//判断修改合同还是提交流程
			zhuangt=visit.getInt1();
			chaxunzt1 = 0;
			_JiesrqsmallValue = new Date();
			_JiesrqbigValue = new Date();
			
			if(visit.isJTUser()){
				
				setGongsValue(null);// drop1
				setIGongsModel(null);
				getIGongsModels();
			}
			
			setFahdwValue(null);// drop2
			setIFahdwModel(null);
			getIFahdwModels();
			
			setFazValue(null);//drop3
			setIFazModel(null);
			getIFazModels();
			
			setRanlpzValue(null);// drop4
			setIRanlpzModel(null);
			getIRanlpzModels();

			setShoukdwValue(null);// drop5
			setIShoukdwModel(null);
			getIShoukdwModels();
			
			setDiancmcValue(null);// drop7
			setIDiancmcModel(null);//
			getIDiancmcModels();
			
			setHetbhValue(null);// drop8
			setIHetbhModel(null);//
			getIHetbhModels();
			
			setKuangfjsmkbhValue(null);// drop10
			setIKuangfjsmkbhModel(null);
			visit.setboolean1(false);//结算单变化时
			getIKuangfjsmkbhModels();
			
			setLiucmcValue(null);// drop11
			setILiucmcModel(null);
			getILiucmcModels();
			
			getSelectData();
		}
		if(zhuangt==1){//不要动
			
			visit.setInt1(1);
			
			
		}
		if(zhuangt==2){//不要动
			
			visit.setInt1(2);
			
		}

		if(visit.getboolean1()){
			visit.setboolean1(true);
			getSelectData();
		}
	}
	
	private void Fuz() {
		JDBCcon con = new JDBCcon();
		try {
			if (getEditValues().size() > 0) {
				String sql = "select h.gongfdwmc,h.gongfkhyh,h.gongfzh from hetb h where h.id="
						+ getHetbhValue().getId() + "";
				ResultSet rs = con.getResultSet(sql);
				if (rs.next()) {

					((Kuangfjsbean) getEditValues().get(0)).setShoukdw(rs
							.getString("gongfdwmc"));
					((Kuangfjsbean) getEditValues().get(0)).setKaihyh(rs
							.getString("gongfkhyh"));
					((Kuangfjsbean) getEditValues().get(0)).setZhangh(rs
							.getString("gongfzh"));
				} else {
					
					sql = "select k.shoukdw,k.zhangh,k.kaihyh from kuangfjsmkb k where k.hetb_id="
							+ getHetbhValue().getId() + "'";
					rs = con.getResultSet(sql);
					if (rs.next()) {
						
						((Kuangfjsbean) getEditValues().get(0)).setShoukdw(rs
								.getString("shoukdw"));
						((Kuangfjsbean) getEditValues().get(0)).setKaihyh(rs
								.getString("kaihyh"));
						((Kuangfjsbean) getEditValues().get(0)).setZhangh(rs
								.getString("zhangh"));
					}
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
//收款单位
	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getIShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setShoukdwValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5() != value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}
	}

	public void setIShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIShoukdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getIShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIShoukdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			String sql = "select distinct gongfdwmc from hetb h order by gongfdwmc";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("gongfdwmc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
//    

	// 填制单位、电厂名称

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setDiancmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean7() != value) {
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getIDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "select id,quanc from diancxxb";
		String where = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			where = " where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			where = " where id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by quanc";
		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			where = " where jib=3 order by quanc ";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(sql + where, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//	end
	
//	 合同号
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getIHetbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHetbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean8() != value) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setIHetbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getIHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getIHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getIHetbhModels() {

		String sql = "";

		if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			sql = "select id,hetbh from hetb where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "  and leib=1 order by hetbh";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			sql = "select id,hetbh from hetb where diancxxb_id in (select id from diancxxb where fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") and leib=1 order by hetbh";

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			sql = "select id,hetbh from hetb where leib=1 order by hetbh";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	//
    
//  填制单位
	private IDropDownBean _TianzdwValue2 ;
    public IDropDownBean getTianzdwValue2() {
    	if(_TianzdwValue2==null){
    		_TianzdwValue2=(IDropDownBean)getITianzdwModel2().getOption(0);
    	}
       return _TianzdwValue2;
    }
    public void setTianzdwValue2(IDropDownBean Value)
    {
    	_TianzdwValue2=Value;
    }
    private static IPropertySelectionModel _ITianzdwModel2;

    public void setITianzdwModel2(IPropertySelectionModel value){
    	_ITianzdwModel2 = value;
	}
    public IPropertySelectionModel getITianzdwModel2(){
            if(_ITianzdwModel2==null){
                getITianzdwModels2();
            }
            return _ITianzdwModel2;
    }
    public IPropertySelectionModel getITianzdwModels2(){
        String sql="select id,quanc from diancxxb order by quanc";
        _ITianzdwModel2 = new IDropDownModel(sql);
        return _ITianzdwModel2;
    }
//	end
    
//  所属公司
	public IDropDownBean getGongsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIGongsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongsValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIGongsModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIGongsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getIGongsModels();
		}

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIGongsModels() {

		String sql = "select id,mingc from diancxxb where jib=2 order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

//	end
    
//  发货单位
	public IDropDownBean getFahdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIFahdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFahdwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIFahdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIFahdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIFahdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIFahdwModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		String sql = "";
		try {

			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select gongysmc from "
						+ " (select distinct gongysmc from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct gongysmc from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by gongysmc";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

				sql = " select gongysmc from "
						+ " (select distinct gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by gongysmc";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				sql = "select gongysmc from "
						+ " (select distinct gongysmc from diancjsmkb where liucztb_id=0)"
						+ " union"
						+ " (select distinct gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
			}

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("gongysmc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
    
//	end

//  发站
	public IDropDownBean getFazValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIFazModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setFazValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setIFazModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIFazModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIFazModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIFazModels() {

		String sql = "";
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select faz from "
						+ " (select distinct faz from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by faz";
			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

				sql = " select faz from "
						+ " (select distinct faz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by faz";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				sql = "select faz from "
						+ " (select distinct faz from diancjsmkb where liucztb_id=0)"
						+ " union"
						+ " (select distinct faz from diancjsyfb where liucztb_id=0) order by faz";
			}

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("faz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    
//	end
    
//  品种
	public IDropDownBean getRanlpzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIRanlpzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setRanlpzValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean4(value);
	}

	public void setIRanlpzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIRanlpzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIRanlpzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIRanlpzModels() {

		String sql = "";
		List List = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			int i = -1;
			List.add(new IDropDownBean(i++, "请选择"));
			if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

				sql = " select meiz from "
						+ " (select distinct meiz from diancjsmkb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb where diancxxb_id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by meiz";
			} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

				sql = " select meiz from "
						+ " (select distinct meiz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
						+ " and liucztb_id=0) order by meiz";

			} else if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

				sql = "select meiz from "
						+ " (select distinct meiz from diancjsmkb where liucztb_id=0)"
						+ " union"
						+ " (select distinct meiz from diancjsyfb where liucztb_id=0) order by meiz";
			}
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("meiz")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
    
//	end
//  **********************打印界面DIV中的数据处理 开始**************************//
//  判断页面是否是第一次调用(报表用)
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	
//	int2 状态 判断是修改合同号还是提交进入流程	1、修改合同，2、提交进入流程
	public int getSelectZhuangt() {
		return ((Visit) getPage().getVisit()).getInt2();
	}

	public void setSelectZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt2(_value);
	}
	
    private String RT_HET = "jies";

	private String mstrReportName = "jies";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_HET)) {
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			return getPrintData();
		} else {
			return "无此报表";
		}
	}
	public boolean getRaw() {
		return true;
	}
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
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
	
	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from gongsxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private int chaxunzt1 = 0;// 查询状态
	private int zhuangt =1;
	
	public String getPrintData() {
		Visit visit = (Visit) getPage().getVisit();
		Kuangfjsd jsd = new Kuangfjsd();

		if(getKuangfjsmkbhValue().getValue()==""){
			return "";
		}else{
			visit.setInt1(2);//是第一次显示
			chaxunzt1=0;
			zhuangt=1;
			return jsd.getKuangfjsd(getKuangfjsmkbhValue().getValue(),0);
		}

	}
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

//**********************打印界面DIV中的数据处理 结束**************************//
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
//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getMissSession()) {
//			return "window.opener=null;self.close();window.parent.close();open('"
//					+ getpageLinks() + "','');";
//		} else {
//			return "";
//		}
//	}
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
