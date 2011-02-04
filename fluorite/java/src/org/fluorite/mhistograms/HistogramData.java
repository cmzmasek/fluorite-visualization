// $Id: HistogramData.java,v 1.17 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.fluorite.util.BasicDescriptiveStatistics;
import org.fluorite.util.DescriptiveStatistics;
import org.fluorite.util.FluoriteUtil;

public class HistogramData {

    private final static String       SEP_1 = "|";
    private final static String       SEP_2 = "#";
    private final String              _name;
    private final Map<String, Double> _data_items;
    private double[]                  _bins;
    private double[]                  _cumulative_percentages;
    private final String[]            _bin_labels;
    private final double              _min_count;
    private final double              _max_count;
    private final double              _mean_value;
    private final double              _sum;
    private double                    _median_first_bin;
    private double                    _median_last_bin;
    private final boolean             _can_re_bin;

    public HistogramData() {
        _name = null;
        _data_items = null;
        _max_count = 0;
        _sum = 0;
        _can_re_bin = false;
        _mean_value = 0;
        _bin_labels = null;
        _min_count = 0;
    }

    public HistogramData( final String name, final List<Double> bins,
            final double median_first_bin, final double median_last_bin,
            final double bin_width ) {
        this( name, bins, null, median_first_bin, median_last_bin );
    }

    /**
     * Constructor. Upon construction, the immutable minimum, maximum and mean
     * values are determined.
     * 
     * @param name
     *            the of name this histogram
     * @param bins
     *            an ArrayList of doubles representing the frequencies/counts
     *            for each bin this histogram
     * @param median_first_bin
     *            the median value for the first bin (lowest values)
     * @param median_last_bin
     *            the median value for the last bin (highest values)
     */
    public HistogramData( final String name, final List<Double> bins,
            final List<String> bin_labels, final double median_first_bin,
            final double median_last_bin ) {
        _can_re_bin = false;
        if ( median_first_bin >= median_last_bin ) {
            throw new IllegalArgumentException(
                    "Error in medians of first and last bins: first bin: "
                            + median_first_bin + " last bin: "
                            + median_last_bin );
        }
        if ( ( ( bin_labels != null ) && !bin_labels.isEmpty() )
                && ( bins.size() != bin_labels.size() ) ) {
            throw new IllegalArgumentException(
                    "counts and labels must have equal numbers of elements" );
        }
        if ( name != null ) {
            _name = name;
        }
        else {
            _name = "";
        }
        if ( bins != null ) {
            _bins = new double[ bins.size() ];
        }
        else {
            _bins = new double[ 0 ];
        }
        if ( ( bin_labels != null ) && !bin_labels.isEmpty() ) {
            _bin_labels = new String[ bin_labels.size() ];
        }
        else {
            _bin_labels = null;
        }
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double sum = 0.0;
        for( int i = 0; i < bins.size(); ++i ) {
            final double d = bins.get( i ).doubleValue();
            if ( d < min ) {
                min = d;
            }
            if ( d > max ) {
                max = d;
            }
            sum += d;
            _bins[ i ] = d;
        }
        _min_count = min;
        _max_count = max;
        _sum = sum;
        if ( bins.size() > 0 ) {
            _mean_value = sum / bins.size();
        }
        else {
            _mean_value = 0;
        }
        _data_items = new HashMap<String, Double>();
        _median_first_bin = median_first_bin;
        _median_last_bin = median_last_bin;
        _cumulative_percentages = new double[ bins.size() ];
        calculateCumulativePercentages();
    } // HistogramData

