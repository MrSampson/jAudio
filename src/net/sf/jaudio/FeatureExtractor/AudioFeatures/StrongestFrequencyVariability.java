/*
 * @(#)StrongestFrequencyVariability.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Strongest Frequency Variability from
 * window to window. This is a good measure of the amount of change in
 * fundamental frequency that a signal goes through over a moderate amount of
 * time.
 *
 * <p>
 * This is calculated by taking the standard deviation of the frequency of the
 * power spectrum bin with the highest power over the last 100 windows.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class StrongestFrequencyVariability extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrongestFrequencyVariability() {
        this.name = "Strongest Frequency Variability";
        this.description = "The standard deviation of the frequency of the"
                + "power spectrum bin with the highest power over"
                + "the last 100 windows.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        int number_windows = 100;

        this.dependencies = new String[number_windows];
        for (int i = 0; i < this.dependencies.length; i++)
            this.dependencies[i] = "Strongest Frequency Via FFT Maximum";

        this.offsets = new int[number_windows];
        for (int i = 0; i < this.offsets.length; i++)
            this.offsets[i] = 0 - i;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * In the case of this feature the sampling_rate is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] freq = other_feature_values[0];
        double std_dev = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                .getStandardDeviation(freq);

        double[] result = new double[1];
        result[0] = std_dev;
        return result;
    }

    @Override
    public Object clone() {
        return new StrongestFrequencyVariability();
    }
}