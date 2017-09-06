package com.zhiren.jt.het.hetwb;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import bsh.Interpreter;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.jt.het.hetmb.Fahxxbean;
import com.zhiren.jt.het.hetmb.Hetxxbean;
import com.zhiren.jt.het.hetmb.Zengkkbean;
import com.zhiren.jt.het.hetmb.Zhilyqbean;
import com.zhiren.jt.het.hetmb.jijbean;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 16��10
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

/*
 * ���ߣ����
 * ʱ�䣺2012-11-07
 * ���÷�Χ����������󿪵糧
 * �������޸ĺ�ͬ��ӡ��ʽ��
 * 		ʹ�ò��������Ƿ��ҳ��ʾ����˫����Ϣ��Ĭ�Ϸ�ҳ��ʾ
 * 		MainGlobal.getXitxx_item("��ͬ", "��ͬ��ӡ�Ƿ��ҳ��ʾ����˫����Ϣ", "0", "��")
 * 		��ͬ��ӡ��������������Ҫ��
 */
/*
	 * ���ߣ���ʤ��
	 * ʱ�䣺2013-03-29
	 * ���÷�Χ���󿪵糧
	 * ������������ͬ��ӡ��ʾ��Ϣ
	 */
public class Hetwb extends BasePage {
	
	Hetxxbean bean=new Hetxxbean();

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
	
// private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����еĳ����Ƿ���ʾ'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

//	private String REPORT_NAME_HETDY = "Hetdy";// 
//
//	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
//		blnIsBegin = false;
//		if (mstrReportName.equals(REPORT_NAME_HETDY)) {
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer str=new StringBuffer();
		str.append(getHetdy());
		if(MainGlobal.getXitxx_item("��ͬ", "��ͬ��ӡ�Ƿ��ҳ��ʾ����˫����Ϣ", "0", "��").equals("��")){
			str.append("<p style='page-break-after: always'></p>");
		}
		str.append(getJiew());
		this.setTijsh();
		if(this.isTijsh()){
			if(visit.getString4()!=null && visit.getString4().equals("2")){//ģ��
				return str.toString();
			}else{//��ͬ
				return str.toString();
			}
		}else{
			return str.toString();
		}
		
		
//		} else {
//			return "�޴˱���";
//		}
	}
