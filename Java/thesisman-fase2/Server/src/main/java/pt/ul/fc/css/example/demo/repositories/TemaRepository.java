package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

  List<Tema> findByAnoLetivo(int anoLetivo);

  List<Tema> findByRemuneracao(int remuneracao);

  // List<Tema> findByMestradosCompativeis(String mestrado);

  // List<Tema> findByAutorId(Integer autorId);

  List<Tema> findByTitulo(String titulo);

  List<Tema> findByDescricao(String descricao);
}
