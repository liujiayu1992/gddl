package com.zhiren.jt.zdt.monthreport.niandhtqk;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * ��������ͬ������Ϊֻȡú�������Ϊ�����±��еĵ����ʣ�
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-20
 * �����������������
 *      ���䷽ʽ��������վǰ
 *      ���ⵥλ������ɲ�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-12
 * �������޸ĸ�������ƻ��Ĺ���
 * 		����������ʾ����
 * 		����ú��λ�ֶ�
 */

public  class Niandhtqk extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		ResultSetList rsl;
		rsl=visit.getExtGrid1().getModifyResultSet(getChange());

		double htl=0;
		String hangid="";
		String strDate="";
		String strYear="";
		String strYearEnd="";
		//������»�����
		strYear=getNianfValue().getValue() +"-01-01";
		strYearEnd=getNianfValue().getValue() +"-12-01";
		
		while (rsl.next()){
			double rez=rsl.getDouble("rez");
			double huiff=rsl.getDouble("huiff");
			double liuf=rsl.getDouble("liuf");
			double chebjg=rsl.getDouble("chebjg");
			double yunf=rsl.getDouble("yunf");
			double zaf=rsl.getDouble("zaf");
			
			long diancxxb_id=(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			long gongysb_id=(getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl.getString("gongysb_id"));
			long meikxxb_id=(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id"));
			long jihkjb_id=(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id"));
			long pinzb_id=(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"));
			long faz_id=(getExtGrid().getColumn("faz_id").combo).getBeanId(rsl.getString("faz_id"));
			long daoz_id=(getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id"));
			long yunsfs=(getExtGrid().getColumn("yunsfs").combo).getBeanId(rsl.getString("yunsfs"));
			
			
			if(!"0".equals(rsl.getString("id"))){				
				String cc ="delete from (select id from niandhtqkb " +
				"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
				"and HANGID="+rsl.getString("HANGID")+" \n"+
				"and DIANCXXB_ID = "+diancxxb_id+")";
				
				flag=con.getDelete(cc);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ cc);
					setMsg(ErrorMessage.DeleteDatabaseFail);
					return;
				}
		   }
			hangid=MainGlobal.getNewID(100);//��ID
			for (int i=1;i<=12;i++){
				htl=rsl.getDouble("Y"+i);
				strDate=getNianfValue().getValue() +"-"+ i +"-01";
				String bb="insert into niandhtqkb(id,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,beiz,hej,riq,yunsfsb_id,REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,hangid) values(" +
				"getNewId("+diancxxb_id+"),"+
				diancxxb_id+","+
				gongysb_id+","+
				meikxxb_id+","+
				jihkjb_id+","+
				pinzb_id+","+
				faz_id+","+
				daoz_id+","+
				"'"+rsl.getString("beiz")+"',"+
				htl+"," +
				"to_date('"+strDate+"','yyyy-mm-dd'),"+
				yunsfs+","+rez+","+huiff+","+liuf+","+chebjg+","+yunf+","+zaf+","+hangid+")";

				flag=con.getInsert(bb);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"	+ bb);
					setMsg(ErrorMessage.InsertDatabaseFail);
					return;
				}
			}
		}
		//ɾ������
		rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			String cc ="delete from (select id from niandhtqkb " +
			"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
			"and HANGID="+rsl.getString("HANGID")+" \n"+
			"and DIANCXXB_ID = "+this.getTreeid()+")";
			
			flag=con.getDelete(cc);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"	+ cc);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
		}
		if(flag!=-1){
			con.commit();
			con.Close();
			setMsg(ErrorMessage.SaveSuccessMessage);
		}else{
		setMsg("����ʧ�ܣ�");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYearData();
		}
	}
	
	public void CoypLastYearData(){
		JDBCcon con = new JDBCcon();
//		 ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long Lstyear;
		Lstyear=intyear-1;
//		������һ��ƻ�
		String copyData =
			"INSERT INTO NIANDHTQKB\n" +
			"(ID,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,JIHKJB_ID,PINZB_ID,FAZ_ID,DAOZ_ID,BEIZ,HEJ,RIQ,YUNSFSB_ID," +
			"REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,HANGID)\n" + 
			"(SELECT GETNEWID(100) ID,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,JIHKJB_ID,PINZB_ID,FAZ_ID,DAOZ_ID,BEIZ,HEJ," +
			"ADD_MONTHS(RIQ, 12) RIQ,YUNSFSB_ID,\n" + 
			"REZ,HUIFF,LIUF,CHEBJG,YUNF,ZAF,HANGID FROM NIANDHTQKB WHERE DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+Lstyear+"')";
		con.getInsert(copyData);
		
//		������ID
		String upd_Hangid="SELECT DISTINCT HANGID FROM NIANDHTQKB WHERE DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+intyear+"'";
		ResultSetList rsl=con.getResultSetList(upd_Hangid);
		String upd="";
		while (rsl.next()){
			String hangid=MainGlobal.getNewID(100);//��ID
			upd+="UPDATE NIANDHTQKB SET HANGID="+hangid+" WHERE HANGID="+rsl.getString("HANGID")+" AND DIANCXXB_ID = "+this.getTreeid()+" AND TO_CHAR(RIQ, 'yyyy') = '"+intyear+"';\n";
			
		}
		if(upd.length()>10){
			con.getUpdate("begin \n"+upd+"end;");
		}
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
//		 �����������������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
	
		String strdiancTreeID = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = " and dc.fuid= " +this.getTreeid();
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and dc.id= " +this.getTreeid();
		}
		
		String chaxun = "select min(n.id) as id,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       mk.mingc as meikxxb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       p.mingc as pinzb_id,\n"
				+"        y.mingc as yunsfs,\n"
				+ "       c.mingc as faz_id,\n"
				+ "       c1.mingc as daoz_id,\n"
				+ "       max(round_new(rez,2)) as rez,\n "
				+ "       max(round_new(huiff,2)) as huiff,\n"
				+ "       max(round_new(liuf,2)) as liuf,\n"
				+ "       max(round_new(chebjg,2)) as chebjg,\n" 
				+ "       max(round_new(yunf,2)) as yunf,\n"
				+ "       max(round_new(zaf,2)) as zaf,\n"
				+ "       round_new(max(round_new(chebjg,2))+max(round_new(yunf,2))+max(round_new(zaf,2)),2) as daocj,"
				+ "       decode(max(rez),0,0,round_new((max(chebjg)/1.17+max(yunf)*(1-0.07)+max(zaf))*29.271/max(rez),2)) as biaomdj,"
				+ "       sum(decode(to_char(riq,'mm'),'01',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'02',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'03',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'04',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'05',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'06',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'07',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'08',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'09',nvl( n.hej,0),0)+\n"
				+ "       decode(to_char(riq,'mm'),'10',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'11',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'12',nvl( n.hej,0),0)) as quann,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '01', nvl(n.hej, 0), 0)) as y1,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '02', nvl(n.hej, 0), 0)) as y2,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '03', nvl(n.hej, 0), 0)) as y3,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '04', nvl(n.hej, 0), 0)) as y4,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '05', nvl(n.hej, 0), 0)) as y5,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '06', nvl(n.hej, 0), 0)) as y6,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '07', nvl(n.hej, 0), 0)) as y7,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '08', nvl(n.hej, 0), 0)) as y8,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '09', nvl(n.hej, 0), 0)) as y9,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '10', nvl(n.hej, 0), 0)) as y10,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '11', nvl(n.hej, 0), 0)) as y11,\n"
				+ "       sum(decode(to_char(riq, 'mm'), '12', nvl(n.hej, 0), 0)) as y12,\n"
				+ "       max(n.beiz) as beiz, n.hangid\n"
				+ "  from niandhtqkb n,diancxxb dc,gongysb g,jihkjb j,chezxxb c,chezxxb c1,yunsfsb y,PINZB p,MEIKXXB mk\n"
				+ " where n.diancxxb_id = dc.id(+)\n"
				+"    and n.yunsfsb_id=y.id(+)\n"
				+ "   and n.gongysb_id = g.id(+)\n"
				+ "   and n.jihkjb_id = j.id(+)\n"
				+ "   and n.faz_id = c.id(+)\n"
				+ "   and n.meikxxb_id = mk.id(+)\n"
				+ "   and n.daoz_id = c1.id(+) and n.pinzb_id=p.id(+)  "+strdiancTreeID +"\n"
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'\n"
				+ "   group by (dc.mingc,g.mingc,mk.mingc, j.mingc, p.mingc, c.mingc,c1.mingc,y.mingc,n.hangid)"
		        + "   order by dc.mingc,g.mingc";

		ResultSetList rsl = con.getResultSetList(chaxun);
