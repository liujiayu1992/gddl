package com.zhiren.dc.jilgl.tiel.duih;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
/*
 * ����:tzf
 * ʱ��:2009-05-26
 * ����:������Ƥ�ص�ʱ�����Ӱ������������빻3����λʱ���Զ�����С���㣬���س�����
 * ��ת����һ�е�Ƥ���ֶν��б༭
 */
/*
 * 2009-04-17
 * ����
 * �����ַ�����ԭ����"��������"���䣬ԭ���������ڹ̶��仯��
 */
/*
 * 2009-04-17
 * ����
 * ��Ϊδ��ȫ��Ｔ��ַ���
 */
public class Duih extends BasePage {
//	�����û���ʾ
	
	private final static String XTSZ="���ź˶ԶԺ�С���Զ�";
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
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	ȡ�úⵥ������
	public IDropDownBean getHengdValue() {
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}
	
//	���úⵥ������
	public void setHengdValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}
//	�����������model
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}

//	ȡ����ѡ��ĳ�Ƥ
	public List getChepSelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}
//	������ѡ��ĳ�Ƥ��Ϣ
	public void setChepSelected(List chep) {
		((Visit) getPage().getVisit()).setList1(chep);
	}
//	���ó�Ƥmodel
	public void setChepModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
//	ȡ�úⵥList��¼
	public List getHengdList() {
		return ((Visit) this.getPage().getVisit()).getList2();
	}

//	ȡ�úⵥ�Ժ�ģʽ
	public int getHengdType() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

//	���ù�����Ϣlist
	public void setHengdList() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id, c.cheph, decode(c.maoz,0,nvl(l.maoz,0),c.maoz) maoz, \n")
		.append("decode(c.piz,0,nvl(l.piz,0),c.piz) piz, c.biaoz, c.ches, c.daozch, c.koud, c.beiz \n")
		.append("from chepb c,(select * from chelxxb where yunsfsb_id = ")
		.append(SysConstant.YUNSFS_HUOY).append(") l \n")
		.append("where hedbz=").append(SysConstant.HEDBZ_TJ).append(" and c.cheph = l.cheph(+)");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		for (int i = 0; i < getHengdList().size(); i++) {
			Guohxx gh = ((Guohxx) getHengdList().get(i));
			String cheph = "";
			long id = 0;
			if (getHengdType() < 3) {
				cheph = gh._cheph;
			}else {
				if(getChepSelected() != null) {
					cheph = 
						((IDropDownBean)getChepSelected().get(i))
						.getValue().split(",")[0];
					id = ((IDropDownBean)getChepSelected().get(i)).getId();
				}
			}
			rsl.beforefirst();
			while (rsl.next()) {
				if ((id ==0 || (id !=0 && id == rsl.getLong("id"))) && cheph.equals(rsl.getString("cheph"))) {
					gh._id = rsl.getLong("id");
					gh._cheph = cheph;
					if (getHengdType() == SysConstant.Hengd_Auto_maoz || getHengdType() == SysConstant.Hengd_Manual_maoz) {
						gh._piz = rsl.getString("piz");
					} else if (getHengdType() == SysConstant.Hengd_Auto_piz || getHengdType() == SysConstant.Hengd_Manual_piz) {
						gh._maoz = rsl.getString("maoz");
					}
					gh._biaoz = rsl.getString("biaoz");
					gh._daozch = rsl.getString("daozch");
					gh._beiz = rsl.getString("beiz");
					gh._koud = rsl.getString("koud");
					rsl.Remove(rsl.getRow());
					break;
				}
			}
		}
	}

	private boolean getXtsz(){
		JDBCcon con=new JDBCcon();
		String sql=" select zhi from xitxxb where mingc='"+XTSZ+"' and zhuangt=1 and leib='����'";
		ResultSetList rsl=con.getResultSetList(sql);
		
		boolean t=false;
		
		if(rsl.next()){
			
			if(rsl.getString("ZHI").equals("��")){
				t=true;
			}
		}
		
		return t;
		
	}
