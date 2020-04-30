package tasks.controller;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

class NewEditControllerTest {
    private NewEditController ctrl;
    private TasksService service = new TasksService(new ArrayTaskList());
    private ObservableList<Task> tasksList = service.getObservableList();

    private static String generateString(int length) {
        Random random = new Random();
        return random.ints(97, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static Stream<Arguments> longTitleProvider() {
        String generatedString = generateString(55);
        return Stream.of(
                Arguments.of(generatedString)
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        this.ctrl = new NewEditController();
        ctrl.setShouldEdit(false);
        ctrl.setService(service);
        ctrl.setTasksList(tasksList);
    }


    @ParameterizedTest
    @MethodSource("longTitleProvider")
    @NullSource
    void addTask_EC_Title_Invalid(String title) {
        try {
            ctrl.addTask(title, new Date(), new Date(), 0, true);
            assert (false);
        } catch (RuntimeException exc) {
            assert (true);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd"})
    @EmptySource
    void addTask_EC_Title_Valid(String title) {
        try {
            ctrl.addTask(title, new Date(), new Date(), 0, true);
            assert (true);
        } catch (RuntimeException exc) {
            assert (false);
        }
    }

    @Test
    void addTask_EC_Interval_Invalid() {
        try {
            ctrl.addTask("mytask", new Date(), new Date(), -1, true);
            assert (false);
        } catch (RuntimeException exc) {
            assert (true);
        }
    }

    @Test
    void addTask_EC_Interval_Valid() {
        try {
            ctrl.addTask("mytask", new Date(), new Date(), 0, true);
            assert (true);
        } catch (RuntimeException exc) {
            assert (false);
        }
    }

    @Test
    void addTask_BVA_Interval_Valid() {
        try {
            ctrl.addTask("mytask", new Date(), new Date(), 1, true);
            assert (true);
        } catch (RuntimeException exc) {
            assert (false);
        }
    }

    @Test
    void addTask_BVA_Interval_Invalid() {
        try {
            ctrl.addTask("mytask", new Date(), new Date(), -1, true);
            assert (false);
        } catch (RuntimeException exc) {
            assert (true);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {49, 50})
    void addTask_BVA_Title_Valid(int length) {
        try {
            String title = generateString(length);
            ctrl.addTask(title, new Date(), new Date(), 0, true);
            assert (true);
        } catch (RuntimeException exc) {
            assert (false);
        }
    }

    @Test
    void addTask_BVA_Title_Invalid() {
        try {
            String title = generateString(51);
            ctrl.addTask(title, new Date(), new Date(), 0, true);
            assert (false);
        } catch (RuntimeException exc) {
            assert (true);
        }
    }

}