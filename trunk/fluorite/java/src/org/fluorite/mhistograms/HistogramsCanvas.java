// $Id: HistogramsCanvas.java,v 1.7 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.mhistograms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JComponent;

public class HistogramsCanvas extends JComponent {

    public static final int      WIDTH                       = 880;
    public static final int      HEIGTH                      = 400;
    public final static int      COLOR_SLIDER_MEAN           = 250;
    public final static int      COLOR_SLIDER_MIN            = 0;
    public final static int      COLOR_SLIDER_MAX            = 500;
    /**
     * 
     */
    private static final long    serialVersionUID            = 6429964263561933755L;
    private final static boolean COLOR_CUMULATIVE_PERCENTAGE = false;
    private final static boolean SHOW_LABELS                 = true;
    private final static boolean SHOW_NEGATIVE_CONTROLS      = false;
    private final static boolean SHOW_POSITIVE_CONTROLS      = false;
    private final static boolean LINE_UP_VALUES              = true;
    private final static boolean LINE_UP_PERCENTAGES         = false;
    private final static boolean THREE_COLORS                = false;                                        // Three
    // or
    // two
    // color
    // schema
    // for
    // color
    // percentage.
    // Colors for counts is always in two color schema.
    private final static boolean DRAW_HISTOGRAM              = true;                                         // Draw
    // a
    // histogram.
    private final static boolean DRAW_COLORIZED_BAND         = false;                                        // Draw
    // a
    // colored
    // band.
    private final static boolean DRAW_LINE                   = true;                                         // Indicate
    // the
    // hight
    // of
    // the
    // histogram
    // with
    // a
    // line.
    private final static boolean COLOR_COUNTS                = true;
    private final static boolean COLOR_PERCENTAGE            = false;
    private final static boolean SHOW_GENES                  = true;
    private final static boolean SHOW_GENE_NAMES             = false;
    private final static double  MAGNIFICATION               = 1.0;
    private final static double  COLOR_MAGNIFICATION         = 250.0;
    private final static boolean NORMALIZE_TO_MIN_COUNT      = false;
    private final static Color   MIN_COLOR                   = new Color( 0, 0,
                                                                     255 );
    private final static Color   MAX_COLOR                   = new Color( 255,
                                                                     255, 0 );
    private final static Color   MEAN_COLOR                  = new Color( 255,
                                                                     0, 0 );
    private final static Color   LINE_COLOR                  = new Color( 0,
                                                                     255, 0 );
    private final static Color   BACKGROUND_COLOR            = new Color( 0, 0,
                                                                     0 );
    private final static Color   VALUES_COLOR                = new Color( 255,
                                                                     255, 255 );
    private final static int     TOWARDS_WHITE               = 50;
    private final static int     COLOR_PERCENTAGE_MAX        = 50;
    private final static int     COLOR_PERCENTAGE_MIN        = 0;
    private final static int     COLOR_CUMUL_MAX             = 100;
    private final static int     COLOR_CUMUL_MIN             = 0;
    private final static int     COLOR_PERCENTAGE_FAKTOR_3   = HistogramsCanvas.COLOR_SLIDER_MAX
                                                                     / HistogramsCanvas.COLOR_PERCENTAGE_MAX;
    private final static int     COLOR_PERCENTAGE_FAKTOR_2   = HistogramsCanvas.COLOR_SLIDER_MEAN
                                                                     / HistogramsCanvas.COLOR_PERCENTAGE_MAX;
    private final static int     COLOR_CUMUL_FAKTOR          = HistogramsCanvas.COLOR_SLIDER_MAX
                                                                     / HistogramsCanvas.COLOR_CUMUL_MAX;
    private final static Font    FONT1                       = new Font(
                                                                     "Arial",
                                                                     Font.PLAIN,
                                                                     10 );
    private final static int     STEPS                       = 400;
    private final static int     BORDER                      = 20;
    private HistogramData[]      _hd;
    private boolean[]            _hidden_ones;
    private boolean              _line_up_values;
    private boolean              _line_up_percentages;
    private boolean              _three_colors;
    private boolean              _draw_histogram;
    private boolean              _draw_colorized_band;
    private boolean              _draw_line;
    private boolean              _color_counts;
    private boolean              _color_percentage;
    private boolean              _color_cumulative_percentage;
    private boolean              _show_labels;
    private boolean              _show_positive_controls;
    private boolean              _show_negative_controls;
    private boolean              _show_genes;
    private boolean              _show_gene_names;
    private int                  _bar_width;
    private int                  _gap;
    private double               _magnification;
    private double               _color_magnification;

