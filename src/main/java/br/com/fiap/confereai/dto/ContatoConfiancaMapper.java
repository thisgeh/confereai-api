package br.com.fiap.confereai.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.confereai.model.ContatoConfianca;

@Component
public class ContatoConfiancaMapper {
    private final ModelMapper modelMapper;

    public ContatoConfiancaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ContatoConfianca toModel(ContatoConfiancaRequest dto) {
        return modelMapper.map(dto, ContatoConfianca.class);
    }

    public void updateModel(ContatoConfiancaRequest dto, ContatoConfianca entity) {
        modelMapper.map(dto, entity);
    }

    public ContatoConfiancaResponse toDto(ContatoConfianca entity) {
        ContatoConfiancaResponse dto = modelMapper.map(entity, ContatoConfiancaResponse.class);
        dto.setUsuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null);
        dto.setLinkLigacao(linkLigacao(entity.getTelefone()));
        return dto;
    }


    private String linkLigacao(String telefone) {
        if (telefone == null) {
            return null;
        }
        String digitos = telefone.replaceAll("\\D", "");
        return digitos.startsWith("55") && digitos.length() > 11 ? "tel:+" + digitos : "tel:+55" + digitos;
    }
}
