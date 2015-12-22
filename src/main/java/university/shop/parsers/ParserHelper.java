package university.shop.parsers;

import university.shop.exception.BadRequestApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dara on 12/5/2015.
 */
public class ParserHelper {
    public static Map<String, String> getValuesFromQuery(List<String> leksems, int startIndex,final List<String> COLUMN_NAMES) throws BadRequestApiException {
        List<String> usedColumns = new ArrayList<>();
        Map<String, String> columnValueMap = new HashMap<>();
        for (int i = startIndex; i < leksems.size(); i+=2) {
            String columnName = leksems.get(i);
            if (!COLUMN_NAMES.contains(columnName)) {
                throw new BadRequestApiException("Unknown column name " + columnName + " was found. Must be " + COLUMN_NAMES);
            }
            if (usedColumns.contains(columnName)){
                throw new BadRequestApiException("One column name " + columnName + " was found several times");
            }
            if (i + 1 >= leksems.size()) {
                throw new BadRequestApiException("Value for " + columnName + " was not found");
            }
            usedColumns.add(columnName);
            String value = leksems.get(i + 1);
            columnValueMap.put(columnName, value);
        }
        return columnValueMap;
    }
}
