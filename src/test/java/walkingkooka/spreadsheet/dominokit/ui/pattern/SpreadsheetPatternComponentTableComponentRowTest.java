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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetPatternComponentTableComponentRowTest implements ClassTesting<SpreadsheetPatternComponentTableComponentRow>,
        HashCodeEqualsDefinedTesting2<SpreadsheetPatternComponentTableComponentRow>,
        ToStringTesting<SpreadsheetPatternComponentTableComponentRow> {

    private final static String LABEL = "Label123";

    private final static Optional<SpreadsheetPattern> PATTERN = Optional.of(
            SpreadsheetPattern.parseTextFormatPattern("@")
    );

    private final static List<TextNode> FORMATTED = Lists.of(
            SpreadsheetText.with("formatted")
                    .toTextNode()
    );

    // dateFormat.......................................................................................................

    @Test
    public void testWithNullLabelFails() {
        this.withFails(
                null,
                PATTERN,
                FORMATTED
        );
    }

    @Test
    public void testWithEmptyLabelFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetPatternComponentTableComponentRow.with(
                        "",
                        PATTERN,
                        FORMATTED
                )
        );
    }

    @Test
    public void testWithNullPatternFails() {
        this.withFails(
                LABEL,
                null,
                FORMATTED
        );
    }

    @Test
    public void testWithNullFormattedFails() {
        this.withFails(
                LABEL,
                PATTERN,
                null
        );
    }

    private void withFails(final String label,
                           final Optional<SpreadsheetPattern> pattern,
                           final List<TextNode> formatted) {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetPatternComponentTableComponentRow.with(
                        label,
                        pattern,
                        formatted
                )
        );
    }
    // equals............................................................................................................

    @Test
    public void testEqualsDifferentLabel() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        "different",
                        PATTERN,
                        FORMATTED
                )
        );
    }

    @Test
    public void testEqualsDifferentPattern() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.of(
                                SpreadsheetPattern.parseTextFormatPattern("\"different\"")
                        ),
                        FORMATTED
                )
        );
    }

    @Test
    public void testEqualsDifferentFormatted() {
        this.checkNotEquals(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        PATTERN,
                        Lists.of(
                                SpreadsheetText.with("different")
                                        .toTextNode()
                        )
                )
        );
    }

    @Override
    public SpreadsheetPatternComponentTableComponentRow createObject() {
        return SpreadsheetPatternComponentTableComponentRow.with(
                LABEL,
                PATTERN,
                FORMATTED
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        PATTERN,
                        FORMATTED
                ),
                "Label123 | @ | formatted"
        );
    }

    @Test
    public void testToStringEmptyPattern() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.empty(),
                        FORMATTED
                ),
                "Label123 | | formatted"
        );
    }

    @Test
    public void testToStringAllEmpty() {
        this.toStringAndCheck(
                SpreadsheetPatternComponentTableComponentRow.with(
                        LABEL,
                        Optional.empty(),
                        Lists.of(
                                SpreadsheetText.EMPTY.toTextNode()
                        )
                ),
                "Label123 | |"
        );
    }

    // ClassVisibility..................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentTableComponentRow> type() {
        return SpreadsheetPatternComponentTableComponentRow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
