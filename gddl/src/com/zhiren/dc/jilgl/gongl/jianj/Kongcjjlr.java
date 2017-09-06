package com.zhiren.dc.jilgl.gongl.jianj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-05-14
 * ����
 * �����Ƿ����ɱ��Ĳ�������
 */
/*
 * 2009-05-13
 * ����
 * ���ӿ�ˮ���Ӵ���,����ϵͳ�������Ƽ��СƱ��ģʽ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-05-23
 * ��������ˮ�����Ƿ���ʾ���Ӳ������� Ĭ��Ϊ����ʾ
 */
public class Kongcjjlr extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
    //���ҹؼ���
	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
			+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private boolean Suodzt(String id){
		boolean issuod=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		StringBuffer sql= new StringBuffer();
			sql.append("select cl.islocked from chepb cp,chelxxb cl where cp.id=");
			sql.append(id);
			sql.append(" and cp.cheph=cl.cheph");
		ps=con.getPresultSet(sql.toString());
	
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs.getString("islocked").equals("1")){
					issuod=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return issuod;
	}
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		�õ��Ƿ����ɱ��Ĳ���
		boolean isCosting = con.getHasIt("select * from xitxxb where mingc = '�ᳵ����Ƿ����ɱ�' and zhi='��' and zhuangt =1 and diancxxb_id =" + visit.getDiancxxb_id());
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		List fhlist = new ArrayList();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Kongcjjlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
//			long diancxxb_id = 0;
//			if(visit.isFencb()) {
//				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
//			}else {
//				diancxxb_id = visit.getDiancxxb_id();
//			}
			String id = rsl.getString("id");
			if(Suodzt(id)){
				setMsg("�ó��ѱ�����,��������!");
				return;
			}
			double maoz = rsl.getDouble("maoz");
			double piz = rsl.getDouble("piz");
			double biaoz = rsl.getDouble("biaoz");
			double koud = rsl.getDouble("koud");
			double kous = rsl.getDouble("kous");
			double kouz = rsl.getDouble("kouz");
			double zongkd = koud + kous + kouz ;
			String fahbid = rsl.getString("fahb_id");
			Jilcz.addFahid(fhlist, fahbid);
			if(biaoz == 0.0) {
				/*
				 * 2009-02-12
				 * wl
				 * Ʊ�����Զ�����ó�ʱ Ʊ��=����
				 */
				/*
				 * 2009-09-16
				 * huochaoyuan
				 * Ʊ�����Զ�����ó�ʱ Ʊ��=����(����ˮ����)
				 */
				biaoz = maoz - piz - koud-kous-kouz;
			}
			sb.delete(0, sb.length());
//			���³�Ƥ�� Ƥ�ء�Ʊ�ء���ע �����복Ƥ��
			sb.append("update chepb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=").append(koud)
			.append(",kous=").append(kous).append(",kouz=").append(kouz).append(",zongkd=").append(zongkd);
			sb.append(",qingcsj = ").append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')");
			sb.append(",qingcjjy = '").append(rsl.getString("qingcjjy")).append("',qingchh = '").append(rsl.getString("qingchh"));
			sb.append("',beiz = '").append(rsl.getString("beiz")).append("' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Kongcjjlr001);
				return;
			} else {
				
				// �ж�Ʊ���Ƿ���۶�
				Jilcz.piaozPz(con, id, "chepb");
			}
//			���泵����Ϣ��
			Jilcz.SaveChelxx(con,visit.getDiancxxb_id(),getYunsdwModel().getBeanId(rsl.getString("yunsdwb_id")),SysConstant.YUNSFS_QIY,
					rsl.getString("CHEPH"),maoz,piz);
//			���ݵ���id ����jilcz ��CountChepbYuns �������㵥��������ӯ��
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ );
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr002);
				setMsg(ErrorMessage.Kongcjjlr002);
				return;
			}
