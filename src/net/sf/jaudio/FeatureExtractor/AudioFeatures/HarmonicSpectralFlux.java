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
package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A variation on spectral flux that is based upon peaks instead of bins. Given
 * two sets of peaks, calculate the correlation between adjacent peaks. This
 * should use proper peak tracking, but for now tracks peaks by matching peaks
 * from the bottom up. Unmatched peaks are discarded. This feature is based upon
 * Stephan McAdams Spectral Centroid in (McAdams 1999).
 * <p>
 * McAdams, S. 1999. Perspectives on the contribution of timbre to musical
 * structure. <i>Computer Music Journal</i>. 23(3):85-102.
 * 
 * @author Daniel McEnnis
 */
public class HarmonicSpectralFlux extends FeatureExtractor {

    /**
     * Basic constructor that sets dependencies, definition, and offsets.
     *
     */
    public HarmonicSpectralFlux() {
         name = new String("Partial Based Spectral Flux");
         description = new String(
                "Cacluate the correlation bettween adjacent frames based peaks instead of spectral bins.  Peak tracking is primitive - whe the number of bins changes, the bottom bins are matched sequentially and the extra unmatched bins are ignored.");
        this.definition = new FeatureDefinition(name, description, true, 1);
        this.dependencies = new String[] { "Peak Detection", "Peak Detection" };
        this.offsets = new int[] { 0, -1 };
    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) {
        double[] result = new double[1];
        double[] old = other_feature_values[1];
        double[] now = other_feature_values[0];
        double x, y, xy, x2, y2;
        x = y = xy = x2 = y2 = 0.0;
        int peakCount = Math.min(old.length, now.length);

        for (int i = 0; i < peakCount; ++i) {
            x += old[i];
            y += now[i];
            xy += old[i] * now[i];
            x2 = old[i] * old[i];
            y2 = now[i] * now[i];
        }

        double top = xy - (x * y) / peakCount;
        double bottom = Math.sqrt(Math.abs((x2 - ((x * x) / peakCount))
                * (y2 - ((y * y) / peakCount))));
        result[0] = top / bottom;
        return result;
    }

    @Override
    public Object clone() {
        return new HarmonicSpectralFlux();
    }

}
