/*
 * �������� 2008-4-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.dc.hesgl.jiesd;
/**
 * @author zsj
 * �˷Ѵ���ԭ��
 * 1�����˷ѵ�����ú���ͬ��ʱ
 * ��Ʊ���㣺
 * ú���ǵ����ۣ�
 * <p>
 * 1����getMeiPrise�м�����˷��ܽ��
 * 2����computData��computData_Dpc���ú��ĺ�˰�ܽ�
 * 3����reCount���ú�˰ú��-��˰�˷ѣ���ֵ����˰ú��ٽ��к�˰���۵�ָ����㡣
 * ����ֹ2009-8-3�գ�ϵͳ����Ϊ��ʱ���˼�Ϊ��˰�˼ۣ�
 * <p>
 * ú��ǵ����ۣ�
 * <p>
 * 1����getMeiPrise�м�����˷��ܽ��
 * 2����computData���ú��ĺ�˰�ܽ�
 * <p>
 * <p>
 * ú����㣺
 * ú���ǵ����ۣ�
 * <p>
 * 1����getMeiPrise��ֻ�õ�ú���ͬ�е��˷ѵ���
 * 2����computData��computData_Dpc��ú����-�˷ѵ���=ú����
 * ����ֹ2009-8-3�գ�ϵͳ����Ϊ��ʱ���˼�Ϊ��ú��۵ĺ�˰�Ͳ���˰������ͬ��
 * 3�����ڲ�����getYunFei�����������˷���Ϊ�ա�
 * <p>
 * ú��ǵ����ۣ�
 * <p>
 * 1����getMeiPrise��ֻ�õ�ú���ͬ�е��˷ѵ���
 * 2����computData��computData_Dpc�в���ú����-�˷ѵ���
 * 3�����ڲ�����getYunFei�����������˷���Ϊ�ա�
 * <p>
 * �˷ѽ��㣺
 * <p>
 * 1��ֱ�Ӽ�����˷�getYunFei��ʵ�֡�
 * <p>
 * 2009-11-06 ���ʯ��ɽ�糧������������ۿ��߼��Ĵ���
 * �������£�
 * ������������ú�յ�����λ����������Ȩƽ��ֵ���㡣�����˵��¹�ú�����15���,
 * �ҵ�������ú�յ�����λ��������Ȩƽ����19 MJ/kg���ϣ�������ú����ú��ͬʱ�������Ҫ��ʱ����
 * ÿ��ú�ں�ͬ�۸�����Ͻ���1Ԫ/�֡����¹�ú������ 15��֣��ҵ�������ú�յ�����λ��������Ȩƽ����19MJ/kg���ϣ�
 * ��������ÿ��ú�ں�ͬ�۸�����Ͻ���5Ԫ/�֡����ÿ��δ��ɱ���ͬ����
 * δ��ɵ�ú����Ӧ�۳�5Ԫ/�֣��۳����ӵ��¹�Ӧú���п۳���
 * <p>
 * ��Ҫ�������ۿ��еġ����÷�Χ�����������֡�δ��ɲ���
 * �����߼��������������÷�ΧΪ���������֡���hetzkkb.shiyfw=1�������ۿ���ں�����м��㡣
 * <p>
 * 2009-11-12 ���˫Ѽɽ�糧����������˷ѽ���������㷨����
 * �������£�
 * �˷ѽ���ʱ�������䵥λ������ú�󣬸���ú����˾಻ͬ���ڽ���ʱֻ��һ��һ��ú����н��㣬
 * ��������ɽ���ʱ��Ƚϳ������Ե糧���Ҫ����Խ��ж��ú��һ��ѡ����е����ε��˷ѽ��㣬����ܽ�������ӡ�
 * <p>
 * �����߼���
 * ע�⣺��ú����ν��㲻ͬ���˷ѵĵ����ν���ʱ�Ŵ�danpcjsmkb�����򲻴�ñ�
 * <p>
 * 1���ڽ������÷���������һ�����˷ѽ��㵥���η�����������ֵΪfahb����
 * 2����CountYf�����У�����һ����ά���顣�ڶ�ά��ֵΪ[0]meikxxb_id��[1]�����������[2]�����ֵ
 * 3�����ݷ�������,�Կ���Ӱ���˷Ѽ۸�����ؽ������¸�ֵ����Ŀǰ��֪���˾࣬ú�󡣶��˾����Ǹ���ú��ȡ�õ�,�����ڹ�����������ʱҪ��meikxxb_id��Ϊ�ر�������
 * 4���ڼ������ۿ�ǰ��������������¸�ֵ��֮�����ÿ�ʷ�����˷ѽ���
 * 5�������һ������ computData_Yf ���������������ݺ�ֵ���� danpcjsmxb���˴�������ǵ����ν����򲻴� danpcjsmxb
 * 6����reCount�����д��¼���ҳ��ú��˷�Ҫ��ʾ��ֵ
 * <p>
 * 2009-11-19 ���ʯ��ɽ�糧�����������˶����ۿλ ��%�֡� �Ĵ����߼�
 * <p>
 * �����߼���
 * ע�⣺��%�֡��Ĵ����߼��ǿ۳�ʵ�ʵĽ���������Ҫ���¼���������
 * <p>
 * 1���ڽ����м��� ����ָ��� ��������_�� ��
 * 2���ڹ�ʽ�м���� ��������_�� �ļ����߼���
 * 3����setJieszb �������ý�������ȥ ��������_�������ڱ�ע�м�¼������
 * <p>
 * 2009-11-24 ���ʯ��ɽ�糧�����������˶����ۿ��Ȩ��ʽ�е� ����Ȩƽ���� �Ĵ����߼�
 * <p>
 * �����߼���
 * ǰ�᣺������Ϊ�����ν���ʱ�Ż�����������
 * <p>
 * 1���ڵõ�Ҫ�����ú��ǰ����Ҫ�õ����ۿ������Ҫ���м�Ȩƽ�������ָ�ꡣ
 * 2���õ�ָ����ü�Ȩƽ���ķ���ȥ���ã�getjiesslzl�ķ�������ʱbsv �������ָ�궼�ᱻ��ֵ��
 * 3�������ҳ����ۿ��е��Ǹ�ָ�꣬�����static״̬���и�ֵ��
 * 4����ú�ۡ��˼ۺ����ۿ�ļ��������һ��ָ���static ״̬��ֵ������Ҫ�����ֵ�������ý���ֵ��
 * ֻ�� Jiesdcz.getZhib_info �������ڷ����н���Ӧָ��� js ֵ��Ϊ static_value
 * ע�⣺�ں����Ĳ����в���������getjiesslzl�ķ�������Ϊ�÷�������д ����ָ��� js
 * <p>
 * 2009-12-29 ����Ȩƽ���㷨�£���Ҫ�����ν��д����ָ������ض��ı��У���ṹ���£�
 * id,jiesb_id,zhibb_id,lie_id,zhi,jiessl,zonghdj,zengkje
 * <p>
 * 2009-12-29 ���ʯ��ɽ�������ں�ͬ���ۿ�ļ�Ȩ��ʽ�м��롰��Ȩ���С� �Ĵ����߼�
 * <p>
 * �����������£�
 * �ȼ�Ȩ���ĳ��ָ��ļ�Ȩֵ�����ݼ�Ȩֵ�����ۿ����ҵ���Ӧ�Ŀ�ֵ��������Ȩֵ��Ϊ��ָ������ۿ����ޣ�
 * ������ѡ���е�ָ��ʹ��������Ƚϣ��ó��ֱ�����ۿ�ֵ�����е����ν��㡣
 * <p>
 * ���磺
 * ��ͬú��185Ԫ/��
 * ���ü�Ȩƽ�������ֵ�����ͬ�жԱȵó��������ֵ0.05��
 * <p>
 * Ȼ������ú��*��ú��-������-ÿ��ú�Ĳ�ֵ��*0.05��������
 * 474.26    *   ��185-��4304-4199.11��*0.05��
 * 628.98    *   ��185-��4304-4188.11��*0.05��
 * 469.12    *   ��185-��4304-4442.32��*0.05��
 * 473.06    *   ��185-��4304-4262.24��*0.05��
 * sum��ÿ���ó���Ǯ��Ϊ����ú��
 * <p>
 * �����߼����£�
 * 1���ں�ͬ�۸������Ӽ�Ȩ��ʽ����Ȩ���С����͵����μ�Ȩ�еķ������ã�
 * ��ָ���ֵ�������Ӧ�� static_value �С�
 * 2�������ҳ����ۿ��е��Ǹ�ָ�꣬�����static_dc״̬���и�ֵ��
 * 3��������ָ���static_dcֵ��Ȼ����ü����Ȩú�ۣ��ó�StringBuffer��ָ����۵��ۡ�
 * 4������۸���ʹ�õ��Ǽ�Ȩƽ���㷨����ϵͳ���ٽ��м�Ȩƽ�����㣬������۸���ʹ�õ��ǵ������㷨��
 * ���ø�ֵӰ�����ۿ��ָ��ֵ��
 * 5����ú�ۡ��˼ۺ����ۿ�ļ��������һ��ָ���static_dc ״̬��ֵ������Ҫ�����ֵ�������ý���ֵ��
 * ֻ�� Jiesdcz.getZhib_info �������ڷ����н���Ӧָ��� js ֵ��Ϊ static_value
 * ע�⣺�ں����Ĳ����в���������getjiesslzl�ķ�������Ϊ�÷�������д ����ָ��� js
 * <p>
 * <p>
 * 2010-05-06 ���ʯ��ɽ������
 * <p>
 * �ں�ͬ�۸��еĽ�����ʽ����Ϊ�����Σ����ۿ��С�����������ָ������Ϊ����Ȩƽ����ʱ
 * �Խ����������ۿ����ʱ����ÿ�����ζ����㡰����������ָ�����ۿ���������ʱͳһ���㡣
 * <p>
 * 2010-05-13	���ʯ��ɽ������
 * <p>
 * �ڵ����ν���ʱ���������еĵ������н��м�Ȩƽ����ȷ��ͳһ�ĺ�ͬ�۸�
 * ���ø������е�ָ��������ۿ���㡣
 * <p>
 * ���ڽ���������⣬
 * ���磺ĳ��3��ú���㣬��ֵ�ֱ�Ϊ 3500��3800��4200
 * ��Ȩ��ֵΪ3900
 * ��ͬ�۸�233������������ۼ�4000-4500 ��10Ԫ/��
 * 3500-4000 ��20Ԫ/�֣�
 * ���ۿ�4000������ÿ������0.05Ԫ
 * ����ʱ���ü�Ȩ��ֵ3900 ȡ�ü۸�233-20=213Ԫ/��
 * Ȼ���ڵ����κ�4000��׼�������ۿ�
 * ��4000-3500��*0.05
 * ��4000-3800��*0.05
 * ��4200-4000��*0.05
 * <p>
 * �����߼���
 * 1���ڽ������÷���������һ���������� "�����ν����ü�Ȩƽ��ȷ���۸�"��ֵΪ"��"��"��"
 * 2������ͬ�۸��У��ж����۸�����ʱ���ڵ����ν��㴦��ʱ���жϸò�����ֵΪ"��"���ͽ��м�Ȩƽ���������۸�
 * 3���ټ���ÿ�������Σ��ڼ���ʱ���жϸ�����ֵ�����Ϊ"��"�������۸����Ĳ��裬ֱ�Ӽ������ۿ�
 * <p>
 * 2010-05-24 ��������糧�����⣬�����δ������۶���ʱҪ������reCount�����У�����۶ֶ�Ӧ���ܽ����ܵ�ú�ۿ��л��˷Ѽۿ��м�ȥ��
 * ������㵥�۶�����
 * <p>
 * <p>
 * 2011-07-04 ��Ժ����糧�����ƽ��������һ��lie_id��Ӧ���pinzb_id���ڽ���ʱ��Ҫ��¼ҳ��ѡ��Ʒ�֣��������������
 * 1����getBalanceData �����м�¼ѡ���Ʒ��
 * 2����getBaseInfo �������ж�bsv.getpinz_id�Ƿ�Ϊ�գ��粻Ϊ���������¸�ֵ��������ranlpz ������һ����
 * 3���޸�Jiesdcz.getJiesszl_Sql ����������Ʒ�ֵ��ж�����
 * <p>
 * �޸ģ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ���������Ȩƽ������֮���ٵ����ο��˵����⡣
 * <p>
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * MainGlobal.getXitxx_item("����", "�����������", "0", "")
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-06
 * �������ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ��ء�
 * ���������Խ�������Ϊ׼
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-01-14
 * �������ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-03-25
 * ����������ǿ�Ƽ����ʶ��ʹ��ϵͳ��Ϣ�������ǿ�Ƽ��㡣
 * ���³���ʱ���滻��Ӧ��Jiesgs.xml
 * <p>
 * �޸ģ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ���������Ȩƽ������֮���ٵ����ο��˵����⡣
 * <p>
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * MainGlobal.getXitxx_item("����", "�����������", "0", "")
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-06
 * �������ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ��ء�
 * ���������Խ�������Ϊ׼
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-01-14
 * �������ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-03-25
 * ����������ǿ�Ƽ����ʶ��ʹ��ϵͳ��Ϣ�������ǿ�Ƽ��㡣
 * ���³���ʱ���滻��Ӧ��Jiesgs.xml
 * <p>
 * �޸ģ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ���������Ȩƽ������֮���ٵ����ο��˵����⡣
 * <p>
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * MainGlobal.getXitxx_item("����", "�����������", "0", "")
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-06
 * �������ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ��ء�
 * ���������Խ�������Ϊ׼
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-01-14
 * �������ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-03-25
 * ����������ǿ�Ƽ����ʶ��ʹ��ϵͳ��Ϣ�������ǿ�Ƽ��㡣
 * ���³���ʱ���滻��Ӧ��Jiesgs.xml
 * <p>
 * �޸ģ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ���������Ȩƽ������֮���ٵ����ο��˵����⡣
 * <p>
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * MainGlobal.getXitxx_item("����", "�����������", "0", "")
 * <p>
 * ���ߣ����
 * ʱ�䣺2012-12-06
 * �������ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ��ء�
 * ���������Խ�������Ϊ׼
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-01-14
 * �������ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
 * <p>
 * ���ߣ����
 * ʱ�䣺2013-03-25
 * ����������ǿ�Ƽ����ʶ��ʹ��ϵͳ��Ϣ�������ǿ�Ƽ��㡣
 * ���³���ʱ���滻��Ӧ��Jiesgs.xml
 */

/**
 * �޸ģ�������
 * ʱ�䣺2012-08-14
 * ���������Ӳ������ƹ���������Ȩƽ������֮���ٵ����ο��˵����⡣
 *
 */
/**
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * 		MainGlobal.getXitxx_item("����", "�����������", "0", "")
 */
/**
 * ���ߣ����
 * ʱ�䣺2012-12-06
 * �������ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ��ء�
 * 		���������Խ�������Ϊ׼
 */
/**
 * ���ߣ����
 * ʱ�䣺2013-01-14
 * �������ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
 */
/**
 * ���ߣ����
 * ʱ�䣺2013-03-25
 * ����������ǿ�Ƽ����ʶ��ʹ��ϵͳ��Ϣ�������ǿ�Ƽ��㡣
 * 		���³���ʱ���滻��Ӧ��Jiesgs.xml
 */

import java.sql.*;
import java.util.Date;

import com.zhiren.common.Jiesgs.Jiesgs;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.*;
import com.zhiren.main.Visit;

import bsh.*;

public class Balances extends BasePage implements PageValidateListener {


    Balances_variable bsv = new Balances_variable();
    Visit visit = new Visit();
    private IPropertySelectionModel _ZhibModel;

    public Balances_variable getBsv() {

        return bsv;
    }

    public void setBsv(Balances_variable value) {

        bsv = value;
    }

    public Balances_variable getBalanceData(String SelIds, long Diancxxb_id, long Jieslx, long Gongysb_id,
                                            long Hetb_id, String Jieszbsftz, String Yansbh, double Jieskdl, double Jieskkje, long Yunsdwb_id,
                                            double Shangcjsl, String Yunsdw, Visit visit, double Yujsjz) throws Exception {
        bsv.setIsError(true);
        bsv.setErroInfo("");
        bsv.setJieslx(Jieslx);
        bsv.setDiancxxb_id(Diancxxb_id);
        bsv.setSelIds(SelIds);
        bsv.setYunsdw(Yunsdw);
        bsv.setYunsdwb_id(Yunsdwb_id);
        bsv.setJieskdl(Jieskdl);
        bsv.setKouk_js(Jieskkje);
        bsv.setJieszbsftz(Jieszbsftz);
        bsv.setShangcjsl(Shangcjsl);
        bsv.setGuohxt(visit.getString19());    // ���ɽ�����ǵ糧��������ϵͳ(Aϵͳ��Bϵͳ)���˷ѽ������⣬�������˷�ʱѡ��Ĺ���ϵͳ�ŵ�bsv��
        if ("".equals(visit.getString19()) || visit.getString19() == null) {
            visit.setString19("0");
        }
        bsv.setRanlpzb_Id(Long.parseLong(visit.getString19()));
        this.visit = visit;
        bsv.setYujsjz(Yujsjz);

        if (Jieslx == Locale.liangpjs_feiylbb_id || Jieslx == Locale.meikjs_feiylbb_id) {

            //region Description//��Ʊ���㡢ú�����
            getBaseInfo(SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieszbsftz, Yansbh, Jieskdl);
            if (bsv.getErroInfo().equals("")) {
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ��Ҫ������������һ��Ҫ���
                if (Gongysb_id == 0) {
//					����ڽ���ѡ��ҳ�湩Ӧ��û��ѡ��
                    Gongysb_id = bsv.getGongysb_Id();
                }

            } else {
                bsv.getErroInfo();
                return bsv;
            }

//			�õ���ʽ��
//			�õ�ȫ����ʽ�����п����ڵ�����ú��ʱ�����ֵ������������Ҫ�����˷ѣ�
            if (!getGongsInfo(Diancxxb_id, "ALL")) {

                return bsv;
            }

//			��ú��,��ͬ�е����ֵ
            if (getMeiPrice(bsv.getRanlpzb_Id(), bsv.getYunsfsb_id(), bsv.getFaz_Id(),
                    bsv.getDaoz_Id(), Diancxxb_id, bsv.getHetb_Id(),
                    bsv.getFahksrq(), Jieslx, Jieszbsftz, SelIds,
                    Gongysb_id, Jieskdl, Yunsdwb_id, Shangcjsl)) {

            } else {

                bsv.getErroInfo();
                return bsv;
            }

            if (Jieslx == Locale.liangpjs_feiylbb_id) {
//				�������Ʊ���㣬����������˷�
                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, "");
                if (getYunFei(SelIds, Jieslx, bsv.getHetb_Id(), Shangcjsl)) {

                } else {
                    bsv.getErroInfo();
                    return bsv;
                }
            }

            if (bsv.getKoud_js() > 0) {
                bsv.setBeiz(bsv.getBeiz() + " ����۶�:" + bsv.getKoud_js());
            }
            if (bsv.getKouk_js() > 0) {
                bsv.setBeiz(bsv.getBeiz() + " ����ۿ�:" + bsv.getKouk_js());
            }

//			��������������ǰ��ˮ��������д�뱸ע��
            if (MainGlobal.getXitxx_item("����", "���������������Ƿ����ˮ�ֿ��˵���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")) {
                double shuifkhzl = getHetsfkhzl(SelIds);
                if (shuifkhzl > 0) {
                    bsv.setBeiz(bsv.getBeiz() + "ˮ�ֿ���������" + shuifkhzl);
                }
            }
            //endregion


        } else {    //�˷ѽ��㡢���������˷�
            getBaseInfo(SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieszbsftz, Yansbh, Jieskdl);

            if (Hetb_id > 0) {
//				����������˷ѣ������������
//				1��ͨ���˶Ի�Ʊ�õ��˷�
//				2��ͨ�������ͬ������˷�
//				��Hetb_id>0ʱ˵���ǵڶ������
                getYunshtInfo(Hetb_id);
            }

            if (bsv.getErroInfo().equals("")) {
//				�õ���Ӧ�̡����䷽ʽ�Ȼ�����Ϣ
                if (Gongysb_id == 0) {
//					����ڽ���ѡ��ҳ�湩Ӧ��û��ѡ��
                    Gongysb_id = bsv.getGongysb_Id();
                }
            } else {
                bsv.getErroInfo();
                return bsv;
            }

            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, "");


//			�õ��˷ѹ�ʽ
            if (getGongsInfo(Diancxxb_id, "YF")) {

            } else {
                //return ErroInfo;
                return bsv;
            }

//			���˷�
            if (getYunFei(SelIds, Jieslx, bsv.getHetb_Id(), Shangcjsl)) {

            } else {
                bsv.getErroInfo();
                return bsv;
            }
//			���������˵������˷ѽ����computData_Yf������ÿ�����е��ã��Ͳ�ͳһ������
//			computData_Yf();
        }

        CountZonghzkk();
        reCount();
        computYunfAndHej();

        bsv.setIsError(false);

        return bsv;
    }

    //�õ�ϵͳ��Ϣ���˷�˰�ʣ�ú��˰�ʣ���ʽ��
    private boolean getGongsInfo(long _Diancxxb_id, String _Type) throws Exception {
//		����˵����_diancxx_id���糧��ʽ
//				 _Type,		  ���Ϊ"MK"�Ǿ���ú�ʽ�����Ϊ"YF"�Ǿ����˷ѹ�ʽ,���Ϊ"ALL"�Ǿ�����Ʊ����
//		JDBCcon con =new JDBCcon();
//	    try {
//
//            //ú����㹫ʽ
//	    	ResultSet rs= con.getResultSet("select id from gongsb where mingc='����ú��' and leix='����' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
//            if (rs.next()) {
//
//            	DataBassUtil clob=new DataBassUtil();
//
//            	bsv.setGongs_Mk(clob.getClob("gongsb", "gongs", rs.getLong(1)));
//
//            }else{
//            	bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
//            	return false;
//            }
//            rs.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        	con.Close();
//        }
//
//		return true;

        String str_Gongs_Mk = "";
        String str_Gongs_Yf = "";

        if (_Type.equals("ALL")) {

            str_Gongs_Mk = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Mk);
            str_Gongs_Yf = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Yf);

            if (str_Gongs_Mk.equals("")) {

                bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
                return false;
            } else {

                bsv.setGongs_Mk(str_Gongs_Mk);
            }

            if (str_Gongs_Yf.equals("")) {

//				bsv.setErroInfo("û�еõ��˷ѹ�ʽ��ϵͳ����ֵ");
//	        	return false;
            } else {

                bsv.setGongs_Yf(str_Gongs_Yf);
            }
        } else if (_Type.equals("MK")) {

            str_Gongs_Mk = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Mk);

            if (str_Gongs_Mk.equals("")) {

                bsv.setErroInfo("û�еõ�ú�۹�ʽ��ϵͳ����ֵ");
                return false;
            } else {

                bsv.setGongs_Mk(str_Gongs_Mk);
            }
        } else if (_Type.equals("YF")) {

            str_Gongs_Yf = Jiesdcz.GetJiesgs(_Diancxxb_id, SysConstant.Gs_JS_HeadName_Yf);
            if (str_Gongs_Yf.equals("")) {

                bsv.setErroInfo("û�еõ��˷ѹ�ʽ��ϵͳ����ֵ");
                return false;
            } else {

                bsv.setGongs_Yf(str_Gongs_Yf);
            }
        }

        return true;
    }


//	�õ�Ҫ����������������Ȼ�����Ϣ

    /**
     * @return
     * @throws Exception
     */

    public void getYunshtInfo(long Yunshtb_id) {
//		�õ������ͬ���տλ���������У��ʺ�
        JDBCcon con = new JDBCcon();
        try {

            String sql =
                    "select nvl(yd.quanc,'') as quanc,\n" +
                            "         nvl(yd.kaihyh,'') as kaihyh,\n" +
                            "         nvl(yd.zhangh,'') as zhangh\n" +
                            "       from hetys hys,yunsdwb yd\n" +
                            "       where hys.yunsdwb_id = yd.id\n" +
                            "             and hys.id=" + Yunshtb_id;


            ResultSet rs = con.getResultSet(sql);

            while (rs.next()) {

                bsv.setShoukdw(rs.getString("quanc"));
                bsv.setKaihyh(rs.getString("kaihyh"));
                bsv.setZhangH(rs.getString("zhangh"));
            }
            rs.close();
        } catch (SQLException s) {
            // TODO �Զ����� catch ��
            s.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    public Balances_variable getBaseInfo(String SelIds, long Diancxxb_id, long Gongysb_id, long Hetb_id, String Jieszbsftz, String Yansbh, double Jieskdl) throws Exception {

        JDBCcon con = new JDBCcon();
        try {
            //�������ڡ��������ڡ����������ء�ӯ�������𡢷��������� from fahb
            //����������ȡ֤����������ú�ǰ���ȡ��������ú�����ȡ��

//	      ��ȡ�������ñ��н������ò���
//      	1����������
//        	2�������Ȩ����
//        	3��������ʾָ��
//        	4��������������С��λ
//        	5����������ȡ����ʽ
//        	6��Mt����С��λ
//        	7��Mad����С��λ
//        	8��Aar����С��λ
//        	9��Aad����С��λ
//        	10��Adb����С��λ
//        	11��Vad����С��λ
//        	12��Vdaf����С��λ
//        	13��Stad����С��λ
//        	14��Std����С��λ
//        	15��Had����С��λ
//        	16��Qnetar����С��λ
//        	17��Qbad����С��λ
//        	18��Qgrad����С��λ
//	    	19��T2����С��λ
//        	19������ָ�����
//	    	20���Ƿ��Կ���������

//	        String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//��������
//	        String jies_Jqsl="jingz";								//�����Ȩ����
//	        String jies_Jsslblxs="0";								//������������С��λ
//	        String jies_Jieslqzfs="sum(round())";					//��������ȡ����ʽ
//	        String jies_Kdkskzqzfs="round(sum())";					//�۶֡���ˮ������ȡ����ʽ
//	        String jies_yunfjssl="jingz";							//�˷ѽ�������
//	        boolean mbl_yunfjssl=false;								//�����ж��û��Ƿ񵥶��趨���˷ѵĽ���������falseû�У���ȡú���������
//
//
//	        jies_Jqsl="f."+MainGlobal.getXitxx_item("����", Locale.jiaqsl_xitxx,
//        			String.valueOf(Diancxxb_id),jies_Jqsl);
//
//        if(Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){
//
//        	String JiesszArray[][]=null;
//
//        	JiesszArray=Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
//
//			for(int i=0;i<JiesszArray.length;i++){
//
//				if(JiesszArray[i][0]!=null){
//
//					if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
//
//						jies_Jssl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
//
//						jies_Jqsl=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
//
//						jies_Jsslblxs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.kdkskzqzfs_jies)){
//
//						jies_Kdkskzqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
//
//						bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
//
//						bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
//					}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
//
//						jies_Jieslqzfs=JiesszArray[i][1];
//					}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
//
//						jies_yunfjssl=JiesszArray[i][1];
//						mbl_yunfjssl=true;
//					}
//				}
//			}
//        }
//
//        if(!mbl_yunfjssl){
//
////        	false��ʾ�û�û�е��������˷ѽ�����������ȡú���������
//        	jies_yunfjssl=jies_Jssl;
//        }

            String sql = "";

            sql = " select hetb_id,yunj,meikxxb_id,gongysb_id,pinzb_id,yunsfsb_id,minfahrq,maxfahrq,mindaohrq,maxdaohrq,gongysqc,meikdwqc,faz,faz_id,daoz_id,yuanshdw," +
                    " pinz,yunsfs,nvl(getMeiksx(meikxxb_id,diancxxb_id,'�˾�'),0) as yunju,jihkjb_id		\n " +
                    " from 														\n" +
                    " (select max(hetb_id) as hetb_id,max(m.yunj) as yunj,max(f.meikxxb_id) as meikxxb_id,max(f.gongysb_id) as gongysb_id, \n " +
                    " max(pinzb_id) as pinzb_id,max(yunsfsb_id) as yunsfsb_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq,to_char(max(fahrq),'yyyy-mm-dd') as maxfahrq, \n " +
                    " max(pz.mingc) as pinz,max(ysfs.mingc) as yunsfs," +
                    " to_char(min(daohrq),'yyyy-mm-dd') as mindaohrq,to_char(max(daohrq),'yyyy-mm-dd') as maxdaohrq,max(g.quanc) as gongysqc,max(m.quanc) as meikdwqc, \n " +
                    " max(cz.mingc) as faz,max(cz.id) as faz_id,max(dz.id) as daoz_id,max(decode(vwydw.mingc,null,(select mingc from diancxxb where id = f.diancxxb_id),vwydw.mingc)) as yuanshdw, \n " +
                    " max(f.diancxxb_id) as diancxxb_id,max(f.jihkjb_id) as jihkjb_id " +
                    " from fahb f,zhilb z,kuangfzlb kz,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz" +
                    " where f.zhilb_id=z.id and kz.id(+)=f.kuangfzlb_id and f.faz_id=cz.id and f.pinzb_id=pz.id " +
                    " and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id" +
                    " and z.liucztb_id=1 and f.liucztb_id=1 " +
                    " and f.lie_id in(" + SelIds + "))";

            ResultSet rs = con.getResultSet(sql);

            if (rs.next()) {

                bsv.setFahksrq(rs.getDate("minfahrq"));
                bsv.setFahjzrq(rs.getDate("maxfahrq"));
                if (bsv.getFahksrq().equals(bsv.getFahjzrq())) {

                    bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()));
                } else {

                    bsv.setFahrq(Jiesdcz.FormatDate(bsv.getFahksrq()) + "��" + Jiesdcz.FormatDate(bsv.getFahjzrq()));
                }

                bsv.setYansksrq(rs.getDate("mindaohrq"));
                bsv.setYansjsrq(rs.getDate("maxdaohrq"));

                if (bsv.getYansksrq().equals(bsv.getYansjsrq())) {

                    bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()));
                } else {

                    bsv.setDaohrq(Jiesdcz.FormatDate(bsv.getYansksrq()) + "��" + Jiesdcz.FormatDate(bsv.getYansjsrq()));
                }

                bsv.setHetb_Id(Hetb_id);
                bsv.setMeikxxb_Id(rs.getLong("meikxxb_id"));
                bsv.setGongysb_Id(rs.getLong("gongysb_id"));
                if (bsv.getRanlpzb_Id() == 0) {
                    bsv.setRanlpzb_Id(rs.getLong("pinzb_id"));
                    bsv.setRanlpz(rs.getString("pinz"));
                } else {
                    bsv.setRanlpz(MainGlobal.getTableCol("pinzb", "mingc", "id", String.valueOf(bsv.getRanlpzb_Id())));
                }

                bsv.setShoukdw(rs.getString("gongysqc"));
                bsv.setFahdw(rs.getString("gongysqc"));
                bsv.setMeikdw(rs.getString("meikdwqc"));
                bsv.setFaz(rs.getString("faz"));
                bsv.setFaz_Id(rs.getLong("faz_id"));
                bsv.setDaoz_Id(rs.getLong("daoz_id"));
                bsv.setYuanshr(rs.getString("yuanshdw"));
                bsv.setXianshr(rs.getString("yuanshdw"));
                bsv.setTianzdw(MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(Diancxxb_id)));
                bsv.setYunsfs(rs.getString("yunsfs"));
                bsv.setYunsfsb_id(rs.getLong("yunsfsb_id"));
                bsv.setJiesrq(Jiesdcz.FormatDate(new Date()));    //��������
                bsv.setJiesbh(Jiesdcz.getJiesbh(String.valueOf(Diancxxb_id), ""));
                bsv.setDaibcc(MainGlobal.getShouwch(SelIds));
                bsv.setYunju_cf(rs.getDouble("yunju"));        //����
                bsv.setJihkjb_id(rs.getLong("jihkjb_id"));
                bsv.setHetbh(MainGlobal.getTableCol("(select id,hetbh from hetb union select id,hetbh from hetys)", "hetbh", "id", String.valueOf(bsv.getHetb_Id())));

            } else {
                bsv.setErroInfo("Ҫ����ĳ�Ƥ��Ϣ�����ڿ����ѱ������û�ɾ��!");
                return bsv;
            }
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }

        return bsv;
    }

    public Balances_variable getJiesszl(String Jieszbsftz, String SelIds, long Diancxxb_id, long Gongysb_id,
                                        long Hetb_id, double Jieskdl, long Yunsdwb_id, long Jieslx, double Shangcjsl, String Tsclzb_where) {

//		������������
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        try {

            rsl = con.getResultSetList(Jiesdcz.getJiesszl_Sql(bsv, visit, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                    Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, Tsclzb_where).toString());

            if (rsl.next()) {

//				����
                bsv.setKoud(rsl.getDouble("koud"));                    //�۶�
                bsv.setKous(rsl.getDouble("kous"));                    //��ˮ
                bsv.setKouz(rsl.getDouble("kouz"));                    //����
                bsv.setChes(rsl.getLong("ches"));                    //����

                bsv.setGongfsl(rsl.getDouble("biaoz"));                //����
                bsv.setYingksl(rsl.getDouble("yingk"));                //ӯ��
                bsv.setYingd(rsl.getDouble("yingd"));                //ӯ��
                bsv.setKuid(rsl.getDouble("kuid"));
                bsv.setBaifbdsl(rsl.getDouble("baifbdjs"));            //�ٷֱȶֻ���

//            	����۶ִ���_begin
                if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {

                    bsv.setJiessl(rsl.getDouble("jiessl") - Jieskdl);        //��������

                    if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.daozdt_feiylbb_leib || Jieslx == Locale.haiyyf_feiylbb_id) {

                        bsv.setJiessl(rsl.getDouble("yunfjssl") - Jieskdl);
                    }

                    if (bsv.getLiangpjsyfbjxkd().equals("��") && Jieslx == Locale.liangpjs_feiylbb_id) {
//                		��Ʊ�����˷Ѳ����п۶�
                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));        //�˷ѽ�������
                    } else {

                        bsv.setYunfjsl(rsl.getDouble("yunfjssl") - Jieskdl);    //�˷ѽ�������

                        String zhi = MainGlobal.getXitxx_item("����", "ʵ�ʽ������Ƿ����ͬ���Ƚ�", String.valueOf(visit.getDiancxxb_id()), "��");
                        if (zhi.equals("��")) {
                            if (rsl.getDouble("yunfjssl") < rsl.getDouble("jiessl")) {
                                bsv.setYunfjsl(rsl.getDouble("jiessl"));
                            }
                        }
                    }

                    if (Jieslx == Locale.liangpjs_feiylbb_id && MainGlobal.getXitxx_item("����", "��ͬ�����������⴦��", "300", "��").equals("��")) {
                        //�������Ʊ����,�����Ǵ�ͬ�����������⴦��ʱ,�˷ѵĽ�������ȡú��Ľ�������
                        bsv.setYunfjsl(rsl.getDouble("jiessl"));
                    }


                    bsv.setKoud_js(Jieskdl);                            //����۶�

                    bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), Shangcjsl));    //ʵ�ʽ�������+�ϴν���������Ϊ���������ۼ��ã�
                } else {
//            		������ʽ���ǡ���Ȩƽ�������㣬�������۶ַ���reCount�����н���
                    bsv.setJiessl(rsl.getDouble("jiessl"));        //��������

                    if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.daozdt_feiylbb_leib || Jieslx == Locale.haiyyf_feiylbb_id) {

                        bsv.setJiessl(rsl.getDouble("yunfjssl"));
                    }

                    if (bsv.getLiangpjsyfbjxkd().equals("��") && Jieslx == Locale.liangpjs_feiylbb_id) {
//                		��Ʊ�����˷Ѳ����п۶�
                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));        //�˷ѽ�������
                    } else {

                        bsv.setYunfjsl(rsl.getDouble("yunfjssl"));    //�˷ѽ�������

                        String zhi = MainGlobal.getXitxx_item("����", "ʵ�ʽ������Ƿ����ͬ���Ƚ�", String.valueOf(visit.getDiancxxb_id()), "��");
                        if (zhi.equals("��")) {
                            if (rsl.getDouble("yunfjssl") < rsl.getDouble("jiessl")) {
                                bsv.setYunfjsl(rsl.getDouble("jiessl"));
                            }
                        }
                    }
                    bsv.setKoud_js(Jieskdl);                            //����۶�
                }
