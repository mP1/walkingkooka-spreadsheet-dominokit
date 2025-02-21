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

import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * Captures any offset and counts that may be part of a {@link HistoryToken}.
 */
public final class HistoryTokenOffsetAndCount implements HasUrlFragment {

    public final static HistoryTokenOffsetAndCount EMPTY = new HistoryTokenOffsetAndCount(
        OptionalInt.empty(),
        OptionalInt.empty()
    );

    public static HistoryTokenOffsetAndCount with(final OptionalInt offset,
                                                  final OptionalInt count) {
        check(
            "offset",
            offset
        );
        check(
            "count",
            count
        );

        return offset.isPresent() || count.isPresent() ?
            new HistoryTokenOffsetAndCount(
                offset,
                count
            ) :
            EMPTY;
    }

    private static OptionalInt check(final String label,
                                     final OptionalInt value) {
        Objects.requireNonNull(value, label);

        if (value.isPresent()) {
            final int valueInt = value.getAsInt();
            if (valueInt < 0) {
                throw new IllegalArgumentException("Invalid " + label + " " + valueInt + " < 0");
            }
        }

        return value;
    }

    private HistoryTokenOffsetAndCount(final OptionalInt offset,
                                       final OptionalInt count) {
        super();

        this.offset = offset;
        this.count = count;
    }

    // offset............................................................................................................

    public OptionalInt offset() {
        return this.offset;
    }

    final OptionalInt offset;

    public HistoryTokenOffsetAndCount setOffset(final OptionalInt offset) {
        return this.offset.equals(offset) ?
            this :
            with(
                offset,
                this.count
            );
    }

    // count............................................................................................................

    public OptionalInt count() {
        return this.count;
    }

    final OptionalInt count;

    public HistoryTokenOffsetAndCount setCount(final OptionalInt count) {
        return this.count.equals(count) ?
            this :
            with(
                this.offset,
                count
            );
    }

    // HasUrlFragment...................................................................................................

    @Override //
    public UrlFragment urlFragment() {
        final UrlFragment urlFragment = urlFragmentAppend(
            urlFragmentAppend(
                UrlFragment.EMPTY,
                OFFSET,
                this.offset
            ),
            COUNT,
            this.count
        );
        return urlFragment.isEmpty() ?
            urlFragment :
            UrlFragment.SLASH.append(
                urlFragment
            );
    }

    private UrlFragment urlFragmentAppend(final UrlFragment urlFragment,
                                          final UrlFragment prefix,
                                          final OptionalInt value) {
        return value.isPresent() ?
            urlFragment.appendSlashThen(
                prefix.appendSlashThen(
                    UrlFragment.with(
                        String.valueOf(value.getAsInt())
                    )
                )
            ) :
            urlFragment;
    }

    private final static UrlFragment OFFSET = UrlFragment.with("offset");
    private final static UrlFragment COUNT = UrlFragment.with("count");

    // equals...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.offset,
            this.count
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof HistoryTokenOffsetAndCount && this.equals0((HistoryTokenOffsetAndCount) other);
    }

    private boolean equals0(final HistoryTokenOffsetAndCount other) {
        return this.offset.equals(other.offset) &&
            this.count.equals(other.count);
    }

    // ToString.........................................................................................................

    @Override
    public String toString() {
        return this.urlFragment().toString();
    }
}