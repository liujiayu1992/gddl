//chh 2008-03-17 �޸Ĳ�ѯ��������ʽ
package com.zhiren.jt.zdt.jiesgl.jiesdlb;
/* 
* ʱ�䣺2009-06-16
* ���ߣ� ll
* �޸����ݣ�1���������ں��������볧����
* 		   
*/ 
/* 
* ʱ�䣺2009-07-27
* ���ߣ� ll
* �޸����ݣ�1��ҳ���н������ں��������볧������ʾ�С�
* 		   
*/ 
/* 
* ʱ�䣺2009-08-06
* ���ߣ� ll
* �޸����ݣ��������㵥��ѯҳ����ʽ����ͬ����ȫ����ʾ��
* 		   
*/ 

/* 
* ʱ�䣺2009-08-27
* ���ߣ� chh
* �޸����ݣ�����һ�д���ֵ
* 		   
*/ 
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesdlb extends BasePage {
	public boolean getRaw() {
		return true;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
//		return getQibb();
		
			return getFenctj();
//		} else {
//			return "�޴˱���";
//		}
	}
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	//�����б�
	private String getFenctj() {
		JDBCcon con = new JDBCcon();
		
		Visit visit = (Visit) getPage().getVisit();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		String strDate=OraDate(DateUtil.getDate(getBeginriqDate()));//��ǰ����
		String strDate2=OraDate(DateUtil.getDate(getEndriqDate()));//��ǰ����
		//�糧����
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = " and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		String jihkj="";
		String liuczt="";
		String yunfzt="";
		String jieszt="";
		if(getLeixSelectValue().getValue().equals("ȫ��")){
			liuczt="";
			jieszt="";
			yunfzt="";
		}else if(getLeixSelectValue().getValue().equals("δ���")){
			liuczt=" and js.liucztb_id="+getLeixSelectValue().getId();
			yunfzt=" and yf.liucztb_id="+getLeixSelectValue().getId();
			jieszt=" js.liucztb_id="+getLeixSelectValue().getId()+"and";
		}else if(getLeixSelectValue().getValue().equals("�����")){
			liuczt=" and js.liucztb_id="+getLeixSelectValue().getId();
			yunfzt=" and yf.liucztb_id="+getLeixSelectValue().getId();
			jieszt=" js.liucztb_id="+getLeixSelectValue().getId()+"and";
		}
		if(getJihkjDropDownValue().getValue().equals("ȫ��")){
			jihkj="";
		}else if(getJihkjDropDownValue().getValue().equals("�г��ɹ�")){
			jihkj=" and js.jihkjb_id="+getJihkjDropDownValue().getId();
		}else if(getJihkjDropDownValue().getValue().equals("�ص㶩��")){
			jihkj=" and js.jihkjb_id="+getJihkjDropDownValue().getId();
		}else if(getJihkjDropDownValue().getValue().equals("���򶩻�")){
			jihkj=" and js.jihkjb_id="+getJihkjDropDownValue().getId();
		}
		
		String jsrq="";
		String yfrq="";
		String jsrq1="";
		String jsrq2="";
		if(getRiqSelectValue().getValue().equals("��������")){
			jsrq="js.jiesrq";
			yfrq="yf.jiesrq";
			jsrq1=" js.jiesrq>="+ strDate + " and js.jiesrq<="+ strDate2+ " \n";
			jsrq2="and yf.jiesrq>="+ strDate + " and yf.jiesrq<="+ strDate2+ " \n";
		}else{
			jsrq="js.yansjzrq";
			yfrq="yf.yansjzrq";
			jsrq1=" js.yansjzrq>="+ strDate + " and js.yansjzrq<="+ strDate2+ " \n";
			jsrq2="and yf.yansjzrq>="+ strDate + " and yf.yansjzrq<="+ strDate2+ " \n";
		}
		
		StringBuffer sbsql = new StringBuffer();

		sbsql.append("select mingc,rownum,bianh,hetbh,gongysmc,jiessl,jies_rz,jies_rzdk,jies_bmdj,jiesrq,rucrq,shenh,beiz from ( ");
		sbsql.append("select dc.mingc,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcx','bianm','mk'||js.id,js.bianm) as bianh, \n");
		sbsql.append("		 getHtmlAlert_het('"+MainGlobal.getHomeContext(this)+"','Shenhrz','hetb_id',ht.id,ht.hetbh) as hetbh, \n");
		sbsql.append(" 		 js.shoukdw as gongysmc,sum(js.jiessl) as jiessl, \n");
		sbsql.append(" 		 decode(sum(js.jiessl),0,0,round(sum(jszb.jies*js.jiessl*4.1816/1000)/sum(js.jiessl),2)) as jies_rz, \n");
		sbsql.append(" 		 round(decode(sum(js.jiessl),0,0,round(sum(jszb.jies*js.jiessl*4.1816/1000)/sum(js.jiessl),2))*1000/4.1816,0) as jies_rzdk, \n");
//		sbsql.append(" 		 decode(sum(js.jiessl),0,0,round(sum(js.jiessl*(js.meikje/js.jiessl) *7000/jszb.jies)/sum(js.jiessl),2)) as jies_bmdj, \n");
		sbsql.append(" 		 decode(sum(js.jiessl),0,0,round(sum(decode(jszb.jies,0,0,js.jiessl*(decode(js.jiessl,0,0,(js.buhsdj*js.jiessl+jyf.buhsyf)/js.jiessl)) *7000/jszb.jies))/sum(js.jiessl),2)) as jies_bmdj, \n");
		sbsql.append(" 		 to_char("+jsrq+",'yyyy-mm-dd') as jiesrq,to_char( jyf.yansjzrq,'yyyy-mm-dd') as rucrq,'' as shenh,js.beiz \n");
		sbsql.append("from jiesb js,diancxxb dc,hetb ht,jieszbsjb jszb,jiesyfb jyf \n");
		sbsql.append("where "+jsrq1);
//		sbsql.append("      and js.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') \n");
		sbsql.append(" and js.diancxxb_id=dc.id and js.hetb_id=ht.id(+) "+liuczt+" \n");
		sbsql.append("		and jszb.jiesdid = js.id  and jszb.zhibb_id=2 and js.id=jyf.diancjsmkb_id"+jihkj+"\n");
		sbsql.append(strGongsID);
		sbsql.append("group by(dc.mingc,js.id,js.bianm,ht.id,ht.hetbh,js.shoukdw,"+jsrq+",jyf.yansjzrq,js.beiz)");
		sbsql.append(" union  \n");
		sbsql.append("select dc.mingc,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcx','bianm','yf'||yf.id,yf.bianm) as bianh, \n");
		sbsql.append("		 getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Shenhrz','hetb_id',ht.id,ht.hetbh) as hetbh, \n");
		sbsql.append("       yf.shoukdw as gongysmc,sum(yf.jiessl) as jiessl,0 as jies_rz,0 as jies_rzdk,0 as jies_bmdj, \n");
		sbsql.append("		 to_char("+yfrq+",'yyyy-mm-dd') as jiesrq,to_char( yf.yansjzrq,'yyyy-mm-dd') as rucrq,'' as shenh,yf.beiz \n");
		sbsql.append("  from jiesyfb yf,diancxxb dc,hetb ht--,liucztb lczt  \n");
		sbsql.append("  where yf.diancxxb_id=dc.id and yf.hetb_id=ht.id(+) "+yunfzt+" \n");
		sbsql.append("    and yf.diancjsmkb_id not in (   \n");
		sbsql.append("        select js.id \n");
		sbsql.append("          from jiesb js \n");
		sbsql.append("         where "+jieszt+"   \n");
//		sbsql.append("           js.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')  )   \n");
//		sbsql.append("   and yf.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and yf.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd') "+strGongsID+" \n");
		sbsql.append(jsrq1+" )");
		sbsql.append(jsrq2);
		sbsql.append(strGongsID+" \n");
		sbsql.append("group by(dc.mingc,yf.id,yf.bianm,ht.id,ht.hetbh,yf.shoukdw,"+yfrq+",yf.yansjzrq,yf.beiz) \n");
		sbsql.append(" )order by mingc,bianh ");
		ResultSet rs = con.getResultSet(sbsql.toString());
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
			ArrHeader=new String[2][13];
			ArrHeader[0]=new String[] {"�糧","���","���㵥���","��ͬ���","��Ӧ��","������","������ֵ","������ֵ","��ú����","��������","�볧����","����","��ע"};
			ArrHeader[1]=new String[] {"�糧","���","���㵥���","��ͬ���","��Ӧ��","������","Mj/kg","Kcal/kg","��ú����","��������","�볧����","����","��ע"};
			
			ArrWidth =new int[] {100,45,90,130,200,50,45,45,50,70,70,70,210};

			rt.setBody(new Table(rs, 2, 0, 5));
			
			String strDanw ="";//���ÿ���糧���㵥�����
			int iXuh=1;
			for(int i=3;i<=rt.body.getRows();i++){
				if (strDanw.equals(rt.body.getCell(i, 1).value)){
					iXuh=iXuh+1;
				}else {
					strDanw=rt.body.getCell(i, 1).value;
					iXuh=1;
				}
				rt.body.setCellValue(i, 2, String.valueOf(iXuh));
			}
			//
			rt.body.ShowZero=true;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
		
			rt.setTitle("ȼ�Ͻ��㵥�б�", ArrWidth);
			//rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(4, 6,DateUtil.FormatDate(DateUtil.getDate(getBeginriqDate()))+"��"+DateUtil.FormatDate(DateUtil.getDate(getEndriqDate())),Table.ALIGN_CENTER);
			rt.body.setPageRows(24);
			rt.body.mergeFixedRowCol();
			
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(rt.body.getCols(), Table.ALIGN_LEFT);
		//	rt.body.mergeCol(1);
//			rt.body.mergeCol(2);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
//			rt.setDefautlFooter(7,1,"��λ:������",Table.ALIGN_RIGHT);
	
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
//	________________________________________________________

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	///////
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
	
		
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�ƻ��ھ�:"));
		ComboBox jhkj = new ComboBox();
		jhkj.setTransform("JihkjDropDown");
		jhkj.setWidth(80);
		tb1.addField(jhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���״̬:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����:"));
		ComboBox rq = new ComboBox();
		rq.setTransform("RiqSelect");
		rq.setWidth(80);
		tb1.addField(rq);
		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("��Ӧ��:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		//meik.setWidth(60);
//		//meik.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(cb2);
//		tb1.addText(new ToolbarText("-"));
	
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString4(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			this.setTreeid(null);
			visit.setDefaultTree(null); 
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj())); 
		}
		getToolbars();
		blnIsBegin = true;
	}
   
    //����
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);

    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(2,"ȫ��"));
        list.add(new IDropDownBean(0,"δ���"));
        list.add(new IDropDownBean(1,"�����"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
//	�ƻ��ھ�
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb order by id\n";
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "ȫ��"));
		return;
	}	
	 //��������
    public IDropDownBean getRiqSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getRiqSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }
    public void setRiqSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean6(Value);

    }
    public void setRiqSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getRiqSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getRiqSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
    public void getRiqSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"��������"));
        list.add(new IDropDownBean(1,"�볧����"));
        ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list)) ;
        return ;
    }
	
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
		public String getTreeDiancmc(String diancmcId) {
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
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
		
		//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
