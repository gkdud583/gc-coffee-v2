package com.example.gccoffee.exception;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_STOCK_QUANTITY(BAD_REQUEST, "잘못된 상품 수량입니다."),
    INVALID_PRICE(BAD_REQUEST, "잘못된 가격입니다."),
    NOT_ENOUGH_STOCK_QUANTITY(BAD_REQUEST, "재고 수량보다 주문 상품 수량이 더 많습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    PRODUCT_NOT_FOUND(NOT_FOUND, "상품을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDetail() {
        return detail;
    }
}