    public HistogramData( final String name,
            final List<HistogramDataItem> data_items,
            final List<String> bin_labels, final int number_of_bins ) {
        if ( data_items == null || data_items.isEmpty() ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data object with null or empty data items" );
        }
        if ( data_items.size() < 3 ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data object less than 3 data items" );
        }
        if ( name != null ) {
            _name = name;
        }
        else {
            _name = "";
        }
        _can_re_bin = true;
        _cumulative_percentages = new double[ number_of_bins ];
        final double[] values = new double[ data_items.size() ];
        int i = 0;
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        _data_items = new HashMap<String, Double>();
        for( final HistogramDataItem data_item : data_items ) {
            final double value = data_item.getValue();
            addDataItem( data_item );
            values[ i++ ] = value;
            if ( value < min ) {
                min = value;
            }
            if ( value > max ) {
                max = value;
            }
        }
        _bins = performBinning( values, min, max, number_of_bins );
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;
        double sum = 0.0;
        for( final double d : _bins ) {
            if ( d < min ) {
                min = d;
            }
            if ( d > max ) {
                max = d;
            }
            sum += d;
        }
        _min_count = min;
        _max_count = max;
        _sum = sum;
        if ( ( bin_labels != null ) && !bin_labels.isEmpty() ) {
            _bin_labels = new String[ bin_labels.size() ];
        }
        else {
            _bin_labels = null;
        }
        if ( _bins.length > 0 ) {
            _mean_value = sum / _bins.length;
        }
        else {
            _mean_value = 0;
        }
        if ( ( ( bin_labels != null ) && !bin_labels.isEmpty() )
                && ( _bins.length != bin_labels.size() ) ) {
            throw new IllegalArgumentException(
                    "counts and labels must have equal numbers of elements" );
        }
        calculateCumulativePercentages();
    }

    public HistogramData( final String name, final double[] values,
            final List<String> bin_labels, final int number_of_bins ) {
        if ( values == null || values.length < 1 ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data object with null or empty values" );
        }
        if ( values.length < 3 ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data object less than 3 values" );
        }
        if ( name != null ) {
            _name = name;
        }
        else {
            _name = "";
        }
        _can_re_bin = true;
        _cumulative_percentages = new double[ number_of_bins ];
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        _data_items = null;
        for( final double value : values ) {
            if ( value < min ) {
                min = value;
            }
            if ( value > max ) {
                max = value;
            }
        }
        _bins = performBinning( values, min, max, number_of_bins );
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;
        double sum = 0.0;
        for( final double d : _bins ) {
            if ( d < min ) {
                min = d;
            }
            if ( d > max ) {
                max = d;
            }
            sum += d;
        }
        _min_count = min;
        _max_count = max;
        _sum = sum;
        if ( ( bin_labels != null ) && !bin_labels.isEmpty() ) {
            _bin_labels = new String[ bin_labels.size() ];
        }
        else {
            _bin_labels = null;
        }
        if ( _bins.length > 0 ) {
            _mean_value = sum / _bins.length;
        }
        else {
            _mean_value = 0;
        }
        if ( ( ( bin_labels != null ) && !bin_labels.isEmpty() )
                && ( _bins.length != bin_labels.size() ) ) {
            throw new IllegalArgumentException(
                    "counts and labels must have equal numbers of elements" );
        }
        calculateCumulativePercentages();
    }

    public HistogramData( final String name, final double[] values,
            final double min, final double max, final List<String> bin_labels,
            final int number_of_bins ) {
        if ( values == null || values.length < 1 ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data with null or empty values" );
        }
        if ( values.length < 3 ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data with less than 3 values" );
        }
        if ( min >= max ) {
            throw new IllegalArgumentException(
                    "attempt to create histogram data with min greater or equal to max " );
        }
        if ( name != null ) {
            _name = name;
        }
        else {
            _name = "";
        }
        _can_re_bin = true;
        _cumulative_percentages = new double[ number_of_bins ];
        _data_items = null;
        _bins = performBinning( values, min, max, number_of_bins );
        double min_count = Double.MAX_VALUE;
        double max_count = -Double.MAX_VALUE;
        double sum = 0.0;
        for( final double d : _bins ) {
            if ( d < min_count ) {
                min_count = d;
            }
            if ( d > max_count ) {
                max_count = d;
            }
            sum += d;
        }
        _min_count = min_count;
        _max_count = max_count;
        _sum = sum;
        if ( ( bin_labels != null ) && !bin_labels.isEmpty() ) {
            _bin_labels = new String[ bin_labels.size() ];
        }
        else {
            _bin_labels = null;
        }
        if ( _bins.length > 0 ) {
            _mean_value = sum / _bins.length;
        }
        else {
            _mean_value = 0;
        }
        if ( ( ( bin_labels != null ) && !bin_labels.isEmpty() )
                && ( _bins.length != bin_labels.size() ) ) {
            throw new IllegalArgumentException(
                    "counts and labels must have equal numbers of elements" );
        }
        calculateCumulativePercentages();
    }

