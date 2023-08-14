package de.itdesign.application.pojo.operations;

public class Operation {

    private OperationName operationName;
    private Function function;
    private OperationFields operationFields;
    private Filter filter;

    public OperationName getOperationName() {
        return operationName;
    }
    public void setOperationName(OperationName operationName) {
        this.operationName = operationName;
    }
    
    public OperationFields getOperationFields() {
        return operationFields;
    }
    public void setOperationFields(OperationFields operationFields) {
        this.operationFields = operationFields;
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
