package agh.ics.project.GUI;

import agh.ics.project.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class App extends Application implements IEngineObserver {

    private final Object PAUSE_KEY_TORUS = new Object();
    private final Object PAUSE_KEY_BORDER = new Object();
    protected final static int CELL_SIZE = 12;
    Map<String, Double> paramMap = new HashMap<>();
    private final HBox root = new HBox();
    private AbstractMap mapT;
    private AbstractMap mapB;
    private final GridPane gridT = new GridPane();
    private final GridPane gridB = new GridPane();
    private EvolutionEngine engineT;
    private EvolutionEngine engineB;
    private final ChartContainer chartContainerT = new ChartContainer();
    private final ChartContainer chartContainerB = new ChartContainer();


    private void loadSimulationArguments() {

        Stage paramStage = new Stage();
        VBox vbox = new VBox();

        Label mapDimensions = new Label("Map dimensions");
        HBox mapDimBox = new HBox();
        mapDimBox.setSpacing(10);
        Label height = new Label("Height: ");
        TextField heightField = new TextField();
        Label width = new Label("Width: ");
        TextField widthField = new TextField();
        mapDimBox.getChildren().addAll(height, heightField, width, widthField);

        Label jungleRatio = new Label("Jungle Ratio");
        TextField jungleRatioField = new TextField();

        Label numberOfAnimals = new Label("Number of animals");
        TextField animalNumberField = new TextField();


        Label startEnergy = new Label("Start energy");
        TextField startEnergyField = new TextField();

        Label eatingGain = new Label("Gained energy from eating");
        TextField eatingGainField = new TextField();

        Label reproductionType = new Label("Type of reproduction");
        HBox reproductionBox = new HBox();
        CheckBox torus = new CheckBox("Magic in Torus");
        CheckBox border = new CheckBox("Magic in Border");
        reproductionBox.setSpacing(10);
        reproductionBox.getChildren().addAll(torus, border);
        Button loadButton = new Button("Load parameter");
        loadButton.setOnAction(action -> {
            try {

                setParamMap(heightField, widthField, jungleRatioField, animalNumberField, startEnergyField,
                        eatingGainField, torus, border);
                System.out.println(paramMap);
                paramStage.close();
            } catch (NumberFormatException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });
        vbox.setSpacing(13);
        vbox.getChildren().addAll(mapDimensions, mapDimBox, jungleRatio, jungleRatioField, numberOfAnimals, animalNumberField,
                startEnergy, startEnergyField, eatingGain, eatingGainField, reproductionType, reproductionBox, loadButton);

        paramStage.setScene(new Scene(vbox));
        paramStage.showAndWait();
    }


    private void setParamMap(TextField heightField, TextField widthField, TextField jungleRatioField, TextField animalNumberField,
                             TextField startEnergyField, TextField eatingGainField, CheckBox normal, CheckBox magic) {
        paramMap.clear();
        paramMap.put("height", Double.parseDouble(heightField.getText()));
        paramMap.put("width", Double.parseDouble(widthField.getText()));
        paramMap.put("jungleRatio", Double.parseDouble(jungleRatioField.getText()));
        paramMap.put("animalNumber", Double.parseDouble(animalNumberField.getText()));
        paramMap.put("startEnergy", Double.parseDouble(startEnergyField.getText()));
        paramMap.put("eatingGain", Double.parseDouble(eatingGainField.getText()));
        paramMap.put("reproductionTorus", normal.isSelected() ? 1.d : 0.d);
        paramMap.put("reproductionBorder", magic.isSelected() ? 1.d: 0.d);
        System.out.println(paramMap);
    }

    private void displayMap(GridPane grid, AbstractMap map) {
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        for (int i = 0; i < map.height + 1; i++) {
            Label label = new Label(Integer.toString(i));
            grid.add(label, map.width + 2, i);
            GridPane.setHalignment(label, HPos.CENTER);
            grid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }

        for (int i = 0; i < map.width + 1; i++) {
            Label label = new Label(Integer.toString(i));
            grid.add(label, i, map.height + 2);
            GridPane.setHalignment(label, HPos.CENTER);
            grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));

        }
        Set<Vector2d> keySet = map.getMapElements().keySet();
        keySet.forEach(key -> fillCell(key, grid, map));
    }

    private void fillCell(Vector2d key, GridPane grid, AbstractMap map) {
        MapCell cell = map.getMapCellAt(key);
        if (!cell.isEmpty() && cell.hasGrass()) {
            VBox box = new BoxElement(cell.getGrass(), 50.0f).getVbox();
            grid.add(box, key.x, key.y);
            GridPane.setHalignment(box, HPos.CENTER);
            grid.setGridLinesVisible(false);
            grid.setGridLinesVisible(true);
        }
        if (!cell.isEmpty() && cell.getBiggestEnergy() != null) {
            VBox box = new BoxElement(cell.getBiggestEnergy(), 50.0f).getVbox();
            grid.add(box, key.x, key.y);
            GridPane.setHalignment(box, HPos.CENTER);
            grid.setGridLinesVisible(false);
            grid.setGridLinesVisible(true);
        }
    }

    private void stopAnimation(Object pauseKey) {
        try {
            pauseKey.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startAnimation(Object pauseKey) {
        pauseKey.notify();
    }

    public VBox setUpBox(GridPane grid, boolean isLeft) {
        Button startT = new Button("Start");
        Button stopT = new Button("Stop");
        startT.setMinSize(80d, 30);
        startT.setOnAction((event) -> startAnimation(isLeft ? PAUSE_KEY_TORUS : PAUSE_KEY_BORDER ));
        stopT.setMinSize(80d, 30);
        stopT.setOnAction((event) -> stopAnimation(isLeft ? PAUSE_KEY_TORUS : PAUSE_KEY_BORDER ));
        HBox buttonBox = new HBox(startT, stopT);
        buttonBox.setSpacing(24);
        buttonBox.setAlignment(Pos.CENTER);
        Label domGenT = new Label("dd");

        VBox charts  = isLeft ? chartContainerT.getCharts(): chartContainerB.getCharts();

        VBox box = new VBox(grid, buttonBox, domGenT, charts);
        grid.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        return box;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        root.setAlignment(Pos.CENTER);
        root.setSpacing(120);
        loadSimulationArguments();

//        this.mapT = new TorusMap(paramMap.get("height").intValue(), paramMap.get("width").intValue(),
//                paramMap.get("jungleRatio").floatValue());
//        this.engineT = new EvolutionEngine(mapT, paramMap.get("animalNumber").intValue(),
//                paramMap.get("startEnergy").intValue(), paramMap.get("reproductionTorus") > 0,
//                paramMap.get("eatingGain").intValue(), 11);
//
//        this.mapB = new TorusMap(paramMap.get("height").intValue(), paramMap.get("width").intValue(),
//                paramMap.get("jungleRatio").floatValue());
//        this.engineB = new EvolutionEngine(mapB, paramMap.get("animalNumber").intValue(),
//                paramMap.get("startEnergy").intValue(), paramMap.get("reproductionBorder") > 0,
//                paramMap.get("eatingGain").intValue(), 11);

        this.mapT = new TorusMap(20, 20, 0.2f);
        this.engineT = new EvolutionEngine(mapT, 20, 20, true, 10, 1);
        this.engineT.addEngineObserver(this);

        this.mapB = new BoundedMap(20, 20, 0.2f);
        this.engineB = new EvolutionEngine(mapB, 20, 20, true, 10, 1);
        this.engineB.addEngineObserver(this);



        Thread engineThreadT = new Thread(engineT);
        engineThreadT.start();
        VBox leftBox = setUpBox(gridT, true);
        displayMap(this.gridT, mapT);
        root.getChildren().add(leftBox);
        HBox.setMargin(leftBox, new Insets(7,10,5,10));




        Thread engineThreadB = new Thread(engineB);
        engineThreadB.start();

        VBox rightBox = setUpBox(gridB, false);
        displayMap(this.gridB, mapB);
        root.getChildren().add(rightBox);
        HBox.setMargin(rightBox, new Insets(7,10,5,10));

        ScrollPane scrollPane = new ScrollPane(root);
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void makeMoves(boolean isTorus, Map<String, Double> stats) {
        Platform.runLater( () -> {
            if (isTorus)  {
                displayMap(this.gridT, mapT);
                chartContainerT.updateCharts(stats);
            } else {
                displayMap(this.gridB, mapB);
                chartContainerB.updateCharts(stats);
            }
        });
    }
}
