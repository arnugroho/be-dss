package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import com.arnugroho.be_dss.repository.CriteriaRepository;
import com.arnugroho.be_dss.repository.PairwiseComparisonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AhpServiceImpl implements AhpService {


    private final CriteriaRepository criteriaRepository;
    private final PairwiseComparisonRepository pairwiseComparisonRepository;
    private final CriteriaService criteriaService;

    public AhpServiceImpl(CriteriaRepository criteriaRepository, PairwiseComparisonRepository pairwiseComparisonRepository, CriteriaService criteriaService) {
        this.criteriaRepository = criteriaRepository;
        this.pairwiseComparisonRepository = pairwiseComparisonRepository;
        this.criteriaService = criteriaService;
    }

    @Override
    public Map<String, Object> calculateAhpForLevel(Long parentId) {
        // get parent
        CriteriaDto parent = criteriaService.findById(parentId);
        // Step 1: Fetch Criteria for the Level
        List<CriteriaEntity> criteriaList = criteriaRepository.findByCriteriaParentId(parentId);

        // Step 2: Fetch Pairwise Comparisons
        List<Long> criteriaIds = criteriaList.stream().map(CriteriaEntity::getId).collect(Collectors.toList());
        List<PairwiseComparisonEntity> pairwiseList = pairwiseComparisonRepository.findByCriteria1IdInAndCriteria2IdIn(criteriaIds, criteriaIds);

        // Step 3: Build Pairwise Matrix
        int n = criteriaList.size();
        double[][] pairwiseMatrix = new double[n][n];

        for (PairwiseComparisonEntity comparison : pairwiseList) {
            int i = criteriaIds.indexOf(comparison.getCriteria1Id());
            int j = criteriaIds.indexOf(comparison.getCriteria2Id());
            pairwiseMatrix[i][j] = comparison.getScore();
            pairwiseMatrix[j][i] = 1.0 / comparison.getScore();
        }

        // mapping berdasarkan criteria list . nomer list criteria dipadankankan jadi angka
//        int[] noCriteria = new int[n];
//        for (int i = 0; i < n; i++) {
//            noCriteria[i] = 0;
//        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    pairwiseMatrix[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print( pairwiseMatrix[i][j]);
                System.out.print("  ");
            }
            System.out.println();
        }


        // Step 4: Normalize Matrix and Calculate Weights
        double[] weights = calculateWeights(pairwiseMatrix);

        // Step 5: Calculate Ax
        double[] ax = calculateAx(pairwiseMatrix, weights);

        // Step 6: Calculate Lambda Max
        double lambdaMax = calculateLambdaMax(ax, weights);

        // Step 7: Calculate CI and CR
        double ci = (lambdaMax - n) / (n - 1);
        double ri = getRandomIndex(n);
        Double cr = (ri == 0) ? null : ci / ri; // Handle RI = 0 for small matrices

        List<Map<String, Object>> criteriaWeights = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("criteriaName", criteriaList.get(i).getCriteriaName());
            map.put("weight",weights[i]);
            criteriaWeights.add(map);
        }

        // Step 8: Return Results
        Map<String, Object> results = new HashMap<>();
        results.put("criteriaWeights", criteriaWeights);
        results.put("lambdaMax", lambdaMax);
        results.put("ci", ci);
        results.put("ri", ri);
        results.put("cr", cr);
        results.put("parentId", parent.getId());
        results.put("parentName", parent.getCriteriaName());

        return results;
    }

    private double[] calculateWeights(double[][] pairwiseMatrix) {
        int n = pairwiseMatrix.length;
        double[] colSums = new double[n];
        double[] rowSums = new double[n];

        // Calculate column sums
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                colSums[j] += pairwiseMatrix[i][j];
            }
        }

        // Normalize matrix and calculate row sums
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pairwiseMatrix[i][j] /= colSums[j];
                rowSums[i] += pairwiseMatrix[i][j];
            }
        }

        // Calculate weights
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = rowSums[i] / n;
        }

        return weights;
    }

    private double[] calculateAx(double[][] pairwiseMatrix, double[] weights) {
        int n = pairwiseMatrix.length;
        double[] ax = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ax[i] += pairwiseMatrix[i][j] * weights[j];
            }
        }
        return ax;
    }

    private double calculateLambdaMax(double[] ax, double[] weights) {
        double sum = 0.0;
        for (int i = 0; i < ax.length; i++) {
            sum += ax[i] / weights[i];
        }
        return sum / ax.length;
    }

    private double getRandomIndex(int n) {
        // Random Index (RI) values for different matrix sizes
        double[] riValues = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};
        return n <= riValues.length ? riValues[n - 1] : 1.49;
    }

}