    /**
     * Add HistogramDataItem data_item only if its id is not empty.
     * 
     * 
     * @param data_item
     */
    public void addDataItem( final HistogramDataItem data_item ) {
        final String id = data_item.getIdentifier();
        if ( !FluoriteUtil.isEmpty( id ) ) {
            if ( getHistogramDataItems().containsKey( id ) ) {
                throw new IllegalArgumentException( "data item id \"" + id
                        + "\" is already present in histogram data" );
            }
            getHistogramDataItems().put( id, data_item.getValue() );
        }
    }

    /**
     * Returns the frequency/counts of the bin with index index (zero based).
     * 
     * @param index
     *            the index of the bin
     * @return the frequency/counts of the bin with index index
     */
    public double getCounts( final int index ) {
        return _bins[ index ];
    }

    /**
     * Returns the cumulative percentage of the bin with index index (zero
     * based).
     * 
     * @param index
     *            the index of the bin
     * @return the frequency/counts of the bin with index index
     */
    public double getCumulativePercentage( final int index ) {
        return _cumulative_percentages[ index ];
    }

    public HistogramDataItem getHistogramDataItem( final String id ) {
        return new BasicHistogramDataItem( id, getHistogramDataItems().get( id ) );
    }

    public Map<String, Double> getHistogramDataItems() {
        return _data_items;
    }

    public String getLabel( final int index ) {
        return _bin_labels[ index ];
    }

    /**
     * Returns the highest frequency/counts.
     * 
     * @return the highest frequency/counts
     */
    public double getMaxOfCounts() {
        return _max_count;
    }

    /**
     * Returns the mean of the frequency/counts.
     * 
     * @return the mean of the frequency/counts
     */
    public double getMeanOfCounts() {
        return _mean_value;
    }

    /**
     * Returns the median of the values of the first bin.
     * 
     * @return the median of the values of the first bin
     */
    public double getMedianOfFirstBin() {
        return _median_first_bin;
    }

    /**
     * Returns the median of the values of the last bin.
     * 
     * @return the median of the values of the last bin
     */
    public double getMedianOfLastBin() {
        return _median_last_bin;
    }

    /**
     * Returns the smallest frequency/counts.
     * 
     * @return the smallest frequency/counts
     */
    public double getMinOfCounts() {
        return _min_count;
    }

    /**
     * Returns the name of this histogram.
     * 
     * 
     * @return the name of this histogram
     */
    public String getName() {
        return _name;
    }

    public int getNumberOfHistogramDataItems() {
        return getHistogramDataItems().size();
    }

    /**
     * Returns the sum of the frequency/counts.
     * 
     * 
     * @return the sum of the frequency/counts.
     */
    public double getSumOfCounts() {
        return _sum;
    }

    /**
     * Returns true if this histogram is empty.
     * 
     * @return true if this histogram is empty
     */
    public boolean isEmpty() {
        return length() < 1;
    }

    /**
     * Returns the number of bins.
     * 
     * @return number of bins
     */
    public int length() {
        return _bins.length;
    }

    public void reBin( final int number_of_bins ) {
        if ( !isCanReBin() ) {
            throw new IllegalStateException(
                    "attempt to re-bin non-re-binnable histogram data" );
        }
        if ( number_of_bins < 2 ) {
            throw new IllegalStateException(
                    "attempt to re-bin to less than two bins" );
        }
        // FIXME do something clever here
    }

