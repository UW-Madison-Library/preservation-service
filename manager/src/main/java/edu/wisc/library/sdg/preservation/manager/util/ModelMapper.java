package edu.wisc.library.sdg.preservation.manager.util;

import com.github.dozermapper.core.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dozer convenience wrapper
 */
public class ModelMapper {

    private Mapper mapper;

    public ModelMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public <T> T map(Object source, Class<T> mappedType) {
        if (source == null) {
            return null;
        }

        if (isEnum(source.getClass()) && isEnum(mappedType)) {
            return (T) Enum.valueOf((Class<Enum>) mappedType, ((Enum) source).name());
        }

        return mapper.map(source, mappedType);
    }

    public <T> List<T> mapList(List<?> source, Class<T> mappedType) {
        if (source == null) {
            return null;
        }

        return source.stream().map(o -> map(o, mappedType)).collect(Collectors.toList());
    }

    private boolean isEnum(Class<?> clazz) {
        var declaringClass = clazz.getDeclaringClass();
        return clazz.isEnum() || (declaringClass != null && declaringClass.isEnum());
    }



}
