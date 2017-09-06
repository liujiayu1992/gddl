package com.zhiren.dc.zonghcx;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zonghcxbb extends BasePage {
	/*
	 * ����:tzf
	 * ʱ��:2010-01-26
	 * �޸�����:����������˾Ҫ��ӯ���������໥���㣬���򱨱����治ƽ���ò������ơ�
	 */
	/*
	 * ����:tzf
	 * ʱ��:2009-08-24
	 * ����  ������ϸ ������
	 */
	/*
	 * ����:tzf
	 * ʱ��:2009-4-16
	 * �޸�����:�����ۺϲ�ѯ  ��ϸ �� ���� �������ֶ�  ����  �������ֶ� ֵ  ����Ӧ  ���Ѿ�����
	 */
	/*
	 * ����:���	
	 * ʱ��:2009-5-11
	 * �޸�����:��4�����е�and fahb.diancxxb_id =\n" + v.getDiancxxb_id()+���Ϊ.append(Jilcz.filterDcid(v,"fahb")).append(" \n")
	 * �޸��������ܲ���ʾֵ������
	 */
	/*
	 * ����:���	
	 * ʱ��:2009-06-10
	 * �޸�����:�ۺϲ�ѯ������ϸ��
	 * ���ϵͳ��Ϣ���е�״̬,�趨��Ӧ��ѡ�����
	 * ����ϵͳ��Ϣ���е����ã��趨��Ӧ�ı�ͷ
	 * ��getPrintTable()��Ҳ����Ӧ���޸�
	 * �����п�
	 */
	
	/*
	 * ����:���	
	 * ʱ��:2009-06-15
	 * �޸�����:�ۺϲ�ѯ������
	 * ����ǰ̨����Ĳ���
	 * �ڱ�ͷ�����û���ѡ��ͬ���ɲ�ͬ������
	 * ��ָ���������ڣ��������ڣ���������
	 */
	/*
	 * ����:���	
	 * ʱ��:2009-06-15
	 * �޸�����:�ۺϲ�ѯ������ϸ��
	 * ���ϵͳ��Ϣ���е�״̬,�趨��Ӧ��ѡ�����
	 * ����ϵͳ��Ϣ���е����ã��趨��Ӧ�ı�ͷ
	 * ��getPrintTable()��Ҳ����Ӧ���޸�
	 * �����п�
	 */
	/*
	 * ����: ���	
	 * ʱ��: 2012-06-28
	 * ����: ����getZonghcx_ZLHZ�д���ֵ��ȡֵ��ʽ
	 */
	/*
	 * ����: ���	
	 * ʱ��: 2013-06-05
	 * ����: ����getZonghcx_ZL_MX�д���ֵ��ȡֵ��ʽ
	 */
	
	
//	�����û���ʾ
	private static final String RptType_SL_HZ = "SL_HZ";
	private static final String RptType_SL_MX = "SL_MX";
	private static final String RptType_ZL_HZ = "ZL_HZ";
	private static final String RptType_ZL_MX = "ZL_MX";
	private String RptType ="HZ";
	private String RptSqltmp = "";
	private String msg="";
	private String tmpSql=""; //����һ����ʱ����ȥ�õ�sql2��ֵ
	private boolean ShowStar=true;
	private String Fahrq="";//���巢�����ں͵������ڵı���
	private String Daohrq="";//���巢�����ں͵������ڵı���
	private String Huaysj="";
	
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
	/*
	 * �����ۺ���������ú���ֶΣ����ӵ糧����
	 * �޸�ʱ�䣺2008-12-04
	 * �޸��ˣ�����
	 * 
	 */
	public String getZonghcx_SLHZ(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		String xhjs_str=" select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
		ResultSetList rsl_ys=con.getResultSetList(xhjs_str);
		String yuns_js=" sum(round_new(chepb.yuns,"+v.getShuldec()+")) yuns,\n";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		if(rsl_ys.next()){
			
			yuns_js=" sum(round_new(chepb.yingk,"+v.getShuldec()+")) + sum(round_new(chepb.biaoz,"+v.getShuldec()+")) " +
			"- sum(round_new(chepb.maoz-chepb.piz,"+v.getShuldec()+")) yuns,\n ";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
	
		}
		rsl_ys.close();
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gongysb_id,\n")
		.append("decode(grouping(g.mingc)+grouping(m.mingc),1,'��Ӧ��С��',m.mingc) meikxxb_id,\n")
		.append("decode(grouping(m.mingc)+grouping(p.mingc),1,'ú��С��',p.mingc)pinzb_id,\n")
		.append("decode(grouping(m.mingc)+grouping(p.mingc)+grouping(y.mingc),1,'���䵥λС��',y.mingc) yunsdwb_id,\n")
		.append("sum(round_new(chepb.maoz-chepb.piz-chepb.koud,"+v.getShuldec()+")) laimsl, \n")
		.append("sum(round_new(chepb.biaoz,"+v.getShuldec()+")) biaoz, \n")
		.append("sum(round_new(chepb.maoz,"+v.getShuldec()+")) maoz, \n")
		.append("sum(round_new(chepb.piz,"+v.getShuldec()+")) piz, \n")
		.append("sum(round_new(chepb.maoz-chepb.piz,"+v.getShuldec()+")) jingz, \n")
		.append("sum(round_new(chepb.maoz-chepb.piz-chepb.koud,"+v.getShuldec()+")) jiessl, \n")
		.append("sum(round_new(chepb.yingk,"+v.getShuldec()+")) yingk, \n")
		.append("sum(round_new(chepb.yuns,"+v.getShuldec()+")) yuns, \n")
		.append(yuns_js)
		.append("count(chepb.id) ches, \n")
		.append("sum(round_new(chepb.zongkd,"+v.getShuldec()+")) zongkd \n")
		.append("from fahb , chepb , yunsdwb y, gongysb g, meikxxb m, pinzb p\n")
		.append("where gongysb_id = g.id and meikxxb_id = m.id and fahb.id=chepb.fahb_id and chepb.yunsdwb_id=y.id\n")
		.append("and pinzb_id = p.id ")
		.append(Jilcz.filterDcid(v,"fahb")).append(" \n")
		.append(RptSqltmp)
		.append("\n")
		.append("group by rollup(g.mingc,m.mingc,p.mingc,y.mingc)\n")
		.append(tmpSql)
		.append("\n")
		.append("order by decode(grouping(g.mingc),1,-999,1),max(g.xuh),\n")
		.append("decode(grouping(m.mingc),1,-999,1),max(m.xuh),\n")
		.append("decode(grouping(p.mingc),1,-999,1),max(p.xuh)\n");
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='SL_HZ' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='SL_HZ'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	ArrHeader =new String[1][14];
        	ArrHeader[0]=new String[] {"��Ӧ��","ú��","���䵥λ","Ʒ��","��ú����","Ʊ��","ë��","Ƥ��","����","������","ӯ��","����","����","�۶�"};
        	
            ArrWidth =new int[] {150, 150, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65};
            rt.setTitle("�����ۺϲ�ѯ����", ArrWidth);
        }

		//����ҳ����
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1,2, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
//		����ֵ���ڵ�����Ʊ�
		if(Fahrq.length()>1 && Huaysj.length()>1){
			rt.setDefaultTitle(3, 4, "��������:"+ Fahrq+"<br>��������:"+ Huaysj,Table.ALIGN_LEFT);
		}else if(Fahrq.length()>1 && Huaysj.length()<1){
			rt.setDefaultTitle(3, 4, "��������: "+ Fahrq,Table.ALIGN_LEFT);
		}else if(Fahrq.length()<1 && Huaysj.length()>1){
			rt.setDefaultTitle(3, 4, "��������: "+ Huaysj,Table.ALIGN_LEFT);
		}else if(Daohrq.length()>1){
			rt.setDefaultTitle(3, 4, "��������: "+ Daohrq,Table.ALIGN_LEFT);
		}

		//����
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
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
		/*rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);*/
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//����ҳ��
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"�����ۺϲ�ѯ����","SL_HZ");
		return rt.getAllPagesHtml();// ph;
	}
	
	/*
	 * ������ϸ�����ӵ糧����
	 * �޸�ʱ�䣺2008-12-04
	 * �޸��ˣ�����
	 */
	
	/*
	 * ������ϸ�в�ѯ����޸�
	 * �޸�ʱ�䣺2008-04-20
	 */
	
	/*
	 * ������ϸ�в�ѯ����޸�
	 * �ڲ鿴��ϸ���ݵ����������ʱ�������в����ԣ�
	 * ����֮ǰ�İ汾���ֶ�ǰ��ӡ����ʶ��
	 * �޸�ʱ�䣺2009-06-12
	 * �޸��ˣ����
	 */
	public String getZonghcx_SLMX(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		boolean hasCaiybm=false;
//		sb.append("select g.mingc gmc,m.mingc mcm,cz.mingc cmc,p.mingc pmc,")
//			.append(" to_char(fahb.daohrq,'yyyy-mm-dd') daohrq,chepb.cheph,chepb.maoz,chepb.piz,chepb.maoz-chepb.piz-chepb.zongkd,chepb.yingk,chepb.yuns,chepb.zongkd ")
//			.append(" from gongysb g,meikxxb m,chezxxb cz,pinzb p,fahb ,chepb ")
//			.append(" where g.id = fahb.gongysb_id ")
//			.append(" and m.id = fahb.meikxxb_id ")
//			.append(" and fahb.diancxxb_id = ").append(v.getDiancxxb_id())
//			.append(" and cz.id = fahb.faz_id ")
//			.append(" and p.id = fahb.pinzb_id ")
//			.append(RptSqltmp)
//			.append("\n")
//			.append(" and chepb.fahb_id = fahb.id ");
//			.append(" order by cp.id");

//		���ϵͳ��Ϣ���е�״̬
		String BianmSel="";
		String BianmGroup="";
		String XitxxbSql="select * from xitxxb where mingc='����������ʾ' and zhi='��'";
		
		//������ʾ���ǵ��жϣ����ַ�����ֵ
		if(ShowStar){
			BianmSel="  decode(grouping(caiyb.bianm)+grouping(chepb.id),1,'��'||caiyb.bianm,caiyb.bianm) bianm, \n";
		}else{
			BianmSel="  caiyb.bianm,\n" ;
		}
		
//		boolean zhongQingcBoo=false;
		String shijStr="";
//		if(con.getHasIt(" select * from xitxxb where mingc='�����ۺϲ�ѯ��ϸ���س�ʱ����ʾ' and leib='����' and zhuangt=1 and zhi='��' and diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id())){
//			zhongQingcBoo=true;
			shijStr=" ,max(to_char(chepb.zhongcsj,'yyyy-MM-dd HH24:mi:ss')) zhongcsj,max(to_char(chepb.qingcsj,'yyyy-MM-dd HH24:mi:ss')) qingcsj ";
//		}
		
		if(con.getHasIt(XitxxbSql)){
			hasCaiybm=true;
			BianmSel+= "GetCaibmFromZhilId(caiyb.zhilb_id) caiybm,GetHuaybmFromZhilId(caiyb.zhilb_id) huaybm, \n";
			BianmGroup=",GetCaibmFromZhilId(caiyb.zhilb_id),GetHuaybmFromZhilId(caiyb.zhilb_id)";
		}
		sb.append(
				"select g.mingc gmc,\n" + 
				"       m.mingc mcm,\n" + 
				"       cz.mingc cmc,\n" +

				"       p.mingc pmc,\n" +
						"y.mingc ymc,\n"+
				"       to_char(fahb.daohrq, 'yyyy-mm-dd') daohrq,\n" +
				BianmSel+
				"       decode(grouping(chepb.id),1,''||count(chepb.id),max(chepb.cheph)) cheph,\n" + 
				"       sum(chepb.maoz) maoz,\n" + 
				"       sum(chepb.piz) piz,\n" + 
				"       sum(chepb.maoz - chepb.piz - chepb.zongkd) jingz,\n" + 
				"       sum(chepb.biaoz) biaoz,\n" + 
				"       sum(chepb.yingk) yingk,\n" + 
				"       sum(chepb.yuns) yuns,\n" + 
				"       sum(chepb.zongkd) zongkd\n" +
				shijStr+" \n"+
				"  from gongysb g, meikxxb m, chezxxb cz, pinzb p, fahb, chepb,caiyb,yunsdwb y\n" +
				" where g.id = fahb.gongysb_id\n" + 
				"   and m.id = fahb.meikxxb_id\n" + 
				Jilcz.filterDcid(v,"fahb")+"\n"+
				"   and cz.id = fahb.faz_id\n" + 
				"   and p.id = fahb.pinzb_id and chepb.yunsdwb_id=y.id\n" +
				"\n" + 
				RptSqltmp
				+"   and chepb.fahb_id = fahb.id\n" + 
				"   and fahb.zhilb_id=caiyb.zhilb_id\n" + 
				"\n" + 
				" group by rollup(g.mingc,m.mingc,cz.mingc,y.mingc,p.mingc,to_char(fahb.daohrq, 'yyyy-mm-dd'),"+
				" caiyb.bianm,chepb.id" +BianmGroup+") \n")
				.append(tmpSql+"\n"+
						"order by daohrq, decode(grouping(g.mingc),1,-999,1),max(g.xuh)," +
						"decode(grouping(m.mingc),1,-999,1),max(m.xuh)," +
						"decode(grouping(p.mingc),1,-999,1),max(p.xuh)," +
						"decode(grouping(to_char(fahb.daohrq, 'yyyy-mm-dd')),1,-999,1)," +
						"decode(grouping(caiyb.bianm),1,-999,1),max(caiyb.xuh)," +
						"decode(grouping(chepb.id),1,-999,1)" );
				
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='SL_MX' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());

        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='SL_MX'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
