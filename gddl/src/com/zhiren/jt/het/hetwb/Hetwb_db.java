package com.zhiren.jt.het.hetwb;
//作者：梁丽丽
/*
 * 描述：比较合同对比显示，合同的数量，质量，价格，增扣款，
 * 其他条款和模板不一样的地方就用红色显示出来
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import bsh.Interpreter;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.sun.tools.jdi.DoubleValueImpl;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
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
/*修改时间：2010-01-27
  修改人：liangll
  修该内容：合同查询其它条款要分开扩充字段，要按照电厂实际合同中的条款进行文字条款的输出，
            其它条款要求一条一条地与模块进行比较，如果有不同的地方显示红色
*/
public class Hetwb_db extends BasePage {

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

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
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

	private String REPORT_NAME_HETDY = "Hetdy";// 

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		Visit visit=(Visit)this.getPage().getVisit();
		
		return getHetdy();
		
	}
   	
	
	private int getXiaosw(){
		Visit visit=(Visit)this.getPage().getVisit();
		int xsw = 3;
		xsw = Integer.parseInt(MainGlobal.getXitxx_item("合同", "扣价和增付价字段要保留的小数位", String.valueOf(visit.getDiancxxb_id()), "3"));
		return xsw;
	}
	/**
	 * 
	 * @param m		模板的条款数组
	 * @param h		合同的条款数组
	 * @return		比较后的本条款数组
	 */
	private String[] CompareArr(String[] m, String[] h) {
		String[] resarr = new String[h.length];
		for (int i = 0; i < h.length; i++) {
			if (i+1>h.length||h[i] == null) {
				resarr[i] = null;
				continue;
			}

			if (i+1>m.length||m[i] == null) {
				resarr[i] = "<font color='red'>" + h[i] + "</font>";
				continue;
			}

			String cstr_h = getContent(h[i]);
			String cstr_m = getContent(m[i]);
			if (cstr_h.equals(cstr_m)) {
				resarr[i] = h[i];
			} else {
				resarr[i] = "<font color='red'>" + h[i] + "</font>";
			}
		}
		return resarr;
	}
	/**
	 * 
	 * @param str	合同其他条款字符串
	 * @return		通过split"："得出的：后面的内容
	 */
	private String getContent(String str) {
		String[] contentArr = str.split("：");
		if (contentArr.length > 1) {
			return contentArr[1];
		} else {
			return str;
		}
	}
	private String Show(String h,String m){//判断合同内容 和 模板内容 是否一致  
		if(h==null){
			return "";
		}
		if(m==null){
			return "<font color='red'>"+h+"</font>";
		}
		if(!h.equals(m)){
			return "<font color='red'>"+h+"</font>";
		}
		return h;
	}
	
	private String getHetdy() {
		
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String[] gongs=new String[3];
		ResultSetList rs1=null;
		ResultSet rec=null;
		try{

			String sql="select id\n" +
			"from (\n" + 
			"select id,leix,decode(mingc,'质量条款文字分析',1,'价格条款文字分析',2,3)xuh,mingc\n" + 
			"from gongsb\n" + 
			"where gongsb.zhuangt=1 and leix='合同'\n" + 
			")\n" + 
			"order by xuh";
			ResultSetList rs0=con.getResultSetList(sql);
			DataBassUtil clob=new DataBassUtil();
			int k=0;
			while(rs0.next()){
				//gongs[k++]=rs0.getString(1);
				gongs[k++]=clob.getClob("gongsb", "gongs", rs0.getLong(0));
				
			}
//合同
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
		buffer.append("       to_char(QISRQ,'YYYY-MM-DD')QISRQ,\n" );
		buffer.append("       to_char(GUOQRQ,'YYYY-MM-DD')GUOQRQ,\n" );
		buffer.append("       jihkjb.mingc jihkjmc,\n" );
		buffer.append("       diancxxb_id,\n" );
		buffer.append("       hetb_mb_id,\n" );
		buffer.append("       meikmcs\n" );
		buffer.append("  from hetb,jihkjb\n" );
		buffer.append(" where hetb.jihkjb_id=jihkjb.id and hetb.ID = "+((Visit) getPage().getVisit()).getLong1());
		Hetxxbean bean=new Hetxxbean();
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
		
//数量(合同部分)
		if(((Visit) getPage().getVisit()).getList7()==null){
			((Visit) getPage().getVisit()).setList7(new ArrayList());
		}
	
		List shulBeans=((Visit) getPage().getVisit()).getList7();	// shulBeans 存储合同中的数量
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
		rs1=con.getResultSetList(buffer.toString());
	
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

//质量
		buffer.setLength(0);
		buffer.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
		buffer.append("from hetzlb,zhibb,tiaojb,danwb\n" );
		buffer.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by hetzlb.id");
		if(((Visit) getPage().getVisit()).getList8()==null){
			((Visit) getPage().getVisit()).setList8(new ArrayList());
		}
		List zhilyqBeans=((Visit) getPage().getVisit()).getList8();
		zhilyqBeans.clear();
		ResultSetList rs2=con.getResultSetList(buffer.toString());


		while(rs2.next()){
			Zhilyqbean zhilyqbean=new Zhilyqbean();
			zhilyqbean.setMingc(rs2.getString("zhib"));
			zhilyqbean.setTiaoj(rs2.getString("tiaoj"));
			zhilyqbean.setShangx(rs2.getDouble("shangx"));
			zhilyqbean.setXiax(rs2.getDouble("xiax"));
			zhilyqbean.setDanw(rs2.getString("danw"));
			zhilyqBeans.add(zhilyqbean);
			
		}
	
		
		//格式化条款
		int l=zhilyqBeans.size();
		String[] zhiltks=new String[l];
		String zhiltk="";
		Interpreter bsh= new Interpreter();
		
		for(int i=0;i<l;i++){
			Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeans.get(i);
			bsh.set("项目",shulbean.getMingc() );
			bsh.set("条件", shulbean.getTiaoj());
			bsh.set("下限",shulbean.getXiax());
			bsh.set("上限",shulbean.getShangx()  );
			bsh.set("单位",shulbean.getDanw() );
			bsh.eval(gongs[0]);
			zhiltk=bsh.get("文字").toString();
			zhiltks[i]=zhiltk;
		}
		 
//价格
		buffer.setLength(0);
		buffer.append("select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" );
		buffer.append("hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是','否')YINGDKF,\n" );
		buffer.append("yunsfsb.mingc yunsfsmc,ZUIGMJ,zuigmjdw.mingc zuigmjdw,fengsjj,fengsjjdw.mingc fengsjjdw,jijlx\n" );
		buffer.append("from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,danwb zuigmjdw,danwb fengsjjdw,yunsfsb\n" );
		buffer.append("where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" );
		buffer.append("and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" );
		buffer.append("and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.fengsjjdw = fengsjjdw.id(+) and hetjgb.zuigmjdw = zuigmjdw.id(+) and hetjgb.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,hetjgb.xiax");
		if(((Visit) getPage().getVisit()).getList9()==null){
			((Visit) getPage().getVisit()).setList9(new ArrayList());
		}
		List jiagBeans=((Visit) getPage().getVisit()).getList9();
		jiagBeans.clear();
		ResultSetList rs3=con.getResultSetList(buffer.toString());

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
			jibean.setZuigmjdw(rs3.getString("zuigmjdw"));
			jibean.setFengsjj(rs3.getDouble("fengsjj"));
			jibean.setFengsjjdw(rs3.getString("fengsjjdw"));
			jibean.setJijlx(rs3.getString("jijlx"));
			
			jiagBeans.add(jibean);
		}
	
//		格式化条款
		int jijRows=0;
		l=jiagBeans.size();
		List[] jiagtks=new List[l];
		String jiagtk="";
		for(int i=0;i<l;i++){
			jijbean jigbean=(jijbean)jiagBeans.get(i);
			bsh.set("项目",jigbean.getZhibb_id());
			bsh.set("条件",jigbean.getTiaojb_id());
			bsh.set("下限",jigbean.getXiax());
			bsh.set("上限",jigbean.getShangx());
			bsh.set("单位",jigbean.getDanwb_id());
			bsh.set("价格",jigbean.getJij());
			bsh.set("价格单位",jigbean.getJijdwid());
			bsh.set("结算方式",jigbean.getHetjsfsb_id());
			bsh.set("计价方式",jigbean.getHetjjfsb_id());
			bsh.set("加权方式",jigbean.getHetjsxsb_id());
			bsh.set("运价",jigbean.getYunj());
			bsh.set("运价单位",jigbean.getYunjdw_id());
			bsh.set("运输方式",jigbean.getYunsfsb_id());
			bsh.set("最高煤价",jigbean.getZuigmj());
			bsh.set("最高煤价单位", jigbean.getZuigmjdw());
			bsh.set("分公司加价",jigbean.getFengsjj());
			bsh.set("分公司加价单位",jigbean.getFengsjjdw());
			bsh.set("计价类型",jigbean.getJijlx());
			bsh.eval(gongs[1]);
			jiagtk=bsh.get("文字").toString();
			
			List jiagtkList=new ArrayList();
			jiagtkList=getRows(jiagtk,118);
			jijRows+=jiagtkList.size();
			jiagtks[i]=jiagtkList;
		}
//增扣款
		buffer.setLength(0);
		buffer.append("select z.ID,zhibb.mingc zhibmc,zhibb.bianm,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" );
		buffer.append("KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" );
		buffer.append("decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" );
		buffer.append(",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" );
		buffer.append("from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
		buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
		buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+) \n" );
		buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id="+((Visit) getPage().getVisit()).getLong1()+" order by zhibb.mingc,decode(tiaojb.mingc,'区间',z.xiax,'大于',z.xiax,'小于',z.shangx,'大于等于',z.xiax,'小于等于',z.shangx,z.xiax)");
		if(((Visit) getPage().getVisit()).getList10()==null){
			((Visit) getPage().getVisit()).setList10(new ArrayList());
		}
		List zengkkBeans=((Visit) getPage().getVisit()).getList10();
		zengkkBeans.clear();
		ResultSetList rs4=con.getResultSetList(buffer.toString());

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

//		格式化条款
		int zengkkRows=0;
		l=zengkkBeans.size();
		List[] zengkktks=new List[l];
		String zengkktk="";
		for(int i=0;i<l;i++){
			Zengkkbean zengkkbean=(Zengkkbean)zengkkBeans.get(i);
			bsh.set("项目",zengkkbean.getZHIBB_ID());
			bsh.set("项目编码",zengkkbean.getZhibb_bm());
			bsh.set("条件",zengkkbean.getTIAOJB_ID());
			bsh.set("下限",zengkkbean.getXIAX());
			bsh.set("上限",zengkkbean.getSHANGX());
			bsh.set("单位",zengkkbean.getDANWB_ID());
			bsh.set("基数",zengkkbean.getJIS());
			bsh.set("基数单位",zengkkbean.getJISDWID());
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(getXiaosw());
//			DecimalFormat df = new DecimalFormat("0.0000"); 
			String kouj = df.format(zengkkbean.getKOUJ()); 
			
			if (Double.parseDouble(kouj)==0){
				bsh.set("扣价",0);
			}else{
				bsh.set("扣价",kouj);
			}
//			bsh.set("扣价",zengkkbean.getKOUJ());
			bsh.set("扣价单位",zengkkbean.getKOUJDW());
			bsh.set("增价",zengkkbean.getZENGFJ());
			bsh.set("增价单位",zengkkbean.getZENGFJDW());
			bsh.set("小数处理",zengkkbean.getXIAOSCL());
			bsh.set("加权方式",zengkkbean.getJIESXXB_ID());
			bsh.set("运输方式",zengkkbean.getYUNSFSB_ID());
			bsh.set("基准增扣价",zengkkbean.getJIZZKJ());
			bsh.set("基准指标",zengkkbean.getJIZZB());
			bsh.set("参照项目",zengkkbean.getCANZXM());
			bsh.set("参照项目单位",zengkkbean.getCANZXMDW());
			bsh.set("参照项目上限",zengkkbean.getCANZSX());
			bsh.set("参照项目下限",zengkkbean.getCANZXX());
			bsh.eval(gongs[2]);
			zengkktk=bsh.get("文字").toString();
			
			List zengkktkList=new ArrayList();
			zengkktkList=getRows(zengkktk,118);
			zengkkRows+=zengkktkList.size();
			zengkktks[i]=zengkktkList;
		}
		
//文字及比较 处理方法：取出文字模板，合同中的其它内容
//			如果需要正确计算合同与模板间条款不一致，必须：
//			1、在每条记录后以句号回车进行结尾。
//			2、在每条记录文字说明前条款名称处以中文全角"："进行结尾。
		String wenzStr_h = "";
		String wenzStr_m = "";
		String[] wenzStrarr_h = null;
		String[] wenzStrarr_m = null;
//		合同其他条款
		buffer.setLength(0);
		buffer.append("select id,wenznr\n");
		buffer.append("from hetwzb\n" );
		buffer.append("where hetb_id="+((Visit) getPage().getVisit()).getLong1());
		ResultSetList rs5=con.getResultSetList(buffer.toString());
		if(rs5.next()){
			wenzStr_h = rs5.getString("wenznr");
			
		}
		rs5.close();
//		模板的其他条款
		buffer.setLength(0);
		buffer.append("select id,wenznr\n");
		buffer.append("from hetwzb\n" );
		buffer.append("where hetb_id in( select hetb_mb_id from hetb where id="+((Visit) getPage().getVisit()).getLong1()+")");
		ResultSetList rsm5=con.getResultSetList(buffer.toString());
		if(rsm5.next()){
			wenzStr_m = rsm5.getString("wenznr");
		}
		rsm5.close();
// 以"。\r\n"来将合同和模板的一整条记录分隔成一条一条的记录
		wenzStrarr_h = wenzStr_h.split("。\r\n");
		wenzStrarr_m = wenzStr_m.split("。\r\n");
		String[] wenz = CompareArr(wenzStrarr_m, wenzStrarr_h);
		List wenzList = getRows(wenzStr_h, 118);
		int qittkRows=wenzList.size();
		
		//////////////////////////////////////////////////
//绑定内容、强行合并进行布局
		String[][] ArrHeader = new String[1][18];
		int ArrWidth[]=new int[] {104,32,32,42,42,45,35,35,35,35,35,35,35,35,35,35,35,35};
		rt.setTitle("煤 炭 购 销 合 同"+"("+jihkjmc+")", ArrWidth);
		int iRows=0;
		int iHeadRows=4;
		int iShulRows=shulBeans.size();
		int iZhilRows=zhilyqBeans.size(); 
		int iJiagRows=jijRows+zengkkRows;//zengkkBeans.size();jiagBeans.size()+
		int iQitRows=qittkRows;
		int iGongxfRows=11;
		int iYouxq=1;
		final int HANGJJ=30;//行间距
		boolean jiagbg =false;
		int jiag_biaot=0;
		if(MainGlobal.getXitxx_item("合同查询", "价格表格", "0", "否").equals("是")){
			jiagbg=true;
			jiag_biaot++;//+1价格标题
			jijRows=jiagBeans.size();
			iJiagRows=jijRows+zengkkRows;
		}
		iRows=iHeadRows+iShulRows+3+iZhilRows+1+iJiagRows+1+iQitRows+1+iGongxfRows+iYouxq+2+jiag_biaot;
		if(fahr==""){
			iRows-=1;
		}
		rt.setBody(new Table(iRows,18));
		rt.body.setWidth(ArrWidth);
		
		rt.body.setCellValue(1, 1, "<font size=2><b>出卖人:</b></font>"+bean.getGONGFDWMC());
		rt.body.mergeCell(1, 1, 1, 10);
		rt.body.setCellValue(1, 11,  "<font size=2><b>合同编号:</b></font>"+bean.getHetbh());
		rt.body.mergeCell(1, 11, 1, 18);
		
		rt.body.setCellValue(2, 11, "<b><font size=2>签订日期:</b></font>"+qiandrq);
		rt.body.mergeCell(2, 11, 2, 18);
		
		rt.body.setCellValue(3, 1, "<font size=2><b>买受人:</b></font>"+bean.getXUFDWMC());
		rt.body.mergeCell(3, 1, 3, 10);
		rt.body.mergeCell(3, 11, 3, 18);
		rt.body.setCellValue(3, 11, "<font size=2><b>签订地点:</b></font>"+bean.getQianddd());
		rt.body.setRowHeight(3,HANGJJ);
		rt.body.setCellVAlign(3, 1,Table.VALIGN_TOP);
		rt.body.setCellVAlign(3, 11,Table.VALIGN_TOP);
		
		rt.body.setCellValue(4, 1, "&nbsp;&nbsp;&nbsp;&nbsp;依据《中华人民共和国合同法》并遵守公平、公正的原则，经双方协商一致，达成如下协议，签订合同约束双方共同履行。");
		rt.body.mergeCell(4, 1, 4, 18);
		rt.body.setCellValue(5, 1, "");
		rt.body.mergeCell(5, 1, 5, 18);
		
		rt.body.setCellValue(6, 1, "<font size=2><b>一、煤炭品种、计量单位、订购数量</b></font>");
		rt.body.setRowHeight(6,30);
		rt.body.setCellVAlign(6, 1,Table.VALIGN_BOTTOM);
		rt.body.mergeCell(6, 1, 6, 18);
		
		rt.body.setCellValue(7, 1, "收货人");
		rt.body.setCellValue(8, 1, "");
		rt.body.mergeCell(7, 1, 8, 1);
		rt.body.setCellValue(7, 2, "煤种");
		rt.body.setCellValue(8, 2, "");
		rt.body.mergeCell(7, 2, 8, 2);
		rt.body.setCellValue(7, 3, "运输方式");
		rt.body.setCellValue(8, 3, "");
		rt.body.mergeCell(7, 3, 8, 3);
		rt.body.setCellValue(7, 4, "发站");
		rt.body.setCellValue(8, 4, "");
		rt.body.mergeCell(7, 4, 8, 4);
		rt.body.setCellValue(7, 5, "到站");
		rt.body.setCellValue(8, 5, "");
		rt.body.mergeCell(7, 5, 8, 5);
		
		rt.body.setCellValue(7, 6, "数量(万吨)");
		rt.body.setCellAlign(7, 6, Table.ALIGN_CENTER);
		rt.body.mergeCell(7, 6, 7, 18);
		rt.body.setCellValue(8, 6, "合计");
		rt.body.setCellValue(8, 7, "一月");
		rt.body.setCellValue(8, 8, "二月");
		rt.body.setCellValue(8, 9, "三月");
		rt.body.setCellValue(8, 10, "四月");
		rt.body.setCellValue(8, 11, "五月");
		rt.body.setCellValue(8, 12, "六月");
		rt.body.setCellValue(8, 13, "七月");
		rt.body.setCellValue(8, 14, "八月");
		rt.body.setCellValue(8, 15, "九月");
		rt.body.setCellValue(8, 16, "十月");
		rt.body.setCellValue(8, 17, "十一月");
		rt.body.setCellValue(8, 18, "十二月");
		
//		模板_begin	
		List sl =new ArrayList();//模板的数量信息
		   
			StringBuffer buffer1 = new StringBuffer("");
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
			

			sl.clear();
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
			
				for(int i=0;i<shulBeans.size();i++){  //合同数量遍历
					
					String shouhr=((Fahxxbean) shulBeans.get(i)).getShouhr();
					String pinz=((Fahxxbean) shulBeans.get(i)).getPinz();
					String yunsfs=((Fahxxbean) shulBeans.get(i)).getYunsfs();
					String faz=((Fahxxbean) shulBeans.get(i)).getFaz();
					String daoz=((Fahxxbean) shulBeans.get(i)).getDaoz();
					String hej=((Fahxxbean) shulBeans.get(i)).getHej()+"";
					String y1=((Fahxxbean) shulBeans.get(i)).getY1()+"";
					String y2=((Fahxxbean) shulBeans.get(i)).getY2()+"";
					String y3=((Fahxxbean) shulBeans.get(i)).getY3()+"";
					String y4=((Fahxxbean) shulBeans.get(i)).getY4()+"";
					String y5=((Fahxxbean) shulBeans.get(i)).getY5()+"";
					String y6=((Fahxxbean) shulBeans.get(i)).getY6()+"";
					String y7=((Fahxxbean) shulBeans.get(i)).getY7()+"";
					String y8=((Fahxxbean) shulBeans.get(i)).getY8()+"";
					String y9=((Fahxxbean) shulBeans.get(i)).getY9()+"";
					String y10=((Fahxxbean) shulBeans.get(i)).getY10()+"";
					String y11=((Fahxxbean) shulBeans.get(i)).getY11()+"";
					String y12=((Fahxxbean) shulBeans.get(i)).getY12()+"";
					
				
					if(sl.size()>i){
//						遍历模板
						
						y1=Show(y1,String.valueOf(((Fahxxbean) sl.get(i)).getY1()));	//比较方法
						y2=Show(y2,String.valueOf(((Fahxxbean) sl.get(i)).getY2()));	//比较方法
						y3=Show(y3,String.valueOf(((Fahxxbean) sl.get(i)).getY3()));  
						y4=Show(y4,String.valueOf(((Fahxxbean) sl.get(i)).getY4()));
						y5=Show(y5,String.valueOf(((Fahxxbean) sl.get(i)).getY5()));
						y6=Show(y6,String.valueOf(((Fahxxbean) sl.get(i)).getY6()));
						y7=Show(y7,String.valueOf(((Fahxxbean) sl.get(i)).getY7()));
						y8=Show(y8,String.valueOf(((Fahxxbean) sl.get(i)).getY8()));
						y9=Show(y9,String.valueOf(((Fahxxbean) sl.get(i)).getY9()));
						y10=Show(y10,String.valueOf(((Fahxxbean) sl.get(i)).getY10()));
						y11=Show(y11,String.valueOf(((Fahxxbean) sl.get(i)).getY11()));
						y12=Show(y12,String.valueOf(((Fahxxbean) sl.get(i)).getY12()));
						pinz=Show(pinz,((Fahxxbean) sl.get(i)).getPinz());
						yunsfs=Show(yunsfs,((Fahxxbean) sl.get(i)).getYunsfs());
						
					}else{  
						
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
					rt.body.setCellValue(i+9, 1,shouhr);//收货人
					rt.body.setCellValue(i+9, 2,pinz);//品种
					rt.body.setCellValue(i+9, 3,yunsfs);//运输方式
					rt.body.setCellValue(i+9, 4,faz);//发站
					rt.body.setCellValue(i+9, 5,daoz);//到站
					rt.body.setCellValue(i+9, 6,hej);//合计
					rt.body.setCellValue(i+9, 7,y1);//一月
					rt.body.setCellValue(i+9, 8,y2);//二月
					rt.body.setCellValue(i+9, 9,y3);//三月
					rt.body.setCellValue(i+9, 10,y4);//四月
					rt.body.setCellValue(i+9, 11,y5);//五月
					rt.body.setCellValue(i+9, 12,y6);//六月
					rt.body.setCellValue(i+9, 13,y7);//七月
					rt.body.setCellValue(i+9, 14,y8);//八月
					rt.body.setCellValue(i+9, 15,y9);//九月
					rt.body.setCellValue(i+9, 16,y10);//十月
					rt.body.setCellValue(i+9, 17,y11);//十一月
					rt.body.setCellValue(i+9, 18,y12);//十二月

					}
			
		int fahrow=0;//发货行索引
		if(fahr!=""){
			rt.body.setCellValue(iShulRows+9, 1, "发货煤矿："+fahr);
			rt.body.mergeCell(iShulRows+9, 1, iShulRows+9, 18);
			fahrow++;
		}
		int zhilrowInd=fahrow+iShulRows+9;//质量行索引
		rt.body.setRowHeight(zhilrowInd,HANGJJ);
		rt.body.setCellVAlign(zhilrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(zhilrowInd, 1, "<font size=2><b>二、煤炭质量");
		rt.body.mergeCell(zhilrowInd, 1, zhilrowInd, 18);
		
	
//      质量
			buffer1.setLength(0);
			buffer1.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
			buffer1.append("from hetzlb,zhibb,tiaojb,danwb\n" );
			buffer1.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id  in (select hetb_mb_id from hetb  where id="+((Visit) getPage().getVisit()).getLong1()+") order by hetzlb.id");
			
			List zhilyqBeansMB=new ArrayList();
			zhilyqBeansMB.clear();
			ResultSetList rmb2=con.getResultSetList(buffer1.toString());
		
			while(rmb2.next()){
				Zhilyqbean zhilyqbean=new Zhilyqbean();
				zhilyqbean.setMingc(rmb2.getString("zhib"));
				zhilyqbean.setTiaoj(rmb2.getString("tiaoj"));
				zhilyqbean.setShangx(rmb2.getDouble("shangx"));
				zhilyqbean.setXiax(rmb2.getDouble("xiax"));
				zhilyqbean.setDanw(rmb2.getString("danw"));
				zhilyqBeansMB.add(zhilyqbean);
				
			}

			//格式化条款
			int lk=zhilyqBeansMB.size();
			String[] zhiltksMB=new String[lk];
			String zhiltkMB="";
			Interpreter bshMB= new Interpreter();
			
			for(int i=0;i<lk;i++){
				Zhilyqbean shulbean=(Zhilyqbean)zhilyqBeansMB.get(i);
				bshMB.set("项目",shulbean.getMingc() );
				bshMB.set("条件", shulbean.getTiaoj());
				bshMB.set("下限",shulbean.getXiax());
				bshMB.set("上限",shulbean.getShangx()  );
				bshMB.set("单位",shulbean.getDanw() );
				bshMB.eval(gongs[0]);
				zhiltkMB=bshMB.get("文字").toString();
				zhiltksMB[i]=zhiltkMB;
			}
			int mm=0;
			for(int i=zhilrowInd+1;i<iZhilRows+zhilrowInd+1;i++){//开始比较内容是否一致
			
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
				
		
				mm+=1;
				rt.body.setCellValue(i, 1,va_t);
				rt.body.mergeCell(i, 1, i, 18);
			}
			
			
		List jiagBeansMB=new ArrayList();
		List[] jiagtks1=null;
       
//		价格
			buffer1.setLength(0);
			buffer1.append("select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,\n" );
			buffer1.append("hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是','否')YINGDKF,\n" );
			buffer1.append("yunsfsb.mingc yunsfsmc,ZUIGMJ,zuigmjdw.mingc zuigmjdw,fengsjj,fengsjjdw.mingc fengsjjdw,jijlx\n" );
			buffer1.append("from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,danwb zuigmjdw,danwb fengsjjdw,yunsfsb\n" );
			buffer1.append("where hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n" );
			buffer1.append("and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n" );
			buffer1.append("and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.zuigmjdw = zuigmjdw.id(+) and hetjgb.fengsjjdw = fengsjjdw.id(+) and hetjgb.hetb_id in ( select  ht.hetb_mb_id from hetb ht where ht.id="+((Visit) getPage().getVisit()).getLong1()+") order by zhibb.mingc,hetjgb.xiax");
			
			jiagBeansMB.clear();
			ResultSetList rmb3=con.getResultSetList(buffer1.toString());
		
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
				jibean.setZuigmjdw(rmb3.getString("zuigmjdw"));
				jibean.setFengsjj(rmb3.getDouble("fengsjj"));
				jibean.setFengsjjdw(rmb3.getString("fengsjjdw"));
				jibean.setJijlx(rmb3.getString("jijlx"));
				
				jiagBeansMB.add(jibean);
			}
	
//			格式化条款
			int jijRows1=0;
			int lm=jiagBeansMB.size();
			jiagtks1=new List[lm];
			String jiagtk1="";
			for(int i=0;i<lm;i++){
				jijbean jigbean=(jijbean)jiagBeansMB.get(i);
				bsh.set("项目",jigbean.getZhibb_id());
				bsh.set("条件",jigbean.getTiaojb_id());
				bsh.set("下限",jigbean.getXiax());
				bsh.set("上限",jigbean.getShangx());
				bsh.set("单位",jigbean.getDanwb_id());
				bsh.set("价格",jigbean.getJij());
				bsh.set("价格单位",jigbean.getJijdwid());
				bsh.set("结算方式",jigbean.getHetjsfsb_id());
				bsh.set("计价方式",jigbean.getHetjjfsb_id());
				bsh.set("加权方式",jigbean.getHetjsxsb_id());
				bsh.set("运价",jigbean.getYunj());
				bsh.set("运价单位",jigbean.getYunjdw_id());
				bsh.set("运输方式",jigbean.getYunsfsb_id());
				bsh.set("最高煤价",jigbean.getZuigmj());
				bsh.set("最高煤价单位",jigbean.getZuigmjdw());
				bsh.set("分公司加价",jigbean.getFengsjj());
				bsh.set("分公司加价单位",jigbean.getFengsjjdw());
				bsh.set("计价类型",jigbean.getJijlx());
				bsh.eval(gongs[1]);
				jiagtk1=bsh.get("文字").toString();
				
				List jiagtkList=new ArrayList();
				jiagtkList=getRows(jiagtk1,118);
				jijRows1+=jiagtkList.size();
				jiagtks1[i]=jiagtkList;
			}
		int jiagrowInd=iZhilRows+zhilrowInd+1;//价格索引
		rt.body.setRowHeight(jiagrowInd,HANGJJ);
		rt.body.setCellVAlign(jiagrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(jiagrowInd, 1, "<font size=2><b>三、结算价格</b></font>");
		rt.body.mergeCell(jiagrowInd, 1, jiagrowInd, 18);
		int jiag_table_index=0;
		int jiag_table_rows=0;
		if(jiagbg){//如果是价格表格
//			价格标题	
			jiag_table_index =jiagrowInd+1;
			jiag_table_rows =jiagBeans.size()+1;
			
			rt.body.setCellValue(jiagrowInd+1, 1,"指标");
			rt.body.setCellValue(jiagrowInd+1, 2,"下限("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 4,"上限("+((jijbean)jiagBeans.get(0)).getDanwb_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 6,"价格("+((jijbean)jiagBeans.get(0)).getJijdwid()+")");
			rt.body.setCellValue(jiagrowInd+1, 8,"结算方式");
			rt.body.setCellValue(jiagrowInd+1, 10,"加权方式");
			rt.body.setCellValue(jiagrowInd+1, 12,"运输方式");
			rt.body.setCellValue(jiagrowInd+1, 14,"封顶煤价("+((jijbean)jiagBeans.get(0)).getZuigmjdw()+")");
			rt.body.setCellValue(jiagrowInd+1, 16,"运价("+((jijbean)jiagBeans.get(0)).getYunjdw_id()+")");
			rt.body.setCellValue(jiagrowInd+1, 18,"加价("+((jijbean)jiagBeans.get(0)).getFengsjjdw()+")");
			rt.body.mergeCell(jiagrowInd+1, 2, jiagrowInd+1, 3);
			rt.body.mergeCell(jiagrowInd+1, 4, jiagrowInd+1, 5);
			rt.body.mergeCell(jiagrowInd+1, 6, jiagrowInd+1, 7);
			rt.body.mergeCell(jiagrowInd+1, 8, jiagrowInd+1, 9);
			rt.body.mergeCell(jiagrowInd+1, 10, jiagrowInd+1, 11);
			rt.body.mergeCell(jiagrowInd+1, 12, jiagrowInd+1, 13);
			rt.body.mergeCell(jiagrowInd+1, 14, jiagrowInd+1, 15);
			rt.body.mergeCell(jiagrowInd+1, 16, jiagrowInd+1, 17);
//			rt.body.setCells(7,1,7,18,Table.PER_BORDER_TOP,2);
//			rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
//			rt.body.setCells(7,18,iShulRows+8,18,Table.PER_BORDER_RIGHT,2);
//			rt.body.setCells(iShulRows+8,1,iShulRows+8,18,Table.PER_BORDER_BOTTOM,2);
//			rt.body.setCellBorderRight(7,6,2);
	
			int kk=0;
				jiagrowInd++;
				for(int i=0;i<jiagBeans.size();i++){//结算价格 合同的
				
					String zhibb=((jijbean)jiagBeans.get(i)).getZhibb_id();
					String xiax=String.valueOf(((jijbean)jiagBeans.get(i)).getXiax());
					String shangx= String.valueOf(((jijbean)jiagBeans.get(i)).getShangx());
					String jij=String.valueOf(((jijbean)jiagBeans.get(i)).getJij());
					String hetjsfsb=((jijbean)jiagBeans.get(i)).getHetjsfsb_id();
					String hetjsxsb=((jijbean)jiagBeans.get(i)).getHetjsxsb_id();
					String yunsfsb=((jijbean)jiagBeans.get(i)).getYunsfsb_id();
					String zuigmj=String.valueOf(((jijbean)jiagBeans.get(i)).getZuigmj());
					String yunj=String.valueOf(((jijbean)jiagBeans.get(i)).getYunj());
					String fengsjj=String.valueOf(((jijbean)jiagBeans.get(i)).getFengsjj());

					if(jiagBeansMB.size()>i){	//价格模板遍历
						
                   	    zhibb=Show(zhibb,((jijbean)jiagBeansMB.get(i)).getZhibb_id());
                        xiax=Show(xiax,String.valueOf(((jijbean)jiagBeansMB.get(i)).getXiax()));
                    	shangx=Show(shangx,String.valueOf(((jijbean) jiagBeansMB.get(i)).getShangx()));
                    	jij=Show(jij,String.valueOf(((jijbean)jiagBeansMB.get(i)).getJij()));
                    	hetjsfsb=Show(hetjsfsb,((jijbean)jiagBeansMB.get(i)).getHetjsfsb_id());
                    	hetjsxsb=Show(hetjsxsb,((jijbean)jiagBeansMB.get(i)).getHetjsxsb_id());
                    	yunsfsb=Show(yunsfsb,((jijbean)jiagBeansMB.get(i)).getYunsfsb_id());
                    	zuigmj=Show(zuigmj,String.valueOf(((jijbean)jiagBeansMB.get(i)).getZuigmj()));
                    	yunj=Show(yunj,String.valueOf(((jijbean)jiagBeansMB.get(i)).getYunj()));
                    	fengsjj=Show(fengsjj,String.valueOf(((jijbean)jiagBeansMB.get(i)).getFengsjj()));
                    
                    }
					else{
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
									
					rt.body.setCellValue(jiagrowInd+1+kk, 1, zhibb);
					rt.body.setCellValue(jiagrowInd+1+kk, 2, xiax);
					rt.body.setCellValue(jiagrowInd+1+kk, 4,shangx);
					rt.body.setCellValue(jiagrowInd+1+kk, 6, jij);
					rt.body.setCellValue(jiagrowInd+1+kk, 8, hetjsfsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 10, hetjsxsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 12,yunsfsb);
					rt.body.setCellValue(jiagrowInd+1+kk, 14, zuigmj);
					rt.body.setCellValue(jiagrowInd+1+kk, 16, yunj);
					rt.body.setCellValue(jiagrowInd+1+kk, 18, fengsjj);
					
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
			for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//合同结算价格
				int c=jiagtks[i-(jiagrowInd+1)].size();
				String st1="";
				for (int j=0;j<c;j++){
					st1+=jiagtks[i-(jiagrowInd+1)].get(j).toString();
				}
					
				boolean t=false;
				for(int m=0;m<jiagBeansMB.size();m++){//模板
					
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
					
					if(t){//内容一致
						rt.body.setCellValue(jiagrowInd+1+kk, 1, jiagtks[i-(jiagrowInd+1)].get(j).toString());
						rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 18);
					}else{
						rt.body.setCellValue(jiagrowInd+1+kk, 1, Show(jiagtks[i-(jiagrowInd+1)].get(j).toString(),null));
						rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 18);
					}
				}
				
			}
			
		}
		
		//增扣款进行处理
		List zengkkBeansMB=null;
		List[] zengkktks1=null;
		
//		增扣款
			buffer.setLength(0);
			buffer.append("select z.ID,zhibb.mingc zhibmc,zhibb.bianm,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n" );
			buffer.append("KOUJ,koujdw.mingc koujdwmc,ZENGFJ,zengfjdw.mingc zengfjdwmc,\n" );
			buffer.append("decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n" );
			buffer.append(",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ\n" );
			buffer.append("from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
			buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
			buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+) \n" );
			buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetb_id in ( select  ht.hetb_mb_id from hetb ht where ht.id="+((Visit) getPage().getVisit()).getLong1()+") order by zhibb.mingc,z.xiax");
			
			zengkkBeansMB=new ArrayList();
			zengkkBeansMB.clear();
			ResultSetList rmb4=con.getResultSetList(buffer.toString());
		
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
			
			
//			格式化条款
			int zengkkRows1=0;
			int lz=zengkkBeansMB.size();
			String zengkktk1="";
			zengkktks1=new List[lz];
			for(int i=0;i<lz;i++){
				Zengkkbean zengkkbean=(Zengkkbean)zengkkBeansMB.get(i);
				bsh.set("项目",zengkkbean.getZHIBB_ID());
				bsh.set("项目编码",zengkkbean.getZhibb_bm());
				bsh.set("条件",zengkkbean.getTIAOJB_ID());
				bsh.set("下限",zengkkbean.getXIAX());
				bsh.set("上限",zengkkbean.getSHANGX());
				bsh.set("单位",zengkkbean.getDANWB_ID());
				bsh.set("基数",zengkkbean.getJIS());
				bsh.set("基数单位",zengkkbean.getJISDWID());
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(getXiaosw());
//				DecimalFormat df = new DecimalFormat("0.0000"); 
				String kouj = df.format(zengkkbean.getKOUJ()); 
				bsh.set("扣价",kouj);
//				bsh.set("扣价",zengkkbean.getKOUJ());
				bsh.set("扣价单位",zengkkbean.getKOUJDW());
				bsh.set("增价",zengkkbean.getZENGFJ());
				bsh.set("增价单位",zengkkbean.getZENGFJDW());
				bsh.set("小数处理",zengkkbean.getXIAOSCL());
				bsh.set("加权方式",zengkkbean.getJIESXXB_ID());
				bsh.set("运输方式",zengkkbean.getYUNSFSB_ID());
				bsh.set("基准增扣价",zengkkbean.getJIZZKJ());
				bsh.set("基准指标",zengkkbean.getJIZZB());
				bsh.set("参照项目",zengkkbean.getCANZXM());
				bsh.set("参照项目单位",zengkkbean.getCANZXMDW());
				bsh.set("参照项目上限",zengkkbean.getCANZSX());
				bsh.set("参照项目下限",zengkkbean.getCANZXX());
				bsh.eval(gongs[2]);
				zengkktk1=bsh.get("文字").toString();
				
				List zengkktkList=new ArrayList();
				zengkktkList=getRows(zengkktk1,118);
				zengkkRows1+=zengkktkList.size();
				zengkktks1[i]=zengkktkList;
			}
		
			int kk=0;
			for(int i=jijRows+jiagrowInd+1;i<zengkkBeans.size()+jijRows+jiagrowInd+1;i++){//结算增扣款,外层：数组内层ｌｉｓｔ
		
				boolean t=false;
				int c=zengkktks[i-(jijRows+jiagrowInd+1)].size();//合同数组
				String st1="";
				for(int j=0;j<c;j++){
					st1+=zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString();
				}
				
				for(int m=0;m<zengkkBeansMB.size();m++){
					
					t=false;
					String st2="";
					for(int n=0;n<zengkktks1[m].size();n++){//模板数组
						st2+=zengkktks1[m].get(n);
					}
					
					String tem=Show(st1,st2);
				
					if(st1!=null && st1.equals(tem)){
						t=true;
						break;
					} 
				}
				
				for (int j=0;j<c;j++){
					
					if(t){//内容一致
						rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1, zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString());
					}else{
						rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1,Show( zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString(),null));
					}
					
					rt.body.mergeCell(jijRows+jiagrowInd+1+kk, 1, jijRows+jiagrowInd+1+kk++, 18);
				}
			}		
			
		int qittkrow=iJiagRows+jiagrowInd+1;//其他条款行索引
		rt.body.setRowHeight(qittkrow,HANGJJ);
		rt.body.setCellVAlign(qittkrow, 1,Table.VALIGN_BOTTOM);
	//其他条款行	
		for(int i = 0 ; i < wenz.length; i++){
			String wenzstr = wenz[i];
			List wzlist = getRows(wenzstr.replaceAll("<font color='red'>", "").replaceAll("</font>", ""),118);
			boolean font = wenzstr.indexOf("<font color='red'>") != -1;
			for(int j = 0; j < wzlist.size(); j++, qittkrow++){
				String[] wzstr = ((String)wzlist.get(j)).split("：");
				String tstr = "";
				String cstr = "";
				if(wzstr.length>1){
					tstr = wzstr[0] + "：";
					cstr = wzstr[1];
				}else{
					cstr = wzstr[0];
				}
				rt.body.setCellValue(qittkrow, 1, tstr + ((j==0)&&font?"<font color='red'>":"") 
						+ cstr + ((j+1==wzlist.size())&&font?"</font>":""));
				rt.body.mergeCell(qittkrow, 1, qittkrow, 18);
			}
		}
		rt.body.mergeCell(qittkrow+1, 1, qittkrow+1, 18);
		int gongxfrow=qittkrow+1;
		rt.body.setRowHeight(gongxfrow,HANGJJ);
		rt.body.setCellVAlign(gongxfrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 9,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 2,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 12,Table.VALIGN_BOTTOM);
		
		rt.body.setCellValue(gongxfrow, 1, "<font size=2><b>供方</b></font>");
		rt.body.mergeCell(gongxfrow, 1, gongxfrow, 8);
		rt.body.setCellValue(gongxfrow, 9, "<font size=2><b>需方</b></font>");
		rt.body.mergeCell(gongxfrow, 9, gongxfrow, 18);
		rt.body.setCellAlign(gongxfrow, 9, Table.ALIGN_CENTER);
		rt.body.setCellAlign(gongxfrow, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(gongxfrow+1, 1, "单位名称(章):");
		rt.body.setCellValue(gongxfrow+1, 2, bean.getGONGFDWMC());
		rt.body.setCellValue(gongxfrow+1, 9, "单位名称(章):");
		rt.body.setCellValue(gongxfrow+1, 12, bean.getXUFDWMC());
		rt.body.mergeCell(gongxfrow+1, 2, gongxfrow+1, 8);
		rt.body.mergeCell(gongxfrow+1, 12, gongxfrow+1, 18);
		rt.body.mergeCell(gongxfrow+1, 9, gongxfrow+1, 11);
		rt.body.setCellAlign(gongxfrow+1, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+2, 1, "单位地址:");
		rt.body.setCellValue(gongxfrow+2, 2, bean.getGONGFDWDZ());
		rt.body.setCellValue(gongxfrow+2, 9, "单位地址:");
		rt.body.setCellValue(gongxfrow+2, 12,bean.getXUFDWDZ());
		rt.body.mergeCell(gongxfrow+2, 2, gongxfrow+2, 8);
		rt.body.mergeCell(gongxfrow+2, 12, gongxfrow+2, 18);
		rt.body.mergeCell(gongxfrow+2, 9, gongxfrow+2, 11);
		rt.body.setCellAlign(gongxfrow+2, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+3, 1, "法定代表人:");
		rt.body.setCellValue(gongxfrow+3, 2, bean.getGONGFFDDBR());
		rt.body.setCellValue(gongxfrow+3, 9, "法定代表人:");
		rt.body.setCellValue(gongxfrow+3, 12, bean.getXUFFDDBR());
		rt.body.mergeCell(gongxfrow+3, 2, gongxfrow+3, 8);
		rt.body.mergeCell(gongxfrow+3, 12, gongxfrow+3, 18);
		rt.body.mergeCell(gongxfrow+3, 9, gongxfrow+3, 11);
		rt.body.setCellAlign(gongxfrow+3, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+4, 1, "委托代理人:");
		rt.body.setCellValue(gongxfrow+4, 2, bean.getGONGFWTDLR());
		rt.body.setCellValue(gongxfrow+4, 9, "委托代理人:");
		rt.body.setCellValue(gongxfrow+4, 12, bean.getXUFWTDLR());
		rt.body.mergeCell(gongxfrow+4, 2, gongxfrow+4, 8);
		rt.body.mergeCell(gongxfrow+4, 12, gongxfrow+4, 18);
		rt.body.mergeCell(gongxfrow+4, 9, gongxfrow+4, 11);
		rt.body.setCellAlign(gongxfrow+4, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+5, 1, "电话:");
		rt.body.setCellValue(gongxfrow+5, 2,bean.getGONGFDH());
		rt.body.setCellValue(gongxfrow+5, 9, "电话:");
		rt.body.setCellValue(gongxfrow+5, 12, bean.getXUFDH());
		rt.body.mergeCell(gongxfrow+5, 2, gongxfrow+5, 8);
		rt.body.mergeCell(gongxfrow+5, 12, gongxfrow+5, 18);
		rt.body.mergeCell(gongxfrow+5, 9, gongxfrow+5, 11);
		rt.body.setCellAlign(gongxfrow+5, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+6, 1, "传真号:");
		rt.body.setCellValue(gongxfrow+6, 2, bean.getGONGFDBGH());
		rt.body.setCellValue(gongxfrow+6, 9, "传真号:");
		rt.body.setCellValue(gongxfrow+6, 12, bean.getXUFDBGH());
		rt.body.mergeCell(gongxfrow+6, 2, gongxfrow+6, 8);
		rt.body.mergeCell(gongxfrow+6, 12, gongxfrow+6, 18);
		rt.body.mergeCell(gongxfrow+6, 9, gongxfrow+6, 11);
		rt.body.setCellAlign(gongxfrow+6, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+7, 1, "开户银行:");
		rt.body.setCellValue(gongxfrow+7,2, bean.getGONGFKHYH());
		rt.body.setCellValue(gongxfrow+7, 9, "开户银行:");
		rt.body.setCellValue(gongxfrow+7, 12, bean.getXUFKHYH());
		rt.body.mergeCell(gongxfrow+7, 2, gongxfrow+7, 8);
		rt.body.mergeCell(gongxfrow+7, 12, gongxfrow+7, 18);
		rt.body.mergeCell(gongxfrow+7, 9, gongxfrow+7, 11);
		rt.body.setCellAlign(gongxfrow+7, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+8, 1, "账号:");
		rt.body.setCellValue(gongxfrow+8, 2, bean.getGONGFZH());
		rt.body.setCellValue(gongxfrow+8, 9, "账号:");
		rt.body.setCellValue(gongxfrow+8, 12, bean.getXUFZH());
		rt.body.mergeCell(gongxfrow+8, 2, gongxfrow+8, 8);
		rt.body.mergeCell(gongxfrow+8, 12, gongxfrow+8, 18);
		rt.body.mergeCell(gongxfrow+8, 9, gongxfrow+8, 11);
		rt.body.setCellAlign(gongxfrow+8, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+9, 1, "邮政编码:");
		rt.body.setCellValue(gongxfrow+9, 2, bean.getGONGFYZBM());
		rt.body.setCellValue(gongxfrow+9, 9, "邮政编码:");
		rt.body.setCellValue(gongxfrow+9, 12, bean.getXUFYZBM());
		rt.body.mergeCell(gongxfrow+9, 2, gongxfrow+9, 8);
		rt.body.mergeCell(gongxfrow+9, 12, gongxfrow+9, 18);
		rt.body.mergeCell(gongxfrow+9, 9, gongxfrow+9, 11);
		rt.body.setCellAlign(gongxfrow+9, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+10, 1, "税号:");
		rt.body.setCellValue(gongxfrow+10, 2, bean.getGongfsh());
		rt.body.setCellValue(gongxfrow+10,9, "税号:");
		rt.body.setCellValue(gongxfrow+10, 12, bean.getXufsh());
		rt.body.mergeCell(gongxfrow+10, 2, gongxfrow+10, 8);
		rt.body.mergeCell(gongxfrow+10, 12, gongxfrow+10, 18);
		rt.body.mergeCell(gongxfrow+10, 9, gongxfrow+10, 11);
		rt.body.setCellAlign(gongxfrow+10, 9, Table.ALIGN_RIGHT);
		
		rt.body.setRowHeight(gongxfrow+11,HANGJJ);
		rt.body.setCellVAlign(gongxfrow+11, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(gongxfrow+11, 1, "<font size=2><b>有效日期:"+QISRQ+"至"+GUOQRQ+"</b></font>");
		rt.body.mergeCell(gongxfrow+11, 1, gongxfrow+11, 18);
		rt.body.setCellAlign(gongxfrow+11, 1, Table.ALIGN_LEFT);
//设置边框	
		rt.body.setBorderNone();
		rt.body.setCells(1,1,gongxfrow+11,18,Table.PER_BORDER_BOTTOM,0);
		rt.body.setCells(1,1,gongxfrow+11,18,Table.PER_BORDER_RIGHT,0);
		//7行到ShulRows+8行数量表
		rt.body.setCells(7,1,iShulRows+8,18,Table.PER_BORDER_BOTTOM,1);
		rt.body.setCells(7,1,iShulRows+8,18,Table.PER_BORDER_RIGHT,1);
		rt.body.setCells(7,1,7,18,Table.PER_BORDER_TOP,2);
		rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
		rt.body.setCells(7,18,iShulRows+8,18,Table.PER_BORDER_RIGHT,2);
		rt.body.setCells(iShulRows+8,1,iShulRows+8,18,Table.PER_BORDER_BOTTOM,2);
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
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,18,Table.PER_BORDER_TOP,2);
				}
				if(i+1==jiag_table_rows){//最后
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,18,Table.PER_BORDER_BOTTOM,2);
				}else{
					rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,18,Table.PER_BORDER_BOTTOM,1);
				}
				rt.body.setCells(jiag_table_index+i,1,jiag_table_index+i,18,Table.PER_BORDER_RIGHT,1);
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
		}
		if (cycle.getRequestContext().getParameter("hetb_id") != null&&!cycle.getRequestContext().getParameter("hetb_id").equals("-1")) {
			visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetb_id")));
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
//	格式化条款；根据硬回车和自动换行来生成行 设显示的列数为
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

			if(tmp[i].length()*2<rowSize){//如果在硬行最大化字符数与规定字符比较，如果小于则不必要分行
				result.add(tmp[i]);
				continue;
			}else{//如果硬行最大字符数与规定字符数比较，如果大于则要分行
				for(int k=0;k<tmp[i].length();k++){
					if(c<rowSize){//小于规定字符数时继续累计
						TemChar=tmp[i].substring(k, k+1);
						TemChars+=TemChar;
//						b=TemChar.getBytes("unicode");
//						if(b[3]==-1){//全角
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