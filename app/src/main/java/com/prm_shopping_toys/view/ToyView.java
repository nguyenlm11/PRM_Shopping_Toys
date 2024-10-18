package com.prm_shopping_toys.view;

import com.prm_shopping_toys.model.Toy;

import java.util.List;

public interface ToyView {
    void onToysLoaded(List<Toy> toys);

    void onAddProductSuccess(String response);

    void onError(String message);

    void onToyDeleted(String response);

    void onToyUpdated(String response);
}
