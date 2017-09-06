package com.zhiren.shanxdted.zhonghtj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ����:tzf
 * ʱ�䣺2009-12-16
 * ���ݣ����ڼ򱨺���ͨ��ʽ  ���� �糧����
 *//*
 * ����:tzf
 * ʱ��:2009-11-23
 * ����:����Ȩ��  �鿴��糧  �򱨵�sql����
 */
public class Shultjeq extends BasePage implements PageValidateListener {

	private static String QY_M = "����úС��";
	private static String HY_M = "��úС��";
	private static String ZJ_M = "�ܼ�";
	private static String DC_M = "�ϼ�";
	private static String CHES = "*";
	
	private boolean isMx = false;
	
//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
//	������
	private String briq;
	private boolean briqBoo=false;
	public String getBRiq() {
		if ("".equals(briq) || briq==null) {
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq!=null && !this.briq.equals(briq)){
			briqBoo=true;
		}
		this.briq = briq;
	}
	
//	������
	private String eriq;
	private boolean eriqBoo=false;
	public String getERiq() {
		if ("".equals(eriq) || eriq==null) {
			eriq = DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {
		if(this.eriq!=null && !this.eriq.equals(eriq)){
			eriqBoo=true;
		}
		this.eriq = eriq;
	}
	
	public boolean getRaw() {
		return true;
	}
    
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid in (" + id + ")";
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
//	��ȡ��ص�SQL

	public String getBaseDcSql() {
		String diancid = "" ;
		
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " where d.fgsid in ( " + getTreeid_dc() + ")";
			} else {
				diancid = " where d.id in ("+ getTreeid_dc() + ")";
			}		
		}
		return diancid;
    }
	
	public String getBaseMeikSql() {
		return null;
	}
	
	public String getBaseYunsdwSql() {
		return null;
	}
	
	public String getBaseHavingSql() {
		String havingSql = "";		
		String gs = "";
		
		if (!"".equals(getGesValue().getValue()) && getGesValue().getValue()!=null) {
			gs = getGesValue().getValue();
		} else {
			gs = ((IDropDownBean)getGesModel().getOption(0)).getValue();
		}
		
		if (gs.equals("��ͨ")) {
			havingSql = 
				"GROUP BY GROUPING SETS(1,(FX.DC, FX.YUNSFSB_ID, FX.MK, FX.MKDQ, CX.YSDW),(FX.DC, FX.YUNSFSB_ID),(FX.YUNSFSB_ID),(fx.dc))\n" +
				"ORDER BY fx.dc,yunsfsb_id,FX.MKDQ,GROUPING(fx.mk)";
			
		} else if (gs.equals("��ϸ")) {
			havingSql = 
				" GROUP BY GROUPING SETS(1,(CP.ID, FH.DC, FH.YUNSFSB_ID, FH.MK,meikdqb.mingc, CP.YSDW, CP.CHEPH),(FH.DC, FH.YUNSFSB_ID, FH.MK,meikdqb.mingc, CP.YSDW),(FH.DC, FH.YUNSFSB_ID),(FH.DC),(FH.YUNSFSB_ID))\n" +
				"ORDER BY FH.DC,FH.YUNSFSB_ID,meikdqb.mingc,FH.MK,CP.YSDW,CH DESC,MAX(DZSJ)";

		} else if (gs.equals("��")) {
			havingSql = 
				"GROUP BY GROUPING SETS(1,(FX.DC, FX.YUNSFSB_ID, FX.MK, FX.MKDQ, CX.YSDW),(FX.DC, FX.YUNSFSB_ID),(FX.YUNSFSB_ID),(fx.dc))\n" +
				"HAVING NOT GROUPING(fx.dc)+GROUPING(cx.ysdw)=0\n" +
				"ORDER BY fx.dc,yunsfsb_id,FX.MKDQ,GROUPING(fx.mk)";
		}

		return havingSql;
	}
	
