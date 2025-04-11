package com.arnugroho.be_dss.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.arnugroho.be_dss.mapper.AlternativeMapper;
import com.arnugroho.be_dss.model.dto.AlternativeDto;
import com.arnugroho.be_dss.model.dto.PredictiveDto;
import com.arnugroho.be_dss.model.entity.AlternativeEntity;
import com.arnugroho.be_dss.model.entity.AlternativeEntity_;
import com.arnugroho.be_dss.model.entity.PredictiveEntity;
import com.arnugroho.be_dss.repository.AlternativeRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlternativeServiceImpl extends CommonBaseServiceImpl<AlternativeEntity, Long, AlternativeDto> implements AlternativeService {

    private final PredictiveService predictiveService;

    public AlternativeServiceImpl(AlternativeRepository repository, AlternativeMapper mapper, PredictiveService predictiveService) {
        super(repository, mapper);

        this.predictiveService = predictiveService;
    }

    @Override
    public AlternativeEntity save(AlternativeDto param) {

        if(param.getDataValue().has("statusDelete")) {
            boolean statusDelete = param.getDataValue().get("statusDelete").asBoolean();
            param.setStatusDelete(!statusDelete);
        } else {
            param.setStatusDelete(true);
        }

        String alternativeName = param.getDataValue().get("alternativeName").asText();
        param.setAlternativeName(alternativeName);

        String description = param.getDataValue().has("description")? param.getDataValue().get("description").asText() : "";
        param.setDescription(description);

        AlternativeEntity alternative = super.save(param);

        Float hasilPredictive = calculatePredictive(param.getDataValue());
        PredictiveDto predictiveDto = new PredictiveDto();
        predictiveDto.setAlternativeId(alternative.getId());
        predictiveDto.setHasil(hasilPredictive);

        predictiveService.save(predictiveDto);

        return alternative;
    }

    @Override
    public void update(AlternativeDto param) {
        if(param.getDataValue().has("statusDelete")) {
            boolean statusDelete = param.getDataValue().get("statusDelete").asBoolean();
            param.setStatusDelete(!statusDelete);
        }

        String alternativeName = param.getDataValue().get("alternativeName").asText();
        param.setAlternativeName(alternativeName);

        String description = param.getDataValue().has("description")? param.getDataValue().get("description").asText() : "";
        param.setDescription(description);

        String uuid = param.getDataValue().has("uuid")? param.getDataValue().get("uuid").asText() : "";
        param.setUuid(uuid);

        AlternativeDto alternative = findByUuid(uuid);
        Optional<PredictiveEntity> cekDbPredictive = predictiveService.findByAlternativeId(alternative.getId());

        if (cekDbPredictive.isPresent()) {

            Float hasilPredictive = calculatePredictive(param.getDataValue());
            PredictiveDto predictiveDto = new PredictiveDto();
            predictiveDto.setAlternativeId(alternative.getId());
            predictiveDto.setHasil(hasilPredictive);
            predictiveDto.setId(cekDbPredictive.get().getId());

            predictiveService.update(predictiveDto);
        } else {

            Float hasilPredictive = calculatePredictive(param.getDataValue());
            PredictiveDto predictiveDto = new PredictiveDto();
            predictiveDto.setAlternativeId(alternative.getId());
            predictiveDto.setHasil(hasilPredictive);

            predictiveService.save(predictiveDto);
        }

        super.update(param);
    }

    @Override
    public Specification<AlternativeEntity> extraSpecification(JsonNode filter) {
        Specification<AlternativeEntity> specification = createDeleteStatusSpecification();
        if (!filter.isEmpty()) {
            Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();


            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> jsonField = fields.next();
                if (!jsonField.getValue().toString().isEmpty()) {
                    String jsonFilterPath = "$." + jsonField.getKey() + " ? (@ like_regex \"" + jsonField.getValue() + "\" flag \"i\")";
                    specification = specification.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.function("jsonb_path_exists",
                                    Boolean.class,
                                    root.get(AlternativeEntity_.DATA_VALUE),
                                    criteriaBuilder.literal(jsonFilterPath)
                            ).in(Boolean.TRUE));
                }
            }
        }
        return specification;
    }

    @Override
    public void delete(AlternativeDto param) {
        param.setStatusDelete(!param.isStatusDelete());
        super.update(param);
    }


    public float calculatePredictive(JsonNode json) {

        // Urutan fitur yang diharapkan
        String[] featureOrder = {
                "EmployeeType", "PayZone", "EmployeeClassificationType", "MaritalDesc",
                "PerformanceScore", "Engagement Score", "Satisfaction Score",
                "Work-Life Balance Score", "TrainingOutcome", "TrainingDurationDays"
        };



        float[] inputData = new float[featureOrder.length];

        for (int i = 0; i < featureOrder.length; i++) {
            String key = featureOrder[i];
            Object val = json.get(key);
            if (val == null) {
                System.err.println("WARNING: Missing value for key: " + key + ", defaulting to 0");
                inputData[i] = 0f;
            } else {
                try {
                    inputData[i] = Float.parseFloat(val.toString());
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Value for key '" + key + "' is not a number: " + val);
                    inputData[i] = 0f;
                }
            }
        }

        float[] prediction;

//        float[] inputData = new float[inputDataOrder.length];
        try (OrtEnvironment env = OrtEnvironment.getEnvironment();
             OrtSession session = env.createSession("src/main/resources/random_forest_model.onnx", new OrtSession.SessionOptions())) {

            OnnxTensor inputTensor = OnnxTensor.createTensor(env, new float[][]{inputData});
            OrtSession.Result result = session.run(Collections.singletonMap("float_input", inputTensor));

            Object resultValue = result.get(0).getValue();



            if (resultValue instanceof long[] longResult) {
                prediction = new float[longResult.length];
                for (int i = 0; i < longResult.length; i++) {
                    prediction[i] = (float) longResult[i];
                }
                System.out.println("Prediction: " + Arrays.toString(prediction));
            } else {
                throw new IllegalArgumentException("Unsupported result type: " + resultValue.getClass().getName());
            }
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
        System.out.println(prediction[0]);
        return prediction[0];
    }
}
