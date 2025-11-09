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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLTableElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.TBodyComponent;
import walkingkooka.spreadsheet.dominokit.dom.TableComponent;
import walkingkooka.spreadsheet.dominokit.dom.TdComponent;
import walkingkooka.spreadsheet.dominokit.dom.TrComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A ui that creates a grid and fills each cell with the current color value taken from {@link SpreadsheetMetadata}.
 * If a {@link SpreadsheetColorName} exists that will be used otherwise a name is formed by combining the "color-" and the color number.
 * Each cell will have a link with a {@link HistoryToken} that saves its {@link Color}.
 */
public final class ColorComponent implements ValueComponent<HTMLTableElement, Color, ColorComponent>,
    HtmlComponentDelegator<HTMLTableElement, ColorComponent>,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    private final static int ROWS = 7;

    private final static int COLUMNS = 8;

    private final static int COLOR_COUNT = ROWS * COLUMNS;

    public static ColorComponent with(final String idPrefix,
                                      final Function<HistoryToken, Optional<HistoryToken>> historyTokenPreparer,
                                      final ColorComponentContext context) {
        return new ColorComponent(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(historyTokenPreparer, "historyTokenPreparer"),
            Objects.requireNonNull(context, "context")
        );
    }

    private ColorComponent(final String idPrefix,
                           final Function<HistoryToken, Optional<HistoryToken>> historyTokenPreparer,
                           final ColorComponentContext context) {
        final TBodyComponent tbody = HtmlElementComponent.tbody();

        int i = 0;
        final TdComponent[] cells = new TdComponent[COLOR_COUNT];
        final HistoryTokenAnchorComponent[] anchors = new HistoryTokenAnchorComponent[COLOR_COUNT];

        // 8x7
        for (int y = 0; y < ROWS; y++) {
            final TrComponent tr = HtmlElementComponent.tr();
            tbody.appendChild(tr);

            for (int x = 0; x < COLUMNS; x++) {
                final TdComponent td = HtmlElementComponent.td();
                td.setCssText("width: 64px; height: 32px; border-color: black; border-width: 2px; border-style: solid; text-align: center;");
                tr.appendChild(td);

                final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty();
                anchor.setId(idPrefix + "color-" + (i + 1) + SpreadsheetElementIds.LINK);

                td.appendChild(anchor);


                cells[i] = td;
                anchors[i] = anchor;

                i++;
            }
        }

        // add a row with a single cell with a link CLEAR
        final TrComponent tr = HtmlElementComponent.tr();
        tbody.appendChild(tr);

        final TdComponent td = HtmlElementComponent.td();
        td.setAttribute("colspan", COLUMNS);
        td.setCssText("width: 100%; height: 32px; text-align: center;");
        tr.appendChild(td);

        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty();
        anchor.setId(idPrefix + "color-clear" + SpreadsheetElementIds.LINK);
        anchor.setTextContent("Clear");
        this.clearAnchor = anchor;

        td.appendChild(anchor);


        final TableComponent table = HtmlElementComponent.table();
        table.setId(
            CharSequences.subSequence(
                idPrefix,
                0,
                -1)
                + SpreadsheetElementIds.TABLE
        );
        table.appendChild(tbody);
        table.setAttribute(
            "className",
            "dui dui-menu-item"
        );

        this.table = table;

        this.cells = cells;
        this.anchors = anchors;

        this.historyTokenPreparer = historyTokenPreparer;
        this.context = context;

        this.refreshAnchors();
    }

    private void refreshAnchors() {
        final ColorComponentContext context = this.context;
        final HistoryToken historyToken = context.historyToken();
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        final TdComponent[] cells = this.cells;
        final HistoryTokenAnchorComponent[] anchors = this.anchors;
        final Function<Integer, Optional<Color>> numberToColors = metadata.numberToColor();
        final Function<Integer, Optional<SpreadsheetColorName>> numberToColorNames = metadata.numberToColorName();

        final Function<HistoryToken, Optional<HistoryToken>> historyTokenPreparer = this.historyTokenPreparer;

        for (int i = 0; i < COLOR_COUNT; i++) {
            final int colorNumber = 1 + i;

            final Optional<Color> maybeColor = numberToColors.apply(colorNumber);
            if (maybeColor.isPresent()) {
                // set the background color of the cell
                final Color color = maybeColor.get();
                cells[i].setBackgroundColor(color.toString());

                final String text = numberToColorNames.apply(colorNumber)
                    .map(SpreadsheetColorName::toString)
                    .orElse("color " + colorNumber);

                // update the link that when clicked will save the selected color
                final HistoryTokenAnchorComponent anchor = anchors[i];
                anchor.setTextContent(text);
                anchor.setHistoryToken(
                    historyTokenPreparer.apply(historyToken)
                        .map(
                            h -> h.setSaveValue(
                                Optional.of(color)
                            )
                        )
                );
            }
        }

        this.clearAnchor.setHistoryToken(
            historyTokenPreparer.apply(historyToken)
                .map(h -> h.clearSaveValue())
        );
    }

    private final TableComponent table;

    private final TdComponent[] cells;

    private final HistoryTokenAnchorComponent[] anchors;

    private final HistoryTokenAnchorComponent clearAnchor;

    /**
     * Transform a {@link HistoryToken} so that it can take a {@link Color}.
     */
    private final Function<HistoryToken, Optional<HistoryToken>> historyTokenPreparer;

    private final ColorComponentContext context;

    // ValueComponent...................................................................................................

    @Override
    public String id() {
        return this.table.id();
    }

    @Override
    public ColorComponent setId(final String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Color> value() {
        return Optional.empty();
    }

    @Override
    public ColorComponent setValue(final Optional<Color> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public ColorComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorComponent addBlurListener(EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public ColorComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public ColorComponent addChangeListener(final ChangeListener<Optional<Color>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public ColorComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public ColorComponent addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public ColorComponent addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public ColorComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    private ColorComponent addEventListener(final EventType type,
                                            final EventListener listener) {
        this.element().addEventListener(
            type.getName(),
            Objects.requireNonNull(listener, "listener")
        );
        return this;
    }

    @Override
    public ColorComponent hideMarginBottom() {
        return this;
    }

    @Override
    public ColorComponent removeBorders() {
        return this;
    }

    @Override
    public ColorComponent removePadding() {
        return this;
    }

    @Override
    public ColorComponent focus() {
        return this;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLTableElement, ?> htmlComponent() {
        return this.table;
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    // SpreadsheetMetadataWatcher.......................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshAnchors();
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // Ignore
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.table.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.table.printTree(printer);
        }
        printer.outdent();
    }
}
