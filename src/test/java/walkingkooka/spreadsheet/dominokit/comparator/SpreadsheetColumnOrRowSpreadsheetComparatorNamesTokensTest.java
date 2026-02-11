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

package walkingkooka.spreadsheet.dominokit.comparator;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokensTest implements ParseStringTesting<SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens>,
    HashCodeEqualsDefinedTesting2<SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens>,
    HasTextTesting,
    ToStringTesting<SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens>,
    ClassTesting<SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens> {

    private final static Optional<SpreadsheetColumnOrRowReferenceOrRange> COLUMN_OR_ROW = Optional.of(
        SpreadsheetSelection.A1.column()
    );
    private final static List<SpreadsheetComparatorName> COMPARATOR_NAMES = Lists.of(
        SpreadsheetComparatorName.NUMBER,
        SpreadsheetComparatorName.TEXT
    );
    private final static String TEXT = "text1";

    // with.............................................................................................................

    @Test
    public void testWithNullColumnOrRowFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                null,
                COMPARATOR_NAMES,
                TEXT
            )
        );
    }

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                null,
                TEXT
            )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                COMPARATOR_NAMES,
                null
            )
        );
    }

    @Test
    public void testWithEmptyColumnOrRowAndNotEmptyComparatorNamesFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW,
                COMPARATOR_NAMES,
                TEXT
            )
        );
    }

    @Test
    public void testWithEmptyColumnOrRows() {
        final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW;
        final List<SpreadsheetComparatorName> comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES;

        this.check(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                columnOrRow,
                comparatorNames,
                TEXT
            ),
            columnOrRow,
            comparatorNames,
            TEXT
        );
    }

    @Test
    public void testWithEmptyComparatorNames() {
        final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow = COLUMN_OR_ROW;
        final List<SpreadsheetComparatorName> comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES;

        this.check(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                columnOrRow,
                comparatorNames,
                TEXT
            ),
            columnOrRow,
            comparatorNames,
            TEXT
        );
    }

    private void check(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens tokens,
                       final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                       final List<SpreadsheetComparatorName> comparatorNames,
                       final String text) {
        this.checkEquals(
            columnOrRow,
            tokens.columnOrRow(),
            "columnOrRow"
        );
        this.checkEquals(
            comparatorNames,
            tokens.comparatorNames(),
            "comparatorNames"
        );
        this.textAndCheck(
            tokens,
            text
        );
    }

    // setText..........................................................................................................

    @Test
    public void testSetTextWithSame() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens tokens = this.createObject();

        assertSame(
            tokens,
            tokens.setText(TEXT)
        );
    }

    @Test
    public void testSetTextWithDifferent() {
        final String differentText = "different";
        checkNotEquals(
            TEXT,
            differentText
        );

        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens tokens = this.createObject();
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens different = tokens.setText(differentText);
        assertNotSame(
            tokens,
            different
        );

        this.check(
            different,
            COLUMN_OR_ROW,
            COMPARATOR_NAMES,
            differentText
        );
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseStringEmpty() {
        this.parseStringAndCheck(
            "",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Test
    public void testParseInvalidColumnOrRow() {
        final String text = "! text";

        this.parseStringAndCheck(
            text,
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                text
            )
        );
    }

    @Test
    public void testParseInvalidColumnOrRow2() {
        final String text = "c!=text";

        this.parseStringAndCheck(
            text,
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                text
            )
        );
    }

    @Test
    public void testParseInvalidFirstComparatorName() {
        this.parseStringAndCheck(
            "A=!invalid",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.column()
                ),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                "!invalid"
            )
        );
    }

    @Test
    public void testParseInvalidSecondComparatorName() {
        this.parseStringAndCheck(
            "A=text,!invalid",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.column()
                ),
                Lists.of(
                    SpreadsheetComparatorName.TEXT
                ),
                "!invalid"
            )
        );
    }

    @Test
    public void testParseColumn() {
        this.parseStringAndCheck(
            "A",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.column()
                ),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Test
    public void testParseColumn2() {
        this.parseStringAndCheck(
            "AB",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.parseColumn("AB")
                ),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Test
    public void testParseColumnAndComparatorNames() {
        this.parseStringAndCheck(
            "A=text,number",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.column()
                ),
                Lists.of(
                    SpreadsheetComparatorName.TEXT,
                    SpreadsheetComparatorName.NUMBER
                ),
                ""
            )
        );
    }

    @Test
    public void testParseColumnAndComparatorNames2() {
        this.parseStringAndCheck(
            "ABC=text,number",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.parseColumn("ABC")
                ),
                Lists.of(
                    SpreadsheetComparatorName.TEXT,
                    SpreadsheetComparatorName.NUMBER
                ),
                ""
            )
        );
    }

    @Test
    public void testParseRow() {
        this.parseStringAndCheck(
            "1",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.row()
                ),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Test
    public void testParseRow2() {
        this.parseStringAndCheck(
            "123",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.parseRow("123")
                ),
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Test
    public void testParseRowAndComparatorNames() {
        this.parseStringAndCheck(
            "1=text,number",
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.row()
                ),
                Lists.of(
                    SpreadsheetComparatorName.TEXT,
                    SpreadsheetComparatorName.NUMBER
                ),
                ""
            )
        );
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens parseString(final String text) {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException cause) {
        return cause;
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentColumnOrRow() {
        this.checkNotEquals(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                Optional.of(
                    SpreadsheetSelection.A1.row()
                ),
                COMPARATOR_NAMES,
                TEXT
            )
        );
    }

    @Test
    public void testEqualsDifferentComparatorNames() {
        this.checkNotEquals(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                TEXT
            )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                COMPARATOR_NAMES,
                ""
            )
        );
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens createObject() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
            COLUMN_OR_ROW,
            COMPARATOR_NAMES,
            TEXT
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                COMPARATOR_NAMES,
                TEXT
            ),
            "A=number,text \"text1\""
        );
    }

    @Test
    public void testToStringWithEmptyText() {
        this.toStringAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                COMPARATOR_NAMES,
                ""
            ),
            "A=number,text"
        );
    }

    @Test
    public void testToStringWhenEmptyColumnOrRow() {
        this.toStringAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                TEXT
            ),
            "\"text1\""
        );
    }

    @Test
    public void testToStringWhenComparatorNamesEmpty() {
        this.toStringAndCheck(
            SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.with(
                COLUMN_OR_ROW,
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.EMPTY_COMPARATOR_NAMES,
                TEXT
            ),
            "A=\"text1\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens> type() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesTokens.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
