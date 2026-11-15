package br.com.fiap.confereai.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.confereai.dto.UsuarioMapper;
import br.com.fiap.confereai.dto.UsuarioRequest;
import br.com.fiap.confereai.dto.UsuarioResponse;
import br.com.fiap.confereai.exception.BusinessException;
import br.com.fiap.confereai.exception.ResourceNotFoundException;
import br.com.fiap.confereai.model.Usuario;
import br.com.fiap.confereai.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        if (repository.existsByEmailIgnoreCase(request.getEmail())) {
            throw BusinessException.conflict("Já existe um usuário com o e-mail informado.");
        }
        Usuario usuario = mapper.toModel(request);
        return mapper.toDto(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = buscarEntidade(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw BusinessException.conflict("Já existe outro usuário com o e-mail informado.");
        }
        mapper.updateModel(request, usuario);
        return mapper.toDto(repository.save(usuario));
    }

    
    @Transactional
    public void deleteById(Long id) {
        repository.delete(buscarEntidade(id));
    }


    @Transactional(readOnly = true)
    public Usuario buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }
}
