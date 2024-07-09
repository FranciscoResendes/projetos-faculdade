package pt.ul.fc.css.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.dtos.DefesaDTO;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.dtos.UtilizadorEmpresarialDTO;
import pt.ul.fc.css.example.demo.entities.Aluno;
import pt.ul.fc.css.example.demo.entities.Defesa;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.MarcacaoDefesaHandler;
import pt.ul.fc.css.example.demo.handlers.SubmeterTemaHandler;
import pt.ul.fc.css.example.demo.services.AtribuicaoTemasAlunosService;
import pt.ul.fc.css.example.demo.services.CadidaturaAlunoTemasService;
import pt.ul.fc.css.example.demo.services.RegistarNotaService;
import pt.ul.fc.css.example.demo.services.StatisticsService;
import pt.ul.fc.css.example.demo.services.UtilizadorService;

@Controller
public class WebController {

  @Autowired private AtribuicaoTemasAlunosService atribuicaoTemasAlunosService;

  @Autowired private CadidaturaAlunoTemasService cadidaturaAlunoTemasService;

  @Autowired private MarcacaoDefesaHandler marcacaoDefesaHandler;

  @Autowired private SubmeterTemaHandler submeterTemaHandler;

  @Autowired private UtilizadorService utilizadorService;

  @Autowired private RegistarNotaService regNotaService;

  @Autowired private StatisticsService statService;

  @RequestMapping("/")
  public String getIndex(Model model) {
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String login(final Model model) {
    return "login";
  }

  @GetMapping("/menu")
  public String menu() {
    return "menu";
  }

  @GetMapping("/registar")
  public String register(Model model) {
    model.addAttribute("registarUtilizador", new UtilizadorEmpresarialDTO());
    return "registar";
  }

  @PostMapping("/registar")
  public String saveUtilizadorEmpresarial(
      @ModelAttribute UtilizadorEmpresarialDTO utilizadorEmpresarial) {
    utilizadorService.registarUtilizador(
        utilizadorService.toUtilizadorEmpresa(utilizadorEmpresarial));
    return "redirect:/login";
  }

  @PostMapping("/login")
  public String logging(@RequestParam("user") String username) {
    Object user = utilizadorService.authenticate(username, "pass");
    if (user != null) {
      // User is authenticated, redirect to the appropriate page
      return "menu";
    }
    // User is not authenticated, redirect to the error page
    return "erro";
  }

  @GetMapping("/submissaoTema")
  public String showForm(Model model) {
    model.addAttribute("temaDTO", new TemaDTO());
    return "submissaoTema";
  }

  @PostMapping("/submissaoTema")
  public String submitForm(@ModelAttribute("temaDTO") TemaDTO temaDTO) {
    submeterTemaHandler.submitTema(temaDTO);
    return "submissaoTema";
  }

  @GetMapping("/erro")
  public String erro(final Model model) {
    return "erro";
  }

  @GetMapping("/atribuicaoTema")
  public String getAtribuicaoTema(Model model) {
    List<Aluno> alunos = atribuicaoTemasAlunosService.getAlunosComCandidaturaFinalizada(true);
    model.addAttribute("alunos", alunos);
    return "ListaAlunosParaAtribuirTema";
  }

  @GetMapping("/marcacaoDefesa")
  public String marcarDefesa(final Model model) {
    model.addAttribute("defesa", new DefesaDTO());
    return "marcacao_defesa";
  }

  @PostMapping("/marcacaoDefesa")
  public String guardarDefesa(
      final Model model,
      @ModelAttribute DefesaDTO defDto,
      @RequestParam("alunoNum") String alunoNum) {
    Defesa novaDefesa = marcacaoDefesaHandler.toDefesa(defDto);
    if (novaDefesa == null) {
      return "redirect:/erro";
    }
    marcacaoDefesaHandler.marcarDefesa(novaDefesa, Integer.parseInt(alunoNum));
    return "menu";
  }

  @GetMapping("/registarNota")
  public String registarNota(final Model model) {
    return "registar_nota";
  }

  @PostMapping("/registarNota")
  public String guardarNota(
      final Model model,
      @RequestParam("numAluno") String numAluno,
      @RequestParam("nota") String nota,
      @RequestParam("defesaId") String defesaId) {
    long def = Long.parseLong(defesaId);
    int classificacao = Integer.parseInt(nota);
    int aluno = Integer.parseInt(numAluno);
    boolean semErro = regNotaService.registarNota(def, classificacao, aluno);
    if (semErro) {
      return "redirect:/menu";
    } else {
      return "erro";
    }
  }

  @GetMapping("/estatisticas")
  public String statisticas(final Model model) {
    model.addAttribute("statistics", statService.getStatistics());
    return "estatisticas";
  }

  @GetMapping("/aluno/{id}")
  public String getAluno(@PathVariable("id") Long id, Model model) {
    Aluno aluno = atribuicaoTemasAlunosService.getAlunoById(id);
    model.addAttribute("aluno", aluno);

    return "atribuicaoTema";
  }

  @PostMapping("/criarTese")
  public String criarTese(
      @RequestParam("alunoId") Long alunoId, @RequestParam("temaId") Long temaId) {
    Aluno aluno = atribuicaoTemasAlunosService.getAlunoById(alunoId);
    Tema tema = atribuicaoTemasAlunosService.getTemaById(temaId);
    atribuicaoTemasAlunosService.criarTese(aluno, tema);
    return "redirect:/menu";
  }
}
