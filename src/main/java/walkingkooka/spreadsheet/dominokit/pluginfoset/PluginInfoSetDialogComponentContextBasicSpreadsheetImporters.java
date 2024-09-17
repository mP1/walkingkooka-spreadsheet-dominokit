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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.importer.SpreadsheetImporterInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetImporterFetcherWatcher;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetDialogComponentContextBasicSpreadsheetImporters extends PluginInfoSetDialogComponentContextBasic<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet> {

    static PluginInfoSetDialogComponentContextBasicSpreadsheetImporters with(final AppContext context) {
        return new PluginInfoSetDialogComponentContextBasicSpreadsheetImporters(context);
    }

    private PluginInfoSetDialogComponentContextBasicSpreadsheetImporters(final AppContext context) {
        super(context);
    }

    // PluginInfoSetDialogComponentContext..............................................................................

    @Override
    public SpreadsheetImporterInfoSetComponent textBox() {
        return SpreadsheetImporterInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<SpreadsheetImporterInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.SPREADSHEET_IMPORTERS;
    }

    @Override public SpreadsheetImporterInfoSet emptyInfoSet() {
        return SpreadsheetImporterInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSet0(final SpreadsheetId id) {
        this.context.spreadsheetImporterFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetImporterInfoSet> set) {
        return this.context.addSpreadsheetImporterFetcherWatcher(
                new SpreadsheetImporterFetcherWatcher() {
                    @Override
                    public void onSpreadsheetImporterInfoSet(final SpreadsheetId id,
                                                             final SpreadsheetImporterInfoSet infos,
                                                             final AppContext context) {
                        set.accept(infos);
                    }

                    @Override
                    public void onBegin(final HttpMethod method,
                                        final Url url,
                                        final Optional<String> body,
                                        final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onFailure(final HttpMethod method,
                                          final AbsoluteOrRelativeUrl url,
                                          final HttpStatus status,
                                          final Headers headers,
                                          final String body,
                                          final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onError(final Object cause,
                                        final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onEmptyResponse(final AppContext context) {
                        // nop
                    }
                }
        );
    }

    @Override
    SpreadsheetImporterInfoSet providerInfoSet0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.spreadsheetImporterInfos();
    }
}
