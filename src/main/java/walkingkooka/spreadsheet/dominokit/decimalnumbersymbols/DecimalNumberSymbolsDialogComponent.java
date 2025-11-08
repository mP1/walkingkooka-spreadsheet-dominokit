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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.Cast;
import walkingkooka.EmptyTextException;
import walkingkooka.NeverError;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.character.CharacterComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponent;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponentContext;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponentSuggestionsValue;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.text.CaseKind;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model dialog that includes numerous form fields supporting the editing on individual {@link DecimalNumberSymbols}
 * which are aggregated and then saved.
 * <pre>
 * - negativeSign textbox
 * - positiveSign textbox
 * - zeroDigit textbox
 * - currencySymbol textbox
 * - decimalSeparator textbox
 * - exponentSymbol textbox
 * - groupSeparator textbox
 * - infinitySymbol textbox
 * - monetaryDecimalSeparator text
 * - nanSymbol textbox
 * - percentSymbol textbox
 * - permillSymbol textbox
 * - decimalNumberSymbols textBox
 * - save link
 * - clear link
 * - undo link
 * - close link
 * </pre>
 */
public final class DecimalNumberSymbolsDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    ComponentLifecycleMatcherDelegator,
    DecimalNumberSymbolsFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link DecimalNumberSymbolsDialogComponent}.
     */
    public static DecimalNumberSymbolsDialogComponent with(final DecimalNumberSymbolsDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new DecimalNumberSymbolsDialogComponent(context);
    }

    private DecimalNumberSymbolsDialogComponent(final DecimalNumberSymbolsDialogComponentContext context) {
        this.context = context;

        this.negativeSign = this.negativeSign();
        this.positiveSign = this.positiveSign();
        this.zeroDigit = this.zeroDigit();

        this.currencySymbol = this.currencySymbol();
        this.decimalSeparator = this.decimalSeparator();
        this.exponentSymbol = this.exponentSymbol();
        this.groupSeparator = this.groupSeparator();
        this.infinitySymbol = this.infinitySymbol();
        this.monetaryDecimalSeparator = this.monetaryDecimalSeparator();
        this.nanSymbol = this.nanSymbol();
        this.percentSymbol = this.percentSymbol();
        this.permillSymbol = this.permillSymbol();

        this.decimalNumberSymbols = this.decimalNumberSymbols();

        this.localeLoad = this.localeLoad(context);

        this.save = this.<DecimalNumberSymbols>saveValueAnchor(context)
            .autoDisableWhenMissingValue();

        this.copyDefaults = this.copyDefaultValueAnchor(context);

        this.clear = this.clearValueAnchor(context);
        this.undo = this.undoAnchor(context);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addDecimalNumberSymbolsFetcherWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
    }

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final DecimalNumberSymbolsDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            FlexLayoutComponent.row()
                .appendChild(this.negativeSign)
                .appendChild(this.positiveSign)
                .appendChild(this.zeroDigit)
                .appendChild(this.currencySymbol)
                .appendChild(this.decimalSeparator)
                .appendChild(this.exponentSymbol)
                .appendChild(this.groupSeparator)
                .appendChild(this.infinitySymbol)
                .appendChild(this.monetaryDecimalSeparator)
                .appendChild(this.nanSymbol)
                .appendChild(this.percentSymbol)
                .appendChild(this.permillSymbol)
                .appendChild(this.decimalNumberSymbols)
        ).appendChild(
            this.localeLoad
        ).appendChild(
            AnchorListComponent.empty()
                .appendChild(this.save)
                .appendChild(this.clear)
                .appendChild(this.undo)
                .appendChild(this.copyDefaults)
                .appendChild(this.close)
        );
    }

    private final DialogComponent dialog;

    private final DecimalNumberSymbolsDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = DecimalNumberSymbols.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // negativeSign.....................................................................................................

    private CharacterComponent negativeSign() {
        return characterComponent(
            "negativeSign", // id
            "Negative sign", // label
            DecimalNumberSymbols.SYMBOL,
            NEGATIVE_SIGN_PROPERTY_INDEX
        );
    }

    private final CharacterComponent negativeSign;

    // positiveSign.....................................................................................................

    private CharacterComponent positiveSign() {
        return characterComponent(
            "positiveSign", // id
            "Positive sign", // label
            DecimalNumberSymbols.SYMBOL,
            POSITIVE_SIGN_PROPERTY_INDEX
        );
    }

    private final CharacterComponent positiveSign;

    // zeroDigit........................................................................................................

    private CharacterComponent zeroDigit() {
        return characterComponent(
            "zeroDigit", // id
            "Zero digit", // label
            DecimalNumberSymbols.ZERO_DIGIT,
            ZERO_DIGIT_PROPERTY_INDEX
        );
    }

    private final CharacterComponent zeroDigit;

    // currencySymbol...................................................................................................

    private TextBoxComponent currencySymbol() {
        return stringComponent(
            "currencySymbol", // id
            "Currency", // label
            CURRENCY_SYMBOL_PROPERTY_INDEX
        );
    }

    private final TextBoxComponent currencySymbol;

    // decimalSeparator.................................................................................................

    private CharacterComponent decimalSeparator() {
        return characterComponent(
            "decimalSeparator", // id
            "Decimal separator", // label
            DecimalNumberSymbols.SYMBOL,
            DECIMAL_SEPARATOR_PROPERTY_INDEX
        );
    }

    private final CharacterComponent decimalSeparator;

    // exponentSymbol...................................................................................................

    private TextBoxComponent exponentSymbol() {
        return stringComponent(
            "exponentSymbol", // id
            "Exponent", // label
            EXPONENT_SYMBOL_PROPERTY_INDEX
        );
    }

    private final TextBoxComponent exponentSymbol;

    // groupingSeparator................................................................................................

    private CharacterComponent groupSeparator() {
        return characterComponent(
            "groupSeparator", // id
            "Group separator", // label
            DecimalNumberSymbols.SYMBOL,
            GROUP_SEPARATOR_PROPERTY_INDEX
        );
    }

    private final CharacterComponent groupSeparator;

    // infinitySymbol...................................................................................................

    private TextBoxComponent infinitySymbol() {
        return stringComponent(
            "infinitySymbol", // id
            "Infinity", // label
            INFINITY_SYMBOL_PROPERTY_INDEX
        );
    }

    private final TextBoxComponent infinitySymbol;

    // monetaryDecimalSeparator.........................................................................................

    private CharacterComponent monetaryDecimalSeparator() {
        return characterComponent(
            "monetaryDecimalSeparator", // id
            "Monetary decimal separator", // label
            DecimalNumberSymbols.SYMBOL,
            MONETARY_DECIMAL_SEPARATOR_PROPERTY_INDEX
        );
    }

    private final CharacterComponent monetaryDecimalSeparator;

    // nanSymbol........................................................................................................

    private TextBoxComponent nanSymbol() {
        return stringComponent(
            "nanSymbol", // id
            "Nan", // label
            NAN_SYMBOL_PROPERTY_INDEX
        );
    }

    private final TextBoxComponent nanSymbol;

    // percentSymbol....................................................................................................

    private CharacterComponent percentSymbol() {
        return characterComponent(
            "percentSymbol", // id
            "Percent symbol", // label
            DecimalNumberSymbols.SYMBOL,
            PERCENT_SYMBOL_PROPERTY_INDEX
        );
    }

    private final CharacterComponent percentSymbol;

    // permillSymbol....................................................................................................

    private CharacterComponent permillSymbol() {
        return characterComponent(
            "permillSymbol", // id
            "Permill symbol", // label
            DecimalNumberSymbols.PERMILL_SYMBOL,
            PERMILL_SYMBOL_PROPERTY_INDEX
        );
    }

    private final CharacterComponent permillSymbol;

    private CharacterComponent characterComponent(final String id,
                                                  final String label,
                                                  final CharPredicate predicate,
                                                  final int propertyIndex) {
        return CharacterComponent.empty(
                label,
                predicate,
                "Invalid character" // TODO message should have more detail about what characters are valid.
            ).setId(ID + '-' + id + SpreadsheetElementIds.TEXT_BOX)
            .setLabel(label)
            .addValueWatcher2(
                (Optional<Character> value) ->
                    this.refreshDecimalNumberSymbols(
                        propertyIndex,
                        value
                    )
            );
    }

    private TextBoxComponent stringComponent(final String id,
                                             final String label,
                                             final int propertyIndex) {
        return TextBoxComponent.empty()
            .setId(ID + '-' + CaseKind.CAMEL.change(id, CaseKind.PASCAL) + SpreadsheetElementIds.TEXT_BOX)
            .setLabel(label)
            .setValidator(SpreadsheetValidators.required())
            .clearIcon()
            .addValueWatcher2(
                (Optional<String> value) ->
                    this.refreshDecimalNumberSymbols(
                        propertyIndex,
                        value
                    )
            );
    }

    /**
     * Target for a {@link CharacterComponent} onchange event to update {@link #decimalNumberSymbols}.
     */
    private void refreshDecimalNumberSymbols(final int propertyIndex,
                                             final Optional<?> value) {
        try {
            Optional<Character> negativeSign = this.negativeSign.value();
            Optional<Character> positiveSign = this.positiveSign.value();
            Optional<Character> zeroDigit = this.zeroDigit.value();
            Optional<String> currencySymbol = this.currencySymbol.value();
            Optional<Character> decimalSeparator = this.decimalSeparator.value();
            Optional<String> exponentSymbol = this.exponentSymbol.value();
            Optional<Character> groupSeparator = this.groupSeparator.value();
            Optional<String> infinitySymbol = this.infinitySymbol.value();
            Optional<Character> monetaryDecimalSeparator = this.monetaryDecimalSeparator.value();
            Optional<String> nanSymbol = this.nanSymbol.value();
            Optional<Character> percentSymbol = this.percentSymbol.value();
            Optional<Character> permillSymbol = this.permillSymbol.value();

            switch (propertyIndex) {
                case NEGATIVE_SIGN_PROPERTY_INDEX:
                    negativeSign = Cast.to(value);
                    break;
                case POSITIVE_SIGN_PROPERTY_INDEX:
                    positiveSign = Cast.to(value);
                    break;
                case ZERO_DIGIT_PROPERTY_INDEX:
                    zeroDigit = Cast.to(value);
                    break;
                case CURRENCY_SYMBOL_PROPERTY_INDEX:
                    currencySymbol = Cast.to(value);
                    break;
                case DECIMAL_SEPARATOR_PROPERTY_INDEX:
                    decimalSeparator = Cast.to(value);
                    break;
                case EXPONENT_SYMBOL_PROPERTY_INDEX:
                    exponentSymbol = Cast.to(value);
                    break;
                case GROUP_SEPARATOR_PROPERTY_INDEX:
                    groupSeparator = Cast.to(value);
                    break;
                case INFINITY_SYMBOL_PROPERTY_INDEX:
                    infinitySymbol = Cast.to(value);
                    break;
                case MONETARY_DECIMAL_SEPARATOR_PROPERTY_INDEX:
                    monetaryDecimalSeparator = Cast.to(value);
                    break;
                case NAN_SYMBOL_PROPERTY_INDEX:
                    nanSymbol = Cast.to(value);
                    break;
                case PERCENT_SYMBOL_PROPERTY_INDEX:
                    percentSymbol = Cast.to(value);
                    break;
                case PERMILL_SYMBOL_PROPERTY_INDEX:
                    permillSymbol = Cast.to(value);
                    break;
                default:
                    NeverError.unhandledCase(
                        propertyIndex,
                        NEGATIVE_SIGN_PROPERTY_INDEX,
                        POSITIVE_SIGN_PROPERTY_INDEX,
                        ZERO_DIGIT_PROPERTY_INDEX,
                        CURRENCY_SYMBOL_PROPERTY_INDEX,
                        DECIMAL_SEPARATOR_PROPERTY_INDEX,
                        EXPONENT_SYMBOL_PROPERTY_INDEX,
                        GROUP_SEPARATOR_PROPERTY_INDEX,
                        INFINITY_SYMBOL_PROPERTY_INDEX,
                        MONETARY_DECIMAL_SEPARATOR_PROPERTY_INDEX,
                        NAN_SYMBOL_PROPERTY_INDEX,
                        PERCENT_SYMBOL_PROPERTY_INDEX,
                        PERMILL_SYMBOL_PROPERTY_INDEX
                    );
            }

            final DecimalNumberSymbols decimalNumberSymbols = DecimalNumberSymbols.with(
                negativeSign.orElseThrow(() -> new EmptyTextException("Negative sign")),
                positiveSign.orElseThrow(() -> new EmptyTextException("Positive sign")),
                zeroDigit.orElseThrow(() -> new EmptyTextException("Zero digit")),
                currencySymbol.orElse(""),
                decimalSeparator.orElseThrow(() -> new EmptyTextException("Decimal separator")),
                exponentSymbol.orElse(""),
                groupSeparator.orElseThrow(() -> new EmptyTextException("Group separator")),
                infinitySymbol.orElse(""),
                monetaryDecimalSeparator.orElseThrow(() -> new EmptyTextException("Monetary decimal separator")),
                nanSymbol.orElse(""),
                percentSymbol.orElseThrow(() -> new EmptyTextException("Percent symbol")),
                permillSymbol.orElseThrow(() -> new EmptyTextException("Permill symbol"))
            );

            this.decimalNumberSymbols.setValue(
                Optional.of(decimalNumberSymbols)
            );

            this.refreshDecimalNumberSymbolsComponentsAndSave(
                Optional.of(decimalNumberSymbols)
            );

        } catch (final RuntimeException ignore) {
            // unable to update #decimalNumberSymbols
            ignore.printStackTrace();
        }
    }

    private final static int NEGATIVE_SIGN_PROPERTY_INDEX = 1;

    private final static int POSITIVE_SIGN_PROPERTY_INDEX = 2;

    private final static int ZERO_DIGIT_PROPERTY_INDEX = 3;

    private final static int CURRENCY_SYMBOL_PROPERTY_INDEX = 4;

    private final static int DECIMAL_SEPARATOR_PROPERTY_INDEX = 5;

    private final static int EXPONENT_SYMBOL_PROPERTY_INDEX = 6;

    private final static int GROUP_SEPARATOR_PROPERTY_INDEX = 7;

    private final static int INFINITY_SYMBOL_PROPERTY_INDEX = 8;

    private final static int MONETARY_DECIMAL_SEPARATOR_PROPERTY_INDEX = 9;

    private final static int NAN_SYMBOL_PROPERTY_INDEX = 10;

    private final static int PERCENT_SYMBOL_PROPERTY_INDEX = 11;

    private final static int PERMILL_SYMBOL_PROPERTY_INDEX = 12;

    // decimalNumberSymbols..................................................................................................

    private DecimalNumberSymbolsComponent decimalNumberSymbols() {
        return DecimalNumberSymbolsComponent.empty()
            .setLabel("Date Time Symbols")
            .addValueWatcher2(
                this::refreshDecimalNumberSymbolsComponentsAndSave
            );
    }

    /**
     * Refreshes other components after a {@link #decimalNumberSymbols} change listener event using its new {@link DecimalNumberSymbols}.
     */
    private void refreshDecimalNumberSymbolsComponentsAndSave(final Optional<DecimalNumberSymbols> maybeDecimalNumberSymbols) {
        if (maybeDecimalNumberSymbols.isPresent()) {
            final DecimalNumberSymbols decimalNumberSymbols = maybeDecimalNumberSymbols.get();

            this.positiveSign.setValue(
                Optional.of(decimalNumberSymbols.positiveSign())
            );
            this.negativeSign.setValue(
                Optional.of(decimalNumberSymbols.negativeSign())
            );
            this.zeroDigit.setValue(
                Optional.of(decimalNumberSymbols.zeroDigit())
            );
            this.currencySymbol.setValue(
                Optional.of(decimalNumberSymbols.currencySymbol())
            );
            this.decimalSeparator.setValue(
                Optional.of(decimalNumberSymbols.decimalSeparator())
            );
            this.exponentSymbol.setValue(
                Optional.of(decimalNumberSymbols.exponentSymbol())
            );
            this.groupSeparator.setValue(
                Optional.of(decimalNumberSymbols.groupSeparator())
            );
            this.infinitySymbol.setValue(
                Optional.of(decimalNumberSymbols.infinitySymbol())
            );
            this.monetaryDecimalSeparator.setValue(
                Optional.of(decimalNumberSymbols.monetaryDecimalSeparator())
            );
            this.nanSymbol.setValue(
                Optional.of(decimalNumberSymbols.nanSymbol())
            );
            this.percentSymbol.setValue(
                Optional.of(decimalNumberSymbols.percentSymbol())
            );
            this.permillSymbol.setValue(
                Optional.of(decimalNumberSymbols.permillSymbol())
            );

            this.save.setValue(
                Optional.of(decimalNumberSymbols)
            );
        } else {
            this.clear();
        }
    }

    private void clear() {
        this.negativeSign.clearValue();
        this.positiveSign.clearValue();
        this.zeroDigit.clearValue();

        this.currencySymbol.clearValue();
        this.decimalSeparator.clearValue();
        this.exponentSymbol.clearValue();
        this.groupSeparator.clearValue();
        this.infinitySymbol.clearValue();
        this.monetaryDecimalSeparator.clearValue();
        this.nanSymbol.clearValue();
        this.percentSymbol.clearValue();
        this.permillSymbol.clearValue();
        this.decimalNumberSymbols.clearValue();
    }

    private final DecimalNumberSymbolsComponent decimalNumberSymbols;

    // localeLoad.......................................................................................................

    // loadLocale.......................................................................................................

    /**
     * A locale drop down that when selected loads the symbols for the selected Locale.
     */
    private LocaleComponent<DecimalNumberSymbols> localeLoad(final DecimalNumberSymbolsDialogComponentContext context) {
        return LocaleComponent.empty(
                new LocaleComponentContext<DecimalNumberSymbols>() {

                    @Override
                    public void filter(final String startsWith,
                                       final SuggestBoxComponent<LocaleComponentSuggestionsValue<DecimalNumberSymbols>> suggestBox) {
                        context.findDecimalNumberSymbolsWithLocaleStartsWith(startsWith);
                    }

                    @Override
                    public MenuItem<LocaleComponentSuggestionsValue<DecimalNumberSymbols>> createMenuItem(final LocaleComponentSuggestionsValue<DecimalNumberSymbols> value) {
                        return this.historyTokenMenuItem(
                            ID,
                            value,
                            DecimalNumberSymbolsDialogComponent.this.context
                        );
                    }

                    @Override
                    public Optional<LocaleComponentSuggestionsValue<DecimalNumberSymbols>> toValue(final Locale locale) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void verifyOption(final LocaleComponentSuggestionsValue<DecimalNumberSymbols> value,
                                             final SuggestBoxComponent<LocaleComponentSuggestionsValue<DecimalNumberSymbols>> suggestBox) {
                        throw new UnsupportedOperationException();
                    }
                }
            ).setLabel("Load from Locale")
            .optional();
    }

    /**
     * This {@link LocaleComponent} is only intended to support search for a Locale, and to allow the user
     * to click any of the suggestions. Operations such as {@link LocaleComponent#setValue(Optional)} are
     * not supported.
     */
    private final LocaleComponent<DecimalNumberSymbols> localeLoad;

    // save.............................................................................................................

    private final HistoryTokenSaveValueAnchorComponent<DecimalNumberSymbols> save;

    // copyDefaults.....................................................................................................

    /**
     * This link will copy the {@link DecimalNumberSymbols} from the locale etc.
     */
    private HistoryTokenSaveValueAnchorComponent<DecimalNumberSymbols> copyDefaultValueAnchor(final DecimalNumberSymbolsDialogComponentContext context) {
        return HistoryTokenSaveValueAnchorComponent.<DecimalNumberSymbols>with(
                this.idPrefix() +
                    "copyDefaults" +
                    SpreadsheetElementIds.LINK,
                context
            ).setTextContent("Copy Defaults")
            .autoDisableWhenMissingValue();
    }

    private void refreshCopyDefaults() {
        this.copyDefaults.setValue(
            this.context.copyDecimalNumberSymbols()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<DecimalNumberSymbols> copyDefaults;

    // clear............................................................................................................

    private void refreshClear() {
        this.clear.clearValue();
    }

    private final HistoryTokenSaveValueAnchorComponent<DecimalNumberSymbols> clear;

    // undo.............................................................................................................

    private void refreshUndo() {
        this.undo.setValue(
            this.context.loadDecimalNumberSymbols()
        );
    }

    private final HistoryTokenSaveValueAnchorComponent<DecimalNumberSymbols> undo;

    // close............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
            Optional.of(
                this.context.historyToken()
                    .close()
            )
        );
    }

    private final HistoryTokenAnchorComponent close;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    @Override
    public void dialogReset() {
        this.clear();
    }

    /**
     * Give focus to the AM PM field.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.positiveSign::focus
        );
    }

    /**
     * Refreshes all components from the current {@link DecimalNumberSymbols} which may be the cell or metadata property.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final Optional<DecimalNumberSymbols> decimalNumberSymbols = this.context.loadDecimalNumberSymbols();
        this.decimalNumberSymbols.setValue(decimalNumberSymbols);
        this.refreshDecimalNumberSymbolsComponentsAndSave(decimalNumberSymbols);

        this.refreshCopyDefaults();
        this.refreshClear();
        this.refreshUndo();
        this.refreshClose();
    }

    @Override
    public  boolean shouldLogLifecycleChanges() {
        return DECIMAL_NUMBER_SYMBOLS_DIALOG_COMPONENT;
    }

    // DecimalNumberSymbolsFetcherWatcher...............................................................................

    @Override
    public void onDecimalNumberSymbolsHateosResource(final LocaleTag id,
                                                     final DecimalNumberSymbolsHateosResource locale,
                                                     final AppContext context) {
        // NOP
    }

    @Override
    public void onDecimalNumberSymbolsHateosResourceSet(final String localeStartsWith,
                                                        final DecimalNumberSymbolsHateosResourceSet symbols,
                                                        final AppContext context) {
        this.localeLoad.suggestBoxComponent()
            .setOptions(
                symbols.stream()
                    .map(LocaleComponentSuggestionsValue::fromDecimalNumberSymbolsHateosResource)
                    .collect(Collectors.toList())
            );
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    // eventually refresh will read the updated *CELL* from the cache
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(this.context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // Ignore many
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
