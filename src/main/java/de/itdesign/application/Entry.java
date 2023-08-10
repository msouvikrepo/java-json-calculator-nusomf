package de.itdesign.application;

public class Entry {

    private Name name;
    private Population population;
    private ExtendedStatistics extendedStatistics;
    
    public Name getName() {
        return name;
    }
    public void setName(Name name) {
        this.name = name;
    }
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
