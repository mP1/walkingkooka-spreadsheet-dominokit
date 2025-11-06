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

package walkingkooka.spreadsheet.dominokit.dialog;

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;

import java.util.Objects;

/**
 * An incomplete {@link DialogComponentContext} intended to be target of a {@link DialogComponentContextDelegator}.
 * Note the {@link #dialogTitle()} throws {@link UnsupportedOperationException}.
 */
final class BasicDialogComponentContext implements DialogComponentContext,
    RefreshContextDelegator {

    static BasicDialogComponentContext with(final RefreshContext context) {
        return new BasicDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private BasicDialogComponentContext(final RefreshContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }
    // RefreshContextDelegator..........................................................................................

    @Override
    public RefreshContext refreshContext() {
        return this.context;
    }

    private final RefreshContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
