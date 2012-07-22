/**
 * Copyright (c) 2012 Edgar Espina
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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package com.github.edgarespina.handlebars.helper;

import java.io.IOException;

import com.github.edgarespina.handlebars.Helper;
import com.github.edgarespina.handlebars.Options;

/**
 * You can use the unless helper as the inverse of the if helper. Its block
 * will be rendered if the expression returns a falsy value.
 *
 * @author edgar.espina
 * @since 0.3.0
 */
public class UnlessHelper implements Helper<Object> {

  /**
   * A singleton instance of this helper.
   */
  public static final Helper<Object> INSTANCE = new UnlessHelper();

  /**
   * The helper's name.
   */
  public static final String NAME = "unless";

  @Override
  public CharSequence apply(final Object context, final Options options)
      throws IOException {
    if (options.isEmpty(context)) {
      return options.fn();
    } else {
      return options.inverse();
    }
  }
}