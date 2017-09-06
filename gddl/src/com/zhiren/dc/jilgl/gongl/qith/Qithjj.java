package com.zhiren.dc.jilgl.gongl.qith;

import java.util.Date;

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
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-15 16��42
 * �������޸�ˢ��δ��ʼ��¼��Ա,����ʱ���ű��治��,Ʒ��ȡֵ����ȷ��BUG
 */
public class Qithjj extends BasePage implements PageValidateListener {
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
		setPTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	private String ptbmsg;
	public String getPTbmsg() {
		return ptbmsg;
	}
	public void setPTbmsg(String tbmsg) {
		this.ptbmsg = tbmsg; 
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change; 
	}
	
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
	//��ӡ��ť
	private boolean _DayButton = false;
	public void DayButton(IRequestCycle cycle) {
		_DayButton = true;
	}
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
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
					+ "Qicjjlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			long diancxxb_id = 0;
			if(visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
		//ע����³���糧����������qithwb�����zhongcsj������Qicjjd_qith.java��ʾ���ļ�ﵥ�С���ë��ʱ�䡱Ϊ�հ�
			sb.append("insert into qithwb \n");
			sb.append("(id, xuh, cheph, diancxxb_id, qitgys_id, pinzb_id, piaojh, fahrq, daohrq, yuanmz, maoz, piz, biaoz,\n");
			sb.append(" qitysdwb_id , yingk, yuns, koud, ches, jianjfs, chebb_id, zhongcjjy, zhongchh, zhongcsj, lursj,lury,beiz,hedbz,shenhzt) \n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),").append(rsl.getString("xuh")).append(",'").append(rsl.getString("cheph")).append("',");
			sb.append(diancxxb_id).append(",");
			sb.append((getExtGrid().getColumn("qitgys_id").combo).getBeanId(rsl.getString("qitgys_id"))).append(",");
			sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"))).append(",");
			sb.append("'").append(rsl.getString("piaojh")).append("',");
			sb.append("to_date('").append(rsl.getString("fahrq")).append("','yyyy-mm-dd hh24:mi:ss')").append(",");
			sb.append("to_date('").append(rsl.getString("daohrq")).append("','yyyy-mm-dd hh24:mi:ss')").append(",");
			//sb.append("to_date('").append(new Date()).append("','yyyy-mm-dd hh24:mi:ss')").append(",");
			sb.append(rsl.getString("maoz")).append(",");
			sb.append(rsl.getString("maoz")).append(",");
			sb.append(rsl.getString("piz")).append(",");
			sb.append(rsl.getString("biaoz")).append(",");
			sb.append((getExtGrid().getColumn("qitysdwb_id").combo).getBeanId(rsl.getString("qitysdwb_id"))).append(",");
			sb.append("0,").append("0,");
			sb.append("0,").append("0,");
			sb.append("'����',");
			sb.append("3,");
			
			sb.append("'").append(rsl.getString("zhongcjjy")).append("',");
			sb.append("'").append(rsl.getString("zhongchh")).append("',");
			sb.append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')").append(",");
			sb.append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')").append(",");
			sb.append("'").append(rsl.getString("lury")).append("',");
			sb.append("'").append(rsl.getString("beiz")).append("',0,").append("0")
			.append(")");
			
			
			
			
		}
		
