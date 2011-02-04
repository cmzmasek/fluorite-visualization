// $Id: RubberbandRectangle.java,v 1.3 2010/12/13 18:59:25 cmzmasek Exp $
//
// FLUORITE -- software libraries and applications for data visualizations.
//
// Copyright (C) 2007-2008 Christian M. Zmasek
// Copyright (C) 2007-2008 Burnham Institute for Medical Research
// All rights reserved
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
//
// Contact: phylosoft @ gmail . com
//     WWW: www.phylosoft.org/fluorite
//          www.sourceforge.net/projects/fluorite

package org.fluorite.heatmap.rubberband;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

public class RubberbandRectangle extends Rubberband {

    public RubberbandRectangle() {
    }

    public RubberbandRectangle( final Component component ) {
        super( component );
    }

    @Override
    public void drawLast( final Graphics g ) {
        final Rectangle rect = lastBounds();
        g.drawRect( rect.x, rect.y, rect.width, rect.height );
    }

    @Override
    public void drawNext( final Graphics g ) {
        final Rectangle rect = getBounds();
        g.drawRect( rect.x, rect.y, rect.width, rect.height );
    }
}
