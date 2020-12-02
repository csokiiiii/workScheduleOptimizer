import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListPropertyBase;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;


public class Controller<fileName> implements Initializable {

    @FXML
    private Button test;
    @FXML
    private Button openFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Button startButton11;
    @FXML
    private Label readFileLabel;
    @FXML
    private Label readFileLabel11;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radiobutton2;
    @FXML
    private Label employeesNOLabel;
    @FXML
    private Label employeesNOLabel11;
    @FXML
    private Label employeesNOLabel1;
    @FXML
    private Label shopsNOLabel;
    @FXML
    private Label shopsNOLabel1;
    @FXML
    private Label shopsNOLabel11;
    @FXML
    private Label workdaysNOLabel1;
    @FXML
    private Label workdaysNOLabel;
    @FXML
    private Label workdaysNOLabel11;
    @FXML
    private Label readWarnLabel;
    @FXML
    private Label readWarnLabel11;
    @FXML
    private Pane employeeNOPanel;
    @FXML
    private Pane employeeNOPanel1;
    @FXML
    private Pane employeeNOPanel11;
    @FXML
    private Pane optimalizationOptionPane;
    @FXML
    private Pane readFileButtonPane;
    @FXML
    private Pane solutionNOPane;
    @FXML
    private Pane solutionNOPane1;
    @FXML
    private Pane solutionNOPane11;
    @FXML
    private TableView tableView;
    @FXML
    private TableView tableView1;
    @FXML
    private TableView tableView11;
    @FXML
    private ScrollPane foScrollPane;
    @FXML
    private AnchorPane foAnchorPane;
    @FXML
    private ChoiceBox solutionNOChoiceBox;
    @FXML
    private ChoiceBox solutionNOChoiceBox1;
    @FXML
    private ChoiceBox solutionNOChoiceBox11;
    @FXML
    private TextField prefWorkdayMinMin11;
    @FXML
    private TextField prefWorkdayMinMax11;
    @FXML
    private TextField prefWorkdayMaxMin11;
    @FXML
    private TextField prefWorkdayMaxMax11;
    @FXML
    private TextField knowledgeLevelMin11;
    @FXML
    private TextField knowledgeLevelMax11;
    @FXML
    private TextField prefWorkersMinMin11;
    @FXML
    private TextField prefWorkersMinMax11;
    @FXML
    private TextField prefWorkersMaxMin11;
    @FXML
    private TextField prefWorkersMaxMax11;
    @FXML
    private TextField expectedKnowledgeMin11;
    @FXML
    private TextField expectedKnowledgeMax11;
    @FXML
    private TextField workersNOText11;
    @FXML
    private TextField shopsNOText11;
    @FXML
    private TextField workdaysNOText11;
    @FXML
    private Label fillWarnLabel11;
    @FXML
    private Button againButton11;
    @FXML
    private Pane textFieldPane11;
    @FXML
    private Label searchLabel11;


    String fileName;
    CalcSchedule scheduleRandom;

    public Controller() throws IOException {
    }

    private void startProcess(TableView tableView, Pane solutionPan, ChoiceBox solutionNOChoiceBo, CalcSchedule schedule) {
        tableView.setVisible(true);
        tableView.getColumns().clear();
        solutionPan.setVisible(true);
        solutionPan.setDisable(false);
        if (solutionNOChoiceBo.getItems().isEmpty()) {
            solutionNOChoiceBo.setValue(0);
            int exit = 0;
            for (int i = 0; i < schedule.solutionMap.get(schedule.finalCost).size(); i++) {
                solutionNOChoiceBo.getItems().add(i + 1);
                if (exit > 20) {
                    break;
                }
                exit++;
            }
        }
    }

    private int solutionNumberStart(ChoiceBox solutionBox) {
        int solutionNumber = (Integer) (solutionBox.getValue());
        if (solutionNumber == 0) {
            solutionNumber = 0;
        } else {
            solutionNumber -= 1;
        }
        return solutionNumber;
    }

    private ObservableList<Shops2> fillStudentsModels(CalcSchedule schedule, int solutionNumber) throws MyException {
        ObservableList<Shops2> studentsModels2 = FXCollections.observableArrayList();
        for (int i = 0; i < schedule.days.size(); i++) {
            studentsModels2.add(new Shops2(schedule.days.get(i), schedule, 0, i, solutionNumber));
        }
        return studentsModels2;
    }

