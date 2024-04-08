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

package walkingkooka.spreadsheet.dominokit.net;

import elemental2.dom.Headers;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;

import java.util.Optional;

public final class FetcherWatchersTest implements ClassTesting<FetcherWatchers<?>> {

    // addOnce..........................................................................................................

    @Test
    public void testAddOnceAndFireSuccess() {
        final TestFetcherWatchers watchers = new TestFetcherWatchers();

        final AppContext context = AppContexts.fake();

        final TestFetcherWatcher watcher = new TestFetcherWatcher();
        watchers.addOnce(watcher);

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("onBeginonSuccess", watcher.toString());

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("onBeginonSuccess", watcher.toString());
    }

    @Test
    public void testAddOnceAndFireSuccess2() {
        final TestFetcherWatchers watchers = new TestFetcherWatchers();

        final AppContext context = AppContexts.fake();

        final TestFetcherWatcher onceWatcher = new TestFetcherWatcher();
        watchers.addOnce(onceWatcher);

        final TestFetcherWatcher watcher = new TestFetcherWatcher();
        watchers.add(watcher);

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("onBeginonSuccess", onceWatcher.b.toString());
        this.checkEquals("onBeginonSuccess", watcher.toString());

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("onBeginonSuccess", onceWatcher.b.toString());
        this.checkEquals("onBeginonSuccessonBeginonSuccess", watcher.toString());
    }

    @Test
    public void testAddOnceAndFireFailure() {
        final TestFetcherWatchers watchers = new TestFetcherWatchers();

        final AppContext context = AppContexts.fake();

        final TestFetcherWatcher watcher = new TestFetcherWatcher();
        watchers.addOnce(watcher);

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onFailure(HttpStatusCode.INTERNAL_SERVER_ERROR.status(), null, "Body", context);

        this.checkEquals("onBeginonFailure", watcher.toString());

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onFailure(HttpStatusCode.INTERNAL_SERVER_ERROR.status(), null, "Body", context);

        this.checkEquals("onBeginonFailure", watcher.toString());
    }

    @Test
    public void testAddOnceAndFireError() {
        final TestFetcherWatchers watchers = new TestFetcherWatchers();

        final AppContext context = AppContexts.fake();

        final TestFetcherWatcher watcher = new TestFetcherWatcher();
        watchers.addOnce(watcher);

        final Exception error = new Exception();

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onError(error, context);

        this.checkEquals("onBeginonError", watcher.toString());

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onError(error, context);

        this.checkEquals("onBeginonError", watcher.toString());
    }

    static final class TestFetcherWatchers extends FetcherWatchers<TestFetcherWatcher> {

        public void onSuccess(final AppContext context) {
            this.fire(
                    new TestSuccessFetcherWatcherEvent(
                            context
                    )
            );
        }
    }

    static final class TestFetcherWatcher implements FetcherWatcher {

        private final StringBuilder b = new StringBuilder();

        @Override
        public void onBegin(final HttpMethod method,
                            final Url url,
                            final Optional<String> body,
                            final AppContext context) {
            this.b.append("onBegin");
        }

        public void onSuccess(final AppContext context) {
            this.b.append("onSuccess");
        }

        @Override
        public void onFailure(final HttpStatus status,
                              final Headers headers,
                              final String body,
                              final AppContext context) {
            this.b.append("onFailure");
        }

        @Override
        public void onError(final Object cause,
                            final AppContext context) {
            this.b.append("onError");
        }

        @Override
        public void onNoResponse(final AppContext context) {
            this.b.append("onNoResponse");
        }

        @Override
        public String toString() {
            return this.b.toString();
        }
    }

    static final class TestSuccessFetcherWatcherEvent extends FetcherWatchersEvent<TestFetcherWatcher> {

        TestSuccessFetcherWatcherEvent(final AppContext context) {
            super(context);
        }

        @Override
        public void accept(final TestFetcherWatcher watcher) {
            watcher.onSuccess(
                    this.context
            );
        }

        @Override
        public String toString() {
            return "TestFetcherWatcherEvent";
        }
    }

    @Test
    public void testAddOnceAndFirePriority() {
        final TestFetcherWatchers2 watchers = new TestFetcherWatchers2();

        final AppContext context = AppContexts.fake();

        final StringBuilder b = new StringBuilder();

        final TestFetcherWatcher2 watcher = new TestFetcherWatcher2("A", b);
        watchers.add(watcher);

        final TestFetcherWatcher2 onceWatcher = new TestFetcherWatcher2("B", b);
        watchers.addOnce(onceWatcher);

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("B.onBeginA.onBeginB.onSuccessA.onSuccess", b.toString());

        watchers.onBegin(HttpMethod.GET, Url.EMPTY_RELATIVE_URL, Optional.of("Body"), context);
        watchers.onSuccess(context);

        this.checkEquals("B.onBeginA.onBeginB.onSuccessA.onSuccessA.onBeginA.onSuccess", b.toString());
    }

    static final class TestFetcherWatchers2 extends FetcherWatchers<TestFetcherWatcher2> {

        public void onSuccess(final AppContext context) {
            this.fire(
                    new TestSuccessFetcherWatcherEvent2(
                            context
                    )
            );
        }
    }

    static final class TestFetcherWatcher2 implements FetcherWatcher {

        TestFetcherWatcher2(final String prefix,
                            final StringBuilder b) {
            this.prefix = prefix;
            this.b = b;
        }

        private final String prefix;
        private final StringBuilder b;

        @Override
        public void onBegin(final HttpMethod method,
                            final Url url,
                            final Optional<String> body,
                            final AppContext context) {
            this.b.append(this.prefix).append(".onBegin");
        }

        public void onSuccess(final AppContext context) {
            this.b.append(this.prefix).append(".onSuccess");
        }

        @Override
        public void onFailure(final HttpStatus status,
                              final Headers headers,
                              final String body,
                              final AppContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onError(final Object cause,
                            final AppContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onNoResponse(AppContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return this.b.toString();
        }
    }

    static final class TestSuccessFetcherWatcherEvent2 extends FetcherWatchersEvent<TestFetcherWatcher2> {

        TestSuccessFetcherWatcherEvent2(final AppContext context) {
            super(context);
        }

        @Override
        public void accept(final TestFetcherWatcher2 watcher) {
            watcher.onSuccess(
                    this.context
            );
        }

        @Override
        public String toString() {
            return "TestFetcherWatcherEvent2";
        }
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<FetcherWatchers<?>> type() {
        return Cast.to(FetcherWatchers.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
