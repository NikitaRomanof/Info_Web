package edu.school21.info.controller.operations;

import edu.school21.info.service.CallStorageService;
import edu.school21.info.service.ExecuteCallService;
import edu.school21.info.util.FileConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/procedure")
public class QueryController {

    private final ExecuteCallService executeCallService;
    private final CallStorageService callStorageService;

    private Map<String, List<Object>> cachedResult;

    @GetMapping("/{id}")
    public String callStoredProcedure(@PathVariable("id") long id,
                                      Model model,
                                      @RequestParam(value = "arg", required = false) Object... args) throws SQLException {
        String query = callStorageService.getOneOrThrow(id).getCall();
        callOrExecute(query, args);
        model.addAttribute("result", cachedResult);
        return "procedure/viewProcedure";
    }

    @RequestMapping("/custom")
    public String executeCustomQuery(@RequestParam(value = "query") String query, Model model) throws SQLException {
        callOrExecute(query);
        model.addAttribute("result", cachedResult);
        return "procedure/viewProcedure";
    }

    @RequestMapping("/func/{id}")
    public String executeWithDate(@PathVariable("id") long id, Model model,
                                  @RequestParam(value = "date") Date date) throws SQLException {
        return callStoredProcedure(id, model, date);
    }

    @SneakyThrows
    @RequestMapping(path = "/unload")
    public void handleFileUnload(HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"custom.csv\"");
        FileConverter.writeCsvFromMap(servletResponse.getWriter(), cachedResult);
    }

    private void callOrExecute(String query, Object... args) throws SQLException {
        boolean isProcedureCall = query.contains("call");
        if (args == null) {
            cachedResult = isProcedureCall ?
                    executeCallService.callProcedure(query) :
                    executeCallService.executeFunction(query);
        } else {
            cachedResult = isProcedureCall ?
                    executeCallService.callProcedure(query, args) :
                    executeCallService.executeFunction(query, args);
        }
    }

}
