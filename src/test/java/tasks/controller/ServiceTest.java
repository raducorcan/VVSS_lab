package tasks.controller;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

//service unit test for adding a new task
class ServiceTest {

    @Mock private ArrayTaskList repo;
    @InjectMocks private TasksService service;

    @InjectMocks
    private NewEditController ctrl;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
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
    void addTask_1(){
        Task t = new Task("title", new Date());
        Mockito.doNothing().when(repo).add(t);
        Mockito.when(repo.size()).thenReturn(1);
        ctrl.addTask(t);

        Mockito.verify(repo, Mockito.times(1)).add(t);
        Mockito.verify(repo, Mockito.times(0)).size();
        assertEquals(ctrl.getCurrentSize(), 1);
        Mockito.verify(repo, Mockito.times(1)).size();
    }

    @Test
    void addTask_2(){
        Task t = new Task(generateString(51), new Date());
        Mockito.doNothing().when(repo).add(t);
        try{
            ctrl.addTask(t);
            assert false;
        } catch (RuntimeException exc){
            assert true;
        }
    }
}