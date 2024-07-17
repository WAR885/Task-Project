import java.time.LocalDate;
import java.util.ArrayList;
public class Scheduler 
{
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<LocalDate> diff;
    private ArrayList<LocalDate> dates;
    private ArrayList<Plan> allocatedPlans;
    private ArrayList<Plan> unallocatedPlans;
    public Scheduler(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay, ArrayList<Plan> plans)
    {
        startDate = LocalDate.of(startYear, startMonth, startDay);
        endDate = LocalDate.of(endYear, endMonth, endDay);
        this.diff = new ArrayList<LocalDate>();
        this.dates = new ArrayList<LocalDate>();
        calculateDifference();
        this.allocatedPlans = new ArrayList<Plan>();
        this.unallocatedPlans = new ArrayList<Plan>();
        for(Plan plan : plans)
        {
            unallocatedPlans.add(plan);
        }
    }
    public String getStartDate()
    {
        return startDate.getMonth() + "/" + startDate.getDayOfMonth() + "/" + startDate.getYear();
    }
    /* public void setStartDate(int startYear, int startMonth, int startDay)
    {
        startDate = LocalDate.of(startYear, startMonth, startDay);
        calculateDifference();
    }
    */
    public String getEndDate()
    {
        return endDate.getMonth() + "/" + endDate.getDayOfMonth() + "/" + endDate.getYear();
    }
    /* public void setEndDate(int endYear, int endMonth, int endDay)
    {
        startDate = LocalDate.of(endYear, endMonth, endDay);
        calculateDifference();
    }
    */
    private void calculateDifference()
    {
        LocalDate tempDate = startDate;
        while(!tempDate.equals(endDate))
        {
            dates.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        for(LocalDate date : dates)
        {
            diff.add(date);
        }

    }
    public void reset()
    {
        dates = new ArrayList<LocalDate>();
        for(LocalDate date : diff)
        {
            dates.add(date);
        }
        unallocateDates();
    }
    public void unallocateDates()
    {
        for(int i = 0; i < allocatedPlans.size(); i++)
        {
            String lastPlan = unallocatedPlans.get(unallocatedPlans.size()-1).getTaskName();
            double lastTime = unallocatedPlans.get(unallocatedPlans.size()-1).getTimeInHours();
            if(!allocatedPlans.get(i).getTaskName().equals(lastPlan))
            {
                Plan newPlan = new Plan(allocatedPlans.get(i).getTimeInHours(),allocatedPlans.get(i).getTaskName());
                unallocatedPlans.add(newPlan);
            }
            else
            {
                unallocatedPlans.get(unallocatedPlans.size()-1).setTimeInHours(lastTime+allocatedPlans.get(i).getTimeInHours());
            }
            allocatedPlans.remove(i);
            i--;
        }
    }
    public void addPlan(Plan plan)
    {
        unallocatedPlans.add(plan);
    }
    public void addAllocatedPlan(Plan plan)
    {
        allocatedPlans.add(plan);
    }
    public static int[] convertDate(String date)
    {
        if(!date.matches("\\d{1,2}/\\d{1,2}/\\d{4}"))
        {
            System.out.println("Sorry, the date is not in the correct format, please try again");
            return null;
        }
        String[] arr = date.split("/");
        int month = Integer.parseInt(arr[0]);
        int day = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2]);
        if(month < 1 || month > 12 || day < 1 || day > 31 || year < 2000 || year > 2050)
        {
            System.out.println("Sorry, the date is either too long or invalid, please try again");
            return null;
        }
        int[] newArr = {year, month, day};
        return newArr;
    }
    public static boolean compareDates(String startDate, String endDate)
    {
        int[] startArr = convertDate(startDate);
        int[] endArr = convertDate(endDate);
        int i = 0;
        for(; i < 3; i++)
        {
            if(startArr[i] > endArr[i])
            {
                System.out.println("Sorry, your start date is bigger than your end date, try again");
                return false;
            }
            else if(startArr[i] == endArr[i])
            {
                continue;
            }
            break;
        }
        if(i == 3)
        {
            System.out.println("Sorry, your start date is the same as your end date, try again");
            return false;
        }
        return true;
    }
    public boolean cancelDayofWeek(String dayOfWeek)
    {
        String[] daysOfWeek = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        boolean validDay = false;
        for(String day : daysOfWeek)
        {
            if(day.toLowerCase().equals(dayOfWeek.toLowerCase()))
            {
                validDay = true;
                break;
            }
        }
        if(!validDay)
        {
            System.out.println("Sorry, the day of the week is not valid, try again");
            return validDay;
        }
        for(int j = 0; j < dates.size(); j++)
        {
            if(dates.get(j).getDayOfWeek().toString().equals(dayOfWeek.toUpperCase()))
            {
                dates.remove(j);
                j--;
            }
        }
        return validDay;
    }
    public void cancelDay(int year, int month, int day)
    {
        for(int i = 0; i < dates.size(); i++)
        {
            if(year == dates.get(i).getYear() && month == dates.get(i).getMonthValue() && day == dates.get(i).getDayOfMonth())
            {
                dates.remove(i);
                return;
            }
        }
        System.out.println("Date was not found");
        return;
    }
    public void deletePlan(Plan plan)
    {
        String name = plan.getTaskName();
        for(int i = 0; i < allocatedPlans.size(); i++ )
        {
            if(allocatedPlans.get(i).getTaskName().equals(name))
            {
                allocatedPlans.remove(i);
                i--;
            }
        }
        for(int i = 0; i < unallocatedPlans.size(); i++ )
        {
            if(unallocatedPlans.get(i).getTaskName().equals(name))
            {
                unallocatedPlans.remove(i);
                i--;
            }
        }
    }
    public void scheduleTimes()
    {
        if(allocatedPlans.size() != 0)
        {
            unallocateDates();
        }
        double totalTime = 0;
        for(int i = 0; i < unallocatedPlans.size(); i++)
        {
            totalTime += unallocatedPlans.get(i).getTimeInHours();
        }
        int days = dates.size();
        double averageTime = totalTime/days;
        double currTime = averageTime;
        double timeForTask = unallocatedPlans.get(0).getTimeInHours();
        String taskName = unallocatedPlans.get(0).getTaskName();
        for(int k = 0; k < dates.size() && 0 < unallocatedPlans.size();)
        {   
            if(currTime > timeForTask)
            {
                allocatedPlans.add(new Plan(dates.get(k),timeForTask,taskName));
                currTime-=timeForTask;
                unallocatedPlans.remove(0);
                if(0 < unallocatedPlans.size() && k < dates.size())
                {
                    timeForTask = unallocatedPlans.get(0).getTimeInHours();
                    taskName = unallocatedPlans.get(0).getTaskName();
                }
            }
            else if(currTime < timeForTask)
            {
                allocatedPlans.add(new Plan(dates.get(k),currTime,taskName));
                timeForTask-=currTime;
                k++;
                if(0 < unallocatedPlans.size() && k < dates.size())
                {
                    currTime = averageTime;
                }
            }
            else if(currTime == timeForTask)
            {
                allocatedPlans.add(new Plan(dates.get(k),currTime,taskName));
                k++;
                unallocatedPlans.remove(0);
                if(0 < unallocatedPlans.size() && k < dates.size())
                {
                    timeForTask = unallocatedPlans.get(0).getTimeInHours();
                    taskName = unallocatedPlans.get(0).getTaskName();
                    currTime = averageTime;
                }
            }
        }
    }
    public String toString()
    {
        String s = "Scheduled Plans: \n";
        for(Plan p : allocatedPlans)
        {
            s += p.toString() + "\n";
        }
        s+="\n";
        if(unallocatedPlans.size() != 0)
        {
            s+="Unscheduled Plans: \n";
            for(Plan u : unallocatedPlans)
            {
                s += u.toString() + "\n";
            }
        }
        return s;
    }
    public String condenscedToString()
    {
        String output = "";
        output += String.format("[%d/%d/%d,%d/%d/%d]%n",startDate.getMonthValue(),startDate.getDayOfMonth(),startDate.getYear(),endDate.getMonthValue(),endDate.getDayOfMonth(),endDate.getYear());
        for(int i = 0; i < allocatedPlans.size(); i++)
        {
            if(i % 100 == 0)
                output += "\n";
            output += allocatedPlans.get(i) + "|";
        }
        for(int i = 0; i < unallocatedPlans.size(); i++)
        {
            if(i % 100 == 0)
                output += "\n";
                output += unallocatedPlans.get(i) + "|";
        }
        return output;
    }


}
