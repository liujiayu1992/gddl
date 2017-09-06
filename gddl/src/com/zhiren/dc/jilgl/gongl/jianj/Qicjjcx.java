package com.zhiren.dc.jilgl.gongl.jianj;

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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * �޸��ˣ���ΰ
 * �޸�ʱ�䣺2009-09-24
 * �޸����ݣ�
 * 			1 ��Ժ����塢��ͷ��������ӡ��ť������������Զ���ӡ
 * 			2 ���ȫ����ӡ��ť��ӡ����δ��ӡ���Ĺ��ⵥ
 * 			 �������ϵͳ����
 			INSERT INTO xitxxb VALUES(
		       (SELECT (MAX(ID)+1) FROM xitxxb),
		       1,diancxxb_id,'���ⵥ�Զ���ӡ','��','','����',1,'ʹ��'
		       )
 */

/*
 * ���ߣ�����
 * ʱ�䣺2009-06-16 14��15
 * ����������¼����Ա��¼��ʱ�䡢�޸���־����   ˫����������ʾ������Ϣ����
 * ʹ�����ݿ⺯�� GetChepRizjj
 */
/*
 * �޸��ˣ����ش�
 * �޸�ʱ�䣺2009-10-29
 * �޸����ݣ���Ժ�����糧�ڳ�Ʊ��ʱ��۶֣����ӱ��水ť�����Խ��п۶�
 * 			
 * 			 �������ϵͳ����
 			INSERT INTO xitxxb VALUES(
		       getnewid(diancxxb_id),
		       1,diancxxb_id,'���ⵥ�ɿ۶�','��','','����',1,'ʹ��'
		       )
 */

public class Qicjjcx extends BasePage implements PageValidateListener {
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
	private boolean zongkdBfb=false;
	
