
package  com.zhiren.jt.zdt.chengbgl.ricb;
/* 
* ʱ�䣺2009-08-29
* ���ߣ� ll
* �޸����ݣ�1�����ӳɱ�����
* 			2���޸Ŀ��С������һ��չʾ��ϸʱ����������
*/ 
/* 
* ʱ�䣺2009-09-17
* ���ߣ� ll
* �޸����ݣ��ۺϵ����۹�ʽ�޸�
*/ 
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class RicbReportmx_zdt extends BasePage {
	
	
	private String leix="fc";
	private String diancid="";
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return biaotmc;
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
		return getPrintDataTz();
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9().substring(visit.getString9().indexOf(",")+1);
        } 
		return ""; 
	}
	
	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString10();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		strCondtion="and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and f.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and f.jihkjb_id=" +lngJihkjId;
		}
		
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}else{
				strCondtion=" and dc.id=-1";
			}
		}else{
			if (!strGongys_id.equals("-1")){
				strCondtion=strCondtion+" and y.dqid=" +strGongys_id;
			}
			if (jib==2){
				strCondtion=strCondtion+" and (dc.fgsid=" +strDiancxxb_id +" or dc.rlgsid="+strDiancxxb_id +")"; 
			}else if (jib==3){
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
			}else if (jib==-1){
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
			}
		}
		return strCondtion;
	}
	private String getPrintDataTz(){
		StringBuffer sbsql = new StringBuffer();
			
//		 -------------------------------------------

//		sbsql.append("select danwmc,daohrq,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj    \n");
		sbsql.append("select danwmc,daohrq,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj    \n");
		sbsql.append("   ,ljmeij as hansdj,ljyunf as yunf,ljzaf as zaf    \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj    \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj    \n");
		sbsql.append("from (    \n");
		sbsql.append(" select decode(grouping(y.mingc),1,'�ܼ�',y.mingc) as danwmc,   \n");
		sbsql.append("      to_char(wclj.daohrq,'yyyy-mm-dd') as daohrq,   \n");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,   \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,    \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,    \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,    \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,    \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf     \n");
		sbsql.append("    from (select dc.id as id,dc.diancid as did,dc.dqid as yid,dc.daohrq,   \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,    \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,    \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,    \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,    \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf    \n");
		sbsql.append("            from ( select f.lieid as id,dc.id as diancid,y.id as dqid,f.daohrq, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,    \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,    \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,    \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,    \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,    \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf    \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys    \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id     \n");
		sbsql.append("                      and f.diancxxb_id=dc.id    \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)    \n");
		sbsql.append("                      and f.gongysb_id=y.id        \n");
		sbsql.append(getCondtion());
		sbsql.append("                  group by (y.id,f.lieid,dc.id,f.daohrq)) dc    \n");
		sbsql.append("             group by diancid,dqid,dc.id,daohrq )  wclj,--�ճɱ�    \n");
		sbsql.append("       vwdianc dc, vwgongys y   \n");
		sbsql.append("   where wclj.yid=y.id and wclj.did=dc.id    and y.id="+leix+"  \n");
		sbsql.append("   group by rollup(y.mingc,wclj.daohrq)   \n");
		sbsql.append("   having not (grouping(y.mingc)|| grouping(daohrq) =1) \n");
		sbsql.append("  order by grouping(y.mingc) ,max(y.xuh),y.mingc,daohrq   \n");
		sbsql.append(")    \n");
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="���볧ú�ɱ���ϸ"+strTitle;
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=new String[2][12];
		ArrHeader[0]=new String[] {"��λ","��������","ú��","�ɱ���","��ֵ","��ֵ","�ۺϼ�","ú��","�˷�","�ӷ�","��ú����(Ԫ/��)","��ú����(Ԫ/��)"};
		ArrHeader[1]=new String[] {"��λ","��������","(��)","(��)","(MJ/kg)","(Kcal/kg)","(Ԫ/��)","&nbsp;(Ԫ/��)","(Ԫ/��)","&nbsp;(Ԫ/��)","��˰","����˰"};
		int ArrWidth[]=new int[] {150,80,80,80,60,60,60,60,60,60,60,60};
//		---------------------------------------------
		
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4,  4,  DateUtil.Formatdate("yyyy��MM��dd��", datStart)+"��"+DateUtil.Formatdate("yyyy��MM��dd��", datEnd), Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
		rt.body.setUseDefaultCss(true);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		/*if(rt.body.getRows()>2){
			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 3);
			rt.body.setCellAlign(rt.body.getRows(), 1, Table.ALIGN_CENTER);
		}*/
		
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("vwgongys_id") !=null) {
			visit.setString9("");
			visit.setString10("");
			visit.setString9((cycle.getRequestContext().getParameters("vwgongys_id")[0]));
			visit.setString10(visit.getString9().substring(0,visit.getString9().indexOf(",")));
//			visit.setString10(diancid);
			leix = visit.getString9().substring(visit.getString9().indexOf(",")+1);
			
        }else{
        	if(!visit.getString9().equals("")) {
        		leix = visit.getString9().substring(visit.getString9().indexOf(",")+1);
        		visit.setString10(visit.getString9().substring(0,visit.getString9().indexOf(",")));
//    			visit.setString10(diancid);
            }
        }
	}

	// ��Ӧ��
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc\n" + "from gongysb\n";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	// ����
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
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
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ֳ�"));
		list.add(new IDropDownBean(2, "�ֿ�"));
		list.add(new IDropDownBean(3, "���̱�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// �糧����
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = 'ʹ�ü���' and zhuangt = 1 and zhi = '�й�����'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		return laiml;
	}
	
	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
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