//				����۶ִ���_end

                bsv.setYanssl(rsl.getDouble("yanssl"));                //������������
                bsv.setJingz(rsl.getDouble("jingz"));                //����

                bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl() - bsv.getJingz()), 2));    //������������(�������͹������Ĳ�ֵ)
//            	bsv.setJiesslcy(rsl.getDouble("jieslcy"));			//������������
                bsv.setYuns(rsl.getDouble("yuns"));                    //ʵ������

                bsv.setChaokdl(rsl.getDouble("chaokdl"));            //����������Ҫ�ŵ�danpcmxb�У�

//            	if(!blnDandszyfjssl&&Jieslx==Locale.liangpjs_feiylbb_id){
////            		û�е��������˷ѽ������ҽ��㷽ʽΪ��Ʊ�����˷ѽ���������Ϊgongfsl
//            		bsv.setYunfjsl(rsl.getDouble("biaoz"));
//            	}

//	        	����ָ��
                bsv.setQnetar_cf(rsl.getDouble("Qnetar_cf"));
                bsv.setStd_cf(rsl.getDouble("Std_cf"));
                bsv.setMt_cf(rsl.getDouble("Mt_cf"));
                bsv.setMad_cf(rsl.getDouble("Mad_cf"));
                bsv.setAar_cf(rsl.getDouble("Aar_cf"));
                bsv.setAad_cf(rsl.getDouble("Aad_cf"));
                bsv.setAd_cf(rsl.getDouble("Ad_cf"));
                bsv.setVad_cf(rsl.getDouble("Vad_cf"));
                bsv.setVdaf_cf(rsl.getDouble("Vdaf_cf"));
                bsv.setStad_cf(rsl.getDouble("Stad_cf"));
                bsv.setStar_cf(rsl.getDouble("Star_cf"));
                bsv.setHad_cf(rsl.getDouble("Had_cf"));
                bsv.setQbad_cf(rsl.getDouble("Qbad_cf"));
                bsv.setQgrad_cf(rsl.getDouble("Qgrad_cf"));
                bsv.setT2_cf(rsl.getDouble("T2_cf"));

//              ��ָ��
                bsv.setQnetar_kf(rsl.getDouble("Qnetar_kf"));
                bsv.setStd_kf(rsl.getDouble("Std_kf"));
                bsv.setStar_kf(rsl.getDouble("Star_kf"));
                bsv.setMt_kf(rsl.getDouble("Mt_kf"));
                bsv.setMad_kf(rsl.getDouble("Mad_kf"));
                bsv.setAar_kf(rsl.getDouble("Aar_kf"));
                bsv.setAad_kf(rsl.getDouble("Aad_kf"));
                bsv.setAd_kf(rsl.getDouble("Ad_kf"));
                bsv.setVad_kf(rsl.getDouble("Vad_kf"));
                bsv.setVdaf_kf(rsl.getDouble("Vdaf_kf"));
                bsv.setStad_kf(rsl.getDouble("Stad_kf"));
                bsv.setHad_kf(rsl.getDouble("Had_kf"));
                bsv.setQbad_kf(rsl.getDouble("Qbad_kf"));
                bsv.setQgrad_kf(rsl.getDouble("Qgrad_kf"));
                bsv.setT2_kf(rsl.getDouble("T2_kf"));

//              ����ָ��
                String strcforkf = "_cf";
//                if(jies_shifykfzljs.equals("��")){
                if (bsv.getShifykfzljs().equals("��")) {
//                	�Ƿ��ÿ���������
                    strcforkf = "_kf";
                }
                String Q=rsl.getString("Qnetar_kf");
                //�ٺ������ж�
                strcforkf=Q!=null&&Double.parseDouble(Q)>0?"_kf":"_cf";
                boolean is_datong = MainGlobal.getXitxx_item("����", "��ͬ�����������⴦��", "300", "��").equals("��");
                if (is_datong) {
                    if (Jieslx == Locale.liangpjs_feiylbb_id || Jieslx == Locale.meikjs_feiylbb_id) {
                        strcforkf = "_js";
                    }

                }

                if (Jieszbsftz.equals("��")) {

                    bsv.setQnetar_js(rsl.getDouble("Qnetar_js"));
                    bsv.setStd_js(rsl.getDouble("Std_js"));
                    bsv.setMt_js(rsl.getDouble("Mt_js"));
                    bsv.setMad_js(rsl.getDouble("Mad_js"));
                    bsv.setAar_js(rsl.getDouble("Aar_js"));
                    bsv.setAad_js(rsl.getDouble("Aad_js"));
                    bsv.setAd_js(rsl.getDouble("Ad_js"));
                    bsv.setVad_js(rsl.getDouble("Vad_js"));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf_js"));
                    if (!bsv.getDanglgs().equals("")) { // ����������÷����������˵����Ĺ�ʽ����ô�õ�����ֵ�滻stad_js��ֵ
                        bsv.setStad_js(rsl.getDouble("dangl"));
                    } else {
                        bsv.setStad_js(rsl.getDouble("Stad" + strcforkf));
                    }
                    bsv.setStar_js(rsl.getDouble("Star_js"));
                    bsv.setHad_js(rsl.getDouble("Had_js"));
                    bsv.setQbad_js(rsl.getDouble("Qbad_js"));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad_js"));
                    bsv.setT2_js(rsl.getDouble("T2_js"));

                } else if (Jieszbsftz.equals("��")) {

                    bsv.setQnetar_js(rsl.getDouble("Qnetar" + strcforkf));
                    bsv.setStd_js(rsl.getDouble("Std" + strcforkf));
                    bsv.setMt_js(rsl.getDouble("Mt" + strcforkf));
                    bsv.setMad_js(rsl.getDouble("Mad" + strcforkf));
                    bsv.setAar_js(rsl.getDouble("Aar" + strcforkf));
                    bsv.setAad_js(rsl.getDouble("Aad" + strcforkf));
                    bsv.setAd_js(rsl.getDouble("Ad" + strcforkf));
                    bsv.setVad_js(rsl.getDouble("Vad" + strcforkf));
                    bsv.setVdaf_js(rsl.getDouble("Vdaf" + strcforkf));
                    if (!bsv.getDanglgs().equals("")) { // ����������÷����������˵����Ĺ�ʽ����ô�õ�����ֵ�滻stad_js��ֵ
                        bsv.setStad_js(rsl.getDouble("dangl"));
                    } else {
                        bsv.setStad_js(rsl.getDouble("Stad" + strcforkf));
                    }
                    bsv.setStar_js(rsl.getDouble("Star" + strcforkf));
                    bsv.setHad_js(rsl.getDouble("Had" + strcforkf));
                    bsv.setQbad_js(rsl.getDouble("Qbad" + strcforkf));
                    bsv.setQgrad_js(rsl.getDouble("Qgrad" + strcforkf));
                    bsv.setT2_js(rsl.getDouble("T2" + strcforkf));


                    if (bsv.getQnetar_js() == 0) {

                        bsv.setErroInfo("û�п󷽻������ݣ���¼�룡");
                    }
                }
                bsv.setYunju_js(bsv.getYunju_cf());        //�˾ำֵ
                bsv.setYunju_jsbz(String.valueOf(bsv.getYunju_js()));    //������У�yunj��ֵ��
                if (bsv.getKoud() + bsv.getKous() + bsv.getKouz() + bsv.getKoud_js() > 0) {
                    if (!is_datong) {//���Ǵ�ͬʱ,��ʾ�۶�
                        bsv.setBeiz(bsv.getBeiz() + " ����۶�:" + (bsv.getKoud() + bsv.getKous() + bsv.getKouz() + bsv.getKoud_js()));
                    }

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return bsv;
    }

    //����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
    private boolean getMeiPrice(long Ranlpzb_id, long Yunsfsb_id, long Faz_id, long Daoz_id, long Diancxxb_id,
                                long Hetb_id, Date Minfahrq, long Jieslx,
                                String Jieszbsftz, String SelIds, long Gongysb_id,
                                double Jieskdl, long yunsdwb_id, double Shangcjsl) {
//		������ȡ����ʽ
        String jiesjdqzfs = MainGlobal.getXitxx_item("����", "������ȡ����ʽ", String.valueOf(Diancxxb_id), "Round_new");
        bsv.setJiesjeqzfs(jiesjdqzfs);
        //�õ���ͬ��Ϣ�е��˼�
        JDBCcon con = new JDBCcon();
        String sql = "";
//        Interpreter bsh = new Interpreter();
        Interpreter bsh = new Jiesgs();
        Jiesdcz Jscz = new Jiesdcz();
        try {

//				����(��ͬ������Ϊ��λ��ÿ�´�һ�����ݶ���ȡһ����)
            sql = "select nvl(htsl.hetl,0) as hetl,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh \n"
                    + " from hetb htb, hetslb htsl		\n"
                    + " where htb.id=htsl.hetb_id(+)	\n"
                    + " and (htsl.pinzb_id=" + Ranlpzb_id + " or htsl.pinzb_id is null) and (yunsfsb_id=" + Yunsfsb_id
                    + " or yunsfsb_id is null) and (faz_id=" + Faz_id + " or faz_id is null) and (daoz_id=" + Daoz_id + " or daoz_id is null)	\n"
                    + " and (htsl.diancxxb_id=" + Diancxxb_id + " or htsl.diancxxb_id is null) and (htsl.riq<=to_date('" + Jiesdcz.FormatDate(Minfahrq)
                    + "','yyyy-MM-dd') or htsl.riq is null)	\n"
                    + " and htb.id=" + Hetb_id + "";

            ResultSetList rsl = con.getResultSetList(sql);
            if (rsl.next()) {

                bsv.setHetml(rsl.getString("hetl"));
                bsv.setShoukdw(rsl.getString("gongfdwmc"));
                bsv.setKaihyh(rsl.getString("gongfkhyh"));
                bsv.setZhangH(rsl.getString("gongfzh"));
            }

//				����(��ͬ��һ����ͬ�Ŷ�Ӧ���������¼)
            sql = "select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
                    + " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
                    + " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n"
                    + " and htzl.danwb_id=dwb.id	\n"
                    + " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"
                    + " and htb.id=" + Hetb_id + " ";

//				�õ����ۿ�����Ҫ���⴦��(�����Ρ����÷�Χ=���������֡�)��ָ�꣬���ָ������Ϊ3������Ҫ���⴦��ĺ�ͬ����ָ��
            sql = "select distinct zbb.bianm as zhib,nvl(shiyfw,0) as shiyfw\n" +
                    "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                    "        where htb.id=htzkk.hetb_id\n" +
                    "              and htzkk.zhibb_id=zbb.id\n" +
                    "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                    "              and (xs.bianm='" + Locale.danpc_jiesxs + "' or htzkk.shiyfw=1) \n" +
                    "              and (zbb.leib=1 or zbb.leib=3)\n" +
                    "              and htb.id=" + Hetb_id + "";

            rsl = con.getResultSetList(sql);
            while (rsl.next()) {
//					SelIdsΪȫ������lie_id
//					����ָ������ֻ�ܸ�ֵһ��
                bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items() + Jiesdcz.getJieszbtscl(rsl.getString("zhib"), SelIds, rsl.getInt("shiyfw")));
            }

            {    //�����ȵ�     ���ֽ���ۿ��
                String s =
                        "select canzxx,xiax,shangx,kouj,zengfj from hetb h,hetzkkb zkk\n" +
                                "where h.id=zkk.hetb_id and h.id=" + Hetb_id + "\n" +
                                "and zkk.zhibb_id=1\n" +
                                "--and shiyfw=0\n" +
                                "and canzxm=2\n" +

                                "order by canzxx";
                ResultSetList rs = con.getResultSetList(s);
                while (rs.next()) {
                    if (rs.getDouble("kouj") > 0) {
                        s =
                                "select sum(f.maoz-f.piz) yanssl\n" +
                                        "  from fahb f, zhilb z\n" +
                                        " where f.zhilb_id = z.id\n" +
                                        "   and f.lie_id in (" + SelIds + ")\n" +
                                        "   and round_new(z.qnet_ar/4.1816*1000,0) >=" + rs.getDouble("canzxx");
                        ResultSetList rs1 = con.getResultSetList(s);
                        if (rs1.next()) {
                            bsv.setShul_xb_kou_static_status(true);
                            bsv.setShul_xb_kou_static_value(rs1.getDouble("yanssl"));
                        }
                    } else if (rs.getDouble("zengfj") > 0) {
                        s =
                                "select sum(f.maoz-f.piz) yanssl\n" +
                                        "  from fahb f, zhilb z\n" +
                                        " where f.zhilb_id = z.id\n" +
                                        "   and f.lie_id in (" + SelIds + ")\n" +
                                        "   and round_new(z.qnet_ar/4.1816*1000,0) >=" + rs.getDouble("canzxx");
                        ResultSetList rs1 = con.getResultSetList(s);
                        if (rs1.next()) {
                            bsv.setShul_xb_jiang_static_status(true);
                            bsv.setShul_xb_jiang_static_value(rs1.getDouble("yanssl"));
                        }

                        bsv.setShul_rz_szs_jiang_static_value(rs.getDouble("canzxx"));
                    }
                }
            }

            if (!bsv.getJieszbtscl_Items().equals("")
                    && bsv.getTsclzbs() == null) {
//					˵����Ҫ���⴦���ָ��
                String ArrayTsclzbs[] = null;
                ArrayTsclzbs = bsv.getJieszbtscl_Items().split(";");
                bsv.setTsclzbs(ArrayTsclzbs);
//					0,�˾�,meikxxb_id,100,10,0;
//					1,�˾�,meikxxb_id,101,15,0;
//					2,�˾�,meikxxb_id,102,20,0;
//					3,Std,meikxxb_id,100,1.0,0;
//					����������ɵ���������
//					0��������'jiessl'��'jiessl'��'C'��0

            }

//				�õ����ۿ�����Ҫ���⴦����Ȩƽ������ָ�꣬����������£�����һ����Ϊ�۸�����ļ�Ȩ��ʽΪ�������Ρ�
//				�õ����ۿ�����Ҫ���á���Ȩ���С��������ۿ��ָ��
            sql = "select distinct zbb.bianm as zhib \n" +
                    "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                    "        where htb.id=htzkk.hetb_id\n" +
                    "              and htzkk.zhibb_id=zbb.id\n" +
                    "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                    "              and (xs.bianm='" + Locale.jiaqpj_jiesxs + "' or xs.bianm='" + Locale.jiaqfl_jiesxs + "') \n" +
                    "              and (zbb.leib=1 or zbb.leib=3)\n" +
                    "              and htb.id=" + Hetb_id + "";

            rsl = con.getResultSetList(sql);
            if (rsl.getRows() > 0) {
//					˵���м�¼
//					����StringBuffer �����洢Ҫ���⴦���ָ��
                StringBuffer sb = new StringBuffer("");
                while (rsl.next()) {

                    sb.append(rsl.getString("zhib")).append(",");
                }

                sb.deleteCharAt(sb.length() - 1);

                if (sb.length() > 0) {
//						Ϊ��Щ��Ҫ���⴦���ָ����ϱ�־
                    Jiesdcz.setJieszbtscl_Jqpj_Flag(bsv, sb);
                    bsv.setJiesxs(Locale.jiaqpj_jiesxs);
//						�Լ�Ȩƽ���ķ�ʽ����getJiesslzl������Ϊbsv�е�����ָ�긳ֵ
                    getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl, bsv.getYunsdwb_id(), Jieslx, Shangcjsl, "");
//						Ϊ��Щָ���static_value�ֵ
                    Jiesdcz.setJieszbtscl_Jqpj_value(bsv, sb);
                }
            }

//				��ͬ����
            sql = "select bianlmc, value from hetblb where hetb_id=" + Hetb_id;
            rsl = con.getResultSetList(sql);

            while (rsl.next()) {

                if (MainGlobal.isDigit(rsl.getString("value"))) {
//						������
                    bsh.set(rsl.getString("bianlmc"), rsl.getDouble("value"));
                } else {
//						��������
                    bsh.set(rsl.getString("bianlmc"), rsl.getString("value"));
                }
            }

//				�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�
            sql = "select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,jijlx,	\n"
                    + " jijdw.bianm as jijdw,nvl(jijgs,'') as jijgs,jsfs.bianm as jiesfs,jsxs.bianm as jiesxs,yunj,htjg.pinzb_id,		\n"
                    + " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs,fengsjj,htb.gongfdwmc,htb.gongfkhyh,htb.gongfzh	\n"
                    + " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
                    + " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
                    + " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n"
                    + " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
                    + " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n"
                    + " and htjg.yunsfsb_id=ysfs.id																\n"
                    + " and htjg.hetjjfsb_id=htjfsb.id															\n"
                    + " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"
                    + " and htb.id=" + Hetb_id + " order by zbb.bianm,tjb.bianm,xiax,shangx";

            rsl = con.getResultSetList(sql);

            if (rsl.next()) {

//					ͨ����ͬ���ý���ֵ
                setJieshtinfo(rsl, bsh);

//					��ͬ����ָ��,ȡ�����������ĺ�ͬ����
                if (rsl.getRows() == 1) {

//						��һ����ͬ
//						Ŀ¼��

//						�����˷�
//						if(Jieslx==Locale.liangpjs_feiylbb_id){
//
//							getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//						}

                    if (bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)) {    //								Ŀ¼�۽���
                        System.out.println("===============Locale.mulj_hetjjfs==============");
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {//								�����ν���
                            System.out.println("===============1.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

//									��ý�������������
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

//									ΪĿ¼�۸�ֵ
                                computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//									������ۿ�
                                getZengkk(Hetb_id, bsh, true, null);

//									ú�˰���۱���С��λ
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									��˰����ȡ����ʽ
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									�û��Զ��幫ʽ
                                bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//									���ۿ��С��λ
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									ִ�к�ͬ�۸�ʽ
                                ExecuteHetmdjgs(bsh);

//									ִ�й�ʽ
                                bsh.eval(bsv.getGongs_Mk());

//									�õ�������ָ��
                                setJieszb(bsh, 0, Shangcjsl);

//									�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                getMeikhsdj(bsh);

//									����ú����
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//						��Ȩƽ��

//								��ý�������������
                            System.out.println("===============1.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                    Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

//								ΪĿ¼�۸�ֵ
                            computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//								������ۿ�
                            getZengkk(Hetb_id, bsh, true, null);

//								ú�˰���۱���С��λ
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								�û��Զ��幫ʽ
                            bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//								��˰����ȡ����ʽ
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								���ۿ��С��λ
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								ִ�к�ͬ�۸�ʽ
                            ExecuteHetmdjgs(bsh);

//								ִ�й�ʽ
                            bsh.eval(bsv.getGongs_Mk());

//								�õ�������ָ��
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									���������Ҫ�������������ָ��
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                            getMeikhsdj(bsh);

//								����ú����
                            computData(SelIds, Hetb_id, Shangcjsl);

                        }

                    } else {

//							һ���۸�
//							��Ŀ¼��
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {//							�����ν���
                            System.out.println("===============2.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

//								�ڵ����۸��ҷ�Ŀ¼�ۣ���������Ϊ�����ν���ʱ���ж��Ƿ���������������
                            bsv.setShul_xb_dongt_static_value(0);
                            ResultSetList rrss = null;

                            if ("��".equals(MainGlobal.getXitxx_item("����", "��%��ʱ������ϵ������������Ϊ׼", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                bsv.setKoud_shulxs_yanssl(true);
                            }
                            bsv.setKoud_plm_ws(Integer.parseInt(MainGlobal.getXitxx_item("����", "��%��ʱ��һ��ú��ָ��۶֣����ձ���λ��", String.valueOf(visit.getDiancxxb_id()), "5")));
                            if ("��".equals(MainGlobal.getXitxx_item("����", "�����ν��㴦����ϲ�����Ŀ����������", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + SelIds + ") and z.qnet_ar/4.1816*1000>=" + bsv.getShul_rz_szs_jiang_static_value());
                                rrss.next();
                                bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                            }

                            for (int i = 0; i < test.length; i++) {

//									��ý�������������
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //����ֵ
                                bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));                                //ָ�굥λ

                                bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                                bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                bsh.set(rsl.getString("zhib") + "��������", 0);
                                bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                bsh.set(rsl.getString("zhib") + "С������", "");
                                bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");


//									������ۿ�
                                getZengkk(Hetb_id, bsh, true, null);

//									�û��Զ��幫ʽ
                                bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//									ú�˰���۱���С��λ
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									��˰����ȡ����ʽ
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									���ۿ��С��λ
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									ִ�к�ͬ�۸�ʽ
                                ExecuteHetmdjgs(bsh);

//									ִ�й�ʽ
                                bsh.eval(bsv.getGongs_Mk());

//									�õ�������ָ��
                                setJieszb(bsh, 0, Shangcjsl);

//									�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                getMeikhsdj(bsh);

//									����ú����
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					��Ȩƽ��

//								��ý�������������
                            System.out.println("===============2.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                    Hetb_id, Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

                            double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //����ֵ
                            bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                            bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                            bsh.set(rsl.getString("zhib") + "��������", 0);
                            bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                            bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                            bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                            bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                            bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                            bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                            bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                            bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                            bsh.set(rsl.getString("zhib") + "С������", "");
                            bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

//								������ۿ�
                            getZengkk(Hetb_id, bsh, true, null);

//								�û��Զ��幫ʽ
                            bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//								ú�˰���۱���С��λ
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								��˰����ȡ����ʽ
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								���ۿ��С��λ
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								ִ�к�ͬ�۸�ʽ
                            ExecuteHetmdjgs(bsh);

//								ִ�й�ʽ
                            bsh.eval(bsv.getGongs_Mk());

//								�õ�������ָ��
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									���������Ҫ�������������ָ��
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                            getMeikhsdj(bsh);

//								����ú����
                            computData(SelIds, Hetb_id, Shangcjsl);

                        } else if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {//					��Ȩ����

//								��ý�����������������Ȩ��ʽ��ȡ��
                            System.out.println("===============2.Locale.jiaqfl_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                    Hetb_id, Jieskdl, yunsdwb_id, Jieslx, Shangcjsl, "");

                            double Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //����ֵ
                            bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                            bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                            bsh.set(rsl.getString("zhib") + "��������", 0);
                            bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                            bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                            bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                            bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                            bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                            bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                            bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                            bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                            bsh.set(rsl.getString("zhib") + "С������", "");
                            bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

//								�õ����ۿ�ָ�����
                            ResultSetList rszkk = MainGlobal.getTableRsl(con,
                                    "select distinct zhibb.bianm as zhibbm\n" +
                                            "        from hetzkkb,zhibb\n" +
                                            "        where hetzkkb.zhibb_id = zhibb.id\n" +
                                            "				and zhibb.leib=1\n" +
                                            "              and hetzkkb.hetb_id=" + Hetb_id);

                            bsv.setJiaqfl_zhibsz(new String[rszkk.getRows()][14]);

                            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                rszkk.next();
                                bsv.getJiaqfl_zhibsz()[i][0] = rszkk.getString("zhibbm");
                            }
                            rszkk.close();

//								������ۿ�������ۿ��ȡ��ȫ�����ۿ��׼�����롰��Ȩ����ָ�����顱��
                            getZengkk(Hetb_id, bsh, true, null);

//								����Ȩ����ָ�����顱��ʼ����ɣ�������õ����ν����㷨����
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

//									��ý�������������
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbltmp);    //����ֵ
                                bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));                                //ָ�굥λ

                                bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                                bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                bsh.set(rsl.getString("zhib") + "��������", 0);
                                bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                bsh.set(rsl.getString("zhib") + "С������", "");
                                bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

//									������ۿ�
//									getZengkk(Hetb_id,bsh,true,null);
                                getZengkk_Jiaqfl(bsh);    //��Ȩ�������ۿ�

//									�û��Զ��幫ʽ
                                bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//									ú�˰���۱���С��λ
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									��˰����ȡ����ʽ
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									���ۿ��С��λ
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									ִ�к�ͬ�۸�ʽ
                                ExecuteHetmdjgs(bsh);

//									ִ�й�ʽ
                                bsh.eval(bsv.getGongs_Mk());

//									�õ�������ָ��
                                setJieszb(bsh, 0, Shangcjsl);

//									�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                getMeikhsdj(bsh);

//									����ú����
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }
                        }
                    }

                    bsv.setHetjgpp_Flag(true);
                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
                } else {
//						�ж���۸�
//						Ŀ¼��

                    if (bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)) {            //Ŀ¼�۽���

//							�����˷�
//							if(Jieslx==Locale.liangpjs_feiylbb_id){
//
//								getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//							}
                        System.out.println("===============3.Locale.mulj_hetjjfs==============");
                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {        //�����ν���
                            System.out.println("===============3.Locale.danpc_jiesxs==============");
                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            for (int i = 0; i < test.length; i++) {

                                //								��ý�������������
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                //								ΪĿ¼�۸�ֵ
                                computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

                                //								������ۿ�
                                getZengkk(Hetb_id, bsh, true, null);

//									�û��Զ��幫ʽ
                                bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//									ú�˰���۱���С��λ
                                bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//									��˰����ȡ����ʽ
                                bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//									���ۿ��С��λ
                                bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//									ִ�к�ͬ�۸�ʽ
                                ExecuteHetmdjgs(bsh);

                                //								ִ�й�ʽ
                                bsh.eval(bsv.getGongs_Mk());

                                //								�õ�������ָ��
                                setJieszb(bsh, 0, Shangcjsl);

//									�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                getMeikhsdj(bsh);

                                //								����ú����
                                computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);
                            }
                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					��Ȩƽ��


//								��ý�������������
                            System.out.println("===============3.Locale.jiaqpj_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

//								ΪĿ¼�۸�ֵ
                            computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);

//								������ۿ�
                            getZengkk(Hetb_id, bsh, true, null);

//								�û��Զ��幫ʽ
                            bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

//								ú�˰���۱���С��λ
                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//								��˰����ȡ����ʽ
                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//								���ۿ��С��λ
                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//								ִ�к�ͬ�۸�ʽ
                            ExecuteHetmdjgs(bsh);

//								ִ�й�ʽ
                            bsh.eval(bsv.getGongs_Mk());

//								�õ�������ָ��
                            setJieszb(bsh, 0, Shangcjsl);

                            if (bsv.getTsclzbs() != null) {
//									���������Ҫ�������������ָ��
                                Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                        Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                            }

//								�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                            getMeikhsdj(bsh);

//								����ú����
                            computData(SelIds, Hetb_id, Shangcjsl);
                        }
                    } else {

//							����۸�
//							��Ŀ¼��
                        double Dbljijzb = 0;

                        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {    //�����ν���

//									�жϲ��� "�����ν����ü�Ȩƽ��ȷ���۸�"��ֵΪ"��"��"��"
//									����ͬ�۸��У��ж����۸�����ʱ���ڵ����ν��㴦��ʱ���жϸò�����ֵΪ"��"���ͽ��м�Ȩƽ���������۸�
                            System.out.println("===============3.Locale.danpc_jiesxs==============");
                            String danpcjsyjqpjqdjg = "��";

                            danpcjsyjqpjqdjg = MainGlobal.getXitxx_item("����", Locale.danpcjsyjqpjqdjg,
                                    String.valueOf(Diancxxb_id), danpcjsyjqpjqdjg);

                            danpcjsyjqpjqdjg = Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,
                                    Hetb_id, Locale.danpcjsyjqpjqdjg, danpcjsyjqpjqdjg);

                            if (danpcjsyjqpjqdjg.equals("��")) {

                                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

                                rsl.beforefirst();
                                while (rsl.next()) {

                                    Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                    Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());
                                    bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                    if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                            && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                            ) {

                                        setJieshtinfo(rsl, bsh);

//												���ý����������
                                        bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //����ֵ
                                        bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                                        bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                                        bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                        bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                        bsh.set(rsl.getString("zhib") + "��������", 0);
                                        bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                        bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                        bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                        bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                        bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                        bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                        bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                        bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                        bsh.set(rsl.getString("zhib") + "С������", "");
                                        bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

                                        break;
                                    }
                                }
                            }

                            String[] test = new String[1];

                            if (SelIds.indexOf(",") == -1) {

                                test[0] = SelIds;
                            } else {

                                test = SelIds.split(",");
                            }

                            bsv.setShul_xb_dongt_static_value(0);
                            ResultSetList rrss = null;
                            //ʯ��ɽ    ����	ȫ��ú��Ȩֵ�ﵽ������Ŀ��׼����������ú
                            if ("��".equals(MainGlobal.getXitxx_item("����", "�����ν��㴦������ȫ����", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb  where lie_id in(" + SelIds + ")");
                                if (rrss.next()) {
                                    bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                                }
                                rrss = con.getResultSetList("select round_new(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),2) qnet_ar from fahb f, zhilb z where f.zhilb_id = z.id and f.lie_id in(" + SelIds + ")");
                                if (rrss.next()) {
                                    bsv.setShul_rz_szs_jiang_static_status(true);
                                    bsv.setShul_rz_szs_jiang_static_value(rrss.getDouble("qnet_ar"));
                                }
                                rrss.close();

                            }

                            {//������
                                if ("��".equals(MainGlobal.getXitxx_item("����", "��%��ʱ������ϵ������������Ϊ׼", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                    bsv.setKoud_shulxs_yanssl(true);
                                }
                                bsv.setKoud_plm_ws(Integer.parseInt(MainGlobal.getXitxx_item("����", "��%��ʱ��һ��ú��ָ��۶֣����ձ���λ��", String.valueOf(visit.getDiancxxb_id()), "5")));
                                if ("��".equals(MainGlobal.getXitxx_item("����", "�����ν��㴦����ϲ�����Ŀ����������", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                    rrss = con.getResultSetList("select sum(sanfsl) yanssl from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + SelIds + ") and z.qnet_ar/4.1816*1000>=" + bsv.getShul_rz_szs_jiang_static_value());
                                    rrss.next();
                                    bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl"));
                                }
                            }
                            for (int i = 0; i < test.length; i++) {


//										��ý�������������
                                getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                        yunsdwb_id, Jieslx, Shangcjsl, "");

//										�����ν����Ƿ����ò�ͬƷ�ֽ��㣨���ã�������con��Ӱ������ٶȣ�
                                if ("��".equals(MainGlobal.getXitxx_item("����", "�����ν����Ƿ����ò�ͬƷ�ֽ���", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                    rrss = con.getResultSetList("select f.pinzb_id from fahb f where f.lie_id in(" + test[i] + ")");
                                    if (rrss.next()) {
//												��������Ʒ��
                                        bsv.setRanlpzb_Id(rrss.getLong("pinzb_id"));
                                    }
                                    rrss.close();
                                }


                                //����xb       ����:     ����ú�ﵽ������Ŀ��׼,�������������ֵ�����ú
                                if ("��".equals(MainGlobal.getXitxx_item("����", "�����ν��㴦��������������", String.valueOf(visit.getDiancxxb_id()), "��"))) {
                                    rrss = con.getResultSetList("select sanfsl yanssl,z.qnet_ar/4.1816*1000 qnet_ar from fahb f,zhilb z where f.zhilb_id=z.id and f.lie_id in(" + test[i] + ")");
                                    if (rrss.next()) {
                                        if (rrss.getDouble("qnet_ar") >= bsv.getShul_rz_szs_jiang_static_value()) {
                                            bsv.setShul_xb_dongt_static_value(rrss.getDouble("yanssl") + bsv.getShul_xb_dongt_static_value());
                                        }
                                    }
                                    rrss.close();
                                }

                                rsl.beforefirst();

                                if (danpcjsyjqpjqdjg.equals("��")) {


//												������ۿ�
                                    getZengkk(Hetb_id, bsh, true, null);

//												�û��Զ��幫ʽ
                                    bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//												ú�˰���۱���С��λ
                                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//												��˰����ȡ����ʽ
                                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//												���ۿ��С��λ
                                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//												ִ�к�ͬ�۸�ʽ
                                    ExecuteHetmdjgs(bsh);

//												ִ�й�ʽ
                                    bsh.eval(bsv.getGongs_Mk());

//												�õ�������ָ��
                                    setJieszb(bsh, 0, Shangcjsl);

//												�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                    getMeikhsdj(bsh);

//												����ú����
                                    computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);

                                    bsv.setHetjgpp_Flag(true);
                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                } else {

                                    while (rsl.next()) {

                                        Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                        Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());
                                        bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                        if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                                && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                                ) {

                                            setJieshtinfo(rsl, bsh);

//													�����˷ѣ�ע�⣺ֻҪ��һ�Σ�Ҫ��һ�������жϣ�
//													if(Jieslx==Locale.liangpjs_feiylbb_id&&!bsv.getDanpcysyf()){
////														�ж�������1������Ʊ���㣻2�������ν��㻹û������˷�
//														getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//														bsv.setDanpcysyf(true);
//													}

//													���ý����������
                                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //����ֵ
                                            bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                                            bsv.setYifzzb(rsl.getString("zhib"));    //Ĭ�ϵ��Ѹ�ֵָ��

                                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                            bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                            bsh.set(rsl.getString("zhib") + "��������", 0);
                                            bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                            bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                            bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                            bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                            bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                            bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                            bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                            bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                            bsh.set(rsl.getString("zhib") + "С������", "");
                                            bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

                                            //											������ۿ�
                                            getZengkk(Hetb_id, bsh, true, null);

                                            //											�û��Զ��幫ʽ
                                            bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//													ú�˰���۱���С��λ
                                            bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//													��˰����ȡ����ʽ
                                            bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//													���ۿ��С��λ
                                            bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//													ִ�к�ͬ�۸�ʽ
                                            ExecuteHetmdjgs(bsh);

                                            //											ִ�й�ʽ
                                            bsh.eval(bsv.getGongs_Mk());

                                            //											�õ�������ָ��
                                            setJieszb(bsh, 0, Shangcjsl);

//													�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                            getMeikhsdj(bsh);

                                            //											����ú����
                                            computData_Dpc(SelIds, Hetb_id, Shangcjsl, test[i]);

                                            bsv.setHetjgpp_Flag(true);
                                            bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                            break;
                                        }
                                    }
                                }
                            }

                        } else if (bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {//					��Ȩƽ��

//									��ý�������������
                            System.out.println("===================4.Locale.jiaqpj_jiesxs===================");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

                            do {
                                System.out.println("zhib : " + rsl.getString("zhib"));
                                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                bsv.setJiagpzId(rsl.getString("pinzb_id"));

//                                        Dbljijzb>=rsl.getDouble("xiax")
//                                                &&Dbljijzb<=(rsl.getDouble("shangx")==0?1e308:rsl.getDouble("shangx"))
//                                                &&Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
//                                System.out.println("Dbljijzb:" + Dbljijzb);
//                                System.out.println("xiax:" + rsl.getDouble("xiax"));
//                                System.out.println("shangx:" + rsl.getDouble("shangx"));
//                                System.out.println("JiagpzId:" + bsv.getJiagpzId());
//                                System.out.println("Ranlpzb_Id:" + bsv.getRanlpzb_Id());
//                                System.out.println(Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id())));
                                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                        && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                        ) {

//											���ý���ĺ�ֵͬ
                                    this.setJieshtinfo(rsl, bsh);

//											�����˷ѣ�ע�⣺ֻҪ��һ�Σ�
//											if(Jieslx==Locale.liangpjs_feiylbb_id){
////												�ж�����������Ʊ����
//												getYunFei(SelIds,Jieslx,bsv.getHetb_Id(),Shangcjsl);
//											}

                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));        //��ͬ�۸��id

                                    bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //����ֵ
                                    bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                                    bsv.setYifzzb(rsl.getString("zhib"));            //Ĭ�ϵ��Ѹ�ֵָ��

                                    bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                    bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                    bsh.set(rsl.getString("zhib") + "��������", 0);
                                    bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                    bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                    bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                    bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                    bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                    bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                    bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                    bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                    bsh.set(rsl.getString("zhib") + "С������", "");
                                    bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

//												������ۿ�
                                    getZengkk(Hetb_id, bsh, true, null);

//												�û��Զ��幫ʽ
                                    bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//											ú�˰���۱���С��λ
                                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//											��˰����ȡ����ʽ
                                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//											���ۿ��С��λ
                                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//											ִ�к�ͬ�۸�ʽ
                                    ExecuteHetmdjgs(bsh);

//											ִ�й�ʽ
//                                    String gongs=bsv.getGongs_Mk();
//                                    System.out.println(gongs);
                                    bsh.eval(bsv.getGongs_Mk());

//												�õ�������ָ��
                                    setJieszb(bsh, 0, Shangcjsl);

                                    if (bsv.getTsclzbs() != null) {
//												���������Ҫ�������������ָ��
                                        Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                                Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                                    }

//											�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                    getMeikhsdj(bsh);

//												����ú����
                                    computData(SelIds, Hetb_id, Shangcjsl);

                                    bsv.setHetjgpp_Flag(true);
                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));

                                    break;
                                }

                            } while (rsl.next());
                        } else if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {    //��Ȩ����

//								��ý�������������
                            System.out.println("===============4.Locale.jiaqfl_jiesxs==============");
                            getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                    yunsdwb_id, Jieslx, Shangcjsl, "");

                            do {
                                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                bsv.setJiagpzId(rsl.getString("pinzb_id"));

                                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                        && Jiesdcz.getHet_condition(bsv.getJiagpzId(), String.valueOf(bsv.getRanlpzb_Id()))
                                        ) {

//										���ý���ĺ�ֵͬ
                                    this.setJieshtinfo(rsl, bsh);

                                    bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));        //��ͬ�۸��id

