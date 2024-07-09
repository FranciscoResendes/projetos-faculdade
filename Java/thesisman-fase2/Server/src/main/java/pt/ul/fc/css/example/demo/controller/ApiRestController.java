package pt.ul.fc.css.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pt.ul.fc.css.example.demo.dtos.AlunoDTO;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Tese;
import pt.ul.fc.css.example.demo.services.CadidaturaAlunoTemasService;
import pt.ul.fc.css.example.demo.services.TeseService;

@RestController()
@RequestMapping("/api")
public class ApiRestController {

  @Autowired private CadidaturaAlunoTemasService cadidaturaAlunoTemasService;
  @Autowired private TeseService teseService;

  @GetMapping("/aluno/{id}")
  public Optional<AlunoDTO> getAlunos(@PathVariable Long id) {
    return cadidaturaAlunoTemasService.getAlunoById(id);
  }

  @PatchMapping("/aluno/{id}/finalizadaCandidatura")
  public boolean updateAlunoFinalizadaCandidatura(
      @PathVariable Long id, @RequestBody Map<String, Boolean> body) {
    Boolean finalizadaCandidatura = body.get("finalizadaCandidatura");
    if (finalizadaCandidatura == null) {
      throw new IllegalArgumentException("finalizadaCandidatura is required");
    }
    return cadidaturaAlunoTemasService.updateAlunoFinalizadaCandidatura(id, finalizadaCandidatura);
  }

  @GetMapping("/aluno/{id}/temas")
  public List<TemaDTO> getTemasByAluno(@PathVariable Long id) {
    return cadidaturaAlunoTemasService.getTemasByAluno(id);
  }

  @GetMapping("/temas/{anoLetivo}")
  public List<TemaDTO> getTemasByAnoLetivo(@PathVariable int anoLetivo) {
    return cadidaturaAlunoTemasService.getTemasByAnoLetivo(anoLetivo);
  }

  @PostMapping("/candidatar")
  public boolean candidatarTema(@RequestBody Map<String, Object> payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    AlunoDTO alunoDTO = objectMapper.convertValue(payload.get("alunoDTO"), AlunoDTO.class);
    TemaDTO temaDTO = objectMapper.convertValue(payload.get("temaDTO"), TemaDTO.class);

    return cadidaturaAlunoTemasService.candidatarTema(alunoDTO, temaDTO);
  }

  @PostMapping("/removerTema")
  public boolean removerTema(@RequestBody Map<String, Object> payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    AlunoDTO alunoDTO = objectMapper.convertValue(payload.get("alunoDTO"), AlunoDTO.class);
    TemaDTO temaDTO = objectMapper.convertValue(payload.get("temaDTO"), TemaDTO.class);

    return cadidaturaAlunoTemasService.removerTema(alunoDTO, temaDTO);
  }

  @PostMapping("/tese/submeterFinal")
  public ResponseEntity<String> submeterTeseFinal(
      @RequestParam("id") Long teseId, @RequestParam("file") MultipartFile file) {
    try {
      // Obtém a instância existente da Tese
      System.out.println("Tese ID: " + teseId);
      Tese tese = teseService.getTeseById(teseId); // Implemente este método no seu serviço

      // Define o arquivo na instância de Tese
      tese.setFileData(file.getBytes());

      // Chama o serviço para lidar com a submissão da tese
      teseService.submeterTeseFinal(tese);

      return new ResponseEntity<>("Documento final de tese submetido com sucesso", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/tese/submeterProposta")
  public ResponseEntity<String> submeterPropostaTese(
      @RequestParam("id") Long teseId, @RequestParam("file") MultipartFile file) {
    try {
      // Obtém a instância existente da Tese
      Tese tese = teseService.getTeseById(teseId); // Implemente este método no seu serviço

      // Define o arquivo na instância de Tese
      tese.setFileData(file.getBytes());

      // Chama o serviço para lidar com a submissão da proposta de tese
      teseService.submeterPropostaTese(tese);

      return new ResponseEntity<>("Proposta de tese submetida com sucesso", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
