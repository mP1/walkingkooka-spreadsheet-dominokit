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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.collect.set.Sets;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAlias;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenuContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link SpreadsheetSelectionMenuContext} used by a {@link SpreadsheetViewportComponent}.
 */
final class SpreadsheetViewportComponentSpreadsheetSelectionMenuContext implements SpreadsheetSelectionMenuContext,
    SpreadsheetComparatorProviderDelegator,
    HistoryContextDelegator,
    LocaleContextDelegator {

    static SpreadsheetViewportComponentSpreadsheetSelectionMenuContext with(final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                                            final List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus,
                                                                            final List<Locale> recentLocales,
                                                                            final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors,
                                                                            final List<TextStyleProperty<?>> recentTextStyleProperties,
                                                                            final List<ValidatorSelector> recentValidatorSelectors,
                                                                            final SpreadsheetViewportComponentContext context) {
        return new SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(
            recentSpreadsheetFormatterSelectors,
            spreadsheetFormatterMenus,
            recentLocales,
            recentSpreadsheetParserSelectors,
            recentTextStyleProperties,
            recentValidatorSelectors,
            context
        );
    }

    private SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                                        final List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus,
                                                                        final List<Locale> recentLocales,
                                                                        final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors,
                                                                        final List<TextStyleProperty<?>> recentTextStyleProperties,
                                                                        final List<ValidatorSelector> recentValidatorSelectors,
                                                                        final SpreadsheetViewportComponentContext context) {
        this.recentSpreadsheetFormatterSelectors = recentSpreadsheetFormatterSelectors;
        this.spreadsheetFormatterMenus = spreadsheetFormatterMenus;

        this.recentLocales = recentLocales;

        this.recentSpreadsheetParserSelectors = recentSpreadsheetParserSelectors;

        this.recentTextStyleProperties = recentTextStyleProperties;

        this.recentValidatorSelectors = recentValidatorSelectors;

        this.context = context;
    }

    @Override
    public List<SpreadsheetComparatorName> sortComparatorNames() {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        final SpreadsheetComparatorNameList sortComparators = metadata.get(SpreadsheetMetadataPropertyName.SORT_COMPARATORS)
            .orElse(SpreadsheetComparatorNameList.EMPTY);

        return SpreadsheetComparatorNameList.with(
            metadata.get(SpreadsheetMetadataPropertyName.COMPARATORS)
                .orElse(SpreadsheetComparatorAliasSet.EMPTY)
                .stream()
                .map(SpreadsheetComparatorAlias::name)
                .filter(sortComparators::contains)
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
        return this.recentSpreadsheetFormatterSelectors;
    }

    private final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors;

    @Override
    public List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus() {
        return this.spreadsheetFormatterMenus;
    }

    private final List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus;

    @Override
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        return this.context.spreadsheetViewportCache()
            .labelMappings(selection);
    }

    @Override
    public List<Locale> recentLocales() {
        return this.recentLocales;
    }

    private final List<Locale> recentLocales;

    @Override
    public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
        return this.recentSpreadsheetParserSelectors;
    }

    private final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors;

    @Override
    public Set<SpreadsheetExpressionReference> references(final SpreadsheetSelection selection) {
        return selection.isExternalReference() ?
            Sets.empty() :
            this.context.spreadsheetViewportCache()
                .cellReferences(selection.toExpressionReference());
    }

    @Override
    public List<TextStyleProperty<?>> recentTextStyleProperties() {
        return this.recentTextStyleProperties;
    }

    private final List<TextStyleProperty<?>> recentTextStyleProperties;

    @Override
    public List<ValidatorSelector> recentValidatorSelectors() {
        return this.recentValidatorSelectors;
    }

    private final List<ValidatorSelector> recentValidatorSelectors;

    @Override
    public List<ValidatorSelector> validatorSelectors() {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        final ValidatorAliasSet validators = metadata.get(SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS)
            .orElse(ValidatorAliasSet.EMPTY);

        return metadata.get(SpreadsheetMetadataPropertyName.VALIDATORS)
            .orElse(ValidatorAliasSet.EMPTY)
            .stream()
            .map((final ValidatorAlias v) -> v.selector().orElse(null))
            .filter((final ValidatorSelector s) -> null != s && validators.containsAliasOrName(s.name()))
            .collect(Collectors.toList());
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID_PREFIX = SpreadsheetViewportComponent.ID_PREFIX + "context-menu-";

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public Optional<SpreadsheetCell> selectionSummary() {
        return this.context.spreadsheetViewportCache()
            .selectionSummary();
    }

    // SpreadsheetComparatorProvider....................................................................................

    @Override
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.context;
    }

    // HistoryContext..............................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    // LocaleContextDelegator...........................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.context;
    }

    @Override
    public SpreadsheetViewportComponentSpreadsheetSelectionMenuContext setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetLabelNameResolver.....................................................................................

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
        return this.context.resolveLabel(spreadsheetLabelName);
    }

    private final SpreadsheetViewportComponentContext context;

    // ColorComponentContext............................................................................................

    /**
     * Components within a menu should not react to any {@link walkingkooka.spreadsheet.dominokit.fetcher.FetcherWatcher} events.
     */
    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return new Runnable() {
            @Override
            public void run() {
                // nop
            }
        };
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
