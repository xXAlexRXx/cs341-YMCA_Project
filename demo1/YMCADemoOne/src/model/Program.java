package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Program {
	private Long programId;
    private String programName;
    private String description;
    private int capacity;
    private int currentCapacity;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private double price;
    private int requirements;
    
    // Default constructor
    public Program() {
    }

    // Full constructor
    public Program(Long programId, String programName, String description, int capacity,
                   int currentCapacity, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String location, int requirements) {
        this.programId = programId;
        this.programName = programName;
        this.description = description;
        this.capacity = capacity;
        this.currentCapacity = currentCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requirements = requirements;
    }

    // Getters and setters
    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public int getCurrentCapacity() {
        return currentCapacity;
    }
    
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }
    
    public void setEndDate(LocalDate date) {
        this.endDate = date;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public LocalTime getEndTime() {
    	return endTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(LocalTime endTime) {
    	this.endTime = endTime;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
    public int getRequirements() {
        return requirements;
    }
    
    public void setRequirements(int requirements) {
        this.requirements = requirements;
    }

	@Override
	public String toString() {
		return "Program [programId=" + programId + ", programName=" + programName + ", description=" + description
				+ ", capacity=" + capacity + ", currentCapacity=" + currentCapacity + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", startTime=" + startTime + ", endTime=" + endTime + ", location="
				+ location + ", requirements=" + requirements + "]";
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
    
    
}
