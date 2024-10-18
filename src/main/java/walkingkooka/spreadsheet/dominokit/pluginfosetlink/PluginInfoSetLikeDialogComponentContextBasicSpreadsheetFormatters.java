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

package walkingkooka.spreadsheet.dominokit.pluginfosetlink;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetLikeDialogComponentContextBasicSpreadsheetFormatters extends PluginInfoSetLikeDialogComponentContextBasic<SpreadsheetFormatterName,
        SpreadsheetFormatterInfo,
        SpreadsheetFormatterInfoSet,
        SpreadsheetFormatterSelector,
        SpreadsheetFormatterAlias,
        SpreadsheetFormatterAliasSet> {

    static PluginInfoSetLikeDialogComponentContextBasicSpreadsheetFormatters with(final AppContext context) {
        return new PluginInfoSetLikeDialogComponentContextBasicSpreadsheetFormatters(context);
    }

    private PluginInfoSetLikeDialogComponentContextBasicSpreadsheetFormatters(final AppContext context) {
        super(context);
    }

    // PluginInfoSetLikeDialogComponentContext..............................................................................

    @Override
    public SpreadsheetFormatterInfoSetComponent textBox() {
        return SpreadsheetFormatterInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<SpreadsheetFormatterInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.FORMATTERS;
    }

    @Override
    public SpreadsheetFormatterInfoSet emptyInfoSetLike() {
        return SpreadsheetFormatterInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSetLike0(final SpreadsheetId id) {
        this.context.spreadsheetFormatterFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetFormatterInfoSet> set) {
        return this.context.addSpreadsheetFormatterFetcherWatcher(
                new SpreadsheetFormatterFetcherWatcher() {
                    @Override
                    public void onSpreadsheetFormatterInfoSet(final SpreadsheetId id,
                                                              final SpreadsheetFormatterInfoSet infos,
                                                              final AppContext context) {
                        set.accept(infos);
                    }

                    @Override
                    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                                   final SpreadsheetFormatterSelectorEdit edit,
                                                                   final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onSpreadsheetFormatterSelectorMenuList(final SpreadsheetId id,
                                                                       final SpreadsheetFormatterSelectorMenuList menu,
                                                                       final AppContext context) {
                        // nop
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
    SpreadsheetFormatterInfoSet providerInfoSetLike0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.spreadsheetFormatterInfos();
    }
}