//	ˢ�³�Ƥ�ⵥ��Ӧ�б�
	public void getSelectData() {
		setHengdList();
		ExtGridUtil egu = new ExtGridUtil("gridDiv");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addColumn(new GridColumn(GridColumn.ColType_Rownum));
		GridColumn gcid = new GridColumn(0, "ID", "ID", 70);
		gcid.setDataType(GridColumn.DataType_String);
		gcid.setUpdate(true);
		gcid.setHidden(true);
		egu.addColumn(gcid);
		TextField tf = new TextField();
		tf.allowBlank = true;
		GridColumn gccph = new GridColumn(0, "CHEPH", "��Ƥ��", 70);
		gccph.setUpdate(true);
		gccph.setDataType(GridColumn.DataType_String);
		gccph.setEditor(tf);
		egu.addColumn(gccph);
		
		GridColumn gcmz = new GridColumn(0, "MAOZ", "ë��", 70);
		gcmz.setUpdate(true);
		gcmz.setDataType(GridColumn.DataType_Float);
		egu.addColumn(gcmz);
		
		GridColumn gcpz = new GridColumn(0, "PIZ", "Ƥ��", 70);
		if(getHengdType() == SysConstant.Hengd_Auto_maoz || getHengdType() == SysConstant.Hengd_Manual_maoz) {
			NumberField nfpz = new NumberField();
			nfpz.setAllowBlank(false);
			nfpz.setDecimalPrecision(3);
			gcpz.setEditor(nfpz);
		}
		gcpz.setUpdate(true);
		gcpz.setDataType(GridColumn.DataType_Float);
		egu.addColumn(gcpz);
		
		GridColumn gcbz = new GridColumn(0, "BIAOZ", "Ʊ��", 70);
		gcbz.setUpdate(false);
		gcbz.setDataType(GridColumn.DataType_Float);
		egu.addColumn(gcbz);
		
		GridColumn gcsd = new GridColumn(0, "CHES", "�ٶ�", 70);
		gcsd.setUpdate(true);
		gcsd.setDataType(GridColumn.DataType_Float);
		egu.addColumn(gcsd);
		
		GridColumn koud = new GridColumn(0, "KOUD", "�۶�", 70);
		NumberField nfkd = new NumberField();
		nfkd.setAllowBlank(false);
		nfkd.setDecimalPrecision(3);
		koud.setEditor(nfkd);
		koud.setUpdate(true);
		koud.setDataType(GridColumn.DataType_Float);
		egu.addColumn(koud);
		
		GridColumn gcdzch = new GridColumn(0, "DAOZCH", "��װ����", 100);
		gcdzch.setUpdate(true);
		gcdzch.setDataType(GridColumn.DataType_String);
		gcdzch.setEditor(tf);
		egu.addColumn(gcdzch);
		
		GridColumn gcbeiz = new GridColumn(0, "BEIZ", "��ע", 100);
		gcbeiz.setUpdate(true);
		gcbeiz.setDataType(GridColumn.DataType_String);
		gcbeiz.setEditor(tf);
		egu.addColumn(gcbeiz);

		egu.addPaging(0);
		List list = getHengdList();
		String[][] data = new String[list.size()][9];
		for(int i=0 ; i<list.size() ; i++) {
			long id = ((Guohxx)list.get(i))._id;
			data[i][0] = String.valueOf(id==0?-1:id);
			data[i][1] = getString(((Guohxx)list.get(i))._cheph);
			data[i][2] = getString(((Guohxx)list.get(i))._maoz);
			data[i][3] = getString(((Guohxx)list.get(i))._piz);
			data[i][4] = getString(((Guohxx)list.get(i))._biaoz);
			data[i][5] = getString(((Guohxx)list.get(i))._sud);
			data[i][6] = getString(((Guohxx)list.get(i))._koud);
			data[i][7] = getString(((Guohxx)list.get(i))._daozch);
			data[i][8] = getString(((Guohxx)list.get(i))._beiz);
		}
		egu.setData(data);
		
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		setNoFound(0);
		StringBuffer NoFoundCheph = new StringBuffer();
		for(int i =0;i<data.length;i++) {
			if("-1".equals(data[i][0])) {
				setNoFound(getNoFound()+1);
				NoFoundCheph.append(data[i][1]).append("��");
			}
		}
		if(getNoFound()>0) {
			setMsg("�� "+getNoFound()+" �� ����δƥ�䣡���� "+NoFoundCheph.deleteCharAt(NoFoundCheph.length()-1));
		}else {
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		}
		GridButton btnreturn = new GridButton("����","function(){document.getElementById('ReturnButton').click();}" );
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
		
		if(egu.getColumn("PIZ").editor!=null){
			egu.getColumn("PIZ").editor.setListeners("specialkey:function(own,e){\n" +
					"if(gridDiv_grid.getStore().getCount()!=row_index+1){\n" +
					"gridDiv_grid.startEditing(row_index+1 , col_index);" +
					"}\n" +
					"}");
		
		
		
		boolean t=this.getXtsz();
		if(t){
			egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){\n" +
					"var rc=e.record;\n" +
					"if( rc.get('PIZ')==null || (rc.get('PIZ')+'').length<=0){rc.set('PIZ','0.0');return;}\n"+
					"if( (rc.get('PIZ')+'').length==1){rc.set('PIZ',rc.get('PIZ')+'.'+'0');return;}\n"+
					"if((rc.get('PIZ')+'').length>=2 && (rc.get('PIZ')+'').indexOf('.')==-1){\n" +
					" var x_va='';\n"+
					"if((rc.get('PIZ')+'').length==2){\n"+
					"x_va=rc.get('PIZ')+'.'+'0';\n"+
					"}"+
					"else { x_va=(rc.get('PIZ')+'').substring(0,2)+'.'+(rc.get('PIZ')+'').substring(2,3);}\n" +
					"rc.set('PIZ',x_va);\n"+
					"}\n" +
					"});");
			
		}else{
			
		}
		
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){\n" +
				"row_index=e.row;\n" +
				"col_index=e.column;" +
				"});");
		
		}
		setExtGrid(egu);
	}
	
	public String getString(String a) {
		return a==null?"":a;
	}
	public int getNoFound() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	public void setNoFound(int nofound) {
		((Visit) this.getPage().getVisit()).setInt1(nofound);
	}

