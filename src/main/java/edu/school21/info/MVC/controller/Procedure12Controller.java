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
public class Procedure12Controller {

    private final ProcedureDAO pDAO;

    public Procedure12Controller(ProcedureDAO pDAO) {
        this.pDAO = pDAO;
    }

    // НУЖНО СДЕЛАТЬ ПОЛЕ ВВОДА ВО ФРОНТЕ, ЧТО БЫ ПРИНИМАТЬ ПАРАМЕТР СЕЙЧАС ОН УХОДИТ В ДАО ХАРД КОДОМ
    @GetMapping("/procedure12")
    public String getTable5(Model model) {
        try {

            Map<String, List<Object>> result = pDAO.callProcedure
                    ("{call most_friendly(?::refcursor, ?::int)}",2);
            model.addAttribute("procedure", result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "procedure/viewProcedure";
    }
}
