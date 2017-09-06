package com.zhiren.heiljkhh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

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
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ����:tzf
 * ʱ��:2009-5-8
 * ����:ʵ�� ������ �����������Ĵ� ���Ի�����
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

public class Laihcrbb extends BasePage implements PageValidateListener {

	private final static String jihkj_zddh_id="1";//�ƻ��ھ�  �ص㶩��id
	private final static String jihkj_sccg_id="2";//�г��ɹ�
	private final static String jihkj_qydh_id="3";//���򶩻�,��ý�ƻ���
	private final static String meiklb_tp="ͳ��";//ú�����  ͳ��
	private final static String meiklb_df="�ط�";//ú�����  �ط�
	private final static String jihw="�ƻ���";
	private final static String jihn="�ƻ���";
	private final static String shicm="�г�ú";
	private final static String laimsl_round_count="0";//�Է�����ȡ����laimsl�ֶ� ����ʱ������Լ��С������,Ĭ��Ϊ2,����2λС��
	
	private final static String meil_table_show="0";  //ú���ֶ�  ҳ����ʾ��β��
	private final static String qit_table_show="0.00";//��ú��֮���ֶ�  ҳ����ʾ��β��
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
	
	
	 //���ָ��day������
	private Date getDateOfDay_Oracle(String date,int day){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try{
			d=sf.parse(date);
			
			String ds=DateUtil.Formatdate("yyyy-MM-dd", d);
			String new_s=ds.substring(0, 7)+"-"+day;
			
			d=sf.parse(new_s);
			
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
	return d;
	
	
	}
	
	private StringBuffer getBaseSql(){

		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String diancxxb_id=this.getTreeid_dc();
		String riq_s=this.getRiq();
		
		StringBuffer temp=new StringBuffer();
		String qissj=DateUtil.Formatdate("yyyy-MM-dd", getDateOfDay_Oracle(riq_s,1));
		
		temp.append(" select zk.mingc xiangm, \n");
		temp.append(" (select s.kuc from shouhcrbb s where s.riq=to_date('"+DateUtil.FormatDate(getLastdayOfLastMonth(riq_s))+"','yyyy-MM-dd') and s.diancxxb_id=zk.id) yuecjc,\n");
		temp.append("  zk.jihkjb_id tjkj,\n");
		temp.append(" decode(zk.jihkjb_id,1,( \n");
		temp.append(" select sum(hs.hetl)||'' hetl  from hetb ht,hetslb hs  where hs.hetb_id=ht.id  \n");
		temp.append(" and to_char(hs.riq,'yyyy-MM')='"+this.getDateStr(riq_s, "yyyy-MM")+"' and hs.diancxxb_id=zk.id and ht.leib=2 and ht.jihkjb_id="+jihkj_zddh_id+" \n");
		temp.append(" ),"+jihkj_qydh_id+",'"+jihw+"',"+jihkj_sccg_id+",'"+shicm+"','"+jihw+"') yuejhtpm,\n");
		
		temp.append(" (select sum(round_new(nvl(fa.laimsl,0),"+laimsl_round_count+")) from fahb fa,meikxxb ma where fa.meikxxb_id=ma.id and ma.leib='"+meiklb_tp+"' \n");
		temp.append("  and fa.daohrq=to_date('"+riq_s+"','yyyy-MM-dd')  and fa.diancxxb_id=zk.id  and fa.jihkjb_id=zk.jihkjb_id ) td,\n");
		
		temp.append(" (select sum(round_new(nvl(fa.laimsl,0),"+laimsl_round_count+")) from fahb fa,meikxxb ma where fa.meikxxb_id=ma.id and ma.leib='"+meiklb_tp+"' \n");
		temp.append("  and fa.daohrq>=to_date('"+qissj+"','yyyy-MM-dd') and fa.daohrq<=to_date('"+riq_s+"','yyyy-MM-dd')  and fa.diancxxb_id=zk.id  and fa.jihkjb_id=zk.jihkjb_id )tl, \n");
		
		temp.append(" (select sum(round_new(nvl(fa.laimsl,0),"+laimsl_round_count+")) from fahb fa,meikxxb ma where fa.meikxxb_id=ma.id and ma.leib='"+meiklb_df+"' \n");
		temp.append("  and fa.daohrq=to_date('"+riq_s+"','yyyy-MM-dd')  and fa.diancxxb_id=zk.id  ) dd,\n");
		
		temp.append(" (select sum(round_new(nvl(fa.laimsl,0),"+laimsl_round_count+")) from fahb fa,meikxxb ma where fa.meikxxb_id=ma.id and ma.leib='"+meiklb_df+"' \n");
		temp.append("  and fa.daohrq>=to_date('"+qissj+"','yyyy-MM-dd') and fa.daohrq<=to_date('"+riq_s+"','yyyy-MM-dd')  and fa.diancxxb_id=zk.id )dl,\n");
		
	//	bf.append(" sr.haoyqkdr hd,\n");
		temp.append(" (select sum(sr.haoyqkdr+sr.feiscy) from shouhcrbb sr where sr.diancxxb_id=zk.id and sr.riq=to_date('"+riq_s+"','yyyy-MM-dd')) hd,");
		
		
		temp.append(" (select sum(sb.haoyqkdr+sb.feiscy) from shouhcrbb sb where sb.riq>=to_date('"+qissj+"','yyyy-MM-dd') and sb.riq<=to_date('"+riq_s+"','yyyy-MM-dd') and sb.diancxxb_id=zk.id )hl,\n");
		
		//bf.append(" sr.kuc benqkc, decode(sr.haoyqkdr,0,'',floor(sr.kuc/sr.haoyqkdr)||'') pingjkyts,\n");
		temp.append(" (select sum(sr.kuc) from shouhcrbb sr where sr.diancxxb_id=zk.id and sr.riq=to_date('"+riq_s+"','yyyy-MM-dd')) benqkc,\n");
		temp.append(" (select decode(sr.haoyqkdr,0,'',floor(sr.kuc/(sr.haoyqkdr+sr.feiscy))||'') from shouhcrbb sr where sr.diancxxb_id=zk.id and sr.riq=to_date('"+riq_s+"','yyyy-MM-dd')) pingjkyts,\n");
		
		
//		bf.append(" sy.shourl jinyd, \n");
		temp.append(" (select sum(sy.shourl) from shouhcrbyb sy where sy.diancxxb_id=zk.id and sy.riq=to_date('"+riq_s+"','yyyy-MM-dd')) jinyd, \n");
		
		
		temp.append(" (select sum(so.shourl) from shouhcrbyb so where so.diancxxb_id=zk.id and so.riq>=to_date('"+qissj+"','yyyy-MM-dd') and so.riq<=to_date('"+riq_s+"','yyyy-MM-dd')) jinyl,\n");
		
//		bf.append(" sy.fady+sy.gongry+sy.cuns+sy.qity haoyd,\n");
		temp.append(" (select sum(sy.fady+sy.gongry+sy.cuns+sy.qity) from shouhcrbyb sy where  sy.diancxxb_id=zk.id and sy.riq=to_date('"+riq_s+"','yyyy-MM-dd'))  haoyd, \n");
		
		temp.append(" (select sum(su.fady+su.gongry+su.qity+su.cuns) from shouhcrbyb su where su.diancxxb_id=zk.id and su.riq>=to_date('"+qissj+"','yyyy-MM-dd') and su.riq<=to_date('"+riq_s+"','yyyy-MM-dd')) haoyl, \n");
		
//		bf.append(" sy.kuc youkc \n");
		temp.append("(select sum(sy.kuc) from shouhcrbyb sy where sy.diancxxb_id=zk.id and sy.riq=to_date('"+riq_s+"','yyyy-MM-dd')) youkc, \n");
		
		temp.append(" (select sy.beiz from shouhcrbyb sy where sy.diancxxb_id=zk.id and sy.riq=to_date('"+riq_s+"','yyyy-MM-dd')) beiz \n");
		
		temp.append(" from \n");
		temp.append(" (  select di.id,j.id jihkjb_id,di.mingc from diancxxb di,jihkjb j where di.fuid="+diancxxb_id+" and j.id in (1,2,3)\n");
		temp.append("  union  \n");
		temp.append("  select dc.id,j.id jihkjb_id,dc.mingc  from diancxxb dc,jihkjb j where dc.id="+diancxxb_id+" and j.id in (1,2,3) and not exists \n");
		temp.append("  (select di.id from diancxxb di where di.fuid="+diancxxb_id+") \n");
		temp.append("  order by  id  ) zk \n");
		
		
		
		
		bf.append(" select distinct * from ( \n");
		
		bf.append(" select xiangm,yuecjc,\n");
		bf.append(" decode(tjkj,"+jihkj_zddh_id+",'"+jihn+"',"+jihkj_sccg_id+",'"+jihw+"',"+jihkj_qydh_id+",'"+jihw+"') kj,\n");
		bf.append(" yuejhtpm,td,tl,dd,dl,hd,hl,benqkc,pingjkyts,jinyd,jinyl,haoyd,haoyl,youkc,beiz \n");
		bf.append("  from ( \n");
		bf.append(temp.toString());
		bf.append(" ) \n");
		bf.append(" union \n");
		
		bf.append(" select '�ϼ�' xiangm,sum(nvl(yuecjc,0))/3 yuecjc,\n");
		bf.append(" '' kj,\n");
		bf.append(" sum(decode(REGEXP_INSTR(yuejhtpm,'[[:digit:]]$'),0,0,yuejhtpm))||'' yuejhtpm,sum(nvl(td,0)) td,sum(nvl(tl,0)) tl,sum(nvl(dd,0))/3 dd, sum(nvl(dl,0))/3 dl,sum(nvl(hd,0))/3 hd,sum(nvl(hl,0))/3 hl,sum(nvl(benqkc,0))/3 benqkc, round_new((sum(nvl(benqkc, 0)) / 3)/(sum(nvl(hd, -1)) / 3),0)|| '' pingjkyts,sum(nvl(jinyd,0))/3 jinyd,sum(nvl(jinyl,0))/3 jinyl,sum(nvl(haoyd,0))/3 haoyd,sum(nvl(haoyl,0))/3 haoyl,sum(nvl(youkc,0))/3 youkc,'' beiz \n");
		bf.append("  from ( \n");
		bf.append(temp.toString());
		bf.append(" ) \n");
		
//		bf.append(" ) order by (select xuh from diancxxb where mingc=xiangm) desc,kj,yuejhtpm asc \n");
		bf.append(" ) order by (select xuh from diancxxb where mingc=xiangm union select 0 from dual where  not exists (select xuh from diancxxb where mingc=xiangm)) asc,kj,yuejhtpm asc \n");
		
		//System.out.println(temp.toString());
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
	
	// ȼ�ϲɹ���ָ���������ձ�
	private String getHuaybgd() {
		Report rt = new Report();
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		StringBuffer bf=new StringBuffer();
		bf=this.getBaseSql();
		ResultSetList rs = con.getResultSetList(bf.toString());
		
		String ArrHeader[][]=new String[2][18];
		ArrHeader[0]=new String[] {"��Ŀ","�³����","ͳ�ƿھ�","�¼ƻ�","ͳ��ú","ͳ��ú","��ú","��ú","��ú","��ú","���ڿ��","ƽ����������","��","��","��","��","��","��"
									};
		ArrHeader[1]=new String[] {"��Ŀ","�³����","ͳ�ƿھ�","ͳ��ú","����","�ۼ�","����","�ۼ�","����","�ۼ�","���ڿ��","ƽ����������","����","�ۼ�","����","�ۼ�","���","˵��"};
	
//		int[] ArrWidth = new int[] { 120, 80, 80, 80, 60, 60 ,60 ,60 ,60 ,60 ,60, 60 ,60 ,60 ,60 ,60 ,60 ,80};
		int[] ArrWidth = new int[] { 110, 60, 60, 60, 55, 55 ,55 ,55 ,55 ,55 ,55, 55 ,50 ,50 ,50 ,50 ,50 ,60};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setWidth(ArrWidth);
		
		
		rt.setTitle(visit.getDiancqc()+"����ȼ�����Ĵ�����ձ���", ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(1, 3, "����:" +this.getDateStr(this.getRiq(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(15, 2, "��λ: ��",
				Table.ALIGN_RIGHT);
		
	
		for (int i = 1; i <= 18; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rt.body.merge(0, 0, 2, 18);
		
//		rt.body.merge(2, 0, rs.getRows()+1, 3);
//		rt.body.merge(3, 7, rs.getRows()+2, 17);
		
		rs.beforefirst();
		
		String dianc_mc="";
		int row_count=0;//���糧�ļ�¼����
		int row_old=0;//��һ���糧�ļ�¼����
		int start_row=3;
		boolean t=false;
		while(rs.next()){
			t=true;
			
			if(rs.getString("xiangm").equals("�ϼ�")){
				continue;
			}
			if(!dianc_mc.equals(rs.getString("xiangm"))){
				
				if(row_count!=0){
					
					rt.body.merge(row_old-row_count+start_row+1, 1, row_old+start_row, 1);
					rt.body.merge(row_old-row_count+start_row+1, 2, row_old+start_row, 2);
					rt.body.merge(row_old-row_count+start_row+1, 3, row_old+start_row, 3);
					for(int i=7;i<=18;i++){
						rt.body.merge(row_old-row_count+start_row+1, i, row_old+start_row, i);
					}
				}
				
				dianc_mc=rs.getString("xiangm");
				row_count=1;
				row_old+=1;
			}else{
				row_count++;
				row_old++;
			}
		}
		
		if(t){

			rt.body.merge(row_old-row_count+start_row+1, 1, row_old+start_row, 1);
			rt.body.merge(row_old-row_count+start_row+1, 2, row_old+start_row, 2);
			rt.body.merge(row_old-row_count+start_row+1, 3, row_old+start_row, 3);
			for(int i=7;i<=18;i++){
				rt.body.merge(row_old-row_count+start_row+1, i, row_old+start_row, i);
			}
		}
		rs.beforefirst();
		
		
		for(int i=2;i<rs.getRows()+3;i++){
//			for(int j=1;j<17;j++){
			for(int j=1;j<19;j++){
				
				String va_str=rt.body.getCellValue(i, j);
				if((va_str==null || va_str.equals("")) && j!=18 && j!=3){
					va_str="0";
				}
				if(j==12){
					rt.body.setCellValue(i, j, rt.body.format(va_str, "0"));
				}else if(j>=2 && j<=11){
					rt.body.setCellValue(i, j, rt.body.format(va_str, meil_table_show));
				}else{
					rt.body.setCellValue(i, j, rt.body.format(va_str, qit_table_show));
				}
				
			}
		}
		
		
//		rt.body.ShowZero = false;
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
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
		
		
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		
		
//		tb1.addText(new ToolbarText("�̵���:"));
//		ComboBox shij = new ComboBox();
//		shij.setTransform("BianmSelect");
//		shij.setWidth(130);
//		shij
//				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
//		tb1.addField(shij);
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
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select distinct pd.id id,pd.bianm bianm from pandtjb pt ,pandb pd where pt.pandb_id=pd.id and pd.diancxxb_id="+visit.getDiancxxb_id()+" order by pd.bianm desc \n");
			
				
			
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
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