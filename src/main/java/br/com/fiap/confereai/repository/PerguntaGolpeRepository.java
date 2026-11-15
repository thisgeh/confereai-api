package br.com.fiap.confereai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.confereai.model.PerguntaGolpe;

@Repository
public interface PerguntaGolpeRepository extends JpaRepository<PerguntaGolpe, Long> {

    List<PerguntaGolpe> findByAtivaTrueOrderByIdAsc();
}
