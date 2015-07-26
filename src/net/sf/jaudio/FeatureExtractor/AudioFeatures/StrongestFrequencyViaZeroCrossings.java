/*
 * @(#)StrongestFrequencyViaZeroCrossings.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that finds the strongest frequency in Hz in a signal by
 * looking at the zero crossings.
 *
 * <p>
 * This is found by mapping the fraction in the zero-crossings to a frequency in
 * Hz.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class StrongestFrequencyViaZeroCrossings extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrongestFrequencyViaZeroCrossings() {
        this.name = "Strongest Frequency Via Zero Crossings";
        this.description = "The strongest frequency component of a signal, in Hz, "
                + "found via the number of zero-crossings.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Zero Crossings";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the <code>sampling_rate parameter</code> is
     * ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double zero_crossings = other_feature_values[0][0];
        double[] result = new double[1];
        result[0] = (zero_crossings / 2.0)
                * (sampling_rate / (double) samples.length);
        return result;
    }

    @Override
    public Object clone() {
        return new StrongestFrequencyViaZeroCrossings();
    }
}