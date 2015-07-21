/*
 * @(#)SpectralVariability.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that extracts the SpectralVariance. This is a measure of
 * the standard deviation of a signal's magnitude spectrum.
 * </p>
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class SpectralVariability extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public SpectralVariability() {
        String name = "Spectral Variability";
        String description = "The standard deviation of the magnitude spectrum. "
                + "This is a measure of the variance of a signal's "
                + "magnitude spectrum.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Magnitude Spectrum";

        this.offsets = new int[1];
        this.offsets[0] = 0;
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
        double[] mag_spec = other_feature_values[0];
        double variance = net.sf.jaudio.FeatureExtractor.GeneralTools.Statistics
                .getStandardDeviation(mag_spec);

        double[] result = new double[1];
        result[0] = variance;
        return result;
    }

    @Override
    public Object clone() {
        return new SpectralVariability();
    }
}