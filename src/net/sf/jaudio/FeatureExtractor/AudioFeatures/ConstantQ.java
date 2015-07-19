//
//  ConstantQ.java
//  jAudio
//
//  Created by Daniel McEnnis on August 17, 2010.
//  Published under the LGPL license.  See most recent LGPL license on www.fsf.org
//  a copy of this license.
//

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * Constant Q
 * 
 * Transform from the time domain to the frequency domain that uses logarithmic
 * bins.
 * 
 * @author Daniel McEnnis
 */
public class ConstantQ extends FeatureExtractor {

    int n;
    double alpha = 1.0;
    int nk[];
    double[] freq;
    double[][] kernelReal;
    double[][] kernelImaginary;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public ConstantQ() {
        String name = "ConstantQ";
        String description = "signal to frequency transform using exponential-spaced frequency bins.";
        boolean is_sequential = true;
        int dimensions = 0;
        String[] attributes = new String[] { "Percent of a semitone per bin" };
        this.definition = new FeatureDefinition(name, description,
                is_sequential, dimensions, attributes);

        this.dependencies = null;

        this.offsets = null;

        this.alpha = 1.0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the <code>sampling_rate</code> and
     * <code>other_feature_values</code> parameters are ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        calcFreq(samples, sampling_rate);
        calcNk(samples);
        calcKernels(samples.length, sampling_rate);
        double[] ret = new double[2 * this.nk.length];
        double[] mag = new double[this.nk.length];
        java.util.Arrays.fill(ret, 0.0);
        for (int bankCounter = 0; bankCounter < (ret.length / 2); ++bankCounter) {
            double[] data = resample(samples, this.nk[bankCounter]);
            for (int i = 0; i < this.nk[bankCounter]; ++i) {
                ret[bankCounter] += this.kernelReal[bankCounter][i] * data[i];
                ret[bankCounter + this.nk.length] += this.kernelImaginary[bankCounter][i]
                        * data[i];
            }
        }
        for (int i = 0; i < mag.length; ++i) {
            mag[i] = Math.sqrt(ret[i] * ret[i] + ret[i + this.nk.length]
                    * ret[i + this.nk.length]);
        }
        return mag;
    }

    @Override
    public Object clone() {
        return new ConstantQ();
    }

    private double[] resample(double[] samples, int window_length) {
        double[] ret = new double[window_length];
        double[] index = new double[window_length];
        double increment = ((double) samples.length) / ((double) window_length);
        for (int i = 0; i < window_length; ++i) {
            index[i] = increment * ((double) i);
        }
        for (int i = 0; i < ret.length; ++i) {
            int base = (int) Math.floor(increment * i);
            if (Math.abs(((double) base) - (increment * i)) < 0.00001) {
                ret[i] = samples[(int) Math.round(increment * i)];
            } else {
                ret[i] = samples[base] * (1.0 - ((increment * i) - base))
                        + samples[base + 1] * ((increment * i) - base);
            }
        }
        return ret;
    }

    private void calcFreq(double[] samples, double sampling_rate) {
        double maxFreq = sampling_rate / 2.0;
        double minFreq = sampling_rate / ((double) samples.length);
        double carry = Math.log(maxFreq / minFreq);
        carry /= Math.log(2);
        carry *= 6 / this.alpha;
        int numFields = (int) (Math.floor(carry));

        this.freq = new double[numFields];
        double currentFreq = minFreq;
        for (int i = 0; i < numFields; ++i) {
            this.freq[i] = currentFreq;
            currentFreq *= Math.pow(2, this.alpha / 12.0);
        }
    }

    private void calcNk(double[] samples) {
        this.nk = new int[this.freq.length];
        double windowLength = samples.length;
        for (int i = 0; i < this.nk.length; ++i) {
            this.nk[i] = (int) Math.ceil(windowLength
                    / (Math.pow(2, ((double) i) * this.alpha / 12)));
        }
    }

    private void calcKernels(double windowLength, double sampleRate) {
        this.kernelReal = new double[this.nk.length][];
        this.kernelImaginary = new double[this.nk.length][];
        // double q = Math.pow(2,alpha/12)-1;
        // double[] freqInRad = new double[nk.length];
        // double numWindowsPerSecond = (sampleRate / windowLength);
        // for (int index=0;index<freqInRad.length;++index){
        // double binSampleRate = numWindowsPerSecond*nk[index] / 2.0;
        // freqInRad[index] = freq[index]/ binSampleRate;
        // }
        double hammingFactor = 25.0 / 46.0;
        for (int i = 0; i < this.kernelReal.length; ++i) {
            this.kernelReal[i] = new double[this.nk[i]];
            this.kernelImaginary[i] = new double[this.nk[i]];
            for (int j = 0; j < this.kernelReal[i].length; ++j) {
                this.kernelReal[i][j] = hammingFactor
                        + (1 - hammingFactor)
                        * Math.cos(2.0 * Math.PI * ((double) j)
                                / ((double) this.nk[i]));
                this.kernelReal[i][j] /= ((double) this.nk[i]);
                this.kernelImaginary[i][j] = this.kernelReal[i][j];
                this.kernelReal[i][j] *= Math.cos(-2.0 * Math.PI * ((double) j)
                        / (Math.pow((double) this.nk[i], 2.0)));
                this.kernelImaginary[i][j] *= Math.sin(-2.0 * Math.PI
                        * ((double) j) / (Math.pow((double) this.nk[i], 2.0)));
            }
        }
    }

    @Override
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            return Double.toString(this.alpha);
        default:
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " passed to LPC:getElement()");
        }
    }

    @Override
    public void setElement(int index, String value) throws Exception {
        switch (index) {
        case 0:
            try {
                double val = Double.parseDouble(value);
                if (val <= 0.0) {
                    throw new Exception("Alpha must be a positive value");
                } else {
                    this.alpha = val;
                }
            } catch (NumberFormatException e) {
                throw new Exception("Alpha value must be a double");
            }
            break;
        default:
            throw new Exception(
                    "INTERNAL ERROR: invalid index passed to ConstantQ:setElement");
        }
    }

}
