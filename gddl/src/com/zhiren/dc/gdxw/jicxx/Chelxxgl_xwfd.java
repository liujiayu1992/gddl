package com.zhiren.dc.gdxw.jicxx;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*
 * ����:���ܱ�
 * ����:2010-11-25 9:36:43
 * ����:����汾�ĳ�����Ϣ����.û��ɾ����ť,���ܸ��ĳ����Ƿ�������״̬.
 * 
 * 
 */
/*
 * ���ߣ�songy
 * ʱ�䣺2011-03-23 
 * �������޸������˵�������Ҫ�������ƽ�������
 */
public class Chelxxgl_xwfd extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String CustomSetKey = "Chelxxgl_xwfd";
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String panduancheh ="";
		
		
		
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			String diancxxb_id=mdrsl.getString("diancxxb_id");
			String yunsdwb_id="select max(id) from yunsdwb where mingc='"+mdrsl.getString("yunsdwb_id")+"'";
			String yunsfsb_id="select max(id) from yunsfsb where mingc='"+mdrsl.getString("yunsfsb_id")+"'";
			String cheph=mdrsl.getString("cheph");
			String maoz=mdrsl.getString("maoz");
			String piz=mdrsl.getString("piz");
			String kah=mdrsl.getString("kah");
			String chang=mdrsl.getString("chang");
			String kuan=mdrsl.getString("kuan");
			String gao=mdrsl.getString("gao");
			String dig=mdrsl.getString("dig");
			String gangssl=mdrsl.getString("gangssl");
			String chex="select max(id) from item where mingc='"+mdrsl.getString("chex")+"'";
			String islocked=mdrsl.getString("islocked");
			String chezxm=mdrsl.getString("chezxm");
			String ched_id="select max(id) from chedxxb where chedjc='"+mdrsl.getString("ched_id")+"'";
			String suocr=" ";
			String suocsj=" ";
			
			if("ͣ��".equals(islocked)){
				suocr=visit.getRenymc();
				//suocsj=DateUtil.FormatOracleDate(DateUtil.FormatDate(new Date()));
				suocsj=DateUtil.FormatOracleDate(new Date());
				islocked="1";
			}else{
				suocr="";
				suocsj="''";
				islocked="0";
			}
	
		   
			if ("".equals(mdrsl.getString("id"))||mdrsl.getString("id")==null ||mdrsl.getInt("id")==0) {
				sbsql.append("insert into chelxxb(id,diancxxb_id,yunsdwb_id,yunsfsb_id,cheph,maoz,piz,islocked,kah,chezxm,ched_id,suocr,suocsj,chang,kuan,gao,dig,gangssl,chex,lury,lursj) values( xl_xul_id.nextval")
				.append(",").append(diancxxb_id).append(", (").append(yunsdwb_id).append("),(").append(yunsfsb_id).append("), '").append(cheph).append("', '").append(maoz).
				append("', '").append(piz).append("', '").append(islocked).append("', '").append(kah).append("', '").append(chezxm).append("', (").append(ched_id).append("), '").append(suocr).
				append("', ").append(suocsj).append(",").append(chang).append(",").append(kuan).append(",").append(gao).append(",").append(dig).append(",").append(gangssl).append(",(").append(chex).append(")" +
						" ,'"+visit.getRenymc()+"',sysdate );\n");
				
				//�жϲ���ʱ�Ƿ���ڳ����ظ�,�����,����
				String sql_check="select * from chelxxb where cheph='"+cheph+"'";
					if (this.Shujpd(con, sql_check) != 0) { 
						this.setMsg("����ʧ��, ����: "+cheph+"  �����ݿ����Ѿ�����,�������ظ����!");
						return;
						
					}
				
			} else {
				
				this.setMsg("��ʾ,����Ȩ���޸�����!!");
				return;
				
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	



	public void SaveLog(JDBCcon con,String zhuangt,String cheph){
		Visit visit = (Visit) this.getPage().getVisit();
		String suocSql="";
		String sql="select c.id,c.jiecr from chelxxlogb c where c.cheph='"+cheph+"' ";
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.next()){
			String jiesry=rs.getString("jiecr");
			if(jiesry.equals("")||jiesry==null){
				suocSql="update chelxxlogb c set c.jiecr='"+visit.getRenymc()+"',jiecsj=sysdate where id="+rs.getLong("id")+"";
			}else{
				suocSql="update chelxxlogb c set c.suocr='"+visit.getRenymc()+"',suocsj=sysdate,jiecr=null,jiecsj=null where id="+rs.getLong("id")+"";
			}
			
		}else if(zhuangt.equals("1")){
			suocSql="insert into chelxxlogb (id,cheph,suocr,suocsj,jiecr,jiecsj) values (" +
					"xl_xul_id.nextval,'"+cheph+"','"+visit.getRenymc()+"',sysdate,null,null)";
		}
		if(suocSql.length()>10){
			con.getUpdate(suocSql);
		}
		
		
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		if("".equals(getChehao())){
			sb.append("SELECT c.id,d.id as diancxxb_id,yd.mingc as yunsdwb_id,yf.mingc as yunsfsb_id, \n");
			sb.append(" cd.chedjc as ched_id,c.cheph,c.chezxm,c.maoz,c.piz,c.chang,c.kuan,c.gao,c.dig,ii.mingc as chex  ,c.gangssl,decode(c.islocked,0,'ʹ��','ͣ��') as islocked, c.suocr,c.suocsj,c.kah    \n");
			sb.append(" FROM chelxxb c, diancxxb d, yunsdwb yd,yunsfsb yf,chedxxb cd,item ii \n");
			sb.append(" WHERE c.diancxxb_id=d.id\n");
			sb.append(" AND c.yunsdwb_id=yd.id(+)\n");
			sb.append(" AND c.yunsfsb_id=yf.id(+)  and c.ched_id=cd.id(+) and c.chex=ii.id(+)\n");
			sb.append(" AND d.ID = ").append(visit.getDiancxxb_id()).append(" order by yd.mingc, c.cheph");
		}else{
			sb.append("SELECT c.id,d.id as diancxxb_id,yd.mingc as yunsdwb_id,yf.mingc as yunsfsb_id, \n");
			sb.append(" cd.chedjc as ched_id,c.cheph,c.chezxm,c.maoz,c.piz,c.chang,c.kuan,c.gao,c.dig,ii.mingc as chex,c.gangssl,decode(c.islocked,0,'ʹ��','ͣ��') as islocked,c.suocr,c.suocsj ,c.kah   \n");
			sb.append(" FROM chelxxb c, diancxxb d, yunsdwb yd,yunsfsb yf,chedxxb cd,item ii  \n");
			sb.append(" WHERE c.diancxxb_id=d.id\n");
			sb.append(" AND c.yunsdwb_id=yd.id(+)\n");
			sb.append(" AND c.yunsfsb_id=yf.id(+)  and c.ched_id=cd.id(+)  and c.chex=ii.id(+) \n");
			sb.append(" AND c.cheph like '%"+this.getChehao()+"%'\n");
			sb.append(" AND d.ID = ").append(visit.getDiancxxb_id()).append(" order by yd.mingc,c.cheph");
		}
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setTableName("chelxxb");
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
			egu.getColumn("diancxxb_id").editor = null;
			egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		}
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("chang").setHeader("���᳧(����)");
		egu.getColumn("kuan").setHeader("�����(����)");
		egu.getColumn("gao").setHeader("�����(����)");
		egu.getColumn("dig").setHeader("�����ظ�(����)");
		egu.getColumn("chex").setHeader("����");
		egu.getColumn("gangssl").setHeader("��������");
		egu.getColumn("kah").setHeader("����");
		egu.getColumn("ched_id").setHeader("��������");
		
		egu.getColumn("chezxm").setHeader("��������");
		egu.getColumn("islocked").setHeader(Locale.Pidc_zhuangt);
		
		egu.getColumn("suocr").setHeader("������");
		egu.getColumn("suocr").setEditor(null);
		egu.getColumn("suocsj").setHeader("����ʱ��");
		egu.getColumn("suocsj").setEditor(null);
		egu.getColumn("yunsfsb_id").setDefaultValue("��·");
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("chang").setDefaultValue("0");
		egu.getColumn("kuan").setDefaultValue("0");
		egu.getColumn("gao").setDefaultValue("0");
		egu.getColumn("dig").setDefaultValue("0");
		egu.getColumn("gangssl").setDefaultValue("0");
		//����Grid����
		egu.addPaging(18);
		// �������䵥λ������
		ComboBox c1= new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(c1);
		c1.setEditable(true);
		String ydSql="select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(ydSql));
		// �������䷽ʽ������
		ComboBox c2= new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c2);
		c2.setEditable(true);
		String yfSql="select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yfSql));
		
