package no.tornado.template;

public class TemplateException extends Exception {
    private Template template;

    public TemplateException(String message, Template template) {
        this(message, template, null);
    }

    public TemplateException(String message, Template template, Throwable cause) {
        super(message, cause);
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

}
