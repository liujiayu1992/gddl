/*
 * 创建日期 2005-5-18
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.common.contrib.message;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponent;

/**
 * @author admin
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public abstract class ModalDialog extends AbstractFormComponent {

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

            return;
        }
        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);

        String value = getMessage();
        if (value != null & !value.equals("")) {
            writer.attribute("value", value);
            writer.begin("script");
            writer.print("var url = \"http://\"+document.location.host+document.location.pathname;");
            writer.print("url = url+\"?service=page/MessageDialog\";");
            writer.print("window.showModalDialog(url,\""+value+"\",\"dialogWidth:250px;dialogHeight:110px;status:no;help:yes\");");
            writer.end("script");
        }
        renderInformalParameters(writer, cycle);
        // writer.closeTag();
    }

    public abstract String getMessage();

    public abstract boolean isDisabled();

}
