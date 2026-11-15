package br.com.fiap.confereai.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.confereai.dto.ContatoConfiancaRequest;
import br.com.fiap.confereai.dto.ContatoConfiancaResponse;
import br.com.fiap.confereai.dto.VerificacaoRequest;
import br.com.fiap.confereai.dto.VerificacaoResponse;
import br.com.fiap.confereai.model.ContatoConfianca;
import br.com.fiap.confereai.model.Verificacao;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(false);

        mapper.emptyTypeMap(ContatoConfiancaRequest.class, ContatoConfianca.class)
                .addMappings(m -> {
                    m.map(ContatoConfiancaRequest::getNome, ContatoConfianca::setNome);
                    m.map(ContatoConfiancaRequest::getTelefone, ContatoConfianca::setTelefone);
                    m.map(ContatoConfiancaRequest::getParentesco, ContatoConfianca::setParentesco);
                    m.map(ContatoConfiancaRequest::getPrincipal, ContatoConfianca::setPrincipal);
                });

        mapper.emptyTypeMap(VerificacaoRequest.class, Verificacao.class)
                .addMappings(m -> {
                    m.map(VerificacaoRequest::getTipoPagamento, Verificacao::setTipoPagamento);
                    m.map(VerificacaoRequest::getCnpj, Verificacao::setCnpj);
                    m.map(VerificacaoRequest::getNomeInformado, Verificacao::setNomeInformado);
                    m.map(VerificacaoRequest::getCodigoBanco, Verificacao::setCodigoBanco);
                    m.map(VerificacaoRequest::getValor, Verificacao::setValor);
                });

        // Entity -> Response (implícito, ignorando campos montados manualmente)
        mapper.emptyTypeMap(ContatoConfianca.class, ContatoConfiancaResponse.class)
                .addMappings(m -> {
                    m.skip(ContatoConfiancaResponse::setUsuarioId);
                    m.skip(ContatoConfiancaResponse::setLinkLigacao);
                })
                .implicitMappings();

        mapper.emptyTypeMap(Verificacao.class, VerificacaoResponse.class)
                .addMappings(m -> {
                    m.skip(VerificacaoResponse::setUsuarioId);
                    m.skip(VerificacaoResponse::setAlertas);
                    m.skip(VerificacaoResponse::setRespostasSim);
                    m.skip(VerificacaoResponse::setContatoConfianca);
                    m.skip(VerificacaoResponse::setMensagem);
                })
                .implicitMappings();
        return mapper;
    }
}
