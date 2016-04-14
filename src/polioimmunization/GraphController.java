/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polioimmunization;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 *
 * @author dwheadon
 */
public class GraphController implements Initializable {
    
    @FXML
    private BarChart chart;
    
    @Override
    public void initialize(URL purl, ResourceBundle rb) {
        String s = "http://apps.who.int/gho/athena/data/GHO/WHS4_544.json?profile=simple&filter=YEAR:1980";
        URL url = null;
        try {
            url = new URL(s);
        } catch (Exception e) {
            System.out.println("Improper URL " + s);
            System.exit(-1);
        }
     
        // read from the URL
        Scanner scan = null;
        try {
            scan = new Scanner(url.openStream());
        } catch (Exception e) {
            System.out.println("Could not connect to " + s);
            System.exit(-1);
        }
        
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine() + "\n";
        }
        scan.close();

        Gson gson = new Gson();
        PolioDataSet ds = gson.fromJson(str, PolioDataSet.class);
        ds.dedup();
        PolioDataPoint[] pdp = ds.getDataPoints();
        XYChart.Series<String, Number> immuneSeries = new XYChart.Series();
        immuneSeries.setName("% Immunized");
        for (PolioDataPoint dp : ds.getDataPoints()) {
            System.out.println(dp);
            immuneSeries.getData().add(new XYChart.Data(dp.getCountry(), dp.getValue()));
        }
        chart.getData().add(immuneSeries);
    }    
    
}