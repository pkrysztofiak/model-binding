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
    
    public ModelSubscription(Observable<Boolean> viewActiveObservale, Observable<T> viewObservable, Consumer<T> viewEmissionConsumer, Observable<T> modelObservable, Consumer<T> modelEmissionConsumer) {
        viewActiveObservale
        .delay(0, TimeUnit.SECONDS, Schedulers.single())
        .switchMap(active -> active ? Observable.empty() : modelObservable.takeUntil(viewActiveObservale.filter(Boolean.TRUE::equals)))
        .takeUntil(disposeRequest)
        .subscribe(modelEmissionConsumer::accept);
        
        viewActiveObservale.switchMap(active -> active ? viewObservable.skip(1) : Observable.empty())
        .delay(0, TimeUnit.SECONDS, Schedulers.single())
        .takeUntil(disposeRequest)
        .subscribe(viewEmissionConsumer::accept);
    }
    
    public void dispose() {
        disposeRequest.onNext(Optional.empty());
    }
}