// $Id: ControlsPanel.java,v 1.10 2010/12/13 18:59:25 cmzmasek Exp $
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

public class ControlsPanel extends JPanel {

    private static final Font  CONTROLS_FONT              = new java.awt.Font(
                                                                  "Verdana", 0,
                                                                  9 );
    private final static Color BACKGROUND_COLOR           = new Color( 0, 0, 0 );
    private final static Color TEXT_COLOR                 = new Color( 40, 140,
                                                                  240 );
    public final static int    WIDTH                      = 120;
    private static final long  serialVersionUID           = 2555463197380988758L;
    GridLayout                 gridLayout1                = new GridLayout();
    JCheckBox                  threeColorsCheckBox        = new JCheckBox();
    JCheckBox                  drawHistoCheckBox          = new JCheckBox();
    JCheckBox                  drawColoredBandCheckBox    = new JCheckBox();
    JCheckBox                  drawLineCheckBox           = new JCheckBox();
    JRadioButton               colorCountsRadioButton     = new JRadioButton();
    JRadioButton               colorPercentRadioButton    = new JRadioButton();
    JSlider                    magSlider                  = new JSlider();
    ButtonGroup                bg2                        = new ButtonGroup();
    JLabel                     jLabel1                    = new JLabel();
    JRadioButton               colorCumulativeRadioButton = new JRadioButton();
    JCheckBox                  display_minmax_cb          = new JCheckBox();
    JSlider                    colorSlider                = new JSlider();
    JLabel                     colorSlyderLabel           = new JLabel();
    JCheckBox                  diplay_counts_cb           = new JCheckBox();
    JPopupMenu                 hidePopupMenu              = new JPopupMenu();
    JButton                    showHidePopupButton        = new JButton();
    JButton                    closeHidePopupButton       = new JButton();
    private HistogramsCanvas   _hc;

    public ControlsPanel() {
        try {
            init();
        }
        catch ( final Exception e ) {
            System.err.println( "Problem with initialization: " + e );
        }
    }

    void closeHidePopupButton_actionPerformed( final ActionEvent e ) {
        hidePopupMenu.setVisible( false );
    }

    void colorCountsRadioButton_actionPerformed( final ActionEvent e ) {
        _hc.setColorCounts( colorCountsRadioButton.isSelected() );
        _hc.setColorPercentage( !colorCountsRadioButton.isSelected() );
        _hc.setColorCumulativePercentage( !colorCountsRadioButton.isSelected() );
        _hc.setThreeColors( !colorCountsRadioButton.isSelected() );
        threeColorsCheckBox.setSelected( !colorCountsRadioButton.isSelected() );
        colorSlyderLabel.setEnabled( false );
        colorSlider.setEnabled( false );
        _hc.repaint();
    }

    void colorCumulativeRadioButton_actionPerformed( final ActionEvent e ) {
        _hc.setColorCounts( !colorCumulativeRadioButton.isSelected() );
        _hc.setColorPercentage( !colorCumulativeRadioButton.isSelected() );
        _hc.setColorCumulativePercentage( colorCumulativeRadioButton
                .isSelected() );
        _hc.setThreeColors( colorCumulativeRadioButton.isSelected() );
        threeColorsCheckBox.setSelected( colorCumulativeRadioButton
                .isSelected() );
        colorSlyderLabel.setEnabled( true );
        colorSlider.setEnabled( true );
        _hc.repaint();
    }

    void colorPercentRadioButton_actionPerformed( final ActionEvent e ) {
        _hc.setColorCounts( !colorPercentRadioButton.isSelected() );
        _hc.setColorPercentage( colorPercentRadioButton.isSelected() );
        _hc.setColorCumulativePercentage( !colorCountsRadioButton.isSelected() );
        _hc.setThreeColors( colorPercentRadioButton.isSelected() );
        threeColorsCheckBox.setSelected( colorPercentRadioButton.isSelected() );
        colorSlyderLabel.setEnabled( true );
        colorSlider.setEnabled( true );
        _hc.repaint();
    }

