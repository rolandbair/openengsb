/**
 * Copyright 2010 OpenEngSB Division, Vienna University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.tooling.pluginsuite.openengsbplugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.openengsb.tooling.pluginsuite.openengsbplugin.tools.OpenEngSBJavaRunner;
import org.ops4j.pax.runner.platform.PlatformException;
import org.ops4j.pax.runner.platform.internal.CommandLineBuilder;

/**
 * Equivalent to execute karaf or karaf.bat per hand after build by mvn clean install in a (typically) assembly
 * directory.
 * 
 * @goal provision
 * 
 * @inheritedByDefault false
 * 
 * @requiresProject true
 */
public class Provision extends AbstractOpenengsbMojo {

    /**
     * This setting should be done in the one of the assembly folders and have to point to the final directory where the
     * karaf system, etc configs and so on consist.
     * 
     * @parameter expression="${provisionPathUnix}"
     */
    private String provisionPathUnix;

    /**
     * This setting should be done in the one of the assembly folders and have to point to the final directory where the
     * karaf system, etc configs and so on consist.
     * 
     * @parameter expression="${provisionPathWindows}"
     */
    private String provisionPathWindows;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (provisionPathWindows != null && provisionPathUnix != null) {
            CommandLineBuilder command = new CommandLineBuilder();
            Map<String, String> environment = new HashMap<String, String>();
            environment.put("KARAF_DEBUG", "true");
            if (System.getProperty("os.name").startsWith("Windows")) {
                command.append(provisionPathWindows);
                environment.put("JAVA_OPTS", "-Djline.terminal=jline.UnsupportedTerminal");
            } else {
                command.append(provisionPathUnix);
            }
            try {
                new OpenEngSBJavaRunner(command, environment).exec();
            } catch (PlatformException e) {
                throw new MojoFailureException(e, e.getMessage(), e.getStackTrace().toString());
            }
        }
    }

}