package com.epam.esm.manager;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.regex.Pattern;

import static com.epam.esm.exception.ServiceExceptionCode.UNKNOWN_PARAMETER;

@Slf4j
@Component
public class CamelStringManager {
    private static final String SCAN_PACKAGE = "com.epam.esm.dto";
    private static final String PATTERN_COMPILE = ".*";
    private static final String SORT_BY = "sortBy";
    private static final String SORT_BY_LOWER = "sortby";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_NUMBER_LOWER_CASE = "pagenumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_SIZE_LOWER_CASE = "pagesize";

    public String toCamelString(String in) {
        final ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(PATTERN_COMPILE)));
        final Set<BeanDefinition> classes = provider.findCandidateComponents(SCAN_PACKAGE);
        String camelString = in.toLowerCase().trim();
        for (BeanDefinition bean : classes) {
            Class<?> clazz;
            try {
                clazz = Class.forName(bean.getBeanClassName());
            } catch (ClassNotFoundException e) {
                ServiceExceptionCode exception = UNKNOWN_PARAMETER;
                log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
                throw new ServiceException(exception);
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.toString();
                int lastDotIndex = fieldName.lastIndexOf('.');
                fieldName = fieldName.substring(lastDotIndex + 1);
                if (String.valueOf(camelString.charAt(0)).equals("-")) {
                    camelString = camelString.substring(1).trim();
                    if (fieldName.toLowerCase().equals(camelString)) {
                        camelString = fieldName;
                        camelString = "-" + camelString;
                    }
                } else if (fieldName.toLowerCase().equals(camelString)) {
                    camelString = fieldName;
                }
                camelString = checkDefaultField(camelString);
            }
        }
        return camelString;
    }

    private String checkDefaultField(String string) {
        switch (string) {
            case SORT_BY_LOWER:
                string = SORT_BY;
                break;
            case PAGE_NUMBER_LOWER_CASE:
                string = PAGE_NUMBER;
                break;
            case PAGE_SIZE_LOWER_CASE:
                string = PAGE_SIZE;
                break;
        }
        return string;
    }
}