//		 ����������
		ComboBox cb_chex= new ComboBox();
		egu.getColumn("chex").setEditor(cb_chex);
		cb_chex.setEditable(true);
		egu.getColumn("chex").editor.setAllowBlank(true);
		String cb_chexSql="select i.id,i.mingc from item i where i.bianm='CHEX' and zhuangt=1 order by mingc ";
		egu.getColumn("chex").setComboEditor(egu.gridId, new IDropDownModel(cb_chexSql));
		
		//״̬:0��ʹ��,1��ͣ��
		List sfss = new ArrayList();
		sfss.add(new IDropDownBean(0, "ʹ��"));
		sfss.add(new IDropDownBean(1, "ͣ��"));
		egu.getColumn("islocked").setEditor(new ComboBox());
		egu.getColumn("islocked").setDefaultValue("ʹ��");
		egu.getColumn("islocked").setEditor(null);
		
		egu.getColumn("islocked").setComboEditor(egu.gridId, new IDropDownModel(sfss));
		egu.getColumn("islocked").setReturnId(true);
		
		
		ComboBox c3= new ComboBox();
		egu.getColumn("ched_id").setEditor(c3);
		c3.setEditable(true);
		String ched="select id,chedjc from chedxxb order by xuh";
		egu.getColumn("ched_id").setComboEditor(egu.gridId, new IDropDownModel(ched));
		egu.getColumn("ched_id").editor.setAllowBlank(true);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
//        "var end = url.indexOf(';');"+
//        "url = url.substring(0,end);"+
//   	    "url = url + '?service=page/' + 'Chelxxreport&lx=rezc';" +
//   	    " window.open(url,'newWin');";
//		GridButton print = new GridButton("��ӡ","function (){"+str+"}");
//		print.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(print);
		
		
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("���Ų���:");
		TextField tf=new TextField();
		tf.setWidth(80);
		tf.setValue(getChehao());
		
		tf.setListeners("change:function(own,n,o){document.getElementById('Chehao').value = n}");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
	
	
//	 ˢ�°�ť
	GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
			"gridDiv", egu.getGridColumns(), "RefurbishButton");
	egu.addTbarBtn(refurbish);
	egu.addTbarText("-");

		setExtGrid(egu);
		con.Close();
	}

	
	private String chehao = "";
	public String getChehao(){
		return chehao;
	}
	public void setChehao(String ch){
		chehao = ch;
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}

	// �ж������Ƿ��ڱ��ؿ����Ѿ�����(�����ڷ���0�����ڷ�������)
	private int Shujpd(JDBCcon con, String sql) {
		return JDBCcon.getRow(con.getResultSet(sql));
	}
}
