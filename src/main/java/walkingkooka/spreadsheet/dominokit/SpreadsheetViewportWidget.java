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

import elemental2.dom.HTMLTableElement;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HtmlContentBuilder;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.CaseKind;

import java.util.Objects;

public final class SpreadsheetViewportWidget implements SpreadsheetDeltaWatcher, SpreadsheetMetadataWatcher {

    static SpreadsheetViewportWidget empty(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportWidget(context);
    }

    private SpreadsheetViewportWidget(final AppContext context) {
        this.context = context;
        this.tableElement = Elements.table();
        this.initTable();

        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    private void initTable() {
        final HtmlContentBuilder<HTMLTableElement> tableElement = this.tableElement;
        tableElement.style("width: 100%; height: 100%;");
        tableElement.add(
                Elements.tbody()
                        .add(
                                Elements.tr()
                                        .add(
                                                Elements.td()
                                                        .add(
                                                                "spreadsheet here"
                                                        ).element()
                                        ).element()
                        ).element()
        );
    }

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.width || height > this.height;

        this.context.debug("SpreadsheetViewport.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.tableElement.element()
                .style.set("height", height + "px");

        this.reload = reload;
        this.loadViewportCellsIfNecessary();
    }

    /**
     * The width allocated to the widget.
     */
    private int width;

    /**
     * The height allocated to the widget.
     */
    private int height;

    private final AppContext context;

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        this.cache.onSpreadsheetDelta(delta, context);
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        Objects.requireNonNull(metadata, "metadata");
        
        final SpreadsheetMetadata previous = this.metadata;
        if(metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        this.metadata = metadata;

        this.loadViewportCellsIfNecessary();
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    private void loadViewportCellsIfNecessary() {
        if (this.reload) {
            if(this.metadata.isEmpty()) {
                this.context.debug("SpreadsheetViewportWidget.loadViewportCellsIfNecessary waiting for metadata");
            } else {
                this.loadViewportCells();
            }
        }
    }

    /**
     * Loads all the cells to fill the viewport. Assumes that a metadata with id is present.
     */
    private void loadViewportCells() {
        this.reload = false;

        final SpreadsheetMetadata metadata = this.metadata;
        final int width = this.width;
        final int height = this.height;
        final SpreadsheetCellReference home = metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT_CELL)
                .orElse(SpreadsheetCellReference.A1);

        this.context.debug("SpreadsheetViewportWidget.loadViewportCells " + home + " " + width + "x" + height);


        // load cells for the new window...
        //http://localhost:3000/api/spreadsheet/1f/cell/*/force-recompute?home=A1&width=1712&height=765&includeFrozenColumnsRows=true

        this.context.spreadsheetDeltaFetcher()
                .get(
                        Url.parseRelative(
                                "/api/spreadsheet/" +
                                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID) +
                                        "/cell/*/" +
                                        CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
                        ).setQuery(
                                UrlQueryString.EMPTY
                                        .addParameter(HOME, home.toString())
                                        .addParameter(WIDTH, String.valueOf(width))
                                        .addParameter(HEIGHT, String.valueOf(height))
                                        .addParameter(INCLUDE_FROZEN_COLUMNS_ROWS, Boolean.TRUE.toString())
                        )
                );
    }

    private final static UrlParameterName HOME = UrlParameterName.with("home");
    private final static UrlParameterName WIDTH = UrlParameterName.with("width");
    private final static UrlParameterName HEIGHT = UrlParameterName.with("height");
    private final static UrlParameterName INCLUDE_FROZEN_COLUMNS_ROWS = UrlParameterName.with("includeFrozenColumnsRows");

    /**
     * Initially false, this will become true, when the metadata for a new spreadsheet is loaded and a resize event happens.
     */
    private boolean reload = false;

    /**
     * The root table element.
     */
    HTMLTableElement tableElement() {
        return this.tableElement.element();
    }

    private HtmlContentBuilder<HTMLTableElement> tableElement;

    /**
     * Cache that holds all the cells, labels etc displayed by this widget.
     */
    private SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
}
