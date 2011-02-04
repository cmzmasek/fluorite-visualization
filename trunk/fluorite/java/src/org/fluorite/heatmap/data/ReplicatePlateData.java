// $Id: ReplicatePlateData.java,v 1.3 2010/12/13 18:59:25 cmzmasek Exp $
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

import java.util.ArrayList;

public class ReplicatePlateData {

    private final ArrayList<PlateData> _plateDatas = new ArrayList<PlateData>();

    public ReplicatePlateData() {
    }

    public void addPlateData( final PlateData plateData ) {
        _plateDatas.add( plateData );
    }

    public int getNumberOfReplicates() {
        return _plateDatas.size();
    }

    public PlateData getPlateData( final int index ) {
        return _plateDatas.get( index );
    }
}