//			���ݳ�Ƥ����fahid ����Jilcz �� updateFahb �������·�����
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr003);
				setMsg(ErrorMessage.Kongcjjlr003);
				return;
			}
			flag = Jilcz.updateLieid(con,fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr004);
				setMsg(ErrorMessage.Kongcjjlr004);
				return;
			}
			visit.setString1(id);
		}
		con.commit();
		con.Close();
		setMsg("����ɹ�");
//		�Ƿ����ɱ�
		if(isCosting){
			Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		}
	}
	
	private boolean _AutoSaveChick = false;
	public void AutoSaveButton(IRequestCycle cycle) {
		_AutoSaveChick = true;
	}
	
	private void AutoSave() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Kongcjjlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = getAutoPiz(con,rsl.getString("cheph"));
			if(piz==0) {
				setMsg("����ʧ�ܣ��ó����Ѽ����Ϣ��");
				return;
			}
			double biaoz = rsl.getDouble("biaoz");
			double koud = rsl.getDouble("koud");
			String fahbid = rsl.getString("fahb_id");
			if(biaoz == 0.0) {
				biaoz = maoz - piz;
			}
			sb.delete(0, sb.length());
//			���³�Ƥ�� Ƥ�ء�Ʊ�ء���ע �����복Ƥ��
			sb.append("update chepb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=").append(koud).append(",zongkd=").append(koud);
			sb.append(",qingcsj = ").append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')");
			sb.append(",qingcjjy = '").append(rsl.getString("qingcjjy")).append("',qingchh = '").append(rsl.getString("qingchh"));
			sb.append("',beiz = 'Ƥ��ȡ����ʷ��Ƥ��¼' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Kongcjjlr001);
				return;
			} else {
				
				// �ж�Ʊ���Ƿ���۶�
				Jilcz.piaozPz(con, id, "chepb");
			}
//			���ݵ���id ����jilcz ��CountChepbYuns �������㵥��������ӯ��
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ );
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr002);
				setMsg(ErrorMessage.Kongcjjlr002);
				return;
			}
//			���ݳ�Ƥ����fahid ����Jilcz �� updateFahb �������·�����
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr003);
				setMsg(ErrorMessage.Kongcjjlr003);
				return;
			}
			flag = Jilcz.updateLieid(con,fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjjlr004);
				setMsg(ErrorMessage.Kongcjjlr004);
				return;
			}
			visit.setString1(id);
		}
		con.commit();
		con.Close();
		setMsg("����ɹ�");
	}
	
	private double getAutoPiz(JDBCcon con,String cheph) {
		double piz = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select piz from chelxxb where cheph='")
		.append(cheph).append("' and yunsfsb_id=").append(SysConstant.YUNSFS_QIY);
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			piz = rsl.getDouble("piz");
		}
		return piz;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_AutoSaveChick) {
			_AutoSaveChick = false;
			AutoSave();
			init();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}
	
	/**
	 * ����:tzf
	 * ʱ��:2009-4-14
	 * ����:��ת����ӡҳ��ʱ�����ݲ��� chepb_id,diancxxb_id
	 * @param cycle
	 */
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ�����ݽ��в鿴��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(rsl.getRows()!=1) {
			setMsg("��ѡ��һ�����д�ӡ��");
			return;
		}
		if(rsl.next()) {
			setChepid(rsl.getString("id"));
			this.setDiancxxb_id(rsl.getString("dc_id"));
		}
		cycle.activate("Qicjjd");
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,f.diancxxb_id dc_id,c.fahb_id, c.cheph, d.mingc diancxxb_id, g.mingc gongysb_id, m.mingc meikxxb_id, p.mingc pinzb_id,\n");
		sb.append("c.maoz, c.piz, c.biaoz, c.sanfsl, c.koud, c.kous, c.kouz, c.yuns, mc.mingc meicb_id, j.mingc jihkjb_id, \n");
		sb.append("y.mingc yunsdwb_id,nvl('").append(visit.getRenymc()).append("','') as qingcjjy, c.qingchh, c.beiz\n");
		sb.append("from chepb c, fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, meicb mc, yunsdwb y, diancxxb d\n");
		sb.append("where f.id = c.fahb_id and c.piz = 0 and f.yunsfsb_id=").append(SysConstant.YUNSFS_QIY).append(" \n");
		sb.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id and f.diancxxb_id = d.id\n");
		sb.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id and c.hedbz < ").append(SysConstant.HEDBZ_YJJ).append(" \n");
		sb.append("and c.meicb_id = mc.id(+) and c.yunsdwb_id = y.id(+) \n");
