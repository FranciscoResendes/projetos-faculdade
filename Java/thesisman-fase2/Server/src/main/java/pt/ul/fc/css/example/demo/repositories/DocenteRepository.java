package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Long> {

  // @Query("SELECT a FROM Docente a WHERE a.nome LIKE %:q% OR a.apelido LIKE %:q% ")
  List<Docente> findAllByNome(String nome);

  Docente findByNome(String nome);

  Docente findByNumero(int num);

  Docente findById(long id);
}
