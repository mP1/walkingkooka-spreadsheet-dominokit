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

package walkingkooka.spreadsheet.dominokit;

/**
 * A component with various lifecycle events that react to an open/close state.
 */
public interface ComponentLifecycle extends ComponentLifecycleMatcher,
        ComponentRefreshable,
        OpenableComponent {

    /**
     * This is called after {@link #open(AppContext)} and {@link #refresh(AppContext)}
     */
    void openGiveFocus(final AppContext context);

    /**
     * Conditionally calls {@link #refresh(AppContext)} if this ui is {@link #isOpen()}.
     */
    void refreshIfOpen(final AppContext context);
}
