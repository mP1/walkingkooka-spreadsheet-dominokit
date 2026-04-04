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

package walkingkooka.spreadsheet.dominokit.dialog;

import elemental2.dom.HTMLDivElement;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentRefreshable;
import walkingkooka.spreadsheet.dominokit.ComponentWithErrors;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A collection of common links for CRUD like {@link walkingkooka.spreadsheet.dominokit.dialog.DialogComponent},
 * with links such as SAVE, UNDO, CLEAR, CLOSE with "clear" being optional.
 */
public final class DialogAnchorListComponent<T> implements HtmlComponentDelegator<HTMLDivElement, DialogAnchorListComponent<T>>,
    ValueComponent<HTMLDivElement, T, DialogAnchorListComponent<T>>,
    DialogComponentAnchors,
    HistoryTokenWatcher,
    ComponentRefreshable,
    ValueWatcher<T> {

    public static <T> DialogAnchorListComponent<T> empty(final String idPrefix,
                                                         final DialogAnchorListComponentContext<T> context) {
        return new DialogAnchorListComponent<>(
            CharSequences.failIfNullOrEmpty(idPrefix, "idPrefix"),
            Objects.requireNonNull(context, "context")
        );
    }

    private DialogAnchorListComponent(final String idPrefix,
                                      final DialogAnchorListComponentContext<T> context) {
        super();

        this.idPrefix = idPrefix;
        this.list = AnchorListComponent.empty();
        this.value = Optional.empty();

        context.addHistoryTokenWatcher(this);
        this.context = context;
    }

    @Override
    public String idPrefix() {
        return this.idPrefix;
    }

    private final String idPrefix;

    /**
     * Sets the {@link ComponentWithErrors} which will provide errors, whenever a new value is {@link #setValue(Optional)}
     * or via {@link #onValue(Optional)}. If one or more errors are present the {@link #save} will be disabled.
     */
    public DialogAnchorListComponent<T> setComponentWithErrors(final ComponentWithErrors<?> hasErrors) {
        Objects.requireNonNull(hasErrors, "hasErrors");

        this.hasErrors = hasErrors;
        this.refreshList();
        return this;
    }

    private ComponentWithErrors<?> hasErrors;

    // list.............................................................................................................

    /**
     * Refresh the list in the following order, provided they have been added: save, clear, undo, appended children and then close
     */
    private void refreshList() {
        this.list.removeAllChildren();

        this.appendChildIfNotNull(this.save);
        this.appendChildIfNotNull(this.clear);
        this.appendChildIfNotNull(this.undo);

        this.children.forEach(
            this.list::appendChild
        );

        this.appendChildIfNotNull(this.close);

        this.refreshClearUndoClose();
    }

    private void appendChildIfNotNull(final AnchorComponent<?> child) {
        if (null != child) {
            this.list.appendChild(child);
        }
    }

    /**
     * The parent of all {@link AnchorComponent}.
     */
    private final AnchorListComponent list;

    // @VisibleForTesting
    final DialogAnchorListComponentContext<T> context;

    // save.............................................................................................................

    public DialogAnchorListComponent<T> save() {
        if (null == this.save) {
            this.save = this.saveValueAnchor(this.context);

            this.refreshList();
        }
        return this;
    }

    public DialogAnchorListComponent<T> saveAutoDisableWhenMissingValue() {
        this.save();
        this.save.autoDisableWhenMissingValue();
        return this;
    }

    /**
     * Disable the save link, temporarily, until the next {@link #setValue(Optional).}
     * This is useful when another component has errors and the save link should be disabled.
     */
    public DialogAnchorListComponent<T> disableSave() {
        this.save();

        this.save.setDisabled(true);
        return this;
    }

    /**
     * A SAVE link.
     */
    private HistoryTokenSaveValueAnchorComponent<T> save;

    // clear............................................................................................................

    public DialogAnchorListComponent<T> clearLink() {
        if (null == this.clear) {
            this.clear = this.clearValueAnchor(this.context);
            this.refreshList();
        }
        return this;
    }

    /**
     * A CLEAR link which will save an empty {@link T}.
     */
    private HistoryTokenSaveValueAnchorComponent<T> clear;

    // undo.............................................................................................................

    public DialogAnchorListComponent<T> undo() {
        if (null == this.undo) {
            this.undo = this.undoAnchor(this.context);
            this.refreshList();
        }
        return this;
    }

    /**
     * A UNDO link which will be updated each time the {@link T} is saved.
     */
    private HistoryTokenSaveValueAnchorComponent<T> undo;

    // close.............................................................................................................

    public DialogAnchorListComponent<T> close() {
        if (null == this.close) {
            this.close = this.closeAnchor();
            this.refreshList();
        }
        return this;
    }

    /**
     * A CLOSE link which will close the dialog.
     */
    private HistoryTokenAnchorComponent close;

    // appendChild......................................................................................................

    public DialogAnchorListComponent<T> appendChild(final AnchorComponent<?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        this.refreshList();
        return this;
    }

    private final List<AnchorComponent<?>> children = Lists.array();

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.setValue(this.value);
        this.refreshClearUndoClose();
    }

    // Value............................................................................................................

    @Override
    public Optional<T> value() {
        return this.value;
    }

    @Override
    public DialogAnchorListComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;

        final HistoryTokenSaveValueAnchorComponent<T> save = this.save;
        if (null != save) {
            final List<String> errors;
            final ComponentWithErrors<?> hasErrors = this.hasErrors;
            if (null != hasErrors) {
                errors = hasErrors.errors();
            } else {
                errors = Lists.empty();
            }

            if (errors.isEmpty()) {
                save.setValue(value);
            } else {
                save.disabled();
            }
        }
        this.refreshClearUndoClose();
        return this;
    }

    /**
     * Cached copy because {@link #save} may be null.
     */
    private Optional<T> value;

    // refreshXXX.......................................................................................................

    private void refreshClearUndoClose() {
        // #clear is optional so null test before "updating"
        final HistoryTokenSaveValueAnchorComponent<T> clear = this.clear;
        if (null != clear) {
            this.clear.clearValue();
        }

        final DialogAnchorListComponentContext<T> context = this.context;

        final HistoryTokenSaveValueAnchorComponent<T> undo = this.undo;
        if (null != undo) {
            undo.setValue(
                context.undo()
            );
        }

        final HistoryTokenAnchorComponent close = this.close;
        if (null != close) {
            close.setHistoryToken(
                Optional.of(
                    context.historyToken()
                        .close()
                )
            );
        }
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.list.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.list;
    }

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        this.refreshClearUndoClose();
    }

    // ValueComponent...................................................................................................

    @Override
    public Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public DialogAnchorListComponent<T> setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogAnchorListComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogAnchorListComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogAnchorListComponent<T> removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogAnchorListComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogAnchorListComponent<T> blur() {
        throw new UnsupportedOperationException();
    }

    // ValueWatcher.....................................................................................................

    @Override
    public void onValue(final Optional<T> value) {
        this.setValue(value);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.list.printTree(printer);
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof DialogAnchorListComponent &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final DialogAnchorListComponent<?> other) {
        return this.list.equals(other.list);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.list)
            .build();
    }
}
