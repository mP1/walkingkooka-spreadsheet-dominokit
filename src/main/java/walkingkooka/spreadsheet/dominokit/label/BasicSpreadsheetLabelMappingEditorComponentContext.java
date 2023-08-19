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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;

/**
 * A basic implementation of {@link SpreadsheetLabelMappingEditorComponentContext}.
 */
final class BasicSpreadsheetLabelMappingEditorComponentContext implements SpreadsheetLabelMappingEditorComponentContext {

    static BasicSpreadsheetLabelMappingEditorComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");
        return new BasicSpreadsheetLabelMappingEditorComponentContext(context);
    }

    private BasicSpreadsheetLabelMappingEditorComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public void addLabelMappingWatcher(final SpreadsheetLabelMappingWatcher watcher) {
        this.context.addSpreadsheetLabelMappingWatcher(watcher);
    }

    @Override
    public void loadLabel(final SpreadsheetLabelName name) {
        final AppContext context = this.context;
        context.spreadsheetLabelMappingFetcher()
                .loadLabelMapping(
                        context.historyToken()
                                .cast(SpreadsheetIdHistoryToken.class)
                                .id(),
                        name
                );
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public SpreadsheetLabelMappingHistoryToken historyToken() {
        return this.context.historyToken()
                .cast(SpreadsheetLabelMappingHistoryToken.class);
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    @Override
    public SpreadsheetLabelName label() {
        return this.historyToken().labelName();
    }

    @Override
    public void delete() {
        this.pushHistoryToken(
                this.historyToken().setDelete()
        );
    }

    @Override
    public void save(final SpreadsheetLabelMapping mapping) {
        this.pushHistoryToken(
                this.historyToken()
                        .setLabelName(
                                mapping.label()
                        ).setSave(
                                mapping.reference()
                                        .text()
                        )
        );
    }

    @Override
    public void close() {
        this.debug("close: " + this.historyToken()
                .close());
        this.pushHistoryToken(
                this.historyToken()
                        .close()
        );
    }

    @Override
    public void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.context.error(values);
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
