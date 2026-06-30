package com.tubes.dbjualkue.converter;

import com.tubes.dbjualkue.entity.Kue;
import com.tubes.dbjualkue.repository.KueRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToKueConverter implements Converter<String, Kue> {
    
    private final KueRepository kueRepository;

    public StringToKueConverter(KueRepository kueRepository) {
        this.kueRepository = kueRepository;
    }
    
    @Override
    public Kue convert(String idKue) {
        return kueRepository.findById(idKue).orElse(null);
    }
}
