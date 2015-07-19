/*
 * @(#)MagnitudeSpectrum.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import net.sf.jaudio.FeatureExtractor.jAudioTools.FFT;

/**
 * <p>
 * A feature extractor that extracts the FFT magnitude spectrum from a set of
 * samples. This is a good measure of the magnitude of different frequency
 * components within a window.
 * </p>
 *
 * <p>
 * The magnitude spectrum is found by first calculating the FFT with a Hanning
 * window. The magnitude spectrum value for each bin is found by first summing
 * the squares of the real and imaginary components. The square root of this is
 * then found and the result is divided by the number of bins.
 * </p>
 *
 * <p>
 * The dimensions of this feature depend on the number of FFT bins, which depend
 * on the number of input samples. The dimensions are stored in the definition
 * field are therefore 0, in order to indicate this variability.
 * </p>
 *
 * <p>
 * No extracted feature values are stored in objects of this class.
 * </p>
 *
 * @author Cory McKay
 */
public class MagnitudeSpectrum extends FeatureExtractor {

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public MagnitudeSpectrum() {
        String name = "Magnitude Spectrum";
        String description = "A measure of the strength of different frequency components.";
        boolean is_sequential = true;
        int dimensions = 0;
        this.definition = new FeatureDefinition(name, description,
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
        return fft.getMagnitudeSpectrum();
    }

    @Override
    public Object clone() {
        return new MagnitudeSpectrum();
    }
}
