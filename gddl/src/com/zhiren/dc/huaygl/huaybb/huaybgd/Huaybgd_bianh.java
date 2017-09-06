package com.zhiren.dc.huaygl.huaybb.huaybgd;

import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huaybgd_bianh extends BasePage {
	private static final String REPORTNAME_HUAYBGD_ZHILB="zhilb";
	private static final String REPORTNAME_HUAYBGD_ZHILLSB="zhillsb"; //当没有设置资源或者 资源不正确也以此默认取数
	
	private String bianm="";
	private void setBianm(String value){
		bianm=value;
	}
	private String getBianm(){
			return bianm;
	}
	
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

	public String getPrintTable() {
		return getHuaybgd();
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

	//	查询zhillsb的数据
	private StringBuffer getSql_zhillsb(){
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,decode(a.fahrq,null,'',to_char(a.fahrq,'yyyy-mm-dd')) as fahrq,decode(GetCaiyry(y.id),null,'',decode(GetZhiyry(y.id),null,'',GetCaiyry(y.id)"); 
		sqlHuaybgd
				.append("||','||");
		sqlHuaybgd
				.append("GetZhiyry(y.id))) as lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,z.shenhzt,\n");
		sqlHuaybgd
				.append(""
//						+ getBianm()
						+ "zm.bianm"
						+ " as bianh,decode(a.meikdwmc,null,' ',a.meikdwmc) as meikdwmc,decode(a.chez,null,' ',a.chez) as chez,decode(a.pinz,null,' ',a.pinz) as pinz,decode(a.cheph,null,' ',a.cheph) as cheph,nvl(a.ches,0) as ches,nvl(a.meil,0) as meil,'' as beiz,' ' as meikdm from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("f.fahrq as fahrq,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd
				.append("round_new(sum(f.laimsl), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");			
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.id = '" + getBianm() +"'").append("\n");
		sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id,f.fahrq) a \n").append(",zhillsb zl,zhuanmb zm\n");
		sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id(+)\n");
		sqlHuaybgd.append("and y.zhilblsb_id(+)=z.id\n");
		sqlHuaybgd.append("and z.id = '" + getBianm() +"'\n");
		sqlHuaybgd.append("and z.id=zl.zhilb_id and zl.id=zm.zhillsb_id");
//		System.out.println(sqlHuaybgd.toString());
		return sqlHuaybgd;
	}
	
	//查询zhilb的数据
	private StringBuffer getSql_zhilb(){
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiyrq,null,' ',TO_CHAR(Y.CAIYRQ, 'YYYY-MM-DD')) AS CAIYRQ,to_char(a.fahrq,'yyyy-mm-dd') as fahrq,decode(z.lury,null,' ',z.lury) as lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
		sqlHuaybgd
				.append(""
//						+ getBianm()
						+ "zm.bianm"
						+ " as bianh,decode(a.meikdwmc,null,'',a.meikdwmc) as meikdwmc,decode(a.chez,null,'',a.chez) as chez,decode(a.pinz,null,'',a.pinz) as pinz,decode(a.cheph,null,'',a.cheph) as cheph,nvl(a.ches,0) as ches,nvl(a.meil,0) as meil,decode(z.beiz,null,'',z.beiz) as beiz,nvl(y.meikdm,'') as meikdm from zhilb z,caiyb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append("f.fahrq as fahrq,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd
				.append("round_new(sum(f.laimsl), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");			
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhilb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.huaybh = '" + getBianm() +"'").append("\n");
		sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id,f.fahrq) a \n").append(",zhillsb zl,zhuanmb zm\n");
		sqlHuaybgd.append("where z.id=a.zhilb_id\n");
		sqlHuaybgd.append("and z.caiyb_id=y.id\n");
		sqlHuaybgd.append("and y.zhilb_id=z.id\n");
		sqlHuaybgd.append("and z.huaybh = '" + getBianm() +"'\n");
		sqlHuaybgd.append("and z.id=zl.zhilb_id and zl.id=zm.zhillsb_id");
//		System.out.println(sqlHuaybgd.toString());
		return sqlHuaybgd;
	}
	// 
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		boolean isChes = MainGlobal.getXitxx_item("化验", "化验报告单显示车数", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"否").equals("是");
		
		StringBuffer sqlHuaybgd = new StringBuffer();
		
		if(this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)){
			sqlHuaybgd=this.getSql_zhillsb();
		}else if(this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILB)){
			sqlHuaybgd=this.getSql_zhilb();
		}
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String shangjshry = "";
		String lury = "";
		String strKuangb="";
		String strChez="";
		String[][] ArrHeader = new String[18][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");
				Table tb = new Table(1, 1);
				//是否显示车数
				StringBuffer buffer = new StringBuffer();
				StringBuffer bufferChe = new StringBuffer();
				String cheph = "";
				if (!isChes) {
					cheph = rs.getString("CHEPH");
					if (cheph.equals(" ")){
						
					}else{
						String[] list = cheph.split(",");				
						for (int i = 1; i <= list.length; i++) {
							if (i % 9 == 0) {
								buffer.append(list[i - 1] + ",<br>");
							} else {
								buffer.append(list[i - 1] + ",");
							}
						}
						
						
						int intChes=Integer.parseInt(MainGlobal.getXitxx_item("化验", "化验报告单显示车号数量", 
								String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
								String.valueOf(list.length)));
						
						if (intChes>0) {
							if (intChes>=list.length){
								cheph = buffer.toString().substring(0, buffer.length() - 1);
							}else{
								for(int j=1;j<=intChes-1;j++){
									if (j % 9 == 0) {
										bufferChe.append(list[j-1] + ",<br>");
									} else {
										bufferChe.append(list[j - 1] + ",");
									}
								}
								bufferChe.append(list[intChes]);
								cheph = bufferChe.toString().substring(0, bufferChe.length() );
							}
						}
						//显示全部车号

						cheph = buffer.toString().substring(0, buffer.length() - 1);

					}
				}
				
				String num = rs.getString("FRL");
				String strCzy="";
				String strCaiyrq="";
				strCaiyrq=rs.getString("CAIYRQ");
				//当从zhilb中取数时，显示矿别、车站。(二级审核过了)
				if(this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)){
				
					//判断是否显示矿别还是显示代码
					boolean isKuangb = MainGlobal.getXitxx_item("化验", "化验报告单显示矿别", 
							String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
							"是").equals("是");
								
					boolean isMeikdm = MainGlobal.getXitxx_item("化验", "化验报告单显示矿别代码", 
							String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
							"是").equals("是");
					if (isKuangb){
						strKuangb=rs.getString("MEIKDWMC");
						strChez=rs.getString("chez");
					}else{
						if(isMeikdm){
							try{
								String sql="select decode(c.meikdm,null,' ',c.meikdm) as meikdm from caiyb c,zhillsb z where c.zhilb_id=z.zhilb_id and z.huaybh="+getBianm();
								ResultSet rec=con.getResultSet(sql);
								if (rec.next()){
									strKuangb=rec.getString("meikdm");
								}else{
									ResultSet rskb=con.getResultSet("select decode(meikmc,null,'',meikmc) as kuangb,decode(caiyry,null,'',caiyry) as caiyry,decode(zhiyry,null,'',zhiyry) as zhiyry,decode(caiyrq,null,'',to_char(caiyrq,'yyyy-mm-dd')) caiysj from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+getBianm()+"'");
									if (rskb.next()){
										strKuangb=rskb.getString("kuangb");
										strCzy=rskb.getString("caiyry") + " "+ rskb.getString("zhiyry");
										strCaiyrq=rskb.getString("caiysj");
									}else{
										strKuangb="";
										strCzy="";
										strCaiyrq="";
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							strChez="";
						}else{
							strKuangb="";
							strChez="";
						}
						if (rs.getLong("shenhzt")==7){
							strKuangb=rs.getString("MEIKDWMC");
							strChez=rs.getString("chez");
						}
					}
				}else if(this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILB)){
					strKuangb=rs.getString("MEIKDWMC");
					strChez=rs.getString("chez");
				}
				ArrHeader[0] = new String[] { "矿别",
						"" + strKuangb + "", "化验日期",
						"" + rs.getString("HUAYRQ") + "", "编号",
						"" + rs.getString("BIANH") + "" };

				ArrHeader[1] = new String[] { "发站",
						"" + strChez + "", "采样日期",
						"" + strCaiyrq + "", "煤量(t)",
						"" + rs.getString("MEIL") + "" };
				
//				ArrHeader[0] = new String[] { "化验编号",
//						"" + rs.getString("BIANH") + "", "矿别",
//						"" + strKuangb + "", "发站",
//						"" + strChez + "" };
//
//				ArrHeader[1] = new String[] { "化验日期",
//						"" + rs.getString("HUAYRQ") + "", "采样日期",
//						"" + strCaiyrq + "", "煤量(t)",
//						"" + rs.getString("MEIL") + "" };
//				String strFahrq="";
				String strLury="";				
//				if (rs.getString("fahrq")==null){
//					strFahrq="";
//				}else{
//					strFahrq=rs.getString("fahrq");
//				}
				if (rs.getString("lurry")==null){
					strLury=strCzy;
				}else{
					strLury=rs.getString("lurry");
				}
				
				
				ArrHeader[2] = new String[] { "采制样人员", "" + strLury + "",
						"" + strLury + "", "" + strLury + "", "" + strLury + "",
						"" + strLury + "" };
				
//				ArrHeader[2] = new String[] { "发货日期",
//						"" + strFahrq + "","煤种",
//						"" + rs.getString("PINZ") + "", "采制样人员",
//						"" + strLury + ""};
				
				//是否显示车数
				if (isChes){
					ArrHeader[3] = new String[] { "车数:", "&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"), "&nbsp;&nbsp;" + rs.getString("ches"), 
							"&nbsp;&nbsp;" + rs.getString("ches"),"&nbsp;&nbsp;" + rs.getString("ches")};
				} else {
					ArrHeader[3] = new String[] { "车号", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
				}
				ArrHeader[4] = new String[] { "全水分Mt(%)", "全水分Mt(%)",
						"全水分Mt(%)", "" + rs.getDouble("MT") + "", "备注", "备注" };
				ArrHeader[5] = new String[] { "空气干燥基水分Mad(%)", "空气干燥基水分Mad(%)",
						"空气干燥基水分Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[6] = new String[] { "空气干燥基灰分Aad(%)", "空气干燥基灰分Aad(%)",
						"空气干燥基灰分Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[7] = new String[] { "收到基灰分Aar(%)", "收到基灰分Aar(%)",
						"收到基灰分Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[8] = new String[] { "干燥基灰分Ad(%)", "干燥基灰分Ad(%)",
						"干燥基灰分Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[9] = new String[] { "空气干燥基挥发分Vad(%)",
						"空气干燥基挥发分Vad(%)", "空气干燥基挥发分Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[10] = new String[] { "干燥无灰基挥发分Vdaf(%)",
						"干燥无灰基挥发分Vdaf(%)", "干燥无灰基挥发分Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[11] = new String[] { "空气干燥基全硫St,ad(%)",
						"空气干燥基全硫St,ad(%)", "空气干燥基全硫St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[12] = new String[] { "干燥基全硫St,d(%)", "干燥基全硫St,d(%)",
						"干燥基全硫St,d(%)", "" + rs.getDouble("STD") + "", "", "" };
				
				ArrHeader[13] = new String[] { "空气干燥基氢Had(%)", "空气干燥基氢Had(%)",
						"空气干燥基氢Had(%)", "" + rs.getDouble("HAD") + "", "", "" };
				
				ArrHeader[14] = new String[] { "弹筒热值Qb,ad(MJ/Kg)",
						"弹筒热值Qb,ad(MJ/Kg)", "弹筒热值Qb,ad(MJ/Kg)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[15] = new String[] { "干燥基高位热值Qgr,d(MJ/Kg)",
						"干燥基高位热值Qgr,d(MJ/Kg)", "干燥基高位热值Qgr,d(MJ/Kg)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[16] = new String[] { "空气干燥基高位热值Qgr,ad(MJ/Kg)",
						"空气干燥基高位热值Qgr,ad(MJ/Kg)", "空气干燥基高位热值Qgr,ad(MJ/Kg)",
						"" + rs.getDouble("QGRAD") + "", "", "" };
				
				ArrHeader[17] = new String[] { "收到基低位热值Qnet,ar(MJ/Kg)",
						"收到基低位热值Qnet,ar(MJ/Kg)", "收到基低位热值Qnet,ar(MJ/Kg)",
						tb.format("" + rs.getDouble("QNETAR") + "","0.00"),
						"" + num + "" + "(千卡/千克)", "" + num + "" + "(千卡/千克)" };
				String beiz = rs.getString("beiz");
				
				if (beiz!=null && !"".equals(beiz)) {
					for (int i=5; i<=16; i++) {
						ArrHeader[i][4] = beiz;
						ArrHeader[i][5] = beiz;
					}
				}
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };
		Visit visit=(Visit)this.getPage().getVisit();
		
		rt.setTitle(visit.getDiancqc()+"<br>"+"煤  质  检  验  报  告", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

//		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		//rt.setDefautlFooter(1, 2, "打印日期:" + str, -1);
		rt.setDefautlFooter(2, 1, "负责人：", -1);
		rt.setDefautlFooter(4, 1, "审核：" + shangjshry, -1);
		rt.setDefautlFooter(5, 2, "化验员：" + lury, -1);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(18, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		for (int i = 1; i < 18; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 18; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(5, 4, rt.body.format(rt.body.getCellValue(5, 4),
				"0.0"));
		for (int i = 6; i < 18; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
		
		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(2, 1, 18, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(3, 2, 3, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(5, 1, 18, 3);
		rt.body.merge(5, 5, 18, 6);
		rt.body.merge(4, 2, 4, 6);
		rt.body.merge(3, 2, 3, 6);
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());

		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			String lxs=cycle.getRequestContext().getParameters("lx")[0];
			String[] lx=lxs.split(",");
			setReportName(lx[0]);
			setBianm(lx[1]);
		} else {
			this.setReportName("");
		}
	}
	
	private void setReportName(String name){
		Visit visit = (Visit) getPage().getVisit();
		
		if(name==null){
			visit.setString13(REPORTNAME_HUAYBGD_ZHILLSB);
		}
		if(name.equals(REPORTNAME_HUAYBGD_ZHILB)){
			visit.setString13(REPORTNAME_HUAYBGD_ZHILB);
		}else{
			visit.setString13(REPORTNAME_HUAYBGD_ZHILLSB);
		}
	}
	private String getReportName(){
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString13();
	}
	
//	class Bianm{
//		long id;
//		String name;
//		private 
//	}
}
