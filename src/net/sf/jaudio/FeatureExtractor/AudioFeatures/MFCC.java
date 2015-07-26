package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

import org.oc.ocvolume.dsp.featureExtraction;

/**
 * <p>
 * Utilizes the MFCC code from the OrangeCow Volume project.
 * </p>
 * <p>
 * S. Pfeiffer, and C. Parker, and T. Vincent. 2005. <i>OC volume: Java speech
 * recognition engine</i>. 2005. [cited April 14, 2005].
 * </p>
 * 
 * @author Daniel McEnnis
 */
public class MFCC extends FeatureExtractor {

    featureExtraction fe;

    /**
     * Construct a MFCC object, setting definition, dependencies, and offsets.
     */
    public MFCC() {
        this.name = "MFCC";
        this.description = "MFCC calculations based upon Orange Cow code";
        this.attributes = new String[] { "Number of Coeffecients" };
        this.definition = new FeatureDefinition(this.name, this.description, true, 13,
                this.attributes);
        this.dependencies = new String[] { "Magnitude Spectrum" };
        this.offsets = new int[] { 0 };
        this.fe = new featureExtraction();
    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {

        int[] cbin = this.fe.fftBinIndices(sampling_rate,
                other_feature_values[0].length);
        double[] fbank = this.fe.melFilter(other_feature_values[0], cbin);
        double[] f = this.fe.nonLinearTransformation(fbank);
        double[] cepc = this.fe.cepCoefficients(f);
        return cepc;
    }

    @Override
    public Object clone() {
        return new MFCC();
    }

    @Override
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            return Integer.toString(this.fe.numCepstra);
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
                this.fe.numCepstra = Integer.parseInt(value);
                String name = this.definition.name;
                String description = this.definition.description;
                String[] attributes = this.definition.attributes;
                this.definition = new FeatureDefinition(name, description,
                        true, this.fe.numCepstra, attributes);
                if (this.parent != null) {
                    this.parent.updateTable();
                }
            } catch (NumberFormatException e) {
                throw new Exception("Lambda value must be a double");
            }
            break;
        default:
            throw new Exception(
                    "INTERNAL ERROR: invalid index passed to LPC:setElement");
        }
    }

}
