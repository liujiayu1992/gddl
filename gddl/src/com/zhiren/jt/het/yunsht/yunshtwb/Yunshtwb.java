package com.zhiren.jt.het.yunsht.yunshtwb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bsh.Interpreter;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
//import com.zhiren.jt.het.hetmb.Fahxxbean;
import com.zhiren.jt.het.yunsht.yunsht.Hetysxxbean;
import com.zhiren.jt.het.hetmb.Zengkkbean;
//import com.zhiren.jt.het.hetmb.Zhilyqbean;
import com.zhiren.jt.het.yunsht.yunsht.Yunsjijbean;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yunshtwb extends BasePage {

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

	private String REPORT_NAME_HETDY = "Hetysdy";// 

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
//		blnIsBegin = false;
//		if (mstrReportName.equals(REPORT_NAME_HETDY)) {
			return getHetysdy();
//		} else {
//			return "无此报表";
//		}
	}
//	private void loadBean (Object obj,"Hetysxxbean"){
//		
//	}
	private String getHetysdy() {
		
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String[] gongs=new String[3];
		try{

			String sql="select id\n" +
			"from (\n" + 
			"select id,leix,decode(mingc,'价格条款文字分析',1,2)xuh,mingc\n" + 
			"from gongsb\n" + 
			"where gongsb.zhuangt=1 and leix='运输合同'\n" + 
			")\n" + 
			"order by xuh";
			ResultSetList rs0=con.getResultSetList(sql);
			DataBassUtil clob=new DataBassUtil();
			int k=0;
			while(rs0.next()){
				//gongs[k++]=rs0.getString(1);
				gongs[k++]=clob.getClob("gongsb", "gongs", rs0.getLong(0));
				
			}
			
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select ID,\n");
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
//		buffer.append("       HETGYSBID,\n" );
//		buffer.append("       GONGYSB_ID,\n" );
		buffer.append("       to_char( QISRQ,'YYYY-MM-DD')QISRQ,\n" );
		buffer.append("       to_char(GUOQRQ,'YYYY-MM-DD')GUOQRQ,\n" );
		buffer.append("       diancxxb_id,\n" );
		buffer.append("       hetys_mb_id,\n" );
		buffer.append("       meikmcs\n" );
		buffer.append("  from hetys\n" );
		buffer.append(" where hetys.ID = "+((Visit) getPage().getVisit()).getLong1());
		Hetysxxbean bean=new Hetysxxbean();
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
//			jihkjmc=rs.getString("jihkjmc");
			fahr=(rs.getString("meikmcs")==null)?"":rs.getString("meikmcs");
		}
//数量
		/*
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
		buffer.append("where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n" );
		buffer.append("and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id");
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
//质量
		buffer.setLength(0);
		buffer.append("select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n" );
		buffer.append("from hetzlb,zhibb,tiaojb,danwb\n" );
		buffer.append("where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="+((Visit) getPage().getVisit()).getLong1());
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
			bsh.eval(yunsdw[0]);
			zhiltk=bsh.get("文字").toString();
			zhiltks[i]=zhiltk;
		}
		*/
//价格
		List zhilyqBeans=((Visit) getPage().getVisit()).getList8();
		int l=0;
		Interpreter bsh= new Interpreter();
		
		buffer.setLength(0);
		
		buffer.append("select j.id as id, j.hetys_id, m.mingc as meikxxb_id, z.mingc as zhibb_id, zd.mingc as danwb_id, t.mingc as tiaojb_id, j.xiax, j.shangx,j. yunja, yd.mingc as yunjdw_id \n ");
		buffer.append(" from hetysjgb j,meikxxb m,zhibb z,tiaojb t,danwb zd, danwb yd \n");
		buffer.append(" where j.meikxxb_id=m.id(+) and j.zhibb_id=z.id(+) and j.tiaojb_id=t.id(+) and j.danwb_id=zd.id(+) and j.yunjdw_id=yd.id(+) and hetys_id="+((Visit) getPage().getVisit()).getLong1()+"\n");
		
		if(((Visit) getPage().getVisit()).getList9()==null){
			((Visit) getPage().getVisit()).setList9(new ArrayList());
		}
		List jiagBeans=((Visit) getPage().getVisit()).getList9();
		ResultSet rs3=con.getResultSet(buffer);
		while(rs3.next()){
			Yunsjijbean jibean=new Yunsjijbean();
			jibean.setMeikxxb_id(rs3.getString("meikxxb_id"));
			jibean.setZhibb_id(rs3.getString("zhibb_id"));
			jibean.setZhibdw_id(rs3.getString("danwb_id"));
			jibean.setTiaoj_id(rs3.getString("tiaojb_id")==null?"":rs3.getString("tiaojb_id"));
			jibean.setXiax(rs3.getDouble("XIAX"));
			jibean.setShangx(rs3.getDouble("SHANGX"));
			jibean.setYunjia(rs3.getDouble("yunja"));
			jibean.setYunjdw_id(rs3.getString("yunjdw_id"));
			
			jiagBeans.add(jibean);
		}
//		格式化条款
		int jijRows=0;
		l=jiagBeans.size();
		List[] jiagtks=new List[l];
		String jiagtk="";
		for(int i=0;i<l;i++){
			Yunsjijbean jigbean=(Yunsjijbean)jiagBeans.get(i);
			bsh.set("煤矿单位",jigbean.getMeikxxb_id());
			bsh.set("项目",jigbean.getZhibb_id());
			bsh.set("项目单位",jigbean.getZhibdw_id());
			bsh.set("条件",jigbean.getTiaoj_id());
			bsh.set("下限",jigbean.getXiax());
			bsh.set("上限",jigbean.getShangx());
			bsh.set("单位",jigbean.getZhibdw_id());
			bsh.set("价格",jigbean.getYunjia());
			bsh.set("价格单位",jigbean.getYunjdw_id());
			
			bsh.eval(gongs[0]);
			
			
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
		buffer.append("from hetyszkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n" );
		buffer.append("where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n" );
		buffer.append("and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n" );
		buffer.append("and z.yunsfsb_id=yunsfsb.id(+) and z.hetys_id="+((Visit) getPage().getVisit()).getLong1());
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
			bsh.set("扣价",zengkkbean.getKOUJ());
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
			bsh.eval(gongs[1]);
			zengkktk=bsh.get("文字").toString();
			
			List zengkktkList=new ArrayList();
			zengkktkList=getRows(zengkktk,118);
			zengkkRows+=zengkktkList.size();
			zengkktks[i]=zengkktkList;
		}
		
//文字
		String wenzStr="";
		buffer.setLength(0);
		buffer.append("select id,wenznr\n");
		buffer.append("from hetyswzb\n" );
		buffer.append("where hetys_id="+((Visit) getPage().getVisit()).getLong1());
		ResultSet rs5=con.getResultSet(buffer);
		while(rs5.next()){
			wenzStr=rs5.getString("wenznr");
		}
//格式化条款	
		List wenzList=new ArrayList();
		int qittkRows=wenzList.size();
		if(wenzStr==null){
			qittkRows=0;
		}else{
			if(!wenzStr.equals("")){
				wenzList=getRows(wenzStr,118);
				qittkRows=wenzList.size();
			}
		}
		//////////////////////////////////////////////////
//绑定内容、强行合并进行布局
		String[][] ArrHeader = new String[1][18];
		int ArrWidth[]=new int[] {104,32,32,42,42,45,35,35,35,35,35,35,35,35,35,35,35,35};
		rt.setTitle("煤 炭 运 输 合 同", ArrWidth);
		int iRows=0;
		int iHeadRows=4;
//		int iShulRows=shulBeans.size();
//		int iZhilRows=zhilyqBeans.size();
		int iJiagRows=jijRows+zengkkRows;//zengkkBeans.size();jiagBeans.size()+
//		int iJiagRows=jijRows+zengkkRows;//zengkkBeans.size();jiagBeans.size()+
		int iQitRows=qittkRows;
		int iGongxfRows=11;
		int iYouxq=1;
		final int HANGJJ=30;//行间距
		iRows=iHeadRows+iJiagRows+iQitRows+iGongxfRows+iYouxq+4;
//		iRows=iHeadRows+iShulRows+3+iZhilRows+1+iJiagRows+1+iQitRows+1+iGongxfRows+iYouxq+2;
		if(fahr==""){
			iRows-=1;
		}
		rt.setBody(new Table(iRows,18));
		rt.body.setWidth(ArrWidth);
		
		rt.body.setCellValue(1, 1, "<font size=2><b>受运人(甲方):</b></font>"+bean.getXUFDWMC());
		rt.body.mergeCell(1, 1, 1, 12);
		rt.body.setCellValue(1, 13,  "<font size=2><b>合同编号:</b></font>"+bean.getHetbh());
		rt.body.mergeCell(1, 13, 1, 18);
		
		rt.body.setCellValue(2, 13, "<b><font size=2>签订日期:</b></font>"+qiandrq);
		rt.body.mergeCell(2, 13, 2, 18);
		
		rt.body.setCellValue(3, 1, "<font size=2><b>承运人(乙方):</b></font>"+bean.getGONGFDWMC());
		rt.body.mergeCell(3, 1, 3, 12);
		rt.body.mergeCell(3, 13, 3, 18);
		rt.body.setCellValue(3, 13, "<font size=2><b>签订地点:</b></font>"+bean.getQianddd());
		rt.body.setRowHeight(3,HANGJJ);
		rt.body.setCellVAlign(3, 1,Table.VALIGN_TOP);
		rt.body.setCellVAlign(3, 13,Table.VALIGN_TOP);
		
		rt.body.setCellValue(4, 1, "&nbsp;&nbsp;&nbsp;&nbsp;依据《中华人民共和国经济合同法》，《工矿产品购销合同条例》并遵守公平、公正的原则，经双方协商一致，" +"达成如下协议，签");
		rt.body.mergeCell(4, 1, 4, 18);
		rt.body.setCellValue(5, 1, "订合同约束双方共同履行。");
		rt.body.mergeCell(5, 1, 5, 18);
		/*
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
		for(int i=9;i<iShulRows+9;i++){//煤炭数量//
				rt.body.setCellValue(i, 1,((Fahxxbean) shulBeans.get(i-9)).getShouhr());//收货人
				rt.body.setCellValue(i, 2,((Fahxxbean) shulBeans.get(i-9)).getPinz());//品种
				rt.body.setCellValue(i, 3,((Fahxxbean) shulBeans.get(i-9)).getYunsfs());//运输方式
				rt.body.setCellValue(i, 4,((Fahxxbean) shulBeans.get(i-9)).getFaz());//发站
				rt.body.setCellValue(i, 5,((Fahxxbean) shulBeans.get(i-9)).getDaoz());//到站
				rt.body.setCellValue(i, 6,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getHej()/10000f));//合计
				rt.body.setCellValue(i, 7,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY1()/10000f));//一月
				rt.body.setCellValue(i, 8,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY2()/10000f));//二月
				rt.body.setCellValue(i, 9,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY3()/10000f));//三月
				rt.body.setCellValue(i, 10,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY4()/10000f));//四月
				rt.body.setCellValue(i, 11,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY5()/10000f));//五月
				rt.body.setCellValue(i, 12,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY6()/10000f));//六月
				rt.body.setCellValue(i, 13,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY7()/10000f));//七月
				rt.body.setCellValue(i, 14,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY8()/10000f));//八月
				rt.body.setCellValue(i, 15,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY9()/10000f));//九月
				rt.body.setCellValue(i, 16,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY10()/10000f));//十月
				rt.body.setCellValue(i, 17,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY11()/10000f));//十一月
				rt.body.setCellValue(i, 18,String.valueOf(((Fahxxbean) shulBeans.get(i-9)).getY12()/10000f));//十二月
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
		
		for(int i=zhilrowInd+1;i<iZhilRows+zhilrowInd+1;i++){//煤炭质量五列//
			rt.body.setCellValue(i, 1,zhiltks[i-(zhilrowInd+1)]);
			rt.body.mergeCell(i, 1, i, 18);
			
		}
		*/
		int jiagrowInd=6;//价格索引
//		int jiagrowInd=iZhilRows+zhilrowInd+1;//价格索引
		rt.body.setRowHeight(jiagrowInd,HANGJJ);
		rt.body.setCellVAlign(jiagrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(jiagrowInd, 1, "<font size=2><b>一、结算价格</b></font>");
		rt.body.mergeCell(jiagrowInd, 1, jiagrowInd, 18);
		int kk=0;
		for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//结算价格
			int c=jiagtks[i-(jiagrowInd+1)].size();
			for (int j=0;j<c;j++){
				rt.body.setCellValue(jiagrowInd+1+kk, 1, jiagtks[i-(jiagrowInd+1)].get(j).toString());
				rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 18);
			}
		}
		kk=0;
		for(int i=jijRows+jiagrowInd+1;i<zengkkBeans.size()+jijRows+jiagrowInd+1;i++){//结算增扣款,外层：数组内层ｌｉｓｔ
			int c=zengkktks[i-(jijRows+jiagrowInd+1)].size();
			for (int j=0;j<c;j++){
				rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1, zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString());
				rt.body.mergeCell(jijRows+jiagrowInd+1+kk, 1, jijRows+jiagrowInd+1+kk++, 18);
			}
		}
