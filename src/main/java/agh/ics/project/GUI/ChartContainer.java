package agh.ics.project.GUI;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ChartContainer {

    private final LineChart<Number, Number> counter;
    private final LineChart<Number, Number> energy;
    private final LineChart<Number, Number> lifeTime;
    private final LineChart<Number, Number> children;


    public ChartContainer() {
        energy = getChart("Average Energy");
        lifeTime = getChart("Average LifeTime");
        children = getChart("Average Number of Children");
        counter = getChart("Animals");
        XYChart.Series<Number, Number> grass = new XYChart.Series<>();
        grass.setName("Grass patches");
        counter.getData().add(grass);
    }

    public VBox getCharts() {
        HBox line1 = new HBox(energy, lifeTime);
        HBox line2 = new HBox(children, counter);
        return new VBox(line1, line2);
    }


    public void updateCharts(Map<String, Double> stats) {
        double epoch = stats.get("epoch");
        energy.getData().get(0).getData().add(new XYChart.Data<>(epoch, stats.get("energy")));
        lifeTime.getData().get(0).getData().add(new XYChart.Data<>(epoch, stats.get("lifeTime")));
        children.getData().get(0).getData().add(new XYChart.Data<>(epoch, stats.get("children")));
        counter.getData().get(0).getData().add(new XYChart.Data<>(epoch, stats.get("animalNumber")));
        counter.getData().get(1).getData().add(new XYChart.Data<>(epoch, stats.get("grassNumber")));
    }

    private LineChart<Number, Number> getChart(String stat) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setAnimated(false);
        yAxis.setAnimated(true);

        LineChart<Number, Number> plot = new LineChart<>(xAxis, yAxis);
        plot.setAnimated(true);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(stat);
        plot.getData().add(series);
        plot.setMaxSize(350, 200);
        plot.setMinSize(150, 75);

        return plot;
    }
}
