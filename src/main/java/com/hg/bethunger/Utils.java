package com.hg.bethunger;

import com.hg.bethunger.exception.ResourceNotFoundException;
import org.springframework.data.repository.CrudRepository;

public class Utils {
    public static <T> T findOrThrow(CrudRepository<T, Long> repository, Long id, String resourceName) throws ResourceNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }

    public static <T> void existsOrThrow(CrudRepository<T, Long> repository, Long id, String resourceName) throws ResourceNotFoundException {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(resourceName, id);
        }
    }
}
