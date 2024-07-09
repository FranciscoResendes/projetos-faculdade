package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

  // @Query("SELECT a FROM Aluno a WHERE a.nome LIKE %:q% OR a.apelido LIKE %:q% ")
  List<Aluno> findByNome(String nome);

  Aluno findByNumero(int num);

  List<Aluno> findByFinalizadaCandidaturaAndTemaAtribuidoIsNull(boolean finalizadaCandidatura);

  Optional<Aluno> findById(Long id);
}
