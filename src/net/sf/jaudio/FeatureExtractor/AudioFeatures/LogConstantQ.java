//
//  Log ConstantQ.java
//  jAudio
//
//  Created by Daniel McEnnis on August 18, 2010.
//  Published under the LGPL license.  See most recent LGPL license on www.fsf.org
//  a copy of this license.
//

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * Log Constant Q
 *
 * Performs the log linear transform of the bins of the constant q transform to
 * produce a representation whose linear content better represents how the human
 * ear hears differences in amplitude.
 *
 * @author Daniel McEnnis
 */
public class LogConstantQ extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public LogConstantQ() {
         this.name = "Log of ConstantQ";
         this.description = "logarithm of each bin of exponentially-spaced frequency bins.";
        boolean is_sequential = true;
        int dimensions = 0;
        // String[] attributes = new String[]{"Percent of a semitone per bin"};
        this.definition = new FeatureDefinition(this.name, this.description, is_sequential,
                dimensions);

        this.dependencies = new String[] { "ConstantQ" };

        this.offsets = new int[] { 0 };

    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * In the case of this feature, the sampling_rate parameter is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] ret = new double[other_feature_values[0].length];
        for (int i = 0; i < ret.length; ++i) {
            if (other_feature_values[0][i] <= 0.0) {
                ret[i] = -50.0;
            } else {
                ret[i] = Math.log(other_feature_values[0][i]);
                if (ret[i] < -50.0) {
                    ret[i] = -50.0;
                }
            }
        }
        return ret;
    }

    @Override
    public Object clone() {
        return new LogConstantQ();
    }

}
