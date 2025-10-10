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
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.validator.ValidatorAliasSetComponent;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<ValidatorName,
    ValidatorInfo,
    ValidatorInfoSet,
    ValidatorSelector,
    ValidatorAlias,
    ValidatorAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final ValidatorAliasSetComponent textBox() {
        return ValidatorAliasSetComponent.empty();
    }

    @Override
    public final ValidatorAliasSet emptyAliasSetLike() {
        return ValidatorAliasSet.EMPTY;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.validatorFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<ValidatorAliasSet> set) {
        return this.context.addValidatorFetcherWatcher(
            new ValidatorFetcherWatcher() {

                @Override
                public void onValidatorInfo(final ValidatorInfo info,
                                            final AppContext context) {
                    // NOP
                }

                @Override
                public void onValidatorInfoSet(final ValidatorInfoSet infos,
                                               final AppContext context) {
                    set.accept(infos.aliasSet());
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body,
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
}
