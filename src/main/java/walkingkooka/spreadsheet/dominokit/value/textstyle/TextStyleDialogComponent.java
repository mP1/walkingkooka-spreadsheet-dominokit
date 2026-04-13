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
import walkingkooka.spreadsheet.dominokit.value.spreadsheetexpressionreference.SpreadsheetExpressionReferenceComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textalign.TextAlignComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration.TextDecorationLineComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.verticalalign.VerticalAlignComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.text.TextStyle;

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

        this.textAlignComponent = this.textAlignComponent();
        this.textDecorationLineComponent = this.textDecorationLineComponent();
        this.verticalAlignComponent = this.verticalAlignComponent();

       this.textStyle.addValueWatcher2(
           this.textAlignComponent.textStyleValueWatcher()
       );

        this.textStyle.addValueWatcher2(
            this.textDecorationLineComponent.textStyleValueWatcher()
        );

        this.textStyle.addValueWatcher2(
            this.verticalAlignComponent.textStyleValueWatcher()
        );

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
                .appendChild(this.textAlignComponent)
                .appendChild(this.textDecorationLineComponent)
                .appendChild(this.verticalAlignComponent)
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

    // TextAlignComponent...............................................................................................

    private TextAlignComponent textAlignComponent() {
        return TextAlignComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    private TextAlignComponent textAlignComponent;

    // TextDecorationLineComponent......................................................................................

    private TextDecorationLineComponent textDecorationLineComponent() {
        return TextDecorationLineComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    private TextDecorationLineComponent textDecorationLineComponent;

    // VerticalAlignComponent...........................................................................................

    private VerticalAlignComponent verticalAlignComponent() {
        return VerticalAlignComponent.with(
            ID_PREFIX,
            this.context
        );
    }

    private VerticalAlignComponent verticalAlignComponent;

    // TextStyleComponent...............................................................................................

    private TextStyleComponent textStyle() {
        return TextStyleComponent.empty()
            .optional()
            .addValueWatcher2(this.links);
    }

    // @VisibleForTesting
    final TextStyleComponent textStyle;

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

        this.links.setValue(undoTextStyle);
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
