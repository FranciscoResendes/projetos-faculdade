package pt.ul.fc.css.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Author;
import pt.ul.fc.css.example.demo.entities.Docente;
import pt.ul.fc.css.example.demo.repositories.AlunoRepository;
import pt.ul.fc.css.example.demo.repositories.AuthorRepository;
import pt.ul.fc.css.example.demo.repositories.DocenteRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@SpringBootApplication
@ComponentScan({
  "pt.ul.fc.css.example.demo.repositories",
  "pt.ul.fc.css.example.demo.controller",
  "pt.ul.fc.css.example.demo.services",
  "pt.ul.fc.css.example.demo.handlers",
  "pt.ul.fc.css.example.demo.catalogs",
  "other.packages.to.scan"
})
public class DemoApplication {

  private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(
      AuthorRepository repository,
      AlunoRepository alunoRep,
      DocenteRepository docRep,
      TemaRepository temaRep) {
    return (args) -> {
      // save a few customers
      alunoRep.save(new Aluno("Diogo", 56969));
      alunoRep.save(new Aluno("Laura", 58111));
      alunoRep.save(new Aluno("Francisco", 57162));
      docRep.save(new Docente("Leonardo", 12345));
      docRep.save(new Docente("Ana", 23456));
      docRep.save(new Docente("Diana", 15678));

      repository.save(new Author("Jack", "Bauer"));
      repository.save(new Author("Chloe", "O'Brian"));
      repository.save(new Author("Kim", "Bauer"));
      repository.save(new Author("David", "Palmer"));
      repository.save(new Author("Michelle", "Dessler"));

      // fetch all customers
      log.info("Customers found with findAll():");
      log.info("-------------------------------");
      for (Author author : repository.findAll()) {
        log.info(author.toString());
      }
      log.info("");

      // fetch an individual customer by ID
      repository
          .findById(1L)
          .ifPresent(
              (Author author) -> {
                log.info("Customer found with findById(1L):");
                log.info("--------------------------------");
                log.info(author.toString());
                log.info("");
              });

      // fetch customers by last name
      log.info("Author found with findByName('Bauer'):");
      log.info("--------------------------------------------");
      repository
          .findByName("Bauer")
          .forEach(
              bauer -> {
                log.info(bauer.toString());
              });
      // for (Customer bauer : repository.findByLastName("Bauer")) {
      // log.info(bauer.toString());
      // }
      log.info("");
    };
  }
}
