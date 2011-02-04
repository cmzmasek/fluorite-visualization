// $Id: DescriptiveStatistics.java,v 1.2 2010/12/13 18:59:25 cmzmasek Exp $
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

package org.fluorite.util;

public interface DescriptiveStatistics {

    public final static String PLUS_MINUS = "" + ( char ) 177;

    public abstract void addValue( final double d );

    public abstract double getValue( final int index );

    public abstract double[] getDataAsDoubleArray();

    public abstract int getN();

    public abstract double getSum();

    public abstract double getMin();

    public abstract double getMax();

    public abstract String getSummaryAsString();

    public abstract double median();

    public abstract double arithmeticMean();

    public abstract double sampleStandardDeviation();

    public abstract double sampleVariance();

    public abstract double midrange();

    public abstract double sumDeviations();

    /**
     * Computes the coefficient of variation. Used to express standard deviation
     * independent of units of measure.
     * 
     * @return
     */
    public abstract double coefficientOfVariation();

    /**
     * Determines relationship between the mean and the median. This reflects
     * how the data differs from the normal bell shaped distribution.
     * 
     * @return
     */
    public abstract double pearsonianSkewness();

    public abstract double standardErrorOfMean();

    public abstract double sampleStandardUnit( final double value );

    public abstract String toString();

    public abstract String asSummary();
}