# App Architecture
 
## Architecture Patterns

The architecture in the Demo app is not 100% consistent at the moment, but the general idea is MVP.

The `DemoBaseView` and `DemoBasePresenter` serve as the base presenter objects.

The View in MVP can be an `Activity`, `Android View`, or `Fragment` as long as it implements the View 
interface you create, and calls the required Presenter events. In the example below we will treat the 
`Activity` as the View, however throughout the app currently, all 3 methods are used.

### New Feature Example

For an example, let's consider some new feature we want to make,

1) Create a new package named for your feature, e.g. `com.bazaarvoice.bvsdkdemoandroid.starcolor`
2) Create a new MVP contract for your feature, extending the `DemoBaseView` and `DemoBasePresenter` 
```java
public interface DemoFeatureContract {
  interface Presenter extends DemoBasePresenter {
    void userTappedFooButton();
  }
  interface View extends DemoBaseView<Presenter> {
    void showFooModel(FooModel fooModel);
  }
}
```
3) Create a new Presenter implementing the interface you just defined, e.g. 
```java
public class DemoFeaturePresenter implements DemoFeatureContract.Presenter {
  private final DemoFeatureContract.View view;
  private Task fooModelTask;
  
  public DemoFeaturePresenter(DemoFeatureContract.View view) {
    this.view = view;
  }
  
  @Override
  public void userTappedFooButton() {
    doSomething();
  }
  
  @Override
  public void onStart() {
    fooModelTask = createFooModelTask();
    fooModelTask.enqueue(fooModel -> view.showFooModel(fooModel));
  }
  
  @Override
  public void onStop() {
    fooModelTask.cancel();
  }
  
  @Inject
  void setupListeners() {
    // Don't forget this, or the presenter will never be hooked up to this presenter
    // and you will get a NPE
    view.setPresenter(this);
  }
}
```
4) Create a new Dagger Module class that provides the dependencies relevant to this presenter, e.g.
```java
@Module
public class DemoFeaturePresenterModule {
  private final DemoFeatureContract.View view;
  
  public DemoFeaturePresenterModule(DemoFeatureContract.View view) {
    this.view = view;
  }
  
  @Provides @DemoActivityScope
  public DemoFeatureContract.View provideView() {
    return view;
  }

  @Provides @DemoActivityScope
  public DemoFeatureContract.Presenter providePresenter() {
    return new DemoFeaturePresenter(view);
  }
}
```
5) Create a new Dagger Component class that defines the injection points, e.g. 
```java
@DemoActivityScope @Component(dependencies = DemoAppComponent.class, modules = {DemoFeaturePresenterModule.class})
public interface DemoFeatureComponent {
  void inject(DemoFeatureActivity activity);
}
```
6) Create a new Activity named for your feature, e.g.
```java
public class DemoFeatureActivity extends AppCompatActivity implements DemoFeatureContract.View {
  @Inject DemoFeatureContract.Presenter presenter;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_feature);
    Butterknife.bind(this);
    DaggerDemoFeatureComponent.builder()
        .appComponent(App.getAppComponent(this))
        .demoFeaturePresenterModule(new DemoFeaturePresenterModule(this))
        .build()
        .inject(this);
    presenter.create();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    presenter.resume();
  }

  @Override
  protected void onPause() {
    presenter.pause();
    super.onPause();
  }
  
  @OnClick(R.id.fooButton) // Butterknife helper annotation for click listeners
  public void userTappedFooButton() {
    presenter.userTappedFooButton();
  }
  
  @Override
  public void setPresenter(DemoFeatureContract.Presenter presenter) {
    // This comes from the DemoBaseView interface, and is required for the View to wire up the presenter
    this.presenter = presenter;
  }
}
```

Now you are all setup, and can start adding more dependencies to the `DemoFeaturePresenter` such as the 
 `BVConversationsClient`, `Picasso`, etc. By persisting data in onPause we can ensure we save state. By 
 cancelling requests in onPause, we can avoid calling setters on Android View classes when the Activity 
 is dying.
 
### Router

The class `DemoRouter` helps us with deeplinking to different parts of the app. It requires Application Context 
to launch Activities, and therefore it is exposed by the `DemoAppComponent` for all classes to be able to inject. 

An example to transition to the `DemoFancyProductDetailActivity` would be, 
```java
public class DemoFeaturePresenter extends DemoFeatureContract.Presenter {
  private final Router router; // injected through constructor
  
  @Override
  public void userTappedFooButton() {
    router.transitionToProductDetail("productId");
  }
}
```

This allows the presenters to interact with the Android Intent system without knowing about Android.

## App Entry Points

### Fancy Demo App

The purpose of this app is to recreate an example clients app. It can be started by launching the 
`DemoHomeActivity`.

### Kitchen Sink App

The purpose of this app is to simply serve as a sandbox for the SDK developers, and to see an isolated 
example of a certain feature. It can be started by launching the `DemoMainActivity`.

## Dependencies

* BVSDK - 🙂
* Picasso - Image loading
* Dagger 2 - Dependency Injection
* Butterknife - findViewById replacement, although may prove unnecessary with Kotlin in the future
* Pretty Time - Turning Date/Time strings into friendly strings
* OkHttp - Customized instance provided to BVSDK, allows intercepting/modifying requests/responses
* Stetho - An OkHttp Interceptor to aid in debugging
* GSON - Parsing JSON files/responses
* Process Phoenix - For restarting the app instance to support the multi-tenant concept of the app, while maintaining immutability 
* Crashlytics - Bug tracking when this app is live. Sometimes a demo app is distributed internally
* Leakcanary - Memory leak tracker
* Support lib stuff 
* Play services stuff
* Dexcount plugin - Generates pie graph of the apk dependencies and their method counts everytime we build
* JUnit - If we started adding demo app tests

