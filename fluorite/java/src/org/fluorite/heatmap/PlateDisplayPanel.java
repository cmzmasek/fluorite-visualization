// $Id: PlateDisplayPanel.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.fluorite.util.FluoriteUtil;

public abstract class PlateDisplayPanel extends JPanel {

    public static final int   NUMBER_OF_STEPS    = 20;
    public static final int   DEFAULT_WELL_SIZE  = 10;
    public static final int   MIN_WELL_SIZE      = 2;
    public static final int   WIDTH_OF_SCROLLBAR = 40;
    public static final Color MIN_COLOR          = new Color( 0, 0, 255 );
    public static final Color MAX_COLOR          = new Color( 0, 255, 0 );
    public static final Color MEAN_COLOR         = new Color( 0, 0, 0 );
    public static String      FONT               = "SansSerif";
    protected PlateRenderer   _platerenderers[][];
    protected int             _wellSize;
    protected int             _currentlyMarkedRow;
    protected int             _maxNumberOfReplicates;
    protected double          _currentMin;
    protected double          _currentMax;
    protected double          _currentMean;
    protected double          _actualMin;
    protected double          _actualMax;
    protected double          _actualMean;
    protected double          _stepSize;
    protected boolean         _useMean;
    protected Color           _minColor;
    protected Color           _maxColor;
    protected Color           _meanColor;
    protected Font            _wellLabelFont;
    protected Font            _plateTitleFont;
    protected Font            _controlFont;
    protected boolean         _resultsOfSearchShowing;
    protected BorderLayout    frameBorderLayout;
    protected JPanel          platesPanel;
    protected JPanel          controlPanel;
    protected JPanel          jPanel3;
    protected JPanel          jPanel4;
    protected JPanel          meanPanel;
    protected JPanel          maxPanel;
    protected JPanel          minPanel;
    protected JPanel          NorthPanel;
    protected JButton         minusButton1;
    protected JButton         plusButton1;
    protected JButton         resetButton1;
    protected JButton         changeColorButton1;
    protected JTextField      valueTextField1;
    protected JLabel          nameLabel1;
    protected JButton         minusButton2;
    protected JButton         plusButton2;
    protected JButton         resetButton2;
    protected JButton         changeColorButton2;
    protected JTextField      valueTextField2;
    protected JLabel          nameLabel2;
    protected JButton         minusButton3;
    protected JButton         plusButton3;
    protected JButton         resetButton3;
    protected JButton         changeColorButton3;
    protected JTextField      valueTextField3;
    protected GridLayout      controlPanelGridLayout;
    protected JToggleButton   meanToggleButton;
    protected JScrollPane     platesPanelScrollPane;
    protected BorderLayout    platesPanelBorderLayout;
    protected GridLayout      gridLayout2;
    protected JPanel          zoomPanel;
    protected JPanel          infoPanel;
    protected JLabel          idLabel;
    protected JTextField      idTextField;
    protected JLabel          nameLabel;
    protected JTextField      nameTextField;
    protected JLabel          valueLabel;
    protected JTextField      valueTextField;
    protected JLabel          descLabel;
    protected JTextArea       descTextArea;
    protected JButton         zoomMinusButton;
    protected JTextField      zoomTextField;
    protected JButton         zoomPlusButton;
    protected JLabel          zoomLabel;
    protected JButton         zoomResetButton;
    protected JPanel          meanPanel1;
    protected JPanel          meanPanel2;
    protected GridLayout      meanPanelGridLayout;
    protected JPanel          minPanel2;
    protected JPanel          minPanel1;
    protected JPanel          maxPanel2;
    protected JPanel          maxPanel1;
    protected GridLayout      minPanelGridLayout;
    protected GridLayout      maxPanelGridLayout;
    protected JPanel          actionPanel;
    protected JButton         unflagSelectedButton;
    protected JButton         displaySelectedButton;
    protected JButton         writeToDBButton;
    protected JButton         flagSelectedButton;
    protected JPanel          colorScalePanel;
    protected JPanel          displayOptionsPanel;
    protected JRadioButton    squareRadioButton;
    protected JRadioButton    circleRadioButton;
    protected JRadioButton    automaticRadioButton;
    protected ButtonGroup     buttonGroup1;
    protected JButton         unselectAllButton;
    protected JPanel          descriptionPanel;
    protected JCheckBox       showControlsCheckBox;
    protected JPanel          actionPanel2;
    protected JPanel          actionPanel1;
    protected FlowLayout      flowLayout1;
    protected FlowLayout      flowLayout2;
    protected GridLayout      actionPanelgridLayout;
    protected JPanel          jPanel1;
    protected JPanel          jPanel2;
    protected GridLayout      gridLayout3;
    protected BorderLayout    borderLayout1;
    protected GridLayout      gridLayout1;
    protected BorderLayout    borderLayout2;
    protected BorderLayout    borderLayout3;
    protected JPanel          idPanel;
    protected GridLayout      gridLayout4;
    protected JButton         openURLButton;
    protected JPanel          displayOptionsPanel1;
    protected JPanel          displayOptionsPanel2;
    protected GridLayout      displayOptionsPanelLayout;
    protected JPanel          minMaxMeanIgnorePanel;
    protected JCheckBox       ignoreFlaggedCheckBox;
    protected JCheckBox       ignoreControlsCheckBox;
    protected JPanel          minMaxMeanIgnorePanel2;
    protected JPanel          minMaxMeanIgnorePanel1;
    protected JPanel          zoomPanel1;
    protected JPanel          zoomPanel2;
    protected GridLayout      minMaxMeanIgnorePanelLayout;
    protected JLabel          ignoreLabel;
    protected JCheckBox       colorizePerPlateCheckBox;
    protected JCheckBox       colorizePerReplicateCheckBox;
    protected JLabel          colorizeLabel;
    protected GridLayout      zoomPanelGridLayout;
    protected JButton         flagOutliersButton;
    protected JButton         unflagOutliersButton;
    protected JCheckBox       showUserFlagsCheckBox;
    protected JCheckBox       showOutliersCheckBox;
    protected JPanel          hitsPanel;
    protected JPanel          hitsPanel1;
    protected JPanel          hitsPanel2;
    protected GridLayout      hitsPanelgridLayout;
    protected JCheckBox       showConfirmedHitsCheckBox;
    protected JCheckBox       showHitPicksCheckBox;
    protected JButton         removeHitPicksFormSelectedButton;
    protected JButton         hitPickSelectedButton;
    protected JButton         saveHitPickChangesButton;
    protected JButton         inverseMinMaxColorsButton;
    protected JButton         searchButton;
    protected JButton         clearSearchButton;
    protected JPanel          namePanel;
    protected JPanel          valuePanel;

