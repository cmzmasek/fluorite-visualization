// $Id: FluoriteTest.java,v 1.12 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.fluorite.heatmap.HeatMapPanel;
import org.fluorite.heatmap.PlateDataParser;
import org.fluorite.heatmap.data.ReplicatePlateData;
import org.fluorite.mhistograms.BasicHistogramDataItem;
import org.fluorite.mhistograms.HistogramData;
import org.fluorite.mhistograms.HistogramDataItem;
import org.fluorite.mhistograms.HistogramsFrame;
import org.fluorite.util.FluoriteUtil;

public final class FluoriteTest {

    private final static String PATH_TO_TEST_FILES = System
                                                           .getProperty( "user.dir" )
                                                           + FluoriteUtil
                                                                   .getFileSeparator()
                                                           + "test_data"
                                                           + FluoriteUtil
                                                                   .getFileSeparator();

    /**
     * @param args
     */
    public static void main( final String[] args ) {
        FluoriteTest.test();
    }

    private static void test() {
        int failed = 0;
        int succeeded = 0;
        System.out
                .print( "[Test if directory with files for testing exists/is readable: " );
        if ( FluoriteTest.testDataFilesDir() ) {
            System.out.println( "OK.]" );
        }
        else {
            System.out.println( "\ncould not find/read from directory \""
                    + FluoriteTest.PATH_TO_TEST_FILES + "\".]" );
            System.out.println( "Testing aborted.\n" );
            System.exit( -1 );
        }
        final long start_time = new Date().getTime();
        System.out.print( "MHistograms 1: " );
        if ( FluoriteTest.testMHistogram1() ) {
            System.out.println( "OK." );
            succeeded++;
        }
        else {
            System.out.println( "failed." );
            failed++;
        }
        System.out.print( "MHistograms 2: " );
        if ( FluoriteTest.testMHistogram2() ) {
            System.out.println( "OK." );
            succeeded++;
        }
        else {
            System.out.println( "failed." );
            failed++;
        }
        System.out.print( "heatmap" );
        if ( FluoriteTest.testHeatMap() ) {
            System.out.println( "OK." );
            succeeded++;
        }
        else {
            System.out.println( "failed." );
            failed++;
        }
        // System.out.print( "Plate data parser: " );
        // if ( FluoriteTest.testPlateDataParser() ) {
        // System.out.println( "OK." );
        // succeeded++;
        // }
        // else {
        // System.out.println( "failed." );
        // failed++;
        // }
        // System.out.print( "Heat map: " );
        // if ( FluoriteTest.testHeatMap() ) {
        // System.out.println( "OK." );
        // succeeded++;
        // }
        // else {
        // System.out.println( "failed." );
        // failed++;
        // }
        System.out.println();
        System.out.println( "Running time    : "
                + ( new Date().getTime() - start_time ) + "ms" );
        System.out.println();
        System.out.println( "Successful tests: " + succeeded );
        System.out.println( "Failed     tests: " + failed );
        System.out.println();
        if ( failed < 1 ) {
            System.out.println( "OK." );
        }
        else {
            System.out.println( "Not OK." );
        }
        System.out.println();
    }

