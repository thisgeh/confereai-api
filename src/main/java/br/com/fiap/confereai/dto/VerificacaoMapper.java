package br.com.fiap.confereai.dto;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.confereai.model.PerguntaGolpe;
import br.com.fiap.confereai.model.Verificacao;

@Component
public class VerificacaoMapper {
    public static final String SEPARADOR_ALERTAS = "\n";

    private final ModelMapper modelMapper;

    public VerificacaoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Verificacao toModel(VerificacaoRequest dto) {
        return modelMapper.map(dto, Verificacao.class);
    }

    public void updateModel(VerificacaoRequest dto, Verificacao entity) {
        modelMapper.map(dto, entity);
    }

    public VerificacaoResponse toDto(Verificacao entity) {
        VerificacaoResponse dto = modelMapper.map(entity, VerificacaoResponse.class);
        dto.setUsuarioId(entity.getUsuario().getId());
        if (entity.getAlertas() != null && !entity.getAlertas().isBlank()) {
            dto.setAlertas(Arrays.asList(entity.getAlertas().split(SEPARADOR_ALERTAS)));
        }
        dto.setRespostasSim(entity.getSinaisMarcados().stream().map(PerguntaGolpe::getId).toList());
        dto.setMensagem(switch (entity.getResultado()) {
            case VERDE -> "🟢 Não encontramos sinais fortes de golpe. Mesmo assim, se tiver dúvida, ligue para sua pessoa de confiança.";
            case AMARELO -> "🟡 Atenção! Encontramos sinais suspeitos. Não pague agora: ligue para sua pessoa de confiança antes.";
            case VERMELHO -> "🔴 Muito perigoso! Tudo indica que é golpe. NÃO pague e ligue para sua pessoa de confiança.";
        });
        return dto;
    }
}