    public PlateDisplayPanel() {
        frameBorderLayout = new BorderLayout();
        platesPanel = new JPanel();
        controlPanel = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        meanPanel = new JPanel();
        maxPanel = new JPanel();
        minPanel = new JPanel();
        NorthPanel = new JPanel();
        minusButton1 = new JButton();
        plusButton1 = new JButton();
        resetButton1 = new JButton();
        changeColorButton1 = new JButton();
        valueTextField1 = new JTextField();
        nameLabel1 = new JLabel();
        minusButton2 = new JButton();
        plusButton2 = new JButton();
        resetButton2 = new JButton();
        changeColorButton2 = new JButton();
        valueTextField2 = new JTextField();
        nameLabel2 = new JLabel();
        minusButton3 = new JButton();
        plusButton3 = new JButton();
        resetButton3 = new JButton();
        changeColorButton3 = new JButton();
        valueTextField3 = new JTextField();
        controlPanelGridLayout = new GridLayout();
        meanToggleButton = new JToggleButton();
        platesPanelScrollPane = new JScrollPane();
        platesPanelBorderLayout = new BorderLayout();
        gridLayout2 = new GridLayout();
        zoomPanel = new JPanel();
        infoPanel = new JPanel();
        idLabel = new JLabel();
        idTextField = new JTextField();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        valueLabel = new JLabel();
        valueTextField = new JTextField();
        descLabel = new JLabel();
        descTextArea = new JTextArea();
        zoomMinusButton = new JButton();
        zoomTextField = new JTextField();
        zoomPlusButton = new JButton();
        zoomLabel = new JLabel();
        zoomResetButton = new JButton();
        meanPanel1 = new JPanel();
        meanPanel2 = new JPanel();
        meanPanelGridLayout = new GridLayout();
        minPanel2 = new JPanel();
        minPanel1 = new JPanel();
        maxPanel2 = new JPanel();
        maxPanel1 = new JPanel();
        minPanelGridLayout = new GridLayout();
        maxPanelGridLayout = new GridLayout();
        actionPanel = new JPanel();
        unflagSelectedButton = new JButton();
        displaySelectedButton = new JButton();
        writeToDBButton = new JButton();
        flagSelectedButton = new JButton();
        colorScalePanel = new JPanel();
        displayOptionsPanel = new JPanel();
        squareRadioButton = new JRadioButton();
        circleRadioButton = new JRadioButton();
        automaticRadioButton = new JRadioButton();
        buttonGroup1 = new ButtonGroup();
        unselectAllButton = new JButton();
        descriptionPanel = new JPanel();
        showControlsCheckBox = new JCheckBox();
        actionPanel2 = new JPanel();
        actionPanel1 = new JPanel();
        flowLayout1 = new FlowLayout();
        flowLayout2 = new FlowLayout();
        actionPanelgridLayout = new GridLayout();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        gridLayout3 = new GridLayout();
        borderLayout1 = new BorderLayout();
        gridLayout1 = new GridLayout();
        borderLayout2 = new BorderLayout();
        borderLayout3 = new BorderLayout();
        idPanel = new JPanel();
        gridLayout4 = new GridLayout();
        openURLButton = new JButton();
        displayOptionsPanel1 = new JPanel();
        displayOptionsPanel2 = new JPanel();
        displayOptionsPanelLayout = new GridLayout();
        minMaxMeanIgnorePanel = new JPanel();
        ignoreFlaggedCheckBox = new JCheckBox();
        ignoreControlsCheckBox = new JCheckBox();
        minMaxMeanIgnorePanel2 = new JPanel();
        minMaxMeanIgnorePanel1 = new JPanel();
        zoomPanel1 = new JPanel();
        zoomPanel2 = new JPanel();
        minMaxMeanIgnorePanelLayout = new GridLayout();
        ignoreLabel = new JLabel();
        colorizePerPlateCheckBox = new JCheckBox();
        colorizePerReplicateCheckBox = new JCheckBox();
        colorizeLabel = new JLabel();
        zoomPanelGridLayout = new GridLayout();
        flagOutliersButton = new JButton();
        unflagOutliersButton = new JButton();
        showUserFlagsCheckBox = new JCheckBox();
        showOutliersCheckBox = new JCheckBox();
        hitsPanel = new JPanel();
        hitsPanel1 = new JPanel();
        hitsPanel2 = new JPanel();
        hitsPanelgridLayout = new GridLayout();
        showConfirmedHitsCheckBox = new JCheckBox();
        showHitPicksCheckBox = new JCheckBox();
        removeHitPicksFormSelectedButton = new JButton();
        hitPickSelectedButton = new JButton();
        saveHitPickChangesButton = new JButton();
        inverseMinMaxColorsButton = new JButton();
        searchButton = new JButton();
        clearSearchButton = new JButton();
        namePanel = new JPanel();
        valuePanel = new JPanel();
    }

