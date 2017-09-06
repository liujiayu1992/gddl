package com.zhiren.dtrlgs.pubclass;

import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class BalanceMingx extends BasePage {
	
	private String[] yr;//多个结算编号
	private String type;
	
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
	
//	private String printTable;
//	public void setPrintTable(String _value){
//		
//	}
	public String getPrintTable() {
		String[] tables=((Visit) getPage().getVisit()).getString3().split(",");
//		if (yr != null && yr.length!=0) {
//			int k=0;
//			StringBuffer sb = new StringBuffer();
//			setAllPages(yr.length);
//			this.setAllPages(yr.length);
//			for (int p = 0; p < yr.length; p++) {
//					sb.append(this.getYansmx(tables[0],tables[1],yr[p],p,type));
//				k+=this.getAllPages();
//			}
//			setAllPages(k);
//			_CurrentPage=1;
//			return sb.toString();
//		} else {
//			return "";
//		}
		return this.getYansmx(tables[0],tables[1],yr[0],type);
	}

	private String getYansmx(String table1,String table2,String bianm,String type) {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
//		StringBuffer talbe=new StringBuffer();	//报表输出
		long Jiesb_id=0;
		String Diancqc="";
		String Meikdwmc="";
		String sql = "";
		String tiaoj = "diancjsmkb_id";
		String danw = "元/千卡(元/吨)";
		
		sbsql.append("select d.quanc,j.id,m.quanc meikdwmc,j.bianm from " +table1+ " j,diancxxb d, meikxxb m where j.meikxxb_id=m.id and j.diancxxb_id=d.id and j.bianm='"+bianm+"'");
		ResultSetList rsl=con.getResultSetList(sbsql.toString());
		if(rsl.next()){
			
			Jiesb_id=rsl.getLong("id");
			Diancqc=rsl.getString("quanc");
			Meikdwmc=rsl.getString("meikdwmc");
		}else{
			return "无数据";
		}
		
		//单位
		sql = "select max(mingc) as mingc from(\n" +
				"select j.bianm,d.mingc\n" + 
				"from kuangfjsmkb j,hetjgb h,danwb d\n" + 
				"where j.hetb_id = h.hetb_id\n" + 
				"      and h.jijdwid = d.id\n" + 
				"union\n" + 
				"select j.bianm,d.mingc\n" + 
				"from kuangfjsyfb j,hetjgb h,danwb d\n" + 
				"where j.hetb_id = h.hetb_id\n" + 
				"      and h.jijdwid = d.id\n" + 
				")\n" + 
			  "where bianm ='"+bianm+"'\n";		
		ResultSetList rsl_dw=con.getResultSetList(sql);	
		while(rsl_dw.next()){
			danw = rsl_dw.getString("mingc");
		}

		
		sbsql.setLength(0);
		if(type.equals("changf")){
			tiaoj = "diancjsmkb_id";
		}else if(type.equals("kuangf")){
			tiaoj = "kuangfjsmkb_id";
		}
		
//		long Jiesb_id_type=0;	//局部变量

		ResultSet rs=null;
		sbsql.setLength(0);
		sbsql.append(
			"select decode(riq,'合计','合计',rownum) as xuh,\n" +
			"       riq,pinz,biaoz,jingz,yingk,yuns,zongkd,qnet_ar,qnet_ar_k,\n" + 
			"       aar,vdaf,mt,std,'' as beiz\n" + 
			"       from\n" + 
			"   (select f.lie_id as id,\n" + 
			"       decode(f.daohrq,null,'合计',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
			"       decode(p.mingc,null,'合计',p.mingc) as pinz,\n" + 
			"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
			"       sum(f.zongkd) as zongkd,\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*round_new(z.qnet_ar,2)))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz)),2) as qnet_ar,\n" + 
			"\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*round_new(z.qnet_ar,2)))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz))*1000/4.1816,0) as qnet_ar_k,\n" + 
			"\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*z.aar))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz)),2) as aar,\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*z.vdaf))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz)),2) as vdaf,\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*z.mt))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz)),2) as mt,\n" + 
			"       round_new(sum(decode(f.jingz,0,0,f.jingz*z.std))\n" + 
			"                     /sum(decode(f.jingz,0,1,f.jingz)),2) as std\n" + 
			"       from fahb f,zhilb z,pinzb p,\n" + 
			"            (select * from " + table1 + " where id="+Jiesb_id+") j\n" + 
			"       where f.zhilb_id=z.id\n" + 
			"             and f.pinzb_id=p.id\n" + 
			"             and f.jiesb_id=j.id\n" + 
//					"             and f.liucztb_id=1\n" + 
//					"             and z.liucztb_id=1\n" + 
			"       group by rollup(f.lie_id,f.daohrq,p.mingc)\n" + 
			"       having not (grouping(p.mingc)=1 and grouping(f.lie_id)=0)\n" + 
			"    )");
		rs=con.getResultSet(sbsql.toString());
		Report rt = new Report(); //报表定义
		String ArrHeader[][]=new String[3][15];
		ArrHeader[0]=new String[] {"序号","日期","品种","数量验收","数量验收","数量验收","数量验收","数量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","备注"};
		ArrHeader[1]=new String[] {"序号","日期","品种","矿发量","验收量","盈亏量","途损量","扣减量","Qnet,ar","Qnet,ar","Aar","Vdaf","Mar","St,d","备注"};
		ArrHeader[2]=new String[] {"序号","日期","品种","吨","吨","吨","吨","吨","MJ/kg","kcal/kg","%","%","%","%","备注"};
		int ArrWidth[]=new int[] {60,90,70,73,75,75,75,75,75,75,75,75,75,75,75};
		// 数据
		rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
		rt.setDefaultTitleLeft("供货单位:"+Meikdwmc+"<br>收货单位:"+Diancqc+"",12);
		rt.setDefaultTitle(14,1,"验收单号:<br>日期:",Table.ALIGN_LEFT);
		rt.setBody(new Table(rs, 3, 0, 0));
		rt.body.setWidth(ArrWidth);
//				rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),3);
		rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.mergeFixedRow();
		rt.body.ShowZero = false;
		rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
//			构造表
//		talbe.append(rt.getAllPagesHtml()+"\n"); 
		
		
		//下面的连接表
//		sbsql.setLength(0);
//		
////			汇总数据
////		sbsql.append(
////				"select * from\n" +
////				"       (select\n" + 
////				"              round_new(\n" + 
////				"                  decode(getjiesdzb('"+table1+"',j.id,'Qnetar','jies'),null,\n" + 
////				"                  0,nvl(j.jiajqdj,0)/getjiesdzb('"+table1+"',j.id,'Qnetar','jies')),4) as farldj_jjq,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj_jjq,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj_jjq,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj_jjq,\n" + 
////				"              round_new(nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000,2) as farlj_jjq,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk_jjq,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl_jjq,\n" + 
////				"              nvl(j.jiajqdj,0) as hansdj_jjq,\n" + 
////				"              round_new(nvl(j.jiajqdj,0)*j.jiessl,2) as hansmk_jjq,\n" + 
////				"              nvl(j.yunj,0) as yunj_jjq,\n" + 
////				"              nvl(jy.hansdj,0) as hansdjyf_jjq,\n" + 
////				"              nvl(jy.hansyf,0) as hansyf_jjq,\n" + 
////				"              (round_new(nvl(j.jiajqdj,0)*nvl(j.jiessl,0),2)+nvl(jy.hansyf,0)) as zongje_jjq\n" + 
////				"              from "+table1+" j,"+table2+" jy\n" + 
////				"              where j.id=jy."+tiaoj+"(+)\n" + 
////				"                    and j.id='"+Jiesb_id+"'),\n" + 
////				"\n" + 
////				"       (select\n" + 
////				"              round_new(\n" + 
////				"                  decode(getjiesdzb('"+table1+"',j.id,'Qnetar','jies'),null,\n" + 
////				"                  0,nvl(j.hansdj,0)/getjiesdzb('"+table1+"',j.id,'Qnetar','jies')),4) as farldj,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj,\n" + 
////				"              round_new(nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000,2) as farlj,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk,\n" + 
////				"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl,\n" + 
////				"              nvl(j.hansdj,0) as hansdj,nvl(j.hansmk,0) as hansmk,nvl(j.yunj,0) as yunj," +
////				"			   nvl(jy.hansdj,0) as hansdjyf,\n" + 
////				"              nvl(jy.hansyf,0) as hansyf,(nvl(j.hansmk,0)+nvl(jy.hansyf,0)) as zongje\n" + 
////				"              from "+table1+" j,"+table2+" jy\n" + 
////				"              where j.id=jy."+tiaoj+"(+)\n" + 
////				"                    and j.id='"+Jiesb_id+"')");
//		
//		
//		sbsql.append("select * from\n"+
//				       "(select\n"+
//				        "      round_new(\n"+
//				        "         decode(getjiesdzb('" + table1 + "',j.id,'Qnetar','jies'),null,\n"+
//				        "         0,\n"+
//				        "         0\n" +
////				        "	   --nvl(j.jiajqdj,0)/getjiesdzb('kuangfjsmkb',j.id,'Qnetar','jies')"+
//				        "          ),4) as farldj_jjq,\n"+
//				        "      nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj_jjq,\n"+
//				        "      nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj_jjq,\n"+
//				        "      nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj_jjq,\n"+
//				        "      round_new(nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000,2) as farlj_jjq,\n"+
//				        "      nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk_jjq,\n"+
//				        "      nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl_jjq,\n"+
////				        "      --nvl(j.jiajqdj,0) as hansdj_jjq,"+
//				        "      round_new(\n" +
////				        "--nvl(j.jiajqdj,0)*"+
//				        "                            j.jiessl,2) as hansmk_jjq,\n"+
////				        "      --nvl(j.yunj,0) as yunj_jjq,"+
//				        "      nvl(jy.hansdj,0) as hansdjyf_jjq,\n"+
//				        "      nvl(jy.hansyf,0) as hansyf_jjq,\n"+
//				        "      (round_new(\n" +
////				        "		--nvl(j.jiajqdj,0)*"+
//				        "                       nvl(j.jiessl,0),2)+nvl(jy.hansyf,0)) as zongje_jjq \n"+
//				        "      from " + table1 + " j," + table2 + " jy\n"+
//				        "      where j.id=jy."+tiaoj+"(+)\n"+
//				        "            and j.id='"+Jiesb_id+"'\n"+
//				        "            ),\n"+
//
//				       "(select\n"+
//				       "       round_new(\n"+
//				       "           decode(getjiesdzb('" + table1 + "',j.id,'Qnetar','jies'),null,\n"+
//				       "           0,nvl(j.hansdj,0)/getjiesdzb('" + table1 + "',j.id,'Qnetar','jies')),4) as farldj,\n"+
//				       "       nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj,\n"+
//				       "       nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj,\n"+
//				       "       nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj,\n"+
//				       "       round_new(nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000,2) as farlj,\n"+
//				       "       nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk,\n"+
//				       "       nvl(getjiesdzb('" + table1 + "',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl,\n"+
//				       "       nvl(j.hansdj,0) as hansdj,\n"+
//				       "       nvl(j.hansmk,0) as hansmk,\n"+
////				       "       --nvl(j.yunj,0) as yunj,"+
//				       "       nvl(jy.hansdj,0) as hansdjyf,\n"+
//				       "       nvl(jy.hansyf,0) as hansyf,(nvl(j.hansmk,0)+nvl(jy.hansyf,0)) as zongje\n"+
//				       "       from " + table1 + " j," + table2 + " jy\n"+
//				       "       where j.id=jy."+tiaoj+"(+)\n"+
//				       "             and j.id='"+Jiesb_id+"'\n"+
//                    ")");
//		
//		
//		rs=con.getResultSet(sbsql);
//		String farldj="";
//		String aarzj="";
//		String vdafzj="";
//		String yunjzj="";
//		String farlj="";
//		String farlk="";
//		String jiessl="";
//		String hansdj="";
//		String hansmk="";
////		String yunj="";
//		String hansdjyf="";
//		String hansyf="";
//		String zongje="";
//		
////		String farldj_jjq="";
//		String aarzj_jjq="";
//		String vdafzj_jjq="";
//		String yunjzj_jjq="";
//		String farlj_jjq="";
//		String farlk_jjq="";
//		String jiessl_jjq="";
////		String hansdj_jjq="";
//		String hansmk_jjq="";
////		String yunj_jjq="";
//		String hansdjyf_jjq="";
//		String hansyf_jjq="";
//		String zongje_jjq="";
//		
//		try {
//			if(rs.next()){
//				
////					加价前
////				farldj_jjq=rs.getString("farldj_jjq");
//				aarzj_jjq=rs.getString("aarzj_jjq");
//				vdafzj_jjq=rs.getString("vdafzj_jjq");
//				yunjzj_jjq=rs.getString("yunjzj_jjq");
//				farlj_jjq=rs.getString("farlj_jjq");
//				farlk_jjq=rs.getString("farlk_jjq");
//				jiessl_jjq=rs.getString("jiessl_jjq");
////				hansdj_jjq=rs.getString("hansdj_jjq");
//				hansmk_jjq=rs.getString("hansmk_jjq");
////				yunj_jjq=rs.getString("yunj_jjq");
//				hansdjyf_jjq=rs.getString("hansdjyf_jjq");
//				hansyf_jjq=rs.getString("hansyf_jjq");
//				zongje_jjq=rs.getString("zongje_jjq");
//				
//				
////					加价后
//				farldj=rs.getString("farldj");
//				aarzj=rs.getString("aarzj");
//				vdafzj=rs.getString("vdafzj");
//				yunjzj=rs.getString("yunjzj");
//				farlj=rs.getString("farlj");
//				farlk=rs.getString("farlk");
//				jiessl=rs.getString("jiessl");
//				hansdj=rs.getString("hansdj");
//				hansmk=rs.getString("hansmk");
////				yunj=rs.getString("yunj");
//				hansdjyf=rs.getString("hansdjyf");
//				hansyf=rs.getString("hansyf");
//				zongje=rs.getString("zongje");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		String ArrHeader2[][]=new String[5][15];
//		ArrHeader2[0]=new String[] {"结算数据","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
//		ArrHeader2[1]=new String[] {"结算数据","Qnet,ar  ","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
//		ArrHeader2[2]=new String[] {"结算数据",danw,"Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
//		ArrHeader2[3]=new String[] {"采购","String.valueOf(Double.parseDouble(farldj_jjq))",aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
//String.valueOf(CustomMaths.Round_new(//Double.parseDouble(farldj_jjq)+  
//									 Double.parseDouble(aarzj_jjq)+
//									 Double.parseDouble(vdafzj_jjq)+
//									 Double.parseDouble(yunjzj_jjq)
//									 ,4)
//									 ),
//											farlj_jjq,farlk_jjq,jiessl_jjq,"hansdj_jjq",hansmk_jjq,"yunj_jjq",hansdjyf_jjq,hansyf_jjq,zongje_jjq};
//		ArrHeader2[4]=new String[] {"销售",String.valueOf(Double.parseDouble(farldj)),aarzj,vdafzj,yunjzj,String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj)+Double.parseDouble(aarzj)+Double.parseDouble(vdafzj)
//											+Double.parseDouble(yunjzj),4)),farlj,farlk,jiessl,hansdj,hansmk,"yunj",hansdjyf,hansyf,zongje};
//
//		int ArrWidth2[]=new int[] {60,90,70,73,75,75,75,75,75,75,75,75,75,75,75};
//
////			 定义页Title
//		Report rt2=new Report();
//		rt2.setBody(new Table(ArrHeader2,0,0,0));
//		rt2.body.setWidth(ArrWidth2);
//		 
////			 合并单元格
//		rt2.body.mergeCell(1,1,3,1);
//		rt2.body.mergeCell(1,2,1,6);
//		rt2.body.mergeCell(1,7,1,8);
//		rt2.body.mergeCell(1,9,2,9);
//		rt2.body.mergeCell(1,10,1,11);
//		rt2.body.mergeCell(1,12,1,14);
//		rt2.body.mergeCell(1,15,2,15);
//		rt2.body.mergeCell(2,3,3,3);
//		rt2.body.mergeCell(2,4,3,4);
//		rt2.body.mergeCell(2,6,3,6);
//		rt2.body.mergeCell(2,7,2,8);
//		rt2.body.setBorder(2, 1, 0, 1);
//		rt2.body.setCells(1, 1, 3, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_CENTER);
//		rt2.body.setCells(4, 1, 5, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
//		rt2.body.setCells(4, 2, 5, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_RIGHT);
//		rt2.body.mergeFixedRow();
//		talbe.append(rt2.getAllPagesHtml()+"\n");

		// 设置页数
//		_CurrentPage = 1;
//		_AllPages = 2;
//		if (_AllPages == 0) {
//			_CurrentPage = 0;
//		}
		_CurrentPage = 1;
		_AllPages = 1;
		return rt.getAllPagesHtml();//+rt2.getAllPagesHtml();
	}

	public boolean isLx(String a,String b){
		boolean lx = false;
		if(b.indexOf(a)>-1){
			
			lx = true;
		}
		return lx;
	}
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());			
			if (cycle.getRequestContext().getParameters("lx")[0].equals("balancemingx")) {
//				String y = ((String)visit.getSession().getAttribute("bianh")).split(";")[0];
//				yr = y.split(",");
//				type = (String)visit.getSession().getAttribute("type");
				
				String y = (((Visit) getPage().getVisit()).getString10()+";").split(";")[0];
				yr = y.split(",");
				type = ((Visit) getPage().getVisit()).getString11();
			}
		}
	}
}
