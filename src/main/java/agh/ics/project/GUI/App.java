package agh.ics.project.GUI;

import agh.ics.project.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class App extends Application implements IEngineObserver {

    private Label leftDom;
    private Label rightDom;
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


    private void loadSimulationArguments(Stage stage) {

        Stage paramStage = new Stage();
        VBox vbox = new VBox();

        Label mapDimensions = new Label("Map dimensions");
        HBox mapDimBox = new HBox();
        mapDimBox.setSpacing(10);
        Label height = new Label("Height: ");
        TextField heightField = new TextField("20");
        heightField.setMaxSize(80, 20);
        Label width = new Label("Width: ");
        TextField widthField = new TextField("20");
        widthField.setMaxSize(80, 20);
        mapDimBox.getChildren().addAll(height, heightField, width, widthField);

        Label jungleRatio = new Label("Jungle Ratio");
        TextField jungleRatioField = new TextField("0.2");
        jungleRatioField.setMaxSize(80, 20);

        Label numberOfAnimals = new Label("Number of animals");
        TextField animalNumberField = new TextField("25");
        animalNumberField.setMaxSize(80, 20);

        Label startEnergy = new Label("Start energy");
        TextField startEnergyField = new TextField("20");
        startEnergyField.setMaxSize(80, 20);

        Label eatingGain = new Label("Gained energy from eating");
        TextField eatingGainField = new TextField("7");
        eatingGainField.setMaxSize(80, 20);

        Label reproductionType = new Label("Type of reproduction");
        HBox reproductionBox = new HBox();
        CheckBox torus = new CheckBox("Magic in Torus");
        CheckBox border = new CheckBox("Magic in Border");
        reproductionBox.setSpacing(10);
        reproductionBox.getChildren().addAll(torus, border);

        Label moveEnergy = new Label("Move energy");
        TextField moveEnergyField = new TextField("1");
        moveEnergyField.setMaxSize(80, 20);

        Button loadButton = new Button("Load parameter");

        loadButton.setOnAction(action -> {
            try {
                setParamMap(heightField, widthField, jungleRatioField, animalNumberField, startEnergyField,
                        eatingGainField, moveEnergyField, torus, border);
                paramStage.close();
                getEnginesGoing(stage);
            } catch (NumberFormatException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.showAndWait();
            }
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(13);
        vbox.getChildren().addAll(mapDimensions, mapDimBox, jungleRatio, jungleRatioField, numberOfAnimals, animalNumberField,
                startEnergy, startEnergyField, eatingGain, eatingGainField, moveEnergy, moveEnergyField, reproductionType, reproductionBox, loadButton);

        for (Node node: vbox.getChildren()) {
            VBox.setMargin(node, new Insets(0, 8, 2, 8));
        }

        paramStage.setScene(new Scene(vbox));
        paramStage.show();

    }


    private void setParamMap(TextField heightField, TextField widthField, TextField jungleRatioField, TextField animalNumberField,
                             TextField startEnergyField, TextField eatingGainField, TextField moveEnergyField, CheckBox normal, CheckBox magic) {
        paramMap.clear();
        paramMap.put("height", Double.parseDouble(heightField.getText()));
        paramMap.put("width", Double.parseDouble(widthField.getText()));
        paramMap.put("jungleRatio", Double.parseDouble(jungleRatioField.getText()));
        paramMap.put("animalNumber", Double.parseDouble(animalNumberField.getText()));
        paramMap.put("startEnergy", Double.parseDouble(startEnergyField.getText()));
        paramMap.put("eatingGain", Double.parseDouble(eatingGainField.getText()));
        paramMap.put("reproductionTorus", normal.isSelected() ? 1.d : 0.d);
        paramMap.put("reproductionBorder", magic.isSelected() ? 1.d: 0.d);
        paramMap.put("moveEnergy", Double.parseDouble(moveEnergyField.getText()));
        System.out.println(paramMap);
    }

    private void displayMap(GridPane grid, AbstractMap map) {
        if (map instanceof TorusMap && engineT != null) leftDom.setText(engineT.getDominantGenotype().toString());
        if (map instanceof BoundedMap && engineB != null) rightDom.setText(engineB.getDominantGenotype().toString());
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
            grid.add(label, i,map.height + 2);
            GridPane.setHalignment(label, HPos.CENTER);
            grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));

        }
        Set<Vector2d> keySet = map.getMapElements().keySet();
        keySet.forEach(key -> fillCell(key, grid, map));
    }

    private void fillCell(Vector2d key, GridPane grid, AbstractMap map) {
        MapCell cell = map.getMapCellAt(key);
        EvolutionEngine engine = map instanceof TorusMap ? this.engineT: this.engineB;
        if (!cell.isEmpty() && cell.hasGrass()) {
            VBox box = new BoxElement(cell.getGrass(), 50.0f, engine).getVbox();
            grid.add(box, key.x, key.y);
            GridPane.setHalignment(box, HPos.CENTER);
            grid.setGridLinesVisible(false);
            grid.setGridLinesVisible(true);
        }
        if (!cell.isEmpty() && cell.getBiggestEnergy() != null) {
            VBox box = new BoxElement(cell.getBiggestEnergy(), 50.0f, engine).getVbox();
            grid.add(box, key.x, key.y);
            GridPane.setHalignment(box, HPos.CENTER);
            grid.setGridLinesVisible(false);
            grid.setGridLinesVisible(true);
        }
    }

    private void stopAnimation(EvolutionEngine engine) { engine.setPaused(true); }
    private void startAnimation(EvolutionEngine engine) {
        engine.setPaused(false);
    }

    private void saveData(ChartContainer chartContainer, boolean isLeft) throws IOException {
        String mapSymbol = isLeft ? "Torus" : "Border";
        String fileName = "stats_" + mapSymbol + "_" + System.currentTimeMillis() + ".csv";
        File file = new File(fileName);
        file.createNewFile();
        CSVWriter writer = new CSVWriter(file, null);
        writer.writeHeader(new String[] {"epoch", "Animals", "Grass", "Average energy", "Average lifespan", "Average kids"});

        ObservableList<XYChart.Data<Number, Number>> animal = chartContainer.getAnimal();
        ObservableList<XYChart.Data<Number, Number>> grass = chartContainer.getGrass();
        ObservableList<XYChart.Data<Number, Number>> energy = chartContainer.getEnergy();
        ObservableList<XYChart.Data<Number, Number>> children = chartContainer.getChildren();
        ObservableList<XYChart.Data<Number, Number>> lifeSpan = chartContainer.getLifeTime();
        for (int i = 0; i < animal.size(); i++) {
            String[] row = new String[] {
                    animal.get(i).getXValue().toString(),
                    animal.get(i).getYValue().toString(),
                    grass.get(i).getYValue().toString(),
                    energy.get(i).getYValue().toString(),
                    children.get(i).getYValue().toString(),
                    lifeSpan.get(i).getYValue().toString()
            };
            writer.writeData(row);
        }

        String[] lastRow = new String[] {
                animal.get(animal.size() - 1).getXValue().toString(),
                String.valueOf(animal.stream().mapToDouble(data -> data.getYValue().doubleValue()).average().orElse(Double.NaN)),
                String.valueOf(grass.stream().mapToDouble(data -> data.getYValue().doubleValue()).average().orElse(Double.NaN)),
                String.valueOf(energy.stream().mapToDouble(data -> data.getYValue().doubleValue()).average().orElse(Double.NaN)),
                String.valueOf(children.stream().mapToDouble(data -> data.getYValue().doubleValue()).average().orElse(Double.NaN)),
                String.valueOf(lifeSpan.stream().mapToDouble(data -> data.getYValue().doubleValue()).average().orElse(Double.NaN))
        };
        writer.writeData(lastRow);
        writer.close();
    }

    private void showDomAlert(EvolutionEngine engine) {
        if (engine.isPaused()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dominant Genotype");
            alert.setHeaderText("Animals with dominant genotype:\n" + engine.getDominantGenotype().toString());
            alert.setContentText(engine.getDomAnimals().toString());
            alert.showAndWait();
        }
    }

    public VBox setUpBox(GridPane grid, boolean isLeft) {
        Button startT = new Button("Start");
        Button stopT = new Button("Stop");
        Button safeData = new Button("Safe Data");
        startT.setMinSize(80d, 30);
        startT.setOnAction((event) -> startAnimation(isLeft ? this.engineT : this.engineB));
        stopT.setMinSize(80d, 30);
        stopT.setOnAction((event) -> stopAnimation(isLeft ? this.engineT : this.engineB ));
        safeData.setMinSize(80d, 30);
        safeData.setOnAction(event -> {
            try { saveData(isLeft ? chartContainerT : chartContainerB, isLeft);
            } catch (IOException e) {
                e.printStackTrace(); } });
        Button domButton = new Button("Show dominant");
        domButton.setMinSize(80d, 30);
        domButton.setOnAction((event -> showDomAlert(isLeft ? this.engineT : this.engineB)));
        HBox buttonBox = new HBox(startT, stopT, safeData, domButton);
        buttonBox.setSpacing(24);
        buttonBox.setAlignment(Pos.CENTER);
        Label domGenT = new Label();
        if (isLeft) leftDom = domGenT;
        else rightDom = domGenT;

        VBox charts  = isLeft ? chartContainerT.getCharts(): chartContainerB.getCharts();

        VBox box = new VBox(grid, buttonBox, domGenT, charts);
        grid.setAlignment(Pos.CENTER);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void getEnginesGoing(Stage primaryStage) {
        this.mapT = new TorusMap(paramMap.get("height").intValue(), paramMap.get("width").intValue(),
                paramMap.get("jungleRatio").floatValue());
        this.engineT = new EvolutionEngine(mapT, paramMap.get("animalNumber").intValue(),
                paramMap.get("startEnergy").intValue(), (paramMap.get("reproductionTorus") > 0),
                paramMap.get("eatingGain").intValue(), paramMap.get("moveEnergy").intValue());
        this.engineT.addEngineObserver(this);
//
        this.mapB = new BoundedMap(paramMap.get("height").intValue(), paramMap.get("width").intValue(),
                paramMap.get("jungleRatio").floatValue());
        this.engineB = new EvolutionEngine(mapB, paramMap.get("animalNumber").intValue(),
                paramMap.get("startEnergy").intValue(), paramMap.get("reproductionBorder") > 0,
                paramMap.get("eatingGain").intValue(), paramMap.get("moveEnergy").intValue());
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
    public void start(Stage primaryStage) throws Exception {
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);
        loadSimulationArguments(primaryStage);
    }

    @Override
    public void makeMoves(boolean isTorus, Map<String, Double> stats, boolean magicHappened) {
        Platform.runLater( () -> {

            if (isTorus)  {
                displayMap(this.gridT, mapT);
                chartContainerT.updateCharts(stats);
            } else {
                displayMap(this.gridB, mapB);
                chartContainerB.updateCharts(stats);
            }
            showMagicAlert(isTorus, stats.get("epoch").toString() ,magicHappened);

        });
    }

    private void showMagicAlert(boolean isTorus, String epoch, boolean magicHappened) {
        if (magicHappened) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String map = isTorus ? "Torus map" : "Border map";
            alert.setHeaderText("Magic happened on " + map + "in epoch: " + epoch);
            alert.show();
        }
    }
}
