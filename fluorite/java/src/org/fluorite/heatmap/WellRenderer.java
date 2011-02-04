// $Id: WellRenderer.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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
import java.awt.RenderingHints;

import org.fluorite.heatmap.data.WellData;

public class WellRenderer extends AbstractRenderer {

    static final Color          EMPTY_COLOR          = new Color( 250, 0, 250 );
    static final Color          POSITIVE_COLOR       = new Color( 250, 0, 250 );
    static final Color          NEGATIVE_COLOR       = new Color( 250, 0, 250 );
    static final Color          NULL_COLOR           = new Color( 50, 50, 50 );
    static final int            DISTANCE_OVAL_BORDER = 1;
    static final int            SIZE_LIMIT           = 7;
    /**
     * 
     */
    private static final long   serialVersionUID     = -2331160296913478874L;
    private final double        _value;
    private final String        _name;
    private final String        _description;
    private final String        _id;
    private Color               _wellColor;
    private boolean             _isMarked;
    private boolean             _isSelected;
    private boolean             _isFlaggingStatusChanged;
    private boolean             _isNegativeControl;
    private boolean             _isPositiveControl;
    private boolean             _isEmptyControl;
    private boolean             _isUserFlagged;
    private final boolean       _isFlaggedByStatisticalAnalysis;
    private final boolean       _isEmpty;
    private final PlateRenderer _parentPlateRenderer;

    public WellRenderer( final double value, final String name,
            final String description, final String id,
            final boolean isUserFlagged,
            final boolean isFlaggedByStatisticalAnalysis,
            final boolean isNegativeControl, final boolean isPositiveControl,
            final boolean isEmptyControl, final boolean isEmpty,
            final PlateRenderer parentPlateRenderer ) {
        _value = value;
        _name = name;
        _description = description;
        _id = id;
        _isFlaggedByStatisticalAnalysis = isFlaggedByStatisticalAnalysis;
        _parentPlateRenderer = parentPlateRenderer;
        _isEmpty = isEmpty;
        setIsSelected( false );
        setIsUserFlagged( isUserFlagged );
        setIsFlaggingStatusChanged( false );
        setIsMarked( false );
        setIsNegativeControl( isNegativeControl );
        setIsPositiveControl( isPositiveControl );
        setIsEmptyControl( isEmptyControl );
        setStatus( ( byte ) 0 );
    }

    public WellRenderer( final WellData wellData,
            final PlateRenderer parentPlateRenderer ) {
        this( wellData.getValue(), wellData.getName(), wellData
                .getDescription(), wellData.getID(), wellData.isUserFlagged(),
                wellData.isFlaggedByStatisticalAnalysis(), wellData
                        .isNegativeControl(), wellData.isPositiveControl(),
                wellData.isEmptyControl(), wellData.isEmpty(),
                parentPlateRenderer );
    }

    private double calcFactor( final double min, final double max ) {
        return ( max - min ) / 255D;
    }

    private Color calcWellColor( double value, final double min,
            final double max, final Color minColor, final Color maxColor ) {
        if ( isEmpty() ) {
            return WellRenderer.NULL_COLOR;
        }
        if ( value < min ) {
            value = min;
        }
        if ( value > max ) {
            value = max;
        }
        final double x = ( 255D * ( value - min ) ) / ( max - min );
        final int red = ( int ) ( minColor.getRed() + x
                * calcFactor( minColor.getRed(), maxColor.getRed() ) );
        final int green = ( int ) ( minColor.getGreen() + x
                * calcFactor( minColor.getGreen(), maxColor.getGreen() ) );
        final int blue = ( int ) ( minColor.getBlue() + x
                * calcFactor( minColor.getBlue(), maxColor.getBlue() ) );
        return new Color( red, green, blue );
    }

