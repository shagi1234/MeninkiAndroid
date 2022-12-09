package com.example.playerslidding.utils;

import com.example.playerslidding.data.CategoryDto;
import com.example.playerslidding.data.GridDto;
import com.example.playerslidding.data.ShopDTO;
import com.example.playerslidding.data.StoreDTO;
import com.example.playerslidding.data.TabItemCustom;

import java.util.ArrayList;

public class StoreList {
    private static ArrayList<StoreDTO> storeDTOS;
    private static ArrayList<GridDto> gridDtos;

    public static ArrayList<StoreDTO> getStoreDTOS() {
        if (storeDTOS == null) {
            storeDTOS = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                storeDTOS.add(new StoreDTO("Trendyol", "https://static.glami.com.tr/img/576x840r/289100551.jpg", "https://play-lh.googleusercontent.com/LosPYfjaz1pOL-I3XCTroj4vQVxfsF5629nzPJM4pIj2KLaQuLbwmXUqV-I1RT5u9A"));
            }
        }
        return storeDTOS;
    }

    public static ArrayList<ShopDTO> getShops() {
        ArrayList<ShopDTO> shops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shops.add(new ShopDTO("https://play-lh.googleusercontent.com/LosPYfjaz1pOL-I3XCTroj4vQVxfsF5629nzPJM4pIj2KLaQuLbwmXUqV-I1RT5u9A", "Вино “У Газиза”", "Лучшее вино, лук шоб занюхать", getGrids()));
        }
        return shops;
    }

    public static ArrayList<GridDto> getGrids() {
        if (gridDtos == null) {
            gridDtos = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                if (i % 2 == 0) {
                    gridDtos.add(new GridDto("0", "https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "Colins jeans ", "13333", "16999", "13", 146));
                } else {
                    gridDtos.add(new GridDto("1", "https://cdn.dsmcdn.com/mnresize/500/-/ty384/product/media/images/20220405/17/83663989/437492006/1/1_org.jpg", "Colins jeans ", "13333", "16999", "13", 146));
                }
            }
        }
        return gridDtos;
    }

    public static ArrayList<TabItemCustom> getTabs() {
        ArrayList<TabItemCustom> tabs = new ArrayList<>();
        tabs.add(new TabItemCustom("Смотреть все", true));
        tabs.add(new TabItemCustom("Медиа", false));
        tabs.add(new TabItemCustom("Реклама", false));
        tabs.add(new TabItemCustom("Товары", false));
        return tabs;
    }

    public static ArrayList<CategoryDto> getCategories() {
        ArrayList<CategoryDto> tabs = new ArrayList<>();
        tabs.add(new CategoryDto("Новинки", false));
        tabs.add(new CategoryDto("все", false));
        tabs.add(new CategoryDto("Мужская", false));
        tabs.add(new CategoryDto("Женская", false));
        tabs.add(new CategoryDto("Детская", false));
        tabs.add(new CategoryDto("Унисекс", false));
        tabs.add(new CategoryDto("Скидки от mahmood store", true));
        tabs.add(new CategoryDto("спецодежда (униформа)", false));
        tabs.add(new CategoryDto("Уход за одеждой", false));
        return tabs;
    }
}
