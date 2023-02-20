package edu.school21.info.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ProcedureDAO {

    private final JdbcTemplate jdbcTemplate;

    public Map<String, List<Object>> callProcedure(String dataProcedure, Object... otherParam) throws SQLException {
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             CallableStatement callableStatement = con.prepareCall(dataProcedure)) {
            con.setAutoCommit(false);
            int parameterIndex = 1;
            callableStatement.setObject(parameterIndex++, null);
            for (Object it : otherParam)
                callableStatement.setObject(parameterIndex++, it);
            callableStatement.registerOutParameter(1, Types.REF_CURSOR);
            callableStatement.execute();
            ResultSet rs = (ResultSet) callableStatement.getObject(1);
            return unpackResultSet(rs);
        }
    }

    public Map<String, List<Object>> executeFunction(String dataProcedure, Object... otherParam) throws SQLException {
        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement statement = con.prepareStatement(dataProcedure)) {
            int parameterIndex = 1;
            for (Object it : otherParam)
                statement.setObject(parameterIndex++, it);
            ResultSet rs = statement.executeQuery();
            return unpackResultSet(rs);
        }
    }

    private Map<String, List<Object>> unpackResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int countColumn = metaData.getColumnCount();
        Map<String, List<Object>> resultMap = new LinkedHashMap<>();
        List<String> nameColumn = new ArrayList<>();
        List<List<Object>> allColumn = new ArrayList<>();
        if (!resultSet.next()) return resultMap;
        for (int i = 0; i < countColumn; ++i) {
            nameColumn.add(metaData.getColumnName(i + 1));
            List<Object> tmp = new ArrayList<>();
            tmp.add(resultSet.getObject(i + 1));
            allColumn.add(tmp);
        }
        while (resultSet.next()) {
            for (int i = 0; i < countColumn; ++i) {
                allColumn.get(i).add(resultSet.getObject(i + 1));
            }
        }
        for (int i = 0; i < countColumn; ++i) {
            resultMap.put(nameColumn.get(i), allColumn.get(i));
        }
        return resultMap;
    }


}
