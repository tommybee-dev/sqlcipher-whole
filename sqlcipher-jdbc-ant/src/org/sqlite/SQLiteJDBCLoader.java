/*--------------------------------------------------------------------------
 *  Copyright 2007 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// SQLite JDBC Project
//
// SQLite.java
// Since: 2007/05/10
//
// $URL$ 
// $Author$
//--------------------------------------
package org.sqlite;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.sqlite.util.OSInfo;

/**
 * Set the system properties, org.sqlite.lib.path, org.sqlite.lib.name,
 * appropriately so that the SQLite JDBC driver can find *.dll, *.jnilib and
 * *.so files, according to the current OS (win, linux, mac).
 * 
 * The library files are automatically extracted from this project's package
 * (JAR).
 * 
 * usage: call {@link #initialize()} before using SQLite JDBC driver.
 * 
 * @author leo
 * 
 */
public class SQLiteJDBCLoader
{

    private static boolean extracted = false;

    /**
     * Loads SQLite native JDBC library.
     * @return True if SQLite native library is successfully loaded; false otherwise.
     */
    public static boolean initialize() throws Exception {
        loadSQLiteNativeLibrary();
        return extracted;
    }

    /**
     * @return True if the SQLite JDBC driver is set to pure Java mode; false otherwise.
     * @deprecated Pure Java no longer supported 
     */
    static boolean getPureJavaFlag() {
        return Boolean.parseBoolean(System.getProperty("sqlite.purejava", "false"));
    }

    /**
     * Checks if the SQLite JDBC driver is set to pure Java mode. 
     * @return True if the SQLite JDBC driver is set to pure Java mode; false otherwise.
     * @deprecated Pure Java nolonger supported
     */
    public static boolean isPureJavaMode() {
        return false;
    }

    /**
     * Checks if the SQLite JDBC driver is set to native mode. 
     * @return True if the SQLite JDBC driver is set to native Java mode; false otherwise.
     */
    public static boolean isNativeMode() throws Exception {
        // load the driver
        initialize();
        return extracted;
    }