	private String getBaseSql(){
		Visit visit = (Visit) getPage().getVisit();
		String gs = "";
		String mk = "";
		String mkWhere = "";
		String mkMxWhere = "";
		String ysdw = "";
		String ysdwWhere = "";
		String ysdwMxWhere = "";
		String mkdq = "";
		String mkdqWhere = "";
		String mkdqMxWhere = "";
		String SQL = "";
		
		if (!"".equals(getGesValue().getValue()) && getGesValue().getValue()!=null) {
			gs = getGesValue().getValue();
		} else {
			gs = ((IDropDownBean)getGesModel().getOption(0)).getValue();
		}
		
		if (!"".equals(getMeikValue().getValue()) && getMeikValue().getValue()!=null) {
			mk = getMeikValue().getValue();
		} else {
			mk = ((IDropDownBean)getMeikModel().getOption(0)).getValue();
		}
		
		if (!"".equals(getYunsdwValue().getValue()) && getYunsdwValue().getValue()!=null) {
			ysdw = getYunsdwValue().getValue();
		} else {
			ysdw = ((IDropDownBean)getYunsdwModel().getOption(0)).getValue();
		}
		
		if (!"".equals(getMeikdqValue().getValue()) && getMeikdqValue().getValue()!=null) {
			mkdq = getMeikdqValue().getValue();
		} else {
			mkdq = ((IDropDownBean)getMeikdqModel().getOption(0)).getValue();
		}
		
		if ("ȫ��".equals(mk)) {
			mkWhere = "";
			mkMxWhere = "";
		} else {
			mkWhere = " AND M.MINGC='" + mk + "'";
			mkMxWhere = " AND FH.MK='" + mk + "'";
		}
		
		if ("ȫ��".equals(ysdw)) {
			ysdwWhere = "";
			ysdwMxWhere = "";
		} else {
			ysdwWhere = " AND Y.MINGC='" + ysdw + "'";
		}
		if ("ȫ��".equals(mkdq)) {
			mkdqWhere = "";
			mkdqMxWhere = "";
		} else {
			mkdqWhere = " AND MDQ.mingc='" + mkdq + "'";
			mkdqMxWhere = " AND meikdqb.mingc='" + mkdq + "'";
		}
		
		//��ж����ʽ��ѯʱ����ȥ��Ƥ����������Ĭ��ȡ��������
		String xiecfs="";
		String chesWhere = "SUM(FX.CHES) CHES,\n";
		String chesMxWhere = "SUM(FH.CHES),\n";
		if(this.getXiecfsValue().getStrId().equals("0")){//ȫ��ж����ʽ
			xiecfs="  ";
		}else{//����ж����ʽ
			xiecfs=" and xiecfsb_id="+this.getXiecfsValue().getStrId()+" ";
			chesWhere = "		SUM(CX.CHES) CHES,\n";
			chesMxWhere = "SUM(CP.CHES),\n";
		}
		
		//���䷽ʽ
		String yunsfsStr="";
		String yunsfsStr1="";
		if(!this.getYunsfsValue().getStrId().equals("0")){
			yunsfsStr=" and f.yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
			yunsfsStr1=" and yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
		}
		
		String fahb_dcid=" and (f.diancxxb_id in ("+this.getTreeid_dc()+") or f.diancxxb_id in (select id from diancxxb where fuid in ("+this.getTreeid_dc()+"))) ";
		
		if (gs.equals("��ͨ") || gs.equals("��")){
			SQL = 
				"SELECT DECODE(FX.DC, NULL, '�ϼ�', SUBSTR(FX.DC, 2)) AS DIANCMC,\n" +
				"       DECODE(FX.YUNSFSB_ID,\n" + 
				"              NULL,\n" + 
				"              DECODE(FX.DC,\n" + 
				"                     NULL,\n" + 
				"                     MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                     MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')),\n" + 
				"                     '�ܼ�' || '(' || MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                     MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || ')'),\n" + 
				"              DECODE(FX.MKDQ,\n" + 
				"                     NULL,\n" + 
				"                     DECODE(FX.YUNSFSB_ID,\n" + 
				"                            '��·',\n" + 
				"                            '����úС��',\n" + 
				"                            '��·',\n" + 
				"                            '��úС��'),\n" + 
				"                     FX.MKDQ)) AS MEIKDQ,\n" + 
				"       DECODE(FX.YUNSFSB_ID,\n" + 
				"              NULL,\n" + 
				"              DECODE(FX.DC,\n" + 
				"                     NULL,\n" + 
				"                     MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                     MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')),\n" + 
				"                     '�ܼ�' || '(' || MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                     MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || ')'),\n" + 
				"              DECODE(FX.MK,\n" + 
				"                     NULL,\n" + 
				"                     DECODE(FX.YUNSFSB_ID,\n" + 
				"                            '��·',\n" + 
				"                            '����úС��',\n" + 
				"                            '��·',\n" + 
				"                            '��úС��'),\n" + 
				"                     FX.MK)) AS MEIKDW,\n" + 
				"       DECODE(CX.YSDW,\n" + 
				"              NULL,\n" + 
				"              DECODE(FX.MK,\n" + 
				"                     NULL,\n" + 
				"                     DECODE(FX.YUNSFSB_ID,\n" + 
				"                            '��·',\n" + 
				"                            '����úС��',\n" + 
				"                            '��·',\n" + 
				"                            '��úС��',\n" + 
				"                            NULL,\n" + 
				"                            DECODE(FX.DC,\n" + 
				"                                   NULL,\n" + 
				"                                   MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                                   MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')),\n" + 
				"                                   '�ܼ�' || '(' ||\n" + 
				"                                   MIN(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"                                   MAX(TO_CHAR(FX.DAOHRQ, 'yyyy-mm-dd')) || ')'))),\n" + 
				"              CX.YSDW) AS YUNSDW,\n" + 
				chesWhere +
				"       SUM(ROUND_NEW(CX.MAOZ, "+visit.getShuldec()+")) MAOZ,\n" + 
				"       SUM(ROUND_NEW(CX.PIZ + CX.ZONGKD, "+visit.getShuldec()+")) PIZ,\n" + 
				"       SUM(ROUND_NEW(CX.JINGZ, "+visit.getShuldec()+")) JINGZ\n" + 
