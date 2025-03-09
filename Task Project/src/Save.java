import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Save {
    static Project readInFile(String fileName)
    {
        Project theProject = new Project("", null, fileName);
        try(BufferedReader file = new BufferedReader(new FileReader(fileName)))
        {
            ArrayList<String> allLines = new ArrayList<>();
            while(file.read()!=-1)
            {
                allLines.add("[" + file.readLine());
            }
            if(allLines.size() < 2)
            {
                System.out.println("Sorry, this file doesn't match the supported format");
                return null;
            }
            for(int j = 0; j < allLines.size(); j++)
            {
                if (allLines.get(j).matches("\\[.+,.+,.+,\\d+\\.\\d+\\]"))
                {
                    allLines.set(j,allLines.get(j).substring(1,allLines.get(j).length()-1));
                    String[] info = allLines.get(j).split(",");
                    Task t = new Task(null,info[0],info[2]);
                    t.setTimeInHours(Double.parseDouble(info[3]));
                    if(info[1].equals("NULL"))
                    {
                        theProject.setTasks(t);
                    }
                    else
                    {
                        System.out.println(theProject.getTasks());
                        Task superTask = theProject.getTasks().findTask(theProject.getTasks(), info[1]);
                        superTask.addSubTasks(t);
                        t.setSuperTasks(superTask);
                    }
                }
                else if (allLines.get(j).matches("\\[.+,\\d+\\.\\d+\\]"))
                {
                    allLines.set(j,allLines.get(j).substring(1,allLines.get(j).length()-1));
                    String[] info = allLines.get(j).split(",");
                    theProject.setName(info[0]);
                    theProject.setTotalTime(Double.parseDouble(info[1]));
                }
                else if(allLines.get(j).matches("\\[\\d{1,2}/\\d{1,2}/\\d{4},\\d{1,2}/\\d{1,2}/\\d{4}\\]"))
                {
                    String[] dates = allLines.get(j).substring(1,allLines.get(j).length()-1).split(",");
                    int[] begDate = Scheduler.convertDate(dates[0]);
                    int[] endDate = Scheduler.convertDate(dates[1]);
                    theProject.setSchedule(begDate[0],begDate[1],begDate[2],endDate[0],endDate[1],endDate[2]);
                }
                else if(allLines.get(j).matches("(\\[.+,\\d\\.\\d+,\\d{1,2}/\\d{1,2}/\\d{4}\\]\\|)+"))
                {
                    if(theProject.getSchedule() == null)
                    {
                        System.out.println("Sorry, this file does not match the supported format");
                        return null;
                    }
                    String[] plansStr = allLines.get(j).split("\\|");
                    for(int i = 0; i < plansStr.length; i++)
                    {
                        plansStr[i] = plansStr[i].substring(1,plansStr[i].length()-2);
                        String[] sub = plansStr[i].split(",");
                        String planName = sub[0];
                        double time = Double.parseDouble(sub[1]);
                        int[] dateInfo = Scheduler.convertDate(sub[2]);
                        Plan p = new Plan(time,planName);
                        p.setDate(dateInfo[0],dateInfo[1],dateInfo[2]);
                        theProject.getSchedule().addAllocatedPlan(p);
                    }
                }
                else if(allLines.get(j).matches("(\\[.+,\\d\\.\\d+,null\\]\\|)+"))
                {
                    if(theProject.getSchedule() == null)
                    {
                        System.out.println("Sorry, this file does not match the supported format");
                        return null;
                    }
                    String[] plansStr = allLines.get(j).split("\\|");
                    for(int i = 0; i < plansStr.length; i++)
                    {
                        plansStr[i] = plansStr[i].substring(1,plansStr[i].length()-2);
                        String[] sub = plansStr[i].split(",");
                        String planName = sub[0];
                        double time = Double.parseDouble(sub[1]);
                        Plan p = new Plan(time,planName);
                        theProject.getSchedule().addAllocatedPlan(p);
                    }
                }
                else
                {
                    System.out.println("Sorry, this file does not match the supported format");
                    return null;
                }
            }
            theProject.findlowestTasks();
        }
        catch(FileNotFoundException flne)
        {
            System.out.println("Sorry, the file was not found");
            theProject = new Project("ERRORCODE455444rrr4r4e33e", null, fileName);
            //flne.printStackTrace();
        }
        catch(IOException io)
        {
            System.out.println("Sorry, an error occured while opening the file");
            theProject = new Project("ERRORCODE455444rrr4r4e33e", null, fileName);
            //io.printStackTrace();
        }
        return theProject;
    }
    static void writeInFile(Project theProject)
    {
        try(BufferedWriter file = new BufferedWriter(new FileWriter((theProject.getFileName()))))
        {
            File createFile = new File(theProject.getFileName());
            createFile.createNewFile();
            String projectStr = String.format("[%s,%.2f]%n",theProject.getName(),theProject.getTotalTime());
            projectStr += theProject.getTasks().toStringCondensced(projectStr,theProject.getTasks());
            if(theProject.getSchedule() != null)
            {
                projectStr+=(theProject.getSchedule().condenscedToString());
            }
            file.write(projectStr);
        }
        catch(IOException e)
        {
            System.out.println("Sorry, an error occured while saving your work");
            e.printStackTrace();
        }

    }
}
