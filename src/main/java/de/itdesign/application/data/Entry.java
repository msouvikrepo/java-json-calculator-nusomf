package de.itdesign.application.data;

public class Entry {

    private DataName dataName;
    
    public DataName getDataName() {
        return dataName;
    }
    public void setDataName(DataName dataName) {
        this.dataName = dataName;
    }
    private Population population;
    private ExtendedStatistics extendedStatistics;
        
    public Population getPopulation() {
        return population;
    }
    public void setPopulation(Population population) {
        this.population = population;
    }
    public ExtendedStatistics getExtendedStatistics() {
        return extendedStatistics;
    }
    public void setExtendedStatistics(ExtendedStatistics extendedStatistics) {
        this.extendedStatistics = extendedStatistics;
    }
    
}
