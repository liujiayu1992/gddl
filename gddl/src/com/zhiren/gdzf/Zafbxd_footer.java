package com.zhiren.gdzf;

import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Cell;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ����
 * ʱ�䣺2013-05-30
 * ���÷�Χ�����������ʤ
 * ���������Ӻϼ���Сд�����ʾ
 */
/*
 * ���ߣ���ռ��
 * ʱ�䣺2013-06-26
 * ���÷�Χ�����������ʤ
 * ���������ĺϼ���Сд�����ʾ
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-07
 * ���÷�Χ���������ʯ��ɽ
 * ������Ϊʯ��ɽ�������������ѯ����
 */
public class Zafbxd_footer extends BasePage {
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
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
	
	public StringBuffer getBaseSql() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
			sb.append("select bxd.bianh, to_char(bxd.riq, 'yyyy-mm-dd') riq, r.quanc as jingbr, r.bum\n" +
							"  from zaffybxd bxd,renyxxb r\n" + 
							" where bxd.bianh = '"+visit.getString5()+"' and bxd.renyxxb_id=r.id");
		return sb;
	}
	
	public StringBuffer getJineSql() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
			sb.append("select djb.beiz, round(djb.danj * djb.shul, 2) jine\n" +
							"  from zaffybxd bxd, ZAFFYBXD_ZAFHTFYDJB zjb, ZAFHTFYDJB djb\n" + 
							" where zjb.zaffybxd_id = bxd.id\n" + 
							"   and zjb.zafhtfydjb_id = djb.id\n" + 
							"   and bxd.bianh = '"+visit.getString5()+"'");
		return sb;
	}
	
	public String getPrintTable(){
		if(MainGlobal.getXitxx_item("���ڷ���", "ʯ��ɽ���ڷ�����ʽ", "0", "��").equals("��")){
			return SZSBB();
		}else{
			return DefaultBB();
		}
	}
	
	public String DefaultBB(){
		Visit visit = (Visit) getPage().getVisit();
		Report rt=new Report();
		JDBCcon con = new JDBCcon();
		if(visit.getString5()==""){
			con.Close();
			return "�޴˱���";
		}
		try{		
			ResultSet rs = con.getResultSet(getBaseSql(),
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (rs == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult);
				setMsg(ErrorMessage.NullResult);
				return "";
			}
			String bianh = "";
			String riq = "";
			String jingbr = "";
			String bum = "";
			while(rs.next()){
				bianh = rs.getString("bianh");
				riq = rs.getString("riq");
				jingbr = rs.getString("jingbr");
				bum = rs.getString("bum");

			}
			rs.close();
			bianh = "��ţ�"+bianh;
			riq = riq.split("-")[0]+"��"+riq.split("-")[1]+"��"+riq.split("-")[2]+"��";
			rs.close();
			 String ArrHeader[][]=new String[6][3];
			 ArrHeader[0]=new String[] {"","���������չ�ɷ����޹�˾",""};
			 ArrHeader[1]=new String[] {"","�������ɹŶ�ʤ�ȵ����޹�˾",""};
			 ArrHeader[2]=new String[] {"","",""};
			 ArrHeader[3]=new String[] {"","���ñ�����",""};
			 ArrHeader[4]=new String[] {"","",""};
			 ArrHeader[5]=new String[] {bianh,riq,""};
			 
			ResultSet rsb = con.getResultSet(getJineSql(),
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (rsb == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult);
				setMsg(ErrorMessage.NullResult);
				return "";
			}
			
			 String ArrBody[][]=new String[8][20];
			ArrBody[0]=new String[] {"������",jingbr,"","","","��������",bum,"","","","���","","","","","","","","",""};
			ArrBody[1]=new String[] {"���","��������˵��","","","","","","","","","Ǫ","��","ʮ","��","Ǫ","��","ʮ","Ԫ","��","��"};
			int i = 2;
			String xuh = "";
			double hej = 0;
			while(rsb.next()){
				if(i>6){
					break;
				}
				xuh = (i-1)+"";
				hej += rsb.getDouble("jine");
				String a =  rsb.getDouble("jine")+"";
				String b = a.substring(0, a.indexOf("."));
				String c = a.substring(a.indexOf(".")+1,a.length());
				switch(b.length()){
				case 0 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","","","","","","","",""};
				break;
				case 1 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","","","","","",b.charAt(0)+"","0","0"};
				break;
				case 2 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"","0","0"};
				break;
				case 3 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"","0","0"};
				break;
				case 4 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"","0","0"};
				break;
				case 5 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"","0","0"};
				break;
				case 6 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"","0","0"};
				break;
				case 7 :  ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"",b.charAt(6)+"","0","0"};
				break;
				case 8 : ArrBody[i] = new String[] {xuh,rsb.getString("beiz"),"","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"",b.charAt(6)+"",b.charAt(7)+"","0","0"};
				break;
				};
				switch (c.length()){
				case 1 : ArrBody[i][18] = c.charAt(0)+"";
				break;
				case 2 : ArrBody[i][18] = c.charAt(0)+"";
				ArrBody[i][19] = c.charAt(1)+"";
				break;
				}
				i=i+1;
			}
			rsb.close();
			for(int m=i;m<8;m++){
				ArrBody[m]=new String[] {(m-1)+"","","","","","","","","","","","","","","","","","","",""};		
			}
			hej = CustomMaths.round(hej, 2);
			String daxje="����ң���д����"+digitUppercase(hej);
