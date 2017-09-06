package com.zhiren.dc.huaygl.rucmzjybb_yc;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Rucmzjybb extends BasePage implements PageValidateListener {
	
	private String zhilb_id; // ���ǰ��ҳ�洫������zhilb_id
	
	private String dianc_id; // ���ǰ��ҳ�洫������dianc_id
	
	public String getDianc_id() {
		return dianc_id;
	}

	public void setDianc_id(String dianc_id) {
		this.dianc_id = dianc_id;
	}

	public String getZhilb_id() {
		return zhilb_id;
	}

	public void setZhilb_id(String zhilb_id) {
		this.zhilb_id = zhilb_id;
	}

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		StringBuffer sb=new StringBuffer();
		sb.append("");
		sb.append("), (select * from (SELECT DISTINCT (SELECT decode(mingc,null,' ',mingc) as mingc \n");
		sb.append(" FROM guobb \n");
		sb.append("WHERE xiangmmc = 'ȫˮ��') AS MTFF,\n");
		sb.append(" (SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("   FROM guobb \n");
		sb.append("  WHERE xiangmmc = '���������ˮ��') AS MADFF,\n");
		sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		sb.append("FROM guobb \n");
		sb.append("WHERE xiangmmc = '����������ҷ�') AS AADFF,\n");
		sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�յ����ҷ�') AS AARFF,\n");
		sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		sb.append(" FROM guobb \n");
	    sb.append("WHERE xiangmmc = '������ҷ�') AS ADFF,\n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '����������ӷ���') AS VADFF, \n");
        sb.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����޻һ��ӷ���') AS VDAFFF,\n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '���������ȫ��') AS STADFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append(" FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����ȫ��') AS STDFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append(" FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�յ���ȫ��') AS STARFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����������') AS HADFF, \n");
        sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�յ�����') AS HARFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����������Ͳ��ֵ') AS QBADFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append(" WHERE xiangmmc = '�������λ��ֵ') AS QGRDFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append(" WHERE xiangmmc = '�̶�̼') AS FCADFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append(" FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����������λ��ֵ') AS QGRADFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append(" FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�����޻һ���λ��ֵ') AS QGRDAFFF, \n");
	    sb.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
	    sb.append("FROM guobb \n");
	    sb.append("WHERE xiangmmc = '�յ�����λ��ֵ') AS QNETARFF \n");
	    sb.append("FROM guobb) g)");
	    String bianm="";
	    String mk ="";
	    if(!"��ѡ��".equals(getBianmValue())){
	    	bianm=getBianmValue().getValue();
	    	
	    }
	  
		String strSql = "select * from ("+
			"select zlls.id zlls_zhilb_id, getHuaybh4zl(zlls.id) bianm, max(mkxx.mingc) mkmc,max(fh.ches) as cheshu,max(cy.caiyry) caiyry, \n" +
			"        max(czxx.mingc) fzmc, to_char(max(zlls.huaysj), 'yyyy-MM-dd') huaysj, to_char(cy.caiyrq, 'yyyy-MM-dd') caiyrq, sum(fh.laimsl) laimsl,\n" + 
			"        pz.mingc meiz, GetZhiyry(max(yp.id)) lurry, GETHUAYBBCHEPS(zlls.id) cheph, getbeiz(zlls.id) beiz,\n" + 
			"        gethuayy4zl(zlls.id) huayy,  gethuaybh4zl(zlls.id) huybh,(select max(shenhry) from zhillsb where zhilb_id =zlls.id) yisry,(select max(shenhryej) from zhillsb where zhilb_id =zlls.id) yisryej,\n" + 
			"        round_new(avg(zlls.qnet_ar),2)*1000 qnet_ar, round_new(avg(zlls.aar),2) aar, formatxiaosws(round_new(avg(zlls.ad),2),2) ad,\n" + 
			"        formatxiaosws(round_new(avg(zlls.vdaf),2),2) vdaf, formatxiaosws(round_new(avg(zlls.mt), 1),1) mt, formatxiaosws(round_new(avg(zlls.stad),2),2) stad,\n" + 
			"        formatxiaosws(round_new(avg(zlls.aad),2),2) aad, formatxiaosws(round_new(avg(zlls.mad),2),2) mad, round_new(avg(zlls.qbad),2)*1000 qbad,\n" + 
			"        formatxiaosws(round_new(avg(zlls.had),2),2) had, formatxiaosws(round_new(avg(zlls.vad),2),2) vad, formatxiaosws(round_new(avg(zlls.std),2),2) std,\n" + 
			"        round_new(avg(zlls.qgrad),2) qgrad, round_new(avg(zlls.qgrad_daf),2) qgrad_daf, round_new(avg(zlls.har),2) har,\n" + 
			"        round_new(avg(zlls.qgrd),2)*1000 qgrd, round_new((100 - avg(zlls.mt)) * avg(zlls.stad) / (100 - avg(zlls.mad)),2) star,\n" + 
			"        round_new(avg(zlls.qnet_ar) * 7000 / 29.271,0) frl," +
			" formatxiaosws(round_new(avg(zlls.fcad),2),2)  fcad" +
			"\n" + 
			"      from zhilb zlls, fahb fh, meikxxb mkxx, chezxxb czxx, caiyb cy, pinzb pz,yangpdhb yp\n" + 
			"      where\n" + 
			"\n" + 
			"       fh.zhilb_id = zlls.id and fh.meikxxb_id = mkxx.id\n" + 
			"        and fh.faz_id = czxx.id and cy.zhilb_id = zlls.id and fh.pinzb_id = pz.id  and yp.caiyb_id=cy.id  \n" + 
			"        and zlls.huaysj=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
			"        and getHuaybh4zl(zlls.id) ='"+ bianm+"' \n" + 
			//"        and mkxx.mingc='"+ mk +"' \n" +
			"      group by zlls.id, mkxx.mingc, cy.caiyrq, pz.mingc "+sb.toString();

		ResultSetList rs = con.getResultSetList(strSql);
		String huayy = ""; // ���뻯��Ա��Ϣ
		String yisry="";
		String yisryej="";
//		System.out.println(strSql);
		String[][] ArrBody = new String[17][8];
		if (rs.next()) {
			ArrBody[0] = new String[] {"���",rs.getString("mkmc")+"<br>"+ rs.getString("bianm"),"", "��վ",rs.getString("fzmc"),"" , "ú��(t)", rs.getString("laimsl")};

			
			ArrBody[1] = new String[] {"ú��", rs.getString("meiz"),"����",rs.getString("cheshu"),"��������",rs.getString("huaysj"), "������Ա", "������"};
			
			ArrBody[2] = new String[] {"���ƺ�", "", "", "", "��������", rs.getString("caiyrq"), "������Ա", rs.getString("lurry")};
			
			ArrBody[3] = new String[] {"�յ���ˮ��Mar(%)", "", "","", "", rs.getString("mt")+"", rs.getString("MTFF"), ""};//mt==mar
			
			ArrBody[4] = new String[] {"���������ˮ��Mad(%)", "",  "","",  "", rs.getString("mad")+"", rs.getString("MADFF"), ""};
			
			ArrBody[5] = new String[] {"����������ҷ�Aad(%)", "",  "","",  "", rs.getString("aad")+"", rs.getString("AADFF"), ""};
//			
			
			ArrBody[6] = new String[] {"������ҷ�Ad(%)", "",  "","",  "", rs.getString("ad")+"", rs.getString("ADFF"), ""};
			
			ArrBody[7] = new String[] {"����������ӷ���Vad(%)", "",  "","",  "", rs.getString("vad")+"", rs.getString("VADFF"), ""};
			
			ArrBody[8] = new String[] {"�����޻һ��ӷ���Vdaf(%)", "",  "","",  "", rs.getString("vdaf")+"", rs.getString("VDAFFF"), ""};
			ArrBody[9] = new String[] {"�̶�̼FCad(%)", "",  "","",  "", rs.getString("fcad")+"", rs.getString("FCADFF"), ""};
//			
			ArrBody[10] = new String[] {"���������ȫ��St,ad(%)", "", "","",  "", rs.getString("stad")+"", rs.getString("STADFF"), ""};
			
			ArrBody[11] = new String[] {"�����ȫ��St,d(%)", "",  "","",  "", rs.getString("std")+"", rs.getString("STDFF"), ""};
//			
//			
			ArrBody[12] = new String[] {"�����������Had(%)", "",  "","",  "", rs.getString("had")+"", rs.getString("HADFF"), ""};
//			
//			
			ArrBody[13] = new String[] {"��Ͳ������Qb.ad(J/g)", "", "","",  "", rs.getLong("qbad")+"", rs.getString("QBADFF"), ""};
			
			ArrBody[14] = new String[] {"�������λ��ֵQgr,d(J/g)", "",  "","",  "", rs.getLong("qgrd")+"", rs.getString("QGRDAFFF"), ""};
			ArrBody[15] = new String[] {"�յ�����λ��ֵQnet,ar(J/g)", "",  "","",  "", rs.getLong("qnet_ar")+"<br>��Լ"+rs.getLong("frl")+"��/�ˣ�", rs.getString("QNETARFF"), ""};
 
			ArrBody[16] = new String[] {"��ע", "",  "","",  "", "", "", ""};
//			�ӽ������ȡ����ע��Ϣ��ʾ��ҳ����
//			String beiz = rs.getString("beiz");
//			if (beiz != null && !"".equals(beiz)) {
//				for (int i = 5; i <= 19; i ++) {
//					ArrBody[i][7] = beiz;
//					ArrBody[i][8] = beiz;
//				}
//			}
			huayy = rs.getString("huayy"); //����Ա
			 yisry=rs.getString("yisry"); //һ����Ա
			 yisryej=rs.getString("yisryej");//������Ա
			if (huayy == null) { // ����ӽ������ȡ�����Ļ���Ա��Ϣ��null����ô��ҳ������ʾ""(�մ�)
				huayy = "";
			}
//			System.out.println("if method");
		} else {
			return null;
		}
		
		int[] ArrWidth = new int[] {75, 75, 75, 70, 90, 135,90,85};
		Visit visit = (Visit) getPage().getVisit();
		rt.setTitle(visit.getDiancmc()+"�볧ú�ʼ��鱨��", ArrWidth);
		rt.setDefaultTitle(7, 2, getBRiq().replaceFirst("-", "��").replaceFirst("-", "��")+"��", Table.ALIGN_CENTER);
		rt.setBody(new Table(ArrBody, 0, 0, 0));//new Table(ArrBody, 0, 0, 0)//new Table(17,9)
		rt.body.setWidth(ArrWidth);
		rt.body.setCells(1, 1, 17,8, Table.PER_ALIGN, Table.ALIGN_CENTER);
		for(int i=1;i<=rt.body.getRows();i++){
			if(i==17){
				rt.body.setRowHeight(i, 100);
			}else{
				rt.body.setRowHeight(i, 40);
			}
			
			
		}
		 
		
		for(int i = 1; i <= 17; i ++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 12);
		}
		for (int i = 4; i <= 16; i ++) {
			rt.body.mergeCell(i, 1, i, 5);
		}

		rt.body.mergeCell(3, 2, 3, 4);
		rt.body.setCells(3, 2, 3, 2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.mergeCell(1, 2, 1, 3);

		
		rt.body.mergeCell(1, 5, 1, 6);	
		for(int i=4;i<=16;i++){
			
			rt.body.mergeCell(i, 7, i, 8);
		}
		
		
		rt.title.setRowHeight(17, 50);
		rt.body.mergeCell(17, 2, 17, 8);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, " ��׼��"+yisryej, Table.ALIGN_LEFT);// + DateUtil.Formatdate("yyyy��MM��dd��", new Date())
		rt.setDefautlFooter(4, 2, " ��ˣ�"+yisry , Table.ALIGN_CENTER);//+huayy
		rt.setDefautlFooter(7, 2, " ������"+huayy , Table.ALIGN_CENTER);//+huayy
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rs.close();
		con.Close();
		return rt.getAllPagesHtml();
		
	}
