// $Id: PlateRenderer.java,v 1.4 2010/12/13 18:59:25 cmzmasek Exp $
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

import org.fluorite.heatmap.data.PlateData;
import org.fluorite.heatmap.data.WellData;
import org.fluorite.heatmap.rubberband.Rubberband;
import org.fluorite.heatmap.rubberband.RubberbandRectangle;

public class PlateRenderer extends JComponent {

    static final byte         SELECT_ALL       = 0;
    static final byte         UNSELECT_ALL     = 1;
    static final byte         FIRST_QUARTER    = 2;
    static final byte         SECOND_QUARTER   = 3;
    static final byte         THIRD_QUARTER    = 4;
    static final byte         FOURTH_QUARTER   = 5;
    /**
     * 
     */
    private static final long serialVersionUID = -68078011081748093L;

    public static boolean isMouseEventAltered( final MouseEvent event ) {
        return event.isShiftDown() || event.isAltDown()
                || event.isControlDown() || event.isAltGraphDown()
                || event.isMetaDown();
    }
    private final PlateDisplayPanel _heatMapPanel;
    private final PlateData         _platedata;
    private final int               _rows;
    private final int               _columns;
    private final int               _my_row;
    private int                     _wellSize;
    private AbstractRenderer        _wells[][];
    private double                  _min;
    private double                  _max;
    private double                  _mean;
    private Color                   _minColor;
    private Color                   _maxColor;
    private Color                   _meanColor;
    private boolean                 _useMean;
    private Rubberband              _rubberband;

    public PlateRenderer( final PlateData platedata, final int wellSize,
            final double min, final double max, final double mean,
            final Color minColor, final Color maxColor, final Color meanColor,
            final PlateDisplayPanel heatMapPanel, final int my_row ) {
        _heatMapPanel = heatMapPanel;
        _platedata = platedata;
        _rows = platedata.geRows();
        _columns = platedata.getColumns();
        _my_row = my_row;
        setMin( min );
        setMax( max );
        setMean( mean );
        setMinColor( minColor );
        setMaxColor( maxColor );
        setMeanColor( meanColor );
        if ( meanColor == null ) {
            setUseMean( false );
        }
        else {
            setUseMean( true );
        }
        setWellSize( wellSize );
        addMouseListeners();
        initializeWells();
        setRubberband( new RubberbandRectangle( this ) );
    }

    private void addMouseListeners() {
        addMouseMotionListener( new MouseMotionAdapter() {

            @Override
            public void mouseDragged( final MouseEvent event ) {
                if ( ( ( event.getModifiers() & 0x10 ) != 0 )
                        && getRubberband().isActive()
                        && !PlateRenderer.isMouseEventAltered( event ) ) {
                    getRubberband().stretch( event.getPoint() );
                }
            }
        } );
        addMouseListener( new MouseAdapter() {

            @Override
            public void mouseClicked( final MouseEvent event ) {
                if ( ( ( event.getModifiers() & 4 ) != 0 )
                        || PlateRenderer.isMouseEventAltered( event ) ) {
                    getInfo( new Rectangle( event.getX(), event.getY(), 1, 1 ) );
                }
                else {
                    changeSelected( new Rectangle( event.getX(), event.getY(),
                            1, 1 ), true );
                    event.consume();
                }
            }

            @Override
            public void mousePressed( final MouseEvent event ) {
                if ( ( ( event.getModifiers() & 4 ) != 0 )
                        || PlateRenderer.isMouseEventAltered( event ) ) {
                    getInfo( new Rectangle( event.getX(), event.getY(), 1, 1 ) );
                }
                if ( ( ( event.getModifiers() & 0x10 ) != 0 )
                        && getRubberband().isActive() ) {
                    getRubberband().anchor( event.getPoint() );
                }
            }

            @Override
            public void mouseReleased( final MouseEvent event ) {
                if ( ( ( event.getModifiers() & 0x10 ) != 0 )
                        && getRubberband().isActive() ) {
                    getRubberband().end( event.getPoint() );
                    rubberbandEnded( getRubberband() );
                }
            }
        } );
    }

