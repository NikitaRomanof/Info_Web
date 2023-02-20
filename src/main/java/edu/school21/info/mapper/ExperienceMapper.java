package edu.school21.info.mapper;

import edu.school21.info.model.dto.ExperienceDto;
import edu.school21.info.model.entity.*;
import edu.school21.info.model.entity.Experience;
import edu.school21.info.service.CheckService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ExperienceMapper extends GenericMapper<Experience, ExperienceDto> {

    private final CheckService checkService;

    public ExperienceMapper(CheckService checkService) {
        super(Experience.class, ExperienceDto.class);
        this.checkService = checkService;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Experience.class, ExperienceDto.class).setPostConverter(toDtoConverter());
        mapper.createTypeMap(ExperienceDto.class, Experience.class).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(ExperienceDto source, Experience destination) throws EntityNotFoundException {
        Long id = source.getCheckId();
        Check check = checkService.getByIdOrElseThrow(id);
        destination.setCheck(check);
    }

    @Override
    void mapSpecificFields(Experience source, ExperienceDto destination) {
        destination.setCheckId(source.getCheck().getId());
    }


}
