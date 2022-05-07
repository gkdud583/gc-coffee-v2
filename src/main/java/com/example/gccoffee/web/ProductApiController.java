package com.example.gccoffee.web;

import com.example.gccoffee.Category;
import com.example.gccoffee.service.product.ProductService;
import com.example.gccoffee.web.dto.ProductSaveRequest;
import com.example.gccoffee.web.dto.ProductResponse;
import com.example.gccoffee.web.dto.ProductUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/products")
@RestController
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam("name") @Nullable String name, @RequestParam("category") @Nullable Category category) {
        if (name != null) {
            return ResponseEntity.ok(
                    productService.findByName(name).stream()
                            .map((product) -> ProductResponse.from(product))
                            .collect(Collectors.toList()));
        }
        if (category != null) {
            return ResponseEntity.ok(
                    productService.findByCategory(category).stream()
                            .map((product) -> ProductResponse.from(product))
                            .collect(Collectors.toList()));

        }
        return ResponseEntity.ok(
                productService.findAll().stream()
                .map((product) -> ProductResponse.from(product))
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProductSaveRequest productRequest) {
        return ResponseEntity.ok(
                ProductResponse.from(productService.save(productRequest.getCategory(), productRequest.getProductName(), productRequest.getPrice(), productRequest.getStockQuantity(), productRequest.getDescription())));

    }

    @DeleteMapping
    public void deleteAll() {
        productService.deleteAll();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(
                ProductResponse.from(productService.update(productUpdateRequest.getProductId(), productUpdateRequest.getCategory(), productUpdateRequest.getProductName(), productUpdateRequest.getPrice(), productUpdateRequest.getStockQuantity(), productUpdateRequest.getDescription())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ProductResponse.from(productService.findById(id)));
    }
}
