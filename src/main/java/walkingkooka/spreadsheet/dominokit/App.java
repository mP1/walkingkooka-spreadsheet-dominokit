package walkingkooka.spreadsheet.dominokit;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.ui.layout.Layout;
import walkingkooka.j2cl.locale.LocaleAware;

import java.util.Locale;

@LocaleAware
public class App implements EntryPoint {

    public void onModuleLoad() {
        Layout.create("Untitled" + Locale.getDefault().getCountry()).show();
    }
}