//	private void loadBean (Object obj,"Hetxxbean"){
//		
//	}
	
	private int getXiaosw(){
		Visit visit=(Visit)this.getPage().getVisit();
		int xsw = 3;
		xsw = Integer.parseInt(MainGlobal.getXitxx_item("��ͬ", "�ۼۺ��������ֶ�Ҫ������С��λ", String.valueOf(visit.getDiancxxb_id()), "3"));
		return xsw;
	}
	
	private String getHetMBdy(){

		
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String[] gongs=new String[3];
		try{

			String sql="select id\n" +
			"from (\n" + 
			"select id,leix,decode(mingc,'�����������ַ���',1,'�۸��������ַ���',2,3)xuh,mingc\n" + 
			"from gongsb\n" + 
			"where gongsb.zhuangt=1 and leix='��ͬ'\n" + 
			")\n" + 
			"order by xuh";
			ResultSetList rs0=con.getResultSetList(sql);
			DataBassUtil clob=new DataBassUtil();
			int k=0;
			while(rs0.next()){
				//gongs[k++]=rs0.getString(1);
				gongs[k++]=clob.getClob("gongsb", "gongs", rs0.getLong(0));
				
			}
//��ͬ
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select hetb_mb.ID,\n");
		buffer.append("       HETBH,\n" );
		buffer.append("       to_char(QIANDRQ,'YYYY-MM-DD')QIANDRQ,\n" );
		buffer.append("       QIANDDD,\n" );
		buffer.append("       GONGFDWMC,\n" );
		buffer.append("       GONGFDWDZ,\n" );
		buffer.append("       GONGFDH,\n" );
		buffer.append("       GONGFFDDBR,\n" );
		buffer.append("       GONGFWTDLR,\n" );
		buffer.append("       GONGFDBGH,\n" );
		buffer.append("       GONGFKHYH,\n" );
		buffer.append("       GONGFZH,\n" );
		buffer.append("       GONGFYZBM,\n" );
		buffer.append("       GONGFSH,\n" );
		buffer.append("       XUFDWMC,\n" );
		buffer.append("       XUFDWDZ,\n" );
		buffer.append("       XUFFDDBR,\n" );
		buffer.append("       XUFWTDLR,\n" );
		buffer.append("       XUFDH,\n" );
		buffer.append("       XUFDBGH,\n" );
		buffer.append("       XUFKHYH,\n" );
		buffer.append("       XUFZH,\n" );
		buffer.append("       XUFYZBM,\n" );
		buffer.append("       XUFSH,\n" );
		buffer.append("       HETGYSBID,\n" );
		buffer.append("       GONGYSB_ID,\n" );
		buffer.append("      to_char( QISRQ,'YYYY-MM-DD')QISRQ,\n" );
		buffer.append("        to_char(GUOQRQ,'YYYY-MM-DD')GUOQRQ,\n" );
		buffer.append("       jihkjb.mingc jihkjmc,\n" );
		buffer.append("       diancxxb_id,\n" );
		buffer.append("       hetb_mb.ID hetb_mb_id,\n" );
		buffer.append("       meikmcs\n" );
		buffer.append("  from hetb_mb,jihkjb\n" );
		buffer.append(" where hetb_mb.jihkjb_id=jihkjb.id and hetb_mb.ID = "+((Visit) getPage().getVisit()).getLong1());
		//Hetxxbean bean=new Hetxxbean();
		ResultSet rs=con.getResultSet(buffer);
		String qiandrq="";
		String QISRQ="";
		String GUOQRQ="";
		String jihkjmc="";
		String fahr="";
		
		if(rs.next()){
			bean.setHetbh(rs.getString("HETBH"));
			bean.setQianddd(rs.getString("QIANDDD"));
			bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
			bean.setXUFDWMC(rs.getString("XUFDWMC"));
			bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
			bean.setGONGFDH(rs.getString("GONGFDH"));
			bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
			bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
			bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
			bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
			bean.setGONGFZH(rs.getString("GONGFZH"));
			bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
			bean.setGongfsh(rs.getString("GONGFSH"));
			bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
			bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
			bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
			bean.setXUFDH(rs.getString("XUFDH"));
			bean.setXUFDBGH(rs.getString("XUFDBGH"));
			bean.setXUFKHYH(rs.getString("XUFKHYH"));
			bean.setXUFZH(rs.getString("XUFZH"));
			bean.setXUFYZBM(rs.getString("XUFYZBM"));
			bean.setXufsh(rs.getString("XUFSH"));
			qiandrq=rs.getString("QIANDRQ");
			QISRQ=rs.getString("QISRQ");
			GUOQRQ=rs.getString("GUOQRQ");
			jihkjmc=rs.getString("jihkjmc");
			fahr=(rs.getString("meikmcs")==null)?"":rs.getString("meikmcs");
		}
//����
		if(((Visit) getPage().getVisit()).getList7()==null){
			((Visit) getPage().getVisit()).setList7(new ArrayList());
		}
		List shulBeans=((Visit) getPage().getVisit()).getList7();
		shulBeans.clear();
		buffer.setLength(0);
		buffer.append(" select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n" );
		buffer.append(" y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n" );
		buffer.append(",diancxxb.mingc shouhr\n" );
		buffer.append("from(\n" );
		buffer.append("\n" );
		buffer.append("select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n" );
		buffer.append("y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n" );
		buffer.append("from\n" );
		buffer.append("    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where hetb_id="+((Visit) getPage().getVisit()).getLong1()+"\n" );
		buffer.append("    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
		buffer.append("    )a,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,1 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='01'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y1,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,2 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='02'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y2,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,3 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='03'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y3,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,4 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='04'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y4,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,5 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='05'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y5,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,6as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='06'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y6,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,7 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='07'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y7,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,8 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='08'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y8,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,9 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='09'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y9,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,10 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='10'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y10,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,11 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='11'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y11,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,12 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='12'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y12\n" );
		buffer.append("\n" );
		buffer.append("------------------------------------\n" );
		buffer.append("where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n" );
		buffer.append("and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n" );
		buffer.append("and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n" );
		buffer.append("and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n" );
		buffer.append("and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n" );
		buffer.append("and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n" );
		buffer.append("and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n" );
		buffer.append("and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n" );
		buffer.append("and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n" );
		buffer.append("and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n" );
		buffer.append("and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n" );
		buffer.append("and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n" );
		buffer.append(")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n" );
		buffer.append("where  aa.pinzb_id=pinzb.id(+) and aa.yunsfsb_id=yunsfsb.id(+)\n" );
		buffer.append("and faz.id(+)=aa.faz_id and aa.daoz_id=daoz.id(+) and diancxxb.id=aa.diancxxb_id");
		ResultSet rs1=con.getResultSet(buffer);
		while(rs1.next()){
			Fahxxbean fahxxbean=new Fahxxbean();
			fahxxbean.setShouhr(rs1.getString("shouhr"));
			fahxxbean.setPinz(rs1.getString("pinz"));
			fahxxbean.setYunsfs(rs1.getString("yunsfs"));
			fahxxbean.setFaz(rs1.getString("faz"));
			fahxxbean.setDaoz(rs1.getString("daoz"));
			fahxxbean.setY1(rs1.getLong("y1"));
			fahxxbean.setY2(rs1.getLong("y2"));
			fahxxbean.setY3(rs1.getLong("y3"));
			fahxxbean.setY4(rs1.getLong("y4"));
			fahxxbean.setY5(rs1.getLong("y5"));
			fahxxbean.setY6(rs1.getLong("y6"));
			fahxxbean.setY7(rs1.getLong("y7"));
			fahxxbean.setY8(rs1.getLong("y8"));
			fahxxbean.setY9(rs1.getLong("y9"));
			fahxxbean.setY10(rs1.getLong("y10"));
			fahxxbean.setY11(rs1.getLong("y11"));
			fahxxbean.setY12(rs1.getLong("y12"));
			fahxxbean.setHej(rs1.getLong("y12")+rs1.getLong("y11")+rs1.getLong("y10")+rs1.getLong("y9")+rs1.getLong("y8")
					+rs1.getLong("y7")+rs1.getLong("y6")+rs1.getLong("y5")+rs1.getLong("y4")+rs1.getLong("y3")+rs1.getLong("y2")
					+rs1.getLong("y1"));
			shulBeans.add(fahxxbean);
		}
//����
		int xuangm=0;//��Ŀ���� 
		if(MainGlobal.getXitxx_item("��ͬ��ѯ", "��Ŀ��", "0", "��").equals("��")){
			xuangm=1;
		}
		
		buffer.setLength(0);
		buffer.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
		buffer.append("from hetzlb,zhibb,tiaojb,danwb\n" );
		buffer.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by hetzlb.id");
		if(((Visit) getPage().getVisit()).getList8()==null){
			((Visit) getPage().getVisit()).setList8(new ArrayList());
		}
		List zhilyqBeans=((Visit) getPage().getVisit()).getList8();
		zhilyqBeans.clear();
		ResultSet rs2=con.getResultSet(buffer);
		while(rs2.next()){
			Zhilyqbean zhilyqbean=new Zhilyqbean();
			zhilyqbean.setMingc(rs2.getString("zhib"));
			zhilyqbean.setTiaoj(rs2.getString("tiaoj"));
			zhilyqbean.setShangx(rs2.getDouble("shangx"));
			zhilyqbean.setXiax(rs2.getDouble("xiax"));
			zhilyqbean.setDanw(rs2.getString("danw"));
			zhilyqBeans.add(zhilyqbean);
			
		}
		//��ʽ������
		int l=zhilyqBeans.size();
		String[] zhiltks=new String[l];
		String zhiltk="";
		Interpreter bsh= new Interpreter();
		if(xuangm==1){//����Ǵ���Ŀ
			for(int i=0;i<l;i++){
				Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeans.get(i);
				bsh.set("��Ŀ",shulbean.getMingc() );
				bsh.set("����", shulbean.getTiaoj());
				bsh.set("����",shulbean.getXiax());
				bsh.set("����",shulbean.getShangx()  );
				bsh.set("��λ",shulbean.getDanw() );
				bsh.eval(gongs[0]);
				zhiltk=bsh.get("����").toString();
				zhiltks[i]="2."+(i+1)+zhiltk;//
			}
		}else{
			for(int i=0;i<l;i++){
				Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeans.get(i);
				bsh.set("��Ŀ",shulbean.getMingc() );
				bsh.set("����", shulbean.getTiaoj());
				bsh.set("����",shulbean.getXiax());
				bsh.set("����",shulbean.getShangx()  );
				bsh.set("��λ",shulbean.getDanw() );
				bsh.eval(gongs[0]);
				zhiltk=bsh.get("����").toString();
				zhiltks[i]=zhiltk;//
			}
		}
		
//�۸�
		buffer.setLength(0);
		buffer.append("select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" );
		buffer.append("hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'��','��')YINGDKF,\n" );
		buffer.append("yunsfsb.mingc yunsfsmc,ZUIGMJ,fengsjj,jijlx\n" );
		buffer.append("from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,yunsfsb\n" );
		buffer.append("where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" );
		buffer.append("and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" );
		buffer.append("and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,hetjgb.xiax");
		if(((Visit) getPage().getVisit()).getList9()==null){
			((Visit) getPage().getVisit()).setList9(new ArrayList());
		}
		List jiagBeans=((Visit) getPage().getVisit()).getList9();
		jiagBeans.clear();
		ResultSet rs3=con.getResultSet(buffer);
		while(rs3.next()){
			jijbean jibean=new jijbean();
			jibean.setZhibb_id(rs3.getString("zhibmc"));
			jibean.setTiaojb_id(rs3.getString("tiaojmc"));
			jibean.setShangx(rs3.getDouble("SHANGX"));
			jibean.setXiax(rs3.getDouble("XIAX"));
			jibean.setDanwb_id(rs3.getString("zhibdwmc"));
			jibean.setJij(rs3.getDouble("JIJ"));
			jibean.setJijdwid(rs3.getString("jijdwmc"));
			jibean.setHetjjfsb_id(rs3.getString("hetjjfsmc"));
			jibean.setHetjsfsb_id(rs3.getString("hetjsfsmc"));
			jibean.setHetjsxsb_id(rs3.getString("hetjsxsmc"));
			jibean.setYunj(rs3.getDouble("YUNJ"));
			jibean.setYunjdw_id(rs3.getString("yunjdwmc"));
			jibean.setYingdkf(rs3.getString("YINGDKF"));
			jibean.setYunsfsb_id(rs3.getString("yunsfsmc"));
			jibean.setZuigmj(rs3.getDouble("ZUIGMJ"));
			
			jibean.setFengsjj(rs3.getDouble("fengsjj"));
			jibean.setJijlx(rs3.getString("jijlx"));
			
			jiagBeans.add(jibean);
		}
//		��ʽ������
		int jijRows=0;
		l=jiagBeans.size();
		List[] jiagtks=new List[l];
		String jiagtk="";
		if(xuangm==1){//����Ǵ���Ŀ
			for(int i=0;i<l;i++){
				jijbean jigbean=(jijbean)jiagBeans.get(i);
				bsh.set("��Ŀ",jigbean.getZhibb_id());
				bsh.set("����",jigbean.getTiaojb_id());
				bsh.set("����",jigbean.getXiax());
				bsh.set("����",jigbean.getShangx());
				bsh.set("��λ",jigbean.getDanwb_id());
				bsh.set("�۸�",jigbean.getJij());
				bsh.set("�۸�λ",jigbean.getJijdwid());
				bsh.set("���㷽ʽ",jigbean.getHetjsfsb_id());
				bsh.set("�Ƽ۷�ʽ",jigbean.getHetjjfsb_id());
				bsh.set("��Ȩ��ʽ",jigbean.getHetjsxsb_id());
				bsh.set("�˼�",jigbean.getYunj());
				bsh.set("�˼۵�λ",jigbean.getYunjdw_id());
				bsh.set("���䷽ʽ",jigbean.getYunsfsb_id());
				bsh.set("���ú��",jigbean.getZuigmj());
				
				bsh.set("�ֹ�˾�Ӽ�",jigbean.getFengsjj());
				bsh.set("�Ƽ�����",jigbean.getJijlx());
				bsh.eval(gongs[1]);
				jiagtk=bsh.get("����").toString();
				jiagtk="3."+(i+1)+jiagtk;//
				List jiagtkList=new ArrayList();
				jiagtkList=getRows(jiagtk,118);
				jijRows+=jiagtkList.size();
				jiagtks[i]=jiagtkList;
			}
		}else{
			for(int i=0;i<l;i++){
				jijbean jigbean=(jijbean)jiagBeans.get(i);
				bsh.set("��Ŀ",jigbean.getZhibb_id());
				bsh.set("����",jigbean.getTiaojb_id());
				bsh.set("����",jigbean.getXiax());
				bsh.set("����",jigbean.getShangx());
				bsh.set("��λ",jigbean.getDanwb_id());
				bsh.set("�۸�",jigbean.getJij());
				bsh.set("�۸�λ",jigbean.getJijdwid());
				bsh.set("���㷽ʽ",jigbean.getHetjsfsb_id());
				bsh.set("�Ƽ۷�ʽ",jigbean.getHetjjfsb_id());
				bsh.set("��Ȩ��ʽ",jigbean.getHetjsxsb_id());
				bsh.set("�˼�",jigbean.getYunj());
				bsh.set("�˼۵�λ",jigbean.getYunjdw_id());
				bsh.set("���䷽ʽ",jigbean.getYunsfsb_id());
				bsh.set("���ú��",jigbean.getZuigmj());
				
				bsh.set("�ֹ�˾�Ӽ�",jigbean.getFengsjj());
				bsh.set("�Ƽ�����",jigbean.getJijlx());
				bsh.eval(gongs[1]);
				jiagtk=bsh.get("����").toString();
				List jiagtkList=new ArrayList();
				jiagtkList=getRows(jiagtk,118);
				jijRows+=jiagtkList.size();
				jiagtks[i]=jiagtkList;
			}
		}
		
//���ۿ�
		buffer.setLength(0);
		buffer.append("select z.ID,zhibb.mingc zhibmc,zhibb.bianm,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" );
		buffer.append("KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" );
		buffer.append("decode(XIAOSCL,1,'��λ',2,'��ȥ',3,'��������',4,'��������(0.1)',5,'��������(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" );
		buffer.append(",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" );
		buffer.append("from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
		buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
		buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+) \n" );
		buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,z.xiax");
		if(((Visit) getPage().getVisit()).getList10()==null){
			((Visit) getPage().getVisit()).setList10(new ArrayList());
		}
		List zengkkBeans=((Visit) getPage().getVisit()).getList10();
		zengkkBeans.clear();
		ResultSet rs4=con.getResultSet(buffer);
		while(rs4.next()){
			Zengkkbean zengkkbean=new Zengkkbean();
			zengkkbean.setZHIBB_ID(rs4.getString("zhibmc"));
			zengkkbean.setZhibb_bm(rs4.getString("bianm"));
			zengkkbean.setTIAOJB_ID(rs4.getString("tiaojmc"));
			zengkkbean.setSHANGX(rs4.getDouble("SHANGX"));
			zengkkbean.setXIAX(rs4.getDouble("XIAX"));
			zengkkbean.setDANWB_ID(rs4.getString("zhibdwmc"));
			zengkkbean.setJIS(rs4.getDouble("JIS"));
			zengkkbean.setJISDWID(rs4.getString("jisdwmc"));
			zengkkbean.setKOUJ(rs4.getDouble("KOUJ"));
			zengkkbean.setKOUJDW(rs4.getString("koujdwmc"));
			zengkkbean.setZENGFJ(rs4.getDouble("ZENGFJ"));
			zengkkbean.setZENGFJDW(rs4.getString("zengfjdwmc"));
			zengkkbean.setXIAOSCL(rs4.getString("XIAOSCL"));
			zengkkbean.setJIZZKJ(rs4.getDouble("JIZZKJ"));
			zengkkbean.setJIZZB(rs4.getDouble("JIZZB"));
			zengkkbean.setCANZXM(rs4.getString("canzxmmc"));
			zengkkbean.setCANZXMDW(rs4.getString("canzxmdwmc"));
			zengkkbean.setCANZSX(rs4.getDouble("CANZSX"));
			zengkkbean.setCANZXX(rs4.getDouble("CANZXX"));
			zengkkbean.setJIESXXB_ID(rs4.getString("hetjsxsmc"));
			zengkkbean.setYUNSFSB_ID(rs4.getString("yunsfsmc"));
			zengkkBeans.add(zengkkbean);
		}	
//		��ʽ������
		int zengkkRows=0;
		l=zengkkBeans.size();
		List[] zengkktks=new List[l];
		String zengkktk="";
		if(xuangm==1){//����Ǵ���Ŀ
			for(int i=0;i<l;i++){
				Zengkkbean zengkkbean=(Zengkkbean)zengkkBeans.get(i);
				bsh.set("��Ŀ",zengkkbean.getZHIBB_ID());
				bsh.set("��Ŀ����",zengkkbean.getZhibb_bm());
				bsh.set("����",zengkkbean.getTIAOJB_ID());
				bsh.set("����",zengkkbean.getXIAX());
				bsh.set("����",zengkkbean.getSHANGX());
				bsh.set("��λ",zengkkbean.getDANWB_ID());
				bsh.set("����",zengkkbean.getJIS());
				bsh.set("������λ",zengkkbean.getJISDWID());
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(getXiaosw());
				String kouj = df.format(zengkkbean.getKOUJ()); 
				bsh.set("�ۼ�",kouj);
//				bsh.set("�ۼ�",zengkkbean.getKOUJ());
				bsh.set("�ۼ۵�λ",zengkkbean.getKOUJDW());
				bsh.set("����",zengkkbean.getZENGFJ());
				bsh.set("���۵�λ",zengkkbean.getZENGFJDW());
				bsh.set("С������",zengkkbean.getXIAOSCL());
				bsh.set("��Ȩ��ʽ",zengkkbean.getJIESXXB_ID());
				bsh.set("���䷽ʽ",zengkkbean.getYUNSFSB_ID());
				bsh.set("��׼���ۼ�",zengkkbean.getJIZZKJ());
				bsh.set("��׼ָ��",zengkkbean.getJIZZB());
				bsh.set("������Ŀ",zengkkbean.getCANZXM());
				bsh.set("������Ŀ��λ",zengkkbean.getCANZXMDW());
				bsh.set("������Ŀ����",zengkkbean.getCANZSX());
				bsh.set("������Ŀ����",zengkkbean.getCANZXX());
				bsh.eval(gongs[2]);
				zengkktk=bsh.get("����").toString();
				zengkktk="3."+(i+jiagBeans.size()+1)+zengkktk;//
				List zengkktkList=new ArrayList();
				zengkktkList=getRows(zengkktk,118);
				zengkkRows+=zengkktkList.size();
				zengkktks[i]=zengkktkList;
			}
		}else{
			for(int i=0;i<l;i++){
				Zengkkbean zengkkbean=(Zengkkbean)zengkkBeans.get(i);
				bsh.set("��Ŀ",zengkkbean.getZHIBB_ID());
				bsh.set("��Ŀ����",zengkkbean.getZhibb_bm());
				bsh.set("����",zengkkbean.getTIAOJB_ID());
				bsh.set("����",zengkkbean.getXIAX());
				bsh.set("����",zengkkbean.getSHANGX());
				bsh.set("��λ",zengkkbean.getDANWB_ID());
				bsh.set("����",zengkkbean.getJIS());
				bsh.set("������λ",zengkkbean.getJISDWID());
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(getXiaosw());
//				DecimalFormat df = new DecimalFormat("0.0000"); 
				String kouj = df.format(zengkkbean.getKOUJ()); 
				bsh.set("�ۼ�",kouj);
//				bsh.set("�ۼ�",zengkkbean.getKOUJ());
				bsh.set("�ۼ۵�λ",zengkkbean.getKOUJDW());
				bsh.set("����",zengkkbean.getZENGFJ());
				bsh.set("���۵�λ",zengkkbean.getZENGFJDW());
				bsh.set("С������",zengkkbean.getXIAOSCL());
				bsh.set("��Ȩ��ʽ",zengkkbean.getJIESXXB_ID());
				bsh.set("���䷽ʽ",zengkkbean.getYUNSFSB_ID());
				bsh.set("��׼���ۼ�",zengkkbean.getJIZZKJ());
				bsh.set("��׼ָ��",zengkkbean.getJIZZB());
				bsh.set("������Ŀ",zengkkbean.getCANZXM());
				bsh.set("������Ŀ��λ",zengkkbean.getCANZXMDW());
				bsh.set("������Ŀ����",zengkkbean.getCANZSX());
				bsh.set("������Ŀ����",zengkkbean.getCANZXX());
				bsh.eval(gongs[2]);
				zengkktk=bsh.get("����").toString();
				List zengkktkList=new ArrayList();
				zengkktkList=getRows(zengkktk,118);
				zengkkRows+=zengkktkList.size();
				zengkktks[i]=zengkktkList;
			}
		}
		
		
//����
		String wenzStr="";
		buffer.setLength(0);
		buffer.append("select id,wenznr\n");
		buffer.append("from hetwzb\n" );
		buffer.append("where hetb_id="+((Visit) getPage().getVisit()).getLong1());
		ResultSet rs5=con.getResultSet(buffer);
		while(rs5.next()){
			wenzStr=rs5.getString("wenznr");
		}
//��ʽ������	
		List wenzList=new ArrayList();
		int qittkRows=wenzList.size();
		if(wenzStr==null){
			qittkRows=0;
		}else{
			if(!wenzStr.equals("")){
				wenzList=getRows(wenzStr,100);
				qittkRows=wenzList.size();
			}
		}
		//////////////////////////////////////////////////
//�����ݡ�ǿ�кϲ����в���
		String[][] ArrHeader = new String[1][17];
		int ArrWidth[]=new int[] {35,32,32,42,42,45,35,35,35,35,35,35,35,35,35,35,35,35};
		rt.setTitle("ú ̿ �� �� �� ͬ ģ ��"+"("+jihkjmc+")", ArrWidth);
		int iRows=0;
		int iHeadRows=4;
		int iShulRows=shulBeans.size();
		int iZhilRows=zhilyqBeans.size();
		int iJiagRows=jijRows+zengkkRows;//zengkkBeans.size();jiagBeans.size()+
		int iQitRows=qittkRows;
		int iGongxfRows=11;
		int iYouxq=1;
		final int HANGJJ=30;//�м��
		boolean jiagbg =false;
		int jiag_biaot=0;
		if(MainGlobal.getXitxx_item("��ͬ��ѯ", "�۸���", "0", "��").equals("��")){
			jiagbg=true;
			jiag_biaot++;//+1�۸����
			jijRows=jiagBeans.size();
			iJiagRows=jijRows+zengkkRows;
		}
		iRows=iHeadRows+iShulRows+3+iZhilRows+1+iJiagRows+1+iQitRows+1+iGongxfRows+iYouxq+2+jiag_biaot;
		if(fahr==""){
			iRows-=1;
		}
		rt.setBody(new Table(iRows,17));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		
/*
*huochaoyuan
*2009-10-22������ͬ��ӡ�ı���ʾ������������/������λ�ú���ͷ��ʾ��
*/		rt.body.setFontSize(10);
		rt.body.setCellValue(1, 1, "<font size=2><b>����(�׷�):</b></font>"+bean.getXUFDWMC());
		rt.body.mergeCell(1, 1, 1, 12);
		rt.body.setCellValue(1, 13,  "<font size=2><b>��ͬ���:</b></font>"+bean.getHetbh());
		rt.body.mergeCell(1, 13, 1, 17);

		rt.body.setCellValue(2, 13, "<b><font size=2>ǩ������:</b></font>"+qiandrq);
		rt.body.mergeCell(2, 13, 2, 17);

		rt.body.setCellValue(3, 1, "<font size=2><b>����(�ҷ�):</b></font>"+bean.getGONGFDWMC());
//end
		rt.body.mergeCell(3, 1, 3, 12);
		rt.body.mergeCell(3, 13, 3, 17);
		rt.body.setCellValue(3, 13, "<font size=2><b>ǩ���ص�:</b></font>"+bean.getQianddd());
		rt.body.setRowHeight(3,HANGJJ);
		rt.body.setCellVAlign(3, 1,Table.VALIGN_TOP);
		rt.body.setCellVAlign(3, 13,Table.VALIGN_TOP);
		
		rt.body.setCellValue(4, 1, "&nbsp;&nbsp;&nbsp;&nbsp;���ݡ��л����񹲺͹���ͬ���������ع�ƽ��������ԭ�򣬾�˫��Э��һ�£�" +"�������Э�飬ǩ");
		rt.body.mergeCell(4, 1, 4, 17);
		rt.body.setCellValue(5, 1, "����ͬԼ��˫����ͬ���С�");
		rt.body.mergeCell(5, 1, 5, 17);
		
		rt.body.setCellValue(6, 1, "<font size=2><b>һ��ú̿Ʒ�֡�������λ����������</b></font>");
		rt.body.setRowHeight(6,HANGJJ);
		rt.body.setCellVAlign(6, 1,Table.VALIGN_BOTTOM);
		rt.body.mergeCell(6, 1, 6, 17);
		
		/*rt.body.setCellValue(7, 1, "�ջ���");
		rt.body.setCellValue(8, 1, "");
		rt.body.mergeCell(7, 1, 8, 1);*/
		rt.body.setCellValue(7, 1, "ú��");
		rt.body.setCellValue(8, 1, "");
		rt.body.mergeCell(7, 1, 8, 1);
		rt.body.setCellValue(7, 2, "���䷽ʽ");
		rt.body.setCellValue(8, 2, "");
		rt.body.mergeCell(7, 2, 8, 2);
		rt.body.setCellValue(7, 3, "��վ");
		rt.body.setCellValue(8, 3, "");
		rt.body.mergeCell(7, 3, 8, 3);
		rt.body.setCellValue(7, 4, "��վ");
		rt.body.setCellValue(8, 4, "");
		rt.body.mergeCell(7, 4, 8, 4);
		
		rt.body.setCellValue(7, 5, "����(���)");
		rt.body.setCellAlign(7, 5, Table.ALIGN_CENTER);
		rt.body.mergeCell(7, 5, 7, 17);
		rt.body.setCellValue(8, 5, "�ϼ�");
		rt.body.setCellValue(8, 6, "һ��");
		rt.body.setCellValue(8, 7, "����");
		rt.body.setCellValue(8, 8, "����");
		rt.body.setCellValue(8, 9, "����");
		rt.body.setCellValue(8, 10, "����");
		rt.body.setCellValue(8, 11, "����");
		rt.body.setCellValue(8, 12, "����");
		rt.body.setCellValue(8, 13, "����");
		rt.body.setCellValue(8, 14, "����");
		rt.body.setCellValue(8, 15, "ʮ��");
		rt.body.setCellValue(8, 16, "ʮһ��");
		rt.body.setCellValue(8, 17, "ʮ����");
		for(int i=9;i<iShulRows+9;i++){//ú̿����//
				rt.body.setCellValue(i, 1,((Fahxxbean) shulBeans.get(i-9)).getShouhr());//�ջ���
				rt.body.setCellValue(i, 2,((Fahxxbean) shulBeans.get(i-9)).getPinz());//Ʒ��
				rt.body.setCellValue(i, 3,((Fahxxbean) shulBeans.get(i-9)).getYunsfs());//���䷽ʽ
				rt.body.setCellValue(i, 4,((Fahxxbean) shulBeans.get(i-9)).getFaz());//��վ
				rt.body.setCellValue(i, 5,((Fahxxbean) shulBeans.get(i-9)).getDaoz());//��վ
				rt.body.setCellValue(i, 6,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getHej()/10000f));//�ϼ�
				rt.body.setCellValue(i, 7,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY1()/10000f));//һ��
				rt.body.setCellValue(i, 8,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY2()/10000f));//����
				rt.body.setCellValue(i, 9,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY3()/10000f));//����
				rt.body.setCellValue(i, 10,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY4()/10000f));//����
				rt.body.setCellValue(i, 11,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY5()/10000f));//����
				rt.body.setCellValue(i, 12,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY6()/10000f));//����
				rt.body.setCellValue(i, 13,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY7()/10000f));//����
				rt.body.setCellValue(i, 14,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY8()/10000f));//����
				rt.body.setCellValue(i, 15,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY9()/10000f));//����
				rt.body.setCellValue(i, 16,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY10()/10000f));//ʮ��
				rt.body.setCellValue(i, 17,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY11()/10000f));//ʮһ��
				rt.body.setCellValue(i, 17,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY12()/10000f));//ʮ����
		}
		
		int fahrow=0;//����������
		if(fahr!=""){
			rt.body.setCellValue(iShulRows+9, 1, "����ú��"+fahr);
			rt.body.mergeCell(iShulRows+9, 1, iShulRows+9, 17);
			fahrow++;
		}
		int zhilrowInd=fahrow+iShulRows+9;//����������
		rt.body.setRowHeight(zhilrowInd,HANGJJ);
		rt.body.setCellVAlign(zhilrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(zhilrowInd, 1, "<font size=2><b>����ú̿����");
		rt.body.mergeCell(zhilrowInd, 1, zhilrowInd, 17);
		
		for(int i=zhilrowInd+1;i<iZhilRows+zhilrowInd+1;i++){//ú̿��������//
			rt.body.setCellValue(i, 1,zhiltks[i-(zhilrowInd+1)]);
			rt.body.mergeCell(i, 1, i, 17);
			
		}
		int jiagrowInd=iZhilRows+zhilrowInd+1;//�۸�����
		rt.body.setRowHeight(jiagrowInd,HANGJJ);
		rt.body.setCellVAlign(jiagrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(jiagrowInd, 1, "<font size=2><b>��������۸�</b></font>");
		rt.body.mergeCell(jiagrowInd, 1, jiagrowInd, 17);
		int jiag_table_index=0;
		int jiag_table_rows=0;
		if(jiagbg){//����Ǽ۸���
//			�۸����	
			jiag_table_index =jiagrowInd+1;
			jiag_table_rows =jiagBeans.size()+1;
			
			rt.body.setCellValue(jiagrowInd+1, 1,"ָ��");
			rt.body.setCellValue(jiagrowInd+1, 2,"����("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 4,"����("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 6,"�۸�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 8,"���㷽ʽ");
			rt.body.setCellValue(jiagrowInd+1, 10,"��Ȩ��ʽ");
			rt.body.setCellValue(jiagrowInd+1, 12,"���䷽ʽ");
			rt.body.setCellValue(jiagrowInd+1, 14,"�ⶥú��("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 16,"�˼�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 17,"�Ӽ�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.mergeCell(jiagrowInd+1, 2, jiagrowInd+1, 3);
			rt.body.mergeCell(jiagrowInd+1, 4, jiagrowInd+1, 5);
			rt.body.mergeCell(jiagrowInd+1, 6, jiagrowInd+1, 7);
			rt.body.mergeCell(jiagrowInd+1, 8, jiagrowInd+1, 9);
			rt.body.mergeCell(jiagrowInd+1, 10, jiagrowInd+1, 11);
			rt.body.mergeCell(jiagrowInd+1, 12, jiagrowInd+1, 13);
			rt.body.mergeCell(jiagrowInd+1, 14, jiagrowInd+1, 15);
			rt.body.mergeCell(jiagrowInd+1, 16, jiagrowInd+1, 17);
//			rt.body.setCells(7,1,7,17,Table.PER_BORDER_TOP,2);
//			rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
//			rt.body.setCells(7,17,iShulRows+8,17,Table.PER_BORDER_RIGHT,2);
//			rt.body.setCells(iShulRows+8,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,2);
//			rt.body.setCellBorderRight(7,6,2);
			int kk=0;
			jiagrowInd++;
			for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
				rt.body.setCellValue(jiagrowInd+1+kk, 1, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZhibb_id());
				rt.body.setCellValue(jiagrowInd+1+kk, 2, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getXiax()));
				rt.body.setCellValue(jiagrowInd+1+kk, 4, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getShangx()));
				rt.body.setCellValue(jiagrowInd+1+kk, 6, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getJij()));
				rt.body.setCellValue(jiagrowInd+1+kk, 8, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsfsb_id());
				rt.body.setCellValue(jiagrowInd+1+kk, 10, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsxsb_id());
				rt.body.setCellValue(jiagrowInd+1+kk, 12, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunsfsb_id());
				rt.body.setCellValue(jiagrowInd+1+kk, 14, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZuigmj()));
				rt.body.setCellValue(jiagrowInd+1+kk, 16, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunj()));
				rt.body.setCellValue(jiagrowInd+1+kk, 17, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getFengsjj()));
				
				rt.body.mergeCell(jiagrowInd+kk+1, 2, jiagrowInd+kk+1, 3);
				rt.body.mergeCell(jiagrowInd+kk+1, 4, jiagrowInd+kk+1, 5);
				rt.body.mergeCell(jiagrowInd+kk+1, 6, jiagrowInd+kk+1, 7);
				rt.body.mergeCell(jiagrowInd+kk+1, 8, jiagrowInd+kk+1, 9);
				rt.body.mergeCell(jiagrowInd+kk+1, 10, jiagrowInd+kk+1, 11);
				rt.body.mergeCell(jiagrowInd+kk+1, 12, jiagrowInd+kk+1, 13);
				rt.body.mergeCell(jiagrowInd+kk+1, 14, jiagrowInd+kk+1, 15);
				rt.body.mergeCell(jiagrowInd+kk+1, 16, jiagrowInd+kk+1, 17);
				kk+=1;
			}
			
		}else{
			int kk=0;
			for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
				int c=jiagtks[i-(jiagrowInd+1)].size();
				for (int j=0;j<c;j++){
					rt.body.setCellValue(jiagrowInd+1+kk, 1, jiagtks[i-(jiagrowInd+1)].get(j).toString());
					rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 17);
				}
			}
		}
		int kk=0;
		for(int i=jijRows+jiagrowInd+1;i<zengkkBeans.size()+jijRows+jiagrowInd+1;i++){//�������ۿ�,��㣺�����ڲ�����
			int c=zengkktks[i-(jijRows+jiagrowInd+1)].size();
			for (int j=0;j<c;j++){
				rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1, zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString());
				rt.body.mergeCell(jijRows+jiagrowInd+1+kk, 1, jijRows+jiagrowInd+1+kk++, 17);
			}
		}
		int qittkrow=iJiagRows+jiagrowInd+1;//��������������
		rt.body.setRowHeight(qittkrow,HANGJJ);
		rt.body.setCellVAlign(qittkrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(qittkrow, 1, "<font size=2><b>�ġ���������</b></font>");
		rt.body.mergeCell(qittkrow, 1, qittkrow, 17);
		
//		rt.body.setCellValue(qittkrow+1, 1, wenzStr);//����������ʱһ��
		for(int i=qittkrow+1;i<qittkRows+qittkrow+1;i++){
			rt.body.setCellValue(i, 1, wenzList.get(i-(qittkrow+1)).toString());//strTiaok_jiag
			rt.body.mergeCell(i, 1, i, 17);
		}
//		rt.body.mergeCell(qittkrow+1, 1, qittkrow+1, 17);
		int gongxfrow=qittkrow+qittkRows+1;
		rt.body.setRowHeight(gongxfrow,HANGJJ);
		rt.body.setCellVAlign(gongxfrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 9,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 2,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 12,Table.VALIGN_BOTTOM);
		
		rt.body.setCellValue(gongxfrow, 1, "<font size=2><b>����</b></font>");
		rt.body.mergeCell(gongxfrow, 1, gongxfrow, 8);
		rt.body.setCellValue(gongxfrow, 9, "<font size=2><b>�跽</b></font>");
		rt.body.mergeCell(gongxfrow, 9, gongxfrow, 17);
		rt.body.setCellAlign(gongxfrow, 9, Table.ALIGN_CENTER);
		rt.body.setCellAlign(gongxfrow, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(gongxfrow+1, 1, "��λ����(��):");
		rt.body.setCellValue(gongxfrow+1, 2, bean.getGONGFDWMC());
		rt.body.setCellValue(gongxfrow+1, 9, "��λ����(��):");
		rt.body.setCellValue(gongxfrow+1, 12, bean.getXUFDWMC());
		rt.body.mergeCell(gongxfrow+1, 2, gongxfrow+1, 8);
		rt.body.mergeCell(gongxfrow+1, 12, gongxfrow+1, 17);
		rt.body.mergeCell(gongxfrow+1, 9, gongxfrow+1, 11);
		rt.body.setCellAlign(gongxfrow+1, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+2, 1, "��λ��ַ:");
		rt.body.setCellValue(gongxfrow+2, 2, bean.getGONGFDWDZ());
		rt.body.setCellValue(gongxfrow+2, 9, "��λ��ַ:");
		rt.body.setCellValue(gongxfrow+2, 12,bean.getXUFDWDZ());
		rt.body.mergeCell(gongxfrow+2, 2, gongxfrow+2, 8);
		rt.body.mergeCell(gongxfrow+2, 12, gongxfrow+2, 17);
		rt.body.mergeCell(gongxfrow+2, 9, gongxfrow+2, 11);
		rt.body.setCellAlign(gongxfrow+2, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+3, 1, "����������:");
		rt.body.setCellValue(gongxfrow+3, 2, bean.getGONGFFDDBR());
		rt.body.setCellValue(gongxfrow+3, 9, "����������:");
		rt.body.setCellValue(gongxfrow+3, 12, bean.getXUFFDDBR());
		rt.body.mergeCell(gongxfrow+3, 2, gongxfrow+3, 8);
		rt.body.mergeCell(gongxfrow+3, 12, gongxfrow+3, 17);
		rt.body.mergeCell(gongxfrow+3, 9, gongxfrow+3, 11);
		rt.body.setCellAlign(gongxfrow+3, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+4, 1, "ί�д�����:");
		rt.body.setCellValue(gongxfrow+4, 2, bean.getGONGFWTDLR());
		rt.body.setCellValue(gongxfrow+4, 9, "ί�д�����:");
		rt.body.setCellValue(gongxfrow+4, 12, bean.getXUFWTDLR());
		rt.body.mergeCell(gongxfrow+4, 2, gongxfrow+4, 8);
		rt.body.mergeCell(gongxfrow+4, 12, gongxfrow+4, 17);
		rt.body.mergeCell(gongxfrow+4, 9, gongxfrow+4, 11);
		rt.body.setCellAlign(gongxfrow+4, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+5, 1, "�绰:");
		rt.body.setCellValue(gongxfrow+5, 2,bean.getGONGFDH());
		rt.body.setCellValue(gongxfrow+5, 9, "�绰:");
		rt.body.setCellValue(gongxfrow+5, 12, bean.getXUFDH());
		rt.body.mergeCell(gongxfrow+5, 2, gongxfrow+5, 8);
		rt.body.mergeCell(gongxfrow+5, 12, gongxfrow+5, 17);
		rt.body.mergeCell(gongxfrow+5, 9, gongxfrow+5, 11);
		rt.body.setCellAlign(gongxfrow+5, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+6, 1, "�����:");
		rt.body.setCellValue(gongxfrow+6, 2, bean.getGONGFDBGH());
		rt.body.setCellValue(gongxfrow+6, 9, "�����:");
		rt.body.setCellValue(gongxfrow+6, 12, bean.getXUFDBGH());
		rt.body.mergeCell(gongxfrow+6, 2, gongxfrow+6, 8);
		rt.body.mergeCell(gongxfrow+6, 12, gongxfrow+6, 17);
		rt.body.mergeCell(gongxfrow+6, 9, gongxfrow+6, 11);
		rt.body.setCellAlign(gongxfrow+6, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+7, 1, "��������:");
		rt.body.setCellValue(gongxfrow+7,2, bean.getGONGFKHYH());
		rt.body.setCellValue(gongxfrow+7, 9, "��������:");
		rt.body.setCellValue(gongxfrow+7, 12, bean.getXUFKHYH());
		rt.body.mergeCell(gongxfrow+7, 2, gongxfrow+7, 8);
		rt.body.mergeCell(gongxfrow+7, 12, gongxfrow+7, 17);
		rt.body.mergeCell(gongxfrow+7, 9, gongxfrow+7, 11);
		rt.body.setCellAlign(gongxfrow+7, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+8, 1, "�˺�:");
		rt.body.setCellValue(gongxfrow+8, 2, bean.getGONGFZH());
		rt.body.setCellValue(gongxfrow+8, 9, "�˺�:");
		rt.body.setCellValue(gongxfrow+8, 12, bean.getXUFZH());
		rt.body.mergeCell(gongxfrow+8, 2, gongxfrow+8, 8);
		rt.body.mergeCell(gongxfrow+8, 12, gongxfrow+8, 17);
		rt.body.mergeCell(gongxfrow+8, 9, gongxfrow+8, 11);
		rt.body.setCellAlign(gongxfrow+8, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+9, 1, "��������:");
		rt.body.setCellValue(gongxfrow+9, 2, bean.getGONGFYZBM());
		rt.body.setCellValue(gongxfrow+9, 9, "��������:");
		rt.body.setCellValue(gongxfrow+9, 12, bean.getXUFYZBM());
		rt.body.mergeCell(gongxfrow+9, 2, gongxfrow+9, 8);
		rt.body.mergeCell(gongxfrow+9, 12, gongxfrow+9, 17);
		rt.body.mergeCell(gongxfrow+9, 9, gongxfrow+9, 11);
		rt.body.setCellAlign(gongxfrow+9, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+10, 1, "˰��:");
		rt.body.setCellValue(gongxfrow+10, 2, bean.getGongfsh());
		rt.body.setCellValue(gongxfrow+10,9, "˰��:");
		rt.body.setCellValue(gongxfrow+10, 12, bean.getXufsh());
		rt.body.mergeCell(gongxfrow+10, 2, gongxfrow+10, 8);
		rt.body.mergeCell(gongxfrow+10, 12, gongxfrow+10, 17);
		rt.body.mergeCell(gongxfrow+10, 9, gongxfrow+10, 11);
		rt.body.setCellAlign(gongxfrow+10, 9, Table.ALIGN_RIGHT);
		
		rt.body.setRowHeight(gongxfrow+11,HANGJJ);
		rt.body.setCellVAlign(gongxfrow+11, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(gongxfrow+11, 1, "<font size=2><b>��Ч����:"+QISRQ+"��"+GUOQRQ+"</b></font>");
		rt.body.mergeCell(gongxfrow+11, 1, gongxfrow+11, 17);
		rt.body.setCellAlign(gongxfrow+11, 1, Table.ALIGN_LEFT);
//���ñ߿�	
		rt.body.setBorderNone();
		rt.body.setCells(1,1,gongxfrow+11,17,Table.PER_BORDER_BOTTOM,0);
		rt.body.setCells(1,1,gongxfrow+11,17,Table.PER_BORDER_RIGHT,0);
		//7�е�ShulRows+8��������
		rt.body.setCells(7,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,1);
		rt.body.setCells(7,1,iShulRows+8,17,Table.PER_BORDER_RIGHT,1);
		rt.body.setCells(7,1,7,17,Table.PER_BORDER_TOP,2);
		rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
		rt.body.setCells(7,17,iShulRows+8,17,Table.PER_BORDER_RIGHT,2);
		rt.body.setCells(iShulRows+8,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,2);
		rt.body.setCellBorderRight(7,6,2);
		rt.body.setCellBorderbottom(gongxfrow+1, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+2, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+2, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+3, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+3, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+4, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+4, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+6, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+6, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+7, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+7, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+8, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+8, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+9, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+9, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+10, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+10, 12, 1);
//		int jiag_table_index =jiagrowInd+1;
//		int jiag_table_rows =jiagBeans.size()+1;
		if(jiagbg){
			for(int i=0;i<jiag_table_rows;i++){
				rt.body.setCellBorderLeft(jiag_table_index+i,1,2);
				rt.body.setCells(jiag_table_index+i,16,jiag_table_index+i,16,Table.PER_BORDER_RIGHT,2);
				if(i==0){
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_TOP,2);
				}
				if(i+1==jiag_table_rows){//���
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_BOTTOM,2);
				}else{
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_BOTTOM,1);
				}
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_RIGHT,1);
			}
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return rt.getAllPagesHtml();
		
	
	}
	
	
	
	private String Show(String sou,String mb){//�жϺ�ͬ���� �� ģ������ �Ƿ�һ��  
		
		if(sou==null){
			return "";
		}
		if(mb==null){
			return "<font color='red'>"+sou+"</font>";
		}
		if(!sou.equals(mb)){
			return "<font color='red'>"+sou+"</font>";
		}
		
		return sou;
	}
	private String getHetdy() {
		int strNum=78;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String[] gongs=new String[3];
		try{

			String sql="select id\n" +
			"from (\n" + 
			"select id,leix,decode(mingc,'�����������ַ���',1,'�۸��������ַ���',2,3)xuh,mingc\n" + 
			"from gongsb\n" + 
			"where gongsb.zhuangt=1 and leix='��ͬ'\n" + 
			")\n" + 
			"order by xuh";
			ResultSetList rs0=con.getResultSetList(sql);
			DataBassUtil clob=new DataBassUtil();
			int k=0;
			while(rs0.next()){
				//gongs[k++]=rs0.getString(1);
				gongs[k++]=clob.getClob("gongsb", "gongs", rs0.getLong(0));
				
			}
//��ͬ
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select hetb.ID,\n");
		buffer.append("       HETBH,\n" );
		buffer.append("       to_char(QIANDRQ,'YYYY-MM-DD')QIANDRQ,\n" );
		buffer.append("       QIANDDD,\n" );
		buffer.append("       GONGFDWMC,\n" );
		buffer.append("       GONGFDWDZ,\n" );
		buffer.append("       GONGFDH,\n" );
		buffer.append("       GONGFFDDBR,\n" );
		buffer.append("       GONGFWTDLR,\n" );
		buffer.append("       GONGFDBGH,\n" );
		buffer.append("       GONGFKHYH,\n" );
		buffer.append("       GONGFZH,\n" );
		buffer.append("       GONGFYZBM,\n" );
		buffer.append("       GONGFSH,\n" );
		buffer.append("       XUFDWMC,\n" );
		buffer.append("       XUFDWDZ,\n" );
		buffer.append("       XUFFDDBR,\n" );
		buffer.append("       XUFWTDLR,\n" );
		buffer.append("       XUFDH,\n" );
		buffer.append("       XUFDBGH,\n" );
		buffer.append("       XUFKHYH,\n" );
		buffer.append("       XUFZH,\n" );
		buffer.append("       XUFYZBM,\n" );
		buffer.append("       XUFSH,\n" );
		buffer.append("       HETGYSBID,\n" );
		buffer.append("       GONGYSB_ID,\n" );
		buffer.append("      to_char( QISRQ,'YYYY-MM-DD')QISRQ,\n" );
		buffer.append("        to_char(GUOQRQ,'YYYY-MM-DD')GUOQRQ,\n" );
		buffer.append("       jihkjb.mingc jihkjmc,\n" );
		buffer.append("       diancxxb_id,\n" );
		buffer.append("       hetb_mb_id,\n" );
		buffer.append("       meikmcs\n" );
		buffer.append("  from hetb,jihkjb\n" );
		buffer.append(" where hetb.jihkjb_id=jihkjb.id and hetb.ID = "+((Visit) getPage().getVisit()).getLong1());
		
		ResultSet rs=con.getResultSet(buffer);
		String qiandrq="";
		String QISRQ="";
		String GUOQRQ="";
		String jihkjmc="";
		String fahr="";
		
		
		StringBuffer buffer1 = new StringBuffer("");

		if(rs.next()){
			bean.setHetbh(rs.getString("HETBH"));
			bean.setQianddd(rs.getString("QIANDDD"));
			bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
			bean.setXUFDWMC(rs.getString("XUFDWMC"));
			bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
			bean.setGONGFDH(rs.getString("GONGFDH"));
			bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
			bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
			bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
			bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
			bean.setGONGFZH(rs.getString("GONGFZH"));
			bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
			bean.setGongfsh(rs.getString("GONGFSH"));
			bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
			bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
			bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
			bean.setXUFDH(rs.getString("XUFDH"));
			bean.setXUFDBGH(rs.getString("XUFDBGH"));
			bean.setXUFKHYH(rs.getString("XUFKHYH"));
			bean.setXUFZH(rs.getString("XUFZH"));
			bean.setXUFYZBM(rs.getString("XUFYZBM"));
			bean.setXufsh(rs.getString("XUFSH"));
			qiandrq=rs.getString("QIANDRQ");
			QISRQ=rs.getString("QISRQ");
			GUOQRQ=rs.getString("GUOQRQ");
			jihkjmc=rs.getString("jihkjmc");
			fahr=(rs.getString("meikmcs")==null)?"":rs.getString("meikmcs");
		}
		
		
		//��ͬ��Ϣ  ���ñȽ�
		/*
		if(this.isTijsh()){
			//��Ӧ�ĺ�ͬģ����������ݣ�Ҫ�Ƚ������Ƿ�һ��
			
			
			buffer1.append("select hetb_mb.ID,\n");
			buffer1.append("       HETBH,\n" );
			buffer1.append("       to_char(QIANDRQ,'YYYY-MM-DD')QIANDRQ,\n" );
			buffer1.append("       QIANDDD,\n" );
			buffer1.append("       GONGFDWMC,\n" );
			buffer1.append("       GONGFDWDZ,\n" );
			buffer1.append("       GONGFDH,\n" );
			buffer1.append("       GONGFFDDBR,\n" );
			buffer1.append("       GONGFWTDLR,\n" );
			buffer1.append("       GONGFDBGH,\n" );
			buffer1.append("       GONGFKHYH,\n" );
			buffer1.append("       GONGFZH,\n" );
			buffer1.append("       GONGFYZBM,\n" );
			buffer1.append("       GONGFSH,\n" );
			buffer1.append("       XUFDWMC,\n" );
			buffer1.append("       XUFDWDZ,\n" );
			buffer1.append("       XUFFDDBR,\n" );
			buffer1.append("       XUFWTDLR,\n" );
			buffer1.append("       XUFDH,\n" );
			buffer1.append("       XUFDBGH,\n" );
			buffer1.append("       XUFKHYH,\n" );
			buffer1.append("       XUFZH,\n" );
			buffer1.append("       XUFYZBM,\n" );
			buffer1.append("       XUFSH,\n" );
			buffer1.append("       HETGYSBID,\n" );
			buffer1.append("       GONGYSB_ID,\n" );
			buffer1.append("      to_char( QISRQ,'YYYY-MM-DD')QISRQ,\n" );
			buffer1.append("        to_char(GUOQRQ,'YYYY-MM-DD')GUOQRQ,\n" );
			buffer1.append("       jihkjb.mingc jihkjmc,\n" );
			buffer1.append("       diancxxb_id,\n" );
			buffer1.append("       hetb_mb.ID hetb_mb_id,\n" );
			buffer1.append("       meikmcs\n" );
			buffer1.append("  from hetb_mb,jihkjb\n" );
			buffer1.append(" where hetb_mb.jihkjb_id=jihkjb.id and hetb_mb.ID in (select hetb_mb_id from  hetb where id="+((Visit) getPage().getVisit()).getLong1()+")");
			
			ResultSetList rmb1=con.getResultSetList(buffer1.toString());
			
			if(rmb1.next()){
				
				bean.setHetbh(Show(bean.getHetbh(),rmb1.getString("HETBH")));
				bean.setQianddd(Show(bean.getQianddd(),rmb1.getString("QIANDDD")));
				bean.setGONGFDWMC(Show(bean.getGONGFDWMC(),rmb1.getString("GONGFDWMC")));
				bean.setXUFDWMC(Show(bean.getXUFDWMC(),rmb1.getString("XUFDWMC")));
			
				
				bean.setGONGFDWDZ(Show(bean.getGONGFDWDZ(),rmb1.getString("GONGFDWDZ")));
				bean.setGONGFDH(Show(bean.getGONGFDH(),rmb1.getString("GONGFDH")));
				bean.setGONGFFDDBR(Show(bean.getGONGFFDDBR(),rmb1.getString("GONGFFDDBR")));
				bean.setGONGFWTDLR(Show(bean.getGONGFWTDLR(),rmb1.getString("GONGFWTDLR")));
				bean.setGONGFDBGH(Show(bean.getGONGFDBGH(),rmb1.getString("GONGFDBGH")));
				bean.setGONGFKHYH(Show(bean.getGONGFKHYH(),rmb1.getString("GONGFKHYH")));
				bean.setGONGFZH(Show(bean.getGONGFZH(),rmb1.getString("GONGFZH")));
				bean.setGONGFYZBM(Show(bean.getGONGFYZBM(),rmb1.getString("GONGFYZBM")));
				bean.setGongfsh(Show(bean.getGongfsh(),rmb1.getString("GONGFSH")));
				bean.setXUFDWDZ(Show(bean.getXUFDWDZ(),rmb1.getString("XUFDWDZ")));
				bean.setXUFFDDBR(Show(bean.getXUFFDDBR(),rmb1.getString("XUFFDDBR")));
				bean.setXUFWTDLR(Show(bean.getXUFWTDLR(),rmb1.getString("XUFWTDLR")));
				bean.setXUFDH(Show(bean.getXUFDH(),rmb1.getString("XUFDH")));
				bean.setXUFDBGH(Show(bean.getXUFDBGH(),rmb1.getString("XUFDBGH")));
				bean.setXUFKHYH(Show(bean.getXUFKHYH(),rmb1.getString("XUFKHYH")));
				bean.setXUFZH(Show(bean.getXUFZH(),rmb1.getString("XUFZH")));
				bean.setXUFYZBM(Show(bean.getXUFYZBM(),rmb1.getString("XUFYZBM")));
				bean.setXufsh(Show(bean.getXufsh(),rmb1.getString("XUFSH")));
				qiandrq=rs.getString("QIANDRQ");
				QISRQ=rs.getString("QISRQ");
				GUOQRQ=rs.getString("GUOQRQ");
				jihkjmc=rs.getString("jihkjmc");
				fahr=(rs.getString("meikmcs")==null)?"":rs.getString("meikmcs");
			}
		}*/
		
//����
		if(((Visit) getPage().getVisit()).getList7()==null){
			((Visit) getPage().getVisit()).setList7(new ArrayList());
		}
		List shulBeans=((Visit) getPage().getVisit()).getList7();
		shulBeans.clear();
		buffer.setLength(0);
		buffer.append(" select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n" );
		buffer.append(" y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n" );
		buffer.append(",diancxxb.mingc shouhr\n" );
		buffer.append("from(\n" );
		buffer.append("\n" );
		buffer.append("select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n" );
		buffer.append("y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n" );
		buffer.append("from\n" );
		buffer.append("    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where hetb_id="+((Visit) getPage().getVisit()).getLong1()+"\n" );
		buffer.append("    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
		buffer.append("    )a,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,1 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='01'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y1,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,2 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='02'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y2,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,3 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='03'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y3,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,4 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='04'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y4,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,5 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='05'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y5,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,6as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='06'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y6,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,7 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='07'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y7,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,8 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='08'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y8,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,9 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='09'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y9,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,10 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='10'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y10,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,11 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='11'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y11,\n" );
		buffer.append("    ----------\n" );
		buffer.append("    (select hetb_id,id,12 as y,hetl\n" );
		buffer.append("    from hetslb\n" );
		buffer.append("    where to_char(riq,'MM')='12'and hetslb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+")y12\n" );
		buffer.append("\n" );
		buffer.append("------------------------------------\n" );
		buffer.append("where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n" );
		buffer.append("and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n" );
		buffer.append("and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n" );
		buffer.append("and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n" );
		buffer.append("and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n" );
		buffer.append("and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n" );
		buffer.append("and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n" );
		buffer.append("and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n" );
		buffer.append("and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n" );
		buffer.append("and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n" );
		buffer.append("and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n" );
		buffer.append("and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n" );
		buffer.append(")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n" );
		buffer.append("where  aa.pinzb_id=pinzb.id(+) and aa.yunsfsb_id=yunsfsb.id(+)\n" );
		buffer.append("and faz.id(+)=aa.faz_id and aa.daoz_id=daoz.id(+) and diancxxb.id=aa.diancxxb_id");
		ResultSet rs1=con.getResultSet(buffer);
		
		
		while(rs1.next()){
			Fahxxbean fahxxbean=new Fahxxbean();
			fahxxbean.setShouhr(rs1.getString("shouhr"));
			fahxxbean.setPinz(rs1.getString("pinz"));
			fahxxbean.setYunsfs(rs1.getString("yunsfs"));
			fahxxbean.setFaz(rs1.getString("faz"));
			fahxxbean.setDaoz(rs1.getString("daoz"));
			fahxxbean.setY1(rs1.getLong("y1"));
			fahxxbean.setY2(rs1.getLong("y2"));
			fahxxbean.setY3(rs1.getLong("y3"));
			fahxxbean.setY4(rs1.getLong("y4"));
			fahxxbean.setY5(rs1.getLong("y5"));
			fahxxbean.setY6(rs1.getLong("y6"));
			fahxxbean.setY7(rs1.getLong("y7"));
			fahxxbean.setY8(rs1.getLong("y8"));
			fahxxbean.setY9(rs1.getLong("y9"));
			fahxxbean.setY10(rs1.getLong("y10"));
			fahxxbean.setY11(rs1.getLong("y11"));
			fahxxbean.setY12(rs1.getLong("y12"));
			fahxxbean.setHej(rs1.getLong("y12")+rs1.getLong("y11")+rs1.getLong("y10")+rs1.getLong("y9")+rs1.getLong("y8")
					+rs1.getLong("y7")+rs1.getLong("y6")+rs1.getLong("y5")+rs1.getLong("y4")+rs1.getLong("y3")+rs1.getLong("y2")
					+rs1.getLong("y1"));
			shulBeans.add(fahxxbean);
		}
		
		
		
//����
		buffer.setLength(0);
		buffer.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
		buffer.append("from hetzlb,zhibb,tiaojb,danwb\n" );
		buffer.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by hetzlb.id");
		if(((Visit) getPage().getVisit()).getList8()==null){
			((Visit) getPage().getVisit()).setList8(new ArrayList());
		}
		List zhilyqBeans=((Visit) getPage().getVisit()).getList8();
		zhilyqBeans.clear();
		ResultSet rs2=con.getResultSet(buffer);
		while(rs2.next()){
			Zhilyqbean zhilyqbean=new Zhilyqbean();
			zhilyqbean.setMingc(rs2.getString("zhib"));
			zhilyqbean.setTiaoj(rs2.getString("tiaoj"));
			zhilyqbean.setShangx(rs2.getDouble("shangx"));
			zhilyqbean.setXiax(rs2.getDouble("xiax"));
			zhilyqbean.setDanw(rs2.getString("danw"));
			zhilyqBeans.add(zhilyqbean);
			
		}
		//��ʽ������
		int l=zhilyqBeans.size();
		String[] zhiltks=new String[l];
		String zhiltk="";
		Interpreter bsh= new Interpreter();
		
		for(int i=0;i<l;i++){
			Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeans.get(i);
			bsh.set("��Ŀ",shulbean.getMingc() );
			bsh.set("����", shulbean.getTiaoj());
			bsh.set("����",shulbean.getXiax());
			bsh.set("����",shulbean.getShangx()  );
			bsh.set("��λ",shulbean.getDanw() );
			bsh.eval(gongs[0]);
			zhiltk="����"+(i+1)+"��"+bsh.get("����").toString();
			zhiltks[i]=zhiltk;
		}
//�۸�
		buffer.setLength(0);
		buffer.append("select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" );
		buffer.append("hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'��','��')YINGDKF,\n" );
		buffer.append("yunsfsb.mingc yunsfsmc,ZUIGMJ,fengsjj,jijlx\n" );
		buffer.append("from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,yunsfsb\n" );
		buffer.append("where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" );
		buffer.append("and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" );
		buffer.append("and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,hetjgb.xiax");
		if(((Visit) getPage().getVisit()).getList9()==null){
			((Visit) getPage().getVisit()).setList9(new ArrayList());
		}
		List jiagBeans=((Visit) getPage().getVisit()).getList9();
		jiagBeans.clear();
		ResultSet rs3=con.getResultSet(buffer);
		while(rs3.next()){
			jijbean jibean=new jijbean();
			jibean.setZhibb_id(rs3.getString("zhibmc"));
			jibean.setTiaojb_id(rs3.getString("tiaojmc"));
			jibean.setShangx(rs3.getDouble("SHANGX"));
			jibean.setXiax(rs3.getDouble("XIAX"));
			jibean.setDanwb_id(rs3.getString("zhibdwmc"));
			jibean.setJij(rs3.getDouble("JIJ"));
			jibean.setJijdwid(rs3.getString("jijdwmc"));
			jibean.setHetjjfsb_id(rs3.getString("hetjjfsmc"));
			jibean.setHetjsfsb_id(rs3.getString("hetjsfsmc"));
			jibean.setHetjsxsb_id(rs3.getString("hetjsxsmc"));
			jibean.setYunj(rs3.getDouble("YUNJ"));
			jibean.setYunjdw_id(rs3.getString("yunjdwmc"));
			jibean.setYingdkf(rs3.getString("YINGDKF"));
			jibean.setYunsfsb_id(rs3.getString("yunsfsmc"));
			jibean.setZuigmj(rs3.getDouble("ZUIGMJ"));
			
			jibean.setFengsjj(rs3.getDouble("fengsjj"));
			jibean.setJijlx(rs3.getString("jijlx"));
			
			jiagBeans.add(jibean);
		}
//		��ʽ������
		int jijRows=0;
		l=jiagBeans.size();
		List[] jiagtks=new List[l];
		String jiagtk="";
		for(int i=0;i<l;i++){
			jijbean jigbean=(jijbean)jiagBeans.get(i);
			bsh.set("��Ŀ",jigbean.getZhibb_id());
			bsh.set("����",jigbean.getTiaojb_id());
			bsh.set("����",jigbean.getXiax());
			bsh.set("����",jigbean.getShangx());
			bsh.set("��λ",jigbean.getDanwb_id());
			bsh.set("�۸�",jigbean.getJij());
			bsh.set("�۸�λ",jigbean.getJijdwid());
			bsh.set("���㷽ʽ",jigbean.getHetjsfsb_id());
			bsh.set("�Ƽ۷�ʽ",jigbean.getHetjjfsb_id());
			bsh.set("��Ȩ��ʽ",jigbean.getHetjsxsb_id());
			bsh.set("�˼�",jigbean.getYunj());
			bsh.set("�˼۵�λ",jigbean.getYunjdw_id());
			bsh.set("���䷽ʽ",jigbean.getYunsfsb_id());
			bsh.set("���ú��",jigbean.getZuigmj());
			
			bsh.set("�ֹ�˾�Ӽ�",jigbean.getFengsjj());
			bsh.set("�Ƽ�����",jigbean.getJijlx());
			bsh.eval(gongs[1]);
			jiagtk="����"+(zhilyqBeans.size()+i+1)+"��"+bsh.get("����").toString();
			
			List jiagtkList=new ArrayList();
			jiagtkList=getRows(jiagtk,strNum);
			//jiagtkList.add(jiagtk);
			jijRows+=jiagtkList.size();
			jiagtks[i]=jiagtkList;
		}
//���ۿ�
		buffer.setLength(0);
		buffer.append("select z.ID,zhibb.mingc zhibmc,zhibb.bianm,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" );
		buffer.append("KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" );
		buffer.append("decode(XIAOSCL,1,'��λ',2,'��ȥ',3,'��������',4,'��������(0.1)',5,'��������(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" );
		buffer.append(",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" );
		buffer.append("from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
		buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
		buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+) \n" );
		buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,decode(tiaojb.mingc,'����',z.xiax,'����',z.xiax,'С��',z.shangx,'���ڵ���',z.xiax,'С�ڵ���',z.shangx,z.xiax)");
		if(((Visit) getPage().getVisit()).getList10()==null){
			((Visit) getPage().getVisit()).setList10(new ArrayList());
		}
		List zengkkBeans=((Visit) getPage().getVisit()).getList10();
		zengkkBeans.clear();
		ResultSet rs4=con.getResultSet(buffer);
		while(rs4.next()){
			Zengkkbean zengkkbean=new Zengkkbean();
			zengkkbean.setZHIBB_ID(rs4.getString("zhibmc"));
			zengkkbean.setZhibb_bm(rs4.getString("bianm"));
			zengkkbean.setTIAOJB_ID(rs4.getString("tiaojmc"));
			zengkkbean.setSHANGX(rs4.getDouble("SHANGX"));
			zengkkbean.setXIAX(rs4.getDouble("XIAX"));
			zengkkbean.setDANWB_ID(rs4.getString("zhibdwmc"));
			zengkkbean.setJIS(rs4.getDouble("JIS"));
			zengkkbean.setJISDWID(rs4.getString("jisdwmc"));
			zengkkbean.setKOUJ(rs4.getDouble("KOUJ"));
			zengkkbean.setKOUJDW(rs4.getString("koujdwmc"));
			zengkkbean.setZENGFJ(rs4.getDouble("ZENGFJ"));
			zengkkbean.setZENGFJDW(rs4.getString("zengfjdwmc"));
			zengkkbean.setXIAOSCL(rs4.getString("XIAOSCL"));
			zengkkbean.setJIZZKJ(rs4.getDouble("JIZZKJ"));
			zengkkbean.setJIZZB(rs4.getDouble("JIZZB"));
			zengkkbean.setCANZXM(rs4.getString("canzxmmc"));
			zengkkbean.setCANZXMDW(rs4.getString("canzxmdwmc"));
			zengkkbean.setCANZSX(rs4.getDouble("CANZSX"));
			zengkkbean.setCANZXX(rs4.getDouble("CANZXX"));
			zengkkbean.setJIESXXB_ID(rs4.getString("hetjsxsmc"));
			zengkkbean.setYUNSFSB_ID(rs4.getString("yunsfsmc"));
			zengkkBeans.add(zengkkbean);
		}	
		//��û�׼��
		int jizj=0;
		buffer.setLength(0);

		buffer.append("select max(h.shangx) jizj\n" +
		"  from hetzkkb h\n" + 
		" where h.hetb_id = "+((Visit) getPage().getVisit()).getLong1()+"\n" + 
		"   and h.zengfj = 0");
		ResultSet list=con.getResultSet(buffer);
		while(list.next()){
			jizj=list.getInt("jizj");
		}
//		��ʽ������
		int zengkkRows=0;
		l=zengkkBeans.size();
		List[] zengkktks=new List[l];
		String zengkktk="";
		for(int i=0;i<l;i++){
			Zengkkbean zengkkbean=(Zengkkbean)zengkkBeans.get(i);
			bsh.set("��Ŀ",zengkkbean.getZHIBB_ID());
			bsh.set("��Ŀ����",zengkkbean.getZhibb_bm());
			bsh.set("����",zengkkbean.getTIAOJB_ID());
			bsh.set("����",zengkkbean.getXIAX());
			bsh.set("����",zengkkbean.getSHANGX());
			bsh.set("��λ",zengkkbean.getDANWB_ID());
			bsh.set("����",zengkkbean.getJIS());
			bsh.set("������λ",zengkkbean.getJISDWID());
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(getXiaosw());
//			DecimalFormat df = new DecimalFormat("0.0000"); 
//			String kouj = df.format(zengkkbean.getKOUJ()); 
			String kouj = String.valueOf(zengkkbean.getKOUJ());
			
			if (Double.parseDouble(kouj)==0){
				bsh.set("�ۼ�",0);
			}else{
				bsh.set("�ۼ�",kouj);
			}
//			bsh.set("�ۼ�",zengkkbean.getKOUJ());
			bsh.set("�ۼ۵�λ",zengkkbean.getKOUJDW());
			bsh.set("����",zengkkbean.getZENGFJ());
			bsh.set("���۵�λ",zengkkbean.getZENGFJDW());
			bsh.set("С������",zengkkbean.getXIAOSCL());
			bsh.set("��Ȩ��ʽ",zengkkbean.getJIESXXB_ID());
			bsh.set("���䷽ʽ",zengkkbean.getYUNSFSB_ID());
			bsh.set("��׼���ۼ�",zengkkbean.getJIZZKJ());
			bsh.set("��׼ָ��",zengkkbean.getJIZZB());
			bsh.set("������Ŀ",zengkkbean.getCANZXM());
			bsh.set("������Ŀ��λ",zengkkbean.getCANZXMDW());
			bsh.set("������Ŀ����",zengkkbean.getCANZSX());
			bsh.set("������Ŀ����",zengkkbean.getCANZXX());
			bsh.set("��׼��", jizj);
			bsh.eval(gongs[2]);
			zengkktk="����"+(i+1)+"��"+bsh.get("����").toString();
			
			List zengkktkList=new ArrayList();
			zengkktkList=getRows(zengkktk,strNum);
			//zengkktkList.add(zengkktk);
			zengkkRows+=zengkktkList.size();
			zengkktks[i]=zengkktkList;
		}
		
//����
		String wenzStr="";
		int isQit=0;
		buffer.setLength(0);
		buffer.append("select id,wenznr\n");
		buffer.append("from hetwzb\n" );
		buffer.append("where hetb_id="+((Visit) getPage().getVisit()).getLong1());
		ResultSet rs5=con.getResultSet(buffer);
		while(rs5.next()){
			wenzStr=rs5.getString("wenznr");
		}
//��ʽ������	
		List wenzList=new ArrayList();
		int qittkRows=wenzList.size();
		if(wenzStr==null){
			qittkRows=0;
		}else{
			if(!wenzStr.equals("")){
				wenzList=getRows(wenzStr,strNum);
				for (int i = 0; i < wenzList.size(); i++) {
					String temp =(String)wenzList.get(i);
					String firstStr=temp==null||temp.equals("")?"":(temp.trim()).substring(0, 1);
					String newTemp="<font size=3><b>"+temp+"</b></font>";
					if (firstStr.equals("��")||
						firstStr.equals("��")||
						firstStr.equals("��")||
						firstStr.equals("��")||
						firstStr.equals("��")||
						firstStr.equals("��")||
						firstStr.equals("ʮ")||
						firstStr.equals("ʮһ")||
						firstStr.equals("ʮ��")||
						firstStr.equals("ʮ��")||
						firstStr.equals("ʮ��")||
						firstStr.equals("ʮ��")||
						firstStr.equals("ʮ��")||
						firstStr.equals("ʮ��")) {
						wenzList.set(i, newTemp);
						isQit++;
					}else{
						wenzList.set(i, "����"+(String)wenzList.get(i));
					}
					
				}
				//wenzList.add(wenzStr);
				qittkRows=wenzList.size();
			}
		}
		//////////////////////////////////////////////////
//�����ݡ�ǿ�кϲ����в���
		String[][] ArrHeader = new String[1][17];
		int ArrWidth[]=new int[] {35,40,40,35,35,30,30,30,30,30,30,30,30,30,35,35,35};
		rt.setTitle("ú ̿ �� �� �� ͬ"+"("+jihkjmc+")", ArrWidth);
		int iRows=0;
		int iHeadRows=5;
		int iShulRows=shulBeans.size();
		int iZhilRows=zhilyqBeans.size();
		int iJiagRows=jijRows;//zengkkBeans.size();jiagBeans.size()+
		int iZengkkRows=zengkkRows;
		int iQitRows=qittkRows;
		//int iGongxfRows=23;
		int iYouxq=1;
		final int HANGJJ=30;//�м��
		boolean jiagbg =false;
		int jiag_biaot=0;
		if(MainGlobal.getXitxx_item("��ͬ��ѯ", "�۸���", "0", "��").equals("��")){
			jiagbg=true;
			jiag_biaot++;//+1�۸����
			jijRows=jiagBeans.size();
			iJiagRows=jijRows+zengkkRows;
		}
		iRows=iHeadRows+iShulRows+3+iZhilRows+iJiagRows+1+iZengkkRows+1+iQitRows+1+jiag_biaot;
		if (isQit!=0) {
			iRows--;
		}
		/*if(fahr==""){
			iRows-=1;
		}*/
		rt.setBody(new Table(iRows,17));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		
/*
*huochaoyuan
*2009-10-22������ͬ��ӡ�ı���ʾ������������/������λ�ú���ͷ��ʾ��
*/			
		rt.body.setFontSize(10);
		rt.body.setCellValue(1, 1, "<font size=3><b>����(�׷�):</b></font>"+bean.getXUFDWMC());
		rt.body.mergeCell(1, 1, 1, 11);
		rt.body.setCellValue(1, 12,  "<font size=3><b>��ͬ���:</b></font>"+bean.getHetbh());
		rt.body.mergeCell(1, 12, 1, 17);

		rt.body.setCellValue(2, 12, "<b><font size=3>ǩ������:</b></font>"+qiandrq);
		rt.body.mergeCell(2, 12, 2, 17);

		rt.body.setCellValue(2, 1, "<font size=3><b>����(�ҷ�):</b></font>"+bean.getGONGFDWMC());
//		end	
		rt.body.mergeCell(2, 1, 3, 11);
		rt.body.mergeCell(3, 12, 3, 17);
		rt.body.setCellValue(3, 12, "<font size=3><b>ǩ���ص�:</b></font>"+bean.getQianddd()+"<br>&nbsp;");
		rt.body.setRowHeight(3,HANGJJ);
		rt.body.setCellVAlign(2, 1,Table.VALIGN_TOP);
		rt.body.setCellVAlign(3, 12,Table.VALIGN_TOP);
		
		rt.body.setCellValue(4, 1, "&nbsp;&nbsp;&nbsp;&nbsp;���ݡ��л����񹲺͹���ͬ���������ع�ƽ��������ԭ�򣬾�˫��Э��һ�£�" +"�������Э�飬ǩ����ͬԼ��˫����ͬ���С�");
		rt.body.mergeCell(4, 1, 4, 17);
		rt.body.setCellValue(5, 1, "");
		rt.body.mergeCell(5, 1, 5, 17);
		
		rt.body.setCellValue(6, 1, "<font size=3><b>һ��ú̿Ʒ�֡�������λ����������</b></font>");
		rt.body.setRowHeight(6,HANGJJ);
		rt.body.setCellVAlign(6, 1,Table.VALIGN_BOTTOM);
		rt.body.mergeCell(6, 1, 6, 17);
		
		/*rt.body.setCellValue(7, 1, "�ջ���");
		rt.body.setCellValue(8, 1, "");
		rt.body.mergeCell(7, 1, 8, 1);*/
		rt.body.setCellValue(7, 1, "ú��");
		rt.body.setCellValue(8, 1, "");
		rt.body.mergeCell(7, 1, 8, 1);
		rt.body.setCellValue(7, 2, "���䷽ʽ");
		rt.body.setCellValue(8, 2, "");
		rt.body.mergeCell(7, 2, 8, 2);
		rt.body.setCellValue(7, 3, "��վ");
		rt.body.setCellValue(8, 3, "");
		rt.body.mergeCell(7, 3, 8, 3);
		rt.body.setCellValue(7, 4, "��վ");
		rt.body.setCellValue(8, 4, "");
		rt.body.mergeCell(7, 4, 8, 4);
		
		rt.body.setCellValue(7, 5, "����(���)");
		rt.body.setCellAlign(7, 5, Table.ALIGN_CENTER);
		rt.body.mergeCell(7, 5, 7, 17);
		rt.body.setCellValue(8, 5, "�ϼ�");
		rt.body.setCellValue(8, 6, "1��");
		rt.body.setCellValue(8, 7, "2��");
		rt.body.setCellValue(8, 8, "3��");
		rt.body.setCellValue(8, 9, "4��");
		rt.body.setCellValue(8, 10, "5��");
		rt.body.setCellValue(8, 11, "6��");
		rt.body.setCellValue(8, 12, "7��");
		rt.body.setCellValue(8, 13, "8��");
		rt.body.setCellValue(8, 14, "9��");
		rt.body.setCellValue(8, 15, "10��");
		rt.body.setCellValue(8, 16, "11��");
		rt.body.setCellValue(8, 17, "12��");
		
		
		List sl =new ArrayList();
		if(this.isTijsh()){		
			
			buffer1.setLength(0);
			buffer1.append(" select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n" );
			buffer1.append(" y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n" );
			buffer1.append(",diancxxb.mingc shouhr\n" );
			buffer1.append("from(\n" );
			buffer1.append("\n" );
			buffer1.append("select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n" );
			buffer1.append("y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n" );
			buffer1.append("from\n" );
			buffer1.append("    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+")\n" );
			buffer1.append("    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n" );
			buffer1.append("    )a,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,1 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='01'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y1,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,2 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='02'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y2,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,3 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='03'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y3,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,4 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='04'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y4,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,5 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='05'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y5,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,6as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='06'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y6,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,7 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='07'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y7,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,8 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='08'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y8,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,9 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='09'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y9,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,10 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='10'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y10,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,11 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='11'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y11,\n" );
			buffer1.append("    ----------\n" );
			buffer1.append("    (select hetb_id,id,12 as y,hetl\n" );
			buffer1.append("    from hetslb\n" );
			buffer1.append("    where to_char(riq,'MM')='12'and hetslb.hetb_id in ( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+"))y12\n" );
			buffer1.append("\n" );
			buffer1.append("------------------------------------\n" );
			buffer1.append("where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n" );
			buffer1.append("and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n" );
			buffer1.append("and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n" );
			buffer1.append("and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n" );
			buffer1.append("and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n" );
			buffer1.append("and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n" );
			buffer1.append("and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n" );
			buffer1.append("and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n" );
			buffer1.append("and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n" );
			buffer1.append("and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n" );
			buffer1.append("and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n" );
			buffer1.append("and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n" );
			buffer1.append(")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n" );
			buffer1.append("where  aa.pinzb_id=pinzb.id(+) and aa.yunsfsb_id=yunsfsb.id(+)\n" );
			buffer1.append("and faz.id(+)=aa.faz_id and aa.daoz_id=daoz.id(+) and diancxxb.id=aa.diancxxb_id");
			
			
			ResultSetList rmb1=con.getResultSetList(buffer1.toString());
			
			
			while(rmb1.next()){
				Fahxxbean fahxxbean=new Fahxxbean();
				fahxxbean.setShouhr(rmb1.getString("shouhr"));
				fahxxbean.setPinz(rmb1.getString("pinz"));
				fahxxbean.setYunsfs(rmb1.getString("yunsfs"));
				fahxxbean.setFaz(rmb1.getString("faz"));
				fahxxbean.setDaoz(rmb1.getString("daoz"));
				fahxxbean.setY1(rmb1.getLong("y1"));
				fahxxbean.setY2(rmb1.getLong("y2"));
				fahxxbean.setY3(rmb1.getLong("y3"));
				fahxxbean.setY4(rmb1.getLong("y4"));
				fahxxbean.setY5(rmb1.getLong("y5"));
				fahxxbean.setY6(rmb1.getLong("y6"));
				fahxxbean.setY7(rmb1.getLong("y7"));
				fahxxbean.setY8(rmb1.getLong("y8"));
				fahxxbean.setY9(rmb1.getLong("y9"));
				fahxxbean.setY10(rmb1.getLong("y10"));
				fahxxbean.setY11(rmb1.getLong("y11"));
				fahxxbean.setY12(rmb1.getLong("y12"));
				fahxxbean.setHej(rmb1.getLong("y12")+rmb1.getLong("y11")+rmb1.getLong("y10")+rmb1.getLong("y9")+rmb1.getLong("y8")
						+rmb1.getLong("y7")+rmb1.getLong("y6")+rmb1.getLong("y5")+rmb1.getLong("y4")+rmb1.getLong("y3")+rmb1.getLong("y2")
						+rmb1.getLong("y1"));
				sl.add(fahxxbean);
			}
			
			
			
			}
		
		if(this.isTijsh()){
			
			
				for(int i=9;i<iShulRows+9;i++){
					
					String shouhr=((Fahxxbean) shulBeans.get(i-9)).getShouhr();
					String pinz=((Fahxxbean) shulBeans.get(i-9)).getPinz();
					String yunsfs=((Fahxxbean) shulBeans.get(i-9)).getYunsfs();
					String faz=((Fahxxbean) shulBeans.get(i-9)).getFaz();
					String daoz=((Fahxxbean) shulBeans.get(i-9)).getDaoz();
					String hej=((Fahxxbean) shulBeans.get(i-9)).getHej()+"";
					String y1=((Fahxxbean) shulBeans.get(i-9)).getY1()+"";
					String y2=((Fahxxbean) shulBeans.get(i-9)).getY2()+"";
					String y3=((Fahxxbean) shulBeans.get(i-9)).getY3()+"";
					String y4=((Fahxxbean) shulBeans.get(i-9)).getY4()+"";
					String y5=((Fahxxbean) shulBeans.get(i-9)).getY5()+"";
					String y6=((Fahxxbean) shulBeans.get(i-9)).getY6()+"";
					String y7=((Fahxxbean) shulBeans.get(i-9)).getY7()+"";
					String y8=((Fahxxbean) shulBeans.get(i-9)).getY8()+"";
					String y9=((Fahxxbean) shulBeans.get(i-9)).getY9()+"";
					String y10=((Fahxxbean) shulBeans.get(i-9)).getY10()+"";
					String y11=((Fahxxbean) shulBeans.get(i-9)).getY11()+"";
					String y12=((Fahxxbean) shulBeans.get(i-9)).getY12()+"";
					
					boolean t=false;
					for(int j=0;j<sl.size();j++){
						
						if(((Fahxxbean) shulBeans.get(i-9)).equals((Fahxxbean) sl.get(j))){
							t=true;
							break;
						}
						
						
					}
					
					if(!t){//������¼����ȣ�ÿ���ֶ���ʾ��ɫ
						shouhr=Show(shouhr,null);
						pinz=Show(pinz,null);
						yunsfs=Show(yunsfs,null);
						faz=Show(faz,null);
						daoz=Show(daoz,null);
						hej=Show(hej,null);
						y1=Show(y1,null);
						y2=Show(y2,null);
						y3=Show(y3,null);
						y4=Show(y4,null);
						y5=Show(y5,null);
						y6=Show(y6,null);
						y7=Show(y7,null);
						y8=Show(y8,null);
						y9=Show(y9,null);
						y10=Show(y10,null);
						y11=Show(y11,null);
						y12=Show(y12,null);
					}
					
					//rt.body.setCellValue(i, 1,shouhr);//�ջ���
					rt.body.setCellValue(i, 1,pinz);//Ʒ��
					rt.body.setCellValue(i, 2,yunsfs);//���䷽ʽ
					rt.body.setCellValue(i, 3,faz);//��վ
					rt.body.setCellValue(i, 4,daoz);//��վ
					rt.body.setCellValue(i, 5,hej);//�ϼ�
					rt.body.setCellValue(i, 6,y1);//һ��
					rt.body.setCellValue(i, 7,y2);//����
					rt.body.setCellValue(i, 8,y3);//����
					rt.body.setCellValue(i, 9,y4);//����
					rt.body.setCellValue(i, 10,y5);//����
					rt.body.setCellValue(i, 11,y6);//����
					rt.body.setCellValue(i, 12,y7);//����
					rt.body.setCellValue(i, 13,y8);//����
					rt.body.setCellValue(i, 14,y9);//����
					rt.body.setCellValue(i, 15,y10);//ʮ��
					rt.body.setCellValue(i, 16,y11);//ʮһ��
					rt.body.setCellValue(i, 17,y12);//ʮ����
				}
			
			
		}else{
			for(int i=9;i<iShulRows+9;i++){//ú̿����//
				//rt.body.setCellValue(i, 1,((Fahxxbean) shulBeans.get(i-9)).getShouhr());//�ջ���
				rt.body.setCellValue(i, 1,((Fahxxbean) shulBeans.get(i-9)).getPinz());//Ʒ��
				rt.body.setCellValue(i, 2,((Fahxxbean) shulBeans.get(i-9)).getYunsfs());//���䷽ʽ
				rt.body.setCellValue(i, 3,((Fahxxbean) shulBeans.get(i-9)).getFaz());//��վ
				rt.body.setCellValue(i, 4,((Fahxxbean) shulBeans.get(i-9)).getDaoz());//��վ
				rt.body.setCellValue(i, 5,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getHej()/10000f));//�ϼ�
				rt.body.setCellValue(i, 6,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY1()/10000f));//һ��
				rt.body.setCellValue(i, 7,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY2()/10000f));//����
				rt.body.setCellValue(i, 8,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY3()/10000f));//����
				rt.body.setCellValue(i, 9,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY4()/10000f));//����
				rt.body.setCellValue(i, 10,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY5()/10000f));//����
				rt.body.setCellValue(i, 11,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY6()/10000f));//����
				rt.body.setCellValue(i, 12,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY7()/10000f));//����
				rt.body.setCellValue(i, 13,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY8()/10000f));//����
				rt.body.setCellValue(i, 14,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY9()/10000f));//����
				rt.body.setCellValue(i, 15,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY10()/10000f));//ʮ��
				rt.body.setCellValue(i, 16,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY11()/10000f));//ʮһ��
				rt.body.setCellValue(i, 17,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY12()/10000f));//ʮ����
		}
		
		}
		
		
		/*int fahrow=0;//����������
		if(fahr!=""){
			rt.body.setCellValue(iShulRows+9, 1, "����ú��"+fahr);
			rt.body.mergeCell(iShulRows+9, 1, iShulRows+9, 17);
			fahrow++;
		}*/
		/*int zhilrowInd=fahrow+iShulRows+9;//����������
		rt.body.setRowHeight(zhilrowInd,HANGJJ);
		rt.body.setCellVAlign(zhilrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(zhilrowInd, 1, "<font size=3><b>����ú̿����");
		rt.body.mergeCell(zhilrowInd, 1, zhilrowInd, 17);
		
		
		
		if(this.isTijsh()){
//			����
			buffer1.setLength(0);
			buffer1.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
			buffer1.append("from hetzlb,zhibb,tiaojb,danwb\n" );
			buffer1.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id  in (select hetb_mb_id from hetb  where id="+((Visit) getPage().getVisit()).getLong1()+") order by hetzlb.id");
			
			List zhilyqBeansMB=new ArrayList();
			zhilyqBeansMB.clear();
			ResultSet rmb2=con.getResultSet(buffer1);
			while(rmb2.next()){
				Zhilyqbean zhilyqbean=new Zhilyqbean();
				zhilyqbean.setMingc(rmb2.getString("zhib"));
				zhilyqbean.setTiaoj(rmb2.getString("tiaoj"));
				zhilyqbean.setShangx(rmb2.getDouble("shangx"));
				zhilyqbean.setXiax(rmb2.getDouble("xiax"));
				zhilyqbean.setDanw(rmb2.getString("danw"));
				zhilyqBeansMB.add(zhilyqbean);
				
			}
			//��ʽ������
			int lk=zhilyqBeansMB.size();
			String[] zhiltksMB=new String[lk];
			String zhiltkMB="";
			Interpreter bshMB= new Interpreter();
			
			for(int i=0;i<lk;i++){
				Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeansMB.get(i);
				bshMB.set("��Ŀ",shulbean.getMingc() );
				bshMB.set("����", shulbean.getTiaoj());
				bshMB.set("����",shulbean.getXiax());
				bshMB.set("����",shulbean.getShangx()  );
				bshMB.set("��λ",shulbean.getDanw() );
				bshMB.eval(gongs[0]);
				zhiltkMB=bshMB.get("����").toString();
				zhiltksMB[i]=zhiltkMB;
			}
			
			
			
			int temp1=1;
			for(int i=zhilrowInd+1;i<iZhilRows+zhilrowInd+1;i++){//��ʼ�Ƚ������Ƿ�һ��
				
				String va_t=zhiltks[i-(zhilrowInd+1)];
				
				boolean t=false;
				for(int j=0;j<zhiltksMB.length;j++){
			
					if(zhiltks[i-(zhilrowInd+1)]!=null && zhiltks[i-(zhilrowInd+1)].equals(zhiltksMB[j])){
						
						t=true;
						break;
					}
					
					
				}
				
				if(!t){
					va_t=this.Show(zhiltks[i-(zhilrowInd+1)], null);
				}
				
				rt.body.setCellValue(i, 1,temp1+"��"+va_t);
				rt.body.mergeCell(i, 1, i, 17);
				temp1++;
			}
			
		}else{
			int temp1=1;
			for(int i=zhilrowInd+1;i<iZhilRows+zhilrowInd+1;i++){//ú̿��������//
				rt.body.setCellValue(i, 1,temp1+"��"+zhiltks[i-(zhilrowInd+1)]);
				rt.body.mergeCell(i, 1, i, 17);
				temp1++;
				
			}
		}*/
		