    /**
     * Constructs a HistogramsCanvas for HistogramData array hd
     * 
     * 
     * @param hd
     *            a HistogramData array
     */
    public HistogramsCanvas( final HistogramData[] hd ) {
        if ( ( hd == null ) || ( hd.length < 1 ) ) {
            System.err
                    .println( "Warning: HistogramsCanvas constructor: HistogramData object is null or empty." );
            return;
        }
        setHistogramData( hd );
        // for testing:
        /*
         * _hd[ 0 ].addGene( "first-1.4-3.975", new Double[]{ new Double(-1.4) ,
         * new Double(3.975)} ); _hd[ 0 ].addGene( "fyn", new Double[]{ new
         * Double(-1.2) , new Double(-1.3)} ); _hd[ 0 ].addGene( "bcl2", new
         * Double[]{ new Double(-1.11) , new Double(-1.22)} ); _hd[ 0 ].addGene(
         * "bclx12", new Double[]{ new Double(2) , new Double(2.6)} );
         * 
         * _hd[ 1 ].addGene( "first-1.4-3.975", new Double[]{ new Double(-0.4) ,
         * new Double(2.975)} ); _hd[ 1 ].addGene( "fyn", new Double[]{ new
         * Double(-1.3) , new Double(-0.9)} ); _hd[ 1 ].addGene( "bcl2", new
         * Double[]{ new Double(-0.3) , new Double(-1.3)} ); _hd[ 1 ].addGene(
         * "bclx12", new Double[]{ new Double(3) , new Double(6)} );
         * 
         * 
         * _hd[ 1 ].addGene( "2nd-0-3", new Double[]{ new Double(-4.5) , new
         * Double(6.9)} ); _hd[ 2 ].addGene( "3rd-1-8.4", new Double[]{ new
         * Double(-0.1375) }); _hd[ 3 ].addGene( "4th", new Double[]{ new
         * Double(1) , new Double(2) , new Double(3) , new Double(4)} );
         * 
         * _hd[ 0 ].setNegativeControlValues( new Double[]{ new Double(-2) , new
         * Double(-3.3), new Double(-2.2) ,new Double(-2.1) } ); _hd[ 0
         * ].setPositiveControlValues( new Double[]{ new Double(1.2) , new
         * Double(1.3) , new Double(1.32) , new Double(1.36)});
         * 
         * _hd[ 4 ].setPositiveControlValues( new Double[]{ new Double(1.1) ,
         * new Double(1.2) , new Double(1.33) , new Double(1.46)});
         */
        init();
    }

    /**
     * This calculates a color. If value is equal to min the returned color is
     * minColor, if value is equal to max the returned color is maxColor,
     * otherwise a color 'proportional' to value is returned.
     * 
     * @param value
     *            the value of this well
     * @param min
     *            the smallest value of the plate/screen
     * @param max
     *            the largest value of the plate/screen
     * @param minColor
     *            the color for min
     * @param maxColor
     *            the color for max
     * @return a Color
     */
    private Color calcColor( double value, final double min, final double max,
            final Color minColor, final Color maxColor ) {
        if ( value < min ) {
            value = min;
        }
        if ( value > max ) {
            value = max;
        }
        final double x = calculateFactor( value, max, min );
        final int red = calculateColorComponent( minColor.getRed(), maxColor
                .getRed(), x );
        final int green = calculateColorComponent( minColor.getGreen(),
                maxColor.getGreen(), x );
        final int blue = calculateColorComponent( minColor.getBlue(), maxColor
                .getBlue(), x );
        return new Color( red, green, blue );
    }

