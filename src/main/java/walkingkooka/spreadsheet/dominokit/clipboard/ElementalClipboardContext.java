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
import elemental2.promise.Promise;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.MediaType;

import java.util.List;
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
        this.clipboard = new ElementalClipboard();
    }

    @Override
    public void read(final Predicate<MediaType> filter,
                     final ClipboardContextReadWatcher watcher) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(watcher, "watcher");

        this.clipboard.read()
                .then(
                        ci -> requestClipboardItems(
                                ci,
                                filter
                        )
                ).catch_(
                        (rejected) -> {
                            watcher.onFailure(rejected);
                            return null;
                        }
                );
    }

    private static Promise<ClipboardTextItem[]> requestClipboardItems(final JsArray<ElementalClipboardItem> items,
                                                                      final Predicate<MediaType> filter) {
        final List<Promise<ClipboardTextItem>> ctiPromises = Lists.array();


        for (int i = 0; i < items.length; i++) {
            final ElementalClipboardItem item = items.getAt(i);
            final JsArray<String> types = item.types();

            for (int j = 0; j < types.length; j++) {
                final String type = types.getAt(j);
                final MediaType mediaType = MediaType.parse(type);

                if (filter.test(mediaType)) {
                    ctiPromises.add(
                            item.getType(type)
                                    .then(
                                            b -> b.text()
                                                    .then(
                                                            text -> Promise.resolve(
                                                                    ClipboardTextItem.with(
                                                                            Lists.of(
                                                                                    mediaType
                                                                            ),
                                                                            text
                                                                    )
                                                            )
                                                    )
                                    )
                    );
                }
            }
        }

        return Promise.all(
                ctiPromises.toArray(
                        new Promise[ctiPromises.size()]
                )
        );
    }

    @Override
    public void write(final ClipboardTextItem item,
                      final ClipboardContextWriteWatcher watcher) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(watcher, "watcher");

        this.clipboard.write(
                new JsArray<>(
                        toElementalClipboardItem(item)
                )
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

    private static ElementalClipboardItem toElementalClipboardItem(final ClipboardTextItem item) {
        final BlobPropertyBag options = BlobPropertyBag.create();
        options.setType(
                item.text()
        );

        return new ElementalClipboardItem(
                JsArray.of(
                        item.types()
                                .stream()
                                .map(MediaType::toString)
                                .toArray(String[]::new)
                ),
                new Blob(
                        JsArray.of(
                                ConstructorBlobPartsArrayUnionType.of(
                                        item.text()
                                )
                        ),
                        options
                )
        );
    }

    /**
     * The native browser clipboard object
     */
    private final ElementalClipboard clipboard;
}
