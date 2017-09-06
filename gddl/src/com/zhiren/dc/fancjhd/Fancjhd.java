package com.zhiren.dc.fancjhd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-07-30
 * 内容:实现翻车机车号核对功能
 */
public class Fancjhd extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	//重车时间下拉框
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IDropDownModel value) {
		_pandModel = value; 
	}
	public IPropertySelectionModel getPandModel() {
		if (_pandModel == null) {
			
			String sql=" select rownum id,mingc from (select distinct to_char(zhongcsj,'HH24:mi:ss') mingc from chepb,fahb f ,yunsfsb y\n" +
			"where chepb.fahb_id=f.id and f.yunsfsb_id = y.id  and y.mingc = '铁路'  and " +
			"to_date(to_char(zhongcsj,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+this.getRiq()+"','yyyy-MM-dd')) order by mingc desc";
//			System.out.println(sql);
		    _pandModel = new IDropDownModel(sql);
		}
	    return _pandModel;
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		_pandValue = value;
	}
	public IDropDownBean getPandValue() {
		if(_pandValue==null && this.getPandModel()!=null && this.getPandModel().getOptionCount()>0){
			_pandValue=(IDropDownBean)this.getPandModel().getOption(0);
		}
		return _pandValue;
	}
	
	//翻车机下拉框
	private IPropertySelectionModel _fancjModel;
	public void setFancjModel(IDropDownModel value) {
		_fancjModel = value; 
	}
	public IPropertySelectionModel getFancjModel() {
		if (_fancjModel == null) {
			
			String sql=" select rownum id,mingc from (select distinct to_char(riq,'HH24:mi:ss') mingc from fancjghb \n" +
			"where to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+this.getRiq1()+"','yyyy-MM-dd')) order by mingc desc";
//			System.out.println(sql);
		    _fancjModel = new IDropDownModel(sql);
		}
	    return _fancjModel;
	}
	private IDropDownBean _fancjValue;
	private boolean _fancboo=false;
	public void setFancjValue(IDropDownBean value) {
		
		if(_fancjValue!=null && value!=null && !_fancjValue.getValue().equals(value.getValue())){
			_fancboo=true;
		}
		_fancjValue = value;
	}
	public IDropDownBean getFancjValue() {
		if(_fancjValue==null && this.getFancjModel()!=null && this.getFancjModel().getOptionCount()>0){
			_fancjValue=(IDropDownBean)this.getFancjModel().getOption(0);
		}
		
		return _fancjValue;
	}
	
	//翻车机未匹配车号
	
	private IPropertySelectionModel _fancphModel;
	public void setFancphModel(IDropDownModel value) {
		_fancphModel = value; 
	}
	public IPropertySelectionModel getFancphModel() {
		if (_fancphModel == null) {
			
			String ts="00:00:00";
			
			if(this.getPandValue()!=null && !this.getPandValue().getStrId().equals("-1")){
				ts=this.getPandValue().getValue();
			}
			String rq1=" to_date('"+this.getRiq()+" "+ts+"','yyyy-MM-dd HH24:mi:ss')";
			
			String zs="00:00:00";
			if(this.getFancjValue()!=null && !this.getFancjValue().getStrId().equals("-1")){
				zs=this.getFancjValue().getValue();
			}
			
			String rq2=" to_date('"+this.getRiq1()+" "+zs+"','yyyy-MM-dd HH24:mi:ss')";
			
			String sql=" select f.id id,f.cheph mingc from fancjghb f where f.riq="+rq2+ " and f.chepb_id is null and f.cheph not in (\n" +
					" select c.cheph from chepb c where c.zhongcsj="+rq1+
					")";
			
//			System.out.println(sql);
		    _fancphModel = new IDropDownModel(sql);
		}
	    return _fancphModel;
	}
	private IDropDownBean _fancphValue;
	private boolean _fancphboo=false;
	public void setFancphValue(IDropDownBean value) {
		if(_fancphValue!=null && value!=null && !_fancphValue.getValue().equals(value.getValue())){
			_fancphboo=true;
		}
		_fancphValue = value;
	}
	public IDropDownBean getFancphValue() {
		if(_fancphValue==null && this.getFancphModel()!=null && this.getFancphModel().getOptionCount()>0){
			_fancphValue=(IDropDownBean)this.getFancphModel().getOption(0);
		}
		return _fancphValue;
	}
	
	
	
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _BulChick=false;  
	public void BulButton(IRequestCycle cycle){
		_BulChick=true;
	}
	
	private boolean _PipChick=false;
	public void PipButton(IRequestCycle cycle){
		_PipChick=true;
	}
   public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			
		} else if(_BulChick){
			_BulChick=false;
			Bul();
		}else if(_PipChick){
			_PipChick=false;
			Pip();
		}
		
		
		if(this.riqBoo){
			this.riqBoo=false;
			setPandModel(null);
			setPandValue(null);
			
			this.setFancphModel(null);
			this.setFancphValue(null);
		}
		
		if(riq1Boo){
			this.riq1Boo=false;
			
			this.setFancjModel(null);
			this.setFancjValue(null);
			
			this.setFancphModel(null);
			this.setFancphValue(null);
		}
		
		if(this._fancboo || this._fancphboo){
			
			this._fancboo = false;
			this._fancphboo = false;
			
			this.setFancphModel(null);
			this.setFancphValue(null);
			
		}
		if(this.getPandValue()!=null)
