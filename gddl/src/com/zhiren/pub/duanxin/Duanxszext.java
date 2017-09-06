/*chh 2009-09-25 
 *���⣺������˾û����ʾ�Լ����ӵ���Ϣ
 *�����޸Ĳ�ѯ�������������糧������ѡ����λ������,����ʾ�ӵ�λ����Ϣ
 */

package com.zhiren.pub.duanxin;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duanxszext extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
	
	//������
	private String Jiesr;

	public String getJiesr() {
		return Jiesr;
	}

	public void setJiesr(String jiesr) {
		Jiesr = jiesr;
	}
	
	
	//���ӵ绰
	private String Fujdh;

	public String getFujdh() {
		return Fujdh;
	}

	public void setFujdh(String fujdh) {
		Fujdh = fujdh;
	}
	
	//����
	private String Neir;

	public String getNeir() {
		return Neir;
	}

	public void setNeir(String neir) {
		Neir = neir;
	}
	
	//�������� 
	private String Fujxx;

	public String getFujxx() {
		return Fujxx;
	}

	public void setFujxx(String neir1) {
		Fujxx = neir1;
	}
	
//	һ�η���Сʱ 
	private String Xiaos;

	public String getXiaos() {
		return Xiaos;
	}

	public void setXiaos(String xiaos) {
		Xiaos = xiaos;
	}
	
//	һ�η�����
	private String Miaoz;

	public String getMiaoz() {
		return Miaoz;
	}

	public void setMiaoz(String miaoz) {
		Miaoz = miaoz;
	}
	
//	�������� ��ʼСʱ 
	private String Xiaos1;

	public String getXiaos1() {
		return Xiaos1;
	}

	public void setXiaos1(String xiaos1) {
		Xiaos1 = xiaos1;
	}
	
//	�������� ��ʼ��
	private String Miaoz1;

	public String getMiaoz1() {
		return Miaoz1;
	}

	public void setMiaoz1(String miaoz1) {
		Miaoz1 = miaoz1;
	}
	
//	�������� ����Сʱ 
	private String Xiaos2;

	public String getXiaos2() {
		return Xiaos2;
	}

	public void setXiaos2(String xiaos2) {
		Xiaos2 = xiaos2;
	}
	
//	�������� ������
	private String Miaoz2;

	public String getMiaoz2() {
		return Miaoz2;
	}

	public void setMiaoz2(String miaoz2) {
		Miaoz2 = miaoz2;
	}
	
//	�������� ����
	private String Leib;

	public String getLeib() {
		return Leib;
	}

	public void setLeib(String leib) {
		Leib = leib;
	}
	
	//����ʱ��
	private boolean _FassjChange=false;
	public String getFassj() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setFassj(String _value) {
		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
			_FassjChange=false;
		} else {
			((Visit)getPage().getVisit()).setString12(_value);
			_FassjChange=true;
		}
	}

	//ҳ�汣��
	private void Save1(String strchange, Visit visit){
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = getExtGrid();
		try {
			
			StringBuffer sql = new StringBuffer("begin \n");
			
			//Ҫɾ��������
			ResultSetList delrsl = egu.getDeleteResultSet(strchange);

			while (delrsl.next()) {
				String duanxdypid=delrsl.getString(0);//�����Զ����id
				String duanxjsrbid=delrsl.getString(1);//���Ž����˱�id
				String duanxplb_id=delrsl.getString(2);//�������ñ�id
				
				//������Ա��
				sql.append(" delete from duanxjsrb where id =").append(duanxjsrbid).append(";\n");
//				if (duanxlb.equals("0")){//�ֹ�����Ķ���
//					//���ŷ��ͱ�
//					sql.append(" delete from duanxfsb where id=").append(duanxfsbid).append(";\n");
//				}else{//�Զ����õĶ���
//					�����Զ����
					sql.append(" delete from duanxdyb where id=").append(duanxdypid).append(";\n");
				
//					���ŷ��ͱ�
					sql.append(" delete from duanxfsb where id=").append(duanxdypid).append(";\n");
//				}
			}
			
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
				if ("0".equals(mdrsl.getString("ID"))) {
					
				}else{
					String zhuangt="";
				
					zhuangt=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[mdrsl.getColumnCount()-1]),
								mdrsl.getString(mdrsl.getColumnCount()-1));
					
					sql.append("  update duanxdyb set zhuangt=").append(zhuangt);
					sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");

				}
			}

			//Ҫ�޸ĵ�����
			//ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			
			sql.append("end;");
			con.getUpdate(sql.toString());
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	
	//����
	private void Save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Date date = new Date();

		String tiaoj1 = getRbvalue();//����Ƶ�ʣ�ÿ�죬ÿ�ܣ�ÿ��
		String tiaoj2 = getRbvalue1();//ÿ��Ƶ�ʣ�һ�η�������������
		
		String tians = getTians();//�ڼ�����
		String zhouqs = getZhouqs();//����������
		
		String zhouqlb = getLeib();//�������ڵ����ͣ�Сʱ������
//		if(getLeibDropDownValue()!=null){
//			zhouqlb=getLeibDropDownValue().getValue();
//		}
		
		String yicfssj ="";
		String yicfs_xiaos = getXiaos();//һ�η����ġ�Сʱ��������
//		if(getXiaosDownValue()!=null){
//			yicfs_xiaos = getXiaosDownValue().getValue();
//		}
		String yicfs_miaos = getMiaoz();//һ�η����ġ����ӡ�������
//		if(getMiaozDownValue()!=null){
//			yicfs_miaos = getMiaozDownValue().getValue();
//		}
		yicfssj=yicfs_xiaos+":"+yicfs_miaos;
		//��ʼʱ��
		String kaissj="";
		String begin_xiaos = getXiaos1();//�������ڵġ�Сʱ��������
//		if(getXiaos1DownValue()!=null){
//			begin_xiaos = getXiaos1DownValue().getValue();
//		}
		String begin_miaos = getMiaoz1();//�������ڵġ����ӡ�������
//		if(getMiaoz1DownValue()!=null){
//			begin_miaos = getMiaoz1DownValue().getValue();
//		}
		kaissj=begin_xiaos+":"+begin_miaos;
		//����ʱ��
		String jiessj="";
		String end_xiaos = getXiaos2();//�������ڵġ�Сʱ��������
//		if(getXiaos2DownValue()!=null){
//			end_xiaos = getXiaos2DownValue().getValue();
//		}
		String end_miaos = getMiaoz2();//�������ڵġ����ӡ�������
