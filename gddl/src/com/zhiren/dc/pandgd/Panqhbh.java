package com.zhiren.dc.pandgd;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * �޸��ˣ� lichenji
 * �޸�ʱ��: 2010��10��25��
 * �޸����ݣ�����Ӱ�ť�������ɣ��������ݻ��Զ��������ɡ�
 *
 */
public class Panqhbh extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 ҳ��仯��¼
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	//�̵���������
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}else if(v.isFencb()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " order by p.bianm desc";
			JDBCcon cn=new JDBCcon();
			ResultSetList rs=cn.getResultSetList(sql);
			if(rs.getRows()==0){
				v.setProSelectionModel10(new IDropDownModel(sql,"������̵����"));
			}else{
		    v.setProSelectionModel10(new IDropDownModel(sql));
			}
			rs.close();
			cn.Close();
		}
	    return v.getProSelectionModel10();
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pand_gd where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  order by bianm desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	public void setID(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getID() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}/*
	private boolean _AddChick = false;
	public void AddButton(IRequestCycle cycle) {
		_AddChick = true;
	}*/
	private boolean _CreateClick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
		} else if(_CreateClick) {
			_CreateClick = false;
			insertData();
		}else if(_DelClick){
			_DelClick=false;
			DelData();
		}
		getSelectData();
	}
	private void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from pandqhbhb where pand_gd_id="+getPandbID());
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	private ResultSetList getPandqhqk(long pandId){
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		String pdbegin="";   //��ǰ�����ʱ�Ŀ�ʼʱ��
		String pdend="";    //��ǰ�����ʱ�Ľ���ʱ��
		String kaisrq="";  //��Ŀ�ʼ�̵�ʱ��
		int diancxxb_id=0;
		String sql="select kaisrq,jiesrq,diancxxb_id from pand_gd where id="+pandId;
		String selSql="";
		ResultSetList list=con.getResultSetList(sql);
		if (list.next()) {
			diancxxb_id=list.getInt("diancxxb_id");
			kaisrq=DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss",list.getDate("kaisrq"));
			int year=DateUtil.getYear(list.getDate("kaisrq"));
			int month=DateUtil.getMonth(list.getDate("kaisrq"));
			int date=DateUtil.getDay(list.getDate("kaisrq"));
			if(year==list.getDate("jiesrq").getYear()&&month==list.getDate("jiesrq").getMonth()){
				pdbegin=year+"-"+month+"-"+date+" 00:00";
				pdend=DateUtil.Formatdate("yyyy-MM-dd 23:59",DateUtil.getLastDayOfMonth(list.getDate("kaisrq")));
				selSql=
					"select dianc.id diancxxb_id,\n" +
					"		round_new(fahdr.laiml,0)  shicll,\n" +
					"       round_new(haoydr.haoyl,0) shichl,\n" + 
					"       round_new(fahpd.laiml,0)  qianhll,\n" + 
					"       round_new(fahpd.mt,2)     rucmt,\n" + 
					"       round_new(haoypd.haoyl,0) qianhhl,\n" + 
					"       round_new(haoypd.mt,2)    rulmt,\n" +
					"round_new(haoypd.haoyl*(1-decode((100-fahpd.mt),0,0,(100-haoypd.mt)/(100-fahpd.mt))),2) shuifc\n" + 
					"  from (select sum(jingz) laiml, diancxxb_id\n" + 
					"          from fahb\n" + 
					"         where daohrq >= to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and daohrq <= to_date('"+kaisrq+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by diancxxb_id) fahdr,\n" + 
					"       (select sum(fadhy + gongrhy + qity + feiscy) haoyl, diancxxb_id\n" + 
					"          from meihyb\n" + 
					"         where rulrq >= to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and rulrq <= to_date('"+kaisrq+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by diancxxb_id) haoydr,\n" + 
					"       (select f.diancxxb_id,\n" + 
					"               sum(f.jingz) laiml,\n" + 
					"               decode(sum(f.jingz), 0, 0, sum(f.jingz * z.mt) / sum(f.jingz)) mt\n" + 
					"          from fahb f, zhilb z\n" + 
					"         where f.zhilb_id = z.id\n" + 
					"           and f.daohrq >=\n" + 
					"               to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and f.daohrq <=\n" + 
					"               to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by f.diancxxb_id) fahpd,\n" + 
					"       (select m.diancxxb_id,\n" + 
					"               sum(m.fadhy + m.gongrhy + m.qity + m.feiscy) haoyl,\n" + 
					"               decode(sum(r.mt),\n" + 
					"                      0,\n" + 
					"                      0,\n" + 
					"                      sum((m.fadhy + m.gongrhy + m.qity + m.feiscy) * r.mt) /\n" + 
					"                      sum(m.fadhy + m.gongrhy + m.qity + m.feiscy)) mt\n" + 
					"          from meihyb m, rulmzlb r\n" + 
					"         where m.rulmzlb_id = r.id\n" + 
					"           and m.rulrq >=\n" + 
					"               to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and m.rulrq <=\n" + 
					"               to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by m.diancxxb_id) haoypd,\n" + 
					"       vwdianc dianc\n" + 
					" where dianc.id = fahdr.diancxxb_id\n" + 
					"   and dianc.id = haoydr.diancxxb_id\n" + 
					"   and dianc.id = fahpd.diancxxb_id\n" + 
					"   and dianc.id = haoypd.diancxxb_id\n" + 
					"   and dianc.id = "+diancxxb_id+"";

			}else{
				pdend=year+"-"+month+"-"+date+" 00:00";
				pdbegin=DateUtil.Formatdate("yyyy-MM-dd 00:00",DateUtil.getFirstDayOfMonth(list.getDate("kaisrq")));
				selSql=
					"select dianc.id diancxxb_id,\n" +
					"		round_new(fahdr.laiml,0)  shicll,\n" +
					"       round_new(haoydr.haoyl,0) shichl,\n" + 
					"       round_new(fahpd.laiml,0)  qianhll,\n" + 
					"       round_new(fahpd.mt,2)     rucmt,\n" + 
					"       round_new(haoypd.haoyl,0) qianhhl,\n" + 
					"       round_new(haoypd.mt,2)    rulmt,\n" +
					"round_new(haoypd.haoyl*(1-decode((100-fahpd.mt),0,0,(100-haoypd.mt)/(100-fahpd.mt))),2) shuifc\n" + 
					"  from (select sum(jingz) laiml, diancxxb_id\n" + 
					"          from fahb\n" + 
					"         where daohrq >= to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and daohrq <= to_date('"+kaisrq+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by diancxxb_id) fahdr,\n" + 
					"       (select sum(fadhy + gongrhy + qity + feiscy) haoyl, diancxxb_id\n" + 
					"          from meihyb\n" + 
					"         where rulrq >= to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and rulrq <= to_date('"+kaisrq+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by diancxxb_id) haoydr,\n" + 
					"       (select f.diancxxb_id,\n" + 
					"               sum(f.jingz) laiml,\n" + 
					"               decode(sum(f.jingz), 0, 0, sum(f.jingz * z.mt) / sum(f.jingz)) mt\n" + 
					"          from fahb f, zhilb z\n" + 
					"         where f.zhilb_id = z.id\n" + 
					"           and f.daohrq >=\n" + 
					"               to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and f.daohrq <=\n" + 
					"               to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by f.diancxxb_id) fahpd,\n" + 
					"       (select m.diancxxb_id,\n" + 
					"               sum(m.fadhy + m.gongrhy + m.qity + m.feiscy) haoyl,\n" + 
					"               decode(sum(r.mt),\n" + 
					"                      0,\n" + 
					"                      0,\n" + 
					"                      sum((m.fadhy + m.gongrhy + m.qity + m.feiscy) * r.mt) /\n" + 
					"                      sum(m.fadhy + m.gongrhy + m.qity + m.feiscy)) mt\n" + 
					"          from meihyb m, rulmzlb r\n" + 
					"         where m.rulmzlb_id = r.id\n" + 
					"           and m.rulrq >=\n" + 
					"               to_date('"+pdbegin+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"           and m.rulrq <=\n" + 
					"               to_date('"+pdend+"', 'yyyy-MM-dd HH24:mi:ss')\n" + 
					"         group by m.diancxxb_id) haoypd,\n" + 
					"       vwdianc dianc\n" + 
					" where dianc.id = fahdr.diancxxb_id\n" + 
					"   and dianc.id = haoydr.diancxxb_id\n" + 
					"   and dianc.id = fahpd.diancxxb_id\n" + 
					"   and dianc.id = haoypd.diancxxb_id\n" + 
					"   and dianc.id = "+diancxxb_id+"";
			}
			rs=con.getResultSetList(selSql);
		}
		con.Close();
		return rs;
	}
	public void insertData() {
		DelData();
		JDBCcon con = new JDBCcon();
		ResultSetList rs=getPandqhqk(getPandbID());
		int diancxxb_id=0;
		long pand_gd_id=getPandbID();
		double dangrscll=0;
		double dangrschl=0;
		double panqhll=0;
		double panqhhl=0;
		double rucpjsf=0;
		double rulpjsf=0;
		double shuifctz=0;
		if (rs!=null&&rs.next()) {
			diancxxb_id=rs.getInt("diancxxb_id");
			dangrscll=rs.getDouble("shicll");
			dangrschl=rs.getDouble("shichl");
			panqhll=rs.getDouble("qianhll");
			panqhhl=rs.getDouble("qianhhl");
			rucpjsf=rs.getDouble("rucmt");
			rulpjsf=rs.getDouble("rulmt");
			shuifctz=rs.getDouble("shuifc");
			
		}
		String sSql=
			"insert into pandqhbhb\n" +
			"  (id,\n" + 
			"   pand_gd_id,\n" + 
			"   dangrscll,\n" + 
			"   dangrschl,\n" + 
			"   panqhll,\n" + 
			"   panqhhl,\n" + 
			"   rucpjsf,\n" + 
			"   rulpjsf,\n" + 
			"   shuifctz)\n" + 
			"values\n" + 
			"  (getnewid("+diancxxb_id+"),"+pand_gd_id+","+dangrscll+","+dangrschl+","+panqhll+","+panqhhl+","+rucpjsf+","+rulpjsf+","+shuifctz+")";
		int re=con.getInsert(sSql);
		if (re==-1) {
			setMsg("����ʧ�ܣ�");
		}else{
			setMsg("���ɳɹ���");
		}
		
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandqhbhb.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//����ɾ������ʱ�����־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Renyzz,
					"pandqhbhb",id+"");
			sSql = "delete from pandqhbhb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandqhbhb.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into pandqhbhb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ",'"
					+ rsl.getString("dangrscll") + "','"
					+ rsl.getString("dangrschl") + "','"
					+ rsl.getString("panqhll") + "','"
					+ rsl.getString("panqhhl") + "','"
					+ rsl.getString("rucpjsf") + "','"
					+ rsl.getString("rulpjsf") + "','"
					+ rsl.getString("shuifctz") + "'"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				//�����޸Ĳ���ʱ�����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Renyzz,
						"pandqhbhb",id+"");
				sSql = "update pandqhbhb set "
					+ " pand_gd_id=" + getPandbID() + ","					
					+ " dangrscll='" + rsl.getString("dangrscll") + "',"
					+ " dangrschl='" + rsl.getString("dangrschl") + "',"
					+ " panqhll='" + rsl.getString("panqhll") + "',"
					+ " panqhhl='" + rsl.getString("panqhhl") + "',"
					+ " rucpjsf='" + rsl.getString("rucpjsf") + "',"
					+ " rulpjsf='" + rsl.getString("rulpjsf") + "',"
					+ " shuifctz='" + rsl.getString("shuifctz") + "'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
	}
	public void loadData() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sID = new StringBuffer();
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select id from pand_gd where riq<(select riq from pand_gd where bianm='" + getPandbm() + "'" 
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + ")"
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + " order by id desc";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				String id = rs.getString("id");
				sql = "select id from pandqhbhb where pand_gd_id =" + id;
				ResultSet rs2 = con.getResultSet(sql);
				boolean flag = false;
				while (rs2.next()) {
					if (!flag) flag = true;
					sID.append(rs2.getString("id")).append(",");
				}
				if (flag) {
					sID.deleteCharAt(sID.length()-1);
					setID(sID.toString());
					flag = false;
				}
			} 
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	//���ɰ���
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
//		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����")
			.append(getPandbm()).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(getPandbm()).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		String sSql = "";
		JDBCcon con = new JDBCcon();
		sSql = "select p.id,dangrscll,dangrschl,panqhll,panqhhl,rucpjsf,rulpjsf,shuifctz from pandqhbhb p,pand_gd where p.pand_gd_id=pand_gd.id and pand_gd.bianm='" + getPandbm() + "'";
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("dangrscll").setHeader("����ʱ������");
		egu.getColumn("dangrschl").setHeader("����ʱ�����");
		egu.getColumn("panqhll").setHeader("��ǰ������");
		egu.getColumn("panqhhl").setHeader("��ǰ�����");
	    egu.getColumn("rucpjsf").setHeader("�볧ƽ��ˮ��");
	    egu.getColumn("rulpjsf").setHeader("��¯ƽ��ˮ��");
	    egu.getColumn("shuifctz").setHeader("ˮ�ֲ����");
		egu.addTbarText("�̵���룺");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
