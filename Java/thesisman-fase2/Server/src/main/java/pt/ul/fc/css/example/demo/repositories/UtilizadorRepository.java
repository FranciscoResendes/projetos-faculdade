package pt.ul.fc.css.example.demo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

  Optional<Utilizador> findById(Integer id);

  Optional<Utilizador> findByNome(String username);
}