//		System.out.println("-----"+this.getPandValue().getValue());
		getSelectData();
		
	}
   
    public void Pip(){
    	Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		String tm="00:00:00";

		if(this.getFancjValue()!=null && !this.getFancjValue().getStrId().equals("-1")){
			tm=this.getFancjValue().getValue();
		}
		String rq2=" to_date('"+this.getRiq1()+" "+tm+"','yyyy-MM-dd HH24:mi:ss')";
		
		
		String sql="";
		int count=0;
		if(this.getChange()!=null && !this.getChange().equals("")){
			
			String[] res=this.getChange().split(";");
			
			for(int i=0;i<res.length;i++){
				String[] rec=res[i].split(",");
				
				String id=rec[0];
				String cheph=rec[1];
				
				String tem=" select * from fancjghb where riq="+rq2+" and cheph='"+cheph+"'";
				
				rsl=con.getResultSetList(tem);
				
				String maoz="0";
				String piz="0";
				String fancj_id="0";
				if(rsl.next()){
					maoz=rsl.getString("maoz");
					piz=rsl.getString("piz");
					fancj_id=rsl.getString("id");
				}
				
				rsl=con.getResultSetList(" select * from chepb where id="+id);
				
				String qian_maoz="";
				String qian_piz="";
				String fahb_id="0";
				if(rsl.next()){
					qian_maoz=rsl.getString("maoz");
					qian_piz=rsl.getString("piz");
					fahb_id=rsl.getString("fahb_id");
				}
				
//				sql+=" update chepb set maoz="+maoz+",piz="+piz+" where id="+id+";\n";
//				sql+=" update fahb set maoz=maoz+"+(Float.parseFloat(maoz)-Float.parseFloat(qian_maoz))+"," +
//						"piz=piz+"+(Float.parseFloat(piz)-Float.parseFloat(qian_piz))+" where id="+fahb_id+";\n";
				sql+=" update fancjghb set chepb_id="+id+" where id="+fancj_id+";\n";
				count++;
				
			}
		}
		
		
		if(!sql.equals("")){
			
			
			
			
			if(count<=1){
				sql=sql.substring(0, sql.lastIndexOf(";"));
			}else{
				sql=" begin\n"+sql+" end;";
			}
			
			int flag=con.getUpdate(sql);
			
			if(flag>=0){
				this.setMsg("数据匹配成功!");
			}else{
				this.setMsg("数据匹配失败!");
			}
		}
		
		rsl.close();
		con.Close();
    }
	public void Bul() {
//		System.out.println(this.getChecklc());
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		ResultSetList gdh=null;
		String sql="";
		int count=0;
		
		String ts="00:00:00";
		
		if(this.getPandValue()!=null && !this.getPandValue().getStrId().equals("-1")){
			ts=this.getPandValue().getValue();
		}
		String rq1=" to_date('"+this.getRiq()+" "+ts+"','yyyy-MM-dd HH24:mi:ss')";
		
		
		if(this.getChecklc().equals("true")){//补录所有
			
			if(this.getFancphModel()!=null){
				
				for(int i=0;i<this.getFancphModel().getOptionCount();i++){
					
					IDropDownBean cp=(IDropDownBean)this.getFancphModel().getOption(i);
					
					if(cp!=null){
						

						
						String id=cp.getStrId();
						
						String temp=" select * from fancjghb where id="+id;
						rsl=con.getResultSetList(temp);
						while(rsl.next()){
							
						count++;
						
						
						String gdh_str="  select * from (select c.* from chepb c where c.zhongcsj="+rq1+" order by c.zhongcsj desc ) where rownum=1";
						
						gdh=con.getResultSetList(gdh_str);
						
						
						String zhongcsj="";
						if(this.getPandValue()!=null){
							zhongcsj="to_date('"+this.getRiq()+" "+this.getPandValue().getValue()+"','yyyy-MM-dd HH24:mi:ss')";
						}else{
							zhongcsj="to_date('"+this.getRiq()+" "+DateUtil.Formatdate("HH24:mi:ss", new Date())+"','yyyy-MM-dd HH24:mi:ss')";
						}
						
						
						
						String xuh="1";
						String piaojh="";
						String yuanmz="0";
						String yuanpz="0";
						String biaoz="0";
						String yingd="0";
						String yingk="0";
						String yuns="0";
						String koud="0";
						String kous="0";
						String kouz="0";
						String koum="0";
						String zongkd="0";
						String sanfsl="0";
						String ches="0";
						String jianjfs="过衡";
						String guohb_id="0";
						String fahb_id="0";
						String chebb_id="0";
						String yunsdwb_id="-1";
						String meicb_id="0";
						String xiecb_id="0";
						String hedbz="0";
						if(gdh.next()){
							yuanmz=gdh.getString("yuanmz");
							yuanpz=gdh.getString("yuanpz");
							biaoz=gdh.getString("biaoz");
							yingd=gdh.getString("yingd");
							yingk=gdh.getString("yingk");
							yuns=gdh.getString("yuns");
							koud=gdh.getString("koud");
							kous=gdh.getString("kous");
							kouz=gdh.getString("kouz");
							koum=gdh.getString("koum");
							zongkd=gdh.getString("zongkd");
							sanfsl=gdh.getString("sanfsl");
							guohb_id=gdh.getString("guohb_id");
							fahb_id=gdh.getString("fahb_id");
							chebb_id=gdh.getString("chebb_id");
							yunsdwb_id=gdh.getString("yunsdwb_id");
							meicb_id=gdh.getString("meicb_id");
							xiecb_id=gdh.getString("xiecb_id");
							hedbz=gdh.getString("hedbz");
		
						}
						
						if(yuanmz==null || yuanmz.equals("")){
							yuanmz="''";
						}
						if(yuanpz==null || yuanpz.equals("")){
							yuanpz="''";
						}
						
						sql+=" insert into chepb(id,xuh,cheph,piaojh,yuanmz,yuanpz,maoz,piz,biaoz,yingd,yingk," +
						"yuns,koud,kous,kouz,koum,zongkd,sanfsl,ches,jianjfs,guohb_id,fahb_id," +
						"chebb_id,yunsdwb_id,meicb_id,xiecb_id,hedbz,lursj,bulsj,zhongcsj,lury) values(" +
						"getnewid(" +visit.getDiancxxb_id()+"),"+xuh+",'"+rsl.getString("cheph")+"','"+piaojh+"',"+yuanmz+","+yuanpz+","+rsl.getString("maoz")+
						","+rsl.getString("piz")+","+""+biaoz+","+yingd+","+yingk+"," +
								yuns+","+koud+","+kous+","+kouz+","+koum+","+zongkd+","+sanfsl+","+ches+",'"+jianjfs+"',"+guohb_id+","+fahb_id+","+chebb_id+"," +
										yunsdwb_id+","+meicb_id+","+xiecb_id+","+hedbz+",sysdate,1,"+zhongcsj+",'"+visit.getRenymc()+"');";
						
						sql+=" update fahb set maoz=maoz+"+rsl.getString("maoz")+",piz=piz+"+rsl.getString("piz")+",ches=ches+1  where id="+fahb_id+";";
						
						
						
						}
					
						
					}
				}
			}
		}else{//补录所选车皮
			
			if(this.getFancphValue()!=null){
				
				String id=this.getFancphValue().getStrId();
				
				String temp=" select * from fancjghb where id="+id;
				rsl=con.getResultSetList(temp);
				while(rsl.next()){
					count++;
				
					String gdh_str="  select * from (select c.* from chepb c where c.zhongcsj="+rq1+" order by c.zhongcsj desc ) where rownum=1";
					
					gdh=con.getResultSetList(gdh_str);
					
					String zhongcsj="";
					if(this.getPandValue()!=null){
						zhongcsj="to_date('"+this.getRiq()+" "+this.getPandValue().getValue()+"','yyyy-MM-dd HH24:mi:ss')";
					}else{
						zhongcsj="to_date('"+this.getRiq()+" "+DateUtil.Formatdate("HH:mm:ss", new Date())+"','yyyy-MM-dd HH24:mi:ss')";
					}
					
					String xuh="1";
					String piaojh="";
					String yuanmz="0";
					String yuanpz="0";
					String biaoz="0";
					String yingd="0";
					String yingk="0";
					String yuns="0";
					String koud="0";
					String kous="0";
					String kouz="0";
					String koum="0";
					String zongkd="0";
					String sanfsl="0";
					String ches="0";
					String jianjfs="过衡";
					String guohb_id="0";
					String fahb_id="0";
					String chebb_id="0";
					String yunsdwb_id="-1";
					String meicb_id="0";
					String xiecb_id="0";
					String hedbz="0";
					if(gdh.next()){
						yuanmz=gdh.getString("yuanmz");
						yuanpz=gdh.getString("yuanpz");
						biaoz=gdh.getString("biaoz");
						yingd=gdh.getString("yingd");
						yingk=gdh.getString("yingk");
						yuns=gdh.getString("yuns");
						koud=gdh.getString("koud");
						kous=gdh.getString("kous");
						kouz=gdh.getString("kouz");
						koum=gdh.getString("koum");
						zongkd=gdh.getString("zongkd");
						sanfsl=gdh.getString("sanfsl");
						guohb_id=gdh.getString("guohb_id");
						fahb_id=gdh.getString("fahb_id");
						chebb_id=gdh.getString("chebb_id");
						yunsdwb_id=gdh.getString("yunsdwb_id");
						meicb_id=gdh.getString("meicb_id");
						xiecb_id=gdh.getString("xiecb_id");
						hedbz=gdh.getString("hedbz");
	
					}
					
					if(yuanmz==null || yuanmz.equals("")){
						yuanmz="''";
					}
					if(yuanpz==null || yuanpz.equals("")){
						yuanpz="''";
					}
				sql+=" insert into chepb(id,xuh,cheph,piaojh,yuanmz,yuanpz,maoz,piz,biaoz,yingd,yingk," +
						"yuns,koud,kous,kouz,koum,zongkd,sanfsl,ches,jianjfs,guohb_id,fahb_id," +
						"chebb_id,yunsdwb_id,meicb_id,xiecb_id,hedbz,lursj,bulsj,zhongcsj,lury) values(" +
						"getnewid(" +visit.getDiancxxb_id()+"),"+xuh+",'"+rsl.getString("cheph")+"','"+piaojh+"',"+yuanmz+","+yuanpz+","+rsl.getString("maoz")+
						","+rsl.getString("piz")+","+""+biaoz+","+yingd+","+yingk+"," +
								yuns+","+koud+","+kous+","+kouz+","+koum+","+zongkd+","+sanfsl+","+ches+",'"+jianjfs+"',"+guohb_id+","+fahb_id+","+chebb_id+"," +
										yunsdwb_id+","+meicb_id+","+xiecb_id+","+hedbz+",sysdate,1,"+zhongcsj+",'"+visit.getRenymc()+"');";
				
				sql+=" update fahb set maoz=maoz+"+rsl.getString("maoz")+",piz=piz+"+rsl.getString("piz")+",ches=ches+1 where id="+fahb_id+";";
				}
			}
			
		}
		
	
		sql=" begin \n"+sql+" end;";
		
		
		int flag=con.getUpdate(sql);
		
		if(flag>=0){
			this.setMsg("数据补录成功!");
			
//			this.setPandModel(null);
//			this.setPandValue(null);
			
			this.setFancphModel(null);
			this.setFancphValue(null);
			
		}else{
			this.setMsg("数据补录失败!");
		}
		
		gdh.close();
		rsl.close();
		con.Close();
	}
	public void save() {
		

		if(this.getChange()!=null && this.getChange().equals("")){
			return ;
		}
		
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList rsl;
		ResultSetList rd=null;
		rsl=visit.getExtGrid1().getModifyResultSet(getChange());
		
		String sql="";
		int count=0;
		while(rsl.next()){
			
			String id=rsl.getString("id");
			
			if(!id.equals("0")){
				count++;
				sql+=" update chepb set cheph='"+rsl.getString("cheph")+"' where id="+id+";\n";
				
				
	
			}
		}
		
		
		
		rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		
		while(rsl.next()){
			
			String id=rsl.getString("id");
			count+=2;
			
			rd=con.getResultSetList(" select * from chepb where id="+id);
			
			String maoz="0";
			String piz="0";
			String fahb_id="0";
			if(rd.next()){
				maoz=rd.getString("maoz");
				piz=rd.getString("piz");
				fahb_id=rd.getString("fahb_id");
			}
			
			sql+=" delete from chepb where id="+id+";\n";
			sql+=" update fahb set maoz=maoz-"+maoz+", piz=piz-"+piz+" where id="+fahb_id+";\n";
			
		}
		
		
		if(count>1){
			sql=" begin \n"+sql+" end;";
		}else{
			sql=sql.substring(0, sql.lastIndexOf(";"));
		}
//		System.out.println(sql);
		int flag=con.getUpdate(sql);
		
		if(flag>=0){
			this.setMsg("数据更新成功!");
		}else{
			this.setMsg("数据更新失败!");
		}
		
		rsl.close();
		con.Close();
		
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		this.setFancphModel(null);
		this.setFancphValue(null);
		String ts="00:00:00";
		
		if(this.getPandValue()!=null && !this.getPandValue().getStrId().equals("-1")){
			ts=this.getPandValue().getValue();
		}
		String rq1=" to_date('"+this.getRiq()+" "+ts+"','yyyy-MM-dd HH24:mi:ss')";
		
		String tm="00:00:00";

		if(this.getFancjValue()!=null && !this.getFancjValue().getStrId().equals("-1")){
			tm=this.getFancjValue().getValue();
		}
		String rq2=" to_date('"+this.getRiq1()+" "+tm+"','yyyy-MM-dd HH24:mi:ss')";

		String sql=" select c.id,c.cheph,c.maoz,c.piz,c.maoz-c.piz jingz,c.biaoz,c.zongkd,to_char(c.zhongcsj,'yyyy-MM-dd HH24:mi:ss') zhongcsj," +
				"to_char(c.qingcsj,'yyyy-MM-dd HH24:mi:ss') qingcsj,\n" +
				"(select mingc from yunsdwb where id=c.yunsdwb_id) ysdw," +
				"decode((select h.id from fancjghb h where h.riq="+rq2+" and rownum=1 and h.cheph=c.cheph),null,0,1) pip \n" +
				"  from chepb c,fahb f,yunsfsb y \n" +
				"where " +
				"c.zhongcsj="+rq1+"  and \n" +
			" c.fahb_id=f.id and f.yunsfsb_id=y.id and  y.mingc='铁路' \n" +
			" and c.id not in ( select  nvl(chepb_id,-111) from fancjghb  where riq="+rq2+"  )" +
			"order by c.zhongcsj, c.cheph";
		
//		System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.addPaging(-1);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("zongkd").setHeader("总扣杂");
		egu.getColumn("zhongcsj").setHeader("重车时间");
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("ysdw").setHeader("运输单位");
		egu.getColByHeader("pip").setHidden(true);
		egu.getColumn("zhongcsj").setHidden(true);
		
		
//		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("zongkd").setEditor(null);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("ysdw").setEditor(null);
		
		
		egu.addTbarText("重车:");
		DateField dStart = new DateField();
		dStart.setWidth(60);
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		
		
		egu.addTbarText("");
		
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(70);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("翻车:");
		DateField dEnd = new DateField();
		dEnd.setWidth(60);
		dEnd.Binding("RIQ1","");
		dEnd.setValue(getRiq1());
		egu.addToolbarItem(dEnd.getScript());
		
		egu.addTbarText("");
		
		ComboBox fancsj = new ComboBox();
		fancsj.setWidth(70);
		fancsj.setTransform("FancjDropDown");
		fancsj.setId("FancjDropDown");
		fancsj.setLazyRender(true);
		egu.addToolbarItem(fancsj.getScript());
		
		
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("未配车皮");// 设置
		
		
		ComboBox chep = new ComboBox();
		chep.setWidth(70);
		chep.setTransform("FancphDropDown");
		chep.setId("FancphDropDown");
		chep.setLazyRender(true);
		egu.addToolbarItem(chep.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addTbarText("-");// 设置分隔符
		
		GridButton pp=new GridButton("匹配","function(){" +
				"var res=gridDiv_ds.getRange();\n" +
				"if(res==null && res.length==0 ){Ext.Msg.alert('提示信息','没有要匹配的数据!');return;}\n" +
				
				" var _value='';\n"+
				
				"for(var i=0;i<res.length;i++){\n" +
				
				"if(res[i].get('PIP')=='1') {\n" +
				" _value+=res[i].get('ID')+','+res[i].get('CHEPH')+';';\n"+
				"}\n" +
				
				"}\n" +
				"" +
				"if(_value==''){Ext.Msg.alert('提示信息','没有要匹配的数据!');return;}\n" +
				"document.all.CHANGE.value=_value;\n" +
				"document.all.PipButton.click();" +
				"}");
		pp.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(pp);
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, "SaveButton");
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
		
		egu.addTbarText("-");// 设置分隔符
		
		GridButton bl=new GridButton("补录","function(){if(PandDropDown.getValue()==null || PandDropDown.getValue()==''){" +
				"Ext.Msg.alert('提示信息','该时间轨道衡没有数据,无法匹配!');return;\n" +
				"}\n" +
				
				"if(FancphDropDown.getValue()==null || FancphDropDown.getValue()=='')" +
				
				"{" +
				"Ext.Msg.alert('提示信息','该时间翻车机没有未匹配的车皮号!');return;\n" +
				"}" +
				
				"var _value=Iscb.getValue();if(_value==true){\n" +
				"Ext.Msg.confirm('提示相信','确认补录所有吗?',function(btn){\n" +
				"if(btn=='yes'){document.all.ISCHECKED.value=_value;document.all.BulButton.click();}" +
				"});\n" +
				"}else{\n" +
				"document.all.ISCHECKED.value=_value;document.all.BulButton.click();" +
				"" +
				"}" +
				
				"}");
		bl.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(bl);
		
		
		
		egu.addTbarText("-");// 设置分隔符
		
		Checkbox cb=new Checkbox();
		cb.setId("Iscb");
		cb.setBoxLabel("全部");
		
		if(this.getChecklc().equals("true")){
			cb.setChecked(true);
		}
		cb.setListeners("check:function(own,checked){if(checked){document.all.ISCHECKED.value='true'}else{document.all.ISCHECKED.value='false'}}");
		egu.addToolbarItem(cb.getScript());
		
		
//		egu.addOtherScript("gridDiv_grid.getView()=new Ext.grid.GridView({\n" +
//				"forceFit:true ,\n" +
//				"enableRowBody:true,\n" +
//				"getRowClass:function(record,rowIndex,p,ds){" +
//				"var cls='white-row';"+
//				"switch(record.data.PIP){" +
//				"case '0':cls='red-row'" +
//				"}" +
//				"return cls;" +
//				"}\n" +
//				"});");
		
		egu.addOtherScript("if(gridDiv_ds!=null){\n" +
				" var res=gridDiv_ds.getRange();\n" +
				
				" var i=0;"+
				"while(res!=null && res.length>0 && i<res.length ){" +
				
				"if(res[i].get('PIP')=='0'){\n" +
				" var colorStr='gridDiv_grid.getView().getRow('+i+').style.backgroundColor=\"red\";'; eval(colorStr);\n" +
				"}\n" +
				"i++;" +
				
				"}" +
				"" +
				"" +
				"}");
		
		setExtGrid(egu);
		con.Close();
	}
	
	
	private String checklc="";
	public void setChecklc(String checklc){
		this.checklc=checklc;
	}
	public String getChecklc(){
		
		if(this.checklc==null ||  this.checklc.equals("")){
			return "false";
		}
		return this.checklc;
	}
	
	
	//-----------------------
	private String riq;
	private boolean riqBoo=false;
	public void setRiq(String value) {
		
		if(riq!=null && !riq.equals(value)){
			riqBoo=true;
		}
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	
	//--------------------
	
	private String riq1;
	private boolean riq1Boo=false;
	public void setRiq1(String value) {
		
		if(riq1!=null && !riq1.equals(value)){
			riq1Boo=true;
		}
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	
	
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			this.setRiq(null);
			this.setRiq1(null);
			
			setPandModel(null);
			setPandValue(null);
			
			this.setFancjModel(null);
			this.setFancjValue(null);
			
			this.setFancphModel(null);
			this.setFancphValue(null);
			
			this.getSelectData();
		}
		
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
}