    /**
     * This calculates a color. If value is equal to min the returned color is
     * minColor, if value is equal to max the returned color is maxColor, if
     * value is equal to mean the returned color is meanColor, otherwise a color
     * 'proportional' to value is returned -- either between min-mean or
     * mean-max
     * 
     * @param value
     *            the value of this well
     * @param min
     *            the smallest value of the plate/screen
     * @param max
     *            the largest value of the plate/screen
     * @param mean
     *            the mean/median value of the plate/screen
     * @param minColor
     *            the color for min
     * @param maxColor
     *            the color for max
     * @param meanColor
     *            the color for mean
     * @return a Color
     */
    private Color calcColor( double value, final double min, final double max,
            final double mean, final Color minColor, final Color maxColor,
            final Color meanColor ) {
        if ( value < min ) {
            value = min;
        }
        if ( value > max ) {
            value = max;
        }
        if ( value < mean ) {
            final double x = calculateFactor( value, mean, min );
            final int red = calculateColorComponent( minColor.getRed(),
                    meanColor.getRed(), x );
            final int green = calculateColorComponent( minColor.getGreen(),
                    meanColor.getGreen(), x );
            final int blue = calculateColorComponent( minColor.getBlue(),
                    meanColor.getBlue(), x );
            return new Color( red, green, blue );
        }
        else if ( value > mean ) {
            final double x = calculateFactor( value, max, mean );
            final int red = calculateColorComponent( meanColor.getRed(),
                    maxColor.getRed(), x );
            final int green = calculateColorComponent( meanColor.getGreen(),
                    maxColor.getGreen(), x );
            final int blue = calculateColorComponent( meanColor.getBlue(),
                    maxColor.getBlue(), x );
            return new Color( red, green, blue );
        }
        else {
            return meanColor;
        }
    }

    /**
     * Helper method for calcColor methods.
     * 
     * @param smallercolor_component_x
     *            color component the smaller color
     * @param largercolor_component_x
     *            color component the larger color
     * @param x
     *            factor
     * @return an int representing a color component
     */
    private int calculateColorComponent( final double smallercolor_component_x,
            final double largercolor_component_x, final double x ) {
        return ( int ) ( smallercolor_component_x + ( ( x * ( largercolor_component_x - smallercolor_component_x ) ) / 255.0 ) );
    }

    /**
     * This changes the color of the histogram slightly, when a colored
     * background -- isDrawColorizedBand() is true -- is used.
     * 
     * @param bandcolor
     * @return modified bandcolor
     */
    private Color calculateColorForHistogram( final Color bandcolor ) {
        if ( isDrawColorizedBand() ) {
            if ( isColorPercentage() || isColorCounts()
                    || isColorCumulativePercentage() ) {
                final int red = bandcolor.getRed();
                final int green = bandcolor.getGreen();
                final int blu = bandcolor.getBlue();
                final Color whiter = new Color( towardsWhite( red ),
                        towardsWhite( green ), blu );
                return whiter;
            }
            else {
                return Color.DARK_GRAY;
            }
        }
        else if ( !isColorPercentage() && !isColorCounts()
                && !isColorCumulativePercentage() ) {
            return Color.DARK_GRAY;
        }
        else {
            return bandcolor;
        }
    }