    void colorSlider_stateChanged( final ChangeEvent e ) {
        if ( _hc != null ) {
            _hc.setColorMagnification( colorSlider.getValue() );
            _hc.repaint();
        }
    }

    void drawColoredBandCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setDrawColorizedBand( drawColoredBandCheckBox.isSelected() );
        _hc.repaint();
    }

    void drawHistoCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setDrawHistogram( drawHistoCheckBox.isSelected() );
        _hc.repaint();
    }

    void drawLineCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setDrawLine( drawLineCheckBox.isSelected() );
        _hc.repaint();
    }

    void init() throws Exception {
        setBackground( BACKGROUND_COLOR );
        setMinimumSize( new Dimension( ControlsPanel.WIDTH,
                HistogramsCanvas.HEIGTH ) );
        setPreferredSize( new Dimension( ControlsPanel.WIDTH,
                HistogramsCanvas.HEIGTH ) );
        setLayout( gridLayout1 );
        gridLayout1.setColumns( 1 );
        gridLayout1.setRows( 0 );
        threeColorsCheckBox.setBackground( BACKGROUND_COLOR );
        threeColorsCheckBox.setFont( CONTROLS_FONT );
        threeColorsCheckBox.setForeground( TEXT_COLOR );
        threeColorsCheckBox
                .setToolTipText( "To use a three color schema instead of a two color schema." );
        threeColorsCheckBox.setText( "Three Colors" );
        threeColorsCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        threeColorsCheckBox_actionPerformed( e );
                    }
                } );
        drawHistoCheckBox.setBackground( BACKGROUND_COLOR );
        drawHistoCheckBox.setFont( CONTROLS_FONT );
        drawHistoCheckBox.setForeground( TEXT_COLOR );
        drawHistoCheckBox
                .setToolTipText( "To draw filled (color coded) histograms." );
        drawHistoCheckBox.setText( "Color Histograms" );
        drawHistoCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        drawHistoCheckBox_actionPerformed( e );
                    }
                } );
        drawColoredBandCheckBox.setBackground( BACKGROUND_COLOR );
        drawColoredBandCheckBox.setFont( CONTROLS_FONT );
        drawColoredBandCheckBox.setForeground( TEXT_COLOR );
        drawColoredBandCheckBox
                .setToolTipText( "To draw a colored coded background." );
        drawColoredBandCheckBox.setText( "Color Background" );
        drawColoredBandCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        drawColoredBandCheckBox_actionPerformed( e );
                    }
                } );
        drawLineCheckBox.setBackground( BACKGROUND_COLOR );
        drawLineCheckBox.setFont( CONTROLS_FONT );
        drawLineCheckBox.setForeground( TEXT_COLOR );
        drawLineCheckBox
                .setToolTipText( "To draw a line indicating the shap of the histograms." );
        drawLineCheckBox.setText( "Draw Line" );
        drawLineCheckBox
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        drawLineCheckBox_actionPerformed( e );
                    }
                } );
        colorCountsRadioButton.setBackground( BACKGROUND_COLOR );
        colorCountsRadioButton.setFont( CONTROLS_FONT );
        colorCountsRadioButton.setForeground( TEXT_COLOR );
        colorCountsRadioButton
                .setToolTipText( "To color according to counts/frequency." );
        colorCountsRadioButton.setText( "Color Counts" );
        colorCountsRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        colorCountsRadioButton_actionPerformed( e );
                    }
                } );
        colorPercentRadioButton.setBackground( BACKGROUND_COLOR );
        colorPercentRadioButton.setFont( CONTROLS_FONT );
        colorPercentRadioButton.setForeground( TEXT_COLOR );
        colorPercentRadioButton
                .setToolTipText( "To color according to (centered) percentiles." );
        colorPercentRadioButton.setText( "Color Percentiles" );
        colorPercentRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        colorPercentRadioButton_actionPerformed( e );
                    }
                } );
        magSlider.addChangeListener( new javax.swing.event.ChangeListener() {

            public void stateChanged( final ChangeEvent e ) {
                magSlider_stateChanged( e );
            }
        } );
        colorSlider.addChangeListener( new javax.swing.event.ChangeListener() {

            public void stateChanged( final ChangeEvent e ) {
                colorSlider_stateChanged( e );
            }
        } );
        magSlider.setMinorTickSpacing( 2 );
        magSlider.setPaintLabels( true );
        magSlider.setPaintTicks( true );
        magSlider.setBackground( BACKGROUND_COLOR );
        magSlider.setFont( CONTROLS_FONT );
        magSlider.setForeground( TEXT_COLOR );
        magSlider.setToolTipText( "To magnify the counts/frequency." );
        jLabel1.setFont( CONTROLS_FONT );
        jLabel1.setForeground( TEXT_COLOR );
        jLabel1.setToolTipText( "To magnify the counts/frequency." );
        jLabel1.setHorizontalAlignment( SwingConstants.CENTER );
        jLabel1.setText( "Magnification:" );
        colorCumulativeRadioButton.setBackground( BACKGROUND_COLOR );
        colorCumulativeRadioButton.setFont( CONTROLS_FONT );
        colorCumulativeRadioButton.setForeground( TEXT_COLOR );
        colorCumulativeRadioButton
                .setToolTipText( "To color according to cumulative percentiles." );
        colorCumulativeRadioButton.setText( "Color Cumulative %" );
        colorCumulativeRadioButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        colorCumulativeRadioButton_actionPerformed( e );
                    }
                } );
        display_minmax_cb.setBackground( BACKGROUND_COLOR );
        display_minmax_cb.setFont( CONTROLS_FONT );
        display_minmax_cb.setForeground( TEXT_COLOR );
        display_minmax_cb
                .setToolTipText( "To show the highest and lowest values in the histograms." );
        display_minmax_cb.setText( "Min/Max" );
        display_minmax_cb
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        labelCheckBox_actionPerformed( e );
                    }
                } );
        colorSlider.setMinorTickSpacing( 50 );
        colorSlider.setPaintLabels( true );
        colorSlider.setPaintTicks( true );
        colorSlider.setBackground( BACKGROUND_COLOR );
        colorSlider.setForeground( TEXT_COLOR );
        colorSlider.setToolTipText( "To adjust the color coding." );
        colorSlyderLabel.setFont( CONTROLS_FONT );
        colorSlyderLabel.setForeground( TEXT_COLOR );
        colorSlyderLabel.setToolTipText( "To adjust the color coding." );
        colorSlyderLabel.setHorizontalAlignment( SwingConstants.CENTER );
        colorSlyderLabel.setText( "Color:" );
        colorSlider.setMinimum( HistogramsCanvas.COLOR_SLIDER_MIN + 1 );
        colorSlider.setMaximum( HistogramsCanvas.COLOR_SLIDER_MAX - 1 );
        colorSlider.setValue( HistogramsCanvas.COLOR_SLIDER_MEAN );
        diplay_counts_cb.setBackground( BACKGROUND_COLOR );
        diplay_counts_cb.setFont( CONTROLS_FONT );
        diplay_counts_cb.setForeground( TEXT_COLOR );
        diplay_counts_cb.setToolTipText( "To display information." );
        diplay_counts_cb.setText( "Information" );
        diplay_counts_cb
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showGeneNamesCheckBox_actionPerformed( e );
                    }
                } );
        showHidePopupButton.setBackground( BACKGROUND_COLOR );
        showHidePopupButton.setFont( CONTROLS_FONT );
        showHidePopupButton.setForeground( TEXT_COLOR );
        showHidePopupButton.setToolTipText( "To hide individual histograms" );
        showHidePopupButton.setText( "Hide Histograms" );
        showHidePopupButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        showHidePopupButton_actionPerformed( e );
                    }
                } );
        closeHidePopupButton.setFont( CONTROLS_FONT );
        closeHidePopupButton.setText( "Close" );
        closeHidePopupButton
                .addActionListener( new java.awt.event.ActionListener() {

                    public void actionPerformed( final ActionEvent e ) {
                        closeHidePopupButton_actionPerformed( e );
                    }
                } );
        bg2.add( colorCountsRadioButton );
        bg2.add( colorPercentRadioButton );
        bg2.add( colorCumulativeRadioButton );
        magSlider.setMinimum( 1 );
        magSlider.setMaximum( 15 );
        magSlider.setValue( 1 );
        this.add( drawHistoCheckBox, null );
        this.add( drawColoredBandCheckBox, null );
        this.add( drawLineCheckBox, null );
        this.add( diplay_counts_cb, null );
        this.add( display_minmax_cb, null );
        this.add( threeColorsCheckBox, null );
        this.add( colorCountsRadioButton, null );
        this.add( colorPercentRadioButton, null );
        this.add( colorCumulativeRadioButton, null );
        this.add( showHidePopupButton, null );
        this.add( jLabel1, null );
        this.add( magSlider, null );
        this.add( colorSlyderLabel, null );
        this.add( colorSlider, null );
    }

    void labelCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setShowLabels( display_minmax_cb.isSelected() );
        _hc.repaint();
    }

    void magSlider_stateChanged( final ChangeEvent e ) {
        if ( _hc != null ) {
            _hc.setMagnification( magSlider.getValue() * magSlider.getValue() );
            _hc.repaint();
        }
    }

    public void setHistogramsCanvas( final HistogramsCanvas hc ) {
        _hc = hc;
        threeColorsCheckBox.setSelected( _hc.isThreeColors() );
        drawHistoCheckBox.setSelected( _hc.isDrawHistogram() );
        drawColoredBandCheckBox.setSelected( _hc.isDrawColorizedBand() );
        drawLineCheckBox.setSelected( _hc.isDrawLine() );
        colorCountsRadioButton.setSelected( _hc.isColorCounts() );
        colorPercentRadioButton.setSelected( _hc.isColorPercentage() );
        display_minmax_cb.setSelected( _hc.isShowLabels() );
        diplay_counts_cb.setSelected( _hc.isShowGeneNames() );
        colorSlyderLabel.setEnabled( !_hc.isColorCounts() );
        colorSlider.setEnabled( !_hc.isColorCounts() );
        hidePopupMenu.removeAll();
        for( int i = 0; i < _hc.getHistogramNames().length; ++i ) {
            final JCheckBox cb = new JCheckBox( _hc.getHistogramNames()[ i ] );
            cb.setSelected( !_hc.isHideHistogram( i ) );
            cb.setFont( CONTROLS_FONT );
            final int final_i = i;
            cb.addActionListener( new java.awt.event.ActionListener() {

                public void actionPerformed( final ActionEvent e ) {
                    System.out.println( "action performed" );
                    toggleHidingOfhistograms( final_i );
                }
            } );
            hidePopupMenu.add( cb );
        }
        // hidePopupMenu.add( new Separator() );
        hidePopupMenu.add( closeHidePopupButton );
    }

    void showGeneNamesCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setShowGeneNames( diplay_counts_cb.isSelected() );
        _hc.repaint();
    }

    void showHidePopupButton_actionPerformed( final ActionEvent e ) {
        hidePopupMenu.show( this, 10, 10 );
    }

    void threeColorsCheckBox_actionPerformed( final ActionEvent e ) {
        _hc.setThreeColors( threeColorsCheckBox.isSelected() );
        _hc.repaint();
    }

    private void toggleHidingOfhistograms( final int i ) {
        _hc.toggleHideHistogram( i );
        _hc.resetBarWidth();
        _hc.repaint();
    }
}
