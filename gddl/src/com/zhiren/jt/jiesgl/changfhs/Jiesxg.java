package com.zhiren.jt.jiesgl.changfhs;

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
import com.zhiren.common.Money;
import com.zhiren.common.DateUtil;
import com.zhiren.common.Liuc;
import com.zhiren.common.IDropDownBean;
 
public class Jiesxg extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
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

	private boolean checkbh() {
        // TODO 自动生成方法存根
	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        sql=" select bianm from ((select id,bianm as bianm from diancjsmkb)union(select id,bianm as bianm from diancjsyfb)) where bianm='"+((Balancebean)getEditValues().get(0)).getDiancjsbs()+((Balancebean)getEditValues().get(0)).getJiesbh()+"' and (id<>"+((Balancebean)getEditValues().get(0)).getId()+" and id<>"+((Balancebean)getEditValues().get(0)).getYid()+")";
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
	private void Save() {

	    JDBCcon con = new JDBCcon();
	    String sql = "";
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Balancebean)getEditValues().get(0)).getJiesbh().equals("")){
	        	if(checkbh()){
	        				
			        		sql=" update diancjsmkb set bianm = '"+((Balancebean)getEditValues().get(0)).getDiancjsbs()+((Balancebean)getEditValues().get(0)).getJiesbh()+"',"
							       + " gongysb_id = "+getProperId(getIFahdwModel(),((Balancebean)getEditValues().get(0)).getFahdw())+","
							       + " gongysmc = '"+((Balancebean)getEditValues().get(0)).getFahdw()+"',"
							       + " faz = '"+((Balancebean)getEditValues().get(0)).getFaz()+"',"
							       + " fahjzrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
							       + " meiz = '"+((Balancebean)getEditValues().get(0)).getPinz()+"',daibch ='"+((Balancebean)getEditValues().get(0)).getDaibcc()+"'," 
							       + " yuanshr ='"+((Balancebean)getEditValues().get(0)).getYuanshr()+"',xianshr = '"+((Balancebean)getEditValues().get(0)).getXianshr()+"',"
							       + " yansksrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd')," 
							       + " yansjzrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),"
							       + " shoukdw ='"+((Balancebean)getEditValues().get(0)).getShoukdw()+"',kaihyh = '"+((Balancebean)getEditValues().get(0)).getKaihyh()+"',"
							       + " yansbh = '"+((Balancebean)getEditValues().get(0)).getYansbh()+"',zhangh = '"+((Balancebean)getEditValues().get(0)).getZhangh()+"',"
							       + " fapbh = '"+((Balancebean)getEditValues().get(0)).getFapbh()+"',fukfs = '"+((Balancebean)getEditValues().get(0)).getFukfs()+"',"
							       + " duifdd = '"+((Balancebean)getEditValues().get(0)).getDuifdd()+"',ches ="+((Balancebean)getEditValues().get(0)).getChes()+","
							       + " jiessl = "+((Balancebean)getEditValues().get(0)).getJiessl()+",guohl = "+((Balancebean)getEditValues().get(0)).getJingz()+","
							       + " hansdj = "+((Balancebean)getEditValues().get(0)).getShulzjbz()+",bukmk = "+((Balancebean)getEditValues().get(0)).getBukyqjk()+","
							       + " hansmk = "+((Balancebean)getEditValues().get(0)).getJiasje()+",buhsmk = "+((Balancebean)getEditValues().get(0)).getJiakhj()+","
							       + " shuik = "+((Balancebean)getEditValues().get(0)).getJiaksk()+",shuil = "+((Balancebean)getEditValues().get(0)).getJiaksl()+","
							       + " buhsdj = "+((Balancebean)getEditValues().get(0)).getBuhsdj()+",";
			        		if(getHetbhValue().getId()>-1){
			        			
			        			sql=sql+" hetb_id = "+getHetbhValue().getId()+",";
			        		}
							sql=sql+ " beiz = '"+((Balancebean)getEditValues().get(0)).getBeiz()+"',meikje = "+((Balancebean)getEditValues().get(0)).getJiakje()+","
							       + " ranlbmjbr='"+((Balancebean)getEditValues().get(0)).getRanlbmjbr()+"',ranlbmjbrq=to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getRanlbmjbrq())+"','yyyy-MM-dd')"
							       + " where id ="+((Balancebean)getEditValues().get(0)).getId()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		sql=" update diancjsyfb set bianm = '"+((Balancebean)getEditValues().get(0)).getDiancjsbs()+((Balancebean)getEditValues().get(0)).getJiesbh()+"',gongysb_id = "+getProperId(getIFahdwModel(),((Balancebean)getEditValues().get(0)).getFahdw())+","
			        			+ " gongysmc = '"+((Balancebean)getEditValues().get(0)).getFahdw()+"',faz = '"+((Balancebean)getEditValues().get(0)).getFaz()+"', fahksrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getFahksrq())+"','yyyy-MM-dd'),"
			        			+ " fahjzrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getFahjzrq())+"','yyyy-MM-dd'),meiz ='"+((Balancebean)getEditValues().get(0)).getPinz()+"',daibch ='"+((Balancebean)getEditValues().get(0)).getDaibcc()+"',"
			        			+ " yuanshr = '"+((Balancebean)getEditValues().get(0)).getYuanshr()+"', xianshr = '"+((Balancebean)getEditValues().get(0)).getXianshr()+"',yansksrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getYansksrq())+"','yyyy-MM-dd'),"
			        			+ " yansjzrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getYansjzrq())+"','yyyy-MM-dd'),yansbh ='"+((Balancebean)getEditValues().get(0)).getYansbh()+"',shoukdw = '"+((Balancebean)getEditValues().get(0)).getShoukdw()+"',"
			        			+ " kaihyh = '"+((Balancebean)getEditValues().get(0)).getKaihyh()+"',zhangh = '"+((Balancebean)getEditValues().get(0)).getZhangh()+"', fapbh = '"+((Balancebean)getEditValues().get(0)).getFapbh()+"',"
			        			+ " fukfs = '"+((Balancebean)getEditValues().get(0)).getFukfs()+"',duifdd = '"+((Balancebean)getEditValues().get(0)).getDuifdd()+"',ches ="+((Balancebean)getEditValues().get(0)).getChes()+","
			        			+ " jiessl = "+((Balancebean)getEditValues().get(0)).getJiessl()+", guohl = "+((Balancebean)getEditValues().get(0)).getJingz()+", guotyf = "+((Balancebean)getEditValues().get(0)).getTielyf()+","
			        			+ " dityf = "+((Balancebean)getEditValues().get(0)).getQitzf()+",jiskc = "+((Balancebean)getEditValues().get(0)).getJiskc()+",bukyf ="+((Balancebean)getEditValues().get(0)).getBukyqyzf()+", hansyf = "+((Balancebean)getEditValues().get(0)).getYunzfhj()+","
			        			+ " buhsyf = "+((Balancebean)getEditValues().get(0)).getBuhsyf()+",shuik = "+((Balancebean)getEditValues().get(0)).getYunfsk()+", shuil ="+((Balancebean)getEditValues().get(0)).getYunfsl()+",jiesrq = to_date('"+this.FormatDate(((Balancebean)getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),";
			        			
			        		if(getHetbhValue().getId()>-1){
			        			
			        			sql=sql+" hetb_id = "+getHetbhValue().getId()+",";
			        		}	
			        		sql=sql+ " beiz = '"+((Balancebean)getEditValues().get(0)).getBeiz()+"' where id = "+((Balancebean)getEditValues().get(0)).getYid()+"";
			        		
			        		con.getUpdate(sql);
			        		
			        		setMsg("结算单更新成功！");
		        		
//	        		}else{
//	        			setMsg("地区信息表中无该地区，请添加！");
//	        		}
	        		
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
	private String Diqbm(String Meikdqmc){
		
		JDBCcon con=new JDBCcon();
		String mdiqbm="";
		try{
			String sql="select meikbm from ((select meikbm,meikdwmc as meikdqqc from meikxxb)union(select meikdqbm as meikbm,meikdqmc from meikdqb)) where meikdqqc='"+Meikdqmc+"'";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				mdiqbm=rs.getString("meikbm");
			}else{
				sql="select diqbm from diancjsmkb  where id = "+((Balancebean)getEditValues().get(0)).getId();
				ResultSet rs2=con.getResultSet(sql);
				if(rs2.next()){
					mdiqbm=rs2.getString("diqbm");
				}
				rs2.close();
			}
			
			if(mdiqbm.length()>6){
				
				mdiqbm=mdiqbm.substring(0,6);
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}	
		return mdiqbm;
	}
	
	private long Diancjsmkb_id(String meikjsdbh){
		
		JDBCcon con=new JDBCcon();
		long id=0;
		try{
			String sql="select id from diancjsmkb where bianm='"+meikjsdbh+"'";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				id=rs.getLong("id");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}	
		return id;
	}
	
	private boolean CheckMeikjsdbh(String meikjsdbh){
		if(!meikjsdbh.equals("")){
			JDBCcon con=new JDBCcon();
			try{
				String sql="select id from diancjsmkb where bianm='"+meikjsdbh+"'";
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){
					return true;
				}
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}	
			return false;
			
		}else{
			
			return true;
		}
	}
	

	private void Retruns() {
		//为 "返回" 按钮添加处理程序
		//List _list =((Visit) getPage().getVisit()).getEditValues();
		//((Balancebean) _list.get(i)).getXXX();
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
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if(_QuedChick){
			_QuedChick=false;
			Submit();
		}
	}
	
	private void Fuz() {
		JDBCcon con = new JDBCcon();

		try {
			String sql = "select GONGFDWMC,GONGFKHYH,GONGFZH,gongys,pinz,faz,daoz "
						+ " from hetb,"
						+ " (select quanc as gongys from gongysb,hetb " 
						+ " where hetb.gongysb_id=gongysb.id and hetbh='"+getHetbhValue().getValue()+"'),"
						+ " (select distinct pinzb.mingc as pinz from hetslb,hetb,pinzb " 
						+ " where hetslb.hetb_id=hetb.id and hetslb.pinzb_id=pinzb.id and hetbh='"+getHetbhValue().getValue()+"'),"
						+ " (select distinct mingc as faz from hetslb,hetb,chezxxb "
						+ " where hetslb.faz_id=chezxxb.id and hetslb.hetb_id=hetb.id and hetbh='"+getHetbhValue().getValue()+"'),"
						+ " (select distinct mingc as daoz from hetslb,hetb,chezxxb "
					    + " where hetslb.daoz_id=chezxxb.id and hetslb.hetb_id=hetb.id and hetbh='"+getHetbhValue().getValue()+"')"
						+ " where hetbh='"+getHetbhValue().getValue()+"'";
			
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				
				((Balancebean) getEditValues().get(0)).setFahdw(rs
						.getString("gongys"));
				((Balancebean) getEditValues().get(0)).setShoukdw(rs
						.getString("GONGFDWMC"));
				((Balancebean) getEditValues().get(0)).setKaihyh(rs
						.getString("GONGFKHYH"));
				((Balancebean) getEditValues().get(0)).setZhangh(rs
						.getString("GONGFZH"));
				((Balancebean) getEditValues().get(0)).setPinz(rs
						.getString("pinz"));
				((Balancebean) getEditValues().get(0)).setFaz(rs
						.getString("faz"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	
	private void Submit(){
		
		if(getZhuangt()==1){
//		修改合同	
			if(getHetbhValue().getId()>-1){
				
				Fuz();
			}
		}else if(getZhuangt()==2){
			
			if(getLiucmcValue().getId()>-1){
//			
				if(((Balancebean) getEditValues().get(0)).getJieslx()==1){//两票结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(((Balancebean) getEditValues().get(0)).getJieslx()==0){//煤款结算
					
					Liuc.tij("diancjsmkb", ((Balancebean) getEditValues().get(0)).getId(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				
				}else if(((Balancebean) getEditValues().get(0)).getJieslx()==2){//运费结算
					
					Liuc.tij("diancjsyfb", ((Balancebean) getEditValues().get(0)).getYid(), ((Visit) getPage().getVisit()).getRenyID(), "", getLiucmcValue().getId());
				}
			}else{
				
				setMsg("请选择流程编号！");
			}
		}
	}
	
	private void Delete(){
	    JDBCcon con=new JDBCcon();
	    try{
	    	String sql="";
	    	long diancjsbId=0;
	    	int zhuangt=0;
	    	boolean flag=false;
	    	if(getEditValues()!= null && !getEditValues().isEmpty()){
	    		//不单独删除运费
	    		if(((Balancebean) getEditValues().get(0)).getJieslx()<2){
	    			
	    			sql="select d.id as zongbid,case when d.hansmk=dm.hansmk and d.yunzfhj=dy.hansyf then 1 else 0 end zhuangt"
	    				+ " from diancjsb d,diancjsmkb dm,diancjsyfb dy "
	    				+ " where d.id=dm.diancjsb_id and d.id=dy.diancjsb_id"
	    				+ " and dm.id="+((Balancebean) getEditValues().get(0)).getId()+"";
	    			
	    			ResultSet rs=con.getResultSet(sql);
	    			if(rs.next()){
	    				
	    				diancjsbId=rs.getLong("zongbid");
	    				zhuangt=rs.getInt("zhuangt");
//	    				zhuangt=1删除总表
//	    				zhuangt=0更新总表
	    				if(zhuangt==1){
	    					
	    					sql="delete from diancjsmkb where id="+((Balancebean) getEditValues().get(0)).getId();
	    					con.getDelete(sql);
	    					
	    					sql="delete from diancjsyfb where id="+((Balancebean) getEditValues().get(0)).getYid();
	    					con.getDelete(sql);
	    					
	    					sql="delete from diancjsb where id="+diancjsbId;
	    					con.getDelete(sql);
	    					
	    					flag=true;
	    				}else{
	    					
	    					sql="update diancjsb set hansmk=hansmk-"+((Balancebean) getEditValues().get(0)).getJiasje()+","
	    						+ " buhsmk=buhsmk-"+((Balancebean) getEditValues().get(0)).getJiakhj()
	    						+",meisk=meisk-"+((Balancebean) getEditValues().get(0)).getJiaksk()
	    						+",guotyf=guotyf-"+((Balancebean) getEditValues().get(0)).getTielyf()
	    						+",dityf=dityf-"+((Balancebean) getEditValues().get(0)).getQitzf()
	    						+",jiskc=jiskc-"+((Balancebean) getEditValues().get(0)).getJiskc()
	    						+",yunfsk=yunfsk-"+((Balancebean) getEditValues().get(0)).getYunfsk()
	    						+",yunzfhj=yunzfhj-"+((Balancebean) getEditValues().get(0)).getYunzfhj()
	    						+",hanshj=hanshj-"+((Balancebean) getEditValues().get(0)).getHej()
	    						+" where id="+diancjsbId;
	    					
	    					con.getUpdate(sql);
	    				}
	    			}
//	    			单独删除运费
	    		}else{
	    			
	    			sql="delete from diancjsyfb where id="+((Balancebean) getEditValues().get(0)).getYid();
					con.getDelete(sql);
					
					sql="update diancjsb set guotyf=guotyf-"+((Balancebean) getEditValues().get(0)).getTielyf()
						+",dityf=dityf-"+((Balancebean) getEditValues().get(0)).getQitzf()
						+",jiskc=jiskc-"+((Balancebean) getEditValues().get(0)).getJiskc()
						+",yunfsk=yunfsk-"+((Balancebean) getEditValues().get(0)).getYunfsk()
						+",yunzfhj=yunzfhj-"+((Balancebean) getEditValues().get(0)).getYunzfhj()
						+",hanshj=hanshj-"+((Balancebean) getEditValues().get(0)).getHej()
						+" where id="+diancjsbId;
				
					con.getUpdate(sql);
	    		}
	   		 
				setMsg("编号："+((Balancebean)getEditValues().get(0)).getJiesbh()+"结算单已删除！");
				
	    	}else{
	    		
	    		setMsg("请选择要删除的结算单！");
	    	}
	    }catch(Exception e){
	    	
	    	e.printStackTrace();
	    }finally{
	    	
	    	con.Close();
	    }
		getIJiesbhModels();
		getEditValues().clear();
	}
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Balancebean _EditValue;
	public Balancebean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Balancebean EditValue) {
		_EditValue = EditValue;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}

	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		
		JDBCcon con = new JDBCcon();
		try {

			long mid = 0;
			long myid = 0;
			String mtianzdw = "";
			String mjiesbh = "";
			String mfahdw = "";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = "";
			String mzhangh = "";
			String mhetsl = "";// 合同数量
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr ="";
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			String mhetrl = "";// 合同热量
			double mgongfrl = 0;// 供方热量
			double myansrl = 0;
			double myingkrl = 0;
			double mrelzjbz = 0;
			double mjiesrl = 0;// 结算热量
			double mrelzjje = 0;
			String mhethff = "";// 合同挥发分
			double mgongfhff = 0;
			double myanshff = 0;
			double myingkhff = 0;
			double mhuiffzjbz = 0;
			double mhuiffzjje = 0;
			double mjieshff = 0;// 结算挥发分
			String mhethf = "";
			double mgongfhf = 0;
			double myanshf = 0;
			double mjieshf = 0;
			double myingkhf = 0;
			double mhuifzjbz = 0;
			double mhuifzjje = 0;
			String mhetsf = "";// 合同水分
			double mgongfsf = 0;
			double myanssf = 0;
			double myingksf = 0;
			double mshuifzjbz = 0;
			double mshuifzjje = 0;
			double mjiessf = 0;// 结算水分
			String mhetlf = "";// 合同硫分
			double mgongflf = 0;
			double myanslf = 0;
			double mliuyk = 0;
			double mliubz = 0;
			double mliuzjbz = 0;
			double mliuzjje = 0;
			double mjieslf = 0;// 结算硫分
			double mjiessl = 0;
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mzaf = 0;
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
			String mranlbmjbr = "";
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			Date mfahrq = new Date();
			double mhuirdzjbz = 0;
			double mhuirdzjje = 0;
			double mhuirdbz = 0;
			double mgongfhrd = 0;
			double myanshrd = 0;
			double mqitzf = 0;
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = "";
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = 0;
			double mrelsx = 0;
			double mrelxx = 0;
			double mliusx = 0;
			double mliuxx = 0;
			double myuns = 0;
			String myunsfs = "";
			String mdiancjsbs="";
			boolean blnHasMeik=false;
			Money mn=new Money();

			String sql="select * from diancjsmkb where bianm='"+getJiesbhValue().getValue()+"'";
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				mid = rs.getLong("id");
				mtianzdw =rs.getString("xianshr");
				mjiesbh = rs.getString("bianm");
				
				mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
				mjiesbh=mjiesbh.substring(mjiesbh.indexOf("-")+1);
				
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
				
				sql="select jieszbsjb.*,zhibb.mingc from jieszbsjb,diancjsmkb,zhibb "
					 + " where jieszbsjb.jiesdid=diancjsmkb.id and zhibb.id=jieszbsjb.zhibb_id and zhibb.leib=1"
			        + " and diancjsmkb.bianm='"+getJiesbhValue().getValue()+"' and jieszbsjb.zhuangt=1";
				
				ResultSet rs2=con.getResultSet(sql);
					
				while(rs2.next()){
					
					if(rs2.getString("mingc").equals("数量")){
						
						mhetsl = rs2.getString("hetbz");
						mgongfsl =rs2.getDouble("gongf") ;
						mshulzjbz =rs2.getDouble("zhejbz");
						myanssl = rs2.getDouble("changf");
						myingksl = rs2.getDouble("yingk");
						mshulzjje=rs2.getDouble("zhejje");
						
					}else if(rs2.getString("mingc").equals("收到基低位热值")){
						
						mhetrl = rs2.getString("hetbz");// 合同热量
						mgongfrl = rs2.getDouble("gongf");// 供方热量
						myansrl = rs2.getDouble("changf");
						myingkrl = rs2.getDouble("yingk");
						mrelzjbz = rs2.getDouble("zhejbz");
						mjiesrl = rs2.getDouble("jies");// 结算热量
						mrelzjje = rs2.getDouble("zhejje");
						
					}else if(rs2.getString("mingc").equals("干燥基全硫")){
						
						mhetlf = rs2.getString("hetbz");// 合同硫分
						mgongflf = rs2.getDouble("gongf");
						myanslf =rs2.getDouble("changf");
						mliuyk = rs2.getDouble("yingk");
						mliubz = rs2.getDouble("gongf");
						mliuzjbz = rs2.getDouble("zhejbz");
						mliuzjje = rs2.getDouble("zhejje");
						mjieslf = rs2.getDouble("jies");// 结算硫分
						
					}else if(rs2.getString("mingc").equals("干燥无灰基挥发分")){
						
						mhethff = rs2.getString("hetbz");// 合同挥发分
						mgongfhff = rs2.getDouble("gongf");
						myanshff = rs2.getDouble("changf");
						myingkhff = rs2.getDouble("yingk");
						mhuiffzjbz = rs2.getDouble("zhejbz");
						mhuiffzjje = rs2.getDouble("zhejje");
						mjieshff = rs2.getDouble("jies");// 结算挥发分
						
					}else if(rs2.getString("mingc").equals("干燥基灰分")){
						
						
						mhethf = rs2.getString("hetbz");
						mgongfhf = rs2.getDouble("gongf");
						myanshf = rs2.getDouble("changf");
						mjieshf = rs2.getDouble("jies");
						myingkhf = rs2.getDouble("yingk");
						mhuifzjbz = rs2.getDouble("zhejbz");
						mhuifzjje = rs2.getDouble("zhejje");
						
					}else if(rs2.getString("mingc").equals("全水分")){
						
						mhetsf = rs2.getString("hetbz");// 合同水分
						mgongfsf = rs2.getDouble("gongf");;
						myanssf = rs2.getDouble("changf");
						myingksf = rs2.getDouble("yingk");
						mshuifzjbz = rs2.getDouble("zhejbz");
						mshuifzjje = rs2.getDouble("zhejje");
						mjiessf = rs2.getDouble("jies");// 结算水分
					}
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
				
			}
//			煤款
//			 0, 煤款结算;
//			 1, 两票结算;
//			 2, 运费结算
			if(blnHasMeik&&mjieslx==1){
//				两票
				sql="select * from diancjsyfb where bianm='"+getJiesbhValue().getValue()+"'";
				rs=con.getResultSet(sql);
				if(rs.next()){
					myid=rs.getLong("id");
					mtielyf=rs.getDouble("guotyf");
					mqitzf = rs.getDouble("dityf");
					mbukyqyzf = rs.getDouble("bukyf");
					mjiskc=rs.getDouble("jiskc");
					mbuhsyf=rs.getDouble("buhsyf");
					myunfsl=rs.getDouble("shuil");
					myunfsk=rs.getDouble("shuik");
					myunzfhj=rs.getDouble("hansyf");
				}
			}else if(mjieslx!=0){
				
				sql=" select * from diancjsyfb where bianm='"+getJiesbhValue().getValue()+"'";
				
					rs=con.getResultSet(sql);
					if(rs.next()){
						myid=rs.getLong("id");
						mtianzdw =rs.getString("xianshr");
						
						mjiesbh = rs.getString("bianm");
						mdiancjsbs=mjiesbh.substring(0,mjiesbh.indexOf("-")+1);
						mjiesbh=mjiesbh.substring(mjiesbh.indexOf("-")+1);
						
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
						mfapbh = rs.getString("fapbh");
						mdaibcc = rs.getString("daibch");
						myansbh =rs.getString("yansbh");
						mduifdd = rs.getString("duifdd");
						mfukfs = rs.getString("fukfs");
						mjiessl = rs.getDouble("jiessl");
						mtielyf=rs.getDouble("guotyf");
						mqitzf = rs.getDouble("dityf");
						mbukyqyzf = rs.getDouble("bukyf");
						mjiskc=rs.getDouble("jiskc");
						mbuhsyf=rs.getDouble("buhsyf");
						myunfsl=rs.getDouble("shuil");
						myunfsk=rs.getDouble("shuik");
						myunzfhj=rs.getDouble("hansyf");
						mjingz = rs.getDouble("guohl");
					}
			}
			mmeikhjdx=getDXMoney(mjiasje);
			myunzfhjdx=getDXMoney(myunzfhj);
			mhej=mjiasje+myunzfhj;
			mdaxhj=getDXMoney(mhej);
			
			
			_editvalues.add(new Balancebean(mid, myid, mtianzdw, mjiesbh,
					mfahdw, mfaz, mshoukdw, mfahksrq, mfahjzrq, myansksrq,
					myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh, mhetsl,
					mgongfsl, mches, mxianshr, mfapbh, mdaibcc, myansbh,
					mduifdd, mfukfs, mshulzjbz, myanssl, myingksl, mshulzjje,
					mhetrl, mgongfrl, myansrl, mjiesrl, myingkrl, mrelzjbz,
					mrelzjje, mhethff, mgongfhff, myanshff, mjieshff,
					myingkhff, mhuiffzjbz, mhuiffzjje, mhethf, mgongfhf,
					myanshf, mjieshf, myingkhf, mhuifzjbz, mhuifzjje, mhetsf,
					mgongfsf, myanssf, mjiessf, myingksf, mshuifzjbz,
					mshuifzjje, mhetlf, mgongflf, myanslf, mjieslf, mliuyk,
					mliubz, mliuzjbz, mliuzjje, mjiessl, mbuhsdj, mjiakje,
					mbukyqjk, mjiakhj, mjiaksl, mjiaksk, mjiasje, mtielyf,
					mzaf, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
					myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
					mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq,
					mfahrq, mhuirdzjbz, mhuirdzjje, mhuirdbz, mgongfhrd,
					myanshrd, mqitzf, mchangcwjbr, mchangcwjbrq, mruzrq,
					mjieszxjbr, mjieszxjbrq, mgongsrlbjbr, mgongsrlbjbrq,
					mhetjg, mjieslx, mrelsx, mrelxx, mliusx, mliuxx, myuns,
					myunsfs,mdiancjsbs));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Balancebean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
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
			if(visit.getRenyjb()==1){
				
				setGongsValue(null);
				setIGongsModel(null);
				getIGongsModels();
				visit.setboolean2(true);
			}
			
			if(visit.getRenyjb()<3){
				
				visit.setboolean3(true);
			}	

			setDiancmcValue(null);
			setIDiancmcModel(null);
			getIDiancmcModels();	
			
			setDiancmcjcValue(null);//drop10
			setIDiancmcjcModel(null);
			getIDiancmcjcModels();
				
			setLiucmcValue(null);
			setILiucmcModel(null);
			getILiucmcModels();
			
			setHetbhValue(null);
			setIHetbhModel(null);
			getIHetbhModels();
			
			setFahdwValue(null);
			setIFahdwModel(null);
			getIFahdwModels();
			
			setFazValue(null);
			setIFazModel(null);
			getIFazModels();
			
			setRanlpzValue(null);
			setIRanlpzModel(null);
			getIRanlpzModels();
			
			
			getIJieslxModels();
			getIShoukdwModels();
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
			
			visit.setboolean1(false);//共用
			visit.setboolean5(false);//合同编号显示
			visit.setboolean6(false);//流程名称显示
			
			getSelectData();
			getJiesbsArrayScript();
		}

		if(visit.getboolean1()){
			visit.setboolean1(false);
			setJiesbhValue(null);
			setIJiesbhModel(null);
			getIJiesbhModels();
		}
		
	
		setJieslxValue(null);
		setIJieslxModel(null);
		setRanlpzValue(null);
		setIRanlpzModel(null);
		setShoukdwValue(null);
		setIShoukdwModel(null);

	}
	
	public String getJiesbsArrayScript() {
		JDBCcon con = new JDBCcon();
        StringBuffer JiesbsArrayScript = new StringBuffer();
        String sql = "";
        String tmp="";
        int i=0;
        try {
           sql=" select dc.quanc,dc.jiesbdcbs||'-' as jiesbdcbs from diancxxb dc ";
           ResultSet rstmp=con.getResultSet(sql);
           while(rstmp.next()){
               
               i++;
           }
           rstmp.close();
           
           for(int j=0;j<i;j++){
              
               if(j==0){
                   tmp="new Array()";                   
               }else{
                   tmp+=",new Array()";
               }
               
           }
           i=0;
           JiesbsArrayScript.append("var Jiesbs=new Array("+tmp+");");
           
           ResultSet rs=con.getResultSet(sql);
           while(rs.next()){
               
               String mmingc=rs.getString("quanc");
               String mjiesdbs=rs.getString("jiesbdcbs");
               
               JiesbsArrayScript.append("Jiesbs["+i+"][0] ='"+mmingc+"';");
               JiesbsArrayScript.append("Jiesbs["+i+"][1] ='"+mjiesdbs+"';");
               i++;
           }
           rs.close();           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
        return JiesbsArrayScript.toString();
	}
	
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
				.setProSelectionModel9(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}
    

//    结算类型
	private IDropDownBean _JieslxValue ;
    public IDropDownBean getJieslxValue() {
    	if(_JieslxValue==null){
    		_JieslxValue=(IDropDownBean)getIJieslxModel().getOption(0);
    	}
       return _JieslxValue;
    }
    public void setJieslxValue(IDropDownBean Value)
    {
    	_JieslxValue = Value;
    }
    private static IPropertySelectionModel _IJieslxModel;

    public void setIJieslxModel(IPropertySelectionModel value){
        _IJieslxModel = value;
	}
    public IPropertySelectionModel getIJieslxModel(){
            if(_IJieslxModel==null){
                getIJieslxModels();
            }
            return _IJieslxModel;
    }
    public IPropertySelectionModel getIJieslxModels(){
        List arryJieslx=new ArrayList();
        arryJieslx.add(new IDropDownBean(0,"煤款结算"));
        arryJieslx.add(new IDropDownBean(1,"两票结算"));
        arryJieslx.add(new IDropDownBean(2,"运费结算"));
            _IJieslxModel = new IDropDownModel(arryJieslx);
        return _IJieslxModel; 
    }
    
//	end
    
//  填制单位
	private IDropDownBean _TianzdwValue ;
	private boolean _Tianzdwchange=false;
    public IDropDownBean getTianzdwValue() {
    	if(_TianzdwValue==null){
    		_TianzdwValue=(IDropDownBean)getITianzdwModel().getOption(0);
    	}
       return _TianzdwValue;
    }
    public void setTianzdwValue(IDropDownBean Value)
    {
    	if(_TianzdwValue!=Value){
    		
    		_Tianzdwchange=true;
    	}
    	_TianzdwValue=Value;
    }
    private static IPropertySelectionModel _ITianzdwModel;

    public void setITianzdwModel(IPropertySelectionModel value){
    	_ITianzdwModel = value;
	}
    public IPropertySelectionModel getITianzdwModel(){
            if(_ITianzdwModel==null){
                getITianzdwModels();
            }
            return _ITianzdwModel;
    }
    public IPropertySelectionModel getITianzdwModels(){
        String sql="select id,quanc from diancxxb order by quanc";
        _ITianzdwModel = new IDropDownModel(sql);
        return _ITianzdwModel;
    }
    
//	end
    
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
    
//  所属公司 DropDownBean1
//  所属公司 ProSelectionModel1   
    public IDropDownBean getGongsValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
    		((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIGongsModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean1();
    }
    public void setGongsValue(IDropDownBean value)
    {
    	if(((Visit)getPage().getVisit()).getDropDownBean1()!=value){
    		
    		((Visit)getPage().getVisit()).setboolean1(true);
    		getIDiancmcModels();
    		((Visit)getPage().getVisit()).setDropDownBean1(value);
    	}
    }
    
    public void setIGongsModel(IPropertySelectionModel value){
    	((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}
    public IPropertySelectionModel getIGongsModel(){
            if(((Visit)getPage().getVisit()).getProSelectionModel1()==null){
                getIGongsModels();
            }
            return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public IPropertySelectionModel getIGongsModels(){
        String sql="select id,mingc from diancxxb where jib=2 order by mingc";
        ((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"全部"));
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    
//	end
    
//  发货单位 DropDownBean2
//  发货单位 ProSelectionMode21
    
    public IDropDownBean getFahdwValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
    		((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getIFahdwModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean2();
    }
    public void setFahdwValue(IDropDownBean Value)
    {
    	((Visit)getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setIFahdwModel(IPropertySelectionModel value){

    	((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}
    public IPropertySelectionModel getIFahdwModel(){
            if(((Visit)getPage().getVisit()).getProSelectionModel2()==null){
                getIFahdwModels();
            }
            return ((Visit)getPage().getVisit()).getProSelectionModel2();
    }
    public IPropertySelectionModel getIFahdwModels(){
    	
		JDBCcon con=new JDBCcon();
		List List=new ArrayList();
    	String sql="";
    	try{
    		
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
        		
        		sql="select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
        	}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){	
    			
    			List.add(new IDropDownBean(i++, rs.getString("gongysmc")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(List));
        return ((Visit)getPage().getVisit()).getProSelectionModel2();
    }
    
//	end

//  发站 DropDownBean3
//  发站 ProSelectionModel3
    public IDropDownBean getFazValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
    		((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getIFazModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean3();
    }
    public void setFazValue(IDropDownBean Value)
    {
    	((Visit)getPage().getVisit()).getDropDownBean3();
    }

    public void setIFazModel(IPropertySelectionModel value){
    	
    	((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}
    public IPropertySelectionModel getIFazModel(){
            if(((Visit)getPage().getVisit()).getProSelectionModel3()==null){
                getIFazModels();
            }
            return ((Visit)getPage().getVisit()).getProSelectionModel3();
    }
    public IPropertySelectionModel getIFazModels(){
    	
    	String sql="";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
        		
        		sql=" select faz from "
    				+ " (select distinct faz from diancjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct faz from diancjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by faz";
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
        		
        		sql=" select faz from "
    				+ " (select distinct faz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct faz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by faz";
        		
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
        		
        		sql="select faz from "
    				+ " (select distinct faz from diancjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct faz from diancjsyfb where liucztb_id=0) order by faz";
        	}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){
    			
    			List.add(new IDropDownBean(i++, rs.getString("faz")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(List));
        return ((Visit)getPage().getVisit()).getProSelectionModel3();
    }
    
//	end
    
//  品种 DropDownBean4
//  品种 ProSelectionModel4
    
    public IDropDownBean getRanlpzValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean4()==null){
    		((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean)getIRanlpzModel().getOption(0));
    	}
       return ((Visit)getPage().getVisit()).getDropDownBean4();
    }
    public void setRanlpzValue(IDropDownBean value)
    {
    	((Visit)getPage().getVisit()).setDropDownBean4(value);
    }

    public void setIRanlpzModel(IPropertySelectionModel value){
    	
    	((Visit)getPage().getVisit()).setProSelectionModel4(value);
	}
    
    public IPropertySelectionModel getIRanlpzModel(){
            if(((Visit)getPage().getVisit()).getProSelectionModel4()==null){
                getIRanlpzModels();
            }
            return ((Visit)getPage().getVisit()).getProSelectionModel4();
    }
    public IPropertySelectionModel getIRanlpzModels(){
        
    	String sql="";
    	List List=new ArrayList();
        JDBCcon con=new JDBCcon();
        try{
        	int i=-1;
        	List.add(new IDropDownBean(i++, "请选择"));
        	if(((Visit)getPage().getVisit()).getRenyjb()==3){
        		
        		sql=" select meiz from "
    				+ " (select distinct meiz from diancjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct meiz from diancjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by meiz";
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
        		
        		sql=" select meiz from "
    				+ " (select distinct meiz from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct meiz from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by meiz";
        		
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
        		
        		sql="select meiz from "
    				+ " (select distinct meiz from diancjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct meiz from diancjsyfb where liucztb_id=0) order by meiz";
        	}
        	ResultSet rs=con.getResultSet(sql);
        	while(rs.next()){
        		
        		List.add(new IDropDownBean(i++, rs.getString("meiz")));
        	}
        	rs.close();
        }catch(Exception e){
        	
        	e.printStackTrace();
        }finally{
        	
        	con.Close();
        }
        
        ((Visit)getPage().getVisit()).setProSelectionModel4(new IDropDownModel(List));
        return  ((Visit)getPage().getVisit()).getProSelectionModel4();
    }
    
//	end
//  电厂 DropDownBean5
//  电厂 ProSelectionModel5
    
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean5()==null){
			
			((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) getIDiancmcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setDiancmcValue(IDropDownBean value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean5()!=value){
			
			((Visit)getPage().getVisit()).setboolean1(true);
			
			((Visit)getPage().getVisit()).setDropDownBean5(value);
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {
		
		((Visit)getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			getIDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIDiancmcModels() {
		
		String sql="";
		
		if(((Visit)getPage().getVisit()).getRenyjb()==2){
			
			sql=" select id,quanc from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by quanc";
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
			
			if(getGongsValue().getId()>-1){
				
				sql=" select id,quanc from diancxxb where fuid="+getGongsValue().getId()+" order by quanc";
				
			}else{
				
				sql=" select id,quanc from diancxxb where jib=3 order by quanc";
			}
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==3){
			
			sql=" select id,quanc from diancxxb where id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by quanc";
		}
		
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
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
    				+ " (select distinct diancjsmkb.bianm from diancjsmkb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct diancjsyfb.bianm from diancjsyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by bianm";
    			
    		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
    			
    			if(getDiancmcValue().getId()==-1){
    				
    				sql=" select bianm from "
        				+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
        				+ " union"
        				+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by bianm";
    			}else{
    				
    				sql=" select bianm from "
        				+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0)"
        				+ " union"
        				+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0) order by bianm";
    			}
    			
    			
    			
    		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
    			
    			if(getGongsValue().getId()==-1){
    				
    				if(getDiancmcValue().getId()==-1){
    					
    					sql="select bianm from "
    	    				+ " (select distinct diancjsmkb.bianm from diancjsmkb where liucztb_id=0)"
    	    				+ " union"
    	    				+ " (select distinct diancjsyfb.bianm from diancjsyfb where liucztb_id=0) order by bianm";
    				}else{
    					
    					sql=" select bianm from "
            				+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0)"
            				+ " union"
            				+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0) order by bianm";
    				}
    			}else{
    				
    				if(getDiancmcValue().getId()==-1){
    					
    					sql=" select bianm from "
            				+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+getGongsValue().getId()+" and liucztb_id=0)"
            				+ " union"
            				+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+getGongsValue().getId()+" and liucztb_id=0) order by bianm";
    				}else{
    					
    					sql=" select bianm from "
            				+ " (select distinct diancjsmkb.bianm from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0)"
            				+ " union"
            				+ " (select distinct diancjsyfb.bianm from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.id="+getDiancmcValue().getId()+" and liucztb_id=0) order by bianm";
    				}
    			}
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
    
//	合同编号 DropDownBean7
//  合同编号 ProSelectionModel7
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean) getIHetbhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setHetbhValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean7()!=value){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean7(value);
		}
	}

	public void setIHetbhModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			
			getIHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public IPropertySelectionModel getIHetbhModels() {
		
		String sql="";
		if(((Visit)getPage().getVisit()).getRenyjb()==3){
			
			sql=" select id,hetbh from hetb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and leib=0 order by hetbh";
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
			
			sql=" select id,hetbh from hetb where diancxxb_id in (select id from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+") and leib=0 order by hetbh";
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
			
			sql="select id,hetbh from hetb where leib=0 order by hetbh";
		}

		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//    合同 end
	
	
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
	
	
	//电厂名称简称10
	public IDropDownBean getDiancmcjcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean) getIDiancmcjcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}

	public void setDiancmcjcValue(IDropDownBean value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean10()!=value){
			
			((Visit)getPage().getVisit()).setboolean1(true);
			
			((Visit)getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setIDiancmcjcModel(IPropertySelectionModel value) {
		
		((Visit)getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIDiancmcjcModel() {
		if (((Visit)getPage().getVisit()).getDropDownBean10() == null) {
			getIDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIDiancmcjcModels() {
		
		String sql="";
		
		if(((Visit)getPage().getVisit()).getRenyjb()==2){
			
			sql=" select id,quanc from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by quanc";
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
			
			if(getGongsValue().getId()>-1){
				
				sql=" select id,quanc from diancxxb where fuid="+getGongsValue().getId()+" order by quanc";
				
			}else{
				
				sql=" select id,quanc from diancxxb where jib=3 order by quanc";
			}
			
		}else if(((Visit)getPage().getVisit()).getRenyjb()==3){
			
			sql=" select id,quanc from diancxxb where id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by quanc";
		}
		
		((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
//***************************************************************************//
    public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from diancxxb where id="+diancxxb_id;
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
    private String RT_HET = "jies";

	private String mstrReportName = "jies";

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
