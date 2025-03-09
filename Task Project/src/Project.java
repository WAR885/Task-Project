import java.util.ArrayList;
public class Project 
{
    private String name;
    private double totalTime = 0.0;
    private String fileName;
    private Task tasks;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Plan> lowestPlans;
    private Scheduler schedule = null;
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Project(String name, Task tasks, String fileName)
    {
        this.name = name;
        this.totalTime = 0.0;
        this.tasks = tasks;
        this.fileName = fileName;
        lowestPlans = new ArrayList<>();
        findlowestTasks();
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setFileName(String file)
    {
       this.fileName = file;
    }
    public void setTasks(Task tasks)
    {
        this.tasks = tasks;
    }
    public String getFileName()
    {
        return fileName;
    }
    public String getName()
    {
        return name;
    }
    public double getTotalTime()
    {
        return totalTime;
    }
    public Task getTasks()
    {
        return tasks;
    }
    public Scheduler getSchedule()
    {
        return schedule;
    }
    public void setTotalTime(double totalTime)
    {
        this.totalTime = totalTime;
    }
    public void setSchedule(Scheduler schedule)
    {
        this.schedule = schedule;
    }
    public void setSchedule(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay)
    {
        schedule = new Scheduler(startYear,startMonth,startDay,endYear,endMonth,endDay,lowestPlans);
    }
    @Override
    public String toString()
    {
        String output = String.format("Project Name: %s %nTotal Time in Hours: %s %n",name,totalTime);
        output += "\nMain Goal: \n";
        output += tasks.toString();
        return output;
    }
    public void removeSubTask(Task t)
    {
        tasks.removeTask(t);
        Task superTask = t.getSuperTask();
        superTask.addTimeInHours(-t.getTimeInHours());
        for(int i = 0; i < lowestPlans.size(); i++)
        {
            if(lowestPlans.get(i).getTaskName().equals(t.getTitle()))
            {
                Plan newPlan = new Plan(superTask.getTimeInHours(),superTask.getTitle());
                if(schedule != null)
                {
                    schedule.deletePlan(lowestPlans.get(i));
                    schedule.addPlan(newPlan);
                }
                lowestPlans.remove(i);
                lowestPlans.add(newPlan);
                
            }
        }
    }
    public void findlowestTasks()
    {
        if(tasks != null)
        {
            Task.findLowestTasks(tasks, lowestPlans);
        }
    }
    public void newLowestTask(Task t)
    {
        Task parent = t.getSuperTask();
        for(int i = 0; i < lowestPlans.size(); i++)
        {
            if(lowestPlans.get(i).getTaskName().equals(parent.getTitle()))
            {
                if(schedule != null)
                {
                    schedule.deletePlan(lowestPlans.get(i));
                }
                lowestPlans.remove(i);
            }
        }
        lowestPlans.add(new Plan(t.getTimeInHours(),t.getTitle()));
        if(schedule != null)
        {
            schedule.addPlan(lowestPlans.get(lowestPlans.size()-1));
        }
    }
    public void changeTimeOnLowestPlan(double time, String name)
    {
        for(int i = 0; i < lowestPlans.size(); i++)
        {
            if(lowestPlans.get(i).getTaskName().equals(name))
            {
                schedule.deletePlan(lowestPlans.get(i));
                lowestPlans.get(i).setTimeInHours(time);
                schedule.addPlan(lowestPlans.get(i));
                break;
            }
        }
    }
    public boolean equals(Project otherProject)
    {
        return (name.equals(otherProject.getName()) && compareTasks(otherProject) && 
        fileName.equals(otherProject.getFileName()));
    }
    private boolean compareTasks(Project otherProject)
    {
        Task otherTask = otherProject.getTasks();
        if(otherTask == null)
        {
            return (tasks == null);
        }
        return otherTask.equals(tasks);
    }
}