    private Color calcWellColor( double value, final double min,
            final double max, final double mean, final Color minColor,
            final Color maxColor, final Color meanColor ) {
        if ( isEmpty() ) {
            return WellRenderer.NULL_COLOR;
        }
        if ( meanColor == null ) {
            return calcWellColor( value, min, max, minColor, maxColor );
        }
        if ( value < min ) {
            value = min;
        }
        if ( value > max ) {
            value = max;
        }
        if ( value < mean ) {
            final double x = ( 255D * ( value - min ) ) / ( mean - min );
            final int red = ( int ) ( minColor.getRed() + x
                    * calcFactor( minColor.getRed(), meanColor.getRed() ) );
            final int green = ( int ) ( minColor.getGreen() + x
                    * calcFactor( minColor.getGreen(), meanColor.getGreen() ) );
            final int blue = ( int ) ( minColor.getBlue() + x
                    * calcFactor( minColor.getBlue(), meanColor.getBlue() ) );
            return new Color( red, green, blue );
        }
        if ( value > mean ) {
            final double x = ( 255D * ( value - mean ) ) / ( max - mean );
            final int red = ( int ) ( meanColor.getRed() + x
                    * calcFactor( meanColor.getRed(), maxColor.getRed() ) );
            final int green = ( int ) ( meanColor.getGreen() + x
                    * calcFactor( meanColor.getGreen(), maxColor.getGreen() ) );
            final int blue = ( int ) ( meanColor.getBlue() + x
                    * calcFactor( meanColor.getBlue(), maxColor.getBlue() ) );
            return new Color( red, green, blue );
        }
        else {
            return meanColor;
        }
    }

    public String getDataDescription() {
        return _description;
    }

    public String getDataID() {
        return _id;
    }

    public String getDataName() {
        return _name;
    }

    public double getDataValue() {
        return _value;
    }

    @Override
    public PlateRenderer getParentPlateRenderer() {
        return _parentPlateRenderer;
    }

    public Color getWellColor() {
        return _wellColor;
    }

    public boolean isEmpty() {
        return _isEmpty;
    }

    public boolean isEmptyControl() {
        return _isEmptyControl;
    }

    public boolean isFlaggedByStatisticalAnalysis() {
        return _isFlaggedByStatisticalAnalysis;
    }

    public boolean isFlaggingStatusChanged() {
        return _isFlaggingStatusChanged;
    }

    public boolean isMarked() {
        return _isMarked;
    }

    public boolean isNegativeControl() {
        return _isNegativeControl;
    }

    public boolean isPositiveControl() {
        return _isPositiveControl;
    }

    @Override
    public boolean isSelected() {
        return _isSelected;
    }

    public boolean isUserFlagged() {
        return _isUserFlagged;
    }

