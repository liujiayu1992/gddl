package com.zhiren.dc.zhuangh.shulgl;

import java.util.Date;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�songy
 * ʱ�䣺2011-4-27
 * ������
 *     submit�����û��ˢ�£�
 */
public class Haiyydlrsh extends BasePage implements PageValidateListener {
	
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
	}

//	 ������
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			setRiqi(DateUtil.FormatDate(new Date()));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	
	private String Changerq;

	public String getChangerq() {
		return Changerq;
	}

	public void setChangerq(String changerq) {
		Changerq = changerq;
	}
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}

    //ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save2Chick = false;
	public void Save2Button(IRequestCycle cycle) {
		_Save2Chick = true;
	}
	
	private boolean _Save1Chick = false;
	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	
	private boolean _MeicChick = false;
	public void MeicButton(IRequestCycle cycle) {
		_MeicChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			
			Save();
			getSelectData();
		}
		if (_Save2Chick) {
			_Save2Chick = false;
			
			Save(cycle);
			getSelectData();
		}
		
		if (_Save1Chick) {
			_Save1Chick = false;
			
			Save1();
			getSelectData();
		}
	}
//	����ú��
	public void Save(IRequestCycle cycle) {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		visit.SetStringBuffer2(new StringBuffer(getChangeid()));
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){

			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
//		if(rsl.getRows()>1){
//			setMsg("�����һ����¼");
//			return;
//		}
		if (rsl == null) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		
		while (rsl.next()) {
			if(getChangeid()==rsl.getString("id")||getChangeid().equals(rsl.getString("id"))){
			//fahb
			long diancxxb_id=visit.getDiancxxb_id();
			// ���õ糧��Ϣid
			visit.setLong1(diancxxb_id);
			
			String gongysb_id=rsl.getString("gongysb_id");
			// ���ù�Ӧ��id
			visit.setString1(gongysb_id);
			
			String zhilb_id=rsl.getString("zhilb_id");
			// ��������id
			visit.setString2(zhilb_id);
			
			String meikxxb_id=rsl.getString("meikxxb_id");
			//����ú����Ϣid
			visit.setString3(meikxxb_id);
			
			String pinzb_id=rsl.getString("pinzb_id");
			visit.setString4(pinzb_id);//����Ʒ��id
			
			String faz_id=rsl.getString("faz_id");
			visit.setString5(faz_id);//���÷�վid
			
			String daoz_id=rsl.getString("daoz_id");
			visit.setString6(daoz_id);//���õ�վid
			
			String hetb_id=rsl.getString("hetb_id");
			visit.setString7(hetb_id);//���ú�ͬid
			
			String luncxxb_id=rsl.getString("luncxxb_id");
			visit.setString8(luncxxb_id);//�����ִ�id
			
			String jihkjb_id="4";//�޼ƻ��ھ�
			
			String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
			visit.setString9(rsl.getDateString("fahrq"));//���÷�������
			
			String daohrq=endriq;
			visit.setString10(daohrq);//���õ�������
//			
			String yunsfsb_id=rsl.getString("yunsfsb_id");
			visit.setString11(yunsfsb_id);//�������䷽ʽid
			
			String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
			visit.setString12(rsl.getDateString("kaobrq"));//���ÿ�������
			
			String yundh=rsl.getString("yundh");
			ResultSetList rs;
			String sql=
				"select max(f.yundh)+1 yund from fahb f where to_char(f.daohrq,'yyyy-mm-dd')='"+getRiqi()+"' and f.yundh like '"+getRiqi().replaceAll("-", "")+"%'";

			if("".equals(yundh)||yundh==null){
				rs=con.getResultSetList(sql);
				while(rs.next()){
					if("".equals(rs.getString("yund"))||rs.getString("yund")==null){
						
						
						yundh=getRiqi().replaceAll("-", "")+"00";
					}else{
						yundh=rs.getString("yund");
					}
				}
			}
			visit.setString14(rsl.getString("dunw"));//
			
			visit.setString13(yundh);
			String xiemkssj="to_date('"+rsl.getDateTimeString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			visit.SetStringBuffer10(new StringBuffer(rsl.getDateTimeString("xiemkssj")));
			String xiemjssj="to_date('"+rsl.getDateTimeString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			visit.setString15(rsl.getDateTimeString("xiemjssj"));//жú����ʱ��
			
			String zhuanggkssj="to_date('"+rsl.getDateTimeString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			visit.setString16(rsl.getDateTimeString("zhuanggkssj"));//װ�ۿ�ʼʱ��
			
			String zhuanggjssj="to_date('"+rsl.getDateTimeString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			visit.setString17(rsl.getString("zhuanggjssj"));//װ�۽���ʱ��
			
			String chec=rsl.getString("chec");
			visit.SetStringBuffer1(new StringBuffer(chec));// ���ó���
			
			double maoz=rsl.getDouble("maoz");
			visit.setDouble1(maoz);//����ë��
			

			double piz=rsl.getDouble("piz");
			visit.setDouble2(piz); //����Ƥ��
			
			double biaoz=rsl.getDouble("biaoz");
			visit.setDouble3(biaoz);//���ñ���
			
			double jingz=maoz-piz;
			double yingd=0;
			double yingk=0;
			double yuns=0;
			if ((maoz-piz-biaoz)<0) {
				if (Math.abs((maoz-piz-biaoz))>(biaoz*0.015)){
					yuns=(biaoz*0.015);
					yingk=0-(Math.abs((maoz-piz-biaoz))-(biaoz*0.015));
				}else{
					yuns=maoz-piz-biaoz;
					yingk=0;
				}
			}else{
				yuns=0;
				yingk=0;
				yingd=maoz-piz-biaoz;
			}
			
			double yunsl=0;
			double koud=0;
			double kous=0;
			double kouz=0;
			double koum=0;
			double zongkd=0;
			double sanfsl=rsl.getDouble("sanfsl");
			visit.setDouble4(sanfsl);// ������������
			
			double ches=0;
			long tiaozbz=0;
			long yansbhb_id=0;
//			long lie_id=0;
			long yuandz_id=0;
			long yuanshdwb_id=0;
			long kuangfzlb_id=0;
			long liucb_id=0;
			long liucztb_id=1;
			long hedbz=0;
			String  meicb_id=rsl.getString("meicb_id");
//			visit.SetStringBuffer2(new StringBuffer(meicb_id));
			String duowmc=rsl.getString("duowmc");
			visit.SetStringBuffer3(new StringBuffer(getRiqi()));//��ʼ����
			
			String beiz="";
			//zhilb 
			String huaybh = rsl.getString("huaybh");
			if(huaybh==null||huaybh.equals(""))
			{
				visit.SetStringBuffer4(new StringBuffer(""));
			}else{
				visit.SetStringBuffer4(new StringBuffer(huaybh));
				
			}
			
			if ("0".equals(rsl.getString("id"))) {
				
				
				//������ʱ��
				String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
				visit.SetStringBuffer5(new StringBuffer(zhillsb_id));//������������id
				
				//������
				String xuh = creatcaiybm(con,endriq,diancxxb_id);//���ɲ�������
				visit.SetStringBuffer6(new StringBuffer(xuh));//�������ɲ������
				
				String caiyb_id = MainGlobal.getNewID(diancxxb_id);
				visit.SetStringBuffer7(new StringBuffer(caiyb_id));//�������ɲ���id
				
				// ���뷢����
				
				String id =MainGlobal.getNewID(diancxxb_id);
				visit.SetStringBuffer8(new StringBuffer(id));//�������ɷ���id
				
				visit.SetStringBuffer9(new StringBuffer(getChangeid()));

			
				
			}else{
				visit.SetStringBuffer9(new StringBuffer(getChangeid()));
			}
			
		}	
			
		}
		
		if(rsl.getRows()==0){
			StringBuffer sbsql=new StringBuffer();
			sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
			sbsql.append("       gongysb.mingc as gongysb_id,\n");
//			sbsql.append("       meikxxb_id, \n");
			sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
			sbsql.append("       yunsfsb.mingc as yunsfsb_id, \n");
			sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
			sbsql.append("       hetb.hetbh hetb_id, \n");
			//sbsql.append("       fahb.faz_id, \n");
			sbsql.append("       chezxxb.mingc as faz_id, \n");
			sbsql.append(" DECODE ((SELECT cz.quanc\n ");
			sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
			sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
			sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '��ѡ��',\n");
			sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
			sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
			//sbsql.append("   	 fahb.pinzb_id, \n");
			sbsql.append("       pinzb.mingc as pinzb_id, \n");
			sbsql.append("       luncxxb.dunw,meicb.mingc as meicb_id,fahb.duowmc, \n");
			sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl,\n");//fahb.jingz,
			//sbsql.append("       fahb.maoz, \n");
			//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
			sbsql.append("   fahrq, \n");// 
			sbsql.append("   kaobrq kaobrq, \n");
			sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
			sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj, \n");
			sbsql.append("   to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
			sbsql.append("   to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//			sbsql.append("   luncxxb.mingc as huocmc, \n");
			sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb \n");
			sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
			sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
			sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)  \n");
			sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
			sbsql.append("   and yunsfsb.mingc='����' and fahb.jiesb_id=0 \n");
			sbsql.append(" and fahb.meicb_id=meicb.id(+) \n");
			sbsql.append("   and fahb.id="+getChangeid()+"\n");
//			sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n");
			sbsql.append("order by fahb.daohrq \n");
			String qufh="select * from fahb where id="+getChangeid();//
			rsl=con.getResultSetList(sbsql.toString());
			while(rsl.next()){
//			fahb
				long diancxxb_id=visit.getDiancxxb_id();
				// ���õ糧��Ϣid
				visit.setLong1(diancxxb_id);
				
				String gongysb_id=rsl.getString("gongysb_id");
				// ���ù�Ӧ��id
				visit.setString1(gongysb_id);
				
				String zhilb_id=rsl.getString("zhilb_id");
				// ��������id
				visit.setString2(zhilb_id);
				
				String meikxxb_id=rsl.getString("meikxxb_id");
				//����ú����Ϣid
				visit.setString3(meikxxb_id);
				
				String pinzb_id=rsl.getString("pinzb_id");
				visit.setString4(pinzb_id);//����Ʒ��id
				
				String faz_id=rsl.getString("faz_id");
				visit.setString5(faz_id);//���÷�վid
				
				String daoz_id=rsl.getString("daoz_id");
				visit.setString6(daoz_id);//���õ�վid
				
				String hetb_id=rsl.getString("hetb_id");
				visit.setString7(hetb_id);//���ú�ͬid
				
				String luncxxb_id=rsl.getString("luncxxb_id");
				visit.setString8(luncxxb_id);//�����ִ�id
				visit.setString14(rsl.getString("dunw"));
				String jihkjb_id="4";//�޼ƻ��ھ�
				
				String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
				visit.setString9(rsl.getDateString("fahrq"));//���÷�������
				
				String daohrq=endriq;
				visit.setString10(daohrq);//���õ�������
				
				String yunsfsb_id=rsl.getString("yunsfsb_id");
				visit.setString11(yunsfsb_id);//�������䷽ʽid
				
				String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
				visit.setString12(rsl.getDateString("kaobrq"));//���ÿ�������
				
				String yundh=rsl.getString("yundh");
				visit.setString13(yundh);
//				String xiemkssj="to_date('"+rsl.getDateTimeString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
				visit.SetStringBuffer10(new StringBuffer(rsl.getDateTimeString("xiemkssj")));//����жú��ʼʱ��
				
//				String xiemjssj="to_date('"+rsl.getDateTimeString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
				visit.setString15(rsl.getDateTimeString("xiemjssj"));//жú����ʱ��
				
//				String zhuanggkssj="to_date('"+rsl.getDateTimeString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.setString16(rsl.getDateTimeString("zhuanggkssj"));//װ�ۿ�ʼʱ��
//				
//				String zhuanggjssj="to_date('"+rsl.getDateTimeString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.setString17(rsl.getDateTimeString("zhuanggjssj"));//װ�۽���ʱ��
				
				String chec=rsl.getString("chec");
				visit.SetStringBuffer1(new StringBuffer(chec));// ���ó���
				
				double maoz=rsl.getDouble("maoz");
				visit.setDouble1(maoz);//����ë��
				
				
				double piz=rsl.getDouble("piz");
				visit.setDouble2(piz); //����Ƥ��
				
				double biaoz=rsl.getDouble("biaoz");
				visit.setDouble3(biaoz);//���ñ���
				
				double sanfsl=rsl.getDouble("sanfsl");
				visit.setDouble4(sanfsl);// ������������
				visit.SetStringBuffer3(new StringBuffer(getRiqi()));//��ʼ����
				
			
				//zhilb 
				String huaybh = rsl.getString("huaybh");
				if(huaybh==null||huaybh.equals(""))
				{
					visit.SetStringBuffer4(new StringBuffer(""));
				}else{
					visit.SetStringBuffer4(new StringBuffer(huaybh));
					
				}
				
				visit.SetStringBuffer9(new StringBuffer(getChangeid()));
			}
		}
		con.commit();
		con.Close();
		visit.SetStringBuffer20(new StringBuffer("shenh"));

		cycle.activate("Haiyydxf_zh");
	}
	public void Meic(IRequestCycle cycle) {
		
Visit visit = (Visit) this.getPage().getVisit();
visit.setLong1(Integer.parseInt(getChangeid()));
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		
		while (rsl.next()) {
			
		}
		//������Ҫ�鿴ʱ������
//		visit.setString15(this.getRiqi());
//		cycle.activate("Shujdr");
	}
	//ȡ�����Ĳ���XUH
	public String creatcaiybm(JDBCcon con,String date,long diancxxb_id) {
		String sql="select To_number(Substr(max(bianm), 9, 2))+1 as xuh from caiyb " +
				"where caiyrq=to_date('"+date+"','yyyy-mm-dd') and zhilb_id="+diancxxb_id+"";
		
		String xuh="01";
		ResultSetList rsl = con.getResultSetList(sql);
		
		while(rsl.next()){
			int xuh1=rsl.getInt("xuh");
			if (xuh1<10){
				xuh="0"+xuh1;
			}
		}
		rsl.close();
		
		return xuh;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//���淽��
	public void Save1() {
		

		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
//			//������
//			String zhil ="delete from zhilb " +
//			"where id="+rsl.getString("zhilb_id")+";\n";
//			sb.append(zhil.toString());
//			//������
//			String caiy ="delete from caiyb " +
//			"where zhilb_id="+rsl.getString("zhilb_id")+";\n";
//			sb.append(caiy.toString());
//			//������ʱ��
//			String dd ="delete from zhillsb " +
//			"where id="+rsl.getString("zhilb_id")+";\n";
//			sb.append(dd.toString());
			//������
			String cc ="delete from fahb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(cc.toString());
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		String jihkjb_id="0";
		String jihkjsql="select id from jihkjb where mingc ='�ص㶩��'";
		if (con.getHasIt(jihkjsql)){
			jihkjb_id="(select id from jihkjb where mingc ='�ص㶩��')";
		}
		while (rsl.next()) {
			//fahb
			if(rsl.getString("jmingc")==null||"".equals(rsl.getString("jmingc"))){
				
			}else{
				
			
				jihkjb_id=String.valueOf((getExtGrid().getColumn("jmingc").combo).getBeanId(rsl.getString("jmingc")));
			}
			long diancxxb_id=visit.getDiancxxb_id();
			String gongysb_id=rsl.getString("gongysb_id");
			String zhilb_id=rsl.getString("zhilb_id");
			String meikxxb_id=rsl.getString("meikxxb_id");
			String pinzb_id=rsl.getString("pinzb_id");
			String faz_id=rsl.getString("faz_id");
			String daoz_id=rsl.getString("daoz_id");
			String hetb_id=rsl.getString("hetb_id");
			String luncxxb_id=rsl.getString("luncxxb_id");
			
			String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
			String daohrq=endriq;
			String yunsfsb_id=rsl.getString("yunsfsb_id");
			String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
//			String yundh=rsl.getString("yundh");
			String yundh=rsl.getString("yundh");
			ResultSetList rs;
			String sql=
				"select max(f.yundh)+1 yund from fahb f where to_char(f.daohrq,'yyyy-mm-dd')='"+getRiqi()+"' and f.yundh like '"+getRiqi().replaceAll("-", "")+"%'";

			if("".equals(yundh)||yundh==null){
				rs=con.getResultSetList(sql);
				while(rs.next()){
					if("".equals(rs.getString("yund"))||rs.getString("yund")==null){
						
						
						yundh=getRiqi().replaceAll("-", "")+"00";
					}else{
						yundh=rs.getString("yund");
					}
				}
			}
			
			visit.setString13(yundh);
			String xiemkssj="to_date('"+rsl.getString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String xiemjssj="to_date('"+rsl.getString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String zhuanggkssj="to_date('"+rsl.getString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
			String zhuanggjssj="to_date('"+rsl.getString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
			
			String chec=rsl.getString("chec");
			double maoz=rsl.getDouble("maoz");
			double piz=rsl.getDouble("piz");
			double biaoz=rsl.getDouble("biaoz");
			double sanfsl=rsl.getDouble("sanfsl");
//			double jingz=maoz-piz-biaoz;
			double jingz=sanfsl;
			double yingd=0;
			double yingk=0;
			double yuns=0;
			double laimsl=0;
			double laimzl=0;
			double laimkc=0;
//			if ((maoz-piz-biaoz)<0) {
//				if ((maoz-piz-biaoz)>(biaoz*0.012)){
//					yuns=(biaoz*0.012);
//					yingk=(maoz-piz-biaoz)-(biaoz*0.012);
//				}else{
//					yuns=maoz-piz-biaoz;
//					yingk=0;
//				}
//			}else{
//				yuns=0;
//				yingk=0;
//				yingd=maoz-piz-biaoz;
//			}
			ResultSetList rs_laim;
			String tongjkj="";
			String sql_laim=
				" select tongjkj from meiltjkjb where zhuangt=1 and gongysb_id="+(getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id);
			rs_laim=con.getResultSetList(sql_laim);
			while(rs_laim.next()){
				tongjkj=rs_laim.getString("tongjkj");
			}
			//Ʊ�ض�Ӧ������ biaoz
			if("Ʊ��".equals(tongjkj)){
				laimsl=biaoz;
				laimzl=biaoz;
				laimkc=biaoz;
				if(biaoz-sanfsl>0){//����
					if((biaoz-sanfsl)>(biaoz*0.012)){
						yingk=(biaoz-sanfsl)-(biaoz*0.012);
						yuns=biaoz*0.012;
					}else{
						yingk=0;
						yuns=biaoz-sanfsl;
						yingd=0;
					}
				}else if(biaoz-sanfsl==0){
					yuns=0;
					yingk=0;
					yingd=0;
				}else if(biaoz-sanfsl<0){
					yingd=sanfsl-biaoz;
					yuns=0;
					yingk=0;
				}
			}else{//С�ƻ��ھ���
				laimsl=sanfsl;
				laimzl=sanfsl;
				laimkc=sanfsl;
				
				if(biaoz-sanfsl>0){
					//
					yuns=0;
					yingk=biaoz-sanfsl;
					yingd=0;
				}else if(biaoz-sanfsl==0){
					yuns=0;yingk=0;yingd=0;
				}else if(biaoz-sanfsl<0){
					yuns=0;
					yingk=0;
					yingd=sanfsl-biaoz;
				}
			}
			
			double yunsl=0;
			double koud=0;
			double kous=0;
			double kouz=0;
			double koum=0;
			double zongkd=0;
			double ches=1;
			long tiaozbz=0;
			long yansbhb_id=0;
//			long lie_id=0;
			long yuandz_id=0;
			long yuanshdwb_id=0;
			long kuangfzlb_id=0;
			long liucb_id=0;
			long liucztb_id=0;
			long hedbz=0;
			String beiz="";
			//zhilb 
			String huaybh = rsl.getString("huaybh");
			
			if ("0".equals(rsl.getString("id"))) {
				
				//������ʱ��
				String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//				sb.append("insert into zhillsb (id,zhilb_id,shenhzt,huaylbb_id,huaylb)");
//				sb.append("select "+zhillsb_id+","+zhillsb_id+",0,id,mingc from leibb where mingc ='������';\n");
//				
				//������
				String xuh = creatcaiybm(con,endriq,diancxxb_id);//���ɲ�������
				
//				String caiyb_id = MainGlobal.getNewID(diancxxb_id);
//				sb.append("insert into caiyb (id,zhilb_id,cunywzb_id,bianm,caiyrq,xuh) values ( \n");
//				sb.append(caiyb_id).append(",").append(zhillsb_id).append(",0,");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("to_date('"+endriq+"','yyyy-mm-dd'),"+xuh+");\n");
				
				//zhuanmb
//				//��������
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=1),'');\n");
//				//��������
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=2),'');\n");
//				//�������
//				sb.append("insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id,zidybm) values (");
//				sb.append("getnewid("+diancxxb_id+"),").append(zhillsb_id).append(",");
//				sb.append("(select to_char(to_date('"+endriq+"','yyyy-mm-dd'),'yyyymmdd')||'"+xuh+"' from dual),");
//				sb.append("(select id from zhuanmlb where jib=3),'');\n");
				
//				//����������
				//String zhilbid = MainGlobal.getNewID(diancxxb_id);
//				sb.append("insert into zhilb(id,huaybh,caiyb_id,huaysj)values(\n");
//				sb.append(zhillsb_id);
//				sb.append(",'").append(huaybh).append("',0");
//				sb.append(",sysdate);\n");
//				
				// ���뷢����
				
				
				String id =MainGlobal.getNewID(diancxxb_id);
				
				sb.append("insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, \n");
				sb.append("pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id,  \n");
				sb.append("zhilb_id, jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz,  \n");
				sb.append("yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd,  \n");
				sb.append("sanfsl,laimsl,laimzl,laimkc, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id,  \n");
				sb.append("yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz,  \n");
				sb.append("beiz,luncxxb_id,kaobrq,YUNDH) \n");//,XIEMKSSJ,XIEMJSSJ,ZHUANGGKSSJ,ZHUANGGJSSJ
				sb.append("values (\n");
				sb.append(id).append(",").append(id).append(",").append(visit.getDiancxxb_id()).append(",");
				sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id)).append(",");
				sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(meikxxb_id)).append(",");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(pinzb_id)).append(",");
				sb.append((getExtGrid().getColumn("faz_id").combo).getBeanId(faz_id)).append(",");
				sb.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(daoz_id)).append(",");
				sb.append(jihkjb_id).append(",");
				sb.append(fahrq);
				sb.append(",to_date('").append(daohrq).append("','yyyy-mm-dd'),");
				sb.append((getExtGrid().getColumn("hetb_id").combo).getBeanId(hetb_id)).append(",");
				sb.append(zhillsb_id).append(",0,");
				sb.append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(yunsfsb_id)).append(",");
				sb.append(chec).append(",").append(maoz).append(",").append(piz).append(",");
				sb.append(jingz).append(",").append(biaoz).append(",").append(yingd).append(",");
				sb.append(yingk).append(",").append(yuns).append(",").append(yunsl).append(",");
				sb.append(koud).append(",").append(kous).append(",").append(kouz).append(",");
				sb.append(koum).append(",").append(zongkd).append(",").append(sanfsl).append(",").append(",")
				.append(laimsl).append(",").append(laimzl).append(",").append(laimkc).append(",");
				sb.append(ches).append(",").append(tiaozbz).append(",").append(yansbhb_id).append(",");
				sb.append(id).append(",").append(yuandz_id).append(",").append(yuanshdwb_id).append(",");
				sb.append(kuangfzlb_id).append(",").append(liucb_id).append(",").append(liucztb_id).append(",").append(hedbz).append(",'");
				sb.append(beiz).append("',");
				sb.append((getExtGrid().getColumn("luncxxb_id").combo).getBeanId(luncxxb_id)).append(",");
				sb.append(kaobrq).append(",'").append(yundh)
				.append("'")
