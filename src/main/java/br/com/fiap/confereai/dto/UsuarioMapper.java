package br.com.fiap.confereai.dto;

import java.time.LocalDate;
import java.time.Period;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.confereai.model.Usuario;

@Component
public class UsuarioMapper {
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Usuario toModel(UsuarioRequest dto) {
        return modelMapper.map(dto, Usuario.class);
    }

    /** Copia os dados do request para uma entidade já existente (PUT). */
    public void updateModel(UsuarioRequest dto, Usuario entity) {
        modelMapper.map(dto, entity);
    }

    public UsuarioResponse toDto(Usuario entity) {
        UsuarioResponse dto = modelMapper.map(entity, UsuarioResponse.class);
        if (entity.getDataNascimento() != null) {
            dto.setIdade(Period.between(entity.getDataNascimento(), LocalDate.now()).getYears());
        }
        return dto;
    }
}