//										�õ����ۿ�ָ�����
                                    ResultSetList rszkk = MainGlobal.getTableRsl(con,
                                            "select distinct zhibb.bianm as zhibbm\n" +
                                                    "        from hetzkkb,zhibb\n" +
                                                    "        where hetzkkb.zhibb_id = zhibb.id\n" +
                                                    "			   and zhibb.leib=1\n" +
                                                    "              and hetzkkb.hetb_id=" + Hetb_id);

                                    bsv.setJiaqfl_zhibsz(new String[rszkk.getRows()][14]);

                                    for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                        rszkk.next();
                                        bsv.getJiaqfl_zhibsz()[i][0] = rszkk.getString("zhibbm");
                                    }
                                    rszkk.close();

//										������ۿ�������ۿ��ȡ��ȫ�����ۿ��׼�����롰��Ȩ����ָ�����顱��
                                    getZengkk(Hetb_id, bsh, true, null);

//										����Ȩ����ָ�����顱��ʼ����ɣ�������õ����ν����㷨����
                                    String[] test = new String[1];

                                    if (SelIds.indexOf(",") == -1) {

                                        test[0] = SelIds;
                                    } else {

                                        test = SelIds.split(",");
                                    }

                                    for (int i = 0; i < test.length; i++) {

//											��ý�������������
                                        getJiesszl(Jieszbsftz, test[i], Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                                                yunsdwb_id, Jieslx, Shangcjsl, "");

                                        Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                                        Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                                        bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), rsl.getString("danw")), Dbljijzb);    //����ֵ
                                        bsh.set(rsl.getString("zhib") + "������λ", rsl.getString("danw"));

                                        bsv.setYifzzb(rsl.getString("zhib"));            //Ĭ�ϵ��Ѹ�ֵָ��

                                        bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("shangx"));        //ָ������
                                        bsh.set(rsl.getString("zhib") + "����", rsl.getDouble("xiax"));            //ָ������

                                        bsh.set(rsl.getString("zhib") + "��������", 0);
                                        bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                                        bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                                        bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                                        bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                                        bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                                        bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                                        bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                                        bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                                        bsh.set(rsl.getString("zhib") + "С������", "");
                                        bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");

                                        getZengkk_Jiaqfl(bsh);    //��Ȩ�������ۿ�

//											�û��Զ��幫ʽ
                                        bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//											ú�˰���۱���С��λ
                                        bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//											��˰����ȡ����ʽ
                                        bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//											���ۿ��С��λ
                                        bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//											ִ�к�ͬ�۸�ʽ
                                        ExecuteHetmdjgs(bsh);

//											ִ�й�ʽ
                                        bsh.eval(bsv.getGongs_Mk());

//											�õ�������ָ��
                                        setJieszb(bsh, 0, Shangcjsl);

                                        if (bsv.getTsclzbs() != null) {
//												���������Ҫ�������������ָ��
                                            Jiestszbcl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id,
                                                    Jieskdl, yunsdwb_id, Jieslx, Shangcjsl);
                                        }

//											�������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�����ú�˰���۽���ȡ������
                                        getMeikhsdj(bsh);

//												����ú����
                                        computData(SelIds, Hetb_id, Shangcjsl);

                                        bsv.setHetjgpp_Flag(true);
                                        bsv.setHetjgb_id(rsl.getLong("hetjgb_id"));
                                    }
                                    break;
                                }

                            } while (rsl.next());
                        }
                    }
                }
            } else {
//					���û�õ���ͬ�۸�Ҫ�õ����������������
                getJiesszl(Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id, Hetb_id, Jieskdl,
                        yunsdwb_id, Jieslx, Shangcjsl, "");
            }

            if (!bsv.getHetjgpp_Flag()) {

                bsv.setErroInfo("û�к�ͬ�۸����������ƥ�䣡");
            }

            rsl.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            con.Close();
        }

        return true;
    }

    private void setJieshtinfo(ResultSetList rsl, Interpreter bsh) {
//		�������ܣ�

//				������ʱ��Ҫ����ͬ��Ϣ��bsh��ֵ���Ա���������
//		�����߼���

//				��rsl��ֵȡ����ֵ��bsh
//		�����βΣ�rsl ��ͬ�����ݼ���bsh �����Զ����������

        bsv.setHetmdj(rsl.getDouble("jij"));            //����ú����
        bsv.setJijlx(rsl.getInt("jijlx"));                //�������ͣ�0����˰��1������˰��
        bsv.setZuigmj(rsl.getDouble("zuigmj"));            //���ú��
        bsv.setHetmdjdw(rsl.getString("jijdw"));        //��ͬú���۵�λ
        bsv.setHetmdjgs(Jiesdcz.getTransform_Hetjgs(
                rsl.getString("jijgs").trim(),
                rsl.getString("danw"),
                rsl.getDouble("jij"),
                bsv.getDiancxxb_id()));        //��ͬ���۹�ʽ
        bsv.setJiesfs(rsl.getString("jiesfs"));            //���㷽ʽ�������۸񡢳���۸�
        bsv.setJiesxs(rsl.getString("jiesxs"));            //������ʽ�������Ρ���Ȩƽ����
        bsv.setHetyj(rsl.getDouble("yunj"));            //��ͬ�˼۵���
        bsv.setHetyjdw(rsl.getString("yunjdw"));        //��ͬ�˼۵�λ
        bsv.setHetjjfs(rsl.getString("hejfs"));            //��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
        bsv.setFengsjj(rsl.getDouble("fengsjj"));        //��ͬ�۸��еķֹ�˾�Ӽۣ�ͳһ�����ã�
//															�ֹ�˾�Ӽ۴����߼���
//																1�����ݺ�ͬ�۸����ͣ���˰������˰�����ԭʼ����ۣ������Ǻ�˰Ҳ�����ǲ���˰����
//																	�ñ�������,�����ֹ�˾�Ӽ۽��б��档
//																2������Ǻ�˰�ۣ����㵥��=���㵥��+�ֹ�˾�Ӽۣ�
//																		����ǲ���˰�ۣ����㵥��=��������ĺ�˰��+�ֹ�˾�Ӽ�
//
        bsv.setShoukdw(rsl.getString("gongfdwmc"));                    //�տλ
        bsv.setKaihyh(rsl.getString("gongfkhyh"));                        //��������
        bsv.setZhangH(rsl.getString("gongfzh"));                        //�����ʺ�
        bsv.setJiagpzId(rsl.getString("pinzb_id"));            //�۸����Ʒ�֣�Ϊ������һ����ͬ��ͬƷ�ֲ�ͬ�۸�����
        try {
            bsh.set("������ʽ", bsv.getJiesxs());
            bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
            bsh.set("�۸�λ", bsv.getHetmdjdw());
            bsh.set("��ͬ�۸�", bsv.getHetmdj());
            bsh.set("���ú��", bsv.getZuigmj());
            bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());

        } catch (EvalError e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void ExecuteHetmdjgs(Interpreter bsh) {
//		ִ�к�ͬ�۸�ʽ
//		�������ܣ�

//			��ִ�н��㹫ʽǰ�������ͬ�۸��д��ڼ۸�ʽ��Ҫ��ִ�м۸�ʽ������ֵ��ֵ������ͬ�۸񡱡�
//		�����߼���

//			ִ�к�ͬ�۸�Ĺ�ʽ��
//		�����βΣ�bsh �����Զ����������

        if (!bsv.getHetmdjgs().equals("")) {

            try {
                bsh.eval(bsv.getHetmdjgs());
                bsv.setHetmdj(Double.parseDouble(bsh.get("��ͬ�۸�").toString()));        //��ͬ�۸�
                bsh.set("��ͬ�۸�", bsv.getHetmdj());
            } catch (EvalError e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private boolean getZengkk(long Hetb_id, Interpreter bsh, boolean Falg, String[] Tsclzb_item) {
//		���ۿ�
//		2010-5-8 zsj�ģ�
//			���������ۿλΪ ����֡� ʱ������� ��������/10000 �ٽ������ۿ���������ڼ���ӯ��ʱֻ����2λС�������������
//				�޸ķ������£�������ۿ��ָ��Ϊ������������ô������ۿλΪ ����֡� �������޳��� 10000 ���������ۿλתΪ ���֡�

        JDBCcon con = new JDBCcon();
        try {
            ResultSetList rsl = null;

            //���������糧,���Ӳ����ж��Ƿ�۸��Ȩ�����ۿ���ε�������
            String jiesxs_condition = "";
            if (MainGlobal.getXitxx_item("����", "�������ۿ���ε�������", String.valueOf(visit.getDiancxxb_id()), "��").equals("��") && bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {
                jiesxs_condition = " and xs.BIANM <> '" + Locale.danpc_jiesxs + "'";
            }


            String sql = "select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jis,	\n"
                    + " jisdw.bianm as jisdw,kouj,koujgs,kjdw.bianm as koujdw,zengfj,zengfjgs,zfjdw.bianm as zengfjdw,	\n"
                    + " xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx, \n"
                    + " nvl(canzxx,0) as canzxx,shiyfw,nvl(xs.bianm,'') as jiesxs,pz.mingc as pinz,pz.id as pinzb_id, decode(htzkk.jijlx, 0, '��˰����', '����˰����') jijlx\n"
                    + " from hetb htb, hetzkkb htzkk,zhibb zbb,tiaojb tjb,danwb dwb,danwb jisdw,danwb kjdw,	\n"
                    + " danwb zfjdw,zhibb czxm,danwb czxmdw,hetjsxsb xs,pinzb pz		\n"
                    + " where htb.id=htzkk.hetb_id and htzkk.zhibb_id=zbb.id and htzkk.tiaojb_id=tjb.id	\n"
                    + " and htzkk.danwb_id=dwb.id(+) and htzkk.jisdwid=jisdw.id(+)	\n"
                    + " and htzkk.koujdw=kjdw.id(+)		\n"
                    + " and htzkk.hetjsxsb_id=xs.id(+)	\n"
                    + " and htzkk.zengfjdw=zfjdw.id(+)	\n"
                    + " and htzkk.canzxm=czxm.id(+)		\n"
                    + " and htzkk.canzxmdw=czxmdw.id(+)	\n"
                    + " and htzkk.pinzb_id=pz.id(+)	\n"
                    + " and tjb.leib=1 and zbb.leib=1	" + jiesxs_condition + "\n"
                    + " and htb.id=" + Hetb_id + " order by zbb.bianm,tjb.bianm,xiax,shangx ";

            rsl = con.getResultSetList(sql);
            double Dbltmp = 0;        //��¼ָ�����ֵ
            double Dblczxm = 0;        //��¼������Ŀ��ֵ
            String Strtmp = "";        //��¼�趨��ָ��
            String Strimplementedzb = "";    //��¼�Ѿ�ִ�й���ָ�꣨���Ѿ������ִ�е�ָ�꣩��
            double Dblimplementedzbsx = 0;    //��¼��ִ�й���ָ�������
            StringBuffer sb = new StringBuffer();    //��¼��ͬ���ۿ��е����÷�ΧΪ1�ļ�¼
            String Feitsclzb = "";        //��¼���ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ��

            String m_zhib = "";    //ָ��
            String m_danw = "";    //��λ
            String m_tiaoj = "";    //����
            double m_zengfj = 0;    //������
            String m_zengfjgs = "";    //�����۹�ʽ
            double m_kouj = 0;    //�ۼ�
            String m_koujgs = "";    //�ۼ۹�ʽ
            String m_zengfjdw = "";    //�����۵�λ
            String m_koujdw = "";        //�۸��۵�λ
            double m_shangx = 0;    //����
            double m_xiax = 0;    //����
            double m_jis = 0;        //����
            String m_jisdw = "";    //������λ
            double m_jizzkj = 0;    //��׼���ۼ�
            int m_xiaoscl = 0;    //С������
            int m_shiyfw = 0;        //���÷�Χ
            String m_jijlx = "";        //��������(0Ϊ��˰���ۡ�1Ϊ����˰����)
            boolean m_qiangzjs = false;    //ǿ�Ƽ����־��ĳЩָ��û�дﵽ���ۿ��������
//										��ҲҪ��ʾ�ڽ��㵥�ϣ���ʱ��Ҫ��¼het��־������������ӯ������Ϣ��
//										ͨ���˱�־�ڹ�ʽ���жϣ����Ϊ��˵��ǿ�Ƽ��㣬�۽��Ϊ0��

            if (Falg) {
//				Falg=true ˵�����������ۿ���㣬��ʱ���ָ�������⴦�������� Ӧ������

                while (rsl.next()) {

                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
//					�õ�������Ŀ�Ľ����׼
                    Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");

//					ָ��Ľ���ָ��
                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                    Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

                    //�������۲��ֵ���
                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                            && rsl.getString("canzxm").equals(Locale.Qnetar_zhibb)) {
                        if (rsl.getDouble("kouj") > 0 && bsv.isShul_xb_kou_static_status()) {
                            Dbltmp = bsv.getShul_xb_kou_static_value();
                        } else if (rsl.getDouble("zengfj") > 0 && bsv.isShul_xb_jiang_static_status()) {
                            Dbltmp = bsv.getShul_xb_dongt_static_value() - 1;
                            if (bsv.isShul_rz_szs_jiang_static_status()) {
                                Dblczxm = bsv.getShul_rz_szs_jiang_static_value();
                            }
                        }
                    }
                    boolean liufqzjs = false;
                    if (MainGlobal.getXitxx_item("����", "����Ƿ����ǿ�Ƽ���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")) {
                        liufqzjs = (rsl.getString("zhib").equals(Locale.Std_zhibb) || rsl.getString("zhib").equals(Locale.Stad_zhibb) || rsl.getString("zhib").equals(Locale.Star_zhibb));    //����std/stad/star��ǿ�����ۿ����
                    }
                    if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax"))
                            && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                            && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                            && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //�����ָ����Ҫ���⣨�����Ρ�������Χ�����������μ�ͳһ�����ۿ�ļ���
                            && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id()))        //Ʒ���ж�
                            || liufqzjs
                            ) {

//						�ж��Ƿ�ǿ�Ƽ����ʶ��
                        if (!(Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //�����ָ����Ҫ���⣨�����Ρ�������Χ�����������μ�ͳһ�����ۿ�ļ���
                                && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id())))) {
//							�������������ۿ�����������ǿ�Ƽ���

                            if (Strimplementedzb.equals(rsl.getString("zhib")) && !m_qiangzjs) {
//								��������ϴβ���ǿ�㣬���˴�Ҫǿ��ʱ��Ӧ�ñ���ǿ�������ֱ��������ѭ��
                                continue;
//								m_qiangzjs = true;
                            } else {

                                m_qiangzjs = true;
                            }

                        } else {

                            m_qiangzjs = false;
                        }

                        if ((bsv.isShul_xb_kou_static_status() || bsv.isShul_xb_jiang_static_status()) && rsl.getString("zhib").equals(Locale.jiessl_zhibb)) {
                            Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                            Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                        }
                        if (bsv.isShul_rz_szs_jiang_static_status()) {
                            Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());
                        }

                        //ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
                        Strimplementedzb = rsl.getString("zhib");    //��¼��ʹ�õ�ָ��
                        Dblimplementedzbsx = rsl.getDouble("shangx");

                        if (bsv.getJiesxs().equals(Locale.jiaqfl_jiesxs)) {
//							���������ʽΪ����Ȩ���С���ô��¼��Ȩָ������ۿ�������

//							û�б���ֵ����Ȩƽ����û�б�����ֵ
                            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

                                if (bsv.getJiaqfl_zhibsz()[i][0].equals(rsl.getString("zhib"))) {

                                    bsv.getJiaqfl_zhibsz()[i][1] = rsl.getString("danw");
                                    bsv.getJiaqfl_zhibsz()[i][2] = rsl.getString("tiaoj");
                                    bsv.getJiaqfl_zhibsz()[i][3] = rsl.getString("zengfj");
                                    bsv.getJiaqfl_zhibsz()[i][4] = rsl.getString("zengfjgs");
                                    bsv.getJiaqfl_zhibsz()[i][5] = rsl.getString("kouj");
                                    bsv.getJiaqfl_zhibsz()[i][6] = rsl.getString("koujgs");
                                    bsv.getJiaqfl_zhibsz()[i][7] = rsl.getString("zengfjdw");
                                    bsv.getJiaqfl_zhibsz()[i][8] = rsl.getString("koujdw");
                                    bsv.getJiaqfl_zhibsz()[i][9] = rsl.getString("shangx");
                                    bsv.getJiaqfl_zhibsz()[i][10] = rsl.getString("xiax");
                                    bsv.getJiaqfl_zhibsz()[i][11] = rsl.getString("jis");
                                    bsv.getJiaqfl_zhibsz()[i][12] = rsl.getString("jisdw");
                                    bsv.getJiaqfl_zhibsz()[i][13] = rsl.getString("jizzkj");
                                    bsv.getJiaqfl_zhibsz()[i][14] = rsl.getString("xiaoscl");
                                    bsv.getJiaqfl_zhibsz()[i][15] = rsl.getString("shiyfw");
                                    bsv.getJiaqfl_zhibsz()[i][16] = String.valueOf(m_qiangzjs);

                                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                            && rsl.getString("danw").equals(Locale.wandun_danw)) {
//										Ϊ�˴������ۿ����ָ��Ϊ���������������ҵ�λΪ ����֡�ʱʹ��
                                        bsv.getJiaqfl_zhibsz()[i][1] = Locale.dun_danw;
                                        bsv.getJiaqfl_zhibsz()[i][7] = String.valueOf(CustomMaths.mul(rsl.getDouble("shangx"), 10000));
                                        bsv.getJiaqfl_zhibsz()[i][8] = String.valueOf(CustomMaths.mul(rsl.getDouble("xiax"), 10000));
                                    }

                                    continue;
                                }
                            }
                            continue;

                        } else if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)
                                && rsl.getString("jiesxs").equals(Locale.jiaqpj_jiesxs)
                                && rsl.getString("zhib").equals(Locale.jiessl_zhibb)) {
//								����۸�Ľ�����ʽΪ�������ν��㡱�����ۿ��С�����������ָ��Ľ�����ʽΪ����Ȩƽ����
//							��ʱ������Ϊ������������ָ������ۿ�Ӧ�����е����ν�����ɺ��ٽ��С�
                            continue;

                        } else {

//							������ʽ���ǡ���Ȩ���С�
                            m_zhib = rsl.getString("zhib");
                            m_danw = rsl.getString("danw");
                            m_tiaoj = rsl.getString("tiaoj");
                            m_zengfj = rsl.getDouble("zengfj");
                            m_zengfjgs = rsl.getString("zengfjgs");
                            m_kouj = rsl.getDouble("kouj");
                            m_koujgs = rsl.getString("koujgs");
                            m_zengfjdw = rsl.getString("zengfjdw");
                            m_koujdw = rsl.getString("koujdw");
                            m_shangx = rsl.getDouble("shangx");
                            m_xiax = rsl.getDouble("xiax");
                            m_jis = rsl.getDouble("jis");
                            m_jisdw = rsl.getString("jisdw");
                            m_jizzkj = rsl.getDouble("jizzkj");
                            m_xiaoscl = rsl.getInt("xiaoscl");
                            m_shiyfw = rsl.getInt("shiyfw");
                            m_jijlx = rsl.getString("jijlx");

                            if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                    && rsl.getString("danw").equals(Locale.wandun_danw)) {
//								Ϊ�˴������ۿ����ָ��Ϊ���������������ҵ�λΪ ����֡�ʱʹ��
                                m_danw = Locale.dun_danw;
                                m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                                m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                                Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                            }
                        }

//						����ݵ�ָ����ں�ͬ��׼ʱ����ȷ�Ͻ�����ǿ�Ƽ�������ã����ǵ����ν�����ϸ������Ϊ������Ӧ����Ϣ��
                        bsh.set(m_zhib + Jiesdcz.getZhibbdw(m_zhib, m_danw), Dbltmp);    //����ֵ
                        bsh.set(m_zhib + "������λ", m_danw);            //ָ�굥λ�������޵�λ��
                        bsh.set(m_zhib + "���ۿ�����", m_tiaoj);            //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                        bsh.set(m_zhib + "��������", m_zengfj);            //������
                        bsh.set(m_zhib + "�������۹�ʽ", m_zengfjgs == null ? "" : m_zengfjgs);        //�����۹�ʽ
                        bsh.set(m_zhib + "�۸�����", m_kouj);            //�ۼ�
                        bsh.set(m_zhib + "�۸����۹�ʽ", m_koujgs == null ? "" : m_koujgs);            //�ۼ۹�ʽ
                        bsh.set(m_zhib + "�����۵�λ", m_zengfjdw == null ? "" : m_zengfjdw);    //���۵�λ
                        bsh.set(m_zhib + "�۸��۵�λ", m_koujdw == null ? "" : m_koujdw);    //�ۼ۵�λ
                        bsh.set(m_zhib + "����", m_shangx);        //ָ������
                        bsh.set(m_zhib + "����", m_xiax);            //ָ������
                        bsh.set(m_zhib + "���ۿ����", m_jis);            //������ÿ����xx�򽵵�xx��
                        bsh.set(m_zhib + "���ۿ������λ", m_jisdw);    //������λ
                        bsh.set(m_zhib + "��׼���ۼ�", m_jizzkj);        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                        bsh.set(m_zhib + "С������", Jiesdcz.getTransform_Xiaoscl(m_xiaoscl));        //С������ÿ����xx�򽵵�xx��
                        bsh.set(m_zhib + "��������", m_jijlx);        //��������(0Ϊ��˰��1Ϊ����˰)
                        bsh.set(m_zhib + "ǿ�Ƽ���", String.valueOf(m_qiangzjs));    //ǿ�Ƽ����־

                        Strtmp += "'" + m_zhib + "',";                    //��¼�û����õ�Ӱ����㵥�۵�ָ��
                        Feitsclzb += m_zhib + ",";                        //��¼���ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ��
//						�������ۿ����÷�Χ������
//							ԭ���Ƚ����ۿ��������Ϣ��¼��һ��StringBuffer�����У���ʽΪ:Qnetar,1;��������,1;
//							(ע�����shiyfwΪ1��Ϊ�ǳ����������ã��ż�¼�������0����������ȫ�����ݣ����ü�¼)
//							ʹ��ʱ�������StringBufffer
                        if (m_shiyfw > 0) {

//							���÷�ΧΪ1 ˵��ֻ�Գ������ֽ��в���
                            sb.append(m_zhib).append(",").append(m_shiyfw).append(";");
                        }
                    }
//					����ELSE��δִ�е�ָ����м�¼
                }
            } else {
//				Falg=false ˵���Ǵ����������ۿ���㣬��ʱ���ָ�겻�����⴦�������� Ӧ������
                while (rsl.next()) {

                    if (Tsclzb_item[1].equals(rsl.getString("zhib"))) {

//						�õ����ۿ�ָ��ֵ
                        if (Tsclzb_item[Tsclzb_item.length - 2].equals("'C'")) {
//							��ʾ�ǶԳ������ֽ����������ۿ����
                            Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                        } else {
//							��ʾ�Լ�Ȩƽ���еĵ����ε����⴦���߼�
                            Dbltmp = Double.parseDouble(Tsclzb_item[Tsclzb_item.length - 2]);
                        }


//						�����¼������ۿ�����⴦���ָ��������־
                        Jiesdcz.Mark_Tsclzbs_bz(bsv.getTsclzbs(), Tsclzb_item[0] + "," + rsl.getString("zhib"));

//						�õ�������Ŀ�Ľ����׼(ֻ�����ڼ�Ȩƽ���Ĳ�����Ŀ)
                        Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");

//						ָ��Ľ���ָ��
                        Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                        Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

//						�������ǿ�Ƽ����ʶ
                        boolean liufqzjs = false;
                        if (MainGlobal.getXitxx_item("����", "����Ƿ����ǿ�Ƽ���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��")) {
                            liufqzjs = (rsl.getString("zhib").equals(Locale.Std_zhibb) || rsl.getString("zhib").equals(Locale.Stad_zhibb) || rsl.getString("zhib").equals(Locale.Star_zhibb));    //����std/stad/star��ǿ�����ۿ����
                        }

                        if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                && !Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //�����ָ����Ҫ���⣨�����Σ����������μ�ͳһ�����ۿ�ļ���
                                && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id()))            //Ʒ���ж�
                                || liufqzjs
                                ) {
//							�ж��Ƿ�ʹ��ǿ�Ƽ���
                            if (!(Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                                    && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))
                                    && Jiesdcz.getJieszbtscl_Zkk(bsv.getJiesxs(), rsl.getString("jiesxs"), rsl.getInt("shiyfw"), rsl.getString("zhib"))    //�����ָ����Ҫ���⣨�����Ρ�������Χ�����������μ�ͳһ�����ۿ�ļ���
                                    && Jiesdcz.getHet_condition(rsl.getString("pinzb_id"), String.valueOf(bsv.getRanlpzb_Id())))) {
                                //							�������������ۿ�����������ǿ�Ƽ���
                                if (Strimplementedzb.equals(rsl.getString("zhib")) && !m_qiangzjs) {
                                    //								��������ϴβ���ǿ�㣬���˴�Ҫǿ��ʱ��Ӧ�ñ���ǿ�������ֱ��������ѭ��
                                    continue;
                                } else {

                                    m_qiangzjs = true;
                                }
                            } else {

                                m_qiangzjs = false;
                            }

                            //ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
                            Strimplementedzb = rsl.getString("zhib");    //��¼��ʹ�õ�ָ��
                            Dblimplementedzbsx = rsl.getDouble("shangx");

                            if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                                    && rsl.getString("danw").equals(Locale.wandun_danw)) {
//								Ϊ�˴������ۿ����ָ��Ϊ���������������ҵ�λΪ ����֡�ʱʹ��
                                m_danw = Locale.dun_danw;
                                m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                                m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                                Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                            } else {

                                m_danw = rsl.getString("danw");
                                m_shangx = rsl.getDouble("shangx");
                                m_xiax = rsl.getDouble("xiax");
                            }

                            bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), m_danw), Dbltmp);    //����ֵ
                            bsh.set(rsl.getString("zhib") + "������λ", m_danw);            //ָ�굥λ�������޵�λ��
                            bsh.set(rsl.getString("zhib") + "���ۿ�����", rsl.getString("tiaoj"));        //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                            bsh.set(rsl.getString("zhib") + "��������", rsl.getDouble("zengfj"));        //������
                            bsh.set(rsl.getString("zhib") + "�������۹�ʽ", rsl.getString("zengfjgs") == null ? "" : rsl.getString("zengfjgs"));        //�����۹�ʽ
                            bsh.set(rsl.getString("zhib") + "�۸�����", rsl.getDouble("kouj"));            //�ۼ�
                            bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", rsl.getString("koujgs") == null ? "" : rsl.getString("koujgs"));        //�ۼ۹�ʽ
                            bsh.set(rsl.getString("zhib") + "�����۵�λ", rsl.getString("zengfjdw") == null ? "" : rsl.getString("zengfjdw"));    //���۵�λ
                            bsh.set(rsl.getString("zhib") + "�۸��۵�λ", rsl.getString("koujdw") == null ? "" : rsl.getString("koujdw"));    //�ۼ۵�λ
                            bsh.set(rsl.getString("zhib") + "����", m_shangx);        //ָ������
                            bsh.set(rsl.getString("zhib") + "����", m_xiax);            //ָ������
                            bsh.set(rsl.getString("zhib") + "���ۿ����", rsl.getDouble("jis"));            //������ÿ����xx�򽵵�xx��
                            bsh.set(rsl.getString("zhib") + "���ۿ������λ", rsl.getString("jisdw"));    //������λ
                            bsh.set(rsl.getString("zhib") + "��׼���ۼ�", rsl.getDouble("jizzkj"));        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                            bsh.set(rsl.getString("zhib") + "С������", Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));        //С������ÿ����xx�򽵵�xx��
                            bsh.set(rsl.getString("zhib") + "��������", rsl.getString("jijlx"));        //��������(0Ϊ��˰��1Ϊ����˰)
                            bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", String.valueOf(m_qiangzjs));        //С������ÿ����xx�򽵵�xx��

                            Strtmp += "'" + rsl.getString("zhib") + "',";                    //��¼�û����õ�Ӱ����㵥�۵�ָ��

//							��Ϊ���ָ��ֵӦ�������ۿ����
                            bsv.setTsclzbzkksfxyjs(true);

//							�������ۿ����÷�Χ������
//								ԭ���Ƚ����ۿ��������Ϣ��¼��һ��StringBuffer�����У���ʽΪ:Qnetar,1;��������,1;
//								(ע�����shiyfwΪ1��Ϊ�ǳ����������ã��ż�¼�������0����������ȫ�����ݣ����ü�¼)
//								ʹ��ʱ�������StringBufffer

                            if (rsl.getInt("shiyfw") > 0) {

//								���÷�ΧΪ1 ˵��ֻ�Գ������ֽ��в���
                                sb.append(rsl.getString("zhib")).append(",").append(rsl.getInt("shiyfw")).append(";");
                            }
                        }
//						����ELSE��δִ�е�ָ����м�¼

                    }
                }
            }

            bsv.setMeikzkksyfw(sb);    //��¼ú�����ۿ������÷�ΧΪ�������ֵ�����
            bsv.setFeitsclzb(Feitsclzb); //��¼���ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ��

//			if(Strtmp.equals("")){
//
//				if(!bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){
//
//					bsv.setErroInfo("ϵͳ��û�з���Ҫ������ۿ��");
//					return false;
//				}
//
//			}

            String Strtmpdw = "";