	public boolean isZongkdBfb() {
		return zongkdBfb;
	}
	public void setZongkdBfb(boolean zongkdBfb) {
		this.zongkdBfb = zongkdBfb;
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	
	public String getChepbid() {
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	
	public void setChepids(List list) {
		((Visit)this.getPage().getVisit()).setList8(list);
	}
	
	public List getChepids() {
		return ((Visit)this.getPage().getVisit()).getList8();
	}
	
//	������
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	

	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean isAutoPrint() {
		boolean isAuto = false;
		Visit visit = (Visit) getPage().getVisit();
		isAuto = "��".equals(MainGlobal.getXitxx_item("����","���ⵥ�Զ���ӡ", 
				"" + visit.getDiancxxb_id(),"��"));
		return isAuto;
	}
	
	private boolean issave() {
		boolean editor = false;
		Visit visit = (Visit) getPage().getVisit();
		editor = "��".equals(MainGlobal.getXitxx_item("����","���ⵥ�ɿ۶�", 
				"" + visit.getDiancxxb_id(),"��"));
		return editor;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	private boolean _ShowAllChick = false;
	public void ShowAllButton(IRequestCycle cycle) {
		_ShowAllChick = true;
	}
	
	private boolean _Save = false;
	public void SaveButton(IRequestCycle cycle) {
		_Save = true;
	}
	private void Show(IRequestCycle cycle) {
		boolean isAt = isAutoPrint();
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
		if (isAt) {
			List l = new ArrayList();
			while(rsl.next()) {
				l.add(rsl.getString("id"));
				
			}
			setChepids(l);
			cycle.activate("Qicjjd_Auto");
			
		} else {		
			if(rsl.getRows()!=1) {
				setMsg("��ѡ��һ�����д�ӡ��");
				return;
			}
		
			if(rsl.next()) {
				setChepid(rsl.getString("id"));
				this.setDiancxxb_id(rsl.getString("diancxxb_id"));
			}
			
			cycle.activate("Qicjjd");
		}
	}
	
	
	private void Save(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		long diancxxb_id ;
		int yunsfsb_id =2;

		List fhlist = new ArrayList();
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundxg.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		while(rsl.next()) {
			String fahbid = rsl.getString("fahbid");
			Jilcz.addFahid(fhlist,fahbid);
			
			
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			if(visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String chepid = rsl.getString("id");
			StringBuffer sb = new StringBuffer();
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Shulxg,
					"chepb",chepid);
			sb.append("update chepb set koud=");
			sb.append(rsl.getString("koud"));
			sb.append(",zongkd=").append(this.isZongkdBfb()?( rsl.getDouble("koud")+rsl.getDouble("kous")+rsl.getDouble("kouz") ):(rsl.getDouble("koud")+rsl.getDouble("kous")+rsl.getDouble("kouz") )  );//ԭϵͳzongkd����koud��  ��������֮��
			sb.append(",biaoz=").append(rsl.getDouble("maoz")-rsl.getDouble("piz")-( rsl.getDouble("koud")+rsl.getDouble("kous")+rsl.getDouble("kouz") ) );
			sb.append(" where id=").append(chepid);
			int flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb);
				setMsg(ErrorMessage.Yundxg002);
				return;
			}
			flag = Jilcz.CountChepbYuns(con, chepid, -1);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg005);
				setMsg(ErrorMessage.Jianjxg005);
				return;
			}
			sb.delete(0, sb.length());
			sb.append("select f.id fahbid\n");
			sb.append("from fahb f ,gongysb g,meikxxb m,pinzb p,jihkjb j,vwyuanshdw d\n");
			sb.append("where f.fahrq = to_date('").append(rsl.getString("fahrq")).append("','yyyy-mm-dd')\n");
			sb.append("and f.daohrq = to_date('").append(rsl.getString("daohrq")).append("','yyyy-mm-dd')\n");
			sb.append("and g.mingc='").append(rsl.getString("gongysb_id")).append("'\n");
			sb.append("and m.mingc ='").append(rsl.getString("meikxxb_id")).append("' and p.mingc = '").append(rsl.getString("pinzb_id")).append("'\n");
			//sb.append("and j.mingc = '").append(rsl.getString("jihkjb_id")).append("' and d.mingc = '").append(rsl.getString("yuanshdwb_id")).append("'\n");
			sb.append("and f.yuandz_id = 1 and f.faz_id = 1 and f.daoz_id = 1 and f.diancxxb_id = ").append(diancxxb_id).append("\n");
			sb.append("and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.pinzb_id=p.id\n");
			sb.append("and f.jihkjb_id=j.id and f.yuanshdwb_id=d.id and f.yunsfsb_id = ").append(yunsfsb_id);
			ResultSetList r = con.getResultSetList(sb.toString());
			if (r == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			if(r.next()) {

					Jilcz.addFahid(fhlist,r.getString("fahbid"));
				}
		
		}
		
		for(int i=0; i< fhlist.size() ;i++) {
			int flag = Jilcz.updateFahb(con,(String)fhlist.get(i));
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg006);
				setMsg(ErrorMessage.Jianjxg006);
				return;
			}
			flag = Jilcz.updateLieid(con,(String)fhlist.get(i));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Jianjxg007);
				setMsg(ErrorMessage.Jianjxg007);
				return;
			}
			
		}
		
		con.commit();
		con.Close();
		setMsg("����ɹ�");
	}
	private void showAll (IRequestCycle cycle) {
		if (this.getChepids().size()<1) {
			setMsg("û��Ҫ��ӡ����");
			return;
		}
		cycle.activate("Qicjjd_Auto");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
		if (_ShowAllChick) {
			_ShowAllChick = false;
			showAll(cycle);
		}
		if (_Save) {
			_Save = false;
			Save();
			init();
		}
	}
	
	/*
	 * ����:ͯ�Ҹ�
	 * ʱ��:2009-4-13
	 * �޸�����:����һ������ʱ�糧id�Ĺ���
	 
	 */
	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	//¼����ԱΪ����Ϊû�д�ӡ����¼�ó�id,�Ա�������ӡ
	private void getNoPrintChepbID(ResultSetList rsl) { 
		List list = new ArrayList();
		if (rsl == null) {
			return;
		}
		
		while (rsl.next()) {
			if (rsl.getString("lury")==null || "".equals(rsl.getString("lury").trim())) {
				list.add(rsl.getString("id"));
			}
		}
		this.setChepids(list);
	}
	
	/*
	 * ����:ͯ�Ҹ�
	 * ʱ��:2009-4-3
	 * �޸�����:���Ӱ�����ģ����ѯ����
	 * 
	 */
	
	/*
	 * ����:���ش�
	 * ʱ��:2009-10-28
	 * �޸����ݣ��۶ֿɱ༭
	 * 
	 */
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  C.ID,c.xuh,D.ID diancxxb_id,c.fahb_id, G.MINGC GONGYSB_ID, M.MINGC MEIKXXB_ID, P.MINGC PINZB_ID,\n")
		.append("		C.CHEPH,c.lury, TO_CHAR(C.QINGCSJ,'YYYY-MM-DD HH24:MI:SS') QINGCSJ, C.MAOZ, C.PIZ, C.BIAOZ,\n")
		.append("		(C.MAOZ-C.PIZ-C.ZONGKD) JINGZ,c.koud, C.ZONGKD,\n")
		.append("		C.ZHONGCJJY||'/'||C.QINGCJJY JIANJY, C.PIAOJH,TO_CHAR(C.ZHONGCSJ,'YYYY-MM-DD HH24:MI:SS') ZHONGCSJ,\n")
		.append("		  to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') lursj,f.fahrq,f.daohrq, GetChepRizjj(c.id) rizsm \n")
