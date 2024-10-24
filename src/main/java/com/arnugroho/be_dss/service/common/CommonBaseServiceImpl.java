package com.arnugroho.be_dss.service.common;


import com.arnugroho.be_dss.configuration.CommonException;
import com.arnugroho.be_dss.configuration.security.UserDetailsImpl;
import com.arnugroho.be_dss.mapper.common.CommonMapper;
import com.arnugroho.be_dss.model.common.CommonDto;
import com.arnugroho.be_dss.model.common.CommonModel;
import com.arnugroho.be_dss.model.common.CommonModel_;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.repository.common.CommonRepository;
import com.arnugroho.be_dss.utils.PageableUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class CommonBaseServiceImpl<T extends CommonModel, ID extends Serializable, DTO extends CommonDto<ID>> implements CommonBaseService<T, ID, DTO> {
    private final CommonRepository<T, ID> commonRepository;
    private final CommonMapper<T, DTO> commonMapper;

    protected final static String EQUALS = "=";
    protected final static String EQUALS_GT = "=>";
    protected final static String EQUALS_LT = "=<";
    protected final static String LIKE = "like";
    protected final static String LIKE_CUSTOM = "like-custom";
    protected final static String GT = ">";
    protected final static String LT = "<";
    protected final static String NOT_EQUAL = "<>";
    protected final static String BETWEEN = "between";
    protected final static String STARTS_WITH = "STARTS WITH";
    protected final static String GREATER_THAN = "GREATER THAN";
    protected final static String IN = "IN";

    public CommonBaseServiceImpl(CommonRepository<T, ID> commonRepository, CommonMapper<T, DTO> commonMapper) {
        this.commonRepository = commonRepository;
        this.commonMapper = commonMapper;

    }

    @Override
    @Transactional
    public T save(DTO param) {
        T entity = commonMapper.fromDto(param);
        entity.setUuid(UUID.randomUUID().toString());
        entity.setCreatedOn(ZonedDateTime.now());
        if (param.getCreatedBy() == null) {
            entity.setCreatedBy(getUserName());
        }
        return commonRepository.save(entity);
    }

    @Override
    @Transactional
    public void update(DTO param) {
        T entity = getEntityByUuid(param.getUuid());
        entity = commonMapper.toEntityForUpdate(param, entity);
        entity.setModifiedOn(ZonedDateTime.now());
        entity.setModifiedBy(getUserName());
        commonRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(DTO param) {
        T entity = commonRepository.findById(param.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity dengan id " + param.getId() + " tidak ditemukan"));
        entity.setDeletedOn(ZonedDateTime.now());
        entity.setDeletedBy(getUserName());
        entity.setStatusDelete(true);
        commonRepository.save(entity);
        commonMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DTO> findAll() {
        return commonMapper.toDtoList(commonRepository.findAll(createDeleteStatusSpecification()));
    }

    @Override
    @Transactional(readOnly = true)
    public DTO findById(ID id) {
        T entity = getEntityById(id);
        return commonMapper.toDto(entity);
    }

    @Override
    public Specification<T> createSpecification(String propertyName, Object value) {
        return createSpecification(propertyName, value, EQUALS);
    }

    @Override
    public Specification<T> createSpecification(String propertyName, Object value, String operation) {
        //TODO Add condition for operation
        if (operation.equalsIgnoreCase(LIKE) && value instanceof String) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get(propertyName)), "%" + String.valueOf(value).toLowerCase() + "%");
        } else if (operation.equalsIgnoreCase(STARTS_WITH) && value instanceof String) {
            return (root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get(propertyName)), String.valueOf(value).toLowerCase() + "%");
        } else if (operation.equalsIgnoreCase(GREATER_THAN)) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(propertyName), (Integer) value);
        } else if (operation.equalsIgnoreCase(IN)) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (value instanceof List<?>) {
                    predicates.add(root.get(propertyName).in(((List<?>) value).toArray()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        } else {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(propertyName), value);
        }
    }

    protected Specification<T> createBetweenSpecification(String propertyName, ZonedDateTime startDate, ZonedDateTime endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(propertyName), startDate, endDate);

    }

    @Transactional(readOnly = true)
    public T getEntityById(ID id) {
        T entity = commonRepository.findOne(createDeleteStatusAndIdSpecification(id))
                .orElseThrow(() -> new EntityNotFoundException("Entity dengan id " + id + " tidak ditemukan"));
        return entity;
    }

    @Transactional(readOnly = true)
    public T getEntityByUuid(String uuid) {
        T entity = commonRepository.findOne(createDeleteStatusAndUuidSpecification(uuid))
                .orElseThrow(() -> new EntityNotFoundException("Entity dengan uuid " + uuid + " tidak ditemukan"));
        return entity;
    }

    protected Specification<T> createDeleteStatusAndIdSpecification(ID id) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicateForId = criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id));
            Predicate predicateStatusDelete = getDeleteStatusPredicate(root, criteriaBuilder);
            return criteriaBuilder.and(predicateForId, predicateStatusDelete);
        };
    }

    protected Specification<T> createDeleteStatusSpecification() {
        return (root, query, criteriaBuilder) -> getDeleteStatusPredicate(root, criteriaBuilder);
    }

    protected Predicate getDeleteStatusPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate predicateForStatusDeleteIsFalse = criteriaBuilder.isFalse(root.get(CommonModel_.statusDelete));
        Predicate predicateForStatusDeleteIsNull = criteriaBuilder.isNull(root.get(CommonModel_.statusDelete));
        return criteriaBuilder.or(predicateForStatusDeleteIsFalse, predicateForStatusDeleteIsNull);
    }

    @Override
    public Page<DTO> findPages(PageableRequest<DTO> request) {

        Pageable pageable = PageableUtil.createPageableRequest(request);
        Specification<T> specification = extraSpecification(request.getFilter());
        Page<T> productEntityPage;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        T entity = commonMapper.fromDto(request.getFilter());
        JsonNode filter = objectMapper.valueToTree(entity);

        if (request.getFilter().getNomenklatur() != null) {
            String[] arrNo = request.getFilter().getNomenklatur().split("-");
            if (arrNo.length == 2) {
                try {
                    ((ObjectNode) filter).put("id", Long.valueOf(arrNo[1]));
                } catch (Exception e) {
                    ((ObjectNode) filter).put("id", 0);
                }
            } else {
                try {
                    ((ObjectNode) filter).put("id", Long.valueOf(arrNo[0]));
                } catch (Exception e) {
                    if (!arrNo[0].isEmpty()) {
                        ((ObjectNode) filter).put("id", 0);
                    }
                }
            }
        }
        ((ObjectNode) filter).remove("nomenklatur");
        ((ObjectNode) filter).remove("statusDelete");

        Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();

        String operation = LIKE;
        Object value;
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            if (!jsonField.getValue().isObject()) {
                if (jsonField.getKey().equalsIgnoreCase(CommonModel_.ID)) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();

                    // Convert long column to string for pattern matching
//                        Expression<String> castedColumn = criteriaBuilder.function("CAST", String.class, joinEntityB.get("longColumn"), criteriaBuilder.literal(String.class));

                } else if (jsonField.getValue().isNumber()) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();
                } else {
                    operation = LIKE;
                    value = jsonField.getValue().asText();
                }

                specification = specification.and(createSpecification(jsonField.getKey().toString(), value, operation));
