// $Id: FluoriteUtil.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.util;

import java.io.File;
import java.util.StringTokenizer;

public final class FluoriteUtil {

    public final static String FILE_SEPARATOR = System
                                                      .getProperty( "file.separator" );
    public final static String LINE_SEPARATOR = System
                                                      .getProperty( "line.separator" );

    public static boolean isEmpty( final String s ) {
        return ( ( s == null ) || ( s.trim().length() < 1 ) );
    }

    public static String cut2( double d ) {
        final int i = ( int ) ( d * 100D );
        d = i / 100D;
        return d + "";
    }

    public static String getFileSeparator() {
        return FluoriteUtil.FILE_SEPARATOR;
    }

    public static String getLineSeparator() {
        return FluoriteUtil.LINE_SEPARATOR;
    }

    public static String isReadableFile( final File f ) {
        if ( !f.exists() ) {
            return "\"" + f + "\" does not exist";
        }
        if ( f.isDirectory() ) {
            return "\"" + f + "\" is a directory";
        }
        if ( !f.isFile() ) {
            return "\"" + f + "\" is not a file";
        }
        if ( !f.canRead() ) {
            return "cannot read from \"" + f + "\"";
        }
        if ( f.length() < 1 ) {
            return "\"" + f + "\" appears empty";
        }
        return null;
    }

    public static double parseDouble( final String new_value,
            final double current_value ) {
        double d = 0.0D;
        try {
            d = Double.valueOf( new_value ).doubleValue();
        }
        catch ( final Exception e ) {
            d = current_value;
        }
        return d;
    }

    public static int parseInt( final String new_value, final int current_value ) {
        int i = 0;
        try {
            i = Integer.valueOf( new_value ).intValue();
        }
        catch ( final Exception e ) {
            i = current_value;
        }
        return i;
    }

    public static String[] splitString( final String delim, final String str ) {
        int count = 0;
        boolean first_time = true;
        String last = "";
        String current = "";
        final StringTokenizer st = new StringTokenizer( str, delim, true );
        final String[] tokens = new String[ st.countTokens() ];
        while ( st.hasMoreTokens() ) {
            current = st.nextToken();
            if ( first_time ) {
                first_time = false;
                last = current;
            }
            if ( current.equals( delim )
                    && ( last.equals( delim ) || !st.hasMoreTokens() ) ) {
                tokens[ count ] = "";
                count++;
            }
            else if ( !current.equals( delim ) ) {
                tokens[ count ] = current;
                count++;
            }
            last = current;
        }
        final String[] fields = new String[ count ];
        for( int i = 0; i < count; i++ ) {
            fields[ i ] = tokens[ i ];
        }
        return fields;
    }

    private FluoriteUtil() {
    }
}