//		�Ƿ���ʾ���ư�ť
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("niandhtqkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("gongysb_id").setCenterHeader("��Ӧ��");
		
		egu.getColumn("MEIKXXB_ID").setCenterHeader("ú��λ");
		
		egu.getColumn("diancxxb_id").setCenterHeader("��λ");
		
		egu.getColumn("quann").setCenterHeader("ȫ��ϼ�<br>(���)");
		
		egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
		
		egu.getColumn("pinzb_id").setCenterHeader("Ʒ��");
		
		egu.getColumn("yunsfs").setCenterHeader("���䷽ʽ");
		
		egu.getColumn("faz_id").setCenterHeader("��վ");
		
		egu.getColumn("daoz_id").setCenterHeader("��վ");
		
		egu.getColumn("rez").setCenterHeader("��ֵ<br>(MJ/Kg)");
		//egu.getColumn("rez").setHidden(true);
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		
		egu.getColumn("huiff").setCenterHeader("�ӷ���<br>(%)");
		//egu.getColumn("huiff").setHidden(true);
		((NumberField)egu.getColumn("huiff").editor).setDecimalPrecision(2);
		
		egu.getColumn("liuf").setCenterHeader("���<br>(%)");
		//egu.getColumn("liuf").setHidden(true);
		((NumberField)egu.getColumn("liuf").editor).setDecimalPrecision(2);
		
		egu.getColumn("chebjg").setCenterHeader("�����<br>(Ԫ/��)");
		((NumberField)egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		
		egu.getColumn("daocj").setCenterHeader("������<br>(Ԫ/��)");
		
		egu.getColumn("biaomdj").setCenterHeader("��ú����<br>(Ԫ/��)");
		
		egu.getColumn("yunf").setCenterHeader("�˷�<br>(Ԫ/��)");
		((NumberField)egu.getColumn("yunf").editor).setDecimalPrecision(2);
		
		egu.getColumn("zaf").setCenterHeader("�ӷ�<br>(Ԫ/��)");
		((NumberField)egu.getColumn("zaf").editor).setDecimalPrecision(2);
		
		egu.getColumn("y1").setCenterHeader("һ��<br>(���)");
		
		egu.getColumn("y2").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y3").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y4").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y5").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y6").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y7").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y8").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y9").setCenterHeader("����<br>(���)");
		
		egu.getColumn("y10").setCenterHeader("ʮ��<br>(���)");
		
		egu.getColumn("y11").setCenterHeader("ʮһ��<br>(���)");
		
		egu.getColumn("y12").setCenterHeader("ʮ����<br>(���)");
		
		egu.getColumn("beiz").setCenterHeader("��ע");
		
		egu.getColumn("hangid").setHidden(true);
		
		egu.getColumn("hangid").setEditor(null);
		egu.getColumn("quann").setEditor(null);
		egu.getColumn("daocj").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		
		//ѭ���趨�еĿ��,���趨С��λ��
		for( int i=1;i<=12;i++){
			egu.getColumn("y"+i).setWidth(45);
			egu.getColumn("y"+i).setDefaultValue("0");
			((NumberField)egu.getColumn("y"+i).editor).setDecimalPrecision(4);
		}
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("gongysb_id").setWidth(130);
		egu.getColumn("meikxxb_id").setWidth(130);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfs").setWidth(60);
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("quann").setWidth(65);
		egu.getColumn("beiz").setWidth(65);
		egu.getColumn("rez").setWidth(45);
		egu.getColumn("huiff").setWidth(45);
		egu.getColumn("liuf").setWidth(45);
		egu.getColumn("chebjg").setWidth(45);
		egu.getColumn("yunf").setWidth(45);
		egu.getColumn("zaf").setWidth(45);
		egu.getColumn("daocj").setWidth(45);
		egu.getColumn("biaomdj").setWidth(55);
		
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("quann").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("daocj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("biaomdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(23);// ���÷�ҳ

		// *****************************************����Ĭ��ֵ****************************
		
//		�糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib2 == 3) {// ѡ�糧ֻˢ�³��õ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		}		
		
		// *************************������*****************************************88
		// ���ù�Ӧ�̵�������
		ComboBox cb_gongys=new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		String GongysSql = "select id, mingc from gongysb where leix = 1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		
//		 ����ú���������
		ComboBox cb_meik=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String cb_meikSql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(cb_meikSql));
		
