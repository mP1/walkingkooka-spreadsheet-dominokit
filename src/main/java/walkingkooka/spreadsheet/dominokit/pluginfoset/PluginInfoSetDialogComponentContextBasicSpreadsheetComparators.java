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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetComparatorFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetDialogComponentContextBasicSpreadsheetComparators extends PluginInfoSetDialogComponentContextBasic<SpreadsheetComparatorName, SpreadsheetComparatorInfo, SpreadsheetComparatorInfoSet> {

    static PluginInfoSetDialogComponentContextBasicSpreadsheetComparators with(final AppContext context) {
        return new PluginInfoSetDialogComponentContextBasicSpreadsheetComparators(context);
    }

    private PluginInfoSetDialogComponentContextBasicSpreadsheetComparators(final AppContext context) {
        super(context);
    }

    // PluginInfoSetDialogComponentContext..............................................................................

    @Override
    public SpreadsheetComparatorInfoSetComponent textBox() {
        return SpreadsheetComparatorInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<SpreadsheetComparatorInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.COMPARATORS;
    }

    @Override
    public SpreadsheetComparatorInfoSet emptyInfoSet() {
        return SpreadsheetComparatorInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSet0(final SpreadsheetId id) {
        this.context.spreadsheetComparatorFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetComparatorInfoSet> set) {
        return this.context.addSpreadsheetComparatorFetcherWatcher(
                new SpreadsheetComparatorFetcherWatcher() {
                    @Override
                    public void onSpreadsheetComparatorInfoSet(final SpreadsheetId id,
                                                               final SpreadsheetComparatorInfoSet infos,
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
    SpreadsheetComparatorInfoSet providerInfoSet0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.spreadsheetComparatorInfos();
    }
}