//		����ע����仰���ж� ����
		sb.append("and c.chebb_id = ").append(SysConstant.CHEB_QC);
	
		sb.append(" order by c.zhongcsj desc");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu;
		egu = new ExtGridUtil("gridDiv", rsl);
		//egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridType(ExtGridUtil.GridselModel_Row_single);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(453);
		egu.addPaging(16);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("dc_id").setHidden(true);
		egu.getColumn("dc_id").editor=null;
		
		
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("cheph").setEditor(null);
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
		}
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("yuns").setHidden(true);
		egu.getColumn("yuns").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(50);
		egu.getColumn("piz").editor.setAllowBlank(false);
		String sql = "select * from shuzhlfwb where leib ='����' and mingc = '������Ƥ��' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			egu.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
		rsl.close();
		//egu.getColumn("piz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(50);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setWidth(50);
		egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setWidth(60);
		sql = "select * from xitxxb where mingc = '��ˮ�����Ƿ���ʾ' " +
			"and zhuangt=1 and diancxxb_id =" + visit.getDiancxxb_id() +
			" and beiz= 'ʹ��' and zhi='��'";
		if(!con.getHasIt(sql)){
			egu.getColumn("kous").setHidden(true);
			egu.getColumn("kouz").setHidden(true);
		}
		egu.getColumn("meicb_id").setHeader(Locale.meicb_id_chepb);
		egu.getColumn("meicb_id").setWidth(80);
		egu.getColumn("meicb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu.getColumn("qingcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu.getColumn("qingchh").setWidth(70);
		egu.getColumn("qingchh").setEditor(null);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(50);
		
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		
		egu.addTbarText("���복�ţ�");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 theKey.setListeners("change:function(thi,newva,oldva){ sta='';},specialkey:function(thi,e){if(e.getKey()==13){chaxun();}}\n");
		 egu.addToolbarItem(theKey.getScript());
	
		GridButton chazhao=new GridButton("��ģ��������/������һ��","function(){chaxun(); }\n");
	     chazhao.setIcon(SysConstant.Btn_Icon_Search);
	     
	     String otherscript = " var sta=''; function chaxun(){ \n"+
	    	 "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"+
             "       var len=gridDiv_data.length;\n"+
             "       var count;\n"+
             "       if(len%"+egu.getPagSize()+"!=0){\n"+
             "        count=parseInt(len/"+egu.getPagSize()+")+1;\n"+
             "        }else{\n"+
             "          count=len/"+egu.getPagSize()+";\n"+
             "        }\n"+
             "        for(var i=0;i<count;i++){\n"+
             "           gridDiv_ds.load({params:{start:i*"+egu.getPagSize()+", limit:"+egu.getPagSize()+"}});\n"+
             "           var rec=gridDiv_ds.getRange();\n"+
             "           for(var j=0;j<rec.length;j++){\n"+
             "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
             "                 var nw=[rec[j]]\n"+
             "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
             "                      gridDiv_sm.selectRecords(nw);\n"+
             "                      sta+=rec[j].get('ID').toString()+';';\n"+
             "                       return;\n"+
             "                  }\n"+
             "                \n"+
             "               }\n"+
             "           }\n"+
             "        }\n"+
             "        if(sta==''){\n"+
             "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"+
             "        }else{\n"+
             "           sta='';\n"+
             "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"+
             "         }\n"+
	     	 " }\n";
	     egu.addOtherScript(otherscript);
	     
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("������ʷƤ��",GridButton.ButtonType_Save, "AutoSaveButton", null, SysConstant.Btn_Icon_Save);
	//	��ӡ��ť
//		GridButton gbp = new GridButton("��ӡ","function (){"+MainGlobal.getOpenWinScript("Qicjjd")+"}");
		GridButton gbp = new GridButton("��ӡ","function (){"+"document.all.ShowButton.click();"+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
//		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
		
		boolean kougFlag=false;
		String kouggs="KOUD";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kougFlag=true;
			kouggs=rsl.getString("zhi");
		}
		
		boolean kousFlag=false;
		String kousgs="KOUS";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='��ˮ�ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kousFlag=true;
			kousgs=rsl.getString("zhi");
		}
		
		boolean kouzFlag=false;
		String kouzgs="KOUZ";
		rsl=con.getResultSetList(" select zhi from xitxxb where  mingc='�����ǰٷֱ�¼��'  and leib='����' and zhuangt=1 and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id());
		if(rsl.next()){
			kouzFlag=true;
			kouzgs=rsl.getString("zhi");
		}
		
		if(kougFlag || kousFlag ||  kouzFlag){
			egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ " +
					
					" var rec=e.record;\n" +
					" var MAOZ=rec.get('MAOZ');\n" +
					" var PIZ=rec.get('PIZ');\n"+
					" var KOUD=rec.get('KOUD');\n" +
					" var KOUS=rec.get('KOUS');\n" +
					" var KOUZ=rec.get('KOUZ');"+
					" var BIAOZ=rec.get('BIAOZ');"+
					" var flag = false;"+
					" var JINGZ = 0;"+
					" if(PIZ==null || PIZ=='' || PIZ==0)\n"+
					" {Ext.MessageBox.alert('��ʾ','���Ȳɼ�Ƥ��');rec.set('KOUD',0);rec.set('KOUS',0);rec.set('KOUZ',0);rec.set('BEIZ','0,0,0');return;}\n"+
					" if(KOUD==null || KOUD==''){KOUD=0;}\n"+
					" if(KOUS==null || KOUS==''){KOUS=0;}\n"+
					" if(KOUZ==null || KOUZ==''){KOUZ=0;}\n"+
					
					"if( e.field=='KOUD' ){\n" +
						" rec.set('KOUD',Round_new("+kouggs+",2) );"+
					"} \n" +
					
					"if( e.field=='KOUS' ){\n" +
					" rec.set('KOUS',Round_new("+kousgs+",2));"+
					"} \n" +
					
					"if( e.field=='KOUZ' ){\n" +
					" rec.set('KOUZ',Round_new("+kouzgs+",2));"+
					"} \n" +
					"" +
					
					" var bs=rec.get('BEIZ');\n" +
					" if(bs==null ||  bs==''){\n" +
					" bs='0,0,0';\n"+
					" }\n"+
					" var bssp=bs.split(',');\n"+//  ��ʽ  koud �� kous �� kouz
					"if(e.field=='KOUD'){bs=KOUD+','+bssp[1]+','+bssp[2];}\n" +
					"if(e.field=='KOUS'){bs=bssp[0]+','+KOUS+','+bssp[2];}\n" +
					"if(e.field=='KOUZ'){bs=bssp[0]+','+bssp[1]+','+KOUZ;}\n" +
				
					"rec.set('BEIZ',bs);\n"+
					""+
//					"rec.set('BIAOZ',Round_new(rec.get('MAOZ')-rec.get('PIZ')-rec.get('KOUD')-rec.get('KOUS')-rec.get('KOUZ'),2));\n" +
					"\n});" );
		}
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private void setJianjdmodel(){
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='�������ﵥģʽ' and zhuangt = 1 and diancxxb_id =" + ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if(rsl.next()){
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
	} 
	
	private void init() {
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		getSelectData();
	}
}