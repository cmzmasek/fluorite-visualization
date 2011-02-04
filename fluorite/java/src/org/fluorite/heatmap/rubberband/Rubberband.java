// $Id: Rubberband.java,v 1.3 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Rubberband {

    private static final Color XOR_COLOR = new Color( 250, 30, 250 );
    protected Point            _anchor;
    protected Point            _stretched;
    protected Point            _last;
    protected Point            _end;
    private Component          _component;
    private boolean            _firstStretch;
    private boolean            _active;

    public Rubberband() {
        _anchor = new Point( 0, 0 );
        _stretched = new Point( 0, 0 );
        _last = new Point( 0, 0 );
        _end = new Point( 0, 0 );
        _firstStretch = true;
        _active = false;
    }

    public Rubberband( final Component c ) {
        _anchor = new Point( 0, 0 );
        _stretched = new Point( 0, 0 );
        _last = new Point( 0, 0 );
        _end = new Point( 0, 0 );
        _firstStretch = true;
        _active = false;
        setComponent( c );
    }

    public void anchor( final Point p ) {
        _firstStretch = true;
        _anchor.x = p.x;
        _anchor.y = p.y;
        _stretched.x = _last.x = _anchor.x;
        _stretched.y = _last.y = _anchor.y;
    }

    public abstract void drawLast( Graphics g );

    public abstract void drawNext( Graphics g );

    public void end( final Point p ) {
        _last.x = _end.x = p.x;
        _last.y = _end.y = p.y;
        final Graphics g = _component.getGraphics();
        if ( g != null ) {
            try {
                g.setXORMode( _component.getBackground() );
                drawLast( g );
            }
            finally {
                g.dispose();
            }
        }
    }

    public Point getAnchor() {
        return _anchor;
    }

    public Rectangle getBounds() {
        return new Rectangle( _stretched.x >= _anchor.x ? _anchor.x
                : _stretched.x, _stretched.y >= _anchor.y ? _anchor.y
                : _stretched.y, Math.abs( _stretched.x - _anchor.x ), Math
                .abs( _stretched.y - _anchor.y ) );
    }

    public Point getEnd() {
        return _end;
    }

    public Point getLast() {
        return _last;
    }

    public Point getStretched() {
        return _stretched;
    }

    public boolean isActive() {
        return _active;
    }

    public Rectangle lastBounds() {
        return new Rectangle( _last.x >= _anchor.x ? _anchor.x : _last.x,
                _last.y >= _anchor.y ? _anchor.y : _last.y, Math.abs( _last.x
                        - _anchor.x ), Math.abs( _last.y - _anchor.y ) );
    }

    public void setActive( final boolean b ) {
        _active = b;
    }

    public void setComponent( final Component c ) {
        _component = c;
    }

    public void stretch( final Point p ) {
        _last.x = _stretched.x;
        _last.y = _stretched.y;
        _stretched.x = p.x;
        _stretched.y = p.y;
        final Graphics g = _component.getGraphics();
        if ( g != null ) {
            try {
                g.setXORMode( Rubberband.XOR_COLOR );
                if ( _firstStretch ) {
                    _firstStretch = false;
                }
                else {
                    drawLast( g );
                }
                drawNext( g );
            }
            catch ( final Exception ex ) {
                ex.printStackTrace();
            }
            finally {
                g.dispose();
            }
        }
    }
}
