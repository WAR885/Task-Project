import java.time.LocalDate;
public class Plan {
    private LocalDate date;
    private double timeInHours;
    private String taskName;
    public Plan(LocalDate date, double timeInHours, String taskName)
    {
        this.date = date;
        this.timeInHours = timeInHours;
        this.taskName = taskName;
    }
    public Plan(double timeInHours, String taskName)
    {
        this.timeInHours = timeInHours;
        this.taskName = taskName;
    }
    public LocalDate getDate()
    {
        return date;
    }
    public double getTimeInHours()
    {
        return timeInHours;
    }
    public void setDate(LocalDate date)
    {
        this.date = date;
    }
    public void setDate(int year, int month, int day)
    {
        LocalDate date = LocalDate.of(year, month, day);
        this.date = date;
    }
    public void setTimeInHours(double timeInHours)
    {
        this.timeInHours = timeInHours;
    }
    public String getTaskName()
    {
        return taskName;
    }
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }
    public String toString()
    {
        int hours = (int)timeInHours;
        double minutes = (timeInHours-hours)*60;
        if(date != null)
        {
            String strD = date.getMonth() + "/" + date.getDayOfMonth() + "/" + date.getYear();
            return String.format("It is scheduled to do task: %s on %s for %d hours and %.2f minutes",taskName,strD,hours,minutes);
        }
        else
        {
            return String.format("It is yet to be scheduled to do task: %s for a total of %d hours and %.2f minutes",taskName,hours,minutes);
        }
       
    }
    public String condenscedToString()
    {
        if(date == null)
        {
            return String.format("[%s,%.2f,null]",taskName,timeInHours);
        }
        String strD = date.getMonth() + "/" + date.getDayOfMonth() + "/" + date.getYear();
        return String.format("[%s,%.2f,%s]",taskName,timeInHours,strD);
    }
}
