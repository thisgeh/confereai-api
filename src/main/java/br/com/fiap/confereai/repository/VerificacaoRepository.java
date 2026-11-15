package br.com.fiap.confereai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.confereai.model.Verificacao;

@Repository
public interface VerificacaoRepository extends JpaRepository<Verificacao, Long> {

    List<Verificacao> findByUsuarioIdOrderByDataVerificacaoDesc(Long usuarioId);

    boolean existsBySinaisMarcadosId(Long perguntaId);
}
