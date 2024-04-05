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

import elemental2.core.JsArray;
import elemental2.dom.Blob;
import elemental2.dom.Blob.ConstructorBlobPartsArrayUnionType;
import elemental2.dom.BlobPropertyBag;
import elemental2.dom.DomGlobal;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.MediaType;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This is the public implementation of a native bridge hiding the Promises and other native details behind a nice
 * abstractions. It is intended that this will be the implementation delegated by the actual
 * {@link walkingkooka.spreadsheet.dominokit.AppContext}.
 */
final class ElementalClipboardContext implements ClipboardContext {

    /**
     * Creates an {@link ElementalClipboardContext}.
     */
    static ElementalClipboardContext empty() {
        return new ElementalClipboardContext();
    }

    private ElementalClipboardContext() {
        this.clipboard = Js.<ElementalNavigator>uncheckedCast(DomGlobal.window.navigator).clipboard;
    }

    @Override
    public void readClipboardItem(final Predicate<MediaType> filter,
                                  final ClipboardContextReadWatcher watcher) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(watcher, "watcher");

        this.clipboard.read()
                .then(
                        items -> {
                            for (int i = 0; i < items.length; i++) {
                                final ElementalClipboardItem item = items.getAt(i);
                                final JsArray<String> types = item.types();

                                for (int j = 0; j < types.length; j++) {
                                    final String type = types.getAt(j);
                                    final MediaType mediaType = MediaType.parse(type);

                                    if (filter.test(mediaType)) {
                                        return item.getType(type)
                                                .then(Blob::text)
                                                .then(blobText -> {
                                                    watcher.onSuccess(
                                                            Lists.of(
                                                                    ClipboardTextItem.with(
                                                                            Lists.of(
                                                                                    mediaType
                                                                            ),
                                                                            blobText
                                                                    )
                                                            )
                                                    );
                                                    return null;
                                                });
                                    }
                                }
                            }
                            throw new RuntimeException("Unhandled ClipboardItems");
                        }
                ).catch_(
                        (rejected) -> {
                            watcher.onFailure(rejected);
                            return null;
                        }
                );
    }

    @Override
    public void writeClipboardItem(final ClipboardTextItem item,
                                   final ClipboardContextWriteWatcher watcher) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(watcher, "watcher");

        final JsArray<ElementalClipboardItem> clipboardItems = JsArray.of();

        for (final MediaType mediaType : item.types()) {
            final String type = mediaType.toString(); // even suffix are not supported.

            final BlobPropertyBag options = BlobPropertyBag.create();
            options.setType(type);

            final Blob blob = new Blob(
                    JsArray.of(
                            ConstructorBlobPartsArrayUnionType.of(
                                    item.text()
                            )
                    ),
                    options
            );

            clipboardItems.push(
                    new ElementalClipboardItem(
                            Js.uncheckedCast(
                                    JsPropertyMap.of(
                                            type,
                                            blob
                                    )
                            )
                    )
            );

            break; // since only text/plain is the only type written to the clipboard ignore others.
        }

        this.clipboard.write(
                clipboardItems
        ).then(
                (ignored) -> {
                    watcher.onSuccess();
                    return Promise.resolve(0);
                }
        ).catch_(
                (rejected) -> {
                    watcher.onFailure(rejected);
                    return null;
                }
        );
    }

    /**
     * The native browser clipboard object
     */
    private final ElementalClipboard clipboard;
}