    /**
     * Computes the MD5 value of the input stream.
     * @param input InputStream.
     * @return Encrypted string for the InputStream.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    static String md5sum(InputStream input) throws IOException {
        BufferedInputStream in = new BufferedInputStream(input);

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            DigestInputStream digestInputStream = new DigestInputStream(in, digest);
            for (; digestInputStream.read() >= 0;) {

            }
            ByteArrayOutputStream md5out = new ByteArrayOutputStream();
            md5out.write(digest.digest());
            return md5out.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm is not available: " + e);
        }
        finally {
            in.close();
        }
    }

    /**
     * Extracts and loads the specified library file to the target folder
     * @param libFolderForCurrentOS Library path.
     * @param libraryFileName Library name.
     * @param targetFolder Target folder.
     * @return
     */
    private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName,
            String targetFolder) {
        String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;
        // Include architecture name in temporary filename in order to avoid conflicts
        // when multiple JVMs with different architectures running at the same time
        final String prefix = "sqlite-" + getVersion() + "-" + OSInfo.getArchName() + "-";

        String extractedLibFileName = prefix + libraryFileName;
        File extractedLibFile = new File(targetFolder, extractedLibFileName);

        try {
            if (extractedLibFile.exists()) {
                // test md5sum value
                String md5sum1 = md5sum(SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath));
                String md5sum2 = md5sum(new FileInputStream(extractedLibFile));

                if (md5sum1.equals(md5sum2)) {
                    return loadNativeLibrary(targetFolder, extractedLibFileName);
                }
                else {
                    // remove old native library file
                    boolean deletionSucceeded = extractedLibFile.delete();
                    if (!deletionSucceeded) {
                        throw new IOException("failed to remove existing native library file: "
                                + extractedLibFile.getAbsolutePath());
                    }
                }
            }

            // extract file into the current directory
            InputStream reader = SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath);
            FileOutputStream writer = new FileOutputStream(extractedLibFile);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }

            writer.close();
            reader.close();

            if (!System.getProperty("os.name").contains("Windows")) {
                try {
                    Runtime.getRuntime().exec(new String[] { "chmod", "755", extractedLibFile.getAbsolutePath() })
                            .waitFor();
                }
                catch (Throwable e) {}
            }

            return loadNativeLibrary(targetFolder, extractedLibFileName);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    /**
     * Loads native library using the given path and name of the library.
     * @param path Path of the native library.
     * @param name Name  of the native library.
     * @return True for successfully loading; false otherwise.
     */
    private static synchronized boolean loadNativeLibrary(String path, String name) {
        File libPath = new File(path, name);
        
        //System.err.println("lib path ... " + libPath.exists());
        
        if (libPath.exists()) {

            try {
                System.load(new File(path, name).getAbsolutePath());
                return true;
            }
            catch (UnsatisfiedLinkError e) {
                System.err.println(e);
                return false;
            }

        }
        else
            return false;
    }

    /**
     * Loads SQLite native library using given path and name of the library.
     * @exception
     */
    private static void loadSQLiteNativeLibrary() throws Exception {
        if (extracted)
            return;

        // Try loading library from org.sqlite.lib.path library path */
        String sqliteNativeLibraryPath = System.getProperty("org.sqlite.lib.path");
        String sqliteNativeLibraryName = System.getProperty("org.sqlite.lib.name");
        
        /*
        String libpath = System.getProperty("java.library.path");
        
        String[] libs = libpath.split(File.pathSeparator);
        boolean found = false;
        
        for(int i = 0; i < libs.length; i++)
        {
        	if(libs[i].equals(sqliteNativeLibraryPath))
        	{
        		found = true;
        		break;
        	}
        }
        
        if(!found) 
        	System.setProperty("java.library.path", "." + File.pathSeparator + sqliteNativeLibraryPath);
        */
        //System.setProperty("java.library.path", "." + File.pathSeparator + "C:/DEV/COMP/msys32/home/tobee/sqlcipher-build/sqlcipher-jdbc-ant/native");
        
        //System.out.println(sqliteNativeLibraryPath+"======"+sqliteNativeLibraryName);
        
        //try {
        	//System.loadLibrary("sqlitejdbc");
        //	System.load(sqliteNativeLibraryPath+File.separator+"sqlitejdbc.dll");
        //    extracted = true;
        //    return;
        //}
        //catch (UnsatisfiedLinkError e) {
        //	System.err.println(">>>>>>>" + System.getProperty("java.library.path"));
         //   System.err.println(e);
        //}
        
        if (sqliteNativeLibraryName == null) {
            sqliteNativeLibraryName = System.mapLibraryName("sqlitejdbc");
            if (sqliteNativeLibraryName != null && sqliteNativeLibraryName.endsWith("dylib")) {
            	sqliteNativeLibraryName = sqliteNativeLibraryName.replace("dylib", "jnilib");
            }
        }

        if (sqliteNativeLibraryPath != null) {
            if (loadNativeLibrary(sqliteNativeLibraryPath, sqliteNativeLibraryName)) {
                extracted = true;
                return;
            }
        }

        // Load the os-dependent library from the jar file
        sqliteNativeLibraryPath = "/org/sqlite/native/" + OSInfo.getNativeLibFolderPathForCurrentOS();

        if (SQLiteJDBCLoader.class.getResource(sqliteNativeLibraryPath + "/" + sqliteNativeLibraryName) == null) {
            extracted = false;
            throw new Exception("Error loading native library: " + sqliteNativeLibraryPath + "/" + sqliteNativeLibraryName);
        }

        // temporary library folder
        String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        // Try extracting the library from jar 
        if (extractAndLoadLibraryFile(sqliteNativeLibraryPath, sqliteNativeLibraryName, tempFolder)) {
            extracted = true;
            return;
        }

        extracted = false;
        return;
    }

    @SuppressWarnings("unused")
    private static void getNativeLibraryFolderForTheCurrentOS() {
        String osName = OSInfo.getOSName();
        String archName = OSInfo.getArchName();
    }

    /**
     * @return The major version of the SQLite JDBC driver.
     */
    public static int getMajorVersion() {
        String[] c = getVersion().split( "\\." );
        if( c.length == 0 ) {
            return 0;
        }
        try {
            return Integer.parseInt( c[0] );
        } catch( NumberFormatException ex ) {
            return 1;
        }
    }

    /**
     * @return The minor version of the SQLite JDBC driver.
     */
    public static int getMinorVersion() {
        String[] c = getVersion().split("\\.");
        return (c.length > 1) ? Integer.parseInt(c[1]) : 0;
    }

    /**
     * @return The version of the SQLite JDBC driver.
     */
    public static String getVersion() {

        URL versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc4/pom.properties");
        if (versionFile == null)
            versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc4/VERSION");

        String version = "unknown";
        try {
            if (versionFile != null) {
                Properties versionData = new Properties();
                versionData.load(versionFile.openStream());
                version = versionData.getProperty("version", version);
                version = version.trim().replaceAll("[^0-9\\.]", "");
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        return version;
    }

}