//			ArrBody[7]=new String[] {"�ϼ�",daxje,"","","","","","","","","","","","","","","","","",""};
//			���¹����ϼ���
			String a =  hej+"";
			String b = a.substring(0, a.indexOf("."));
			String c = a.substring(a.indexOf(".")+1,a.length());
			switch(b.length()){
			case 0 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","","","","","","","",""};
			break;
			case 1 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","","","","","",b.charAt(0)+"","0","0"};
			break;
			case 2 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"","0","0"};
			break;
			case 3 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"","0","0"};
			break;
			case 4 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"","0","0"};
			break;
			case 5 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"","0","0"};
			break;
			case 6 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"","0","0"};
			break;
			case 7 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"",b.charAt(6)+"","0","0"};
			break;
			case 8 : ArrBody[7] = new String[] {"�ϼ�",daxje,"","","","","","","","",b.charAt(0)+"",b.charAt(1)+"",b.charAt(2)+"",b.charAt(3)+"",b.charAt(4)+"",b.charAt(5)+"",b.charAt(6)+"",b.charAt(7)+"","0","0"};
			break;
			};
			switch (c.length()){
			case 1 : ArrBody[7][18] = c.charAt(0)+"";
			break;
			case 2 : ArrBody[7][18] = c.charAt(0)+"";
					 ArrBody[7][19] = c.charAt(1)+"";
			break;
			}
