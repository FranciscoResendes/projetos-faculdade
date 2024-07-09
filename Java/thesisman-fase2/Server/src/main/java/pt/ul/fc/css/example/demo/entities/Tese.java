package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tese {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @OneToOne protected Tema tema;

  @ManyToOne protected Docente orientador;

  protected byte[] fileData;

  public Tema getTema() {
    return this.tema;
  }

  public Long getId() {
    return this.id;
  }

  public Docente getOrientador() {
    return this.orientador;
  }

  public byte[] getFileData() {
    return fileData;
  }

  public void setFileData(byte[] fileData) {
    this.fileData = fileData;
  }

  // Método para salvar o conteúdo do arquivo no banco de dados
  public void saveFileContent(File file) throws IOException {
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] data = new byte[(int) file.length()];
      fis.read(data);
      this.fileData = data;
    }
  }

  // Método para recuperar o conteúdo do arquivo do banco de dados e salvar em um arquivo no sistema
  // de arquivos
  public void writeFileContent(File file) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(this.fileData);
    }
  }
}
