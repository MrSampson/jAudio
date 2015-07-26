package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;

/**
 * <p>
 * This class implements 2D statistical methods of moments as implemented by
 * Fujinaga (1997). The number of consecutive windows that one can edit across
 * is an editable property. Furthermore, this classes window property is
 * affected by global window size changes. This version calculates across
 * constant q based MFCCs.
 * </p>
 * <p>
 * Fujinaga, I. <i>Adaptive Optical Music Recognition</i>. PhD thesis, McGill
 * University, 1997.
 * </p>
 * 
 * Created by Daniel McEnnis on August 18, 2010. Published under the LGPL
 * license. See most recent LGPL license on www.fsf.org for a copy of this
 * license.
 *
 * @author Daniel McEnnis
 */
public class AreaMomentsConstantQMFCC extends FeatureExtractor {

    int lengthOfWindow = 10;

    double x;

    double y;

    double x2;

    double xy;

    double y2;

    double x3;

    double x2y;

    double xy2;

    double y3;

    /**
     * Constructor that sets description, dependencies, and offsets from
     * FeatureExtractor
     */
    public AreaMomentsConstantQMFCC() {
        this.name = "Area Method of Moments of ConstantQ-based MFCCs";
        this.description = "2D statistical method of moments of ConstantQ-based MFCCs";
        this.attributes = new String[] { "Area Method of Moments Window Length" };

        this.definition = new FeatureDefinition(this.name, this.description, true, 10,
                this.attributes);
        this.dependencies = new String[this.lengthOfWindow];
        for (int i = 0; i < this.dependencies.length; ++i) {
            this.dependencies[i] = "ConstantQ derived MFCCs";
        }
        this.offsets = new int[this.lengthOfWindow];
        for (int i = 0; i < this.offsets.length; ++i) {
            this.offsets[i] = 0 - i;
        }

    }

    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        double[] ret = new double[10];
        double sum = 0.0;
        for (int i = 0; i < other_feature_values.length; ++i) {
            for (int j = 0; j < other_feature_values[i].length; ++j) {
                sum += other_feature_values[i][j];
            }
        }
        if (sum == 0.0) {
            java.util.Arrays.fill(ret, 0.0);
            return ret;
        }
        for (int i = 0; i < other_feature_values.length; ++i) {
            for (int j = 0; j < other_feature_values[i].length; ++j) {
                double tmp = other_feature_values[i][j] / sum;
                this.x += tmp * i;
                this.y += tmp * j;
                this.x2 += tmp * i * i;
                this.xy += tmp * i * j;
                this.y2 += tmp * j * j;
                this.x3 += tmp * i * i * i;
                this.x2y += tmp * i * i * j;
                this.xy2 += tmp * i * j * j;
                this.y3 += tmp * j * j * j;
            }
        }
        ret[0] = sum;
        ret[1] = this.x;
        ret[2] = this.y;
        ret[3] = this.x2 - this.x * this.x;
        ret[4] = this.xy - this.x * this.y;
        ret[5] = this.y2 - this.y * this.y;
        ret[6] = 2 * Math.pow(this.x, 3.0) - 3 * this.x * this.x2 + this.x3;
        ret[7] = 2 * this.x * this.xy - this.y * this.x2 + this.x2 * this.y;
        ret[8] = 2 * this.y * this.xy - this.x * this.y2 + this.y2 * this.x;
        ret[9] = 2 * Math.pow(this.y, 3.0) - 3 * this.y * this.y2 + this.y3;

        return ret;
    }

    @Override
    public void setWindow(int n) throws Exception {
        if (n < 2) {
            throw new Exception(
                    "Area Method of Moment's Window length must be two or greater");
        } else {
            this.lengthOfWindow = n;
            this.dependencies = new String[this.lengthOfWindow];
            this.offsets = new int[this.lengthOfWindow];
            for (int i = 0; i < this.lengthOfWindow; ++i) {
                this.dependencies[i] = "Magnitude Spectrum";
                this.offsets[i] = 0 - i;
            }
        }
    }

    @Override
    public String getElement(int index) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:getElement");
        } else {
            return Integer.toString(this.lengthOfWindow);
        }
    }

    @Override
    public void setElement(int index, String value) throws Exception {
        if (index != 0) {
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaMoments:setElement");
        } else {
            try {
                int type = Integer.parseInt(value);
                setWindow(type);
            } catch (Exception e) {
                throw new Exception(
                        "Length of Area Method of Moments must be an integer");
            }
        }
    }

    @Override
    public Object clone() {
        AreaMoments ret = new AreaMoments();
        ret.lengthOfWindow = this.lengthOfWindow;
        return ret;
    }

}
