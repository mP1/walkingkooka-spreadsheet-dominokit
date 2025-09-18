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

package walkingkooka.spreadsheet.dominokit.convert;

import walkingkooka.collect.map.Maps;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.server.url.SpreadsheetUrlPathTemplate;

import java.util.Objects;
import java.util.Optional;

final class AppContextConverterSelectorDialogComponentContext implements ConverterSelectorDialogComponentContext,
    DialogComponentContextDelegator {

    static AppContextConverterSelectorDialogComponentContext with(final SpreadsheetMetadataPropertyName<ConverterSelector> propertyName,
                                                                  final AppContext context) {
        return new AppContextConverterSelectorDialogComponentContext(
            Objects.requireNonNull(propertyName, "propertyName"),
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextConverterSelectorDialogComponentContext(final SpreadsheetMetadataPropertyName<ConverterSelector> propertyName,
                                                              final AppContext context) {
        this.propertyName = propertyName;
        this.context = context;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            this.propertyName.equals(
                token.metadataPropertyName()
                    .orElse(null)
            );
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(this.propertyName);
    }

    @Override
    public Optional<ConverterSelector> undo() {
        return this.context.spreadsheetMetadata()
            .get(this.propertyName);
    }

    @Override
    public void verifySelector(final String selector) {
        Objects.requireNonNull(selector, "selector");

        this.context.converterFetcher()
            .postVerify(
                this.context.historyToken()
                    .cast(SpreadsheetIdHistoryToken.class)
                    .id(),
                this.propertyName,
                selector
            );
    }

    @Override
    public boolean isVerifyConverterSelectorUrl(final UrlPath path) {
        return VERIFY_PATH.extract(path)
            .equals(
                Maps.of(
                    SpreadsheetUrlPathTemplate.SPREADSHEET_ID,
                    this.historyToken()
                        .cast(SpreadsheetIdHistoryToken.class)
                        .id(),
                    SpreadsheetUrlPathTemplate.SPREADSHEET_METADATA_PROPERTY_NAME,
                    this.propertyName
                )
            );
    }

    private final SpreadsheetUrlPathTemplate VERIFY_PATH = SpreadsheetUrlPathTemplate.parse("/api/spreadsheet/${SpreadsheetId}/metadata/${SpreadsheetMetadataPropertyName}/verify");


    @Override
    public Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher) {
        return this.context.addConverterFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // DialogComponentContext...........................................................................................

    @Override
    public DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    private final SpreadsheetMetadataPropertyName<ConverterSelector> propertyName;

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.propertyName + " " + this.context;
    }
}
