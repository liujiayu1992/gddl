package com.zhiren.heiljkhh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ����:ly
 * ʱ��:2009-06-10
 * ����:��������1.�������ȫ��ѡ�� 2.�������п�ϼƲ�ѯ
 */
/*
 * ����:tzf
 * ʱ��:2009-05-28
 * ����:���ݷ�����Ҫ��û�е����� ��0��ʾ��ʱ�������ѡ��ʱ�䵱�³�����ѡʱ��Ϊ�Σ����в�ѯ
 */
/*
 * ����:tzf
 * ʱ��:2009-5-8
 * ����:ʵ��������˾ ȼ�ϵ��˹��� ���Ի�����
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Ranldygl extends BasePage implements PageValidateListener {


	private final static String jihkj_zddh_id="1";//�ƻ��ھ�  �ص㶩��id
	private final static String jihkj_sccg_id="2";//�г��ɹ�
	private final static String jihkj_qydh_id="3";//���򶩻�,��ý�ƻ���
	private final static String meiklb_tp="ͳ��";//ú�����  ͳ��
	private final static String meiklb_df="�ط�";//ú�����  �ط�
	
	public boolean getRaw() {
		return true;
	}

	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public String getPrintTable() {
		return getHuaybgd();
	}
	
	
	
	private StringBuffer getBaseSql(String sql_mc,Date date){
		
		StringBuffer bf = new StringBuffer();
		String diancxxb_id=this.getTreeid_dc();
		String briq_s=this.getBRiq();
		
		String sql = 
			"(select z.id as id,\n" +
			"        (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) mdr,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) myl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) rdr,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) ryl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum((r.meij + r.yunj + r.jiaohqzf + r.daozzf + r.qitfy) *\n" + 
			"                          nvl(f.laimsl, 0)) / sum(nvl(f.laimsl, 0)))\n" + 
			"          from ruccb r, fahb f\n" + 
			"         where r.fahb_id = f.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.meikxxb_id = z.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) dcj\n" + 
			"  from (select m.id from meikxxb m where m.id in "+sql_mc+") z\n" + 
			"  )\n" + 
			"\n" + 
			" union\n" + 
			"\n" + 
			"  (select 0 as id,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) mdr,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) myl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) rdr,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) ryl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum((r.meij + r.yunj + r.jiaohqzf + r.daozzf + r.qitfy) *\n" + 
			"                          nvl(f.laimsl, 0)) / sum(nvl(f.laimsl, 0)))\n" + 
			"          from ruccb r, fahb f\n" + 
			"         where r.fahb_id = f.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) dcj\n" + 
			"  from dual)\n" + 
			"  order by id\n";

		
		bf.append(sql);
	
	
//		System.out.println(bf.toString());
		return bf;
	}
	
	
	
	private String getDateStr(String date,String format){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(date==null){
			date=DateUtil.FormatDate(new Date());
		}
		try {
			return DateUtil.Formatdate(format, sf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	//����³���ʱ�� ��2009-1-6---------��  2008-12-31
	private Date getLastdayOfLastMonth(String date){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(date==null){
			date=DateUtil.FormatDate(new Date());
		}
		try {
			Date temD= sf.parse(date);
			int month = DateUtil.getMonth(temD);
			int year =DateUtil.getYear(temD);
			
			if(month==1){
				year-=1;
				month=12;
			}else{
				month-=1;
			}
			
			Date newD = DateUtil.getLastDayOfMonth(year+"-"+month+"-1");
			return newD;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	
	//��õ��µ�����
	private int getDays(String date){
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		Date d=null;
		try{
			d=sf.parse(date);
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
		int firstD=Integer.valueOf(DateUtil.Formatdate("dd", DateUtil.getFirstDayOfMonth(d))).intValue();
		int lastD=Integer.valueOf(DateUtil.Formatdate("dd", DateUtil.getLastDayOfMonth(d))).intValue();
		
		return lastD-firstD+1;
	}
	
 //���ָ��day������
	private Date getDateOfDay_Oracle(String date,int day){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try{
			d=sf.parse(date);
			if(day!=0){
				long l = d.getTime()+24*60*60*day*1000;
				d.setTime(l);
			}

//			String ds=DateUtil.Formatdate("yyyy-MM-dd", d);
//			String new_s=ds.substring(0, 7)+"-"+day;
//			
//			d=sf.parse(new_s);
			
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
	return d;
	
	
	}
	
	//�������������������
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	 }

	
	//��õ��ºͷ��������������ú����Ϣ
	private StringBuffer getMeikXX(){
		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String diancxxb_id=this.getTreeid_dc();
		String riq_s=this.getRiq();
		String briq_s=this.getBRiq();
		
//	---	����õ糧���߼��������������ĵ糧
		String s=diancxxb_id;
		
		String leib="";
		if(this.getBianmValue().getId()==0){
			leib = " ";
		}else{
			leib = " and m.leib='"+this.getBianmValue().getValue()+"' \n";
		}
		
//		bf.append(" select distinct m.id ,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
//		bf.append(" and to_char(f.daohrq,'yyyy-MM')=to_char(to_date('"+this.getDateStr(riq_s, "yyyy-MM-dd")+"','yyyy-MM-dd'),'yyyy-MM') \n");
//		bf.append(" and f.diancxxb_id in ( "+s+" ) \n");
//		bf.append(" and m.leib='"+leib+"' \n");//ͳ��ú
//		bf.append(" order by m.id \n");System.out.println(bf.toString());
		
		bf.append(" select distinct m.id ,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq>=to_date('"+briq_s+"','yyyy-MM-dd') \n");
		bf.append(" and f.daohrq<=to_date('"+riq_s+"','yyyy-MM-dd') \n");
		bf.append(" and f.diancxxb_id in ( "+s+" ) \n");
		bf.append(leib);//ͳ��ú
		bf.append(" union \n");
		bf.append(" select 0 as id,'�ϼ�' as mingc from dual \n");
		bf.append(" order by id \n");
//		System.out.println(bf.toString());
		
		return bf;
	}
	// ȼ�ϲɹ���ָ���������ձ�
	private String getHuaybgd() {
		Report rt = new Report();
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String briq_s=this.getBRiq();
		String riq_s=this.getRiq();
		StringBuffer bf=new StringBuffer();
		bf=this.getMeikXX();
		
		
		
		ResultSetList rs = con.getResultSetList(bf.toString());
		
		int rows=rs.getRows();
		
		if(rows==1){
			
			if(!visit.getboolean5()){
				visit.setboolean5(true);
			}else{
//				this.setMsg("�õ糧����û�����ݣ�");
			}
			this.setAllPages(-1);
			this.setCurrentPage(-1);
			return "";
		}
		
//		this.setMsg(null);
		
		
		Date dt=null;
		Date bdt=null;
		Date xitsj=null;
		try{
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			dt=sf.parse(riq_s);
			bdt=sf.parse(briq_s);
			xitsj=new Date();
		}catch(Exception e){
			dt=new Date();
			bdt=new Date();
			xitsj=new Date();
		}
		
//		//ϵͳ��ǰʱ��
//		
//		int xit_year=Integer.valueOf(DateUtil.Formatdate("yyyy", new Date())).intValue();
//		int xit_month=Integer.valueOf(DateUtil.Formatdate("MM", new Date())).intValue();
//		int xit_day=Integer.valueOf(DateUtil.Formatdate("dd", new Date())).intValue();
//		
//		//��ѡ������
//		int bsele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", bdt)).intValue();
//		int bsele_month=Integer.valueOf(DateUtil.Formatdate("MM", bdt)).intValue();
//		int bsele_day=Integer.valueOf(DateUtil.Formatdate("dd", bdt)).intValue();
//		
//		int sele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", dt)).intValue();
//		int sele_month=Integer.valueOf(DateUtil.Formatdate("MM", dt)).intValue();
//		int sele_dat=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
		
		int count=-1;
		if(daysOfTwo(bdt,xitsj)>=0){
			if(daysOfTwo(dt,xitsj)>=0){
				count = daysOfTwo(bdt,dt)+1;
			}else{
				count = daysOfTwo(bdt,xitsj)+1;
			}
		}else{
			count = -1;
		}

//		if(bsele_year==xit_year){
//			if(bsele_month==xit_month){
//				if()
//				count = sele_dat-bsele_day+1;
//			}else if(bsele_month<xit_month){
//				
//			}else{
//				count=-1;
//			}
//			
////			if(sele_month<xit_month){
//////				count=this.getDays(riq_s);
////				count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////			}else if(sele_month==xit_month){
////				
////				if(sele_year==xit_year){
////					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////				}else{
//////					count=this.getDays(riq_s);
////					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////				}
////				
////			}else{
////				count=-1;
////			}
//		}else if(bsele_year<xit_year){
//		
//		}else{
//			count=-1;
//		}
		
		if(count<0){
			this.setAllPages(-1);
			this.setCurrentPage(-1);
			return "";
		}
		
		
		
		
		int mc_count=5;//һ��ú����Ϣ ��5���ֶ�
//		String ArrHeader[][]=new String[3+this.getDays(riq_s)][1+rows*mc_count];
		String ArrHeader[][]=new String[3+count][1+rows*mc_count];
		
		ArrHeader[0][0] = "����";
		ArrHeader[1][0] = "����";
		ArrHeader[2][0] = "����";
		String sql_mc="(";
		int col_index=1;//�� �±� ��ʼλ��
		
		int i=1;
		while(rs.next()){
			sql_mc+=rs.getString("id")+",";
			
			if(i!=1){
				col_index+=mc_count;
			}
			for(int j=0;j<=2;j++){  //3��
				
				if(j==0){//��һ��  ��ͷ
					ArrHeader[j][col_index]=rs.getString("MINGC");
					ArrHeader[j][col_index+1]=rs.getString("MINGC");
					ArrHeader[j][col_index+2]=rs.getString("MINGC");
					ArrHeader[j][col_index+3]=rs.getString("MINGC");
					ArrHeader[j][col_index+4]=rs.getString("MINGC");
				}
				if(j==1){
					ArrHeader[j][col_index]="��ú";
					ArrHeader[j][col_index+1]="��ú";
					ArrHeader[j][col_index+2]="��ֵ";
					ArrHeader[j][col_index+3]="��ֵ";
					ArrHeader[j][col_index+4]="������";
				}
				if(j==2){
					ArrHeader[j][col_index]="����";
					ArrHeader[j][col_index+1]="����";
					ArrHeader[j][col_index+2]="����";
					ArrHeader[j][col_index+3]="����";
					ArrHeader[j][col_index+4]="������";
				}
				
			}
			i++;
		}
		
		sql_mc=sql_mc.substring(0, sql_mc.lastIndexOf(","))+")";
		
	//  ArrHeader ��ֵ
		rs.close();
		
		int data_index_start=3;//�ӵ�4�п�ʼ��ֵ
		col_index=1;

		
		for(int m=0;m<count;m++){//ÿһ��ѭ�� ȡ�õ���ķ���
			
//			Date ora_date=this.getDateOfDay_Oracle(briq_s, m);
			
			Date ora_date=DateUtil.AddDate(DateUtil.FormatDateTime(DateUtil.getDate(briq_s)), m, DateUtil.AddType_intDay);
			
			StringBuffer sql=this.getBaseSql(sql_mc,ora_date);
			
			rs=con.getResultSetList(sql.toString());
			
			ArrHeader[data_index_start][0]=DateUtil.FormatDate(ora_date);
			
			i=1;
			col_index=1;
			while(rs.next()){
				
				if(i!=1){
					col_index+=mc_count;
				}
				
				ArrHeader[data_index_start][col_index]=rs.getString("MDR");
				ArrHeader[data_index_start][col_index+1]=rs.getString("MYL");
				ArrHeader[data_index_start][col_index+2]=rs.getString("RDR");
				ArrHeader[data_index_start][col_index+3]=rs.getString("RYL");
				ArrHeader[data_index_start][col_index+4]=rs.getString("DCJ");
				
				i++;
			}
			
			data_index_start++;
		}
		
		
		int[] ArrWidth = new int[1+rows*mc_count];
		ArrWidth[0]=75;
		for(int m=1;m<ArrWidth.length;m++){  //��ʾ��� ����
			ArrWidth[m]=55;
		}
		
//		Table bt=new Table(3+this.getDays(riq_s),1+rows*mc_count);
		Table bt=new Table(3+count,1+rows*mc_count);
		rt.setBody(bt);
		
		

		String[][] ArrHeader1 = new String[3][1+rows*mc_count];
		ArrHeader1[0] = ArrHeader[0];
		ArrHeader1[1] = ArrHeader[1];
		ArrHeader1[2] = ArrHeader[2];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(this.getDateStr(this.getRiq(), "yyyy��MM��")+visit.getDiancmc()+this.getBianmValue().getValue()+"ú���ʼ�ͳ��̨��", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(rows*mc_count-3, 2, "��λ:�� MJ/KJ",
				Table.ALIGN_RIGHT);
		
		
//		for ( i = 3; i < 3+this.getDays(riq_s); i++) {
		for ( i = 3; i < 3+count; i++) {
			for (int j = 0; j < 1+rows*mc_count; j++) {
				
				if ( ArrHeader[i][j] != null && !ArrHeader[i][j].equals("") ) {
					
					ArrHeader[i][j] = rt.body.format(ArrHeader[i][j], "0.00");
				}else{
					ArrHeader[i][j] ="0";
				}
				
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				rt.body.setCellAlign(i+1, j+1, Table.ALIGN_RIGHT);

				
			}
		}
		
//		for (i = 1; i <= 1+rows*mc_count; i++) {
//			rt.body.setColAlign(i, Table.ALIGN_CENTER);
//		}
//		
		rt.body.merge(0, 0, 3, 1+rows*mc_count);
		
		//rt.body.mergeCol(0);
//		rt.body.mergeCol(1);
		
//		rt.body.merge(2, 0, rs.getRows()+1, 3);
//		rt.body.merge(3, 7, rs.getRows()+2, 17);
		
		
		
		rt.body.ShowZero = true;

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		//rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
//		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
//		sql+=" union \n";
//		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		String sql = " select d.id,d.mingc from diancxxb d ";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END----------
	
	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
	
    
    
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
		tb1.addText(new ToolbarText("��λ����:"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		
		tb1.addText(new ToolbarText("-"));
		
		
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("BRIQ");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}


	
	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
	//	((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
		//	setRiq(DateUtil.FormatDate(new Date()));
			visit.setboolean5(false);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			getSelectData();
		}
	
		
	}

	boolean riqchange = false;
	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}
	
	boolean briqchange = false;
	private String briq;

	public String getBRiq() {
		if (briq == null || briq.equals("")) {
			briq =DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return briq;
	}

	public void setBRiq(String briq) {

		if (this.briq != null && !this.briq.equals(briq)) {
			this.briq = briq;
			briqchange = true;
		}

	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {

		List list=new ArrayList();
		list.add(new IDropDownBean("0","ȫ��"));
		list.add(new IDropDownBean("1",meiklb_tp));
		list.add(new IDropDownBean("2",meiklb_df));
			
		setBianmModel(new IDropDownModel(list));
	}


	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������
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