//				.append(",").append(xiemkssj).append(",");
//				sb.append(xiemjssj).append(",").append(zhuanggkssj).append(",").append(zhuanggjssj)
				.append(");\n");
				
				String chepb_id =MainGlobal.getNewID(diancxxb_id);
				sb.append("insert into chepb(id,cheph,piaojh,yuanmz,maoz,piz,biaoz,yingd,yingk,yuns,ches,fahb_id,lury,sanfsl)values(\n" );
				sb.append(chepb_id).append(",'").append(luncxxb_id).append("','").append(yundh).append("',");
				sb.append(maoz).append(",").append(maoz).append(",").append(piz).append(",").append(biaoz).append(",").append(yingd).append(",").
				append(yingk).append(",").append(yuns).append(",").append("0").append(",").append(id).append(",'").append(visit.getRenymc())
				.append("',").append(sanfsl).append(");");			

				
			}else{
				sb.append("update fahb set meikxxb_id=");
				sb.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(meikxxb_id));
				sb.append(",gongysb_id=");
				sb.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(gongysb_id));
				sb.append(",pinzb_id=");
				sb.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(pinzb_id));
				sb.append(",faz_id=");
				sb.append((getExtGrid().getColumn("faz_id").combo).getBeanId(faz_id));
				sb.append(",hetb_id=");
				sb.append((getExtGrid().getColumn("hetb_id").combo).getBeanId(hetb_id));
				sb.append(",chec=");
				sb.append(chec);
				sb.append(",maoz=").append(maoz);
				sb.append(",sanfsl=").append(sanfsl).append(",laimsl=").append(laimsl).append(",laimzl=").append(laimzl).append(",laimkc=").append(laimkc);
				sb.append(",piz=").append(piz);
				sb.append(",jingz=").append(jingz);
				sb.append(",biaoz=").append(biaoz);
				sb.append(",yundh='").append(yundh).append("'");
				sb.append(", jihkjb_id=").append(jihkjb_id);
				
				if("".equals(rsl.getString("xiemkssj"))||rsl.getString("xiemkssj")==null){
					
				}else{
					
					sb.append(",xiemkssj=").append(xiemkssj);
				}
				if("".equals(rsl.getString("xiemjssj"))||rsl.getString("xiemjssj")==null){
					
				}else{
					
					sb.append(",xiemjssj=").append(xiemjssj);
				}
				if("".equals(rsl.getString("zhuanggkssj"))||rsl.getString("zhuanggkssj")==null){
					
				}else{
					
					sb.append(",zhuanggkssj=").append(zhuanggkssj);
				}
				if("".equals(rsl.getString("zhuanggjssj"))||rsl.getString("zhuanggjssj")==null){
					
				}else{
					
					sb.append(",zhuanggjssj=").append(zhuanggjssj);
				}
