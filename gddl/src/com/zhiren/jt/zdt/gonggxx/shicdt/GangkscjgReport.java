package com.zhiren.jt.zdt.gonggxx.shicdt;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class GangkscjgReport  extends BasePage implements PageValidateListener{
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//��ʼ����v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setList1(null);
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	/**
	 * ���缯�ŵ�ú��Ϣ�ձ���
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//��ǰ����
//		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
//		int ArrWidth[] = null;
//		String ArrHeader[][] = null;
		String titlename="�е����ȼ�Ϲ�����Ҫָ��";
//		int iFixedRows=0;//�̶��к�
//		int iCol=0;//����
//		String danwmc="";//��������
		//��������
		titlename=titlename+"";
		String dr1="",dr2="",dr3="",dr4="",dr5="";
		String ylj1="",ylj2="",ylj3="",ylj4="",ylj5="";
		StringBuffer yued_sql = new StringBuffer();		
		StringBuffer strSQL = new StringBuffer();
		
		yued_sql.append(" select leix,zhib,dangy,danw,tongb,huanb,nianlj,tongb2,huanb2 ")
			.append(" from zhuyzb z,diancxxb d ")
			.append(" where z.riq = ").append(riq)
			.append(" and z.diancxxb_id = d.id ")
			.append(" order by z.id");
		
		strSQL.append(yued_sql.toString());
		String str = "";
		if(isJitUserShow()){
			str = " ";
		}else if(isGongsUser()){
			str = " and (dc.fgsid=" + ((Visit)getPage().getVisit()).getDiancxxb_id() + " or dc.rlgsid=" + ((Visit)getPage().getVisit()).getDiancxxb_id() + ")";
        }else if(isDiancUser()){
        	str = " and dc.rlgsid = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + " ";
        }
		ResultSetList rsl = null;
		//���Ĵ�
		String ri_sql = 
                 "select nvl(dr.gm,0) as gm,\n" + 
                 "              nvl(dr.hy,0) as hy,\n" + 
                 "              nvl(dr.kuc,0) as kuc,\n" + 
                 "              nvl(s.gm,0) as gms,\n" + 
                 "              nvl(s.hy,0) as hys,\n" + 
                 "              nvl(s.kuc,0) as kucs \n" + 
                 "         from\n" + 
                 "              (select\n" + 
                 "                      sum(dangrgm) gm,\n" + 
                 "                      sum(haoyqkdr) hy,\n" + 
                 "                      sum(kuc) as kuc\n" + 
                 "                      from shouhcrbb hc, vwdianc dc\n" + 
                 "                      where hc.diancxxb_id = dc.id\n" + 
                 str+"\n"+
                 "                      and riq = " + riq + 
                 "\n" + 
                 "                     ) dr,\n" + 
                 "              (select sum(dangrgm) gm,\n" + 
                 "                      sum(haoyqkdr) hy,\n" + 
                 "                      sum(kuc) as kuc\n" + 
                 "                      from shouhcrbb hc, vwdianc dc\n" + 
                 "                      where hc.diancxxb_id = dc.id\n" + 
                 "                      and riq >= first_day(" + riq + ")\n"+
                 "                      and riq < " + riq + "+1\n" + 
                 str+"\n"+
                 "                      ) s \n";
        //��¯ú��ֵ         
        String rl_sql = 
        		 "select nvl(dr.qnet_ar1,0) as farl_dr,\n" +
        		 "              nvl(lj.qnet_ar1,0) as farl_llj \n" + 
                 "         from\n" + 
                 "              (select round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,2)*jianzl)/sum(jianzl)),2),0)/0.0041816,0) as qnet_ar1\n" + 
                 "                       from(select dc.id as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,\n" + 
                 "                       round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as qnet_ar,\n" + 
                 "                       round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1\n" + 
                 "                       from rulmzlb r,vwdianc dc\n" + 
                 "                       where r.rulrq<=" + riq + "\n" + 
                 str+"\n"+
                 "                       group by dc.id ) sj) dr,\n" + 
                 "                       (select round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,2)*jianzl)/sum(jianzl)),2),0)/0.0041816,0) as qnet_ar1\n" + 
                 "                       from(select dc.id as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,\n" + 
                 "                       round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as qnet_ar,\n" + 
                 "                       round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1\n" + 
                 "                       from rulmzlb r,vwdianc dc\n" + 
                 "                       where r.rulrq>=" + riq + "\n" + 
                 "                       and r.rulrq<=" + riq + "\n" + 
                 str+"\n"+
                 "                       and dc.id=r.diancxxb_id\n" + 
                 "                       group by dc.id ) sj) lj \n";
        //�볧ú��ֵ         
        String rc_sql = 

        	"select nvl(cdr.farldk,0) as farl_cdr,nvl(clj.farldk,0) as farl_clj\n" +
        	"from (select decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)) as farl,\n" + 
        	"       round_new(decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)*1000/4.1816),0) as farldk\n" + 
        	" from (select (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0))   as laiml,\n" + 
        	"       round(decode(sum(nvl(z.qnet_ar,0)),0,0,(round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,\n" + 
        	"       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,\n" + 
        	"              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,2))/\n" + 
        	"              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as farl\n" + 
        	"       from fahb fh,zhilb z,vwdianc dc\n" + 
        	"       where fh.zhilb_id=z.id\n" + 
        	"         and fh.diancxxb_id=dc.id\n" + 
        	"         and fh.daohrq<=" + riq + "\n" + 
        	str+"\n"+
        	" group by fh.lieid) sj) cdr,\n" + 
        	" (select decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)) as farl,\n" + 
        	"       round_new(decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)*1000/4.1816),0) as farldk\n" + 
        	" from (select (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0))   as laiml,\n" + 
        	"       round(decode(sum(nvl(z.qnet_ar,0)),0,0,(round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,\n" + 
        	"       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,\n" + 
        	"              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,2))/\n" + 
        	"              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as farl\n" + 
        	"       from fahb fh,zhilb z,vwdianc dc\n" + 
        	"       where fh.zhilb_id=z.id\n" + 
        	"         and fh.diancxxb_id=dc.id\n" + 
        	"         and fh.daohrq>=" + riq + "\n" + 
        	"         and fh.daohrq<=" + riq + "\n" + 
        	str+"\n"+
        	" group by fh.lieid) sj) clj";
//
//
//		System.out.println("1111111111111"+ri_sql);
//		System.out.println("2222222222222"+rl_sql);
//		System.out.println("3333333333333"+rc_sql);
		try{
			rsl = cn.getResultSetList(ri_sql);
			if(rsl.next()){
				dr1 = rsl.getString("gm");
				dr2 = rsl.getString("hy");
				dr3 = rsl.getString("kuc")+"";
				
				ylj1 = rsl.getString("gms");
				ylj2 = rsl.getString("hys");
				ylj3 = rsl.getString("kucs");
				
			}
			
			rsl = cn.getResultSetList(rl_sql);
			if(rsl.next()){
				dr5 = rsl.getString("farl_dr");
				ylj5 = rsl.getString("farl_llj");
			}
			
			rsl = cn.getResultSetList(rc_sql);
			if(rsl.next()){
				dr4 = rsl.getString("farl_cdr");
				ylj4 = rsl.getString("farl_clj");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}	
	
		Report rtDay = new Report();
		String ArrHeaderDay[][]=new String[7][5];
		ArrHeaderDay[0]=new String[] {"","ָ��","����","���ۼ�","��λ"};
		ArrHeaderDay[1]=new String[] {"���Ĵ�","�ɹ�ԭú",dr1,ylj1,"���"};
		ArrHeaderDay[2]=new String[] {"","�� ��",dr2,ylj2,"���"};
		ArrHeaderDay[3]=new String[] {"","���",dr3,ylj3,"���"};
		ArrHeaderDay[4]=new String[] {"","","","",""};
		ArrHeaderDay[5]=new String[] {"����","�볧ú��ֵ",dr4,ylj4,"ǧ��/ǧ��"};
		ArrHeaderDay[6]=new String[] {"","��¯ú��ֵ",dr5,ylj5,"ǧ��/ǧ��"};

		int ArrWidthDay[]=new int[] {54,54,54,54,54};


		rtDay.setTitle(titlename,ArrWidthDay);
		rtDay.setDefaultTitle(1, 5, DateUtil.Formatdate("yyyy��MM��dd��",getBeginriqDate()), Table.ALIGN_CENTER);
		
		
		
		rtDay.setBody(new Table(ArrHeaderDay,0,0,0));
		
		rtDay.body.setColAlign(1, Table.ALIGN_CENTER);
		//rtDay.body.setRowCells(1, Table.PER_BACKCOLOR, "silver");//���ñ�����
		rtDay.body.setRowCells(1, Table.PER_FONTSIZE,14);//�ֺ�
		rtDay.body.setRowCells(1, Table.PER_FONTNAME,"����" );//����
		rtDay.body.setColCells(1, Table.PER_FONTSIZE,14);
		rtDay.body.setColCells(1, Table.PER_FONTNAME,"����");
		rtDay.body.setRowCells(1, Table.PER_FORECOLOR,"#0099FF");//��һ����ɫ
		rtDay.body.setColCells(1, Table.PER_FORECOLOR, "#0099FF");//��һ����ɫ
		rtDay.body.setCells(2,1,4,5,Table.PER_BACKCOLOR, "#FFCC99");//(2,1)��(4,5)��������
		rtDay.body.setCells(6,1,7,5,Table.PER_BACKCOLOR, "#FFCCFF");
		rtDay.body.setCellAlign(1,2, Table.ALIGN_CENTER);//     
		rtDay.body.setCellAlign(1,3, Table.ALIGN_CENTER);//    ��
		rtDay.body.setCellAlign(1,4, Table.ALIGN_CENTER);//    ��
		rtDay.body.setCellAlign(1,5, Table.ALIGN_CENTER);//
		rtDay.body.setCells(2,2,7,5,Table.PER_FONTSIZE,10);//(2,2)��(7,5)�����ֺ�
//		�ϲ���Ԫ��
		rtDay.body.mergeCell(2,1,4,1);
		rtDay.body.mergeCell(6,1,7,1);
			
		String ArrHeader[][]=new String[1][9];
		ArrHeader[0]=new String[] {"","ָ��","����","��λ","ͬ��","����","���ۼ�","ͬ��","����"};

		int ArrWidth[]=new int[] {70,90,70,70,70,70,70,70,70};

			ResultSet rs = cn.getResultSet(strSQL.toString());

			// ����
			//rt.setBody(new Table(rs,1, 0, 1));
			
			
			Table tb=new Table(rs, 1, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(DateUtil.Formatdate("yyyy��MM���¶�����",getBeginriqDate()), ArrWidth);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setRowCells(1, Table.PER_FONTSIZE,14);//���ֺ�
			rt.body.setRowCells(1, Table.PER_FONTNAME,"����" );//������
			rt.body.setColCells(1, Table.PER_FONTSIZE,14);//���ֺ�
			rt.body.setColCells(1, Table.PER_FONTNAME,"����");//������
			rt.body.setRowCells(1, Table.PER_FORECOLOR,"#0099FF");//��һ����ɫ
			rt.body.setColCells(1, Table.PER_FORECOLOR, "#0099FF");//��һ����ɫ
			rt.body.setCells(2,1,4,9,Table.PER_BACKCOLOR, "#FFCC99");//(2,1)��(4,9)���򱳾���ɫ
			rt.body.setCells(5,1,6,9,Table.PER_BACKCOLOR, "#FFCCFF");
			rt.body.setRowCells(7, Table.PER_BACKCOLOR, "#CCCCFF");
			rt.body.setCells(2,2,7,9,Table.PER_FONTSIZE,10);//(2,2)��(7,9)�����ֺ�
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
//			rt.body.setRowCells(1, Table.PER_BORDER_RIGHT,1)��
			
			rt.body.mergeCol(1);//�ϲ�
			//rt.body.mergeFixedCols();//�ϲ�
//			rt.body.merge(2,1, 4, 1);//�ϲ�
			
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rtDay.getAllPagesHtml()+rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}


}