//		for(int i=jiagrowInd+1;i<jiagBeans.size()+jiagrowInd+1;i++){//结算价格
//			int c=jiagtks[i-(jiagrowInd+1)].size();
//			for (int j=0;j<c;j++){
//				rt.body.setCellValue(jiagrowInd+1+kk, 1, jiagtks[i-(jiagrowInd+1)].get(j).toString());
//				rt.body.mergeCell(jiagrowInd+1+kk, 1, jiagrowInd+1+kk++, 18);
//			}
//		}
//		kk=0;
////		没有增扣款
//		for(int i=jijRows+jiagrowInd+1;i<jijRows+jiagrowInd+1;i++){//结算增扣款,外层：数组内层ｌｉｓｔ
////			rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1, zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString());
//			rt.body.mergeCell(jijRows+jiagrowInd+1+kk, 1, jijRows+jiagrowInd+1+kk++, 18);
//		}
////		有增扣款
//		for(int i=jijRows+jiagrowInd+1;i<zengkkBeans.size()+jijRows+jiagrowInd+1;i++){//结算增扣款,外层：数组内层ｌｉｓｔ
//			int c=zengkktks[i-(jijRows+jiagrowInd+1)].size();
//			for (int j=0;j<c;j++){
//				rt.body.setCellValue(jijRows+jiagrowInd+1+kk, 1, zengkktks[i-(jijRows+jiagrowInd+1)].get(j).toString());
//				rt.body.mergeCell(jijRows+jiagrowInd+1+kk, 1, jijRows+jiagrowInd+1+kk++, 18);
//			}
//		}
		int qittkrow=iJiagRows+jiagrowInd+1;//其他条款行索引
		rt.body.setRowHeight(qittkrow,HANGJJ);
		rt.body.setCellVAlign(qittkrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(qittkrow, 1, "<font size=2><b>二、其他条款</b></font>");
		rt.body.mergeCell(qittkrow, 1, qittkrow, 18);
		
//		rt.body.setCellValue(qittkrow+1, 1, wenzStr);//其他条款暂时一行
		for(int i=qittkrow+1;i<qittkRows+qittkrow+1;i++){
			rt.body.setCellValue(i, 1, wenzList.get(i-(qittkrow+1)).toString());//strTiaok_jiag
			rt.body.mergeCell(i, 1, i, 11);
		}
//		rt.body.mergeCell(qittkrow+1, 1, qittkrow+1, 18);
		int gongxfrow=qittkrow+qittkRows+1;
		rt.body.setRowHeight(gongxfrow,HANGJJ);
		rt.body.setCellVAlign(gongxfrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 9,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 2,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 12,Table.VALIGN_BOTTOM);
		
		rt.body.setCellValue(gongxfrow, 1, "<font size=2><b>承运人(乙方)</b></font>");
		rt.body.mergeCell(gongxfrow, 1, gongxfrow, 8);
		rt.body.setCellValue(gongxfrow, 9, "<font size=2><b>受运人(甲方)</b></font>");
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
		rt.body.setCellValue(gongxfrow+10, 2,  bean.getGongfsh());
		rt.body.setCellValue(gongxfrow+10,9, "税号:");
		rt.body.setCellValue(gongxfrow+10, 12,  bean.getXufsh());
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
		/*
		rt.body.setCells(7,1,iShulRows+8,18,Table.PER_BORDER_BOTTOM,1);
		rt.body.setCells(7,1,iShulRows+8,18,Table.PER_BORDER_RIGHT,1);
		rt.body.setCells(7,1,7,18,Table.PER_BORDER_TOP,2);
		rt.body.setCells(7,1,iShulRows+8,1,Table.PER_BORDER_LEFT,2);
		rt.body.setCells(7,18,iShulRows+8,18,Table.PER_BORDER_RIGHT,2);
		rt.body.setCells(iShulRows+8,1,iShulRows+8,18,Table.PER_BORDER_BOTTOM,2);
		rt.body.setCellBorderRight(7,6,2);
		*/
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
		if (cycle.getRequestContext().getParameter("hetys_id") != null&&!cycle.getRequestContext().getParameter("hetys_id").equals("-1")) {
			visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetys_id")));
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
