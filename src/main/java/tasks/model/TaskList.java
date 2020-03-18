package tasks.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface TaskList extends Iterable<Task>, Serializable {
    void add(Task task);
    boolean remove(Task task);
    int size();
    Task getTask(int index);
    List<Task> getAll();

    default TaskList incoming(Date from, Date to){
        TaskList incomingTasks;
        if (this instanceof ArrayTaskList){
            incomingTasks = new ArrayTaskList();
        }
        else {
            incomingTasks = new LinkedTaskList();
        }

        for(int i = 0; i < this.size(); i++){
            if(getTask(i).nextTimeAfter(from) != null && getTask(i).nextTimeAfter(from).before(to)){
                incomingTasks.add(getTask(i));
                System.out.println(getTask(i).getTitle());
            }
        }
        return incomingTasks;
    }



}