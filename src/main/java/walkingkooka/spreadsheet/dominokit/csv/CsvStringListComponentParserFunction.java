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

package walkingkooka.spreadsheet.dominokit.csv;

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.CsvStringList;

import java.util.function.Function;

/**
 * A {@link Validator} which asserts that a {@link CsvStringListComponent} has between the min/max number of elements.
 */
final class CsvStringListComponentParserFunction implements Function<String, CsvStringList> {

    static CsvStringListComponentParserFunction with(final int minLength,
                                                     final int maxLength,
                                                     final boolean inclusive) {
        if (minLength < 0) {
            throw new IllegalArgumentException("Min length " + minLength + " < 0");
        }
        if (maxLength < minLength) {
            throw new IllegalArgumentException("Max length " + maxLength + " < " + minLength);
        }

        return new CsvStringListComponentParserFunction(
            minLength,
            maxLength,
            inclusive
        );
    }

    private CsvStringListComponentParserFunction(final int minLength,
                                                 final int maxLength,
                                                 final boolean inclusive) {
        super();

        this.minLength = minLength;
        this.maxLength = maxLength;
        this.inclusive = inclusive;
    }

    @Override
    public CsvStringList apply(final String text) {
        final CsvStringList list = CsvStringList.parse(text);

        final int count = list.size();

        final int minLength = this.minLength;
        final int maxLength = this.maxLength;

        String orMore = "";
        String orLess = "";
        if (minLength != maxLength) {
            orLess = " or less";
            orMore = " or more";
        }

        if (count < minLength) {
            throw new IllegalArgumentException("Require " + minLength + orMore);
        } else {
            if (this.inclusive) {
                if (count > maxLength) {
                    throw new IllegalArgumentException("Require " + maxLength + orLess);
                }
            } else {
                if (count >= maxLength) {
                    throw new IllegalArgumentException("Require less than " + maxLength);
                }
            }
        }
        return list;
    }

    private final int minLength;
    private final int maxLength;

    /**
     * When true {@link #maxLength} is inclusive, false is exclusive.
     */
    private final boolean inclusive;
    // Object...........................................................................................................

    @Override
    public String toString() {
        return "minLength=" +
            this.minLength +
            " maxLength=" +
            this.maxLength;
    }
}
