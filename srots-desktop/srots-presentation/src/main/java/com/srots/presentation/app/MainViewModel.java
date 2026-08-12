package com.srots.presentation.app;

import com.srots.application.usecase.product.GetProductsUseCase;
import com.srots.application.dto.ProductDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {

    private final GetProductsUseCase getProductsUseCase;
    private final ObjectProperty<MainUiState> state = new SimpleObjectProperty<>(MainUiState.INITIALIZING);
    private final StringProperty windowTitle = new SimpleStringProperty("SROTS");
    private final StringProperty systemStatus = new SimpleStringProperty();
    private final ObservableList<ProductDTO> products = FXCollections.observableArrayList();

    public MainViewModel(GetProductsUseCase getProductsUseCase) {
        this.getProductsUseCase = getProductsUseCase;
        this.systemStatus.set("Design system ready");
        loadData();
    }

    public void loadData() {
        state.set(MainUiState.INITIALIZING);
        try {
            if (getProductsUseCase != null) {
                products.setAll(getProductsUseCase.execute());
            }
            state.set(MainUiState.READY);
        } catch (Exception e) {
            state.set(MainUiState.ERROR);
        }
    }

    public ObjectProperty<MainUiState> stateProperty() { return state; }
    public StringProperty windowTitleProperty() { return windowTitle; }
    public StringProperty systemStatusProperty() { return systemStatus; }
    public ObservableList<ProductDTO> getProducts() { return products; }
}
