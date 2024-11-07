package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.EventProvider;
import com.around.aroundcore.database.repositories.EventProviderRepository;
import com.around.aroundcore.web.exceptions.api.entity.EventProviderNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class EventProviderService {
    private final EventProviderRepository eventProviderRepository;
    public EventProvider findById(Integer id){
        return eventProviderRepository.findById(id).orElseThrow(EventProviderNullException::new);
    }
}
