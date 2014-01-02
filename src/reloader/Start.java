package reloader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class Start {

    static Class<?> clazz;
    static URL url;
    static boolean propagate = false;

    /**
     * demo: tries to reload class {@link reloader.A} undefinitely every second
     * 
     * @param args 
     */
    public static void main(String[] args) {
        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        while (true) {
            try {
                Reloader r = new Reloader(cl);
                
                if (propagate) 
                    Thread.currentThread().setContextClassLoader(r);
                
                Class<?> clazz = r.loadClass("reloader.A");
                clazz.newInstance();

                Thread.sleep(1000);

            } catch (Exception e) {
                System.out.println(e);
            }
         

        }
    }

    /**
     * adapted from http://stackoverflow.com/a/3971771/7849
     */
    static class Reloader extends ClassLoader {

        ClassLoader orig;

        Reloader(ClassLoader orig) {
            this.orig = orig;
        }

        @Override
        public Class<?> loadClass(String s) {
            return findClass(s);
        }

        @Override
        public Class<?> findClass(String s) {
            try {
                byte[] bytes = loadClassData(s);
                return defineClass(s, bytes, 0, bytes.length);
            } catch (IOException ioe) {
                try {
                    return super.loadClass(s);
                } catch (ClassNotFoundException ignore) {
                    ignore.printStackTrace(System.out);
                }
                ioe.printStackTrace(System.out);
                return null;
            }
        }

        private byte[] loadClassData(String className) throws IOException {
            try {
                /*
                 * get the actual path using the original classloader 
                 */
                Class<?> clazz = orig.loadClass(className);
                url = clazz.getResource(clazz.getSimpleName() + ".class");

                /*
                 * force reload
                 */
                File f = new File(url.toURI());
                int size = (int) f.length();
                byte buff[] = new byte[size];
                FileInputStream fis = new FileInputStream(f);
                DataInputStream dis = new DataInputStream(fis);
                dis.readFully(buff);
                dis.close();
                return buff;
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }
    }

}
