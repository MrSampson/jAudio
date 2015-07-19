/*
 * @(#)RMS.java	0.5	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Root Mean Square (RMS) from a set of
 * samples. This is a good measure of the power of a signal.
 *
 * <p>
 * RMS is calculated by summing the squares of each sample, dividing this by the
 * number of samples in the window, and finding the square root of the result.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class RMS extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public RMS() {
        String name = "Root Mean Square";
        String description = "A measure of the power of a signal.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions);

        this.dependencies = null;

        this.offsets = null;
    }



    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the sampling_rate and other_feature_values
     * parameters are ignored.</p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double sum = 0.0;
        for (int samp = 0; samp < samples.length; samp++)
            sum += Math.pow(samples[samp], 2);
        double rms = Math.sqrt(sum / samples.length);
        double[] result = new double[1];
        result[0] = rms;
        return result;
    }

    @Override
    public Object clone() {
        return new RMS();
    }
}