// $Id: PlateDataParser.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.fluorite.heatmap.data.PlateData;
import org.fluorite.heatmap.data.ReplicatePlateData;
import org.fluorite.heatmap.data.WellData;

public class PlateDataParser {

    public final static int NUMBER_OF_ROWS    = 16;
    public final static int NUMBER_OF_COLUMNS = 24;

    /**
     * This constructor creates a new PlateDataParser object.
     * 
     * 
     */
    public PlateDataParser() {
    }

    /**
     * This parses the stream in BufferedReader is.
     * 
     * 
     * @param is
     *            a stream to parse
     * @return an array of ReplicatePlateDatas
     * @throws IOException
     */
    private ReplicatePlateData[] parseFile( final BufferedReader is )
            throws IOException {
        final TreeMap<Long, ReplicatePlateData> replicate_plate_datas = new TreeMap<Long, ReplicatePlateData>(); // key
        // =
        // plate
        // number,
        // value =
        // replicate_plate_datas
        String current_line = "";
        final String ERROR = "expected: [plate,row(as char),col,"
                + "value1,value2,,,,user-flag for value1,user-flag for value2"
                + ",,,,well type(P,N,E,D),id,name,\"description\","
                + "[flagged by stat analysis for value1,for value2,,,,], got: ";
        final ArrayList<Double> values = new ArrayList<Double>();
        final ArrayList<Boolean> user_flags = new ArrayList<Boolean>();
        final ArrayList<Boolean> stat_flags = new ArrayList<Boolean>();
        try {
            while ( ( current_line = is.readLine() ) != null ) {
                current_line = current_line.trim();
                if ( ( current_line.indexOf( "," ) > 0 )
                        && !current_line.startsWith( "#" ) ) {
                    int number_of_values = 0;
                    boolean isPostiveControl = false;
                    boolean isNegativeControl = false;
                    boolean isEmpty = false;
                    String id = "";
                    String name = "";
                    String desc = "";
                    final String error_line = ERROR + current_line;
                    StringTokenizer st = null;
                    StringTokenizer st_after_desc_flags = null;
                    current_line = separateCommas( current_line );
                    final int start_of_desc = current_line.indexOf( ",\"" );
                    if ( start_of_desc < 8 ) {
                        throw new IOException( error_line );
                    }
                    desc = current_line.substring( start_of_desc + 2,
                            current_line.length() );
                    current_line = current_line.substring( 0, start_of_desc );
                    final int end_of_desc = desc.indexOf( "\"," );
                    // Yes:
                    if ( end_of_desc >= 0 ) {
                        final String desc0 = desc.substring( 0, end_of_desc );
                        st_after_desc_flags = new StringTokenizer( desc
                                .substring( end_of_desc + 2 ), "," );
                        desc = desc0;
                    }
                    else {
                        desc = desc.substring( 0, desc.length() - 1 ); // Removes
                        // "
                        // at the
                        // end.
                    }
                    st = new StringTokenizer( current_line, "," );
                    number_of_values = ( st.countTokens() - 6 ) / 2;
                    if ( number_of_values < 1 ) {
                        throw new IOException( error_line );
                    }
                    stat_flags.clear();
                    if ( st_after_desc_flags != null ) {
                        // stat flags:
                        for( int i = 0; i < number_of_values; ++i ) {
                            Boolean flag;
                            if ( st_after_desc_flags.nextToken().equals( "T" ) ) {
                                flag = Boolean.FALSE;
                            }
                            else {
                                flag = Boolean.TRUE;
                            }
                            stat_flags.add( flag );
                        }
                        if ( st_after_desc_flags.hasMoreTokens() ) {
                            throw new IOException( error_line );
                        }
                    }
                    else {
                        for( int i = 0; i < number_of_values; ++i ) {
                            stat_flags.add( Boolean.FALSE );
                        }
                    }
                    final Long plate_number = new Long( st.nextToken() );
                    final char row_char = st.nextToken().charAt( 0 );
                    final int col = Integer.parseInt( st.nextToken() );
                    values.clear();
                    for( int i = 0; i < number_of_values; ++i ) {
                        final Double value = new Double( st.nextToken() );
                        values.add( value );
                    }
                    // user flags:
                    user_flags.clear();
                    for( int i = 0; i < number_of_values; ++i ) {
                        Boolean flag;
                        if ( st.nextToken().equals( "T" ) ) {
                            flag = Boolean.TRUE;
                        }
                        else {
                            flag = Boolean.FALSE;
                        }
                        user_flags.add( flag );
                    }
                    final String well_type = st.nextToken();
                    // positive
                    if ( well_type.equals( "P" ) ) {
                        isPostiveControl = true;
                    }
                    // negative
                    else if ( well_type.equals( "N" ) ) {
                        isNegativeControl = true;
                    }
                    // empty
                    else if ( well_type.equals( "E" ) ) {
                        isEmpty = true;
                    }
                    else if ( !well_type.equals( "D" ) ) {
                        throw new IOException(
                                "Well type must be either P, N, E, or D [got "
                                        + well_type + "]." );
                    }
                    // id
                    id = st.nextToken();
                    if ( id.equals( "null" ) ) {
                        id = "";
                    }
                    // name
                    name = st.nextToken();
                    if ( name.equals( "null" ) ) {
                        name = "";
                    }
                    // null is not nice
                    if ( desc.equals( "null" ) ) {
                        desc = "";
                    }
                    if ( st.hasMoreTokens() ) {
                        throw new IOException( error_line );
                    }
                    if ( replicate_plate_datas.containsKey( plate_number ) ) {
                        final ReplicatePlateData rpd = replicate_plate_datas
                                .get( plate_number );
                        for( int i = 0; i < values.size(); ++i ) {
                            final PlateData pd = rpd.getPlateData( i );
                            pd.setData( new WellData( values.get( i )
                                    .doubleValue(), // value
                                    name.trim(), // name
                                    desc.trim(), // desc
                                    id.trim(), // id
                                    user_flags.get( i ).booleanValue(), // flagged
                                    stat_flags.get( i ).booleanValue(), // flagged
                                    // by
                                    // stats
                                    isNegativeControl, // negative control
                                    isPostiveControl, // positive control
                                    isEmpty, // empty
                                    false ), // non existant, null
                                    row_char, col );
                        }
                    }
                    else {
                        final ReplicatePlateData rpd = new ReplicatePlateData();
                        for( int i = 0; i < values.size(); ++i ) {
                            final PlateData pd = new PlateData(
                                    PlateDataParser.NUMBER_OF_ROWS,
                                    PlateDataParser.NUMBER_OF_COLUMNS, i, ""
                                            + plate_number );
                            pd.setData( new WellData( values.get( i )
                                    .doubleValue(), name.trim(), // name
                                    desc.trim(), // desc
                                    id.trim(), // id
                                    user_flags.get( i ).booleanValue(), // flagged
                                    stat_flags.get( i ).booleanValue(), // flagged
                                    // by
                                    // stats
                                    isNegativeControl, // negative control
                                    isPostiveControl, // positive control
                                    isEmpty, // empty
                                    false ), // non existant, null
                                    row_char, col );
                            rpd.addPlateData( pd );
                        }
                        replicate_plate_datas.put( plate_number, rpd );
                    }
                }
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            throw new IOException( "Error while parsing line [" + current_line
                    + "]: " + e.getMessage() );
        }
        final Collection<ReplicatePlateData> c = replicate_plate_datas.values();
        final ReplicatePlateData[] rpd_array = new ReplicatePlateData[ c.size() ];
        final Object[] oa = c.toArray();
        for( int i = 0; i < oa.length; ++i ) {
            rpd_array[ i ] = ( ReplicatePlateData ) oa[ i ];
        }
        return rpd_array;
    }

    /**
     * This parses the data in File file. Format: plate,row(as
     * char),col,value1,value2,,,,user-flag for value1, user-flag for
     * value2,,,,well type(P,N,E,D),id,name,"description", [flagged by stat
     * analysis for value1,for value2,,,,],[hit-status(H,C,N)] user-flag: "0" =
     * false, true otherwise well type: "P" = positive control "N" = negative
     * control "E" = empty control "D" = data flagged by stat analysis: "0" =
     * false, true otherwise
     * 
     * @param file
     *            a file to parse
     * @return an array of ReplicatePlateDatas
     * @throws IOException
     */
    public ReplicatePlateData[] parseFile( final File file ) throws IOException {
        final BufferedReader is = new BufferedReader( new FileReader( file ) );
        return parseFile( is );
    }

    /**
     * This parses the data in URL url. Format: plate,row(as
     * char),col,value1,value2,,,,user-flag for value1 (T or F), user-flag for
     * value2,,,,well type(P,N,E,D),id,name,"description", [flagged by stat
     * analysis for value1,for value2,,,,],[hit-status(H,C,N)] user-flag: "0" =
     * false, true otherwise well type: "P" = positive control "N" = negative
     * control "E" = empty control "D" = data flagged by stat analysis: "0" =
     * false, true otherwise
     * 
     * @param url
     *            data to parse
     * @return an array of ReplicatePlateDatas
     * @throws IOException
     */
    public ReplicatePlateData[] parseFile( final URL url ) throws IOException {
        final BufferedReader is = new BufferedReader( new InputStreamReader(
                url.openStream() ) );
        return parseFile( is );
    }

    /**
     * This replaces each ",," by ",null,".
     * 
     * 
     * @param s
     *            the String for which commas have to be separated by "null"
     * @return the argument s with each ",," replaced by a ",null,"
     */
    private String separateCommas( String s ) {
        int i = s.indexOf( ",," );
        while ( i > 1 ) {
            s = s.substring( 0, i + 1 ) + "null"
                    + s.substring( i + 1, s.length() );
            i = s.indexOf( ",," );
        }
        return s;
    }
}
