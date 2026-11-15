package br.com.fiap.confereai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.confereai.model.ContatoConfianca;

@Repository
public interface ContatoConfiancaRepository extends JpaRepository<ContatoConfianca, Long> {

    List<ContatoConfianca> findByUsuarioIdOrderByPrincipalDescNomeAsc(Long usuarioId);

    Optional<ContatoConfianca> findFirstByUsuarioIdOrderByPrincipalDescIdAsc(Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}
