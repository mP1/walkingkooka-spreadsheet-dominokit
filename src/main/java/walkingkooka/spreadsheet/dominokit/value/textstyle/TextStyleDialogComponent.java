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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.dialog.DialogAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.grid.ThreeColumnComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.spreadsheetexpressionreference.SpreadsheetExpressionReferenceComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.BackgroundColorComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStyleColorComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.direction.DirectionComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontfamily.FontFamilyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontfamily.FontFamilyComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontkerning.FontKerningComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize.FontSizeComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize.FontSizeComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontstretch.FontStretchComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontstyle.FontStyleComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontvariant.FontVariantComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight.BigFontWeightComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.hangingpunctuation.HangingPunctuationComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.height.HeightComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.hyphens.HyphensComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.letterspacing.LetterSpacingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.lineheight.LineHeightComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.margin.BigMarginComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.opacity.OpacityComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.overflow.OverflowComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.overflow.OverflowWrapComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.padding.BigPaddingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.sample.TextStyleSampleComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textalign.TextAlignComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationLineComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationStyleComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationThicknessComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textindent.TextIndentComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textjustify.TextJustifyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textoverflow.BigTextOverflowComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.texttransform.TextTransformComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textwrapping.TextWrappingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.verticalalign.VerticalAlignComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.visibility.VisibilityComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.whitespace.WhitespaceComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.width.WidthComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.wordbreak.WordBreakComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.wordwrap.WordWrapComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.writingmode.WritingModeComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A model dialog that displays a {@link TextStyleComponent} allowing the user to pick a {@link TextStyle}.
 */
public final class TextStyleDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    ComponentLifecycleMatcherDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher {

    /**
     * Creates a new {@link TextStyleDialogComponent}.
     */
    public static TextStyleDialogComponent with(final TextStyleDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new TextStyleDialogComponent(context);
    }

    private TextStyleDialogComponent(final TextStyleDialogComponentContext context) {
        this.context = TextStyleDialogComponentContexts.dialogComponentOpenAware(
            this::isOpen,
            context
        );

        this.links = this.dialogAnchorListComponent(context)
            .setHistoryTokenPreProcessor(
                (HistoryToken historyToken) -> historyToken.clearStylePropertyName()
            ).save()
            .undo()
            .clearLink()
            .close();

        // TextStyle after save because TextStyle passes a method reference to #save
        this.selection = this.selection();
        this.sample = this.sample();
        this.filter = this.filter();
        this.textStyle = this.textStyle();

        this.links.setComponentWithErrors(this.textStyle);

        this.components = Lists.of(
            this.backgroundColorComponent(),
            this.textStyleColorComponent(),
            this.directionComponent(),
            this.fontFamilyComponent(),
            this.fontKerningComponent(),
            this.fontSizeComponent(),
            this.fontStretchComponent(),
            this.fontStyleComponent(),
            this.fontVariantComponent(),
            this.fontWeightComponent(),
            this.hangingPunctuationComponent(),
            this.heightComponent(),
            this.hyphensComponent(),
            this.letterSpacingComponent(),
            this.lineHeightComponent(),
            this.marginComponent(),
            this.opacityComponent(),
            this.overflowXComponent(),
            this.overflowYComponent(),
            this.overflowWrapComponent(),
            this.paddingComponent(),
            this.textAlignComponent(),
            this.textDecorationLineComponent(),
            this.textDecorationStyleComponent(),
            this.textDecorationThicknessComponent(),
            this.textIndentComponent(),
            this.textJustifyComponent(),
            this.textOverflowComponent(),
            this.textTransformComponent(),
            this.textWrappingComponent(),
            this.verticalAlignComponent(),
            this.visibilityComponent(),
            this.whitespaceComponent(),
            this.widthComponent(),
            this.wordBreakComponent(),
            this.wordWrapComponent(),
            this.writingModeComponent()
        );

        this.components.forEach(
            (final TextStylePropertyComponent<?, ?, ?> component) -> {
                component.setLabelFromPropertyName();

                // skip syncing textStyle and components or vice versa if the value change has an error
                // any incomplete edit of a property will be an error until the edit is complete and correct.
                // without the skip the style is cleared and all children are cleared because of this value change
                this.textStyle.addValueWatcherSkipIfErrors2(
                    component.textStyleValueWatcher()
                );

                final TextStylePropertyName<?> propertyName = component.name();

                component.addValueWatcherSkipIfErrors2(
                    new ValueWatcher() {
                        @Override
                        public void onValue(final Optional value) {
                            TextStyleDialogComponent.this.textStyle.pushHistoryTokenIfNecessary(
                                Cast.to(
                                    propertyName.setOrRemoveValue(
                                        value
                                    )
                                ),
                                TextStyleDialogComponent.this.context
                            );
                        }
                    }
                );

                component.addFocusInListener(
                    this.pushHistoryTokenWithTextStylePropertyNameEventListener(propertyName)
                );
            }
        );

        this.textStyle.addValueWatcher2(this.links)
            .addValueWatcher2(this.sample)
            .addFocusInListener(
                this.pushHistoryTokenWithTextStylePropertyNameEventListener(
                    TextStylePropertyName.ALL
                )
            );

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addHistoryTokenWatcher(
            (final HistoryToken previous,
             final AppContext appContext) -> this.giveFocusTextStylePropertyOrTextStyle()
        );
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
    }

