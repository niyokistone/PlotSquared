/*
 *       _____  _       _    _____                                _
 *      |  __ \| |     | |  / ____|                              | |
 *      | |__) | | ___ | |_| (___   __ _ _   _  __ _ _ __ ___  __| |
 *      |  ___/| |/ _ \| __|\___ \ / _` | | | |/ _` | '__/ _ \/ _` |
 *      | |    | | (_) | |_ ____) | (_| | |_| | (_| | | |  __/ (_| |
 *      |_|    |_|\___/ \__|_____/ \__, |\__,_|\__,_|_|  \___|\__,_|
 *                                    | |
 *                                    |_|
 *            PlotSquared plot management system for Minecraft
 *                  Copyright (C) 2020 IntellectualSites
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.bukkit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaVersionCheck {

    private static final Logger logger = LoggerFactory.getLogger("P2/" + JavaVersionCheck.class.getSimpleName());
    private final static Pattern NUM_PATTERN = Pattern.compile("\\d+");

    private JavaVersionCheck() {
    }

    public static void checkJavaVersion() {
        final String javaVersion = System.getProperty("java.version");
        final int dotIndex = javaVersion.indexOf('.');

        if (javaVersion.startsWith("1.")) {
            JavaVersionCheck.notify(javaVersion, logger);
            return;
        }

        final int endIndex = dotIndex == -1 ? javaVersion.length() : dotIndex;
        final String version = javaVersion.substring(0, endIndex);

        final int javaVersionNumber;
        try {
            Matcher versionMatcher = NUM_PATTERN.matcher(version);
            if (!versionMatcher.find()) {
                JavaVersionCheck.notify(javaVersion, logger);
                return;
            }
            javaVersionNumber = Integer.parseInt(versionMatcher.group());
        } catch (final NumberFormatException e) {
            logger.error("Failed to determine Java version. Could not parse {}", version, e);
            JavaVersionCheck.notify(javaVersion, logger);
            return;
        }

        if (javaVersionNumber < 11) {
            JavaVersionCheck.notify(javaVersion, logger);
        }
        if (javaVersionNumber >= 11) {
            logger.error("************************************************************");
            logger.error("* PlotSquared uses Nashorn for the internal scripting engine.");
            logger.error("* Within Java 15, Nashorn has been removed from Java.");
            logger.error("* Until we add a suitable workaround, you should stick to Java 11");
            logger.error("* to use all features of PlotSquared.");
            logger.error("************************************************************");
        }
    }

    public static void notify(final String version, final Logger logger) {
        logger.error("************************************************************");
        logger.error("* WARNING - YOU ARE RUNNING AN OUTDATED VERSION OF JAVA.");
        logger.error("* PLOTSQUARED WILL STOP BEING COMPATIBLE WITH THIS VERSION OF");
        logger.error("* JAVA WHEN MINECRAFT 1.17 IS RELEASED.");
        logger.error("*");
        logger.error("* Please update the version of Java to 11.");
        logger.error("*");
        logger.error("* Current Java version: " + version);
        logger.error("************************************************************");
    }
}
