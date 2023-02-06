package edu.school21.info.service;

import edu.school21.info.dao.ProcedureDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcedureCallService {

    private final ProcedureDAO procedureDAO;

    public Map<String, List<Object>> callProcedure(String dataProcedure, Object... otherParam) {
        return procedureDAO.callProcedure(dataProcedure, otherParam);
    }

}
