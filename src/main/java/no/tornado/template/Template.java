package no.tornado.template;

import java.io.*;
import java.net.URL;
import java.util.Map;

/**
 * @see <a href="http://blog.syse.no/2010/09/10/creating-a-simple-lightweight-template-engine/"> take a look </a>
 * @author kb5w
 *
 */
public class Template implements Serializable {
    private String source;
    private Map context;

    public Template(String source, Map context) {
        this.source = source;
        this.context = context;
    }

    public Template(InputStream source, Map context) throws IOException {
        this.source = readFromInputStream(source);
        this.context = context;
    }

    public Template(URL source, Map context) throws IOException {
        this.source = readFromInputStream(source.openStream());
        this.context = context;
    }

    public Template(File source, Map context) throws IOException {
        this.source = readFromInputStream(new FileInputStream(source));
        this.context = context;
    }

    private String readFromInputStream(InputStream in) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String read;
        while ((read = reader.readLine()) != null)
            s.append(read).append("\n");
        return s.toString();
    }

    public String render() throws TemplateException {
        return new Parser(this).render();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        return !(source != null ? !source.equals(template.source) : template.source != null);
    }

    public int hashCode() {
        return source != null ? source.hashCode() : 0;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map getContext() {
        return context;
    }

    public void setContext(Map context) {
        this.context = context;
    }
}
