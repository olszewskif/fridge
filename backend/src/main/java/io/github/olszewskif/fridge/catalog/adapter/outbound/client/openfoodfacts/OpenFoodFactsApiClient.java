package io.github.olszewskif.fridge.catalog.adapter.outbound.client.openfoodfacts;

import io.github.olszewskif.fridge.catalog.adapter.outbound.client.openfoodfacts.dto.ExternalProductData;
import io.github.olszewskif.fridge.catalog.application.dto.ExternalProduct;
import io.github.olszewskif.fridge.catalog.application.port.OpenFoodFactsApi;
import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import io.github.olszewskif.fridge.catalog.domain.Unit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
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
        String url = openFoodFactsUrl + "/product/" + barcode.value();
        ExternalProductData data;
        try {
            data = restTemplate.getForObject(url, ExternalProductData.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.info("Product not found in OpenFoodFacts for barcode: {}", barcode.value());
            return Optional.empty();
        } catch (RestClientException ex) {
            log.warn("Error calling OpenFoodFacts API for barcode {}: {}", barcode.value(), ex.getMessage());
            return Optional.empty();
        }

        if (data == null || data.status() == null || !data.status().startsWith("success")) {
            return Optional.empty();
        }

        Set<String> categories = cleanCategories(data.product().categories());

        return Optional.of(new ExternalProduct(
                data.product().name() == null ? "Unknown" : data.product().name(),
                data.product().brand(),
                barcode,
                toNetQuantity(data.product().amount(), data.product().unit()),
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

    private static NetQuantity toNetQuantity(Double amount, String unit) {
        if (amount == null || amount <= 0 || unit == null || unit.isBlank()) {
            return null;
        }
        return Unit.find(unit).map(u -> new NetQuantity(amount, u))
                .orElse(null);
    }
}
