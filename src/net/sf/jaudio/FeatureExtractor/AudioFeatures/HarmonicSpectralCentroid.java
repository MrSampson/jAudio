package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * @author Daniel McEnnis
 *
 * This file is part of Solo695.
 *
 *   Solo695 is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   Solo695 is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with MusicalBlackboard; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * <p>
 * A variation on spectral centroid that is based upon peaks instead of bins.
 * Given a set of peaks, calculate the peak index that corresponds to 50% of the
 * energy in the window. Is based upon Stephan McAdams Spectral Centroid in
 * (McAdams 1999).
 * </p>
 * <p>
 * McAdams, S. 1999. Perspectives on the contribution of timbre to musical
 * structure. <i>Computer Music Journal</i>. 23(3):85-102.
 * </p>
 * 
 */
public class HarmonicSpectralCentroid extends FeatureExtractor {

    /**
     * Basic constructor that sets dependencies, definition, and offsets.
     *
     */
    public HarmonicSpectralCentroid() {
        String name = "Partial Based Spectral Centroid";
        String description = "Spectral Centroid calculated based on the center of mass of partials instead of center of mass of bins.";
        this.definition = new FeatureDefinition(name, description, true, 1);
        this.dependencies = new String[] { "Peak Detection" };
        this.offsets = new int[] { 0 };
    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) {
        double[] result = new double[1];
        double[] peaks = other_feature_values[0];
        double total = 0.0;
        double weightedTotal = 0.0;
        for (int i = 0; i < peaks.length; ++i) {
            weightedTotal += i / 2 * peaks[i];
            total += peaks[i];
        }
        result[0] = weightedTotal / total;
        return result;
    }

    @Override
    public Object clone() {
        return new HarmonicSpectralCentroid();
    }

}
