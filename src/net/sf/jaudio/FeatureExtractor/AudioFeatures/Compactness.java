/*
 * @(#)Compactness.java	1.0	April 7, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Compactness. This is a measure of the
 * noisiness of a signal.
 * <p>
 * This is calculated by comparing the value of a magnitude spectrum bin with
 * its surrounding values.
 * <p>
 * No extracted feature values are stored in objects of this class.
 * <p>
 * Daniel McEnnis 05-07-05 added check for degenerate case of 0 in magnitude
 * spectrum and added clone
 * 
 * @author Cory McKay
 */
public class Compactness extends FeatureExtractor {
   
    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public Compactness() {
        String name = "Compactness";
        String description = "A measure of the noisiness of a signal. "
                + "Found by comparing the components of a window's "
                + "magnitude spectrum with the magnitude spectrum "
                + "of its neighbouring windows.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions);

        this.dependencies = new String[1];
        this.dependencies[0] = "Magnitude Spectrum";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * In the case of this feature, the sampling_rate parameter is ignored.
     * </p>
     * 
     * @author Daniel McEnnis 05-07-05 checks for degenerate case where
     *         magnitude spectrum entry is exactly zero - skips these values
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] mag_spec = other_feature_values[0];
        double compactness = 0.0;
        for (int i = 1; i < mag_spec.length - 1; i++) {
            if ((mag_spec[i - 1] > 0.0) && (mag_spec[i] > 0.0)
                    && (mag_spec[i + 1] > 0.0)) {
                compactness += Math
                        .abs(20.0
                                * Math.log(mag_spec[i])
                                - 20.0
                                * (Math.log(mag_spec[i - 1])
                                        + Math.log(mag_spec[i]) + Math
                                            .log(mag_spec[i + 1])) / 3.0);
            }
        }

        double[] result = new double[1];
        result[0] = compactness;
        return result;
    }

    @Override
    public Object clone() {
        return new Compactness();
    }
}