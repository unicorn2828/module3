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
import static com.epam.esm.manager.ManagerData.*;

@Slf4j
@Component
public class CamelStringManager {

    public String toCamelString(String in) {
        final ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(PATTERN_COMPILE)));
        final Set<BeanDefinition> classes = provider.findCandidateComponents(SCAN_PACKAGE);
        if (in.isEmpty() || in.isBlank()) {
            in = DEFAULT;
        } else if (in.equals(MINUS)) {
            in = ID;
        }
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
                if (String.valueOf(camelString.charAt(0)).equals(MINUS)) {
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
