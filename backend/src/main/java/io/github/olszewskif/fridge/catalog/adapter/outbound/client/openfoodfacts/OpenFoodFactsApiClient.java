package io.github.olszewskif.fridge.catalog.adapter.outbound.client.openfoodfacts;

import io.github.olszewskif.fridge.catalog.adapter.outbound.client.openfoodfacts.dto.ExternalProductData;
import io.github.olszewskif.fridge.catalog.application.dto.ExternalProduct;
import io.github.olszewskif.fridge.catalog.application.port.OpenFoodFactsApi;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenFoodFactsApiClient implements OpenFoodFactsApi {

    @Value("${app.external.open-food-facts.url}")
    private String openFoodFactsUrl;
    private final RestTemplate restTemplate;

    @Override
    public Optional<ExternalProduct> findByBarcode(Barcode barcode) {
        ExternalProductData data = restTemplate.getForObject(openFoodFactsUrl + "/product/" + barcode.value(),
                ExternalProductData.class);
        log.info("Response: {}", data);

        if (data == null || data.status() == null || !data.status().startsWith("success")) {
            return Optional.empty();
        }

        Set<String> categories = cleanCategories(data.product().categories());

        return Optional.of(new ExternalProduct(
                data.product().name() == null ? "Unknown" : data.product().name(),
                data.product().brand(),
                barcode,
                data.product().imageUrl(),
                categories
        ));
    }

    private static Set<String> cleanCategories(Set<String> categories) {
        if (categories == null) return new HashSet<>();
        return categories.stream().map(c -> c
                .replaceFirst("^[a-z]{2}:", "")
                .replace("-", " ")
                .replace("_", " ")
                .trim())
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }
}