//				"       SUM(ROUND_NEW(CX.ZONGKD, "+visit.getShuldec()+")) ZONGKD,\n" + 
//				"       SUM(ROUND_NEW(CX.BIAOZ, "+visit.getShuldec()+")) BIAOZ,\n" + 
//				"       SUM(ROUND_NEW(CX.YUNS, "+visit.getShuldec()+")) YUNS,\n" + 
//				"       SUM(ROUND_NEW(CX.YINGK, "+visit.getShuldec()+")) YINGK\n" + 
				"\n" + 
				"  FROM (SELECT FH.ID, M.MINGC MK,MDQ.MINGC AS MKDQ," +
				" (select xuh||mingc from diancxxb where id=fh.diancxxb_id) dc,\n"+
				" FH.YUNSFSB_ID, FH.DAOHRQ, FH.CHES\n" + 
				"          FROM (SELECT F.ID,\n" + 
				"                       F.DIANCXXB_ID,\n" + 
				"                       F.MEIKXXB_ID,\n" + 
				"                       F.DAOHRQ,\n" + 
				"                       F.CHES,\n" + 
				"                       (SELECT YF.MINGC\n" + 
				"                          FROM YUNSFSB YF\n" + 
				"                         WHERE YF.ID = F.YUNSFSB_ID) YUNSFSB_ID\n" + 
				"                  FROM FAHB F\n" + 
				"                 WHERE F.DAOHRQ >= TO_DATE('" + this.getBRiq()+ "', 'yyyy-mm-dd')\n" + 
				"                   AND F.DAOHRQ <= TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')"+fahb_dcid+yunsfsStr+") FH,\n" + 
				"\n" + 
				"               MEIKXXB M, \n" + 
				"               MEIKDQB MDQ \n" +
				"         WHERE" +
				" FH.MEIKXXB_ID = M.ID AND m.meikdq2_id=mdq.id " + mkWhere + mkdqWhere + ") FX,\n" + 
				"       (SELECT CP.FAHB_ID,\n" + 
				"               SUM(CP.MAOZ) MAOZ,\n" + 
				"               SUM(CP.PIZ) PIZ,\n" + 
				"               SUM(DECODE(QINGCSJ,NULL,0,CP.BIAOZ)) BIAOZ,\n" + 
				"               SUM(CP.YUNS) YUNS,\n" + 
				"               SUM(CP.YINGK) YINGK,\n" + 
				"               SUM(CP.ZONGKD) ZONGKD,\n" + 
				"               SUM(DECODE(QINGCSJ,NULL,0,(CP.MAOZ - CP.PIZ - CP.ZONGKD))) JINGZ,\n" + 
				"               COUNT(*) CHES,\n" + 
				"               Y.MINGC YSDW\n" + 
				"          FROM CHEPB CP, YUNSDWB Y\n" + 
				"         WHERE CP.YUNSDWB_ID = Y.ID(+) " + ysdwWhere + xiecfs+ "\n" +
				"          AND CP.FAHB_ID IN\n" + 
				"               (SELECT ID\n" + 
				"                  FROM FAHB\n" + 
				"                 WHERE DAOHRQ BETWEEN TO_DATE('" + this.getBRiq()+ "', 'yyyy-mm-dd') AND\n" + 
				"                       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')"+yunsfsStr1+xiecfs+")\n" + 
				"          GROUP BY CP.FAHB_ID, Y.MINGC) CX\n" + 
				" WHERE FX.ID = CX.FAHB_ID\n" + getBaseHavingSql();
			
			isMx = false;
			
		} else if (gs.equals("��ϸ")) {
			SQL = 
				"SELECT DECODE(FH.DC, NULL, '�ϼ�', SUBSTR(FH.DC, 2)) AS DC,\n" + 
				"       DECODE(meikdqb.mingc,\n" + 
				"              NULL,\n" + 
				"              DECODE(FH.YUNSFSB_ID,\n" + 
				"                     NULL,\n" + 
				"                     '�ܼ�',\n" + 
				"                     '��·',\n" + 
				"                     '����úС��',\n" + 
				"                     '��·',\n" + 
				"                     '��úС��'),\n" + 
				"               meikdqb.mingc) AS MKDQ,\n" + 
				"       DECODE(FH.MK,\n" + 
				"              NULL,\n" + 
				"              DECODE(FH.YUNSFSB_ID,\n" + 
				"                     NULL,\n" + 
				"                     '�ܼ�',\n" + 
				"                     '��·',\n" + 
				"                     '����úС��',\n" + 
				"                     '��·',\n" + 
				"                     '��úС��'),\n" + 
				"              FH.MK) AS MK,\n" + 
				"       DECODE(CP.YSDW, NULL, '����', CP.YSDW) AS YSDW,\n" + 
				"       DECODE(CP.CHEPH,\n" +
				"               NULL,\n" + 
				"               DECODE(FH.MK, NULL, '', '*') || " + chesMxWhere +
				"               CP.CHEPH) AS CH,\n" +
				"       SUM(ROUND_NEW(CP.MAOZ, "+visit.getShuldec()+")) AS MZ,\n" + 
				"       SUM(ROUND_NEW(CP.PIZ+CP.KOUD, "+visit.getShuldec()+")) AS PZ,\n" + 
				"       SUM(ROUND_NEW(CP.JINGZ, "+visit.getShuldec()+")) AS JZ,\n" + 
//				"       SUM(ROUND_NEW(CP.KOUD, "+visit.getShuldec()+")) AS KD,\n" + 
//				"       SUM(ROUND_NEW(CP.BIAOZ, "+visit.getShuldec()+")) AS BZ,\n" + 
//				"       SUM(ROUND_NEW(CP.YUNS, "+visit.getShuldec()+")) AS YUNS,\n" + 
//				"       SUM(ROUND_NEW(CP.YINGK, "+visit.getShuldec()+")) AS YINGK,\n" + 
				"DECODE(CP.CHEPH,\n" +
				"       NULL,\n" + 
				"       MIN(TO_CHAR(DZSJ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"       MAX(TO_CHAR(DZSJ, 'yyyy-mm-dd')),\n" + 
				"       MAX(CP.ZHONGCSJ)) AS ZSJ,\n" + 
				"DECODE(CP.CHEPH,\n" + 
				"       NULL,\n" + 
				"       MIN(TO_CHAR(DZSJ, 'yyyy-mm-dd')) || '��' ||\n" + 
				"       MAX(TO_CHAR(DZSJ, 'yyyy-mm-dd')),\n" + 
				"       MAX(CP.ZHONGCJJY)) AS ZJJ\n" +
				"  FROM (SELECT C.ID,\n" + 
				"               C.FAHB_ID,\n" + 
				"               CHEPH,\n" + 
				"               MAOZ,\n" + 
				"               PIZ,\n" + 
				"               DECODE(QINGCSJ,NULL,0,(MAOZ - PIZ - ZONGKD)) AS JINGZ,\n" + 
				"               ZONGKD AS KOUD,\n" + 
				"               DECODE(QINGCSJ,NULL,0,BIAOZ) BIAOZ,\n" + 
				"               YINGK,\n" + 
				"               YUNS,\n" + 
				"               TO_CHAR(ZHONGCSJ,'yyyy-mm-dd hh24:mi:ss') AS ZHONGCSJ,\n" + 
				"               ZHONGCSJ as dzsj,\n" +
				"               ZHONGCJJY,\n" + 
				"               1 AS CHES,\n" + 
				"               (SELECT max(Y.MINGC)as mingc FROM YUNSDWB Y WHERE Y.ID = C.YUNSDWB_ID) AS YSDW\n" + 
				"          FROM CHEPB C\n" + 
				"         WHERE FAHB_ID IN\n" + 
				"               (SELECT ID\n" + 
				"                  FROM FAHB\n" + 
				"                 WHERE DAOHRQ BETWEEN TO_DATE('" + this.getBRiq()+ "', 'yyyy-mm-dd') AND\n" + 
				"                       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')"+yunsfsStr1+xiecfs+")) CP,\n" + 
				"       (SELECT  A.ID,DECODE(COUNT(C.ID),0,0,A.CHES/COUNT(C.ID)) CHES,A.DC,A.YUNSFSB_ID,A.MK,A.meikdq2_id \n" +
				"         FROM \n" +
				"          (SELECT F.ID,F.CHES,\n" + 
				"               DH.XUH||DH.MINGC DC,\n" + 
				"               (SELECT YF.MINGC FROM YUNSFSB YF WHERE YF.ID = F.YUNSFSB_ID) YUNSFSB_ID,\n" + 
				"               (SELECT M.MINGC FROM MEIKXXB M WHERE M.ID = F.MEIKXXB_ID) AS MK,\n" + 
				"               (SELECT m.meikdq2_id FROM MEIKXXB M WHERE M.ID = F.MEIKXXB_ID) AS meikdq2_id\n" + 
				"             FROM FAHB F,\n" + 
				"               (SELECT D.ID, D.XUH, D.MINGC FROM VWDIANC D " + getBaseDcSql() + ") DH\n" + 
				"            WHERE DAOHRQ BETWEEN TO_DATE('" + this.getBRiq()+ "', 'yyyy-mm-dd') AND\n" + 
				"               TO_DATE('" + this.getERiq()+ "', 'yyyy-mm-dd')\n"+yunsfsStr+
				"             AND F.DIANCXXB_ID = DH.ID) A, CHEPB C\n" +
				"        WHERE A.ID=C.FAHB_ID\n" +
				"         GROUP BY A.ID,A.CHES,A.DC,A.YUNSFSB_ID,A.MK,A.meikdq2_id) FH,meikdqb\n" + 
				" WHERE CP.FAHB_ID = FH.ID AND meikdqb.id = fh.meikdq2_id " + mkdqMxWhere + "" + mkMxWhere + ysdwMxWhere + "\n" + getBaseHavingSql();
			
			//�鿴������ϸ�ı���
			isMx = true;
		} 
		return SQL;
	}
		
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));		