//			ȡ��zhibb��û����hetzkkb�����ֵ���Ŀ��Ŀ����ҲҪ�ŵ���ʽ��ȥ����
            if (!Falg) {
//				��������⴦������ۿ���Ŀ���ڼ۸��и�ֵ��yifzzb(�Ѹ�ֵָ��)�ڴ˴��Ͳ������ˣ�Ӧ�����
                bsv.setYifzzb("");
            }
            if (!Strtmp.equals("") && bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";
            } else if (!Strtmp.equals("") && !bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp + "'" + bsv.getYifzzb() + "') and leib=1 ";
            } else if (!bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in ('" + bsv.getYifzzb() + "') and leib=1 ";
            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            rsl = con.getResultSetList(sql);

            String l_qiangzjs = "false";
            while (rsl.next()) {
//					����IF�ж϶Լ�¼�ڰ���ָ�����ǿ���ж�
//					�������������ǿ�Ƽ����ʶ����ΪTrue
//				    l_qiangzjs="true"
                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");
                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //����ֵ
                bsh.set(rsl.getString("zhib") + "������λ", Strtmpdw);                //ָ�굥λ
                bsh.set(rsl.getString("zhib") + "���ۿ�����", "����");                    //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                bsh.set(rsl.getString("zhib") + "��������", 0);                            //������
                bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");                    //������
                bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");                    //�ۼ�
                bsh.set(rsl.getString("zhib") + "�����۵�λ", "");                    //���۵�λ
                bsh.set(rsl.getString("zhib") + "�۸��۵�λ", "");                    //�ۼ۵�λ
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "���ۿ����", 0);                        //������ÿ����xx�򽵵�xx��
                bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");                //������λ
                bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);                        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                bsh.set(rsl.getString("zhib") + "С������", "");                    //С������ÿ����xx�򽵵�xx��
                bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", l_qiangzjs);                    //ǿ�Ƽ����־
            }
            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    //	���ۿ�_��Ȩ����
    private boolean getZengkk_Jiaqfl(Interpreter bsh) {
//		����Ȩ���������ֵ���������ۿ�Ĺ�ʽ��ֵ

        JDBCcon con = new JDBCcon();
        try {
            double Dbltmp = 0;        //��¼ָ�����ֵ
            double Dblczxm = 0;        //��¼������Ŀ��ֵ
            String Strtmp = "";        //��¼�趨��ָ��
            StringBuffer sb = new StringBuffer();    //��¼��ͬ���ۿ��е����÷�ΧΪ1�ļ�¼
            String sql = "";

            for (int i = 0; i < bsv.getJiaqfl_zhibsz().length; i++) {

//				ָ��Ľ���ָ��
                Dbltmp = Jiesdcz.getZhib_info(bsv, bsv.getJiaqfl_zhibsz()[i][0], "js");
                Dbltmp = Jiesdcz.getUnit_transform(bsv.getJiaqfl_zhibsz()[i][0], bsv.getJiaqfl_zhibsz()[i][1], Dbltmp, bsv.getMj_to_kcal_xsclfs());

                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + Jiesdcz.getZhibbdw(bsv.getJiaqfl_zhibsz()[i][0], bsv.getJiaqfl_zhibsz()[i][1]), Dbltmp);    //����ֵ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "������λ", bsv.getJiaqfl_zhibsz()[i][1]);            //ָ�굥λ�������޵�λ��
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "���ۿ�����", bsv.getJiaqfl_zhibsz()[i][2]);            //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "��������", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][3]));            //������
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "�������۹�ʽ", bsv.getJiaqfl_zhibsz()[i][4] == null ? "" : bsv.getJiaqfl_zhibsz()[i][4]);            //�����۹�ʽ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "�۸�����", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][5]));            //�ۼ�
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "�۸����۹�ʽ", bsv.getJiaqfl_zhibsz()[i][6] == null ? "" : bsv.getJiaqfl_zhibsz()[i][6]);            //�ۼ۹�ʽ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "�����۵�λ", bsv.getJiaqfl_zhibsz()[i][7] == null ? "" : bsv.getJiaqfl_zhibsz()[i][7]);    //���۵�λ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "�۸��۵�λ", bsv.getJiaqfl_zhibsz()[i][8] == null ? "" : bsv.getJiaqfl_zhibsz()[i][8]);    //�ۼ۵�λ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "����", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][9]));            //ָ������
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "����", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][10]));            //ָ������
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "���ۿ����", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][11]));            //������ÿ����xx�򽵵�xx��
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "���ۿ������λ", bsv.getJiaqfl_zhibsz()[i][12]);            //������λ
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "��׼���ۼ�", Double.parseDouble(bsv.getJiaqfl_zhibsz()[i][13]));        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "С������", Jiesdcz.getTransform_Xiaoscl(Integer.parseInt(bsv.getJiaqfl_zhibsz()[i][14])));        //С������ÿ����xx�򽵵�xx��
                bsh.set(bsv.getJiaqfl_zhibsz()[i][0] + "ǿ�Ƽ���", bsv.getJiaqfl_zhibsz()[i][16]);        //С������ÿ����xx�򽵵�xx��
                Strtmp += "'" + bsv.getJiaqfl_zhibsz()[i][0] + "',";                    //��¼�û����õ�Ӱ����㵥�۵�ָ��
            }

            if (!Strtmp.equals("") && bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";
            } else if (!Strtmp.equals("") && !bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp + "'" + bsv.getYifzzb() + "') and leib=1 ";
            } else if (!bsv.getYifzzb().equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in ('" + bsv.getYifzzb() + "') and leib=1 ";
            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            ResultSetList rsl = con.getResultSetList(sql);

            String Strtmpdw = "";

            while (rsl.next()) {


                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");

                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //����ֵ
                bsh.set(rsl.getString("zhib") + "������λ", Strtmpdw);                //ָ�굥λ
                bsh.set(rsl.getString("zhib") + "���ۿ�����", "����");                    //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                bsh.set(rsl.getString("zhib") + "��������", 0);                            //������
                bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");                    //�����۹�ʽ
                bsh.set(rsl.getString("zhib") + "�۸�����", 0);                            //�ۼ�
                bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");                    //�ۼ۹�ʽ
                bsh.set(rsl.getString("zhib") + "�����۵�λ", "");                    //���۵�λ
                bsh.set(rsl.getString("zhib") + "�۸��۵�λ", "");                    //�ۼ۵�λ
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "���ۿ����", 0);                        //������ÿ����xx�򽵵�xx��
                bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");                //������λ
                bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);                        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                bsh.set(rsl.getString("zhib") + "С������", "");                    //С������ÿ����xx�򽵵�xx��
                bsh.set(rsl.getString("zhib") + "ǿ�Ƽ���", "false");                    //ǿ�Ƽ���
            }
            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    private long getYunshtb_id(long Hetb_id) {
//		�õ�hetys.id
        JDBCcon con = new JDBCcon();
        long lngYunshtb_id = 0;
        long lngFinYshtb_id = 0;

//		���1���������Ʊ���㡢�ȴ�ú���ͬ����ȡ�˷ѡ�
//		���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid,
//		�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�

        try {
//			��getMeiprise�����У��Ѿ���¼�º�ͬ���õ�hetjgb_id
            ResultSet rs = null;
            String sql = "select yunj,danwb.bianm as yunjdw from hetjgb,danwb 	\n"
                    + " where hetjgb.yunjdw_id=danwb.id and hetjgb.id=" + bsv.getHetjgb_id();
            ResultSet rec = con.getResultSet(sql);
            while (rec.next()) {

                bsv.setHetyj(rec.getDouble("yunj"));
                bsv.setHetyjdw(rec.getString("yunjdw"));
            }

            if (bsv.getHetyj() == 0) {
//				���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid

                sql = "select hetys_id from meikyfhtglb where hetb_id=" + Hetb_id;
                rec = con.getResultSet(sql);
                while (rec.next()) {
//					�ж���˷Ѻ�ͬ
                    lngYunshtb_id = rec.getLong("hetys_id");
                    sql = "select hetys.id from hetysjgb,hetys 	\n"
                            + " where hetys.id=hetysjgb.hetys_id	\n"
                            + " and hetys.id=" + lngYunshtb_id + " 		\n"
                            + " and (meikxxb_id=" + bsv.getMeikxxb_Id()
                            + " or meikxxb_id = 0)";

                    rs = con.getResultSet(sql);
                    while (rs.next()) {

                        lngFinYshtb_id = lngYunshtb_id;
                        return lngFinYshtb_id;
                    }
                }
            }
            if (rs != null) {

                rs.close();
            }
            if (rec != null) {

                rec.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
        return 0;
    }

    private void countYf_jiuq(String Selids, long Hetb_id, double Shangcjsl) {

//		�����˷�
//		˼·��
//			���1��ú���ͬ�����˼ۣ�bsv.getHetyj()>0�����˼۵�λ�����֣�Ԫ/�֣�Ԫ/��*���,
//					û�����ۿ����.��Ϊû���õ��˷Ѻ�ͬ�����Բ��������ۿ�
//			���2����Hetyj��Hetyjdw��ֵ,Ҫ��hetysjgb��ȡֵ�������ۿ�
        try {
            String Yunfdpc[][] = null;    //��ά���飬�����˷ѵ����ν���ķ���ֵ
            Interpreter bsh = new Interpreter();
//			�˷Ѻ�˰���۱���С��λ
            if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//				���1
//				ȡhetb�е��˼�

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					�˷ѽ������������Ϊ��
//					�����������ν����˷�
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());
                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							�߼��������Ҫ�˷Ѱ�ĳһ�������������ν��㣬Ӧ����Ϊ�˷ѽ���ؼ��ָ�ֵ
//								1������ú���ͬ�к��˷ѵ�����Ӽ۸�ĽǶȣ�ֻ�����»�á��˾ࡱָ�꼴�ɡ�
                            bsh.set("��ͬ�˼�", bsv.getHetyj());
                            bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
//							bsh.set("�˼����", bsv.getYunju_js());

                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                    , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                            bsh.set("�˼����", bsv.getYunju_js());

                            if (!bsv.getGongs_Yf().equals("")) {

//								�������η��������������ָ��
                                this.getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                        , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                        Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//								�˷Ѻ�˰���۱���С��λ
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                getZengkk_yf(Hetb_id, bsh);    //�����κ�����ֻ�ǹ�ʽ�ϲ�����
//								���˷�
                                bsh.eval(bsv.getGongs_Yf());

                                setJieszb(bsh, 1, Shangcjsl);
//								ú��λ
                                bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0]));
//								�˾�
//								2010-05-12 zsj�ģ�
//								��������һ���糧�����������⣬����������������˾಻ͬ����ú�����ʱ�˾ఴ��Сֵ���н���
                                bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                        , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                bsv.setYunju_cf(bsv.getYunju_js());

                                computData_Yf("0");

                            } else {

                                bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
                                return;
                            }
                        }
                    }
                } else {

                    bsh.set("��ͬ�˼�", bsv.getHetyj());
                    bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
                    bsh.set("�˼����", bsv.getYunju_js());

                    if (!bsv.getGongs_Yf().equals("")) {

//						�˷Ѻ�˰���۱���С��λ
                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                        getZengkk_yf(Hetb_id, bsh);    //�����κ�����ֻ�ǹ�ʽ�ϲ�����
//						���˷�
                        bsh.eval(bsv.getGongs_Yf());

                        setJieszb(bsh, 1, Shangcjsl);

                        computData_Yf("0");

                    } else {

                        bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
                        return;
                    }
                }

            } else {

//				ȡhetys�е��˼�
                JDBCcon con = new JDBCcon();
                String sql = "";
                ResultSetList rsl = null;

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					�˷ѽ������������Ϊ��
//					�����������ν����˷�
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());

                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        String m_meikxxb_id = "";

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							�߼��������Ҫ�˷Ѱ�ĳһ�������������ν��㣬Ӧ����Ϊ�˷ѽ���ؼ��ָ�ֵ
//							1������ʹ�õ����˷Ѻ�ͬ��������˷ѽ���Ӽ۸�ĽǶȣ�ҪΪú���˾ࡢ����ָ�����¸�ֵ��

                            if (Yunfdpc[i][0].indexOf(",") > -1) {
//								����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                String m_tmp[] = null;
                                m_tmp = Yunfdpc[i][0].split(",");
                                m_meikxxb_id = m_tmp[0];
                            } else {

                                m_meikxxb_id = Yunfdpc[i][0];
                            }

//							�õ������ͬ�۸�
                            sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                                    + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                                    + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                                    + " danwb yjdw										\n"
                                    + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                                    + " and jg.tiaojb_id=tj.id(+)						\n"
                                    + " and jg.danwb_id=dw.id(+)						\n"
                                    + " and jg.yunjdw_id=yjdw.id(+)						\n"
                                    + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                                    + m_meikxxb_id + " or jg.meikxxb_id = 0)		\n";

                            rsl = con.getResultSetList(sql);

                            if (rsl.next()) {
//								�����ν���
//								����Ƿ��н���۸񷽰��������˵�������ⷽʽ���㣬����ԭ���ĺ�ͬ���ۿ���ʽ
                                if (rsl.getLong("yunsjgfab_id") > 0) {
//									�����˷ѽ��㷽���������㷨
                                    bsh.set("��ͬ�˼�", 0);
                                    bsh.set("��ͬ�˼۵�λ", Locale.yuanmd_danw);
                                    bsh.set("�˼����", 0);

//									�����˷Ѽ۸���ϸ�е�����
                                    bsh = Jiesdcz.CountYfjsfa(bsv, Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                            bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

//									����ͬ�˼۸�ֵ
                                    bsv.setHetyj(Double.parseDouble(bsh.get("��ͬ�˼�").toString()));
                                    bsv.setHetyjdw(Locale.yuanmd_danw);

                                    if (Double.parseDouble(bsh.get("��ͬ�˼�").toString()) == 0) {

                                        bsv.setErroInfo("û�к�����۸񷽰�ƥ������ݣ�");
                                        return;
                                    }

//									�������η��������������ָ��
                                    getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                            , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                            Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//									������ۿ�
                                    getZengkk_yf(Hetb_id, bsh);

//									�˷Ѻ�˰���۱���С��λ
                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                    bsh.eval(bsv.getGongs_Yf());
                                    setJieszb(bsh, 1, Shangcjsl);

//									Ϊú����Ϣ��ֵ
                                    bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]));

                                    computData_Yf("0");

                                } else {
//									�����ν���
//									û�н���۸񷽰�����ԭ���ļ۸���������ۿ�������н���
                                    String where_condition = "";
                                    if (rsl.getRows() == 1) {
//										��һ����ͬ�۸�
                                        bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));

//										���Ӷ����ǵ糧Aϵͳ��Bϵͳ�Ĵ���
                                        if (Yunfdpc[i][0].indexOf(",") > -1) {
//											����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                            String m_tmp[] = null;
                                            m_tmp = Yunfdpc[i][0].split(",");

                                            where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                        }

                                        bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                        + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                        + where_condition
                                                        + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                        bsh.set("�˼����", bsv.getYunju_js());

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                            return;
                                        }

//										�������η��������������ָ��
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }
                                        if (jiessl > 0) {
//											�жϸõ������˷ѽ����Ƿ���������Ҫ��û�оͲ���������

//											������ۿ�
                                            getZengkk_yf(Hetb_id, bsh);

//											�˷Ѻ�˰���۱���С��λ
                                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                            bsh.eval(bsv.getGongs_Yf());
                                            setJieszb(bsh, 1, Shangcjsl);
//											ú����Ϣ��id
                                            bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//											�˾�
                                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(min(gl.zhi),0) as zhi"
                                                    , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                            + where_condition
                                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                            bsv.setYunju_cf(bsv.getYunju_js());

                                            computData_Yf("0");
                                        }

                                    } else {
//										�����ν���
//										�����ͬ�۸�
                                        double shangx = 0;
                                        double xiax = 0;
                                        double yunju = bsv.getYunju_cf();    //�˾�
                                        double Dbltmp = 0;


//										�������η��������������ָ��
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }

                                        if (jiessl > 0) {
//											�жϸõ������˷ѽ����Ƿ���������Ҫ��û�оͲ���������
                                            do {
                                                shangx = rsl.getDouble("shangx");
                                                xiax = rsl.getDouble("xiax");

                                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

//												�����˷Ѽ۸�Ŀǰֻ���˾��йأ�����ȷ���˷Ѽ۸�ʱֻ���˾�ָ��
//												�������˷ѽ���Ҫ���»��yunjuָ��

                                                if (Yunfdpc[i][0].indexOf(",") > -1) {
//													����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                                    String m_tmp[] = null;
                                                    m_tmp = Yunfdpc[i][0].split(",");

                                                    where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                                }

                                                yunju = Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                        , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                                + where_condition
                                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'"));

                                                if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                                    bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                                    bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                                    bsh.set("�˼����", bsv.getYunju_js());

                                                    bsv.setHetyj(rsl.getDouble("yunja"));
                                                    bsv.setHetyjdw(rsl.getString("yunjdw"));
                                                    bsv.setYunju_js(yunju);

                                                    bsh.set("�˼����", yunju);

                                                    if (rsl.getDouble("yunja") == 0) {

                                                        bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                                        return;
                                                    }

                                                    getZengkk_yf(Hetb_id, bsh);

//													�˷Ѻ�˰���۱���С��λ
                                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                                    bsh.eval(bsv.getGongs_Yf());
                                                    setJieszb(bsh, 1, Shangcjsl);
//													ú����Ϣ��
                                                    bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//													�˾�
                                                    bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                            , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                    + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + "	\n"
                                                                    + where_condition
                                                                    + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                                    bsv.setYunju_cf(bsv.getYunju_js());

                                                    computData_Yf("0");
                                                    break;
                                                }

                                            } while (rsl.next());
                                        }
                                    }
                                }
                            } else {

                                bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
                                return;
                            }
                        }
                    }

                } else {

                    sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                            + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                            + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                            + " danwb yjdw										\n"
                            + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                            + " and jg.tiaojb_id=tj.id(+)						\n"
                            + " and jg.danwb_id=dw.id(+)						\n"
                            + " and jg.yunjdw_id=yjdw.id(+)						\n"
                            + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                            + bsv.getMeikxxb_Id() + " or jg.meikxxb_id = 0)		\n";

                    rsl = con.getResultSetList(sql);

                    if (rsl.next()) {
//						����Ƿ��н���۸񷽰��������˵�������ⷽʽ���㣬����ԭ���ĺ�ͬ���ۿ���ʽ
                        if (rsl.getLong("yunsjgfab_id") > 0) {
//							�����˷ѽ��㷽���������㷨�����㷨�˾��Ǻ����˼��еģ����˼��Ƕ�Σ��ʲ������¸�ֵ
                            bsh.set("��ͬ�˼�", 0);
                            bsh.set("��ͬ�˼۵�λ", Locale.yuanmd_danw);
                            bsh.set("�˼����", 0);

//							�����˷Ѽ۸���ϸ�е�����
                            bsh = Jiesdcz.CountYfjsfa(bsv, bsv.getMeikxxb_Id(), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                    bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

                            bsv.setHetyj(Double.parseDouble(bsh.get("��ͬ�˼�").toString()));
                            bsv.setHetyjdw(Locale.yuanmd_danw);

                            if (Double.parseDouble(bsh.get("��ͬ�˼�").toString()) == 0) {

                                bsv.setErroInfo("û�к�����۸񷽰�ƥ������ݣ�");
                                return;
                            }

//							������ۿ�
                            getZengkk_yf(Hetb_id, bsh);

//							�˷Ѻ�˰���۱���С��λ
                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                            bsh.eval(bsv.getGongs_Yf());
                            setJieszb(bsh, 1, Shangcjsl);

                        } else {
//							û�н���۸񷽰�����ԭ���ļ۸���������ۿ�������н���
                            if (rsl.getRows() == 1) {
//								��һ����ͬ�۸�
//								yuss
                                String yunxkdxs = "";//�������ϵ��
                                double hetyj = 0;//��ͬ�˼�
                                double yingk = 0;//����ӯ��
                                double yunxkssl = 0;//�����������
                                double kaohkssl = 0;//���˿�������
                                double biaoz = 0;//Ʊ��
                                double hetmj = 0;//��ͬú��
                                double shijyfdj = 0;//ʵ���˷ѵ���
                                hetyj = rsl.getDouble("yunja");
                                //�õ���������ʵ������
                                String sql_fah = "select sum(biaoz) biaoz,sum(yingk) yingk from fahb f where f.lie_id in(" + Selids + ")";
                                ResultSetList rsl_fah = con.getResultSetList(sql_fah);
                                if (rsl_fah.next()) {//
                                    yingk = rsl_fah.getDouble("yingk");
                                    biaoz = rsl_fah.getDouble("biaoz");
                                }
                                //�õ��������ϵ��
                                String sql_yunxkdxs =

                                        "select a.zhi\n" +
                                                "   from (select zhi, jiesszbmb_id\n" +
                                                "           from jiesszb\n" +
                                                "             where jiesszfab_id in\n" +
                                                "                (select jiesszfab_id\n" +
                                                "                   from jiesszfahtglb gl\n" +
                                                "                      where hetb_id = (select hetb_id\n" +
                                                "                           from meikyfhtglb\n" +
                                                "                          where hetys_id = " + Hetb_id + ")\n" +
                                                "                 )) a,\n" +
                                                " (select id from jiesszbmb where bianm = '�������ϵ��') b\n" +
                                                " where a.jiesszbmb_id = b.id\n" +
                                                "";

                                ResultSetList rsl_yunxkdxs = con.getResultSetList(sql_yunxkdxs);
                                if (rsl_yunxkdxs.next()) {//˵���������÷����������ˡ��������ϵ���������϶����ص�ú
                                    yunxkdxs = rsl_yunxkdxs.getString("zhi");
                                    yunxkssl = CustomMaths.Round_New(biaoz * Double.parseDouble(yunxkdxs), 4);
                                    if (-yingk <= yunxkssl) {//���������������֮�ڣ����ý��п���

                                        bsh.set("��ͬ�˼�", hetyj);
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                    } else {//�����𳬹����������ķ�Χ��Ҫ���п���
                                        kaohkssl = (-yingk) - yunxkssl;
                                        String sql_hetmj = "select jij from hetjgb where hetb_id=(select hetb_id from meikyfhtglb where hetys_id=" + Hetb_id + ")";
                                        ResultSetList rsl_hetmj = con.getResultSetList(sql_hetmj);
                                        if (rsl_hetmj.next()) {
                                            hetmj = rsl_hetmj.getDouble("jij");
                                        }
                                        //ʵ���˷ѵ���
                                        shijyfdj = CustomMaths.Round_new((biaoz * hetyj - kaohkssl * hetmj - kaohkssl * hetyj) / biaoz, 2);

                                        bsh.set("��ͬ�˼�", shijyfdj);
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

                                        bsv.setHetyj(shijyfdj);
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                    }

                                    rsl_fah.close();
                                    rsl_yunxkdxs.close();
                                }
                                //yuss
                                /*

								bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
								bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
								bsh.set("�˼����", bsv.getYunju_js());
//								����ͬ�˼۸�ֵ
								bsv.setHetyj(rsl.getDouble("yunja"));
								bsv.setHetyjdw(rsl.getString("yunjdw"));*/

                                if (rsl.getDouble("yunja") == 0) {

                                    bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                    return;
                                }
//								������ۿ�
                                getZengkk_yf(Hetb_id, bsh);

//								�˷Ѻ�˰���۱���С��λ
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                bsh.eval(bsv.getGongs_Yf());
                                setJieszb(bsh, 1, Shangcjsl);
                                computData_Yf("0");

                            } else {
//								�����ͬ�۸�
                                double shangx = 0;
                                double xiax = 0;
                                double yunju = bsv.getYunju_cf();    //�˾�
                                double Dbltmp = 0;

                                do {
                                    shangx = rsl.getDouble("shangx");
                                    xiax = rsl.getDouble("xiax");

                                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                    if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                        bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

//										����ͬ�˼۸�ֵ
                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                        bsv.setYunju_js(yunju);

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                            return;
                                        }

                                        getZengkk_yf(Hetb_id, bsh);

//										�˷Ѻ�˰���۱���С��λ
                                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                        bsh.eval(bsv.getGongs_Yf());
                                        setJieszb(bsh, 1, Shangcjsl);
                                    }

                                } while (rsl.next());
                            }
                        }
                    } else {

                        bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
                        return;
                    }
                }

                rsl.close();
                con.Close();
            }
//			���ɽ�����ǵ糧����������ϵͳ(Aϵͳ��Bϵͳ)��ҵ�񣬼������ϵͳ��ж���ѣ�����ʾ�ڽ��㵥��ı�ע�С�
            if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id) {
                String zhi = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(visit.getDiancxxb_id()), "��");
                if (zhi.equals("��")) {
                    zhi = MainGlobal.getXitxx_item("����", "����ϵͳж����", String.valueOf(visit.getDiancxxb_id()), "");
                    if (visit.getString19().equals(zhi)) {
                        String xiecf = MainGlobal.getXitxx_item("����", "ÿ��ж����", String.valueOf(visit.getDiancxxb_id()), "6");
                        bsv.setBeiz(bsv.getBeiz() + "ж����:" + Double.parseDouble(xiecf) * bsv.getChes());
                        visit.setDouble17(Double.parseDouble(xiecf) * bsv.getChes());
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void CountYf(long Hetb_id, double Shangcjsl) {
//		�����˷�
//		˼·��
//			���1��ú���ͬ�����˼ۣ�bsv.getHetyj()>0�����˼۵�λ�����֣�Ԫ/�֣�Ԫ/��*���,
//					û�����ۿ����.��Ϊû���õ��˷Ѻ�ͬ�����Բ��������ۿ�
//			���2����Hetyj��Hetyjdw��ֵ,Ҫ��hetysjgb��ȡֵ�������ۿ�
        try {
            String Yunfdpc[][] = null;    //��ά���飬�����˷ѵ����ν���ķ���ֵ
            Interpreter bsh = new Interpreter();
//			�˷Ѻ�˰���۱���С��λ
            if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//				���1
//				ȡhetb�е��˼�

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					�˷ѽ������������Ϊ��
//					�����������ν����˷�
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());
                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							�߼��������Ҫ�˷Ѱ�ĳһ�������������ν��㣬Ӧ����Ϊ�˷ѽ���ؼ��ָ�ֵ
//								1������ú���ͬ�к��˷ѵ�����Ӽ۸�ĽǶȣ�ֻ�����»�á��˾ࡱָ�꼴�ɡ�
                            bsh.set("��ͬ�˼�", bsv.getHetyj());
                            bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
//							bsh.set("�˼����", bsv.getYunju_js());

                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                    , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                            bsh.set("�˼����", bsv.getYunju_js());

                            if (!bsv.getGongs_Yf().equals("")) {

//								�������η��������������ָ��
                                this.getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                        , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                        Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//								�˷Ѻ�˰���۱���С��λ
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                getZengkk_yf(Hetb_id, bsh);    //�����κ�����ֻ�ǹ�ʽ�ϲ�����
//								���˷�
                                bsh.eval(bsv.getGongs_Yf());

                                setJieszb(bsh, 1, Shangcjsl);
//								ú��λ
                                bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0]));
//								�˾�
//								2010-05-12 zsj�ģ�
//								��������һ���糧�����������⣬����������������˾಻ͬ����ú�����ʱ�˾ఴ��Сֵ���н���
                                bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl", "nvl(gl.zhi,0) as zhi"
                                        , "m.id=gl.meikxxb_id and m.id=" + Yunfdpc[i][0] + " and \n"
                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id()
                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                bsv.setYunju_cf(bsv.getYunju_js());

                                computData_Yf("0");

                            } else {

                                bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
                                return;
                            }
                        }
                    }
                } else {

                    bsh.set("��ͬ�˼�", bsv.getHetyj());
                    bsh.set("��ͬ�˼۵�λ", bsv.getHetyjdw());
                    bsh.set("�˼����", bsv.getYunju_js());

                    if (!bsv.getGongs_Yf().equals("")) {

//						�˷Ѻ�˰���۱���С��λ
                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                        getZengkk_yf(Hetb_id, bsh);    //�����κ�����ֻ�ǹ�ʽ�ϲ�����
//						���˷�
                        bsh.eval(bsv.getGongs_Yf());

                        setJieszb(bsh, 1, Shangcjsl);

                        computData_Yf("0");

                    } else {

                        bsv.setErroInfo("�������˷Ѽ��㹫ʽ");
                        return;
                    }
                }

            } else {

//				ȡhetys�е��˼�
                JDBCcon con = new JDBCcon();
                String sql = "";
                ResultSetList rsl = null;

                if (!bsv.getYunfjsdpcfztj().equals("")) {
//					�˷ѽ������������Ϊ��
//					�����������ν����˷�
                    Yunfdpc = Jiesdcz.getYunfjsdpcsz(bsv.getYunfjsdpcfztj(), bsv.getSelIds(), bsv.getGuohxt());

                    if (Yunfdpc != null && Yunfdpc.length > 0) {

                        String m_meikxxb_id = "";

                        for (int i = 0; i < Yunfdpc.length; i++) {
//							�߼��������Ҫ�˷Ѱ�ĳһ�������������ν��㣬Ӧ����Ϊ�˷ѽ���ؼ��ָ�ֵ
//							1������ʹ�õ����˷Ѻ�ͬ��������˷ѽ���Ӽ۸�ĽǶȣ�ҪΪú���˾ࡢ����ָ�����¸�ֵ��

                            if (Yunfdpc[i][0].indexOf(",") > -1) {
//								����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                String m_tmp[] = null;
                                m_tmp = Yunfdpc[i][0].split(",");
                                m_meikxxb_id = m_tmp[0];
                            } else {

                                m_meikxxb_id = Yunfdpc[i][0];
                            }

//							�õ������ͬ�۸�
                            sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                                    + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                                    + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                                    + " danwb yjdw										\n"
                                    + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                                    + " and jg.tiaojb_id=tj.id(+)						\n"
                                    + " and jg.danwb_id=dw.id(+)						\n"
                                    + " and jg.yunjdw_id=yjdw.id(+)						\n"
                                    + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                                    + m_meikxxb_id + " or jg.meikxxb_id = 0)		\n";

                            rsl = con.getResultSetList(sql);

                            if (rsl.next()) {
//								�����ν���
//								����Ƿ��н���۸񷽰��������˵�������ⷽʽ���㣬����ԭ���ĺ�ͬ���ۿ���ʽ
                                if (rsl.getLong("yunsjgfab_id") > 0) {
//									�����˷ѽ��㷽���������㷨
                                    bsh.set("��ͬ�˼�", 0);
                                    bsh.set("��ͬ�˼۵�λ", Locale.yuanmd_danw);
                                    bsh.set("�˼����", 0);

//									�����˷Ѽ۸���ϸ�е�����
                                    bsh = Jiesdcz.CountYfjsfa(bsv, Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                            bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

//									����ͬ�˼۸�ֵ
                                    bsv.setHetyj(Double.parseDouble(bsh.get("��ͬ�˼�").toString()));
                                    bsv.setHetyjdw(Locale.yuanmd_danw);

                                    if (Double.parseDouble(bsh.get("��ͬ�˼�").toString()) == 0) {

                                        bsv.setErroInfo("û�к�����۸񷽰�ƥ������ݣ�");
                                        return;
                                    }

//									�������η��������������ָ��
                                    getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                            , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                            Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

//									������ۿ�
                                    getZengkk_yf(Hetb_id, bsh);

//									�˷Ѻ�˰���۱���С��λ
                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                    bsh.eval(bsv.getGongs_Yf());
                                    setJieszb(bsh, 1, Shangcjsl);

//									Ϊú����Ϣ��ֵ
                                    bsv.setMeikxxb_Id(Long.parseLong(Yunfdpc[i][0].indexOf(",") > 0 ? Yunfdpc[i][0].substring(0, Yunfdpc[i][0].indexOf(",")) : Yunfdpc[i][0]));

                                    computData_Yf("0");

                                } else {
//									�����ν���
//									û�н���۸񷽰�����ԭ���ļ۸���������ۿ�������н���
                                    String where_condition = "";
                                    if (rsl.getRows() == 1) {
//										��һ����ͬ�۸�
                                        bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));

//										���Ӷ����ǵ糧Aϵͳ��Bϵͳ�Ĵ���
                                        if (Yunfdpc[i][0].indexOf(",") > -1) {
//											����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                            String m_tmp[] = null;
                                            m_tmp = Yunfdpc[i][0].split(",");

                                            where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                        }

                                        bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(max(gl.zhi),0) as zhi"
                                                , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                        + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                        + where_condition
                                                        + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                        bsh.set("�˼����", bsv.getYunju_js());

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                            return;
                                        }

//										�������η��������������ָ��
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }
                                        if (jiessl > 0) {
//											�жϸõ������˷ѽ����Ƿ���������Ҫ��û�оͲ���������

//											������ۿ�
                                            getZengkk_yf(Hetb_id, bsh);

//											�˷Ѻ�˰���۱���С��λ
                                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                            bsh.eval(bsv.getGongs_Yf());
                                            setJieszb(bsh, 1, Shangcjsl);
//											ú����Ϣ��id
                                            bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//											�˾�
                                            bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(min(gl.zhi),0) as zhi"
                                                    , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                            + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                            + where_condition
                                                            + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                            bsv.setYunju_cf(bsv.getYunju_js());

                                            computData_Yf("0");
                                        }

                                    } else {
//										�����ν���
//										�����ͬ�۸�
                                        double shangx = 0;
                                        double xiax = 0;
                                        double yunju = bsv.getYunju_cf();    //�˾�
                                        double Dbltmp = 0;


//										�������η��������������ָ��
                                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), Hetb_id
                                                , bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), Shangcjsl,
                                                Jiesdcz.getSql_Where_assemble(Yunfdpc[i][1], Yunfdpc[i][2]));

                                        double jiessl = 0;
                                        if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.daozdt_feiylbb_leib) {

                                            jiessl = bsv.getJiessl();
                                        } else {

                                            jiessl = bsv.getYunfjsl();
                                        }

                                        if (jiessl > 0) {
//											�жϸõ������˷ѽ����Ƿ���������Ҫ��û�оͲ���������
                                            do {
                                                shangx = rsl.getDouble("shangx");
                                                xiax = rsl.getDouble("xiax");

                                                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

//												�����˷Ѽ۸�Ŀǰֻ���˾��йأ�����ȷ���˷Ѽ۸�ʱֻ���˾�ָ��
//												�������˷ѽ���Ҫ���»��yunjuָ��

                                                if (Yunfdpc[i][0].indexOf(",") > -1) {
//													����õ��ķ�������ֵΪ�������һ��һ��Ϊ meikxxb_id ��ֵ
                                                    String m_tmp[] = null;
                                                    m_tmp = Yunfdpc[i][0].split(",");

                                                    where_condition = " and i.bianm=" + m_tmp[1] + " \n";
                                                }

                                                yunju = Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                        , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + " \n"
                                                                + where_condition
                                                                + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'"));

                                                if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                                    bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                                    bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                                    bsh.set("�˼����", bsv.getYunju_js());

                                                    bsv.setHetyj(rsl.getDouble("yunja"));
                                                    bsv.setHetyjdw(rsl.getString("yunjdw"));
                                                    bsv.setYunju_js(yunju);

                                                    bsh.set("�˼����", yunju);

                                                    if (rsl.getDouble("yunja") == 0) {

                                                        bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                                        return;
                                                    }

                                                    getZengkk_yf(Hetb_id, bsh);

//													�˷Ѻ�˰���۱���С��λ
                                                    bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                                    bsh.eval(bsv.getGongs_Yf());
                                                    setJieszb(bsh, 1, Shangcjsl);
//													ú����Ϣ��
                                                    bsv.setMeikxxb_Id(Long.parseLong(m_meikxxb_id));
//													�˾�
                                                    bsv.setYunju_js(Double.parseDouble(MainGlobal.getTableCol("meikxxb m,meiksxglb gl,item i", "nvl(gl.zhi,0) as zhi"
                                                            , "m.id=gl.meikxxb_id and gl.shuxmc = i.id and m.id=" + m_meikxxb_id + " and \n"
                                                                    + " gl.diancxxb_id=" + bsv.getDiancxxb_id() + "	\n"
                                                                    + where_condition
                                                                    + " and gl.shifsy=1 and shuxbm='" + Locale.Yunju_zhibb + "'")));

                                                    bsv.setYunju_cf(bsv.getYunju_js());

                                                    computData_Yf("0");
                                                    break;
                                                }

                                            } while (rsl.next());
                                        }
                                    }
                                }
                            } else {

                                bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
                                return;
                            }
                        }
                    }

                } else {

                    sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,jg.shangx,jg.xiax,dw.bianm as danw,	\n"
                            + " yunja,yjdw.bianm as yunjdw,nvl(yunsjgfab_id,0) as yunsjgfab_id \n"
                            + " from hetys ht,hetysjgb jg,zhibb zb,tiaojb tj,danwb dw,	\n"
                            + " danwb yjdw										\n"
                            + " where ht.id=jg.hetys_id and jg.zhibb_id=zb.id(+)	\n"
                            + " and jg.tiaojb_id=tj.id(+)						\n"
                            + " and jg.danwb_id=dw.id(+)						\n"
                            + " and jg.yunjdw_id=yjdw.id(+)						\n"
                            + " and ht.id=" + Hetb_id + " and (jg.meikxxb_id="
                            + bsv.getMeikxxb_Id() + " or jg.meikxxb_id = 0)		\n";

                    rsl = con.getResultSetList(sql);

                    if (rsl.next()) {
//						����Ƿ��н���۸񷽰��������˵�������ⷽʽ���㣬����ԭ���ĺ�ͬ���ۿ���ʽ
                        if (rsl.getLong("yunsjgfab_id") > 0) {
//							�����˷ѽ��㷽���������㷨�����㷨�˾��Ǻ����˼��еģ����˼��Ƕ�Σ��ʲ������¸�ֵ
                            bsh.set("��ͬ�˼�", 0);
                            bsh.set("��ͬ�˼۵�λ", Locale.yuanmd_danw);
                            bsh.set("�˼����", 0);

//							�����˷Ѽ۸���ϸ�е�����
                            bsh = Jiesdcz.CountYfjsfa(bsv, bsv.getMeikxxb_Id(), bsv.getDiancxxb_id(), bsv.getFaz_Id(),
                                    bsv.getDaoz_Id(), rsl.getLong("yunsjgfab_id"), bsh);

                            bsv.setHetyj(Double.parseDouble(bsh.get("��ͬ�˼�").toString()));
                            bsv.setHetyjdw(Locale.yuanmd_danw);

                            if (Double.parseDouble(bsh.get("��ͬ�˼�").toString()) == 0) {

                                bsv.setErroInfo("û�к�����۸񷽰�ƥ������ݣ�");
                                return;
                            }

//							������ۿ�
                            getZengkk_yf(Hetb_id, bsh);

//							�˷Ѻ�˰���۱���С��λ
                            bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                            bsh.eval(bsv.getGongs_Yf());
                            setJieszb(bsh, 1, Shangcjsl);

                        } else {
//							û�н���۸񷽰�����ԭ���ļ۸���������ۿ�������н���
                            if (rsl.getRows() == 1) {
//								��һ����ͬ�۸�
                                bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                bsh.set("�˼����", bsv.getYunju_js());
//								����ͬ�˼۸�ֵ
                                bsv.setHetyj(rsl.getDouble("yunja"));
                                bsv.setHetyjdw(rsl.getString("yunjdw"));

                                if (rsl.getDouble("yunja") == 0) {

                                    bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                    return;
                                }
//								������ۿ�
                                getZengkk_yf(Hetb_id, bsh);

//								�˷Ѻ�˰���۱���С��λ
                                bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                bsh.eval(bsv.getGongs_Yf());
                                setJieszb(bsh, 1, Shangcjsl);
                                computData_Yf("0");

                            } else {
//								�����ͬ�۸�
                                double shangx = 0;
                                double xiax = 0;
                                double yunju = bsv.getYunju_cf();    //�˾�
                                double Dbltmp = 0;

                                do {
                                    shangx = rsl.getDouble("shangx");
                                    xiax = rsl.getDouble("xiax");

                                    Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");

                                    Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());

                                    if (yunju >= xiax && yunju <= (shangx == 0 ? 1e308 : shangx)) {

                                        bsh.set("��ͬ�˼�", rsl.getDouble("yunja"));
                                        bsh.set("��ͬ�˼۵�λ", rsl.getString("yunjdw"));
                                        bsh.set("�˼����", bsv.getYunju_js());

//										����ͬ�˼۸�ֵ
                                        bsv.setHetyj(rsl.getDouble("yunja"));
                                        bsv.setHetyjdw(rsl.getString("yunjdw"));
                                        bsv.setYunju_js(yunju);

                                        if (rsl.getDouble("yunja") == 0) {

                                            bsv.setErroInfo("��ͬ�˼�Ϊ0�������ͬ��");
                                            return;
                                        }

                                        getZengkk_yf(Hetb_id, bsh);

//										�˷Ѻ�˰���۱���С��λ
                                        bsh.set(Locale.yunfhsdjblxsw_jies, bsv.getYunfhsdjblxsw());

                                        bsh.eval(bsv.getGongs_Yf());
                                        setJieszb(bsh, 1, Shangcjsl);
                                    }

                                } while (rsl.next());
                            }
                        }
                    } else {

                        bsv.setErroInfo("û�еõ��˷Ѻ�ͬ�۸�");
                        return;
                    }
                }

                rsl.close();
                con.Close();
            }
