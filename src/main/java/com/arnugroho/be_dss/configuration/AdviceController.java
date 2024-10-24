package com.arnugroho.be_dss.configuration;

import com.arnugroho.be_dss.configuration.security.UserDetailsImpl;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdviceController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    public static final String ERROR_PREPARE_DATA = "Gagal Mempersiapkan Data";
    public static final String ERROR_DELETE_DATA = "Gagal Menghapus Data";
    public static final String ERROR_SAVE_DATA = "Gagal Menyimpan Data";
    public static final String SERVER_ERROR = "Server Tidak Dapat Memproses";
    public static final String ERROR_MAX_FILE_SIZE = "File yang Diupload Terlalu Besar";
    public static final String ERROR_DATA_NOT_FOUND = "Data tidak ditemukan";
    public static final String ERROR_ACCESS_DENIED = "Tidak dapat mengakses";

    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";


    private String getUsernameAccess() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (userDetails != null) {
            username = userDetails.getUsername();
        } else {
            username = "username";
        }
        return username;
    }

    private void writeErrorLog(WebRequest request, Exception e) {
        Map<String, Object> map = new HashMap<>();
        if (request != null) {
            map.put("username", getUsernameAccess());
            map.put("requestURL", ((ServletWebRequest) request).getRequest().getRequestURI());
        }
        map.put(SUCCESS, false);
        map.put(MESSAGE, e.getMessage());
        String msg = new Gson().toJson(map);
        log.error(msg, e);
    }

    private void writeWarnLog(WebRequest request, Exception e) {
        Map<String, Object> map = new HashMap<>();
        if (request != null) {
            map.put("username", getUsernameAccess());
            map.put("requestURL", ((ServletWebRequest) request).getRequest().getRequestURI());
        }
        map.put(SUCCESS, false);
        map.put(MESSAGE, e.getMessage());
        String msg = new Gson().toJson(map);
        log.warn(msg, e);
    }

    @ExceptionHandler({Exception.class})
    public @ResponseBody
    DefaultResponse<String> handleException(Exception ex, WebRequest request) {
        writeErrorLog(request, ex);
        return DefaultResponse.error(SERVER_ERROR);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class, FileSizeLimitExceededException.class})
    public @ResponseBody
    DefaultResponse<String> handleMultipartException(WebRequest request, MaxUploadSizeExceededException e) {
        writeErrorLog(request, e);
        return DefaultResponse.error(ERROR_MAX_FILE_SIZE);
    }

    @ExceptionHandler(CommonException.class)
    public @ResponseBody
    DefaultResponse<String> handleCommonException(WebRequest request, CommonException e) {
        writeWarnLog(request, e);
        return DefaultResponse.error(e.getMessage());
    }

//    @ExceptionHandler(BruteException.class)
//    public @ResponseBody
//    DefaultResponse handleBruteException(Authentication authentication, CommonException e) {
//        writeErrorLog(authentication, e);
//        return DefaultResponse.ok();
//    }

    @ExceptionHandler({AccessDeniedException.class})
    public @ResponseBody
    DefaultResponse<String> handleAccessDeniedException(WebRequest request,Exception e) {
        writeErrorLog(request, e);
        return DefaultResponse.error(ERROR_ACCESS_DENIED);
    }

//    @ExceptionHandler({ConnectTimeoutException.class})
//    public @ResponseBody DefaultResponse handleConnectTimeoutException(WebRequest req, Exception e) {
//        writeWarnLog(req, e);
//        Map<String, Object> res = new HashMap<>();
//        return DefaultResponse.error(ERROR_ACCESS_DENIED);
//    }
//
//    @ExceptionHandler({WebClientResponseException.class})
//    public @ResponseBody DefaultResponse handleWebClientResponseException(WebRequest req, Exception e) {
//        writeErrorLog(req, e);
//        Map<String, Object> res = new HashMap<>();
//        return DefaultResponse.error(ERROR_ACCESS_DENIED);
//    }

    @ExceptionHandler({EntityNotFoundException.class})
    public @ResponseBody DefaultResponse<String> handleEntityNotFoundException(WebRequest req, Exception e) {
        writeWarnLog(req, e);
        return DefaultResponse.noContent(e.getMessage());
    }

    @ExceptionHandler({AuthException.class})
    public @ResponseBody DefaultResponse<String> authException(WebRequest request, Exception e) {
        writeWarnLog(request, e);
        return DefaultResponse.unauthorized();
    }
}
