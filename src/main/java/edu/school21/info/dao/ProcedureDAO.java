package edu.school21.info.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class ProcedureDAO {

    private final ConnectionByAllProcedure connect;

    public Map<String, List<Object>> callProcedure(String dataProcedure, Object ... otherParam) {
        try (ResultSet rs = connect.startProcedure(dataProcedure, otherParam)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int countColumn = rsmd.getColumnCount();
            Map<String, List<Object>> resultMap = new TreeMap<>();
            List<String> nameColumn = new ArrayList<>();
            List<List<Object>> allColumn = new ArrayList<>();

            rs.next();
            for (int i = 0; i < countColumn; ++i) {
                nameColumn.add(rsmd.getColumnName(i +  1));
                List<Object> tmp = new ArrayList<>();
                tmp.add(rs.getObject(i + 1 ));
                allColumn.add(tmp);
            }

            while (rs.next()) {
                for (int i = 0; i < countColumn; ++i) {
                    allColumn.get(i).add(rs.getObject(i + 1));
                }
            }

            for (int i = 0; i < countColumn; ++i) {
                resultMap.put(nameColumn.get(i), allColumn.get(i));
            }

            return resultMap;

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new TreeMap<String, List<Object>>();
    }

}
