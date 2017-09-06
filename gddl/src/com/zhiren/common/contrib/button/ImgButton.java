/*
 * �������� 2005-5-18
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.common.contrib.button;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;

/**
 * @author admin
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public abstract class ImgButton extends AbstractFormComponent {

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {

        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        if (rewinding) {
            // Don't bother doing anything if disabled.

            if (isDisabled())
                return;

            // How to know which Submit button was actually
            // clicked? When submitted, it produces a request parameter
            // with its name and value (the value serves double duty as both
            // the label on the button, and the parameter value).

            String value = cycle.getRequestContext().getParameter(name);

            // If the value isn't there, then this button wasn't
            // selected.

            if (value == null)
                return;

            IBinding selectedBinding = getSelectedBinding();

            if (selectedBinding != null)
                selectedBinding.setObject(getTag());

            IActionListener listener = getListener();

            if (listener != null)
                listener.actionTriggered(this, cycle);

            return;
        }
        writer.beginEmpty("input");

        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        String label = getLabel();
        String buttontype = getButtontype().toLowerCase();
        if (buttontype.equals("nosubmitbuttons")) {
            writer.attribute("type", "button");
        } else {
            writer.attribute("type", "submit");
        }
        String strstyle = "btn_mouseout";
        writer.attribute("class", strstyle);
        strstyle = "this.className='btn_mouseout'";
        writer.attribute("onmouseout", strstyle);
        strstyle = "this.className='btn_mouseover'";
        writer.attribute("onmouseover", strstyle);
        strstyle = "this.className='btn_mousedown'";
        writer.attribute("onmousedown", strstyle);
        strstyle = "this.className='btn_mouseup'";
        writer.attribute("onmouseup", strstyle);
        //		
        // if (label != null) {
        // writer.attribute("value", label);
        // }else {
        writer.attribute("value", getBtype(buttontype, label));
        // }

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    public IAsset getImagePath(String _assetName) {
        IAsset objImageAsset;
        objImageAsset = getAsset(_assetName);
        return objImageAsset;
    }

    private String getBtype(String _name, String label) {
        if (label != null) {
            if (label.equals("")) {
                return "��ť����";
            } else {
                return label;
            }
        }
        if (_name.toLowerCase().equals("insert")) {
            return "���";
        }
        if (_name.toLowerCase().equals("save")) {
            return "����";
        }
        if (_name.toLowerCase().equals("delete")) {
            return "ɾ��";
        }
        if (_name.toLowerCase().equals("refer")) {
            return "�ύ";
        }
        if (_name.toLowerCase().equals("refurbish")) {
            return "ˢ��";
        }
        if (_name.toLowerCase().equals("login")) {
            return "��¼";
        }
        if (_name.toLowerCase().equals("create")) {
            return "����";
        }
        if (_name.toLowerCase().equals("register")) {
            return "ע��";
        }
        return "��ť����";
    }

    public abstract String getLabel();

    public abstract String getButtontype();

    public abstract IBinding getSelectedBinding();

    public abstract boolean isDisabled();

    public abstract IActionListener getListener();

    public abstract Object getTag();

}
