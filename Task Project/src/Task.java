import java.util.ArrayList;

public class Task 
{
    private ArrayList<Task> subTasks = new ArrayList<>();
    private Task superTask;
    private String title;
    private String description;
    private double timeInHours = 0.0;
    public Task(Task superTask, String title, String description, double timeInHours)
    {
        this.superTask = superTask;
        this.title = title;
        this.description = description;
        this.timeInHours = timeInHours;
        if(superTask != null)
            superTask.addTimeInHours(timeInHours);
    }
    public Task(Task superTask, String title, String description)
    {
        this.superTask = superTask;
        this.title = title;
        this.description = description;
    }
    public ArrayList<Task> getSubTasks()
    {
        return subTasks;
    }
    public Task getSubTask(int index)
    {
        if(index < subTasks.size())
        {
            return subTasks.get(index);
        }
        return null;
    }
    public Task getSuperTask()
    {
        return superTask;
    }
    public String getTitle()
    {
        return title;
    }
    public String getDescription()
    {
        return description;
    }
    public double getTimeInHours()
    {
        return timeInHours;
    }
    public void setSubTasks(ArrayList<Task> subTasks)
    {
        this.subTasks = subTasks;
    }
    public void setSuperTasks(Task superTask)
    {
        this.superTask = superTask;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setSubTasks(Task subTask, int index)
    {
        subTasks.set(index,subTask);
    }
    public void addSubTasks(Task subTask)
    {
        subTasks.add(subTask);
    }
    public void addSubTasks(Task subTask, int index)
    {
        subTasks.add(index,subTask);
    }
    public void removeSubTask(int index)
    {
        subTasks.remove(index);
    }
    public int getSubTaskSize()
    {
        return subTasks.size();
    }
    public void setTimeInHours(double timeInHours)
    {
        double difference = timeInHours-this.timeInHours;
        this.timeInHours = timeInHours;
        if(superTask != null)
            superTask.addTimeInHours(difference);
    }
    public void addTimeInHours(double timeInHours)
    {
        double tempHours = timeInHours;
        if(subTasks.size() < 1)
        {
            tempHours = timeInHours - this.timeInHours;
            this.timeInHours = 0;
        }
        this.timeInHours+=timeInHours;
        timeInHours = tempHours;
        if(superTask != null)
        {
            superTask.addTimeInHours(timeInHours);
        }
    }
    @Override
    public String toString()
    {
        String output = "";
        output += String.format("Title: %s %nDescription: %s %nTime In Hours: %s %n",title,description,timeInHours);
        output += "\n";
        if(superTask != null)
        {
            output += "Greater Task: " + superTask.getTitle();
        }
        output += "\n";
        output += "Tasks: \n";
        output += traverseTree(this, "", "");
        return output;

        
    }
    public String traverseTree(Task task, String output, String indent)
    {
        output+=indent+"*"+task.getTitle()+"\n";
        indent+="  ";
        ArrayList<Task> sTasks = task.getSubTasks();
        for(int i = 0; i < task.getSubTaskSize(); i++)
        {
            output+=traverseTree(sTasks.get(i),"",indent);
        }
        return output;
    }
    public Task findTask(Task task, String taskName)
    {
        Task retTask = null;
        if(task.getTitle().equals(taskName))
        {
            return task;
        }
        for(Task t : task.getSubTasks())
        {
            retTask = findTask(t,taskName);
        }
        return retTask;

    }
    public String toStringCondensced(String str, Task task)
    {
        if(task != null)
        {
            String superTitle;
            if(task.getSuperTask() == null)
            {
                superTitle = "NULL";
            }
            else
            {
                superTitle = task.getSuperTask().getTitle();
            }
            str += String.format("[%s,%s,%s,%.2f]%n",task.getTitle(),superTitle,task.getDescription(),task.getTimeInHours());
            for(Task subTask : task.getSubTasks())
            {
                str = toStringCondensced(str,subTask);
            }
        }
        return str;
    }
    public static void removeTask(String taskName, Task task)
    {
        for(int i = 0; i < task.getSubTaskSize(); i++)
        {
            if(task.getSubTask(i).getTitle().equals(taskName))
            {
                task.removeSubTask(i);
                return;
            }
            removeTask(taskName,task.getSubTask(i));
        }
    }
    public void removeTask(Task task)
    {
        Task sTask = task.getSuperTask();
        for(int i = 0; i < sTask.getSubTaskSize(); i++)
        {
            if(sTask.getSubTask(i).getTitle().equals(task.getTitle()))
            {
                sTask.removeSubTask(i);
                return;
            }
        }
    }
    public static ArrayList<Plan> findLowestTasks(Task t, ArrayList<Plan> plans)
    {
        if(t.getSubTaskSize() == 0)
        {
            plans.add(new Plan(t.getTimeInHours(),t.getTitle()));
        }
        for(int j = 0; j < t.getSubTaskSize(); j++)
        {
            findLowestTasks(t.getSubTask(j), plans);
        }
        return plans;
    }
    public boolean equals(Task otherTask)
    {
        return (otherTask.getTitle().equals(title) && otherTask.compare(this) && 
        otherTask.getDescription().equals(description) && otherTask.getTimeInHours() == timeInHours);
    }
    private boolean compare(Task otherTask)
    {
        if(otherTask.getSubTasks() == null || otherTask.getSubTasks().isEmpty())
        {
            return (subTasks == null || subTasks.isEmpty());
        }
        ArrayList<Task> otherSub = otherTask.getSubTasks();
        if(otherSub.size() != subTasks.size())
        {
            return false;
        }
        for(int i = 0; i < otherSub.size(); i++)
        {
            if(!otherSub.get(i).equals(subTasks.get(i)))
                return false;
        }
        return true;

    }
}
