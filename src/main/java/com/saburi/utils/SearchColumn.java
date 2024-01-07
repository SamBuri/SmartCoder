/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

//import static helpers.FXUIUtils.getDouble;

import com.saburi.dataacess.DataAccess;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 *
 * @author ClinicMaster13
 */
public class SearchColumn {

    private String name;
    private String displayValue;
    private Object value;
    private boolean visible = true;
    private SearchDataTypes dataType;
    private List searchTypes;
    private SearchType defaultSearchType;
    private LogicalOperators predicateType;

    public static enum LogicalOperators {
        OR, AND
    }

    public static enum SearchDataTypes {
        NUMBER, STRING, BOOLEAN, DATE
    }

    public static enum SearchType {
        Contains, Equal, Between, Less, Greater, Before, After, Ends, Begins, Length_Less, Length_Greater, Length_Equal
    }

    public static enum StringSearchTypes {
        Contains, Equal, Begins, Ends, Length_Less, Length_Greater, Length_Equal
    }

    public static enum NunberSearchTypes {
        Contains, Equal, Between, Less, Greater, Begins, Ends, Length_Less, Length_Greater, Length_Equal
    }

    public static enum BooleanSearchTypes {
        Contains, Equal
    }

    public static enum DateSearchTypes {
        Contains, Equal, Between, Before, After, Begins, Ends, Lenth_Less, Length_Greater, Length_Equal
    }

