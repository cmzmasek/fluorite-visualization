// $Id: HeatMapPanel.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fluorite.heatmap.data.ReplicatePlateData;

public class HeatMapPanel extends PlateDisplayPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -7768067819232372908L;
    private String            _name;
    private HeatMapApplet     _parent_applet;

    HeatMapPanel() {
    }

    // private ColorScaleRenderer colorScaleRenderer;
    /**
     * This constructor creates a new HeatMapPanel.
     * 
     * 
     * @param replicatePlateDatas
     *            the data to be displayed as array of ReplicatePlateDatas
     * @param name
     *            a name for this
     * 
     * @param parent_applet
     *            the parent HeatMapApplet, null if used in application
     * @throws IllegalArgumentException
     */
    public HeatMapPanel( final ReplicatePlateData[] replicatePlateDatas,
            final String name, final HeatMapApplet parent_applet )
            throws IllegalArgumentException {
        if ( ( replicatePlateDatas == null )
                || ( replicatePlateDatas.length < 1 ) ) {
            throw new IllegalArgumentException(
                    "PlateData array cannot be empty or null" );
        }
        _parent_applet = parent_applet;
        _name = name;
        setName( "Heat Map: " + name );
        setMinColor( PlateDisplayPanel.MIN_COLOR );
        setMaxColor( PlateDisplayPanel.MAX_COLOR );
        setMeanColor( PlateDisplayPanel.MEAN_COLOR );
        setWellSize( PlateDisplayPanel.DEFAULT_WELL_SIZE );
        setUseMean( true );
        meanToggleButton.setSelected( true );
        int maxNumberOfReplicates = 0;
        for( int i = 0; i < replicatePlateDatas.length; ++i ) {
            final int numberOfPlates = replicatePlateDatas[ i ]
                    .getNumberOfReplicates();
            if ( numberOfPlates > maxNumberOfReplicates ) {
                maxNumberOfReplicates = numberOfPlates;
            }
        }
        _maxNumberOfReplicates = maxNumberOfReplicates;
        _platerenderers = new PlateRenderer[ replicatePlateDatas.length ][ _maxNumberOfReplicates ];
        try {
            jbInit();
        }
        catch ( final Exception e ) {
            System.err.println( "Error in jbInit: " + e );
        }
        for( int i = 0; i < replicatePlateDatas.length; ++i ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
                if ( j >= replicatePlateDatas[ i ].getNumberOfReplicates() ) {
                    final JPanel jp = new JPanel();
                    jp.setBackground( Color.black );
                    jPanel4.add( jp, null );
                    setPlateRenderer( null, i, j );
                }
                else {
                    final PlateRenderer pr = new PlateRenderer(
                            replicatePlateDatas[ i ].getPlateData( j ),
                            getWellSize(), getCurrentMin(), getCurrentMax(),
                            getCurrentMean(), getMinColor(), getMaxColor(),
                            getMeanColor(), this, i );
                    jPanel4.add( pr, null );
                    setPlateRenderer( pr, i, j );
                }
            }
        }
        resetMinMaxMean( true, true );
        platesPanelScrollPane.getViewport().add( jPanel4, null );
        platesPanelScrollPane.getViewport().setBackground( Color.black );
        platesPanelScrollPane.setBackground( Color.black );
        platesPanelScrollPane.getVerticalScrollBar().setUnitIncrement( 10 );
        setBackground( Color.black );
        jPanel4.setBackground( Color.black );
        updateDisplay();
    }

    // Search for something which does not exist will clear the
    // previous search results.
    void clearSearchButton_actionPerformed( final ActionEvent e ) {
        setResultsOfSearchShowing( false );
        findString( "~~", false, false, false, true );
        updateDisplay();
    }

    void colorizePerReplicateCheckBox_actionPerformed( final ActionEvent e ) {
        if ( colorizePerReplicateCheckBox.isSelected() ) {
            colorizePerPlateCheckBox.setSelected( false );
            resetMinMaxMeanPerColumn( ignoreFlaggedCheckBox.isSelected(),
                    ignoreControlsCheckBox.isSelected() );
            setEnabledOfMinMaxMeanChangingItems( false );
        }
        else {
            setEnabledOfMinMaxMeanChangingItems( true );
        }
        updateDisplay();
    }

    void displaySelectedButton_actionPerformed( final ActionEvent e ) {
        final String s = getWellInformationAsString( false, false, false,
                false, true );
        final JTextArea ta = new JTextArea( s );
        final JScrollPane sp = new JScrollPane();
        sp.getViewport().setPreferredSize( new Dimension( 300, 300 ) );
        sp.getViewport().add( ta, null );
        ta.setEditable( false );
        final JOptionPane op = new JOptionPane( sp, JOptionPane.DEFAULT_OPTION );
        final JDialog d = op.createDialog( this, "SELECTED WELLS" );
        d.setVisible( true );
    }

    void flagOutliersButton_actionPerformed( final ActionEvent e ) {
        final String s = getWellInformationAsString( true, false, false, true,
                false );
        final JTextArea ta = new JTextArea( s );
        final JScrollPane sp = new JScrollPane();
        sp.getViewport().setPreferredSize( new Dimension( 300, 300 ) );
        sp.getViewport().add( ta, null );
        ta.setEditable( false );
        final int r = JOptionPane.showConfirmDialog( this, sp,
                "FLAG OUTLIERS (FLAGGED BY STATISTICAL ANALYSIS)",
                JOptionPane.OK_CANCEL_OPTION );
        if ( r == JOptionPane.OK_OPTION ) {
            showOutliersCheckBox.setSelected( true );
            showUserFlagsCheckBox.setSelected( true );
            setFlaggedStatusOfOutlierWells( true );
        }
    }

    void flagSelectedButton_actionPerformed( final ActionEvent e ) {
        final String s = getWellInformationAsString( false, false, false, true,
                true );
        final JTextArea ta = new JTextArea( s );
        final JScrollPane sp = new JScrollPane();
        sp.getViewport().setPreferredSize( new Dimension( 300, 300 ) );
        sp.getViewport().add( ta, null );
        ta.setEditable( false );
        final int r = JOptionPane.showConfirmDialog( this, sp,
                "FLAG SELECTED WELLS", JOptionPane.OK_CANCEL_OPTION );
        if ( r == JOptionPane.OK_OPTION ) {
            showUserFlagsCheckBox.setSelected( true );
            setFlaggedStatusOfSelectedWells( true );
        }
    }

    public String getName() {
        return _name;
    }

    // Set and get methods
    // ===================
    private HeatMapApplet getParentHeatMapApplet() {
        return _parent_applet;
    }

    /**
     * This returns a string description of certain wells. If outliers is true
     * it only returs descriptions for wells which are considered "outliers"
     * (flagged by a statistical analysis). If not_outliers is true it only
     * returs descriptions for wells which are not considered "outliers"
     * (flagged by a statistical analysis). If flagged is true it only returs
     * descriptions for wells which are flagged (by the user). If not_flagged is
     * true it only returs descriptions for wells which are not flagged (by the
     * user). If selected is true it only returs descriptions for wells which
     * are selected (by the user).
     * 
     * @param outliers
     *            limit to "outliers"
     * @param not_outliers
     *            limit to non "outliers"
     * @param flagged
     *            limit to flagged
     * @param not_flagged
     *            limit to not flagged
     * @param selected
     *            limit to selected
     * 
     * @return a string describing all certain wells
     */
    String getWellInformationAsString( final boolean outliers,
            final boolean not_outliers, final boolean flagged,
            final boolean not_flagged, final boolean selected ) {
        final StringBuffer sb = new StringBuffer();
        for( int i = 0; i < getPlateRendererLength(); ++i ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    final String s = pr.getWellInformationAsString( outliers,
                            not_outliers, flagged, not_flagged, selected );
                    if ( ( s != null ) && ( s.length() > 0 ) ) {
                        sb.append( "PLATE: " );
                        sb.append( pr.getPlateData().getName() );
                        sb.append( " REPLICATE: " );
                        sb.append( pr.getPlateData().getReplicateNumber() + 1 );
                        sb.append( "\n" );
                        sb.append( s );
                        sb.append( "\n" );
                    }
                }
            }
        }
        return sb.toString();
    }

    void ignoreControlsCheckBox_actionPerformed( final ActionEvent e ) {
        recalculateMinMaxMeanAfterChange();
    }

    void ignoreFlaggedCheckBox_actionPerformed( final ActionEvent e ) {
        recalculateMinMaxMeanAfterChange();
    }

    void inverseMinMaxColorsButton_actionPerformed( final ActionEvent e ) {
        final Color min = getMinColor();
        setMinColor( getMaxColor() );
        setMaxColor( min );
        updateDisplay();
    }

    /**
     * Returns whether the flagged status of any well has been changed.
     * 
     * 
     * @return whether the flagged status of any well has been changed
     */
    // private boolean isAnyFlaggedStatusChanged() {
    // for( int i = 0; i < getPlateRendererLength(); ++i ) {
    // final PlateRenderer pr0 = getPlateRenderer( i, 0 );
    // for( int well_row = 0; well_row < pr0.getRows(); ++well_row ) {
    // for( int well_col = 0; well_col < pr0.getColumns(); ++well_col ) {
    // for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
    // final PlateRenderer pr = getPlateRenderer( i, j );
    // if ( pr != null ) {
    // final WellRenderer wr = ( WellRenderer ) pr
    // .getAbstractRenderer( well_row, well_col );
    // if ( wr.isFlaggingStatusChanged() ) {
    // return true;
    // }
    // }
    // }
    // }
    // }
    // }
    // return false;
    // }
    /**
     * This initializes the panel. Must only be called once in the lifecycle of
     * the panel.
     * 
     * 
     * @throws Exception
     */
    public void jbInit() throws Exception {
        setLayout( frameBorderLayout );
        controlPanel.setLayout( controlPanelGridLayout );
        inverseMinMaxColorsButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        inverseMinMaxColorsButton.setText( "Inverse" );
        inverseMinMaxColorsButton
                .setToolTipText( "This inverses the colors for minimum and maximum." );
        inverseMinMaxColorsButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        inverseMinMaxColorsButton_actionPerformed( e );
                    }
                } );
        searchButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        searchButton.setText( "Search" );
        searchButton
                .setToolTipText( "This performs a text search in the accession, name, and description fields." );
        searchButton.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                searchButton_actionPerformed( e );
            }
        } );
        clearSearchButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        clearSearchButton.setText( "Clear" );
        clearSearchButton
                .setToolTipText( "This clears previous search results." );
        clearSearchButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        clearSearchButton_actionPerformed( e );
                    }
                } );
        plusButton1.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        plusButton1.setText( "+" );
        plusButton1.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                plusButton1_actionPerformed( e );
            }
        } );
        plusButton2.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        plusButton2.setText( "+" );
        plusButton2.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                plusButton2_actionPerformed( e );
            }
        } );
        plusButton3.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        plusButton3.setText( "+" );
        plusButton3.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                plusButton3_actionPerformed( e );
            }
        } );
        valueTextField1.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        valueTextField1.setPreferredSize( new Dimension( 50, 21 ) );
        valueTextField1.setText( "" );
        valueTextField1.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                valueTextField1_actionPerformed( e );
            }
        } );
        valueTextField2.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        valueTextField2.setPreferredSize( new Dimension( 50, 21 ) );
        valueTextField2.setText( "" );
        valueTextField2.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                valueTextField2_actionPerformed( e );
            }
        } );
        valueTextField3.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        valueTextField3.setPreferredSize( new Dimension( 50, 21 ) );
        valueTextField3.setText( "" );
        valueTextField3.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                valueTextField3_actionPerformed( e );
            }
        } );
        minusButton1.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        minusButton1.setText( "-" );
        minusButton1.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                minusButton1_actionPerformed( e );
            }
        } );
        minusButton2.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        minusButton2.setText( "-" );
        minusButton2.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                minusButton2_actionPerformed( e );
            }
        } );
        minusButton3.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        minusButton3.setText( "-" );
        minusButton3.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                minusButton3_actionPerformed( e );
            }
        } );
        changeColorButton1.setText( "Change Color" );
        changeColorButton1
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        changeColorButton1_actionPerformed( e );
                    }
                } );
        changeColorButton2.setText( "Change Color" );
        changeColorButton2
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        changeColorButton2_actionPerformed( e );
                    }
                } );
        changeColorButton3.setText( "Change Color" );
        changeColorButton3
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        changeColorButton3_actionPerformed( e );
                    }
                } );
        changeColorButton1.setBackground( PlateDisplayPanel.MIN_COLOR );
        changeColorButton1.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        changeColorButton2.setBackground( PlateDisplayPanel.MAX_COLOR );
        changeColorButton2.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        changeColorButton3.setBackground( PlateDisplayPanel.MEAN_COLOR );
        changeColorButton3.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        changeColorButton3.setForeground( Color.lightGray );
        nameLabel1.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        nameLabel1.setToolTipText( "" );
        nameLabel1.setText( "Min" );
        resetButton1.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        resetButton1.setToolTipText( "" );
        resetButton1.setText( "Reset" );
        resetButton1.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                resetButton1_actionPerformed( e );
            }
        } );
        nameLabel2.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        nameLabel2.setToolTipText( "" );
        nameLabel2.setText( "Max" );
        resetButton2.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        resetButton2.setToolTipText( "" );
        resetButton2.setText( "Reset" );
        resetButton2.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                resetButton2_actionPerformed( e );
            }
        } );
        resetButton3.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        resetButton3.setToolTipText( "" );
        resetButton3.setText( "Reset" );
        resetButton3.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                resetButton3_actionPerformed( e );
            }
        } );
        controlPanelGridLayout.setColumns( 1 );
        controlPanelGridLayout.setRows( 0 );
        meanToggleButton.setFont( new java.awt.Font( "Dialog", 1, 9 ) );
        meanToggleButton
                .setToolTipText( "This turns on/off coloring using a mean value" );
        meanToggleButton.setText( "Mean" );
        meanToggleButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        meanToggleButton_actionPerformed( e );
                    }
                } );
        addComponentListener( new ComponentAdapter() {

            public void componentResized( final ComponentEvent e ) {
                adjustSize();
            }
        } );
        platesPanel.setLayout( platesPanelBorderLayout );
        jPanel4.setLayout( gridLayout2 );
        gridLayout2.setColumns( getMaxNumberOfReplicates() );
        gridLayout2.setRows( 0 );
        infoPanel.setLayout( borderLayout2 );
        idLabel.setText( "Accession " );
        nameLabel.setText( "Name" );
        valueLabel.setText( "Value" );
        descLabel.setText( "Description" );
        nameTextField.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        nameTextField.setDisabledTextColor( Color.white );
        nameTextField.setEditable( false );
        idTextField.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        idTextField.setDisabledTextColor( Color.white );
        idTextField.setEditable( false );
        valueTextField.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        valueTextField.setDisabledTextColor( Color.white );
        valueTextField.setEditable( false );
        descTextArea.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        descTextArea.setMinimumSize( new Dimension( 185, 15 ) );
        descTextArea.setPreferredSize( new Dimension( 185, 15 ) );
        descTextArea.setDisabledTextColor( Color.white );
        descTextArea.setEditable( false );
        descTextArea.setLineWrap( true );
        descTextArea.setRows( 1 );
        zoomMinusButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        zoomMinusButton.setText( "-" );
        zoomMinusButton.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                zoomMinusButton_actionPerformed( e );
            }
        } );
        zoomPlusButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        zoomPlusButton.setMaximumSize( new Dimension( 39, 23 ) );
        zoomPlusButton.setMinimumSize( new Dimension( 39, 23 ) );
        zoomPlusButton.setPreferredSize( new Dimension( 39, 23 ) );
        zoomPlusButton.setMnemonic( '0' );
        zoomPlusButton.setText( "+" );
        zoomPlusButton.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                zoomPlusButton_actionPerformed( e );
            }
        } );
        zoomLabel.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        zoomLabel.setText( "Zoom" );
        zoomResetButton.setFont( new java.awt.Font( "Dialog", 0, 8 ) );
        zoomResetButton.setText( "Reset" );
        zoomResetButton.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                zoomResetButton_actionPerformed( e );
            }
        } );
        zoomTextField.setFont( new java.awt.Font( "SansSerif", 0, 9 ) );
        zoomTextField.setPreferredSize( new Dimension( 30, 21 ) );
        zoomTextField.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                zoomTextField_actionPerformed( e );
            }
        } );
        minPanel.setBorder( BorderFactory.createEtchedBorder() );
        minPanel.setLayout( minPanelGridLayout );
        maxPanel.setBorder( BorderFactory.createEtchedBorder() );
        maxPanel.setLayout( maxPanelGridLayout );
        meanPanel.setForeground( Color.lightGray );
        meanPanel.setBorder( BorderFactory.createEtchedBorder() );
        meanPanel.setLayout( meanPanelGridLayout );
        zoomPanel.setBorder( BorderFactory.createEtchedBorder() );
        zoomPanel.setLayout( zoomPanelGridLayout );
        meanPanelGridLayout.setColumns( 1 );
        meanPanelGridLayout.setRows( 2 );
        minPanelGridLayout.setColumns( 1 );
        minPanelGridLayout.setRows( 2 );
        maxPanelGridLayout.setColumns( 1 );
        maxPanelGridLayout.setRows( 2 );
        actionPanel.setBorder( BorderFactory.createEtchedBorder() );
        actionPanel.setLayout( actionPanelgridLayout );
        unflagSelectedButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        unflagSelectedButton.setToolTipText( "this unflags the selected wells" );
        unflagSelectedButton.setText( "Unflag Selected" );
        unflagSelectedButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        unflagSelectedButton_actionPerformed( e );
                    }
                } );
        displaySelectedButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        displaySelectedButton
                .setToolTipText( "this displays the information for the selected wells" );
        displaySelectedButton.setText( "Display Selected" );
        displaySelectedButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        displaySelectedButton_actionPerformed( e );
                    }
                } );
        flagSelectedButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        flagSelectedButton.setToolTipText( "this flags the selected wells" );
        flagSelectedButton.setText( "Flag Selected" );
        flagSelectedButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        flagSelectedButton_actionPerformed( e );
                    }
                } );
        displayOptionsPanel.setBorder( BorderFactory.createEtchedBorder() );
        displayOptionsPanel.setLayout( displayOptionsPanelLayout );
        automaticRadioButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        automaticRadioButton
                .setToolTipText( "This draws wells as circles (larger) or as squares (smaller)" );
        automaticRadioButton.setSelected( true );
        automaticRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        automaticRadioButton_actionPerformed( e );
                    }
                } );
        circleRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        circleRadioButton_actionPerformed( e );
                    }
                } );
        squareRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        squareRadioButton_actionPerformed( e );
                    }
                } );
        unselectAllButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        unselectAllButton
                .setToolTipText( "this unselects (NOT unflags) all wells " );
        unselectAllButton.setText( "Unselect All" );
        unselectAllButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        unselectAllButton_actionPerformed( e );
                    }
                } );
        descriptionPanel.setLayout( borderLayout3 );
        showControlsCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        showControlsCheckBox
                .setToolTipText( "This shows positive (P), negative (N), and empty (E) control wells" );
        showControlsCheckBox.setSelected( true );
        showControlsCheckBox.setText( "Show Controls" );
        showControlsCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showControlsCheckBox_actionPerformed( e );
                    }
                } );
        circleRadioButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        circleRadioButton.setToolTipText( "This draws wells as circles" );
        squareRadioButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        squareRadioButton.setToolTipText( "This draws wells as squares" );
        actionPanel1.setLayout( flowLayout1 );
        actionPanel2.setLayout( flowLayout2 );
        actionPanelgridLayout.setRows( 2 );
        jPanel2.setLayout( gridLayout1 );
        jPanel1.setLayout( gridLayout3 );
        gridLayout3.setColumns( 1 );
        gridLayout3.setRows( 0 );
        colorScalePanel.setLayout( borderLayout1 );
        gridLayout1.setColumns( 1 );
        gridLayout1.setRows( 0 );
        idPanel.setLayout( gridLayout4 );
        namePanel.setLayout( gridLayout4 );
        valuePanel.setLayout( gridLayout4 );
        openURLButton.setEnabled( false );
        openURLButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        openURLButton
                .setToolTipText( "This is to view the corresponding entry in Symgene (DOES NOT WORK "
                        + "IF POP UPS ARE BLOCKED IN WEB BROWSER)" );
        openURLButton.setText( "Open in Symgene" );
        openURLButton.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed( final ActionEvent e ) {
                openURLButton_actionPerformed( e );
            }
        } );
        displayOptionsPanelLayout.setColumns( 1 );
        displayOptionsPanelLayout.setRows( 2 );
        ignoreFlaggedCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        ignoreFlaggedCheckBox
                .setToolTipText( "select this to ignore all flagged wells for min, max, and mean calculation" );
        ignoreFlaggedCheckBox.setSelected( true );
        ignoreFlaggedCheckBox.setText( "Flagged Wells" );
        ignoreFlaggedCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        ignoreFlaggedCheckBox_actionPerformed( e );
                    }
                } );
        ignoreControlsCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        ignoreControlsCheckBox
                .setToolTipText( "select this to ignore all control wells (P,N.E) for min, max, and "
                        + "mean calculation" );
        ignoreControlsCheckBox.setSelected( true );
        ignoreControlsCheckBox.setText( "Control Wells" );
        ignoreControlsCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        ignoreControlsCheckBox_actionPerformed( e );
                    }
                } );
        minMaxMeanIgnorePanel.setLayout( minMaxMeanIgnorePanelLayout );
        minMaxMeanIgnorePanelLayout.setColumns( 1 );
        minMaxMeanIgnorePanelLayout.setRows( 2 );
        minMaxMeanIgnorePanel.setBorder( BorderFactory.createEtchedBorder() );
        ignoreLabel.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        ignoreLabel.setToolTipText( "ingnore for color calculations" );
        ignoreLabel.setText( "Ignore:" );
        colorizePerPlateCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        colorizePerPlateCheckBox
                .setToolTipText( "Min, max, and mean are calculated on a per-plate basis as opposed "
                        + "to per-screen" );
        colorizePerPlateCheckBox.setText( "Per-Plate" );
        colorizePerPlateCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        colorizePerPlateCheckBox_actionPerformed( e );
                    }
                } );
        colorizeLabel.setFont( new java.awt.Font( "Dialog", 0, 10 ) );
        colorizeLabel.setText( "Normalize Colors:" );
        colorizePerReplicateCheckBox
                .setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        colorizePerReplicateCheckBox
                .setToolTipText( "Min, max, and mean are calculated on a per-replicate basis as opposed "
                        + "to per-screen" );
        colorizePerReplicateCheckBox.setText( "Per-Replicate" );
        colorizePerReplicateCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        colorizePerReplicateCheckBox_actionPerformed( e );
                    }
                } );
        zoomPanelGridLayout.setColumns( 1 );
        zoomPanelGridLayout.setRows( 2 );
        flagOutliersButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        flagOutliersButton.setText( "Flag Outliers" );
        flagOutliersButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        flagOutliersButton_actionPerformed( e );
                    }
                } );
        unflagOutliersButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        unflagOutliersButton.setText( "Unflag Outliers" );
        unflagOutliersButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        unflagOutliersButton_actionPerformed( e );
                    }
                } );
        showUserFlagsCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        showUserFlagsCheckBox
                .setToolTipText( "this is to show the (user set) flags as pink crosses (those which "
                        + "are also flagged by the statistical analysis are turquoise)" );
        showUserFlagsCheckBox.setSelected( true );
        showUserFlagsCheckBox.setText( "Show Flags" );
        showUserFlagsCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showUserFlagsCheckBox_actionPerformed( e );
                    }
                } );
        showOutliersCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        showOutliersCheckBox
                .setToolTipText( "this is to mark the wells which are flagged by the statistical analysis "
                        + "with turquoise lines or crosses" );
        showOutliersCheckBox.setSelected( true );
        showOutliersCheckBox.setText( "Show Outliers" );
        showOutliersCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showOutliersCheckBox_actionPerformed( e );
                    }
                } );
        hitsPanel.setBorder( BorderFactory.createEtchedBorder() );
        hitsPanel.setLayout( hitsPanelgridLayout );
        hitsPanelgridLayout.setColumns( 1 );
        hitsPanelgridLayout.setRows( 2 );
        showConfirmedHitsCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        showConfirmedHitsCheckBox
                .setToolTipText( "this is to show the confirmed hits as white \"<\"" );
        showConfirmedHitsCheckBox.setText( "Show Confirmed Hits" );
        showConfirmedHitsCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showConfirmedHitsCheckBox_actionPerformed( e );
                    }
                } );
        showHitPicksCheckBox.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        showHitPicksCheckBox
                .setToolTipText( "this is to show the hit picks as white \"H\"" );
        showHitPicksCheckBox.setText( "Show Hit Picks" );
        showHitPicksCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showHitPicksCheckBox_actionPerformed( e );
                    }
                } );
        removeHitPicksFormSelectedButton.setFont( new java.awt.Font( "Dialog",
                0, 9 ) );
        removeHitPicksFormSelectedButton.setToolTipText( "" );
        removeHitPicksFormSelectedButton.setText( "Remove Hitpicks" );
        hitPickSelectedButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        hitPickSelectedButton.setToolTipText( "" );
        hitPickSelectedButton.setText( "Hitpick Selected" );
        saveHitPickChangesButton.setEnabled( false );
        saveHitPickChangesButton.setFont( new java.awt.Font( "Dialog", 0, 9 ) );
        saveHitPickChangesButton.setToolTipText( "" );
        saveHitPickChangesButton.setText( "Save" );
        buttonGroup1.add( squareRadioButton );
        buttonGroup1.add( circleRadioButton );
        buttonGroup1.add( automaticRadioButton );
        squareRadioButton.setText( "Square" );
        circleRadioButton.setText( "Circle" );
        automaticRadioButton.setText( "Auto" );
        maxPanel.add( maxPanel1, null );
        maxPanel1.add( nameLabel2, null );
        maxPanel1.add( minusButton2, null );
        maxPanel1.add( valueTextField2, null );
        maxPanel1.add( plusButton2, null );
        maxPanel1.add( resetButton2, null );
        maxPanel.add( maxPanel2, null );
        maxPanel2.add( changeColorButton2, null );
        minPanel.add( minPanel1, null );
        minPanel1.add( nameLabel1, null );
        minPanel1.add( minusButton1, null );
        minPanel1.add( valueTextField1, null );
        minPanel1.add( plusButton1, null );
        minPanel1.add( resetButton1, null );
        minPanel.add( minPanel2, null );
        minPanel2.add( changeColorButton1, null );
        minPanel2.add( inverseMinMaxColorsButton, null );
        add( platesPanel, BorderLayout.CENTER );
        platesPanel.add( platesPanelScrollPane, BorderLayout.NORTH );
        add( controlPanel, BorderLayout.EAST );
        meanPanel.add( meanPanel1, null );
        meanPanel.add( meanPanel2, null );
        meanPanel2.add( changeColorButton3, null );
        meanPanel1.add( meanToggleButton, null );
        meanPanel1.add( minusButton3, null );
        meanPanel1.add( valueTextField3, null );
        meanPanel1.add( plusButton3, null );
        meanPanel1.add( resetButton3, null );
        actionPanel.add( actionPanel1, null );
        actionPanel1.add( flagSelectedButton, null );
        actionPanel1.add( unflagSelectedButton, null );
        actionPanel1.add( writeToDBButton, null );
        actionPanel.add( actionPanel2, null );
        actionPanel2.add( flagOutliersButton, null );
        actionPanel2.add( unflagOutliersButton, null );
        controlPanel.add( minPanel, null );
        controlPanel.add( maxPanel, null );
        controlPanel.add( meanPanel, null );
        controlPanel.add( minMaxMeanIgnorePanel, null );
        minMaxMeanIgnorePanel.add( minMaxMeanIgnorePanel1, null );
        minMaxMeanIgnorePanel1.add( ignoreLabel, null );
        minMaxMeanIgnorePanel1.add( ignoreControlsCheckBox, null );
        minMaxMeanIgnorePanel1.add( ignoreFlaggedCheckBox, null );
        minMaxMeanIgnorePanel.add( minMaxMeanIgnorePanel2, null );
        minMaxMeanIgnorePanel2.add( colorizeLabel, null );
        minMaxMeanIgnorePanel2.add( colorizePerPlateCheckBox, null );
        minMaxMeanIgnorePanel2.add( colorizePerReplicateCheckBox, null );
        // minMaxMeanIgnorePanel2.add(colorizePerPlateSeriesCheckBox, null);
        // Remove this functionalty for now (users did not consider it useful).
        // Although the whole infrastructure is still here.
        controlPanel.add( zoomPanel, null );
        controlPanel.add( displayOptionsPanel, null );
        displayOptionsPanel.add( displayOptionsPanel1, null );
        displayOptionsPanel1.add( showUserFlagsCheckBox, null );
        displayOptionsPanel1.add( showOutliersCheckBox, null );
        displayOptionsPanel1.add( showControlsCheckBox, null );
        displayOptionsPanel.add( displayOptionsPanel2, null );
        displayOptionsPanel2.add( displaySelectedButton, null );
        displayOptionsPanel2.add( unselectAllButton, null );
        // controlPanel.add(colorScalePanel, null); // ADD LATER!
        infoPanel.add( jPanel1, BorderLayout.CENTER );
        infoPanel.add( jPanel2, BorderLayout.WEST );
        jPanel2.add( idLabel, null );
        jPanel2.add( nameLabel, null );
        jPanel2.add( valueLabel, null );
        idPanel.add( idTextField, null );
        idPanel.add( openURLButton, null );
        namePanel.add( nameTextField, null );
        namePanel.add( searchButton, null );
        valuePanel.add( valueTextField, null );
        valuePanel.add( clearSearchButton, null );
        jPanel1.add( idPanel, null );
        jPanel1.add( namePanel, null );
        jPanel1.add( valuePanel, null );
        controlPanel.add( actionPanel, null );
        controlPanel.add( hitsPanel, null );
        hitsPanel.add( hitsPanel1, null );
        hitsPanel.add( hitsPanel2, null );
        controlPanel.add( infoPanel, null );
        controlPanel.add( descriptionPanel, null );
        descriptionPanel.add( descTextArea, BorderLayout.CENTER );
        descriptionPanel.add( descLabel, BorderLayout.WEST );
        add( NorthPanel, BorderLayout.NORTH );
        zoomPanel.add( zoomPanel1 );
        zoomPanel.add( zoomPanel2 );
        zoomPanel2.add( automaticRadioButton, null );
        zoomPanel2.add( squareRadioButton, null );
        zoomPanel2.add( circleRadioButton, null );
        zoomPanel1.add( zoomLabel, null );
        zoomPanel1.add( zoomMinusButton, null );
        zoomPanel1.add( zoomTextField, null );
        zoomPanel1.add( zoomPlusButton, null );
        zoomPanel1.add( zoomResetButton, null );
        hitsPanel1.add( showHitPicksCheckBox, null );
        hitsPanel1.add( showConfirmedHitsCheckBox, null );
        // hitsPanel2.add(hitPickSelectedButton, null);
        // hitsPanel2.add(removeHitPicksFormSelectedButton, null);
        // hitsPanel2.add(saveHitPickChangesButton, null);
    }

    void openURLButton_actionPerformed( final ActionEvent e ) {
        final String s = idTextField.getText().trim();
        if ( ( s != null ) && ( s.length() > 1 )
                && ( getParentHeatMapApplet() != null ) ) {
            try {
                final URL url = new URL( "" ); // FIXME
                System.out.println( "Trying to open: " + url );
                getParentHeatMapApplet().go( url );
            }
            catch ( final Exception ex ) {
                System.err.println( "Unable to open url " + ex );
            }
        }
    }

    /**
     * Performs the necessary recalculations if either ignoreControlsCheckBox or
     * ignoreFlaggedCheckBox changes selected state, or if flagging changed.
     * This is only called by method ignoreControlsCheckBox_actionPerformed,
     * ignoreFlaggedCheckBox_actionPerformed, and
     * setFlaggedStatusOfSelectedWells(boolean).
     * 
     * 
     */
    void recalculateMinMaxMeanAfterChange() {
        resetMinMaxMean( ignoreFlaggedCheckBox.isSelected(),
                ignoreControlsCheckBox.isSelected() );
        if ( colorizePerPlateCheckBox.isSelected() ) {
            resetMinMaxMeanPerPlate( ignoreFlaggedCheckBox.isSelected(),
                    ignoreControlsCheckBox.isSelected() );
        }
        else if ( colorizePerReplicateCheckBox.isSelected() ) {
            resetMinMaxMeanPerColumn( ignoreFlaggedCheckBox.isSelected(),
                    ignoreControlsCheckBox.isSelected() );
        }
        updateDisplay();
    }

    void searchButton_actionPerformed( final ActionEvent e ) {
        setResultsOfSearchShowing( false );
        final String s = JOptionPane
                .showInputDialog( "Please enter the string to search for in accession, name, and description" );
        if ( ( s != null ) && ( s.trim().length() > 0 ) ) {
            final int counter = findString( s, true, true, true, false );
            updateDisplay();
            if ( counter > 0 ) {
                setResultsOfSearchShowing( true );
                JOptionPane.showMessageDialog( this, "Found " + counter
                        + " instances of \"" + s + "\"" );
            }
            else {
                JOptionPane.showMessageDialog( this, "\"" + s + "\" not found" );
            }
        }
    }

    /**
     * This allows to change the flag of all "outlier" wells (flagged by a
     * statistical analysis, as oppesed to user-flagging).
     * 
     * 
     * @param set_flagged_to_this
     *            flag of "outlier" wells is set to this
     */
    void setFlaggedStatusOfOutlierWells( final boolean set_flagged_to_this ) {
        for( int i = 0; i < getPlateRendererLength(); ++i ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    pr.setFlaggedStatusOfOutlierWells( set_flagged_to_this );
                }
            }
        }
        recalculateMinMaxMeanAfterChange();
    }

    /**
     * This allows to change the flag of all selected wells.
     * 
     * @param set_flagged_to_this
     *            flag of selected wells is set to this
     */
    void setFlaggedStatusOfSelectedWells( final boolean set_flagged_to_this ) {
        for( int i = 0; i < getPlateRendererLength(); ++i ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    pr.setFlaggedStatusOfSelectedWells( set_flagged_to_this );
                }
            }
        }
        recalculateMinMaxMeanAfterChange();
    }

    /**
     * This sets flagging-status-changed of all wells to false. This is intended
     * to be called only after a successful write of all changes to the
     * database!
     * 
     */
    // private void setIsFlaggingStatusChangedToFalseForAllWells() {
    // for( int i = 0; i < getPlateRendererLength(); ++i ) {
    // for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
    // final PlateRenderer pr = getPlateRenderer( i, j );
    // if ( pr != null ) {
    // pr.setIsFlaggingStatusChangedToFalse();
    // }
    // }
    // }
    // updateDisplay();
    // }
    void showConfirmedHitsCheckBox_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    void showControlsCheckBox_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    void showHitPicksCheckBox_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    void showUserFlagsCheckBox_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    void unflagOutliersButton_actionPerformed( final ActionEvent e ) {
        final String s = getWellInformationAsString( true, false, true, false,
                false );
        final JTextArea ta = new JTextArea( s );
        final JScrollPane sp = new JScrollPane();
        sp.getViewport().setPreferredSize( new Dimension( 300, 300 ) );
        sp.getViewport().add( ta, null );
        ta.setEditable( false );
        final JOptionPane op = new JOptionPane( sp,
                JOptionPane.OK_CANCEL_OPTION );
        final int r = JOptionPane.showConfirmDialog( this, sp,
                "UNFLAG OUTLIERS (FLAGGED BY STATISTICAL ANALYSIS)",
                JOptionPane.OK_CANCEL_OPTION );
        if ( r == JOptionPane.OK_OPTION ) {
            showOutliersCheckBox.setSelected( true );
            showUserFlagsCheckBox.setSelected( true );
            setFlaggedStatusOfOutlierWells( false );
        }
    }

    void unflagSelectedButton_actionPerformed( final ActionEvent e ) {
        final String s = getWellInformationAsString( false, false, true, false,
                true );
        final JTextArea ta = new JTextArea( s );
        final JScrollPane sp = new JScrollPane();
        sp.getViewport().setPreferredSize( new Dimension( 300, 300 ) );
        sp.getViewport().add( ta, null );
        ta.setEditable( false );
        final JOptionPane op = new JOptionPane( sp,
                JOptionPane.OK_CANCEL_OPTION );
        final int r = JOptionPane.showConfirmDialog( this, sp,
                "UNFLAG SELECTED WELLS", JOptionPane.OK_CANCEL_OPTION );
        if ( r == JOptionPane.OK_OPTION ) {
            showUserFlagsCheckBox.setSelected( true );
            setFlaggedStatusOfSelectedWells( false );
        }
    }

    // Action performed methods
    // ========================
    void unselectAllButton_actionPerformed( final ActionEvent e ) {
        for( int i = 0; i < getPlateRendererLength(); ++i ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); ++j ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    pr.unSelectUnMarkAll();
                }
            }
        }
        setResultsOfSearchShowing( false );
        updateDisplay();
    }

    /**
     * This updates the well information displayed.
     * 
     * 
     * @param value
     *            the value
     * @param id
     *            the id or accession
     * @param name
     *            the gene/protein name
     * @param desc
     *            a description
     */
    public void updateInfo( final String value, final String id,
            final String name, final String desc ) {
        valueTextField.setText( value );
        idTextField.setText( id );
        nameTextField.setText( name );
        descTextArea.setText( desc );
        if ( ( id != null ) && ( id.length() > 1 ) ) {
            openURLButton.setEnabled( true );
        }
        else {
            openURLButton.setEnabled( false );
        }
    }
}
