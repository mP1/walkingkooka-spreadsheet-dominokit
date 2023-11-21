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

package walkingkooka.spreadsheet.dominokit.color;

import elemental2.dom.HTMLTableElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.TDElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.elements.TableRowElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.component.Anchor;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A component that creates a grid and fills each cell with the current color value taken from {@link SpreadsheetMetadata}.
 * If a {@link SpreadsheetColorName} exists that will be used otherwise a name is formed by combining the "color-" and the color number.
 * Each cell will have a link with a {@link HistoryToken} that saves its {@link Color}.
 */
public final class SpreadsheetColorPickerComponent implements SpreadsheetMetadataFetcherWatcher,
        NopFetcherWatcher,
        IsElement<HTMLTableElement> {

    private final static String ID = "color-picker";

    private final static int ROWS = 7;

    private final static int COLUMNS = 8;

    private final static int COLOR_COUNT = ROWS * COLUMNS;

    public static SpreadsheetColorPickerComponent with(final HistoryToken historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        return new SpreadsheetColorPickerComponent(historyToken);
    }

    public SpreadsheetColorPickerComponent(final HistoryToken historyToken) {
        final TBodyElement tbody = ElementsFactory.elements.tbody();

        int i = 0;
        final TDElement[] cells = new TDElement[COLOR_COUNT];
        final Anchor[] anchors = new Anchor[COLOR_COUNT];

        // 8x7
        for(int y = 0; y < ROWS; y++) {
            final TableRowElement tr = ElementsFactory.elements.tr();
            tbody.appendChild(tr);

            for(int x = 0; x < COLUMNS; x++) {
                final TDElement td = ElementsFactory.elements.td();
                td.style("width: 64px; height: 32px; border-color: black; border-width: 2px; border-style: solid; text-align: center;");
                tr.appendChild(td);

                final Anchor anchor = Anchor.empty();
                anchor.setId(ID + "-color-" + (i + 1) + SpreadsheetIds.LINK);

                td.appendChild(anchor);


                cells[i] = td;
                anchors[i] = anchor;

                i++;
            }
        }

        // add a row with a single cell with a link CLEAR
        final TableRowElement tr = ElementsFactory.elements.tr();
        tbody.appendChild(tr);

        final TDElement td = ElementsFactory.elements.td();
        td.setAttribute("colspan", COLUMNS);
        td.style("width: 100%; height: 32px; text-align: center;");
        tr.appendChild(td);

        final Anchor anchor = Anchor.empty();
        anchor.setId(ID + "-color-clear" + SpreadsheetIds.LINK);
        anchor.setTextContent("Clear");
        this.clearAnchor = anchor;

        td.appendChild(anchor);


        final TableElement tableElement = ElementsFactory.elements.table();
        tableElement.setId(ID);
        tableElement.appendChild(tbody);

        this.table = tableElement;

        this.cells = cells;
        this.anchors = anchors;

        this.historyToken = historyToken;
    }

    public void refreshAll(final HistoryToken token,
                           final SpreadsheetMetadata metadata) {
        final TDElement[] cells = this.cells;
        final Anchor[] anchors = this.anchors;
        final Function<Integer, Optional<Color>> numberToColors = metadata.numberToColor();
        final Function<Integer, Optional<SpreadsheetColorName>> numberToColorNames = metadata.numberToColorName();

        for (int i = 0; i < COLOR_COUNT; i++) {
            final int colorNumber = 1 + i;

            final Optional<Color> maybeColor = numberToColors.apply(colorNumber);
            if (maybeColor.isPresent()) {
                // set the background color o the cell
                final Color color = maybeColor.get();
                cells[i].setBackgroundColor(color.toString());

                final String text = numberToColorNames.apply(colorNumber)
                        .map(SpreadsheetColorName::toString)
                        .orElse("color " + colorNumber);

                // update the link that when clicked will save the selected color
                final Anchor anchor = anchors[i];
                anchor.setTextContent(text);
                anchor.setHistoryToken(
                        Optional.of(
                                token.setSave(color)
                        )
                );
            }
        }

        this.clearAnchor.setHistoryToken(
                Optional.of(
                        token.clearSave()
                )
        );
    }

    // IsElement.......................................................................................................

    @Override
    public HTMLTableElement element() {
        return this.table.element();
    }

    private final TableElement table;

    private final TDElement[] cells;

    private final Anchor[] anchors;

    private final Anchor clearAnchor;

    // SpreadsheetMetadataWatcher.......................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshAll(
                this.historyToken,
                metadata
        );
    }

    private final HistoryToken historyToken;
}
