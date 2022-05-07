package com.example.gccoffee.web.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequest {
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력입니다.")
    private final String email;
    @NotBlank(message = "주소는 필수 입력입니다.")
    private final String address;
    @NotBlank(message = "우편번호는 필수 입력입니다.")
    private final String postcode;
    @NotEmpty(message = "주문 상품은 필수 입력입니다.")
    @Valid
    private final List<OrderItemRequest> orderItems;

    public OrderRequest(String email, String address, String postcode, List<OrderItemRequest> orderItems) {
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}
