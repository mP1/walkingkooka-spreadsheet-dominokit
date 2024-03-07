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

import java.util.Objects;
import java.util.Optional;

/**
 * Captures the properties used to build a single context menu item.
 */
public final class SpreadsheetContextMenuItem {

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
                false // checked
        );
    }

    SpreadsheetContextMenuItem(final String id,
                               final String text,
                               final Optional<String> badge,
                               final Optional<HistoryToken> historyToken,
                               final Optional<Icon<?>> icon,
                               final boolean checked) {
        this.id = id;
        this.text = text;
        this.badge = badge;
        this.historyToken = historyToken;
        this.icon = icon;
        this.checked = checked;
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
                        this.checked
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
                        this.checked
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
                        this.checked
                );
    }

    final Optional<Icon<?>> icon;

    public SpreadsheetContextMenuItem checked(final boolean checked) {

        return this.checked == checked ?
                this :
                new SpreadsheetContextMenuItem(
                        this.id,
                        this.text,
                        this.badge,
                        this.historyToken,
                        this.icon,
                        checked
                );
    }

    final boolean checked;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.id)
                .enable(ToStringBuilderOption.QUOTE)
                .value(this.text)
                .disable(ToStringBuilderOption.QUOTE)
                .value(this.badge)
                .value(this.historyToken)
                .value(this.icon)
                .label("checked")
                .value(this.checked)
                .build();
    }
}
