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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenOffsetAndCountTest implements HasUrlFragmentTesting,
    CanBeEmptyTesting,
    HashCodeEqualsDefinedTesting2<HistoryTokenOffsetAndCount>,
    ToStringTesting<HistoryTokenOffsetAndCount>,
    ClassTesting<HistoryTokenOffsetAndCount> {

    private final static OptionalInt OFFSET = OptionalInt.of(1);

    private final static OptionalInt COUNT = OptionalInt.of(23);

    // empty............................................................................................................

    @Test
    public void testEmpty() {
        this.check(
            HistoryTokenOffsetAndCount.EMPTY,
            OptionalInt.empty(), // offset
            OptionalInt.empty() // count
        );
    }

    @Test
    public void testNonEmptySetOffsetEmptySetCountEmpty() {
        assertSame(
            HistoryTokenOffsetAndCount.EMPTY,
            HistoryTokenOffsetAndCount.with(
                    OFFSET,
                    COUNT
                ).setOffset(OptionalInt.empty())
                .setCount(OptionalInt.empty())
        );
    }

    // with.............................................................................................................

    @Test
    public void testWithNullOffsetFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenOffsetAndCount.with(
                null,
                COUNT
            )
        );
    }

    @Test
    public void testWithInvalidOffsetFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> HistoryTokenOffsetAndCount.with(
                OptionalInt.of(-987),
                COUNT
            )
        );

        this.checkEquals(
            "Invalid offset -987 < 0",
            thrown.getMessage()
        );
    }

    @Test
    public void testWithNullCountFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryTokenOffsetAndCount.with(
                OFFSET,
                null
            )
        );
    }

    @Test
    public void testWithInvalidCountFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> HistoryTokenOffsetAndCount.with(
                OFFSET,
                OptionalInt.of(-1)
            )
        );

        this.checkEquals(
            "Invalid count -1 < 0",
            thrown.getMessage()
        );
    }

    @Test
    public void testWith() {
        this.check(
            HistoryTokenOffsetAndCount.with(
                OFFSET,
                COUNT
            ),
            OFFSET,
            COUNT
        );
    }

    private void check(final HistoryTokenOffsetAndCount offsetAndCount,
                       final OptionalInt offset,
                       final OptionalInt count) {
        this.checkEquals(
            offset,
            offsetAndCount.offset,
            "offset"
        );
        this.checkEquals(
            count,
            offsetAndCount.count,
            "count"
        );
    }

    // setOffset........................................................................................................

    @Test
    public void testSetOffsetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setOffset(null)
        );
    }

    @Test
    public void testSetOffsetWithInvalidFails() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> this.createObject()
                .setOffset(
                    OptionalInt.of(-2)
                )
        );

        this.checkEquals(
            "Invalid offset -2 < 0",
            thrown.getMessage()
        );
    }

    @Test
    public void testSetOffsetSame() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.with(
            OFFSET,
            COUNT
        );
        assertSame(
            offsetAndCount,
            offsetAndCount.setOffset(OFFSET)
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.with(
            OFFSET,
            COUNT
        );
        final OptionalInt differentOffset = OptionalInt.of(999);
        final HistoryTokenOffsetAndCount different = offsetAndCount.setOffset(differentOffset);

        assertNotSame(
            offsetAndCount,
            different
        );

        this.check(
            different,
            differentOffset,
            COUNT
        );

        this.check(
            offsetAndCount,
            OFFSET,
            COUNT
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setCount(null)
        );
    }

    @Test
    public void testSetCountWithInvalidFails() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> this.createObject()
                .setCount(
                    OptionalInt.of(-3)
                )
        );

        this.checkEquals(
            "Invalid count -3 < 0",
            thrown.getMessage()
        );
    }

    @Test
    public void testSetCountSame() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.with(
            OFFSET,
            COUNT
        );
        assertSame(
            offsetAndCount,
            offsetAndCount.setCount(COUNT)
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.with(
            OFFSET,
            COUNT
        );
        final OptionalInt differentCount = OptionalInt.of(999);
        final HistoryTokenOffsetAndCount different = offsetAndCount.setCount(differentCount);

        assertNotSame(
            offsetAndCount,
            different
        );

        this.check(
            different,
            OFFSET,
            differentCount
        );

        this.check(
            offsetAndCount,
            OFFSET,
            COUNT
        );
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragmentEmpty() {
        this.toStringAndCheck(
            HistoryTokenOffsetAndCount.with(
                OptionalInt.empty(),
                OptionalInt.empty()
            ),
            ""
        );
    }

    @Test
    public void testUrlFragmentOffset() {
        this.toStringAndCheck(
            HistoryTokenOffsetAndCount.with(
                OFFSET,
                OptionalInt.empty()
            ),
            "/offset/1"
        );
    }

    @Test
    public void testUrlFragmentCount() {
        this.toStringAndCheck(
            HistoryTokenOffsetAndCount.with(
                OptionalInt.empty(),
                COUNT
            ),
            "/count/23"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.toStringAndCheck(
            HistoryTokenOffsetAndCount.with(
                OFFSET,
                COUNT
            ),
            "/offset/1/count/23"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseOtherText() {
        this.parseAndCheck(
            "/hello",
            HistoryTokenOffsetAndCount.EMPTY,
            "/hello"
        );
    }

    @Test
    public void testParseOffsetMissingNumberFails() {
        this.parseFails(
            "/offset",
            "Missing value for \"offset\""
        );
    }

    @Test
    public void testParseOffsetAndNumber() {
        this.parseAndCheck(
            "/offset/123",
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(123)
            ),
            ""
        );
    }

    @Test
    public void testParseOffsetAndNumberAndExtraToken() {
        this.parseAndCheck(
            "/offset/123/$",
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(123)
            ),
            "/$"
        );
    }

    @Test
    public void testParseCountMissingNumberFails() {
        this.parseFails(
            "/count",
            "Missing value for \"count\""
        );
    }

    @Test
    public void testParseCountNumberOffsetMissingNumberFails() {
        this.parseFails(
            "/offset/1/count",
            "Missing value for \"count\""
        );
    }

    @Test
    public void testParseCountAndNumber() {
        this.parseAndCheck(
            "/count/123",
            HistoryTokenOffsetAndCount.EMPTY.setCount(
                OptionalInt.of(123)
            ),
            ""
        );
    }

    @Test
    public void testParseCountNumberAndExtraToken() {
        this.parseAndCheck(
            "/count/123/$",
            HistoryTokenOffsetAndCount.EMPTY.setCount(
                OptionalInt.of(123)
            ),
            "/$"
        );
    }

    @Test
    public void testParseOffsetNumberCountNumber() {
        this.parseAndCheck(
            "/offset/123/count/456",
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(123)
            ).setCount(
                OptionalInt.of(456)
            ),
            ""
        );
    }

    @Test
    public void testParseOffsetNumberCountNumberAndExtraTokens() {
        this.parseAndCheck(
            "/offset/123/count/456/extra",
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(123)
            ).setCount(
                OptionalInt.of(456)
            ),
            "/extra"
        );
    }

    private void parseAndCheck(final String text,
                               final HistoryTokenOffsetAndCount offsetAndCount,
                               final String textLeft) {
        final TextCursor cursor = TextCursors.charSequence(text);

        this.checkEquals(
            offsetAndCount,
            HistoryTokenOffsetAndCount.parse(cursor),
            text
        );

        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        this.checkEquals(
            textLeft,
            save.textBetween().toString(),
            "textLeft"
        );
    }

    private void parseFails(final String text,
                            final String expected) {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> HistoryTokenOffsetAndCount.parse(TextCursors.charSequence(text))
        );

        this.checkEquals(
            expected,
            thrown.getMessage()
        );
    }

    // CanBeEmpty.......................................................................................................

    @Test
    public void testCanBeEmptyWithEmpty() {
        this.isEmptyAndCheck(
            HistoryTokenOffsetAndCount.EMPTY,
            true
        );
    }

    @Test
    public void testCanBeEmptyWithNotEmpty() {
        this.isEmptyAndCheck(
            this.createObject(),
            false
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentOffset() {
        this.checkNotEquals(
            HistoryTokenOffsetAndCount.with(
                OptionalInt.of(999),
                COUNT
            )
        );
    }

    @Test
    public void testEqualsDifferentCount() {
        this.checkNotEquals(
            HistoryTokenOffsetAndCount.with(
                OFFSET,
                OptionalInt.of(999)
            )
        );
    }

    @Override
    public HistoryTokenOffsetAndCount createObject() {
        return HistoryTokenOffsetAndCount.with(
            OFFSET,
            COUNT
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "/offset/1/count/23"
        );
    }

    // class............................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<HistoryTokenOffsetAndCount> type() {
        return HistoryTokenOffsetAndCount.class;
    }
}
