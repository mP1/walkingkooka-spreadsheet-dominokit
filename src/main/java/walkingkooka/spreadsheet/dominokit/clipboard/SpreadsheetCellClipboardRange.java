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

import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A pair that holds a {@link SpreadsheetCellRange} and Set of values.
 */
public final class SpreadsheetCellClipboardRange<T> implements Value<Map<SpreadsheetCellReference, T>>,
        TreePrintable {

    public static <T> SpreadsheetCellClipboardRange<T> with(final SpreadsheetCellRange range,
                                                            final Map<SpreadsheetCellReference, T> value) {
        Objects.requireNonNull(range, "range");
        Objects.requireNonNull(value, "value");

        final Map<SpreadsheetCellReference, T> copy = Maps.immutable(value);

        final Set<SpreadsheetCellReference> outOfBounds = copy.keySet()
                .stream()
                .filter(c -> false == range.testCell(c))
                .collect(Collectors.toCollection(Sets::sorted));

        if (false == outOfBounds.isEmpty()) {
            throw new IllegalArgumentException("Found " + outOfBounds.size() + " cells out of range " + range + " got " +
                    outOfBounds.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))
            );
        }

        return new SpreadsheetCellClipboardRange<>(
                range,
                copy
        );
    }

    private SpreadsheetCellClipboardRange(final SpreadsheetCellRange range,
                                          final Map<SpreadsheetCellReference, T> value) {
        this.range = range;
        this.value = value;
    }

    public SpreadsheetCellRange range() {
        return this.range;
    }

    private final SpreadsheetCellRange range;

    @Override
    public Map<SpreadsheetCellReference, T> value() {
        return this.value;
    }

    private final Map<SpreadsheetCellReference, T> value;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.range.toStringMaybeStar());

        printer.indent();
        {
            for (final Entry<SpreadsheetCellReference, ?> cellAndValue : this.value.entrySet()) {
                printer.println(
                        cellAndValue.getKey()
                                .toString()
                );

                printer.indent();
                {
                    TreePrintable.printTreeOrToString(
                            cellAndValue.getValue(),
                            printer
                    );
                    printer.lineStart();
                }
                printer.outdent();
            }
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.range,
                this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SpreadsheetCellClipboardRange && this.equals0((SpreadsheetCellClipboardRange<?>) other);
    }

    private boolean equals0(final SpreadsheetCellClipboardRange<?> other) {
        return this.range.equals(other.range) &&
                this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.range + " " + this.value;
    }
}
