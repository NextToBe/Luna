package me.zen.luna.ioc.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class ClassLoaderImpl implements ClassLoader {

    private static final String JAR_FILE   = "jar:file:";
    private static final String WSJAR_FILE = "wsjar:file:";

    @Override
    public Set<Class<?>> load(Scanner scanner) {
        Set<Class<?>> result = new HashSet<>();
        String packageName = scanner.getPackageName().replace('.', '/');
        Enumeration<URL> dirs;

        try {
            dirs = getClass().getClassLoader().getResources(packageName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    loadByPackage(result, url.getPath(), scanner.getPackageName(), scanner.getSuperClass(), scanner.isRecursive(), scanner.getAnnotation());
                } else if (protocol.equals("jar")) {
                    loadByJar(result, url, packageName, scanner.getSuperClass(), scanner.getAnnotation());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    private void loadByPackage(Set<Class<?>> result, final String packagePath, final String packageName, final Class<?> superClass,
                               final boolean recursive, final Class<? extends Annotation> annotation) throws ClassNotFoundException{
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("package not found: {}", packageName);
        }

        File[] files = filter(dir, recursive);
        //load all files
        if (files == null || files.length == 0) {
            return;
        }

        for(File file : files) {
            if (file.isDirectory()) {
                loadByPackage(result, file.getAbsolutePath(), packageName + "." + file.getName(), superClass, recursive, annotation);
            } else {
                // this file is a class file
                String clazzName = file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(packageName + "." + clazzName);
                doLoad(result, clazz, superClass, annotation);
            }
        }
    }


    private void loadByJar(Set<Class<?>> result, final URL url, final String packagePath, final Class<?> superClass, final Class<? extends Annotation> annotation) throws ClassNotFoundException {
       try {
           if (!url.toString().startsWith(JAR_FILE) && !url.toString().startsWith(WSJAR_FILE)) {
               return;
           }

           JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
           if (jarFile == null) {
               return;
           }

           Enumeration<JarEntry> entries = jarFile.entries();
           while (entries.hasMoreElements()) {
               JarEntry entry = entries.nextElement();
               String name = entry.getName();
               // if start with '/'
               if (name.charAt(0) == '/') {
                   name = name.substring(1);
               }

               // If the first half is the same as the defined package name
               if (!name.startsWith(packagePath)) {
                   continue;
               }


               if (!name.endsWith(".class") || entry.isDirectory()) {
                    continue;
               }

               String className = name.substring(0, name.lastIndexOf('.')).replaceAll("/", ".");
               Class<?> clazz = Class.forName(className);
               doLoad(result, clazz, superClass, annotation);
           }
       } catch (IOException e) {
           log.error(e.getMessage());
       }
    }




    private void doLoad(Set<Class<?>> result, Class<?> clazz, final Class<?> superClass,  final Class<? extends Annotation> annotation) {
        if (null != superClass && null != annotation) {
            if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(superClass) && clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
            return;
        }

        if (null != superClass) {
            if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(superClass)) {
                result.add(clazz);
            }
            return;
        }

        if (null != annotation) {
            if (clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
            return;
        }

        result.add(clazz);
    }

    /***
     * @param dir the path or filename
     * @param recursive
     * @return
     */
    private File[] filter(File dir, final boolean recursive) {
        // Custom filtering rules If you can loop (include subdirectories) or is the end of the file. Class (compiled java class file)
        return dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
    }
}
