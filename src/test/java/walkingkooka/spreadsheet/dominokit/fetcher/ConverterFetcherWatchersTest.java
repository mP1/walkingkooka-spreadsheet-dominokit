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

package walkingkooka.spreadsheet.dominokit.fetcher;

import elemental2.dom.Headers;
import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;

import java.util.Optional;

public final class ConverterFetcherWatchersTest extends FetcherWatchersTestCase<ConverterFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final ConverterInfoSet infos = ConverterProviders.empty()
            .converterInfos();

        final ConverterFetcherWatchers watchers = ConverterFetcherWatchers.empty();
        watchers.add(
            new FakeConverterFetcherWatcher() {

                @Override
                public void onConverterInfoSet(final ConverterInfoSet is) {
                    ConverterFetcherWatchersTest.this.checkEquals(is, infos);

                    ConverterFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onConverterInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testAddOnce() {
        this.fired = 0;

        final ConverterInfoSet infos = ConverterInfoSet.EMPTY;

        final ConverterFetcherWatchers watchers = ConverterFetcherWatchers.empty();
        watchers.addOnce(
            new FakeConverterFetcherWatcher() {

                @Override
                public void onConverterInfoSet(final ConverterInfoSet is) {
                    ConverterFetcherWatchersTest.this.checkEquals(is, infos);

                    ConverterFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onConverterInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);

        watchers.onConverterInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testFireBegin() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.with("CustomHttpMethod");
        final Url url = Url.parseAbsolute("https://example/");
        final Optional<FetcherRequestBody<?>> body = Optional.of(
            FetcherRequestBody.string("Body123")
        );
        final AppContext appContext = AppContexts.fake();

        final ConverterFetcherWatchers watchers = ConverterFetcherWatchers.empty();
        watchers.add(
            new FakeConverterFetcherWatcher() {

                @Override
                public void onBegin(final HttpMethod m,
                                    final Url u,
                                    final Optional<FetcherRequestBody<?>> b) {
                    ConverterFetcherWatchersTest.this.checkEquals(method, m);
                    ConverterFetcherWatchersTest.this.checkEquals(u, u);
                    ConverterFetcherWatchersTest.this.checkEquals(b, b);

                    ConverterFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onBegin(method, url, body);
        this.checkEquals(1, this.fired);

        watchers.onBegin(method, url, body);
        this.checkEquals(2, this.fired);

        watchers.onBegin(method, url, body);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireError() {
        this.fired = 0;

        final Object cause = "cause-123";

        final ConverterFetcherWatchers watchers = ConverterFetcherWatchers.empty();
        watchers.add(
            new FakeConverterFetcherWatcher() {
                @Override
                public void onError(final Object c) {
                    ConverterFetcherWatchersTest.this.checkEquals(cause, c);

                    ConverterFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onError(cause);
        this.checkEquals(1, this.fired);

        watchers.onError(cause);
        this.checkEquals(2, this.fired);

        watchers.onError(cause);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireFailure() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.GET;
        final AbsoluteOrRelativeUrl url = Url.parseRelative("/something/api/");
        final HttpStatus status = HttpStatusCode.withCode(123).setMessage("status message 456");
        final Headers headers = null;
        final String body = "Body-failure-789";

        final ConverterFetcherWatchers watchers = ConverterFetcherWatchers.empty();
        watchers.add(
            new FakeConverterFetcherWatcher() {

                @Override
                public void onFailure(final HttpMethod m,
                                      final AbsoluteOrRelativeUrl u,
                                      final HttpStatus s,
                                      final Headers h,
                                      final String b) {
                    ConverterFetcherWatchersTest.this.checkEquals(method, m);
                    ConverterFetcherWatchersTest.this.checkEquals(url, u);
                    ConverterFetcherWatchersTest.this.checkEquals(status, s);
                    ConverterFetcherWatchersTest.this.checkEquals(headers, h);
                    ConverterFetcherWatchersTest.this.checkEquals(body, b);

                    ConverterFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(1, this.fired);

        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(2, this.fired);

        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(3, this.fired);
    }

    private int fired = 0;

    // ClassTesting....................................................................................................

    @Override
    public Class<ConverterFetcherWatchers> type() {
        return ConverterFetcherWatchers.class;
    }
}