    public void adjustSize() {
        platesPanelScrollPane.getViewport().setPreferredSize(
                new Dimension( ( int ) getParent().getSize().getWidth(),
                        ( int ) getParent().getSize().getHeight() - 40 ) );
        platesPanelScrollPane.getViewport().revalidate();
        revalidate();
    }

    public void automaticRadioButton_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    public void changeColorButton1_actionPerformed( final ActionEvent e ) {
        final Color color = JColorChooser.showDialog( this, "Pick A Color",
                getMinColor() );
        if ( color != null ) {
            setMinColor( color );
            updateDisplay();
        }
    }

    public void changeColorButton2_actionPerformed( final ActionEvent e ) {
        final Color color = JColorChooser.showDialog( this, "Pick A Color",
                getMaxColor() );
        if ( color != null ) {
            setMaxColor( color );
            updateDisplay();
        }
    }

    public void changeColorButton3_actionPerformed( final ActionEvent e ) {
        final Color color = JColorChooser.showDialog( this, "Pick A Color",
                getMeanColor() );
        if ( color != null ) {
            setMeanColor( color );
            updateDisplay();
        }
    }

    public void circleRadioButton_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    public void colorizePerPlateCheckBox_actionPerformed( final ActionEvent e ) {
        if ( colorizePerPlateCheckBox.isSelected() ) {
            colorizePerReplicateCheckBox.setSelected( false );
            resetMinMaxMeanPerPlate( ignoreFlaggedCheckBox.isSelected(),
                    ignoreControlsCheckBox.isSelected() );
            setEnabledOfMinMaxMeanChangingItems( false );
        }
        else {
            setEnabledOfMinMaxMeanChangingItems( true );
        }
        updateDisplay();
    }

