import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMenu {


    @SuppressWarnings("ConvertToStringSwitch")
    public static void mainSystem()
    {
        try(Scanner reader = new Scanner(System.in))
        {
            Project theProject;
            while(true)
            {
                System.out.println("Hello and welcome to Project Manager. A simple application to better plan your time.");
                System.out.println("Would you like to open an existing project or start a new one?");
                System.out.println("press 1 for yes and 0 for no");
                String ans = reader.nextLine();
                if(ans.equals("1"))
                {
                    while(true)
                    {
                        System.out.println("What is the file name of the project?");
                        String fileName = reader.nextLine();
                        theProject = Save.readInFile(fileName);
                        if(theProject.equals(new Project("ERRORCODE455444rrr4r4e33e", null, fileName)))
                        {
                            continue;
                        }
                        projectEditor(theProject,reader);
                        break;
                    }
                    break;
                }
                else if(ans.equals("0"))
                {
                    System.out.println("What would you like your project to be called?");
                    String projectName = reader.nextLine();
                    System.out.println("What is the main goal of your project?");
                    String baseNodeName = reader.nextLine();
                    System.out.println("What is the description for your project?");
                    String description = reader.nextLine();
                    theProject = new Project(projectName,new Task(null,baseNodeName,description),projectName);
                    projectEditor(theProject,reader);
                    break;

                }
                else
                {
                    System.out.println("Sorry, your input is invalid, please try again");
                }
                
            }
        }
    }
    public static void projectEditor(Project theProject, Scanner reader)
    {
        while(true)
        {
            System.out.println("\n");
            System.out.println(theProject);
            System.out.println("What would you like to change?");
            System.out.println("Press 1 to rename project");
            System.out.println("2 to add a new task or modify a task");
            System.out.println("3 to exit");
            System.out.println("4 to save the project in a file");
            System.out.println("5 to schedule your project");
            String ans = reader.nextLine();
            boolean endProgram  = false;
            switch(ans)
            {
                case "1": 
                    System.out.println("Type the new name of the project: ");
                    ans = reader.nextLine();
                    theProject.setName(ans);
                    break;
                case "2":
                    changeTask(theProject.getTasks(),reader,theProject);
                    theProject.setTotalTime(theProject.getTasks().getTimeInHours());
                    break;
                case "3": 
                    endProgram = true;
                    break;
                case "4":
                    Save.writeInFile(theProject);
                    break;
                case "5":
                    SchedulerMenu(theProject,reader);
                    break;
                default:
                    System.out.println("Sorry, your response is not valid, please try again (Please press any button to continue)");
                    reader.nextLine();
                    break;
            }
            if (endProgram)
                break;
        }
    }
    public static void changeTask(Task task, Scanner reader, Project theProject)
    {
        while (true) {
            boolean endProgram = false;
            System.out.println("\n");
            System.out.println(task);
            System.out.println("What would you like to change?");
            System.out.println("Press 1 for a name change");
            System.out.println("2 for a description change");
            System.out.println("3 to change a further subtask(Including add a subtask to a subtask)");
            System.out.println("4 to exit to supertask(If on main goal, you will exit to main menu)");
            System.out.println("5 to add a subtask");
            System.out.println("6 to remove this task");
            if(task.getSubTaskSize() == 0)
                System.out.println("7 to change the total time");
            String ans = reader.nextLine();
            switch (ans) {
                case "1":
                    System.out.println("What would you like the new name to be?");
                    ans = reader.nextLine();
                    task.setTitle(ans);
                    break;
                case "2":
                    System.out.println("What would you like the new description to be?");
                    ans = reader.nextLine();
                    task.setDescription(ans);
                    break;
                case "3":
                    ArrayList<Task> subTasks = task.getSubTasks();
                    for(Task subTask : subTasks)
                    {
                        System.out.print(subTask.getTitle() + " ");
                    }   System.out.println();
                    System.out.println("Which one of these subtasks do you want to change?");
                    System.out.println("NOTE: If the subtask does not appear in the above list, it is not a direct subtask. Go to its supertask first");
                    ans = reader.nextLine();
                    for(Task subTask : subTasks)
                    {
                        if(ans.equals(subTask.getTitle()))
                        {
                            changeTask(subTask,reader,theProject);
                            break;
                        }
                    }   
                    break;
                case "4":
                    endProgram = true;
                    break;
                case "5":
                    System.out.println("What would you like the subtask's name to be?");
                    String name = reader.nextLine();
                    System.out.println("What would you like the subtask's description to be?");
                    String description = reader.nextLine();
                    double time = 0;
                    while(true)
                    {
                        System.out.println("What would you like the subtask's time in hours to be?");
                        ans = reader.nextLine();
                        try
                        {
                            time = Math.abs(Double.parseDouble(ans));
                        }
                        catch (NumberFormatException nfe)
                        {
                            System.out.println("Sorry, you have entered an invalid response, please try again");
                            continue;
                        }
                        break;
                    }
                    Task newSub = new Task(task,name,description,time);
                    task.addSubTasks(newSub);
                    theProject.newLowestTask(newSub);
                    break;
                case "6":
                    if (task.equals(theProject.getTasks())) {
                        System.out.println("Sorry, the central goal cannot be removed, try again");
                        break;
                    }
                    theProject.removeSubTask(task);
                    break;
                case "7":
                    if(task.getSubTaskSize() > 0)
                    {
                        System.out.println("Sorry, the time cannot be changed as the time is a sum of the times of this task's subtasks. (Press any button to continue)");
                        reader.nextLine();
                        
                    }
                    else
                    {
                        time = 0;
                        while(true)
                        {
                            System.out.println("What would you like the subtask's time in hours to be?");
                            ans = reader.nextLine();
                            try
                            {
                                time = Math.abs(Double.parseDouble(ans));
                            }
                            catch (NumberFormatException nfe)
                            {
                                System.out.println("Sorry, you have entered an invalid response, please try again");
                                continue;
                            }
                            break;
                            
                        }
                        task.setTimeInHours(time);
                        theProject.changeTimeOnLowestPlan(time, task.getTitle());
                    }
                    break;
                default:
                    System.out.println("Sorry your response is invalid, please try again (Press any button to continue)");
                    reader.nextLine();
                    break;
            }
            if(endProgram)
                break;
        }
    }
    @SuppressWarnings("ConvertToStringSwitch")
    public static void SchedulerMenu(Project theProject, Scanner reader)
    {
        System.out.println();
        Scheduler schedule = theProject.getSchedule();
        while (true) {
            if(schedule == null)
            {
                String startDate;
                String endDate;
                int[] date;
                int[] dateTwo;
                while(true)
                {
                    System.out.println("Please enter your starting date in the form: MM/DD/YYYY");
                    startDate = reader.nextLine();
                    date = Scheduler.convertDate(startDate);
                    if(date == null)
                    {
                        continue;
                    }
                    break;
                }
                while(true)
                {
                    System.out.println("Please enter the day after your ending date in the form: MM/DD/YYYY");
                    endDate = reader.nextLine();
                    dateTwo = Scheduler.convertDate(endDate);
                    if(dateTwo == null)
                    {
                        continue;
                    }
                    break;
                }
                if(!Scheduler.compareDates(startDate,endDate))
                {
                    break;
                }
                theProject.setSchedule(date[0], date[1],date[2], dateTwo[0], dateTwo[1], dateTwo[2]);
                schedule = theProject.getSchedule();
            }
            boolean endProgram = true;
            System.out.println("Hello, welcome to the scheduler menu, you have the following options");
            System.out.println("Type the specific number to choose.");
            System.out.println("1. Schedule your tasks");
            System.out.println("2. block off certain dates");
            System.out.println("3. take off all dates from schedule");
            System.out.println("4. Completely reset including changing the dates");
            System.out.println("5. Display your schedule");
            System.out.println("6. Exit the scheduler menu");
            String ans = reader.nextLine();
            switch (ans) {
                case "1":
                    System.out.println("\n");
                    System.out.println("The tasks will schedule based on your lowest tasks");
                    schedule.scheduleTimes();
                    System.out.println("Task scheduled successfully");
                    break;
                case "2":
                    while(true)
                    {
                        System.out.println("\n");
                        System.out.println("Do you wish to block off certain dates or specific days of the week?");
                        System.out.println("Type 1 to schedule specific days of the week");
                        System.out.println("Type 2 to schedule specific dates");
                        ans = reader.nextLine();
                        if(ans.equals("1"))
                        {
                            System.out.println("\n");
                            System.out.println("Enter the day of the week in its full name");
                            ans = reader.nextLine();
                            schedule.cancelDayofWeek(ans);
                        }
                        else if(ans.equals("2"))
                        {
                            System.out.println("\n");
                            System.out.println("Enter the date in the form MM/DD/YYYY");
                            ans = reader.nextLine();
                            int[] theDate = Scheduler.convertDate(ans);
                            if(theDate == null)
                            {
                                break;
                            }
                            schedule.cancelDay(theDate[0],theDate[1],theDate[2]);
                            
                        }
                        else
                        {
                            System.out.println("Sorry, your answer was invalid");
                        }
                    }   
                    break;
                case "3":
                    System.out.println("\n");
                    schedule.reset();
                    break;
                case "4":
                    System.out.println("\n");
                    schedule = null;
                    break;
                case "5":
                    System.out.println("\n");
                    System.out.println(schedule);
                    break;
                case "6":
                    break;
                default:
                    System.out.println("Your answer was invalid, please try again");
                    break;
            }
            if(endProgram)
                break;
        }
    }

}
