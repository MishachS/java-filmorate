package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MainModel;

import java.util.*;

public class InMemoryMainStorage<T extends MainModel> implements Storage<T> {
    private final Map<Long, T> storage = new HashMap<>();
    private long generationId;

    @Override
    public T create(T data) {
        data.setId(++generationId);
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new NotFoundException(String.format("Ошибка обновления! %s не существует!", data));
        }
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public List<T> getAll() {
        ArrayList<T> list = new ArrayList<>();
        if (storage.isEmpty()) {
            return Collections.emptyList();
        }
        for (T data : storage.values()) {
            list.add(data);
        }
        return list;
    }

    @Override
    public T getId(Long id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        }
        throw new NotFoundException("Объекта с таким Id не существует!");
    }

    @Override
    public boolean delete(T data) {
        if (storage.containsKey(data.getId())) {
            storage.remove(data.getId());
            return true;
        }
        throw new NotFoundException(String.format("Ошибка удаления! %s не существует!", data));
    }
}