//			���ɽ�����ǵ糧����������ϵͳ(Aϵͳ��Bϵͳ)��ҵ�񣬼������ϵͳ��ж���ѣ�����ʾ�ڽ��㵥��ı�ע�С�
            if (bsv.getJieslx() == Locale.guotyf_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id) {
                String zhi = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(visit.getDiancxxb_id()), "��");
                if (zhi.equals("��")) {
                    zhi = MainGlobal.getXitxx_item("����", "����ϵͳж����", String.valueOf(visit.getDiancxxb_id()), "");
                    if (visit.getString19().equals(zhi)) {
                        String xiecf = MainGlobal.getXitxx_item("����", "ÿ��ж����", String.valueOf(visit.getDiancxxb_id()), "6");
                        bsv.setBeiz(bsv.getBeiz() + "ж����:" + Double.parseDouble(xiecf) * bsv.getChes());
                        visit.setDouble17(Double.parseDouble(xiecf) * bsv.getChes());
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void getZengkk_yf(long Hetb_id, Interpreter bsh) {

//		�˷ѵ����ۿ�ԭ������˷Ѽ۸����õ��˸�ָ�꣬�����������ۿ����ۼƼ���
        JDBCcon con = new JDBCcon();
        try {

            String sql = " select distinct zb.bianm as zhib,tj.bianm as tiaoj,shangx,xiax,dw.bianm as danw,			\n"
                    + " 	jis,jsdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,			\n"
                    + " 	xiaoscl,jizzkj,jizzb,czxm.bianm as canzxm,czxmdw.bianm as canzxmdw,nvl(canzsx,0) as canzsx,	\n"
                    + "		nvl(canzxx,0) as canzxx			\n"
                    + " 	from hetys ht,hetyszkkb zkk,zhibb zb,tiaojb tj,danwb dw,danwb jsdw,danwb kjdw,				\n"
                    + " 		danwb zfjdw,zhibb czxm,danwb czxmdw														\n"
                    + " 		where ht.id=zkk.hetys_id and zkk.zhibb_id=zb.id and zkk.tiaojb_id=tj.id					\n"
                    + "  			and zkk.danwb_id=dw.id and zkk.jisdwid=jsdw.id(+) and zkk.koujdw=kjdw.id(+)			\n"
                    + "  			and zkk.zengfjdw=zfjdw.id(+) and zkk.canzxm=czxm.id(+) and zkk.canzxmdw=czxmdw.id(+)	\n"
                    + " 			and ht.id=" + Hetb_id + " order by zb.bianm,tj.bianm,xiax,shangx ";
            ResultSetList rsl = con.getResultSetList(sql);
            double Dbltmp = 0;        //��¼ָ�����ֵ
            String Strtmp = "";        //��¼�趨��ָ��
            double Dblczxm = 0;        //��¼������Ŀ��ֵ
            String Strimplementedzb = "";    //��¼�Ѿ�ִ�й���ָ�꣨���Ѿ������ִ�е�ָ�꣩��
            double Dblimplementedzbsx = 0;    //��¼��ִ�й���ָ�������
            String m_danw = "";
            double m_shangx = 0;
            double m_xiax = 0;

            while (rsl.next()) {

                Dbltmp = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                Dblczxm = Jiesdcz.getZhib_info(bsv, rsl.getString("canzxm"), "js");
//				ָ��Ľ���ָ��
                Dbltmp = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbltmp, bsv.getMj_to_kcal_xsclfs());
                Dblczxm = Jiesdcz.getUnit_transform(rsl.getString("canzxm"), rsl.getString("canzxmdw"), Dblczxm, bsv.getMj_to_kcal_xsclfs());

                if (Dbltmp >= Jiesdcz.getZengkkxx_Value(Strimplementedzb, rsl.getString("zhib"), Dblimplementedzbsx, rsl.getDouble("xiax")) && Dbltmp <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))
                        && Jiesdcz.getHet_condition(rsl.getString("canzxm"), Dblczxm, rsl.getDouble("canzxx"), rsl.getDouble("canzsx"))

                        ) {

                    if (bsv.getShifyrzcjs().equals("��")) {

                        Dbltmp = Dbltmp - bsv.getKuikjs();

                    } else if (MainGlobal.getXitxx_item("����", "�Ƿ�����ֵ�����", String.valueOf(bsv.getDiancxxb_id()), "��").equals("��")) {

                        Dbltmp = Dbltmp - Double.parseDouble(MainGlobal.getXitxx_item("����", "��������", String.valueOf(bsv.getDiancxxb_id()), "0"));

                    }

                    Strimplementedzb = rsl.getString("zhib");
                    Dblimplementedzbsx = rsl.getDouble("shangx");

                    if (rsl.getString("zhib").equals(Locale.jiessl_zhibb)
                            && rsl.getString("danw").equals(Locale.wandun_danw)) {
//						Ϊ�˴������ۿ����ָ��Ϊ���������������ҵ�λΪ ����֡�ʱʹ��
                        m_danw = Locale.dun_danw;
                        m_shangx = CustomMaths.mul(rsl.getDouble("shangx"), 10000);
                        m_xiax = CustomMaths.mul(rsl.getDouble("xiax"), 10000);
                        Dbltmp = CustomMaths.mul(Dbltmp, 10000);
                    } else {

                        m_danw = rsl.getString("danw");
                        m_shangx = rsl.getDouble("shangx");
                        m_xiax = rsl.getDouble("xiax");
                    }

                    bsh.set(rsl.getString("zhib") + Jiesdcz.getZhibbdw(rsl.getString("zhib"), m_danw), Dbltmp);    //����ֵ
                    bsh.set(rsl.getString("zhib") + "������λ", m_danw);            //ָ�굥λ
                    bsh.set(rsl.getString("zhib") + "���ۿ�����", rsl.getString("tiaoj"));        //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                    bsh.set(rsl.getString("zhib") + "��������", rsl.getDouble("zengfj"));        //������
                    bsh.set(rsl.getString("zhib") + "�۸�����", rsl.getDouble("kouj"));            //�ۼ�
                    bsh.set(rsl.getString("zhib") + "�����۵�λ", rsl.getString("zengfjdw") == null ? "" : rsl.getString("zengfjdw"));    //���۵�λ
                    bsh.set(rsl.getString("zhib") + "�۸��۵�λ", rsl.getString("koujdw") == null ? "" : rsl.getString("koujdw"));    //�ۼ۵�λ
                    bsh.set(rsl.getString("zhib") + "����", m_shangx);        //ָ������
                    bsh.set(rsl.getString("zhib") + "����", m_xiax);            //ָ������
                    bsh.set(rsl.getString("zhib") + "���ۿ����", rsl.getDouble("jis"));            //������ÿ����xx�򽵵�xx��
                    bsh.set(rsl.getString("zhib") + "��׼���ۼ�", rsl.getDouble("jizzkj"));        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                    bsh.set(rsl.getString("zhib") + "С������", Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));        //С������ÿ����xx�򽵵�xx��

                    Strtmp += "'" + rsl.getString("zhib") + "',";                    //��¼�û����õ�Ӱ���˷ѽ��㵥�۵�ָ��
                }
            }

            if (!Strtmp.equals("")) {

                sql = "select distinct bianm as zhib from zhibb where bianm not in (" + Strtmp.substring(0, Strtmp.lastIndexOf(",")) + ") and leib=1 ";

            } else {

                sql = "select distinct bianm as zhib from zhibb where leib=1 ";
            }

            rsl = con.getResultSetList(sql);

            String Strtmpdw = "";

            while (rsl.next()) {


                Strtmpdw = Jiesdcz.getZhibbdw(rsl.getString("zhib"), "");

                bsh.set(rsl.getString("zhib") + Strtmpdw, 0);                        //����ֵ
                bsh.set(rsl.getString("zhib") + "������λ", Strtmpdw);                //ָ�굥λ
                bsh.set(rsl.getString("zhib") + "���ۿ�����", "����");                    //���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
                bsh.set(rsl.getString("zhib") + "��������", 0);                        //������
                bsh.set(rsl.getString("zhib") + "�۸�����", 0);                        //�ۼ�
                bsh.set(rsl.getString("zhib") + "�����۵�λ", "");                    //���۵�λ
                bsh.set(rsl.getString("zhib") + "�۸��۵�λ", "");                    //�ۼ۵�λ
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "����", 0);                        //ָ������
                bsh.set(rsl.getString("zhib") + "���ۿ����", 0);                        //������ÿ����xx�򽵵�xx��
                bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);                        //��׼���ۼۣ����ڶ�����ۼ��ۼ�ʱʹ�ã�
                bsh.set(rsl.getString("zhib") + "С������", "");                    //С������ÿ����xx�򽵵�xx��

            }

            rsl.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    //�����˷ѣ������ӷѣ���˰�۳��������˷�
    private boolean getYunFei(String SelIds, long Jieslx, long Hetb_id, double Shangcjsl) {
        JDBCcon con = new JDBCcon();
        try {

            String sql = "";
            ResultSet rs = null;
            String sql_colum = "";    //�����У������˷��ã�
            String sql_talbe = "";    //���ӱ������˷��ã�
            String sql_where = "";    //Ϊ�˴�������˷�Ϊ��ʱ���õ�
            String strJieslx = "";    //����ׯ�ӵ糧�С������˷ѡ����ֽ������ͣ�������Ʊ����ʱ���˷ѿ����ǡ������˷ѡ����ǡ������˷ѡ���
            //�������߶��У���ô�ڴ˽���Ʊ�����е��˷�ת���ɡ������˷ѡ��͡������˷ѡ���
            //Ŀǰ��Ʊ����ʱ���˷�����ʱ��ô����
            long lngJieslx = 0;
            long lngYunshtb_id = 0;
            lngJieslx = Jieslx;
            strJieslx = String.valueOf(Jieslx);
            String yunjslx = MainGlobal.getXitxx_item("����", "�˷�˰�Ƿ�����ֵ˰", String.valueOf(visit.getDiancxxb_id()), "��");

//				���ɽ�����ǵ糧����������Aϵͳ��Bϵͳ����ú�����ֿ������˷ѽ���
            String zhi = "��";
            String guohxt = "";
            if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.dityf_feiylbb_id) {
                zhi = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(visit.getDiancxxb_id()), "��");
                if (zhi.equals("��")) {
                    guohxt = "and c.zhongchh = '" + visit.getString19() + "'\n";
                }
            }


            if (lngJieslx == Locale.liangpjs_feiylbb_id || lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {
            //region Description��Ʊ���㡢�����˷�
//
                /*
                *huochaoyuan
	    		*2009-10-22���������ͬ�����˷�ʱ�����㵥���Զ�ȡ�������ͬ������Ŀ�������˰�ŵ���Ϣ
	    		*/
                if (lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {
                    JDBCcon con1 = new JDBCcon();
                    String sqll = "select h.gongfdwmc,h.gongfzh,h.gongfkhyh from hetys h where \n"
                            + "h.id=" + Hetb_id;

                    ResultSetList rsll = con1.getResultSetList(sqll);
                    if (rsll.next()) {

                        bsv.setShoukdw(rsll.getString("gongfdwmc"));
                        bsv.setKaihyh(rsll.getString("gongfkhyh"));
                        bsv.setZhangH(rsll.getString("gongfzh"));
                    }
                    con1.Close();
                }
//		    		end
                sql_colum = ",decode(kuangqyf,null,0,kuangqyf) as kuangqyf,	\n"
                        + "decode(kuangqzf,null,0,kuangqzf) as kuangqzf,	\n"
                        + "decode(kuangqyfs,null,0,kuangqyfs) as kuangqyfsk,	\n"
                        + "decode(kuangqyfj,null,0,kuangqzf) as kuangqyfjk	\n";

                sql_talbe = " , (select max(0) as id,sum(zhi) as kuangqyf,\n";
//		        	˰�ʵ���
                String CONDITON = "";
                if (yunjslx.equals("��")) {
                    CONDITON = "sum(zhi)-round_new(sum(zhi)/(1+shuil),2) as kuangqyfs, \n"
                            + " round_new(sum(zhi)/(1+shuil),2) as kuangqyfj,";
                } else {
                    CONDITON = "round_new(sum(zhi)*shuil,2) as kuangqyfs, \n"
                            + " sum(zhi)-round_new(sum(zhi)*shuil,2) as kuangqyfj,";
                }
                //
                sql_talbe += CONDITON + " shuil from 	\n"
                        + " (select distinct feiyb.zhi,yunfdjb_id,feiyxmb.shuil  																	\n"
                        + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                        + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                        + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"
                        + " and feiylbb.id=" + Locale.kuangqyf_feiylbb_id + " and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"
                        + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + "))) group by shuil) c		\n"
                        + " ,																							\n"
                        + "(select max(0) as id,sum(zhi) as kuangqzf from 	\n"
                        + " (select distinct feiyb.*,yunfdjb.id  																	\n"
                        + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                        + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                        + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id														\n"
                        + " and feiylbb.id=" + Locale.kuangqyf_feiylbb_id + " and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in	\n"
                        + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + ")))) d		\n";

                sql_where = " and a.id=c.id(+) and a.id=d.id(+) ";

                lngJieslx = Locale.guotyf_feiylbb_id;
                strJieslx = Locale.guotyf_feiylbb_id + "," + Locale.haiyyf_feiylbb_id;
                //endregion
            }


            sql = " select decode(tielyf,null,0,tielyf) as tielyf,												\n"
                    + " decode(tielzf,null,0,tielzf) as tielzf,														\n"
                    + " decode(tielyfs,null,0,tielyfs) as tielyfsk,"
                    + " decode(tielyfj,null,0,tielyfj) as tielyfjk "
                    + sql_colum
                    + " from	"
                    + "(select max(0) as id,sum(zhi) as tielyf,\n";

//		        	˰�ʵ���
            String CONDITON = "";
            if (yunjslx.equals("��")) {
                CONDITON = "sum(zhi)-round_new(sum(zhi)/(1+shuil),2) as tielyfs, \n"
                        + " round_new(sum(zhi)/(1+shuil),2) as tielyfj, \n";
            } else {
                CONDITON = "round_new(sum(zhi)*shuil,2) as tielyfs,  \n"
                        + " sum(zhi)-round_new(sum(zhi)*shuil,2) as tielyfj, \n";
            }
//
            sql += CONDITON + " shuil from	\n"
                    + "(select distinct feiyb.zhi,yunfdjb_id,feiyxmb.shuil																		\n"
                    + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                    + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                    + " and yunfdjb.feiyb_id=feiyb.feiyb_id	and feiyb.feiyxmb_id=feiyxmb.id 														\n"
                    + " and feiylbb.id in (" + strJieslx + ") and feiyb.shuib=1 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"
                    + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + "))) group by shuil) a,		\n"
                    + "(select max(0) as id,sum(zhi) as tielzf from			\n"
                    + " (select distinct feiyb.*,yunfdjb.id  																\n"
                    + " from yunfdjb,danjcpb,feiylbb,feiyb,feiyxmb															\n"
                    + " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
                    + " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
                    + " and feiylbb.id in (" + strJieslx + ") and feiyb.shuib=0 and feiyxmb.juflx=0 and danjcpb.chepb_id in							\n"
                    + " (select c.id from chepb c,fahb f where f.id=c.fahb_id " + guohxt + " and f.lie_id in (" + SelIds + ")))) b		\n"
                    + sql_talbe
                    + " where a.id=b.id(+) "
                    + sql_where;

            rs = con.getResultSet(sql);

            if (rs.next()) {
//				        ��Ʊ���㣨��·����yunfdjb,danjcpb��ȡֵ��ǰ����Ҫ�Ƚ��л�Ʊ�˶ԣ�
//				    	yunf��·��Ʊ�����еķ���
//				    	yunfzf��·��Ʊ�����в��ɵ�˰�ķ���
//				    	��Ʊ���������˷�ʱ��
                bsv.setTielyf(rs.getDouble("tielyf"));
                bsv.setTielzf(rs.getDouble("tielzf"));
                bsv.setTielyfsk(rs.getDouble("tielyfsk"));
                bsv.setTielyfjk(rs.getDouble("tielyfjk"));

                if (lngJieslx == Locale.liangpjs_feiylbb_id
                        || lngJieslx == Locale.guotyf_feiylbb_id || lngJieslx == Locale.haiyyf_feiylbb_id) {

//				    		��Ʊ���㡢�����˷�
                    bsv.setKuangqyf(rs.getDouble("kuangqyf"));
                    bsv.setKuangqzf(rs.getDouble("kuangqzf"));
                    bsv.setKuangqjk(rs.getDouble("kuangqyfjk"));
                    bsv.setKuangqsk(rs.getDouble("kuangqyfsk"));
                }
            } else {

                bsv.setTielyf(0);
                bsv.setTielzf(0);
                bsv.setTielyfsk(0);
                bsv.setTielyfjk(0);

                bsv.setKuangqyf(0);
                bsv.setKuangqzf(0);
                bsv.setKuangqjk(0);
                bsv.setKuangqsk(0);
            }

            if (bsv.getTielyf() == 0 && bsv.getTielzf() == 0 && bsv.getKuangqyf() == 0 && bsv.getKuangqzf() == 0) {
//				    	�����yunfdjb,danjcpb��ȡֵ������˵���Ǵ�ú����л����˷Ѻ�ͬ��ȡ��

//				    	���1���������Ʊ���㡢�ȴ�ú���ͬ����ȡ�˷ѡ�
//				    			���û��ȡ���˷ѣ��ٴ�ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid,
//				    			�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�

//				    	���2������ǵ������˷ѣ���ôHetb_id�����˷Ѻ�ͬ��id,
//				    			�ٸ����˷Ѻ�ͬ�е�ú��ȡ������۸�

//				    	bsv.setYunfjsl(bsv.getGongfsl());
                //region Description
                if (Jieslx == Locale.liangpjs_feiylbb_id) {
//				    		��Ʊ���㣬���1

                    if (bsv.getHetyj() > 0 && !bsv.getHetyjdw().equals("")) {
//			    				���ú���ͬ�����˷������

                        CountYf(0, Shangcjsl);
                    } else {
//			    				���ú���ͬ��û���˷ѣ���ú���˷Ѻ�ͬ��������ȡ���˷Ѻ�ͬid
                        lngYunshtb_id = getYunshtb_id(Hetb_id);

                        if (lngYunshtb_id > 0) {
//			    					˵�������ҵ���Ӧ���˷Ѻ�ͬ��
                            CountYf(lngYunshtb_id, Shangcjsl);
                        } else if (bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//			    					�����۽���,�������Ʊ�����˷ѣ������ǵ����ۺ�ͬ�Ļ����������˷�

                        } else {
//			    					����۽���
                            bsv.setErroInfo("ú���ͬ<" + Jiesdcz.getHetbh(bsv.getHetb_Id()) + ">û�ж�Ӧ���˷Ѻ�ͬ�������ã�");
                            return false;
                        }
                    }
                } else if (Jieslx != Locale.meikjs_feiylbb_id) {
//				    		�����˷ѽ���(���������˷�)
                    CountYf(bsv.getHetb_Id(), Shangcjsl);
                }
                //endregion
            } else {
//				    	��yunfdjb,danjcpb��ȡ�����ݣ�˵����ͨ���˶Ի�Ʊ���ɵ��˷ѣ��˷ѽ�����Ϊbiaoz
//				    	����ǵ�������˷ѣ�������ԴҲ��yunfdjb,danjcpb,��ô��Ϊ��������ҲΪbiaoz
                bsv.setYunfjsl(bsv.getGongfsl());
                if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.haiyyf_feiylbb_id) {

                    bsv.setJiessl(bsv.getGongfsl());
                }
            }

////		    		һ�ڼ��˷�Դ��ú��
//		    		if(bsv.getYikj_yunfyymk().equals("��")){
//
//		    			sql=" select decode(tielyf,null,0,tielyf) as tielyf,												\n"
//				        	+ " decode(tielzf,null,0,tielzf) as tielzf														\n"
//				        	+ " from																						\n"
//				        	+ "(select sum(zhi) as tielyf																	\n"
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n"
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=1 and danjcpb.chepb_id in		\n"
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))		\n"
//				        	+ " ,																							\n"
//				        	+ " (select sum(zhi) as tielzf  																\n"
//				        	+ " from yunfdjb,danjcpb,feiylbb,feiyb															\n"
//				        	+ " where danjcpb.yunfdjb_id=yunfdjb.id and yunfdjb.feiylbb_id=feiylbb.id						\n"
//				        	+ " and yunfdjb.feiyb_id=feiyb.feiyb_id															\n"
//				        	+ " and feiylbb.mingc='"+Locale.guotyf_feiylbb+"' and feiyb.shuib=0 and danjcpb.chepb_id in		\n"
//				        	+ " (select c.id from chepb c,fahb f where f.id=c.fahb_id and f.lie_id in ("+SelIds+")))";
//		    		}else{
//
//		    			if(bsv.getHetjgb_id()>0){
////		    				�Ѿ��к�ͬ�۸��
//
//		    				sql="select hj.yunj as qiyj	from hetjgb hj where hj.id="+bsv.getHetjgb_id();
//		    			}else{
//
//		    				sql="select hj.yunj as qiyj	from hetjgb hj,hetb ht where hj.hetb_id=ht.id and ht.id="+Hetb_id;
//		    			}
//		    		}
//
//		    		rs=con.getResultSet(sql);
//
//		    		if(rs.next()){
//
//		    			bsv.setTielyf((double)CustomMaths.Round_new(rs.getDouble("qiyj")*bsv.getYunfjsl(),2));
//		    		}
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            con.Close();
        }
        return true;
    }

    private void setJieszb(Interpreter bsh, int Type, double Shangcjsl) {
//		�˷�����Jiesdcz.java�л���һ�����Ƶķ���������޸Ĵ˷�����Ҫ��ͬ�Ǹ�����һ���޸�
        try {

//			Type	0:ú�����
//					1:�˷ѽ���
            if (Type == 0 || (Type == 1 && bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id)) {
//				�����ú��������Ʊ�������ú��ʱ���Խ��и�ֵ������ֻ���ڵ��������˷�ʱ�ſɸ�ֵ

                //			�������ۿ�ȡֵ
                bsv.setShul_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_��������").toString()));
                bsv.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
                bsv.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
                bsv.setShul_zsl(Double.parseDouble(bsh.get("������_��������").toString()));
                bsv.setShul_zsldw(bsh.get("��������λ_��������").toString());

                //			Qnetar
                bsv.setQnetar_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Qnetar").toString()));
                bsv.setQnetar_yk(Double.parseDouble(bsh.get("ӯ��_Qnetar").toString()));
                bsv.setQnetar_zdj(Double.parseDouble(bsh.get("�۵���_Qnetar").toString()));
                bsv.setQnetar_zsl(Double.parseDouble(bsh.get("������_Qnetar").toString()));
                bsv.setQnetar_zsldw(bsh.get("��������λ_Qnetar").toString());

                //			Std
                bsv.setStd_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Std").toString()));
                bsv.setStd_yk(Double.parseDouble(bsh.get("ӯ��_Std").toString()));
                bsv.setStd_zdj(Double.parseDouble(bsh.get("�۵���_Std").toString()));
                bsv.setStd_zsl(Double.parseDouble(bsh.get("������_Std").toString()));
                bsv.setStd_zsldw(bsh.get("��������λ_Std").toString());

                //			Ad
                bsv.setAd_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Ad").toString()));
                bsv.setAd_yk(Double.parseDouble(bsh.get("ӯ��_Ad").toString()));
                bsv.setAd_zdj(Double.parseDouble(bsh.get("�۵���_Ad").toString()));
                bsv.setAd_zsl(Double.parseDouble(bsh.get("������_Ad").toString()));
                bsv.setAd_zsldw(bsh.get("��������λ_Ad").toString());

                //			Vdaf
                bsv.setVdaf_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Vdaf").toString()));
                bsv.setVdaf_yk(Double.parseDouble(bsh.get("ӯ��_Vdaf").toString()));
                bsv.setVdaf_zdj(Double.parseDouble(bsh.get("�۵���_Vdaf").toString()));
                bsv.setVdaf_zsl(Double.parseDouble(bsh.get("������_Vdaf").toString()));
                bsv.setVdaf_zsldw(bsh.get("��������λ_Vdaf").toString());

                //			Mt
                bsv.setMt_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Mt").toString()));
                bsv.setMt_yk(Double.parseDouble(bsh.get("ӯ��_Mt").toString()));
                bsv.setMt_zdj(Double.parseDouble(bsh.get("�۵���_Mt").toString()));
                bsv.setMt_zsl(Double.parseDouble(bsh.get("������_Mt").toString()));
                bsv.setMt_zsldw(bsh.get("��������λ_Mt").toString());

                //			Qgrad
                bsv.setQgrad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Qgrad").toString()));
                bsv.setQgrad_yk(Double.parseDouble(bsh.get("ӯ��_Qgrad").toString()));
                bsv.setQgrad_zdj(Double.parseDouble(bsh.get("�۵���_Qgrad").toString()));
                bsv.setQgrad_zsl(Double.parseDouble(bsh.get("������_Qgrad").toString()));
                bsv.setQgrad_zsldw(bsh.get("��������λ_Qgrad").toString());

                //			Qbad
                bsv.setQbad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Qbad").toString()));
                bsv.setQbad_yk(Double.parseDouble(bsh.get("ӯ��_Qbad").toString()));
                bsv.setQbad_zdj(Double.parseDouble(bsh.get("�۵���_Qbad").toString()));
                bsv.setQbad_zsl(Double.parseDouble(bsh.get("������_Qbad").toString()));
                bsv.setQbad_zsldw(bsh.get("��������λ_Qbad").toString());

                //			Had
                bsv.setHad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Had").toString()));
                bsv.setHad_yk(Double.parseDouble(bsh.get("ӯ��_Had").toString()));
                bsv.setHad_zdj(Double.parseDouble(bsh.get("�۵���_Had").toString()));
                bsv.setHad_zsl(Double.parseDouble(bsh.get("������_Had").toString()));
                bsv.setHad_zsldw(bsh.get("��������λ_Had").toString());

                //			Stad
                bsv.setStad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Stad").toString()));
                bsv.setStad_yk(Double.parseDouble(bsh.get("ӯ��_Stad").toString()));
                bsv.setStad_zdj(Double.parseDouble(bsh.get("�۵���_Stad").toString()));
                bsv.setStad_zsl(Double.parseDouble(bsh.get("������_Stad").toString()));
                bsv.setStad_zsldw(bsh.get("��������λ_Stad").toString());

                //			Star
                bsv.setStar_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Star").toString()));
                bsv.setStar_yk(Double.parseDouble(bsh.get("ӯ��_Star").toString()));
                bsv.setStar_zdj(Double.parseDouble(bsh.get("�۵���_Star").toString()));
                bsv.setStar_zsl(Double.parseDouble(bsh.get("������_Star").toString()));
                bsv.setStar_zsldw(bsh.get("��������λ_Star").toString());

                //			Mad
                bsv.setMad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Mad").toString()));
                bsv.setMad_yk(Double.parseDouble(bsh.get("ӯ��_Mad").toString()));
                bsv.setMad_zdj(Double.parseDouble(bsh.get("�۵���_Mad").toString()));
                bsv.setMad_zsl(Double.parseDouble(bsh.get("������_Mad").toString()));
                bsv.setMad_zsldw(bsh.get("��������λ_Mad").toString());

                //			Aar
                bsv.setAar_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Aar").toString()));
                bsv.setAar_yk(Double.parseDouble(bsh.get("ӯ��_Aar").toString()));
                bsv.setAar_zdj(Double.parseDouble(bsh.get("�۵���_Aar").toString()));
                bsv.setAar_zsl(Double.parseDouble(bsh.get("������_Aar").toString()));
                bsv.setAar_zsldw(bsh.get("��������λ_Aar").toString());

                //			Aad
                bsv.setAad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Aad").toString()));
                bsv.setAad_yk(Double.parseDouble(bsh.get("ӯ��_Aad").toString()));
                bsv.setAad_zdj(Double.parseDouble(bsh.get("�۵���_Aad").toString()));
                bsv.setAad_zsl(Double.parseDouble(bsh.get("������_Aad").toString()));
                bsv.setAad_zsldw(bsh.get("��������λ_Aad").toString());

                //			Vad
                bsv.setVad_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_Vad").toString()));
                bsv.setVad_yk(Double.parseDouble(bsh.get("ӯ��_Vad").toString()));
                bsv.setVad_zdj(Double.parseDouble(bsh.get("�۵���_Vad").toString()));
                bsv.setVad_zsl(Double.parseDouble(bsh.get("������_Vad").toString()));
                bsv.setVad_zsldw(bsh.get("��������λ_Vad").toString());

                //			St
                bsv.setT2_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_T2").toString()));
                bsv.setT2_yk(Double.parseDouble(bsh.get("ӯ��_T2").toString()));
                bsv.setT2_zdj(Double.parseDouble(bsh.get("�۵���_T2").toString()));
                bsv.setT2_zsl(Double.parseDouble(bsh.get("������_T2").toString()));
                bsv.setT2_zsldw(bsh.get("��������λ_T2").toString());

                //			�˾�
                bsv.setYunju_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_�˾�").toString()));
                bsv.setYunju_yk(Double.parseDouble(bsh.get("ӯ��_�˾�").toString()));
                bsv.setYunju_zdj(Double.parseDouble(bsh.get("�۵���_�˾�").toString()));
                bsv.setYunju_zsl(Double.parseDouble(bsh.get("������_�˾�").toString()));
                bsv.setYunju_zsldw(bsh.get("��������λ_�˾�").toString());
            }
            //���㵥��
            if (Type == 0) {

//				����������%�֡������
                Jiesdcz.getReCountJiessl(bsv, 0, Type);

                bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));

            } else if (Type == 1) {

                if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                        && bsv.getJieslx() != Locale.meikjs_feiylbb_id) {

//					����������%�֡������
                    Jiesdcz.getReCountJiessl(bsv, 0, Type);
                    bsv.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
                    bsv.setJiessl(bsv.getYunfjsl());
                }

                bsv.setYunfjsdj(Double.parseDouble(bsh.get("����۸�").toString()));
                bsv.setYunfjsdj_mk(bsv.getYunfjsdj());    //���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���
//				����ǷǺ˶Ի�Ʊ��ʽ�����˷ѽ��㣬��Ϊ�˷ѵ��۸�ֵ��ֱ�Ӽ�����˷Ѻϼ�
                bsv.setTielyf((double) CustomMaths.Round_new(bsv.getYunfjsdj() * bsv.getYunfjsl(), 2));
            }
            if ((!bsv.getShangcjslct_Flag()) && bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)) {

                bsv.setJiessl(CustomMaths.sub(bsv.getJiessl(), Shangcjsl)); //���������ۼۺ��ϴν�����ɾ������Ϊ���ν�����
                bsv.setShangcjslct_Flag(true);
            }

        } catch (EvalError e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void computMlj(Interpreter bsh, ResultSetList rsl, Jiesdcz Jscz, long Diancxxb_id, long Gongysb_id, long Hetb_id) {

//		Ϊ����Ŀ¼�۸�ֵ
        try {
            double Dbljijzb = 0;

            bsh.set("��ֵ����_" + Locale.Qnetar_zhibb, 0);
            bsh.set(Locale.Qnetar_zhibb + "_����", 0);
            bsh.set(Locale.Qnetar_zhibb + "_����", 0);
            bsh.set("�ӷ��ݱȼ�_" + Locale.Vdaf_zhibb, 0);
            bsh.set(Locale.Vdaf_zhibb + "_����", 0);
            bsh.set(Locale.Vdaf_zhibb + "_����", 0);
            bsh.set("��ֱȼ�_" + Locale.Std_zhibb, 0);
            bsh.set(Locale.Std_zhibb + "_����", 0);
            bsh.set(Locale.Std_zhibb + "_����", 0);
            bsh.set("��ֱȼ�_" + Locale.Stad_zhibb, 0);
            bsh.set(Locale.Stad_zhibb + "_����", 0);
            bsh.set(Locale.Stad_zhibb + "_����", 0);
            bsh.set("�ҷֱȼ�_" + Locale.Aar_zhibb, 0);
            bsh.set(Locale.Aar_zhibb + "_����", 0);
            bsh.set(Locale.Aar_zhibb + "_����", 0);
            bsh.set("�ҷֱȼ�_" + Locale.Aad_zhibb, 0);
            bsh.set(Locale.Aad_zhibb + "_����", 0);
            bsh.set(Locale.Aad_zhibb + "_����", 0);

            do {

                Dbljijzb = Jiesdcz.getZhib_info(bsv, rsl.getString("zhib"), "js");
                Dbljijzb = Jiesdcz.getUnit_transform(rsl.getString("zhib"), rsl.getString("danw"), Dbljijzb, bsv.getMj_to_kcal_xsclfs());

                if (Dbljijzb >= rsl.getDouble("xiax") && Dbljijzb <= (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))) {

                    if (Jiesdcz.CheckMljRz(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_����", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx"))));

                        bsh.set(rsl.getString("zhib") + "_����", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));

                        bsh.set("��ֵ����_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljHff(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_����", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_����", rsl.getDouble("xiax"));

                        bsh.set("�ӷ��ݱȼ�_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljLiuf(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_����", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_����", rsl.getDouble("xiax"));

                        bsh.set("��ֱȼ�_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }
                    if (Jiesdcz.CheckMljHiuf(rsl.getString("zhib"))) {

                        bsh.set(rsl.getString("zhib") + "_����", (rsl.getDouble("shangx") == 0 ? 1e308 : rsl.getDouble("shangx")));

                        bsh.set(rsl.getString("zhib") + "_����", rsl.getDouble("xiax"));

                        bsh.set("�ҷֱȼ�_" + rsl.getString("zhib"), rsl.getDouble("jij"));
                    }

                    bsv.setHetyj(rsl.getDouble("yunj"));            //��ͬ�˼۵���
                    bsv.setHetyjdw(rsl.getString("yunjdw"));        //��ͬ�˼۵�λ

                    bsv.setHetjgpp_Flag(true);
                }

            } while (rsl.next());

            bsh.set("Ʒ�ֱȼ�", Jscz.getMljPzbj(bsv.getRanlpzb_Id()));

            //	�����ԼӼ�
            bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id, Locale.zhengcxjj_jies, "0")));

            //	�Ӽ�
            bsh.set(Locale.jiaj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id, Locale.jiaj_jies, "0")));

        } catch (EvalError e) {

            e.printStackTrace();
        }
    }

    //������ü�Ȩ
    private void computData(String selIds, long hetb_id, double shangcjsl) {
        //����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
        //ú��
        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();    //ú���������
        double _Meiksl = bsv.getMeiksl();

        //�˷�
//		double _Tielyf=bsv.getTielyf();
//		double _Tielzf=bsv.getTielzf();
//		double _Yunfsl=bsv.getYunfsl();
//		double _Kuangqyf=bsv.getKuangqyf();
//		double _Kuangqzf=bsv.getKuangqzf();
//		double _Kuangqsk=bsv.getKuangqsk();
//		double _Kuangqjk=bsv.getKuangqjk();

        //ָ��ӯ��
        double _Shulzbyk = bsv.getShul_yk();        //ִ�к�ͬ�еĳ��ֽ�����(����ָ��ӯ��)

        double _Jiashj = 0;
        double _Jiakhj = 0;
        double _Jiaksk = 0;
        double _Jine = 0;
        double _Buhsmj = 0;
        double _Shulzjbz = 0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
        double _Hej = 0;

        //ָ���۽��
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzje = 0;
        double _Shulzbzje = 0;
        double _Yunjuzje = 0;
        double _Starzje = 0;

        double _Meikzkktzsj[] = null;
        boolean _Iszksjtz = false; //�ж��Ƿ��Ѿ����������ۿ�����ݵ�����

        Danjsmk_dcjcl(1, selIds, hetb_id, shangcjsl);

//		if(!bsv.getMeikzkksyfw().equals("")
//				&&bsv.getMeikzkksyfw()!=null){
////			˵����Ҫ�������ۿ����Ŀ,Ŀǰֻ���������������ܼӼ۵�ҵ��
////			1���������ֶ�Ӧ���۵���
////			2�����������۽��
//
////			�����߼���
////			�ܽ�	(hansmj-�������ֵ��ۼ�)*��������+�������ֵ��ۼۡ���������
//			_Meikzkktzsj=Jiesdcz.Zengkktz(bsv);
//		}
        //�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//					��������Ϊ��һ�����������һ�֣�
//						���һ��

//							��˰��ú��=�����ۡ�ú���������-�˷Ѻ�˰���ۡ��˷ѽ�������
//							��˰���˷�=�˷Ѻ�˰���ۡ��˷ѽ�������
//							���������ڽ������������ӡ�һ�ڼ�(�˷�Դ��ú��)�������ã�Ĭ��ֵ���񡱣����ֵΪ���ǡ�
//									���մ��������
//						�������

//							ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//							��˰�˷ѵ���=��ͬ�۸��еĺ�˰�˷ѵ���
//							ͬʱҪ����Hansmj
//							��������ϵͳĬ�ϣ��������ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//									�ҽ��������С�һ�ڼ�(�˷�Դ��ú��)��ֵΪ���񡱣����մ��������

//
        //			�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸����˷Ѻ�˰���۴���0�����˷Ѽ۸�λ�����ڡ���

//					if(bsv.getHetyjdw().equals(Locale.yuanmd_danw)){
//		//				����˷ѵ��۵�λ=��Ԫ/�֡�
//		//					����С���Ĵ����������ݽ�������"ú�˰���۱���С��λ"
////						ԭ��
//		//					1������ú���ǵ������˷ѻ����������⣬���ú���ǲ���˰�ģ��������������⣬���Ȳ��迼�ǣ���Ϊ�˷Ѻ�ú������ͬ�ֵ�������
////							2�����ڷֹ�˾�Ӽ����⣬��Ϊ�Ӽ۶�Ϊ��˰�ۡ�
////								���ú��Ϊ��˰�ۣ���˰����=��˰����+�ֹ�˾�Ӽ�
////								���ú��Ϊ����˰�ۣ���˰����=����˰���ۡ���1+˰�ʣ�+�ֹ�˾�Ӽ�
//
////						���ۿ�������ŵ����紦��
////						if(_Meikzkktzsj!=null){
////
//////							1���������ֶ�Ӧ���۵���
//////							2�����������۽��
//////							(hansmj-�������ֵ��ۼ�)*��������+���������۽��
////							_Jiashj=(_Hansmj-_Meikzkktzsj[0])*_Jiessl+_Meikzkktzsj[1];
////							if(bsv.getJijlx()==0){
//////								��˰����
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,bsv.getMeikhsdjblxsw());
////							}else if(bsv.getJijlx()==1){
//////								����˰
////								_Hansmj=(double)CustomMaths.Round_new(_Jiashj/_Jiessl,7);
////							}
////						}
//
//						_Hansmj=(double)CustomMaths.Round_new(_Hansmj-bsv.getHetyj(), bsv.getMeikhsdjblxsw());
//					}
        //			else if(bsv.getHetyjdw().equals(Locale.dun_danw)){
        ////				����˷ѵ��۵�λ=���֡�
        ////				����С���Ĵ����������ݽ�������"������������С��λ"
        //				_Jiessl=(double)CustomMaths.Round_new(_Jiessl-bsv.getHetyj(), Integer.parseInt(bsv.getJiesslblxs()));
        //				bsv.setJiessl(_Jiessl);
        //			}

