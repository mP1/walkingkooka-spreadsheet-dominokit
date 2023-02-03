package walkingkooka.spreadsheet.dominokit;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.ui.layout.Layout;

public class App implements EntryPoint {

    public void onModuleLoad() {
        Layout.create("Untitled").show();
    }
}