//		.append("		FROM FAHB F, CHEPB C, GONGYSB G, MEIKXXB M, PINZB P\n")
			.append("		FROM FAHB F, CHEPB C, GONGYSB G, MEIKXXB M, PINZB P,DIANCXXB D\n")
		.append("WHERE F.ID = C.FAHB_ID AND F.GONGYSB_ID = G.ID \n")

		.append("and  F.diancxxb_id =D.ID  AND F.diancxxb_id in "+this.filterDcid(visit)+"\n")
		
		.append("AND F.MEIKXXB_ID = M.ID AND F.PINZB_ID = P.ID \n")
		.append("AND C.QINGCSJ BETWEEN \n")
		.append(DateUtil.FormatOracleDate(getBeginRiq()))
		.append("AND ")
		.append(DateUtil.FormatOracleDate(getEndRiq()))
		.append("+1 \nAND F.YUNSFSB_ID = ")
		.append(SysConstant.YUNSFS_QIY)
		.append("\n")
		.append("		ORDER BY c.xuh desc");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		//ȡ��û�д�ӡ��chepbid
		getNoPrintChepbID(rsl);
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//      grid�ɱ༭		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//����ÿҳ��ʾ����
		egu.addPaging(24);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor=null;
		
		egu.getColumn("daohrq").setHidden(true);
		egu.getColumn("daohrq").editor=null;
		
		egu.getColumn("fahrq").setHidden(true);
		egu.getColumn("fahrq").editor=null;
		
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").editor=null;
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu.getColumn("piaojh").setWidth(100);
		egu.getColumn("piaojh").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setRenderer("mzChange");
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("koud").setHeader(Locale.koud_fahb);
		egu.getColumn("koud").setWidth(60);
		if(!this.issave()){
			
			egu.getColumn("koud").setHidden(true);
	
			
		}
		
		egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb);
		egu.getColumn("zongkd").setWidth(60);
		egu.getColumn("zongkd").setEditor(null);
		egu.getColumn("jianjy").setHeader("�����Ա");
		egu.getColumn("jianjy").setWidth(100);
		egu.getColumn("jianjy").setEditor(null);
		egu.getColumn("qingcsj").setHeader(Locale.qingcsj_chepb);
		egu.getColumn("qingcsj").setWidth(110);
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("lury").setHeader("¼����Ա");
		//egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setRenderer("ztChange");
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("rizsm").setHeader("��־˵��");
		egu.getColumn("rizsm").setHidden(true);
		egu.getColumn("rizsm").setEditor(null);
		
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setWidth(110);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setHidden(true);
		
		if(visit.getString15()!=null && visit.getString15().equals("PRINT_BAOER")){
			egu.getColumn("zhongcsj").setHidden(false);
		}
		egu.addTbarText("���복�ţ�");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 
		 theKey.setListeners("change:function(thi,newva,oldva){ sta='';}\n");
		 egu.addToolbarItem(theKey.getScript());
//	  ����ext�еĵڶ���egu�����д���gridDiv�����ı������ȵ�һ����Piz������gridDiv----gridDivPiz.
		GridButton chazhao=new GridButton("��ģ��������/������һ��","function(){\n"+
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
	                "           var rec=gridDiv_ds.getRange();\n "+
	                "           for(var j=0;j<rec.length;j++){\n "+
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
	                "      }\n");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");
		
		egu.addOtherScript("gridDiv_grid.on('rowdblclick',function(own,row,e){showRizsm(row)});");
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("BeginRq");
		egu.addTbarText("������ڣ�");
		egu.addToolbarItem(df.getScript());
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		egu.addTbarText("��");
		egu.addToolbarItem(dfe.getScript());
		
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		if (this.isAutoPrint()) {
			GridButton gb = new GridButton("ȫ����ӡ", "function() {document.getElementById('ShowAllButton').click();}");
			gb.setIcon(SysConstant.Btn_Icon_Print);
			egu.addTbarBtn(gb);
		}
		if(this.issave()){
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			
		}
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
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
			if(!visit.getActivePageName().toString().equals("Qicjjd")){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				String dianclb= cycle.getRequestContext().getParameter("lx");
				if(dianclb!=null){
					visit.setString15(dianclb);
				}else{
					visit.setString15("PRINT_MOR");
					//��ʼ�����ģʽ
					setJianjdmodel();
				}
			}
			//getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
		init();
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		setChepids(null);
		getSelectData();
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
		con.Close();
	}
}