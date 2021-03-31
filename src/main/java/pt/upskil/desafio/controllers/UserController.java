package pt.upskil.desafio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.upskil.desafio.entities.Dificuldade;
import pt.upskil.desafio.entities.Pergunta;
import pt.upskil.desafio.entities.Resposta;
import pt.upskil.desafio.entities.User;
import pt.upskil.desafio.exceptions.AdicionarPerguntaException;
import pt.upskil.desafio.services.PerguntaServico;
import pt.upskil.desafio.services.RegistrationService;
import pt.upskil.desafio.services.UserService;
import pt.upskil.desafio.services.UserValidationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserValidationService userValidationService;
    @Autowired
    PerguntaServico perguntaServico;


    @GetMapping(value = "/userToMain")
    public String showMainPage() {
        return "redirect:/";
    }


    //registration
    @GetMapping(value = "/public/registration")
    public String showRegistrationPage(ModelMap modelMap) {
        modelMap.put("user", new User());

        return "public/registration";
    }

    @PostMapping(value = "/public/registration")
    public String registerUser(@ModelAttribute User user,
                               ModelMap mpError,
                               @RequestParam String confirmarPassword2) {

        userValidationService.clear().setUser(user)
                .validFirstName()
                .validLastName()
                .validPassword()
                .samePassword(confirmarPassword2)
                .validEmail()
                .validEmail2()
                .validUsername()
                .validCity();

        if (!userValidationService.isValid()) {
            mpError.addAllAttributes(userValidationService.getErrorModelMap());
            mpError.put("user", user);

            return "public/registration";
        }

        // Add user to database

        registrationService.encryptPassword(user);
        userService.addUser(user);

        return "redirect:/public/login";
    }

    //login
    @GetMapping(value = {"/public/login", "/"})
    public String showLoginPage() {

        return "public/login";
    }

    //recover password
    @GetMapping(value = "/public/recover-password")
    public String showRecoverPasswordPage(ModelMap modelMap) {
        modelMap.put("username", "");
        modelMap.put("email", "");

        return "public/recover-password";
    }

    @PostMapping(value = "/public/recover-password")
    public String recoverPassword(ModelMap modelMap,
                                  @RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password1,
                                  @RequestParam String password2) {
        if (userService.validateUserMail(username, email)) {
            if (password1.equals(password2)) {
                User user = userService.findUser(username);
                registrationService.changeEncryptPassword(user, password1);
                userService.addUser(user);
                return "redirect:/login";
            } else {
                modelMap.put("errorMessage", "Palavras-passe não coincidem");
            }
        } else {
            modelMap.put("errorMessage", "Username/Email inválido");
        }
        modelMap.put("username", username);
        modelMap.put("email", email);
        return "public/recover-password";
    }

    @GetMapping("/player/dashboard")
    public String goToDashboard() {
        return "/player/dashboard";
    }

    @GetMapping("/player/add-question")
    public String showPageAdicionarPergunta(ModelMap modelMap) {
        List<Dificuldade> dificuldades = Arrays.asList(Dificuldade.values());
        modelMap.put("dificuldades", dificuldades);
        return "/player/add-question";
    }

    @PostMapping("/player/add-question")
    public String adicionarPergunta(ModelMap modelMap,
                                    @RequestParam String pergunta,
                                    @RequestParam String resposta1,
                                    @RequestParam String resposta2,
                                    @RequestParam String resposta3,
                                    @RequestParam String resposta4,
                                    @RequestParam int respostaCerta,
                                    @RequestParam int dificuldadeType) {

        Pergunta perguntaObj = Pergunta.criarPergunta(pergunta,
                resposta1,
                resposta2,
                resposta3,
                resposta4,
                respostaCerta,
                dificuldadeType);

        try {
            perguntaServico.addicionarPergunta(perguntaObj);
        } catch (AdicionarPerguntaException e) {
            modelMap.put("pergunta", pergunta);
            modelMap.put("resposta1", resposta1);
            modelMap.put("resposta2", resposta2);
            modelMap.put("resposta3", resposta3);
            modelMap.put("resposta4", resposta4);
            modelMap.put("respostaCerta", respostaCerta);
            modelMap.put("dificuldadeType", dificuldadeType);
            modelMap.put("msgError", "Não foi possível adicionar a questão.");
            return "/player/add-question";
        }

        System.out.println("Pergunta adicionada");
        // TODO página de confirmação
        return "/player/dashboard";
    }


}
