// $Id: BasicHistogramDataItem.java,v 1.5 2010/12/13 18:59:25 cmzmasek Exp $
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

public class BasicHistogramDataItem implements HistogramDataItem {

    private final double _value;
    private final String _id;

    public BasicHistogramDataItem( final String id, final double value ) {
        _id = id;
        _value = value;
    }

    public BasicHistogramDataItem( final double value ) {
        _id = "";
        _value = value;
    }

    @Override
    public int compareTo( final HistogramDataItem data_item ) {
        if ( this == data_item ) {
            return 0;
        }
        return getIdentifier().toLowerCase().compareTo(
                data_item.getIdentifier().toLowerCase() );
    }

    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) {
            return true;
        }
        else if ( o == null ) {
            throw new IllegalArgumentException(
                    "attempt to check data item equality to null" );
        }
        else if ( o.getClass() != this.getClass() ) {
            throw new IllegalArgumentException(
                    "attempt to check domain id equality to " + o + " ["
                            + o.getClass() + "]" );
        }
        else {
            return getIdentifier().equals(
                    ( ( HistogramDataItem ) o ).getIdentifier() );
        }
    }

    public String getId() {
        return _id;
    }

    @Override
    public String getIdentifier() {
        return _id;
    }

    @Override
    public double getValue() {
        return _value;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public String toString() {
        return getIdentifier() + " [" + getValue() + "]";
    }
}
