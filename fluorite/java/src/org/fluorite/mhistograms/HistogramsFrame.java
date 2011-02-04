// $Id: HistogramsFrame.java,v 1.11 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

public class HistogramsFrame extends JFrame {

    private static final long serialVersionUID = -7690510475169778394L;
    private HistogramsPanel   _hp;

    // public static void main( final String[] args ) {
    // // TODO main should be in "tools" type class.
    // final HistogramsFrame h_frame = new HistogramsFrame( args[ 0 ] );
    // h_frame.setVisible( true );
    // }
    public HistogramsFrame( final HistogramData[] histogram_datas ) {
        init( histogram_datas );
    }

    public HistogramsFrame( final List<HistogramData> histogram_datas ) {
        final HistogramData[] histograms_ary = new HistogramData[ histogram_datas
                .size() ];
        int i = 0;
        for( final HistogramData histogram_data : histogram_datas ) {
            histograms_ary[ i++ ] = histogram_data;
        }
        init( histograms_ary );
    }

    // public HistogramsFrame( final String filename ) {
    // HistogramData[] histogram_datas = null;
    // try {
    // histogram_datas= IO.readInHistogramData( filename );
    //            
    // }
    // catch ( final Exception e ) {
    // System.err.println( "Problem creating HistogramsFrame: " + e );
    // }
    //     
    // init( histogram_datas );
    // }
    private void init( final HistogramData[] histogram_datas ) {
        _hp = new HistogramsPanel( histogram_datas );
        getContentPane().add( _hp );
        setSize( HistogramsCanvas.WIDTH + ControlsPanel.WIDTH + 20,
                HistogramsCanvas.HEIGTH + 100 );
        // JMenuBar menuBar;
        // JMenu menu, submenu;
        // JMenuItem menuItem;
        // JRadioButtonMenuItem rbMenuItem;
        // JCheckBoxMenuItem cbMenuItem;
        // // Create the menu bar.
        // menuBar = new JMenuBar();
        // // Build the first menu.
        // menu = new JMenu( "A Menu" );
        // menu.setMnemonic( KeyEvent.VK_A );
        // menu.getAccessibleContext().setAccessibleDescription(
        // "The only menu in this program that has menu items" );
        // menuBar.add( menu );
        // // a group of JMenuItems
        // menuItem = new JMenuItem( "A text-only menu item", KeyEvent.VK_T );
        // menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_1,
        // ActionEvent.ALT_MASK ) );
        // menuItem.getAccessibleContext().setAccessibleDescription(
        // "This doesn't really do anything" );
        // menu.add( menuItem );
        // menuItem = new JMenuItem( "Both text and icon", new ImageIcon(
        // "images/middle.gif" ) );
        // menuItem.setMnemonic( KeyEvent.VK_B );
        // menu.add( menuItem );
        // menuItem = new JMenuItem( new ImageIcon( "images/middle.gif" ) );
        // menuItem.setMnemonic( KeyEvent.VK_D );
        // menu.add( menuItem );
        // // a group of radio button menu items
        // menu.addSeparator();
        // final ButtonGroup group = new ButtonGroup();
        // rbMenuItem = new JRadioButtonMenuItem( "A radio button menu item" );
        // rbMenuItem.setSelected( true );
        // rbMenuItem.setMnemonic( KeyEvent.VK_R );
        // group.add( rbMenuItem );
        // menu.add( rbMenuItem );
        // rbMenuItem = new JRadioButtonMenuItem( "Another one" );
        // rbMenuItem.setMnemonic( KeyEvent.VK_O );
        // group.add( rbMenuItem );
        // menu.add( rbMenuItem );
        // // a group of check box menu items
        // menu.addSeparator();
        // cbMenuItem = new JCheckBoxMenuItem( "A check box menu item" );
        // cbMenuItem.setMnemonic( KeyEvent.VK_C );
        // menu.add( cbMenuItem );
        // cbMenuItem = new JCheckBoxMenuItem( "Another one" );
        // cbMenuItem.setMnemonic( KeyEvent.VK_H );
        // menu.add( cbMenuItem );
        // // a submenu
        // menu.addSeparator();
        // submenu = new JMenu( "A submenu" );
        // submenu.setMnemonic( KeyEvent.VK_S );
        // menuItem = new JMenuItem( "An item in the submenu" );
        // menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_2,
        // ActionEvent.ALT_MASK ) );
        // submenu.add( menuItem );
        // menuItem = new JMenuItem( "Another item" );
        // submenu.add( menuItem );
        // menu.add( submenu );
        // // Build second menu in the menu bar.
        // menu = new JMenu( "Another Menu" );
        // menu.setMnemonic( KeyEvent.VK_N );
        // menu.getAccessibleContext().setAccessibleDescription(
        // "This menu does nothing" );
        // menuBar.add( menu );
        // setJMenuBar( menuBar );
        addWindowListener( new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing( final WindowEvent win_event ) {
                // Perhaps ask user if they want to save any unsaved files
                // first.
                System.exit( 0 );
            }
        } );
        setVisible( true );
        validate();
    }
}
