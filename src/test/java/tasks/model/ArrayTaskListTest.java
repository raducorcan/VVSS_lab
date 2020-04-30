package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayTaskListTest {
    @Mock private Task task;
    @InjectMocks private ArrayTaskList repo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addTask_1(){
        repo.add(task);
        assertEquals(repo.size(), 1);
        assert repo.getAll().equals(Collections.singletonList(task));
    }

    @Test
    void addTask_2(){
        try {
            repo.add(null);
            assert false;
        } catch (NullPointerException exc){
            assert true;
        }
    }
}