//		����ָ�����⴦��ʵ���߼�

        double jieszjecj = 0;    //�����ܽ���ۣ�����ָ�����ۿ��۽�������ɺ�Ҫ��jieszje��Ҫ������Ǯ��
//		                      jieszjecj�Ǻ�˰�ģ���JijlxΪ1ʱ����jieszjecjת��Ϊ����˰�ģ�

        if (bsv.getTsclzbs() != null) {
//			��ʱ��Tsclzbs����������ָ����ۼ���Ϣ
//			������Ԫ�����У�ָ�����,ָ���۵���,�ۼ�����,�۽��
            String tmp[] = null;
            String zhibbm = "";
            double zhibzje = 0;
            double zhibzdj = 0;
            double zhibjsbz = 0;        //����ָ��Ľ����׼(�����ν���ָ���ֵ)
            double zhibjsbzdysl = 0;    //����ָ������׼��Ӧ������
            double zhibjsbzjqz = 0;    //����ָ������׼�ļ�Ȩֵ������ָ��Ľ����׼������ָ������׼��Ӧ��������

            for (int i = 0; i < bsv.getTsclzbs().length; i++) {

                tmp = bsv.getTsclzbs()[i].split(",");

                if (zhibbm.equals(tmp[0])) {
//						ͬһ��ָ��
//						���¼����۽��
                    zhibzje = CustomMaths.add(zhibzje, Double.parseDouble(tmp[tmp.length - 1]));
//						���¼����۵���
                    zhibzdj = CustomMaths.Round_new(zhibzje / bsv.getJiessl(), bsv.getMeikzkkblxsw());
//
//						�����ν���ָ���ֵ
                    if (tmp[1].equals("'C'")) {
//						˵���ǶԳ������ֵ����ۿ��
                        zhibjsbz = 0;
                    } else {

                        zhibjsbz = Double.parseDouble(tmp[1]);
                    }
//						�����׼��Ӧ������
                    zhibjsbzdysl = CustomMaths.add(zhibjsbzdysl, Double.parseDouble(tmp[3]));
//						�ۼӽ����׼�ļ�Ȩֵ
                    zhibjsbzjqz = CustomMaths.add(zhibjsbzjqz, CustomMaths.mul(zhibjsbz, Double.parseDouble(tmp[3])));
//						��ָ�긳ֵ
                    Jiesdcz.setJieszbzdj_Tszb(zhibbm, bsv, zhibzdj, CustomMaths.Round_new(CustomMaths.div(zhibjsbzjqz, zhibjsbzdysl), bsv.getMeikzkkblxsw()), zhibzje);

                } else {
//						����һ�β���ͬһ��ָ��
                    zhibbm = tmp[0];
//						����۽��
                    zhibzje = Double.parseDouble(tmp[tmp.length - 1]);
//						����۵���
                    zhibzdj = Double.parseDouble(tmp[2]);
//						�����ν���ָ���ֵ
                    if (tmp[1].equals("'C'")) {
//						˵���ǶԳ������ֵ����ۿ��
                        zhibjsbz = 0;
                    } else {
//						˵���Ƕԡ���Ȩƽ�����еġ������Ρ����߼��Ĵ���
                        zhibjsbz = Double.parseDouble(tmp[1]);
                    }

//						�����׼��Ӧ������
                    zhibjsbzdysl = Double.parseDouble(tmp[3]);
//						�����׼�ļ�Ȩֵ
                    zhibjsbzjqz = CustomMaths.mul(zhibjsbz, zhibjsbzdysl);
//						��ָ�긳ֵ
                    Jiesdcz.setJieszbzdj_Tszb(zhibbm, bsv, zhibzdj, zhibjsbz, zhibzje);
                }
//				���������ۿ���ۼ۽���¼����
                jieszjecj = zhibzje;
            }
        }

//			����ֹ�˾�Ӽۡ��Ͳ���˰���ۼ���
        if (bsv.getJijlx() == 0) {
//					��˰����

            if (jieszjecj == 0) {
//				û������ָ�굥������

                bsv.setJiajqdj(_Hansmj);                                        //����Ӽ�ǰ����
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//					��Ҫ���㺬˰����(�����ۺ��˷�)
                    _Jiashj = (double) CustomMaths.sub((double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2), bsv.getYunzfhj());        //��˰�ϼ�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
                }
                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
                    _Hansmj = _Hansmj + bsv.getFengsjj();                                //���Ϸֹ�˾�Ӽ�
                    bsv.setHansmj(_Hansmj);                                            //���º�˰����
                    _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //��˰�ϼ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                    _Jine = _Jiakhj;                                                    //���
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //����˰����

                } else {
                    _Hansmj = _Hansmj + bsv.getFengsjj();                                //���Ϸֹ�˾�Ӽ�
                    bsv.setHansmj(_Hansmj);                                            //���º�˰����
                    _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //��˰�ϼ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                    _Jine = _Jiakhj;                                                    //���
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //����˰����
                }

            } else {
//				������ָ�굥������

                _Jiashj = (double) CustomMaths.Round_new(_Hansmj * _Jiessl, 2);        //��˰�ϼ�
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }
                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
                    _Jiashj = (double) CustomMaths.Round_new(CustomMaths.add(_Jiashj, jieszjecj), 2);            //���㲻���ֹ�˾�Ӽ۵ļ�˰�ϼ�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //���Ƽ�Ǯǰ��˰����
                    bsv.setJiajqdj(_Hansmj);
                    _Jiashj = (double) CustomMaths.Round_new(_Jiashj
                            + (double) CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl), 2), 2); //j��������ָ�����ۿ���˰�ϼ�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //���ƺ�˰����
                    bsv.setHansmj(_Hansmj);                                            //���º�˰����
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                    _Jine = _Jiakhj;                                                    //���
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //����˰����
                } else {
                    _Jiashj = (double) CustomMaths.Round_new(CustomMaths.add(_Jiashj, jieszjecj), 2);            //���㲻���ֹ�˾�Ӽ۵ļ�˰�ϼ�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //���Ƽ�Ǯǰ��˰����
                    bsv.setJiajqdj(_Hansmj);
                    _Jiashj = (double) CustomMaths.Round_new(_Jiashj
                            + (double) CustomMaths.Round_new(CustomMaths.mul(bsv.getFengsjj(), _Jiessl), 2), 2);                                    //j��������ָ�����ۿ���˰�ϼ�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());    //���ƺ�˰����
                    bsv.setHansmj(_Hansmj);                                            //���º�˰����
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                    _Jine = _Jiakhj;                                                    //���
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //����˰����
                }

            }

        } else if (bsv.getJijlx() == 1) {
//						�������ͣ�0����˰��1������˰��
//						����˰
//					bsv.setJiajqdj(_Hansmj);

            if (jieszjecj == 0) {
//				û������ָ�굥������

                _Buhsmj = _Hansmj;
                _Jiakhj = (double) CustomMaths.Round_new(_Buhsmj * _Jiessl, 2);
//				����Ӽ�ǰ��˰����
                _Jiashj = (double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2);

                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }

                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							����Ӽ�ǰ��˰����_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                } else {
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							����Ӽ�ǰ��˰����_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                }

				/*
                *huochaoyuan
				*2009-10-22��Ա���ʵ�����ע��������䣬���㵥����ʾ��������ȷ
				*/                                                                //���
//								_Buhsmj=(double)CustomMaths.Round_new(_Jiakhj/_Jiessl,7);		//����˰����
//												end
            } else {
//				������ָ�굥������

                _Buhsmj = _Hansmj;
                _Jiakhj = (double) CustomMaths.Round_new(_Buhsmj * _Jiessl, 2);
//				_Jiakhj=CustomMaths.add(_Jiakhj,jieszjecj);										//�������⴦���ָ���۽��
                _Jiakhj = CustomMaths.add(_Jiakhj, CustomMaths.Round_new(jieszjecj / (1 + _Meiksl), 2));    //�������⴦���ָ���۽��
                _Jiashj = (double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2);
                if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                    _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
                }

                if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);
//							����Ӽ�ǰ��˰����_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                } else {
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setJiajqdj(_Hansmj);    //����Ӽ�ǰ��˰����_end

                    _Jiashj = (double) CustomMaths.Round_new((double) CustomMaths.Round_new(_Jiakhj * (1 + _Meiksl), 2)
                            + (double) CustomMaths.Round_new(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                    _Jiakhj = (double) CustomMaths.Round_new((_Jiashj) / (1 + _Meiksl), 2);
                    _Jine = _Jiakhj;
                    _Jiaksk = (double) CustomMaths.Round_new((_Jiashj - _Jiakhj), 2);
                    _Hansmj = (double) CustomMaths.Round_new(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                    bsv.setHansmj(_Hansmj);
                    _Buhsmj = (double) CustomMaths.Round_new(_Jiakhj / _Jiessl, 7);        //����˰����
                }

            }
        }

        _Shulzjbz = _Hansmj;
        //�ϼ�
        _Hej = (double) CustomMaths.Round_new((_Jiashj), 2);

        //ָ���۵���

        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shulzb = bsv.getShul_zdj();        //����ָ���۵���
        double _Yunju = bsv.getYunju_zdj();        //�˾��۵���
        double _Star = bsv.getStar_zdj();            //Star�۵���

        //����ӯ�����ۼ۽��

//		�����������ۼ�_begin
        if (bsv.getMeikzkksyfw().indexOf(Locale.jiessl_zhibb) > -1) {
//				˵���������ֵ����ۿ��а�����ָ��

            _Shulzbzje = bsv.getShul_zje_tscl();    //����������ӯ��
            bsv.setShulzb_zje(_Shulzbzje);
            bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Shulzbyk), bsv.getMeikzkkblxsw()));
        } else {

            _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shulzb * _Jiessl, 2), bsv.getShul_zje_tscl());    //����������ӯ��
            bsv.setShulzb_zje(_Shulzbzje);
            bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Jiessl), bsv.getMeikzkkblxsw()));
        }


//		�����������ۼ�_end

        _Qnetarzje = CustomMaths.add((double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2), bsv.getQnetar_zje_tscl());
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Stdzje = CustomMaths.add((double) CustomMaths.Round_new(_Std * _Jiessl, 2), bsv.getStd_zje_tscl());
        bsv.setStd_zje(_Stdzje);
        bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Adzje = CustomMaths.add((double) CustomMaths.Round_new(_Ad * _Jiessl, 2), bsv.getAd_zje_tscl());
        bsv.setAd_zje(_Adzje);
        bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Vdafzje = CustomMaths.add((double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2), bsv.getVdaf_zje_tscl());
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Mtzje = CustomMaths.add((double) CustomMaths.Round_new(_Mt * _Jiessl, 2), bsv.getMt_zje_tscl());
        bsv.setMt_zje(_Mtzje);
        bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qgradzje = CustomMaths.add((double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2), bsv.getQgrad_zje_tscl());
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qbadzje = CustomMaths.add((double) CustomMaths.Round_new(_Qbad * _Jiessl, 2), bsv.getQbad_zje_tscl());
        bsv.setQbad_zje(_Qbadzje);
        bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Hadzje = CustomMaths.add((double) CustomMaths.Round_new(_Had * _Jiessl, 2), bsv.getHad_zje_tscl());
        bsv.setHad_zje(_Hadzje);
        bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Stadzje = CustomMaths.add((double) CustomMaths.Round_new(_Stad * _Jiessl, 2), bsv.getStad_zje_tscl());
        bsv.setStad_zje(_Stadzje);
        bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Starzje = CustomMaths.add((double) CustomMaths.Round_new(_Star * _Jiessl, 2), bsv.getStar_zje_tscl());
        bsv.setStar_zje(_Starzje);
        bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Madzje = CustomMaths.add((double) CustomMaths.Round_new(_Mad * _Jiessl, 2), bsv.getMad_zje_tscl());
        bsv.setMad_zje(_Madzje);
        bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aarzje = CustomMaths.add((double) CustomMaths.Round_new(_Aar * _Jiessl, 2), bsv.getAar_zje_tscl());
        bsv.setAar_zje(_Aarzje);
        bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aadzje = CustomMaths.add((double) CustomMaths.Round_new(_Aad * _Jiessl, 2), bsv.getAad_zje_tscl());
        bsv.setAad_zje(_Aadzje);
        bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Vadzje = CustomMaths.add((double) CustomMaths.Round_new(_Vad * _Jiessl, 2), bsv.getVad_zje_tscl());
        bsv.setVad_zje(_Vadzje);
        bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _T2zje = CustomMaths.add((double) CustomMaths.Round_new(_T2 * _Jiessl, 2), bsv.getT2_zje_tscl());
        bsv.setT2_zje(_T2zje);
        bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Yunjuzje = CustomMaths.add((double) CustomMaths.Round_new(_Yunju * bsv.getJiessl(), 2), bsv.getYunju_zje_tscl());            //�˾��۽��
        bsv.setYunju_zje(_Yunjuzje);
        bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //����������ӯ��
        bsv.setShul_zje(_Shulzje);
        bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl), bsv.getMeikzkkblxsw()));

//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨

        //���㵥��ʾʱָ���۽����
        bsv.setJiashj(_Jiashj);
        bsv.setJiakhj(_Jiakhj);
        bsv.setJiaksk(_Jiaksk);
        bsv.setJine(_Jine);
        bsv.setBuhsmj(_Buhsmj);
        bsv.setShulzjbz(_Shulzjbz);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
