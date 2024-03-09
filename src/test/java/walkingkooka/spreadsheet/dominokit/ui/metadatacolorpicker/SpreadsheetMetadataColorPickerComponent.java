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

package walkingkooka.spreadsheet.dominokit.ui.metadatacolorpicker;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

public class SpreadsheetMetadataColorPickerComponent implements IsElement<Element>, TreePrintable {

    public static SpreadsheetMetadataColorPickerComponent with(final HistoryToken token) {
        return new SpreadsheetMetadataColorPickerComponent(token);
    }

    private SpreadsheetMetadataColorPickerComponent(final HistoryToken token) {
        this.token = token;
    }

    @Override
    public Element element() {
        throw new UnsupportedOperationException();
    }

    public void refreshAll(final HistoryToken token,
                           final SpreadsheetMetadata metadata) {
        // nop
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("metadata color picker");
    }

    private final HistoryToken token;
}
