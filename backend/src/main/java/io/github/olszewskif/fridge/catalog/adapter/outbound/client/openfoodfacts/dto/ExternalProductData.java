package io.github.olszewskif.fridge.catalog.adapter.outbound.client.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record ExternalProductData(
        @JsonProperty("product") Product product,
        @JsonProperty("status") String status
) {
    public record Product(
            @JsonProperty("product_name") String name,
            @JsonProperty("brands") String brand,
            @JsonProperty("image_front_url") String imageUrl,
            @JsonProperty("categories_tags") Set<String> categories
    ) {}
}