//				sb.append("',xiemkssj=").append(xiemkssj);
//				sb.append(",xiemjssj=").append(xiemjssj);
//				sb.append(",zhuanggkssj=").append(zhuanggkssj);
//				sb.append(",zhuanggjssj=").append(zhuanggjssj);
				sb.append(" where fahb.id=").append(rsl.getString("id")).append(";\n");
				
				sb.append("update chepb set cheph='");
				sb.append(luncxxb_id);
				sb.append("',piaojh='");
				sb.append(yundh);
				sb.append("',maoz=");
				sb.append(maoz);
				sb.append(",yuanmz=");
				sb.append(maoz);
				sb.append(",piz=");
				sb.append(piz);
				sb.append(",biaoz=");
				sb.append(biaoz);
				sb.append(",yingk=");
				sb.append(yingk);
				sb.append(",yingd=").append(yingd);
				sb.append(",yuns=").append(yuns).append(",sanfsl=").append(sanfsl).append(" where fahb_id=").append(rsl.getString("id")).append(";\n");
//				sb.append("update zhilb set zhilb.huaybh='");
//				sb.append(huaybh).append("where zhilb.id=(");
//				sb.append("select zhilb_id from fahb where fahb.id=");
//				sb.append(rsl.getString("id")).append(");\n");
//				
//				sb.append("update chepb set cheph='");
//				sb.append(huaybh).append("' where chepb.fahb_id=");
//				sb.append(rsl.getString("id")).append(";\n");
				
			}
			
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);

	
		
	
		
	
	}
	//����ú��
	public void Save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		visit.SetStringBuffer2(new StringBuffer(getChangeid()));
