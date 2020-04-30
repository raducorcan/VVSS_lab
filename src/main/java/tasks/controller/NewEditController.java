package tasks.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TaskIO;
import tasks.services.TasksService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


public class NewEditController {

    private static Button clickedButton;

    private static final Logger log = Logger.getLogger(NewEditController.class.getName());

    public static void setClickedButton(Button clickedButton) {
        NewEditController.clickedButton = clickedButton;
    }

    public static void setCurrentStage(Stage currentStage) {
        NewEditController.currentStage = currentStage;
    }

    private static Stage currentStage;

    private Task currentTask;
    private ObservableList<Task> tasksList;
    private TasksService service;
    private DateService dateService;

    public void setShouldEdit(boolean shouldEdit) {
        this.shouldEdit = shouldEdit;
    }

    private boolean shouldEdit;


    private boolean incorrectInputMade;
    @FXML
    private TextField fieldTitle;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private TextField txtFieldTimeStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TextField txtFieldTimeEnd;
    @FXML
    private TextField fieldInterval;
    @FXML
    private CheckBox checkBoxActive;
    @FXML
    private CheckBox checkBoxRepeated;

    private static final String DEFAULT_START_TIME = "8:00";
    private static final String DEFAULT_END_TIME = "10:00";
    private static final String DEFAULT_INTERVAL_TIME = "0:30";

    public void setTasksList(ObservableList<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public void setService(TasksService service) {
        this.service = service;
        this.dateService = new DateService(service);
    }

    public int getCurrentSize() {
        return service.getCurrentSize();
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
        String id = clickedButton.getId();
        if ("btnNew".equals(id)) {
            initNewWindow("New Task");
        } else if ("btnEdit".equals(id)) {
            initEditWindow("Edit Task");
        }
    }

    @FXML
    public void initialize() {
        log.info("new/edit window initializing");

    }

    private void initNewWindow(String title) {
        currentStage.setTitle(title);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
        checkBoxActive.setSelected(true);
    }

    private void initEditWindow(String title) {
        currentStage.setTitle(title);
        checkBoxActive.setSelected(true);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(DateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));

        if (currentTask.isRepeated()) {
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
            datePickerEnd.setValue(DateService.getLocalDateValueFromDate(currentTask.getEndTime()));
            fieldInterval.setText(service.getIntervalInHours(currentTask));
            txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));
        }
        if (currentTask.isActive()) {
            checkBoxActive.setSelected(true);

        }
    }

    @FXML
    public void switchRepeatedCheckbox(ActionEvent actionEvent) {
        CheckBox source = (CheckBox) actionEvent.getSource();
        if (source.isSelected()) {
            hideRepeatedTaskModule(false);
        } else if (!source.isSelected()) {
            hideRepeatedTaskModule(true);
        }
    }

    private void hideRepeatedTaskModule(boolean toShow) {
        datePickerEnd.setDisable(toShow);
        fieldInterval.setDisable(toShow);
        txtFieldTimeEnd.setDisable(toShow);

        datePickerEnd.setValue(LocalDate.now());
        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
        fieldInterval.setText(DEFAULT_INTERVAL_TIME);
    }

    public void addTask(Task t) {
        if (incorrectInputMade) return;
        if (t.getTitle().length() > 50) {
            throw new RuntimeException("Title too long!");
        }
        if (t.getRepeatInterval() < 0) {
            throw new RuntimeException("Invalid interval");
        }

        if (!shouldEdit) {//no task was chosen -> add button was pressed
            tasksList.add(t);
        } else {
            for (int i = 0; i < tasksList.size(); i++) {
                if (currentTask.equals(tasksList.get(i))) {
                    tasksList.set(i, t);
                }
            }
            currentTask = null;
        }
        TaskIO.rewriteFile(tasksList);
        service.addTask(t);
//        ArrayTaskList temp = new ArrayTaskList();
//        for(Task el: tasksList){
//            temp.add(el);
//        }
//        service.setTasks(temp);
        try {
            Controller.editNewStage.close();
        } catch (Throwable th) {
        }
    }

    void addTask(String title, Date startDate, Date endDate, int interval, boolean isActive) {
        Task tmp;
        if (interval == 0) {
            tmp = new Task(title, startDate, endDate, interval);
        } else {
            tmp = new Task(title, startDate);
        }
        tmp.setActive(isActive);
        this.addTask(tmp);
    }

    @FXML
    public void saveChanges() {
        try {
            collectFieldsData();
        } catch (RuntimeException exc) {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/field-validator.fxml"));
                stage.setScene(new Scene(root, 350, 150));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException ioe) {
                log.error("error loading field-validator.fxml");
            }
        }
    }

    @FXML
    public void closeDialogWindow() {
        Controller.editNewStage.close();
    }

    private void collectFieldsData() {
        incorrectInputMade = false;
        try {
            makeTask();
        } catch (RuntimeException e) {
            incorrectInputMade = true;
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/field-validator.fxml"));
                stage.setScene(new Scene(root, 350, 150));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException ioe) {
                log.error("error loading field-validator.fxml");
            }
        }
    }

    private void makeTask() {
        String newTitle = fieldTitle.getText();
        Date startDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerStart.getValue());//ONLY date!!without time
        Date newStartDate = dateService.getDateMergedWithTime(txtFieldTimeStart.getText(), startDateWithNoTime);
        if (checkBoxRepeated.isSelected()) {
            Date endDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerEnd.getValue());
            Date newEndDate = dateService.getDateMergedWithTime(txtFieldTimeEnd.getText(), endDateWithNoTime);
            int newInterval = service.parseFromStringToSeconds(fieldInterval.getText());
            if (newStartDate.after(newEndDate)) throw new IllegalArgumentException("Start date should be before end");
            Task t = new Task(newTitle, newStartDate, newEndDate, newInterval);
            t.setActive(checkBoxActive.isSelected());
            addTask(t);
//            addTask(newTitle, newStartDate, newEndDate, newInterval, checkBoxActive.isSelected());
        } else {
            addTask(newTitle, newStartDate, newStartDate, 0, checkBoxActive.isSelected());
        }
    }


}
