/*
 * @(#)SpectralFlux.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * A feature extractor that extracts the Spectral Flux from a window of samples
 * and the preceeding window. This is a good measure of the amount of spectral
 * change of a signal.
 * </p>
 * <p>
 * Spectral flux is calculated by first calculating the difference between the
 * current value of each magnitude spectrum bin in the current window from the
 * corresponding value of the magnitude spectrum of the previous window. Each of
 * these differences is then squared, and the result is the sum of the squares.
 * </p>
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class SpectralFlux extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public SpectralFlux() {
        String name = "Spectral Flux";
        String description = "A measure of the amount of spectral change in a signal. "
                + // \n" +
                "Found by calculating the change in the magnitude spectrum " + // \n"
                                                                               // +
                "from frame to frame.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions);

        this.dependencies = new String[2];
        this.dependencies[0] = "Magnitude Spectrum";
        this.dependencies[1] = "Magnitude Spectrum";

        this.offsets = new int[2];
        this.offsets[0] = 0;
        this.offsets[1] = -1;
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
        double[] this_magnitude_spectrum = other_feature_values[0];
        double[] previous_magnitude_spectrum = other_feature_values[1];

        double sum = 0.0;
        for (int bin = 0; bin < this_magnitude_spectrum.length; bin++) {
            double difference = this_magnitude_spectrum[bin]
                    - previous_magnitude_spectrum[bin];
            double differences_squared = difference * difference;
            sum += differences_squared;
        }

        double[] result = new double[1];
        result[0] = sum;
        return result;
    }

    @Override
    public Object clone() {
        return new SpectralFlux();
    }
}