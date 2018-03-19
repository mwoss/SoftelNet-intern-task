package presenter.parser;

import java.util.*;
import java.util.List;


public class SQLParser {

    private final LinkedHashSet<String> sqlKeywords =
            new LinkedHashSet<>(Arrays.asList("SELECT", "UPDATE", "DELETE"));
    private final String paramsKeyword = "params";


    public String parseRow(String dataRow){
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(dataRow.split("\\s+")));

        if(!isNumeric(inputList.get(0)))
            throw new IllegalArgumentException("Table row with SQL code has to start with numeric ID");

        return String.join(" ", toNormalSqlForm(inputList));
    }

    private ArrayList<String> toNormalSqlForm(ArrayList<String> inputList){
        ArrayList<String> sqlCode = extractSQLCode(inputList);
        ArrayList<String> parameters = extractParameters(sqlCode);
        System.out.println(String.join(" ", sqlCode));
        System.out.println(String.join(" ", parameters));

        for(int i = 0; i < sqlCode.size(); i++){
            if(sqlCode.get(i).equals("?")){
                sqlCode.set(i, parameters.get(0));
                parameters.remove(0);
            }
            else if(sqlCode.get(i).equals("?)")){
                sqlCode.set(i, parameters.get(0) + ")");
                parameters.remove(0);
            }
            else if(sqlCode.get(i).contains(paramsKeyword))
                return new ArrayList<>(sqlCode.subList(0, i));
        }
        return sqlCode;
    }

    private ArrayList<String> extractValue(List<String> parameters){
        ArrayList<String> paramValues = new ArrayList<>();
        for(int i = 1; i < parameters.size(); i+=2){
            paramValues.add(parameters.get(i).replaceAll("[\\[\\],]", ""));
        }
        return paramValues;
    }

    private ArrayList<String> extractParameters(ArrayList<String> sqlCode){
        for(int i = 0; i < sqlCode.size(); i++){
            if(sqlCode.get(i).contains(paramsKeyword))
                return extractValue(sqlCode.subList(i, sqlCode.size()));
        }
        throw new IllegalArgumentException("Cannot find any parameters");
    }

    private ArrayList<String> extractSQLCode(ArrayList<String> inputList){
        for(int i = 0; i < inputList.size(); i++){
            if(sqlKeywords.contains(inputList.get(i)))
                return new ArrayList<>(inputList.subList(i, inputList.size()));
        }
        throw new IllegalArgumentException("Cannot find any SQL query");
    }

    private boolean isNumeric(String value) {
        return value.matches("-?\\d+(\\.\\d+)?");
    }

}
