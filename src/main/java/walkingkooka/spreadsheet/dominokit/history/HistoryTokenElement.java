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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import elemental2.dom.KeyboardEvent;
import org.jboss.elemento.EventType;
import org.jboss.elemento.IsElement;

import java.util.Objects;

import static org.jboss.elemento.Key.Enter;

/**
 * An abstraction that provides common actions for an element and {@link HistoryToken} actions such as pushing
 * a token when clicked.
 */
public final class HistoryTokenElement<T extends HTMLElement> implements IsElement<T> {

    static <T extends HTMLElement> HistoryTokenElement<T> with(final T element,
                                                               final HistoryToken historyToken) {
        Objects.requireNonNull(element, "element");
        Objects.requireNonNull(historyToken, "historyToken");

        return new HistoryTokenElement<>(
                element,
                historyToken
        );
    }

    private HistoryTokenElement(final T element,
                                final HistoryToken historyToken) {
        this.element = element;
        this.historyToken = historyToken;
    }

    public HistoryTokenElement<T> addClick(final EventListener listener) {
        this.element.addEventListener(
                EventType.click.getName(),
                listener
        );
        return this;
    }

    public HistoryTokenElement<T> addKeydown(final EventListener listener) {
        this.element.addEventListener(
                EventType.keydown.getName(),
                listener
        );
        return this;
    }

    public HistoryTokenElement<T> pushHistoryToken(final HistoryTokenContext context) {
        final HistoryToken historyToken = this.historyToken;

        return this.addClick(
                (e) -> {
                    e.preventDefault();
                    context.pushHistoryToken(historyToken);
                }
        ).addKeydown(
                (e) -> {
                    final KeyboardEvent keyboardEvent = (KeyboardEvent) e;
                    if (keyboardEvent.code.equals(Enter)) {
                        keyboardEvent.preventDefault();
                        context.pushHistoryToken(historyToken);
                    }
                }
        );
    }

    public HistoryTokenElement<T> setAttribute(final String name,
                                               final String value) {
        this.element.setAttribute(name, value);
        return this;
    }

    public HistoryTokenElement<T> setAttribute(final String name,
                                               final int value) {
        this.element.setAttribute(name, value);
        return this;
    }

    public HistoryTokenElement<T> tabIndex(final int tabIndex) {
        return this.setAttribute(
                "tabindex",
                tabIndex
        );
    }

    public HistoryTokenElement<T> text(final String text) {
        this.element.textContent = text;
        return this;
    }

    private final HistoryToken historyToken;


    @Override
    public T element() {
        return this.element;
    }

    private final T element;
}