//	���ʹ�õķ���
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
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	private void DeleteXuh(){//ȥ�� ��ѡ�� �е� ���  �� 
		
		
		Visit visit=(Visit)this.getPage().getVisit();
		
		
		visit.setList6(new ArrayList());
		
		for(int i=0;i<getChepSelected().size();i++){
			IDropDownBean be=(IDropDownBean)getChepSelected().get(i);
			
			String[] value=be.getValue().split("->");
			
			if(value.length>1){
				visit.setboolean6(true);//�ȴ��������
				visit.getList6().add(value[0]);
				be.setValue(value[1]);
			}
			
			
		}
		
	
	}


//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		setTbmsg(null);
		DeleteXuh();
		getSelectData();
	}

//	��ť�ļ����¼�
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private String fn2time(String fileName) {
		if (fileName == null || "".equals(fileName)) {
			return "";
		}
		String StrName = fileName;
		if (StrName.length() >= 14) {
			String strYear = StrName.substring(0, 4);
			String strMonth = StrName.substring(4, 6);
			String strDate = StrName.substring(6, 8);
			String strHours = StrName.substring(8, 10);
			String strMinutes = StrName.substring(10, 12);
			String strSeconds = StrName.substring(12, 14);
//			String strSign = StrName.substring(14);
			return strYear + "-" + strMonth + "-" + strDate + " " + strHours
					+ ":" + strMinutes + ":" + strSeconds ;
		} else {
			return "";
		}

	} 

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("����", "������㷽��", String.valueOf(visit.getDiancxxb_id()), "����"));
		StringBuffer sql = new StringBuffer();
		String wenjm = getHengdValue().getValue();//		�ļ���
		String jilhh = wenjm.substring(14,15);//		�������
		String guohsj = fn2time(wenjm);//	����ʱ�� yyyy-mm-dd hh24:mi:ss
		String guohrq = guohsj.substring(0,10);//	�������� yyyy-mm-dd
		String oraGuohsj = DateUtil.FormatOracleDateTime(guohsj);// �������� to_date('','yyyy-mm-dd')
		String guohbid = MainGlobal.getNewID(((Visit) this.getPage().getVisit()).getDiancxxb_id());//	�����id
//		�洢�����
		sql.append("insert into guohb(id, guohsj, wenjm) values(")
		.append(guohbid).append(",").append(oraGuohsj).append(",'")
		.append(wenjm).append("')");
		int flag = con.getInsert(sql.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
					+"SQL:"+sql);
			setMsg(ErrorMessage.Chehhd006);
			return;
		}
//		ȡ��ҳ��ĶԺ���Ϣ
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Duih.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
//		���³�Ƥ��
		while(rsl.next()) {
			String chepbid = rsl.getString("ID");
			sql.delete(0, sql.length());
			sql.append("update chepb set xuh=").append(rsl.getRow()+1).append(",cheph='")
			.append(rsl.getString("CHEPH")).append("',");
			sql.append("maoz=").append(rsl.getDouble("maoz")).append(",");
			sql.append("koud=").append(rsl.getDouble("koud")).append(",");
			sql.append("zongkd=").append(rsl.getDouble("koud")).append(",");
			sql.append("piz=").append(rsl.getString("piz")).append(",");
			sql.append("yuanmz=").append(rsl.getDouble("maoz")).append(",");
			if(rsl.getString("ches") != null && !"".equals(rsl.getString("ches")))
				sql.append("ches=").append(rsl.getString("ches")).append(",");
//			�жϼ���ļ����� �����Ǳ����ᳵʱ�仹���س�ʱ��
			if(getHengdType() == SysConstant.Hengd_Auto_all || getHengdType() == SysConstant.Hengd_Manual_all) {
				sql.append("zhongcsj=").append(oraGuohsj).append(",");
				sql.append("zhongcjjy='").append(visit.getRenymc()).append("',");
				sql.append("zhongchh='").append(jilhh).append("',");
				sql.append("qingcsj=").append(oraGuohsj).append(",");
				sql.append("qingcjjy='").append(visit.getRenymc()).append("',");
				sql.append("qingcchh='").append(jilhh).append("',");
			}else if(getHengdType() == SysConstant.Hengd_Auto_maoz || getHengdType() == SysConstant.Hengd_Manual_maoz) {
				sql.append("zhongcsj=").append(oraGuohsj).append(",");
				sql.append("zhongcjjy='").append(visit.getRenymc()).append("',");
				sql.append("zhongchh='").append(jilhh).append("',");
			}else
				if(getHengdType() == SysConstant.Hengd_Auto_piz || getHengdType() == SysConstant.Hengd_Manual_piz) {
					sql.append("qingcsj=").append(oraGuohsj).append(",");
					sql.append("qingcjjy='").append(visit.getRenymc()).append("',");
					sql.append("qingcchh='").append(jilhh).append("',");
				}
			
			sql.append("daozch='").append(rsl.getString("daozch")).append("',");
			sql.append("hedbz=").append(SysConstant.HEDBZ_YJJ).append(",");
			sql.append("beiz='").append(rsl.getString("beiz")).append("',");
			sql.append("guohb_id =").append(guohbid).append("\n");
			sql.append(" where id=").append(chepbid);
			flag = con.getUpdate(sql.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Chehhd007);
				setMsg(ErrorMessage.Chehhd007);
				return;
			}
