/*
 * @(#)SpectralRolloffPoint.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * A feature extractor that extracts the Spectral Rolloff Point. This is a
 * measure of the amount of the right-skewedness of the power spectrum.
 * <p>
 * The spectral rolloff point is the fraction of bins in the power spectrum at
 * which 85% of the power is at lower frequencies.
 * <p>
 * No extracted feature values are stored in objects of this class.
 * 
 * @author Cory McKay
 */
public class SpectralRolloffPoint extends FeatureExtractor {

    protected double cutoff = 0.85;

     /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public SpectralRolloffPoint() {
        String name = "Spectral Rolloff Point";
        String description = "The fraction of bins in the power spectrum at which 85% "
                + // System.getProperty("line.separator") +
                "of the power is at lower frequencies. This is a measure " + // System.getProperty("line.separator")
                // +
                "of the right-skewedness of the power spectrum.";
        boolean is_sequential = true;
        int dimensions = 1;
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions, new String[] { "Cutoff point (0-1)" });

        this.dependencies = new String[1];
        this.dependencies[0] = "Power Spectrum";

        this.offsets = new int[1];
        this.offsets[0] = 0;
    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] pow_spectrum = other_feature_values[0];

        double total = 0.0;
        for (int bin = 0; bin < pow_spectrum.length; bin++)
            total += pow_spectrum[bin];
        double threshold = total * this.cutoff;

        total = 0.0;
        int point = 0;
        for (int bin = 0; bin < pow_spectrum.length; bin++) {
            total += pow_spectrum[bin];
            if (total >= threshold) {
                point = bin;
                bin = pow_spectrum.length;
            }
        }

        double[] result = new double[1];
        result[0] = ((double) point) / ((double) pow_spectrum.length);
        return result;
    }

    @Override
    public Object clone() {
        SpectralRolloffPoint ret = new SpectralRolloffPoint();
        ret.cutoff = this.cutoff;
        return ret;
    }

    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:getElement");
        } else {
            return Double.toString(this.cutoff);
        }
    }

    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:setElement");
        } else {
            try {
                double type = Double.parseDouble(value);
                setCutoff(type);
            } catch (Exception e) {
                throw new Exception(
                        "Length of Area Method of Moments must be an integer");
            }
        }
    }

    /**
     * Permits users to set the precise cutoff point. This value should be
     * strictly between 0 and 1.
     * 
     * @param c
     *            new cutoff point
     * @throws Exception
     *             thrown if c is not a real number strictly between 0 and 1.
     */
    public void setCutoff(double c) throws Exception {
        if (Double.isInfinite(c) || Double.isNaN(c)) {
            throw new Exception("SpectralRolloff cutoff must be a real number");
        } else if ((c <= 0.0) || (c >= 1.0)) {
            throw new Exception(
                    "SpectralRolloff cutoff must be gretaer than 0 and less than 1");
        } else {
            this.cutoff = c;
        }
    }
}