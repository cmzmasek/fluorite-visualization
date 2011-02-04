// $Id: LabelRenderer.java,v 1.4 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.heatmap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class LabelRenderer extends AbstractRenderer {

    /**
     * 
     */
    private static final long   serialVersionUID = -2114939410336781144L;
    private static final Color  LABEL_COLOR      = new Color( 255, 255, 255 );
    private final String        _label;
    private boolean             _isSelected;
    private final PlateRenderer _parentPlateRenderer;

    LabelRenderer( final String label, final PlateRenderer parentPlateRenderer ) {
        _label = label;
        _parentPlateRenderer = parentPlateRenderer;
        setIsSelected( false );
    }

    String getLabel() {
        return _label;
    }

    @Override
    PlateRenderer getParentPlateRenderer() {
        return _parentPlateRenderer;
    }

    @Override
    boolean isSelected() {
        return _isSelected;
    }

    @Override
    public void paint( final Graphics g ) {
        final int width = getWellSize() + 1;
        final Graphics2D g2 = ( Graphics2D ) g;
        g2.setColor( AbstractRenderer.DEFAULT_COLOR );
        g2.fillRect( getX(), getY(), width, width );
        if ( width > 6 ) {
            g2.setColor( LabelRenderer.LABEL_COLOR );
            g2.setFont( getParentPlateRenderer().getPlateDisplayPanel()
                    .getWellLabelFont() );
            g2.drawString( getLabel(), getX() + 2, ( getY() + width ) - 2 );
        }
    }

    @Override
    void setIsSelected( final boolean isSelected ) {
        _isSelected = isSelected;
    }

    @Override
    void setWellSize( final int size ) {
        _well_size = size;
    }
}