    public int findString( final String searchString,
            final boolean inAccession, final boolean inName,
            final boolean inDescpription, final boolean exactMatch ) {
        int counter = 0;
        boolean first = true;
        final String ss = searchString.toUpperCase().trim();
        for( int i = 0; i < getPlateRendererLength(); i++ ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); j++ ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    final int hits = pr.findString( ss, inAccession, inName,
                            inDescpription, exactMatch );
                    counter += hits;
                    if ( first && ( hits > 0 ) ) {
                        first = false;
                        final double y = platesPanelScrollPane.getViewport()
                                .getViewSize().getHeight();
                        final double yy = y
                                * ( ( double ) i / ( double ) getPlateRendererLength() );
                        final Point p = new Point( 0, ( int ) yy );
                        platesPanelScrollPane.getViewport().setViewPosition( p );
                    }
                }
            }
        }
        return counter / getMaxNumberOfReplicates();
    }

    public double getActualMax() {
        return _actualMax;
    }

    public double getActualMean() {
        return _actualMean;
    }

    public double getActualMin() {
        return _actualMin;
    }

    public Font getControlFont() {
        return _controlFont;
    }

    public int getCurrentlyMarkedRow() {
        return _currentlyMarkedRow;
    }

    public double getCurrentMax() {
        return _currentMax;
    }

    public double getCurrentMean() {
        return _currentMean;
    }

    public double getCurrentMin() {
        return _currentMin;
    }

    public Color getMaxColor() {
        return _maxColor;
    }

    public int getMaxNumberOfReplicates() {
        return _maxNumberOfReplicates;
    }

    public Color getMeanColor() {
        return _meanColor;
    }

    public Color getMinColor() {
        return _minColor;
    }

    public PlateRenderer getPlateRenderer( final int row, final int col ) {
        return _platerenderers[ row ][ col ];
    }

    public int getPlateRendererLength() {
        return _platerenderers.length;
    }

    public Font getPlateTitleFont() {
        return _plateTitleFont;
    }

    public double getStepSize() {
        return _stepSize;
    }

    public Font getWellLabelFont() {
        return _wellLabelFont;
    }

    public int getWellSize() {
        return _wellSize;
    }

    public boolean isDrawCircle() {
        return circleRadioButton.isSelected();
    }

    public boolean isDrawSquare() {
        return squareRadioButton.isSelected();
    }

    public boolean isResultsOfSearchShowing() {
        return _resultsOfSearchShowing;
    }

    public boolean isShowControls() {
        return showControlsCheckBox.isSelected();
    }

    public boolean isUseMean() {
        return _useMean;
    }

    public abstract void jbInit() throws Exception;

    public void meanToggleButton_actionPerformed( final ActionEvent e ) {
        if ( meanToggleButton.isSelected() ) {
            setUseMean( true );
            changeColorButton3.setEnabled( true );
            if ( !colorizePerPlateCheckBox.isSelected()
                    && !colorizePerReplicateCheckBox.isSelected() ) {
                minusButton3.setEnabled( true );
                plusButton3.setEnabled( true );
                resetButton3.setEnabled( true );
                valueTextField3.setEnabled( true );
            }
        }
        else {
            setUseMean( false );
            changeColorButton3.setEnabled( false );
            minusButton3.setEnabled( false );
            plusButton3.setEnabled( false );
            resetButton3.setEnabled( false );
            valueTextField3.setEnabled( false );
        }
        updateDisplay();
    }

    public void minusButton1_actionPerformed( final ActionEvent e ) {
        setCurrentMin( getCurrentMin() - getStepSize() );
        updateDisplay();
    }

    public void minusButton2_actionPerformed( final ActionEvent e ) {
        setCurrentMax( getCurrentMax() - getStepSize() );
        updateDisplay();
    }

    public void minusButton3_actionPerformed( final ActionEvent e ) {
        setCurrentMean( getCurrentMean() - getStepSize() );
        updateDisplay();
    }

    public void plusButton1_actionPerformed( final ActionEvent e ) {
        setCurrentMin( getCurrentMin() + getStepSize() );
        updateDisplay();
    }

    public void plusButton2_actionPerformed( final ActionEvent e ) {
        setCurrentMax( getCurrentMax() + getStepSize() );
        updateDisplay();
    }

    public void plusButton3_actionPerformed( final ActionEvent e ) {
        setCurrentMean( getCurrentMean() + getStepSize() );
        updateDisplay();
    }

    public void resetButton1_actionPerformed( final ActionEvent e ) {
        setCurrentMin( getActualMin() );
        updateDisplay();
    }

    public void resetButton2_actionPerformed( final ActionEvent e ) {
        setCurrentMax( getActualMax() );
        updateDisplay();
    }

    public void resetButton3_actionPerformed( final ActionEvent e ) {
        setCurrentMean( getActualMean() );
        updateDisplay();
    }

    public void resetMinMaxMean( final boolean ignoreFlagged,
            final boolean ignoreControls ) {
        double min = 1.7976931348623157E+308D;
        double max = 4.9406564584124654E-324D;
        BigDecimal sum = new BigDecimal( 0.0D );
        int n = 0;
        for( int i = 0; i < getPlateRendererLength(); i++ ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); j++ ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    final double plate_min = pr.calculateMinValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_max = pr.calculateMaxValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_sum = pr.calculateSumOfValuesOfWells(
                            ignoreFlagged, ignoreControls );
                    if ( plate_min < min ) {
                        min = plate_min;
                    }
                    if ( plate_max > max ) {
                        max = plate_max;
                    }
                    n += pr.calculateNumberOWells( ignoreFlagged,
                            ignoreControls );
                    sum = sum.add( new BigDecimal( plate_sum ) );
                }
            }
        }
        _actualMax = _currentMax = max;
        _actualMin = _currentMin = min;
        try {
            _actualMean = _currentMean = sum.divide( new BigDecimal( n ), 6 )
                    .doubleValue();
        }
        catch ( Exception e ) {
            // TODO: handle exception
            e.printStackTrace();
        }
        setStepSize( ( getActualMax() - getActualMin() ) / 20D );
    }

    public void resetMinMaxMeanPerColumn( final boolean ignoreFlagged,
            final boolean ignoreControls ) {
        for( int col = 0; col < getMaxNumberOfReplicates(); col++ ) {
            double min = 1.7976931348623157E+308D;
            double max = 4.9406564584124654E-324D;
            double mean = 0.0D;
            BigDecimal sum = new BigDecimal( 0.0D );
            int n = 0;
            for( int row = 0; row < getPlateRendererLength(); row++ ) {
                final PlateRenderer pr = getPlateRenderer( row, col );
                if ( pr != null ) {
                    final double plate_min = pr.calculateMinValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_max = pr.calculateMaxValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_sum = pr.calculateSumOfValuesOfWells(
                            ignoreFlagged, ignoreControls );
                    if ( plate_min < min ) {
                        min = plate_min;
                    }
                    if ( plate_max > max ) {
                        max = plate_max;
                    }
                    n += pr.calculateNumberOWells( ignoreFlagged,
                            ignoreControls );
                    sum = sum.add( new BigDecimal( plate_sum ) );
                }
            }
            mean = sum.divide( new BigDecimal( n ), 6 ).doubleValue();
            for( int row = 0; row < getPlateRendererLength(); row++ ) {
                final PlateRenderer pr = getPlateRenderer( row, col );
                if ( pr != null ) {
                    pr.setMin( min );
                    pr.setMax( max );
                    pr.setMean( mean );
                }
            }
        }
    }

    public void resetMinMaxMeanPerPlate( final boolean ignoreFlagged,
            final boolean ignoreControls ) {
        for( int i = 0; i < getPlateRendererLength(); i++ ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); j++ ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    final double plate_min = pr.calculateMinValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_max = pr.calculateMaxValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_sum = pr.calculateSumOfValuesOfWells(
                            ignoreFlagged, ignoreControls );
                    final int plate_n = pr.calculateNumberOWells(
                            ignoreFlagged, ignoreControls );
                    pr.setMin( plate_min );
                    pr.setMax( plate_max );
                    pr.setMean( plate_sum / plate_n );
                }
            }
        }
    }

    public void resetMinMaxMeanPerRow( final boolean ignoreFlagged,
            final boolean ignoreControls ) {
        for( int row = 0; row < getPlateRendererLength(); row++ ) {
            double min = 1.7976931348623157E+308D;
            double max = 4.9406564584124654E-324D;
            double mean = 0.0D;
            BigDecimal sum = new BigDecimal( 0.0D );
            int n = 0;
            for( int col = 0; col < getMaxNumberOfReplicates(); col++ ) {
                final PlateRenderer pr = getPlateRenderer( row, col );
                if ( pr != null ) {
                    final double plate_min = pr.calculateMinValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_max = pr.calculateMaxValueOfWells(
                            ignoreFlagged, ignoreControls );
                    final double plate_sum = pr.calculateSumOfValuesOfWells(
                            ignoreFlagged, ignoreControls );
                    if ( plate_min < min ) {
                        min = plate_min;
                    }
                    if ( plate_max > max ) {
                        max = plate_max;
                    }
                    n += pr.calculateNumberOWells( ignoreFlagged,
                            ignoreControls );
                    sum = sum.add( new BigDecimal( plate_sum ) );
                }
            }
            mean = sum.divide( new BigDecimal( n ), 6 ).doubleValue();
            for( int col = 0; col < getMaxNumberOfReplicates(); col++ ) {
                final PlateRenderer pr = getPlateRenderer( row, col );
                if ( pr != null ) {
                    pr.setMin( min );
                    pr.setMax( max );
                    pr.setMean( mean );
                }
            }
        }
    }

    public void setControlFont( final Font controlFont ) {
        _controlFont = controlFont;
    }

    public void setCurrentlyMarkedRow( final int currentlyMarkedRow ) {
        _currentlyMarkedRow = currentlyMarkedRow;
    }

    public void setCurrentMax( final double currentMax ) {
        if ( isUseMean() && ( currentMax < getCurrentMean() ) ) {
            _currentMax = getCurrentMean();
        }
        else if ( currentMax < getCurrentMin() ) {
            _currentMax = getCurrentMin();
        }
        else {
            _currentMax = currentMax;
        }
    }

    public void setCurrentMean( final double currentMean ) {
        if ( currentMean > getCurrentMax() ) {
            _currentMean = getCurrentMax();
        }
        else if ( currentMean < getCurrentMin() ) {
            _currentMean = getCurrentMin();
        }
        else {
            _currentMean = currentMean;
        }
    }

    public void setCurrentMin( final double currentMin ) {
        if ( isUseMean() && ( currentMin > getCurrentMean() ) ) {
            _currentMin = getCurrentMean();
        }
        else if ( currentMin > getCurrentMax() ) {
            _currentMin = getCurrentMax();
        }
        else {
            _currentMin = currentMin;
        }
    }

    public void setEnabledOfMinMaxMeanChangingItems( final boolean enabled ) {
        minusButton1.setEnabled( enabled );
        minusButton2.setEnabled( enabled );
        plusButton1.setEnabled( enabled );
        plusButton2.setEnabled( enabled );
        resetButton1.setEnabled( enabled );
        resetButton2.setEnabled( enabled );
        valueTextField1.setEnabled( enabled );
        valueTextField2.setEnabled( enabled );
        if ( isUseMean() ) {
            minusButton3.setEnabled( enabled );
            plusButton3.setEnabled( enabled );
            resetButton3.setEnabled( enabled );
            valueTextField3.setEnabled( enabled );
        }
    }

    public void setMarked( final int plate_row, final int well_row,
            final int well_col ) {
        setMarkedToFalse( getCurrentlyMarkedRow() );
        setCurrentlyMarkedRow( plate_row );
        for( int plate_col = 0; plate_col < getMaxNumberOfReplicates(); plate_col++ ) {
            final PlateRenderer pr = getPlateRenderer( plate_row, plate_col );
            if ( pr != null ) {
                pr.inverseMarkedOfWell( well_row, well_col );
                pr.repaint();
            }
        }
    }

    public void setMarkedToFalse( final int plate_row ) {
        for( int plate_col = 0; plate_col < getMaxNumberOfReplicates(); plate_col++ ) {
            final PlateRenderer pr = getPlateRenderer( plate_row, plate_col );
            if ( pr != null ) {
                pr.setMarkedOfAllWellsToFalse();
            }
        }
        updateDisplay();
    }

    public void setMaxColor( final Color maxColor ) {
        _maxColor = maxColor;
    }

    public void setMeanColor( final Color meanColor ) {
        _meanColor = meanColor;
    }

    public void setMinColor( final Color minColor ) {
        _minColor = minColor;
    }

    public void setPlateRenderer( final PlateRenderer pr, final int row,
            final int col ) {
        _platerenderers[ row ][ col ] = pr;
    }

    public void setPlateTitleFont( final Font plateTitleFont ) {
        _plateTitleFont = plateTitleFont;
    }

    public void setResultsOfSearchShowing( final boolean resultsOfSearchShowing ) {
        _resultsOfSearchShowing = resultsOfSearchShowing;
    }

    public void setStepSize( final double stepSize ) {
        if ( stepSize < 0.0D ) {
            _stepSize = 0.0D;
        }
        else {
            _stepSize = stepSize;
        }
    }

    public void setUseMean( final boolean useMean ) {
        _useMean = useMean;
        if ( useMean ) {
            setCurrentMean( getCurrentMean() );
        }
    }

    public void setWellLabelFont( final Font wellLabelFont ) {
        _wellLabelFont = wellLabelFont;
    }

    public void setWellSize( final int wellSize ) {
        if ( wellSize < 2 ) {
            _wellSize = 2;
        }
        else {
            _wellSize = wellSize;
        }
        setWellLabelFont( new Font( PlateDisplayPanel.FONT, 0,
                ( int ) ( wellSize * 0.5D + 2D ) ) );
        setPlateTitleFont( new Font( PlateDisplayPanel.FONT, 0,
                ( int ) ( wellSize * 0.29999999999999999D + 8D ) ) );
        setControlFont( new Font( PlateDisplayPanel.FONT, 0,
                ( int ) ( wellSize * 0.5D + 2D ) ) );
    }

    public void showOutliersCheckBox_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    public void squareRadioButton_actionPerformed( final ActionEvent e ) {
        updateDisplay();
    }

    public void updateDisplay() {
        valueTextField1.setText( FluoriteUtil.cut2( getCurrentMin() ) );
        changeColorButton1.setBackground( getMinColor() );
        valueTextField2.setText( FluoriteUtil.cut2( getCurrentMax() ) );
        changeColorButton2.setBackground( getMaxColor() );
        valueTextField3.setText( FluoriteUtil.cut2( getCurrentMean() ) );
        changeColorButton3.setBackground( getMeanColor() );
        zoomTextField.setText( getWellSize() + "" );
        for( int i = 0; i < getPlateRendererLength(); i++ ) {
            for( int j = 0; j < getMaxNumberOfReplicates(); j++ ) {
                final PlateRenderer pr = getPlateRenderer( i, j );
                if ( pr != null ) {
                    pr.resetWellSize( getWellSize() );
                    if ( !colorizePerPlateCheckBox.isSelected()
                            && !colorizePerReplicateCheckBox.isSelected() ) {
                        pr.setMin( getCurrentMin() );
                        pr.setMax( getCurrentMax() );
                        pr.setMean( isUseMean() ? getCurrentMean() : 0.0D );
                    }
                    pr.setUseMean( isUseMean() );
                    pr.setMinColor( getMinColor() );
                    pr.setMaxColor( getMaxColor() );
                    pr.setMeanColor( isUseMean() ? getMeanColor() : null );
                    pr.resetWellColors();
                    pr.revalidate();
                }
            }
        }
        repaint();
    }

    public abstract void updateInfo( String s, String s1, String s2, String s3 );

    public void valueTextField1_actionPerformed( final ActionEvent e ) {
        setCurrentMin( FluoriteUtil.parseDouble( valueTextField1.getText(),
                getCurrentMin() ) );
        updateDisplay();
    }

    public void valueTextField2_actionPerformed( final ActionEvent e ) {
        setCurrentMax( FluoriteUtil.parseDouble( valueTextField2.getText(),
                getCurrentMax() ) );
        updateDisplay();
    }

    public void valueTextField3_actionPerformed( final ActionEvent e ) {
        setCurrentMean( FluoriteUtil.parseDouble( valueTextField3.getText(),
                getCurrentMean() ) );
        updateDisplay();
    }

    public void zoomMinusButton_actionPerformed( final ActionEvent e ) {
        if ( getWellSize() > 2 ) {
            setWellSize( getWellSize() - 1 );
            updateDisplay();
        }
    }

    public void zoomPlusButton_actionPerformed( final ActionEvent e ) {
        setWellSize( getWellSize() + 1 );
        updateDisplay();
    }

    public void zoomResetButton_actionPerformed( final ActionEvent e ) {
        setWellSize( 10 );
        updateDisplay();
    }

    public void zoomTextField_actionPerformed( final ActionEvent e ) {
        setWellSize( FluoriteUtil.parseInt( zoomTextField.getText(),
                getWellSize() ) );
        updateDisplay();
    }
}