//		if(getMiaoz2DownValue()!=null){
//			end_miaos = getMiaoz2DownValue().getValue();
//		}
		jiessj=end_xiaos+":"+end_miaos;
		
		String biaot = "";//����
		String fujdh = "0";//���ӵ��getFujdh();
		String lury = visit.getRenymc();//¼��Ա����
		
		String lurysj = "sysdate ";//¼��ʱ��
		String fujxx = getFujxx();//������Ϣ
		String neir = "(select fashs from duanxdypzb where id="+getNeir()+")";//����
		ResultSetList rslneir=con.getResultSetList(neir);
		String neir1="";
		if(rslneir.next()){
			neir1="(select "+rslneir.getString("fashs")+" from dual )";
		}
		
		String duanxfsb_id = MainGlobal.getNewID(visit.getDiancxxb_id());//���ͱ�ID
		
		//��ö��Ŷ������ñ�id
		long duanxdypzb_id = 0;
		
		if(getNeirDropDownValue()!=null){
			duanxdypzb_id=getNeirDropDownValue().getId();
		}
		
		//������Ž����˱�
		String jiesr = getJiesr();
		String[] lurytemp = jiesr.split(",");
		StringBuffer str_lury = new StringBuffer();
		int size=lurytemp.length;
		//��ý����˵�id
		for (int i=0;i<size;i++){
			String lurystr="select id from lianxrb where xingm ='"+lurytemp[i]+"'";
			try{
				ResultSetList rl = con.getResultSetList(lurystr);
				if(rl.next()){
					str_lury.append(rl.getString("id")).append(",");
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
		}
		if(str_lury.length()!=0){
			str_lury.setLength(str_lury.length()-1);
		}
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("insert into duanxjsrb (id,duanxfsb_id,jiesr) values ( \n ");
		sql1.append("getnewid(").append(visit.getDiancxxb_id()).append("),").append(duanxfsb_id).append(",'");
		sql1.append(str_lury.toString()).append("')");
		
		con.getInsert(sql1.toString());
		
		//������Ŷ����

		StringBuffer sql2 = new StringBuffer();
		sql2.append("insert into duanxdyb (id,diancxxb_id,duanxdypzb_id,fassj,cazy,cazsj,zhuangt,beiz) values ( \n");
		sql2.append(duanxfsb_id).append(",").append(getTreeid()).append(",");
		sql2.append(getNeir()).append(",").append("to_date('"+DateUtil.FormatDate(date)+" "+yicfssj+"','YYYY-MM-DD HH24:MI:SS')");
		sql2.append(",'");
		sql2.append(lury).append("',").append(lurysj).append(",1,''");
		sql2.append(")");
		
		con.getInsert(sql2.toString());
		
		StringBuffer sql4 = new StringBuffer();
		sql4.append("insert into duanxfsb (id,diancxxb_id,riq,neir,lury,lurysj,zhuangt,leib,fujxx) values ( \n");
		sql4.append(duanxfsb_id).append(",").append(getTreeid()).append(",");
		sql4.append("to_date('"+DateUtil.FormatDate(date)+" "+yicfssj+"','YYYY-MM-DD HH24:MI:SS')").append(",").append(neir1);
		sql4.append(",'");
		sql4.append(lury).append("',sysdate,0,0,'").append(fujxx);
		sql4.append("')");
		
		con.getInsert(sql4.toString());
		
		
		String leib ="";//���ÿ�գ�ÿ�ܣ�ÿ��
		String fasrq ="";//��������
		String faszq ="";//��������
		String faszqdw ="";//���ͣ�Сʱ������
		String begindate ="";//��ʼʱ��
		String enddate ="";//����ʱ��
		
		String fasmrpd ="0";//����ÿ��Ƶ��
		
		if (tiaoj1.equals("tiaoj1")){//ÿ��
			leib="ÿ��";
			fasrq="0";
		}else if(tiaoj1.equals("tiaoj2")){//ÿ��
			leib="ÿ��";
			fasrq=tians;
		}
//		else if(tiaoj1.equals("tiaoj3")){//ÿ��
//			leib="ÿ��";
//			fasrq=tians;
//		}
		
		if(tiaoj2.equals("tiaoj1")){//һ�η���
			faszq="0";
			faszqdw ="0";
			begindate=yicfssj;
			enddate=yicfssj;
		}else if(tiaoj2.equals("tiaoj2")){//��������
			faszq=zhouqs;
			faszqdw=zhouqlb;
			begindate=kaissj;
			enddate=jiessj;
		}
			
//		//�������Ƶ�ʱ�
//		String duanxplb_id=MainGlobal.getNewID(visit.getDiancxxb_id());
//		
//		StringBuffer sql3 = new StringBuffer();
//		sql3.append("insert into duanxplb (id,duanxdyb_id,leib,fasrq,faszqsssj,faszqjssj,fasmrpd,faszq,faszqdw) values ( \n");
//		sql3.append(duanxplb_id).append(",").append(duanxfsb_id).append(",'");
//		sql3.append(leib).append("','").append(fasrq).append("',").append("to_date('"+DateUtil.FormatDate(date)+" "+begindate+"','YYYY-MM-DD HH24:MI:SS')");
//		sql3.append(",").append("to_date('"+DateUtil.FormatDate(date)+" "+enddate+"','YYYY-MM-DD HH24:MI:SS')").append(",");
//		sql3.append(fasmrpd).append(",").append(faszq).append(",'").append(faszqdw).append("')");
//		
//		con.getInsert(sql3.toString());
		
		
//		//������ŷ��ͱ�
//		StringBuffer sql = new StringBuffer();
//		sql.append("insert into duanxfsb (id,diancxxb_id,riq,biaot,neir,fujdh,lury,lurysj,zhuangt,leib,beiz,duanxdyb_id,duanxdypzb_id) values( \n");
//		sql.append(duanxfsb_id).append(",").append(visit.getDiancxxb_id()).append(",to_date('"+DateUtil.FormatDate(date)+"','yyyy-mm-dd'),'"+biaot+"','");
//		sql.append(fujxx).append("','").append(fujdh).append("','").append(lury);
//		sql.append("','").append(lurysj).append("',0,1,'',"+duanxdyb_id+","+duanxdypzb_id+")\n");
//		
//		con.getInsert(sql.toString());
//		
		
		setTians("1");
		setZhouqs("1");
		con.Close();
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Save1Chick = false;

	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Save1Chick) {
			Visit visit = (Visit) this.getPage().getVisit();
			_Save1Chick = false;
			Save1(getChange(), visit);
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		setJiesr("");
		setNeir("");
		setFujxx("");
		setFujdh("");
		setFassj("");
		JDBCcon con = new JDBCcon(); 
		String riqTiaoj=this.getRiqi();
		String riq1Tiaoj=this.getRiqi1();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		if(riq1Tiaoj==null||riq1Tiaoj.equals("")){
			riq1Tiaoj=DateUtil.FormatDate(new Date());
		}
		
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and dc.id = " + getTreeid() + "";
//			}
//		}
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
//			str = "";
//		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
//		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
//			str = "and dc.id = " + getTreeid() + "";
//			
//		}
		//chh 2009-08-25  �޸Ĳ�ѯ�������������糧������ѡ����λ������,����ʾ�ӵ�λ����Ϣ
		str = "and dc.id = " + getTreeid() + "";
	
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select ds.id,dr.id as duanxjsrbid,ds.cazy,dp.miaos,GetJiesr(dr.jiesr) as jiesr,\n");
		sbsql.append("a.fujxx,\n");
		sbsql.append("decode(ds.zhuangt,0,'ͣ��','����') as zhuangt,dp.fashs,dr.jiesr jiesr_id \n");
		sbsql.append("from duanxdyb ds,duanxdypzb dp,duanxjsrb dr,diancxxb dc,duanxfsb a \n");
		sbsql.append("where ds.duanxdypzb_id=dp.id and dr.duanxfsb_id=ds.id and a.id=ds.id and ds.diancxxb_id=dc.id "+str+" \n");
		//and ds.riq>=to_date('"+riqTiaoj+"','yyyy-mm-dd') and ds.riq<=to_date('"+riq1Tiaoj+"','yyyy-mm-dd')
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		
		
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duanxfsb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("duanxjsrbid").setHidden(true);
		egu.getColumn("duanxjsrbid").setEditor(null);
		
		egu.getColumn("cazy").setHeader("������");
		egu.getColumn("cazy").setWidth(100);
		egu.getColumn("cazy").setEditor(null);
		
		egu.getColumn("miaos").setHeader("����");
		egu.getColumn("miaos").setWidth(100);
		egu.getColumn("miaos").setEditor(null);
		
		egu.getColumn("jiesr").setHeader("������");
		egu.getColumn("jiesr").setWidth(100);
		egu.getColumn("jiesr").setEditor(null);
		
		egu.getColumn("fujxx").setHeader("������Ϣ");
		egu.getColumn("fujxx").setWidth(100);
		egu.getColumn("fujxx").setEditor(null);
		
		egu.getColumn("fashs").setHidden(true);
		egu.getColumn("jiesr_id").setHidden(true);
		
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setWidth(100);
		egu.getColumn("zhuangt").setHidden(true);
		//egu.getColumn("zhuangt").setEditor(null);

		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		List juflxlist = new ArrayList();
		juflxlist.add(new IDropDownBean(0, "ͣ��"));
		juflxlist.add(new IDropDownBean(1, "����"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(juflxlist));
		egu.getColumn("zhuangt").setReturnId(true);
		egu.getColumn("zhuangt").setDefaultValue("����");
		
		
//		egu.addTbarText("����:");
//		DateField df = new DateField();
//		df.setValue(this.getRiqi());
//		df.Binding("RIQI","");// ��htmlҳ�е�id��,���Զ�ˢ��
//		egu.addToolbarItem(df.getScript());
//		
//		egu.addTbarText("��");
//		DateField df1 = new DateField();
//		df1.setValue(this.getRiqi1());
//		df1.Binding("RIQI1","");// ��htmlҳ�е�id��,���Զ�ˢ��
//		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		egu.addTbarText("-");
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		//egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);

		String Strtmpfunction = 
			"//��������\n" +
			"var tiansfield = new Ext.form.TextField({\n" + 
			"\tfieldLabel:'�ڼ���',\n" + 
			"    anchor:'50%',\n" + 
			"\tname: 'tians',\n" + 
			"    listeners:{\n" + 
			"      change:function(own,newValue,oldValue) {\n" + 
			"      \t\tif(meiz.checked){\n" + 
			"\t      \t\tif(newValue>7){\n" + 
			"\t\t\t\t\tExt.MessageBox.alert('��ʾ��Ϣ','¼������������Χ(1~7)��');\n" + 
			"\t      \t\t}\n" + 
			"\t      \t}\n" + 
			
//			"\t      \tif(meiy.checked){\n" + 
//			"\t      \t\tif(newValue>31){\n" + 
//			"\t\t\t\t\tExt.MessageBox.alert('��ʾ��Ϣ','¼������������Χ(1~31)��');\n" + 
//			"\t      \t\t}\n" + 
//			"\t      \t}\n" + 
			
			"\t\t\tdocument.getElementById('TIANS').value = newValue;}\n" + 
			"\t},\n" + 
			"    value:document.all.item('TIANS').value\n" + 
			"});\n" + 
			"//Radio��ÿ��\n" + 
			"var meit = new Ext.form.Radio({\n" + 
			"\tboxLabel:'ÿ��',\n" + 
			" \tValue:'tiaoj1',\n" + 
			"    labelSeparator:'',\n" + 
			"\tchecked:true,\n" + 
			"\tname:'tiaoj',\n" + 
			"\tlisteners:{\n" + 
			"\t\t'check':function(r,c){\n" + 
			"         \tif(r.checked){\n" + 
			"         \ttiansfield.setDisabled(true);\n" + 
			"         \tdocument.getElementById('rbvalue').value=r.Value;}\n" + 
			"     \t}\n" + 
			"    }\n" + 
			"});\n" + 
			"//Radio��ÿ��\n" + 
			"var meiz = new Ext.form.Radio({\n" + 
			"\tboxLabel:'ÿ��',\n" + 
			"\tValue:'tiaoj2',\n" + 
			"    labelSeparator:'',\n" + 
			"\tname:'tiaoj',\n" + 
			"\tlisteners:{\n" + 
			"\t\t'check':function(r,c){\n" + 
			"\t\t\tif(r.checked){\n" + 
			"\t\t\ttiansfield.setDisabled(false);\n" + 
			"\t\t\tdocument.getElementById('rbvalue').value=r.Value;}\n" + 
			"       }\n" + 
			"    }\n" + 
			"});\n" + 
			
			
//			"//Radio��ÿ��\n" + 
//			"var meiy = new Ext.form.Radio({\n" + 
//			"\tboxLabel:'ÿ��',\n" + 
//			"\tValue:'tiaoj3',\n" + 
//			"    labelSeparator:'',\n" + 
//			"\tname:'tiaoj',\n" + 
//			"\tlisteners:{\n" + 
//			"\t\t'check':function(r,c){\n" + 
//			"\t\t\tif(r.checked){\n" + 
//			"\t\t\ttiansfield.setDisabled(false);\n" + 
//			"\t\t\tdocument.getElementById('rbvalue').value=r.Value;}\n" + 
//			"       }\n" + 
//			"    }\n" + 
//			"});\n" + 
			
			
			"//��������\n" + 
			"var navtree = new Ext.tree.TreePanel({\n" + 
			"    title: '������',\n" + 
			"    region: 'east',\n" + 
			"    autoScroll:true,\n" + 
			"\trootVisible:false,\n" + 
			"    split: true,\n" + 
			"    width: 200,\n" + 
			"    minSize: 160,\n" + 
			"    maxSize: 240,\n" + 
			"    collapsible: true,\n" + 
			"    margins:'1 0 1 1',\n" + 
			"    cmargins:'1 1 1 1',\n" + 
			"   \troot:navTree0,\n" + 
			"\tlisteners : {\n" + 
			"\t\t'dblclick':function(node,e){\n" + 
			"\t\t\tif(node.getDepth() == 3){\n" + 
			"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"    \t\t\tCobj.value = node.id;\n" + 
			"\t\t\t\ttmp = Ext.getDom('jiesr1').value;\n" + 
			"        \t\tExt.getDom('jiesr1').value=(tmp?tmp+',':'')+node.text;\n" + 
			"\t\t\t\tdocument.getElementById('JIESR').value=Ext.getDom('jiesr1').value;\n" + 
			"\t\t\t}else{\n" + 
			"\t\t\t\te.cancel = true;\n" + 
			"\t\t\t}\n" + 
			"\t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\tnode.expand();\n" + 
			"\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t}\n" + 
			"\t\t\t\t});\n" + 
			"\t\t\t}\n" + 
			"\t},\n" + 
			"   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
			"\t\tvar cs = navtree.getChecked();\n" + 
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"        for(var i = 0; i < cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"        \t}\n" + 
			"        }\n" + 
			"       \tExt.getDom('jiesr1').value=tmp2;\n" + 
			"\t\tdocument.getElementById('JIESR').value=Ext.getDom('jiesr1').value;\n" + 
			"    }}]\n" + 
			"});\n" + 
			
			"//����������\n" +
			"var neir =new Ext.form.ComboBox({\n" + 
			"\t\t\t\t    width:100,\n" + 
			"\t\t\t\t\tfieldLabel:'����',\n" + 
			"\t\t\t\t    selectOnFocus:true,\n" + 
			"\t\t\t\t \ttransform:'NeirDropDown',\n" + 
			"\t\t\t\t\tlazyRender:true,\n" + 
			"\t\t\t\t\ttriggerAction:'all',\n" + 
			"\t\t\t\t\ttypeAhead:true,\n" + 
			"\t\t\t\t    forceSelection:true,\n" + 
			"\t\t\t\t\teditable:false\n" + 
			"\t\t        });"+

//			"//��������\n" + 
//			"var nav = new Ext.Panel({\n" + 
//			"    region: 'center',\n" + 
//			"    autoScroll: true,\n" + 
//			"    split: true,\n" + 
//			"    width: '100%',\n" + 
//			"    margins:'1 0 1 1',\n" + 
//			"    cmargins:'1 1 1 1',\n" + 
//			"    items: [new Ext.form.FormPanel({\n" + 
//			"\t\t        frame:true,\n" + 
//			"\t\t        width:'100%',\n" + 
//			"\t\t        labelWidth: 60,\n" + 
//			"\t\t        labelAlign: 'right',\n" + 
//			"\t\t        //url:'save-form.php',\n" + 
//			"\t\t        defaultType: 'textfield',\n" + 
//			"\t\t        items: [{\n" + 
//			"\t\t            fieldLabel: '������',\n" + 
//			"\t\t\t\t\tid: 'jiesr1',\n" + 
//			"\t\t            name: 'jiesr',\n" + 
//			"\t\t            disabled: true,\n" + 
//			"\t\t            anchor:'95%',\n" + 
//			"\t\t            listeners:{\n" + 
//			"\t\t            change:function(own,newValue,oldValue) {\n" + 
//			"\t\t\t\t\t\tdocument.getElementById('JIESR').value = newValue;}\n" + 
//			"\t\t            },\n" + 
//			"\t\t            value:document.all.item('JIESR').value\n" + 
//			"\t\t        },{\n" + 
//			"\t\t            fieldLabel: '���ӵ绰',\n" + 
//			"\t\t            name: 'fujdh',\n" + 
//			"\t\t            hideLabel: true,\n" + 
//			"\t\t            hidden: true,\n" + 
//			"\t\t            anchor: '95%',\n" + 
//			"\t\t            listeners:{\n" + 
//			"\t\t            change:function(own,newValue,oldValue) {\n" + 
//			"\t\t\t\t\t\tdocument.getElementById('FUJDH').value = newValue;}\n" + 
//			"\t\t            },\n" + 
//			"\t\t            value:document.all.item('FUJDH').value\n" + 
//			"            },neir,{\n" + 
//			"\t\t            fieldLabel: '������Ϣ',\n" + 
//			"\t\t\t\t\t		 id:'fujxx1',\n" + 
//			"\t\t            xtype: 'textarea',\n" + 
//			"\t\t\t\t\tmaxLength :180,\n" + 
//			"\t\t            height: 35,\n" + 
//			"\t\t            name: 'fujxx',\n" + 
//			"\t\t            anchor: '94%',\n" + 
//			"\t\t            listeners:{\n" + 
//			"\t\t            change:function(own,newValue,oldValue) {\n" + 
//			"\t\t\t\t           if(newValue.length>180){\n" + 
//			"\t\t\t\t\t          Ext.MessageBox.alert('��ʾ��Ϣ','�����������Ϊ180���ַ�,�������ϵͳ���Զ���ȡǰ180���ַ���');\n" + 
//			"\t\t\t\t\t          Ext.getDom('fujxx1').value=newValue.substr(0,180);\n" + 
//			"\t\t\t\t          }\n" + 
//			"\t\t            \t  document.getElementById('FUJXX').value = Ext.getDom('fujxx1').value;}\n" + 
//			"\t\t\t\t\t},\n" + 
//			"\t\t\t\t\tvalue:document.all.item('FUJXX').value\n" + 
//			"\t\t        }" +
//			"" +
//			"]\n" + 
//			"\t\t    })\n" + 
//			"\t]\n" + 
//			"});\n" + 
//
//			"var hour_pan = new Ext.Panel({\n" + 
//			"\t//baseCls: 'x-plain',\n" + 
//			"\theight:40,\n" + 
//			"\tlayout:'table',\n" + 
//			"\titems : [{html:'&nbsp;<font size=\"2\">����ʱ��:</font>&nbsp;' },{\n" + 
//			"\t\t\t xtype:'datefield',\n" + 
//			"\t\t     name:'fassj',\n" +  
//			"\t\t     anchor: '90%',\n" + 
////			"    	 listeners:{change:function(own,newValue,oldValue) { }},\n" +
////			"     	 value:document.all.item('FASSJ').value \n },"+
//			"\t\t\t{html:'&nbsp;ʱ&nbsp;'},{html:'&nbsp;��&nbsp;'}]\n" + 
//			"});"+
			
		
			"//******************************���÷���Ƶ��******************************//\n" + 
			"//һ�η���\n" + 
			"var yicfs = new Ext.form.Radio({\n" + 
			"\tboxLabel:'һ�η���',\n" + 
			" \tValue:'tiaoj1',\n" + 
			"    labelSeparator:'',\n" + 
			"\tchecked:true ,\n" + 
			"\tname:'tiaoj1',\n" + 
			"\tlisteners:{\n" + 
			"\t\t'check':function(r,c){\n" + 
			"         \tif(r.checked){\n" + 
			"         \t//��ѡ��һ�η���ʱ�������ܷ��µĹ��ܶ�����ѡ�񣡡�\n" + 
			"         \tXiaos.setDisabled(false)//��ʼ��Сʱ��\n" + 
			"         \tMiaoz.setDisabled(false)//��ʼ�����ӡ�\n" + 
			"\n" + 
			"         \tzhoups.setDisabled(true);//������\n" + 
			"         \tzhouqlx.setDisabled(true)//��������\n" + 
			"         \txiaos1.setDisabled(true)//��ʼ��Сʱ��\n" + 
			"         \tmiaoz1.setDisabled(true)//��ʼ�����ӡ�\n" + 
			"         \txiaos2.setDisabled(true)//������Сʱ��\n" + 
			"         \tmiaoz2.setDisabled(true)//���������ӡ�\n" + 
			"         \tdocument.getElementById('rbvalue1').value=r.Value;}\n" + 
			"     \t}\n" + 
			"    }\n" + 
			"});\n" + 
			
//			
//			"//��������\n" + 
//			"var zhouqfs = new Ext.form.Radio({\n" + 
//			"\tboxLabel:'��������',\n" + 
//			"\tValue:'tiaoj2',\n" + 
//			"\tlabelSeparator:'',\n" + 
//			"\tname:'tiaoj1',\n" + 
//			"\tlisteners:{\n" + 
//			"\t\t'check':function(r,c){\n" + 
//			"\t\t\tif(r.checked){\n" + 
//			"\t\t\t//��ѡ��һ�η���ʱ��һ�η����µĹ��ܶ�����ѡ�񣡡�\n" + 
//			"\t\t\tXiaos.setDisabled(true)//��ʼ��Сʱ��\n" + 
//			"         \tMiaoz.setDisabled(true)//��ʼ�����ӡ�\n" + 
//			"\n" + 
//			"         \tzhoups.setDisabled(false);//������\n" + 
//			"         \tzhouqlx.setDisabled(false)//��������\n" + 
//			"         \txiaos1.setDisabled(false)//��ʼ��Сʱ��\n" + 
//			"         \tmiaoz1.setDisabled(false)//��ʼ�����ӡ�\n" + 
//			"         \txiaos2.setDisabled(false)//������Сʱ��\n" + 
//			"         \tmiaoz2.setDisabled(false)//���������ӡ�\n" + 
//			"\t\t\tdocument.getElementById('rbvalue1').value=r.Value;}\n" + 
//			"\t   }\n" + 
//			"\t}\n" + 
//			"});\n" + 
			"//һ�η����ġ�Сʱ��\n" + 
			"var Xiaos = new Ext.form.ComboBox({\n" + 
			"\twidth:60,\n" + 
			"\tfieldLabel:'ʱ',\n" + 
			"    selectOnFocus:true,\n" + 
			" \ttransform:'XiaosDropDown',\n" + 
			"\tlazyRender:true,\n" + 
			"\ttriggerAction:'all',\n" + 
			"\ttypeAhead:true,\n" + 
			"    forceSelection:true,\n" + 
			"\teditable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('XIAOS').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('XIAOS').value"+
			"});\n" + 
			
			
			"//������\n" + 
			"var zhoups= new Ext.form.TextField({\n" + 
			"    xtype:'textfield',\n" + 
			"    hideLabel :true,\n" + 
			"    anchor:'60%',\n" + 
			"    name: 'zhoups',\n" + 
			"    listeners:{\n" + 
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('ZHOUQS').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('ZHOUQS').value\n" + 
			"});\n" + 
			"//��ʼ��Сʱ��\n" + 
			"var xiaos1 = new Ext.form.ComboBox({\n" + 
			"\twidth:60,\n" + 
			"\tfieldLabel:'ʱ',\n" + 
			"    selectOnFocus:true,\n" + 
			" \ttransform:'Xiaos1DropDown',\n" + 
			"\tlazyRender:true,\n" + 
			"\ttriggerAction:'all',\n" + 
			"\ttypeAhead:true,\n" + 
			"    forceSelection:true,\n" + 
			"\teditable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('XIAOS1').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('XIAOS1').value"+
			"});\n" + 
			"//������Сʱ��\n" + 
			"var xiaos2 = new Ext.form.ComboBox({\n" + 
			"     width:60,\n" + 
			"\t fieldLabel:'ʱ',\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'Xiaos2DropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('XIAOS2').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('XIAOS2').value"+
			"});\n" + 
			"//һ�η����ġ����ӡ�\n" + 
			"var Miaoz = new Ext.form.ComboBox({\n" + 
			"  \t width:60,\n" + 
			"\t fieldLabel:'��',\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'MiaozDropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('MIAOZ').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('MIAOZ').value"+
			"});\n" + 
			"//����ת������\n" + 
			"var zhouqlx = new Ext.form.ComboBox({\n" + 
			"     width:60,\n" + 
			"\t hideLabel :true,\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'LeibDropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('LEIB').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('LEIB').value"+
			"});\n" + 
			"//��ʼ�����ӡ�\n" + 
			"var miaoz1 = new Ext.form.ComboBox({\n" + 
			" \t width:60,\n" + 
			"\t fieldLabel:'��',\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'Miaoz1DropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('MIAOZ1').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('MIAOZ1').value"+
			"});\n" + 
			"//���������ӡ�\n" + 
			"var miaoz2 = new Ext.form.ComboBox({\n" + 
			"\t width:60,\n" + 
			"\t fieldLabel:'��',\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'Miaoz2DropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('MIAOZ2').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('MIAOZ2').value"+
			"});\n" + 
//  
			"var hour_pan = new Ext.Panel({\n" + 
			"\t//baseCls: 'x-plain',\n" + 
			"\theight:40,\n" + 
			"\tlayout:'table',\n" + 
			"\titems : [{html:'&nbsp;<font size=\"2\">����ʱ��:</font>&nbsp;' },{\n" + 
//			"\t\t\t xtype:'datefield',\n" + 
			"\t\t     name:'fassj',\n" + 
			"\t\t     anchor: '90%',\n" + 
			"    	 listeners:{change:function(own,newValue,oldValue) {\n document.getElementById('FASSJ').value = newValue.dateFormat('Y-m-d');}},\n" +
			"     	 value:document.all.item('FASSJ').value \n }"+
			"\t\t\t,{html:'&nbsp;ʱ&nbsp;'},Xiaos,{html:'&nbsp;��&nbsp;'},Miaoz]\n" + 
			"});"+
			//
			"\t        var nav = new Ext.Panel({\n" + 
			"\t            title: '��������',\n" + 
			"\t            region: 'center',\n" + 
			"\t            autoScroll: true,\n" +
			"\t            split: true,\n" + 
			"\t            width: '100%',\n" + 
			//"\t            collapsible: true,\n" + 
			"\t            margins:'1 0 1 1',\n"+
			"\t            cmargins:'1 1 1 1',\n  frame: true, \n"+
			"\t            items: [new Ext.form.FormPanel({\n" + 
			"\t\t\t\t\t        baseCls: 'x-plain',\n" + 
			"\t\t\t\t\t        width:'100%',\n" + 
			"\t\t\t\t\t        labelWidth: 60,\n" + 
			"\t\t\t\t\t        labelAlign: 'right',\n" + 
			"\t\t\t\t\t        //url:'save-form.php',\n" + 
			"\t\t\t\t\t        defaultType: 'textfield',\n" + 
			"\n" + 
			"\t\t\t\t\t        items: [{\n" + 
			"\t\t\t\t\t            fieldLabel: '������',\n id: 'jiesr1',\n disabled: true,\n" + 
			"\t\t\t\t\t            name: 'jiesr',\n" + 
			"\t\t\t\t\t            anchor:'95%',\n" +
			"\t\t\t\t\t            listeners:{ \n" +
			"\t\t\t\t\t            change:function(own,newValue,oldValue) {\n document.getElementById('JIESR').value = newValue;}\n"+
			"\t\t\t\t\t            },"+
			"\t\t\t\t\t            value:document.all.item('JIESR').value \n"+
			"\t\t\t\t\t        },neir,{\n" + 
			"\t\t\t\t\t            fieldLabel: '������Ϣ',\n id: 'neir1',\n" + 
			"\t\t\t\t\t            xtype: 'textarea',\n maxLength :180,\n" + 
			"\t\t\t\t\t            height: 180,\n" + 
			"\t\t\t\t\t            name: 'neir',\n" + 
			"\t\t\t\t\t            anchor: '94%',\n" +
			"\t\t\t\t\t            listeners:{ \n" +
			"\t\t\t\t\t            change:function(own,newValue,oldValue) {\n "+
			"                      if(newValue.length>180){   \n" +
			"                      Ext.MessageBox.alert('��ʾ��Ϣ','�����������Ϊ180���ַ�,�������ϵͳ���Զ���ȡǰ180���ַ���'); \n" +
			"                      Ext.getDom('FUJXX').value=newValue.substr(0,180);\n" +
			
			"                      }\n" +
			"\t\t\t\t\t            document.getElementById('FUJXX').value = Ext.getDom('neir1').value;}\n },"+
			"\t\t\t\t\t            value:document.all.item('FUJXX').value \n"+
			"\t\t\t\t\t        },hour_pan" +//,hour_pan
			
//			"{\n" + 
//			"						xtype:'datefield', \n"   
//			+ "						fieldLabel:'����ʱ��', \n"   
//			+ "						name:'fassj', \n" 
//			+ "\t\t\t\t\t            anchor: '57%',\n" 
//			+ "    	 				listeners:{change:function(own,newValue,oldValue) {\n document.getElementById('FASSJ').value = newValue.dateFormat('Y-m-d');}},\n"
//			+ "     				value:document.all.item('FASSJ').value \n }"+
			"\t\t\t\t\t        ]\n" + 
			"\t\t\t\t\t    })\n" + 
			"\t\t\t\t]\n" + 
			"\t        });\n" + 
			
			
			" win = new Ext.Window({\n" + 
			" \ttitle: '�������Ϣ',\n" + 
			"             closable:false,\n" + 
			"            width:600,\n" + 
			"            height:493,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            layout: 'border',\n" + 
			"            items: [nav,navtree],\n" + 
			"             buttons: [{\n" + 
			"   \ttext:'����',\n" + 
			"\t   \thandler:function(){\n" + 
			
			"\t   \t if (document.getElementById('JIESR').value==''){\n"+
			"\t   \t    Ext.MessageBox.alert('��ʾ��Ϣ','�����˲���Ϊ�գ���ѡ������ˣ�'); \n" +
			
			"\t   \t }else if (neir.getValue() =='-1'){\n"+
			"\t   \t    Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ���͵����ݣ�');  \n" +
			
			"\t   \t }else{ \n"+
			
			"\t\t  \twin.hide();\n" + 
			"\t\t\tdocument.getElementById('RADIO').value=document.getElementById('rbvalue').value;\n" + 
			"\t\t\tdocument.getElementById('RADIO1').value=document.getElementById('rbvalue1').value;\n" + 
			"\t\t\tdocument.getElementById('NEIR').value=neir.getValue();\n" + 
//			"\t\t\tdocument.getElementById('FASSJ').value=fassj.getValue();\n" + 	
			"\t\t\tdocument.getElementById('XIAOS').value=Xiaos.value;\n" +
			"\t\t\tdocument.getElementById('MIAOZ').value=Miaoz.value;\n" +
			"\t\t\tdocument.getElementById('XIAOS1').value=xiaos1.value;\n" +
			"\t\t\tdocument.getElementById('MIAOZ1').value=miaoz1.value;\n" +
			"\t\t\tdocument.getElementById('XIAOS2').value=xiaos2.value;\n" +
			"\t\t\tdocument.getElementById('MIAOZ2').value=miaoz2.value;\n" +
			"\t\t\tdocument.getElementById('LEIB').value=zhouqlx.getRawValue();\n" +
			
			"\t\t\tdocument.getElementById('SaveButton').click();\n" + 
			"\t  \t}}\n" + 
			"\t},{\n" + 
			"\t   text: 'ȡ��',\n" + 
			"\t   handler: function(){\n" + 
			"\t     win.hide();\n" + 
			"\t   }\n" + 
			"\t}]\n" + 
			" });\n";
		
		StringBuffer cpb1 = new StringBuffer();
		cpb1.append("function(){ if(!win){"+Strtmpfunction+"}").append(
				"win.show(this);	\n}");
		GridButton cpr1 = new GridButton("���", cpb1.toString());
		cpr1.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(cpr1);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "Save1Button");
		
		setExtGrid(egu);
		con.Close();
	}
	
	
	//����
	public IDropDownBean getNeirDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getNeirDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setNeirDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setNeirDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getNeirDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNeirDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getNeirDropDownModels() {
		String sql = "select id,miaos from duanxdypzb";
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
		return;
	}
	
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString13(rbvalue);
	}
	
	public String getRbvalue1() {
		return ((Visit) this.getPage().getVisit()).getString14();
	}

	public void setRbvalue1(String rbvalue1) {
		((Visit) this.getPage().getVisit()).setString14(rbvalue1);
	}
	//������ʼ
	