    /**
     * This claculates the color for histograms. The color is dependent on the
     * return value of isColorPercentage(), isColorCumulativePercentage(),
     * isColorCounts(), and on isThreeColors().
     * 
     * @param percentage
     *            used if isColorPercentage() or isColorCumulativePercentage()
     *            is ture.
     * @param counts_for_colorization
     *            used if isColorCounts() is true.
     * @param min
     *            the min count
     * @param max
     *            the max count
     * @param mean
     *            the mean count
     * @return a Color according to percentage or counts
     */
    private Color calculateColorForHistogram( final double percentage,
            final double counts_for_colorization, final double min,
            final double max, final double mean ) {
        Color bandcolor = null;
        if ( isColorPercentage() ) {
            if ( isThreeColors() ) {
                bandcolor = calcColor( Math.abs( percentage
                        - HistogramsCanvas.COLOR_PERCENTAGE_MAX ),
                        HistogramsCanvas.COLOR_PERCENTAGE_MIN,
                        HistogramsCanvas.COLOR_PERCENTAGE_MAX,
                        getColorMagnification()
                                / HistogramsCanvas.COLOR_PERCENTAGE_FAKTOR_3,
                        HistogramsCanvas.MAX_COLOR, HistogramsCanvas.MIN_COLOR,
                        HistogramsCanvas.MEAN_COLOR );
            }
            else {
                double color_max;
                double color_min;
                if ( getColorMagnification() >= HistogramsCanvas.COLOR_SLIDER_MEAN ) {
                    color_min = ( getColorMagnification() - HistogramsCanvas.COLOR_SLIDER_MEAN )
                            / HistogramsCanvas.COLOR_PERCENTAGE_FAKTOR_2;
                    color_max = HistogramsCanvas.COLOR_PERCENTAGE_MAX;
                }
                else {
                    color_min = HistogramsCanvas.COLOR_PERCENTAGE_MIN;
                    color_max = HistogramsCanvas.COLOR_PERCENTAGE_MAX
                            - ( ( getColorMagnification() - HistogramsCanvas.COLOR_SLIDER_MEAN ) / HistogramsCanvas.COLOR_PERCENTAGE_FAKTOR_2 );
                }
                bandcolor = calcColor( Math.abs( percentage
                        - HistogramsCanvas.COLOR_PERCENTAGE_MAX ), color_min,
                        color_max, HistogramsCanvas.MAX_COLOR,
                        HistogramsCanvas.MIN_COLOR );
            }
        }
        else if ( isColorCumulativePercentage() ) {
            if ( isThreeColors() ) {
                bandcolor = calcColor( percentage,
                        HistogramsCanvas.COLOR_CUMUL_MIN,
                        HistogramsCanvas.COLOR_CUMUL_MAX,
                        getColorMagnification()
                                / HistogramsCanvas.COLOR_CUMUL_FAKTOR,
                        HistogramsCanvas.MIN_COLOR, HistogramsCanvas.MAX_COLOR,
                        HistogramsCanvas.MEAN_COLOR );
            }
            else {
                bandcolor = calcColor( percentage,
                        HistogramsCanvas.COLOR_CUMUL_MIN,
                        HistogramsCanvas.COLOR_CUMUL_MAX,
                        HistogramsCanvas.MIN_COLOR, HistogramsCanvas.MAX_COLOR );
            }
        }
        else if ( isColorCounts() ) {
            if ( isThreeColors() ) {
                bandcolor = calcColor( counts_for_colorization, min, max, mean,
                        HistogramsCanvas.MIN_COLOR, HistogramsCanvas.MAX_COLOR,
                        HistogramsCanvas.MEAN_COLOR );
            }
            else {
                bandcolor = calcColor( counts_for_colorization, min, max,
                        HistogramsCanvas.MIN_COLOR, HistogramsCanvas.MAX_COLOR );
            }
        }
        return bandcolor;
    }

    /**
     * Helper method for calcColor methods.
     * 
     * 
     * @param value
     *            the value
     * @param larger
     *            the largest value
     * @param smaller
     *            the smallest value
     * @return a normalized value between larger and smaller
     */
    private double calculateFactor( final double value, final double larger,
            final double smaller ) {
        return ( 255.0 * ( value - smaller ) ) / ( larger - smaller );
    }

