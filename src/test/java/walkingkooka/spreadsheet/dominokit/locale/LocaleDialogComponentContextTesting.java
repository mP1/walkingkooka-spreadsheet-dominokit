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

package walkingkooka.spreadsheet.dominokit.locale;

import walkingkooka.locale.LocaleContextTesting;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;

import java.util.Locale;
import java.util.Optional;

public interface LocaleDialogComponentContextTesting<C extends LocaleDialogComponentContext> extends DialogComponentContextTesting<C>,
    LocaleContextTesting,
    ComponentLifecycleMatcherTesting {

    default void dialogTitleAndCheck(final C context,
                                     final String expected) {
        this.checkEquals(
            expected,
            context.dialogTitle()
        );
    }

    // undoLocale.......................................................................................................

    default void undoLocaleAndCheck(final C context) {
        this.undoLocaleAndCheck(
            context,
            Optional.empty()
        );
    }

    default void undoLocaleAndCheck(final C context,
                                    final Locale expected) {
        this.undoLocaleAndCheck(
            context,
            Optional.ofNullable(expected)
        );
    }

    default void undoLocaleAndCheck(final C context,
                                    final Optional<Locale> expected) {
        this.checkEquals(
            expected,
            context.undoLocale()
        );
    }

    // type.............................................................................................................

    @Override
    default String typeNameSuffix() {
        return LocaleDialogComponentContext.class.getSimpleName();
    }
}
