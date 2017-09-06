package com.zhiren.jt.meiysgl.rezcb.rezcb;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.jt.diaoygl.shouhcrb.shouhcrb.Shouhcrbbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;

public class Rezcb extends BasePage implements PageValidateListener{

	// 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}

    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
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

	// 开始日期
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (!DateUtil.Formatdate("yyyy-MM-dd", _BeginriqValue).equals(
				DateUtil.Formatdate("yyyy-MM-dd", _value))) {
			_BeginriqChange = true;
		}
		_BeginriqValue = _value;
	}

	// //////////////////////////////////////////////////////////////////////////
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());

			setEditValues(null);
			getEditValues();
			setChaocfx(null);
			setDiancmcValue(null);
			getIDiancmcModels();
			this.setDiancmcValue1(null);
			this.getIDiancmcModel1s();
			_BeginriqValue = new Date();
			
			visit.setList1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.getFengsModels();
			
			getSelectData();
		}
		if (_diancmcchange) {
			_diancmcchange = false;
			getSelectData();
		}
		if (_diancmcchange1) {
			_diancmcchange1 = false;
			getSelectData();
		}
		if (_BeginriqChange) {
			_BeginriqChange = false;
			getSelectData();
		}
		if (_fengschange) {

			this.setDiancmcValue1(null);
			getIDiancmcModel1s();
			getSelectData();
			_fengschange = false;

		}
		// getSelectData();
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Rezcbbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Delete() {
		
		List _value = getEditValues();
		if (_value != null && !_value.isEmpty()) {
			JDBCcon con = new JDBCcon();
			int introw = getEditTableRow();
			con.setAutoCommit(false);
			
			if (introw == -1) {
				this.setMsg("请先选中要删除行最前面的复选框!");
				con.rollBack();
				getSelectData();
				return;
			}
			for (introw = 0; introw < _value.size(); introw++) {
				if (((Rezcbbean) _value.get(introw)).getFlag()) {
					int flag = con
							.getDelete(" Delete  From rezcb Where  id="
									+ ((Rezcbbean) _value.get(introw))
											.getId());
					if (flag == -1) {
						setMsg("删除数据失败！");
						con.rollBack();
						getSelectData();
						return;
					}
					_value.remove(introw--);
				}
			}
			con.commit();
		}
		getSelectData();
		
	}

	private void Save() {
		Visit visit = (Visit) getPage().getVisit();
		List _list = visit.getList1();
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		try {
			String sql = "";
			long id = 0;
			String riq = "";
			long dianchangid = 0;
			long rucsl = 0;
			double rucrl = 0;
			double rucsf = 0;
			long rulsl = 0;
			double rulrl = 0;
			double rulsf = 0;
			double rezctzq = 0;
			double rezctzh = 0;
			String beiz = "";
			String chaocfx = getChaocfx();
			String chaocfxry = "";
			if (chaocfx != null && !chaocfx.equals("")) {
				chaocfx = chaocfx.replaceAll("'", "''");
				chaocfxry = visit.getRenymc();
			}
			// 直接保存
			if (_list != null || _list.size() != 0) {
				for (int i = 0; i < _list.size(); i++) {
					id = ((Rezcbbean) _list.get(i)).getId();
					riq = ((Rezcbbean) _list.get(i)).getRiq();
					if (!riq.equals("合计")) {
						dianchangid = ((Rezcbbean) _list.get(i))
								.getDianchangid();
						rucsl = ((Rezcbbean) _list.get(i)).getRucsl();
						rucrl = ((Rezcbbean) _list.get(i)).getRucrl();
						rucsf = ((Rezcbbean) _list.get(i)).getRucsf();
						rulsl = ((Rezcbbean) _list.get(i)).getRulsl();
						rulrl = ((Rezcbbean) _list.get(i)).getRulrl();
						rulsf = ((Rezcbbean) _list.get(i)).getRulsf();
						rezctzq = ((Rezcbbean) _list.get(i)).getRezctzq();
						rezctzh = ((Rezcbbean) _list.get(i)).getRezctzh();
						beiz = ((Rezcbbean) _list.get(i)).getBeiz();

						String check = "select rz.id from rezcb rz where rz.RIQ=to_date('"
								+ riq
								+ "','yyyy-mm-dd') and diancxxb_id="
								+ dianchangid;
						rs = con.getResultSet(check);
						while (rs.next()) {
							id = rs.getLong("id");
						}
						if (id == 0) {

							sql = "insert into rezcb (ID,RIQ,DIANCXXB_ID,RUCSL,RUCRL,RUCSF,RULSL,RULRL,RULSF,REZCTZQ,REZCTZH,BEIZ,CHAOCFX,CHAOCFXRY) values("
									+ MainGlobal.getNewID(((Visit) getPage()
											.getVisit()).getDiancxxb_id())
									+ ",to_date('"
									+ riq
									+ "','yyyy-mm-dd'),"
									+ dianchangid
									+ ","
									+ rucsl
									+ ","
									+ rucrl
									+ ","
									+ rucsf
									+ ","
									+ rulsl
									+ ","
									+ rulrl
									+ ","
									+ rulsf
									+ ","
									+ rezctzq
									+ ","
									+ rezctzh
									+ ",'"
									+ beiz
									+ "','"
									+ chaocfx
									+ "','" + chaocfxry + "')";
							con.getInsert(sql);
						} else if (rucsl != 0 || rucrl != 0 || rucsf != 0
								|| rulsl != 0 || rulrl != 0 || rulsf != 0) {
							sql = "update rezcb set riq=to_date('" + riq
									+ "','yyyy-mm-dd'),diancxxb_id="
									+ dianchangid + ",rucsl=" + rucsl
									+ ",rucrl=" + rucrl + ",rucsf=" + rucsf
									+ ",rulsl=" + rulsl + ",rulrl=" + rulrl
									+ ",rulsf=" + rulsf + ",rezctzq=" + rezctzq
									+ ",rezctzh=" + rezctzh + ",beiz='" + beiz
									+ "',chaocfx='" + chaocfx + "',chaocfxry='"
									+ chaocfxry + "' where id=" + id;
							con.getUpdate(sql);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	// 超差分析
	public String getChaocfx() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setChaocfx(String _value) {
		((Visit) getPage().getVisit()).setString1(_value);
	}

	private String chaocfxry = "";// 超差分析人员

	public String getChaocfxry() {
		return chaocfxry;
	}

	public void setChaocfxry(String _chaocfxry) {
		chaocfxry = _chaocfxry;
	}

	private Rezcbbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Rezcbbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Rezcbbean EditValue) {
		_EditValue = EditValue;
	}

	private Date WeekFistDate(Date date) {// 得到当前日期所在周
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		return cal.getTime();
	}

	private Date WeekLastDate(Date date) {// 得到当前日期所在周
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	public List getSelectData() {
		
		JDBCcon con = new JDBCcon();
		int leib=((Visit) getPage().getVisit()).getRenyjb();
		
		long strdiancid=-1;
		
		String WeekDays[]= new String[7];
		String WeekFirstDay = DateUtil.Formatdate("yyyy-MM-dd", WeekFistDate(getBeginriqDate()));
		
		int year = Integer.parseInt(WeekFirstDay.substring(0,WeekFirstDay.indexOf("-")));
		int months = Integer.parseInt(WeekFirstDay.substring(5,WeekFirstDay.lastIndexOf("-")));
		int day = Integer.parseInt(WeekFirstDay.substring(WeekFirstDay.lastIndexOf("-")+1));
		
		String NextYear = "0";
		String NextMonths = "0";
		String NextDay="0";
		
		for(int i=0;i<=6;i++){
			
			NextYear = String.valueOf(year);
			NextMonths = String.valueOf(months);
			NextDay = String.valueOf(day+i);
			if(months==1 || months==3 || months==5 || months==7 || months==8 || months==10){
				
				if(day+i>31){
					NextMonths = String.valueOf(months+1);
					NextDay = String.valueOf(day+i-31);
				}
// WeekDays[i]=String.valueOf(day);
			}else if(months==4 || months==6 || months==9 || months==11){
				if(day+i>30){
					NextMonths = String.valueOf(months+1);
					NextDay = String.valueOf(day+i-30);
				}
			}else if(months==2){
				if(year%4!=0){
					if(day+i>28){
						NextMonths = String.valueOf(months+1);
						NextDay = String.valueOf(day+i-28);
					}
				}else{
					if(day+i>29){
						NextMonths = String.valueOf(months+1);
						NextDay = String.valueOf(day+i-29);
					}
				}
			}else if(months==12){
				if(day+i>31){
					NextYear = String.valueOf(year+1);
					NextMonths = "1";
					NextDay = String.valueOf(day+i-31);
				}
			}
			if(Integer.parseInt(NextMonths)<10){
				NextMonths = "0"+NextMonths;
			}
			if(Integer.parseInt(NextDay)<10){
				NextDay = "0"+NextDay;
			}
			WeekDays[i] = NextYear+"-"+NextMonths+"-"+NextDay;
			
		}
		String where=" where r.riq>=" +OraDate(WeekFistDate(getBeginriqDate()))+" and r.riq<="+OraDate(WeekLastDate(getBeginriqDate())) +"";
		
		
		if (leib==1){//集团用户
			strdiancid = this.getDiancmcValue1().getId();
		}else if(leib==2){//公司用户
			strdiancid = this.getDiancmcValue().getId();
		}else if (leib==3){//电厂用户
			strdiancid=	((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		
		if(strdiancid!=-1){
			
			String sql = "select r.id,decode(grouping(riq),1,'合计',to_char(r.riq, 'yyyy-mm-dd')) as riq,d.mingc,r.diancxxb_id,r.chaocfx,r.chaocfxry,"
						+"       sum(r.rucsl) as rucsl,"
						+"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) as rucrl,"
						+"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2)) as rucsf,"
						+"       sum(r.rulsl) as rulsl,"
						+"       decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) as rulrl,"
						+"       decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2)) as rulsf,"
		       
						+"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) "
						+"            -decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) as rezctzq,"
						+"       round((decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) "
						+"            -decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)))*1000/4.1816,0) as rezctzqdk,"     
		
						+"       ROUND(decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) "
						+"           - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) "
						+"           * (100 - decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2))) " 
						+"           / (100 - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2))), 2) as rezctzh, "
						+"       ROUND(ROUND(decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) "
						+"           - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) "
						+"           * (100 - decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2))) " 
						+"           / (100 - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2))), 2) " 
						+"           * 1000 / 4.1816, 0) as rezctzhdk,r.beiz "
						+"  from rezcb r, diancxxb d "+where 
						+"   and r.diancxxb_id = d.id and r.diancxxb_id = "+strdiancid
						+" group by rollup ((r.id,d.mingc,r.diancxxb_id,r.beiz,riq,r.chaocfx,r.chaocfxry)) order by riq ";
			//System.out.println(sql);
// String sql = "select r.id,to_char(r.riq,'yyyy-mm-dd') as
// riq,d.jianc,r.diancxxb_id,r.rucsl,"
// +" r.rucrl, r.rucsf, r.rulsl, r.rulrl,r.rulsf, (r.rucrl - r.rulrl) as
// rezctzq, "
// +" ROUND((r.rucrl - r.rulrl) * 1000 / 4.1816, 0) as rezctzqdk, "
// +" ROUND(r.rucrl - r.rulrl * (100 - r.rucsf) / (100 - r.rulsf), 2) as
// rezctzh, "
// +" ROUND(ROUND(r.rucrl - r.rulrl * (100 - r.rucsf) / (100 - r.rulsf), 2) *
// 1000 / 4.1816, 0) as rezctzhdk,r.beiz "
// +" from rezcb r,diancxxb d "+ where+" and r.diancxxb_id=d.id and
// r.diancxxb_id="+strdiancid
// +" order by riq";
			try {
				ResultSet rs=null;
				List _value = new ArrayList();
				
				rs = con.getResultSet(sql);
		
				long id=0;
				String riq = "";
				String mdiancmc="";
				long mdiancid=0;
				long rucsl=0;
				double rucrl=0;
				double rucsf=0;
				long rulsl=0;
				double rulrl=0;
				double rulsf=0;
				double rezctzq=0;
				long rezctzqdk=0;
				double rezctzh=0;
				long rezctzhdk=0;
				String beiz="";
				String chaocfx="";
				String chaocfxry = "";
				if(rs.next()){
					chaocfx=rs.getString("chaocfx");
					chaocfxry=rs.getString("chaocfxry");
				}
				rs.beforeFirst();
				setChaocfx(chaocfx);
				setChaocfxry(chaocfxry);
				for(int i=0;i<=6;i++){
					_value.add(new Rezcbbean(0,strdiancid,getProperValue(getIDiancmcModel(),strdiancid),WeekDays[i],0,0,0,0,0,0,0,0,0,0,""));
				}
				_value.add(new Rezcbbean(0,strdiancid,getProperValue(getIDiancmcModel(),strdiancid),"合计",0,0,0,0,0,0,0,0,0,0,""));
				while(rs.next()){
					id=rs.getLong("id");
					riq = rs.getString("riq");
					mdiancid=rs.getLong("diancxxb_id");
					mdiancmc=rs.getString("mingc");
					for(int j=0;j<=7;j++){
						
						if (riq.equals(((Rezcbbean) _value.get(j)).getRiq())){
						
							rucsl=rs.getLong("rucsl");
							rucrl=rs.getDouble("rucrl");
							rucsf=rs.getDouble("rucsf");
							rulsl=rs.getLong("rulsl");
							rulrl=rs.getDouble("rulrl");
							rulsf=rs.getDouble("rulsf");
							rezctzq=rs.getDouble("rezctzq");
							rezctzqdk=rs.getLong("rezctzqdk");
							rezctzh=rs.getDouble("rezctzh");
							rezctzhdk=rs.getLong("rezctzhdk");
							beiz=rs.getString("BEIZ");
							
							((Rezcbbean) _value.get(j)).setId(id);
							((Rezcbbean) _value.get(j)).setDianchangid(mdiancid);
							((Rezcbbean) _value.get(j)).setDianchangmc(mdiancmc);
							((Rezcbbean) _value.get(j)).setRucsl(rucsl);
							((Rezcbbean) _value.get(j)).setRucrl(rucrl);
							((Rezcbbean) _value.get(j)).setRucsf(rucsf);
							((Rezcbbean) _value.get(j)).setRulsl(rulsl);
							((Rezcbbean) _value.get(j)).setRulrl(rulrl);
							((Rezcbbean) _value.get(j)).setRulsf(rulsf);
							((Rezcbbean) _value.get(j)).setRezctzq(rezctzq);
							((Rezcbbean) _value.get(j)).setRezctzqdk(rezctzqdk);
							((Rezcbbean) _value.get(j)).setRezctzh(rezctzh);
							((Rezcbbean) _value.get(j)).setRezctzhdk(rezctzhdk);
							((Rezcbbean) _value.get(j)).setBeiz(beiz);
							
	// _value.add(new Rezcbbean(id,mdiancid,mdiancmc,riq,rucsl,
	// rucrl,rucsf,rulsl,rulrl,rulsf,rezctzq,rezctzqdk,rezctzh,rezctzhdk,beiz));
						}
					}
				}
				rs.close();
				setEditValues(_value);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
		}
		return getEditValues();
	
	}

	// ///////////////////////////////////////////////////////////////////////////
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			// getSelectData();
		}

	}

	// ///////////////////////////////////////////////////////////////

	public int getEditTableRow() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	// 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModels().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "";
		long diancId = ((Visit) getPage().getVisit()).getDiancxxb_id();
		sql = "select d.id,d.mingc from diancxxb d where d.fuid=" + diancId
				+ " order by d.mingc desc";
		// System.out.println(sql);

		_IDiancmcModel = new IDropDownModel(sql);
		return _IDiancmcModel;
	}

	// 集团用户用的和分公司下拉框关联的电厂下拉框
	public boolean _diancmcchange1 = false;

	private IDropDownBean _DiancmcValue1;

	public IDropDownBean getDiancmcValue1() {
		if (_DiancmcValue1 == null) {
			_DiancmcValue1 = (IDropDownBean) getIDiancmcModel1s().getOption(0);
		}
		return _DiancmcValue1;
	}

	public void setDiancmcValue1(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue1 != null) {
			id = _DiancmcValue1.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange1 = true;
			} else {
				_diancmcchange1 = false;
			}
		}
		_DiancmcValue1 = Value;
	}

	private IPropertySelectionModel _IDiancmcModel1;

	public void setIDiancmcModel1(IPropertySelectionModel value) {
		_IDiancmcModel1 = value;
	}

	public IPropertySelectionModel getIDiancmcModel1() {
		if (_IDiancmcModel1 == null) {
			getIDiancmcModel1s();
		}
		return _IDiancmcModel1;
	}

	public IPropertySelectionModel getIDiancmcModel1s() {

		String sql = "";
		long fenggsId = this.getFengsValue().getId();
		sql = "select d.id,d.mingc from diancxxb d where d.fuid=" + fenggsId
				+ " order by d.mingc desc";

		_IDiancmcModel1 = new IDropDownModel(sql);
		return _IDiancmcModel1;
	}

	// /结束
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++)
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == (long) value)
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();

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

	// 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql));
	}
}