    /**
     * This is to draw a scale mapping colors to percentages.
     * 
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param g
     */
    private void drawScale( final int x, int y, final Graphics g ) {
        double stepsize = 0.0;
        double start = 0.0;
        double end = 0.0;
        final int h = HistogramsCanvas.WIDTH / 400;
        if ( isColorPercentage() ) {
            start = 0.0;
            end = 50.25;
            stepsize = 0.25;
        }
        else {
            start = 100.0;
            end = -0.5;
            stepsize = -0.5;
        }
        g.setFont( new Font( "Arial", Font.PLAIN, 10 ) );
        final int w = ( getBarWidth() < 18 ) ? getBarWidth() : 20;
        for( double i = start; i != end; i += stepsize ) {
            g.setColor( calculateColorForHistogram( i, 0, 0, 0, 0 ) );
            g.fillRect( x, y, w, h );
            if ( ( isColorPercentage() && ( ( i % 2 ) == 0 ) )
                    || ( !isColorPercentage() && ( ( i % 5 ) == 0 ) ) ) {
                double ii = i;
                if ( isColorPercentage() ) {
                    ii = 50 - ii;
                }
                g.drawString( "" + i, x + w + 4, y + 4 );
            }
            y += h;
        }
    }

    /**
     * Draws "special feature" for a histogram hd as a bar. The special feature
     * could be a particular gene of interest, postive or negative controls. The
     * feature is a box/line between x1 and x2 with thickness thickness and of
     * color color. It can also be labeled wirh label label. The positions are
     * given by the values of that feature.
     * 
     * 
     * @param values
     * @param x1
     * @param x2
     * @param thickness
     * @param color
     * @param label
     * @param hd
     * @param g
     */
    private void drawSpecialFeature( final double[] values, final int x1,
            final int x2, final int thickness, final Color color,
            final String label, final HistogramData hd, final Graphics g ) {
        // TODO this could be used to find and draw domains!!!!! Very Cool!!!
        if ( ( values == null ) || ( values.length < 1 ) ) {
            return;
        }
        for( int z = 0; z < values.length; ++z ) {
            final double f = ( values[ z ] - hd.getMedianOfFirstBin() )
                    / ( hd.getMedianOfLastBin() - hd.getMedianOfFirstBin() );
            final int y1 = ( int ) ( ( 0.5 + ( HistogramsCanvas.HEIGTH + HistogramsCanvas.BORDER ) ) - ( f * HistogramsCanvas.HEIGTH ) );
            g.setColor( color );
            g.fillRect( x1, y1, x2 - x1, thickness );
            if ( ( label != null ) && ( label.length() > 0 ) ) {
                g.drawString( label, x1 + getBarWidth(), y1 );
            }
        }
    }

    public int getBarWidth() {
        return _bar_width;
    }

    public double getColorMagnification() {
        return _color_magnification;
    }

    private int getGap() {
        return _gap;
    }

    private boolean[] getHiddenHistograms() {
        return _hidden_ones;
    }

    /**
     * Returns the HistogramData array being displayed.
     * 
     * @return the HistogramData array being displayed
     */
    private HistogramData[] getHistogramData() {
        return _hd;
    }

    /**
     * Returns a String array containg the names of all HistogramDatas.
     * 
     * @return a String array containg the names of all HistogramDatas
     */
    public String[] getHistogramNames() {
        final String[] names = new String[ getHistogramData().length ];
        for( int i = 0; i < getHistogramData().length; ++i ) {
            names[ i ] = getHistogramData()[ i ].getName();
        }
        return names;
    }

    public double getMagnification() {
        return _magnification;
    }

    /**
     * Calculates the number of hidden histograms.
     * 
     * @return the number of hidden histograms
     */
    private int getNumberOfHiddenHistograms() {
        int hidden_ones = 0;
        for( int i = 0; i < getHiddenHistograms().length; ++i ) {
            if ( getHiddenHistograms()[ i ] ) {
                ++hidden_ones;
            }
        }
        return hidden_ones;
    }