//		bsv.setYunzfhj(_Yunzfhj);
//		bsv.setYunfsk(_Yunfsk);
//		bsv.setBuhsyf(_Buhsyf);
        bsv.setHej(_Hej);

        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer("begin	\n");

        bsv.setXuh(bsv.getXuh() + 1);
        if (bsv.getMeikjsb_id() == 0) {

            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
        }

        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", selIds));

        sql.append("end;");

        if (sql.length() > 13) {

            con.getInsert(sql.toString());
        }
        sql.setLength(0);

        con.Close();
    }

    //	������õ�����
    private void computData_Dpc(String selIds, long hetb_id, double shangcjsl, String lie_id) {
        //����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
        //ú��
        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer("begin 	\n");

        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();
        double _Meiksl = bsv.getMeiksl();

        //ָ���۵���
        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Star = bsv.getStar_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shulzb = bsv.getShul_zdj();
        double _Yunju = bsv.getYunju_zdj();
        //ָ��ӯ��
        double _Shulyk = bsv.getShul_yk();                                //ִ�к�ͬ�еĳ��ֽ�����

        double _Jiashj = 0;
        double _Jiakhj = 0;
        double _Jiaksk = 0;
        double _Jine = 0;
        double _Buhsmj = 0;
        double _Shulzjbz = 0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
        double _Hej = 0;

        //ָ���۽��
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Starzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzje = 0;
        double _Shulzbzje = 0;
        double _Yunjuzje = 0;

        Danjsmk_dcjcl(1, selIds, hetb_id, 0);

        //�۸������
//		2008-12-9zsj�ӣ�
//		�߼���	�����ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//		��������Ϊ��һ�����������һ�֣�
//			���һ��

//				��˰��ú��=�����ۡ�ú���������-�˷Ѻ�˰���ۡ��˷ѽ�������
//				��˰���˷�=�˷Ѻ�˰���ۡ��˷ѽ�������
//				���������ڽ������������ӡ�һ�ڼ�(�˷�Դ��ú��)�������ã�Ĭ��ֵ���񡱣����ֵΪ���ǡ�
//						���մ��������
//			�������

//				ú�˰����=�������ú�˰����-��ͬ�۸��еĺ�˰�˷ѵ���
//				��˰�˷ѵ���=��ͬ�۸��еĺ�˰�˷ѵ���
//				ͬʱҪ����Hansmj
//				��������ϵͳĬ�ϣ��������ͬ�۸�Ľ��㷽ʽ�ǡ������۸񡱣��Һ�ͬ�۸��л�������˰�˷ѵ��ۣ�
//						�ҽ��������С�һ�ڼ�(�˷�Դ��ú��)��ֵΪ���񡱣����մ��������

        if ("�����������".equals(MainGlobal.getXitxx_item("����", "�����������", "0", ""))) {

            _Hansmj -= bsv.getShul_zdj();
            //bsv.getShul_zdj();

            ResultSetList rsl1 = con.getResultSetList("SELECT * FROM hetb WHERE guoqrq>=DATE'2012-09-24' and id=" + bsv.getHetb_Id());
            if (rsl1.getRows() > 0) {
//				�ж��Ƿ�ۿ�ʱ��Ϊȡ�������������Ǿ���
//				���������Խ�������Ϊ׼
//				��2013-11-29���ݵ糧Ҫ�����Ϊ����ú�ֽ��������Ӽۿ���
                String sql1 = "select sum(maoz-piz) yangbrl, sum(decode(pinzb_id,2018237,jingz,jingz)) from fahb f where f.lie_id in(" + selIds + ")";
                rsl1 = con.getResultSetList(sql1);
                double sl_all = 0;
                double sl_cym = 0;
                if (rsl1.next()) {
                    sl_all = rsl1.getDouble(0);
                    sl_cym = rsl1.getDouble(1);
                }

                //����ú������
                rsl1 = con.getResultSetList(
                        "select kouj,zengfj,shangx,xiax,canzxx,pinzb_id from hetzkkb where zhibb_id=1 and hetb_id=" + bsv.getHetb_Id() + " order by xiax desc");
                while (rsl1.next()) {
                    if (bsv.getRanlpzb_Id() == rsl1.getLong("pinzb_id")) {
                        //ԭúֱ�ӼӼ�
                        _Hansmj += rsl1.getDouble("zengfj");
                    } else {
                        //С������ֱ�ӿۼۣ�����ú�֣�
                        if (sl_all < rsl1.getDouble("shangx") && rsl1.getDouble("xiax") == 0) {
                            _Hansmj -= rsl1.getDouble("kouj");
                            continue;
                        }
                        //��������ֱ�����ۣ���ԭú������ú�֣�
//						��2013-11-29���ݵ糧Ҫ�����Ϊ����ú�ֽ��������Ӽۿ��ˣ�ȡ��&& 2018237 != this.bsv.getRanlpzb_Id()
                        if ((sl_cym >= rsl1.getDouble("xiax") && sl_cym < rsl1.getDouble("shangx")) || (sl_cym >= rsl1.getDouble("xiax") && 0 == rsl1.getDouble("shangx") && 0 != rsl1.getDouble("xiax"))) {
                            if (bsv.getShul_xb_dongt_static_value() >= rsl1.getDouble("xiax") && (CustomMaths.Round_new(this.bsv.getQnetar_js() / 4.1816 * 1000, 0) >= rsl1.getDouble("canzxx"))) {
                                _Hansmj += rsl1.getDouble("zengfj");
                            }
                        }
                    }
                }
                if (_Hansmj < 0) {
                    _Hansmj = 0;
                }

                bsv.setHansmj(_Hansmj);
            }
        }


        if (bsv.getJijlx() == 0) {
//								��˰����

//							if(Meikzkktzsj!=null){
////								˵���в������ܼӼ۵����
////								1���������ֶ�Ӧ���۵���
////								2�����������۽��
////								(hansmj-�������ֵ��ۼ�)*��������+���������۽��
//
//							}

            bsv.setJiajqdj(_Hansmj);
            if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//					��Ҫ���㺬˰����(�����ۺ��˷�)
                _Jiashj = (double) CustomMaths.sub((double) getRound_xz(_Hansmj * _Jiessl, 2), bsv.getYunzfhj());        //��˰�ϼ�
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
            }
            if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
//					����Ӽ�ǰ����
                _Hansmj = _Hansmj + bsv.getFengsjj();                                //���Ϸֹ�˾�Ӽ�
                bsv.setHansmj(_Hansmj);                                            //���º�˰����
                _Jiashj = (double) getRound_xz(_Hansmj * _Jiessl, 2);        //��˰�ϼ�
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                _Jine = _Jiakhj;                                                    //���
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //����˰����
            } else {
//					����Ӽ�ǰ����
                _Hansmj = _Hansmj + bsv.getFengsjj();                                //���Ϸֹ�˾�Ӽ�
                bsv.setHansmj(_Hansmj);                                            //���º�˰����
                _Jiashj = (double) getRound_xz(_Hansmj * _Jiessl, 2);        //��˰�ϼ�
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);    //�ۿ�ϼ�
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);        //�ۿ�˰��
                _Jine = _Jiakhj;                                                    //���
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //����˰����
            }

        } else if (bsv.getJijlx() == 1) {
//								�������ͣ�0����˰��1������˰��
//								����˰
            _Buhsmj = _Hansmj;
            _Jiakhj = (double) getRound_xz(_Buhsmj * _Jiessl, 2);
//				����Ӽ�ǰ��˰����
            _Jiashj = (double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2);

            if (bsv.getYunzfhj() > 0 && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {

                _Jiashj = (double) CustomMaths.sub(_Jiashj, bsv.getYunzfhj());
            }

            if (bsv.getJiesfs().equals(Locale.pingcjg_ht_jsfs) || bsv.getJiesfs().equals(Locale.chebjg_ht_jsfs)) { // ƽ�ּ۸� �� ����۸�
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
//					����Ӽ�ǰ��˰����_end

                _Jiashj = (double) getRound_xz((double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2)
                        + (double) getRound_xz(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);
                _Jine = _Jiakhj;
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setHansmj(_Hansmj);
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //����˰����
            } else {
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setJiajqdj(_Hansmj);
//					����Ӽ�ǰ��˰����_end

                _Jiashj = (double) getRound_xz((double) getRound_xz(_Jiakhj * (1 + _Meiksl), 2)
                        + (double) getRound_xz(bsv.getFengsjj() * bsv.getJiessl(), 2), 2);    //����ֹ�˾�Ӽ�
                _Jiakhj = (double) getRound_xz((_Jiashj) / (1 + _Meiksl), 2);
                _Jine = _Jiakhj;
                _Jiaksk = (double) getRound_xz((_Jiashj - _Jiakhj), 2);
                _Hansmj = (double) getRound_xz(_Jiashj / _Jiessl, bsv.getMeikhsdjblxsw());
                bsv.setHansmj(_Hansmj);
                _Buhsmj = (double) getRound_xz(_Jiakhj / _Jiessl, 7);        //����˰����
            }

        }

        _Shulzjbz = _Hansmj;
        //�ϼ�
        _Hej = (double) getRound_xz((_Jiashj), 2);

        //����ӯ�����ۼ۽��
        _Qnetarzje = (double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2);
        _Stdzje = (double) CustomMaths.Round_new(_Std * _Jiessl, 2);
        _Adzje = (double) CustomMaths.Round_new(_Ad * _Jiessl, 2);
        _Vdafzje = (double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2);
        _Mtzje = (double) CustomMaths.Round_new(_Mt * _Jiessl, 2);
        _Qgradzje = (double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2);
        _Qbadzje = (double) CustomMaths.Round_new(_Qbad * _Jiessl, 2);
        _Hadzje = (double) CustomMaths.Round_new(_Had * _Jiessl, 2);
        _Stadzje = (double) CustomMaths.Round_new(_Stad * _Jiessl, 2);
        _Starzje = (double) CustomMaths.Round_new(_Star * _Jiessl, 2);
        _Madzje = (double) CustomMaths.Round_new(_Mad * _Jiessl, 2);
        _Aarzje = (double) CustomMaths.Round_new(_Aar * _Jiessl, 2);
        _Aadzje = (double) CustomMaths.Round_new(_Aad * _Jiessl, 2);
        _Vadzje = (double) CustomMaths.Round_new(_Vad * _Jiessl, 2);
        _T2zje = (double) CustomMaths.Round_new(_T2 * _Jiessl, 2);
        _Yunjuzje = (double) CustomMaths.Round_new(_Yunju * _Jiessl, 2);    //�˾��۽��
        _Shulzbzje = (double) CustomMaths.Round_new(_Shulzb * _Jiessl, 2);        //��¼������ͬ��׼�İ��ֽ������㷨
        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //����������ӯ��

        //���㵥��ʾʱָ���۽����
        bsv.setShulzjbz(_Shulzjbz);
        bsv.setShulzjje(_Shulzje);
        bsv.setJiashj(_Jiashj);
        bsv.setJiakhj(_Jiakhj);
        bsv.setJiaksk(_Jiaksk);
        bsv.setJine(_Jine);
        bsv.setHej(_Hej);

        bsv.setShulzb_zje(_Shulzbzje);
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setStd_zje(_Stdzje);
        bsv.setAd_zje(_Adzje);
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setMt_zje(_Mtzje);
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQbad_zje(_Qbadzje);
        bsv.setHad_zje(_Hadzje);
        bsv.setStad_zje(_Stadzje);
        bsv.setMad_zje(_Madzje);
        bsv.setAar_zje(_Aarzje);
        bsv.setAad_zje(_Aadzje);
        bsv.setVad_zje(_Vadzje);
        bsv.setT2_zje(_T2);
        bsv.setStar_zje(_Starzje);
        bsv.setYunju_zje(_Yunjuzje);

        bsv.setXuh(bsv.getXuh() + 1);

        if (bsv.getMeikjsb_id() == 0) {

            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
        }

        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", lie_id));

        sql.append("end;");

        if (sql.length() > 13) {

            con.getInsert(sql.toString());
        }
        sql.setLength(0);

        con.Close();
    }

    private void computData_Yf(String lie_id) {
        //�����˷ѽ������۵���

        double _Hansmj = bsv.getHansmj();
        double _Jiessl = bsv.getJiessl();    //ú���������
        //ָ���۽��
        double _Qnetarzje = 0;
        double _Stdzje = 0;
        double _Adzje = 0;
        double _Vdafzje = 0;
        double _Mtzje = 0;
        double _Qgradzje = 0;
        double _Qbadzje = 0;
        double _Hadzje = 0;
        double _Stadzje = 0;
        double _Madzje = 0;
        double _Aarzje = 0;
        double _Aadzje = 0;
        double _Vadzje = 0;
        double _T2zje = 0;
        double _Shulzbzje = 0;
        double _Shulzje = 0;
        double _Yunjuzje = 0;
        double _Starzje = 0;
        double _Shulzjbz = 0;
        //ָ���۵���
        double _Qnetar = bsv.getQnetar_zdj();
        double _Std = bsv.getStd_zdj();
        double _Ad = bsv.getAd_zdj();
        double _Vdaf = bsv.getVdaf_zdj();
        double _Mt = bsv.getMt_zdj();
        double _Qgrad = bsv.getQgrad_zdj();
        double _Qbad = bsv.getQbad_zdj();
        double _Had = bsv.getHad_zdj();
        double _Stad = bsv.getStad_zdj();
        double _Mad = bsv.getMad_zdj();
        double _Aar = bsv.getAar_zdj();
        double _Aad = bsv.getAad_zdj();
        double _Vad = bsv.getVad_zdj();
        double _T2 = bsv.getT2_zdj();
        double _Shul = bsv.getShul_zdj();
        double _Yunju = bsv.getYunju_zdj();        //�˾��۵���
        double _Star = bsv.getStar_zdj();            //Star�۵���

        //����ӯ�����ۼ۽��

        _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shul * _Jiessl, 2), bsv.getShul_zje_tscl());    //����������ӯ��
        bsv.setShulzb_zje(_Shulzbzje);
        bsv.setShulzb_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzbzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setQnetar_yk(Jiesdcz.reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht(), "-", 0),
                MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(), 0, bsv.getMj_to_kcal_xsclfs()), bsv.getQnetar_yk()));
        _Qnetarzje = CustomMaths.add((double) CustomMaths.Round_new(_Qnetar * _Jiessl, 2), bsv.getQnetar_zje_tscl());
        bsv.setQnetar_zje(_Qnetarzje);
        bsv.setQnetar_zdj(CustomMaths.Round_new(CustomMaths.div(_Qnetarzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setStd_yk(Jiesdcz.reCoundYk(bsv.getStd_ht(), bsv.getStd_js(), bsv.getStd_yk()));
        _Stdzje = CustomMaths.add((double) CustomMaths.Round_new(_Std * _Jiessl, 2), bsv.getStd_zje_tscl());
        bsv.setStd_zje(_Stdzje);
        bsv.setStd_zdj(CustomMaths.Round_new(CustomMaths.div(_Stdzje, _Jiessl), bsv.getMeikzkkblxsw()));

        bsv.setAd_yk(Jiesdcz.reCoundYk(bsv.getAd_ht(), bsv.getAd_js(), bsv.getAd_yk()));
        _Adzje = CustomMaths.add((double) CustomMaths.Round_new(_Ad * _Jiessl, 2), bsv.getAd_zje_tscl());
        bsv.setAd_zje(_Adzje);
        bsv.setAd_zdj(CustomMaths.Round_new(CustomMaths.div(_Adzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Vdafzje = CustomMaths.add((double) CustomMaths.Round_new(_Vdaf * _Jiessl, 2), bsv.getVdaf_zje_tscl());
        bsv.setVdaf_zje(_Vdafzje);
        bsv.setVdaf_zdj(CustomMaths.Round_new(CustomMaths.div(_Vdafzje, _Jiessl), bsv.getMeikzkkblxsw()));

        _Mtzje = CustomMaths.add((double) CustomMaths.Round_new(_Mt * _Jiessl, 2), bsv.getMt_zje_tscl());
        bsv.setMt_zje(_Mtzje);
        bsv.setMt_zdj(CustomMaths.Round_new(CustomMaths.div(_Mtzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qgradzje = CustomMaths.add((double) CustomMaths.Round_new(_Qgrad * _Jiessl, 2), bsv.getQgrad_zje_tscl());
        bsv.setQgrad_zje(_Qgradzje);
        bsv.setQgrad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qgradzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Qbadzje = CustomMaths.add((double) CustomMaths.Round_new(_Qbad * _Jiessl, 2), bsv.getQbad_zje_tscl());
        bsv.setQbad_zje(_Qbadzje);
        bsv.setQbad_zdj(CustomMaths.Round_new(CustomMaths.div(_Qbadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Hadzje = CustomMaths.add((double) CustomMaths.Round_new(_Had * _Jiessl, 2), bsv.getHad_zje_tscl());
        bsv.setHad_zje(_Hadzje);
        bsv.setHad_zdj(CustomMaths.Round_new(CustomMaths.div(_Hadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Stadzje = CustomMaths.add((double) CustomMaths.Round_new(_Stad * _Jiessl, 2), bsv.getStad_zje_tscl());
        bsv.setStad_zje(_Stadzje);
        bsv.setStad_zdj(CustomMaths.Round_new(CustomMaths.div(_Stadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Starzje = CustomMaths.add((double) CustomMaths.Round_new(_Star * _Jiessl, 2), bsv.getStar_zje_tscl());
        bsv.setStar_zje(_Starzje);
        bsv.setStar_zdj(CustomMaths.Round_new(CustomMaths.div(_Starzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Madzje = CustomMaths.add((double) CustomMaths.Round_new(_Mad * _Jiessl, 2), bsv.getMad_zje_tscl());
        bsv.setMad_zje(_Madzje);
        bsv.setMad_zdj(CustomMaths.Round_new(CustomMaths.div(_Madzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aarzje = CustomMaths.add((double) CustomMaths.Round_new(_Aar * _Jiessl, 2), bsv.getAar_zje_tscl());
        bsv.setAar_zje(_Aarzje);
        bsv.setAar_zdj(CustomMaths.Round_new(CustomMaths.div(_Aarzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Aadzje = CustomMaths.add((double) CustomMaths.Round_new(_Aad * _Jiessl, 2), bsv.getAad_zje_tscl());
        bsv.setAad_zje(_Aadzje);
        bsv.setAad_zdj(CustomMaths.Round_new(CustomMaths.div(_Aadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Vadzje = CustomMaths.add((double) CustomMaths.Round_new(_Vad * _Jiessl, 2), bsv.getVad_zje_tscl());
        bsv.setVad_zje(_Vadzje);
        bsv.setVad_zdj(CustomMaths.Round_new(CustomMaths.div(_Vadzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _T2zje = CustomMaths.add((double) CustomMaths.Round_new(_T2 * _Jiessl, 2), bsv.getT2_zje_tscl());
        bsv.setT2_zje(_T2zje);
        bsv.setT2_zdj(CustomMaths.Round_new(CustomMaths.div(_T2zje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Yunjuzje = CustomMaths.add((double) CustomMaths.Round_new(_Yunju * bsv.getJiessl(), 2), bsv.getYunju_zje_tscl());            //�˾��۽��
        bsv.setYunju_zje(_Yunjuzje);
        bsv.setYunju_zdj(CustomMaths.Round_new(CustomMaths.div(_Yunjuzje, _Jiessl), bsv.getMeikzkkblxsw()));


        _Shulzje = (double) CustomMaths.Round_new(_Shulzjbz * bsv.getYingksl(), 2);    //����������ӯ��
        bsv.setShul_zje(_Shulzje);
        bsv.setShul_zdj(CustomMaths.Round_new(CustomMaths.div(_Shulzje, _Jiessl), bsv.getMeikzkkblxsw()));

        //���㵥��ʾʱָ���۽����
        bsv.setShulzjbz(_Shulzjbz);

        if (!bsv.getYunfjsdpcfztj().equals("")) {
//			����˷Ѳ��ǵ����ν���ģ��򲻴�danpcjsmxb;
//			ֻ���˷���Ҫ�����μ���ʱ�Ž������ν����ֵ����danpcjsmxb

            JDBCcon con = new JDBCcon();
            StringBuffer sql = new StringBuffer("begin 	\n");

            bsv.setXuh_yf(bsv.getXuh_yf() + 1);

            if (bsv.getYunfjsb_id() == 0) {

                bsv.setYunfjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
            }

            computYunfAndHej();

            if (bsv.getYunju_ht().equals("")) {

//				���ǵ�һ��Ҫ���뵥���ν����¼������˾�ָ��Ϊ�վͶ�����и�ֵ
                bsv.setYunju_ht("0");
            }

//			����˷Ѳ��ǵ����ν���ģ�Ҳ��danpcjsmxbһ����¼
            sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "YF", lie_id));

            sql.append("end;");

            if (sql.length() > 13) {

                con.getInsert(sql.toString());
            }
            sql.setLength(0);
        }
    }

    private void CountZonghzkk() {

//		�������ܣ�
//				Ϊ�˴����ͬ�۸��еĽ��㷽ʽΪ����Ȩƽ���������ۿ��С�����������ָ��Ľ��㷽ʽΪ�������Ρ�ʱ��
//			����ʱ���߼�ӦΪ�ȼ��㵥���ε��߼��������reCount �����У��ټ��㡰����������ָ���ȫ���������������ۿ���Ϣ

//		�����߼���
//				�����ж���û������ҵ�����Σ������������
//				1�������߼�������getMeiPrice �����м�Ȩƽ�������߼�
//				2���������ۿ���Ϣ�õ������������۵�����Ϣ������danpcjsmxb�У�xuhΪ0

//		�����βΣ�
//			bsv Balances_variable�ͱ��� , jiessl Ҫ��Ϊ����, Type:0		ú����㣻1	�˷ѽ���

        if (bsv.getJiesxs().equals(Locale.danpc_jiesxs)) {

            JDBCcon con = new JDBCcon();
            StringBuffer sql = new StringBuffer();
            ResultSetList rsl = null;
            Interpreter bsh = new Interpreter();

            try {

                sql.append("select distinct zbb.bianm as zhib \n" +
                        "        from hetb htb, hetzkkb htzkk,zhibb zbb,hetjsxsb xs\n" +
                        "        where htb.id=htzkk.hetb_id\n" +
                        "              and htzkk.zhibb_id=zbb.id\n" +
                        "              and htzkk.hetjsxsb_id=xs.id(+)\n" +
                        "              and (xs.bianm='" + Locale.jiaqpj_jiesxs + "' or xs.bianm='" + Locale.jiaqfl_jiesxs + "') \n" +
                        "              and (zbb.leib=1 or zbb.leib=3)\n" +
                        "              and htb.id=" + bsv.getHetb_Id() + "");

                rsl = con.getResultSetList(sql.toString());
                if (rsl.getRows() > 0) {
                    //			˵���м�¼
                    //			����StringBuffer �����洢Ҫ���⴦���ָ��
                    StringBuffer sb = new StringBuffer("");
                    while (rsl.next()) {

                        sb.append(rsl.getString("zhib")).append(",");
                    }

                    if (sb.indexOf(Locale.jiessl_zhibb) > -1) {
                        //					˵���н�������
                        sql.setLength(0);

                        if (bsv.getMeikjsb_id() == 0) {

                            bsv.setMeikjsb_id(Long.parseLong(MainGlobal.getNewID(bsv.getDiancxxb_id())));
                        }

                        bsv.setXuh(0);

//						��ʼ����������ָ��
                        getJiesszl(bsv.getJieszbsftz(), bsv.getSelIds(), bsv.getDiancxxb_id(), bsv.getGongysb_Id(), bsv.getHetb_Id(),
                                bsv.getJieskdl(), bsv.getYunsdwb_id(), bsv.getJieslx(), bsv.getShangcjsl(), "");

                        sql.append(
                                "select sum(jiessl) as jiessl from\n" +
                                        "	(select xuh,\n" +
                                        "        max(mx.jiessl) as jiessl\n" +
                                        "        from danpcjsmxb mx\n" +
                                        "        where leib=1 and zhekfs = 0 and jiesdid=" + bsv.getMeikjsb_id() + "\n" +
                                        "        group by xuh)");

                        rsl = con.getResultSetList(sql.toString());
                        while (rsl.next()) {

                            bsv.setJiessl(rsl.getDouble("jiessl"));

                            bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), bsv.getShangcjsl()));
                        }

                        String[] tmp = new String[6];
                        tmp[0] = "0";
                        tmp[1] = Locale.jiessl_zhibb;
                        tmp[2] = "meikxxb_id";
                        tmp[3] = "-1";
                        tmp[4] = String.valueOf(bsv.getJiessl());
                        tmp[5] = "0";

                        bsv.setTsclzbs(new String[1]);
                        bsv.getTsclzbs()[0] = "0, " + Locale.jiessl_zhibb + ", meikxxb_id, 0, " + String.valueOf(bsv.getJiessl()) + ", 0";

//						0,�˾�,meikxxb_id,100,10,0;

                        bsh.set("������ʽ", bsv.getJiesxs());
                        bsh.set("�Ƽ۷�ʽ", bsv.getHetjjfs());
                        bsh.set("�۸�λ", bsv.getHetmdjdw());
                        bsh.set("��ͬ�۸�", bsv.getHetmdj());
                        bsh.set("���ú��", bsv.getZuigmj());
                        bsh.set("��ͬ�۸�ʽ", bsv.getHetmdjgs());

                        double Dbltmp = Jiesdcz.getZhib_info(bsv, Locale.jiessl_zhibb, "js");

                        Dbltmp = Jiesdcz.getUnit_transform(Locale.jiessl_zhibb, Locale.dun_danw, Dbltmp, bsv.getMj_to_kcal_xsclfs());

                        bsh.set(Locale.jiessl_zhibb + Locale.dun_danw, Dbltmp);    //����ֵ
                        bsh.set(rsl.getString("zhib") + "������λ", Locale.dun_danw);                                //ָ�굥λ

                        bsv.setYifzzb(Locale.jiessl_zhibb);    //Ĭ�ϵ��Ѹ�ֵָ��

                        bsh.set(rsl.getString("zhib") + "����", 0);        //ָ������
                        bsh.set(rsl.getString("zhib") + "����", 0);            //ָ������

                        bsh.set(rsl.getString("zhib") + "��������", 0);
                        bsh.set(rsl.getString("zhib") + "�������۹�ʽ", "");
                        bsh.set(rsl.getString("zhib") + "�۸�����", 0);
                        bsh.set(rsl.getString("zhib") + "�۸����۹�ʽ", "");
                        bsh.set(rsl.getString("zhib") + "�����۵�λ", "");
                        bsh.set(rsl.getString("zhib") + "���ۿ�����", "");
                        bsh.set(rsl.getString("zhib") + "���ۿ����", 0);
                        bsh.set(rsl.getString("zhib") + "���ۿ������λ", "");
                        bsh.set(rsl.getString("zhib") + "��׼���ۼ�", 0);
                        bsh.set(rsl.getString("zhib") + "С������", "");

//						������ۿ�
                        getZengkk(bsv.getHetb_Id(), bsh, false, tmp);

//						�û��Զ��幫ʽ
                        bsh.set(Locale.user_custom_fmlj_jiesgs, bsv.getUser_custom_fmlj_jiesgs());

//						ú�˰���۱���С��λ
                        bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//						��˰����ȡ����ʽ
                        bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

//						���ۿ��С��λ
                        bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//						ִ�к�ͬ�۸�ʽ
                        ExecuteHetmdjgs(bsh);

//						ִ�й�ʽ
                        bsh.eval(bsv.getGongs_Mk());

                        Jiesdcz.SetZhib_het_clear(bsv);    //�������ָ��ĺ�ͬ��׼ֵ

                        bsv.setShul_ht(Jiesdcz.Regular_Ht(bsh.get("��ͬ��׼_��������").toString()));
                        bsv.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
                        bsv.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
                        bsv.setShul_zsl(Double.parseDouble(bsh.get("������_��������").toString()));
                        bsv.setShul_zsldw(bsh.get("��������λ_��������").toString());


                        if (bsh.get(Locale.jiessl_zhibb + "������λ").equals(Locale.wandun_danw)) {
//							��Ϊ ����ȡֵ�����Ľ���������λΪ���֡�����������ۿ��е�������λΪ����֡� �������ʱҪ����ת��
//							Double.parseDouble(value.substring(value.indexOf("-")+1))
                            if (bsv.getShul_ht().indexOf("-") > -1) {
//								˵�������ӷ�

                                String shulht = "";

                                shulht = String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht().substring(0, bsv.getShul_ht().indexOf("-"))), 10000));

                                shulht = shulht + "-" + String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht().substring(bsv.getShul_ht().indexOf("-") + 1)), 10000));

                                bsv.setShul_ht(shulht);

                            } else {

                                bsv.setShul_ht(String.valueOf(CustomMaths.mul(Double.parseDouble(bsv.getShul_ht()), 10000)));
                            }

                            bsv.setShul_yk(CustomMaths.mul(bsv.getShul_yk(), 10000));
                        }


                        double _Shulzb = bsv.getShul_zdj();        //����ָ���۵���
                        double _Shulzbzje = CustomMaths.add((double) CustomMaths.Round_new(_Shulzb * bsv.getJiessl(), 2), bsv.getShul_zje_tscl());

                        bsv.setShulzb_zdj(_Shulzb);    //����ָ���۵���
                        bsv.setShulzb_zje(_Shulzbzje);    //����ָ���۽��


                        sql.setLength(0);

                        sql.append("begin	\n");
                        sql.append(Jiesdcz.InsertDanpcjsmkb(bsv, this.getZhibModel(), "MK", bsv.getSelIds()));
                        sql.append("end;");
                        if (sql.length() > 13) {

                            con.getUpdate(sql.toString());
                        }
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            if (rsl != null) {

                rsl.close();
            }
            con.Close();
        }
    }

    private void reCount() {
//		����danpcjsmxb �����ĵ������ܽ��㵥��ֵ��ע����Ȩ
        JDBCcon con = new JDBCcon();
        try {

//			�õ������ۼ۵Ľ���ָ����Ϣ
            String strSql =
                    "select zb.bianm,\n" +
                            "   max(mx.hetbz) as hetbz,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(gongf)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                            "                       end\n" +
                            "              end\n" +
                            "   end as gongf,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(changf)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                            "                       end\n" +
                            "             end\n" +
                            "   end as changf,\n" +
                            "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(jies)\n" +
                            "        else\n" +
                            "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                            "                  else\n" +
                            "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                            "                            else\n" +
                            "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl))," + bsv.getDanglblxsw() + ")\n" +
                            "                       end\n" +
                            "             end\n" +
                            "   end as jies,\n" +
                            "   sum(mx.yingk) as yingk,\n" +
                            "   round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.zhejbz))/sum(decode(jiessl,0,1,jiessl))," + bsv.getDanglblxsw() + ") as zhejbz,\n" +
                            "   sum(mx.zhejje) as zhejje\n" +
                            " from danpcjsmxb mx,zhibb zb\n" +
                            " where zb.id=mx.zhibb_id and mx.leib=1 and mx.zhekfs = 0 and jiesdid=" + bsv.getMeikjsb_id() + "\n" +
                            " group by zb.bianm";

            ResultSet rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getString("bianm").equals(Locale.Shul_zhibb)) {

                    bsv.setShul_ht(rs.getString("hetbz"));
                    bsv.setGongfsl(rs.getDouble("gongf"));
                    bsv.setYanssl(rs.getDouble("changf"));
                    bsv.setJiessl(rs.getDouble("jies"));
                    bsv.setShulzb_yk(rs.getDouble("yingk"));
                    bsv.setShulzb_zdj(rs.getDouble("zhejbz"));
                    bsv.setShulzb_zje(rs.getDouble("zhejje"));


                } else if (rs.getString("bianm").equals(Locale.Qnetar_zhibb)) {

                    bsv.setQnetar_ht(rs.getString("hetbz"));
                    bsv.setQnetar_kf(rs.getDouble("gongf"));
                    bsv.setQnetar_cf(rs.getDouble("changf"));
                    bsv.setQnetar_js(rs.getDouble("jies"));
                    bsv.setQnetar_yk(rs.getDouble("yingk"));
                    bsv.setQnetar_zdj(rs.getDouble("zhejbz"));
                    bsv.setQnetar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Std_zhibb)) {

                    bsv.setStd_ht(rs.getString("hetbz"));
                    bsv.setStd_kf(rs.getDouble("gongf"));
                    bsv.setStd_cf(rs.getDouble("changf"));
                    bsv.setStd_js(rs.getDouble("jies"));
                    bsv.setStd_yk(rs.getDouble("yingk"));
                    bsv.setStd_zdj(rs.getDouble("zhejbz"));
                    bsv.setStd_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Ad_zhibb)) {

                    bsv.setAd_ht(rs.getString("hetbz"));
                    bsv.setAd_kf(rs.getDouble("gongf"));
                    bsv.setAd_cf(rs.getDouble("changf"));
                    bsv.setAd_js(rs.getDouble("jies"));
                    bsv.setAd_yk(rs.getDouble("yingk"));
                    bsv.setAd_zdj(rs.getDouble("zhejbz"));
                    bsv.setAd_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Vdaf_zhibb)) {

                    bsv.setVdaf_ht(rs.getString("hetbz"));
                    bsv.setVdaf_kf(rs.getDouble("gongf"));
                    bsv.setVdaf_cf(rs.getDouble("changf"));
                    bsv.setVdaf_js(rs.getDouble("jies"));
                    bsv.setVdaf_yk(rs.getDouble("yingk"));
                    bsv.setVdaf_zdj(rs.getDouble("zhejbz"));
                    bsv.setVdaf_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Mt_zhibb)) {

                    bsv.setMt_ht(rs.getString("hetbz"));
                    bsv.setMt_kf(rs.getDouble("gongf"));
                    bsv.setMt_cf(rs.getDouble("changf"));
                    bsv.setMt_js(rs.getDouble("jies"));
                    bsv.setMt_yk(rs.getDouble("yingk"));
                    bsv.setMt_zdj(rs.getDouble("zhejbz"));
                    bsv.setMt_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Qgrad_zhibb)) {

                    bsv.setQgrad_ht(rs.getString("hetbz"));
                    bsv.setQgrad_kf(rs.getDouble("gongf"));
                    bsv.setQgrad_cf(rs.getDouble("changf"));
                    bsv.setQgrad_js(rs.getDouble("jies"));
                    bsv.setQgrad_yk(rs.getDouble("yingk"));
                    bsv.setQgrad_zdj(rs.getDouble("zhejbz"));
                    bsv.setQgrad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Qbad_zhibb)) {

                    bsv.setQbad_ht(rs.getString("hetbz"));
                    bsv.setQbad_kf(rs.getDouble("gongf"));
                    bsv.setQbad_cf(rs.getDouble("changf"));
                    bsv.setQbad_js(rs.getDouble("jies"));
                    bsv.setQbad_yk(rs.getDouble("yingk"));
                    bsv.setQbad_zdj(rs.getDouble("zhejbz"));
                    bsv.setQbad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Had_zhibb)) {

                    bsv.setHad_ht(rs.getString("hetbz"));
                    bsv.setHad_kf(rs.getDouble("gongf"));
                    bsv.setHad_cf(rs.getDouble("changf"));
                    bsv.setHad_js(rs.getDouble("jies"));
                    bsv.setHad_yk(rs.getDouble("yingk"));
                    bsv.setHad_zdj(rs.getDouble("zhejbz"));
                    bsv.setHad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Stad_zhibb)) {

                    bsv.setStad_ht(rs.getString("hetbz"));
                    bsv.setStad_kf(rs.getDouble("gongf"));
                    bsv.setStad_cf(rs.getDouble("changf"));
                    bsv.setStad_js(rs.getDouble("jies"));
                    bsv.setStad_yk(rs.getDouble("yingk"));
                    bsv.setStad_zdj(rs.getDouble("zhejbz"));
                    bsv.setStad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Star_zhibb)) {

                    bsv.setStar_ht(rs.getString("hetbz"));
                    bsv.setStar_kf(rs.getDouble("gongf"));
                    bsv.setStar_cf(rs.getDouble("changf"));
                    bsv.setStar_js(rs.getDouble("jies"));
                    bsv.setStar_yk(rs.getDouble("yingk"));
                    bsv.setStar_zdj(rs.getDouble("zhejbz"));
                    bsv.setStar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Mad_zhibb)) {

                    bsv.setMad_ht(rs.getString("hetbz"));
                    bsv.setMad_kf(rs.getDouble("gongf"));
                    bsv.setMad_cf(rs.getDouble("changf"));
                    bsv.setMad_js(rs.getDouble("jies"));
                    bsv.setMad_yk(rs.getDouble("yingk"));
                    bsv.setMad_zdj(rs.getDouble("zhejbz"));
                    bsv.setMad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Aar_zhibb)) {

                    bsv.setAar_ht(rs.getString("hetbz"));
                    bsv.setAar_kf(rs.getDouble("gongf"));
                    bsv.setAar_cf(rs.getDouble("changf"));
                    bsv.setAar_js(rs.getDouble("jies"));
                    bsv.setAar_yk(rs.getDouble("yingk"));
                    bsv.setAar_zdj(rs.getDouble("zhejbz"));
                    bsv.setAar_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Aad_zhibb)) {

                    bsv.setAad_ht(rs.getString("hetbz"));
                    bsv.setAad_kf(rs.getDouble("gongf"));
                    bsv.setAad_cf(rs.getDouble("changf"));
                    bsv.setAad_js(rs.getDouble("jies"));
                    bsv.setAad_yk(rs.getDouble("yingk"));
                    bsv.setAad_zdj(rs.getDouble("zhejbz"));
                    bsv.setAad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Vad_zhibb)) {

                    bsv.setVad_ht(rs.getString("hetbz"));
                    bsv.setVad_kf(rs.getDouble("gongf"));
                    bsv.setVad_cf(rs.getDouble("changf"));
                    bsv.setVad_js(rs.getDouble("jies"));
                    bsv.setVad_yk(rs.getDouble("yingk"));
                    bsv.setVad_zdj(rs.getDouble("zhejbz"));
                    bsv.setVad_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.T2_zhibb)) {

                    bsv.setT2_ht(rs.getString("hetbz"));
                    bsv.setT2_kf(rs.getDouble("gongf"));
                    bsv.setT2_cf(rs.getDouble("changf"));
                    bsv.setT2_js(rs.getDouble("jies"));
                    bsv.setT2_yk(rs.getDouble("yingk"));
                    bsv.setT2_zdj(rs.getDouble("zhejbz"));
                    bsv.setT2_zje(rs.getDouble("zhejje"));

                } else if (rs.getString("bianm").equals(Locale.Yunju_zhibb)) {

                    bsv.setYunju_ht(rs.getString("hetbz"));
                    bsv.setYunju_kf(rs.getDouble("gongf"));
                    bsv.setYunju_cf(rs.getDouble("changf"));
                    bsv.setYunju_js(rs.getDouble("jies"));
                    bsv.setYunju_yk(rs.getDouble("yingk"));
                    bsv.setYunju_zdj(rs.getDouble("zhejbz"));
                    bsv.setYunju_zje(rs.getDouble("zhejje"));
                }
            }

//			�����������۸񡢽����Ϣ
            strSql = "select sum(gongfsl) as gongfsl,sum(yanssl) as yanssl,sum(jiessl) as jiessl,sum(koud) as koud,	\n"
                    + " 	sum(kous) as kous,sum(kouz) as kouz,sum(ches) as ches,sum(jingz) as jingz,	\n"
                    + " 	sum(koud_js) as koud_js,sum(jiesslcy) as jiesslcy,sum(yuns) as yuns, sum(shulzbyk) as shulzbyk,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiajqdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiajqdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiesdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiesdj,	\n"
                    + " 	sum(jiakhj) as jiakhj,sum(jiaksk) as jiaksk,sum(jiashj) as jiashj,sum(chaokdl) as chaokdl,		\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*biaomdj))/sum(decode(jiessl,0,1,jiessl)),2) as biaomdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*buhsbmdj))/sum(decode(jiessl,0,1,jiessl)),2) as buhsbmdj	\n"
                    + " from 	\n"
                    + " 	(select xuh,	\n"
                    + " 		max(mx.gongfsl) as gongfsl,		\n"
                    + " 		max(mx.yanssl) as yanssl,		\n"
                    + " 		max(mx.jiessl) as jiessl,		\n"
                    + " 		max(mx.koud)  as koud,			\n"
                    + " 		max(mx.kous)  as kous,			\n"
                    + " 		max(mx.kouz)  as kouz,			\n"
                    + " 		max(mx.ches)  as ches,			\n"
                    + " 		max(mx.jingz)  as jingz,		\n"
                    + " 		max(mx.koud_js)  as koud_js,	\n"
                    + " 		max(mx.jiesslcy) as jiesslcy,	\n"
                    + " 		max(mx.yuns) as yuns,			\n"
                    + " 		max(mx.jiajqdj) as jiajqdj,		\n"
                    + " 		max(mx.jiesdj) as jiesdj,		\n"
                    + " 		max(mx.jiakhj) as jiakhj,		\n"
                    + " 		max(mx.jiaksk) as jiaksk,		\n"
                    + " 		max(mx.jiashj) as jiashj,		\n"
                    + "			max(mx.chaokdl) as chaokdl,		\n"
                    + "			max(mx.shulzbyk) as shulzbyk,	\n"
                    + " 		0 as biaomdj,		\n"
                    + " 		0 as buhsbmdj		\n"
                    + " 	from danpcjsmxb mx		\n"
                    + " 	where leib=1 and jiesdid=" + bsv.getMeikjsb_id() + "	\n"
                    + "			and xuh>0 and mx.zhekfs = 0	\n"
                    + " 	group by xuh)";
            rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getDouble("jiessl") > 0) {

//					����۸�
                    bsv.setGongfsl(rs.getDouble("gongfsl"));
                    bsv.setYanssl(rs.getDouble("yanssl"));
                    bsv.setJiessl(rs.getDouble("jiessl"));
                    bsv.setKoud(rs.getDouble("koud"));
                    bsv.setKous(rs.getDouble("kous"));
                    bsv.setKouz(rs.getDouble("kouz"));
                    bsv.setChes(rs.getLong("ches"));
                    bsv.setJingz(rs.getDouble("jingz"));
                    bsv.setKoud_js(rs.getDouble("koud_js"));
                    bsv.setJiesslcy(rs.getDouble("jiesslcy"));
                    bsv.setYuns(rs.getDouble("yuns"));
                    bsv.setJiajqdj(rs.getDouble("jiajqdj"));
                    bsv.setShulzjbz(rs.getDouble("jiesdj"));
                    bsv.setHansmj(rs.getDouble("jiesdj"));
                    if (bsv.getJiajqdj() >= bsv.getHansmj()) {

                        bsv.setJiajqdj(CustomMaths.sub(bsv.getHansmj(), bsv.getFengsjj()));
                    }
                    bsv.setJiakhj(rs.getDouble("jiakhj"));
                    bsv.setJine(rs.getDouble("jiakhj"));
                    bsv.setJiaksk(rs.getDouble("jiaksk"));
                    bsv.setJiashj(rs.getDouble("jiashj"));
                    bsv.setBuhsmj((double) CustomMaths.Round_new(bsv.getJiakhj() / bsv.getJiessl(), 7));
                    bsv.setChaokdl(rs.getDouble("chaokdl"));
                    bsv.setYingksl(rs.getDouble("shulzbyk"));
                }
            }

//			ú�����ʱ������۸�Ľ�����ʽΪ�������Ρ���ʽ�����С�����������ָ�������ۿ���Ϊ����Ȩƽ�������㣨��danpcjsmxb��xuh=0 ��Ϊȫ�����ۿ
//				��ʱ�ڼ��������ռ۸�����¼ۿ���Ƶ���

            strSql =
                    "select nvl(sum(mx.zhejje),0) as zhejje\n" +
                            "          from danpcjsmxb mx\n" +
                            "         where leib = 1\n" +
                            "           and jiesdid = " + bsv.getMeikjsb_id() + " \n" +
                            "           and xuh=0 and zhekfs = 0";

            rs = con.getResultSet(strSql);
            if (rs.next()) {

                if (rs.getDouble("zhejje") != 0) {

//					���۲�
                    double jiac = CustomMaths.sub(bsv.getHansmj(), bsv.getJiajqdj());

                    bsv.setJiashj(CustomMaths.Round_new(CustomMaths.add(bsv.getJiashj(), rs.getDouble("zhejje")), 2));
                    bsv.setShulzjbz(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), bsv.getMeikhsdjblxsw()));
                    bsv.setHansmj(bsv.getShulzjbz());
                    bsv.setJiajqdj(CustomMaths.Round_new(CustomMaths.sub(bsv.getHansmj(), jiac), bsv.getMeikhsdjblxsw()));
                    bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                    bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                    bsv.setJine(bsv.getJiakhj());
                    bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiakhj(), bsv.getJiessl()), 7));
                }
            }

//			����������Ͳ�������Ʊ�����ú����㡣���˷�Ϊ�����ν����������Ҫ�����˷ѵ��ۼ����
            if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                    && (!bsv.getYunfjsdpcfztj().equals(""))) {


//				�����˷ѵ��ۼ�
                strSql = "select zb.bianm,\n" +
                        "   max(mx.hetbz) as hetbz,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(gongf)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.gongf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "              end\n" +
                        "   end as gongf,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(changf)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.changf))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "             end\n" +
                        "   end as changf,\n" +
                        "   case when zb.bianm='" + Locale.Shul_zhibb + "' then sum(jies)\n" +
                        "        else\n" +
                        "             case when zb.bianm='" + Locale.Qnetar_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),0)\n" +
                        "                  else\n" +
                        "                       case when zb.bianm='" + Locale.Mt_zhibb + "' then round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),1)\n" +
                        "                            else\n" +
                        "                                 round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.jies))/sum(decode(jiessl,0,1,jiessl)),2)\n" +
                        "                       end\n" +
                        "             end\n" +
                        "   end as jies,\n" +
                        "   sum(mx.yingk) as yingk,\n" +
                        "   round_new(sum(decode(mx.jiessl,0,0,jiessl*mx.zhejbz))/sum(decode(jiessl,0,1,jiessl)),4) as zhejbz,\n" +
                        "   sum(mx.zhejje) as zhejje\n" +
                        " from danpcjsmxb mx,zhibb zb\n" +
                        " where zb.id=mx.zhibb_id and mx.leib=1 and mx.zhekfs = 0 and jiesdid=" + bsv.getYunfjsb_id() + " \n" +
                        " group by zb.bianm";
                rs = con.getResultSet(strSql);

                while (rs.next()) {

                    if (rs.getString("bianm").equals(Locale.Shul_zhibb)) {

                        bsv.setShul_ht(rs.getString("hetbz"));
                        bsv.setGongfsl(rs.getDouble("gongf"));
                        bsv.setYanssl(rs.getDouble("changf"));
                        bsv.setJiessl(rs.getDouble("jies"));
                        bsv.setShulzb_yk(rs.getDouble("yingk"));
                        bsv.setShulzb_zdj(rs.getDouble("zhejbz"));
                        bsv.setShulzb_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qnetar_zhibb)) {

                        bsv.setQnetar_ht(rs.getString("hetbz"));
                        bsv.setQnetar_kf(rs.getDouble("gongf"));
                        bsv.setQnetar_cf(rs.getDouble("changf"));
                        bsv.setQnetar_js(rs.getDouble("jies"));
                        bsv.setQnetar_yk(rs.getDouble("yingk"));
                        bsv.setQnetar_zdj(rs.getDouble("zhejbz"));
                        bsv.setQnetar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Std_zhibb)) {

                        bsv.setStd_ht(rs.getString("hetbz"));
                        bsv.setStd_kf(rs.getDouble("gongf"));
                        bsv.setStd_cf(rs.getDouble("changf"));
                        bsv.setStd_js(rs.getDouble("jies"));
                        bsv.setStd_yk(rs.getDouble("yingk"));
                        bsv.setStd_zdj(rs.getDouble("zhejbz"));
                        bsv.setStd_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Ad_zhibb)) {

                        bsv.setAd_ht(rs.getString("hetbz"));
                        bsv.setAd_kf(rs.getDouble("gongf"));
                        bsv.setAd_cf(rs.getDouble("changf"));
                        bsv.setAd_js(rs.getDouble("jies"));
                        bsv.setAd_yk(rs.getDouble("yingk"));
                        bsv.setAd_zdj(rs.getDouble("zhejbz"));
                        bsv.setAd_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Vdaf_zhibb)) {

                        bsv.setVdaf_ht(rs.getString("hetbz"));
                        bsv.setVdaf_kf(rs.getDouble("gongf"));
                        bsv.setVdaf_cf(rs.getDouble("changf"));
                        bsv.setVdaf_js(rs.getDouble("jies"));
                        bsv.setVdaf_yk(rs.getDouble("yingk"));
                        bsv.setVdaf_zdj(rs.getDouble("zhejbz"));
                        bsv.setVdaf_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Mt_zhibb)) {

                        bsv.setMt_ht(rs.getString("hetbz"));
                        bsv.setMt_kf(rs.getDouble("gongf"));
                        bsv.setMt_cf(rs.getDouble("changf"));
                        bsv.setMt_js(rs.getDouble("jies"));
                        bsv.setMt_yk(rs.getDouble("yingk"));
                        bsv.setMt_zdj(rs.getDouble("zhejbz"));
                        bsv.setMt_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qgrad_zhibb)) {

                        bsv.setQgrad_ht(rs.getString("hetbz"));
                        bsv.setQgrad_kf(rs.getDouble("gongf"));
                        bsv.setQgrad_cf(rs.getDouble("changf"));
                        bsv.setQgrad_js(rs.getDouble("jies"));
                        bsv.setQgrad_yk(rs.getDouble("yingk"));
                        bsv.setQgrad_zdj(rs.getDouble("zhejbz"));
                        bsv.setQgrad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Qbad_zhibb)) {

                        bsv.setQbad_ht(rs.getString("hetbz"));
                        bsv.setQbad_kf(rs.getDouble("gongf"));
                        bsv.setQbad_cf(rs.getDouble("changf"));
                        bsv.setQbad_js(rs.getDouble("jies"));
                        bsv.setQbad_yk(rs.getDouble("yingk"));
                        bsv.setQbad_zdj(rs.getDouble("zhejbz"));
                        bsv.setQbad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Had_zhibb)) {

                        bsv.setHad_ht(rs.getString("hetbz"));
                        bsv.setHad_kf(rs.getDouble("gongf"));
                        bsv.setHad_cf(rs.getDouble("changf"));
                        bsv.setHad_js(rs.getDouble("jies"));
                        bsv.setHad_yk(rs.getDouble("yingk"));
                        bsv.setHad_zdj(rs.getDouble("zhejbz"));
                        bsv.setHad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Stad_zhibb)) {

                        bsv.setStad_ht(rs.getString("hetbz"));
                        bsv.setStad_kf(rs.getDouble("gongf"));
                        bsv.setStad_cf(rs.getDouble("changf"));
                        bsv.setStad_js(rs.getDouble("jies"));
                        bsv.setStad_yk(rs.getDouble("yingk"));
                        bsv.setStad_zdj(rs.getDouble("zhejbz"));
                        bsv.setStad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Star_zhibb)) {

                        bsv.setStar_ht(rs.getString("hetbz"));
                        bsv.setStar_kf(rs.getDouble("gongf"));
                        bsv.setStar_cf(rs.getDouble("changf"));
                        bsv.setStar_js(rs.getDouble("jies"));
                        bsv.setStar_yk(rs.getDouble("yingk"));
                        bsv.setStar_zdj(rs.getDouble("zhejbz"));
                        bsv.setStar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Mad_zhibb)) {

                        bsv.setMad_ht(rs.getString("hetbz"));
                        bsv.setMad_kf(rs.getDouble("gongf"));
                        bsv.setMad_cf(rs.getDouble("changf"));
                        bsv.setMad_js(rs.getDouble("jies"));
                        bsv.setMad_yk(rs.getDouble("yingk"));
                        bsv.setMad_zdj(rs.getDouble("zhejbz"));
                        bsv.setMad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Aar_zhibb)) {

                        bsv.setAar_ht(rs.getString("hetbz"));
                        bsv.setAar_kf(rs.getDouble("gongf"));
                        bsv.setAar_cf(rs.getDouble("changf"));
                        bsv.setAar_js(rs.getDouble("jies"));
                        bsv.setAar_yk(rs.getDouble("yingk"));
                        bsv.setAar_zdj(rs.getDouble("zhejbz"));
                        bsv.setAar_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Aad_zhibb)) {

                        bsv.setAad_ht(rs.getString("hetbz"));
                        bsv.setAad_kf(rs.getDouble("gongf"));
                        bsv.setAad_cf(rs.getDouble("changf"));
                        bsv.setAad_js(rs.getDouble("jies"));
                        bsv.setAad_yk(rs.getDouble("yingk"));
                        bsv.setAad_zdj(rs.getDouble("zhejbz"));
                        bsv.setAad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Vad_zhibb)) {

                        bsv.setVad_ht(rs.getString("hetbz"));
                        bsv.setVad_kf(rs.getDouble("gongf"));
                        bsv.setVad_cf(rs.getDouble("changf"));
                        bsv.setVad_js(rs.getDouble("jies"));
                        bsv.setVad_yk(rs.getDouble("yingk"));
                        bsv.setVad_zdj(rs.getDouble("zhejbz"));
                        bsv.setVad_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.T2_zhibb)) {

                        bsv.setT2_ht(rs.getString("hetbz"));
                        bsv.setT2_kf(rs.getDouble("gongf"));
                        bsv.setT2_cf(rs.getDouble("changf"));
                        bsv.setT2_js(rs.getDouble("jies"));
                        bsv.setT2_yk(rs.getDouble("yingk"));
                        bsv.setT2_zdj(rs.getDouble("zhejbz"));
                        bsv.setT2_zje(rs.getDouble("zhejje"));

                    } else if (rs.getString("bianm").equals(Locale.Yunju_zhibb)) {

                        bsv.setYunju_ht(rs.getString("hetbz"));
                        bsv.setYunju_kf(rs.getDouble("gongf"));
                        bsv.setYunju_cf(rs.getDouble("changf"));
                        bsv.setYunju_js(rs.getDouble("jies"));
                        bsv.setYunju_yk(rs.getDouble("yingk"));
                        bsv.setYunju_zdj(rs.getDouble("zhejbz"));
                        bsv.setYunju_zje(rs.getDouble("zhejje"));
                    }
                }
            }

