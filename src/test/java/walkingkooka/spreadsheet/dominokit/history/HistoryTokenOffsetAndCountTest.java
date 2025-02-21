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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragmentTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.OptionalInt;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public final class HistoryTokenOffsetAndCountTest implements HasUrlFragmentTesting,
    HashCodeEqualsDefinedTesting2<HistoryTokenOffsetAndCount>,
    ToStringTesting<HistoryTokenOffsetAndCount>,
    ClassTesting<HistoryTokenOffsetAndCount> {

    private final static OptionalInt OFFSET = OptionalInt.of(1);

    private final static OptionalInt COUNT = OptionalInt.of(23);

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
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<HistoryTokenOffsetAndCount> type() {
        return HistoryTokenOffsetAndCount.class;
    }
}
