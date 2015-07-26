/*
 * @(#)StrongestFrequencyViaFFTMax.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that finds the strongest frequency component of a signal,
 * in Hz.
 *
 * <p>
 * This is found by finding the highest bin in the power spectrum.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class StrongestFrequencyViaFFTMax extends FeatureExtractor {
  
    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrongestFrequencyViaFFTMax() {
        this.name = "Strongest Frequency Via FFT Maximum";
        this.description = "The strongest frequency component of a signal, in Hz, "
                + "found via finding the FFT bin with the highest power.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = new String[2];
        this.dependencies[0] = "Power Spectrum";
        this.dependencies[1] = "FFT Bin Frequency Labels";

        this.offsets = new int[2];
        this.offsets[0] = 0;
        this.offsets[1] = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the sampling_rate parameter is ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] power_spectrum = other_feature_values[0];
        double[] labels = other_feature_values[1];
        int highest_bin = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                .getIndexOfLargest(power_spectrum);
        double[] result = new double[1];
        result[0] = labels[highest_bin];
        return result;
    }

 
    @Override
    public Object clone() {
        return new StrongestFrequencyViaFFTMax();
    }
}