//	 ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRiq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("riq");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));

		
		tb1.addText(new ToolbarText("�������:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		//shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		shij.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		
		ToolbarButton rbtn = new ToolbarButton(null, "ˢ��",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		
		

		
		setToolbar(tb1);
	}
	
//	 ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	
	// �����Ƿ�仯
	private boolean riqchange = false;
//	������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (this.briq != null) {
			if (!this.briq.equals(briq))
				riqchange = true;
		}
//		this.riq = riq;
		
		this.briq = briq;
	}

//	// ������
//	private String riq;
//
//	public String getRiq() {
//		return riq;
//	}
//
//	public void setRiq(String riq) {
//		if (this.riq != null) {
//			if (!this.riq.equals(riq))
//				riqchange = true;
//		}
//		this.riq = riq;
//	}

	/*
	 * �����ݿ��ѯ�����ĳ�Ƥ���ַ������ܺܳ����Ӷ�Ӱ�쵽�����ȵ�������ʾ��
	 * �������Ƥ���ַ�������<br>(���з�)��������һ���⡣
	 */
	public String getCheph(String strCheph) {
		String[] arr = strCheph.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i ++) {
			sb.append(arr[i] + ",");
			if (sb.toString().split(",").length % 10 == 0) {
				sb.append("<br>");
			}
		}
		if (sb.toString().endsWith("<br>")) {
			return sb.toString();
		} else {
			return sb.substring(0, sb.length() - 1);
		}
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
	
	//
	private boolean hychange = false;
	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		if(value != null&&!"".equals(value)){
			if (!(((Visit) this.getPage().getVisit()).getDropDownBean1() == value)){
				hychange = true;
			}
		}
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
		
		String sss="select zlls.id id, gethuaybh4zl(zlls.id) as mingc   from zhilb zlls, fahb fh, meikxxb mkxx, chezxxb czxx, caiyb cy, pinzb pz " +
		" where "+"fh.zhilb_id = zlls.id and fh.meikxxb_id = mkxx.id and  fh.faz_id = czxx.id and cy.zhilb_id = zlls.id and fh.pinzb_id = pz.id"
		+"  and zlls.huaysj=to_date('"+getBRiq()+"','yyyy-mm-dd')"
	//	+" and mkxx.mingc='"+getMkValue().getValue()
		+" group by zlls.id, mkxx.mingc, cy.caiyrq, pz.mingc"
		;
		
//	System.out.println(sss.toString());
		setBianmModel(new IDropDownModel(sss, "��ѡ��"));
	}

	

	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		if (!visit.getActivePageName().equals(getPageName())) {
//			visit.setActivePageName(getPageName());
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(this.getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			// setYuefValue(null);
//			getBRiq();
			setBianmValue(null);
			setBianmModel(null);
			getBianmModel();
		
			
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
			getBianmModel();
			
		}
		
		getSelectData();
	}
}