    public SearchColumn(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public SearchColumn(String name, Object value, LogicalOperators predicateType) {
        this.name = name;
        this.value = value;
        this.predicateType = predicateType;
    }

    public SearchColumn(String name, Object value, SearchType defaultSearchType, LogicalOperators predicateType) {
        this.name = name;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
        this.predicateType = predicateType;
    }

    public SearchColumn(String name, Object value, SearchType defaultSearchType) {
        this.name = name;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
    }

    public SearchColumn(String name, Object value, SearchType defaultSearchType, LogicalOperators predicateType, boolean visible) {
        this.name = name;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
        this.predicateType = predicateType;
        this.visible = visible;
    }

    public SearchColumn(String name, Object value, SearchType defaultSearchType, boolean visible) {
        this.name = name;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
        this.visible = visible;
    }

    public SearchColumn(String name, String displayValue, Object value, SearchDataTypes dataType) {
        this.name = name;
        this.displayValue = displayValue;
        this.dataType = dataType;
        this.value = value;
        switch (dataType) {
            case STRING:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
            case NUMBER:
                this.searchTypes = Arrays.asList(NunberSearchTypes.values());
                this.defaultSearchType = SearchType.Equal;
                break;
            case DATE:
                this.searchTypes = Arrays.asList(DateSearchTypes.values());
                this.defaultSearchType = SearchType.Between;
                break;

            case BOOLEAN:
                this.searchTypes = Arrays.asList(BooleanSearchTypes.values());
                this.defaultSearchType = SearchType.Equal;
                break;
            default:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
        }
    }

    public SearchColumn(String name, String displayValue, Object value, SearchDataTypes dataType, boolean visible) {
        this.name = name;
        this.displayValue = displayValue;
        this.dataType = dataType;
        this.value = value;
        this.visible = visible;
        switch (dataType) {
            case STRING:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
            case NUMBER:
                this.searchTypes = Arrays.asList(NunberSearchTypes.values());
                this.defaultSearchType = SearchType.Equal;
                break;
            case DATE:
                this.searchTypes = Arrays.asList(DateSearchTypes.values());
                this.defaultSearchType = SearchType.Between;
                break;

            case BOOLEAN:
                this.searchTypes = Arrays.asList(BooleanSearchTypes.values());
                this.defaultSearchType = SearchType.Equal;
                break;
            default:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
        }
    }

    public SearchColumn(String name, String displayValue, Object value, SearchDataTypes dataType, SearchType defaultSearchType) {
        this.name = name;
        this.displayValue = displayValue;
        this.dataType = dataType;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
        switch (dataType) {
            case STRING:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                break;
            case NUMBER:
                this.searchTypes = Arrays.asList(NunberSearchTypes.values());
                break;
            case DATE:
                this.searchTypes = Arrays.asList(DateSearchTypes.values());
                break;

            case BOOLEAN:
                this.searchTypes = Arrays.asList(BooleanSearchTypes.values());
                break;
            default:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
        }
    }

    public SearchColumn(String name, String displayValue, Object value, SearchDataTypes dataType, SearchType defaultSearchType, boolean visible) {
        this.name = name;
        this.displayValue = displayValue;
        this.dataType = dataType;
        this.value = value;
        this.defaultSearchType = defaultSearchType;
        this.visible = visible;
        switch (dataType) {
            case STRING:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                break;
            case NUMBER:
                this.searchTypes = Arrays.asList(NunberSearchTypes.values());
                break;
            case DATE:
                this.searchTypes = Arrays.asList(DateSearchTypes.values());
                break;

            case BOOLEAN:
                this.searchTypes = Arrays.asList(BooleanSearchTypes.values());
                break;
            default:
                this.searchTypes = Arrays.asList(StringSearchTypes.values());
                this.defaultSearchType = SearchType.Contains;
                break;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setDataType(SearchDataTypes dataType) {
        this.dataType = dataType;
    }

    public void setSearchTypes(List searchTypes) {
        this.searchTypes = searchTypes;
    }

    public void setDefaultSearchType(SearchType defaultSearchType) {
        this.defaultSearchType = defaultSearchType;
    }

    public String getName() {
        return name;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public SearchDataTypes getDataType() {
        return dataType;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public LogicalOperators getPredicateType() {
        return predicateType;
    }

    public void setPredicateType(LogicalOperators predicateType) {
        this.predicateType = predicateType;
    }

    public List getSearchTypes() {
        return searchTypes;
    }

    public SearchType getDefaultSearchType() {
        return defaultSearchType;
    }

    public Object getValue() {
        return value;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getSQLString() {
        return this.predicateType == null ? this.name.concat(" " + getOperator() + " :".concat(value.toString())) : this.predicateType.name().concat(" ").concat(this.name.concat(" " + getOperator() + " :".concat(value.toString()))).concat(" ");
    }

    private String getOperator() {
        switch (this.defaultSearchType) {
            case Equal:
                return "=";
            case Greater:
                return ">";
            case Less:
                return "<";
            default:
                return "=";
        }
    }

    public boolean valueEquals(Object value) {

        switch (this.dataType) {
            case BOOLEAN:
                return Objects.equals(Boolean.valueOf((this.value.toString())), Boolean.valueOf(String.valueOf(value.toString())));

            case NUMBER:

                try {
//                                Double d = getDouble(value, "Search Value");
                    return Double.valueOf(this.getValue().toString()).equals(Double.valueOf(String.valueOf(value.toString())));
                } catch (Exception ex) {
                    throw ex;

                }

            case DATE:
                return this.value.equals(LocalDate.parse(value.toString()));
            default:
                return this.value.toString().equalsIgnoreCase(String.valueOf(value));

        }

    }

    public boolean valueContains(String value) {
        return this.value.toString().toLowerCase().contains(value);
    }

    public boolean valueBegins(String value) {
        return this.value.toString().toLowerCase().startsWith(value);
    }

    public boolean valueEnds(String value) {
        return this.value.toString().toLowerCase().endsWith(value);
    }

    public boolean lengthLess(int value) {
        return this.value.toString().length() < value;
    }

    public boolean lengthEqual(int value) {
        return this.value.toString().length() == value;
    }

    public boolean lengthGreater(int value) {
        return this.value.toString().length() > (value);
    }

    public boolean valueBetween(Object value, Object value1) {
        if (this.dataType == SearchDataTypes.DATE) {
            LocalDate date = LocalDate.parse(this.value.toString());
            return (date.isAfter(LocalDate.parse(value.toString())) && date.isAfter(LocalDate.parse(value1.toString())));
        } else {

            double number = Double.valueOf(this.value.toString());
            return (number > Double.valueOf(value.toString()) && number < Double.valueOf(value1.toString()));
        }
    }

    public boolean dateBefore(LocalDate localDate) {
        LocalDate date = LocalDate.parse(this.value.toString());
        return (date.isBefore(localDate));

    }

    public boolean dateAfter(LocalDate localDate) {
        LocalDate date = LocalDate.parse(this.value.toString());
        return (date.isAfter(localDate));

    }

    public Predicate<SearchColumn> equalsPrediacte(Object value) {

        switch (this.dataType) {
            case BOOLEAN:
                return (p) -> Objects.equals(Boolean.valueOf((p.value.toString())), Boolean.valueOf(String.valueOf(value.toString())));

            case NUMBER:

                try {
//                                Double d = getDouble(value, "Search Value");
                    return (p) -> Double.valueOf(p.getValue().toString()).equals(Double.valueOf(String.valueOf(value.toString())));
                } catch (Exception ex) {
                    throw ex;

                }

            case DATE:
                return (p) -> p.value.equals(LocalDate.parse(value.toString()));
            default:
                return (p) -> p.value.toString().equalsIgnoreCase(String.valueOf(value));

        }

    }

    public Predicate<SearchColumn> containsPrediacte(String value) {
        return (p) -> p.getValue().toString().toLowerCase().contains(value);
    }

    public Predicate<SearchColumn> beginsPrediacte(String value) {
        return (p) -> p.getValue().toString().toLowerCase().startsWith(value);
    }

    public Predicate<SearchColumn> endsPrediacte(String value) {
        return (p) -> p.getValue().toString().toLowerCase().endsWith(value);
    }

    public Predicate<SearchColumn> lengthLessPrediacte(int value) {
        return (p) -> p.getValue().toString().length() < value;
    }

    public Predicate<SearchColumn> lengthEqualPrediacte(int value) {
        return (p) -> p.getValue().toString().length() == (value);
    }

    public Predicate<SearchColumn> lengthGreaterPredicate(int value) {
        return (p) -> p.getValue().toString().length() > (value);
    }

    public Predicate<SearchColumn> betweenPrediacte(Object value, Object value1) {
        if (this.dataType == SearchDataTypes.DATE) {
            LocalDate date = LocalDate.parse(this.value.toString());
            return (p) -> (date.isAfter(LocalDate.parse(value.toString())) && date.isAfter(LocalDate.parse(value1.toString())));
        } else {

            int number = Integer.valueOf(this.value.toString());
            return (p) -> (number > Integer.valueOf(value.toString()) && number < Integer.valueOf(value1.toString()));
        }
    }

    public Predicate<SearchColumn> dateBeforePrediacte(LocalDate localDate) {
        LocalDate date = LocalDate.parse(this.value.toString());
        return (p) -> (date.isBefore(localDate));

    }

    public Predicate<SearchColumn> dateAfterPrediacte(LocalDate localDate) {
        LocalDate date = LocalDate.parse(this.value.toString());
        return (p) -> (date.isAfter(localDate));

    }

    public Predicate<DataAccess> equalsPrediacte(SearchColumn selectedSearchColumn, Object value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn)
                -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn)
                -> (searchColumn.valueEquals(value)));

    }

    public Predicate<DataAccess> containsPrediacte(SearchColumn selectedSearchColumn, String value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.valueContains(value)));

    }

    public Predicate<DataAccess> beginsPrediacte(SearchColumn selectedSearchColumn, String value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.valueBegins(value)));

    }

    public Predicate<DataAccess> endsPrediacte(SearchColumn selectedSearchColumn, String value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.valueEnds(value)));

    }

    public Predicate<DataAccess> lengthLessPrediacte(SearchColumn selectedSearchColumn, int value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.lengthLess(value)));

    }

    public Predicate<DataAccess> lengthLessEqualPredicate(SearchColumn selectedSearchColumn, int value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.lengthEqual(value)));

    }

    public Predicate<DataAccess> lengthLessGreaterPredicate(SearchColumn selectedSearchColumn, int value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.lengthGreater(value)));
    }

    public Predicate<DataAccess> betweenPredicate(SearchColumn selectedSearchColumn, Object value, Object value1) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.valueBetween(value, value1)));

    }

    public Predicate<DataAccess> dateBeforePredicate(SearchColumn selectedSearchColumn, LocalDate value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.dateBefore(value)));

    }

    public Predicate<DataAccess> dateAfterPredicate(SearchColumn selectedSearchColumn, LocalDate value) {
        return (p) -> p.getSearchColumns().stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.dateAfter(value)));

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchColumn other = (SearchColumn) obj;
        return Objects.equals(this.name, other.name);
    }

}