    private EventListener pushHistoryTokenWithTextStylePropertyNameEventListener(final TextStylePropertyName<?> propertyName) {
        return (Event event) ->
            this.context.pushHistoryToken(
                this.context.historyToken()
                    .setStylePropertyName(
                        Optional.of(
                            propertyName
                        )
                    )
            );
    }

    // dialog...........................................................................................................

    // SELECTION
    // COLUMN 1 | COLUMN 2 | COLUMN 3
    // TextStyleComponent
    // LINKS
    private DialogComponent dialogCreate() {
        final TextStyleDialogComponentContext context = this.context;

        return DialogComponent.largeEdit(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(
                this.componentsParent.appendChild(
                        this.selection
                    ).appendChild(
                        this.sample
                    ).appendChild(
                        this.filter
                    ).appendChildren(this.components)
            ).appendChild(this.textStyle)
            .appendChild(this.links);
    }

    private final DialogComponent dialog;

    private final TextStyleDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    private final ThreeColumnComponent componentsParent = ThreeColumnComponent.empty();

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = TextStyle.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // selection........................................................................................................

    private SpreadsheetExpressionReferenceComponent selection() {
        return SpreadsheetExpressionReferenceComponent.empty()
            .setId(
                ID_PREFIX + "selection" + SpreadsheetElementIds.TEXT_BOX
            ).setLabel("Selection")
            .addValueWatcher2((Optional<SpreadsheetExpressionReference> value) -> this.pushHistoryTokenWithSelection(
                    t -> t.setSelection(
                        Cast.to(value)
                    ).setStylePropertyName(
                        this.context.historyToken()
                            .stylePropertyName()
                    )
                )
            );
    }

    private final SpreadsheetExpressionReferenceComponent selection;

    /**
     * Updates a component of the current {@link HistoryToken} and then maybe pushes the new {@link HistoryToken}.
     */
    private void pushHistoryTokenWithSelection(final Function<HistoryToken, HistoryToken> historyTokenSetter) {
        final TextStyleDialogComponentContext context = this.context;

        // if setter failed ignore, validation will eventually show an error for the field.
        HistoryToken historyToken;
        try {
            historyToken = historyTokenSetter.apply(
                context.historyToken()
            );
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final RuntimeException ignore) {
            historyToken = null;
        }

        // only update history token if setter was successful.
        if (historyToken instanceof SpreadsheetMetadataPropertyStyleHistoryToken || historyToken instanceof SpreadsheetCellStyleHistoryToken) {
            context.pushHistoryToken(historyToken);
        }
    }

    // sample...........................................................................................................

    private TextStyleSampleComponent sample() {
        return TextStyleSampleComponent.empty()
            .setLabel("Sample");
    }

    private final TextStyleSampleComponent sample;

    // filter...........................................................................................................

    /**
     * This filter uses the text entered to filter components by {@link TextStylePropertyComponent#name()}, only
     * keeping matching components connected to the DOM.
     */
    private TextStylePropertyFilterComponent filter() {
        return TextStylePropertyFilterComponent.with(ID_PREFIX)
            .setLabel("Filter")
            .clearIcon()
            .optional()
            .addValueWatcher2(
                (Optional<TextStylePropertyFilter> filter) -> {
                    final TextStylePropertyFilter filter2 = filter.orElse(
                        TextStylePropertyFilter.ALL
                    );

                    // the first three children are selection, sample, filter never delete them
                    final ThreeColumnComponent parent = this.componentsParent;
                    while(parent.children().size() > 3) {
                        parent.removeChild(3);
                    }

                    for(final TextStylePropertyComponent<?, ?, ?> component : this.components) {
                        if(component.filterTest(filter2)) {
                            parent.appendChild(component);
                        }
                    }
                }
            );
    }

    // @VisibleForTesting
    final TextStylePropertyFilterComponent filter;

    // TextStylePropertyName components.................................................................................

    // BackgroundColorComponent.........................................................................................

    private BackgroundColorComponent backgroundColorComponent() {
        return BackgroundColorComponent.with(
            ID_PREFIX,
            this.context
        ).optional();
    }

    // TextStyleColorComponent.........................................................................................

    private TextStyleColorComponent textStyleColorComponent() {
        return TextStyleColorComponent.with(
            ID_PREFIX,
            this.context
        ).optional();
    }
    
    // DirectionComponent...............................................................................................

    private DirectionComponent directionComponent() {
        return DirectionComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontFamilyComponent.............................................................................................

    private FontFamilyComponent fontFamilyComponent() {
        return FontFamilyComponent.empty(
            ID_PREFIX,
            FontFamilyComponentContexts.historyContext(
                Lists.of(
                    FontFamily.with("Courier"),
                    FontFamily.with("Sans Serif"),
                    FontFamily.with("Times New Roman")
                ),
                this.context
            )
        );
    }
    
    // FontKerningComponent.............................................................................................

    private FontKerningComponent fontKerningComponent() {
        return FontKerningComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontSizeComponent................................................................................................

    private FontSizeComponent fontSizeComponent() {
        return FontSizeComponent.empty(
            FontSizeComponentContexts.historyContext(this.context)
        ).optional();
    }
    
    // FontStretchComponent.............................................................................................

    private FontStretchComponent fontStretchComponent() {
        return FontStretchComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontStyleComponent...............................................................................................

    private FontStyleComponent fontStyleComponent() {
        return FontStyleComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontVariantComponent.............................................................................................

    private FontVariantComponent fontVariantComponent() {
        return FontVariantComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontWeightComponent.............................................................................................

    private BigFontWeightComponent fontWeightComponent() {
        return BigFontWeightComponent.with(
                ID_PREFIX + "fontWeight-",
                this.context
            ).optional();
    }

    // HangingPunctuationComponent......................................................................................

    private HangingPunctuationComponent hangingPunctuationComponent() {
        return HangingPunctuationComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // HeightComponent..................................................................................................

    private HeightComponent heightComponent() {
        return HeightComponent.empty(ID_PREFIX)
            .optional();
    }
    
    // HyphensComponent..................................................................................................

    private HyphensComponent hyphensComponent() {
        return HyphensComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // LetterSpacingComponent...........................................................................................

    private LetterSpacingComponent letterSpacingComponent() {
        return LetterSpacingComponent.empty(ID_PREFIX)
            .optional();
    }

    // LineHeightComponent..............................................................................................

    private LineHeightComponent lineHeightComponent() {
        return LineHeightComponent.empty(ID_PREFIX)
            .optional();
    }

    // BigMarginComponent...............................................................................................

    private BigMarginComponent marginComponent() {
        return BigMarginComponent.with(ID_PREFIX)
            .optional();
    }

    // OpacityComponent.................................................................................................

    private OpacityComponent opacityComponent() {
        return OpacityComponent.with(ID_PREFIX)
            .optional();
    }
    
    // OverflowWrapComponent............................................................................................

    private OverflowWrapComponent overflowWrapComponent() {
        return OverflowWrapComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // OverflowComponent................................................................................................

    private OverflowComponent overflowXComponent() {
        return OverflowComponent.overflowX(
            ID_PREFIX,
            this.context
        );
    }

    // OverflowComponent................................................................................................

    private OverflowComponent overflowYComponent() {
        return OverflowComponent.overflowY(
            ID_PREFIX,
            this.context
        );
    }

    // BigPaddingComponent..............................................................................................

    private BigPaddingComponent paddingComponent() {
        return BigPaddingComponent.with(ID_PREFIX)
            .optional();
    }
    
    // TextAlignComponent...............................................................................................

    private TextAlignComponent textAlignComponent() {
        return TextAlignComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextDecorationLineComponent......................................................................................

    private TextDecorationLineComponent textDecorationLineComponent() {
        return TextDecorationLineComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextDecorationStyleComponent......................................................................................

    private TextDecorationStyleComponent textDecorationStyleComponent() {
        return TextDecorationStyleComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextDecorationThicknessComponent.................................................................................

    private TextDecorationThicknessComponent textDecorationThicknessComponent() {
        return TextDecorationThicknessComponent.empty(
            ID_PREFIX
        ).optional();
    }

    // TextIndentComponent..............................................................................................

    private TextIndentComponent textIndentComponent() {
        return TextIndentComponent.empty(ID_PREFIX)
            .optional();
    }
    
    // TextJustifyComponent.............................................................................................

    private TextJustifyComponent textJustifyComponent() {
        return TextJustifyComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextOverflowComponent.............................................................................................

    private BigTextOverflowComponent textOverflowComponent() {
        return BigTextOverflowComponent.with(
            ID_PREFIX,
            this.context
        ).optional();
    }

    // TextTransformComponent...........................................................................................

    private TextTransformComponent textTransformComponent() {
        return TextTransformComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextWrappingComponent............................................................................................

    private TextWrappingComponent textWrappingComponent() {
        return TextWrappingComponent.with(
            ID_PREFIX,
            this.context
        );
    }
    
    // VerticalAlignComponent...........................................................................................

    private VerticalAlignComponent verticalAlignComponent() {
        return VerticalAlignComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // VisibilityComponent..............................................................................................

    private VisibilityComponent visibilityComponent() {
        return VisibilityComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // WidthComponent...................................................................................................

    private WidthComponent widthComponent() {
        return WidthComponent.empty(ID_PREFIX)
            .optional();
    }
    
    // WhitespaceComponent..............................................................................................

    private WhitespaceComponent whitespaceComponent() {
        return WhitespaceComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // WordBreakComponent...............................................................................................

    private WordBreakComponent wordBreakComponent() {
        return WordBreakComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // WordWrapComponent................................................................................................

    private WordWrapComponent wordWrapComponent() {
        return WordWrapComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // WritingModeComponent.............................................................................................

    private WritingModeComponent writingModeComponent() {
        return WritingModeComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // TextStyleComponent...............................................................................................

    private TextStyleComponent textStyle() {
        return TextStyleComponent.empty()
            .optional()
            .addValueWatcher2(this.links)
            .setLabel("Style");
    }

    // @VisibleForTesting
    final TextStyleComponent textStyle;

    final List<TextStylePropertyComponent<?, ?, ?>> components;

    // links............................................................................................................

    private final DialogAnchorListComponent<TextStyle> links;

    // HistoryTokenAwareComponentLifecycle..............................................................................

    @Override
    public void dialogReset() {
        this.textStyle.clear();
    }

    /**
     * Give focus to the {@link TextStyleComponent}.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        this.selection.setValue(
            Cast.to(
                context.historyToken()
                    .selection()
            )
        );

        // clear filter - make all components "appear"
        this.filter.setValue(Optional.empty());
    }

    /**
     * Gives focus to the {@link TextStylePropertyComponent} mentioned in the {@link HistoryToken#stylePropertyName()}.
     */
    private void giveFocusTextStylePropertyOrTextStyle() {

        // dont run logic if this dialog is closed - there are two dialogs matching different history tokens
        if (this.isOpen()) {
            final TextStyleDialogComponentContext context = this.context;
            final HistoryToken historyToken = context.historyToken();

            // save history tokens probably include TextStylePropertyName.* which is prolly different if any property
            // is selected
            if (false == historyToken.isSave()) {
                FormValueComponent<?, ?, ?> giveFocus = this.textStyle;

                final TextStylePropertyName<?> propertyNameOrNull = historyToken.stylePropertyName()
                    .orElse(null);

                if (null != propertyNameOrNull) {
                    final TextStylePropertyFilter filter = this.filter.value()
                        .orElse(TextStylePropertyFilter.ALL);

                    for (final TextStylePropertyComponent<?, ?, ?> component : this.components) {
                        if (false == component.name().equals(propertyNameOrNull)) {
                            continue;
                        }

                        if (filter.testComponent(component)) {
                            giveFocus = component;
                        }
                        break;
                    }
                }

                context.giveFocus(giveFocus::focus);
            }
        }
    }

    /**
     * Refreshes all components using the context.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        this.selection.setValue(
            Cast.to(
                context.historyToken()
                    .selection()
            )
        );

        final Optional<TextStyle> undoTextStyle = this.context.undo();

        this.textStyle.setValue(undoTextStyle);
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_TEXT_STYLE_COMPONENT_LIFECYCLE;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        this.refreshIfOpen(this.context);
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // Ignore many
    }

    // ComponentLifecycleMatcherDelegator...............................................................................

    @Override
    public ComponentLifecycleMatcher componentLifecycleMatcher() {
        return this.context;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
