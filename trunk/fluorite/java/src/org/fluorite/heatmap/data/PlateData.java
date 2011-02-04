// $Id: PlateData.java,v 1.3 2010/12/13 18:59:25 cmzmasek Exp $
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

public class PlateData {

    public static final char ALPHABET[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z' };
    final int                _rows;
    final int                _columns;
    final int                _replicateNumber;
    final String             _name;
    final WellData           _welldata[][];

    public PlateData( final int rows, final int columns,
            final int replicateNumber, final String name ) {
        _welldata = new WellData[ rows ][ columns ];
        _rows = rows;
        _columns = columns;
        _replicateNumber = replicateNumber;
        _name = name;
    }

    public int geRows() {
        return _rows;
    }

    public int getColumns() {
        return _columns;
    }

    public WellData getData( final int row, final int column ) {
        return _welldata[ row ][ column ];
    }

    public String getName() {
        return _name;
    }

    public int getReplicateNumber() {
        return _replicateNumber;
    }

    public void setData( final WellData wellData, final char row_char,
            final int column ) {
        for( int i = 0; i < PlateData.ALPHABET.length; i++ ) {
            if ( row_char == PlateData.ALPHABET[ i ] ) {
                setData( wellData, i, column );
                return;
            }
        }
        throw new IllegalArgumentException(
                "Expected character ['A'-'Z'] got\"" + row_char + "\"" );
    }

    public void setData( final WellData wellData, final int row,
            final int column ) {
        _welldata[ row ][ column ] = wellData;
    }
}