    @Override
    public void paint( final Graphics g ) {
        final int width = getWellSize() - 1;
        final int width_ = width - 1;
        final int width__ = ( width_ - 1 ) + 1;
        final int width__s = width__ - 2;
        final int x_ = getX() + 1;
        final int y_ = getY() + 1;
        final PlateDisplayPanel hmp = getParentPlateRenderer()
                .getPlateDisplayPanel();
        boolean draw_circle = hmp.isDrawCircle()
                || ( !hmp.isDrawCircle() && !hmp.isDrawSquare() && ( width > 7 ) );
        final boolean show_user_flags = _parentPlateRenderer
                .getPlateDisplayPanel().showUserFlagsCheckBox.isSelected();
        final boolean show_outlier_flags = _parentPlateRenderer
                .getPlateDisplayPanel().showOutliersCheckBox.isSelected();
        // final boolean show_hit_picks = _parentPlateRenderer
        // .getPlateDisplayPanel().showHitPicksCheckBox.isSelected();
        // final boolean show_confirmed_hits = _parentPlateRenderer
        // .getPlateDisplayPanel().showConfirmedHitsCheckBox.isSelected();
        final Graphics2D g2 = ( Graphics2D ) g;
        g2.setRenderingHint( RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED );
        if ( isMarked() ) {
            g2.setColor( AbstractRenderer.MARKED_COLOR );
        }
        else if ( !draw_circle && isSelected() ) {
            g2.setColor( AbstractRenderer.SELECTED_COLOR );
        }
        else {
            g2.setColor( AbstractRenderer.DEFAULT_COLOR );
        }
        g2.drawRect( getX(), getY(), width, width );
        if ( draw_circle ) {
            if ( isSelected() && isMarked() ) {
                g2.setColor( AbstractRenderer.MARKED_COLOR );
            }
            else if ( isSelected() ) {
                g2.setColor( AbstractRenderer.SELECTED_COLOR );
            }
            else {
                g2.setColor( AbstractRenderer.DEFAULT_COLOR );
            }
            g2.fillRect( x_, y_, width_, width_ );
        }
        g2.setColor( getWellColor() );
        if ( draw_circle ) {
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON );
            if ( isSelected() && ( width > 6 ) ) {
                g2.fillOval( getX() + 2, getY() + 2, width__s, width__s );
            }
            else if ( width < 5 ) {
                g2.fillOval( ( getX() + 1 ) - 1, ( getY() + 1 ) - 1,
                        width__ + 2, width__ + 2 );
            }
            else {
                g2.fillOval( getX() + 1, getY() + 1, width__, width__ );
            }
        }
        else if ( isMarked() || isSelected() ) {
            g2.fillRect( getX() + 1, getY() + 1, width_, width_ );
        }
        else {
            g2.fillRect( ( getX() + 1 ) - 1, ( getY() + 1 ) - 1, width_ + 2,
                    width_ + 2 );
        }
        if ( isUserFlagged() && isFlaggedByStatisticalAnalysis()
                && show_user_flags && show_outlier_flags ) {
            if ( isMarked() ) {
                g2.setColor( AbstractRenderer.MARKED_COLOR );
            }
            else if ( isSelected() ) {
                g2.setColor( AbstractRenderer.SELECTED_COLOR );
            }
            g2.drawLine( x_, y_, getX() + width_, getY() + width_ );
            g2.drawLine( x_, getY() + width_, getX() + width_, y_ );
        }
        else if ( isUserFlagged() && show_user_flags ) {
            if ( isMarked() ) {
                g2.setColor( AbstractRenderer.MARKED_COLOR );
            }
            else if ( isSelected() ) {
                g2.setColor( AbstractRenderer.SELECTED_COLOR );
            }
            else {
                g2.setColor( AbstractRenderer.USER_FLAGGED_COLOR );
            }
            g2.drawLine( x_, y_, getX() + width_, getY() + width_ );
            g2.drawLine( x_, getY() + width_, getX() + width_, y_ );
        }
        else if ( isFlaggedByStatisticalAnalysis() && show_outlier_flags ) {
            if ( isMarked() ) {
                g2.setColor( AbstractRenderer.MARKED_COLOR );
            }
            else if ( isSelected() ) {
                g2.setColor( AbstractRenderer.SELECTED_COLOR );
            }
            g2.drawLine( x_, y_, getX() + width_, getY() + width_ );
        }
        if ( hmp.isShowControls() ) {
            if ( isEmptyControl() ) {
                g2.setColor( WellRenderer.EMPTY_COLOR );
                g2.setFont( hmp.getControlFont() );
                g2.drawString( "E", getX() + 4, getY() + width );
            }
            else if ( isNegativeControl() ) {
                g2.setColor( WellRenderer.NEGATIVE_COLOR );
                g2.setFont( hmp.getControlFont() );
                g2.drawString( "N", getX() + 4, getY() + width );
            }
            else if ( isPositiveControl() ) {
                g2.setColor( WellRenderer.POSITIVE_COLOR );
                g2.setFont( hmp.getControlFont() );
                g2.drawString( "P", getX() + 4, getY() + width );
            }
        }
    }

    public void resetWellColor( final double min, final double max,
            final Color minColor, final Color maxColor ) {
        setWellColor( calcWellColor( getDataValue(), min, max, minColor,
                maxColor ) );
    }

    public void resetWellColor( final double min, final double max,
            final double mean, final Color minColor, final Color maxColor,
            final Color meanColor ) {
        setWellColor( calcWellColor( getDataValue(), min, max, mean, minColor,
                maxColor, meanColor ) );
    }

    public void setIsEmptyControl( final boolean isEmptyControl ) {
        _isEmptyControl = isEmptyControl;
        if ( isEmptyControl ) {
            _isNegativeControl = false;
            _isPositiveControl = false;
        }
    }

    public void setIsFlaggingStatusChanged(
            final boolean isFlaggingStatusChanged ) {
        _isFlaggingStatusChanged = isFlaggingStatusChanged;
    }

    public void setIsMarked( final boolean isMarked ) {
        _isMarked = isMarked;
    }

    public void setIsNegativeControl( final boolean isNegativeControl ) {
        _isNegativeControl = isNegativeControl;
        if ( isNegativeControl ) {
            _isEmptyControl = false;
            _isPositiveControl = false;
        }
    }

    public void setIsPositiveControl( final boolean isPositiveControl ) {
        _isPositiveControl = isPositiveControl;
        if ( isPositiveControl ) {
            _isEmptyControl = false;
            _isNegativeControl = false;
        }
    }

    @Override
    public void setIsSelected( final boolean isSelected ) {
        _isSelected = isSelected;
    }

    public void setIsUserFlagged( final boolean isUserFlagged ) {
        if ( _isUserFlagged != isUserFlagged ) {
            _isUserFlagged = isUserFlagged;
            setIsFlaggingStatusChanged( true );
        }
    }

    private void setWellColor( final Color wellColor ) {
        _wellColor = wellColor;
    }
}
