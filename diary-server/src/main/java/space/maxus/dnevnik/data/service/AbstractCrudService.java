package space.maxus.dnevnik.data.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<V, I, R extends CrudRepository<V, I>> {
    protected abstract @NotNull R getRepository();

    public @NotNull List<V> findAll() {
        return (List<V>) getRepository().findAll();
    }

    public Optional<V> findById(I id) {
        return getRepository().findById(id);
    }

    public void insertUpdate(V value) {
        getRepository().save(value);
    }

    public void delete(V value) {
        getRepository().delete(value);
    }

    public void deleteById(I id) {
        getRepository().deleteById(id);
    }
}
