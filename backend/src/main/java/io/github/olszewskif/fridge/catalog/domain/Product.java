package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public final class Product extends AggregateRoot<ProductId> {

    private String name;
    private String brand;
    private Barcode barcode;
    private String imageUrl;
    private final Set<CategoryId> categories;

    private Product(ProductId id, String name, String brand, Barcode barcode,
                    String imageUrl, Set<CategoryId> categories) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
        this.brand = brand;
        this.barcode = Objects.requireNonNull(barcode, "Barcode cannot be null!");
        this.imageUrl = imageUrl;
        this.categories = categories == null ? new HashSet<>() : new HashSet<>(categories);
    }

    public static Product create(String name, String brand, Barcode barcode,
                                 String imageUrl, Set<CategoryId> categories) {
        return new Product(ProductId.generate(), name, brand, barcode, imageUrl, categories);
    }

    public static Product restore(ProductId id, String name, String brand, Barcode barcode,
                                  String imageUrl, Set<CategoryId> categories) {
        return new Product(id, name, brand, barcode, imageUrl, categories);
    }

    public Set<CategoryId> getCategories() {
        return new HashSet<>(categories);
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
    }

    public void updateBrand(String brand) {
        this.brand = brand;
    }

    public void updateBarcode(Barcode barcode) {
        this.barcode = Objects.requireNonNull(barcode, "Barcode cannot be null!");
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addCategory(CategoryId id) {
        Objects.requireNonNull(id, "Category id cannot be null!");
        categories.add(id);
    }

    public void removeCategory(CategoryId id) {
        categories.remove(id);
    }
}