//		�糧Tree
		
		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
		" and z.mingc='shulzhcxqx' and r.id="+visit.getRenyID();//zuxxb���������
		ResultSetList rsl=con.getResultSetList(toaijsql);
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		if(rsl.next()){
			diancxxb_id = 300;
		}
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
		ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid_dc(),null,true);

		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid_dc().split(",");
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		ToolbarButton toolb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		toolb2.setIcon("ext/resources/images/list-items.gif");
		toolb2.setCls("x-btn-icon");
		toolb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(toolb2);
		
		//ú��		
		tb1.addText(new ToolbarText("ú��:"));		
		ComboBox cboMeik = new ComboBox();
		cboMeik.setTransform("MeikSelect");	
		cboMeik.setWidth(240);
		cboMeik.setLazyRender(true);
		tb1.addField(cboMeik);
		tb1.addText(new ToolbarText("-"));
				
		//���䵥λ			
		tb1.addText(new ToolbarText("���䵥λ:"));
		ComboBox cboYunsdw = new ComboBox();
		cboYunsdw.setTransform("YunsdwSelect");
		cboYunsdw.setWidth(210);
		cboYunsdw.setLazyRender(true);
		tb1.addField(cboYunsdw);
		
		//�ڶ�������
		Toolbar tb2 = new Toolbar("tbdiv");
		//ú�����
		tb2.addText(new ToolbarText("ú�����:"));
		ComboBox meikdq = new ComboBox();
		meikdq.setTransform("MeikdqSelect");
		meikdq.setEditable(true);
		meikdq.setWidth(100);
		tb2.addField(meikdq);	
		
		//���䷽ʽ
		tb2.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(60);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb2.addField(meik);	
		
		tb2.addText(new ToolbarText("-"));	
		
		//��ʽ
		tb2.addText(new ToolbarText("��ʽ:"));
		ComboBox ges = new ComboBox();
		ges.setTransform("GesSelect");
		ges.setWidth(60);
		tb2.addField(ges);		
		
		String sql=" select * from xitxxb where mingc='��ú�����Ƿ���ʾ˫������' and leib='����'  \n" +
				"  and zhi='��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		
		boolean flag=false;
		if(con.getHasIt(sql)){
			flag=true;
		}
		con.Close();

		if(flag){

			tb2.addText(new ToolbarText("ж����ʽ:"));
			ComboBox xiecfs = new ComboBox();
			xiecfs.setTransform("XiecfsSelect");
			xiecfs.setWidth(100);
			tb2.addField(xiecfs);	
				
			
			tb2.addText(new ToolbarText("-"));
			
			ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
			rbtn.setIcon(SysConstant.Btn_Icon_Search);
			tb2.addItem(rbtn);
			
			tb2.setWidth("bodyWidth");
			tb2.addFill();
			
		}else{

			tb2.addText(new ToolbarText("-"));
			
			ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
			rbtn.setIcon(SysConstant.Btn_Icon_Search);
			tb2.addItem(rbtn);
		}
		
		tb1.setWidth("bodyWidth");
		tb1.addFill();
		
		setToolbar(tb1);
		setToolbar2(tb2);
	}
	
	public String getCurrDiancQc(JDBCcon con) throws SQLException {
		String Diancqc = "";
		String SQL = "SELECT quanc FROM diancxxb WHERE ID in (" + this.getTreeid_dc() + ")";
		
		ResultSet rs = con.getResultSet(SQL);
		
		if (rs.next()) {
			Diancqc = rs.getString(1);
		}
		return Diancqc;
	}
	
	public String getPrintTable() throws SQLException{	
		JDBCcon con = new JDBCcon();;
		String sql= this.getBaseSql();
//		System.out.println(sql);
		ResultSet rs = con.getResultSet(sql);
		String diancqc = getCurrDiancQc(con);
		
		if (isMx) {
			Report rt = new Report();
			String[][] ArrHeader = null;
			ArrHeader = new String[1][10];
			ArrHeader[0] = new String[] {"�糧����","ú�����","ú��λ","���䵥λ","����/����","ë��","Ƥ��","����","����ʱ��","����Ա"};
			int[] ArrWidth = new int[] {100, 70, 150, 120,  80, 80, 80, 80, 130, 70};
			
			rt.setTitle(diancqc+ "��ú����", ArrWidth);
			rt.title.fontSize=10;
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + diancqc, Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 3, getBRiq() + " �� " + getERiq(),Table.ALIGN_RIGHT);
			rt.setDefaultTitle(7, 4, "��λ���֡���" + "&nbsp;&nbsp;��ӡ���ڣ�" 
					+ DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", new Date()), 
					Table.ALIGN_RIGHT);
			
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setWidth(ArrWidth);
			rt.getPages();
			rt.body.setHeaderData(ArrHeader);//��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeCol(1);
			rt.body.mergeCol(2);

			for (int i=1;i<=rt.body.getRows();i++) {
				if(rt.body.getCellValue(i, 5).indexOf(CHES)>-1) {
					rt.body.setCellValue(i, 5, rt.body.getCellValue(i, 5).substring(1));
					rt.body.mergeCell(i, 9, i, 10);
					rt.body.setRowCells(i, Table.PER_FONTBOLD, true);
					rt.body.setRowCells(i, Table.PER_FORECOLOR, "blue");
				}
				if (QY_M.equals(rt.body.getCellValue(i, 2)) 
						|| HY_M.equals(rt.body.getCellValue(i, 2))
						|| (!"".equals(rt.body.getCellValue(i, 2))
								&& rt.body.getCellValue(i, 2).indexOf(ZJ_M)>-1)) {
					
					rt.body.mergeCell(i, 2, i, 4);
					rt.body.mergeCell(i, 9, i, 10);
					rt.body.setRowCells(i, Table.PER_FONTBOLD, true);
					rt.body.setRowCells(i, Table.PER_FORECOLOR, "blue");
				}
				if (!"".equals(rt.body.getCellValue(i, 2))
						&& rt.body.getCellValue(i, 1).indexOf(DC_M)>-1) {
					rt.body.mergeCell(i, 2, i, 4);
					rt.body.mergeCell(i, 9, i, 10);
					rt.body.setRowCells(i, Table.PER_FONTBOLD, true);
					rt.body.setRowCells(i, Table.PER_FORECOLOR, "red");
				}
			}
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(9, Table.ALIGN_CENTER);
			rt.body.setColAlign(10, Table.ALIGN_CENTER);
			
			for (int i=6; i<=7; i++)
				rt.body.setColFormat(i, "0.00");				
			rs = null;
			
//			���䷽ʽ
			String yunsfsStr1="";
			if(!this.getYunsfsValue().getStrId().equals("0")){
				yunsfsStr1=" and yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
			}
			
			double nianLj = 0;
			double yueLj = 0;
			sql = 
				"SELECT SUM(MAOZ - PIZ - ZONGKD) AS JINGZ\n" +
				"  FROM FAHB\n" + 
				" WHERE DAOHRQ BETWEEN TO_DATE(to_char(" + DateUtil.FormatOracleDate(this.getERiq())+ ",'yyyy') || '-01-01', 'yyyy-mm-dd') AND\n" + 
				"       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" +yunsfsStr1+
				"  AND DIANCXXB_ID IN ( SELECT D.ID FROM VWDIANC D " + getBaseDcSql() + ")";
		
			rs = con.getResultSet(sql);
			if (rs.next()) {
				nianLj = rs.getDouble("jingz");
			}
			rs=null;
			sql = 			
				"SELECT SUM(MAOZ - PIZ - ZONGKD) AS JINGZ\n" +
				"  FROM FAHB\n" + 
				" WHERE DAOHRQ BETWEEN TO_DATE(to_char(" + DateUtil.FormatOracleDate(this.getERiq())+ ",'yyyy-mm') || '-01', 'yyyy-mm-dd') AND\n" + 
				"       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" +yunsfsStr1+
				"  AND DIANCXXB_ID IN ( SELECT D.ID FROM VWDIANC D " + getBaseDcSql() + ")";		
			rs = con.getResultSet(sql);
			if (rs.next()) {
				yueLj = rs.getDouble("jingz");
			}
			
			rt.createFooter(1, ArrWidth);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_BOTTOM);
			rt.footer.setCellValue(1, 1, "������ۼ�:");
			rt.footer.setCellValue(1, 2, nianLj==0?"":""+nianLj);
			rt.footer.setCellValue(1, 3, "�����ۼ�:");
			rt.footer.setCellValue(1, 4, yueLj==0?"":""+yueLj);
			rt.footer.setCells(1, 1, 1, 1, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 2, 1, 2, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 3, 1, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.footer.setCells(1, 4, 1, 4, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_FORECOLOR, "blue");
			rt.footer.setRowCells(1, Table.PER_FONTBOLD, true);
			
			rt.footer.setCellValue(1, 6, "��ˣ�");
			rt.footer.setCellValue(1, 7, "�೤��");
			rt.footer.setCellValue(1, 9, "�Ʊ�");
			
			rt.footer.setCells(1, 6, 1, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 7, 1, 7, Table.PER_ALIGN, Table.ALIGN_RIGHT);
			rt.footer.setCells(1, 9, 1, 9, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.fontSize=10;
			_CurrentPage=1;
			_AllPages=rt.getPages();
			
			if (_AllPages==0){
				_CurrentPage=0;
			}
			
			rs.close();
			con.Close();
			return rt.getAllPagesHtml();
			
		} else {
			Report rt = new Report();
			String[][] ArrHeader = null;

			ArrHeader = new String[1][8];
			ArrHeader[0] = new String[] {"�糧����","ú�����","ú��λ","���䵥λ","����","ë��","Ƥ��","����"};
			int[] ArrWidth = new int[] {100, 70, 200, 150,  100, 100, 100, 100 };
			rt.setTitle(diancqc + "��ú����", ArrWidth);
			rt.title.fontSize=10;
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + diancqc, Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 2, getBRiq() + " �� " + getERiq(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, "��λ���֡���" + "&nbsp;&nbsp;��ӡ���ڣ�" 
					+ DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", new Date()), 
					Table.ALIGN_RIGHT);
			
			rt.setBody(new Table(rs,1,0,2));
			rt.body.setWidth(ArrWidth);
			rt.getPages();
			rt.body.setHeaderData(ArrHeader);//��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeCol(1);
			rt.body.mergeCol(2);

			for (int i=1;i<=rt.body.getRows();i++) {
				if (QY_M.equals(rt.body.getCellValue(i, 2)) 
						|| HY_M.equals(rt.body.getCellValue(i, 2))
						|| (!"".equals(rt.body.getCellValue(i, 2))
								&& rt.body.getCellValue(i, 2).indexOf(ZJ_M)>-1)) {
					
					rt.body.mergeCell(i, 2, i, 4);
					rt.body.setRowCells(i, Table.PER_FONTBOLD, true);
					rt.body.setRowCells(i, Table.PER_FORECOLOR, "blue");
				}
				if (!"".equals(rt.body.getCellValue(i, 2))
						&& rt.body.getCellValue(i, 1).indexOf(DC_M)>-1) {
					rt.body.mergeCell(i, 2, i, 4);
					rt.body.setRowCells(i, Table.PER_FONTBOLD, true);
					rt.body.setRowCells(i, Table.PER_FORECOLOR, "red");
				}
			}
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			
			for (int i=6; i<=rt.body.getCols(); i++)
				rt.body.setColFormat(i, "0.00");				
			rs = null;
			
			
//			���䷽ʽ
			String yunsfsStr1="";
			if(!this.getYunsfsValue().getStrId().equals("0")){
				yunsfsStr1=" and yunsfsb_id="+this.getYunsfsValue().getStrId()+" ";
			}
			
			double nianLj = 0;
			double yueLj = 0;
			sql = 
				"SELECT SUM(MAOZ - PIZ - ZONGKD) AS JINGZ\n" +
				"  FROM FAHB\n" + 
				" WHERE DAOHRQ BETWEEN TO_DATE(to_char(" + DateUtil.FormatOracleDate(this.getERiq())+ ",'yyyy') || '-01-01', 'yyyy-mm-dd') AND\n" + 
				"       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" +yunsfsStr1+
				"  AND DIANCXXB_ID IN ( SELECT D.ID FROM VWDIANC D " + getBaseDcSql() + ")\n" +
				"  AND (SELECT COUNT(*) FROM chepb WHERE chepb.fahb_id=fahb.id)>0";
		
			rs = con.getResultSet(sql);
			if (rs.next()) {
				nianLj = rs.getDouble("jingz");
			}
			rs=null;
			sql = 			
				"SELECT SUM(MAOZ - PIZ - ZONGKD) AS JINGZ\n" +
				"  FROM FAHB\n" + 
				" WHERE DAOHRQ BETWEEN TO_DATE(to_char(" + DateUtil.FormatOracleDate(this.getERiq())+ ",'yyyy-mm') || '-01', 'yyyy-mm-dd') AND\n" + 
				"       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" +yunsfsStr1+
				"  AND DIANCXXB_ID IN ( SELECT D.ID FROM VWDIANC D " + getBaseDcSql() + ")" +
				"  AND (SELECT COUNT(*) FROM chepb WHERE chepb.fahb_id=fahb.id)>0";
			rs = con.getResultSet(sql);
			if (rs.next()) {
				yueLj = rs.getDouble("jingz");
			}
			
			rt.createFooter(1, ArrWidth);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_BOTTOM);
			rt.footer.setCellValue(1, 1, "������ۼ�:");
			rt.footer.setCellValue(1, 2, nianLj==0?"":""+nianLj);
			rt.footer.setCellValue(1, 3, "�����ۼ�:");
			rt.footer.setCellValue(1, 4, yueLj==0?"":""+yueLj);
			rt.footer.setCells(1, 1, 1, 1, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 2, 1, 2, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 3, 1, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.footer.setCells(1, 4, 1, 4, Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_FORECOLOR, "blue");
			rt.footer.setRowCells(1, Table.PER_FONTBOLD, true);
			
			rt.footer.setCellValue(1, 5, "���:");
			rt.footer.setCellValue(1, 6, "�೤:");
			rt.footer.setCellValue(1, 7, "�Ʊ�:");
			
			rt.footer.setCells(1, 5,1,5 ,Table.PER_ALIGN, Table.ALIGN_LEFT);
			rt.footer.setCells(1, 6,1,6 ,Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.footer.setCells(1, 7,1,7 ,Table.PER_ALIGN, Table.ALIGN_RIGHT);
			
			rt.footer.fontSize=10;
			_CurrentPage=1;
			_AllPages=rt.getPages();
			
			if (_AllPages==0){
				_CurrentPage=0;
			}
			
			rs.close();
			con.Close();
			return rt.getAllPagesHtml();
		}				
	}

//	ú��������
	public IDropDownBean getMeikValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getMeikModel().getOptionCount()>0) {
				setMeikValue((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	
	public void setMeikValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getMeikModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getMeikModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setMeikModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
	
    public void getMeikModels() {
    	String sql = ""; 	
    	String strDc = "";
    	
    	if(hasDianc(getTreeid_dc())){
    		strDc = "";
    	} else {
    		strDc = "   AND FAHB.DIANCXXB_ID = " + getTreeid_dc() + "\n";
    	}
    	sql=
    		"SELECT DISTINCT MEIKXXB.ID, MEIKXXB.MINGC\n" +
    		"  FROM FAHB, MEIKXXB\n" + 
    		" WHERE DAOHRQ BETWEEN TO_DATE('" + this.getBRiq() + "', 'yyyy-mm-dd') AND\n" + 
    		"       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" + 
    		"   AND FAHB.MEIKXXB_ID = MEIKXXB.ID\n" + 
    		strDc +
    		" ORDER BY MEIKXXB.MINGC";

    	setMeikModel(new IDropDownModel(sql, "ȫ��")) ;
        return ;
    }
    
	//���䵥λ  
	public IDropDownBean getYunsdwValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYunsdwModel().getOptionCount()>0) {
				setYunsdwValue((IDropDownBean)getYunsdwModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYunsdwValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYunsdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYunsdwModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getYunsdwModels() {
		String strDc = "";
		
    	if(hasDianc(getTreeid_dc())){
    		strDc = "";
    	} else {
    		strDc = "                   AND FAHB.DIANCXXB_ID = " + getTreeid_dc() + "\n";
    	}
		
		String sql = 
			"SELECT YUNSDWB.ID, YUNSDWB.MINGC\n" +
			"  FROM (SELECT DISTINCT YUNSDWB_ID\n" + 
			"          FROM CHEPB\n" + 
			"         WHERE FAHB_ID IN\n" + 
			"               (SELECT ID\n" + 
			"                  FROM FAHB\n" + 
			"                 WHERE DAOHRQ BETWEEN TO_DATE('" + this.getBRiq() + "', 'yyyy-mm-dd') AND\n" + 
			"                       TO_DATE('" + this.getERiq() + "', 'yyyy-mm-dd')\n" + 
			strDc +
			"        )) CP,YUNSDWB\n" + 
			" WHERE CP.YUNSDWB_ID = YUNSDWB.ID\n" + 
			" ORDER BY YUNSDWB.MINGC";

		setYunsdwModel(new IDropDownModel(sql, "ȫ��"));
		return;
	}
	
//	 ���䷽ʽ������
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) getYunsfsModel().getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select 0 id,'ȫ��' mingc from dual union select id,mingc from yunsfsb";//����һ�������У����IDΪ0ʱ�����䷽ʽΪȫ��.
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}
	
	//ú����� 
	public IDropDownBean getMeikdqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean10()==null) {
			if(getMeikdqModel().getOptionCount()>0) {
				setMeikdqValue((IDropDownBean)getMeikdqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean10();
	}
	
	public void setMeikdqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean10(value);
	}
	
	public IPropertySelectionModel getMeikdqModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getMeikdqModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setMeikdqModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(_value);
	}

	public void getMeikdqModels() {
		
		String sql = 
			"SELECT ID,mingc FROM meikdqb ORDER BY xuh";
		
		setMeikdqModel(new IDropDownModel(sql, "ȫ��"));
		return;
	}
	
//	��ʽ������
	public IDropDownBean getGesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGesModel().getOptionCount()>0) {
				setGesValue((IDropDownBean)getGesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	
	public void setGesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getGesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setGesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setGesModels() {	
		List list=new ArrayList();
		list.add(new IDropDownBean("1","��ͨ"));
		list.add(new IDropDownBean("2","��ϸ"));
		list.add(new IDropDownBean("3","��"));		
		setGesModel(new IDropDownModel(list));
	}
	
	
	
//	ж����ʽ������
	public IDropDownBean getXiecfsValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean7()==null) {
			if(getXiecfsModel().getOptionCount()>0) {
				setXiecfsValue((IDropDownBean)getXiecfsModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean7();
	}
	
	public void setXiecfsValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean7(value);
	}
	
	public IPropertySelectionModel getXiecfsModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel7()==null) {
			setXiecfsModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel7();
	}
	public void setXiecfsModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel7(value);
	}
	
	public void setXiecfsModels() {	
	
		String sql=" select id,mingc  from xiecfsb  where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()
		+" union select 0 id, 'ȫ��' mingc from dual ";
		setXiecfsModel(new IDropDownModel(sql));
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript() + getOtherScript("diancTree");
	}
	//���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
	//�ڶ���������
	Toolbar tbar2;
	public Toolbar getToolbar2() {
		return tbar2;
	}
	public void setToolbar2(Toolbar tb2) {
		tbar2=tb2;
	}
	
	public String getToolbarScript2() {
		if(tbar2==null){
			return "";
		}
		return getToolbar2().getRenderScript();
	}
	
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();	
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());		
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));					
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			getMeikModels();
			getYunsdwModels();
			getMeikdqModels();
		}
		
		getSelectData();
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;		
				getSelectData();
			}			
		
		if(this.briqBoo || this.eriqBoo){
			this.briqBoo=false;
			this.eriqBoo=false;
			
			getMeikModels();
			getYunsdwModels();
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