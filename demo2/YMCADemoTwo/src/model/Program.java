package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

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
    private String days;
    private Long staffId;

    // Default constructor
    public Program() {
    }

    // Full constructor
    public Program(Long programId, String programName, String description, int capacity,
                   int currentCapacity, LocalDate startDate, LocalDate endDate,
                   LocalTime startTime, LocalTime endTime, String location,
                   double price, int requirements, String days, Long staffId) {
        this.programId = programId;
        this.programName = programName;
        this.description = description;
        this.capacity = capacity;
        this.currentCapacity = currentCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.price = price;
        this.requirements = requirements;
        this.days = days;
        this.staffId = staffId;
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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRequirements() {
        return requirements;
    }

    public void setRequirements(int requirements) {
        this.requirements = requirements;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Long getStaffId() {   // <<< Getter for staffId
        return staffId;
    }

    public void setStaffId(Long staffId) {   // <<< Setter for staffId
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "Program [programId=" + programId + ", programName=" + programName + ", description=" + description
                + ", capacity=" + capacity + ", currentCapacity=" + currentCapacity + ", startDate=" + startDate
                + ", endDate=" + endDate + ", startTime=" + startTime + ", endTime=" + endTime + ", location="
                + location + ", price=" + price + ", requirements=" + requirements + ", days=" + days
                + ", staffId=" + staffId + "]";
    }

    public boolean meetsPrerequisite(Program prerequisiteProgram, boolean isRegisteredForPrerequisite) {
        // If there is no prerequisite, then it's fine
        if (this.requirements == 0 || this.requirements == -1) {
            return true;
        }

        // If user is NOT registered for the prerequisite, they don't meet it
        if (!isRegisteredForPrerequisite) {
            return false;
        }

        // Make sure the prerequisite ends BEFORE this program starts
        if (prerequisiteProgram.getEndDate().isBefore(this.startDate)) {
            return true; // Good: prerequisite will be done before starting
        } else {
            return false; // Bad: prerequisite is still running
        }
    }

    public boolean conflictsWith(Program other) {
        // Check if times are completely separate (no overlap)
        // Check if date ranges don't overlap
        if (this.startTime.isAfter(other.endTime) || other.startTime.isAfter(this.endTime) || this.startDate.isAfter(other.endDate) || other.startDate.isAfter(this.endDate)) {
            return false; // No date overlap
        }

        // Find overlapping dates
        Set<DayOfWeek> overlapDays = new HashSet<>();
        LocalDate curDay = this.startDate.isAfter(other.startDate) ? this.startDate : other.startDate;
        LocalDate endDay = this.endDate.isBefore(other.endDate) ? this.endDate : other.endDate;

        while (!curDay.isAfter(endDay)) {
            overlapDays.add(curDay.getDayOfWeek());
            curDay = curDay.plusDays(1);
        }

        // Check if they meet on the same overlapping days
        for (DayOfWeek day : overlapDays) {
            if (this.occursOn(day) && other.occursOn(day)) {
                return true; // Conflict detected
            }
        }

        return false; // No conflict
    }

    private boolean occursOn(DayOfWeek day) {
        switch (day) {
            case MONDAY: return days.contains("Mon");
            case TUESDAY: return days.contains("Tue");
            case WEDNESDAY: return days.contains("Wed");
            case THURSDAY: return days.contains("Thu");
            case FRIDAY: return days.contains("Fri");
            case SATURDAY: return days.contains("Sat");
            case SUNDAY: return days.contains("Sun");
            default: return false;
        }
    }
}
