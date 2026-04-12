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

package walkingkooka.spreadsheet.dominokit.value.textstyle;


import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public interface TextStyleDialogComponentContextTesting<C extends TextStyleDialogComponentContext> extends DialogComponentContextTesting<C>,
    ComponentLifecycleMatcherTesting {

    default void dialogTitleAndCheck(final C context,
                                     final String expected) {
        this.checkEquals(
            expected,
            context.dialogTitle()
        );
    }

    // undo.............................................................................................................

    default void undoAndCheck(final C context) {
        this.undoAndCheck(
            context,
            Optional.empty()
        );
    }

    default void undoAndCheck(final C context,
                              final TextStyle expected) {
        this.undoAndCheck(
            context,
            Optional.ofNullable(expected)
        );
    }

    default void undoAndCheck(final C context,
                              final Optional<TextStyle> expected) {
        this.checkEquals(
            expected,
            context.undo()
        );
    }

    // type.............................................................................................................

    @Override
    default String typeNameSuffix() {
        return TextStyleDialogComponentContext.class.getSimpleName();
    }
}