    /**
     * Initializer.
     * 
     */
    private void init() {
        setHiddenHistograms( new boolean[ _hd.length ] );
        setSize( HistogramsCanvas.WIDTH, HistogramsCanvas.HEIGTH + 50 );
        setPreferredSize( new Dimension( HistogramsCanvas.WIDTH,
                HistogramsCanvas.HEIGTH + 50 ) );
        resetBarWidth();
        setBackground( HistogramsCanvas.BACKGROUND_COLOR );
        setLineUpValues( HistogramsCanvas.LINE_UP_VALUES );
        setLineUpPercentages( HistogramsCanvas.LINE_UP_PERCENTAGES );
        setThreeColors( HistogramsCanvas.THREE_COLORS );
        setDrawHistogram( HistogramsCanvas.DRAW_HISTOGRAM );
        setDrawColorizedBand( HistogramsCanvas.DRAW_COLORIZED_BAND );
        setDrawLine( HistogramsCanvas.DRAW_LINE );
        setColorCounts( HistogramsCanvas.COLOR_COUNTS );
        setColorPercentage( HistogramsCanvas.COLOR_PERCENTAGE );
        setColorCumulativePercentage( HistogramsCanvas.COLOR_CUMULATIVE_PERCENTAGE );
        setShowLabels( HistogramsCanvas.SHOW_LABELS );
        setShowPositiveControls( HistogramsCanvas.SHOW_POSITIVE_CONTROLS );
        setShowNegativeControls( HistogramsCanvas.SHOW_NEGATIVE_CONTROLS );
        setShowGenes( HistogramsCanvas.SHOW_GENES );
        setShowGeneNames( HistogramsCanvas.SHOW_GENE_NAMES );
        setMagnification( HistogramsCanvas.MAGNIFICATION );
        setColorMagnification( HistogramsCanvas.COLOR_MAGNIFICATION );
        if ( isLineUpValues() ) {
            rePad();
        }
    }

    public boolean isColorCounts() {
        return _color_counts;
    }

    public boolean isColorCumulativePercentage() {
        return _color_cumulative_percentage;
    }

    public boolean isColorPercentage() {
        return _color_percentage;
    }

    public boolean isDrawColorizedBand() {
        return _draw_colorized_band;
    }

    public boolean isDrawHistogram() {
        return _draw_histogram;
    }

    public boolean isDrawLine() {
        return _draw_line;
    }

    public boolean isHideHistogram( final int histogram_number ) {
        return getHiddenHistograms()[ histogram_number ];
    }

    public boolean isLineUpPercentages() {
        return _line_up_percentages;
    }

    public boolean isLineUpValues() {
        return _line_up_values;
    }

    public boolean isShowGeneNames() {
        return _show_gene_names;
    }

    public boolean isShowGenes() {
        return _show_genes;
    }

    public boolean isShowLabels() {
        return _show_labels;
    }

    public boolean isShowNegativeControls() {
        return _show_negative_controls;
    }

    public boolean isShowPositiveControls() {
        return _show_positive_controls;
    }

    public boolean isThreeColors() {
        return _three_colors;
    }

