package pl.pkrysztofiak.demo.modelbinding.employee;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Employee {

    private final ObjectProperty<String> surnameProperty = new SimpleObjectProperty<>(); 
    public final Observable<String> surnameObservable = JavaFxObservable.valuesOf(surnameProperty);
    
    private final ObjectProperty<Integer> ageProperty = new SimpleObjectProperty<>();
    public final Observable<Integer> ageObservable = JavaFxObservable.valuesOf(ageProperty);
    
    public Employee(String surname, int age) {
        surnameProperty.set(surname);
        ageProperty.set(age);
    }
    
    public void setSurname(String surname) {
        surnameProperty.set(surname);
    }
    
    public void setAge(int age) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ageProperty.set(age);
    }

    public Integer getAge() {
        return ageProperty.get();
    }
}