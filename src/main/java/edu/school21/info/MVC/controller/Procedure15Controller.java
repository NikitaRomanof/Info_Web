package edu.school21.info.MVC.controller;

import edu.school21.info.dao.ProcedureDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/procedure")
public class Procedure15Controller {

    private final ProcedureDAO pDAO;

    public Procedure15Controller(ProcedureDAO pDAO) {
        this.pDAO = pDAO;
    }

    // НУЖНО СДЕЛАТЬ ПОЛЕ ВВОДА ВО ФРОНТЕ, ЧТО БЫ ПРИНИМАТЬ 2,3,4 ПАРАМЕТРЫ СЕЙЧАС ОНИ УХОДИТЯ В ДАО ХАРД КОДОМ
    @GetMapping("/procedure15")
    public String getTable5(Model model) {
        try {

            Map<String, List<Object>> result = pDAO.callProcedure
                    ("{call first_second_but_not_third(?::refcursor, ?::varchar, ?::varchar, ?::varchar)}", "C2_SimpleBashUtils", "C3_s21_stringplus", "C5_s21_decimal");
            model.addAttribute("procedure", result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "procedure/viewProcedure";
    }
}