//			�����˷ѵĽ����������۸񡢽����Ϣ
            strSql = "select sum(gongfsl) as gongfsl,sum(yanssl) as yanssl,sum(jiessl) as jiessl,sum(koud) as koud,	\n"
                    + " 	sum(kous) as kous,sum(kouz) as kouz,sum(ches) as ches,sum(jingz) as jingz,	\n"
                    + " 	sum(koud_js) as koud_js,sum(jiesslcy) as jiesslcy,sum(yuns) as yuns, sum(shulzbyk) as shulzbyk,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiajqdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiajqdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*jiesdj))/sum(decode(jiessl,0,1,jiessl))," + bsv.getMeikhsdjblxsw() + ") as jiesdj,	\n"
                    + " 	sum(jiakhj) as jiakhj,sum(jiaksk) as jiaksk,sum(jiashj) as jiashj,sum(chaokdl) as chaokdl,		\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*biaomdj))/sum(decode(jiessl,0,1,jiessl)),2) as biaomdj,	\n"
                    + " 	round_new(sum(decode(jiessl,0,0,jiessl*buhsbmdj))/sum(decode(jiessl,0,1,jiessl)),2) as buhsbmdj	\n"
                    + " from 	\n"
                    + " 	(select xuh,	\n"
                    + " 		max(mx.gongfsl) as gongfsl,		\n"
                    + " 		max(mx.yanssl) as yanssl,		\n"
                    + " 		max(mx.jiessl) as jiessl,		\n"
                    + " 		max(mx.koud)  as koud,			\n"
                    + " 		max(mx.kous)  as kous,			\n"
                    + " 		max(mx.kouz)  as kouz,			\n"
                    + " 		max(mx.ches)  as ches,			\n"
                    + " 		max(mx.jingz)  as jingz,		\n"
                    + " 		max(mx.koud_js)  as koud_js,	\n"
                    + " 		max(mx.jiesslcy) as jiesslcy,	\n"
                    + " 		max(mx.yuns) as yuns,			\n"
                    + " 		max(mx.jiajqdj) as jiajqdj,		\n"
                    + " 		max(mx.jiesdj) as jiesdj,		\n"
                    + " 		max(mx.jiakhj) as jiakhj,		\n"
                    + " 		max(mx.jiaksk) as jiaksk,		\n"
                    + " 		max(mx.jiashj) as jiashj,		\n"
                    + "			max(mx.chaokdl) as chaokdl,		\n"
                    + "			max(mx.shulzbyk) as shulzbyk,	\n"
                    + " 		0 as biaomdj,		\n"
                    + " 		0 as buhsbmdj		\n"
                    + " 	from danpcjsmxb mx		\n"
                    + " 	where leib=1 and zhekfs = 0 and jiesdid=" + bsv.getYunfjsb_id() + "	\n"
                    + " 	group by xuh)";
            rs = con.getResultSet(strSql);
            while (rs.next()) {

                if (rs.getDouble("jiessl") > 0) {

                    if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                            && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                            && (!bsv.getYunfjsdpcfztj().equals(""))) {

//						���˷ѽ��㣬Ҫͨ��danpcjsmxb��ҳ�����Ŀ��ֵ

//						����۸�
                        bsv.setGongfsl(rs.getDouble("gongfsl"));
                        bsv.setYanssl(rs.getDouble("yanssl"));
                        bsv.setJiessl(rs.getDouble("jiessl"));
                        bsv.setYunfjsl(bsv.getJiessl());
                        bsv.setKoud(rs.getDouble("koud"));
                        bsv.setKous(rs.getDouble("kous"));
                        bsv.setKouz(rs.getDouble("kouz"));
                        bsv.setChes(rs.getLong("ches"));
                        bsv.setJingz(rs.getDouble("jingz"));
                        bsv.setKoud_js(rs.getDouble("koud_js"));
                        bsv.setJiesslcy(rs.getDouble("jiesslcy"));
                        bsv.setYuns(rs.getDouble("yuns"));
                        bsv.setJiajqdj(rs.getDouble("jiajqdj"));
//						bsv.setShulzjbz(rs.getDouble("jiesdj"));
                        bsv.setHansmj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj_mk(rs.getDouble("jiesdj"));
                        bsv.setBuhsyf(rs.getDouble("jiakhj"));    //setJiakhj
//						bsv.setJine(rs.getDouble("jiakhj"));
                        bsv.setYunfsk(rs.getDouble("jiaksk"));    //setJiaksk
                        bsv.setYunzfhj(rs.getDouble("jiashj"));    //setJiashj
                        bsv.setTielyf(rs.getDouble("jiashj"));    //�����ν����˷ѵ����ֻ�����������ͬ�ģ�
                        //	�˷Ѳ�������ͨ���˶Ի�Ʊ��õġ�
//						bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
                        bsv.setChaokdl(rs.getDouble("chaokdl"));
                        bsv.setYingksl(rs.getDouble("shulzbyk"));

                    } else if ((!bsv.getYunfjsdpcfztj().equals(""))
                            && (bsv.getJieslx() == Locale.liangpjs_feiylbb_id
                            || bsv.getJieslx() == Locale.meikjs_feiylbb_id)) {

                        bsv.setBuhsyf(rs.getDouble("jiakhj"));    //setJiakhj
                        bsv.setYunfsk(rs.getDouble("jiaksk"));    //setJiaksk
                        bsv.setYunzfhj(rs.getDouble("jiashj"));    //setJiashj
                        bsv.setTielyf(rs.getDouble("jiashj"));    //�����ν����˷ѵ����ֻ�����������ͬ�ģ�
                        //	�˷Ѳ�������ͨ���˶Ի�Ʊ��õġ�
                        bsv.setYunfjsdj(rs.getDouble("jiesdj"));
                        bsv.setYunfjsdj_mk(rs.getDouble("jiesdj"));
                        bsv.setYingksl(rs.getDouble("shulzbyk"));
                    }
                }
            }

            rs.close();

//			���㵽������ú���˷ѵ����
            if (bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)) {
//				�����ͬ�еĽ��㷽ʽΪ������,ͳһ�㷨��
//				������˷ѣ���ô����ú���ȥ���˷�ʣ�µľ�����ú��
//				computYunfAndHej();
//				if(bsv.getYunzfhj()>0){

//					bsv.setJiashj((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getYunzfhj(),2));	//ԭʼ��˰�ϼ�
//					bsv.setJiakhj((double)CustomMaths.Round_new(bsv.getJiashj()/(1+bsv.getMeiksl()),2));
//					bsv.setJiaksk((double)CustomMaths.Round_new(bsv.getJiashj()-bsv.getJiakhj(),2));
//					bsv.setJine(bsv.getJiakhj());
//					bsv.setHansmj((double)CustomMaths.Round_new(bsv.getJiashj()/bsv.getJiessl(), bsv.getMeikhsdjblxsw()));
//					bsv.setBuhsmj((double)CustomMaths.Round_new(bsv.getJiakhj()/bsv.getJiessl(),7));
//
////					Jiesdcz.UpdateDanpcjsmkb(bsv.getMeikjsb_id(), bsv.getHansmj(), bsv.getJiakhj(),
////							bsv.getJiaksk(), bsv.getJiashj(), CustomMaths.sub(bsv.getHansmj(), bsv.getFengsjj()));

                Danjsmk_dcjcl(2, "", 0, 0);
//				}
            }


//	    	����ܸ������˷�
            if (bsv.getKuidjfyf().equals("��")) {

                double Tielyfdj = (double) CustomMaths.Round_new(bsv.getTielyf() / bsv.getGongfsl(), 2);
                double Tielzfdj = (double) CustomMaths.Round_new(bsv.getTielzf() / bsv.getGongfsl(), 2);
                double Tielyfsdj = (double) CustomMaths.Round_new(bsv.getTielyfsk() / bsv.getGongfsl(), 2);

                double Kuangqyfdj = (double) CustomMaths.Round_new(bsv.getKuangqyf() / bsv.getGongfsl(), 2);
                double Kuangqzfdj = (double) CustomMaths.Round_new(bsv.getKuangqzf() / bsv.getGongfsl(), 2);
                double Kuangqyfsdj = (double) CustomMaths.Round_new(bsv.getKuangqsk() / bsv.getGongfsl(), 2);

                bsv.setTielyf((double) CustomMaths.Round_new(bsv.getTielyf() - (double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2), 2));
                bsv.setTielzf((double) CustomMaths.Round_new(bsv.getTielzf() - (double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2), 2));
                bsv.setTielyfsk((double) CustomMaths.Round_new(bsv.getTielyfsk() - (double) CustomMaths.Round_new(Tielyfsdj * bsv.getKuid(), 2), 2));

                bsv.setKuangqyf((double) CustomMaths.Round_new(bsv.getKuangqyf() - (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2), 2));
                bsv.setKuangqzf((double) CustomMaths.Round_new(bsv.getKuangqzf() - (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2), 2));
                bsv.setKuangqsk((double) CustomMaths.Round_new(bsv.getKuangqsk() - (double) CustomMaths.Round_new(Kuangqyfsdj * bsv.getKuid(), 2), 2));

                if ((double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) > 0) {

                    bsv.setBeiz(bsv.getBeiz() + " " + "���־ܸ��˷ѣ�" + (double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) + "Ԫ�����־ܸ��ӷѣ�" + (double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2) + "Ԫ");
                }

                if ((double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2) > 0) {

                    bsv.setBeiz(bsv.getBeiz() + " " + "���־ܸ����˷ѣ�" + (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2) + "Ԫ�����־ܸ����ӷѣ�" + (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2) + "Ԫ");
                }

                bsv.setKuidjfyf_je((double) CustomMaths.Round_new(Tielyfdj * bsv.getKuid(), 2) + (double) CustomMaths.Round_new(Kuangqyfdj * bsv.getKuid(), 2));
                bsv.setKuidjfzf_je((double) CustomMaths.Round_new(Tielzfdj * bsv.getKuid(), 2) + (double) CustomMaths.Round_new(Kuangqzfdj * bsv.getKuid(), 2));
            }

//			�����ν���ú��ʱ������п۶�����ֵʱ���ڴ������ļ���
            if ((!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
                    && bsv.getJieskdl() != 0) {
//				�����ú����㣬�ҽ�����ʽ���ǡ���Ȩƽ���������¼������۶ֺ��ϴν�����

//				����۶�+�ϴν�������Ӧ���۽��
                double koudzje = CustomMaths.Round_new(CustomMaths.mul(-bsv.getJieskdl(), bsv.getHansmj()), 2);

                bsv.setJiashj(CustomMaths.Round_new(CustomMaths.add(bsv.getJiashj(), koudzje), 2));
                if (bsv.getKoudcxjsdj()) {
                    bsv.setHansmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), 2));
                    bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
                }
                bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                bsv.setJine(bsv.getJiakhj());
                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), -bsv.getJieskdl()));
                bsv.setJiesslcy(CustomMaths.Round_new((bsv.getJiessl() - bsv.getJingz()), 2));    //������������(�������͹������Ĳ�ֵ)

                if (bsv.getShulzb_yk() != 0) {
//					���ڶԡ�������ָ������ۿҪ���¼���ӯ��

                    bsv.setShulzb_yk(Jiesdcz.reCoundYk(bsv.getShul_ht(), bsv.getJiessl(), bsv.getShul_yk()));
                }
            }

//			�����ν���ú��ʱ������п۽��ʱ���ڴ������ļ���
            if ((!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
                    && bsv.getKouk_js() != 0) {

                bsv.setJiashj(bsv.getJiashj() - bsv.getKouk_js());
                bsv.setHansmj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), bsv.getJiessl()), 2));
                bsv.setBuhsmj(CustomMaths.Round_new(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiakhj(CustomMaths.Round_new(CustomMaths.div(bsv.getJiashj(), (1 + bsv.getMeiksl())), 2));
                bsv.setJiaksk(CustomMaths.Round_new(CustomMaths.sub(bsv.getJiashj(), bsv.getJiakhj()), 2));
                bsv.setJine(bsv.getJiakhj());
            }

//			�������˷ѽ���ʱ������п۶ֻ��ϴν�������ֵʱ���ڴ������ļ���
            if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                    && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                    && (!bsv.getYunfjsdpcfztj().equals(""))
                    && (bsv.getJieskdl() != 0)
                    && (!bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs))
//					�˷�Ϊ�����ν���ʱ��������ڿ۶ֻ��ϴν�����Ҫ���¼������˼��ܽ��
                    ) {

                bsv.setJiessl(CustomMaths.add(bsv.getJiessl(), -bsv.getJieskdl()));
                bsv.setYunfjsl(bsv.getJiessl());
                bsv.setJiesslcy(CustomMaths.sub(bsv.getJiessl(), bsv.getJingz()));

                if (bsv.getJieslx() != Locale.liangpjs_feiylbb_id
                        && bsv.getJieslx() != Locale.meikjs_feiylbb_id
                        && (!bsv.getYunfjsdpcfztj().equals(""))) {

//					�˷ѽ���
                    bsv.setYunzfhj(CustomMaths.sub(bsv.getYunzfhj(), CustomMaths.mul(bsv.getYunfjsdj(), -bsv.getJieskdl())));
                    bsv.setBuhsyf(CustomMaths.Round_new(bsv.getYunzfhj() * (1 - bsv.getYunfsl()), 2));    //setJiakhj
                    bsv.setYunfsk(CustomMaths.sub(bsv.getYunzfhj(), bsv.getBuhsyf()));    //setJiaksk
                    bsv.setTielyf(bsv.getYunzfhj());    //�����ν����˷ѵ����ֻ�����������ͬ�ģ�
                    //	�˷Ѳ�������ͨ���˶Ի�Ʊ��õġ�
                } else if ((!bsv.getYunfjsdpcfztj().equals(""))
                        && (bsv.getJieslx() == Locale.liangpjs_feiylbb_id
                        || bsv.getJieslx() == Locale.meikjs_feiylbb_id)) {

                    bsv.setYunzfhj(CustomMaths.sub(bsv.getYunzfhj(), CustomMaths.mul(bsv.getYunfjsdj(), -bsv.getJieskdl())));
                    bsv.setBuhsyf(CustomMaths.Round_new(bsv.getYunzfhj() * (1 - bsv.getYunfsl()), 2));    //setJiakhj
                    bsv.setYunfsk(CustomMaths.sub(bsv.getYunzfhj(), bsv.getBuhsyf()));    //setJiaksk
                    bsv.setTielyf(bsv.getYunzfhj());    //�����ν����˷ѵ����ֻ�����������ͬ�ģ�
                    //	�˷Ѳ�������ͨ���˶Ի�Ʊ��õġ�
                }

                if (bsv.getShulzb_yk() != 0) {
//					���ڶԡ�������ָ������ۿҪ���¼���ӯ��

                    bsv.setShulzb_yk(Jiesdcz.reCoundYk(bsv.getShul_ht(), bsv.getJiessl(), bsv.getShul_yk()));
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            con.Close();
        }
    }

    private void computYunfAndHej() {

        //�˷�
        double _Tielyf = bsv.getTielyf();
        double _Tielzf = bsv.getTielzf();
        double _Yunfsl = bsv.getYunfsl();        //�˷�˰��
        double _Tielyfjk = bsv.getTielyfjk();
        double _Tielyfsk = bsv.getTielyfsk();
        double _Kuangqyf = bsv.getKuangqyf();
        double _Kuangqzf = bsv.getKuangqzf();
        double _Kuangqsk = bsv.getKuangqsk();
        double _Kuangqjk = bsv.getKuangqjk();
        double _Bukjk = bsv.getBukjk();
        double _Bukyzf = bsv.getBukyzf();

        double _Yunfsk = 0;
        double _Yunzfhj = 0;
        double _Buhsyf = 0;
        double _Hej = 0;
        String _Hejdx = "";

        //�����˷���
        _Yunzfhj = (double) CustomMaths.Round_new(_Tielyf + _Tielzf + _Kuangqyf + _Kuangqzf + _Bukjk, 2);                                    //���ӷѺϼ�
        String yunjslx = MainGlobal.getXitxx_item("����", "�˷�˰�Ƿ�����ֵ˰", visit.getDiancxxb_id(), "��");
//		˰�ʵ���
        if (_Tielyfsk + _Kuangqsk > 0) {
            //		����Ǹ��ݻ�Ʊ������
            if (yunjslx.equals("��")) {
                _Yunfsk = (double) CustomMaths.Round_new(CustomMaths.add(CustomMaths.add(_Tielyfsk, _Kuangqsk), (double) CustomMaths.Round_new(_Bukjk - _Bukjk / (1 + _Yunfsl), 2)), 2);        //�˷�˰��
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(CustomMaths.add(CustomMaths.add(_Tielyfsk, _Kuangqsk), (double) CustomMaths.Round_new(_Bukjk * _Yunfsl, 2)), 2);        //�˷�˰��
            }
        } else {
            //		�˷�˰����ص����˴�
            if (yunjslx.equals("��")) {
                _Yunfsk = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new(_Tielyf - _Tielyf / (1 + _Yunfsl), 2) + _Kuangqsk + (double) CustomMaths.Round_new(_Bukjk - _Bukjk / (1 + _Yunfsl), 2)), 2);        //�˷�˰��
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new(_Tielyf * _Yunfsl, 2) + _Kuangqsk + (double) CustomMaths.Round_new(_Bukjk * _Yunfsl, 2)), 2);        //�˷�˰��
            }
        }

        _Buhsyf = (double) CustomMaths.Round_new(((double) CustomMaths.Round_new((_Yunzfhj - _Yunfsk), 2) + _Kuangqjk), 2);        //����˰�˷�

        if (_Yunzfhj == 0 || _Yunzfhj == _Bukjk) {

            _Yunzfhj = (double) CustomMaths.Round_new(bsv.getYunfjsdj() * bsv.getYunfjsl() + _Bukjk, 2);                        //�˷�˰��
//			˰�ʵ���
//			�˷�˰����ص����˴�
            if (yunjslx.equals("��")) {
                _Yunfsk = (double) CustomMaths.Round_new(_Yunzfhj - _Yunzfhj / (1 + _Yunfsl), 2);
            } else {
                _Yunfsk = (double) CustomMaths.Round_new(_Yunzfhj * _Yunfsl, 2); //�˷�˰��
            }
            //�˷�˰��
            _Buhsyf = (double) CustomMaths.Round_new((_Yunzfhj - _Yunfsk), 2);                                            //����˰�˷�
            _Tielyf = _Yunzfhj;
            bsv.setTielyf(_Tielyf);
        }

//		���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���
        if (bsv.getYunzfhj_mk() == 0) {

            bsv.setYunzfhj_mk(_Yunzfhj);
            bsv.setBuhsyf_mk(_Buhsyf);
        } else if (_Yunzfhj > 0 && bsv.getYunzfhj_mk() != _Yunzfhj) {

            bsv.setYunzfhj_mk(_Yunzfhj);
            bsv.setBuhsyf_mk(_Buhsyf);
        }
//		��¼�˷ѽ�������
        bsv.setYunfjsl_mk(bsv.getYunfjsl());
//		���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���_end

        bsv.setJiashj(CustomMaths.add(bsv.getJiashj(), _Bukyzf));
        bsv.setJiakhj(CustomMaths.Round_new((bsv.getJiashj()) / (1 + bsv.getMeiksl()), 2));    //�ۿ�ϼ�
        bsv.setJiaksk((double) CustomMaths.Round_new((bsv.getJiashj() - bsv.getJiakhj()), 2));        //�ۿ�˰��
        //�ϼ�

        _Hej = (double) CustomMaths.Round_new((_Yunzfhj + bsv.getJiashj()), 2);
        _Hejdx = getDXMoney(_Hej);

        bsv.setYunfsk(_Yunfsk);
        bsv.setBuhsyf(_Buhsyf);
        bsv.setYunzfhj(_Yunzfhj);
        bsv.setYunfsk(_Yunfsk);
        bsv.setBuhsyf(_Buhsyf);
        bsv.setHej(_Hej);
        bsv.setHejdx(_Hejdx);

        if (bsv.getYunfjsdj() == 0.0) {
            bsv.setYunfjsdj(CustomMaths.Round_new(_Yunzfhj / bsv.getYunfjsl(), bsv.getYunfhsdjblxsw()));
        }

        String flag = MainGlobal.getXitxx_item("����", "��Ʊ����,�˷��㵽ú����,���ҷ��㵥��", String.valueOf(bsv.getDiancxxb_id()), "��");
        if ("��".equals(flag)) {
//			ú��������˷�------------��ú��ú�����˷�ú����һ�����Է���
            bsv.setJiashj(bsv.getJiashj() + bsv.getYunzfhj());
            bsv.setJiakhj(getRound_xz((bsv.getJiashj()) / (1 + bsv.getMeiksl()), 2));
            bsv.setJiaksk(getRound_xz((bsv.getJiashj() - bsv.getJiakhj()), 2));
            bsv.setHansmj(getRound_xz(bsv.getJiashj() / bsv.getJiessl(), 2));
            bsv.setBuhsmj(getRound_xz(CustomMaths.div(bsv.getHansmj(), (1 + bsv.getMeiksl())), 2));
            bsv.setJiajqdj(bsv.getJiashj());
            bsv.setJine(bsv.getJiashj());
//			��������˷���Ϣ
            bsv.setTielyf(0);
            bsv.setTielzf(0);
            bsv.setYunfsk(0);
            bsv.setBuhsyf(0);
            bsv.setYunzfhj(0);
            bsv.setYunfsk(0);
            bsv.setBuhsyf(0);
            bsv.setYunfjsdj(0);
            bsv.setYunfsl(0);
        }
    }

    private void Jiestszbcl(String Jieszbsftz, String SelIds, long Diancxxb_id, long Gongysb_id,
                            long Hetb_id, double Jieskdl, long Yunsdwb_id, long Jieslx, double Shangcjsl) {
//		��������ָ�괦��
//		Ŀǰ��ֻ�����Ȩƽ������ʱ��ĳЩָ����Ҫ�����μ��㣬�������⴦������
//		�ú���ֻ�������ۿ�
        Interpreter bsh = null;
        String tmp[] = null;
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        String strtmp = "";
        double jiessl = bsv.getJiessl();
        try {
            for (int i = 0; i < bsv.getTsclzbs().length; i++) {

                tmp = bsv.getTsclzbs()[i].split(",");
                if (tmp[tmp.length - 1].equals("0")) {
//					˵����ָ�껹δ�������⴦��

                    bsh = new Interpreter();
                    bsh.set("������ʽ", Locale.jiaqpj_jiesxs);

//					ú�˰���۱���С��λ
                    bsh.set(Locale.meikhsdjblxsw_jies, bsv.getMeikhsdjblxsw());

//					��˰����ȡ����ʽ
                    bsh.set(Locale.meikhsdjqzfs_jies, bsv.getMeikhsdj_qzfs());

                    bsh.set("�Ƽ۷�ʽ", Locale.rezakkf_hetjjfs);
                    bsh.set("�۸�λ", Locale.yuanmqk_danw);
                    bsh.set("��ͬ�۸�", 0);
                    bsh.set("���ú��", bsv.getZuigmj());

//					���ۿ��С��λ
                    bsh.set(Locale.Meikzkkblxsw_xitxx, bsv.getMeikzkkblxsw());

//					�û��Զ��幫ʽ
                    bsh.set(Locale.user_custom_mlj_jiesgs, bsv.getUser_custom_mlj_jiesgs());

                    this.getZengkk(bsv.getHetb_Id(), bsh, false, tmp);
                    if (bsv.getTsclzbzkksfxyjs()) {

//						����һ��ָ��ֵ���������ۿ����
                        bsv.setTsclzbzkksfxyjs(false);
//						��ָ����Ҫ���ۿ����⴦��
                        bsh.eval(bsv.getGongs_Mk());
//						�õ����ۿ�۸���Ϣ
                        Jiesdcz.setJieszb_Tszbcl(bsh, bsv, tmp[1]);

//						Ҫ�ҵ������ۿ��Ӧ������
                        rsl = con.getResultSetList(Jiesdcz.getJiesszl_Sql(bsv, visit, Jieszbsftz, SelIds, Diancxxb_id, Gongysb_id,
                                Hetb_id, Jieskdl, Yunsdwb_id, Jieslx, Shangcjsl, " and " + tmp[2] + "=" + tmp[3]).toString());
                        bsv.setJiessl(jiessl);
                        if (rsl.next()) {

//							���ĳһ��ָ������ۿ��Ƿ�Ϊ������,����Ǿ����ܽ������ϼ�ȥ������������۽�����
                            if (!Jiesdcz.checkZbzkksl(bsv, tmp[1])) {

                                double _Meiksl = bsv.getMeiksl();
                                double zhibzdj = Double.parseDouble(bsh.get("�۵���_" + tmp[1]).toString());

                                if (bsh.get(tmp[1] + "��������").toString().equals("����˰����")) {
                                    zhibzdj = CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_" + tmp[1]).toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw());
                                }

//								�õ���ָ����ۼ۱�׼
//								�ַ�������ָ�����,ָ���ֵ,ָ���۵���,�ۼ�����,�۽��
                                if (tmp[tmp.length - 2].equals("'C'")) {
//									˵���ǶԳ������ֵĵ��������ۼ۵Ĳ�������ʱ���ۿ��Ӧ����ӦΪ ��ָ���ӯ��������Ϊֻ���������ۼۣ�Ŀǰ�������ģ�
//									strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("�۵���_"+tmp[1]).toString()+","+bsh.get("ӯ��_"+tmp[1]).toString()+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("�۵���_"+tmp[1]).toString()),Double.parseDouble(bsh.get("ӯ��_"+tmp[1]).toString())),2)+";";
                                    strtmp += tmp[1] + "," + tmp[4] + "," + zhibzdj + "," + bsh.get("ӯ��_" + tmp[1]).toString() + "," + CustomMaths.Round_new(CustomMaths.mul(zhibzdj, Double.parseDouble(bsh.get("ӯ��_" + tmp[1]).toString())), 2) + ";";
                                } else {
//									˵���Ƕ����ۿ��С������Ρ�����Ĵ���
//									strtmp+=tmp[1]+","+tmp[4]+","+bsh.get("�۵���_"+tmp[1]).toString()+","+rsl.getString("jiessl")+","+CustomMaths.Round_new(CustomMaths.mul(Double.parseDouble(bsh.get("�۵���_"+tmp[1]).toString()),rsl.getDouble("jiessl")),2)+";";
                                    strtmp += tmp[1] + "," + tmp[4] + "," + zhibzdj + "," + rsl.getString("jiessl") + "," + CustomMaths.Round_new(CustomMaths.mul(zhibzdj, rsl.getDouble("jiessl")), 2) + ";";
                                }
                            } else {

                                Jiesdcz.getReCountJiessl(bsv, rsl.getDouble("jiessl"), 0);
                            }
                        }
                    } else if (tmp[1].equals(Locale.jiessl_zhibb)) {

                        bsv.setJiessl(jiessl);
                    }
                }
            }

            if (!strtmp.equals("")) {
//				��������⴦���ָ�꣬�ͽ�Tsclzbs��ֵΪ����ָ������ۿ��¼

                bsv.setTsclzbs(strtmp.split(";"));
//				�˾�,25,10,100,1000;
//				�˾�,23,12,300,3600;
//				����ǶԳ������ֵ����ۿ�������ַ�������ʽ����:
//					��������,'C',4.75,95.0,451.25;
            } else {
//				��Tsclzbs����
                bsv.setTsclzbs(null);
            }

            if (rsl != null) {

                rsl.close();
            }

            con.Close();

        } catch (EvalError ev) {
            // TODO �Զ����� catch ��
            ev.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void Danjsmk_dcjcl(int Place, String selIds, long hetb_id, double shangcjsl) {
//		�������ƣ�

//			������ú������۴���
//		�������ܣ�

//			��������ú��ʱ����ú���ͬΪ������ʱ�����˷ѵ�����
//		�����߼���
//			�߼�1��
//				����ǵ�����ú�������ǵ�����ʱ�������˷ѡ�

//			�߼�2��
//				��recount�������Ѿ�������˵����۵������Ҫ���˷������ֵ���
//		�����βΣ�
//			PlaceҪӦ�õ��߼��ṹ��1Ϊ�߼�1��2Ϊ�߼�2
//			selIdsҪ�������id
//			idhetb_idú���ͬ��

        if (Place == 1) {
//			�߼�1
            if ((bsv.getJieslx() == Locale.meikjs_feiylbb_id || bsv.getJieslx() == Locale.liangpjs_feiylbb_id)
                    && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
                    ) {
//				����ҵ�����Σ�����ǵ�����ú���ͬ�۸��ǵ�����ʱ�������˷ѣ�
//					�ں����recount�д�ú���м����˷ѣ������˷�������ա�
                this.getYunFei(selIds, Locale.liangpjs_feiylbb_id, hetb_id, shangcjsl);
                computYunfAndHej();
            }
        } else if (Place == 2) {
//			�߼�2
            if (bsv.getJieslx() == Locale.meikjs_feiylbb_id
                    && bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)
                    ) {
//				����ҵ�����Σ�����ǵ�����ú���ͬ�۸��ǵ�����ʱ�������˷ѣ�
//					�ں����recount�д�ú���м����˷ѣ������˷�������ա�
                bsv.setTielyf(0);
                bsv.setTielzf(0);
                bsv.setYunfsl(0);        //�˷�˰��
                bsv.setKuangqyf(0);
                bsv.setKuangqzf(0);
                bsv.setKuangqsk(0);
                bsv.setKuangqjk(0);
                bsv.setYunfjsdj(0);
                bsv.setYunfjsl(0);
            }
        }
    }

    /**
     * �������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�
     * ����ú�˰���۽���ȡ������
     * @author yinjm
     * @param bsh
     * @return void
     */
    public void getMeikhsdj(Interpreter bsh) {
//		  �������ƣ�
//		      ��ȡú��˰����
//
//		  �������ܣ�
//	         �������ۿ������õ�ָ���������(jijlx=0Ϊ��˰��jijlx=1Ϊ����˰)���¼���ú�˰���ۣ�
//		     ��Щָ��Ϊ���ۿ��еķ����⴦��ָ�ꡣ
//
//	      �����߼���
//			  ȡ�����ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ�꣬�ж�ָ��Ļ��������Ƿ�˰��
//			  �����Ϊ��˰����ô��ָ����۵���ת���ɺ�˰�ġ�
//
//	      �����βΣ�
//	     	 bsh ��Interpreter������ȡ��ָ����Ϣ

        double _Meiksl = bsv.getMeiksl();

        try {

//			ȡ�����ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ��
            String[] zengkkzb = bsv.getFeitsclzb().split(",");

            for (int i = 0; i < zengkkzb.length; i++) {

                if (zengkkzb[i].equals(Locale.Std_zhibb)) {

                    if (bsh.get("Std��������").toString().equals("����˰����")) {
                        bsv.setStd_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Std").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Ad_zhibb)) {

                    if (bsh.get("Ad��������").toString().equals("����˰����")) {
                        bsv.setAd_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Ad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Vdaf_zhibb)) {

                    if (bsh.get("Vdaf��������").toString().equals("����˰����")) {
                        bsv.setVdaf_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Vdaf").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Mt_zhibb)) {

                    if (bsh.get("Mt��������").toString().equals("����˰����")) {
                        bsv.setMt_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Mt").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Qgrad_zhibb)) {

                    if (bsh.get("Qgrad��������").toString().equals("����˰����")) {
                        bsv.setQgrad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Qgrad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Qbad_zhibb)) {

                    if (bsh.get("Qbad��������").toString().equals("����˰����")) {
                        bsv.setQbad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Qbad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Had_zhibb)) {

                    if (bsh.get("Had��������").toString().equals("����˰����")) {
                        bsv.setHad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Had").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Stad_zhibb)) {

                    if (bsh.get("Stad��������").toString().equals("����˰����")) {
                        bsv.setStad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Stad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Star_zhibb)) {

                    if (bsh.get("Star��������").toString().equals("����˰����")) {
                        bsv.setStar_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Star").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Mad_zhibb)) {

                    if (bsh.get("Mad��������").toString().equals("����˰����")) {
                        bsv.setMad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Mad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Aar_zhibb)) {

                    if (bsh.get("Aar��������").toString().equals("����˰����")) {
                        bsv.setAar_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Aar").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Aad_zhibb)) {

                    if (bsh.get("Aad��������").toString().equals("����˰����")) {
                        bsv.setAad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Aad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Vad_zhibb)) {

                    if (bsh.get("Vad��������").toString().equals("����˰����")) {
                        bsv.setVad_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_Vad").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.T2_zhibb)) {

                    if (bsh.get("T2��������").toString().equals("����˰����")) {
                        bsv.setT2_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_T2").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.Yunju_zhibb)) {

                    if (bsh.get("�˾��������").toString().equals("����˰����")) {
                        bsv.setT2_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_�˾�").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                } else if (zengkkzb[i].equals(Locale.jiessl_zhibb)) {

                    if (bsh.get("����������������").toString().equals("����˰����")) {
                        bsv.setShul_zdj(CustomMaths.Round_new(Double.parseDouble(bsh.get("�۵���_��������").toString()) * (1 + _Meiksl), bsv.getMeikzkkblxsw()));
                    }

                }
            }

//			����ú����㵥��

            //zld 2010-12-10 ���㹫ʽ���Ѿ���������ָ����ۼۡ�����Ҫ�ظ����ˡ�
            double value = Double.parseDouble(bsh.get("����۸�").toString());
            //+ bsv.getStd_zdj() + bsv.getAd_zdj() + bsv.getVdaf_zdj() + bsv.getMt_zdj()
            //+ bsv.getQgrad_zdj() + bsv.getQbad_zdj() + bsv.getHad_zdj() + bsv.getStad_zdj()
            //+ bsv.getStar_zdj() + bsv.getMad_zdj() + bsv.getAar_zdj() + bsv.getAad_zdj()
            //+ bsv.getVad_zdj() + bsv.getT2_zdj() + bsv.getYunju_zdj() + bsv.getShul_zdj();
            //zld end
//			��ú����㵥�۽���ȡ������
            getMeikhsdj_quz(bsv.getMeikhsdj_qzfs(), value, bsv.getMeikhsdjblxsw());

            if (bsv.getZuigmj() > 0) {

                if (bsv.getHansmj() > bsv.getZuigmj()) {

                    bsv.setHansmj(bsv.getZuigmj());
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (EvalError e) {
            e.printStackTrace();
        }

    }

    /**
     * ����ú�˰���۵ı���С��λ
     * ������bsh������ȡ����ʽ��Ҫ�����ֵ��������С��λ
     * @author yinjm
     * @param Quzfs
     * @param value
     * @param xiaosw
     * @return
     */
    public void getMeikhsdj_quz(String Quzfs, double value, int xiaosw) {
//	  �������ƣ�
//	     ú�˰����ȡ��
//
//	  �������ܣ�
//       ��ú�˰���۽���ȡ������
//
//    �����βΣ�
//   	 Quzfs  ȡ����ʽ(��λ����ȥ����������)
//		 value  ú�˰����
//		 xiaosw ����С��λ

        double Dblvalue = 0;
        String StrValue = "";
        String StrQz = "1";    //Ȩ��
        StrValue = String.valueOf(value);

        if (StrValue.indexOf('.') == -1) {

            Dblvalue = value;
        } else {

            StrValue = StrValue.substring(StrValue.indexOf('.'));
            if (Double.parseDouble(StrValue) == 0) {

                Dblvalue = value;
            }
        }

        for (int i = 0; i < xiaosw; i++) {
            StrQz = StrQz + "0";
        }

        if (Quzfs.equals("")) {

            Dblvalue = value;

        } else if (Quzfs.equals("��λ")) {

            Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz))) + 1;
            Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
            if (value < 0) {
                Dblvalue = 0 - Dblvalue;
            }

        } else if (Quzfs.equals("��ȥ")) {

            Dblvalue = Math.floor(CustomMaths.mul(Math.abs(value), Double.parseDouble(StrQz)));
            Dblvalue = CustomMaths.div(Dblvalue, Double.parseDouble(StrQz));
            if (value < 0) {

                Dblvalue = 0 - Dblvalue;
            }

        } else if (Quzfs.equals("��������")) {

            Dblvalue = CustomMaths.Round_new(Math.abs(value), xiaosw);
            if (value < 0) {

                Dblvalue = 0 - Dblvalue;
            }

        }

        bsv.setHansmj(Dblvalue);
    }

    public String getDXMoney(double _Money) {
        Money money = new Money();
        return money.NumToRMBStr(_Money);
    }

    public IPropertySelectionModel getZhibModel() {

        if (_ZhibModel == null) {
            getIZhibmModels();
        }
        return _ZhibModel;
    }

    public IPropertySelectionModel getIZhibmModels() {

        String sql = "select id,bianm from zhibb order by bianm";
        _ZhibModel = new IDropDownModel(sql);
        return _ZhibModel;
    }

    public void pageValidate(PageEvent arg0) {
        // TODO �Զ����ɷ������

    }

    public double getRound_xz(double v, int scale) {
        if ("Round".equals(bsv.getJiesjeqzfs())) {
            if ("�����������".equals(MainGlobal.getXitxx_item("����", "�����������", "0", ""))) {
                return CustomMaths.Round(v, scale);
            } else {
                return CustomMaths.round(v, scale);
            }
        } else {
            return CustomMaths.Round_new(v, scale);
        }
    }

    private double getHetsfkhzl(String SelIds) {
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = null;
        double shuifkhzl = 0;
        try {
            rsl = con.getResultSetList("select sum(getHetsfkhl(fh.lie_id)) tiaozsl   from fahb fh,zhilb zl,hetsfkhb sfkh where fh.ZHILB_ID = zl.id and fh.HETB_ID = sfkh.HETB_ID and fh.LIE_ID in (" + SelIds + ")");
            if (rsl.next()) {
                shuifkhzl = rsl.getDouble("tiaozsl");
            }
        } catch (Exception e) {
        } finally {
            rsl.close();
            con.Close();
        }
        return shuifkhzl;
    }
}