    /**
     * Allows the pad the beginning and end of the histogram with empty (0
     * counts) bins. Important: This does not change the min, max and mean of
     * the frequency/count.
     * 
     * @param new_first_bin_median
     *            the value of the new first median
     * @param new_last_bin_median
     *            the value of the new last median
     */
    public void rePad( final double new_first_bin_median,
            final double new_last_bin_median ) {
        if ( new_first_bin_median > getMedianOfFirstBin() ) {
            throw new IllegalArgumentException( "New first bin median: "
                    + new_first_bin_median + ", current first bin median: "
                    + getMedianOfFirstBin() );
        }
        if ( new_last_bin_median < getMedianOfLastBin() ) {
            throw new IllegalArgumentException( "New last bin median: "
                    + new_last_bin_median + ", current last bin median: "
                    + getMedianOfLastBin() );
        }
        final double diff_start = Math.abs( getMedianOfFirstBin()
                - new_first_bin_median );
        final double diff_end = Math.abs( new_last_bin_median
                - getMedianOfLastBin() );
        final double diff = getMedianOfLastBin() - getMedianOfFirstBin();
        final int steps = length();
        final double factor = diff / steps;
        final int steps_to_add_start = ( int ) ( diff_start / factor );
        final int steps_to_add_add = ( int ) ( diff_end / factor );
        final int a = steps_to_add_start + steps;
        final int b = a + steps_to_add_add;
        final double[] new_data = new double[ b ];
        final double[] new_perc = new double[ b ];
        for( int i = 0; i < steps_to_add_start; ++i ) {
            new_data[ i ] = 0.0;
            new_perc[ i ] = 0.0;
        }
        int x = 0;
        for( int i = steps_to_add_start; i < a; ++i ) {
            new_data[ i ] = _bins[ x ];
            new_perc[ i ] = _cumulative_percentages[ x++ ];
        }
        for( int i = steps_to_add_start + steps; i < b; ++i ) {
            new_data[ i ] = 0.0;
            new_perc[ i ] = _cumulative_percentages[ _cumulative_percentages.length - 1 ];
        }
        _bins = new_data;
        _cumulative_percentages = new_perc;
        _median_first_bin = new_first_bin_median;
        _median_last_bin = new_last_bin_median;
    } // rePad (double, double)

