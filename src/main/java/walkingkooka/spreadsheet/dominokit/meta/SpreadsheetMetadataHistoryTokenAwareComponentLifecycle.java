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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetFormComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSaveHistoryToken;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;

/**
 * A wrapper component that watches {@link HistoryToken} and fires lifecycle events to a given {@link SpreadsheetFormComponentLifecycle}.
 */
public final class SpreadsheetMetadataHistoryTokenAwareComponentLifecycle<E extends HTMLElement> implements HistoryTokenAwareComponentLifecycle,
    HtmlComponentDelegator<E, SpreadsheetMetadataHistoryTokenAwareComponentLifecycle<E>> {

    public static <E extends HTMLElement> SpreadsheetMetadataHistoryTokenAwareComponentLifecycle<E> with(final SpreadsheetFormComponentLifecycle<E, ?> form,
                                                                                                         final HistoryContext context) {
        return new SpreadsheetMetadataHistoryTokenAwareComponentLifecycle<>(
            Objects.requireNonNull(form, "form"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetMetadataHistoryTokenAwareComponentLifecycle(final SpreadsheetFormComponentLifecycle<E, ?> form,
                                                                   final HistoryContext context) {
        this.form = form;
        context.addHistoryTokenWatcher(this);
    }

    // ComponentLifecycle...............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken ||
            token instanceof SpreadsheetMetadataPropertyStyleSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        // isAnyOpen test because a dialog belonging to the viewing/editing of a SpreadsheetMetadata property.
        return token instanceof SpreadsheetMetadataHistoryToken &&
            false == DialogComponent.isAnyOpen();
    }

    @Override
    public boolean isOpen() {
        return this.form.isOpen();
    }

    @Override
    public void open(final RefreshContext context) {
        this.form.open(context);
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        final HistoryToken token = context.historyToken();
        if (token instanceof SpreadsheetMetadataPropertySelectHistoryToken) {
            this.form.openGiveFocus(context);
        }
    }

    @Override
    public void refresh(final RefreshContext context) {
        this.form.refresh(context);
    }

    @Override
    public void close(final RefreshContext context) {
        this.form.close(context);
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_METADATA_HISTORY_TOKEN_AWARE_COMPONENT_LIFECYCLE;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false; // query form components not the form itself
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.form.toString();
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HtmlComponent<E, ?> htmlComponent() {
        return this.form;
    }

    private final SpreadsheetFormComponentLifecycle<E, ?> form;

    @Override
    public SpreadsheetMetadataHistoryTokenAwareComponentLifecycle<E> removeCssProperty(final String name) {
        this.form.removeCssProperty(name);
        return this;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }
}