//	����
	private String Tians="1";

	public String getTians() {
		return Tians;
	}

	public void setTians(String tians) {
		Tians = tians;
	}
	
//	������
	private String Zhouqs="1";

	public String getZhouqs() {
		return Zhouqs;
	}

	public void setZhouqs(String zhouqs) {
		Zhouqs = zhouqs;
	}
	
	
	//Сʱ
	private static IPropertySelectionModel _XiaosDownModel;
    public IPropertySelectionModel getXiaosDownModel() {
        if (_XiaosDownModel == null) {
            getXiaosDownModels();
        }
        return _XiaosDownModel;
    }
    
	private IDropDownBean _XiaosDownValue;
	
    public IDropDownBean getXiaosDownValue() {
        if (_XiaosDownValue == null) {
            int _xiaos = DateUtil.getHour(new Date());
            for (int i = 0; i < getXiaosDownModel().getOptionCount(); i++) {
                Object obj = getXiaosDownModel().getOption(i);
                if (_xiaos == ((IDropDownBean) obj).getId()) {
                    _XiaosDownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _XiaosDownValue;
    }
	
    public void setXiaosDownValue(IDropDownBean Value) {
    	if  (_XiaosDownValue!=Value){
    		_XiaosDownValue = Value;
    	}
    }

    public IPropertySelectionModel getXiaosDownModels() {
        List listXiaosDown = new ArrayList();
        int i;
        for (i = 0; i <= 23; i++) {
            listXiaosDown.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _XiaosDownModel = new IDropDownModel(listXiaosDown);
        return _XiaosDownModel;
    }

    public void setXiaosDownModel(IPropertySelectionModel _value) {
        _XiaosDownModel = _value;
    }
    
//  ����
	private static IPropertySelectionModel _MiaozDownModel;
    public IPropertySelectionModel getMiaozDownModel() {
        if (_MiaozDownModel == null) {
            getMiaozDownModels();
        }
        return _MiaozDownModel;
    }
    
	private IDropDownBean _MiaozDownValue;
	
    public IDropDownBean getMiaozDownValue() {
        if (_MiaozDownValue == null) {
            int _Miaoz = DateUtil.getMinutes(new Date());
            for (int i = 0; i < getMiaozDownModel().getOptionCount(); i++) {
                Object obj = getMiaozDownModel().getOption(i);
                if (_Miaoz == ((IDropDownBean) obj).getId()) {
                    _MiaozDownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _MiaozDownValue;
    }
	
    public void setMiaozDownValue(IDropDownBean Value) {
    	if  (_MiaozDownValue!=Value){
    		_MiaozDownValue = Value;
    	}
    }

    public IPropertySelectionModel getMiaozDownModels() {
        List listMiaozDown = new ArrayList();
//        int i;
//        for (i = 0; i <= 59; i++) {
//            listMiaozDown.add(new IDropDownBean(i, String.valueOf(i)));
//        }
        listMiaozDown.add(new IDropDownBean(0, String.valueOf(0)));
        listMiaozDown.add(new IDropDownBean(10, String.valueOf(10)));
        listMiaozDown.add(new IDropDownBean(20, String.valueOf(20)));
        listMiaozDown.add(new IDropDownBean(30, String.valueOf(30)));
        listMiaozDown.add(new IDropDownBean(40, String.valueOf(40)));
        listMiaozDown.add(new IDropDownBean(50, String.valueOf(50)));
        _MiaozDownModel = new IDropDownModel(listMiaozDown);
        return _MiaozDownModel;
    }

    public void setMiaozDownModel(IPropertySelectionModel _value) {
        _MiaozDownModel = _value;
    }
    
//	����
	public IDropDownBean getLeibDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeibDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeibDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeibDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeibDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeibDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeibDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "����"));
		list.add(new IDropDownBean(2, "Сʱ"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
    
//	Сʱ
	private static IPropertySelectionModel _Xiaos1DownModel;
    public IPropertySelectionModel getXiaos1DownModel() {
        if (_Xiaos1DownModel == null) {
            getXiaos1DownModels();
        }
        return _Xiaos1DownModel;
    }
    
	private IDropDownBean _Xiaos1DownValue;
	
    public IDropDownBean getXiaos1DownValue() {
        if (_Xiaos1DownValue == null) {
            int _xiaos = DateUtil.getHour(new Date());
            for (int i = 0; i < getXiaos1DownModel().getOptionCount(); i++) {
                Object obj = getXiaos1DownModel().getOption(i);
                if (_xiaos == ((IDropDownBean) obj).getId()) {
                    _Xiaos1DownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _Xiaos1DownValue;
    }
	
    public void setXiaos1DownValue(IDropDownBean Value) {
    	if  (_Xiaos1DownValue!=Value){
    		_Xiaos1DownValue = Value;
    	}
    }

    public IPropertySelectionModel getXiaos1DownModels() {
        List listXiaos1Down = new ArrayList();
        int i;
        for (i = 1; i <= 24; i++) {
            listXiaos1Down.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _Xiaos1DownModel = new IDropDownModel(listXiaos1Down);
        return _Xiaos1DownModel;
    }

    public void setXiaos1DownModel(IPropertySelectionModel _value) {
        _Xiaos1DownModel = _value;
    }
    
//  ����
	private static IPropertySelectionModel _Miaoz1DownModel;
    public IPropertySelectionModel getMiaoz1DownModel() {
        if (_Miaoz1DownModel == null) {
            getMiaoz1DownModels();
        }
        return _Miaoz1DownModel;
    }
    
	private IDropDownBean _Miaoz1DownValue;
	
    public IDropDownBean getMiaoz1DownValue() {
        if (_Miaoz1DownValue == null) {
            int _Miaoz = DateUtil.getMinutes(new Date());
            for (int i = 0; i < getMiaoz1DownModel().getOptionCount(); i++) {
                Object obj = getMiaoz1DownModel().getOption(i);
                if (_Miaoz == ((IDropDownBean) obj).getId()) {
                    _Miaoz1DownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _Miaoz1DownValue;
    }
	
    public void setMiaoz1DownValue(IDropDownBean Value) {
    	if  (_Miaoz1DownValue!=Value){
    		_Miaoz1DownValue = Value;
    	}
    }

    public IPropertySelectionModel getMiaoz1DownModels() {
        List listMiaozDown = new ArrayList();
        int i;
        for (i = 1; i <= 60; i++) {
            listMiaozDown.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _Miaoz1DownModel = new IDropDownModel(listMiaozDown);
        return _Miaoz1DownModel;
    }

    public void setMiaoz1DownModel(IPropertySelectionModel _value) {
        _Miaoz1DownModel = _value;
    }
    
//	Сʱ
	private static IPropertySelectionModel _Xiaos2DownModel;
    public IPropertySelectionModel getXiaos2DownModel() {
        if (_Xiaos2DownModel == null) {
            getXiaos2DownModels();
        }
        return _Xiaos2DownModel;
    }
    
	private IDropDownBean _Xiaos2DownValue;
	
    public IDropDownBean getXiaos2DownValue() {
        if (_Xiaos2DownValue == null) {
            int _xiaos = DateUtil.getHour(new Date());
            for (int i = 0; i < getXiaos2DownModel().getOptionCount(); i++) {
                Object obj = getXiaos2DownModel().getOption(i);
                if (_xiaos == ((IDropDownBean) obj).getId()) {
                    _Xiaos2DownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _Xiaos2DownValue;
    }
	
    public void setXiaos2DownValue(IDropDownBean Value) {
    	if  (_Xiaos2DownValue!=Value){
    		_Xiaos2DownValue = Value;
    	}
    }

    public IPropertySelectionModel getXiaos2DownModels() {
        List listXiaos2Down = new ArrayList();
        int i;
        for (i = 1; i <= 24; i++) {
            listXiaos2Down.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _Xiaos2DownModel = new IDropDownModel(listXiaos2Down);
        return _Xiaos2DownModel;
    }

    public void setXiaos2DownModel(IPropertySelectionModel _value) {
        _Xiaos2DownModel = _value;
    }
    
//  ����
	private static IPropertySelectionModel _Miaoz2DownModel;
    public IPropertySelectionModel getMiaoz2DownModel() {
        if (_Miaoz2DownModel == null) {
            getMiaoz2DownModels();
        }
        return _Miaoz2DownModel;
    }
    
	private IDropDownBean _Miaoz2DownValue;
	
    public IDropDownBean getMiaoz2DownValue() {
        if (_Miaoz2DownValue == null) {
            int _Miaoz = DateUtil.getMinutes(new Date());
            for (int i = 0; i < getMiaoz2DownModel().getOptionCount(); i++) {
                Object obj = getMiaoz2DownModel().getOption(i);
                if (_Miaoz == ((IDropDownBean) obj).getId()) {
                    _Miaoz2DownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _Miaoz2DownValue;
    }
	
    public void setMiaoz2DownValue(IDropDownBean Value) {
    	if  (_Miaoz2DownValue!=Value){
    		_Miaoz2DownValue = Value;
    	}
    }

    public IPropertySelectionModel getMiaoz2DownModels() {
        List listMiaozDown = new ArrayList();
        int i;
        for (i = 1; i <= 60; i++) {
            listMiaozDown.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _Miaoz2DownModel = new IDropDownModel(listMiaozDown);
        return _Miaoz2DownModel;
    }

    public void setMiaoz2DownModel(IPropertySelectionModel _value) {
        _Miaoz2DownModel = _value;
    }
    
    //���������
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		���������Ĳ�ѯSQL
		String sql ="";
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			sql="select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by fuid=prior id order SIBLINGS by mingc )";

		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			sql="select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid,shangjgsid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid,0 as shangjgsid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by (fuid=prior id or shangjgsid=prior id) order SIBLINGS by mingc )";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			sql="select id,decode(jib,1,'��',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by fuid=prior id order SIBLINGS by mingc )";
		}
		
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		sql = 
			"select id, mingc, fuid, 0 dc\n" +
			"  from diancxxb\n" + 
			" where\n" + 
			"  id not in (select distinct diancxxb_id\n" + 
			"  from lianxrb l, diancxxb d\n" + 
			" where l.diancxxb_id = d.id and d.id in (select id\n" + 
			"\t\t\t from(\n" + 
			"\t\t\t select id from diancxxb\n" + 
			"\t\t\t start with id="+getTreeid()+"\n" + 
			"\t\t\t connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			"\t\t\t )\n" + 
			"\t\t\t union\n" + 
			"\t\t\t select id\n" + 
			"\t\t\t from diancxxb\n" + 
			"\t\t\t where id="+getTreeid()+"))\n" + 
			" order by fuid desc";


		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode tmp;
		while(rsl.next()){
			tmp = (TreeNode)node.getNodeById(rsl.getString("id"));
			if(tmp!=null && tmp.isLeaf()){
				tmp.remove();
			}
		}
		rsl.close();
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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

	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)), 0, DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {

		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	
	boolean riqi1change=false;
	private String riqi1;
	public String getRiqi1() {
		if(riqi1==null||riqi1.equals("")){
			riqi1=DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}
	public void setRiqi1(String riqi1) {
		
		if(this.riqi1!=null &&!this.riqi1.equals(riqi1)){
			this.riqi1 = riqi1;
			riqi1change=true;
		}
		 
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString12("");
			this.setTreeid(null);
			visit.setString13("");
			visit.setString14("");
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			setJiesr("");
			setNeir("");
			setFujdh("");
			setFassj("");
			setTbmsg(null);
			initNavigation();
		}
		initNavigation();
		getSelectData();
	}
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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