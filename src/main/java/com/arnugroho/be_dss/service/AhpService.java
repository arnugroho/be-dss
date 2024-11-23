package com.arnugroho.be_dss.service;

import java.util.Map;

public interface AhpService {
    Map<String, Object> calculateAhpForLevel(Long parentId);
}
