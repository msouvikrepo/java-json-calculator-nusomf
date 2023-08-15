package de.itdesign.application.model.output;

public class Output {

    private OutputName outputName;
    public OutputName getOutputName() {
        return outputName;
    }
    public void setOutputName(OutputName outputName) {
        this.outputName = outputName;
    }
    private RoundedValue roundedValue;

    
    
    public RoundedValue getRoundedValue() {
        return roundedValue;
    }
    public void setRoundedValue(RoundedValue roundedValue) {
        this.roundedValue = roundedValue;
    }
    
}