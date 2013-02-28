package no.tornado.template;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser implements Serializable {

    enum TRANSFORMATIONS { SIZE, FORMAT}
    enum STRING_TRANSFORMATIONS { CAMELCASE, METHOD_NAME, UPPERCASE, LOWERCASE};
    public static final String PATH_SEPARATOR = "\\.(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*?)}");
    public static final Pattern ITERATE_PATTERN = Pattern.compile("\\[#list (.*?) as (.*?)](.*?)\\[/#list( separator='(.*?)')?]", Pattern.DOTALL);
    public static final Pattern ARRAY_PATTERN = Pattern.compile(".*\\[(\\d*)\\]$");
    public static final Pattern TRANSFORM_PATTERN = Pattern.compile(".*\\?(.*)$");
    public static final Pattern TRANSFORM_ARG_PATTERN = Pattern.compile(".*\\((.*)\\)$");
    public static final Pattern DEFAULT_PATTERN = Pattern.compile(".*!(.*)$");

    private Template template;

    public Parser(Template template) {
        this.template = template;
    }

    public String render() throws TemplateException {
        // Iterate over loop expressions
        Matcher matcher = ITERATE_PATTERN.matcher(template.getSource());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            System.out.println(
            matcher.groupCount());
            
            String listProperty = matcher.group(1);
            String listVariable = matcher.group(2);
            String content = matcher.group(3);
            String separator = matcher.group(5);
            System.out.println("separator " + separator);
            // Extract content in iterable and parse
            matcher.appendReplacement(sb, expandIterator(listProperty, listVariable, content, separator));
        }
        matcher.appendTail(sb);

        // Parse the whole source again to expand variables outside of loops
        return renderPart(sb.toString());
    }

    /**
     * Parse content in each iterator loop
     * @param listProperty The list property to lookup
     * @param listVariable The variable name for each entry in the list
     * @param content The content between the [#list] tags [/#list]
     * @return An expanded string with parsed content
     * @throws TemplateException If object without default value is encountered
     */
    private String expandIterator(String listProperty, String listVariable, String content, String separator) throws TemplateException {
        StringBuilder sb = new StringBuilder();
        List list;
        separator = separator == null ? "" : separator;

        Object listObject = lookupContext(listProperty);
        if (listObject == null)
            throw new TemplateException("Trying to iterate over unexisting property " + listProperty, template);

        if (listObject instanceof List)
            list = (List) listObject;
        else if (listObject instanceof Object[]) {
            list = new ArrayList();
            list.addAll(Arrays.asList((Object[])listObject));
        } else if(listObject instanceof Collection){
            list = new ArrayList();
            list.addAll((Collection) listObject);
        }else
            throw new TemplateException("Trying to iterate over property " + listProperty + " which is not a list or array", template);
        int length = list.size() ;
        String sep = "";
        for (int i = 0; i < length; i++) {
            Object object = list.get(i);
            Object existingVariable = template.getContext().get(object);
            template.getContext().put(listVariable, object);
            sb.append(sep);
            sb.append(renderPart(content).replaceAll("^\n", ""));
            sep = separator;
            template.getContext().put(listVariable, existingVariable);
        }
        return sb.toString();
    }

    private String renderPart(String partSource) throws TemplateException {
        Matcher matcher = VARIABLE_PATTERN.matcher(partSource);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(sb, 
                    matcher.quoteReplacement(convert(lookupContext(matcher.group(1)))));
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Object lookupContext(String pathExpression) throws TemplateException {
        String[] path = pathExpression.split(PATH_SEPARATOR);
        Object resolved = findPropertyInObject(template.getContext(), path[0]);

        StringBuilder resolvePath = new StringBuilder();

        for (int i = 1; i < path.length; i++) {
            String pathEntry = path[i];
            if (resolvePath.length() > 0)
                resolvePath.append(PATH_SEPARATOR);

            resolvePath.append(pathEntry);
            resolved = findPropertyInObject(resolved, pathEntry);
        }

        return resolved;
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    private Object findPropertyInObject(Object object, String property) throws TemplateException {
        // Extract array/list expression and rewrite property expression if found
        Matcher arrayMatcher = ARRAY_PATTERN.matcher(property);
        Integer arrayIndex = null;
        if (arrayMatcher.matches()) {
            arrayIndex = Integer.valueOf(arrayMatcher.group(1));
            property = property.replaceAll("\\[" + arrayIndex + "\\]$", "");
        }

        // Look for default value expression and rewrite property expression if found
        String defaultExpression = null;
        Matcher defaultMatcher = DEFAULT_PATTERN.matcher(property);
        if (defaultMatcher.matches()) {
            defaultExpression = defaultMatcher.group(1);
            property = property.replaceAll("!.*$", "");
        }

        // Look for transformations
        String transformExpression = null;
        Matcher transformMatcher = TRANSFORM_PATTERN.matcher(property);
        if (transformMatcher.matches()) {
            transformExpression = transformMatcher.group(1);
            property = property.replaceAll("\\?.*$", "");
        }

        Object value;

        // Lookup in Map
        if (object instanceof Map) {
            value = ((Map) object).get(property);
            if (value != null)
                return postProcessValue(property, arrayIndex, transformExpression, value);
        }

        // Lookup getter
        try {
            Method getter = object.getClass().getDeclaredMethod(convertPropertyToGetter(property));
            value = getter.invoke(object);
            if (value != null)
                return postProcessValue(property, arrayIndex, transformExpression, value);
        } catch (Exception noGetter) {
        }

        // Lookup public field
        try {
            Field field = object.getClass().getDeclaredField(property);
            value = field.get(object);
            if (value != null)
                return postProcessValue(property, arrayIndex, transformExpression, value);
        } catch (Exception noProperty) {
        }

        // Not found, use default expression if any
        if (defaultExpression != null)
            return expandDefaultExpression(defaultExpression);

        // Nothing, throw exception
        String description = template.getContext().equals(object) ? "context" : object.toString();
        throw new TemplateException("Property " + property + " not getter or public field in " + description, template);
    }

    private Object expandDefaultExpression(String defaultExpression) throws TemplateException {
        // If surrounded by double quotes or single ticks, output string literal
        if (defaultExpression.startsWith("\"") && defaultExpression.endsWith("\"")
          || defaultExpression.startsWith("'") && defaultExpression.endsWith("'"))
            return stripQuotes(defaultExpression);

        // Assume expression, lookup in context
        return lookupContext(defaultExpression);
    }

    private String stripQuotes(String defaultExpression) {
        return defaultExpression.substring(1, defaultExpression.length() - 1);
    }

    private Object postProcessValue(String property, Integer arrayIndex, String transformExpression, Object object) throws TemplateException {
        if (arrayIndex == null)
            return transform(transformExpression, object);

        if (object instanceof List)
            return transform(transformExpression, ((List) object).get(arrayIndex));

        if (object instanceof Object[])
            return transform(transformExpression, ((Object[]) object)[arrayIndex]);

        throw new TemplateException("Property " + property + " has array reference but is not List or Array", template);
    }

    private Object transform(String transformExpression, Object object) throws TemplateException {
        // Return object if no transform rules are applied
        if (transformExpression == null)
            return object;

        // Look for modifiers to transform
        Matcher argMatcher = TRANSFORM_ARG_PATTERN.matcher(transformExpression);
        String args = null;
        if (argMatcher.matches()) {
            args = stripQuotes(argMatcher.group(1));
            transformExpression = transformExpression.replace("(" + argMatcher.group(1) + ")", "");
        }

        switch (TRANSFORMATIONS.valueOf(transformExpression.toUpperCase())) {
            case SIZE:
                if (object instanceof Object[])
                    return ((Object[]) object).length;
                if (object instanceof List)
                    return ((List) object).size();
                if (object instanceof Map)
                    return ((Map) object).size();
                if (object instanceof String)
                    return ((String) object).length();

                throw new TemplateException("Don't know how to return size for object of class " + object.getClass(), template);

            case FORMAT:
                if (args == null)
                    throw new TemplateException("No arguments given to format transformation:" + transformExpression, template);
                if (object instanceof Date || object instanceof java.sql.Date || object instanceof java.sql.Time)
                    return new SimpleDateFormat(args).format(object);
                if (object instanceof String )
                    return this.formatString( (String) object,  args);
            default: throw new TemplateException("Unknown transform encountered: ?" + transformExpression, template);
        }
    }

    private Object formatString(String content, String transformExpression) throws TemplateException {
        STRING_TRANSFORMATIONS transform = STRING_TRANSFORMATIONS.valueOf(transformExpression.toUpperCase());
        
        switch(transform) {
            case CAMELCASE:
                StringBuilder sb = new StringBuilder();
                String[] trunks = content.split("_");
                for(String trunk : trunks) {
                    sb.append(String.valueOf(trunk.charAt(0)).toUpperCase());
                    sb.append(trunk.substring(1).toLowerCase());
                }
                return sb.toString();
            case LOWERCASE:
                return content.toLowerCase();
            case UPPERCASE:
                return content.toUpperCase();
            case METHOD_NAME:   
                
            default: throw new TemplateException("Unknown transform encountered: ?" + transformExpression, template);
        }
        
    }

    private String convertPropertyToGetter(String property) {
        return "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    private String convert(Object object) {
        return object.toString();
    }
}
