package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.repositories.AuthorRepository;

@SpringBootTest
class DemoApplicationTests {

  @Autowired private AuthorRepository repository;

  @Test
  void testAuthorListIsNotEmpty() {
    assertTrue(repository.count() > 0);
  }
}