//			�ϼ��й������
			
			int ArrWidth[]=new int[] {84,64,84,84,84,84,84,74,74,54,30,30,30,30,30,30,30,30,30,30};
			int ArrWidthTitle[]=new int[] {400,280,400};
			//����ҳTitle
	
			rt.setTitle(new Table(ArrHeader,0,0,0));
			rt.setBody(new Table(ArrBody,0,0,0));
			rt.body.setWidth(ArrWidth);
			rt.body.ShowZero = true;
			rt.title.setWidth(ArrWidthTitle);
			rt.title.setBorder(0,0,0,0);
			 rt.title.setCellAlign(1, 2, Table.ALIGN_CENTER);
			 rt.title.setCellAlign(2, 2, Table.ALIGN_CENTER);
			 rt.title.setCellAlign(5, 2, Table.ALIGN_CENTER);
			 rt.title.setCellAlign(6, 2, Table.ALIGN_CENTER);
			 rt.title.setCellAlign(4, 2, Table.ALIGN_CENTER);
			 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
			 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
			 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
			 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
			 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
			 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
					 
			 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
			 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
			 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
			 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
			 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
			 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
			 
			 rt.title.setCells(1, 2, 1, 2, Table.PER_FONTNAME, "����");
			 rt.title.setCells(1, 2, 1, 2, Table.PER_FONTSIZE, 13);
			 rt.title.setCells(2, 1, 2, 2, Table.PER_FONTNAME, "Arial Unicode MS");
			 rt.title.setCells(2, 1, 2, 2, Table.PER_FONTSIZE, 15);
			 rt.title.setCells(4, 1, 4, 2, Table.PER_FONTNAME, "����");
			 rt.title.setCells(4, 1, 4, 2, Table.PER_FONTSIZE, 20);
			 rt.title.setCellImage(1, 1, 120, 60, "imgs/report/GDBZ.gif");	//����ı�־�����ֳ�Ҫһ�����Ͼ����ˣ�
			 rt.title.setCellImage(5, 2, 140, 10, "imgs/report/GDHX.gif");
			//�ϲ���Ԫ��	
			rt.body.mergeCell(1,2,1,5);
			rt.body.mergeCell(1,7,1,10);
			rt.body.mergeCell(1,11,1,20);
			rt.body.mergeCell(2,2,2,10);
			rt.body.mergeCell(3,2,3,10);
			rt.body.mergeCell(4,2,4,10);
			rt.body.mergeCell(5,2,5,10);
			rt.body.mergeCell(6,2,6,10);
			rt.body.mergeCell(7,2,7,10);
			rt.body.mergeCell(8,2,8,10);
			//��������
			 rt.body.setColAlign(1, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(1, 2, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(1, 6, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(1, 7, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(1, 11, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(2, 2, Table.ALIGN_CENTER);
			 for(int w=11;w<21;w++){
				 rt.body.setCellAlign(2, w, Table.ALIGN_CENTER);
			 }
			 for(int x=3;x<=8;x++){
				 for(int y=10;y<=20;y++){
					 rt.body.setCellAlign(x, y, Table.ALIGN_RIGHT);
				 }
			 }
			 _CurrentPage = 1;
				_AllPages=1;
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
		}catch(Exception e) {
			// TODO �Զ����ɷ������
					e.printStackTrace();
		}
		
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public String SZSBB(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		
		String SQL="SELECT DECODE(GROUPING(MX.MINGC), 1, '�ϼ�', MX.MINGC) MINGC,\n" +
			"       SUM(DJB.SHUL) SHUL,\n" + 
			"       DJB.DANJ,\n" + 
			"       SUM(ROUND(DJB.DANJ * DJB.SHUL, 2)) JINE,\n" + 
			"       TO_CHAR(DJB.QISRQ, 'yyyy.mm.dd') || '��' || TO_CHAR(DJB.JIESRQ, 'yyyy.mm.dd') JIESZQ,\n" + 
			"       DJB.BEIZ\n" + 
			"  FROM ZAFFYBXD            BXD,\n" + 
			"       ZAFFYBXD_ZAFHTFYDJB ZJB,\n" + 
			"       ZAFHTFYDJB          DJB,\n" + 
			"       CHANGNFYXMB         MX\n" + 
			" WHERE ZJB.ZAFFYBXD_ID = BXD.ID\n" + 
			"   AND ZJB.ZAFHTFYDJB_ID = DJB.ID\n" + 
			"   AND MX.SHIYZT = 1\n" + 
			"   AND DJB.CHANGNFYXMB_ID = MX.ID\n" + 
			"   AND BXD.BIANH = '"+visit.getString5()+"'\n" + 
			" GROUP BY ROLLUP((MX.MINGC, DJB.DANJ, TO_CHAR(DJB.QISRQ, 'yyyy.mm.dd') || '��' ||TO_CHAR(DJB.JIESRQ, 'yyyy.mm.dd'), DJB.BEIZ))";
		
		ArrHeader=new String[1][6];
		ArrHeader[0]=new String[] {"��Ŀ","��������(��)","����(Ԫ/��)","������(Ԫ)","��������","��ע"};
		
		ArrWidth=new int[] {150,150,150,150,150,300};
		arrFormat=new String[]{"","0.00","0.00","0.00","",""};
		
		String btSql=
			"SELECT TO_CHAR(Z.RIQ, 'yyyy\"��\"mm\"��\"dd\"��\"') RIQ, DC.QUANC\n" +
			"  FROM ZAFFYBXD Z, DIANCXXB DC\n" + 
			" WHERE Z.DIANCXXB_ID = DC.ID\n" + 
			"   AND Z.BIANH = '"+visit.getString5()+"'";

		ResultSetList rs = con.getResultSetList(btSql);
		String riq = "";
		String dcqc="";
		while(rs.next()){
			riq = rs.getString("riq");
			dcqc	= rs.getString("quanc");
		}
		
		Cell c = new Cell();
		c.setBorderNone();
		Table title = new Table(8, ArrWidth.length,c);
		title.setWidth(ArrWidth);
		title.setBorderNone();
		title.setCellValue(2, 1, "���������չ�ɷ����޹�˾");
		title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		title.setCellFont(2, 1, "", 14, true);
		title.mergeRowCells(2);
		title.setRowHeight(3, 10);
		
		title.setCellValue(4, 1, dcqc);
		title.setCellAlign(4, 1, Table.ALIGN_CENTER);
		title.setCellFont(4, 1, "", 20, true);
		title.mergeRowCells(4);
		title.setRowHeight(5, 10);
		
		title.setCellValue(6, 1, "<span style=\"border-bottom:2pt double #000000\"> ��ú���������ѽ��㵥</span>");
		title.setCellAlign(6, 1, Table.ALIGN_CENTER);
		title.setCellFont(6, 1, "", 16, true);
		title.mergeRowCells(6);
		title.setRowHeight(6, 40);
		title.setRowHeight(7, 10);
		
		title.setCellValue(8, 1, "��λ�����ĺӱ�������������ţ��ɷ����޹�˾");
		title.setCellAlign(8, 1, Table.ALIGN_LEFT);
		title.setCellFont(8, 1, "", 10, false);
		title.mergeCell(8, 1, 8, 4);
		
		title.setCellValue(8, 5, "��������:"+riq);
		title.setCellAlign(8, 5, Table.ALIGN_RIGHT);
		title.setCellFont(8, 5, "", 10, false);
		title.mergeCell(8, 5, 8, 6);
		rt.setTitle(title);
		// ����
		rs = con.getResultSetList(SQL);
		Table tb = new Table(rs,1, 0, 1);
		rt.setBody(tb);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = false;
		rt.body.setColFormat(arrFormat);
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
			
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
		 
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	/**
	 * ���ֽ���дת����˼����д��������Ȼ������ʰ�滻����
	 * Ҫ�õ�������ʽ
	 */
	public static String digitUppercase(double n){
		String fraction[] = {"��", "��"};
	    String digit[] = { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };
	    String unit[][] = {{"Ԫ", "��", "��"},
	                 {"", "ʰ", "��", "Ǫ"}};

	    String head = n < 0? "��": "";
	    n = Math.abs(n);
	    
	    String s = "";
	    for (int i = 0; i < fraction.length; i++) {
	        s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(��.)+", "");
	    }
	    if(s.length()<1){
		    s = "��";	
	    }
	    int integerPart = (int)Math.floor(n);

	    for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
	        String p ="";
	        for (int j = 0; j < unit[1].length && n > 0; j++) {
	            p = digit[integerPart%10]+unit[1][j] + p;
	            integerPart = integerPart/10;
	        }
	        s = p.replaceAll("(��.)*��$", "").replaceAll("^$", "��") + unit[0][i] + s;
	    }
	    return head + s.replaceAll("(��.)*��Ԫ", "Ԫ").replaceFirst("(��.)+", "").replaceAll("(��.)+", "��").replaceAll("^��$", "��Ԫ��");
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			String reportType = cycle.getRequestContext().getParameter("bh");
			if(reportType != null) {
				visit.setString5(reportType);
			}
			visit.setActivePageName(getPageName().toString());
			getPrintTable();
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
