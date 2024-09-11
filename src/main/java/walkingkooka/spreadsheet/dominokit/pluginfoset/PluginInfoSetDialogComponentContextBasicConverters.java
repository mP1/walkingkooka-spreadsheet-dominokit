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
import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.convert.ConverterInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.net.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetDialogComponentContextBasicConverters extends PluginInfoSetDialogComponentContextBasic<ConverterName, ConverterInfo, ConverterInfoSet> {

    static PluginInfoSetDialogComponentContextBasicConverters with(final AppContext context) {
        return new PluginInfoSetDialogComponentContextBasicConverters(context);
    }

    private PluginInfoSetDialogComponentContextBasicConverters(final AppContext context) {
        super(context);
    }

    // PluginInfoSetDialogComponentContext..............................................................................

    @Override
    public ConverterInfoSetComponent textBox() {
        return ConverterInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<ConverterInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.CONVERTERS;
    }

    @Override public ConverterInfoSet emptyInfoSet() {
        return ConverterInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSet0(final SpreadsheetId id) {
        this.context.spreadsheetFormatterFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<ConverterInfoSet> set) {
        return this.context.addConverterFetcherWatcher(
                new ConverterFetcherWatcher() {
                    @Override
                    public void onConverterInfoSet(final SpreadsheetId id,
                                                   final ConverterInfoSet infos,
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
    public ConverterInfoSet providerInfoSet() {
        return this.context.converterInfos();
    }
}
