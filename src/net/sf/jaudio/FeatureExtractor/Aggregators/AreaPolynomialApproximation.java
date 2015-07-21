/**
 * AreaPolynomialApproximation
 * created August 21, 2010
 * Author: Daniel McEnnis
 * Published under the LGPL see license.txt or at http://www.fsf.org
 * Utilizes the colt matrix package under either the LGPL or BSD license (see colt's online documentation for specifics).
 */
package net.sf.jaudio.FeatureExtractor.Aggregators;

import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.AggregatorDefinition;
import net.sf.jaudio.FeatureExtractor.ACE.DataTypes.FeatureDefinition;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

/**
 * <h2>2D Polynomial Approximation</h2>
 * <p>
 * This specific aggregator was first released in an August 2010 working paper
 * by Daniel McEnnis. It transforms a 2D matrix of signal feature vectors into a
 * set of coefficients of the polynomial function f(x,y) that bests matches the
 * given signal. It is calculated by constructing a matrix of coefficients by
 * substituting concrete data points into each term that coefficient is attached
 * to (such as x^2*y^3) and a answer matrix is created from the signal at that
 * matrix index. The coefficients are then calculated by a least squares
 * minimization matrix solver (colt matrix function).
 * </p>
 * <p>
 * Utilizes the Colt java matrix package.
 * </p>
 * 
 * @author Daniel McEnnis
 *
 */
public class AreaPolynomialApproximation extends Aggregator {

    int xDim = 20;
    int yDim = 5;

    int windowLength = 1;
    int featureLength = 1;

    DenseDoubleMatrix2D terms;

    DenseDoubleMatrix2D z;

    String[] featureNames = null;
    int[] featureNameIndecis = null;

    public AreaPolynomialApproximation() {
        this.metadata = new AggregatorDefinition(
                "2D Polynomial Approximation of a signal",
                "Calculates the coeefecients of a polynomial that approximates the signal",
                false, null);
    }

    @Override
    public void aggregate(double[][][] values) {
        this.result = null;
        int offset = super.calculateOffset(values, this.featureNameIndecis);
        int[][] featureIndecis = super.collapseFeatures(values,
                this.featureNameIndecis);
        this.result[0] = 0.0;
        this.windowLength = this.featureNameIndecis.length - offset;
        this.featureLength = featureIndecis[0].length;
        for (int i = offset; i < values.length; ++i) {
            for (int j = 0; j < featureIndecis.length; ++j) {
                this.result[0] += values[i][featureIndecis[j][0]][featureIndecis[j][1]];
            }
        }
        this.terms = new DenseDoubleMatrix2D(this.xDim * this.yDim, this.windowLength
                * this.featureLength);
        this.z = new DenseDoubleMatrix2D(1, this.featureLength);
        calcTerms(this.terms);
        this.result = ((new Algebra()).solve(this.terms, this.z)).viewRow(0).toArray();
    }

    @Override
    public Object clone() {
        AreaPolynomialApproximation ret = new AreaPolynomialApproximation();
        if (this.featureNames != null) {
            ret.featureNames = this.featureNames.clone();
        }
        if (this.featureNameIndecis != null) {
            ret.featureNameIndecis = this.featureNameIndecis.clone();
        }
        return ret;
    }

    @Override
    public FeatureDefinition getFeatureDefinition() {
        return this.definition;
    }

    @Override
    public String[] getFeaturesToApply() {
        return this.featureNames;
    }

    @Override
    public void init(int[] featureIndecis) throws Exception {
        if (featureIndecis.length != this.featureNames.length) {
            throw new Exception(
                    "INTERNAL ERROR (Agggregator.AreaPolynomialApproximation): number of feature indeci does not match number of features");
        }
        this.featureNameIndecis = featureIndecis;
    }

    @Override
    public String[] getParamaters() {
        return new String[] { Integer.toString(this.xDim), Integer.toString(this.yDim) };
    }

    private void calcTerms(DoubleMatrix2D terms) {
        terms.assign(0.0);
        for (int x = 0; x < this.windowLength; ++x) {
            for (int y = 0; y < this.featureLength; ++y) {
                for (int i = 0; i < this.xDim; ++i) {
                    for (int j = 0; j < this.yDim; ++j) {
                        terms.set(this.yDim * i + j, this.featureLength * x + y,
                                Math.pow(x, i) * Math.pow(y, j));
                    }
                }
            }
        }
    }

    @Override
    public void setParameters(String[] featureNames, String[] params)
            throws Exception {
        // get number of x terms
        if (params.length != 2) {
            this.xDim = 20;
            this.yDim = 5;
        } else {
            try {
                int val = Integer.parseInt(params[0]);
                if (val < 1) {
                    throw new Exception(
                            "Number of x terms in Area Polynomial Approximation must be positive");
                } else {
                    this.xDim = val;
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of x terms in Area Polynomial Approximation must be an integer");
            }

            // get number of y terms
            try {
                int val = Integer.parseInt(params[1]);
                if (val < 1) {
                    throw new Exception(
                            "Number of y terms in Area Polynomial Approximation must be positive");
                } else {
                    this.yDim = val;
                }
            } catch (Exception e) {
                throw new Exception(
                        "Number of y terms of Area Polynomial Approximation must be an integer");
            }
        }
        this.featureNames = featureNames;
        String names = featureNames[0];
        for (int i = 1; i < featureNames.length; ++i) {
            names += " " + featureNames[i];
        }
        this.definition = new FeatureDefinition("2D Polynomial Approximation: "
                + names, "2D moments constructed from features " + names + ".",
                true, 0);
    }
}
