Simple Router for GWT Applications.

```
public class Entry implements EntryPoint {
  final Router router = Router.get();

  @Override
  public void onModuleLoad() {
    router.setNotFoundView(new NotFoundView()); 
    router.map("todoApp", new TodoInterface());
    router.install();
  }
}
```
Supports wildcard (*) and path parameters (e.g. /document/:id). 
Path and query parameters can be accessed using the current Route:

For path <i>/document/:id</i> the parameters for the route <i>/document/312342?page=5</i> can be accessed by 
```
Route current = Router.get().currentRoute()

String id = current.getPathParamter("id") // returns "312342"
String sort = current.getQueryParamter("page"); //returns "5"

```
