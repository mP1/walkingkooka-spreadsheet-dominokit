/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.HashChangeEvent;
import elemental2.dom.History;
import jsinterop.base.Js;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import java.util.Optional;

@LocaleAware
public class App implements EntryPoint, AppContext {

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final Layout layout = Layout.create();

    public void onModuleLoad() {
        this.setupHistoryListener();

        this.layout.show();
        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);

        this.fireInitialHashToken();
    }

    private void setupHistoryListener() {
        DomGlobal.self.addEventListener(
                "hashchange",
                event -> this.onHashChange(
                        Js.cast(event)
                )
        );
    }

    private void fireInitialHashToken() {
        this.onHashChange(null);
    }

    private void onHashChange(final HashChangeEvent event) {
        // remove the leading hash if necessary.
        String hash = DomGlobal.location.hash;
        if (hash.startsWith("#")) {
            hash = hash.substring(1);
        }

        try {
            final Optional<HistoryToken> previousToken = this.previousToken;
            final Optional<HistoryToken> maybeToken = HistoryToken.parse(UrlFragment.parse(hash));
            if (false == previousToken.equals(maybeToken)) {
                this.previousToken = maybeToken;

                if (maybeToken.isPresent()) {
                    final HistoryToken token = maybeToken.get();
                    DomGlobal.location.hash = "#" + token.urlFragment().toString();

                    token.onHashChange(this);
                } else {
                    DomGlobal.location.hash = "";
                }
            }
            debug("onHashChange from " + toString(previousToken) + " to " + toString(maybeToken));
        } catch (final Exception e) {
            error(e.getMessage());
        }
    }

    /**
     * Helper which pretty's a {@link HistoryToken} for logging.
     */
    private String toString(final Optional<HistoryToken> token) {
        String toString = "<nothing>";
        if (token.isPresent()) {
            toString = token.get().getClass().getSimpleName() + " " + token.get();
        }
        return toString;
    }

    /**
     * Maybe later switch to history#pushState
     */
    private final History history = Js.cast(DomGlobal.self.history);

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private Optional<HistoryToken> previousToken = Optional.empty();

    public void setSpreadsheetName(final String name) {
        this.layout.setTitle(name);
    }

    private void showMetadataPanel(final boolean show) {
        final DominoElement<?> right = this.layout.getRightPanel();
        if (show) {
            right.show();
        } else {
            right.hide();
        }
    }

    public void debug(final Object message) {
        DomGlobal.console.debug(message);
    }

    public void error(final Object message) {
        DomGlobal.console.error(message);
    }
}