//			���泵����Ϣ��
			Jilcz.SaveChelxx(con,visit.getDiancxxb_id(),0,SysConstant.YUNSFS_HUOY,
					rsl.getString("CHEPH"),rsl.getDouble("maoz"),rsl.getDouble("piz"));
//			����Ǽ��㵥�����������
			if(isDancYuns) {
				flag = Jilcz.CountChepbYuns(con, chepbid, SysConstant.HEDBZ_YJJ);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chehhd008);
					setMsg(ErrorMessage.Chehhd008);
					return;
				}
			}
		}
//		ȡ�ø��¹��ĳ�Ƥfahb_id
		sql.delete(0, sql.length());
		sql.append("select distinct fahb_id from chepb where guohb_id = ").append(guohbid);
		rsl = con.getResultSetList(sql.toString());
		List fhlist = new ArrayList();
		while(rsl.next()) {
			String fahbid = rsl.getString("fahb_id");
			Jilcz.addFahid(fhlist, fahbid);
			sql.delete(0, sql.length());
			sql.append("select count(id) allc,nvl(sum(decode(hedbz,0,0,1,0,1)),0) jjc from chepb where fahb_id = ")
			.append(fahbid);
			ResultSetList rs = con.getResultSetList(sql.toString());
			java.util.Date daohrq = DateUtil.getDate(guohrq);
			if(rs.next() && rs.getInt("allc")!= rs.getInt("jjc")) {
				String newfhid = Jilcz.CopyFahb(con,fahbid,visit.getDiancxxb_id());
				if(newfhid == null) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chehhd009);
					setMsg(ErrorMessage.Chehhd009);
					return;
				}
				sql.delete(0, sql.length());
				sql.append("update chepb set fahb_id=")
				.append(newfhid).append(" where guohb_id=")
				.append(guohbid).append(" and fahb_id=").append(fahbid);
				flag = con.getUpdate(sql.toString());
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chehhd010);
					setMsg(ErrorMessage.Chehhd010);
					return;
				}
				flag = Jilcz.updateFahb(con, newfhid, daohrq);
				if(flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chehhd011);
					setMsg(ErrorMessage.Chehhd011);
					return;
				}
//				�����ַ�����ԭ����"��������"����
				daohrq = null;
			}
//			���·�����������Ϣ
			flag = Jilcz.updateFahb(con, fahbid, daohrq);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Chehhd011);
				setMsg(ErrorMessage.Chehhd011);
				return;
			}
//			����ǰ����μ������������
			if(!isDancYuns) {
				flag = Jilcz.CountFahbYuns(con, fahbid);
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.Chehhd012);
					setMsg(ErrorMessage.Chehhd012);
					return;
				}
			}
		}
//		������ļ���������Ŀ¼
		File file = new File(visit.getXitwjjwz() + "/shul/jianjwj/"+wenjm);
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwjbak/"+wenjm.substring(0, 4)+"/"+wenjm.substring(4, 6));
		File bakfile = new File(filepath+"/",wenjm);
		if(bakfile.exists()) {
			bakfile.delete();
		}
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		if(!file.renameTo(bakfile)) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Chehhd013);
			setMsg(ErrorMessage.Chehhd013);
			return;
		}
		setHengdValue(null);
		setHengdModel(null);
		setChepSelected(null);
		setChepModel(null);
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private void Return(IRequestCycle cycle) {
		((Visit) getPage().getVisit()).setboolean1(true);
		cycle.activate("Chehhd");
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			Return(cycle);
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}
}