//		sb.append("begin\n");
		
//		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
//		while (rsl.next()){
//
//			
//		}
		
//		rsl = getExtGrid().getModifyResultSet(getChange());
//		if(rsl.getRows()>1){
//			setMsg("�����һ����¼");
//			return;
//		}
//		if (rsl == null) {
//			setMsg("û����Ҫ����ļ�¼��");
//			return;
//		}
		int flag = 0;
		if(!"".equals(getChangeid())||getChangeid()!=null){
			//���±������ݵ�����
//			if(rsl.getString("xiemkssj").equals("")||rsl.getString("xiemkssj")==null){
//				
//				setMsg("жú��ʼʱ��û����д�������");
//				return;
//			}
			if("".equals(getChangerq())||getChangerq()==null){
				
				setMsg("жú����ʱ��û����д�������");
				return;
			}
			
			String fahbid = getChangeid();


			sb.delete(0, sb.length());
			sb.append("update fahb set liucztb_id =1,hedbz = ").append(SysConstant.HEDBZ_YJJ).append(" where id = ").append(fahbid);
			flag = con.getUpdate(sb.toString());
			
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
						+"SQL:"+sb);
				setMsg(ErrorMessage.Shujsh002);
				return;
			}
			sb.delete(0, sb.length());
			
			
		}else{
			
			setMsg("��ѡ����һ������");
			return ;
		}

		
	
		
//		while (rsl.next()) {
//			if(getChangeid()==rsl.getString("id")||getChangeid().equals(rsl.getString("id"))){
//			//fahb
//			long diancxxb_id=visit.getDiancxxb_id();
//			// ���õ糧��Ϣid
//			visit.setLong1(diancxxb_id);
//			
//			String gongysb_id=rsl.getString("gongysb_id");
//			// ���ù�Ӧ��id
//			visit.setString1(gongysb_id);
//			
//			String zhilb_id=rsl.getString("zhilb_id");
//			// ��������id
//			visit.setString2(zhilb_id);
//			
//			String meikxxb_id=rsl.getString("meikxxb_id");
//			//����ú����Ϣid
//			visit.setString3(meikxxb_id);
//			
//			String pinzb_id=rsl.getString("pinzb_id");
//			visit.setString4(pinzb_id);//����Ʒ��id
//			
//			String faz_id=rsl.getString("faz_id");
//			visit.setString5(faz_id);//���÷�վid
//			
//			String daoz_id=rsl.getString("daoz_id");
//			visit.setString6(daoz_id);//���õ�վid
//			
//			String hetb_id=rsl.getString("hetb_id");
//			visit.setString7(hetb_id);//���ú�ͬid
//			
//			String luncxxb_id=rsl.getString("luncxxb_id");
//			visit.setString8(luncxxb_id);//�����ִ�id
//			
//			String jihkjb_id="4";//�޼ƻ��ھ�
//			
//			String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
//			visit.setString9(rsl.getDateString("fahrq"));//���÷�������
//			
//			String daohrq=endriq;
//			visit.setString10(daohrq);//���õ�������
////			
//			String yunsfsb_id=rsl.getString("yunsfsb_id");
//			visit.setString11(yunsfsb_id);//�������䷽ʽid
//			
//			String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
//			visit.setString12(rsl.getDateString("kaobrq"));//���ÿ�������
//			
//			String yundh=rsl.getString("yundh");
//			ResultSetList rs;
//			String sql=
//				"select max(f.yundh)+1 yund from fahb f where to_char(f.daohrq,'yyyy-mm-dd')='"+getRiqi()+"' and f.yundh like '"+getRiqi().replaceAll("-", "")+"%'";
//
//			if("".equals(yundh)||yundh==null){
//				rs=con.getResultSetList(sql);
//				while(rs.next()){
//					if("".equals(rs.getString("yund"))||rs.getString("yund")==null){
//						
//						
//						yundh=getRiqi().replaceAll("-", "")+"00";
//					}else{
//						yundh=rs.getString("yund");
//					}
//				}
//			}
//			visit.setString14(rsl.getString("dunw"));//
//			
//			visit.setString13(yundh);
//			String xiemkssj="to_date('"+rsl.getDateTimeString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
//			visit.SetStringBuffer10(new StringBuffer(rsl.getDateTimeString("xiemkssj")));
//			String xiemjssj="to_date('"+rsl.getDateTimeString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
//			visit.setString15(rsl.getDateTimeString("xiemjssj"));//жú����ʱ��
//			
//			String zhuanggkssj="to_date('"+rsl.getDateTimeString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
//			visit.setString16(rsl.getDateTimeString("zhuanggkssj"));//װ�ۿ�ʼʱ��
//			
//			String zhuanggjssj="to_date('"+rsl.getDateTimeString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
//			visit.setString17(rsl.getString("zhuanggjssj"));//װ�۽���ʱ��
//			
//			String chec=rsl.getString("chec");
//			visit.SetStringBuffer1(new StringBuffer(chec));// ���ó���
//			
//			double maoz=rsl.getDouble("maoz");
//			visit.setDouble1(maoz);//����ë��
//			
//
//			double piz=rsl.getDouble("piz");
//			visit.setDouble2(piz); //����Ƥ��
//			
//			double biaoz=rsl.getDouble("biaoz");
//			visit.setDouble3(biaoz);//���ñ���
//			
//			double jingz=maoz-piz;
//			double yingd=0;
//			double yingk=0;
//			double yuns=0;
//			if ((maoz-piz-biaoz)<0) {
//				if (Math.abs((maoz-piz-biaoz))>(biaoz*0.015)){
//					yuns=(biaoz*0.015);
//					yingk=0-(Math.abs((maoz-piz-biaoz))-(biaoz*0.015));
//				}else{
//					yuns=maoz-piz-biaoz;
//					yingk=0;
//				}
//			}else{
//				yuns=0;
//				yingk=0;
//				yingd=maoz-piz-biaoz;
//			}
//			
//			double yunsl=0;
//			double koud=0;
//			double kous=0;
//			double kouz=0;
//			double koum=0;
//			double zongkd=0;
//			double sanfsl=rsl.getDouble("sanfsl");
//			visit.setDouble4(sanfsl);// ������������
//			
//			double ches=0;
//			long tiaozbz=0;
//			long yansbhb_id=0;
////			long lie_id=0;
//			long yuandz_id=0;
//			long yuanshdwb_id=0;
//			long kuangfzlb_id=0;
//			long liucb_id=0;
//			long liucztb_id=1;
//			long hedbz=0;
//			String  meicb_id=rsl.getString("meicb_id");
////			visit.SetStringBuffer2(new StringBuffer(meicb_id));
//			String duowmc=rsl.getString("duowmc");
//			visit.SetStringBuffer3(new StringBuffer(getRiqi()));//��ʼ����
//			
//			String beiz="";
//			//zhilb 
//			String huaybh = rsl.getString("huaybh");
//			if(huaybh==null||huaybh.equals(""))
//			{
//				visit.SetStringBuffer4(new StringBuffer(""));
//			}else{
//				visit.SetStringBuffer4(new StringBuffer(huaybh));
//				
//			}
//			
//			if ("0".equals(rsl.getString("id"))) {
//				
//				
//				//������ʱ��
//				String zhillsb_id = MainGlobal.getNewID(diancxxb_id);
//				visit.SetStringBuffer5(new StringBuffer(zhillsb_id));//������������id
//				
//				//������
//				String xuh = creatcaiybm(con,endriq,diancxxb_id);//���ɲ�������
//				visit.SetStringBuffer6(new StringBuffer(xuh));//�������ɲ������
//				
//				String caiyb_id = MainGlobal.getNewID(diancxxb_id);
//				visit.SetStringBuffer7(new StringBuffer(caiyb_id));//�������ɲ���id
//				
//				// ���뷢����
//				
//				String id =MainGlobal.getNewID(diancxxb_id);
//				visit.SetStringBuffer8(new StringBuffer(id));//�������ɷ���id
//				
//				visit.SetStringBuffer9(new StringBuffer(getChangeid()));
//
//			
//				
//			}else{
//				visit.SetStringBuffer9(new StringBuffer(getChangeid()));
//			}
//			
//		}	
//			
//		}
		