//		���üƻ��ھ�������
		ComboBox cb_jihkj=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(cb_jihkj);
		cb_jihkj.setEditable(true);
		egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
		String jihkjSql="select j.id,j.mingc from jihkjb j order by id  ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
		egu.getColumn("jihkjb_id").setDefaultValue("�ص㶩��");
		
//		����Ʒ��������
		ComboBox cb_pinz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cb_pinz);
		cb_pinz.setEditable(true);
		egu.getColumn("pinzb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
		String pinzSql="select id,mingc from pinzb order by id ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
//		egu.getColumn("pinzb_id").setDefaultValue("ԭú");
		
//      ���䷽ʽ������
		ComboBox cb_yunsfs=new ComboBox();
		egu.getColumn("yunsfs").setEditor(cb_yunsfs);
		cb_yunsfs.setEditable(true);
		egu.getColumn("yunsfs").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
		String yunsfsSql="select j.id,j.mingc from yunsfsb j order by id  ";
		egu.getColumn("yunsfs").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfs").setDefaultValue("��·");
//		���÷�վ������
		ComboBox cb_faz=new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql="select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
		//���õ�վ������
		ComboBox cb_daoz=new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);
	
		String daozSql="select id,mingc from chezxxb c where c.leib='��վ' order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
		
		

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// ������
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		

