package edu.school21.info.util;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;

class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) {

        int numColumns = getFieldMap().values().size();
        String[] header = new String[numColumns];
        super.setColumnMapping(header);

        BeanField<T, Integer> beanField;
        for (int i = 0; i < numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = beanField.getField().getDeclaredAnnotation(CsvBindByName.class).column();
            header[i] = columnHeaderName;
        }
        return header;
    }
}