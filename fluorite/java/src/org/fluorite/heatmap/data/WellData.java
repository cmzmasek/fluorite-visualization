// $Id: WellData.java,v 1.3 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.heatmap.data;

public class WellData {

    public static final double DEFAULT_FOR_EMPTY_WELLS = 4.9406564584124654E-324D;
    private final double       _value;
    private final String       _name;
    private final String       _description;
    private final String       _id;
    private final boolean      _isUserFlagged;
    private final boolean      _isFlaggedByStatisticalAnalysis;
    private final boolean      _isNegativeControl;
    private final boolean      _isPositiveControl;
    private final boolean      _isEmptyControl;
    private final boolean      _isEmpty;

    public WellData() {
        _value = 0.0D;
        _name = null;
        _description = null;
        _id = null;
        _isUserFlagged = false;
        _isFlaggedByStatisticalAnalysis = false;
        _isNegativeControl = false;
        _isPositiveControl = false;
        _isEmptyControl = false;
        _isEmpty = true;
    }

    public WellData( final double value, final String name,
            final String description, final String id,
            final boolean isUserFlagged,
            final boolean isFlaggedByStatisticalAnalysis,
            final boolean isNegativeControl, final boolean isPositiveControl,
            final boolean isEmptyControl, final boolean isEmpty ) {
        _value = value;
        _name = name;
        _description = description;
        _id = id;
        _isUserFlagged = isUserFlagged;
        _isFlaggedByStatisticalAnalysis = isFlaggedByStatisticalAnalysis;
        _isNegativeControl = isNegativeControl;
        _isPositiveControl = isPositiveControl;
        _isEmptyControl = isEmptyControl;
        _isEmpty = isEmpty;
    }

    public String getDescription() {
        if ( isEmpty() ) {
            return "";
        }
        else {
            return _description;
        }
    }

    public String getID() {
        if ( isEmpty() ) {
            return "";
        }
        else {
            return _id;
        }
    }

    public String getName() {
        if ( isEmpty() ) {
            return "";
        }
        else {
            return _name;
        }
    }

    public double getValue() {
        if ( isEmpty() ) {
            return 0.0;
        }
        else {
            return _value;
        }
    }

    public boolean isEmpty() {
        return _isEmpty;
    }

    public boolean isEmptyControl() {
        if ( isEmpty() ) {
            return false;
        }
        else {
            return _isEmptyControl;
        }
    }

    public boolean isFlaggedByStatisticalAnalysis() {
        if ( isEmpty() ) {
            return false;
        }
        else {
            return _isFlaggedByStatisticalAnalysis;
        }
    }

    public boolean isNegativeControl() {
        if ( isEmpty() ) {
            return false;
        }
        else {
            return _isNegativeControl;
        }
    }

    public boolean isPositiveControl() {
        if ( isEmpty() ) {
            return false;
        }
        else {
            return _isPositiveControl;
        }
    }

    public boolean isUserFlagged() {
        if ( isEmpty() ) {
            return false;
        }
        else {
            return _isUserFlagged;
        }
    }
}