//        	����ϵͳ��Ϣ���е����ã��趨��Ӧ�ı�ͷ
//        	�����п�
        	if(hasCaiybm){
        		ArrHeader=new String[1][18];
            	ArrHeader[0]=new String[] {"��Ӧ��","ú��","��վ","Ʒ��","���䵥λ","��������","����","��������", "�������","����","ë��","Ƥ��","����","������","Ʊ��","ӯ��","����","�۶�","�س�ʱ��","�ᳵʱ��"};
                ArrWidth=new int[] {120, 120, 65, 65, 90, 75, 140, 140, 55, 55, 55, 55, 55, 55, 55, 55, 120, 120,120,120};
        	}else{

	        	ArrHeader=new String[1][16];
	//        	ArrHeader[0]=new String[] {"��Ӧ��","ú��","��վ","Ʒ��","��������","����", "����","ë��","Ƥ��","����","ӯ��","����","�۶�"};
	        	
	        	ArrHeader[0]=new String[] {"��Ӧ��","ú��","��վ","Ʒ��","���䵥λ","��������","����", "����","ë��","Ƥ��","����","������","Ʊ��","ӯ��","����","�۶�","�س�ʱ��","�ᳵʱ��"};
	        	
	            ArrWidth=new int[] {120, 120, 65, 65, 90, 75, 55, 55, 55, 55, 55, 55, 55, 55, 120, 120,120,120};
        	}
            rt.setTitle("�����ۺϲ�ѯ��ϸ", ArrWidth);
        	
    	}

		//����ҳ����
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		
		//����ֵ���ڵ�����Ʊ�
		if(Fahrq.length()>1 && Huaysj.length()>1){
			rt.setDefaultTitle(5, 6, "��������:"+ Fahrq+"<br>��������:"+ Huaysj,Table.ALIGN_CENTER);
		}else if(Fahrq.length()>1 && Huaysj.length()<1){
			rt.setDefaultTitle(5, 6, "��������: "+ Fahrq,Table.ALIGN_CENTER);
		}else if(Fahrq.length()<1 && Huaysj.length()>1){
			rt.setDefaultTitle(5, 6, "��������: "+ Huaysj,Table.ALIGN_CENTER);
		}else if(Daohrq.length()>1){
			rt.setDefaultTitle(5, 6, "��������: "+ Daohrq,Table.ALIGN_CENTER);
		}

		//����
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		if(hasCaiybm){
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);
			rt.body.setColAlign(14, Table.ALIGN_RIGHT);
			rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		}else{
			rt.body.setColAlign(7, Table.ALIGN_RIGHT);
			rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		
		
//		rt.body.setColAlign(ArrHeader.length-1, Table.ALIGN_CENTER);
//		rt.body.setColAlign(ArrHeader.length, Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//����ҳ��
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 4, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 4, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"�����ۺϲ�ѯ��ϸ","SL_MX");
		return rt.getAllPagesHtml();// ph;
	}
	/*
	 * �����ۺ�	1��������������������Ȩֵ�� ���� ��Ϊ ��ú���� 
	 * 			2��������ϵͳ�趨����������Լ
	 * 			3������糧����
	 * �޸�ʱ�䣺2008-12-04
	 * �޸��ˣ�����
	 * 
	 * �޸�ʱ�䣺2009-04-20
	 * �޸�SQL��䣬����С��
	 * �޸�SQL��䣬��������
	 */
	public String getZonghcx_ZLHZ(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(gongysb.mingc),1,'�ܼ�',gongysb.mingc) meikdq," +
				"decode(grouping(gongysb.mingc)+grouping(meikxxb.mingc),1,'��Ӧ��С��',meikxxb.mingc) miekdw," +
				"decode(grouping(meikxxb.mingc)+grouping(chezxxb.mingc),1,'ú��С��',chezxxb.mingc) faz,")
               .append("pinzb.mingc as pinz,sum(fahb.ches) as ches,sum(round_new(fahb.laimsl,"+v.getShuldec()+")) as jingz,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getFarldec()+")/4.1816 * 1000,0)) as farl1,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getFarldec()+") * 1000,0)) as farl2,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.mt,"+v.getMtdec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getMtdec()+")) as mt,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.mad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as mad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aar * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aar,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qbad,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as qbad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.ad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),2)) as ad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vdaf,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.sdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as sdaf,")
               .append("decode(sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 0, 0, round_new(sum(zhilb.stad * round_new(fahb.laimsl, " + v.getShuldec() + ")) / sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 2)) as stad,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.std * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as std,")
               .append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.hdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as hdaf ")
               .append("from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb ")
               .append("where fahb.zhilb_id = zhilb.id ")
               .append("and fahb.gongysb_id = gongysb.id ")
               .append("and fahb.meikxxb_id = meikxxb.id ")
               .append("and fahb.faz_id = chezxxb.id ")
               .append("and fahb.pinzb_id = pinzb.id ")
               .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
               .append(RptSqltmp).append("\n")
               .append("group by rollup(gongysb.mingc,meikxxb.mingc,chezxxb.mingc,pinzb.mingc)")
               .append("\n")
               .append(tmpSql)
               .append("\n")
               .append("order by decode(grouping(gongysb.mingc),1,-999,1),max(gongysb.xuh)," +
               		"decode(grouping(meikxxb.mingc),1,-999,1),max(meikxxb.xuh)," +
               		"decode(grouping(chezxxb.mingc),1,-999,1),max(chezxxb.xuh),"+
               		"decode(grouping(pinzb.mingc),1,-999,1),max(pinzb.xuh)");

		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='ZL_HZ' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='ZL_HZ'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        } else {
        	rs = rstmp;
            ArrHeader = new String[1][20];
        	ArrHeader[0] = new String[] {"ú�����", "ú��λ", "��վ", "Ʒ��", "����", "��������(��)", "�յ�����λ��ֵ(Kcal<br>/kg)", 
        				"�յ�����λ��ֵ(j/g)", "ȫˮ��(%)Mt", "���������ˮ��(%)Mad", "�յ����ҷ�(%)Aar", "����������ҷ�(%)Aad", 
        				"��Ͳ<br>��ֵ<br>(j/g)<br>Qb,ad", "������ҷ�(%)Ad", "�����޻һ��ӷ���(%)Vdaf", "����������ӷ���(%)Vad", 
        				"�����޻һ���(%)Sdaf", "�����������(%)St,ad", "�����ȫ��(%)S,td", "�����޻һ���(%)Hdaf"};
        	
            ArrWidth = new int[] {120, 120, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
            rt.setTitle("�����ۺϲ�ѯ����", ArrWidth);
        }


		//����ҳ����
		
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		
		//����ֵ���ڵ�����Ʊ�
		int i=0;
		String tj="";
		if(Fahrq.length()>1){
			tj += "��������:"+ Fahrq;
			i++;
		}
		if(Daohrq.length()>1){
			if(i>0){
				tj += "<br>��������:"+ Daohrq;
			}else{
				tj += "��������:"+ Daohrq;
			}
			i++;
		}
		if(Huaysj.length()>1){
			if(i>0){
				tj += "<br>��������:"+ Huaysj;
			}else{
				tj += "��������:"+ Huaysj;
			}
			i++;
		}
		rt.setDefaultTitle(5, 5, tj,Table.ALIGN_LEFT);
		
		
//		if(Fahrq.length()>1 && Huaysj.length()>1){
//			rt.setDefaultTitle(5, 5, "��������:"+ Fahrq+"<br>��������:"+ Huaysj,Table.ALIGN_LEFT);
//		}else if(Fahrq.length()>1 && Huaysj.length()<1){
//			rt.setDefaultTitle(5, 5, "��������: "+ Fahrq,Table.ALIGN_LEFT);
//		}else if(Fahrq.length()<1 && Huaysj.length()>1){
//			rt.setDefaultTitle(5, 5, "��������: "+ Huaysj,Table.ALIGN_LEFT);
//		}

		//����
		rt.setBody(new Table(rs, 1, 0, 4));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
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
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		
		//����ҳ��
		rt.setDefautlFooter(1, 6, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 6, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 7, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"�����ۺϲ�ѯ����","ZL_HZ");
		return rt.getAllPagesHtml();// ph;
	}
	/*
	 * �����ۺ�	1�������������� ���� ��Ϊ ��ú���� 
	 * 			2��������ϵͳ�趨����������Լ
	 * 			3������糧����
	 * �޸�ʱ�䣺2008-12-04
	 * �޸��ˣ�����
	 */
	public String getZonghcx_ZL_MX(){
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con= new JDBCcon();
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT huaybh,meikdq,miekdw,faz,pinz,ches,laimsl,\n");
		sb.append("round_new((farl2/1000)/4.1816*1000,0) farl1,\n");
		sb.append("farl2,mt,mad,aar,aad,qbad,ad,vdaf,vad,sdaf,stad,std,hdaf,huaysj,huayy\n");
		sb.append("FROM(\n");
		sb.append("select \n")
		.append("       zhilb.huaybh,\n")
		.append("       gongysb.mingc as meikdq,\n")
		.append("       meikxxb.mingc as miekdw,\n")
		.append("       chezxxb.mingc as faz,\n" )
		.append("       pinzb.mingc as pinz,\n" )
	    .append("       fahb.ches,\n" )
		.append("       round_new(fahb.laimsl,"+v.getShuldec()+") as laimsl,\n" )
		.append("       round_new(round_new(zhilb.qnet_ar,"+v.getFarldec()+") / 4.1816 * 1000, 0) as farl1,\n") 
		.append("       round_new(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * 1000, 0) as farl2,\n" )
		.append("       round_new(zhilb.mt, "+v.getMtdec()+") as mt,\n" )
		.append("       round_new(zhilb.mad, 2) as mad,\n") 
		.append("       round_new(zhilb.aar, 2) as aar,\n" ) 
	    .append("       round_new(zhilb.aad, 2) as aad,\n" ) 
	    .append("       round_new(round_new(zhilb.qbad,"+v.getFarldec()+") * 1000, 0) as qbad,\n" ) 
	    .append("       round_new(zhilb.ad, 2) as ad,\n" )
	    .append("       round_new(zhilb.vdaf, 2) as vdaf,\n" ) 
	    .append("       round_new(zhilb.vad, 2) as vad,\n" ) 
	    .append("       round_new(zhilb.sdaf, 2) as sdaf,\n" ) 
	    .append("       round_new(zhilb.stad, 2) as stad,\n" ) 
	    .append("       round_new(zhilb.std, 2) as std,\n" ) 
	    .append("       round_new(zhilb.hdaf, 2) as hdaf,\n" ) 
	    .append("       to_char(zhilb.huaysj,'yyyy-mm-dd') as huaysj,\n" )
	    .append("       zhilb.huayy\n" )
	    .append("  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb\n" ) 
	    .append(" where fahb.zhilb_id = zhilb.id\n" )
	    .append("   and fahb.gongysb_id = gongysb.id\n" ) 
	    .append("   and fahb.meikxxb_id = meikxxb.id\n" ) 
	    .append("   and fahb.faz_id = chezxxb.id\n" )
	    .append("   and fahb.pinzb_id = pinzb.id\n" ) 
	    .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
	    .append(RptSqltmp).append("\n");
		
		if(tmpSql!=null && tmpSql.indexOf("1")!=-1){//���Ӻϼ���
			sb.append(" union \n");
			sb.append("select \n")
			.append("       '�ϼ�' huaybh,\n")
			.append("       '' as meikdq,\n")
			.append("       '' as miekdw,\n")
			.append("       '' as faz,\n" )
			.append("       '' as pinz,\n" )
		    .append("      sum(fahb.ches) as ches,\n" )
			.append("       sum(round_new(fahb.laimsl,"+v.getShuldec()+")) as laimsl,\n" )
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new((sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+"))) /4.1816 * 1000,0)) as farl1,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qnet_ar,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as farl2,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.mt,"+v.getMtdec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),"+v.getMtdec()+")) as mt,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.mad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as mad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aar * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aar,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.aad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as aad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(round_new(zhilb.qbad,"+v.getFarldec()+") * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")) * 1000,0)) as qbad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.ad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")),2)) as ad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.vad * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as vad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.sdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n")
			.append("decode(sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 0, 0, round_new(sum(zhilb.stad * round_new(fahb.laimsl, " + v.getShuldec() + ")) / sum(round_new(fahb.laimsl, " + v.getShuldec() + ")), 2)) as stad,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.std * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as std,\n")
			.append("decode(sum(round_new(fahb.laimsl,"+v.getShuldec()+")),0,0,round_new(sum(zhilb.hdaf * round_new(fahb.laimsl,"+v.getShuldec()+")) / sum(round_new(fahb.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n ")
		    .append("       '' as huaysj,\n" )
		    .append("       '' as huayy\n" )
		    .append("  from fahb, zhilb, gongysb, meikxxb, chezxxb, pinzb\n" ) 
		    .append(" where fahb.zhilb_id = zhilb.id\n" )
		    .append("   and fahb.gongysb_id = gongysb.id\n" ) 
		    .append("   and fahb.meikxxb_id = meikxxb.id\n" ) 
		    .append("   and fahb.faz_id = chezxxb.id\n" )
		    .append("   and fahb.pinzb_id = pinzb.id\n" ) 
		    .append(Jilcz.filterDcid(v,"fahb")).append(" \n")
		    .append(RptSqltmp).append("\n");
		}
		sb.append(" order by huaysj)");
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer SB=new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='ZL_MX' order by xuh");
        ResultSetList rsl=con.getResultSetList(SB.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='ZL_MX'");
        	String Htitle="";
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        } else {
        	rs = rstmp;
        	ArrHeader = new String[1][23];
        	ArrHeader[0] = new String[] {"������", "ú�����", "ú��λ", "��վ", "Ʒ��", "����", "��������(��)", "�յ�����λ��ֵ(Kcal<br>/kg)", 
        				"�յ�����λ��ֵ(j/g)", "ȫ<br>ˮ<br>��<br>(%)<br>Mt", "���������ˮ��(%)Mad", "�յ����ҷ�(%)Aar", "����������ҷ�(%)Aad", 
        				"��Ͳ<br>��ֵ<br>(j/g)<br>Qb,ad", "������ҷ�(%)Ad", "�����޻һ��ӷ���(%)Vdaf", "����������ӷ���(%)Vad", 
        				"�����޻һ���(%)Sdaf", "�����������(%)St,ad", "�����ȫ��(%)S,td", "�����޻һ���(%)Hdaf", "����<br>����", "������Ա"};
        	
        	ArrWidth=new int[] {54, 100, 100, 100, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 65, 54};
        	rt.setTitle("�����ۺϲ�ѯ��ϸ", ArrWidth);
        }
