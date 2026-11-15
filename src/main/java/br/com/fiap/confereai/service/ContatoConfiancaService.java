package br.com.fiap.confereai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.confereai.dto.ContatoConfiancaMapper;
import br.com.fiap.confereai.dto.ContatoConfiancaRequest;
import br.com.fiap.confereai.dto.ContatoConfiancaResponse;
import br.com.fiap.confereai.exception.ResourceNotFoundException;
import br.com.fiap.confereai.model.ContatoConfianca;
import br.com.fiap.confereai.model.Usuario;
import br.com.fiap.confereai.repository.ContatoConfiancaRepository;

@Service
public class ContatoConfiancaService {

    private final ContatoConfiancaRepository repository;
    private final ContatoConfiancaMapper mapper;
    private final UsuarioService usuarioService;

    public ContatoConfiancaService(ContatoConfiancaRepository repository, ContatoConfiancaMapper mapper,
            UsuarioService usuarioService) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public ContatoConfiancaResponse create(ContatoConfiancaRequest request) {
        Usuario usuario = usuarioService.buscarEntidade(request.getUsuarioId());
        ContatoConfianca contato = mapper.toModel(request);
        contato.setUsuario(usuario);
    
        boolean primeiro = repository.countByUsuarioId(usuario.getId()) == 0;
        contato.setPrincipal(primeiro || Boolean.TRUE.equals(request.getPrincipal()));
        ContatoConfianca salvo = repository.save(contato);
        garantirUnicoPrincipal(salvo);
        return mapper.toDto(salvo);
    }

    @Transactional(readOnly = true)
    public List<ContatoConfiancaResponse> findAll(Long usuarioId) {
        List<ContatoConfianca> contatos = usuarioId == null
                ? repository.findAll()
                : repository.findByUsuarioIdOrderByPrincipalDescNomeAsc(usuarioService.buscarEntidade(usuarioId).getId());
        return contatos.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ContatoConfiancaResponse findById(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public ContatoConfiancaResponse update(Long id, ContatoConfiancaRequest request) {
        ContatoConfianca contato = buscarEntidade(id);
        Usuario usuario = usuarioService.buscarEntidade(request.getUsuarioId());
        mapper.updateModel(request, contato);
        contato.setUsuario(usuario);
        contato.setPrincipal(Boolean.TRUE.equals(request.getPrincipal()));
        ContatoConfianca salvo = repository.save(contato);
        garantirUnicoPrincipal(salvo);
        return mapper.toDto(salvo);
    }

    @Transactional
    public void deleteById(Long id) {
        ContatoConfianca contato = buscarEntidade(id);
        Long usuarioId = contato.getUsuario().getId();
        boolean eraPrincipal = Boolean.TRUE.equals(contato.getPrincipal());
        contato.getUsuario().getContatos().remove(contato);
        repository.delete(contato);
        repository.flush();
      
        if (eraPrincipal) {
            repository.findFirstByUsuarioIdOrderByPrincipalDescIdAsc(usuarioId).ifPresent(c -> {
                c.setPrincipal(true);
                repository.save(c);
            });
        }
    }

    @Transactional(readOnly = true)
    public Optional<ContatoConfiancaResponse> buscarPrincipal(Long usuarioId) {
        return repository.findFirstByUsuarioIdOrderByPrincipalDescIdAsc(usuarioId).map(mapper::toDto);
    }

    private ContatoConfianca buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato de confiança", id));
    }


    private void garantirUnicoPrincipal(ContatoConfianca salvo) {
        if (!Boolean.TRUE.equals(salvo.getPrincipal())) {
            return;
        }
        repository.findByUsuarioIdOrderByPrincipalDescNomeAsc(salvo.getUsuario().getId()).stream()
                .filter(c -> !c.getId().equals(salvo.getId()) && Boolean.TRUE.equals(c.getPrincipal()))
                .forEach(c -> {
                    c.setPrincipal(false);
                    repository.save(c);
                });
    }
}
