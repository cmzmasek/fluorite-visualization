// $Id: IO.java,v 1.6 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public final class IO {

    /**
     * This method is for checking of the readability of File f. If f does not
     * exist, cannot be read from, is empty, or is a directory an IOException is
     * be thrown.
     * 
     * @param f
     *            the File to be checked
     * @throws IOException
     */
    static void checkFile( final File f ) throws IOException {
        if ( f == null ) {
            throw new IOException( "Attempt to read from a file which is null" );
        }
        else if ( !f.exists() ) {
            throw new IOException( "File \"" + f + "\" does not exist" );
        }
        else if ( !f.canRead() ) {
            throw new IOException( "Cannot read from file \"" + f + "\"" );
        }
        else if ( f.isDirectory() ) {
            throw new IOException( f + " \" is a directory" );
        }
        else if ( f.length() < 1 ) {
            throw new IOException( f + " \" is empty" );
        }
    }

    /**
     * This reads the file named filename into the HistogramData array hd at
     * starting position current. For each HistogramData read in, current is
     * increased by one and then returned. Format: name "name",left of next
     * one,middle,value "name",left of next one,middle,value name "name",left of
     * next one,middle,value ...
     * 
     * @param filename
     *            the name of the file to be read in from
     * @return a HistogramData array
     * @throws IOException
     */
    public static HistogramData readInHistogramData( final String filename )
            throws IOException {
        final File f = new File( filename );
        IO.checkFile( f );
        final BufferedReader is = new BufferedReader( new FileReader( f ) );
        final ArrayList<Double> ar = new ArrayList<Double>( 200 );
        HistogramData hd = null;
        final String name = "";
        String inline = "";
        int i = 0;
        boolean first = true;
        double begin = 0.0;
        double end = 0.0;
        double width = 0.0;
        String second = "";
        String third = "";
        boolean thereYet = false;
        boolean posControls = false;
        final ArrayList<Double> positives = new ArrayList<Double>();
        boolean negControls = false;
        final ArrayList<Double> negatives = new ArrayList<Double>();
        while ( ( inline = is.readLine() ) != null ) {
            inline = inline.trim();
            if ( !thereYet && inline.endsWith( "histn.png\");" ) ) {
                inline = is.readLine(); // read the next line, which is column
                // headers
                inline = is.readLine(); // this is the first line of data
                inline = inline.trim();
                System.out.println( "Found histogram data" );
                thereYet = true;
            }
            if ( thereYet ) {
                if ( inline.startsWith( "null device" ) ) { // this is the end
                    // of the data
                    thereYet = false; // we're finished
                    hd = new HistogramData( name, ar, begin, end, width );
                    i++;
                }
                else if ( inline.length() > 0 ) {
                    final StringTokenizer st = new StringTokenizer( inline, "," );
                    if ( !posControls && inline.endsWith( "\"pos.counts\"" ) ) {
                        System.out.println( "Found positive controls: "
                                + inline );
                        posControls = true;
                    }
                    else if ( !negControls
                            && inline.endsWith( "\"neg.counts\"" ) ) {
                        System.out.println( "Found negative controls: "
                                + inline );
                        posControls = false;
                        negControls = true;
                    }
                    else if ( posControls ) { // these are the positive
                        // controls
                        System.out.println( "Positive control: " + inline );
                        st.nextToken(); // the quoted line number
                        final Double posMid = new Double( Double
                                .parseDouble( st.nextToken() ) );
                        st.nextToken(); // the endpoint - ignored
                        final int posCount = Integer.parseInt( st.nextToken() );
                        if ( posCount > 0 ) {
                            System.out.println( "Adding positive control "
                                    + posMid );
                            positives.add( posMid );
                        }
                    }
                    else if ( negControls ) { // these are the negative
                        // controls
                        System.out.println( "Negative control: " + inline );
                        st.nextToken(); // the quoted line number
                        final Double negMid = new Double( Double
                                .parseDouble( st.nextToken() ) );
                        st.nextToken(); // the endpoint - ignored
                        final int negCount = Integer.parseInt( st.nextToken() );
                        if ( negCount > 0 ) {
                            System.out.println( "Adding negative control "
                                    + negMid );
                            negatives.add( negMid );
                        }
                    }
                    else { // so this is the histogram data we want
                        System.out.println( "Histogram data: " + inline );
                        st.nextToken();
                        second = st.nextToken();
                        third = st.nextToken();
                        if ( first ) {
                            first = false;
                            begin = Double.parseDouble( third );
                            width = 2 * Math.abs( begin
                                    - Double.parseDouble( second ) );
                        }
                        final String forth = st.nextToken();
                        end = Double.parseDouble( third );
                        ar.add( new Double( Double.parseDouble( forth ) ) );
                        i++;
                    }
                }
            }
        }
        return hd;
    } // readInHistogramData( String )

    /**
     * This reads the file named filename into the HistogramData array hd at
     * starting position current. For each HistogramData read in, current is
     * increased by one and then returned. Format: name "name",left of next
     * one,middle,value "name",left of next one,middle,value name "name",left of
     * next one,middle,value ...
     * 
     * @param filename
     *            the name of the file to be read in from
     * @return a HistogramData array
     * @throws IOException
     */
    static HistogramData[] readInHistogramDatas( final String filename )
            throws IOException {
        final File f = new File( filename );
        IO.checkFile( f );
        final BufferedReader is = new BufferedReader( new FileReader( f ) );
        ArrayList<Double> ar = new ArrayList<Double>( 200 );
        final HistogramData[] hd_temp = new HistogramData[ 1000 ];
        String name = "";
        String inline = "";
        int i = 0;
        boolean first = true;
        double begin = 0.0;
        double end = 0.0;
        double width = 0.0;
        String second = "";
        String third = "";
        int current = 0;
        while ( ( inline = is.readLine() ) != null ) {
            inline = inline.trim();
            if ( inline.length() > 0 ) {
                final StringTokenizer st = new StringTokenizer( inline, "," );
                if ( st.countTokens() != 4 ) {
                    if ( i > 0 ) {
                        end = Double.parseDouble( third );
                        hd_temp[ current++ ] = new HistogramData( name, ar,
                                begin, end, width );
                    }
                    name = inline;
                    ar = new ArrayList<Double>( 200 );
                    i = 0;
                    first = true;
                    begin = 0.0;
                    end = 0.0;
                }
                else {
                    st.nextToken();
                    second = st.nextToken();
                    third = st.nextToken();
                    if ( first ) {
                        first = false;
                        begin = Double.parseDouble( third );
                        width = 2 * Math.abs( begin
                                - Double.parseDouble( second ) );
                    }
                    final String forth = st.nextToken();
                    end = Double.parseDouble( third );
                    ar.add( new Double( Double.parseDouble( forth ) ) );
                    i++;
                }
            }
        }
        if ( i > 0 ) {
            hd_temp[ current++ ] = new HistogramData( name, ar, begin, end,
                    width );
        }
        final HistogramData[] hd = new HistogramData[ current ];
        for( int j = 0; j < current; ++j ) {
            hd[ j ] = hd_temp[ j ];
        }
        return hd;
    } // readInHistogramDatas( String, HistogramData[], int )

    // private static Double[] log2(Double[] values) {
    // Double[] logValues = new Double[values.length];
    // for (int i=0; i<values.length; i++) {
    // if (values[i] != null) {
    // logValues[i] = new Double(Math.log(values[i].doubleValue())/Math.log(2));
    // //Transform to log2
    // } else {
    // logValues[i] = new Double(0);
    // }
    // }
    // return logValues;
    // }
    // Constructor, not used.
    private IO() {
    }
}
