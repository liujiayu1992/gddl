package com.zhiren.dc.chengbgl.baob;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ǹ��ʷ����������ι�˾/�������Ƿ����������ι�˾����ú�ձ���
 * ʱ�䣺2010-05-24
 * ���ߣ�����
 */
public class Jincmrb_yc extends BasePage {
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
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	������
//	private String riq;
//
//	public String getRiq() {
//		return riq;
//	}
//
//	public void setRiq(String riq) {
//		this.riq = riq;
//	}
	
	boolean riqichange = false;
	private String riq;
	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setRiq(String riq) {
		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqichange = true;
		}
	}
	
//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id()+" order by id");
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
//	��ȡ��ص�SQL
	public StringBuffer getBaseSql() {
		Visit v = (Visit) this.getPage().getVisit();
		long diancxxbid=0;
		if(v.isFencb()){
			diancxxbid = getChangbValue().getId();
		}else{
			diancxxbid = v.getDiancxxb_id();
		}
		String CurrentDate = DateUtil.FormatOracleDate(getRiq());
		String FirstDateOfMonth = DateUtil.FormatOracleDate(DateUtil.getFDOfMonth(getRiq()));
		StringBuffer sb = new StringBuffer();
		String sql = 
			"select decode(grouping(y.mingc), 1, '�ܼ�', y.mingc) as yunsfsb_id,\n" +
			"       decode(grouping(y.mingc) + grouping(g.mingc) + grouping(m.mingc),\n" + 
			"              2,\n" + 
			"              '�ϼ�',\n" + 
			"              g.mingc) as gongysb_id,\n" + 
			"       decode(grouping(y.mingc) + grouping(g.mingc) + grouping(m.mingc),\n" + 
			"              1,\n" + 
			"              'С��',\n" + 
			"              m.mingc) as meikxxb_id,\n" + 
			"       sum(nvl(yq.ches,0) + nvl(eq.ches,0)) as ches,\n" + 
			"       sum(yq.biaoz_kf) as yqkf,\n" + 
			"       sum(eq.biaoz_kf) as eqkf,\n" + 
			"       sum(nvl(yq.biaoz_kf,0) + nvl(eq.biaoz_kf,0)) as kfxj,\n" + 
			"       sum(yq.jingz_ss) as yqss,\n" + 
			"       sum(eq.jingz_ss) as eqss,\n" + 
			"       sum(nvl(yq.jingz_ss,0) + nvl(eq.jingz_ss,0)) as ssxj,\n" + 
			"       sum((nvl(yq.jingz_ss,0) + nvl(eq.jingz_ss,0)) - (nvl(yq.biaoz_kf,0) + nvl(eq.biaoz_kf,0))) as chaz,\n" + 
			"       sum((nvl(yq.biaoz_kf,0) + nvl(eq.biaoz_kf,0)) * 0.01) as yxls,\n" + 
			"       sum((nvl(yq.jingz_ss,0) + nvl(eq.jingz_ss,0)) - (nvl(yq.biaoz_kf,0) + nvl(eq.biaoz_kf,0)) +\n" + 
			"           (nvl(yq.biaoz_kf,0) + nvl(eq.biaoz_kf,0)) * 0.01) as yingk,\n" + 
			"       sum(yqlj.ches) as yqljcs,\n" + 
			"       sum(eqlj.ches) as eqljcs,\n" + 
			"       sum(nvl(yqlj.ches,0)+nvl(eqlj.ches,0)) as ljcs,\n" + 
			"       sum(yqlj.biaoz_lj) as yqkflj,\n" + 
			"       sum(eqlj.biaoz_lj) as eqkflj,\n" + 
			"       sum(nvl(yqlj.biaoz_lj,0)+nvl(eqlj.biaoz_lj,0)) as kflj,\n" + 
			"       sum(yqlj.jingz_lj) as yqsslj,\n" + 
			"       sum(eqlj.jingz_lj) as eqsslj,\n" + 
			"       sum(nvl(yqlj.jingz_lj,0)+nvl(eqlj.jingz_lj,0)) as sslj,\n" + 
			"       '' as beiz\n" + 
			"  from yunsfsb y,\n" + 
			"       gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       --��ͷ\n" + 
			"       (select distinct f.yunsfsb_id, f.gongysb_id, f.meikxxb_id\n" + 
			"          from fahb f, chepb c\n" + 
			"         where c.fahb_id = f.id\n" + 
			"			and f.diancxxb_id = "+diancxxbid+"\n" +
			"           and f.daohrq <= "+CurrentDate+"\n" + 
			"           and f.daohrq >= "+FirstDateOfMonth+") bt,\n" + 
			"       --һ��\n" + 
			"       (select f.yunsfsb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               count(c.id) as ches,\n" + 
			"               sum(c.biaoz) as biaoz_kf,\n" + 
			"               sum(c.maoz - c.piz - c.zongkd) jingz_ss\n" + 
			"          from fahb f, chepb c\n" + 
			"         where c.fahb_id = f.id\n" + 
			"			and f.diancxxb_id = "+diancxxbid+"\n" +
			"           and f.daohrq = "+CurrentDate+"\n" + 
			"           and (c.zhongchh is null or c.zhongchh = 'Aϵͳ')\n" + 
			"         group by (f.yunsfsb_id, f.gongysb_id, f.meikxxb_id)) yq,\n" + 
			"       --����\n" + 
			"       (select f.yunsfsb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               count(c.id) as ches,\n" + 
			"               sum(c.biaoz) as biaoz_kf,\n" + 
			"               sum(c.maoz - c.piz - c.zongkd) jingz_ss\n" + 
			"          from fahb f, chepb c\n" + 
			"         where c.fahb_id = f.id\n" + 
			"			and f.diancxxb_id = "+diancxxbid+"\n" +
			"           and f.daohrq = "+CurrentDate+"\n" + 
			"           and c.zhongchh = 'Bϵͳ'\n" + 
			"         group by (f.yunsfsb_id, f.gongysb_id, f.meikxxb_id)) eq,\n" + 
			"       --һ���ۼ�\n" + 
			"       (select f.yunsfsb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               count(c.id) as ches,\n" + 
			"               sum(c.biaoz) as biaoz_lj,\n" + 
			"               sum(c.maoz - c.piz - c.zongkd) jingz_lj\n" + 
			"          from fahb f, chepb c\n" + 
			"         where c.fahb_id = f.id\n" + 
			"			and f.diancxxb_id = "+diancxxbid+"\n" +
			"           and f.daohrq <= "+CurrentDate+"\n" + 
			"           and f.daohrq >= "+FirstDateOfMonth+"\n" + 
			"           and (c.zhongchh is null or c.zhongchh = 'Aϵͳ')\n" + 
			"         group by (f.yunsfsb_id, f.gongysb_id, f.meikxxb_id)) yqlj,\n" + 
			"       --�����ۼ�\n" + 
			"       (select f.yunsfsb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               count(c.id) as ches,\n" + 
			"               sum(c.biaoz) as biaoz_lj,\n" + 
			"               sum(c.maoz - c.piz - c.zongkd) jingz_lj\n" + 
			"          from fahb f, chepb c\n" + 
			"         where c.fahb_id = f.id\n" + 
			"			and f.diancxxb_id = "+diancxxbid+"\n" +
			"           and f.daohrq <= "+CurrentDate+"\n" + 
			"           and f.daohrq >= "+FirstDateOfMonth+"\n" + 
			"           and c.zhongchh = 'Bϵͳ'\n" + 
			"         group by (f.yunsfsb_id, f.gongysb_id, f.meikxxb_id)) eqlj\n" + 
			" where bt.yunsfsb_id = y.id\n" + 
			"   and bt.gongysb_id = g.id\n" + 
			"   and bt.meikxxb_id = m.id\n" + 
			"   and bt.yunsfsb_id = yq.yunsfsb_id(+)\n" + 
			"   and bt.yunsfsb_id = eq.yunsfsb_id(+)\n" + 
			"   and bt.yunsfsb_id = yqlj.yunsfsb_id(+)\n" + 
			"   and bt.yunsfsb_id = eqlj.yunsfsb_id(+)\n" + 
			"   and bt.gongysb_id = yq.gongysb_id(+)\n" + 
			"   and bt.gongysb_id = eq.gongysb_id(+)\n" + 
			"   and bt.gongysb_id = yqlj.gongysb_id(+)\n" + 
			"   and bt.gongysb_id = eqlj.gongysb_id(+)\n" + 
			"   and bt.meikxxb_id = yq.meikxxb_id(+)\n" + 
			"   and bt.meikxxb_id = eq.meikxxb_id(+)\n" + 
			"   and bt.meikxxb_id = yqlj.meikxxb_id(+)\n" + 
			"   and bt.meikxxb_id = eqlj.meikxxb_id(+)\n" + 
			" group by rollup(y.mingc, g.mingc, m.mingc)\n" + 
			" order by grouping(y.mingc),\n" + 
			"          y.mingc,\n" + 
			"          grouping(g.mingc),\n" + 
			"          g.mingc,\n" + 
			"          grouping(m.mingc),\n" + 
			"          m.mingc\n";

		sb.append(sql);
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("Riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		
		ResultSet rs = con.getResultSet(getBaseSql(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		String rq = getRiq();
		String[] r = rq.split("-");
		int day = Integer.parseInt(r[2]);
		
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[3][23];
		 ArrHeader[0]=new String[] {"���䷽ʽ","��ú��λ","����(��վ)","�ܳ���","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�� �� �� ú","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�ۼƽ�ú(1-"+day+"��)","�� ע"};
		 ArrHeader[1]=new String[] {"���䷽ʽ","��ú��λ","����(��վ)","�ܳ���","����(��)","����(��)","����(��)","ʵ����(��)","ʵ����(��)","ʵ����(��)","��ֵ","����·��1%(��)","ӯ��(��)","����","����","����","��(��)","��(��)","��(��)","ʵ��(��)","ʵ��(��)","ʵ��(��)","�� ע"};
		 ArrHeader[2]=new String[] {"���䷽ʽ","��ú��λ","����(��վ)","�ܳ���","һ��","����","С��","һ��","����","С��","��ֵ","����·��1%(��)","ӯ��(��)","һ��","����","С��","һ��","����","С��","һ��","����","С��","�� ע"};

		 int ArrWidth[]=new int[] {40,120,120,45,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,45};

		rt.setTitle("���ǹ��ʷ����������ι�˾/�������Ƿ����������ι�˾����ú�ձ���", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 23, DateUtil.Formatdate("yyyy��MM��dd��",stringToDate(getRiq())),Table.ALIGN_CENTER);
//		rt.setDefaultTitle(1, 23, "��λ���֡���", Table.ALIGN_CENTER);
		String[] arrFormat = new String[] { "", "", "", "0", "0.00", "0.00", "0.00", "0.00","0.00", "0.00", "0.00", "0.00", "0.00", "0", "0", "0", "0.00", "0.00", "0.00", "0.00","0.00", "0.00",""};

		rt.setBody(new Table(rs, 3, 0, 3));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
		rt.body.setColAlign(22, Table.ALIGN_RIGHT);
		rt.body.setColAlign(23, Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "��ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(22, 2, "���ڣ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
	
	/**
	 * String������ת��Data������
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date temD = new Date();
		
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			temD = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temD;
	}

	
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		String reportType = cycle.getRequestContext().getParameter("lx");
//		if(reportType != null) {
//			setRiq(DateUtil.FormatDate(new Date()));
//			visit.setInt1(Integer.parseInt(reportType));
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}
		getSelectData();
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
//	ҳ���½��֤
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
}
