// $Id: HistogramsApplet.java,v 1.4 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.applet.Applet;

public class HistogramsApplet extends Applet {

    /**
     * 
     */
    private static final long serialVersionUID = 980346147030412432L;
    String                    gene_name;

    // Construct the applet
    public HistogramsApplet() {
    }

    // Destroy the applet
    @Override
    public void destroy() {
    }

    // Get Applet information
    @Override
    public String getAppletInfo() {
        return "Applet Information";
    }

    // Get a parameter value
    public String getParameter( final String key, final String def ) {
        return ( ( getParameter( key ) != null ) ? getParameter( key ) : def );
    }

    // Get parameter info
    @Override
    public String[][] getParameterInfo() {
        final String[][] pinfo = { { "gene_name", "String", "" }, };
        return pinfo;
    }

    // Initialize the applet
    @Override
    public void init() {
        HistogramsPanel hp = null;
        try {
            final String histsAsString = getParameter( "Histograms" );
            final HistogramData[] hists = HistogramData
                    .createArrayFromString( histsAsString );
            hp = new HistogramsPanel( hists );
            this.add( hp );
            setSize( HistogramsCanvas.WIDTH + ControlsPanel.WIDTH + 20,
                    HistogramsCanvas.HEIGTH + 100 );
            setVisible( true );
            validate();
        }
        catch ( final Exception e ) {
            System.err.println( "Unable to set up HistogramsApplet" + e );
        }
        try {
            jbInit();
        }
        catch ( final Exception e ) {
            System.err.println( "Problem in jbInit" + e );
        }
    }

    // Component initialization
    private void jbInit() throws Exception {
    }

    // Start the applet
    @Override
    public void start() {
    }

    // Stop the applet
    @Override
    public void stop() {
    }
}
