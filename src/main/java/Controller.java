import com.sun.javafx.tk.FileChooserType;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Controller<fileName> implements Initializable {


    @FXML
    private Button openFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Label readFileLabel;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radiobutton2;
    @FXML
    private Label employeesNOLabel;
    @FXML
    private Label shopsNOLabel;
    @FXML
    private Label workdaysNOLabel;
    @FXML
    private Label readWarnLabel;
    @FXML
    private Pane employeeNOPanel;
    @FXML
    private Pane optimalizationOptionPane;
    @FXML
    private Pane readFileButtonPane;
    @FXML
    private Pane solutionNOPane;

    @FXML
    private TableView tableView;
    @FXML
    private ScrollPane foScrollPane;
    @FXML
    private AnchorPane foAnchorPane;
    @FXML
    private ChoiceBox solutionNOChoiceBox;
    @FXML
    private TextField prefWorkdayMinMin;
    @FXML
    private TextField prefWorkdayMinMax;
    @FXML
    private TextField prefWorkdayMaxMin;
    @FXML
    private TextField prefWorkdayMaxMax;
    @FXML
    private TextField knowledgeLevelMin;
    @FXML
    private TextField knowledgeLevelMax;
    @FXML
    private TextField paymentDemandMin;
    @FXML
    private TextField paymentDemandMax;
    @FXML
    private TextField prefWorkersMinMin;
    @FXML
    private TextField prefWorkersMinMax;
    @FXML
    private TextField prefWorkersMaxMin;
    @FXML
    private TextField prefWorkersMaxMax;
    @FXML
    private TextField expectedKnowledgeMin;
    @FXML
    private TextField expectedKnowledgeMax;
    @FXML
    private TextField workersNOText;
    @FXML
    private TextField shopsNOText;
    @FXML
    private TextField workdaysNOText;
    @FXML
    private Label fillWarnLabel;
    @FXML
    private Button againButton;
    @FXML
    private Pane textFieldPane;
    @FXML
    private Label choosenMethodLabel;
    @FXML
    private Pane readFilePane;
    @FXML
    private ToggleGroup optimalizationOption;
    @FXML
    private Label cantReachFileLabel;

    boolean changedMethod = false;
    String actualMethodId = "1";
    String fileName;
    CalcSchedule schedule;
    Alert a = new Alert(Alert.AlertType.NONE);
    ObservableList<Shops2> studentsModels;
    int method =0;  //0 tudásMax, 1 costMin

    @FXML
    private void informAlert() {
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setContentText("You can reach the developer via email:\n marton.isti.steve@gmail.com");
        a.show();
    }

    private void errorAlert(String text, boolean b) {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText(text);
        if (b) {
            a.setWidth(300);
        }
        a.setResizable(true);
        a.show();
    }

    ;

    public Controller() throws IOException {
    }

    private void startProcess(TableView tableView, Pane solutionPan, ChoiceBox solutionNOChoiceBo, CalcSchedule schedule) {
        tableView.getColumns().clear();
        solutionPan.setVisible(true);
        solutionPan.setDisable(false);

        if (solutionNOChoiceBo.getItems().isEmpty()|| changedMethod) {
            solutionNOChoiceBo.getItems().clear();
            if(actualMethodId.equals("1")) {
                solutionNOChoiceBo.setValue(0);
                int exit = 0;
                for (int i = 0; i < schedule.solutionMap.get(schedule.finalCost).size(); i++) {
                    solutionNOChoiceBo.getItems().add(i + 1);
                    if (exit > 20) {
                        break;
                    }
                    exit++;
                }
            }else{
                solutionNOChoiceBo.setValue(0);
                int exit2 = 0;
                for (int i = 0; i < schedule.solutionMap2.get(schedule.finalPayment).size(); i++) {
                    solutionNOChoiceBo.getItems().add(i + 1);
                    if (exit2 > 20) {
                        break;
                    }
                    exit2++;
                }
            }
        }

        changedMethod = false;
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
            studentsModels2.add(new Shops2(schedule.days.get(i), schedule, 0, i, solutionNumber, actualMethodId));
        }
        return studentsModels2;
    }

    private ArrayList<TableColumn<Shops2, String>> fillColumns(CalcSchedule schedule) {
        ArrayList<TableColumn<Shops2, String>> columns = new ArrayList<>();
        TableColumn<Shops2, String> column2 = new TableColumn<>("Days");
        column2.setCellValueFactory(new PropertyValueFactory<Shops2, String>("days"));
        column2.setStyle("-fx-font-size:15px;");
        columns.add(column2);
        for (int j = 0; j < schedule.shops.size(); j++) {
            String shopName = schedule.shops.get(j);
            TableColumn<Shops2, String> column = new TableColumn<>("Shop " + shopName);
            int finalJ = j;
            column.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper((String) cellData.getValue().getNamesList().get(finalJ).toString().subSequence(1, cellData.getValue().getNamesList().get(finalJ).toString().length() - 1)));
            column.setStyle("-fx-font-size:15px;");
            columns.add(column);
        }
        return columns;
    }

    private void finishProcess(Pane employeeNOPane, Label employeesLabel, Label shopsLabel, Label workdaysLabel, CalcSchedule schedule) {

        employeesLabel.setText(String.valueOf("Number of employees: " + schedule.people.size()));
        shopsLabel.setText(String.valueOf("Number of shops: " + schedule.shops.size()));
        workdaysLabel.setText(String.valueOf("Number of workdays: " + schedule.days.size()));

        solutionNOPane.setVisible(true);
        if(actualMethodId.equals("1")){
            readFileLabel.setText(("Runtime in sec: "+schedule.runTimeSec+"\nAll solutions number: "+schedule.solutionMap.size()+"\nNO of solutions (Knowledge level:solutionNO):\n " + schedule.solutionMap.keys()));
        }else{
            readFileLabel.setText(("NO of solutions (Cost:solutionNO):\n " + schedule.solutionMap2.keys()));
        }
        readFileLabel.setVisible(true);
    }

    private void clearOpenFilePane() {
        readFilePane.setVisible(false);
        readFileLabel.setText("");
        readFileLabel.setId("");
    }

    private void changeMethod() {

        solutionNOPane.setVisible(false);
        tableView.getColumns().clear();
        schedule = null;
        solutionNOChoiceBox.setValue(0);
        fillWarnLabel.setVisible(false);
        solutionNOChoiceBox.getItems().clear();
        againButton.setVisible(false);
        startButton.setVisible(false);
    }

    @FXML
    private void handleChangeMethod(ActionEvent event){
        changedMethod = true;
        RadioButton selectedRadioButton = (RadioButton) optimalizationOption.getSelectedToggle();
        actualMethodId = selectedRadioButton.getId();
        if(!startButton.isVisible()) {
            solutionNOChoiceBox.getItems().clear();
            handleRandomButton(event);
        }

    }

    @FXML
    private void handleCalculationMethodMenu1(ActionEvent event) throws MyException, IOException {
        changeMethod();
        choosenMethodLabel.setText("1) Calculate from File");
        choosenMethodLabel.setId("1");
        readFilePane.setVisible(true);
        textFieldPane.setVisible(false);
        handleOpenFileButton(event);
        handleRandomButton(event);
    }

    @FXML
    private void handleCalculationMethodMenu2(ActionEvent event) throws MyException, IOException {
        choosenMethodLabel.setText("2) Use Built in Data");
        choosenMethodLabel.setId("2");
        textFieldPane.setVisible(false);
        clearOpenFilePane();
        changeMethod();
        handleRandomButton(event);
    }

    @FXML
    private void handleCalculationMethodMenu3(ActionEvent event) {
        choosenMethodLabel.setText("3) Use RandomData");
        choosenMethodLabel.setId("3");
        textFieldPane.setVisible(true);
        clearOpenFilePane();
        changeMethod();
        startButton.setVisible(true);
    }

    private void catchError() {
        startButton.setVisible(false);
        againButton.setVisible(true);
        solutionNOPane.setVisible(false);
        errorAlert(readFileLabel.getText(), false);
    }

    private void changeButtonStyle(String buttonStyle) {
        openFileButton.setStyle(buttonStyle);
        againButton.setStyle(buttonStyle);
        startButton.setStyle(buttonStyle);
    }

    @FXML
    private void changeStyleBright(ActionEvent event) {

        foScrollPane.setStyle("-fx-background-color: #d18e1f");
        changeButtonStyle(" -fx-font-family: \"Comic Sans MS\";\n" +
                "    -fx-font-weight: bold;\n" +
                "   -fx-background-color: #CCFF99;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");
    }

    @FXML
    private void changeStyleGrey(ActionEvent event) {

        foScrollPane.setStyle("-fx-background: #2f4f4f;");
        changeButtonStyle("  -fx-font-weight: bold;\n" +
                "    -fx-background-color: linear-gradient(#61a2b1, #2A5058);\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );");
    }

    @FXML
    private void handleTestButton(ActionEvent event) {

        try {
            Beosztas beosztas = new Beosztas(1);
            schedule = new CalcSchedule(1, beosztas);
            startProcess(tableView, solutionNOPane, solutionNOChoiceBox, schedule);

            int solutionNumber = solutionNumberStart(solutionNOChoiceBox);
            studentsModels = fillStudentsModels(schedule, solutionNumber);
            ArrayList<TableColumn<Shops2, String>> columns = fillColumns(schedule);

            for (int i = 0; i < columns.size(); i++) {
                tableView.getColumns().addAll(columns.get(i));
            }
            tableView.setItems(studentsModels);
            finishProcess(employeeNOPanel, employeesNOLabel, shopsNOLabel, workdaysNOLabel, schedule);
        } catch (MyException e) {
            readFileLabel.setText(e.str1);
            catchError();
        } catch (noSolutionException nse) {
            readFileLabel.setText(nse.str1);
            catchError();
        } catch (OutOfMemoryError ooe) {
            readFileLabel.setText("Too many solutions has the same expediency\n" +
                    "Please narrow the possibilities");
            catchError();

        } catch (Exception ex) {
           catchOther(ex);
        }
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
                solutionNOChoiceBox.setValue(0);
                solutionNOChoiceBox.getItems().clear();
                handleRandomButton(event);
            }
        } catch (Exception e) {
            readFileLabel.setText("please choose a file!");
        }
    }


    @FXML
    private void handleStartButton(ActionEvent event)  {
        if (fileName != null) {
            readWarnLabel.setVisible(false);
            try {
                Beosztas beosztas = new Beosztas(3, readFileLabel.getId());
                schedule = new CalcSchedule(3, beosztas);
                startProcess(tableView, solutionNOPane, solutionNOChoiceBox, schedule);

                int solutionNumber = solutionNumberStart(solutionNOChoiceBox);
                studentsModels = fillStudentsModels(schedule, solutionNumber);
                ArrayList<TableColumn<Shops2, String>> columns = fillColumns(schedule);

                for (int i = 0; i < columns.size(); i++) {
                    tableView.getColumns().addAll(columns.get(i));
                }
                tableView.setItems(studentsModels);
                finishProcess(employeeNOPanel, employeesNOLabel, shopsNOLabel, workdaysNOLabel, schedule);
            } catch (MyException e) {
                readFileLabel.setText(e.str1);
                catchError();
            } catch (noSolutionException nse) {
                readFileLabel.setText(nse.str1);
                catchError();
            } catch (OutOfMemoryError ooe) {
                readFileLabel.setText("Too many solutions has the same expediency\n" +
                        "Please narrow the possibilities");
                catchError();
            } catch (Exception ex) {
               catchOther(ex);
            }
        } else {
            readFileLabel.setText("please choose a *.csv file!");
        }
    }

    private boolean textsNotFilled() {
        boolean filled = (workersNOText.getLength() == 0 || prefWorkdayMinMin.getLength() == 0 ||
                prefWorkdayMinMax.getLength() == 0 || knowledgeLevelMin.getLength() == 0 ||
                prefWorkdayMaxMin.getLength() == 0 || prefWorkdayMaxMax.getLength() == 0 ||
                knowledgeLevelMin.getLength() == 0 || knowledgeLevelMax.getLength() == 0 ||
                paymentDemandMin.getLength() ==0   || paymentDemandMax.getLength() ==0   ||
                prefWorkersMinMin.getLength() == 0 || prefWorkersMinMax.getLength() == 0 ||
                prefWorkersMaxMin.getLength() == 0 || prefWorkersMaxMax.getLength() == 0 ||
                expectedKnowledgeMin.getLength() == 0 || expectedKnowledgeMax.getLength() == 0 ||
                shopsNOText.getLength() == 0 || workdaysNOText.getLength() == 0);

        return filled;
    }

    private boolean textsMinBiggerMax(){
        boolean filled = ( Integer.parseInt( prefWorkdayMinMin.getText()) > Integer.parseInt( prefWorkdayMinMax.getText()) ||
                Integer.parseInt(prefWorkdayMaxMin.getText()) >  Integer.parseInt( prefWorkdayMaxMax.getText()) ||
                Integer.parseInt(knowledgeLevelMin.getText()) >  Integer.parseInt( knowledgeLevelMax.getText()) ||
                Integer.parseInt(paymentDemandMin.getText()) >   Integer.parseInt( paymentDemandMax.getText())  ||
                Integer.parseInt(prefWorkersMinMin.getText()) >  Integer.parseInt(prefWorkersMinMax.getText())  ||
                Integer.parseInt(prefWorkersMaxMin.getText()) >  Integer.parseInt(prefWorkersMaxMax.getText())  ||
                Integer.parseInt(expectedKnowledgeMin.getText()) >  Integer.parseInt(expectedKnowledgeMax.getText()));

        return filled;
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

    private void catchOther(Exception ex) {
        readFileLabel.setText("egyeb hiba");
        startButton.setVisible(false);

        solutionNOPane.setVisible(false);
        ex.printStackTrace();
        String hiba = "egyeb hiba\n";
        StringWriter stackTraceWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTraceWriter));
        String hiba3 = stackTraceWriter.toString();
        String hiba2 = hiba3.substring(0, 200);
        hiba = hiba.concat(hiba2);
        errorAlert(hiba, true);
    }

    @FXML
    private void handleRandomButton(ActionEvent event) {
        if (choosenMethodLabel.getId().equals("3")) {
            if (textsNotFilled()) {
                fillWarnLabel.setText("Please fill all fields!");
                fillWarnLabel.setVisible(true);
            } else if(textsMinBiggerMax()) {
                fillWarnLabel.setText("Maximum can't be lower than Minimum");
                fillWarnLabel.setVisible(true);
            }else {

                try {
                    fillWarnLabel.setVisible((false));
                    textFieldPane.setVisible(false);
                    if (schedule == null || !againButton.isVisible() ) {
                        Beosztas beosztas = new Beosztas(2, Integer.parseInt(shopsNOText.getText()), Integer.parseInt(workersNOText.getText()), Integer.parseInt(workdaysNOText.getText()),
                                Integer.parseInt(prefWorkdayMinMin.getText()), Integer.parseInt(prefWorkdayMinMax.getText()), Integer.parseInt(prefWorkdayMaxMin.getText()),
                                Integer.parseInt(prefWorkdayMaxMax.getText()), Integer.parseInt(knowledgeLevelMin.getText()),Integer.parseInt(knowledgeLevelMax.getText()),
                                Integer.parseInt(paymentDemandMin.getText()), Integer.parseInt(paymentDemandMax.getText()),
                                Integer.parseInt(prefWorkersMinMin.getText()), Integer.parseInt(prefWorkersMinMax.getText()), Integer.parseInt(prefWorkersMaxMin.getText()),
                                Integer.parseInt(prefWorkersMaxMax.getText()), Integer.parseInt(expectedKnowledgeMin.getText()), Integer.parseInt(expectedKnowledgeMax.getText())); //Ide kell 2es mód

                        startButton.setVisible(false);
                        schedule = new CalcSchedule(2, beosztas);
                    }
                    fillWarnLabel.setVisible((false));
                    textFieldPane.setVisible(false);
                    readFileLabel.setText("reading...");

                    startProcess(tableView, solutionNOPane, solutionNOChoiceBox, schedule);

                    int solutionNumber = solutionNumberStart(solutionNOChoiceBox);
                    studentsModels = fillStudentsModels(schedule, solutionNumber);
                    ArrayList<TableColumn<Shops2, String>> columns = fillColumns(schedule);

                    for (int i = 0; i < columns.size(); i++) {
                        tableView.getColumns().addAll(columns.get(i));
                    }
                    tableView.setItems(studentsModels);
                    finishProcess(employeeNOPanel, employeesNOLabel, shopsNOLabel, workdaysNOLabel, schedule);
                    againButton.setVisible(true);
                } catch (MyException e) {

                    readFileLabel.setText(e.str1);
                    againButton.setVisible(true);
                    catchError();

                } catch (NumberFormatException e) {

                    readFileLabel.setText("Please only use numbers!");
                    againButton.setVisible(true);
                    catchError();

                } catch (noSolutionException nse) {
                    readFileLabel.setText(nse.str1);
                    againButton.setVisible(true);
                    catchError();
                } catch (OutOfMemoryError ooe) {
                    readFileLabel.setText("Too many solutions has the same expediency\n" +
                            "Please narrow the possibilities");
                    againButton.setVisible(true);
                    catchError();
                } catch (Exception ex) {
                    againButton.setVisible(true);
                    catchOther(ex);
                }

            }

        } else if (choosenMethodLabel.getId().equals("2")) {
            handleTestButton(event);
        } else if (choosenMethodLabel.getId().equals("1")) {
            handleStartButton(event);
        } else {
            fillWarnLabel.setText("please choose a method");
            fillWarnLabel.setVisible(true);
        }
    }

    @FXML
    private void handleAgainButton(ActionEvent event) {
        solutionNOChoiceBox.setValue(0);
        solutionNOChoiceBox.getItems().clear(); //Ezt valamiért időnként nem futtatja le, minden második alkalommal fut csak le

        textFieldPane.setVisible(true);
        solutionNOPane.setVisible(false);
        againButton.setVisible(false);
        startButton.setVisible(true);
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        cantReachFileLabel.setVisible(false);
        System.out.println("Save");
        List<String> solution = new ArrayList<>();
        solution.add( tableView.getItems().get(0).toString());

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save solution");
            fileChooser.setInitialFileName("schedule.csv");
            //FileChooserType type = new FileType(".csv");
            fileChooser.setInitialDirectory(new File("/"));
    //        fileChooser.setSelectedExtensionFilter(csv);
            File selectedFile = fileChooser.showSaveDialog(null);
            new Kiir(selectedFile, solution, studentsModels, schedule.shops);
        } catch (FileNotFoundException fnfe) {
            cantReachFileLabel.setVisible(true);
            fnfe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            System.out.println("---Choose a File to save in!--");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
    }


}
