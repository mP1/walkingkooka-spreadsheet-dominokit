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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
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
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.spreadsheetexpressionreference.SpreadsheetExpressionReferenceComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.direction.DirectionComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontkerning.FontKerningComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontstretch.FontStretchComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontstyle.FontStyleComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.fontvariant.FontVariantComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.hangingpunctuation.HangingPunctuationComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.hyphens.HyphensComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.overflow.OverflowComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.overflow.OverflowWrapComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textalign.TextAlignComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationLineComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationStyleComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textjustify.TextJustifyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.texttransform.TextTransformComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textwrapping.TextWrappingComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.verticalalign.VerticalAlignComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.visibility.VisibilityComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.whitespace.WhitespaceComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.wordbreak.WordBreakComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.wordwrap.WordWrapComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.writingmode.WritingModeComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.text.TextStyle;

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
        this.context = context;

        this.links = this.dialogAnchorListComponent(context)
            .save()
            .undo()
            .clearLink()
            .close();

        // TextStyle after save because TextStyle passes a method reference to #save
        this.selection = this.selection();
        this.textStyle = this.textStyle();

        this.links.setComponentWithErrors(this.textStyle);

        this.components = Lists.of(
            this.directionComponent(),
            this.fontKerningComponent(),
            this.fontStretchComponent(),
            this.fontStyleComponent(),
            this.fontVariantComponent(),
            this.hangingPunctuationComponent(),
            this.hyphensComponent(),
            this.overflowXComponent(),
            this.overflowYComponent(),
            this.overflowWrapComponent(),
            this.textAlignComponent(),
            this.textDecorationLineComponent(),
            this.textDecorationStyleComponent(),
            this.textJustifyComponent(),
            this.textTransformComponent(),
            this.textWrappingComponent(),
            this.verticalAlignComponent(),
            this.visibilityComponent(),
            this.whitespaceComponent(),
            this.wordBreakComponent(),
            this.wordWrapComponent(),
            this.writingModeComponent()
        );

        this.components.forEach(
            (FormValueComponent<?, ?, ?> component) -> {
                final ValueWatcher<TextStyle> valueWatcher;
                if(component instanceof HasTextStyleValueWatcherValueWatcher){
                    valueWatcher = ((HasTextStyleValueWatcherValueWatcher) component)
                        .textStyleValueWatcher();
                } else {
                    throw new UnsupportedOperationException(component.getClass().getSimpleName() + " " + component.toString());
                }

                this.textStyle.addValueWatcher2(valueWatcher);
            }
        );

        this.textStyle.addValueWatcher2(this.links);

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
    }

    // dialog...........................................................................................................

    private DialogComponent dialogCreate() {
        final TextStyleDialogComponentContext context = this.context;

        return DialogComponent.smallerPrompt(
            ID + SpreadsheetElementIds.DIALOG,
            DialogComponent.INCLUDE_CLOSE,
            context
        ).appendChild(
            FlexLayoutComponent.row()
                .appendChild(this.selection)
                .appendChildren(this.components)
                .appendChild(this.textStyle)
        ).appendChild(this.links);
    }

    private final DialogComponent dialog;

    private final TextStyleDialogComponentContext context;

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

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
            .addValueWatcher2((Optional<SpreadsheetExpressionReference> value) -> {
                    this.setAndPushHistoryToken(
                        t -> t.setSelection(
                            Cast.to(value)
                        ).setStylePropertyName(
                            this.context.historyToken()
                                .stylePropertyName()
                        )
                    );
                }
            );
    }

    private final SpreadsheetExpressionReferenceComponent selection;

    // TextStylePropertyName components.................................................................................

    // DirectionComponent...............................................................................................

    private DirectionComponent directionComponent() {
        return DirectionComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // FontKerningComponent.............................................................................................

    private FontKerningComponent fontKerningComponent() {
        return FontKerningComponent.with(
            ID_PREFIX,
            this.context
        );
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

    // HangingPunctuationComponent......................................................................................

    private HangingPunctuationComponent hangingPunctuationComponent() {
        return HangingPunctuationComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    // HyphensComponent..................................................................................................

    private HyphensComponent hyphensComponent() {
        return HyphensComponent.with(
            ID_PREFIX,
            this.context
        );
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

    // TextJustifyComponent.............................................................................................

    private TextJustifyComponent textJustifyComponent() {
        return TextJustifyComponent.with(
            ID_PREFIX,
            this.context
        );
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

    final List<FormValueComponent<?, ?, ?>> components;

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
        context.giveFocus(
            this.textStyle::focus
        );

        this.selection.setValue(
            Cast.to(
                context.historyToken()
                    .selection()
            )
        );
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

    /**
     * Updates a component of the current {@link HistoryToken} and then maybe pushes the new {@link HistoryToken}.
     */
    private void setAndPushHistoryToken(final Function<HistoryToken, HistoryToken> historyTokenSetter) {
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
}
