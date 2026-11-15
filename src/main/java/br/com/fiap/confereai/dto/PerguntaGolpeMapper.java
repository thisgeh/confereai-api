package br.com.fiap.confereai.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.confereai.model.PerguntaGolpe;

@Component
public class PerguntaGolpeMapper {
    private final ModelMapper modelMapper;

    public PerguntaGolpeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PerguntaGolpe toModel(PerguntaGolpeRequest dto) {
        return modelMapper.map(dto, PerguntaGolpe.class);
    }

    public void updateModel(PerguntaGolpeRequest dto, PerguntaGolpe entity) {
        modelMapper.map(dto, entity);
    }

    public PerguntaGolpeResponse toDto(PerguntaGolpe entity) {
        return modelMapper.map(entity, PerguntaGolpeResponse.class);
    }
}
