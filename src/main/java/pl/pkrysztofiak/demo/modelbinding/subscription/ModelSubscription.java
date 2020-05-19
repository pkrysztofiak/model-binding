package pl.pkrysztofiak.demo.modelbinding.subscription;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ModelSubscription<T> {

    private final Subject<Optional<Void>> disposeRequest = PublishSubject.create();
    
    public ModelSubscription(Observable<Boolean> viewEmittingObservale, Observable<T> viewObservable, Consumer<T> viewEmissionConsumer, Observable<T> modelObservable, Consumer<T> modelEmissionConsumer) {
        viewEmittingObservale
        .delay(0, TimeUnit.SECONDS, Schedulers.single())
        .switchMap(emitting -> emitting ? Observable.empty() : modelObservable.takeUntil(viewEmittingObservale.filter(Boolean.TRUE::equals)))
        .takeUntil(disposeRequest)
        .subscribe(modelEmissionConsumer::accept);
        
        viewEmittingObservale.switchMap(emitting -> emitting ? viewObservable.skip(1) : Observable.empty())
        .delay(0, TimeUnit.SECONDS, Schedulers.single())
        .takeUntil(disposeRequest)
        .subscribe(viewEmissionConsumer::accept);
    }
    
    public void dispose() {
        disposeRequest.onNext(Optional.empty());
    }
}