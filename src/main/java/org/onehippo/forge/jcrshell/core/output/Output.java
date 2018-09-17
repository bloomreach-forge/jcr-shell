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

public abstract class Output {

    public static TextOutput out() {
        return new TextOutput();
    }

    private Output next;
    private Output previous;
    private StringBuilder sb = new StringBuilder();

    Output(Output previous) {
        if (previous != null) {
            this.previous = previous;
            previous.next = this;
        }
    }

    public boolean hasNext() {
        return next != null;
    }

    public Output next() {
        return next;
    }

    public Output head() {
        if (previous != null) {
            return previous.head();
        } else {
            return this;
        }
    }

    public Output a(String text) {
        sb.append(text);
        return this;
    }

    public String getText() {
        return sb.toString();
    }
}