//		if (getID() != null) {
//			gbt = new GridButton("�����ϴ�","function(){document.getElementById('AddButton').click();}");
//			gbt.setIcon(SysConstant.Btn_Icon_Create);
//			egu.addTbarBtn(gbt);
//		}
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//���ɰ���
		GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		//ɾ��
		GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		//����
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
//	public String getAddHandler(JDBCcon con, ExtGridUtil egu) {
////		Visit visit = (Visit) getPage().getVisit();
//		StringBuffer handler = new StringBuffer();
//		handler.append("\nfunction() {\n");	
//		String sql = "select bum,reny,zhiz from pandbmryzzb where id in(" + getID() + ")";
//		System.out.println(sql);
//		try {
//			ResultSet rs = con.getResultSet(sql);
//			while (rs.next()) {
//				handler.append("var plant = new ").append(egu.gridId).append("_plant({");
//				List columns = egu.getGridColumns();
//				for (int i =1; i < columns.size(); i++) {
//					if(((GridColumn)columns.get(i)).coltype == GridColumn.ColType_default) {
//						GridColumn gc = ((GridColumn)columns.get(i));
//						if (gc.dataIndex.equalsIgnoreCase("bum")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("bum")).append("',");
//						} else if (gc.dataIndex.equalsIgnoreCase("reny")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("reny")).append("',");
//						} else if (gc.dataIndex.equalsIgnoreCase("zhiz")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("zhiz")).append("',");
//						} else {
//							handler.append(gc.dataIndex).append(": '").append(gc.defaultvalue).append("',");
//						}
//					}
//				}
//				handler.deleteCharAt(handler.length()-1);
//				handler.append("});\n");
//				handler.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);");
//			}
//		} catch (SQLException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
//		handler.append("}");
//		return handler.toString();
//	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		setID(null);
		loadData();
		getSelectData();
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

