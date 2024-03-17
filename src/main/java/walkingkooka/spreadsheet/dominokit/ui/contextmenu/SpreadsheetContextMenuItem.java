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

package walkingkooka.spreadsheet.dominokit.ui.contextmenu;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Optional;

/**
 * Captures the properties used to build a single context menu item.
 */
public final class SpreadsheetContextMenuItem implements TreePrintable {

    public static SpreadsheetContextMenuItem with(final String id,
                                                  final String text) {
        CharSequences.failIfNullOrEmpty(id, "id");
        if (false == id.endsWith(SpreadsheetIds.MENU_ITEM)) {
            throw new IllegalArgumentException(
                    "Invalid menu item id " +
                            CharSequences.quote(id) +
                            " missing " +
                            CharSequences.quote(SpreadsheetIds.MENU_ITEM)
            );
        }
        CharSequences.failIfNullOrEmpty(text, "text");

        return new SpreadsheetContextMenuItem(
                id,
                text,
                Optional.empty(), // badge
                Optional.empty(), // historyToken
                Optional.empty(), // icon
                "", // key
                false, // checked
                true // enabled
        );
    }

    SpreadsheetContextMenuItem(final String id,
                               final String text,
                               final Optional<String> badge,
                               final Optional<HistoryToken> historyToken,
                               final Optional<Icon<?>> icon,
                               final String key,
                               final boolean checked,
                               final boolean enabled) {
        this.id = id;
        this.text = text;
        this.badge = badge;
        this.historyToken = historyToken;
        this.icon = icon;
        this.key = key;
        this.checked = checked;
        this.enabled = enabled;
    }

    final String id;

    final String text;

    public SpreadsheetContextMenuItem badge(final Optional<String> badge) {
        Objects.requireNonNull(badge, "badge");

        return this.badge.equals(badge) ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        badge,
                        this.historyToken,
                        this.icon,
                        this.key,
                        this.checked,
                        this.enabled
                );
    }

    final Optional<String> badge;

    public SpreadsheetContextMenuItem historyToken(final Optional<HistoryToken> historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        return this.historyToken.equals(historyToken) ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        historyToken,
                        this.icon,
                        this.key,
                        this.checked,
                        this.enabled
                );
    }

    final Optional<HistoryToken> historyToken;

    public SpreadsheetContextMenuItem icon(final Optional<Icon<?>> icon) {
        Objects.requireNonNull(icon, "icon");

        return this.icon.equals(icon) ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        this.historyToken,
                        icon,
                        this.key,
                        this.checked,
                        this.enabled
                );
    }

    final Optional<Icon<?>> icon;

    public SpreadsheetContextMenuItem key(final String key) {
        Objects.requireNonNull(key, "key");

        return this.key.equals(key) ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        this.historyToken,
                        this.icon,
                        key,
                        this.checked,
                        this.enabled
                );
    }

    String key;

    public SpreadsheetContextMenuItem checked(final boolean checked) {

        return this.checked == checked ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        this.historyToken,
                        this.icon,
                        this.key,
                        checked,
                        this.enabled
                );
    }

    final boolean checked;

    /**
     * May be used to enable/disable a {@link SpreadsheetContextMenuItem}.
     */
    public SpreadsheetContextMenuItem enabled(final boolean enabled) {
        return this.enabled == enabled ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        this.historyToken,
                        this.icon,
                        this.key,
                        this.checked,
                        enabled
                );
    }

    final boolean enabled;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.id)
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.key)
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.text)
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.badge)
                .value(this.historyToken)
                .value(this.icon.map(Icon::getName))
                .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .label("disabled")
                .value(false == this.enabled)
                .label("checked")
                .value(this.checked)
                .build();
    }

    // TreePrintable...................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        final Optional<Icon<?>> icon = this.icon;
        if (icon.isPresent()) {
            printer.print("(");
            printer.print(icon.get().getName());
            printer.print(") ");
        }

        printer.print(this.id);
        printer.print(" ");

        final String key = this.key;
        if (false == key.isEmpty()) {
            printer.print(key);
            printer.print(" ");
        }

        printer.print(CharSequences.quoteAndEscape(this.text));

        final Optional<HistoryToken> token = this.historyToken;
        if (token.isPresent()) {
            printer.print(" [");

            if (false == this.enabled) {
                printer.print("disabled ");
            }

            printer.print(token.get().urlFragment().toString());
            printer.print("]");
        }

        if (this.checked) {
            printer.print(" v/");
        }

        final Optional<String> badge = this.badge;
        if (badge.isPresent()) {
            printer.print(" [");
            printer.print(badge.get());
            printer.print("]");
        }

        printer.println();
    }
}
