package pl.pkrysztofiak.demo.modelbinding.demo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.pkrysztofiak.demo.modelbinding.employee.Employee;

public class Demo1App extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Employee employee = new Employee("Kowalsky", 54);
        
        HBox hBox = new HBox(16, new EmployeeView(employee), new EmployeeView(employee));
        
        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();
    }
}