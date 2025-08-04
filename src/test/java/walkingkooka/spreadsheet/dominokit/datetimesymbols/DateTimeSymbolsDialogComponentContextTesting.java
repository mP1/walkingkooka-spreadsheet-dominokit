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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;

import java.util.Optional;

public interface DateTimeSymbolsDialogComponentContextTesting<C extends DateTimeSymbolsDialogComponentContext> extends DialogComponentContextTesting<C>,
    ComponentLifecycleMatcherTesting {

    default void copyDateTimeSymbolsAndCheck(final C context) {
        this.copyDateTimeSymbolsAndCheck(
            context,
            Optional.empty()
        );
    }

    default void copyDateTimeSymbolsAndCheck(final C context,
                                             final DateTimeSymbols expected) {
        this.copyDateTimeSymbolsAndCheck(
            context,
            Optional.of(expected)
        );
    }

    default void copyDateTimeSymbolsAndCheck(final C context,
                                             final Optional<DateTimeSymbols> expected) {
        this.checkEquals(
            expected,
            context.copyDateTimeSymbols()
        );
    }

    default void loadDateTimeSymbolsAndCheck(final C context,
                                             final Optional<DateTimeSymbols> expected) {
        this.checkEquals(
            expected,
            context.loadDateTimeSymbols()
        );
    }

    @Override
    default String typeNameSuffix() {
        return DateTimeSymbolsDialogComponentContext.class.getSimpleName();
    }
}