//		if(rsl.getRows()==0){
//			StringBuffer sbsql=new StringBuffer();
//			sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
//			sbsql.append("       gongysb.mingc as gongysb_id,\n");
////			sbsql.append("       meikxxb_id, \n");
//			sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
//			sbsql.append("       yunsfsb.mingc as yunsfsb_id, \n");
//			sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
//			sbsql.append("       hetb.hetbh hetb_id, \n");
//			//sbsql.append("       fahb.faz_id, \n");
//			sbsql.append("       chezxxb.mingc as faz_id, \n");
//			sbsql.append(" DECODE ((SELECT cz.quanc\n ");
//			sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
//			sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
//			sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '��ѡ��',\n");
//			sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
//			sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
//			//sbsql.append("   	 fahb.pinzb_id, \n");
//			sbsql.append("       pinzb.mingc as pinzb_id, \n");
//			sbsql.append("       luncxxb.dunw,meicb.mingc as meicb_id,fahb.duowmc, \n");
//			sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl,\n");//fahb.jingz,
//			//sbsql.append("       fahb.maoz, \n");
//			//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
//			sbsql.append("   fahrq, \n");// 
//			sbsql.append("   kaobrq kaobrq, \n");
//			sbsql.append("   to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
//			sbsql.append("   to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj, \n");
//			sbsql.append("   to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
//			sbsql.append("   to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
////			sbsql.append("   luncxxb.mingc as huocmc, \n");
//			sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb \n");
//			sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
//			sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
//			sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)  \n");
//			sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
//			sbsql.append("   and yunsfsb.mingc='����' and fahb.jiesb_id=0 \n");
//			sbsql.append(" and fahb.meicb_id=meicb.id(+) \n");
//			sbsql.append("   and fahb.id="+getChangeid()+"\n");
////			sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n");
//			sbsql.append("order by fahb.daohrq \n");
//			String qufh="select * from fahb where id="+getChangeid();//
//			rsl=con.getResultSetList(sbsql.toString());
//			while(rsl.next()){
////			fahb
//				long diancxxb_id=visit.getDiancxxb_id();
//				// ���õ糧��Ϣid
//				visit.setLong1(diancxxb_id);
//				
//				String gongysb_id=rsl.getString("gongysb_id");
//				// ���ù�Ӧ��id
//				visit.setString1(gongysb_id);
//				
//				String zhilb_id=rsl.getString("zhilb_id");
//				// ��������id
//				visit.setString2(zhilb_id);
//				
//				String meikxxb_id=rsl.getString("meikxxb_id");
//				//����ú����Ϣid
//				visit.setString3(meikxxb_id);
//				
//				String pinzb_id=rsl.getString("pinzb_id");
//				visit.setString4(pinzb_id);//����Ʒ��id
//				
//				String faz_id=rsl.getString("faz_id");
//				visit.setString5(faz_id);//���÷�վid
//				
//				String daoz_id=rsl.getString("daoz_id");
//				visit.setString6(daoz_id);//���õ�վid
//				
//				String hetb_id=rsl.getString("hetb_id");
//				visit.setString7(hetb_id);//���ú�ͬid
//				
//				String luncxxb_id=rsl.getString("luncxxb_id");
//				visit.setString8(luncxxb_id);//�����ִ�id
//				visit.setString14(rsl.getString("dunw"));
//				String jihkjb_id="4";//�޼ƻ��ھ�
//				
//				String fahrq="to_date('"+rsl.getString("fahrq")+"','yyyy-mm-dd')";
//				visit.setString9(rsl.getDateString("fahrq"));//���÷�������
//				
//				String daohrq=endriq;
//				visit.setString10(daohrq);//���õ�������
//				
//				String yunsfsb_id=rsl.getString("yunsfsb_id");
//				visit.setString11(yunsfsb_id);//�������䷽ʽid
//				
//				String kaobrq="to_date('"+rsl.getString("kaobrq")+"','yyyy-mm-dd')";
//				visit.setString12(rsl.getDateString("kaobrq"));//���ÿ�������
//				
//				String yundh=rsl.getString("yundh");
//				visit.setString13(yundh);
//				String xiemkssj="to_date('"+rsl.getDateTimeString("xiemkssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.SetStringBuffer10(new StringBuffer(rsl.getDateTimeString("xiemkssj")));//����жú��ʼʱ��
//				
//				String xiemjssj="to_date('"+rsl.getDateTimeString("xiemjssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.setString15(rsl.getDateTimeString("xiemjssj"));//жú����ʱ��
//				
//				String zhuanggkssj="to_date('"+rsl.getDateTimeString("zhuanggkssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.setString16(rsl.getDateTimeString("zhuanggkssj"));//װ�ۿ�ʼʱ��
//				
//				String zhuanggjssj="to_date('"+rsl.getDateTimeString("zhuanggjssj")+"','yyyy-mm-dd hh24:mi:ss')";
//				visit.setString17(rsl.getDateTimeString("zhuanggjssj"));//װ�۽���ʱ��
//				
//				String chec=rsl.getString("chec");
//				visit.SetStringBuffer1(new StringBuffer(chec));// ���ó���
//				
//				double maoz=rsl.getDouble("maoz");
//				visit.setDouble1(maoz);//����ë��
//				
//				
//				double piz=rsl.getDouble("piz");
//				visit.setDouble2(piz); //����Ƥ��
//				
//				double biaoz=rsl.getDouble("biaoz");
//				visit.setDouble3(biaoz);//���ñ���
//				
//				double sanfsl=rsl.getDouble("sanfsl");
//				visit.setDouble4(sanfsl);// ������������
//				visit.SetStringBuffer3(new StringBuffer(getRiqi()));//��ʼ����
//				
//			
//				//zhilb 
//				String huaybh = rsl.getString("huaybh");
//				if(huaybh==null||huaybh.equals(""))
//				{
//					visit.SetStringBuffer4(new StringBuffer(""));
//				}else{
//					visit.SetStringBuffer4(new StringBuffer(huaybh));
//					
//				}
//				
//				visit.SetStringBuffer9(new StringBuffer(getChangeid()));
//			}
//		}
		con.commit();
		con.Close();