    private static boolean testDataFilesDir() {
        try {
            final File f = new File( PATH_TO_TEST_FILES );
            if ( !f.exists() ) {
                return false;
            }
            if ( !f.isDirectory() ) {
                return false;
            }
            if ( !f.canRead() ) {
                return false;
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace( System.out );
            return false;
        }
        return true;
    }

    private static boolean testHeatMap() {
        ReplicatePlateData[] replicatePlateDatas = null;
        final PlateDataParser pdp = new PlateDataParser();
        try {
            replicatePlateDatas = pdp.parseFile( new File( PATH_TO_TEST_FILES
                    + "heat_map_data_1.txt" ) );
        }
        catch ( final Exception e ) {
            e.printStackTrace( System.out );
            return false;
        }
        final JFrame f = new JFrame();
        f.setSize( 800, 800 );
        final HeatMapPanel hm = new HeatMapPanel( replicatePlateDatas,
                "test heat map", null );
        f.getContentPane().add( hm );
        f.setVisible( true );
        return true;
    }

    private static boolean testMHistogram1() {
        try {
            final double[] ds = new double[ 14 ];
            ds[ 0 ] = 34;
            ds[ 1 ] = 23;
            ds[ 2 ] = 1;
            ds[ 3 ] = 32;
            ds[ 4 ] = 11;
            ds[ 5 ] = 2;
            ds[ 6 ] = 12;
            ds[ 7 ] = 33;
            ds[ 8 ] = 13;
            ds[ 9 ] = 22;
            ds[ 10 ] = 21;
            ds[ 11 ] = 35;
            ds[ 12 ] = 24;
            ds[ 13 ] = 31;
            final double[] bins = new HistogramData().performBinning( ds, 0,
                    40, 4 );
            if ( bins.length != 4 ) {
                return false;
            }
            if ( bins[ 0 ] != 2 ) {
                return false;
            }
            if ( bins[ 1 ] != 3 ) {
                return false;
            }
            if ( bins[ 2 ] != 4 ) {
                return false;
            }
            if ( bins[ 3 ] != 5 ) {
                return false;
            }
            final double[] ds1 = new double[ 9 ];
            ds1[ 0 ] = 10.0;
            ds1[ 1 ] = 19.0;
            ds1[ 2 ] = 9.999;
            ds1[ 3 ] = 0.0;
            ds1[ 4 ] = 39.9;
            ds1[ 5 ] = 39.999;
            ds1[ 6 ] = 30.0;
            ds1[ 7 ] = 19.999;
            ds1[ 8 ] = 30.1;
            final double[] bins1 = new HistogramData().performBinning( ds1, 0,
                    40, 4 );
            if ( bins1.length != 4 ) {
                return false;
            }
            if ( bins1[ 0 ] != 2 ) {
                return false;
            }
            if ( bins1[ 1 ] != 3 ) {
                return false;
            }
            if ( bins1[ 2 ] != 0 ) {
                return false;
            }
            if ( bins1[ 3 ] != 4 ) {
                return false;
            }
            final double[] bins1_1 = new HistogramData().performBinning( ds1,
                    0, 40, 3 );
            if ( bins1_1.length != 3 ) {
                return false;
            }
            if ( bins1_1[ 0 ] != 3 ) {
                return false;
            }
            if ( bins1_1[ 1 ] != 2 ) {
                return false;
            }
            if ( bins1_1[ 2 ] != 4 ) {
                return false;
            }
            final double[] bins1_2 = new HistogramData().performBinning( ds1,
                    1, 39, 3 );
            if ( bins1_2.length != 3 ) {
                return false;
            }
            if ( bins1_2[ 0 ] != 2 ) {
                return false;
            }
            if ( bins1_2[ 1 ] != 2 ) {
                return false;
            }
            if ( bins1_2[ 2 ] != 2 ) {
                return false;
            }
            final List<Double> data0 = new ArrayList<Double>();
            final List<Double> data1 = new ArrayList<Double>();
            final List<Double> data2 = new ArrayList<Double>();
            final List<Double> data3 = new ArrayList<Double>();
            data0.add( new Double( 2 ) );
            data0.add( new Double( 4 ) );
            data0.add( new Double( 8 ) );
            data0.add( new Double( 16 ) );
            data0.add( new Double( 14 ) );
            data0.add( new Double( 12 ) );
            data0.add( new Double( 10 ) );
            data0.add( new Double( 9 ) );
            data0.add( new Double( 8 ) );
            data0.add( new Double( 7 ) );
            data0.add( new Double( 6 ) );
            data0.add( new Double( 5 ) );
            data0.add( new Double( 4 ) );
            data0.add( new Double( 3 ) );
            data1.add( new Double( 2 ) );
            data1.add( new Double( 4 ) );
            data1.add( new Double( 8 ) );
            data1.add( new Double( 16 ) );
            data1.add( new Double( 14 ) );
            data1.add( new Double( 12 ) );
            data1.add( new Double( 10 ) );
            data2.add( new Double( 2 ) );
            data2.add( new Double( 43 ) );
            data2.add( new Double( 833 ) );
            data2.add( new Double( 16 ) );
            data2.add( new Double( 14 ) );
            data2.add( new Double( 123 ) );
            data2.add( new Double( 1033 ) );
            data2.add( new Double( -1 ) );
            data2.add( new Double( 3 ) );
            data2.add( new Double( 8 ) );
            data2.add( new Double( 1633 ) );
            data2.add( new Double( 14 ) );
            data2.add( new Double( 123 ) );
            data2.add( new Double( 10 ) );
            data3.add( new Double( 23 ) );
            data3.add( new Double( 41 ) );
            data3.add( new Double( 5 ) );
            data3.add( new Double( 8 ) );
            data3.add( new Double( 95 ) );
            data3.add( new Double( 2 ) );
            data3.add( new Double( 10 ) );
            data3.add( new Double( 23 ) );
            data3.add( new Double( 4 ) );
            data3.add( new Double( 5 ) );
            data3.add( new Double( 8 ) );
            data3.add( new Double( 95 ) );
            data3.add( new Double( -4 ) );
            data3.add( new Double( 10 ) );
            data3.add( new Double( 23 ) );
            data3.add( new Double( 41 ) );
            data3.add( new Double( 554 ) );
            data3.add( new Double( 8 ) );
            data3.add( new Double( 95 ) );
            data3.add( new Double( 2 ) );
            data3.add( new Double( 1 ) );
            data3.add( new Double( 233 ) );
            data3.add( new Double( 413 ) );
            data3.add( new Double( 5 ) );
            data3.add( new Double( 83 ) );
            data3.add( new Double( 935 ) );
            data3.add( new Double( 2 ) );
            data3.add( new Double( 120 ) );
            final HistogramData[] hists = new HistogramData[ 4 ];
            final HistogramData hd0 = new HistogramData( "hd 0", data0, 1, 12,
                    10 );
            final HistogramData hd1 = new HistogramData( "hd 1", data1, 1, 10,
                    10 );
            final HistogramData hd2 = new HistogramData( "hd 2", data2, 1, 10,
                    20 );
            final HistogramData hd3 = new HistogramData( "hd 3", data3, 1, 10,
                    10 );
            hists[ 0 ] = hd0;
            hists[ 1 ] = hd1;
            hists[ 2 ] = hd2;
            hists[ 3 ] = hd3;
            // final HistogramsFrame hf = new HistogramsFrame( hists );
            // hf.setVisible( true );
        }
        catch ( final Exception e ) {
            e.printStackTrace( System.out );
            return false;
        }
        return true;
    }

    private static boolean testMHistogram2() {
        try {
            List<HistogramDataItem> l0 = new ArrayList<HistogramDataItem>();
            l0.add( new BasicHistogramDataItem( "34", 34 ) );
            l0.add( new BasicHistogramDataItem( "23", 23 ) );
            l0.add( new BasicHistogramDataItem( "1", 1 ) );
            l0.add( new BasicHistogramDataItem( "32", 32 ) );
            l0.add( new BasicHistogramDataItem( "11", 11 ) );
            l0.add( new BasicHistogramDataItem( "2", 2 ) );
            l0.add( new BasicHistogramDataItem( "12", 12 ) );
            l0.add( new BasicHistogramDataItem( "33", 33 ) );
            l0.add( new BasicHistogramDataItem( "13", 13 ) );
            l0.add( new BasicHistogramDataItem( "22", 22 ) );
            l0.add( new BasicHistogramDataItem( "21", 21 ) );
            l0.add( new BasicHistogramDataItem( "35", 35 ) );
            l0.add( new BasicHistogramDataItem( "24", 24 ) );
            l0.add( new BasicHistogramDataItem( "31", 31 ) );
            List<HistogramDataItem> l1 = new ArrayList<HistogramDataItem>();
            l1.add( new BasicHistogramDataItem( "34", 34 ) );
            l1.add( new BasicHistogramDataItem( "23", 23 ) );
            l1.add( new BasicHistogramDataItem( "1", 1 ) );
            l1.add( new BasicHistogramDataItem( "32", 32 ) );
            l1.add( new BasicHistogramDataItem( "11", 11 ) );
            l1.add( new BasicHistogramDataItem( "2", 2 ) );
            l1.add( new BasicHistogramDataItem( "12", 12 ) );
            l1.add( new BasicHistogramDataItem( "33", 33 ) );
            l1.add( new BasicHistogramDataItem( "13", 13 ) );
            l1.add( new BasicHistogramDataItem( "22", 22 ) );
            l1.add( new BasicHistogramDataItem( "21", 21 ) );
            l1.add( new BasicHistogramDataItem( "35", 35 ) );
            l1.add( new BasicHistogramDataItem( "24", 24 ) );
            l1.add( new BasicHistogramDataItem( "31", 31 ) );
            List<HistogramDataItem> l2 = new ArrayList<HistogramDataItem>();
            l2.add( new BasicHistogramDataItem( "34", 34 ) );
            l2.add( new BasicHistogramDataItem( "23", 23 ) );
            l2.add( new BasicHistogramDataItem( "1", 1 ) );
            l2.add( new BasicHistogramDataItem( "32", 32 ) );
            l2.add( new BasicHistogramDataItem( "11", 11 ) );
            l2.add( new BasicHistogramDataItem( "2", 2 ) );
            l2.add( new BasicHistogramDataItem( "12", 12 ) );
            l2.add( new BasicHistogramDataItem( "33", 33 ) );
            l2.add( new BasicHistogramDataItem( "13", 13 ) );
            l2.add( new BasicHistogramDataItem( "22", 22 ) );
            l2.add( new BasicHistogramDataItem( "21", 21 ) );
            l2.add( new BasicHistogramDataItem( "35", 35 ) );
            l2.add( new BasicHistogramDataItem( "24", 24 ) );
            l2.add( new BasicHistogramDataItem( "31", 31 ) );
            List<HistogramDataItem> l3 = new ArrayList<HistogramDataItem>();
            l3.add( new BasicHistogramDataItem( "", 45 ) );
            l3.add( new BasicHistogramDataItem( "", 6 ) );
            l3.add( new BasicHistogramDataItem( "", 5 ) );
            l3.add( new BasicHistogramDataItem( "", 5 ) );
            l3.add( new BasicHistogramDataItem( "", 7 ) );
            l3.add( new BasicHistogramDataItem( "", 51 ) );
            l3.add( new BasicHistogramDataItem( "", 4 ) );
            l3.add( new BasicHistogramDataItem( "", 41 ) );
            l3.add( new BasicHistogramDataItem( "", 6 ) );
            l3.add( new BasicHistogramDataItem( "", 56 ) );
            l3.add( new BasicHistogramDataItem( "", 32 ) );
            l3.add( new BasicHistogramDataItem( "", 6 ) );
            l3.add( new BasicHistogramDataItem( "", -8 ) );
            l3.add( new BasicHistogramDataItem( "", -7 ) );
            l3.add( new BasicHistogramDataItem( "", -6 ) );
            l3.add( new BasicHistogramDataItem( "", 8 ) );
            l3.add( new BasicHistogramDataItem( "", 4 ) );
            l3.add( new BasicHistogramDataItem( "", -32 ) );
            l3.add( new BasicHistogramDataItem( "", 11 ) );
            l3.add( new BasicHistogramDataItem( "", 4 ) );
            l3.add( new BasicHistogramDataItem( "", 75 ) );
            l3.add( new BasicHistogramDataItem( "", 53 ) );
            l3.add( new BasicHistogramDataItem( "", -1 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 55 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 7 ) );
            l3.add( new BasicHistogramDataItem( "", 35 ) );
            l3.add( new BasicHistogramDataItem( "", 6 ) );
            l3.add( new BasicHistogramDataItem( "", 64 ) );
            l3.add( new BasicHistogramDataItem( "", 74 ) );
            l3.add( new BasicHistogramDataItem( "", 3 ) );
            l3.add( new BasicHistogramDataItem( "", 30 ) );
            l3.add( new BasicHistogramDataItem( "", 8 ) );
            l3.add( new BasicHistogramDataItem( "", 23 ) );
            l3.add( new BasicHistogramDataItem( "", 3 ) );
            l3.add( new BasicHistogramDataItem( "", 20 ) );
            l3.add( new BasicHistogramDataItem( "", 49 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 59 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 44 ) );
            l3.add( new BasicHistogramDataItem( "", 3 ) );
            HistogramData hd0 = new HistogramData( "zero", l0, null, 4 );
            HistogramData hd1 = new HistogramData( "one", l1, null, 4 );
            HistogramData hd2 = new HistogramData( "two", l2, null, 4 );
            HistogramData hd3 = new HistogramData( "three", l3, null, 10 );
            final HistogramData[] hists = new HistogramData[ 4 ];
            hists[ 0 ] = hd0;
            hists[ 1 ] = hd1;
            hists[ 2 ] = hd2;
            hists[ 3 ] = hd3;
            final HistogramsFrame hf = new HistogramsFrame( hists );
            hf.setVisible( true );
        }
        catch ( final Exception e ) {
            e.printStackTrace( System.out );
            return false;
        }
        return true;
    }

    private static boolean testPlateDataParser() {
        ReplicatePlateData[] replicatePlateDatas = null;
        final PlateDataParser pdp = new PlateDataParser();
        try {
            replicatePlateDatas = pdp.parseFile( new File( PATH_TO_TEST_FILES
                    + "heat_map_data_1.txt" ) );
        }
        catch ( final Exception ex ) {
            System.out.println( ex );
            return false;
        }
        return true;
    }
}
