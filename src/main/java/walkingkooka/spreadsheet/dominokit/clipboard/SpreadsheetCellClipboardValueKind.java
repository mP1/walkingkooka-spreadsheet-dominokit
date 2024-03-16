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

package walkingkooka.spreadsheet.dominokit.clipboard;

import walkingkooka.collect.list.Lists;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.HasMediaType;
import walkingkooka.net.header.MediaType;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellClipboardHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

import java.util.Arrays;
import java.util.Objects;


/**
 * Used to mark the type of the clipboard value in a {@link SpreadsheetCellClipboardHistoryToken}.
 * For the moment this only supports possible cell values from the {@link walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportComponent},
 * which means the clipboard can only holds cells or components of a cell such as the formula text.
 */
public enum SpreadsheetCellClipboardValueKind implements HasMediaType,
        HasUrlFragment {

    /**
     * The clipboard value is {@link SpreadsheetCell}.
     */
    CELL(
            SpreadsheetCell.class,
            "cell"
    ),

    /**
     * The clipboard value is cells to {@link String formula text}.
     */
    FORMULA(
            SpreadsheetFormula.class,
            "formula"
    ),

    /**
     * The clipboard value is cells to {@link SpreadsheetFormatPattern}.
     */
    FORMAT_PATTERN(
            SpreadsheetFormatPattern.class,
            "format-pattern"
    ),

    /**
     * The clipboard value is a cells to {@link SpreadsheetParsePattern}.
     */
    PARSE_PATTERN(
            SpreadsheetParsePattern.class,
            "parse-pattern"
    ),

    /**
     * The clipboard value is cells to {@link TextStyle}.
     */
    STYLE(
            TextStyle.class,
            "style"
    ),

    /**
     * The clipboard value is a formatted text.
     */
    FORMATTED(
            TextNode.class,
            "formatted"
    );

    SpreadsheetCellClipboardValueKind(final Class<?> type,
                                      final String urlFragment) {
        this.mediaType = MediaType.APPLICATION_JSON.setSuffixes(
                Lists.of(
                        "walkingkooka.spreadsheet.dominokit",
                        type.getName()
                )
        );
        this.mediaTypeClass = type;
        this.urlFragment = UrlFragment.parse(urlFragment);
    }

    // HasMediaType.....................................................................................................

    @Override
    public MediaType mediaType() {
        return this.mediaType;
    }

    private final MediaType mediaType;

    /**
     * Getter that returns the class type for this {@link MediaType}.
     */
    public Class<?> mediaTypeClass() {
        return this.mediaTypeClass;
    }

    private Class<?> mediaTypeClass;

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        return this.urlFragment;
    }

    private final transient UrlFragment urlFragment;

    public static SpreadsheetCellClipboardValueKind parse(final String string) {
        Objects.requireNonNull(string, "string");

        return Arrays.stream(values())
                .filter(e -> e.urlFragment.value().equals(string))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid cell clipboard selector: " + CharSequences.quote(string)));
    }
}