    public double calculateMaxValueOfWells( boolean ignoreFlagged,
            boolean ignoreControls ) {
        double max = 4.9406564584124654E-324D;
        double value = 0.0D;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( !wr.isEmpty()
                        && ( !ignoreFlagged || !wr.isUserFlagged() )
                        && ( !ignoreControls || ( !wr.isPositiveControl()
                                && !wr.isNegativeControl() && !wr
                                .isEmptyControl() ) ) ) {
                    value = wr.getDataValue();
                    if ( value > max ) {
                        max = value;
                    }
                }
            }
        }
        return max;
    }

    public double calculateMinValueOfWells( boolean ignoreFlagged,
            boolean ignoreControls ) {
        double min = 1.7976931348623157E+308D;
        double value = 0.0D;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( !wr.isEmpty()
                        && ( !ignoreFlagged || !wr.isUserFlagged() )
                        && ( !ignoreControls || ( !wr.isPositiveControl()
                                && !wr.isNegativeControl() && !wr
                                .isEmptyControl() ) ) ) {
                    value = wr.getDataValue();
                    if ( value < min ) {
                        min = value;
                    }
                }
            }
        }
        return min;
    }

    public int calculateNumberOWells( boolean ignoreFlagged,
            boolean ignoreControls ) {
        int n = 0;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( !wr.isEmpty()
                        && ( !ignoreFlagged || !wr.isUserFlagged() )
                        && ( !ignoreControls || ( !wr.isPositiveControl()
                                && !wr.isNegativeControl() && !wr
                                .isEmptyControl() ) ) ) {
                    n++;
                }
            }
        }
        return n;
    }

    public double calculateSumOfValuesOfWells( boolean ignoreFlagged,
            boolean ignoreControls ) {
        double sum = 0.0D;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( !wr.isEmpty()
                        && ( !ignoreFlagged || !wr.isUserFlagged() )
                        && ( !ignoreControls || ( !wr.isPositiveControl()
                                && !wr.isNegativeControl() && !wr
                                .isEmptyControl() ) ) ) {
                    sum += wr.getDataValue();
                }
            }
        }
        return sum;
    }

    private void changeSelected( final Rectangle selected, final boolean clicked ) {
        final int selected_height_ = ( int ) ( selected.getY() + selected
                .getHeight() );
        final int selected_width_ = ( int ) ( selected.getX() + selected
                .getWidth() );
        final int well_width_ = getAbstractRenderer( 0, 0 ).getWellSize() - 1;
        final boolean selected_not_empty = ( selected.getHeight() > 1.0D )
                || ( selected.getWidth() > 1.0D );
        final Rectangle rect = new Rectangle( 0, 0, well_width_, well_width_ );
        AbstractRenderer rend = null;
        for( int row = 0; row < getRows() + 1; row++ ) {
            for( int col = 0; col < getColumns() + 1; col++ ) {
                rend = getAbstractRenderer( row, col );
                if ( rend.getY() > selected_height_ ) {
                    return;
                }
                if ( rend.getX() > selected_width_ ) {
                    break;
                }
                rect.setLocation( rend.getX(), rend.getY() );
                if ( rect.intersects( selected ) ) {
                    if ( clicked
                            && ( ( row == getRows() ) || ( col == getColumns() ) ) ) {
                        if ( ( row == getRows() ) && ( col == getColumns() ) ) {
                            if ( rend.getStatus() == 0 ) {
                                rend.setStatus( ( byte ) 2 );
                                setIsSelectedOfAll( true );
                            }
                            else if ( rend.getStatus() == 2 ) {
                                rend.setStatus( ( byte ) 3 );
                                setIsSelectedToQuarter( 1 );
                            }
                            else if ( rend.getStatus() == 3 ) {
                                rend.setStatus( ( byte ) 4 );
                                setIsSelectedToQuarter( 2 );
                            }
                            else if ( rend.getStatus() == 4 ) {
                                rend.setStatus( ( byte ) 5 );
                                setIsSelectedToQuarter( 3 );
                            }
                            else if ( rend.getStatus() == 5 ) {
                                rend.setStatus( ( byte ) 1 );
                                setIsSelectedToQuarter( 4 );
                            }
                            else {
                                rend.setStatus( ( byte ) 0 );
                                setIsSelectedOfAll( false );
                            }
                        }
                        else if ( row == getRows() ) {
                            if ( rend.isSelected() ) {
                                setIsSelectedOfColumn( col, false );
                            }
                            else {
                                setIsSelectedOfColumn( col, true );
                            }
                        }
                        else if ( rend.isSelected() ) {
                            setIsSelectedOfRow( row, false );
                        }
                        else {
                            setIsSelectedOfRow( row, true );
                        }
                        return;
                    }
                    if ( clicked || selected_not_empty ) {
                        if ( rend.isSelected() ) {
                            rend.setIsSelected( false );
                        }
                        else {
                            rend.setIsSelected( true );
                        }
                        if ( clicked ) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public int findString( final String ss, final boolean inAccession,
            final boolean inName, final boolean inDescpription,
            final boolean exactMatch ) {
        int counter = 0;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( !wr.isEmpty() ) {
                    if ( exactMatch ) {
                        if ( ( inAccession && wr.getDataID().toUpperCase()
                                .trim().equals( ss ) )
                                || ( inName && wr.getDataName().toUpperCase()
                                        .trim().equals( ss ) )
                                || ( inDescpription && wr.getDataDescription()
                                        .toUpperCase().trim().equals( ss ) ) ) {
                            wr.setIsMarked( true );
                            counter++;
                        }
                        else {
                            wr.setIsMarked( false );
                        }
                    }
                    else if ( ( inAccession && ( wr.getDataID().toUpperCase()
                            .trim().indexOf( ss ) >= 0 ) )
                            || ( inName && ( wr.getDataName().toUpperCase()
                                    .trim().indexOf( ss ) >= 0 ) )
                            || ( inDescpription && ( wr.getDataDescription()
                                    .toUpperCase().trim().indexOf( ss ) >= 0 ) ) ) {
                        wr.setIsMarked( true );
                        counter++;
                    }
                    else {
                        wr.setIsMarked( false );
                    }
                }
            }
        }
        return counter;
    }

    public AbstractRenderer getAbstractRenderer( final int row, final int col ) {
        return _wells[ row ][ col ];
    }

    public int getColumns() {
        return _columns;
    }

    private void getInfo( final Rectangle selected ) {
        final int well_width_ = getAbstractRenderer( 0, 0 ).getWellSize() - 1;
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer rend = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                final Rectangle rect = new Rectangle( rend.getX(), rend.getY(),
                        well_width_, well_width_ );
                if ( rect.intersects( selected ) ) {
                    getPlateDisplayPanel().updateInfo(
                            rend.getDataValue() + "", rend.getDataID(),
                            rend.getDataName(), rend.getDataDescription() );
                    if ( !getPlateDisplayPanel().isResultsOfSearchShowing() ) {
                        getPlateDisplayPanel().setMarked( getMyRow(), row, col );
                    }
                    return;
                }
            }
        }
    }

    private double getMax() {
        return _max;
    }

    private Color getMaxColor() {
        return _maxColor;
    }

    private double getMean() {
        return _mean;
    }

    private Color getMeanColor() {
        return _meanColor;
    }

    private double getMin() {
        return _min;
    }

    private Color getMinColor() {
        return _minColor;
    }

    public int getMyRow() {
        return _my_row;
    }

    public PlateData getPlateData() {
        return _platedata;
    }

    public PlateDisplayPanel getPlateDisplayPanel() {
        return _heatMapPanel;
    }

    @Override
    public Dimension getPreferredSize() {
        final int width = ( getWellSize() + 1 ) * ( getColumns() + 1 );
        final int hight = ( getWellSize() + 1 ) * ( getRows() + 1 ) + 30;
        return new Dimension( width, hight );
    }

    public int getRows() {
        return _rows;
    }

    private Rubberband getRubberband() {
        return _rubberband;
    }

    public String getWellInformationAsString( boolean outliers,
            boolean not_outliers, boolean flagged, boolean not_flagged,
            boolean selected ) {
        final StringBuffer sb = new StringBuffer();
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( ( !outliers || wr.isFlaggedByStatisticalAnalysis() )
                        && ( !not_outliers || !wr
                                .isFlaggedByStatisticalAnalysis() )
                        && ( !flagged || wr.isUserFlagged() )
                        && ( !not_flagged || !wr.isUserFlagged() )
                        && ( !selected || wr.isSelected() ) ) {
                    sb.append( PlateData.ALPHABET[ row ] );
                    sb.append( col + 1 );
                    sb.append( ": " );
                    sb.append( wr.getDataID() );
                    sb.append( ": " );
                    sb.append( wr.getDataName() );
                    sb.append( ": " );
                    sb.append( wr.getDataDescription() );
                    sb.append( " [" );
                    sb.append( wr.getDataValue() );
                    sb.append( "]" );
                    sb.append( "\n" );
                }
            }
        }
        return sb.toString();
    }

    private int getWellSize() {
        return _wellSize;
    }

    private void initializeWells() {
        _wells = new AbstractRenderer[ getRows() + 1 ][ getColumns() + 1 ];
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns() + 1; col++ ) {
                AbstractRenderer r;
                if ( col == getColumns() ) {
                    r = new LabelRenderer( PlateData.ALPHABET[ row
                            % PlateData.ALPHABET.length ]
                            + "", this );
                }
                else if ( getPlateData().getData( row, col ) == null ) {
                    r = new WellRenderer( new WellData(), this );
                }
                else {
                    r = new WellRenderer( getPlateData().getData( row, col ),
                            this );
                }
                r.setVisible( true );
                setAbstractRenderer( r, row, col );
            }
        }
        for( int col = 0; col < getColumns() + 1; col++ ) {
            AbstractRenderer r;
            if ( col == getColumns() ) {
                r = new LabelRenderer( "", this );
            }
            else {
                r = new LabelRenderer( ( col + 1 ) + "", this );
            }
            r.setVisible( true );
            setAbstractRenderer( r, getRows(), col );
        }
    }

    public void inverseMarkedOfWell( final int well_row, final int well_col ) {
        final WellRenderer rend = ( WellRenderer ) getAbstractRenderer(
                well_row, well_col );
        if ( rend.isMarked() ) {
            rend.setIsMarked( false );
        }
        else {
            rend.setIsMarked( true );
        }
    }

    private boolean isUseMean() {
        return _useMean;
    }

    @Override
    public void paint( final Graphics g ) {
        g.setColor( Color.white );
        g.setFont( getPlateDisplayPanel().getPlateTitleFont() );
        g.drawString( "Number:" + getPlateData().getName() + " Replicate:"
                + ( getPlateData().getReplicateNumber() + 1 ), 10, 20 );
        for( int row = 0; row < getRows() + 1; row++ ) {
            for( int col = 0; col < getColumns() + 1; col++ ) {
                getAbstractRenderer( row, col ).paint( g );
            }
        }
    }

    public void resetWellColors() {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer r = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( isUseMean() ) {
                    r.resetWellColor( getMin(), getMax(), getMean(),
                            getMinColor(), getMaxColor(), getMeanColor() );
                }
                else {
                    r.resetWellColor( getMin(), getMax(), getMinColor(),
                            getMaxColor() );
                }
            }
        }
    }

    public void resetWellSize( final int well_size ) {
        setWellSize( well_size );
        final int factor = well_size + 0;
        for( int row = 0; row < getRows() + 1; row++ ) {
            for( int col = 0; col < getColumns() + 1; col++ ) {
                final AbstractRenderer r = getAbstractRenderer( row, col );
                r.setX( 10 + factor * col );
                r.setY( factor * row + 30 );
                r.setWellSize( well_size );
            }
        }
    }

    private void rubberbandEnded( final Rubberband rb ) {
        changeSelected( rb.getBounds(), false );
        repaint();
    }

    private void setAbstractRenderer( final AbstractRenderer ar, final int row,
            final int col ) {
        _wells[ row ][ col ] = ar;
    }

    public void setFlaggedStatusOfOutlierWells(
            final boolean set_flagged_to_this ) {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( wr.isFlaggedByStatisticalAnalysis() ) {
                    wr.setIsUserFlagged( set_flagged_to_this );
                }
            }
        }
    }

    public void setFlaggedStatusOfSelectedWells(
            final boolean set_flagged_to_this ) {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                if ( wr.isSelected() ) {
                    wr.setIsUserFlagged( set_flagged_to_this );
                    wr.setIsSelected( false );
                }
            }
        }
    }

    public void setIsFlaggingStatusChangedToFalse() {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                wr.setIsFlaggingStatusChanged( false );
            }
        }
    }

    private void setIsSelectedOfAll( final boolean isSelected ) {
        for( int col = 0; col < getColumns() + 1; col++ ) {
            setIsSelectedOfColumn( col, isSelected );
        }
    }

    private void setIsSelectedOfColumn( final int column,
            final boolean isSelected ) {
        for( int row = 0; row < getRows() + 1; row++ ) {
            getAbstractRenderer( row, column ).setIsSelected( isSelected );
        }
    }

    private void setIsSelectedOfRow( final int row, final boolean isSelected ) {
        for( int col = 0; col < getColumns() + 1; col++ ) {
            getAbstractRenderer( row, col ).setIsSelected( isSelected );
        }
    }

    private void setIsSelectedOfRowAlternating( final int row,
            final boolean even ) {
        boolean selected = even;
        for( int col = 0; col < getColumns(); col++ ) {
            getAbstractRenderer( row, col ).setIsSelected( selected );
            selected = !selected;
        }
    }

    private void setIsSelectedToQuarter( final int quarter ) {
        boolean evenRow = false;
        boolean evenColumn = false;
        if ( quarter <= 1 ) {
            evenRow = true;
            evenColumn = true;
        }
        else if ( quarter == 2 ) {
            evenColumn = true;
        }
        else if ( quarter == 3 ) {
            evenRow = true;
        }
        for( int row = 0; row < getRows(); row++ ) {
            if ( evenColumn ) {
                setIsSelectedOfRowAlternating( row, evenRow );
            }
            else {
                setIsSelectedOfRow( row, false );
            }
            evenColumn = !evenColumn;
        }
    }

    public void setMarkedOfAllWellsToFalse() {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer rend = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                rend.setIsMarked( false );
            }
        }
    }

    public void setMax( final double max ) {
        _max = max;
    }

    void setMaxColor( final Color maxColor ) {
        _maxColor = maxColor;
    }

    void setMean( final double mean ) {
        _mean = mean;
    }

    public void setMeanColor( final Color meanColor ) {
        _meanColor = meanColor;
    }

    public void setMin( final double min ) {
        _min = min;
    }

    void setMinColor( final Color minColor ) {
        _minColor = minColor;
    }

    private void setRubberband( final Rubberband rb ) {
        if ( _rubberband != null ) {
            _rubberband.setActive( false );
        }
        _rubberband = rb;
        if ( _rubberband != null ) {
            _rubberband.setComponent( this );
            _rubberband.setActive( true );
        }
    }

    public void setUseMean( final boolean useMean ) {
        _useMean = useMean;
    }

    private void setWellSize( final int wellSize ) {
        _wellSize = wellSize;
    }

    public void unSelectUnMarkAll() {
        for( int row = 0; row < getRows(); row++ ) {
            for( int col = 0; col < getColumns(); col++ ) {
                final WellRenderer wr = ( WellRenderer ) getAbstractRenderer(
                        row, col );
                wr.setIsSelected( false );
                wr.setIsMarked( false );
            }
        }
    }
}
