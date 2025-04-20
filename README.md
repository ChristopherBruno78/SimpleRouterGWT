Simple Router for GWT Applications.

```
public class Entry implements EntryPoint {
  final Router router = Router.get();

  @Override
  public void onModuleLoad() {
    router.setNotFoundView(new NotFoundView()); 
    router.mapRoute("/todo", new TodoInterface());
    router.install();
  }
}
```
