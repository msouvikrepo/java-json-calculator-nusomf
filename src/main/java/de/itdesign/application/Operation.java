package de.itdesign.application;

public class Operation {

    private Name name;
    private Function function;
    private OperationFields operationFields;
    private Filter filter;

    public OperationFields getOperationFields() {
        return operationFields;
    }
    public void setOperationFields(OperationFields operationFields) {
        this.operationFields = operationFields;
    }
    
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
    public Function getFunction() {
        return function;
    }
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }



}
