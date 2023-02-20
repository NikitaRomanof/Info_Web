package edu.school21.info.service;

import edu.school21.info.dao.ProcedureDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExecuteCallService {

    private final ProcedureDAO procedureDAO;

    public Map<String, List<Object>> callProcedure(String dataProcedure, Object... otherParam) throws SQLException {
        return procedureDAO.callProcedure(dataProcedure, otherParam);
    }

    public Map<String, List<Object>> executeFunction(String dataProcedure, Object... otherParam) throws SQLException {
        return procedureDAO.executeFunction(dataProcedure, otherParam);
    }


}
