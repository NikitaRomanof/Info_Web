package edu.school21.info.service;

import edu.school21.info.model.BaseModel;
import edu.school21.info.repository.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class GenericService<T extends BaseModel, V> {

    protected final GenericRepository<T, V> repository;

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> getOne(V id) {
        return repository.findById(id);
    }

    public T create(T object) {
        return repository.saveAndFlush(object);
    }

    public T update(T object) {
        return repository.saveAndFlush(object);
    }

    public void delete(V id) {
        repository.deleteById(id);
    }

    public List<T> saveAll(List<T> objects) {
        return repository.saveAllAndFlush(objects);
    }
}
