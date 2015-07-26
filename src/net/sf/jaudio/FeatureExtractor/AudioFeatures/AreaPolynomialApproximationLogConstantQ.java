package net.sf.jaudio.FeatureExtractor.AudioFeatures;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

/**
 * 2D Polynomial Approximation Feature
 *
 * Creates a set of polynomial factors for a 2D polynomial of order k*l where k
 * is the number of terms in the x direction and l is the number of terms in the
 * y direction. The source is a square matrix of DSP data of some sort. (This
 * version is over FFT data.) The output is a vector of the coeffecients of the
 * polynomial that best fits this data.
 * 
 * @author Daniel McEnnis
 */
public class AreaPolynomialApproximationLogConstantQ extends FeatureExtractor {

    int windowLength = 50;

    int featureLength = 20;

    int k = 10;

    int l = 5;

    DenseDoubleMatrix2D terms;

    DenseDoubleMatrix2D z;

    /**
     * Constructor that sets description, dependencies, and offsets from
     * FeatureExtractor
     */
    public AreaPolynomialApproximationLogConstantQ() {
        this.name = "2D Polynomial Approximation of Log of ConstantQ";
        this.description = "coeffecients of 2D polynomial best describing the input matrix.";
        this.attributes = new String[] { "horizontal size (window length)",
                "vertical size (number of feature dimensions)",
                "number of x (horizontal) terms",
                "number of y (vertical) terms" };

        this.definition = new FeatureDefinition(this.name, this.description,
                true, 0, this.attributes);

        this.dependencies = new String[this.windowLength];
        for (int i = 0; i < this.dependencies.length; ++i) {
            this.dependencies[i] = "Log of ConstantQ";
        }

        this.offsets = new int[this.windowLength];
        for (int i = 0; i < this.offsets.length; ++i) {
            this.offsets[i] = 0 - i;
        }

        this.terms = new DenseDoubleMatrix2D(this.k * this.l, this.windowLength
                * this.featureLength);
        this.z = new DenseDoubleMatrix2D(1, this.featureLength
                * this.windowLength);
        calcTerms(this.terms);
    }

    /**
     * 
     * {@inheritDoc}
     * <p>
     * Calculates based on windows of magnitude spectrum. Encompasses portion of
     * Moments class, but has a delay of lengthOfWindow windows before any
     * results are calculated.
     * </p>
     */
    @Override
    public double[] extractFeature(double[] samples, double sampling_rate,
            double[][] other_feature_values) throws Exception {
        if ((this.featureLength != other_feature_values[0].length)
                || (this.windowLength != other_feature_values.length)) {
            this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                    this.windowLength * this.featureLength);
            this.z = new DenseDoubleMatrix2D(1, this.featureLength
                    * this.windowLength);
            calcTerms(this.terms);
        }
        for (int i = 0; i < this.windowLength; ++i) {
            for (int j = 0; j < this.featureLength; ++j) {
                this.z.set(0, this.windowLength * i + j,
                        other_feature_values[i][j]);
            }
        }
        DoubleMatrix2D retMatrix = (new Algebra()).solve(this.terms, this.z);
        return retMatrix.viewRow(0).toArray();
    }

    @Override
    public void setWindow(int n) throws Exception {
        if (n < 1) {
            throw new Exception(
                    "Area Polynomial Approximation window length must be positive");
        } else {
            this.windowLength = n;
            this.dependencies = new String[this.windowLength];
            this.offsets = new int[this.windowLength];
            for (int i = 0; i < this.windowLength; ++i) {
                this.dependencies[i] = "Magnitude Spectrum";
                this.offsets[i] = 0 - i;
            }
            this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                    this.windowLength * this.featureLength);
            this.z = new DenseDoubleMatrix2D(1, this.featureLength
                    * this.windowLength);
            calcTerms(this.terms);
        }
    }

    @Override
    public String getElement(int index) throws Exception {
        switch (index) {
        case 0:
            // get windowLength
            return Integer.toString(this.windowLength);

        case 1:
            // get featureLength
            return Integer.toString(this.featureLength);

        case 2:
            // get number of x terms
            return Integer.toString(this.k);

        case 3:
            // get number of y terms
            return Integer.toString(this.k);

        default:
            // get number of y terms
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaPolynomialApproximation:getElement");
        }
    }

    @Override
    public void setElement(int index, String value) throws Exception {
        switch (index) {
        case 0:
            // get windowLength
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Area Polynomial Approximation window length must be positive");
                } else {
                    this.windowLength = val;
                    this.dependencies = new String[this.windowLength];
                    this.offsets = new int[this.windowLength];
                    for (int i = 0; i < this.windowLength; ++i) {
                        this.dependencies[i] = "Magnitude Spectrum";
                        this.offsets[i] = 0 - i;
                    }
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "horizontal (windowLength) of Area Polynomial Approximation must be an integer");
            }
            break;

        case 1:
            // get featureLength
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Area Polynomial Approximation feature dimension length must be positive");
                } else {
                    this.featureLength = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "vertical (feature dimensions) of Area Polynomial Approximation must be an integer");
            }
            break;

        case 2:
            // get number of x terms
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Number of x terms in Area Polynomial Approximation must be positive");
                } else {
                    this.k = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of x terms in Area Polynomial Approximation must be an integer");
            }
            break;

        case 3:
            // get number of y terms
            try {
                int val = Integer.parseInt(value);
                if (val < 1) {
                    throw new Exception(
                            "Number of y terms in Area Polynomial Approximation must be positive");
                } else {
                    this.l = val;
                    this.terms = new DenseDoubleMatrix2D(this.k * this.l,
                            this.windowLength * this.featureLength);
                    this.z = new DenseDoubleMatrix2D(1, this.featureLength
                            * this.windowLength);
                    calcTerms(this.terms);
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of y terms of Area Polynomial Approximation must be an integer");
            }
            break;

        default:
            throw new Exception("INTERNAL ERROR: invalid index " + index
                    + " sent to AreaPolynomialApproximation:getElement");
        }
    }

    @Override
    public Object clone() {
        AreaPolynomialApproximation ret = new AreaPolynomialApproximation();
        return ret;
    }

    private void calcTerms(DoubleMatrix2D terms) {
        terms.assign(0.0);
        for (int x = 0; x < this.windowLength; ++x) {
            for (int y = 0; y < this.featureLength; ++y) {
                for (int i = 0; i < this.k; ++i) {
                    for (int j = 0; j < this.l; ++j) {
                        terms.set(this.l * i + j, this.featureLength * x + y,
                                Math.pow(x, i) * Math.pow(y, j));
                    }
                }
            }
        }
    }

}
