package com.hg.bethunger;

import com.hg.bethunger.exception.ResourceNotFoundException;
import org.springframework.data.repository.CrudRepository;

public class Utils {
    public static <T> T findByIdOrThrow(CrudRepository<T, Long> repository, Long id, String resourceName) throws ResourceNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }

    public static <T> void existsByIdOrThrow(CrudRepository<T, Long> repository, Long id, String resourceName) throws ResourceNotFoundException {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resourceName, id);
        }
    }
}
