/**
 * Copyright (c) 2012-2013 Edgar Espina
 *
 * This file is part of Handlebars.java.
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
 */
package com.github.jknack.handlebars.cache;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Parser;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.cache.Cache;

/**
 * An implementation of {@link TemplateCache} built on top of Guava.
 *
 * @author edgar.espina
 * @since 0.11.0
 */
public class GuavaTemplateCache implements TemplateCache {

  /**
   * The guava cache.
   */
  private final Cache<TemplateSource, Template> cache;

  /**
   * Creates a new {@link GuavaTemplateCache}.
   *
   * @param cache The guava cache to use. Required.
   */
  public GuavaTemplateCache(final Cache<TemplateSource, Template> cache) {
    this.cache = notNull(cache, "The cache is required.");
  }

  @Override
  public void clear() {
    cache.invalidateAll();
  }

  @Override
  public void evict(final TemplateSource source) {
    cache.invalidate(source);
  }

  @Override
  public Template get(final TemplateSource source, final Parser parser) throws IOException {
    notNull(source, "The source is required.");
    notNull(parser, "The parser is required.");
    try {
      return cache.get(source, new Callable<Template>() {
        @Override
        public Template call() throws Exception {
          return parser.parse(source);
        }
      });
    } catch (ExecutionException ex) {
      throw launderThrowable(source, ex.getCause());
    }
  }

  /**
   * Re-throw the cause of an execution exception.
   *
   * @param source The template source. Required.
   * @param cause The cause of an execution exception.
   * @return Re-throw a cause of an execution exception.
   */
  private RuntimeException launderThrowable(final TemplateSource source, final Throwable cause) {
    if (cause instanceof RuntimeException) {
      return (RuntimeException) cause;
    } else if (cause instanceof Error) {
      throw (Error) cause;
    } else {
      return new HandlebarsException("Can't parse: " + source, cause);
    }
  }
}
