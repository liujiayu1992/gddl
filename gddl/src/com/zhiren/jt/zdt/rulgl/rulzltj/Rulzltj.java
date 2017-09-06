package com.zhiren.jt.zdt.rulgl.rulzltj;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ����
 * ʱ�䣺2012-03-16
 * ����: ʹ�ö�ѡ�糧�����е�λ��ѡ��Ĭ��ѡ��ȫ����λ
 * 		  ���յ糧��Ž�������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-22
 * ����: ɾ���������С�
 *      ��λ��������ǰ�ˡ�
 *      ��ֵ����ˮ��ǰ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-04-03
 * ����: ��ʾȫ��������Ϣ
 */
public class Rulzltj extends BasePage {
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
		return getZhiltj();
	}

	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));

		StringBuffer strSQL = new StringBuffer();

		strSQL.append(" SELECT diancmc,fenx,haoyl,qnet_ar,qnet_ar1,mt,mad,aar,ad,aad,var,vad,vdaf,std,stad,qbad FROM\n");
		strSQL.append(" (SELECT DECODE(grouping(fx.mingc),1,'�ܼ�','&nbsp;&nbsp;'||getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rulzltjbb','rulzltjbb','lx'||fx.diancxxb_id,fx.mingc))  as diancmc,\n"); 
		strSQL.append("fx.fenx,\n"); 
		strSQL.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(qnet_ar*jianzl)/sum(jianzl)),2),0) as qnet_ar,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum((qnet_ar/0.0041816)*jianzl)/sum(jianzl))),0) as qnet_ar1,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(mt*jianzl)/sum(jianzl)),1),0) as mt,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(aar*jianzl)/sum(jianzl)),2),0) as aar,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(stad*jianzl)/sum(jianzl)),2),0) as stad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(aad*jianzl)/sum(jianzl)),2),0) as aad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(mad*jianzl)/sum(jianzl)),2),0) as mad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(qbad*jianzl)/sum(jianzl)),2),0) as qbad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(had*jianzl)/sum(jianzl)),2),0) as had,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(vad*jianzl)/sum(jianzl)),2),0) as vad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(fcad*jianzl)/sum(jianzl)),2),0) as fcad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(qgrad*jianzl)/sum(jianzl)),2),0) as qgrad,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(hdaf*jianzl)/sum(jianzl)),2),0) as hdaf,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(sdaf*jianzl)/sum(jianzl)),2),0) as sdaf,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(var*jianzl)/sum(jianzl)),2),0) as var,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(har*jianzl)/sum(jianzl)),2),0) as har,\n"); 
		strSQL.append("nvl(round(decode(sum(jianzl),0,0,sum(qgrd*jianzl)/sum(jianzl)),2),0) as qgrd\n"); 
		strSQL.append("from\n"); 
		strSQL.append("(select distinct d.id as diancxxb_id,d.fuid,d.mingc, d.xuh xuh, vwfenx.fenx\n"); 
		strSQL.append("  from diancxxb d,vwFenx where d.id IN ("+this.getTreeid()+") AND d.id NOT IN (100,112,300) ) fx,\n"); 
		strSQL.append("(select dc.id,r.diancxxb_id, decode(1,1,'����') as fenx,nvl(sum(fadhy+gongrhy+qity+feiscy),0) as haoyl,\n"); 
		strSQL.append("sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)) as jianzl,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qnet_ar*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qnet_ar,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)))) as qnet_ar1,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(mt*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),1) as mt,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(ad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as ad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(vdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as vdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(std*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as std,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(aar*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as aar,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(stad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as stad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(aad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as aad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(mad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as mad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qbad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qbad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(had*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as had,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(vad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as vad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(fcad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as fcad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qgrad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qgrad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(hdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as hdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(sdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as sdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(var*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as var,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(har*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as har,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qgrd*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qgrd\n"); 
		strSQL.append("from rulmzlb r,meihyb m,diancxxb dc\n"); 
		strSQL.append("where m.rulmzlb_id(+)=r.id and r.rulrq=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"); 
		strSQL.append("and dc.id(+)=r.diancxxb_id\n"); 
		strSQL.append("group by r.diancxxb_id,dc.id\n"); 
		strSQL.append("union\n"); 
		strSQL.append("select dc.id, r.diancxxb_id, decode(1,1,'�ۼ�') as fenx,sum(nvl(fadhy+gongrhy,0)) as haoyl,sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)) as jianzl,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qnet_ar*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qnet_ar,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)))) as qnet_ar1,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(mt*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),1) as mt,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(ad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as ad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(vdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as vdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(std*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as std,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(aar*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as aar,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(stad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as stad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(aad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as aad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(mad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as mad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qbad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qbad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(had*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as had,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(vad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as vad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(fcad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as fcad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qgrad*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qgrad,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(hdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as hdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(sdaf*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as sdaf,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(var*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as var,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(har*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as har,\n"); 
		strSQL.append("round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy)),0,0,sum(qgrd*(fadhy+gongrhy+qity+feiscy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy+qity+feiscy))),2) as qgrd\n"); 
		strSQL.append("from rulmzlb r,meihyb m,diancxxb dc\n"); 
		strSQL.append("where m.rulmzlb_id=r.id(+) and r.rulrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"); 
		strSQL.append("and r.rulrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')\n"); 
		strSQL.append("and dc.id=r.diancxxb_id  group by r.diancxxb_id,dc.id ) zhil\n"); 
		strSQL.append("where zhil.diancxxb_id(+)=fx.diancxxb_id and zhil.fenx(+)=fx.fenx\n"); 
		strSQL.append("group by rollup(fx.fenx,(fx.mingc,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rulzltjbb','rulzltjbb','lx'||fx.diancxxb_id,fx.mingc),fx.xuh))\n"); 
		strSQL.append("HAVING(GROUPING(fx.fenx)+GROUPING(fx.mingc)<2)\n"); 
		strSQL.append("ORDER BY GROUPING(fx.mingc) DESC, fx.xuh, fx.fenx)");

		//System.out.println(strsql);
		ResultSet rs = con.getResultSet(strSQL,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String ArrHeader[][]=new String[2][16];
		ArrHeader[0]=new String[] {"�糧","�糧","��¯ú��(��)","��λ������","��λ������","ȫˮ<br>Mt(%)","�ոɻ�ˮ��<br>Mad(%)","�յ����ҷ�<br>Aar(%)","������ҷ�<br>Ad(%)","�ոɻ��ҷ�<br>Aad(%)","�յ����ӷ���<br>Var(%)","������ӷ���<br>Vad(%)","�����޻һ��ӷ���<br>Vdaf(%)","��������<br>St,d(%)","���������ȫ��<br>St,ad(%)","��Ͳ����<br>Qbad(MJ/kg)"};
		ArrHeader[1]=new String[] {"�糧","�糧","��¯ú��(��)","(MJ/kg)","(kcal/Kg)","ȫˮ<br>Mt(%)","�ոɻ�ˮ��<br>Mad(%)","�յ����ҷ�<br>Aar(%)","������ҷ�<br>Ad(%)","�ոɻ��ҷ�<br>Aad(%)","�յ����ӷ���<br>Var(%)","������ӷ���<br>Vad(%)","�����޻һ��ӷ���<br>Vdaf(%)","��������<br>St,d(%)","���������ȫ��<br>St,ad(%)","��Ͳ����<br>Qbad(MJ/kg)"};
		
		int ArrWidth[]=new int[] {150,60,60,80,60,60,60,60,60,60,60,60,60,60,60,60};

		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}

		rt.body.ShowZero=true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle("��¯����ͳ��", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3,riq+"-"+riq1,Table.ALIGN_CENTER);
		rt.body.setPageRows(22);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "��ˣ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
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
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}

		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		
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
//			��ʼ���糧��
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			this.setTreeid(null);
			this.getTree();
			initDiancTree();
//			��ʼ������
			visit.setString4(null);
			visit.setString5(null);
		}
		getToolbars();

		blnIsBegin = true;

	}


//	----------------��ѡ�糧����ʼ--------------------

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
//	�糧����
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select d.id,d.mingc as jianc from diancxxb d order by d.xuh";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}


	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
	
//	���ӵ糧��ѡ���ļ���
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
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
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
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
//	��ʼ����ѡ�糧���е�Ĭ��ֵ
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
//	----------------��ѡ�糧������--------------------
}