    /**
     * The paint method.
     * 
     * @param g
     *            a Graphics object
     */
    @Override
    public void paint( final Graphics g ) {
        if ( getHistogramData() == null ) {
            return;
        }
        int hidden_ones_encountered = 0;
        for( int i = 0; i < getHistogramData().length; ++i ) {
            if ( isHideHistogram( i ) ) {
                ++hidden_ones_encountered;
                continue;
            }
            final HistogramData hd = getHistogramData()[ i ];
            final int x1 = HistogramsCanvas.BORDER
                    + ( ( i - hidden_ones_encountered ) * ( getBarWidth() + getGap() ) );
            final int x2 = x1 + getBarWidth();
            final double factor = ( double ) HistogramsCanvas.STEPS
                    / ( double ) hd.length();
            final double min;
            if ( HistogramsCanvas.NORMALIZE_TO_MIN_COUNT ) {
                min = hd.getMinOfCounts();
            }
            else {
                min = 0.0;
            }
            final double max = hd.getMaxOfCounts();
            final double mean = hd.getMeanOfCounts();
            int jj = 0;
            int stepsize = ( HistogramsCanvas.HEIGTH - ( 2 * HistogramsCanvas.BORDER ) )
                    / HistogramsCanvas.STEPS;
            if ( stepsize < 1 ) {
                stepsize = 1;
            }
            for( int j = 0; j < HistogramsCanvas.STEPS; ++j ) {
                final int y1 = ( HistogramsCanvas.HEIGTH + HistogramsCanvas.BORDER )
                        - ( j * stepsize );
                final int y2 = y1 + stepsize;
                double counts = 0.0;
                double counts_for_colorization = 0.0;
                double precentage = 0.0;
                boolean max_reached = false;
                if ( factor >= 1 ) {
                    jj = ( int ) ( j / factor );
                    counts_for_colorization = counts = hd.getCounts( jj );
                    precentage = hd.getCumulativePercentage( jj );
                    if ( getMagnification() > 1 ) {
                        counts *= getMagnification();
                        if ( counts > max ) {
                            counts = max;
                            max_reached = true;
                        }
                    }
                }
                else {
                    // Need to average (~~ NEEDS TO BE TESTED!! CZ 10/28/04 ~~):
                    double sum = 0;
                    final int fi = ( int ) ( 0.5 + ( 1.0 / factor ) );
                    for( int xx = 0; xx < fi; ++xx ) {
                        if ( jj > ( hd.length() - 1 ) ) {
                            jj = hd.length() - 1;
                        }
                        sum += hd.getCounts( jj++ );
                    }
                    counts = sum / fi;
                }
                final Color bandcolor = calculateColorForHistogram( precentage,
                        counts_for_colorization, min, max, mean );
                if ( isDrawColorizedBand()
                        && ( isColorCounts() || isColorPercentage() || isColorCumulativePercentage() ) ) {
                    g.setColor( bandcolor );
                    g.fillRect( x1, y1, x2 - x1, y2 - y1 );
                }
                if ( isDrawHistogram() || isDrawLine() ) {
                    final double ratio = ( counts - min ) / ( max - min );
                    final int x = ( int ) ( 0.5 + ( ( x2 - x1 ) * ratio ) );
                    g.setColor( calculateColorForHistogram( bandcolor ) );
                    if ( isDrawHistogram() ) {
                        g.fillRect( x1, y1, x, y2 - y1 );
                    }
                    if ( isDrawLine() && !max_reached ) {
                        g.setColor( HistogramsCanvas.LINE_COLOR );
                        g.drawLine( x1 + x, y1, x1 + x, y2 );
                    }
                }
                if ( j == 0 ) {
                    g.setColor( HistogramsCanvas.VALUES_COLOR );
                    g.setFont( HistogramsCanvas.FONT1 );
                    String name = hd.getName();
                    final int l = 135 / ( _hd.length - getNumberOfHiddenHistograms() );
                    name = ( name.length() > l ) ? name.substring( 0, l )
                            : name;
                    g.drawString( name + "", x1, y1 + 24 );
                }
                if ( isShowLabels() ) {
                    if ( ( j == 0 ) || ( j == ( HistogramsCanvas.STEPS - 1 ) ) ) {
                        g.setColor( HistogramsCanvas.VALUES_COLOR );
                        g.setFont( HistogramsCanvas.FONT1 );
                    }
                    if ( j == 0 ) {
                        g.drawString( hd.getMedianOfFirstBin() + "", x1,
                                y1 + 12 );
                    }
                    else if ( j == ( HistogramsCanvas.STEPS - 1 ) ) {
                        g.drawString( hd.getMedianOfLastBin() + "", x1, y1 );
                    }
                }
            } // end of j loop.
            // if ( isShowGenes() && ( hd.getGeneNames() != null ) ) {
            // }
        } // end of i loop.
        if ( !isColorCounts() ) {
            drawScale(
                    HistogramsCanvas.BORDER
                            + ( ( getHistogramData().length - hidden_ones_encountered ) * ( getBarWidth() + getGap() ) ),
                    HistogramsCanvas.BORDER, g );
        }
    } // paint( Graphics g )