		if(sb.length() == 0) {
			WriteLog.writeErrorLog(ErrorMessage.Qicjjlr001);
			setMsg(ErrorMessage.Qicjjlr001);
			con.rollBack();
			con.Close();
			return;
		}
		
		
		flag = con.getInsert(sb.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb);
			setMsg(ErrorMessage.Qicjjlr002);
			return;
		}
		
		con.commit();  
		con.Close();
		setMsg("ë�ر���ɹ�");
	}
	private boolean _SavePizChick = false;
	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}
	
	
	private void SavePiz() {
		if(getChange()==null || "".equals(getChange())) {
			setPTbmsg("û�������κθ��ģ�");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qincjjlr.SavePiz �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		while(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = rsl.getDouble("piz");
			double biaoz = rsl.getDouble("biaoz");
			double koud = rsl.getDouble("koud");
		
			if(biaoz == 0.0) {
				biaoz = maoz - piz;
			}
			sb.delete(0, sb.length());
//			���³�Ƥ�� Ƥ�ء�Ʊ�ء��ᳵʱ�䡢�ᳵ��š��ᳵ��Ա �����복Ƥ��
			sb.append("update qithwb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=")
			.append(koud).append(",cheph=").append(rsl.getString("cheph"));
			sb.append(",qingcsj=sysdate,qingchh='").append(rsl.getString("qingchh")).append("',");
			sb.append("qingcjjy='").append(rsl.getString("qingcjjy")).append("'");
//			sb.append("renyxxb_id = ").append(getPizGrid().getValueSql(getPizGrid().getColumn("renyxxb_id"),rsl.getString("renyxxb_id")));
			sb.append(" where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjjlr008);
				setMsg(ErrorMessage.Qicjjlr008);
				return;
			}
//			
		
			visit.setString1(id);
		}
		con.commit();
		con.Close();
		setPTbmsg("Ƥ�ر���ɹ�");
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
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
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
			
			if(biaoz == 0.0) {
				biaoz = maoz - piz;
			}
			sb.delete(0, sb.length());
//			���³�Ƥ�� Ƥ�ء�Ʊ�ء���ע �����복Ƥ��
			sb.append("update qithwb set piz = ").append(piz).append(",biaoz = ").append(biaoz).append(",koud=").append(koud).append(",zongkd=").append(koud);
			sb.append(",qingcsj = ").append("to_date('").append(DateUtil.FormatDateTime(new Date())).append("','yyyy-mm-dd hh24:mi:ss')");
			sb.append(",qingcjjy = '").append(rsl.getString("qingcjjy")).append("',qingchh = '").append(rsl.getString("qingchh"));
//			sb.append(",renyxxb_id = ").append(getPizGrid().getValueSql(getPizGrid().getColumn("renyxxb_id"),rsl.getString("renyxxb_id")));
			sb.append("',beiz = 'Ƥ��ȡ����ʷ��Ƥ��¼' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Kongcjjlr001);
				return;
			}

		}
		con.commit();
		con.Close();
		setMsg("����ɹ�");
	}
	
	private double getAutoPiz(JDBCcon con,String cheph) {
		double piz = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select max(piz) piz from chelxxb where cheph='")
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
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_AutoSaveChick) {
			_AutoSaveChick = false;
			AutoSave();
			init();
		}
		if(_DayButton){
			_DayButton = false;
			System.out.println(this.getChange());
			Visit visit = (Visit) this.getPage().getVisit();
			visit.setString19(getChange());
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		ExtGridUtil egu;
		 String g="select id,mingc from qitgysb order by mingc";
		ResultSetList rslg=con.getResultSetList(g);
		 String qitgys="";
		//String chep="";
		String pinz="";
		String yunsdw="";
		if(rslg.next()){
			qitgys=rslg.getString("mingc");
			
		}
		/*g=" select * from chebb where mingc<>'Ƥ����' order by id";
		rslg=con.getResultSetList(g);
		if(rslg.next()){
			chep=rslg.getString("mingc");
			System.out.println(chep);
		}*/
		g="select id,mingc from qitysdwb where diancxxb_id="+visit.getDiancxxb_id()+" order by mingc";
		rslg=con.getResultSetList(g);
		if(rslg.next()){
			yunsdw=rslg.getString("mingc");
		}
		g="select id,mingc from pinzb where leib = '����' order by mingc";
		rslg=con.getResultSetList(g);
		if(rslg.next()){
			pinz=rslg.getString("mingc");
			
		}
		sb.append("select 0 id,0 xuh,'' cheph, '' diancxxb_id, nvl('").append(qitgys).append("','') qitgys_id,  nvl('").append(pinz).append("','') pinzb_id, \n");
		sb.append("sysdate fahrq, sysdate daohrq ,0 maoz, 0 piz,0 biaoz, \n");
		sb.append("nvl('").append(yunsdw).append("','') qitysdwb_id ,'' piaojh, 0 yingk ,0 yuns ,0 koud ,0 ches,\n"); 
		sb.append(" nvl('").append(visit.getRenymc()).append("','') zhongcjjy,'' zhongchh,");
		sb.append("sysdate lursj,'").append(visit.getRenymc()).append("' lury , '' beiz from dual");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(88);
		egu.addPaging(0);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setWidth(60);
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
		egu.getColumn("qitgys_id").setHeader("��Ӧ��");
		egu.getColumn("qitgys_id").setWidth(100);
		egu.getColumn("qitgys_id").setEditor(null);
	
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(70); 	
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("piaojh").setHeader("�˵�Ʊ�ݺ�");
		egu.getColumn("piaojh").setWidth(100);
		
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		//egu.getColumn("yuanmz").setHeader("ԭë��");
		//egu.getColumn("yuanmz").setWidth(50);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").editor.setAllowBlank(false);
		String sql1 = "select * from shuzhlfwb where leib ='����' and mingc = '������ë��' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql1);
		if(rsl.next()){
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(50);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(50);
		//egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("qitysdwb_id").setHeader("���䵥λ");
		egu.getColumn("qitysdwb_id").setWidth(60);
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yingk").setHidden(true);
		egu.getColumn("yingk").setEditor(null);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("yuns").setHidden(true);
		egu.getColumn("yuns").setEditor(null);
		
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("koud").setHidden(true);
		egu.getColumn("koud").setEditor(null);
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setHidden(true);
		egu.getColumn("ches").setEditor(null);
		
		//egu.getColumn("jianjfs").setHeader("��﷽ʽ");
		//egu.getColumn("jianjfs").setWidth(80);
		//egu.getColumn("chebb_id").setHeader("����");
		//egu.getColumn("chebb_id").setWidth(80);
		
		egu.getColumn("zhongcjjy").setHeader("�س����Ա");
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setHeader("�س����");
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setEditor(null);
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setWidth(80);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setWidth(60);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(100);
//		egu.getColumn("shoulr").setHeader("������");
//		egu.getColumn("shoulr").setWidth(70);
		
		//����������������
//		ComboBox shoulr= new ComboBox();
//		egu.getColumn("shoulr").setEditor(shoulr);
//		shoulr.setEditable(true);
//		String shoulrSql="select id,quanc from renyxxb where zhiw = '������' order by mingc";
//		egu.getColumn("shoulr").setComboEditor(egu.gridId, new IDropDownModel(shoulrSql));
		
//   	���ù�Ӧ��������
		ComboBox cgys= new ComboBox();
		egu.getColumn("qitgys_id").setEditor(cgys);
		cgys.setEditable(true);
		String gysSql="select id,mingc from qitgysb order by mingc";
		egu.getColumn("qitgys_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
	
	
//		����Ʒ��������
		ComboBox cpz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cpz);
		cpz.setEditable(true);
		String pinzSql="select id,mingc from pinzb where leib = '����' order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		
	
//		���䵥λ
		ComboBox cysdw = new ComboBox();
		egu.getColumn("qitysdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from qitysdwb where diancxxb_id="+ visit.getDiancxxb_id();
		egu.getColumn("qitysdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
		
/*		��﷽ʽ
		
		egu.getColumn("jianjfs").setEditor(new ComboBox());
		egu.getColumn("jianjfs").setDefaultValue("����");
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"����"));
		list.add(new IDropDownBean(2,"���"));
		egu.getColumn("jianjfs").setComboEditor(egu.gridId, new IDropDownModel(list));
*/
		/*
//      ����
		ComboBox cheb=new ComboBox();
		egu.getColumn("chebb_id").setEditor(cheb);
		cheb.setEditable(true);
		String chebsql="select * from chebb where mingc<>'Ƥ����'";
		egu.getColumn("chebb_id").setComboEditor(egu.gridId, new IDropDownModel(chebsql));
		*/
	
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='nosel';DataIndex='MAOZ';});");
		/*egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		*/egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		setExtGrid(egu);

		
		sb.delete(0, sb.length());
		sb.append("select q.id,q.cheph,d.mingc diancxxb_id,--(select r.quanc from renyxxb r where id = renyxxb_id) as renyxxb_id,\n g.mingc qitgys_id,p.mingc pingzb_id ,");
		sb.append("q.fahrq fahrq ,q.yuanmz yuanmz,q.maoz maoz,q.piz piz,q.biaoz biaoz ,");
		sb.append("qt.mingc qitysdwb_id,q.koud koud,nvl('").append(visit.getRenymc()).append("','') qingcjjy,");
		sb.append("q.qingchh qingchh,q.beiz beiz");
		sb.append("  from qithwb q,diancxxb d,qitgysb g,pinzb p,qitysdwb qt");
		sb.append(" where q.diancxxb_id=d.id and q.qitgys_id=g.id and q.pinzb_id=p.id and q.qitysdwb_id=qt.id(+) order by q.lursj desc\n");
		ResultSetList rsl1 = con.getResultSetList(sb.toString());

		if (rsl1 == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl1);
		//����ҳ����
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		egu1.setHeight("bodyHeight-206");
		egu1.addPaging(12);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").editor=null;
		egu1.getColumn("cheph").setHeader("����");
		egu1.getColumn("cheph").setWidth(50);
		egu1.getColumn("diancxxb_id").setHidden(true);
		egu1.getColumn("diancxxb_id").setEditor(null);
		egu1.getColumn("qitgys_id").setHeader("��Ӧ��");
		egu1.getColumn("qitgys_id").setWidth(100);
		egu1.getColumn("qitgys_id").setEditor(null);
		egu1.getColumn("pingzb_id").setHeader("Ʒ��");
		egu1.getColumn("pingzb_id").setWidth(60);
		egu1.getColumn("pingzb_id").setEditor(null);
		
//		egu1.getColumn("renyxxb_id").setHeader("������");
//		
//		egu1.getColumn("renyxxb_id").setWidth(70);
//		
//		//����������������
//		ComboBox shoulr= new ComboBox();
//		egu1.getColumn("renyxxb_id").setEditor(shoulr);
//		egu1.getColumn("renyxxb_id").setHidden(true);
////		shoulr.setEditable(true);
//		String shoulrSql="select id,quanc from renyxxb where zhiw = '������' order by mingc";
//		egu1.getColumn("renyxxb_id").setComboEditor(egu1.gridId, new IDropDownModel(shoulrSql));
//		egu1.getColumn("renyxxb_id").returnId=true;
//		
		egu1.getColumn("fahrq").setHeader("��������");
		egu1.getColumn("fahrq").setWidth(70);
		egu1.getColumn("fahrq").setEditor(null);
		egu1.getColumn("yuanmz").setHeader("ԭë��");
		egu1.getColumn("yuanmz").setWidth(60);
		egu1.getColumn("yuanmz").setEditor(null);
		egu1.getColumn("maoz").setHeader("ë��");
		egu1.getColumn("maoz").setWidth(50);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("piz").setHeader("Ƥ��");
		egu1.getColumn("piz").setWidth(50);
		sql1 = "select * from shuzhlfwb where leib ='����' and mingc = '������Ƥ��' and diancxxb_id = " + visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql1);
		if(rsl.next()){
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
		egu1.getColumn("biaoz").setHeader("Ʊ��");
		egu1.getColumn("biaoz").setWidth(50);
		egu1.getColumn("biaoz").setEditor(null);
		egu1.getColumn("qitysdwb_id").setHeader("���䵥λ");
		egu1.getColumn("qitysdwb_id").setWidth(70);
		egu1.getColumn("qitysdwb_id").setEditor(null);
		
		egu1.getColumn("koud").setHeader("�۶�");
		egu1.getColumn("koud").setWidth(50);
		 
		
		egu1.getColumn("qingcjjy").setHeader("�ᳵ���Ա");
		egu1.getColumn("qingcjjy").setWidth(80);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("qingchh").setHeader("�ᳵ���");
		egu1.getColumn("qingchh").setWidth(70);
		egu1.getColumn("qingchh").setEditor(null);
		
//		egu1.getColumn("piz").setEditor(null);
		
		egu1.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu1.getColumn("beiz").setWidth(100);
		egu1.getColumn("beiz").setEditor(null);
		egu1.addToolbarButton(GridButton.ButtonType_Save, "SavePizButton");
		egu1.addToolbarButton("������ʷƤ��",GridButton.ButtonType_Save, "AutoSaveButton", null, SysConstant.Btn_Icon_Save);
//		��ӡ��ť

		GridButton gbp = new GridButton("��ӡ","function (){var Mrcd = gridDivPiz_grid.getSelectionModel().getSelections();if(Mrcd.length == 0){Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ������');return;}" +
				"else{var Mrcd = gridDivPiz_grid.getSelectionModel().getSelections();" +
				"for(i=0;i<Mrcd.length;i++)" +
				"document.getElementById('CHANGE').value=Mrcd[i].get('ID');" +
				"}" +
				"document.getElementById('DayButton').click();"
				+MainGlobal.getOpenWinScript("Qicjjd_qith")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu1.addTbarBtn(gbp);
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		setPizGrid(egu1);
		rslg.close();
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
	
	public String getGridScriptPiz() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getPTbmsg()!=null) {
			getPizGrid().addToolbarItem("'->'");
			getPizGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getPTbmsg()+"</marquee>'");
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getGridHtmlPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
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
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
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
		setGongysModels();
		//setMeikModels();
		setExtGrid(null);
		setPizGrid(null);
		//setDefaultTree(null);
		getSelectData();
	}
}