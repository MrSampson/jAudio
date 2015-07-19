/*
 * @(#)SpectralCentroid.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Spectral Centroid. This is a measure of
 * the "centre of mass" of the power spectrum.
 *
 * <p>
 * This is calculated by calculating the mean bin of the power spectrum. The
 * result returned is a number from 0 to 1 that represents at what fraction of
 * the total number of bins this central frequency is.
 * </p>
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class SpectralCentroid extends FeatureExtractor {
    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     * 
     * <p>
     * @author Daniel McEnnis 05-07-05 altered offsets to match dependencies<\p>
     */
    public SpectralCentroid() {
        String name = "Spectral Centroid";
        String description = "The centre of mass of the power spectrum.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Power Spectrum";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * In the case of this feature, the <code>sampling_rate parameter</code> is
     * ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] pow_spectrum = other_feature_values[0];

        double total = 0.0;
        double weighted_total = 0.0;
        for (int bin = 0; bin < pow_spectrum.length; bin++) {
            weighted_total += bin * pow_spectrum[bin];
            total += pow_spectrum[bin];
        }

        double[] result = new double[1];
        if (total != 0.0) {
            result[0] = weighted_total / total;
        } else {
            result[0] = 0.0;
        }
        return result;
    }

    @Override
    public SpectralCentroid clone() {
        return new SpectralCentroid();
    }
}