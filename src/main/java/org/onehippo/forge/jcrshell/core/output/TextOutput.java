/*
 *  Copyright 2009 Hippo.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.onehippo.forge.jcrshell.core.output;

public class TextOutput extends Output {

    TextOutput() {
        super(null);
    }

    TextOutput(Output previous) {
        super(previous);
    }

    public TextOutput a(String text) {
        return (TextOutput) super.a(text);
    }

    public OkOutput ok() {
        return new OkOutput(this);
    }

    public TextOutput ok(String text) {
        OkOutput ok = new OkOutput(this);
        ok.a(text);
        return ok.end();
    }

    public WarnOutput warn() {
        return new WarnOutput(this);
    }

    public TextOutput warn(String text) {
        WarnOutput warn = new WarnOutput(this);
        warn.a(text);
        return warn.end();
    }

    public ErrorOutput error() {
        return new ErrorOutput(this);
    }

    public TextOutput error(String text) {
        ErrorOutput error = new ErrorOutput(this);
        error.a(text);
        return error.end();
    }

    public DebugOutput debug() {
        return new DebugOutput(this);
    }

    public TextOutput debug(String text) {
        return new DebugOutput(this).a(text).end();
    }

}
