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

package walkingkooka.spreadsheet.dominokit.anchor;

import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Defines some operations common to all anchor and anchor like components.
 */
public interface AnchorComponent<A extends AnchorComponent<A, T>, T> extends ValueComponent<HTMLAnchorElement, T, A> {

    @Override
    default boolean isDisabled() {
        return null == this.href();
    }

    @Override
    A setDisabled(final boolean disabled);

    // checked.........................................................................................................

    boolean isChecked();

    A setChecked(final boolean checked);

    // href.............................................................................................................

    /**
     * Getter that returns the HREF attribute of this ANCHOR.
     */
    Url href();

    /**
     * Setter that replaces the HREF attribute of this ANCHOR.
     */
    A setHref(final Url url);

    // id...............................................................................................................

    @Override
    String id();

    @Override
    A setId(final String id);

    // tabIndex.........................................................................................................

    int tabIndex();

    A setTabIndex(final int tabIndex);

    // target.........................................................................................................

    String target();

    A setTarget(final String target);

    // textContent......................................................................................................

    String textContent();

    A setTextContent(final String text);

    // badge............................................................................................................

    String badge();

    /**
     * Sets the text for a badge that appears to the right of the anchor. Empty text removes the badge completely.
     */
    A setBadge(final String text);

    // count............................................................................................................

    /**
     * Sets the text for the badge to the given count value.
     */
    default A setCount(final OptionalInt count) {
        Objects.requireNonNull(count, "count");

        return (A) this.setBadge(
            count.isPresent() ?
                String.valueOf(count.getAsInt()) :
                ""
        );
    }

    // flag.............................................................................................................

    String flag();

    A setFlag(final String countryCode);

    // iconBefore | text Content | iconAfter

    // iconBefore......................................................................................................

    Optional<Icon<?>> iconBefore();

    A setIconBefore(final Optional<Icon<?>> icon);

    // iconAfter......................................................................................................

    Optional<Icon<?>> iconAfter();

    A setIconAfter(final Optional<Icon<?>> icon);

    @Override
    default A hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default A removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default A removePadding() {
        throw new UnsupportedOperationException();
    }

    // Value............................................................................................................

    @Override
    default Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }
}