//		cycle.activate("Haiyydxf_zh");
	}

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String beginriq=getRiqi();
		String endriq=getRiq2();
		
		StringBuffer sbsql = new StringBuffer("");
		
		sbsql.append("select fahb.id,fahb.yundh,luncxxb.mingc as luncxxb_id, \n");
		sbsql.append("       gongysb.mingc as gongysb_id,\n");
//		sbsql.append("       meikxxb_id, \n");
		sbsql.append("       meikxxb.mingc as meikxxb_id, \n");
		sbsql.append("       yunsfsb.mingc as yunsfsb_id,j.mingc as jmingc, \n");
		sbsql.append("       fahb.chec, \n");//zhilb.huaybh,
		sbsql.append("       hetb.hetbh hetb_id, \n");
		//sbsql.append("       fahb.faz_id, \n");
		sbsql.append("       chezxxb.mingc as faz_id, \n");
		sbsql.append(" DECODE ((SELECT cz.quanc\n ");
		sbsql.append(" FROM chezxxb cz, diancdzb dz\n");
		sbsql.append(" WHERE cz.ID = dz.chezxxb_id\n");
		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id),NULL, '��ѡ��',\n");
		sbsql.append(" (SELECT cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id \n");
		sbsql.append(" AND dz.leib = '�ۿ�' AND dz.diancxxb_id = fahb.diancxxb_id)) AS daoz_id,\n");
		//sbsql.append("   	 fahb.pinzb_id, \n");
		sbsql.append("       pinzb.mingc as pinzb_id, \n");
		sbsql.append("       luncxxb.dunw,--meicb.mingc as meicb_id,fahb.duowmc, \n");
		sbsql.append("       fahb.maoz,fahb.piz,fahb.biaoz,fahb.sanfsl\n");//fahb.jingz,
		//sbsql.append("       fahb.maoz, \n");
		//sbsql.append("   to_char(daohrq,'YYYY-MM-DD hh24:mi:ss')daohrq, \n");
		sbsql.append("    , fahrq, \n");// 
		sbsql.append("   kaobrq kaobrq, \n");
		sbsql.append("  to_char(xiemkssj,'YYYY-MM-DD hh24:mi:ss')xiemkssj, \n");
		sbsql.append("  to_char(xiemjssj,'YYYY-MM-DD hh24:mi:ss')xiemjssj--, \n");
		sbsql.append("  --to_char(zhuanggkssj,'YYYY-MM-DD hh24:mi:ss')zhuanggkssj, \n");
		sbsql.append(" -- to_char(zhuanggjssj,'YYYY-MM-DD hh24:mi:ss')zhuanggjssj \n");
//		sbsql.append("   luncxxb.mingc as huocmc, \n");
		sbsql.append("  from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb,meicb ,jihkjb j \n");
		sbsql.append(" where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+) \n");
		sbsql.append("   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+) \n");
		sbsql.append("   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)  \n");
		sbsql.append("   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)  \n");
		sbsql.append("   and yunsfsb.mingc='����' and fahb.jiesb_id=0 and fahb.jihkjb_id=j.id\n");
		sbsql.append(" and fahb.meicb_id=meicb.id(+) \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+beginriq+"'  \n");
		sbsql.append("   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+endriq+"'  \n").append(" and fahb.liucztb_id=0 ");
		sbsql.append("order by fahb.daohrq \n");
//		System.out.println(sbsql.toString());
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����GRID�Ƿ���Ա༭
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("yundh").setHeader("�˵���");
		egu.getColumn("yundh").setWidth(70);
		egu.getColumn("yundh").setEditor(null);
		egu.getColumn("luncxxb_id").setHeader("��������");
		egu.getColumn("luncxxb_id").setWidth(70);
		egu.getColumn("gongysb_id").setHeader("��Ӧ������");
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader("ú������");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("jmingc").setHeader("�ƻ��ھ�");
		egu.getColumn("jmingc").setWidth(60);
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(40);
		egu.getColumn("faz_id").setHeader("װ����");
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setHeader("������");
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("pinzb_id").setHeader("ú��");
		egu.getColumn("pinzb_id").setWidth(60);
//		egu.getColumn("huaybh").setHeader("�����ͬ���");
//		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("hetb_id").setHeader("�ɹ���ͬ���");
		egu.getColumn("hetb_id").setWidth(120);
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("dunw").setHeader("ͣ����λ");
		egu.getColumn("dunw").setWidth(70);
		egu.getColumn("dunw").setHidden(true);
		egu.getColumn("dunw").setEditor(null);
		egu.getColumn("maoz").setHeader("ж����(��)");
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setHeader("Ƥ��(��)");
		egu.getColumn("piz").setHidden(true);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setDefaultValue("0");
		
		egu.getColumn("sanfsl").setHeader("ˮ����(��)");
//		egu.getColumn("sanfsl").setEditor(null);
		egu.getColumn("sanfsl").setWidth(70);
//		egu.getColumn("meicb_id").setHeader("ú������");//ú��id
//		egu.getColumn("meicb_id").setWidth(70);//ú��id
//		egu.getColumn("meicb_id").setReturnId(true);
//		egu.getColumn("duowmc").setHeader("ú������");//ú������
//		egu.getColumn("duowmc").setWidth(70);//ú������
		
		egu.getColumn("biaoz").setHeader("�˵���(��)");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(90);
		egu.getColumn("kaobrq").setHeader("��������");
		egu.getColumn("kaobrq").setWidth(90);
		egu.getColumn("xiemkssj").setHeader("жú��ʼʱ��");
		egu.getColumn("xiemkssj").setWidth(120);
		egu.getColumn("xiemjssj").setHeader("жú����ʱ��");
		egu.getColumn("xiemjssj").setWidth(120);
//		egu.getColumn("zhuanggkssj").setHeader("װ�ۿ�ʼʱ��");
//		egu.getColumn("zhuanggkssj").setWidth(120);
//		egu.getColumn("zhuanggjssj").setHeader("װ�۽���ʱ��");
//		egu.getColumn("zhuanggjssj").setWidth(120);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		DateField datet = new DateField();
		egu.getColumn("fahrq").setDefaultValue("");//DateUtil.FormatDate(new Date())
		egu.getColumn("kaobrq").setEditor(datet);
		egu.getColumn("kaobrq").setDefaultValue("");//DateUtil.FormatDate(new Date())
//		
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("xiemkssj").setEditor(datetime);
		egu.getColumn("xiemkssj").setDefaultValue("");//DateUtil.FormatDateTime(new Date())
		
		egu.getColumn("xiemjssj").setEditor(datetime);
		egu.getColumn("xiemjssj").setDefaultValue("");//DateUtil.FormatDateTime(new Date())
		
//		egu.getColumn("zhuanggkssj").setEditor(datetime);
//		egu.getColumn("zhuanggkssj").setDefaultValue("");//DateUtil.FormatDateTime(new Date())
		
//		egu.getColumn("zhuanggjssj").setEditor(datetime);
//		egu.getColumn("zhuanggjssj").setDefaultValue("");//DateUtil.FormatDateTime(new Date())
//		egu.getColumn("xiemjssj").editor.allowBlank=true;
		egu.getColumn("xiemkssj").editor.allowBlank=false;
//		egu.getColumn("zhuanggjssj").editor.allowBlank=true;
//		egu.getColumn("zhuanggkssj").editor.allowBlank=true;
		
//		ú��id
		ComboBox c9 = new ComboBox();
		egu.getColumn("jmingc").setEditor(c9);
		egu.getColumn("jmingc").setDefaultValue("");
		c9.setEditable(true);
		String MeikbSql="select id,mingc from jihkjb order by mingc";
		egu.getColumn("jmingc").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql));
		egu.getColumn("jmingc").setReturnId(true);
		egu.getColumn("jmingc").editor.allowBlank=true;
		
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql="select id,mingc from gongysb  where leix =1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		
		ComboBox c7 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c7);
		c7.setEditable(true);
		String yunsfsid = "select id,mingc from yunsfsb where mingc ='����' order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsid));
		egu.getColumn("yunsfsb_id").setDefaultValue("����");
		
		egu.getColumn("yunsfsb_id").setHidden(true);
		
		ComboBox c8 = new ComboBox();
		egu.getColumn("hetb_id").setEditor(c8);
		c8.setEditable(true);
		c8.allowBlank=true;
		String hetbid = "select id,hetbh from hetb where diancxxb_id="+visit.getDiancxxb_id()+" order by hetbh";
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel(hetbid));
//		ú��id
//		ComboBox c9 = new ComboBox();
//		egu.getColumn("meicb_id").setEditor(c9);
//		egu.getColumn("meicb_id").setDefaultValue("");
//		c9.setEditable(true);
//		String MeikbSql="select id,mingc from meicb order by mingc";
//		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikbSql));
//		egu.getColumn("duowmc").setReturnId(true);
//		//ú������
//		ComboBox c10 = new ComboBox();
//		egu.getColumn("duowmc").setEditor(c10);
//		egu.getColumn("duowmc").setDefaultValue("");
//		c10.setEditable(true);
//		String DuowmcSql="select id,mingc from duow order by mingc";
//		egu.getColumn("duowmc").setComboEditor(egu.gridId, new IDropDownModel(DuowmcSql));
		
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("luncxxb_id").setEditor(c1);
		c1.setEditable(true);
		String luncid = "select id,mingc from luncxxb order by mingc";
		egu.getColumn("luncxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(luncid));
		
		ComboBox c2 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c2);
		c2.setEditable(true);
		String faz = "select id,mingc from chezxxb where leib='�ۿ�' order by mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(faz));
		
		ResultSetList rsl_daoz = con.getResultSetList("SELECT cz.id,cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id AND dz.leib = '�ۿ�'");
		int coun_d=0;
		String zhuangh="";
		while(rsl_daoz.next()){
			
			if(coun_d==0){
				
				zhuangh=rsl_daoz.getString("quanc");
			}
			coun_d++;
		}
		if(coun_d==0){
			zhuangh="ׯ�Ӹۻ�Ȧ��ͷ";
		}
		ComboBox c5 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c5);
		egu.getColumn("daoz_id").setDefaultValue(zhuangh);
		c5.setEditable(true);
		String daoz = "SELECT cz.id,cz.quanc FROM chezxxb cz, diancdzb dz WHERE cz.ID = dz.chezxxb_id AND dz.leib = '�ۿ�'";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daoz));
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c3);
		c3.setEditable(true);
		//c3.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		
