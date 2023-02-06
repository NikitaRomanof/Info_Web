package edu.school21.info.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ConnectionByAllProcedure {

    private final JdbcTemplate jdbcTemplate;

    ResultSet startProcedure(String dataProcedure, Object... otherParam) {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             CallableStatement callableStatement = con.prepareCall(dataProcedure)) {

            con.setAutoCommit(false);
            int parameterIndex = 1;

            callableStatement.setObject(parameterIndex++, null);
            for (Object it : otherParam) {
                callableStatement.setObject(parameterIndex++, it);
            }
            callableStatement.registerOutParameter(1, Types.REF_CURSOR);
            callableStatement.execute();
            return (ResultSet) callableStatement.getObject(1);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        throw new RuntimeException("Error with getting ResultSet");
    }

}