//		����ҳ����
			
			rt.title.fontSize=10;
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
					Table.ALIGN_LEFT);
			//����ֵ���ڵ�����Ʊ�
			if(Fahrq.length()>1 && Huaysj.length()>1){
				rt.setDefaultTitle(9, 5, "��������:"+ Fahrq+"<br>��������:"+ Huaysj,Table.ALIGN_LEFT);
			}else if(Fahrq.length()>1 && Huaysj.length()<1){
				rt.setDefaultTitle(9, 5, "��������: "+ Fahrq,Table.ALIGN_LEFT);
			}else if(Fahrq.length()<1 && Huaysj.length()>1){
				rt.setDefaultTitle(9, 5, "��������: "+ Huaysj,Table.ALIGN_LEFT);
			}else if(Daohrq.length()>1){
				rt.setDefaultTitle(9, 5, "��������: "+ Daohrq,Table.ALIGN_LEFT);
			}
//			����
			rt.setBody(new Table(rs, 1, 0, 4));
			//rt.body.setColAlign(0, Table.ALIGN_CENTER);
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
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
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
//			rt.body.setColFormat(arrFormat);
			rt.body.setPageRows(40);
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();

//			rt.createDefautlFooter(ArrWidth);
			rt.createFooter(1,ArrWidth);
			
			