//		 ����Ʒ��������
		ComboBox c4 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c4);
		c4.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		
//		�������ڲ�ѯ
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		String shez="";//���ü�¼���
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Save, "Save1Button");//,condition
		
		GridButton gb = new GridButton("���", "function(){ "
				
				+
				"   var gridDivsave_history = '';\n" +
//				"   var rec = gridDiv_sm.getSelected();\n" + 
//				"    if(rec != null){\n" + 
//				"      var id = rec.get('ID');\n" + 
//				"       var Cobjid = document.getElementById('CHANGEID');\n" + 
//				"        Cobjid.value = id;\n" + 
//				"        }\n" + 
				"   var Mrcd = gridDiv_ds.getModifiedRecords();\n" + 
				"  for(i = 0; i< Mrcd.length; i++){\n" + 
				"    if(typeof(gridDiv_save)=='function'){\n" + 
				"    var revalue = gridDiv_save(Mrcd[i]);\n" + 
				"    if(revalue=='return'){\n" + 
				"    return;\n" + 
				"    }else if(revalue=='continue'){continue;}\n" + 
				"    }\n" + 
				"    if(Mrcd[i].get('LUNCXXB_ID') == ''){\n" + 
				"      Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
				"    }\n" + 
				"    if(Mrcd[i].get('GONGYSB_ID') == ''){\n" + 
				"    Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��Ӧ������ ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('MEIKXXB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('YUNSFSB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���䷽ʽ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('CHEC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('FAZ_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ���� ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('DAOZ_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('PINZB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('MEICB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('DUOWMC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('MAOZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ж����(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('MAOZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ж����(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('MAOZ')!=0 && Mrcd[i].get('MAOZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ж����(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('PIZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�Ƥ��(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('PIZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� Ƥ��(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('PIZ')!=0 && Mrcd[i].get('PIZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� Ƥ��(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('BIAOZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ��˵���(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('BIAOZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� �˵���(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('BIAOZ')!=0 && Mrcd[i].get('BIAOZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �˵���(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('SANFSL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ˮ����(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('SANFSL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ˮ����(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('SANFSL')!=0 && Mrcd[i].get('SANFSL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ˮ����(��) ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('FAHRQ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('KAOBRQ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('XIEMKSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� жú��ʼʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('XIEMJSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� жú����ʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('ZHUANGGKSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ�ۿ�ʼʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('ZHUANGGJSSJ') == ''){\n" + 
//				"    Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ�۽���ʱ�� ����Ϊ��');return;\n" + 
				"    }\n" + 
				"    gridDivsave_history += '<result>' + '<sign>U</sign>' +\n" + 
				"     '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"    + '<YUNDH update=\"true\">' + Mrcd[i].get('YUNDH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDH>'\n" + 
				"    + '<LUNCXXB_ID update=\"true\">' + Mrcd[i].get('LUNCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LUNCXXB_ID>'\n" + 
				"    + '<GONGYSB_ID update=\"true\">' + Mrcd[i].get('GONGYSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</GONGYSB_ID>'\n" + 
				"    + '<MEIKXXB_ID update=\"true\">' + Mrcd[i].get('MEIKXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKXXB_ID>'\n" + 
				"    + '<YUNSFSB_ID update=\"true\">' + Mrcd[i].get('YUNSFSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNSFSB_ID>'\n" + 
				"    + '<CHEC update=\"true\">' + Mrcd[i].get('CHEC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEC>'\n" + 
				"    + '<HETB_ID update=\"true\">' + Mrcd[i].get('HETB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HETB_ID>'\n" + 
				"    + '<FAZ_ID update=\"true\">' + Mrcd[i].get('FAZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAZ_ID>'\n" + 
				"    + '<DAOZ_ID update=\"true\">' + Mrcd[i].get('DAOZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DAOZ_ID>'\n" + 
				"    + '<PINZB_ID update=\"true\">' + Mrcd[i].get('PINZB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINZB_ID>'\n" + 
				"    + '<DUNW update=\"true\">' + Mrcd[i].get('DUNW')+ '</DUNW>'\n" + 
//				"    + '<MEICB_ID update=\"true\">' + Mrcd[i].get('MEICB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEICB_ID>'\n" + 
//				"    + '<DUOWMC update=\"true\">' + Mrcd[i].get('DUOWMC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DUOWMC>'\n" + 
				"    + '<MAOZ update=\"true\">' + Mrcd[i].get('MAOZ')+ '</MAOZ>'\n" + 
				"    + '<PIZ update=\"true\">' + Mrcd[i].get('PIZ')+ '</PIZ>'\n" + 
				"    + '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n" + 
				"    + '<SANFSL update=\"true\">' + Mrcd[i].get('SANFSL')+ '</SANFSL>'\n" + 
				"    + '<FAHRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('FAHRQ'))?Mrcd[i].get('FAHRQ'):Mrcd[i].get('FAHRQ').dateFormat('Y-m-d'))+ '</FAHRQ>'\n" + 
				"    + '<KAOBRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('KAOBRQ'))?Mrcd[i].get('KAOBRQ'):Mrcd[i].get('KAOBRQ').dateFormat('Y-m-d'))+ '</KAOBRQ>'\n" + 
				"    + '<XIEMKSSJ update=\"true\">' + Mrcd[i].get('XIEMKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMKSSJ>'\n" + 
				"    + '<XIEMJSSJ update=\"true\">' + Mrcd[i].get('XIEMJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMJSSJ>'\n" + 
//				"    + '<ZHUANGGKSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGKSSJ>'\n" + 
//				"    + '<ZHUANGGJSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGJSSJ>'\n" + 
				"     + '</result>' ;\n" + 
				"     }\n" + 
				"    if(gridDiv_history=='' && gridDivsave_history=='' && rec == null){\n" + 
				"   \n" + // Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');
				"\t\t\t}else{ Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ����');return false; \n" + 
//				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
//				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
//				"\t\t\t\tdocument.getElementById('SaveButton').click();\n" + 
//				"\t\t\t\tExt.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" 
				
				+ " var rec = gridDiv_sm.getSelected();var rq; "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;" +
				"  rq = rec.get('XIEMJSSJ'); var Cobjrq = document.getElementById('CHANGERQ');Cobjrq.value = rq;" +
				" \n"+
						"}else{ Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ϣ'); return false;\n}"+ 
						"if((rec != null && rq=='')||(rec != null && rq==null)){Ext.MessageBox.alert('��ʾ��Ϣ','����джú����ʱ��');" +
						" }else{document.getElementById('SaveButton').click();}"+
				
				"\t}"

				+ " ");
	
		
		egu.addTbarText("-");
//		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
//		String shez="";//���ü�¼���
		
		
		GridButton gb1 = new GridButton("ú������", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;}"
				+
				"   var gridDivsave_history = '';\n" +
				"   var rec = gridDiv_sm.getSelected();\n" + 
				"    if(rec != null){\n" + 
				"      var id = rec.get('ID');\n" + 
				"       var Cobjid = document.getElementById('CHANGEID');\n" + 
				"        Cobjid.value = id;\n" + 
				"        }\n" + 
				"   var Mrcd = gridDiv_ds.getModifiedRecords();\n" + 
				"  for(i = 0; i< Mrcd.length; i++){\n" + 
				"    if(typeof(gridDiv_save)=='function'){\n" + 
				"    var revalue = gridDiv_save(Mrcd[i]);\n" + 
				"    if(revalue=='return'){\n" + 
				"    return;\n" + 
				"    }else if(revalue=='continue'){continue;}\n" + 
				"    }\n" + 
				"    if(Mrcd[i].get('LUNCXXB_ID') == ''){\n" + 
				"      Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
				"    }\n" + 
				"    if(Mrcd[i].get('GONGYSB_ID') == ''){\n" + 
				"    Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��Ӧ������ ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('MEIKXXB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('YUNSFSB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���䷽ʽ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('CHEC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('FAZ_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ���� ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('DAOZ_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('PINZB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('MEICB_ID') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('DUOWMC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ú������ ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('MAOZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ж����(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('MAOZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ж����(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('MAOZ')!=0 && Mrcd[i].get('MAOZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ж����(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('PIZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�Ƥ��(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('PIZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� Ƥ��(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('PIZ')!=0 && Mrcd[i].get('PIZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� Ƥ��(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('BIAOZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ��˵���(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('BIAOZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� �˵���(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('BIAOZ')!=0 && Mrcd[i].get('BIAOZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �˵���(��) ����Ϊ��');return;\n" + 
				"    }if(Mrcd[i].get('SANFSL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ˮ����(��) ������Сֵ -100000000');return;\n" + 
				"    }if( Mrcd[i].get('SANFSL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ˮ����(��) �������ֵ 100000000000');return;\n" + 
				"    }if(Mrcd[i].get('SANFSL')!=0 && Mrcd[i].get('SANFSL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ˮ����(��) ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('FAHRQ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('KAOBRQ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �������� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('XIEMKSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� жú��ʼʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('XIEMJSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� жú����ʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('ZHUANGGKSSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ�ۿ�ʼʱ�� ����Ϊ��');return;\n" + 
//				"    }if(Mrcd[i].get('ZHUANGGJSSJ') == ''){\n" + 
//				"    Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� װ�۽���ʱ�� ����Ϊ��');return;\n" + 
				"    }\n" + 
				"    gridDivsave_history += '<result>' + '<sign>U</sign>' +\n" + 
				"     '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"    + '<YUNDH update=\"true\">' + Mrcd[i].get('YUNDH').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNDH>'\n" + 
				"    + '<LUNCXXB_ID update=\"true\">' + Mrcd[i].get('LUNCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LUNCXXB_ID>'\n" + 
				"    + '<GONGYSB_ID update=\"true\">' + Mrcd[i].get('GONGYSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</GONGYSB_ID>'\n" + 
				"    + '<MEIKXXB_ID update=\"true\">' + Mrcd[i].get('MEIKXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIKXXB_ID>'\n" + 
				"    + '<YUNSFSB_ID update=\"true\">' + Mrcd[i].get('YUNSFSB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNSFSB_ID>'\n" + 
				"    + '<CHEC update=\"true\">' + Mrcd[i].get('CHEC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEC>'\n" + 
				"    + '<HETB_ID update=\"true\">' + Mrcd[i].get('HETB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HETB_ID>'\n" + 
				"    + '<FAZ_ID update=\"true\">' + Mrcd[i].get('FAZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAZ_ID>'\n" + 
				"    + '<DAOZ_ID update=\"true\">' + Mrcd[i].get('DAOZ_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DAOZ_ID>'\n" + 
				"    + '<PINZB_ID update=\"true\">' + Mrcd[i].get('PINZB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</PINZB_ID>'\n" + 
				"    + '<DUNW update=\"true\">' + Mrcd[i].get('DUNW')+ '</DUNW>'\n" + 
//				"    + '<MEICB_ID update=\"true\">' + Mrcd[i].get('MEICB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEICB_ID>'\n" + 
//				"    + '<DUOWMC update=\"true\">' + Mrcd[i].get('DUOWMC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DUOWMC>'\n" + 
				"    + '<MAOZ update=\"true\">' + Mrcd[i].get('MAOZ')+ '</MAOZ>'\n" + 
				"    + '<PIZ update=\"true\">' + Mrcd[i].get('PIZ')+ '</PIZ>'\n" + 
				"    + '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n" + 
				"    + '<SANFSL update=\"true\">' + Mrcd[i].get('SANFSL')+ '</SANFSL>'\n" + 
//				"    + '<FAHRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('FAHRQ'))?Mrcd[i].get('FAHRQ'):Mrcd[i].get('FAHRQ').dateFormat('Y-m-d'))+ '</FAHRQ>'\n" + 
//				"    + '<KAOBRQ update=\"true\">' + ('object' != typeof(Mrcd[i].get('KAOBRQ'))?Mrcd[i].get('KAOBRQ'):Mrcd[i].get('KAOBRQ').dateFormat('Y-m-d'))+ '</KAOBRQ>'\n" + 
//				"    + '<XIEMKSSJ update=\"true\">' + Mrcd[i].get('XIEMKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMKSSJ>'\n" + 
//				"    + '<XIEMJSSJ update=\"true\">' + Mrcd[i].get('XIEMJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIEMJSSJ>'\n" + 
//				"    + '<ZHUANGGKSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGKSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGKSSJ>'\n" + 
//				"    + '<ZHUANGGJSSJ update=\"true\">' + Mrcd[i].get('ZHUANGGJSSJ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ZHUANGGJSSJ>'\n" + 
				"     + '</result>' ;\n" + 
				"     }\n" + 
				"    if(gridDiv_history=='' && gridDivsave_history=='' && rec == null){\n" + 
				"    Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n" + 
				"\t\t\t}else{\n" + 
				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
				"\t\t\t\tdocument.getElementById('Save2Button').click();\n" + 
				"\t\t\t\tExt.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" + 
				"\t}"

				+ " ");
		egu.addTbarBtn(gb1);
		egu.addTbarText("-");
		egu.addTbarBtn(gb);
		String cond=" "
			+ " var rec = gridDiv_sm.getSelected(); "
			+ " if(rec != null){var id = rec.get('ID');"
			+ " var Cobjid = document.getElementById('CHANGEID');"
			+ " Cobjid.value = id;"
			+ " }";//document.getElementById('SaveButton').click();
		
		String condition= 
			"var rsl = gridDiv_grid.getSelectionModel().getSelections();\n" +
			"var rec = gridDiv_sm.getSelected(); " + 
			"var Mrcd = gridDiv_ds.getModifiedRecords();\n" +
			"if(Mrcd.length>0){ for (i = 0; i < rsl.length; i ++) {\n" + 

//			"    if (rsl[i].get('ID') == '' || rsl[i].get('ID') == 0) {\n" +
//			"        Ext.MessageBox.alert('��ʾ��Ϣ','жú����ʱ�䲻��Ϊ��'); return false;\n" + 
//			"    }\n" +
			"    }\n" +
			"    if (rsl.length==1) {\n" 
//			+ " if(rsl != null){var id = rsl.get('ID');"
//			+ " var Cobjid = document.getElementById('CHANGEID');"
//			+ " Cobjid.value = id;" +
			+"    \n" +
			
//			"    total += rsl[i].get('BIL');\n" + 
			"}}\n " + 
			"    if (rsl.length>=1) {\n" +
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��������ú����Ϣ');return false;\n" + 
			"    }\n" +
			"";
//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition, null,condition);
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		

		egu.setDefaultsortable(false);
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(this.getPageName().toString());
			visit.setString1("");	//����ͬid	
//			setRiqi(DateUtil.FormatDate(new Date()));
//			setRiq2(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
