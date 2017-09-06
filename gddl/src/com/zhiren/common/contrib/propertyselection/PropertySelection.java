package com.zhiren.common.contrib.propertyselection;


import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionRenderer;
import org.apache.tapestry.form.RadioPropertySelectionRenderer;
import org.apache.tapestry.form.SelectPropertySelectionRenderer;


public abstract class PropertySelection extends org.apache.tapestry.form.PropertySelection
{

    public static final IPropertySelectionRenderer DEFAULT_SELECT_RENDERER =
        new SelectPropertySelectionRenderer();

    /**
     *  A shared instance of {@link RadioPropertySelectionRenderer}.
     *
     * 
     **/

    public static final IPropertySelectionRenderer DEFAULT_RADIO_RENDERER =
        new RadioPropertySelectionRenderer();

    /**
     *  Renders the component, much of which is the responsiblity
     *  of the {@link IPropertySelectionRenderer renderer}.  The possible options,
     *  thier labels, and the values to be encoded in the form are provided
     *  by the {@link IPropertySelectionModel model}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);
        
        if (rewinding)
        {
            // If disabled, ignore anything that comes up from the client.

            if (isDisabled())
                return;

            String optionValue = cycle.getRequestContext().getParameter(name);

            Object value = (optionValue == null) ? null : getModel().translateValue(optionValue);

            setValue(value);

            return;
        }

        IPropertySelectionRenderer renderer = getRenderer();

        if (renderer != null)
        {
            renderWithRenderer(writer, cycle, renderer);
            return;
        }
        writer.begin("div");
        	writer.attribute("id", "cboDiv_"+name);
        
        writer.begin("select");
        writer.attribute("name", name);

        if (isDisabled())
            writer.attribute("disabled", "disabled");

        // Apply informal attributes.

        renderInformalParameters(writer, cycle);

        writer.println();

        IPropertySelectionModel model = getModel();

        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");

        int count = model.getOptionCount();
        boolean foundSelected = false;
        boolean selected = false;
        Object value = getValue();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            if (!foundSelected)
            {
                selected = isEqual(option, value);
                if (selected)
                    foundSelected = true;
            }

            writer.begin("option");
            writer.attribute("value", model.getValue(i));

            if (selected)
                writer.attribute("selected", "selected");

            writer.print(model.getLabel(i));

            writer.end();

            writer.println();

            selected = false;
        }

        writer.end("select"); // <select>
        writer.begin("script");
        writer.print("var cbo_"+name+";");
        writer.println();
        writer.print("function cbofun_"+name+"(){");
        writer.println();
        writer.print("if(cbo_"+name+" == null){");
        
        writer.print("if(Ext.isReady){");
        writer.print("cbo_"+name+" = new Ext.form.ComboBox({");
        writer.print("typeAhead: true,");
        writer.print("triggerAction: 'all',");
        writer.print("transform:'"+name+"',");
        if(!getEditable()) {
        	writer.print("editable: false,");
        }
        if(getWidth() !=0 ) {
        	writer.print("width:"+getWidth()+",");
        }
        else {
        	writer.print("width:document.getElementById('"+name+"').offsetWidth,");
        }
        writer.print("forceSelection:true});");
        writer.print("cbo_"+name+".onchange = function(){");
        
        if (getSubmitOnChange())
        	writer.print(form.getName()+".submit();");
        writer.print(getOnchange());
        writer.print("}");
        writer.print("}}}");
        writer.end("script");
        writer.end("div");
    }

    /**
     *  Renders the property selection using a {@link IPropertySelectionRenderer}.
     *  Support for this will be removed in 2.3.
     * 
     **/

    private void renderWithRenderer(
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionRenderer renderer)
    {
        renderer.beginRender(this, writer, cycle);

        IPropertySelectionModel model = getModel();

        int count = model.getOptionCount();

        boolean foundSelected = false;
        boolean selected = false;

        Object value = getValue();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            if (!foundSelected)
            {
                selected = isEqual(option, value);
                if (selected)
                    foundSelected = true;
            }

            renderer.renderOption(this, writer, cycle, model, option, i, selected);

            selected = false;
        }

        // A PropertySelection doesn't allow a body, so no need to worry about
        // wrapped components.

        renderer.endRender(this, writer, cycle);
    }

    private boolean isEqual(Object left, Object right)
    {
        // Both null, or same object, then are equal

        if (left == right)
            return true;

        // If one is null, the other isn't, then not equal.

        if (left == null || right == null)
            return false;

        // Both non-null; use standard comparison.

        return left.equals(right);
    }

    public abstract IPropertySelectionModel getModel();

    public abstract IPropertySelectionRenderer getRenderer();

    /** @since 2.2 **/

    public abstract boolean getSubmitOnChange();

    /** @since 2.2 **/

    public abstract Object getValue();

    /** @since 2.2 **/

    public abstract void setValue(Object value);

    /**
     *  Returns true if this PropertySelection's disabled parameter yields true.
     *  The corresponding HTML control(s) should be disabled.
     **/

    public abstract boolean isDisabled();
    
    public abstract boolean getEditable();

    public abstract int getWidth();
    
    public abstract String getOnchange();
}
