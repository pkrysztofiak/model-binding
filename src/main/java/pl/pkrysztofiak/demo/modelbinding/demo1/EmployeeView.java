package pl.pkrysztofiak.demo.modelbinding.demo1;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.pkrysztofiak.demo.modelbinding.employee.Employee;
import pl.pkrysztofiak.demo.modelbinding.subscription.ModelSubscription;

public class EmployeeView extends VBox {

    private final TextField surnameTextField = new TextField();
    
    private final TextField ageTextField = new TextField();
    private final Observable<Boolean> ageFocusedObservable = JavaFxObservable.valuesOf(ageTextField.focusedProperty());
    private final Observable<Integer> ageObservable = JavaFxObservable.valuesOf(ageTextField.textProperty()).filter(this::ageValid).map(Integer::valueOf);

    private final Observable<ScrollEvent> ageTextFieldScrollObservable = JavaFxObservable.eventsOf(ageTextField, ScrollEvent.SCROLL);
    
    public EmployeeView(Employee employee) {
        getChildren().setAll(
                new HBox(4, new Label("Surname:"), surnameTextField),
                new HBox(4, new Label("Age:"), ageTextField));

        ageTextFieldScrollObservable.subscribe(this::onScroll);
        
        ModelSubscription<Integer> ageSubscription = new ModelSubscription<>(ageFocusedObservable, ageObservable, employee::setAge, employee.ageObservable, this::setAge);
        
    }
    
    private boolean ageValid(String text) {
        return Pattern.matches("(^[0-9]+$)", text);
    }
    
    private void setAge(int value) {
        Platform.runLater(() -> {
            ageTextField.setText(String.valueOf(value));
        });
    }
    
    private void onScroll(ScrollEvent scrollEvent) {
        String text = ageTextField.getText();
        Integer value = Integer.valueOf(text);
        if (ageValid(text)) {
            if (scrollEvent.getDeltaY() > 0) {
                ageTextField.setText(String.valueOf(++value));
            } else {
                ageTextField.setText(String.valueOf(--value));
            }
        }
    }
}