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

package walkingkooka.spreadsheet.dominokit.meta;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.OpenableComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;

/**
 * A component that displays numerous {@link SpreadsheetMetadata} properties with support for editing the individual values.
 */
public final class SpreadsheetMetadataPanelComponent implements ComponentLifecycle,
        SpreadsheetMetadataWatcher {

    public static SpreadsheetMetadataPanelComponent with(final OpenableComponent drawer,
                                                         final SpreadsheetMetadataPanelComponentContext context) {
        Objects.requireNonNull(drawer, "drawer");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetMetadataPanelComponent(
                drawer,
                context
        );
    }

    private SpreadsheetMetadataPanelComponent(final OpenableComponent drawer,
                                              final SpreadsheetMetadataPanelComponentContext context) {
        this.drawer = drawer;
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken ||
                token instanceof SpreadsheetMetadataPropertyStyleSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return this.drawer.isOpen();
    }

    @Override
    public void open(final AppContext context) {
        this.drawer.open(context);
    }

    @Override
    public void refresh(final AppContext context) {
        context.debug("SpreadsheetMetadataPanelComponent.refresh");
    }

    @Override
    public void close(final AppContext context) {
        this.drawer.close(context);
    }

    /**
     * Typically the right drawer of an {@link org.dominokit.domino.ui.layout.AppLayout}.
     */
    private final OpenableComponent drawer;

    // SpreadsheetMetadataWatcher.............................................,.........................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(context);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.drawer.toString();
    }
}
