package com.rogers.ute.hhHistoryService.exception;

import com.rogers.ute.commons.exceptions.ErrorCode;
import com.rogers.ute.commons.exceptions.ErrorCodeRange;

public enum HHHistoryServiceErrorCodes implements ErrorCode {
    //TODO: Proper Error scenarios to be listed
    GENERAL_ERROR(0, "Unclassified error"),
    FETCHING_FROM_CACHE_FAILED(1, "Fetching from cache failed"),
    CONVERTING_FROM_CASSANDRA_RESULT_FAILED(2, "Converting from cassandra result failed"),
    MISSING_DATA_IN_BACKEND(3, "Missing data in the backend"),
    REQUEST_TIMED_OUT(4, "Request timed out"),
    POPULATING_CACHE_FAILED(5, "Failure during cache population"),
    ACCOUNT_ASSOCIATION_FETCHING_FAILURE(6, "Account association fetching failure"),
    BACKEND_TIMEOUT(7, "Backend timeout"),
    FETCHING_FROM_CACHE_TIMED_OUT(8, "Timeout during fetching from cache"),
    POPULATING_CACHE_TIMED_OUT(9, "Timeout during cache population");

    private final int code;
    private final String description;

    private final ErrorCodeRange ERROR_CODE_RANGE = ErrorCodeRange.BILLING_ACCOUNT_SERVICE; //TODO: take Proper ErrorCodeRange for HHProfileService from com.rogers.ute.commons.exceptions.ErrorCodeRange

    HHHistoryServiceErrorCodes(int localCode, String description) {
        this.code = ERROR_CODE_RANGE.getCode(localCode);
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public ErrorCodeRange getErrorCodeRange() {
        return ERROR_CODE_RANGE;
    }

}
