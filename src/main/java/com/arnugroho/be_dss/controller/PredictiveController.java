package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.PredictiveDto;
import com.arnugroho.be_dss.service.PredictiveService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ai.onnxruntime.*;

import java.util.*;


@RestController
@RequestMapping("/Predictive")
public class PredictiveController {
    private final PredictiveService predictiveService;

    public PredictiveController(PredictiveService predictiveService) {
        this.predictiveService = predictiveService;
    }

    @PostMapping("/paged")
    public DefaultPageResponse<List<PredictiveDto>> getPaged(@RequestBody PageableRequest<PredictiveDto> request) {
        Page<PredictiveDto> pagedData = predictiveService.findPages(request);
//        List<JsonNode> result = pagedData.getContent().stream().map(AlternativeController::assemblyMapping).collect(Collectors.toList());

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }



//    @GetMapping("/calculate")
//    public float calculatePredictive() throws JsonProcessingException {
//        String json = "{\n" +
//                "\"PayZone\": 4,\n" +
//                "\"MaritalDesc\": 1,\n" +
//                "\"EmployeeType\": 4,\n" +
//                "\"statusDelete\": true,\n" +
//                "\"TrainingOutcome\": 3,\n" +
//                "\"alternativeName\": \"Susi Susanti\",\n" +
//                "\"Engagement Score\": 3,\n" +
//                "\"PerformanceScore\": 2,\n" +
//                "\"Satisfaction Score\": 4,\n" +
//                "\"TrainingDurationDays\": 3,\n" +
//                "\"Work-Life Balance Score\": 3,\n" +
//                "\"EmployeeClassificationType\": 2\n" +
//                "}";
//
//        // Urutan fitur yang diharapkan
//        String[] featureOrder = {
//                "EmployeeType", "PayZone", "EmployeeClassificationType", "MaritalDesc",
//                "PerformanceScore", "Engagement Score", "Satisfaction Score",
//                "Work-Life Balance Score", "TrainingOutcome", "TrainingDurationDays"
//        };
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = mapper.readValue(json, LinkedHashMap.class);
//
//        float[] inputData = new float[featureOrder.length];
//
//        for (int i = 0; i < featureOrder.length; i++) {
//            String key = featureOrder[i];
//            Object val = map.get(key);
//            if (val == null) {
//                System.err.println("WARNING: Missing value for key: " + key + ", defaulting to 0");
//                inputData[i] = 0f;
//            } else {
//                try {
//                    inputData[i] = Float.parseFloat(val.toString());
//                } catch (NumberFormatException e) {
//                    System.err.println("ERROR: Value for key '" + key + "' is not a number: " + val);
//                    inputData[i] = 0f;
//                }
//            }
//        }
//
//        float[] prediction;
//
////        float[] inputData = new float[inputDataOrder.length];
//        try (OrtEnvironment env = OrtEnvironment.getEnvironment();
//             OrtSession session = env.createSession("src/main/resources/random_forest_model.onnx", new OrtSession.SessionOptions())) {
//
//            OnnxTensor inputTensor = OnnxTensor.createTensor(env, new float[][]{inputData});
//            OrtSession.Result result = session.run(Collections.singletonMap("float_input", inputTensor));
//
//            Object resultValue = result.get(0).getValue();
//
//
//
//            if (resultValue instanceof long[] longResult) {
//                prediction = new float[longResult.length];
//                for (int i = 0; i < longResult.length; i++) {
//                    prediction[i] = (float) longResult[i];
//                }
//                System.out.println("Prediction: " + Arrays.toString(prediction));
//            } else {
//                throw new IllegalArgumentException("Unsupported result type: " + resultValue.getClass().getName());
//            }
//        } catch (OrtException e) {
//            throw new RuntimeException(e);
//        }
//
//        return prediction[0];
//    }


}
