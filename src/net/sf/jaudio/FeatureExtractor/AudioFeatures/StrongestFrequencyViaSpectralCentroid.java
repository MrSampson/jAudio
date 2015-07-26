/*
 * @(#)StrongestFrequencyViaSpectralCentroid.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that finds the strongest frequency in Hz in a signal by
 * looking at the spectral centroid.
 * </p>
 *
 * <p>
 * This is found by mapping the fraction in the spectral centroid to a frequency
 * in Hz.
 * </p>
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class StrongestFrequencyViaSpectralCentroid extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public StrongestFrequencyViaSpectralCentroid() {
        this.name = "Strongest Frequency Via Spectral Centroid";
        this.description = "The strongest frequency component of a signal, in Hz, "
                + "found via the spectral centroid.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = new String[2];
        this.dependencies[0] = "Spectral Centroid";
        this.dependencies[1] = "Power Spectrum";

        this.offsets = new int[2];
        this.offsets[0] = 0;
        this.offsets[1] = 0;
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
        double spectral_centroid = other_feature_values[0][0];
        double[] pow_spectrum = other_feature_values[1];
        double[] result = new double[1];
        result[0] = (spectral_centroid / pow_spectrum.length)
                * (sampling_rate / 2.0);
        return result;
    }

    @Override
    public Object clone() {
        return new StrongestFrequencyViaSpectralCentroid();
    }
}