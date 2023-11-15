package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.model.MainModel;
import ru.yandex.practicum.filmorate.storage.InMemoryMainStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@AllArgsConstructor
public abstract class MainService<T extends MainModel> implements Storage<T> {

    private final InMemoryMainStorage<T> inMemoryMainStorage;

    @Override
    public T create(T data) {
        validate(data);
        return inMemoryMainStorage.create(data);
    }

    @Override
    public T update(T data) {
        validate(data);
        return inMemoryMainStorage.update(data);
    }

    @Override
    public List<T> getAll() {
        return inMemoryMainStorage.getAll();
    }

    @Override
    public T getId(Long id) {
        return inMemoryMainStorage.getId(id);
    }

    @Override
    public boolean delete(T data) {
        return inMemoryMainStorage.delete(data);
    }

    public abstract void validate(T data);
}