    private ArrayList<TableColumn<Shops2, String>> fillColumns(CalcSchedule schedule) {
        ArrayList<TableColumn<Shops2, String>> columns = new ArrayList<>();
        TableColumn<Shops2, String> column2 = new TableColumn<>("Days");
        column2.setCellValueFactory(new PropertyValueFactory<Shops2, String>("days"));
        columns.add(column2);
        for (int j = 0; j < schedule.shops.size(); j++) {
            String shopName = schedule.shops.get(j);
            TableColumn<Shops2, String> column = new TableColumn<>("Shop " + shopName);
            int finalJ = j;
            column.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper((String) cellData.getValue().getNamesList().get(finalJ).toString().subSequence(1, cellData.getValue().getNamesList().get(finalJ).toString().length() - 1)));
            columns.add(column);
        }
        return columns;
    }

    private void closeProcess(Pane employeeNOPane, Label employeesLabel, Label shopsLabel, Label workdaysLabel, CalcSchedule schedule){
        employeeNOPane.setVisible(true);
        employeesLabel.setText(String.valueOf("Number of employees: " + schedule.people.size()));
        shopsLabel.setText(String.valueOf("Number of shops: " + schedule.shops.size()));
        workdaysLabel.setText(String.valueOf("Number of workdays: " + schedule.days.size()));
    }

    @FXML
    private void handleTestButton(ActionEvent event) throws IOException, MyException, noSolutionException {//Ide kellhet trycatch

        Beosztas beosztas = new Beosztas(1);
        CalcSchedule schedule = new CalcSchedule(1, beosztas);
        startProcess(tableView1, solutionNOPane1, solutionNOChoiceBox1, schedule);

        int solutionNumber = solutionNumberStart(solutionNOChoiceBox1);
        ObservableList<Shops2> studentsModels = fillStudentsModels(schedule, solutionNumber);
        ArrayList<TableColumn<Shops2, String>> columns = fillColumns(schedule);

        for (int i = 0; i < columns.size(); i++) {
            tableView1.getColumns().addAll(columns.get(i));
        }
        tableView1.setItems(studentsModels);
    }

    @FXML
    private void handleOpenFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        try {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                readFileLabel.setText("File selected: " + selectedFile.getName());
                readFileLabel.setId(selectedFile.getPath());
                fileName = selectedFile.getPath();
            }
        } catch (Exception e) {
            readFileLabel.setText("please choose a file!");
        }
    }


    @FXML
    private void handleStartButton(ActionEvent event) throws IOException {
        if (fileName != null) {
            readWarnLabel.setVisible(false);
            try {
                Beosztas beosztas = new Beosztas(3, readFileLabel.getId());
                CalcSchedule schedule = new CalcSchedule(3, beosztas);
                startProcess(tableView, solutionNOPane, solutionNOChoiceBox, schedule);

                int solutionNumber = solutionNumberStart(solutionNOChoiceBox);
                ObservableList<Shops2> studentsModels = fillStudentsModels(schedule,solutionNumber);
                ArrayList<TableColumn<Shops2, String>> columns = fillColumns(schedule);

                for (int i = 0; i < columns.size(); i++) {
                    tableView.getColumns().addAll(columns.get(i));
                }
                tableView.setItems(studentsModels);
                closeProcess(employeeNOPanel,employeesNOLabel,shopsNOLabel,workdaysNOLabel,schedule);
            } catch (MyException e) {
                readFileLabel.setText(e.str1);
            } catch (noSolutionException nse) {
                readFileLabel.setText(nse.str1);
            }

        } else {
            readWarnLabel.setVisible(true);
        }
    }

    @FXML
    private void handleRandomButton(ActionEvent event) {
        if (workersNOText11.getLength() == 0 || prefWorkdayMinMin11.getLength() == 0 ||
                prefWorkdayMinMax11.getLength() == 0 || knowledgeLevelMin11.getLength() == 0 ||
                prefWorkdayMaxMin11.getLength() == 0 || prefWorkdayMaxMax11.getLength() == 0 ||
                knowledgeLevelMin11.getLength() == 0 || knowledgeLevelMax11.getLength() == 0 ||
                prefWorkersMinMin11.getLength() == 0 || prefWorkersMinMax11.getLength() == 0 ||
                prefWorkersMaxMin11.getLength() == 0 || prefWorkdayMaxMax11.getLength() == 0 ||
                expectedKnowledgeMin11.getLength() == 0 || expectedKnowledgeMax11.getLength() == 0 ||
                shopsNOText11.getLength() == 0 || workdaysNOText11.getLength() == 0) {
            fillWarnLabel11.setVisible(true);
        } else {
            System.out.println("*****elkezdtem a randomot********************");

            fillWarnLabel11.setVisible((false));
            tableView11.getColumns().clear();
            textFieldPane11.setVisible(false);
            readFileLabel11.setText("reading...");
            foAnchorPane.requestFocus();


            //   Beosztas beosztas = new Beosztas();

            try {
                if (scheduleRandom == null || !againButton11.isVisible()) {
                    searchLabel11.setVisible(true);
                    Beosztas beosztas = new Beosztas(2, Integer.parseInt(shopsNOText11.getText()), Integer.parseInt(workersNOText11.getText()), Integer.parseInt(workdaysNOText11.getText()),
                            Integer.parseInt(prefWorkdayMinMin11.getText()), Integer.parseInt(prefWorkdayMinMax11.getText()), Integer.parseInt(prefWorkdayMaxMin11.getText()),
                            Integer.parseInt(prefWorkdayMaxMax11.getText()), Integer.parseInt(knowledgeLevelMin11.getText()), Integer.parseInt(knowledgeLevelMax11.getText()),
                            Integer.parseInt(prefWorkersMinMin11.getText()), Integer.parseInt(prefWorkersMinMax11.getText()), Integer.parseInt(prefWorkersMaxMin11.getText()),
                            Integer.parseInt(prefWorkersMaxMax11.getText()), Integer.parseInt(expectedKnowledgeMin11.getText()), Integer.parseInt(expectedKnowledgeMax11.getText())); //Ide kell 2es mód

                    startButton11.setVisible(false);
                    scheduleRandom = new CalcSchedule(2, beosztas);
                }

                System.out.println("itt---------------------------");
                if (scheduleRandom.errorMessage != null) {
                    readWarnLabel11.setText(scheduleRandom.errorMessage);
                    readWarnLabel11.setVisible(true);

                } else {
                    if (solutionNOChoiceBox11.getItems().isEmpty()) {
                        solutionNOChoiceBox11.setValue(0);
                        int exit = 0;
                        for (int i = 0; i < scheduleRandom.solutionMap.get(scheduleRandom.finalCost).size(); i++) {
                            solutionNOChoiceBox11.getItems().add(i + 1);
                            if (exit > 20) {
                                break;
                            }
                            exit++;
                        }
                    }
                    solutionNOPane11.setVisible(true);
                    int solutionNumber = (Integer) (solutionNOChoiceBox11.getValue());
                    if (solutionNumber == 0) {
                        solutionNumber = 0;
                    } else {
                        solutionNumber -= 1;
                    }


                    ArrayList<TableColumn<Shops2, String>> columns = new ArrayList<>();
                    ObservableList<Shops2> studentsModels = FXCollections.observableArrayList();
                    for (int i = 0; i < scheduleRandom.days.size(); i++) { //Itt a napok számán kell végigmenni

                        studentsModels.add(new Shops2(scheduleRandom.days.get(i), scheduleRandom, 0, i, solutionNumber));
                    }
                    TableColumn<Shops2, String> column2 = new TableColumn<>("Days");
                    column2.setCellValueFactory(new PropertyValueFactory<Shops2, String>("days"));
                    // column.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Shops2, String>, ObservableValue<String>>) studentsModels.get(0).getNames0());//itt a probléma abban, hogy ugyanazt töltöm be, dinamikusan kellene megkapni azt a names arrayt amelyik ezt a napot tartalmazza
                    columns.add(column2);
                    for (int j = 0; j < scheduleRandom.shops.size(); j++) {
                        String shopName = scheduleRandom.shops.get(j);
                        TableColumn<Shops2, String> column = new TableColumn<>("Shop " + shopName);
                        int finalJ = j;
                        column.setCellValueFactory(cellData ->
                                new ReadOnlyStringWrapper((String) cellData.getValue().getNamesList().get(finalJ).toString().subSequence(1, cellData.getValue().getNamesList().get(finalJ).toString().length() - 1)));
                        columns.add(column);
                    }


                    for (int i = 0; i < columns.size(); i++) {
                        tableView11.getColumns().addAll(columns.get(i));
                    }

                    tableView11.setItems(studentsModels);

                    employeeNOPanel11.setVisible(true);
                    employeesNOLabel11.setText(String.valueOf("Number of employees: " + scheduleRandom.people.size()));
                    shopsNOLabel11.setText(String.valueOf("Number of shops: " + scheduleRandom.shops.size()));
                    workdaysNOLabel11.setText(String.valueOf("Number of workdays: " + scheduleRandom.days.size()));
                }

                tableView11.setVisible(true);
                againButton11.setVisible(true);
                searchLabel11.setVisible(false);

                readFileLabel11.setText(("NO of solutions with expediencies: " + scheduleRandom.solutionMap.keys()));

            } catch (MyException e) {

                readFileLabel11.setText(e.str1);
                againButton11.setVisible(true);
                startButton11.setVisible(false);
                searchLabel11.setVisible(false);
            } catch (noSolutionException nse) {
                readFileLabel11.setText(nse.str1);
                startButton11.setVisible(false);
                againButton11.setVisible(true);
                searchLabel11.setVisible(false);
            } catch (OutOfMemoryError ooe) {
                readFileLabel11.setText("Too many solutions has the same expediency\n" +
                        "Please narrow the possibilities");
                startButton11.setVisible(false);
                againButton11.setVisible(true);
                searchLabel11.setVisible(false);
            } catch (Exception ex) {
                readFileLabel11.setText("egyeb hiba");
                startButton11.setVisible(false);
                againButton11.setVisible(true);
                searchLabel11.setVisible(false);
                ex.printStackTrace();
                //  betenni egy felugró ablakba a stacktracet
            }

        }

    }

    @FXML
    private void handleAgainButton(ActionEvent event) {
        textFieldPane11.setVisible(true);
        tableView11.setVisible(false);
        againButton11.setVisible(false);
        startButton11.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
    }



}
