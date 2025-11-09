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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.FormHandlerFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.formhandler.FormHandlerAliasSetComponent;
import walkingkooka.validation.form.provider.FormHandlerAlias;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerName;
import walkingkooka.validation.form.provider.FormHandlerSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<FormHandlerName,
    FormHandlerInfo,
    FormHandlerInfoSet,
    FormHandlerSelector,
    FormHandlerAlias,
    FormHandlerAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final FormHandlerAliasSetComponent textBox() {
        return FormHandlerAliasSetComponent.empty();
    }

    @Override
    public final FormHandlerAliasSet emptyAliasSetLike() {
        return FormHandlerAliasSet.EMPTY;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.formHandlerFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<FormHandlerAliasSet> set) {
        return this.context.addFormHandlerFetcherWatcher(
            new FormHandlerFetcherWatcher() {

                @Override
                public void onFormHandlerInfo(final FormHandlerInfo info) {
                    // NOP
                }

                @Override
                public void onFormHandlerInfoSet(final FormHandlerInfoSet infos) {
                    set.accept(infos.aliasSet());
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body) {
                    // nop
                }

                @Override
                public void onFailure(final HttpMethod method,
                                      final AbsoluteOrRelativeUrl url,
                                      final HttpStatus status,
                                      final Headers headers,
                                      final String body) {
                    // nop
                }

                @Override
                public void onError(final Object cause) {
                    // nop
                }

                @Override
                public void onEmptyResponse() {
                    // nop
                }
            }
        );
    }
}
