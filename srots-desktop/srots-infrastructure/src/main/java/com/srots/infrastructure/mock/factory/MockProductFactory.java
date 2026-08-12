package com.srots.infrastructure.mock.factory;

import com.srots.domain.model.Product;
import com.srots.domain.model.enums.ProductStatus;
import com.srots.domain.valueobject.ProductId;
import com.srots.domain.valueobject.VersionNumber;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

public final class MockProductFactory {

    private MockProductFactory() {
    }

    public static Product srots() {
        return new Product(
                new ProductId(MockDataSeeder.PRODUCT_SROTS),
                "SROTS",
                "SROTS",
                "Enterprise control plane for company operations and product lifecycle.",
                new VersionNumber("0.2.0"),
                new VersionNumber("0.3.0"),
                "Aisha Rahman",
                "Enterprise Platform",
                ProductStatus.ACTIVE,
                "TEAM-PLATFORM",
                "https://git.srots.example/srots",
                MockConfiguration.MOCK_REFERENCE_DATE.minusYears(1));
    }

    public static Product compty() {
        return new Product(
                new ProductId(MockDataSeeder.PRODUCT_COMPTY),
                "COMPTY",
                "COMPTY",
                "ATE Intelligence Platform managed by SROTS.",
                new VersionNumber("1.9.0"),
                new VersionNumber("2.0.0"),
                "Elena Petrova",
                "ATE Intelligence Platform",
                ProductStatus.ACTIVE,
                "TEAM-BACKEND",
                "https://git.srots.example/compty",
                MockConfiguration.MOCK_REFERENCE_DATE.minusYears(3));
    }
}
