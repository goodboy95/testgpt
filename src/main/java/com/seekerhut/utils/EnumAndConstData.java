package com.seekerhut.utils;

public class EnumAndConstData {
    public enum HTTPMethod {
        get,
        post,
        put,
        delete
    }

    public enum EncodeType {
        formdata,
        json,
    }
    
    public enum CharRangeKey {
        number,
        uppercase,
        lowercase,
        symbol,
        wordchar,
        chinese,
    }

    public enum ErrorInfo {
        thirdpartyAPIError(-100);

        private final int value;
        private ErrorInfo(int errCode) {
            this.value = errCode;
        }
    }
}
