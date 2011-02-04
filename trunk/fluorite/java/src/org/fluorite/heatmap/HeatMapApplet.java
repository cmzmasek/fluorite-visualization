// $Id: HeatMapApplet.java,v 1.4 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.JFrame;

import org.fluorite.heatmap.data.ReplicatePlateData;

public class HeatMapApplet extends Applet {

    // Data
    // ----
    public final static String APPLET_VERSION   = "0.1";
    private static final long  serialVersionUID = 1009656541863391949L;
    private String             _message1;
    private String             _message2;

    /**
     * This constructs the applet.
     * 
     */
    public HeatMapApplet() {
        _message1 = "";
        _message2 = "";
    }

    /**
     * Opens a new browser frame displaying the contents at the URL url.
     * 
     * 
     * @param url
     *            URL whose contents are to be displayed
     * @throws Exception
     */
    public void go( final URL url ) throws Exception {
        final AppletContext context = getAppletContext();
        context.showDocument( url, "Heat Map Applet" );
        // Opens a new browser frame. All the subsequently opened web
        // pages will be shown in this frame. Currently (08/31/99), it
        // seems not possible to use "_self" in IE 4 or 5.
        // (It would work for the first page, but then null pointers will be
        // thrown.)
    }

    @Override
    public void init() {
        try {
            setName( "Heat Map Applet" );
            setBackground( Color.black );
            setForeground( Color.white );
            setFont( new Font( "SansSerif", Font.BOLD, 9 ) );
            repaint();
            initializeComponents();
        }
        catch ( final Exception e ) {
            System.err.println( "Problem initializing the applet: " + e );
        }
    }

    private void initializeComponents() throws Exception {
        ReplicatePlateData[] replicatePlateDatas = null;
        final PlateDataParser pdp = new PlateDataParser();
        URL url_for_input = null;
        boolean error = false;
        String s = "";
        String inputfile = "";
        String name = "";
        try {
            s = System.getProperty( "java.version" );
        }
        catch ( final Exception ex ) {
            System.err.println( "Could not determine Java version: " + ex );
        }
        if ( s != null ) {
            _message2 = "[Your Java version: " + s + "]" + " [Applet version: "
                    + HeatMapApplet.APPLET_VERSION + "]";
            repaint();
        }
        try {
            name = getParameter( "name" );
            System.out.println( "name=" + name );
            inputfile = getParameter( "inputfile" );
            System.out.println( "inputfile=" + inputfile );
        }
        catch ( final Exception ex ) {
            error = true;
            _message1 = "Failed to obtain string for data input url or string for servlet url. ";
            repaint();
            System.err.println( _message1 + ex );
        }
        if ( !error ) {
            if ( ( inputfile == null ) || ( inputfile.length() < 1 ) ) {
                error = true;
                _message1 = "Failed to obtain string for data input url. ";
                repaint();
            }
            if ( ( name == null ) || ( name.length() < 1 ) ) {
                error = true;
                _message1 = "Failed to obtain name. ";
                repaint();
            }
        }
        if ( !error ) {
            try {
                url_for_input = new URL( inputfile );
            }
            catch ( final Exception ex ) {
                error = true;
                _message1 = "Failed to create URL for data input. ";
                repaint();
                System.err.println( _message1 + ex );
            }
        }
        if ( !error ) {
            try {
                replicatePlateDatas = pdp.parseFile( url_for_input );
            }
            catch ( final Exception ex ) {
                error = true;
                _message1 = "Failed to read/parse \"" + url_for_input + "\". ";
                repaint();
                System.err.println( _message1 + ex );
            }
            if ( ( replicatePlateDatas == null )
                    || ( replicatePlateDatas.length < 1 ) ) {
                error = true;
                _message1 = "Input data from \"" + url_for_input
                        + "\" is (might be) empty.";
                repaint();
            }
        }
        if ( !error && ( replicatePlateDatas != null )
                && ( replicatePlateDatas.length > 0 ) ) {
            final JFrame f = new JFrame();
            f.setSize( 1000, 800 );
            final HeatMapPanel hm = new HeatMapPanel( replicatePlateDatas,
                    name, this );
            f.getContentPane().add( hm );
            f.setVisible( true );
            _message1 = "Heat Map Applet is now ready.";
            repaint();
        }
    }

    /**
     * Prints message when initialization is finished. Called automatically.
     * 
     * 
     * @param g
     *            Graphics
     * 
     */
    @Override
    public void paint( final Graphics g ) {
        g.drawString( _message2, 10, 20 );
        g.drawString( _message1, 10, 40 );
    }
}
