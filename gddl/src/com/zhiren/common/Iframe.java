//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.zhiren.common;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Implements a &lt;frame&gt; within a &lt;frameset&gt;. [<a
 * href="../../../../../ComponentReference/Iframe.html">Component Reference</a>]
 * 
 * @author Howard Lewis Ship
 * @version $Id: Iframe.java,v 1.1.1.1 2007/12/18 02:33:12 qzw Exp $
 * 
 */

public abstract class Iframe extends AbstractComponent {
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        if (cycle.isRewinding())
            return;

        IEngine engine = cycle.getEngine();
        IEngineService pageService = engine.getService(Tapestry.PAGE_SERVICE);
        ILink link = pageService.getLink(cycle, this,
                new String[] { getTargetPage() });
        writer.begin("iframe");
        writer.attribute("src", link.getURL());
        renderInformalParameters(writer, cycle);
        writer.end();
    }

    public abstract String getTargetPage();

    public abstract void setTargetPage(String targetPage);
}