//			����ҳ��
			rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
					Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 4, "��ˣ�", Table.ALIGN_LEFT);
			rt.setDefautlFooter(10, 4, "�Ʊ�", Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize=10;
//			rt.footer.setRowHeight(1, 1);
			con.Close();
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"�����ۺϲ�ѯ��ϸ","ZL_MX");
		return  rt.getAllPagesHtml();// ph;
	}
	
	public String getPrintTable(){
		if(RptType.equals(RptType_SL_HZ)){
			//��������������sql2�д���������
			String sqltmp2=tmpSql;
			if(sqltmp2!=null){
				tmpSql= "having grouping(p.mingc)=0";
				String[] srctmp = {"1","2","3"};
				String[] streval = {"grouping(g.mingc)=1","grouping(m.mingc)+grouping(g.mingc)=1",
						"grouping(m.mingc)=0"};
				String[] stmp = sqltmp2.split(",");
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
			//���
			return getZonghcx_SLHZ();
		}
		if(RptType.equals(RptType_SL_MX)){
//			������������ϸsql2�д���������
			String sqltmp2=tmpSql;
//			��ϵͳ��Ϣ�����ж��Ƿ���ʾ��������
			String BianmGrouping="";
			String XitxxbSql="select * from xitxxb where mingc='����������ʾ' and zhi='��'";
			JDBCcon con=new JDBCcon();
			if(con.getHasIt(XitxxbSql)){
				BianmGrouping="GetHuaybmFromZhilId(caiyb.zhilb_id)";
			}else{
				BianmGrouping="chepb.id";
			}
			con.Close();
			if(sqltmp2!=null){
				tmpSql= "having grouping("+BianmGrouping+")=0";
				String[] srctmp = {"1","2","3","4","5","6"};
				String[] streval = {"grouping(g.mingc)+ grouping(m.mingc)=1",
						"grouping(m.mingc) + grouping(cz.mingc)=1",
						"grouping(p.mingc)+grouping(cz.mingc)=1",
						"grouping(p.mingc)+grouping(to_char(fahb.daohrq, 'yyyy-mm-dd'))=1",
						"grouping(to_char(fahb.daohrq, 'yyyy-mm-dd'))+ grouping(caiyb.bianm) = 1",
						"grouping(caiyb.bianm)=0"};
				String[] stmp = sqltmp2.split(",");
				//�ж�һ��ɸѡ���ٸ�ֵ,����һ��ֵ��ô����ʾ����
				if(stmp.length>1){ShowStar=false;}
				
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
//			���
			return getZonghcx_SLMX();
		}
		if(RptType.equals(RptType_ZL_HZ)){
//			��������������sql2�д���������
			String sqltmp2=tmpSql;
			if(sqltmp2!=null){
				tmpSql= "having grouping(pinzb.mingc)=0";
				String[] srctmp = {"1","2","3"};
				String[] streval = {"grouping(gongysb.mingc)=1",
						"grouping(gongysb.mingc)+grouping(meikxxb.mingc)=1",
						"grouping(meikxxb.mingc)+grouping(chezxxb.mingc)=1"};
				String[] stmp = sqltmp2.split(",");
				for(int i = 0; i < srctmp.length; i++){
					int m = 0; 
					for(; m < stmp.length; m++){
						if(stmp[m].equals(srctmp[i])){
							tmpSql+= " or " + streval[i];
							break;
						}
					}

				}
				
			}
			//���
			return getZonghcx_ZLHZ();
		}
		if(RptType.equals(RptType_ZL_MX)){
			return getZonghcx_ZL_MX();
		}
		return null;
	}
	
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		String sqltmp="";
		String reportType="";
		Visit visit = (Visit) getPage().getVisit();
//		sqltmp = cycle.getRequestContext().getParameter("sql").getBytes();
//		tmpSql = cycle.getRequestContext().getParameter("sql2");
//		String reportType = cycle.getRequestContext().getParameter("lx");
//		Fahrq = cycle.getRequestContext().getParameter("fahrq").replaceFirst(" ", "��");
//		Daohrq = cycle.getRequestContext().getParameter("daohrq").replaceFirst(" ","��");
		/*
		 * �޸�ʱ�䣺2009-06-28
		 * �޸��ˣ�  ��ΰ
		 * �޸����ݣ�
		 * 		�����������⣬�磺�����д��к��֣���B1234��"��"
		 */
		try {
			sqltmp = new String(cycle.getRequestContext().getParameter("sql").getBytes("iso8859-1"), "gb2312");
			tmpSql = new String(cycle.getRequestContext().getParameter("sql2").getBytes("iso8859-1"), "gb2312");
			reportType = cycle.getRequestContext().getParameter("lx");
			if(visit.getActivePageName().toString().equals("Zonghcx_zl")){
				Huaysj = cycle.getRequestContext().getParameter("huaysj").replaceFirst(" ", "��");
			}
			Fahrq = cycle.getRequestContext().getParameter("fahrq").replaceFirst(" ", "��");
			Daohrq = cycle.getRequestContext().getParameter("daohrq").replaceFirst(" ", "��");
			//Huaysj = cycle.getRequestContext().getParameter("huaysj").replaceFirst(" ","��");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(sqltmp != null) {
			RptSqltmp = sqltmp;
		}
		if(reportType != null) {
			RptType = reportType.trim();
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