//		 �趨�������������Զ�ˢ��
//		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		if (showBtn) {
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("������������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			sb.append("e.record.set('DAOCJ',Round(parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2));\n"
					+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(Round((parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))/1.17+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*(1-0.07)+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')))*29.271/parseFloat(e.record.get('REZ')==''?0:e.record.get('REZ')),2),2));}\n"
					+ "\n");
			sb.append("if(!(e.field == 'DIANCXXB_ID'||e.field == 'GONGYSB_ID'||e.field == 'JIHKJB_ID'||e.field == 'FAZ_ID'||e.field == 'DAOZ_ID')){e.record.set('QUANN',parseFloat(e.record.get('Y1')==''?0:e.record.get('Y1'))+parseFloat(e.record.get('Y2')==''?0:e.record.get('Y2'))+parseFloat(e.record.get('Y3')==''?0:e.record.get('Y3'))+parseFloat(e.record.get('Y4')==''?0:e.record.get('Y4'))" +
					" +parseFloat(e.record.get('Y5')==''?0:e.record.get('Y5'))+parseFloat(e.record.get('Y6')==''?0:e.record.get('Y6'))+parseFloat(e.record.get('Y7')==''?0:e.record.get('Y7'))+parseFloat(e.record.get('Y8')==''?0:e.record.get('Y8'))+parseFloat(e.record.get('Y9')==''?0:e.record.get('Y9'))" +
					"  +parseFloat(e.record.get('Y10')==''?0:e.record.get('Y10'))+parseFloat(e.record.get('Y11')==''?0:e.record.get('Y11'))+parseFloat(e.record.get('Y12')==''?0:e.record.get('Y12')) )};");
			
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=200 scrollamount=2>" + getTbmsg()+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			setTbmsg(null);
		}
			getSelectData();
	}
	
//	 ���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
//	private String treeid;
	
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
}