//
            }
        }

        productEntityPage = commonRepository.findAll(specification, pageable);


        return new PageImpl<>(productEntityPage.getContent().stream().map(commonMapper::toDto).collect(Collectors.toList()), pageable, productEntityPage.getTotalElements());

    }

    @Override
    public Page<DTO> findPagesJsonNode(PageableRequest<JsonNode> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        JsonNode filter = request.getFilter();
        ((ObjectNode) filter).remove("current");
        ((ObjectNode) filter).remove("pageSize");

        Specification<T> specification = extraSpecification(request.getFilter());

        if (request.getFilter().has("nomenklatur")) {
            String[] arrNo = request.getFilter().get("nomenklatur").asText().split("-");
            if (arrNo.length == 2) {
                try {
                    ((ObjectNode) filter).put("id", Long.valueOf(arrNo[1]));
                } catch (Exception e) {
                    ((ObjectNode) filter).put("id", 0);
                }
            } else {
                try {
                    ((ObjectNode) filter).put("id", Long.valueOf(arrNo[0]));
                } catch (Exception e) {
                    if (!arrNo[0].isEmpty()) {
                        ((ObjectNode) filter).put("id", 0);
                    }
                }
            }
            ((ObjectNode) filter).remove("nomenklatur");

            specification = specification.and(createSpecification(CommonModel_.ID, arrNo[0], EQUALS));
        }

        if (filter.has("uuid")) {
            specification = specification.and(createSpecification(CommonModel_.UUID, filter.get("uuid").asText(), EQUALS));
        }



        Page<T> page = commonRepository.findAll(specification, pageable);
        return new PageImpl<>(page.getContent().stream().map(commonMapper::toDto).collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    protected String getUserName() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        return userDetails.getUsername();
        return "admin";
    }

    @Override
    public DTO findByUuid(String uuid) {
        T entity = commonRepository.findOne(createDeleteStatusAndUuidSpecification(uuid))
                .orElseThrow(() -> new EntityNotFoundException("Entity dengan uuid " + uuid + " tidak ditemukan"));
        return commonMapper.toDto(entity);
    }

    protected Specification<T> createDeleteStatusAndUuidSpecification(String uuid) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicateForId = criteriaBuilder.and(criteriaBuilder.equal(root.get("uuid"), uuid));
            Predicate predicateStatusDelete = getDeleteStatusPredicate(root, criteriaBuilder);
            return criteriaBuilder.and(predicateForId, predicateStatusDelete);
        };
    }

    protected void checkFolder(Path path) {
        File directory = new File(path.toUri());
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    protected String getDocumentName(String originalFilename, String documentCode) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return documentCode + fileExtension;
    }

    protected String uploadDocument(MultipartFile file, String uuid, String rootFolderPath) {

        try {
            String folderPath = rootFolderPath + "/" + uuid;
            Path path = Paths.get(folderPath);
            checkFolder(path);
            String newFilename = getDocumentName(Objects.requireNonNull(file.getOriginalFilename()), String.valueOf(new Date().getTime()));
            Path filePath = path.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (Exception e) {
            throw new CommonException("Gagal Upload File");
        }
    }

    public JsonNode mappingJsonValue(JsonNode eavAttribute, JsonNode source) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.createObjectNode();
        if (eavAttribute.isArray()) {
            for (JsonNode jsonNode : eavAttribute) {
                if (source.findValue(jsonNode.get("dataIndex").asText()) != null) {
                    ((ObjectNode) result).set(jsonNode.get("dataIndex").asText(), source.findValue(jsonNode.get("dataIndex").asText()));
                }
            }
        }
        return result;
    }

    public JsonNode mappingJsonValueIgnoreNull(JsonNode eavAttribute, JsonNode source) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.createObjectNode();
        if (eavAttribute.isArray()) {
            for (JsonNode jsonNode : eavAttribute) {
                String key = jsonNode.get("dataIndex").asText();
                JsonNode value = source.findValue(jsonNode.get("dataIndex").asText());
                if (value != null) {
                    ((ObjectNode) result).set(key, value);
                }
            }
        }
        return result;
    }

    @Override
    public Specification<T> extraSpecification(DTO dto) {
        return createDeleteStatusSpecification();
    }

    @Override
    public Specification<T> extraSpecification(JsonNode filter) {
        Specification specification = createDeleteStatusSpecification();
        if (!filter.isEmpty()) {
            Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();


//            while (fields.hasNext()) {
//                Map.Entry<String, JsonNode> jsonField = fields.next();
//                if (!jsonField.getValue().toString().isEmpty()) {
//                    String jsonFilterPath = "$." + jsonField.getKey() + " ? (@ like_regex \"" + jsonField.getValue().asText() + "\" flag \"i\")";
//                    specification = specification.and((root, query, criteriaBuilder) ->
//                            criteriaBuilder.function("jsonb_path_exists",
//                                    Boolean.class,
//                                    root.get(CommonModelEav_.ATTRIBUTE_SET_VALUE),
//                                    criteriaBuilder.literal(jsonFilterPath)
//                            ).in(Boolean.TRUE));
//                }
//            }
        }
        return specification;
    }
}
