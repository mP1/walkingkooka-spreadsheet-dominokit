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
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.utils.DominoElement;
import walkingkooka.j2cl.locale.LocaleAware;

@LocaleAware
public class App implements EntryPoint {

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final Layout layout = Layout.create();

    public void onModuleLoad() {
        this.layout.show();
        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);
    }

    public void setSpreadsheetName(final String name) {
        this.layout.setTitle(name);
    }

    private void showMetadataPanel(final boolean show) {
        final DominoElement<?> right = this.layout.getRightPanel();
        if(show) {
            right.show();
        } else {
            right.hide();
        }
    }
}