//�۸�
		
		List jiagBeansMB=new ArrayList();
		List[] jiagtks1=null;
		if(this.isTijsh()){
			buffer1.setLength(0);
			buffer1.append("select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" );
			buffer1.append("hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'��','��')YINGDKF,\n" );
			buffer1.append("yunsfsb.mingc yunsfsmc,ZUIGMJ,fengsjj,jijlx\n" );
			buffer1.append("from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,yunsfsb\n" );
			buffer1.append("where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" );
			buffer1.append("and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" );
			buffer1.append("and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.hetb_id in ( select  ht.hetb_mb_id from hetb ht where ht.id="+((Visit) getPage().getVisit()).getLong1()+") order by zhibb.mingc,hetjgb.xiax");
			
			
			jiagBeansMB.clear();
			ResultSet rmb3=con.getResultSet(buffer1);
			while(rmb3.next()){
				jijbean jibean=new jijbean();
				jibean.setZhibb_id(rmb3.getString("zhibmc"));
				jibean.setTiaojb_id(rmb3.getString("tiaojmc"));
				jibean.setShangx(rmb3.getDouble("SHANGX"));
				jibean.setXiax(rmb3.getDouble("XIAX"));
				jibean.setDanwb_id(rmb3.getString("zhibdwmc"));
				jibean.setJij(rmb3.getDouble("JIJ"));
				jibean.setJijdwid(rmb3.getString("jijdwmc"));
				jibean.setHetjjfsb_id(rmb3.getString("hetjjfsmc"));
				jibean.setHetjsfsb_id(rmb3.getString("hetjsfsmc"));
				jibean.setHetjsxsb_id(rmb3.getString("hetjsxsmc"));
				jibean.setYunj(rmb3.getDouble("YUNJ"));
				jibean.setYunjdw_id(rmb3.getString("yunjdwmc"));
				jibean.setYingdkf(rmb3.getString("YINGDKF"));
				jibean.setYunsfsb_id(rmb3.getString("yunsfsmc"));
				jibean.setZuigmj(rmb3.getDouble("ZUIGMJ"));
				
				jibean.setFengsjj(rmb3.getDouble("fengsjj"));
				jibean.setJijlx(rmb3.getString("jijlx"));
				
				jiagBeansMB.add(jibean);
			}
//			��ʽ������
			int jijRows1=0;
			int lm=jiagBeansMB.size();
			jiagtks1=new List[lm];
			String jiagtk1="";
			for(int i=0;i<lm;i++){
				jijbean jigbean=(jijbean)jiagBeansMB.get(i);
				bsh.set("��Ŀ",jigbean.getZhibb_id());
				bsh.set("����",jigbean.getTiaojb_id());
				bsh.set("����",jigbean.getXiax());
				bsh.set("����",jigbean.getShangx());
				bsh.set("��λ",jigbean.getDanwb_id());
				bsh.set("�۸�",jigbean.getJij());
				bsh.set("�۸�λ",jigbean.getJijdwid());
				bsh.set("���㷽ʽ",jigbean.getHetjsfsb_id());
				bsh.set("�Ƽ۷�ʽ",jigbean.getHetjjfsb_id());
				bsh.set("��Ȩ��ʽ",jigbean.getHetjsxsb_id());
				bsh.set("�˼�",jigbean.getYunj());
				bsh.set("�˼۵�λ",jigbean.getYunjdw_id());
				bsh.set("���䷽ʽ",jigbean.getYunsfsb_id());
				bsh.set("���ú��",jigbean.getZuigmj());
				
				bsh.set("�ֹ�˾�Ӽ�",jigbean.getFengsjj());
				bsh.set("�Ƽ�����",jigbean.getJijlx());
				bsh.eval(gongs[1]);
				jiagtk1=bsh.get("����").toString();
				
				List jiagtkList=new ArrayList();
				jiagtkList=getRows(jiagtk1,117);
				jijRows1+=jiagtkList.size();
				jiagtks1[i]=jiagtkList;
			}
		}
		
		
		int jiagrowInd=iShulRows+9;//�۸�����
		rt.body.setRowHeight(jiagrowInd,HANGJJ);
		rt.body.setCellVAlign(jiagrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(jiagrowInd, 1, "<font size=3><b>����ú̿�۸�������׼</b></font>");
		rt.body.mergeCell(jiagrowInd, 1, jiagrowInd, 17);
		int jiag_table_index=0;
		int jiag_table_rows=0;
		
//		����ʾ�۸�����ǰ���������������ʾ
//		ֻ���ض��������ʾ������
		if(!this.isTijsh()& !jiagbg){
			int kk=0;
			for(int i=jiagrowInd+1;i<zhilyqBeans.size()+jiagrowInd+1;i++){//����Ҫ��
					rt.body.setCellValue(jiagrowInd+1+kk, 1, zhiltks[i-(jiagrowInd+1)].toString());
					rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 17);
			}
		}
		jiagrowInd=iShulRows+iZhilRows+9;
		
		if(jiagbg){//����Ǽ۸���
//			�۸����	
			jiag_table_index =jiagrowInd+1;
			jiag_table_rows =jiagBeans.size()+1;
			
			rt.body.setCellValue(jiagrowInd+1, 1,"ָ��");
			rt.body.setCellValue(jiagrowInd+1, 2,"����("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 4,"����("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 6,"�۸�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 8,"���㷽ʽ");
			rt.body.setCellValue(jiagrowInd+1, 10,"��Ȩ��ʽ");
			rt.body.setCellValue(jiagrowInd+1, 12,"���䷽ʽ");
			rt.body.setCellValue(jiagrowInd+1, 14,"�ⶥú��("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 16,"�˼�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 17,"�Ӽ�("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.mergeCell(jiagrowInd+1, 2, jiagrowInd+1, 3);
			rt.body.mergeCell(jiagrowInd+1, 4, jiagrowInd+1, 5);
			rt.body.mergeCell(jiagrowInd+1, 6, jiagrowInd+1, 7);
			rt.body.mergeCell(jiagrowInd+1, 8, jiagrowInd+1, 9);
			rt.body.mergeCell(jiagrowInd+1, 10, jiagrowInd+1, 11);
			rt.body.mergeCell(jiagrowInd+1, 12, jiagrowInd+1, 13);
			rt.body.mergeCell(jiagrowInd+1, 14, jiagrowInd+1, 15);
			rt.body.mergeCell(jiagrowInd+1, 16, jiagrowInd+1, 17);
//			rt.body.setCells(7,1,7,17,Table.PER_BORDER_TOP,2);
//			rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
//			rt.body.setCells(7,17,iShulRows+8,17,Table.PER_BORDER_RIGHT,2);
//			rt.body.setCells(iShulRows+8,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,2);
//			rt.body.setCellBorderRight(7,6,2);
			
			if(this.isTijsh()){
				int kk=0;
				jiagrowInd++;
				for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
					
					
					String zhibb=((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZhibb_id();
					String xiax=String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getXiax());
					String shangx= String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getShangx());
					String jij=String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getJij());
					String hetjsfsb=((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsfsb_id();
					String hetjsxsb=((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsxsb_id();
					String yunsfsb=((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunsfsb_id();
					String zuigmj=String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZuigmj());
					String yunj=String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunj());
					String fengsjj=String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getFengsjj());
					
					boolean t=false;
					for(int m=0;m<jiagBeansMB.size();m++){
						
						if(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).equals((jijbean)jiagBeansMB.get(m))){
							t=true;
							break;
						}
					}
					
					if(!t){

						 zhibb=Show(zhibb,null);
						 xiax=Show(xiax,null);
						 shangx= Show(shangx,null);
						 jij=Show(jij,null);
						 hetjsfsb=Show(hetjsfsb,null);
						 hetjsxsb=Show(hetjsxsb,null);
						 yunsfsb=Show(yunsfsb,null);
						 zuigmj=Show(zuigmj,null);
						 yunj=Show(yunj,null);
						 fengsjj=Show(fengsjj,null);
					}
					int j=1;
					rt.body.setCellValue(jiagrowInd+1+kk, 1, zhibb);
					rt.body.setCellValue(jiagrowInd+1+kk, 2, xiax);
					rt.body.setCellValue(jiagrowInd+1+kk, 4,shangx);
					rt.body.setCellValue(jiagrowInd+1+kk, 6, jij);
					rt.body.setCellValue(jiagrowInd+1+kk, 8, hetjsfsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 10, hetjsxsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 12,yunsfsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 14, zuigmj);
					rt.body.setCellValue(jiagrowInd+1+kk, 16, yunj);
					rt.body.setCellValue(jiagrowInd+1+kk, 17, fengsjj);
					
					rt.body.mergeCell(jiagrowInd+kk+1, 2, jiagrowInd+kk+1, 3);
					rt.body.mergeCell(jiagrowInd+kk+1, 4, jiagrowInd+kk+1, 5);
					rt.body.mergeCell(jiagrowInd+kk+1, 6, jiagrowInd+kk+1, 7);
					rt.body.mergeCell(jiagrowInd+kk+1, 8, jiagrowInd+kk+1, 9);
					rt.body.mergeCell(jiagrowInd+kk+1, 10, jiagrowInd+kk+1, 11);
					rt.body.mergeCell(jiagrowInd+kk+1, 12, jiagrowInd+kk+1, 13);
					rt.body.mergeCell(jiagrowInd+kk+1, 14, jiagrowInd+kk+1, 15);
					rt.body.mergeCell(jiagrowInd+kk+1, 16, jiagrowInd+kk+1, 17);
					kk+=1;
					j++;
				}
			}else{
				
				int kk=0;
				jiagrowInd++;
				for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
					rt.body.setCellValue(jiagrowInd+1+kk, 1, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZhibb_id());
					rt.body.setCellValue(jiagrowInd+1+kk, 2, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getXiax()));
					rt.body.setCellValue(jiagrowInd+1+kk, 4, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getShangx()));
					rt.body.setCellValue(jiagrowInd+1+kk, 6, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getJij()));
					rt.body.setCellValue(jiagrowInd+1+kk, 8, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsfsb_id());
					rt.body.setCellValue(jiagrowInd+1+kk, 10, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getHetjsxsb_id());
					rt.body.setCellValue(jiagrowInd+1+kk, 12, ((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunsfsb_id());
					rt.body.setCellValue(jiagrowInd+1+kk, 14, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getZuigmj()));
					rt.body.setCellValue(jiagrowInd+1+kk, 16, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getYunj()));
					rt.body.setCellValue(jiagrowInd+1+kk, 17, String.valueOf(((jijbean)jiagBeans.get(i-(jiagrowInd+1))).getFengsjj()));
					
					rt.body.mergeCell(jiagrowInd+kk+1, 2, jiagrowInd+kk+1, 3);
					rt.body.mergeCell(jiagrowInd+kk+1, 4, jiagrowInd+kk+1, 5);
					rt.body.mergeCell(jiagrowInd+kk+1, 6, jiagrowInd+kk+1, 7);
					rt.body.mergeCell(jiagrowInd+kk+1, 8, jiagrowInd+kk+1, 9);
					rt.body.mergeCell(jiagrowInd+kk+1, 10, jiagrowInd+kk+1, 11);
					rt.body.mergeCell(jiagrowInd+kk+1, 12, jiagrowInd+kk+1, 13);
					rt.body.mergeCell(jiagrowInd+kk+1, 14, jiagrowInd+kk+1, 15);
					rt.body.mergeCell(jiagrowInd+kk+1, 16, jiagrowInd+kk+1, 17);
					kk+=1;
				}
				
			}
		
			
		}else{
			
			if(this.isTijsh()){
				int kk=0;
				for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
					int c=jiagtks[i-(jiagrowInd+1)].size();
					String st1="";
					for (int j=0;j<c;j++){
						st1+=jiagtks[i-(jiagrowInd+1)].get(j).toString();
					}
						
					boolean t=false;
					for(int m=0;m<jiagBeansMB.size();m++){
						
						t=false;
						String st2="";
						for(int n=0;n<jiagtks1[m].size();n++){
							st2+=jiagtks1[m].get(n).toString();
						}
						
						String tems=Show(st1,st2);
						
						if(st1!=null && st1.equals(tems)){
							t=true;
							break;
						}
						
					}
					
					for (int j=0;j<c;j++){
						
						if(t){//����һ��
							rt.body.setCellValue(jiagrowInd+1+kk, 1,jiagtks[i-(jiagrowInd+1)].get(j).toString());
							rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 17);
						}else{
							rt.body.setCellValue(jiagrowInd+1+kk, 1, Show(jiagtks[i-(jiagrowInd+1)].get(j).toString(),null));
							rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 17);
						}
					}
				}
				
			}else{
				int kk=0;
				for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//����۸�
					int c=jiagtks[i-(jiagrowInd+1)].size();
					for (int j=0;j<c;j++){
						rt.body.setCellValue(jiagrowInd+1+kk, 1, jiagtks[i-(jiagrowInd+1)].get(j).toString());
						rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 17);
					}
				}
			}
			
		}
		
		
		//���ۿ���д���
		
		
		List zengkkBeansMB=null;
		List[] zengkktks1=null;
		
		if(this.isTijsh()){
//			���ۿ�
			buffer.setLength(0);
			buffer.append("select z.ID,zhibb.mingc zhibmc,zhibb.bianm,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" );
			buffer.append("KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" );
			buffer.append("decode(XIAOSCL,1,'��λ',2,'��ȥ',3,'��������',4,'��������(0.1)',5,'��������(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" );
			buffer.append(",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" );
			buffer.append("from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
			buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
			buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+) \n" );
			buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id in ( select  ht.hetb_mb_id from hetb ht where ht.id="+((Visit) getPage().getVisit()).getLong1()+") order by zhibb.mingc,z.xiax");
			
			zengkkBeansMB=new ArrayList();
			zengkkBeansMB.clear();
			ResultSet rmb4=con.getResultSet(buffer);
			while(rmb4.next()){
				Zengkkbean zengkkbean=new Zengkkbean();
				zengkkbean.setZHIBB_ID(rmb4.getString("zhibmc"));
				zengkkbean.setZhibb_bm(rmb4.getString("bianm"));
				zengkkbean.setTIAOJB_ID(rmb4.getString("tiaojmc"));
				zengkkbean.setSHANGX(rmb4.getDouble("SHANGX"));
				zengkkbean.setXIAX(rmb4.getDouble("XIAX"));
				zengkkbean.setDANWB_ID(rmb4.getString("zhibdwmc"));
				zengkkbean.setJIS(rmb4.getDouble("JIS"));
				zengkkbean.setJISDWID(rmb4.getString("jisdwmc"));
				zengkkbean.setKOUJ(rmb4.getDouble("KOUJ"));
				zengkkbean.setKOUJDW(rmb4.getString("koujdwmc"));
				zengkkbean.setZENGFJ(rmb4.getDouble("ZENGFJ"));
				zengkkbean.setZENGFJDW(rmb4.getString("zengfjdwmc"));
				zengkkbean.setXIAOSCL(rmb4.getString("XIAOSCL"));
				zengkkbean.setJIZZKJ(rmb4.getDouble("JIZZKJ"));
				zengkkbean.setJIZZB(rmb4.getDouble("JIZZB"));
				zengkkbean.setCANZXM(rmb4.getString("canzxmmc"));
				zengkkbean.setCANZXMDW(rmb4.getString("canzxmdwmc"));
				zengkkbean.setCANZSX(rmb4.getDouble("CANZSX"));
				zengkkbean.setCANZXX(rmb4.getDouble("CANZXX"));
				zengkkbean.setJIESXXB_ID(rmb4.getString("hetjsxsmc"));
				zengkkbean.setYUNSFSB_ID(rmb4.getString("yunsfsmc"));
				zengkkBeansMB.add(zengkkbean);
			}	
			
			
//			��ʽ������
			int zengkkRows1=0;
			int lz=zengkkBeansMB.size();
			String zengkktk1="";
			zengkktks1=new List[lz];
			for(int i=0;i<lz;i++){
				Zengkkbean zengkkbean=(Zengkkbean)zengkkBeansMB.get(i);
				bsh.set("��Ŀ",zengkkbean.getZHIBB_ID());
				bsh.set("��Ŀ����",zengkkbean.getZhibb_bm());
				bsh.set("����",zengkkbean.getTIAOJB_ID());
				bsh.set("����",zengkkbean.getXIAX());
				bsh.set("����",zengkkbean.getSHANGX());
				bsh.set("��λ",zengkkbean.getDANWB_ID());
				bsh.set("����",zengkkbean.getJIS());
				bsh.set("������λ",zengkkbean.getJISDWID());
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(getXiaosw());
//				DecimalFormat df = new DecimalFormat("0.0000"); 
				String kouj = df.format(zengkkbean.getKOUJ()); 
				bsh.set("�ۼ�",kouj);
//				bsh.set("�ۼ�",zengkkbean.getKOUJ());
				bsh.set("�ۼ۵�λ",zengkkbean.getKOUJDW());
				bsh.set("����",zengkkbean.getZENGFJ());
				bsh.set("���۵�λ",zengkkbean.getZENGFJDW());
				bsh.set("С������",zengkkbean.getXIAOSCL());
				bsh.set("��Ȩ��ʽ",zengkkbean.getJIESXXB_ID());
				bsh.set("���䷽ʽ",zengkkbean.getYUNSFSB_ID());
				bsh.set("��׼���ۼ�",zengkkbean.getJIZZKJ());
				bsh.set("��׼ָ��",zengkkbean.getJIZZB());
				bsh.set("������Ŀ",zengkkbean.getCANZXM());
				bsh.set("������Ŀ��λ",zengkkbean.getCANZXMDW());
				bsh.set("������Ŀ����",zengkkbean.getCANZSX());
				bsh.set("������Ŀ����",zengkkbean.getCANZXX());
				bsh.eval(gongs[2]);
				zengkktk1=bsh.get("����").toString();
				
				List zengkktkList=new ArrayList();
				zengkktkList=getRows(zengkktk1,117);
				zengkkRows1+=zengkktkList.size();
				zengkktks1[i]=zengkktkList;
			}

		}
		
		int zengkkrowInd=jiagrowInd+iJiagRows+1;//���ۿ�����
		rt.body.setRowHeight(zengkkrowInd,HANGJJ);
		rt.body.setCellVAlign(zengkkrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(zengkkrowInd, 1, "<font size=3><b>�����������ۿ���Ϣ</b></font>");
		rt.body.mergeCell(zengkkrowInd, 1, zengkkrowInd, 17);
		if(this.isTijsh()){
			
			int kk=0;
			for(int i=jijRows+zengkkrowInd+1;i<zengkkBeans.size()+jijRows+zengkkrowInd+1;i++){//�������ۿ�,��㣺�����ڲ�����
				
				boolean t=false;
				int c=zengkktks[i-(jijRows+zengkkrowInd+1)].size();
//				for(int m=0;m<zengkkBeansMB.size();m++){
//					if(((Zengkkbean)zengkkBeans.get(i-(jijRows+jiagrowInd+1))).equals((Zengkkbean)zengkkBeansMB.get(m))){
//						t=true;
//						break;
//					}
//				}
				String st1="";
				for(int j=0;j<c;j++){
					st1+=zengkktks[i-(jijRows+zengkkrowInd+1)].get(j).toString();
				}
				
				for(int m=0;m<zengkkBeansMB.size();m++){
					
					t=false;
					String st2="";
					for(int n=0;n<zengkktks1[m].size();n++){
						st2+=zengkktks1[m].get(n);
					}
					
					String tem=Show(st1,st2);
					
					if(st1!=null && st1.equals(tem)){
						t=true;
						break;
					}
				}
				
				for (int j=0;j<c;j++){
					
					if(t){//����һ��
						rt.body.setCellValue(zengkkrowInd+1+kk, 1, zengkktks[i-(zengkkrowInd+1)].get(j).toString());
					}else{
						rt.body.setCellValue(zengkkrowInd+1+kk, 1,Show( zengkktks[i-(zengkkrowInd+1)].get(j).toString(),null));
					}
					rt.body.mergeCell(zengkkrowInd+1+kk, 1, zengkkrowInd+1+kk++, 17);
				}
			}
			
		}else{
			int kk=0;
			for(int i=zengkkrowInd+1;i<zengkkBeans.size()+zengkkrowInd+1;i++){//�������ۿ�,��㣺�����ڲ�����
				int c=zengkktks[i-(zengkkrowInd+1)].size();
				for (int j=0;j<c;j++){
					rt.body.setCellValue(zengkkrowInd+1+kk, 1,zengkktks[i-(zengkkrowInd+1)].get(j).toString());
					rt.body.mergeCell(zengkkrowInd+1+kk, 1, zengkkrowInd+1+kk++, 17);
				}
			}
		}
		
		
		
		int qittkrow=iZengkkRows+zengkkrowInd+1;//��������������

		rt.body.setRowHeight(qittkrow,HANGJJ);
		rt.body.setCellVAlign(qittkrow, 1,Table.VALIGN_BOTTOM);
		
		if (isQit==0) {
			rt.body.setCellValue(qittkrow, 1, "<font size=3><b>�ġ���������</b></font>");
			rt.body.mergeCell(qittkrow, 1, qittkrow, 17);
//			rt.body.setCellValue(qittkrow+1, 1, wenzStr);//����������ʱһ��
			for(int i=qittkrow+1;i<qittkRows+qittkrow+1;i++){
				rt.body.setCellValue(i, 1, wenzList.get(i-(qittkrow+1)).toString());//strTiaok_jiag
				rt.body.mergeCell(i, 1, i, 17);
			}
		}else{
			//qittkrow--;
			for(int i=qittkrow;i<qittkRows+qittkrow;i++){
				rt.body.setCellValue(i, 1, wenzList.get(i-(qittkrow)).toString());//strTiaok_jiag
				rt.body.mergeCell(i, 1, i, 17);
			}
		}
		

		
//���ñ߿�	
		rt.body.setBorderNone();
		rt.body.setCells(1,1,iRows,17,Table.PER_BORDER_BOTTOM,0);
		rt.body.setCells(1,1,iRows,17,Table.PER_BORDER_RIGHT,0);
		//7�е�ShulRows+8��������
		rt.body.setCells(7,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,1);
		rt.body.setCells(7,1,iShulRows+8,17,Table.PER_BORDER_RIGHT,1);
		rt.body.setCells(7,1,7,17,Table.PER_BORDER_TOP,2);
		rt.body.setCellBorderRight(7, 5, 2);
		rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
		rt.body.setCells(7,17,iShulRows+8,17,Table.PER_BORDER_RIGHT,2);
		rt.body.setCells(iShulRows+8,1,iShulRows+8,17,Table.PER_BORDER_BOTTOM,2);
		rt.body.setCellBorderRight(7,6,2);
		
//		int jiag_table_index =jiagrowInd+1;
//		int jiag_table_rows =jiagBeans.size()+1;
		if(jiagbg){
			for(int i=0;i<jiag_table_rows;i++){
				rt.body.setCellBorderLeft(jiag_table_index+i,1,2);
				rt.body.setCells(jiag_table_index+i,16,jiag_table_index+i,16,Table.PER_BORDER_RIGHT,2);
				if(i==0){
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_TOP,2);
				}
				if(i+1==jiag_table_rows){//���
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_BOTTOM,2);
				}else{
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_BOTTOM,1);
				}
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,17,Table.PER_BORDER_RIGHT,1);
			}
		}
		//rt.body.setPageRows(32);
		/*rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 17, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);*/
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return rt.getAllPagesHtml(0);
		
	}
	//��ͬ�ļ�β��������������
	private String getJiew(){
		String beginTime="";
		String endTime="";
		JDBCcon con = new JDBCcon();
		ResultSetList list=con.getResultSetList("select to_char( QISRQ,'YYYY-MM-DD') QISRQ," +
				"to_char(GUOQRQ,'YYYY-MM-DD') GUOQRQ " +
				"from hetb where id="+((Visit) getPage().getVisit()).getLong1());
		while(list.next()){
			beginTime=list.getString("QISRQ");
			endTime=list.getString("GUOQRQ");
		}
		con.Close();
		Report rt = new Report();
		int[] ArrWidth=new int[]{100,500};
		rt.setBody(new Table(23,2));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		int HANGJJ=30;
		int beginCol=2;
		int endCol=4;
		
		rt.body.setFontSize(10);
		
		rt.body.setCellValue(1, 1, "<font size=3><b>����(�ҷ�)</b></font>");
		rt.body.mergeCell(1, 1, 1, 2);
		rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(2, 1, "��λ����(��):");
		rt.body.setCellValue(2, 2, bean.getGONGFDWMC());
		
		
		rt.body.setCellValue(3, 1, "��λ��ַ:");
		rt.body.setCellValue(3,  2, bean.getGONGFDWDZ());
		
		
		rt.body.setCellValue(4, 1, "����������:");
		rt.body.setCellValue(4,2, bean.getGONGFFDDBR());
		
		rt.body.setCellValue(5, 1, "ί�д�����:");
		rt.body.setCellValue(5,2, bean.getGONGFWTDLR());
		
		
		rt.body.setCellValue(6, 1, "�绰:");
		rt.body.setCellValue(6,2,bean.getGONGFDH());
		
		
		rt.body.setCellValue(7, 1, "�����:");
		rt.body.setCellValue(7,2, bean.getGONGFDBGH());
		
		
		rt.body.setCellValue(8, 1, "��������:");
		rt.body.setCellValue(8,2, bean.getGONGFKHYH());
		
		rt.body.setCellValue(9, 1, "�˺�:");
		rt.body.setCellValue(9,2, bean.getGONGFZH());
		
		
		rt.body.setCellValue(10, 1, "��������:");
		rt.body.setCellValue(10,2, bean.getGONGFYZBM());
		
		
		rt.body.setCellValue(11, 1, "˰��:");
		rt.body.setCellValue(11,2, bean.getGongfsh());
		
//����
		rt.body.setRowHeight(12,HANGJJ);
		rt.body.setCellValue(12, 1, "<font size=3><b>����(�׷�)</b></font>");
		rt.body.mergeCell(12, 1, 12, 2);
		rt.body.setCellAlign(12, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(13, 1, "��λ����(��):");
		rt.body.setCellValue(13,2, bean.getXUFDWMC());
		
		rt.body.setCellValue(14, 1, "��λ��ַ:");
		rt.body.setCellValue(14,2,bean.getXUFDWDZ());
		
		rt.body.setCellValue(15, 1, "����������:");
		rt.body.setCellValue(15,2, bean.getXUFFDDBR());
		
		rt.body.setCellValue(16, 1, "ί�д�����:");
		rt.body.setCellValue(16,2, bean.getXUFWTDLR());
		
		rt.body.setCellValue(17, 1, "�绰:");
		rt.body.setCellValue(17,2, bean.getXUFDH());
		
		rt.body.setCellValue(18, 1, "�����:");
		rt.body.setCellValue(18,2, bean.getXUFDBGH());

		rt.body.setCellValue(19, 1, "��������:");
		rt.body.setCellValue(19,2, bean.getXUFKHYH());

		rt.body.setCellValue(20, 1, "�˺�:");
		rt.body.setCellValue(20,2, bean.getXUFZH());

		rt.body.setCellValue(21, 1, "��������:");
		rt.body.setCellValue(21,2, bean.getXUFYZBM());

		rt.body.setCellValue(22, 1, "˰��:");
		rt.body.setCellValue(22,2, bean.getXufsh());

		
		for(int i=1;i<=23;i++){
			if(i==12||i==1){
				rt.body.setCellBorder(i, 1, 0, 0, 0, 0);
				continue;
			}
			rt.body.setCellBorder(i, 1, 0, 0, 0, 0);
			rt.body.setCellBorder(i, 2, 0, 0, 0, 1);
			rt.body.setCellAlign(i, 1, Table.ALIGN_RIGHT);
		}

		rt.body.setBorderNone();
		rt.body.setRowHeight(23,HANGJJ);
		rt.body.setCellVAlign(23, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(23, 1, "<font size=3><b>��Ч����:"+beginTime+"��"+endTime+"</b></font>");
		rt.body.mergeCell(23, 1, 23, 2);
		rt.body.setCellAlign(23, 1, Table.ALIGN_LEFT);
		
		
		/*_CurrentPage = 1;
		//_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}*/

		if(MainGlobal.getXitxx_item("��ͬ", "��ͬ��ӡ�Ƿ��ҳ��ʾ����˫����Ϣ", "0", "��").equals("��")){
			_AllPages =_AllPages+rt.body.getPages();
			return rt.getAllPagesHtml(1);
		}else{
			_AllPages =_AllPages;
			return rt.getAllPagesHtml(0);
		}
			
	}

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

	
	private boolean tijsh;//�Ƿ����  �ύ����������˹���
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='��ͬģ���ύ���' and leib='��ͬģ��' and zhi='��' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			visit.setActivePageName(getPageName().toString());
			visit.setList7(null);
			visit.setList8(null);
			visit.setList9(null);
			visit.setList10(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
		}
		if (cycle.getRequestContext().getParameter("hetb_id") != null&&!cycle.getRequestContext().getParameter("hetb_id").equals("-1")) {
			visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetb_id")));
			visit.setString4(cycle.getRequestContext().getParameter("leix"));//����  leix
		}else{
			blnIsBegin = false;
			return;
		}
		blnIsBegin = true;
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}
//	��ʽ���������Ӳ�س����Զ������������� ����ʾ������Ϊ
	/**
	 * private String[] getRows(String strValue,int rowSize);
	 */
	private  List getRows(String strValue,int rowSize){
	String[] tmp = strValue.split("\n");
	List result=new ArrayList();
	String TemChars="";
	String TemChar="";
	int j=0;
	int c=0;
	byte[] b=new byte[4];
	try{
		for(int i=0;i<tmp.length;i++){
			if(tmp[i].length()*2<rowSize){//�����Ӳ������ַ�����涨�ַ��Ƚϣ����С���򲻱�Ҫ����
				result.add(tmp[i]);
				continue;
			}else{//���Ӳ������ַ�����涨�ַ����Ƚϣ����������Ҫ����
				for(int k=0;k<tmp[i].length();k++){
					if(c<rowSize){//С�ڹ涨�ַ���ʱ�����ۼ�
						TemChar=tmp[i].substring(k, k+1);
						TemChars+=TemChar;
//						b=TemChar.getBytes("unicode");
//						if(b[3]==-1){//ȫ��
//							c+=2;
//						}else{
//							c++;
//						}
						if(isLetter(TemChar.toCharArray()[0])){
							c++;
						}else{
							c+=2;
						}
					}else{
						result.add(TemChars);
						TemChars="";
						TemChar="";
						c=0;
						k--;
					}
				}
				if(!TemChars.equals("\r")){
					result.add(TemChars);
				}
				TemChars="";
				TemChar="";
				c=0;
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return result;
}
	 public  boolean isLetter(char c) {
	        int k = 0x80;
	        return c / k == 0 ? true : false;
	 }
}