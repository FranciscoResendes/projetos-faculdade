package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.UtilizadorEmpresarial;

public interface UtilizadorEmpresarialRepository
    extends JpaRepository<UtilizadorEmpresarial, Long> {

  // @Query("SELECT a FROM UtilizadorEmpresarial a WHERE a.nome LIKE %:q% OR a.apelido LIKE %:q% ")
  List<UtilizadorEmpresarial> findAllByNome(String nome);

  UtilizadorEmpresarial findById(long id);

  UtilizadorEmpresarial findByNome(String nome);

  UtilizadorEmpresarial findByNumero(int num);
}
