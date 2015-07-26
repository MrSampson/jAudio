/*
 * @(#)PowerSpectrum.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import net.sf.jaudio.FeatureExtractor.jAudioTools.FFT;

/**
 * A feature extractor that extracts the FFT power spectrum from a set of
 * samples. This is a good measure of the power of different frequency
 * components within a window.
 *
 * <p>
 * The power spectrum is found by first calculating the FFT with a Hanning
 * window. The magnitude spectrum value for each bin is found by first summing
 * the squares of the real and imaginary components. The result is divided by
 * the number of bins.
 *
 * <p>
 * The dimensions of this feature depend on the number of FFT bins, which depend
 * on the number of input samples. The dimensions are stored in the definition
 * field are therefore 0, in order to indicate this variability.
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 *
 * @author Cory McKay
 */
public class PowerSpectrum extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public PowerSpectrum() {
        this.name = "Power Spectrum";
        this.description = "A measure of the power of different frequency components.";
        boolean is_sequential = true;
        int dimensions = 0;
        this.definition = new FeatureDefinition(this.name, this.description,
                is_sequential, dimensions);

        this.dependencies = null;

        this.offsets = null;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * In the case of this feature, the <code>sampling_rate</code> and
     * <code>other_feature_values</code> parameters are ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        FFT fft = new FFT(samples, null, false, true);
        return fft.getPowerSpectrum();
    }

    @Override
    public Object clone() {
        return new PowerSpectrum();
    }
}
