//
//  ConstantQ.java
//  jAudio0.4.5.1
//
//  Created by Daniel McEnnis on August 18, 2010.
//  Published under the LGPL license.  See most recent LGPL license on www.fsf.org
//  a copy of this license.
//

package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * Constant Q MFCCs
 *
 * Implements MFCCs using the log constant q function. Produces MFCC's without
 * the error of rebinning linear bins to logarithmic bins.
 *
 * @author Daniel McEnnis
 */
public class ConstantQMFCC extends FeatureExtractor {

    int numCepstra = 13;

    /**
     * Basic constructor that sets the definition and dependencies (and their
     * offsets) of this feature.
     */
    public ConstantQMFCC() {
        String name = "ConstantQ derived MFCCs";
        String description = "MFCCs directly caluclated from ConstantQ exponential bins";
        boolean is_sequential = true;
        int dimensions = 0;
        String[] attributes = new String[] { "Number of cepstra to return" };
        this.definition = new FeatureDefinition(name, description, is_sequential,
                dimensions, attributes);

        this.dependencies = new String[] { "Log of ConstantQ" };

        this.offsets = new int[] { 0 };

    }

    /**
     * {@inheritDoc}
     * <p>
     * In the case of this feature, the sampling_rate and other_feature_values
     * parameters are ignored.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        return cepCoefficients(other_feature_values[0]);
    }

    @Override
    public Object clone() {
        return new ConstantQ();
    }

    @Override
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            return Integer.toString(this.numCepstra);
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
                int val = Integer.parseInt(value);
                if (val <= 0.0) {
                    throw new Exception("Alpha must be a positive value");
                } else {
                    this.numCepstra = val;
                }
            } catch (NumberFormatException e) {
                throw new Exception("Lambda value must be a double");
            }
            break;
        default:
            throw new Exception(
                    "INTERNAL ERROR: invalid index passed to ConstantQ:setElement");
        }
    }

    /**
     * Borrowed from Orange Cow MFCC implementation (BSD) Cepstral coefficients
     * are calculated from the output of the Non-linear Transformation method<br>
     * calls: none<br>
     * called by: featureExtraction
     * 
     * @param f
     *            Output of the Non-linear Transformation method
     * @return Cepstral Coefficients
     */
    public double[] cepCoefficients(double f[]) {
        double cepc[] = new double[this.numCepstra];

        for (int i = 0; i < cepc.length; i++) {
            for (int j = 1; j <= f.length; j++) {
                cepc[i] += f[j - 1]
                        * Math.cos(Math.PI * i / f.length * (j - 0.5));
            }
        }

        return cepc;
    }
}
