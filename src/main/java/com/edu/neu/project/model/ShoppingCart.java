package com.edu.neu.project.model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Integer, Integer> map;

    public ShoppingCart(Map<Integer, Integer> map) {
        this.map = map;
    }

    public ShoppingCart() {
        this.map = new HashMap<>();
    }

    public void empty() {
        this.map.clear();
    }

    public Integer getItemQuantityByID(int id) {
        if (map.get(id) == null) return 0;
        return map.get(id);
    }

    public boolean containsID(int id) {
        return map.containsKey(id);
    }

    public void add(int id) {
        if (map.containsKey(id)) map.compute(id, (k, v) -> v + 1);
        else map.put(id, 1);
    }

    public void add(int id, int Quantity) {
        if (map.containsKey(id)) map.compute(id, (k, v) -> v += Quantity);
        else map.put(id, Quantity);
    }

    public void setQuantityByID(int id, int Quantity) {
        if (map.containsKey(id)) {
            map.replace(id, Math.max(Quantity, 0));
        } else {
            map.put(id, Math.max(Quantity, 0));
        }
        if (Quantity <= 0)
            map.remove(id);
    }

    public void remove(int id) {
        map.compute(id, (k, v) -> v - 1);
        if (map.get(id) <= 0) map.remove(id);
    }

    public void remove(int id, int Quantity) {
        map.compute(id, (k, v) -> v -= Quantity);
        if (map.get(id) <= 0) map.remove(id);
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }


}

