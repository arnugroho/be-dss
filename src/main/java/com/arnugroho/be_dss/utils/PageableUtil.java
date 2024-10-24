package com.arnugroho.be_dss.utils;

import com.arnugroho.be_dss.configuration.CommonException;
import com.arnugroho.be_dss.model.common.PageableRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
   public static Pageable createPageableRequest(PageableRequest request) {
       if (request.getSort()== null){
           request.setSort("id");
           request.setIsSortAsc(Boolean.FALSE);
       }
       Sort sort = createSorting(request.getSort(), request.getIsSortAsc());
       if(request.getCurrent() < 1) {
           throw new CommonException("Page tidak boleh kurang dari 1");
       }
       return PageRequest.of(request.getCurrent() - 1, request.getPageSize(), sort);
   }

   private static Sort createSorting(String sortBy, Boolean isSortAsc) {
       Sort sort;
       if(StringUtils.isNotBlank(sortBy)) {
           sort = Sort.by(sortBy);
       } else {
           sort = Sort.unsorted();
       }
       if(isSortAsc) {
           sort = sort.ascending();
       } else {
           sort = sort.descending();
       }
       return sort;
   }
}