    /**
     * Returns this histogram as a String.
     * 
     * @return this histogram as a String
     */
    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer();
        s.append( "Name: " );
        s.append( getName() );
        s.append( FluoriteUtil.LINE_SEPARATOR );
        s.append( "Mean of first bin: " );
        s.append( getMedianOfFirstBin() );
        s.append( FluoriteUtil.LINE_SEPARATOR );
        s.append( "Mean of last bin: " );
        s.append( getMedianOfLastBin() );
        s.append( FluoriteUtil.LINE_SEPARATOR );
        s.append( "Number of bins: " );
        s.append( length() );
        s.append( FluoriteUtil.LINE_SEPARATOR );
        s.append( "Counts:" );
        s.append( FluoriteUtil.LINE_SEPARATOR );
        for( int i = 0; i < length(); ++i ) {
            s.append( i );
            s.append( ": " );
            s.append( getCounts( i ) );
            s.append( " [" );
            s.append( getCumulativePercentage( i ) );
            s.append( "%]" );
            s.append( FluoriteUtil.LINE_SEPARATOR );
        }
        return s.toString();
    }

    /**
     * This calculates the cumulative percentages.
     * 
     */
    private void calculateCumulativePercentages() {
        double sum = 0.0;
        for( int i = 0; i < length(); ++i ) {
            _cumulative_percentages[ i ] = ( sum += ( ( 100 * getCounts( i ) ) / getSumOfCounts() ) );
            ;
        }
    }

    private boolean isCanReBin() {
        return _can_re_bin;
    }

    public static HistogramData[] createArrayFromString( final String s ) {
        final StringTokenizer st = new StringTokenizer( s, "@$" );
        final String firstLine = st.nextToken();
        final StringTokenizer st1 = new StringTokenizer( firstLine, "|||" );
        final String intro = st1.nextToken();
        if ( !intro.startsWith( " Array of HistogramData of size " ) ) {
            System.err.println( "Format error in input string: " + intro );
            return null;
        }
        final StringTokenizer st2 = new StringTokenizer( intro, " " );
        while ( !st2.nextToken().equals( "size" ) ) {
        }
        final String size = st2.nextToken();
        int sizeInt = 0;
        try {
            sizeInt = Integer.parseInt( size );
        }
        catch ( final NumberFormatException nfe ) {
            System.err.println( "Size of array should be given, instead got:"
                    + size + "!" );
            return null;
        }
        final HistogramData[] container = new HistogramData[ sizeInt ];
        String currentLine = st.nextToken();
        int index = 0;
        while ( !currentLine.equals( "||| End of Array |||" ) ) {
            if ( currentLine.startsWith( "HistogramData " ) ) {
                try {
                    index = Integer.parseInt( currentLine.substring( 14 ) );
                }
                catch ( final NumberFormatException nfe ) {
                    System.err
                            .println( "Array index should be in this line; instead gave "
                                    + currentLine.substring( 15 ) );
                    return null;
                }
            }
            else if ( currentLine.startsWith( "End of HistogramData" ) ) {
                // no action required
            }
            else {
                container[ index ] = HistogramData
                        .createFromString( currentLine );
            }
            currentLine = st.nextToken();
        }
        return container;
    }

    public static HistogramData[] createArrayFromX( final Object s ) {
        return null;
    }

    public static HistogramData createFromString( final String s ) {
        try {
            final String[] tokens = FluoriteUtil.splitString(
                    HistogramData.SEP_2, s );
            final StringTokenizer one = new StringTokenizer( tokens[ 0 ],
                    HistogramData.SEP_1 );
            final StringTokenizer neg_controls = new StringTokenizer(
                    tokens[ 1 ], HistogramData.SEP_1 );
            final StringTokenizer pos_controls = new StringTokenizer(
                    tokens[ 2 ], HistogramData.SEP_1 );
            final StringTokenizer counts = new StringTokenizer( tokens[ 3 ],
                    HistogramData.SEP_1 );
            final String name = one.nextToken();
            final double bin_width = Double.parseDouble( one.nextToken() );
            final double median_first_bin = Double
                    .parseDouble( one.nextToken() );
            final double median_last_bin = Double.parseDouble( one.nextToken() );
            final double[] neg = new double[ neg_controls.countTokens() ];
            int i = 0;
            while ( neg_controls.hasMoreTokens() ) {
                neg[ i++ ] = Double.parseDouble( neg_controls.nextToken() );
            }
            final double[] pos = new double[ pos_controls.countTokens() ];
            i = 0;
            while ( pos_controls.hasMoreTokens() ) {
                pos[ i++ ] = Double.parseDouble( pos_controls.nextToken() );
            }
            final ArrayList<Double> c = new ArrayList<Double>( counts
                    .countTokens() );
            i = 0;
            while ( counts.hasMoreTokens() ) {
                c.add( new Double( counts.nextToken() ) );
            }
            final HistogramData new_one = new HistogramData( name, c,
                    median_first_bin, median_last_bin, bin_width );
            for( int j = 4; j < tokens.length; j++ ) {
                final StringTokenizer gene = new StringTokenizer( tokens[ j ],
                        HistogramData.SEP_1 );
                if ( gene.hasMoreTokens() ) {
                    final String gene_name = gene.nextToken();
                    final Double[] gene_values = new Double[ gene.countTokens() ];
                    i = 0;
                    while ( gene.hasMoreTokens() ) {
                        gene_values[ i++ ] = new Double( gene.nextToken() );
                    }
                    // new_one.addGene( gene_name, gene_values );
                }
            }
            return new_one;
        }
        catch ( final Exception e ) {
            System.err.println( "Problem creating Histogram with this input: "
                    + s + ": " + e );
            return null;
        }
    }

    public double[] performBinning( final double[] values, final double min,
            final double max, final int number_of_bins ) {
        if ( min >= max ) {
            throw new IllegalArgumentException( "min [" + min
                    + "] is larger than or equal to max [" + max + "]" );
        }
        if ( number_of_bins < 3 ) {
            throw new IllegalArgumentException(
                    "number of bins is smaller than 3" );
        }
        final double[] bins = new double[ number_of_bins ];
        for( int i = 0; i < bins.length; ++i ) {
            bins[ i ] = 0;
        }
        final double binning_factor = number_of_bins / ( max - min );
        final int last_index = number_of_bins - 1;
        final DescriptiveStatistics stats_first = new BasicDescriptiveStatistics();
        final DescriptiveStatistics stats_last = new BasicDescriptiveStatistics();
        for( final double value : values ) {
            if ( !( ( value > max ) || ( value < min ) ) ) {
                final int bin = ( int ) ( ( value - min ) * binning_factor );
                if ( bin > last_index ) {
                    ++bins[ last_index ];
                }
                else {
                    ++bins[ bin ];
                }
                if ( bin == 0 ) {
                    stats_first.addValue( value );
                }
                else if ( bin >= last_index ) {
                    stats_last.addValue( value );
                }
            }
        }
        if ( stats_first.getN() > 0 && stats_last.getN() > 0 ) {
            _median_first_bin = stats_first.median();
            _median_last_bin = stats_last.median();
        }
        else {
            _median_first_bin = min;
            _median_last_bin = max;
        }
        return bins;
    }
}
