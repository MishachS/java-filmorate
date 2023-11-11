package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.MainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MainController <T extends MainModel> {
    private final Map<Long, T> storage = new HashMap<>();
    private long generationId;

    public List<T> getMap() {
        return new ArrayList<T>(storage.values());
    }

    public T update(T data) {
        validate(data);
        if (!storage.containsKey(data.getId())) {
            throw new NotFoundException(String.format("Обновление невозможно %s не сущесвует", data));
        }
        storage.put(data.getId(), data);
        return data;

    }

    public T create(T data) {
        validate(data);
        data.setId(++generationId);
        storage.put(data.getId(), data);
        return data;
    }

    public abstract void validate(T data);
}