    /**
     * This repads all HistogramDatas (adding zero to the shorter ones, so that
     * all end up with the same lengthm for alignment).
     * 
     */
    private void rePad() {
        double min_start = Double.MAX_VALUE;
        double max_end = -Double.MAX_VALUE;
        for( int i = 0; i < getHistogramData().length; ++i ) {
            final double start = getHistogramData()[ i ].getMedianOfFirstBin();
            final double end = getHistogramData()[ i ].getMedianOfLastBin();
            if ( start < min_start ) {
                min_start = start;
            }
            if ( end > max_end ) {
                max_end = end;
            }
        }
        for( int i = 0; i < getHistogramData().length; ++i ) {
            getHistogramData()[ i ].rePad( min_start, max_end );
        }
    }

    /**
     * This resets the width of the histograms and the gap inbetween according
     * to the number of visible histograms.
     * 
     * 
     */
    public void resetBarWidth() {
        final int length = _hd.length - getNumberOfHiddenHistograms();
        if ( length != 0 ) {
            setGap( ( ( 300 / length ) < 20 ) ? ( 300 / length ) : 20 );
            setBarWidth( ( HistogramsCanvas.WIDTH - 50 - ( getGap() * ( length + 1 ) ) )
                    / length );
        }
    }

    public void setBarWidth( final int bar_width ) {
        _bar_width = bar_width;
    }

    public void setColorCounts( final boolean color_counts ) {
        _color_counts = color_counts;
    }

    public void setColorCumulativePercentage(
            final boolean color_cumulative_percentage ) {
        _color_cumulative_percentage = color_cumulative_percentage;
    }

    public void setColorMagnification( final double color_magnification ) {
        _color_magnification = color_magnification;
    }

    public void setColorPercentage( final boolean color_percentage ) {
        _color_percentage = color_percentage;
    }

    public void setDrawColorizedBand( final boolean draw_colorized_band ) {
        _draw_colorized_band = draw_colorized_band;
    }

    public void setDrawHistogram( final boolean draw_histogram ) {
        _draw_histogram = draw_histogram;
    }

    public void setDrawLine( final boolean draw_line ) {
        _draw_line = draw_line;
    }

    private void setGap( final int gap ) {
        _gap = gap;
    }

    private void setHiddenHistograms( final boolean[] hidden_ones ) {
        _hidden_ones = hidden_ones;
    }

    /**
     * Sets the HistogramData array to be displayed.
     * 
     * @param hd
     *            the HistogramData array to be displayed
     */
    private void setHistogramData( final HistogramData[] hd ) {
        _hd = hd;
    }

    public void setLineUpPercentages( final boolean line_up_percentages ) {
        _line_up_percentages = line_up_percentages;
    }

    public void setLineUpValues( final boolean line_up_values ) {
        _line_up_values = line_up_values;
    }

    public void setMagnification( final double magnification ) {
        _magnification = magnification;
    }

    public void setShowGeneNames( final boolean show_gene_names ) {
        _show_gene_names = show_gene_names;
    }

    public void setShowGenes( final boolean show_genes ) {
        _show_genes = show_genes;
    }

    public void setShowLabels( final boolean show_labels ) {
        _show_labels = show_labels;
    }

    public void setShowNegativeControls( final boolean show_negative_controls ) {
        _show_negative_controls = show_negative_controls;
    }

    public void setShowPositiveControls( final boolean show_positive_controls ) {
        _show_positive_controls = show_positive_controls;
    }

    public void setThreeColors( final boolean three_colors ) {
        _three_colors = three_colors;
    }

    public void toggleHideHistogram( final int histogram_number ) {
        getHiddenHistograms()[ histogram_number ] = !getHiddenHistograms()[ histogram_number ];
    }

    /**
     * Helper method for calculateColorForHistogramBar. Adds a contstant to c as
     * long as a certain threshold is not crossed.
     * 
     * @param c
     * @return c with a constant added as long as a certain threshold is not
     *         crossed
     */
    private int towardsWhite( int c ) {
        c += HistogramsCanvas.TOWARDS_WHITE;
        if ( c > 255 ) {
            c = 255;
        }
        return c;
    }
}
