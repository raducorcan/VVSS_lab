package tasks.services;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.controller.NewEditController;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Int_ServiceRepoTaskTest {
    private Task addedTask = new Task(null, new Date());
    private ArrayTaskList repo = new ArrayTaskList();
    private TasksService service = new TasksService(repo);
    private NewEditController ctrl = new NewEditController();

    @BeforeEach
    void setUp() {
        ctrl.setTasksList(FXCollections.observableArrayList());
        ctrl.setService(service);
    }

    private static String generateString(int length) {
        Random random = new Random();
        return random.ints(97, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Test
    void addTask_1() {
        try {
            addedTask.setTitle(generateString(51));
            addedTask.setTime(new Date());
            ctrl.addTask(addedTask);
            assert false;
        } catch (RuntimeException exc) {
            assert true;
        }
        assertEquals(service.getCurrentSize(), 0);
    }

    @Test
    void addTask_2() {
        addedTask.setTitle(generateString(42));
        addedTask.setTime(new Date());
        ctrl.addTask(addedTask);

        assertEquals(ctrl.getCurrentSize(), 1);
    }
}
