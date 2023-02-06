package edu.school21.info.MVC.controller;


import edu.school21.info.service.CallBodyService;
import edu.school21.info.service.ProcedureCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/procedure")
public class ProcedureController {

    private final ProcedureCallService procedureCallService;
    private final CallBodyService callBodyService;

    @GetMapping("/procedure4")
    public String getTable4(Model model) {

        try {
            Map<String, List<Object>> result = procedureCallService.callProcedure("{call success_percent(?::refcursor)}");
            model.addAttribute("procedure", result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "procedure/viewProcedure";
    }

    @GetMapping("/{id}")
    public String callProcedure(@PathVariable("id") long id, Model model) {
        try {
            String callBody = callBodyService.getOneOrThrow(id).getCall();
            Map<String, List<Object>> result = procedureCallService.callProcedure(callBody);
            model.addAttribute("procedure", result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "procedure/viewProcedure";
    }

}
